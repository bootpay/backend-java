package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.order_subscription.request.OrderSubscriptionRequestIng;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorChargeParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorChargeRevokeParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorPauseParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorResumeParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorTerminateParams;
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

    /**
     * 관리자 구독 일시정지 (supervisor 권한 필요)
     * - 검증 최소화, 즉시 일시정지 처리
     * @param orderSubscriptionId 구독 ID 또는 external_uid (가맹점 고유 ID)
     * @param params 일시정지 파라미터 (pausedAt 필수, reason/expectedResumeAt 선택)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse supervisorPause(String orderSubscriptionId, SupervisorPauseParams params) throws Exception {
        return SOrderSubscriptionService.supervisorPause(bootpay, orderSubscriptionId, params);
    }

    /**
     * 관리자 구독 재개 (supervisor 권한 필요)
     * - 검증 최소화, 즉시 재개 처리
     * @param orderSubscriptionId 구독 ID 또는 external_uid (가맹점 고유 ID)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse supervisorResume(String orderSubscriptionId) throws Exception {
        return SOrderSubscriptionService.supervisorResume(bootpay, orderSubscriptionId, null);
    }

    /**
     * 관리자 구독 재개 (supervisor 권한 필요)
     * - 검증 최소화, 즉시 재개 처리
     * @param orderSubscriptionId 구독 ID 또는 external_uid (가맹점 고유 ID)
     * @param params 재개 파라미터 (reason 선택)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse supervisorResume(String orderSubscriptionId, SupervisorResumeParams params) throws Exception {
        return SOrderSubscriptionService.supervisorResume(bootpay, orderSubscriptionId, params);
    }

    /**
     * 수시결제(온디맨드) charge_key 즉시 결제 (supervisor 권한 필요)
     * POST /v1/order_subscriptions/charge
     * ⚠️ charge_key 는 body 로만 전송된다 (URL/query 금지 — 액세스 로그 노출 방지)
     * @param params 결제 파라미터 (chargeKey/price 필수, idempotencyKey 미지정시 자동 생성)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse supervisorCharge(SupervisorChargeParams params) throws Exception {
        return SOrderSubscriptionService.supervisorCharge(bootpay, params);
    }

    /**
     * 수시결제(온디맨드) charge_key 해지 (supervisor 권한 필요)
     * DELETE /v1/order_subscriptions/charge
     * 해지 이후 해당 키로의 재결제는 불가능하다.
     * @param params 해지 파라미터 (chargeKey 필수, idempotencyKey 미지정시 자동 생성)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse supervisorChargeRevoke(SupervisorChargeRevokeParams params) throws Exception {
        return SOrderSubscriptionService.supervisorChargeRevoke(bootpay, params);
    }

    /**
     * 관리자 구독 해지 (supervisor 권한 필요)
     *
     * <p>{@link #terminate(String, String)} 와 같은 엔드포인트지만 위약금·환불액·서비스 종료일 등
     * 정산 항목을 함께 전달할 수 있다.</p>
     *
     * @param orderSubscriptionId 구독 ID 또는 external_uid
     * @param params 해지 파라미터 (전부 선택)
     * @return BootpayStoreResponse
     */
    public BootpayStoreResponse supervisorTerminate(String orderSubscriptionId, SupervisorTerminateParams params) throws Exception {
        return SOrderSubscriptionService.supervisorTerminate(bootpay, orderSubscriptionId, params);
    }

}
