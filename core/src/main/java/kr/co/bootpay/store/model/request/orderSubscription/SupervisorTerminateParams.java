package kr.co.bootpay.store.model.request.orderSubscription;

/**
 * 관리자 구독 해지 파라미터 (PUT order_subscriptions/:id/terminate, supervisor 권한 필요).
 *
 * <p>{@code terminate(orderSubscriptionId[, reason])} 는 사유만 전달합니다. 위약금·환불액·서비스 종료일
 * 같은 정산 항목까지 지정해야 할 때 이 파라미터를 사용하세요.</p>
 */
public class SupervisorTerminateParams {
    /** 해지 사유 */
    public String reason;
    /** 해지 위약금 */
    public Double terminationFee;
    /** 마지막 청구 건 환불 금액 */
    public Double lastBillRefundPrice;
    /** 최종 정산 금액 */
    public Double finalFee;
    /** 서비스 종료 일시 */
    public String serviceEndAt;
    /** 해지 처리 기준일 */
    public String cancelDate;
}
