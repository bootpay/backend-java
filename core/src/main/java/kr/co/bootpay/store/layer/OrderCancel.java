package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelActionParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelListParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelParams;
import kr.co.bootpay.store.service.orders.SOrderCancelService;

public class OrderCancel {
    private final BootpayStore bootpay;

    public OrderCancel(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse list(OrderCancelListParams params)  throws Exception {
        return SOrderCancelService.list( bootpay, params);
    }
    public BootpayStoreResponse request(OrderCancelParams params) throws Exception {
        return SOrderCancelService.request(bootpay, params);
    }

    public BootpayStoreResponse withdraw(String orderCancelRequestHistoryId) throws Exception {
        return SOrderCancelService.withdraw(bootpay, orderCancelRequestHistoryId);
    }

    public BootpayStoreResponse approve(OrderCancelActionParams params) throws Exception {
        return SOrderCancelService.approve(bootpay, params);
    }

    public BootpayStoreResponse reject(OrderCancelActionParams params) throws Exception {
        return SOrderCancelService.reject(bootpay, params);
    }




}
