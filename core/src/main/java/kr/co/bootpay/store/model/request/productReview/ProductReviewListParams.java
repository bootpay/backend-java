package kr.co.bootpay.store.model.request.productReview;


/**
 * 상품평 목록 조회 파라미터 (GET /v1/reviews)
 *
 * <p>회원 모드는 {@code userId} · {@code loginId} · {@code userJwt} 로 정한 회원이 쓴 상품평만 본다.
 * {@code supervisor} 면 몰 전체 상품평을 보며, 이때 {@code userId} 는 작성 회원 필터(선택)다.</p>
 *
 * <p>상품 상세의 공개 상품평 목록은 별도 경로(GET /v1/products/{product_id}/reviews)를 쓴다.</p>
 */
public class ProductReviewListParams {
    /** 미지정시 1 */
    public Integer page;
    /** 미지정시 20 */
    public Integer limit;
    public String productId;
    /** 'all' 이면 숨김 상품평까지 조회한다 (운영자 모드에서만 의미가 있다) */
    public String view;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** true 면 운영자 모드 — Bootpay-Role 을 supervisor 로 보낸다 */
    public Boolean supervisor;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
