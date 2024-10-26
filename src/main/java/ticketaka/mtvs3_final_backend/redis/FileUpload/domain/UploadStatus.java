package ticketaka.mtvs3_final_backend.redis.FileUpload.domain;

public enum UploadStatus {
    // 인증 대기, 업로드 완료, 인증 완료, 인증 실패
    PENDING, UPLOADED, SUCCESS, FAIL
}
