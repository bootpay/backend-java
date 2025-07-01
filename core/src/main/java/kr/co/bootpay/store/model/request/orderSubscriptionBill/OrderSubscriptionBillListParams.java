package kr.co.bootpay.store.model.request.orderSubscriptionBill;

import kr.co.bootpay.store.model.request.ListParams;

import java.util.ArrayList;
import java.util.List;

public class OrderSubscriptionBillListParams extends ListParams {
    public String orderSubscriptionId;
    public List<Integer> status;

    public OrderSubscriptionBillListParams() {
        this.status = new ArrayList<>();
    }
}
