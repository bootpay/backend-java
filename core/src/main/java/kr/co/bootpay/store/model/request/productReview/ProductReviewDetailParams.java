package kr.co.bootpay.store.model.request.productReview;


/**
 * 상품평 단건 조회 파라미터 (GET /v1/reviews/{product_review_id})
 *
 * <p>공개 상품평은 누구나, 숨김 상품평은 작성 회원 본인 또는 supervisor 만 본다.</p>
 */
public class ProductReviewDetailParams {
    public String productReviewId;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** true 면 운영자 모드 — Bootpay-Role 을 supervisor 로 보낸다 */
    public Boolean supervisor;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
