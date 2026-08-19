package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SMallSetting;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.mall_setting.SMallSettingService;

/**
 * 몰 설정 모듈 (supervisor scope 전용)
 * GET / PUT /v1/mall-setting
 */
public class MallSetting {
    private final BootpayStore bootpay;

    public MallSetting(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 몰 설정 조회
     */
    public BootpayStoreResponse getMallSetting() throws Exception {
        return SMallSettingService.detail(bootpay, null);
    }

    /**
     * 몰 설정 조회
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse getMallSetting(String idempotencyKey) throws Exception {
        return SMallSettingService.detail(bootpay, idempotencyKey);
    }

    public BootpayStoreResponse detail() throws Exception {
        return getMallSetting();
    }

    public BootpayStoreResponse detail(String idempotencyKey) throws Exception {
        return getMallSetting(idempotencyKey);
    }

    /**
     * 몰 설정 수정 — flatten 바디, 전달된 값(non-null)만 서버로 전송된다.
     * @param setting 수정할 설정값
     */
    public BootpayStoreResponse updateMallSetting(SMallSetting setting) throws Exception {
        return SMallSettingService.update(bootpay, setting, null);
    }

    /**
     * 몰 설정 수정
     * @param setting 수정할 설정값
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse updateMallSetting(SMallSetting setting, String idempotencyKey) throws Exception {
        return SMallSettingService.update(bootpay, setting, idempotencyKey);
    }

    public BootpayStoreResponse update(SMallSetting setting) throws Exception {
        return updateMallSetting(setting);
    }

    public BootpayStoreResponse update(SMallSetting setting, String idempotencyKey) throws Exception {
        return updateMallSetting(setting, idempotencyKey);
    }
}
