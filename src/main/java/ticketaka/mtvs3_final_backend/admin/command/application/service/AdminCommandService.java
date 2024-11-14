package ticketaka.mtvs3_final_backend.admin.command.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception400;
import ticketaka.mtvs3_final_backend._core.error.exception.Exception401;
import ticketaka.mtvs3_final_backend.admin.command.application.dto.AdminCommandRequestDTO;
import ticketaka.mtvs3_final_backend.admin.command.domain.dto.KakaoFeignClientResponseDTO;
import ticketaka.mtvs3_final_backend.admin.command.domain.model.KakaoToken;
import ticketaka.mtvs3_final_backend.admin.command.domain.repository.KakaoTokenRepository;
import ticketaka.mtvs3_final_backend.file.command.application.service.FileCommandService;
import ticketaka.mtvs3_final_backend.sticker.command.domain.model.Sticker;
import ticketaka.mtvs3_final_backend.ticketing.concert.command.domain.model.Concert;
import ticketaka.mtvs3_final_backend.ticketing.concert.query.repositroy.ConcertQueryRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminCommandService {

    private final TitleAdminCommandService titleAdminCommandService;
    private final StickerAdminCommandService stickerAdminCommandService;
    private final FileCommandService fileCommandService;
    private final KakaoAdminService kakaoAdminService;

    private final ConcertQueryRepository concertQueryRepository;
    private final KakaoTokenRepository kakaoTokenRepository;

    /*
        Title 추가
     */
    @Transactional
    public void uploadTitle(Long concertId, AdminCommandRequestDTO.uploadTitleDTO requestDTO) {

        titleAdminCommandService.saveTitle(concertId, requestDTO);
    }

    /*
        Sticker 추가
     */
    @Transactional
    public void uploadSticker(Long concertId, AdminCommandRequestDTO.uploadStickerDTO requestDTO) {

        getConcert(concertId);

        Sticker sticker = stickerAdminCommandService.saveSticker(concertId, requestDTO);
        fileCommandService.saveStickerImage(sticker.getId(), requestDTO.stickerImage());
    }

    // Concert 조회
    private Concert getConcert(Long concertId) {
        return concertQueryRepository.findById(concertId)
                .orElseThrow(() -> new Exception400("해당 공연을 찾을 수 없습니다."));
    }

    /*
        Kakao Token 발급 및 저장
     */
    public void saveKakaoToken(String code) {

        // Kakao Token 발급
        KakaoFeignClientResponseDTO.KakaoTokenDTO responseDTO = kakaoAdminService.getKakaoToken(code);

        log.info("Kakao token: {}", responseDTO);

        // Kakao Token 저장
        kakaoAdminService.saveKakaoToken(responseDTO);
    }


    /*
        Kakao 친구 목록 조회
     */
    public KakaoFeignClientResponseDTO.KakaoFriendListDTO getKakaoFriendList() {

        KakaoToken kakaoToken = kakaoTokenRepository.findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new Exception401("저장된 Kakao Token 값이 없습니다."));

        KakaoFeignClientResponseDTO.KakaoFriendListDTO kakaoFriendListDTO = kakaoAdminService.getKakaoFriendList(kakaoToken);

        log.info("Kakao friend list: {}", kakaoFriendListDTO);

        return null;
    }

    /*
        Kakao 친구 메세지 전송
     */
    public void sendKakaoMessage(String userName) {

        KakaoToken kakaoToken = kakaoTokenRepository.findTopByOrderByCreatedAtDesc()
                .orElseThrow(() -> new Exception401("저장된 Kakao Token 값이 없습니다."));

        kakaoAdminService.sendKakaoMessage(kakaoToken, userName);
    }
}
