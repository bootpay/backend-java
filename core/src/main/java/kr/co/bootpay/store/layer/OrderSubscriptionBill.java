package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;
import kr.co.bootpay.store.service.order_subscription_bill.SOrderSubscriptionBillService;
import kr.co.bootpay.store.service.order_subscriptions.SOrderSubscriptionService;

import java.util.HashMap;
import java.util.Optional;

public class OrderSubscriptionBill {
    private final BootpayStore bootpay;

    public OrderSubscriptionBill(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public HashMap<String, Object> list( String keyword, Integer page, Integer limit)  throws Exception {
        return SOrderSubscriptionBillService.list(
                bootpay,
                Optional.ofNullable(keyword),
                Optional.ofNullable(page),
                Optional.ofNullable(limit)
        );
    }
    public HashMap<String, Object> detail(String orderSubscriptionBillId) throws Exception {
        return SOrderSubscriptionBillService.detail(bootpay, orderSubscriptionBillId);
    }

    public HashMap<String, Object> update(SOrderSubscriptionBill orderSubscriptionBill) throws Exception {
        return SOrderSubscriptionBillService.update(bootpay, orderSubscriptionBill);
    }
}
