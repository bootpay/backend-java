package kr.co.bootpay.store.model.request.order;

import java.util.List;

public class OrderPurchasesUpdateParams {
    /** 주문 번호 — 경로에만 쓰이며 body 에는 포함되지 않는다 */
    public transient String orderNumber;
    /** 갱신할 발주 목록 (최대 50건) */
    public List<OrderPurchaseUpdateItem> purchases;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
