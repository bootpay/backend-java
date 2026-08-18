package kr.co.bootpay.store.layer.order_subscription.request;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.orderSubscription.request.OrderSubscriptionRequestListParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.OrderSubscriptionRequestUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.order_subscriptions.request.SOrderSubscriptionRequestService;

public class OrderSubscriptionRequest {
    private final BootpayStore bootpay;

    public OrderSubscriptionRequest(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse list(OrderSubscriptionRequestListParams params) throws Exception {
        return SOrderSubscriptionRequestService.list(
                bootpay,
                params
        );
    }

    public BootpayStoreResponse detail(String requestHistoryId) throws Exception {
        return SOrderSubscriptionRequestService.detail(bootpay, requestHistoryId);
    }

    public BootpayStoreResponse detail(String requestHistoryId, String projectId) throws Exception {
        return SOrderSubscriptionRequestService.detail(bootpay, requestHistoryId, projectId);
    }

    public BootpayStoreResponse update(OrderSubscriptionRequestUpdateParams params) throws Exception {
        return SOrderSubscriptionRequestService.update(bootpay, params);
    }

    public BootpayStoreResponse approve(OrderSubscriptionRequestUpdateParams params) throws Exception {
        return SOrderSubscriptionRequestService.approve(bootpay, params);
    }

    public BootpayStoreResponse reject(OrderSubscriptionRequestUpdateParams params) throws Exception {
        return SOrderSubscriptionRequestService.reject(bootpay, params);
    }
}
