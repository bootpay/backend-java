package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.mallSetting.MallSettingUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.mall_setting.SMallSettingService;

public class MallSetting {
    private final BootpayStore bootpay;

    public MallSetting(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse get() throws Exception {
        return SMallSettingService.getMallSetting(bootpay, null);
    }

    public BootpayStoreResponse get(String idempotencyKey) throws Exception {
        return SMallSettingService.getMallSetting(bootpay, idempotencyKey);
    }

    // Mall API alias
    public BootpayStoreResponse getMallSetting() throws Exception {
        return get();
    }

    public BootpayStoreResponse getMallSetting(String idempotencyKey) throws Exception {
        return get(idempotencyKey);
    }

    public BootpayStoreResponse update(MallSettingUpdateParams params) throws Exception {
        return SMallSettingService.updateMallSetting(bootpay, params);
    }

    // Mall API alias
    public BootpayStoreResponse updateMallSetting(MallSettingUpdateParams params) throws Exception {
        return update(params);
    }
}
