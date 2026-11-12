package kr.co.bootpay.store.model.request.payout;

import kr.co.bootpay.store.model.response.payout.CommercePayout;

/** 입점사 지급 목록 조회 파라미터 (GET /v1/sdk/payouts) — 최근 생성 순. */
public class PayoutListParams {
    public CommercePayout.Status status;
    public String subSellerId;
    /** 지급 예정일 시작 (YYYY-MM-DD, 포함) */
    public String from;
    /** 지급 예정일 끝 (YYYY-MM-DD, 포함) */
    public String to;
    public Integer page;
    public Integer limit;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
