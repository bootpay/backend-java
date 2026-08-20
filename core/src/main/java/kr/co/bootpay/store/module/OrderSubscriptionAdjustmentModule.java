package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.OrderSubscriptionAdjustment;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionAdjustment;
import kr.co.bootpay.store.model.request.orderSubscriptionAdjustment.OrderSubscriptionAdjustmentUpdateParams;

/**
 * 정기구독 조정 모듈.
 *
 * @since 3.3.0
 */
public class OrderSubscriptionAdjustmentModule {

    private final OrderSubscriptionAdjustment delegate;

    public OrderSubscriptionAdjustmentModule(BootpayStore bootpay) {
        this.delegate = new OrderSubscriptionAdjustment(bootpay);
    }

    /**
     * 조정 생성.
     *
     * @param orderSubscriptionId 구독 id
     * @param adjustment          조정 정보
     * @return 생성 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(String orderSubscriptionId, SOrderSubscriptionAdjustment adjustment) throws Exception {
        return CommerceResponses.of(delegate.create(orderSubscriptionId, adjustment));
    }

    /**
     * 조정 수정.
     *
     * @param params 수정 정보
     * @return 수정 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(OrderSubscriptionAdjustmentUpdateParams params) throws Exception {
        return CommerceResponses.of(delegate.update(params));
    }

    /**
     * 조정 삭제.
     *
     * @param orderSubscriptionId           구독 id
     * @param orderSubscriptionAdjustmentId 조정 id
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse delete(String orderSubscriptionId, String orderSubscriptionAdjustmentId) throws Exception {
        return CommerceResponses.of(delegate.delete(orderSubscriptionId, orderSubscriptionAdjustmentId));
    }
}
