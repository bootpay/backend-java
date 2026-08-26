package kr.co.bootpay.store.model.request.product;

/**
 * 상품 목록 조회 파라미터 (V1 Mall API)
 * GET /v1/products
 * page/limit 미지정시 각각 1 / 20 이 적용된다.
 * ⚠️ 서버(v1/products_controller#index)가 읽는 것은 page/limit/keyword/category_id/ex_uid/sort 뿐이다.
 *    type/periodType/sAt/eAt/categoryCode 는 보내도 조용히 무시된다.
 *    keyword 는 26-08-26 서버 변경부터 적용된다 — 그 이전 배포본에서는 무시된다.
 *
 * <p>{@code categoryId} / {@code exUid} / {@code sort} 는 {@link ProductListParams} 에서 상속받는다.</p>
 */
public class MallProductListParams extends ProductListParams {
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송, body/query 에는 포함되지 않는다) */
    public transient String userJwt;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body/query 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
