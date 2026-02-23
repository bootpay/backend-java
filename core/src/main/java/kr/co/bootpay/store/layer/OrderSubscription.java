package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.order_subscription.request.OrderSubscriptionRequestIng;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionListParams;
import kr.co.bootpay.store.model.request.orderSubscription.OrderSubscriptionUpdateParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionApproveParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionRejectParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionTerminateParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionPauseParams;
import kr.co.bootpay.store.model.request.orderSubscription.SupervisorOrderSubscriptionResumeParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.order_subscriptions.SOrderSubscriptionService;

public class OrderSubscription {
    private final BootpayStore bootpay;
    public final OrderSubscriptionRequestIng requestIng;

    public OrderSubscription(BootpayStore bootpay) {
        this.bootpay = bootpay;
        this.requestIng = new OrderSubscriptionRequestIng(bootpay);
    }

    public BootpayStoreResponse list(OrderSubscriptionListParams params)  throws Exception {
        return SOrderSubscriptionService.list(
                bootpay,
                params
        );
    }
    public BootpayStoreResponse detail(String orderSubscriptionId) throws Exception {
        return SOrderSubscriptionService.detail(bootpay, orderSubscriptionId);
    }

    public BootpayStoreResponse update(OrderSubscriptionUpdateParams params) throws Exception {
        return SOrderSubscriptionService.update(bootpay, params);
    }

    public BootpayStoreResponse supervisorApprove(String orderSubscriptionId, SupervisorOrderSubscriptionApproveParams params) throws Exception {
        return SOrderSubscriptionService.supervisorApprove(bootpay, orderSubscriptionId, params);
    }

    public BootpayStoreResponse supervisorReject(String orderSubscriptionId, SupervisorOrderSubscriptionRejectParams params) throws Exception {
        return SOrderSubscriptionService.supervisorReject(bootpay, orderSubscriptionId, params);
    }

    public BootpayStoreResponse supervisorTerminate(String orderSubscriptionId, SupervisorOrderSubscriptionTerminateParams params) throws Exception {
        return SOrderSubscriptionService.supervisorTerminate(bootpay, orderSubscriptionId, params);
    }

    public BootpayStoreResponse supervisorPause(String orderSubscriptionId, SupervisorOrderSubscriptionPauseParams params) throws Exception {
        return SOrderSubscriptionService.supervisorPause(bootpay, orderSubscriptionId, params);
    }

    public BootpayStoreResponse supervisorResume(String orderSubscriptionId, SupervisorOrderSubscriptionResumeParams params) throws Exception {
        return SOrderSubscriptionService.supervisorResume(bootpay, orderSubscriptionId, params);
    }
}
