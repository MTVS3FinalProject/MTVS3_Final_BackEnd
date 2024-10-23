package ticketaka.mtvs3_final_backend.redis.drawing.domain;

public enum PaymentStatus {
    // 결제 준비, 배송지 입력 완료, 결제 완료, 결제 실패
    PENDING, IN_PROGRESS, COMPLETED, FAILED
}
