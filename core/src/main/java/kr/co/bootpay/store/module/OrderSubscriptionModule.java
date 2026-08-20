package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.OrderSubscription;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorChargeParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorChargeRevokeParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorPauseParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorResumeParams;

/**
 * 정기구독 모듈.
 *
 * @since 3.3.0
 */
public class OrderSubscriptionModule {

    private final OrderSubscription delegate;

    /** 진행 중인 구독에 대한 요청 (일시정지 / 재개 / 해지 등). */
    public final OrderSubscriptionRequestIngModule requestIng;

    public OrderSubscriptionModule(BootpayStore bootpay) {
        this.delegate = new OrderSubscription(bootpay);
        this.requestIng = new OrderSubscriptionRequestIngModule(delegate.requestIng);
    }

    /**
     * 구독 목록 조회.
     *
     * @param params 조회 조건
     * @return 구독 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(OrderSubscriptionListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 구독 상세 조회.
     *
     * @param orderSubscriptionId 구독 id
     * @return 구독 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String orderSubscriptionId) throws Exception {
        return CommerceResponses.of(delegate.detail(orderSubscriptionId));
    }

    /**
     * 구독 수정.
     *
     * @param params 수정 정보
     * @return 수정 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(OrderSubscriptionUpdateParams params) throws Exception {
        return CommerceResponses.of(delegate.update(params));
    }

    /**
     * 구독 승인.
     *
     * @param orderSubscriptionId 구독 id
     * @return 승인 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse approve(String orderSubscriptionId) throws Exception {
        return CommerceResponses.of(delegate.approve(orderSubscriptionId));
    }

    /**
     * 구독 승인.
     *
     * @param orderSubscriptionId 구독 id
     * @param reason              승인 사유
     * @return 승인 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse approve(String orderSubscriptionId, String reason) throws Exception {
        return CommerceResponses.of(delegate.approve(orderSubscriptionId, reason));
    }

    /**
     * 구독 거절.
     *
     * @param orderSubscriptionId 구독 id
     * @param reason              거절 사유
     * @return 거절 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse reject(String orderSubscriptionId, String reason) throws Exception {
        return CommerceResponses.of(delegate.reject(orderSubscriptionId, reason));
    }

    /**
     * 구독 해지.
     *
     * @param orderSubscriptionId 구독 id
     * @return 해지 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse terminate(String orderSubscriptionId) throws Exception {
        return CommerceResponses.of(delegate.terminate(orderSubscriptionId));
    }

    /**
     * 구독 해지.
     *
     * @param orderSubscriptionId 구독 id
     * @param reason              해지 사유
     * @return 해지 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse terminate(String orderSubscriptionId, String reason) throws Exception {
        return CommerceResponses.of(delegate.terminate(orderSubscriptionId, reason));
    }

    /**
     * 구독 일시정지 (supervisor 권한).
     *
     * @param orderSubscriptionId 구독 id
     * @param params              일시정지 정보
     * @return 처리 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse supervisorPause(String orderSubscriptionId, SupervisorPauseParams params) throws Exception {
        return CommerceResponses.of(delegate.supervisorPause(orderSubscriptionId, params));
    }

    /**
     * 구독 재개 (supervisor 권한).
     *
     * @param orderSubscriptionId 구독 id
     * @return 처리 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse supervisorResume(String orderSubscriptionId) throws Exception {
        return CommerceResponses.of(delegate.supervisorResume(orderSubscriptionId));
    }

    /**
     * 구독 재개 (supervisor 권한).
     *
     * @param orderSubscriptionId 구독 id
     * @param params              재개 정보
     * @return 처리 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse supervisorResume(String orderSubscriptionId, SupervisorResumeParams params) throws Exception {
        return CommerceResponses.of(delegate.supervisorResume(orderSubscriptionId, params));
    }

    /**
     * 구독 강제 청구 (supervisor 권한).
     *
     * @param params 청구 정보
     * @return 처리 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse supervisorCharge(SupervisorChargeParams params) throws Exception {
        return CommerceResponses.of(delegate.supervisorCharge(params));
    }

    /**
     * 구독 강제 청구 취소 (supervisor 권한).
     *
     * @param params 취소 정보
     * @return 처리 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse supervisorChargeRevoke(SupervisorChargeRevokeParams params) throws Exception {
        return CommerceResponses.of(delegate.supervisorChargeRevoke(params));
    }
}
