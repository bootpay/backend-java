package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.payout.PayoutCreateParams;
import kr.co.bootpay.store.model.request.payout.PayoutListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.payouts.SPayoutService;

/** 입점사 지급 요청 모듈 — /v1/sdk/payouts 계열, supervisor 전용. */
public class Payout {
    private final BootpayStore bootpay;

    public Payout(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** 지급 요청 등록. */
    public BootpayStoreResponse create(PayoutCreateParams params) throws Exception {
        return SPayoutService.create(bootpay, params);
    }

    /** @param payoutId merchant_payout_id 또는 지급 항목 id */
    public BootpayStoreResponse detail(String payoutId) throws Exception {
        return detail(payoutId, null);
    }

    /** @param idempotencyKey 미지정시 자동 생성 */
    public BootpayStoreResponse detail(String payoutId, String idempotencyKey) throws Exception {
        return SPayoutService.detail(bootpay, payoutId, idempotencyKey);
    }

    /** 최근 생성 순 지급 목록. */
    public BootpayStoreResponse list() throws Exception {
        return list(null);
    }

    /** 최근 생성 순 지급 목록. */
    public BootpayStoreResponse list(PayoutListParams params) throws Exception {
        return SPayoutService.list(bootpay, params);
    }
}
