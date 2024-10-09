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
import ticketaka.mtvs3_final_backend.member.command.application.dto.MemberAuthRequestDTO;
import ticketaka.mtvs3_final_backend.member.command.application.dto.MemberAuthResponseDTO;
import ticketaka.mtvs3_final_backend.member.command.domain.model.Member;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.AgeGroup;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Authority;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Gender;
import ticketaka.mtvs3_final_backend.member.command.domain.model.property.Status;
import ticketaka.mtvs3_final_backend.member.command.domain.repository.MemberRepository;
import ticketaka.mtvs3_final_backend.redis.domain.RefreshToken;
import ticketaka.mtvs3_final_backend.redis.repository.RefreshTokenRedisRepository;

import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberAuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

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

        // 비밀번호 확인
        checkValidPassword(requestDTO.password(), passwordEncoder.encode(requestDTO.confirmPassword()));

        // 회원 생성
        Member member = newMember(requestDTO);

        // 회원 저장
        memberRepository.save(member);
    }

    // 이메일 중복 확인
    private void checkDuplicatedEmail(String email) {

        Optional<Member> member = memberRepository.findByEmail(email);

        if(member.isPresent()) {
            throw new Exception400("이미 가입된 이메일입니다.");
        }
    }

    // 비밀번호 확인
    private void checkValidPassword(String rawPassword, String encodedPassword) {

        if(!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new Exception400("비밀번호가 유효하지 않습니다.");
        }
    }

    // 회원 생성
    protected Member newMember(MemberAuthRequestDTO.signUpDTO requestDTO) {
        return Member.builder()
                .nickname(requestDTO.nickName())
                .email(requestDTO.email())
                .password(passwordEncoder.encode(requestDTO.password()))
                .gender(Gender.fromString(requestDTO.gender()))
                .ageGroup(AgeGroup.fromString(requestDTO.age_range()))
                .authority(Authority.USER)
                .status(Status.ACTIVE)
                .build();
    }

    /*
        기본 로그인
     */
    public MemberAuthResponseDTO.authTokenDTO login(HttpServletRequest httpServletRequest, MemberAuthRequestDTO.authDTO requestDTO) {

        // 1. 이메일 확인
        Member member = memberRepository.findByEmail(requestDTO.email())
                .orElseThrow(() -> new Exception400("가입 되지 않은 이메일입니다."));

        // 2. 비밀번호 확인
        checkValidPassword(requestDTO.password(), member.getPassword());

        // 3. 회원 상태 확인
        if (!member.getStatus().equals(Status.ACTIVE)) {
            throw new Exception400(member.getStatus() + " 회원 입니다.");
        };

        return getAuthTokenDTO(requestDTO.email(), requestDTO.password(), httpServletRequest);
    }

    // 토큰 발급
    protected MemberAuthResponseDTO.authTokenDTO getAuthTokenDTO(String email, String password, HttpServletRequest httpServletRequest) {

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
}
