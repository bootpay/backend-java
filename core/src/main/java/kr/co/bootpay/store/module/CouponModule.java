package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Coupon;
import kr.co.bootpay.store.model.request.coupon.CouponDownloadParams;
import kr.co.bootpay.store.model.request.coupon.CouponListParams;

/**
 * 쿠폰 모듈.
 *
 * @since 3.3.0
 */
public class CouponModule {

    private final Coupon delegate;

    public CouponModule(BootpayStore bootpay) {
        this.delegate = new Coupon(bootpay);
    }

    /**
     * 쿠폰 목록 조회.
     *
     * @param params 조회 조건
     * @return 쿠폰 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list(CouponListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }

    /**
     * 쿠폰 목록 조회 (조건 없이).
     *
     * @return 쿠폰 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /**
     * 다운로드 가능한 쿠폰 목록 조회.
     *
     * @return 쿠폰 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse available() throws Exception {
        return CommerceResponses.of(delegate.available());
    }

    /**
     * 쿠폰 다운로드.
     *
     * @param params 다운로드 정보
     * @return 다운로드 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse download(CouponDownloadParams params) throws Exception {
        return CommerceResponses.of(delegate.download(params));
    }
}
