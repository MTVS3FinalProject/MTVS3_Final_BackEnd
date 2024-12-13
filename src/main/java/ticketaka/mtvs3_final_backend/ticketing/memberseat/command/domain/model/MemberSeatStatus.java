package ticketaka.mtvs3_final_backend.ticketing.memberseat.command.domain.model;

public enum MemberSeatStatus {
    // 접수 중, 예약 대기, 예약 미루기, 예약, 추첨 탈락, 예약 취소
    RECEIVED, WAITING_RESERVE, POSTPONE, RESERVED, FAILED, CANCEL_RESERVE
}
