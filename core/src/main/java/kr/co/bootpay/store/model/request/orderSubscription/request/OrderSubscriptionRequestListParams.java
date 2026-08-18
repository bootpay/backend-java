package kr.co.bootpay.store.model.request.orderSubscription.request;

import kr.co.bootpay.store.model.request.ListParams;

// 구독 변경요청 목록 조회 파라미터 (GET order-subscription-requests)
// projectId를 지정하면 supervisor role(프로젝트 전체 검색)로, 없으면 user role(본인 요청)로 요청한다
public class OrderSubscriptionRequestListParams extends ListParams {
    public String projectId;
    public String orderSubscriptionId;
    public String sAt;
    public String eAt;
    public Integer status;
    public Integer requestType;
    public String userId;
    public String userGroupId;
}
