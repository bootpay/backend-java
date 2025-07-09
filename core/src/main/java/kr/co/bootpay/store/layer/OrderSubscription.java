package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.order_subscription.request.OrderSubscriptionRequestIng;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.order_subscriptions.SOrderSubscriptionService;

public class OrderSubscription {
    private final BootpayStore bootpay;
    public final OrderSubscriptionRequestIng requestIng;

    public OrderSubscription(BootpayStore bootpay) {
        this.bootpay = bootpay;
        this.requestIng = new OrderSubscriptionRequestIng(bootpay);
    }

    public BootpayStoreResponse list(OrderSubscriptionListParams params)  throws Exception {
        return SOrderSubscriptionService.list(
                bootpay,
                params
        );
    }
    public BootpayStoreResponse detail(String orderSubscriptionId) throws Exception {
        return SOrderSubscriptionService.detail(bootpay, orderSubscriptionId);
    }

    public BootpayStoreResponse update(OrderSubscriptionUpdateParams params) throws Exception {
        return SOrderSubscriptionService.update(bootpay, params);
    }
}
