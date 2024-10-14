package ticketaka.mtvs3_final_backend.coin.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.coin.command.application.dto.CoinUserRequestDTO;
import ticketaka.mtvs3_final_backend.coin.command.application.service.CoinUserService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coin")
public class CoinUserController {

    private final CoinUserService coinUserService;

    /*
        Coin 구매
     */
    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestBody CoinUserRequestDTO.purchaseDTO requestDTO) {

        coinUserService.purchase(requestDTO, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
