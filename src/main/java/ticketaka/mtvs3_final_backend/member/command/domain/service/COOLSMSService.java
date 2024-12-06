package ticketaka.mtvs3_final_backend.member.command.domain.service;

public interface COOLSMSService {

    // 단일 메세지 전송
    void sendOne(String toNumber, String content);
}
