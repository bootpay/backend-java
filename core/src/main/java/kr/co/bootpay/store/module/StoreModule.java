package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Store;

/**
 * 가맹점 정보 모듈.
 *
 * @since 3.3.0
 */
public class StoreModule {

    private final Store delegate;

    public StoreModule(BootpayStore bootpay) {
        this.delegate = new Store(bootpay);
    }

    /**
     * 가맹점 기본 정보 조회.
     *
     * @return 가맹점 기본 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse info() throws Exception {
        return CommerceResponses.of(delegate.info());
    }

    /**
     * 가맹점 기본 정보 조회.
     *
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 가맹점 기본 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse info(String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.info(idempotencyKey));
    }

    /**
     * 가맹점 상세 정보 조회.
     *
     * @return 가맹점 상세 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail() throws Exception {
        return CommerceResponses.of(delegate.detail());
    }

    /**
     * 가맹점 상세 정보 조회.
     *
     * @param idempotencyKey 미지정 시 자동 생성 (Idempotency-Key 헤더)
     * @return 가맹점 상세 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.detail(idempotencyKey));
    }
}
