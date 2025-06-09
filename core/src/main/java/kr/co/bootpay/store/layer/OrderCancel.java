package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelActionParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelListParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelParams;
import kr.co.bootpay.store.service.orders.SOrderCancelService;

import java.util.HashMap;

public class OrderCancel {
    private final BootpayStore bootpay;

    public OrderCancel(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public HashMap<String, Object> list(OrderCancelListParams params)  throws Exception {
        return SOrderCancelService.list( bootpay, params);
    }
    public HashMap<String, Object> request(OrderCancelParams params) throws Exception {
        return SOrderCancelService.request(bootpay, params);
    }

    public HashMap<String, Object> withdraw(String orderCancelRequestHistoryId) throws Exception {
        return SOrderCancelService.withdraw(bootpay, orderCancelRequestHistoryId);
    }

    public HashMap<String, Object> approve(OrderCancelActionParams params) throws Exception {
        return SOrderCancelService.approve(bootpay, params);
    }

    public HashMap<String, Object> reject(OrderCancelActionParams params) throws Exception {
        return SOrderCancelService.reject(bootpay, params);
    }




}
