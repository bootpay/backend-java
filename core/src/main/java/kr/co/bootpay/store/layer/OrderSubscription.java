package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.service.order_subscriptions.SOrderSubscriptionService;
import kr.co.bootpay.store.service.orders.SOrderService;

import java.util.HashMap;
import java.util.Optional;

public class OrderSubscription {
    private final BootpayStore bootpay;

    public OrderSubscription(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse list(ListParams params)  throws Exception {
        return SOrderSubscriptionService.list(
                bootpay,
                params
        );
    }
    public BootpayStoreResponse detail(String orderSubscriptionId) throws Exception {
        return SOrderSubscriptionService.detail(bootpay, orderSubscriptionId);
    }
}
