package kr.co.bootpay.store.model.request.invoice;


import kr.co.bootpay.store.model.request.ListParams;

/**
 * 청구서 목록 조회 파라미터 (GET /v1/invoices)
 * 응답은 { list: [...], count: N } 구조다 ({ items, total } 아님).
 * page/limit 미지정시 각각 1 / 24 (서버 기본값) 가 적용된다.
 * ListParams 를 상속해 list(ListParams) 오버로드와의 모호성 없이 더 구체적인 타입으로 선택된다.
 */
public class InvoiceListParams extends ListParams {
    public Integer type;
    public String csType;
    public String userId;
    public Integer productType;
    public String cssAt; //검색 시작일
    public String cseAt; //검색 종료일
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, query 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
