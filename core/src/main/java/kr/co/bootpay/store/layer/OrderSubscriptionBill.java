package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.service.order_subscription_bill.SOrderSubscriptionBillService;

public class OrderSubscriptionBill {
    private final BootpayStore bootpay;

    public OrderSubscriptionBill(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse list(ListParams params)  throws Exception {
        return SOrderSubscriptionBillService.list(
                bootpay,
                params
        );
    }
    public BootpayStoreResponse detail(String orderSubscriptionBillId) throws Exception {
        return SOrderSubscriptionBillService.detail(bootpay, orderSubscriptionBillId);
    }

    public BootpayStoreResponse update(SOrderSubscriptionBill orderSubscriptionBill) throws Exception {
        return SOrderSubscriptionBillService.update(bootpay, orderSubscriptionBill);
    }
}
