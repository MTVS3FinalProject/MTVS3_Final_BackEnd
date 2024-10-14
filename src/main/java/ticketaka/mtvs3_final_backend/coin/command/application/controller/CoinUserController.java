package ticketaka.mtvs3_final_backend.coin.command.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketaka.mtvs3_final_backend._core.utils.ApiUtils;

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
    public ResponseEntity<?> purchase() {

        coinUserService.purchase();

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
