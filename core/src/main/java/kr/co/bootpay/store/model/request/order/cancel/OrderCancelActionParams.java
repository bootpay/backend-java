package kr.co.bootpay.store.model.request.order.cancel;

public class OrderCancelActionParams {
    /** 취소 요청 이력 ID — 정식 이름. 구 이름 orderCancelRequestHistoryId 도 계속 동작한다. */
    public String orderCancellationRequestId;
    /** @deprecated 정식 이름은 {@link #orderCancellationRequestId} — 하위호환을 위해 유지 */
    public String orderCancelRequestHistoryId;
    public String message; // 승인/거절 메시지
    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
