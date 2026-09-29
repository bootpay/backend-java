package kr.co.bootpay.store.model.request.productQna;


/**
 * 상품문의 수정 파라미터 (PUT /v1/product-qnas/{product_qna_id}) — 작성 회원 본인 또는 비회원 비밀번호, 답변 전만
 *
 * <p>답변이 달린 문의는 409 PRODUCT_QNA_ALREADY_ANSWERED, 비회원 비밀번호가 틀리면
 * 403 PRODUCT_QNA_GUEST_PASSWORD_INVALID.</p>
 */
public class ProductQnaUpdateParams {
    public String productQnaId;
    public String title;
    public String content;
    public Boolean isSecret;
    public String guestPassword;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
