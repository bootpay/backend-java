package kr.co.bootpay.store.model.request.order.cancel;

public class OrderCancelActionParams {
    // 서버(v1/order/cancel_controller)는 approve/reject/withdraw 셋 다 params[:id]를
    // order_cancellation_request_id로 동일하게 취급한다. reject와 이름이 달라 다른 값처럼 보였던
    // 문제를 orderCancellationRequestId로 맞춘다. 구 이름(orderCancelRequestHistoryId)도 계속 받는다.
    public String orderCancellationRequestId;

    @Deprecated
    public String orderCancelRequestHistoryId;

    public String message; // 승인/거절 메시지

    // 신규 인자명을 우선하고, 없으면 구 인자명으로 폴백한다 (하위호환)
    public String resolveOrderCancellationRequestId() {
        if (orderCancellationRequestId != null && !orderCancellationRequestId.isEmpty()) return orderCancellationRequestId;
        return orderCancelRequestHistoryId;
    }
}
