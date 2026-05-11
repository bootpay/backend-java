package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.coupon.CouponDownloadParams;
import kr.co.bootpay.store.model.request.coupon.CouponListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.coupons.SCouponService;

public class Coupon {
    private final BootpayStore bootpay;

    public Coupon(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 사용자 보유 쿠폰 목록
     */
    public BootpayStoreResponse list(CouponListParams params) throws Exception {
        return SCouponService.list(bootpay, params);
    }

    public BootpayStoreResponse list() throws Exception {
        return SCouponService.list(bootpay, null);
    }

    /**
     * 다운로드 가능한 쿠폰 목록
     */
    public BootpayStoreResponse available() throws Exception {
        return SCouponService.available(bootpay);
    }

    /**
     * 쿠폰 다운로드 (issue_from_template)
     */
    public BootpayStoreResponse download(CouponDownloadParams params) throws Exception {
        return SCouponService.download(bootpay, params);
    }
}
