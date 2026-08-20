package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.MallSetting;
import kr.co.bootpay.store.model.pojo.SMallSetting;

/**
 * 몰 설정 모듈.
 *
 * <p>기존 표면의 {@code getMallSetting} / {@code updateMallSetting} 은 {@code detail} / {@code update}
 * 와 완전히 같은 동작의 별칭이었습니다. 신규 표면에서는 다른 모듈과 같은 CRUD 이름 하나만 노출합니다.</p>
 *
 * @since 3.3.0
 */
public class MallSettingModule {

    private final MallSetting delegate;

    public MallSettingModule(BootpayStore bootpay) {
        this.delegate = new MallSetting(bootpay);
    }

    /**
     * 몰 설정 조회.
     *
     * @return 몰 설정
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail() throws Exception {
        return CommerceResponses.of(delegate.detail());
    }

    /**
     * 몰 설정 조회.
     *
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 몰 설정
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.detail(idempotencyKey));
    }

    /**
     * 몰 설정 수정 — 전달된 값(non-null)만 서버로 전송됩니다.
     *
     * @param setting 수정할 설정값
     * @return 수정 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(SMallSetting setting) throws Exception {
        return CommerceResponses.of(delegate.update(setting));
    }

    /**
     * 몰 설정 수정.
     *
     * @param setting        수정할 설정값
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 수정 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse update(SMallSetting setting, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.update(setting, idempotencyKey));
    }
}
