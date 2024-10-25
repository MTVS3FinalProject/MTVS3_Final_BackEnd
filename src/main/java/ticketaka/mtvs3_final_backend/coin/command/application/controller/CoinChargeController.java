package ticketaka.mtvs3_final_backend.coin.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;
import ticketaka.mtvs3_final_backend.coin.command.application.dto.CoinChargeRequestDTO;
import ticketaka.mtvs3_final_backend.coin.command.application.service.CoinChargeService;

import static ticketaka.mtvs3_final_backend._core.utils.SecurityUtils.getCurrentMemberId;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/coin")
public class CoinChargeController {

    private final CoinChargeService coinChargeService;

    /*
        Coin 구매
     */
    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(@RequestBody CoinChargeRequestDTO.purchaseDTO requestDTO) {

        coinChargeService.purchase(requestDTO, getCurrentMemberId());

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
