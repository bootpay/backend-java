package kr.co.bootpay.store.model.request.productQna;


/**
 * 상품문의 작성 파라미터 (POST /v1/product-qnas)
 *
 * <p>회원({@code userId} · {@code loginId} · {@code userJwt}) 또는 비회원({@code guestName} +
 * {@code guestPassword}, 몰이 비회원 작성을 허용할 때)으로 쓴다. 회원 문의면 {@code guest*} 는 무시된다.</p>
 */
public class ProductQnaCreateParams {
    public String productId;
    public String content;
    public String title;
    public String productOptionId;
    public String optionText;
    public Boolean isSecret;
    public String guestName;
    public String guestPassword;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
