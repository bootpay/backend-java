package kr.co.bootpay.store.layer.order_subscription.request;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionPauseParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionResumeParams;
import kr.co.bootpay.store.model.request.orderSubscription.request.ing.OrderSubscriptionTerminationParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.order_subscriptions.request.SOrderSubscriptionRequestIngService;

public class OrderSubscriptionRequestIng {
    private final BootpayStore bootpay;

    public OrderSubscriptionRequestIng(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse pause(OrderSubscriptionPauseParams params)  throws Exception {
        return SOrderSubscriptionRequestIngService.pause(
                bootpay,
                params
        );
    }

    public BootpayStoreResponse resume(OrderSubscriptionResumeParams params)  throws Exception {
        return SOrderSubscriptionRequestIngService.resume(
                bootpay,
                params
        );
    }

    public BootpayStoreResponse calculateTerminationFee(String orderSubscriptionId)  throws Exception {
        return SOrderSubscriptionRequestIngService.calculateTerminationFee(
                bootpay,
                orderSubscriptionId,
                null
        );
    }

    public BootpayStoreResponse calculateTerminationFee(String orderSubscriptionId, String orderNumber)  throws Exception {
        return SOrderSubscriptionRequestIngService.calculateTerminationFee(
                bootpay,
                orderSubscriptionId,
                orderNumber
        );
    }

    public BootpayStoreResponse calculateTerminationFeeByOrderNumber(String orderNumber)  throws Exception {
        return SOrderSubscriptionRequestIngService.calculateTerminationFee(
                bootpay,
                null,
                orderNumber
        );
    }

    public BootpayStoreResponse termination(OrderSubscriptionTerminationParams params)  throws Exception {
        return SOrderSubscriptionRequestIngService.termination(
                bootpay,
                params
        );
    }

//    public BootpayStoreResponse detail(String orderSubscriptionId) throws Exception {
//        return SOrderSubscriptionService.detail(bootpay, orderSubscriptionId);
//    }
//
//    public BootpayStoreResponse update(OrderSubscriptionUpdateParams params) throws Exception {
//        return SOrderSubscriptionService.update(bootpay, params);
//    }

}
