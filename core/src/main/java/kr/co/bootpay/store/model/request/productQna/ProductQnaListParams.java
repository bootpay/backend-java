package kr.co.bootpay.store.model.request.productQna;


/**
 * 상품문의 목록 조회 파라미터 (GET /v1/product-qnas)
 *
 * <p>고객 모드는 {@code productId} 가 필수다. 다른 사람의 비밀글은 {@code is_viewable: false} 로 본문 없이 온다 —
 * 회원({@code userId} · {@code loginId} · {@code userJwt})을 보내면 그 회원이 쓴 비밀글 본문이 보인다.
 * {@code supervisor} + {@code view: "all"} 이면 몰 전체(숨김 포함)를 보며, 이때 {@code productId} 는 선택 필터다.</p>
 */
public class ProductQnaListParams {
    /** 고객 모드에서는 필수, 운영자 모드에서는 선택 필터 */
    public String productId;
    /** 'all' 이면 숨김 글까지 조회한다 (운영자 모드에서만 의미가 있다) */
    public String view;
    /** 미지정시 1 */
    public Integer page;
    /** 미지정시 20 */
    public Integer limit;
    public String userId;
    public String loginId;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송) */
    public String userJwt;
    /** true 면 운영자 모드 — Bootpay-Role 을 supervisor 로 보낸다 */
    public Boolean supervisor;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
