package kr.co.bootpay.store.model.request.inquiry;


/**
 * 1:1 문의 단건 조회 파라미터 (GET /v1/inquiries/{inquiry_id})
 *
 * <p>회원 모드는 작성 회원 본인 문의만 본다 (다른 회원 문의는 404). {@code supervisor} 면 몰의 모든 문의.</p>
 */
public class InquiryDetailParams {
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
