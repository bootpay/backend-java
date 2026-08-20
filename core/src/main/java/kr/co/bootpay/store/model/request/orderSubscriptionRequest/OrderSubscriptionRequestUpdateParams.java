package kr.co.bootpay.store.model.request.orderSubscriptionRequest;

/**
 * 구독 변경요청 승인/반려 파라미터 (PUT order-subscription-requests/:id).
 *
 * <p>승인과 반려는 별도 액션이 아니라 {@link #approval} 값으로 갈린다
 * (서버가 {@code params[:action]} 을 Rails 예약어로 사용하기 때문에 키 이름이 approval 이다).</p>
 */
public class OrderSubscriptionRequestUpdateParams {

    public static final String APPROVAL_APPROVE = "approve";
    public static final String APPROVAL_REJECT = "reject";

    public String orderSubscriptionRequestHistoryId;
    /** "approve" 또는 "reject" */
    public String approval;
    public String reason;

    /** 승인 시 확정 금액 */
    public Double price;
    /** 승인 시 확정 비과세 금액 */
    public Double taxFreePrice;
    /** 해지 위약금 */
    public Double terminationFee;
    /** 마지막 청구 건 환불 금액 */
    public Double lastBillRefundPrice;
    /** 최종 정산 금액 */
    public Double finalFee;
    /** 서비스 종료 일시 */
    public String serviceEndAt;

    /** 미지정시 자동 생성 (Idempotency-Key 헤더로 전송, body 에는 포함되지 않는다) */
    public transient String idempotencyKey;
}
