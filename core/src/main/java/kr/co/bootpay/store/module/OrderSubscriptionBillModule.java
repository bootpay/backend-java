package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.OrderSubscriptionBill;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;
import kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams;

/**
 * 정기구독 청구 모듈.
 *
 * @since 3.3.0
 */
public class OrderSubscriptionBillModule {

    private final OrderSubscriptionBill delegate;

    public OrderSubscriptionBillModule(BootpayStore bootpay) {
        this.delegate = new OrderSubscriptionBill(bootpay);
    }

    /**
     * 청구 목록 조회.
     *
     * @param params 조회 조건
     * @return 청구 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(OrderSubscriptionBillListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 청구 상세 조회.
     *
     * @param orderSubscriptionBillId 청구 id
     * @return 청구 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String orderSubscriptionBillId) throws Exception {
        return CommerceResponses.of(delegate.detail(orderSubscriptionBillId));
    }

    /**
     * 청구 수정.
     *
     * @param orderSubscriptionBill 수정할 청구 정보
     * @return 수정 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(SOrderSubscriptionBill orderSubscriptionBill) throws Exception {
        return CommerceResponses.of(delegate.update(orderSubscriptionBill));
    }
}
