package kr.co.bootpay.store.model.request.invoice;


import kr.co.bootpay.store.model.request.ListParams;

// 청구서 목록 조회 파라미터 (GET invoices)
// 응답은 { list: [...], count: N } 구조이며 서버 기본 limit은 24다
public class InvoiceListParams extends ListParams {
    public Integer type;
    public String csType;
    public String userId;
    public Integer productType;
    public String cssAt; //검색 시작일
    public String cseAt; //검색 종료일
}
