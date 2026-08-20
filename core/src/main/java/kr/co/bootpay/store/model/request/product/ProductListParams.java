package kr.co.bootpay.store.model.request.product;


import kr.co.bootpay.store.model.request.ListParams;

// 상품 목록 조회 파라미터 (GET products)
// ⚠️ ListParams의 keyword는 서버(v1/products_controller#index)가 읽지 않는다.
//    컨트롤러는 page/limit/category_id/ex_uid/sort 만 사용하므로 keyword를 보내도 조용히 무시된다.
//    하위호환 때문에 인자는 남겨두되, 검색이 필요하면 서버 지원 추가가 선행되어야 한다.
public class ProductListParams extends ListParams {
//    public Integer corporateType; //1: 개인, 2: 기업

    public Integer type;
    public String periodType;
    public String sAt;
    public String eAt;
    public String categoryCode;
}
