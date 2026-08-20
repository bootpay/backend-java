package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Order;
import kr.co.bootpay.store.model.request.order.OrderListParams;

/**
 * 주문 모듈.
 *
 * @since 3.3.0
 */
public class OrderModule {

    private final Order delegate;

    public OrderModule(BootpayStore bootpay) {
        this.delegate = new Order(bootpay);
    }

    /**
     * 주문 목록 조회.
     *
     * @param params 조회 조건
     * @return 주문 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(OrderListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 주문 상세 조회.
     *
     * @param orderId 주문 id
     * @return 주문 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String orderId) throws Exception {
        return CommerceResponses.of(delegate.detail(orderId));
    }

    /**
     * 월별 주문 집계 조회.
     *
     * @param userGroupId 사용자 그룹 id
     * @param searchDate  조회 년월 (YYYY-MM)
     * @return 집계 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse month(String userGroupId, String searchDate) throws Exception {
        return CommerceResponses.of(delegate.month(userGroupId, searchDate));
    }
}
