package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.order.OrderListParams;
import kr.co.bootpay.store.service.orders.SOrderService;

public class Order {
    private final BootpayStore bootpay;

    public Order(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse list(OrderListParams params)  throws Exception {
        return SOrderService.list( bootpay, params);
    }
    public BootpayStoreResponse detail(String orderId) throws Exception {
        return SOrderService.detail(bootpay, orderId);
    }

    public BootpayStoreResponse month(String userGroupId, String searchDate) throws Exception {
        return SOrderService.month(bootpay, userGroupId, searchDate);
    }
}
