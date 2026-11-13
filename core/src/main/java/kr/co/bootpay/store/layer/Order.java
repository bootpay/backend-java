package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.order.OrderListParams;
import kr.co.bootpay.store.model.request.order.OrderPurchasesUpdateParams;
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

    public BootpayStoreResponse purchases(String orderNumber) throws Exception {
        return SOrderService.purchases(bootpay, orderNumber, null);
    }

    public BootpayStoreResponse purchases(String orderNumber, String idempotencyKey) throws Exception {
        return SOrderService.purchases(bootpay, orderNumber, idempotencyKey);
    }

    public BootpayStoreResponse updatePurchases(OrderPurchasesUpdateParams params) throws Exception {
        return SOrderService.updatePurchases(bootpay, params);
    }
}
