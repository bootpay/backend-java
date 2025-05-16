package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SSubscriptionSetting;
import kr.co.bootpay.store.service.delivery_shipping.SDeliveryShippingService;
import kr.co.bootpay.store.service.subscription_setting.SSubscriptionSettingService;

import java.util.HashMap;
import java.util.Optional;

public class DeliveryShipping {
    private final BootpayStore bootpay;

    public DeliveryShipping(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

//    public HashMap<String, Object> list( String keyword, Integer page, Integer limit)  throws Exception {
//        return SDeliveryShippingService.list(
//                bootpay,
//                Optional.ofNullable(keyword),
//                Optional.ofNullable(page),
//                Optional.ofNullable(limit)
//        );
//    }
//
//    public HashMap<String, Object> create(SSubscriptionSetting subscriptionSetting) throws Exception {
//        return SDeliveryShippingService.create(
//                bootpay,
//                subscriptionSetting
//        );
//    }
//
//    public HashMap<String, Object> detail(String subscriptionSettingId) throws Exception {
//        return SDeliveryShippingService.detail(bootpay, subscriptionSettingId);
//    }
//
//    public HashMap<String, Object> update(SSubscriptionSetting subscriptionSetting) throws Exception {
//        return SDeliveryShippingService.update(bootpay, subscriptionSetting);
//    }
//
//    public HashMap<String, Object> delete(String subscriptionSettingId) throws Exception {
//        return SDeliveryShippingService.delete(bootpay, subscriptionSettingId);
//    }
}
