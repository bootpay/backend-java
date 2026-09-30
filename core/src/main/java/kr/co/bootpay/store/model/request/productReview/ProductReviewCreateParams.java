package kr.co.bootpay.store.model.request.productReview;


import java.util.List;

/**
 * 상품평 작성 파라미터 (POST /v1/reviews) — 회원 전용
 *
 * <p>{@code orderId} 는 회원의 구매확정 주문, {@code productId}(·{@code productOptionId})는 그 주문에 담긴
 * 상품이어야 한다. {@code images} 는 사진 URL 최대 5개(문자열 또는 { url: ... } 형태)이며 파일 업로드는 지원하지 않는다.</p>
 */
public class ProductReviewCreateParams {
    public String orderId;
    public String productId;
    public Integer rating;
    public String content;
    public String productOptionId;
    /** 사진 URL 최대 5개 — 문자열 또는 { url: ... } 형태를 모두 받는다 */
    public List<Object> images;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
