package kr.co.bootpay.store.model.request.order.cancel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrderCancelActionParamsTest {

    @Test
    public void 신규_인자명을_우선한다() {
        OrderCancelActionParams params = new OrderCancelActionParams();
        params.orderCancellationRequestId = "new_id";
        params.orderCancelRequestHistoryId = "legacy_id";

        assertEquals("new_id", params.resolveOrderCancellationRequestId());
    }

    @Test
    public void 신규_인자명이_없으면_구_인자명으로_폴백한다() {
        OrderCancelActionParams params = new OrderCancelActionParams();
        params.orderCancelRequestHistoryId = "legacy_id";

        assertEquals("legacy_id", params.resolveOrderCancellationRequestId());
    }

    @Test
    public void 둘_다_없으면_null을_반환한다() {
        assertNull(new OrderCancelActionParams().resolveOrderCancellationRequestId());
    }
}
