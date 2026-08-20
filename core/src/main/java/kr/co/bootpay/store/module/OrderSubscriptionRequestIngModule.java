package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.layer.order_subscription.request.OrderSubscriptionRequestIng;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionPauseParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionPurchaseParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionResumeParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionTerminationParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionTransferParams;

/**
 * 진행 중인 구독에 대한 요청 모듈 ({@code orderSubscription.requestIng}).
 *
 * @since 3.3.0
 */
public class OrderSubscriptionRequestIngModule {

    private final OrderSubscriptionRequestIng delegate;

    public OrderSubscriptionRequestIngModule(OrderSubscriptionRequestIng delegate) {
        this.delegate = delegate;
    }

    /**
     * 구독 일시정지 요청.
     *
     * @param params 일시정지 정보
     * @return 요청 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse pause(OrderSubscriptionPauseParams params) throws Exception {
        return CommerceResponses.of(delegate.pause(params));
    }

    /**
     * 구독 재개 요청.
     *
     * @param params 재개 정보
     * @return 요청 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse resume(OrderSubscriptionResumeParams params) throws Exception {
        return CommerceResponses.of(delegate.resume(params));
    }

    /**
     * 구독 추가 구매 요청.
     *
     * @param params 구매 정보
     * @return 요청 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse purchase(OrderSubscriptionPurchaseParams params) throws Exception {
        return CommerceResponses.of(delegate.purchase(params));
    }

    /**
     * 구독 양도 요청.
     *
     * @param params 양도 정보
     * @return 요청 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse transfer(OrderSubscriptionTransferParams params) throws Exception {
        return CommerceResponses.of(delegate.transfer(params));
    }

    /**
     * 해지 위약금 계산.
     *
     * @param orderSubscriptionId 구독 id
     * @return 계산 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse calculateTerminationFee(String orderSubscriptionId) throws Exception {
        return CommerceResponses.of(delegate.calculateTerminationFee(orderSubscriptionId));
    }

    /**
     * 해지 위약금 계산.
     *
     * @param orderSubscriptionId 구독 id
     * @param orderNumber         주문번호
     * @return 계산 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse calculateTerminationFee(String orderSubscriptionId, String orderNumber) throws Exception {
        return CommerceResponses.of(delegate.calculateTerminationFee(orderSubscriptionId, orderNumber));
    }

    /**
     * 해지 위약금 계산 (주문번호 기준).
     *
     * @param orderNumber 주문번호
     * @return 계산 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse calculateTerminationFeeByOrderNumber(String orderNumber) throws Exception {
        return CommerceResponses.of(delegate.calculateTerminationFeeByOrderNumber(orderNumber));
    }

    /**
     * 구독 해지 요청.
     *
     * @param params 해지 정보
     * @return 요청 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse termination(OrderSubscriptionTerminationParams params) throws Exception {
        return CommerceResponses.of(delegate.termination(params));
    }
}
