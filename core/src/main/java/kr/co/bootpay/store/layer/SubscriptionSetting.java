package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
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

    public HashMap<String, Object> list(ListParams params)  throws Exception {
        return SSubscriptionSettingService.list(
                bootpay,
                params
        );
    }

    public HashMap<String, Object> create(SSubscriptionSetting subscriptionSetting) throws Exception {
        return SSubscriptionSettingService.create(
                bootpay,
                subscriptionSetting
        );
    }

    public HashMap<String, Object> detail(String subscriptionSettingId) throws Exception {
        return SSubscriptionSettingService.detail(bootpay, subscriptionSettingId);
    }

    public HashMap<String, Object> update(SSubscriptionSetting subscriptionSetting) throws Exception {
        return SSubscriptionSettingService.update(bootpay, subscriptionSetting);
    }

    public HashMap<String, Object> delete(String subscriptionSettingId) throws Exception {
        return SSubscriptionSettingService.delete(bootpay, subscriptionSettingId);
    }
}
