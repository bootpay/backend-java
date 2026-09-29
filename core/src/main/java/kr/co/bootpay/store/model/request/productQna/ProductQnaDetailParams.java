package kr.co.bootpay.store.model.request.productQna;


/**
 * 상품문의 단건 조회 파라미터 (GET /v1/product-qnas/{product_qna_id})
 *
 * <p>다른 사람의 비밀글은 403 PRODUCT_QNA_SECRET_FORBIDDEN. {@code supervisor} 면 비밀글·숨김 글도 본다.</p>
 */
public class ProductQnaDetailParams {
    public String productQnaId;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** true 면 운영자 모드 — Bootpay-Role 을 supervisor 로 보낸다 */
    public Boolean supervisor;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
