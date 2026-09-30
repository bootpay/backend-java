package kr.co.bootpay.store.model.request.productQna;


/**
 * 상품문의 삭제 파라미터 (DELETE /v1/product-qnas/{product_qna_id}) — 작성 회원 본인 · 비회원 비밀번호 또는 supervisor
 *
 * <p>{@code guestPassword} 는 URL 쿼리가 아닌 본문으로 보낸다 (서버는 둘 다 받는다).</p>
 */
public class ProductQnaDeleteParams {
    public String productQnaId;
    public String guestPassword;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** true 면 운영자 모드 — Bootpay-Role 을 supervisor 로 보낸다 */
    public Boolean supervisor;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
