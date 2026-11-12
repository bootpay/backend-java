package kr.co.bootpay.store.model.request.payout;

/** 입점사 지급 요청 파라미터 (POST /v1/sdk/payouts) — supervisor 전용. */
public class PayoutCreateParams {
    /** 이 프로젝트에 속한 입점사 ID (필수) */
    public String subSellerId;
    /** 지급액 — 1원 이상 정수 (필수) */
    public Long amount;
    /** 가맹점 지급 번호이자 프로젝트별 멱등 키 (필수) */
    public String merchantPayoutId;
    /** 가맹점 주문 번호 */
    public String orderNumber;
    /** 지급 예정일 (YYYY-MM-DD, 미지정시 오늘) */
    public String payoutDate;
    /** 미지정시 자동 생성 (Idempotency-Key 헤더) */
    public String idempotencyKey;
}
