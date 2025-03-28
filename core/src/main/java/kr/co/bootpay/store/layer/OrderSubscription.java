package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.service.order_subscriptions.SOrderSubscriptionService;
import kr.co.bootpay.store.service.orders.SOrderService;

import java.util.HashMap;
import java.util.Optional;

public class OrderSubscription {
    private final BootpayStore bootpay;

    public OrderSubscription(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public HashMap<String, Object> list( String keyword, Integer page, Integer limit)  throws Exception {
        return SOrderSubscriptionService.list(
                bootpay,
                Optional.ofNullable(keyword),
                Optional.ofNullable(page),
                Optional.ofNullable(limit)
        );
    }
    public HashMap<String, Object> detail(String orderSubscriptionId) throws Exception {
        return SOrderSubscriptionService.detail(bootpay, orderSubscriptionId);
    }
}
