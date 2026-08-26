package kr.co.bootpay.store.model.request.product;


import kr.co.bootpay.store.model.request.ListParams;

/**
 * 상품 목록 조회 파라미터 (GET /v1/products)
 *
 * <p>⚠️ 서버(v1/products_controller#index)가 실제로 읽는 것은
 * page / limit / keyword / category_id / ex_uid / sort <b>뿐</b>이다.
 * 아래 {@code type} / {@code periodType} / {@code sAt} / {@code eAt} / {@code categoryCode} 는
 * 전송되더라도 에러 없이 무시되고 전체 목록이 돌아온다 — 필터가 걸린 것으로 착각하지 말 것.</p>
 *
 * <p>{@code keyword} 는 26-08-26 서버 변경부터 적용된다. 그 이전 배포본에서는 무시된다.</p>
 */
public class ProductListParams extends ListParams {
//    public Integer corporateType; //1: 개인, 2: 기업

    /** 카테고리 ID 로 필터한다 (하위 카테고리 포함) */
    public String categoryId;
    /** 가맹점이 지정한 상품 외부 고유 ID (서버 #index 의 ex_uid) */
    public String exUid;
    /** 정렬 키 — position | created_at | -created_at | price | -price | -sold */
    public String sort;

    // ── 아래는 서버가 읽지 않는다 (하위호환 때문에 인자만 유지) ──
    /**
     * @deprecated 서버가 읽지 않는다. 서버의 상품 타입 필터는 문자열
     *             ({@code subscription} | {@code discount} | {@code normal})이라 이 숫자 필드와 값 체계가 다르다.
     */
    @Deprecated
    public Integer type;
    /** @deprecated 서버(v1/products_controller#index)가 읽지 않는다 */
    @Deprecated
    public String periodType;
    /** @deprecated 서버(v1/products_controller#index)가 읽지 않는다 */
    @Deprecated
    public String sAt;
    /** @deprecated 서버(v1/products_controller#index)가 읽지 않는다 */
    @Deprecated
    public String eAt;
    /** @deprecated 서버(v1/products_controller#index)가 읽지 않는다 */
    @Deprecated
    public String categoryCode;
}
