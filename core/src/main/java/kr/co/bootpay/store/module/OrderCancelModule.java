package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.OrderCancel;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelActionParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelListParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelParams;

/**
 * 주문 취소 모듈.
 *
 * @since 3.3.0
 */
public class OrderCancelModule {

    private final OrderCancel delegate;

    public OrderCancelModule(BootpayStore bootpay) {
        this.delegate = new OrderCancel(bootpay);
    }

    /**
     * 취소 요청 목록 조회.
     *
     * @param params 조회 조건
     * @return 취소 요청 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(OrderCancelListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 취소 요청.
     *
     * @param params 취소 요청 정보
     * @return 취소 요청 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse request(OrderCancelParams params) throws Exception {
        return CommerceResponses.of(delegate.request(params));
    }

    /**
     * 취소 요청 철회.
     *
     * @param orderCancelRequestHistoryId 취소 요청 이력 id
     * @return 철회 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse withdraw(String orderCancelRequestHistoryId) throws Exception {
        return CommerceResponses.of(delegate.withdraw(orderCancelRequestHistoryId));
    }

    /**
     * 취소 요청 철회.
     *
     * @param orderCancelRequestHistoryId 취소 요청 이력 id
     * @param idempotencyKey              미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 철회 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse withdraw(String orderCancelRequestHistoryId, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.withdraw(orderCancelRequestHistoryId, idempotencyKey));
    }

    /**
     * 취소 요청 승인.
     *
     * @param params 승인 정보
     * @return 승인 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse approve(OrderCancelActionParams params) throws Exception {
        return CommerceResponses.of(delegate.approve(params));
    }

    /**
     * 취소 요청 거절.
     *
     * @param params 거절 정보
     * @return 거절 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse reject(OrderCancelActionParams params) throws Exception {
        return CommerceResponses.of(delegate.reject(params));
    }
}
