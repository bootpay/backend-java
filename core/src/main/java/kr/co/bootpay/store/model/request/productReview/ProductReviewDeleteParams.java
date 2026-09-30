package kr.co.bootpay.store.model.request.productReview;


/**
 * 상품평 삭제 파라미터 (DELETE /v1/reviews/{product_review_id}) — 작성 회원 본인 또는 supervisor
 *
 * <p>지급된 적립금 회수·상품 통계 차감이 함께 일어난다. {@code reason} 은 운영자 모드의 삭제 사유다.</p>
 */
public class ProductReviewDeleteParams {
    public String productReviewId;
    /** 운영자 모드의 삭제 사유 */
    public String reason;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** true 면 운영자 모드 — Bootpay-Role 을 supervisor 로 보낸다 */
    public Boolean supervisor;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
