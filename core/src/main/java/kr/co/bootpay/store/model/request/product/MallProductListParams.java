package kr.co.bootpay.store.model.request.product;

/**
 * 상품 목록 조회 파라미터 (V1 Mall API)
 * GET /v1/products
 * page/limit 미지정시 각각 1 / 20 이 적용된다.
 * ⚠️ keyword 는 서버(v1/products_controller#index)가 읽지 않는다 — page/limit/category_id/ex_uid/sort 만 사용하며
 *    keyword 를 보내도 조용히 무시된다. 하위호환 때문에 인자는 남겨두되, 검색이 필요하면 서버 지원이 선행되어야 한다.
 */
public class MallProductListParams extends ProductListParams {
    public String categoryId;
    /** 가맹점이 지정한 상품 외부 고유 ID (서버 #index 의 ex_uid). */
    public String exUid;
    public String sort;
    /** 회원 JWT (Bootpay-User-JWT 헤더로 전송, body/query 에는 포함되지 않는다) */
    public transient String userJwt;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body/query 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
