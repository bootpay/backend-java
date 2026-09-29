package kr.co.bootpay.store.model.request.inquiry;


/**
 * 1:1 문의 삭제 파라미터 (DELETE /v1/inquiries/{inquiry_id}) — 작성 회원 본인 또는 supervisor
 *
 * <p>달린 답변도 함께 삭제된다.</p>
 */
public class InquiryDeleteParams {
    public String inquiryId;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** true 면 운영자 모드 — Bootpay-Role 을 supervisor 로 보낸다 */
    public Boolean supervisor;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
