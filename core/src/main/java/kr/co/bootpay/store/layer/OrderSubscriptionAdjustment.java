package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionAdjustment;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;
import kr.co.bootpay.store.model.request.orderSubscriptionAdjustment.OrderSubscriptionAdjustmentUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.order_subscription_adjustment.SOrderSubscriptionAdjustmentService;
import kr.co.bootpay.store.service.order_subscription_bill.SOrderSubscriptionBillService;

import java.util.List;

public class OrderSubscriptionAdjustment {
    private final BootpayStore bootpay;

    public OrderSubscriptionAdjustment(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 가감산 조정항목 추가 (supervisor 권한 필요)
     *
     * <p>type 미지정시 서버가 price&gt;0 이면 SETUP_PRICE, 아니면 PERIOD_DISCOUNT 로 자동 판정한다.</p>
     *
     * <p>회차 지정 방법 3가지 (아래로 갈수록 넓다).</p>
     * <ul>
     *   <li>{@code duration = 5} → 5회차 한 건만</li>
     *   <li>{@code durationFrom = 3, durationTo = 7} → 3~7회차 각각 한 건씩 (총 5건)</li>
     *   <li>{@code durationFrom = 3, isUnlimited = true} → 3회차부터 계약 끝까지 (레코드는 1건, {@code durationTo} 는 무시)</li>
     * </ul>
     * <p>상한은 계약 총회차이며, 총회차가 무제한인 계약은 60회차까지다.
     * 이미 결제가 끝난 회차는 거절된다. 범위 중 한 회차라도 최종 금액이 음수면 전부 거절된다 (부분 반영 없음).</p>
     *
     * @param orderSubscriptionId 구독 ID 또는 external_uid (가맹점 고유 ID)
     * @param adjustment 조정항목 (회차 미지정시 1회차)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse create(String orderSubscriptionId, SOrderSubscriptionAdjustment adjustment)  throws Exception {
        return SOrderSubscriptionAdjustmentService.create(
                bootpay,
                orderSubscriptionId,
                adjustment
        );
    }

    public BootpayStoreResponse update(OrderSubscriptionAdjustmentUpdateParams params) throws Exception {
        return SOrderSubscriptionAdjustmentService.update(
                bootpay,
                params
        );
    }

    public BootpayStoreResponse delete(String orderSubscriptionId, String orderSubscriptionAdjustmentId) throws Exception {
        return SOrderSubscriptionAdjustmentService.delete(
                bootpay,
                orderSubscriptionId,
                orderSubscriptionAdjustmentId
        );
    }
}
