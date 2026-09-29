package kr.co.bootpay.store.model.request.productReview;


import java.util.List;

/**
 * 상품평 수정 파라미터 (PUT /v1/reviews/{product_review_id}) — 작성 회원 본인, 작성 후 7일 이내
 *
 * <p>{@code images} 는 보내면 통째로 교체한다 (빈 목록이면 모두 삭제). 7일이 지나면 REVIEW_EDIT_TIME_EXPIRED.</p>
 */
public class ProductReviewUpdateParams {
    public String productReviewId;
    public Integer rating;
    public String content;
    /** 보내면 통째로 교체한다 (빈 목록이면 모두 삭제) */
    public List<Object> images;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
