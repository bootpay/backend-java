package kr.co.bootpay.store.model.request.inquiry;


/**
 * 1:1 문의 작성 파라미터 (POST /v1/inquiries) — 회원 전용
 *
 * <p>{@code productId} 는 이 몰의 상품, {@code orderId} 는 작성 회원의 주문이어야 한다.
 * {@code option} 은 상품 옵션 문구로 {@code productId} 가 함께 필요하다.</p>
 */
public class InquiryCreateParams {
    public String content;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    public String title;
    public String productId;
    /** 상품 옵션 문구 ({@code productId} 필요) */
    public String option;
    public String orderId;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
