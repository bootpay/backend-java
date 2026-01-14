package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.order_subscription.request.OrderSubscriptionRequestIng;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.order_subscriptions.SOrderSubscriptionService;

public class OrderSubscription {
    private final BootpayStore bootpay;
    public final OrderSubscriptionRequestIng requestIng;

    public OrderSubscription(BootpayStore bootpay) {
        this.bootpay = bootpay;
        this.requestIng = new OrderSubscriptionRequestIng(bootpay);
    }

    /**
     * 구독 목록 조회
     * <p>
     * params에서 사용 가능한 ID 필드:
     * <ul>
     *   <li>userId: 부트페이 사용자 ID</li>
     *   <li>userExUid, userExternalUid, userUid: 가맹점 사용자 고유 ID (ex_uid)</li>
     *   <li>userGroupId: 부트페이 사용자 그룹 ID</li>
     *   <li>userGroupExUid, userGroupExternalUid, userGroupUid: 가맹점 사용자 그룹 고유 ID (ex_uid)</li>
     * </ul>
     * </p>
     * @param params 조회 조건 (OrderSubscriptionListParams)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse list(OrderSubscriptionListParams params)  throws Exception {
        return SOrderSubscriptionService.list(
                bootpay,
                params
        );
    }

    /**
     * 구독 상세 조회
     * @param orderSubscriptionId 구독 ID 또는 external_uid (가맹점 고유 ID)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse detail(String orderSubscriptionId) throws Exception {
        return SOrderSubscriptionService.detail(bootpay, orderSubscriptionId);
    }

    /**
     * 구독 내용 변경
     * @param params 변경할 내용 (orderSubscriptionId는 부트페이 ID 또는 external_uid 사용 가능)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse update(OrderSubscriptionUpdateParams params) throws Exception {
        return SOrderSubscriptionService.update(bootpay, params);
    }

    /**
     * 구독 승인
     * @param orderSubscriptionId 구독 ID 또는 external_uid (가맹점 고유 ID)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse approve(String orderSubscriptionId) throws Exception {
        return SOrderSubscriptionService.approve(bootpay, orderSubscriptionId, null);
    }

    /**
     * 구독 승인 (사유 포함)
     * @param orderSubscriptionId 구독 ID 또는 external_uid (가맹점 고유 ID)
     * @param reason 승인 사유
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse approve(String orderSubscriptionId, String reason) throws Exception {
        return SOrderSubscriptionService.approve(bootpay, orderSubscriptionId, reason);
    }

    /**
     * 구독 거절
     * @param orderSubscriptionId 구독 ID 또는 external_uid (가맹점 고유 ID)
     * @param reason 거절 사유 (필수)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse reject(String orderSubscriptionId, String reason) throws Exception {
        return SOrderSubscriptionService.reject(bootpay, orderSubscriptionId, reason);
    }

    /**
     * 관리자 구독 해지 (supervisor 권한 필요)
     * - 검증 최소화, 즉시 해지 처리
     * - 일반 사용자의 해지 요청과 달리 승인 대기 없이 바로 해지됨
     * @param orderSubscriptionId 구독 ID 또는 external_uid (가맹점 고유 ID)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse terminate(String orderSubscriptionId) throws Exception {
        return SOrderSubscriptionService.terminate(bootpay, orderSubscriptionId, null);
    }

    /**
     * 관리자 구독 해지 (supervisor 권한 필요)
     * - 검증 최소화, 즉시 해지 처리
     * - 일반 사용자의 해지 요청과 달리 승인 대기 없이 바로 해지됨
     * @param orderSubscriptionId 구독 ID 또는 external_uid (가맹점 고유 ID)
     * @param reason 해지 사유
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse terminate(String orderSubscriptionId, String reason) throws Exception {
        return SOrderSubscriptionService.terminate(bootpay, orderSubscriptionId, reason);
    }
}
