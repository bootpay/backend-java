package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SSubscriptionSetting;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.service.subscription_setting.SSubscriptionSettingService;

import java.util.HashMap;
import java.util.Optional;

public class SubscriptionSetting {
    private final BootpayStore bootpay;

    public SubscriptionSetting(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse list(ListParams params)  throws Exception {
        return SSubscriptionSettingService.list(
                bootpay,
                params
        );
    }

    public BootpayStoreResponse create(SSubscriptionSetting subscriptionSetting) throws Exception {
        return SSubscriptionSettingService.create(
                bootpay,
                subscriptionSetting
        );
    }

    public BootpayStoreResponse detail(String subscriptionSettingId) throws Exception {
        return SSubscriptionSettingService.detail(bootpay, subscriptionSettingId);
    }

    public BootpayStoreResponse update(SSubscriptionSetting subscriptionSetting) throws Exception {
        return SSubscriptionSettingService.update(bootpay, subscriptionSetting);
    }

    public BootpayStoreResponse delete(String subscriptionSettingId) throws Exception {
        return SSubscriptionSettingService.delete(bootpay, subscriptionSettingId);
    }
}
