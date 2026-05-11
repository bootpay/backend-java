package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestListParams;
import kr.co.bootpay.store.model.request.orderSubscriptionRequest.OrderSubscriptionRequestUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.order_subscription_requests.SOrderSubscriptionRequestService;

/**
 * V1 OrderSubscription Request 조회/승인 모듈
 *
 * 본인 모드 (user role): projectId 없이 호출 → 본인 요청 목록/단건
 * 슈퍼바이저 모드 (supervisor role): projectId 포함 → 프로젝트 전체 + update (승인/거절)
 *
 * 구매자측 요청 생성 (pause/resume/termination 등) 은
 * {@code orderSubscription.requestIng.*} 모듈을 사용한다.
 */
public class OrderSubscriptionRequest {
    private final BootpayStore bootpay;

    public OrderSubscriptionRequest(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 요청 목록 조회 (user / supervisor 공용)
     */
    public BootpayStoreResponse list(OrderSubscriptionRequestListParams params) throws Exception {
        return SOrderSubscriptionRequestService.list(bootpay, params);
    }

    public BootpayStoreResponse list() throws Exception {
        return SOrderSubscriptionRequestService.list(bootpay, null);
    }

    /**
     * 요청 단건 조회 (user / supervisor 공용)
     */
    public BootpayStoreResponse detail(String orderSubscriptionRequestHistoryId) throws Exception {
        return SOrderSubscriptionRequestService.detail(bootpay, orderSubscriptionRequestHistoryId, null);
    }

    public BootpayStoreResponse detail(String orderSubscriptionRequestHistoryId, String projectId) throws Exception {
        return SOrderSubscriptionRequestService.detail(bootpay, orderSubscriptionRequestHistoryId, projectId);
    }

    /**
     * 요청 승인/거절 (supervisor 전용)
     */
    public BootpayStoreResponse update(OrderSubscriptionRequestUpdateParams params) throws Exception {
        return SOrderSubscriptionRequestService.update(bootpay, params);
    }
}
