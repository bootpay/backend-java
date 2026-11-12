package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Payout;
import kr.co.bootpay.store.model.request.payout.PayoutCreateParams;
import kr.co.bootpay.store.model.request.payout.PayoutListParams;

/**
 * 입점사 지급 요청 모듈 — 인스턴스 role 과 무관하게 supervisor 전용입니다.
 *
 * @since 3.8.0
 */
public class PayoutModule {
    private final Payout delegate;

    public PayoutModule(BootpayStore bootpay) {
        this.delegate = new Payout(bootpay);
    }

    /** 지급 요청 등록. */
    public BootpayResponse create(PayoutCreateParams params) throws Exception {
        return CommerceResponses.of(delegate.create(params));
    }

    /** @param payoutId merchant_payout_id 또는 지급 항목 id */
    public BootpayResponse detail(String payoutId) throws Exception {
        return CommerceResponses.of(delegate.detail(payoutId));
    }

    /** 지급 항목 조회 (Idempotency-Key 지정). */
    public BootpayResponse detail(String payoutId, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.detail(payoutId, idempotencyKey));
    }

    /** 최근 생성 순 지급 목록. */
    public BootpayResponse list() throws Exception {
        return CommerceResponses.of(delegate.list());
    }

    /** 최근 생성 순 지급 목록. */
    public BootpayResponse list(PayoutListParams params) throws Exception {
        return CommerceResponses.of(delegate.list(params));
    }
}
