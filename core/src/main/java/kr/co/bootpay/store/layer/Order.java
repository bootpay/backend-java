package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.OrderListParams;
import kr.co.bootpay.store.service.orders.SOrderService;

import java.util.HashMap;
import java.util.Optional;

public class Order {
    private final BootpayStore bootpay;

    public Order(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public HashMap<String, Object> list(OrderListParams params)  throws Exception {
        return SOrderService.list( bootpay, params);
    }
    public HashMap<String, Object> detail(String orderId) throws Exception {
        return SOrderService.detail(bootpay, orderId);
    }
}
