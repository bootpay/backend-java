package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.OrderSubscriptionRequest;
import kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestListParams;
import kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestUpdateParams;

/**
 * 정기구독 요청 이력 모듈.
 *
 * @since 3.3.0
 */
public class OrderSubscriptionRequestModule {

    private final OrderSubscriptionRequest delegate;

    public OrderSubscriptionRequestModule(BootpayStore bootpay) {
        this.delegate = new OrderSubscriptionRequest(bootpay);
    }

    /**
     * 요청 이력 목록 조회.
     *
     * @param params 조회 조건
     * @return 요청 이력 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(OrderSubscriptionRequestListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 요청 이력 목록 조회 (조건 없이).
     *
     * @return 요청 이력 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /**
     * 요청 이력 상세 조회.
     *
     * @param orderSubscriptionRequestHistoryId 요청 이력 id
     * @return 요청 이력
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String orderSubscriptionRequestHistoryId) throws Exception {
        return CommerceResponses.of(delegate.detail(orderSubscriptionRequestHistoryId));
    }

    /**
     * 요청 이력 상세 조회.
     *
     * @param orderSubscriptionRequestHistoryId 요청 이력 id
     * @param projectId                         프로젝트 id
     * @return 요청 이력
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String orderSubscriptionRequestHistoryId, String projectId) throws Exception {
        return CommerceResponses.of(delegate.detail(orderSubscriptionRequestHistoryId, projectId));
    }

    /**
     * 요청 이력 수정.
     *
     * @param params 수정 정보
     * @return 수정 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(OrderSubscriptionRequestUpdateParams params) throws Exception {
        return CommerceResponses.of(delegate.update(params));
    }
}
