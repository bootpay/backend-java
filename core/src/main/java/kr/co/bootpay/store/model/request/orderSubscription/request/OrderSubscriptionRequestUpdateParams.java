package kr.co.bootpay.store.model.request.orderSubscription.request;

// 구독 변경요청 승인/반려 파라미터 (PUT order-subscription-requests/:id)
// 승인과 반려는 별도 액션이 아니라 approval 값으로 갈린다
// (서버가 params[:action]을 Rails 예약어로 사용하기 때문에 키 이름이 approval이다)
public class OrderSubscriptionRequestUpdateParams {
    public static final String APPROVAL_APPROVE = "approve";
    public static final String APPROVAL_REJECT = "reject";

    public transient String requestHistoryId; // URL path로만 사용하며 body에는 담지 않는다

    public String approval; // approve | reject
    public String reason;
    public Double price;
    public Double taxFreePrice;
    public Double terminationFee;
    public Double lastBillRefundPrice;
    public Double finalFee;
    public String serviceEndAt;
}
