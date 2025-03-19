package ticketaka.mtvs3_final_backend.member.command.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.jwt.JWTTokenProvider;
import ticketaka.mtvs3_final_backend.file.command.application.service.FileCommandService;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.FilePurpose;
import ticketaka.mtvs3_final_backend.file.command.domain.model.property.RelationType;
import ticketaka.mtvs3_final_backend.member.command.application.dto.MemberAuthDTO;
import ticketaka.mtvs3_final_backend.member.command.application.dto.MemberAuthRequestDTO;
import ticketaka.mtvs3_final_backend.member.command.application.dto.MemberAuthResponseDTO;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.model.MemberInfo;
import ticketaka.mtvs3_final_backend.member.command.domain.model.MemberPwd;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Authority;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Status;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.FileUploadForAuth;
import ticketaka.mtvs3_final_backend.redis.FileUpload.domain.UploadStatus;
import ticketaka.mtvs3_final_backend.redis.FileUpload.repository.FileUploadForAuthRedisRepository;
import ticketaka.mtvs3_final_backend.redis.refreshtoken.domain.RefreshToken;
import ticketaka.mtvs3_final_backend.redis.refreshtoken.repository.RefreshTokenRedisRepository;
import ticketaka.mtvs3_final_backend.title.command.domain.model.Title;
import ticketaka.mtvs3_final_backend.title.query.service.TitleQueryService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberAuthService {

    private final FileCommandService fileCommandService;
    private final TitleQueryService titleQueryService;

    private final MemberRepository memberRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final FileUploadForAuthRedisRepository fileUploadForAuthRedisRepository;

    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /*
        기본 회원 가입
     */
    @Transactional
    public void signUp(MemberAuthRequestDTO.signUpDTO requestDTO) {

        // 이메일 중복 확인
        checkDuplicatedEmail(requestDTO.email());

        // 닉네임 중복 확인
        checkDuplicatedNickname(requestDTO.nickname());

        // imgUrl 확인
        MemberAuthDTO.FileUploadDTO fileUploadDTO = checkUploadedImg(requestDTO.email());

        // 회원 생성
        Member member = newMember(requestDTO, fileUploadDTO.secondPwd());

        // 회원 저장
        memberRepository.save(member);

        // File 객체 생성
        fileCommandService.newFile(RelationType.MEMBER, member.getId(), fileUploadDTO.imgUrl(), FilePurpose.SIGNUP);
    }

    // 이메일 중복 확인
    private void checkDuplicatedEmail(String email) {

        // 이메일 중복 확인
        memberRepository.findByMemberInfo_Email(email)
                .ifPresent(member -> { throw new Exception400("이미 가입된 이메일입니다."); });
    }

    // 닉네임 중복 확인
    private void checkDuplicatedNickname(String nickname) {

        Optional<Member> member = memberRepository.findByMemberInfo_Nickname(nickname);

        if(member.isPresent()) {
            throw new Exception400("이미 사용 중인 이름입니다.");
        }
    }

    // 비밀번호 확인
    private void checkValidPassword(String rawPassword, MemberPwd memberPwd) {

        if(!memberPwd.matchPassword(rawPassword, passwordEncoder)) {
            throw new Exception400("비밀번호가 유효하지 않습니다.");
        }
    }

    // 이미지 업로드 확인
    private MemberAuthDTO.FileUploadDTO checkUploadedImg(String email) {

        FileUploadForAuth fileUpload = fileUploadForAuthRedisRepository.findById(email)
                .orElseThrow(() -> new Exception400("이메일 기록을 찾을 수 없습니다."));

        if(!fileUpload.getUploadStatus().equals(UploadStatus.SUCCESS)) {
            throw new Exception400("이미지가 제대로 업로드 되지 않았습니다.");
        }

        String imgUrl = fileUpload.getImgUrl();
        String secondPwd = fileUpload.getCode();

        fileUploadForAuthRedisRepository.delete(fileUpload);

        return new MemberAuthDTO.FileUploadDTO(imgUrl, secondPwd);
    }

    // 회원 생성
    protected Member newMember(MemberAuthRequestDTO.signUpDTO requestDTO, String secondPwd) {

        String encodedPassword = passwordEncoder.encode(requestDTO.password());
        String encodedSecondPassword = passwordEncoder.encode(secondPwd);

        return Member.createMember(
                new MemberInfo(
                        requestDTO.nickname(),
                        requestDTO.email(),
                        getLocalDateBirth(requestDTO.birth())
                ),
                new MemberPwd(encodedPassword),
                new MemberPwd(encodedSecondPassword),
                requestDTO.avatarData(),
                Authority.FAN
        );
    }

    // 생일 포맷 변환
    private LocalDate getLocalDateBirth(String birth) {
        // 변환할 날짜 포맷 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        // String 을 LocalDate 로 변환
        return LocalDate.parse(birth, formatter);
    }

    /*
        기본 로그인
     */
    @Transactional
    public MemberAuthResponseDTO.loginDTO login(MemberAuthRequestDTO.authDTO requestDTO) {

        // 1. 이메일 확인
        Member member = memberRepository.findByMemberInfo_Email(requestDTO.email())
                .orElseThrow(() -> new Exception400("가입 되지 않은 이메일입니다."));

        // 2. 비밀번호 확인
        checkValidPassword(requestDTO.password(), member.getPassword());

        // 3. 회원 상태 확인
        if (!member.getStatus().equals(Status.ACTIVE)) {
            throw new Exception400(member.getStatus() + " 회원 입니다.");
        };

        MemberAuthResponseDTO.memberInfoDTO memberInfoDTO = getMemberInfo(member);
        MemberAuthResponseDTO.authTokenDTO authTokenDTO = getAuthToken(requestDTO.email(), requestDTO.password());

        return new MemberAuthResponseDTO.loginDTO(memberInfoDTO, authTokenDTO);
    }

    // 반환할 회원 정보 구성
    private MemberAuthResponseDTO.memberInfoDTO getMemberInfo(Member member) {

        Title title = titleQueryService.getMemberTitle(member.getId());

        return new MemberAuthResponseDTO.memberInfoDTO(
                member.getBIsHost(),
                member.getMemberInfo().getNickname(),
                member.getMemberInfo().getBirth().toString(),
                member.getCoin(),
                member.getAvatarData(),
                title != null ? title.getId().intValue() : -1,
                title != null ? title.getTitleName() : null,
                title != null ? title.getTitleRarity().toString() : null
        );
    }

    // 토큰 발급
    protected MemberAuthResponseDTO.authTokenDTO getAuthToken(String email, String password) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(email, password);
        AuthenticationManager manager = authenticationManagerBuilder.getObject();
        Authentication authentication = manager.authenticate(usernamePasswordAuthenticationToken);

        MemberAuthResponseDTO.authTokenDTO authTokenDTO = jwtTokenProvider.generateToken(authentication);

        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(authentication.getName())
                .authorities(authentication.getAuthorities())
                .refreshToken(authTokenDTO.refreshToken())
                .build()
        );

        return authTokenDTO;
    }

    /*
        토큰 재발급
     */
    @Transactional
    public MemberAuthResponseDTO.authTokenDTO reissueToken(HttpServletRequest httpServletRequest) {

        // Request Header 에서 JWT Token 추출
        String token = jwtTokenProvider.resolveToken(httpServletRequest);

        // 토큰 유효성 검사
        if(token == null || !jwtTokenProvider.validateToken(token)) {
            throw new Exception400("유효하지 않은 Access Token 입니다.");
        }

        // type 확인
        if(!jwtTokenProvider.isRefreshToken(token)) {
            throw new Exception400("유효하지 않은 Refresh Token 입니다.");
        }

        // RefreshToken
        RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(token);

        if(refreshToken == null) {
            throw new Exception400("Refresh Token 이 비어있습니다.");
        }

        // Redis 에 저장된 RefreshToken 정보를 기반으로 JWT Token 생성
        MemberAuthResponseDTO.authTokenDTO authTokenDTO = jwtTokenProvider.generateToken(
                refreshToken.getId(), refreshToken.getAuthorities()
        );

        // Redis 에 RefreshToken Update
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(refreshToken.getId())
                .authorities(refreshToken.getAuthorities())
                .refreshToken(authTokenDTO.refreshToken())
                .build());

        return authTokenDTO;
    }

    /*
        로그아웃
     */
    @Transactional
    public void logout(HttpServletRequest httpServletRequest) {

        log.info("로그아웃 - Refresh Token 확인");

        String token = jwtTokenProvider.resolveToken(httpServletRequest);

        if(token == null || !jwtTokenProvider.validateToken(token)) {
            throw new Exception400("유효하지 않은 Refresh Token 입니다.");
        }

        RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(token);
        refreshTokenRedisRepository.delete(refreshToken);
    }
}
