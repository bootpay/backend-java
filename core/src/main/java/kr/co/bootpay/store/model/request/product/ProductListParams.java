package kr.co.bootpay.store.model.request.product;


import kr.co.bootpay.store.model.request.ListParams;

public class ProductListParams extends ListParams {
//    public Integer corporateType; //1: 개인, 2: 기업

    public Integer type;
    public String periodType;
    public String sAt;
    public String eAt;
    public String categoryCode;
}
