package kr.co.bootpay.store.model.request.order;

import kr.co.bootpay.store.model.request.ListParams;

import java.util.List;

public class OrderListParams extends ListParams {
    public String csType;
    public String cssAt;
    public String cseAt;
    public List<Integer> status;
    public List<Integer> paymentStatus;
    public String userId;
    public String userGroupId;
}
