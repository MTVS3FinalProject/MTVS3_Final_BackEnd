package ticketaka.mtvs3_final_backend.member.command.infrastructure.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ticketaka.mtvs3_final_backend.member.command.domain.service.COOLSMSService;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@Service
public class COOLSMSServiceImpl implements COOLSMSService {

    @Value("${COOLSMS.ACCESS.KEY}")
    private String accessKey;
    @Value("${COOLSMS.SECRET.KEY}")
    private String secretKey;
    @Value("${COOLSMS.FROM.NUMBER}")
    private String fromNumber;

    private DefaultMessageService defaultMessageService;

    @PostConstruct
    private void init() {
        this.defaultMessageService = NurigoApp.INSTANCE.initialize(accessKey, secretKey, "https://api.coolsms.co.kr");
    }

    @Override
    public void sendOne(String toNumber, String content) {

        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(toNumber);
        message.setText(content);

        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
        SingleMessageSentResponse response = defaultMessageService.sendOne(request);

        log.info("COOLSMSService.sendOne - response: {}", response);
    }
}
