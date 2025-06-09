package kr.co.bootpay.store.model.request.order.cancel;

import kr.co.bootpay.store.model.request.order.cancel.RequestCancelParameter;

public class OrderCancelParams {
    public String orderNumber;
    public RequestCancelParameter requestCancelParameters = new RequestCancelParameter();
}
