package kr.co.bootpay.store.model.request.order.cancel;

import java.util.List;

public class RequestCancelParameter {
    public String cancelId;
    public Double cancelPrice;
    public Double cancelTaxFreePrice;
    public String cancelRequester;
    public String cancelMessage;
    public List<CancelProduct> cancelProducts;
    public boolean cancelImmediately;
    public List<CancelOrderSubscriptionBill> cancelOrderSubscriptionBills;
}
