package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.deposit.DepositCreateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.deposits.SDepositService;

/** 뱅크알람 입금대기 모듈 — /v1/sdk/deposits 계열, supervisor 전용. */
public class Deposit {
    private final BootpayStore bootpay;

    public Deposit(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    /** 판매자 입금 계좌 목록. */
    public BootpayStoreResponse accounts() throws Exception {
        return accounts(null);
    }

    /** @param idempotencyKey 미지정시 자동 생성 */
    public BootpayStoreResponse accounts(String idempotencyKey) throws Exception {
        return SDepositService.accounts(bootpay, idempotencyKey);
    }

    /** 웹훅 서명 비밀값 발급 — 원문은 이 응답에서만 받습니다. */
    public BootpayStoreResponse webhookSecret() throws Exception {
        return webhookSecret(null);
    }

    /** @param idempotencyKey 미지정시 자동 생성 */
    public BootpayStoreResponse webhookSecret(String idempotencyKey) throws Exception {
        return SDepositService.webhookSecret(bootpay, idempotencyKey);
    }

    /** 입금대기 등록 — 같은 merchantOrderId 로 금액·입금자명·계좌가 같은 재호출은 기존 주문을 반환합니다(다르면 18110). */
    public BootpayStoreResponse create(DepositCreateParams params) throws Exception {
        return SDepositService.create(bootpay, params);
    }

    /** @param depositId 가맹점 주문번호 또는 부트페이 주문번호 */
    public BootpayStoreResponse detail(String depositId) throws Exception {
        return detail(depositId, null);
    }

    /**
     * @param depositId 가맹점 주문번호 또는 부트페이 주문번호
     * @param idempotencyKey 미지정시 자동 생성
     */
    public BootpayStoreResponse detail(String depositId, String idempotencyKey) throws Exception {
        return SDepositService.detail(bootpay, depositId, idempotencyKey);
    }

    /** 입금대기 취소 — 이미 닫힌 주문은 그대로 반환합니다. */
    public BootpayStoreResponse cancel(String depositId) throws Exception {
        return cancel(depositId, null);
    }

    /** @param idempotencyKey 미지정시 자동 생성 */
    public BootpayStoreResponse cancel(String depositId, String idempotencyKey) throws Exception {
        return SDepositService.cancel(bootpay, depositId, idempotencyKey);
    }
}
