package kr.co.bootpay.store.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.layer.Deposit;
import kr.co.bootpay.store.model.request.deposit.DepositCreateParams;

/**
 * 뱅크알람 입금대기 모듈 — 인스턴스 role 과 무관하게 supervisor 전용입니다.
 *
 * @since 3.8.0
 */
public class DepositModule {
    private final Deposit delegate;

    public DepositModule(BootpayStore bootpay) {
        this.delegate = new Deposit(bootpay);
    }

    /**
     * 판매자 입금 계좌 목록.
     *
     * @return 입금 계좌 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse accounts() throws Exception {
        return CommerceResponses.of(delegate.accounts());
    }

    /**
     * 웹훅 서명 비밀값 발급 — 원문은 이 응답에서만 받습니다.
     *
     * @return 웹훅 서명 비밀값
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse webhookSecret() throws Exception {
        return CommerceResponses.of(delegate.webhookSecret());
    }

    /**
     * 웹훅 서명 비밀값 발급 (Idempotency-Key 지정).
     *
     * @param idempotencyKey 미지정시 자동 생성 (Idempotency-Key 헤더)
     * @return 웹훅 서명 비밀값
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse webhookSecret(String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.webhookSecret(idempotencyKey));
    }

    /**
     * 입금대기 등록 — merchantOrderId · price · depositor 필수.
     * 같은 merchantOrderId 로 금액·입금자명·계좌가 같은 재호출은 기존 주문을 돌려주고, 다르면 18110 이다.
     *
     * @param params 입금대기 주문 정보
     * @return 입금대기 주문
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse create(DepositCreateParams params) throws Exception {
        return CommerceResponses.of(delegate.create(params));
    }

    /**
     * 입금대기 조회 — 가맹점 주문번호 또는 부트페이 주문번호.
     *
     * @param depositId 가맹점 주문번호 또는 부트페이 주문번호
     * @return 입금대기 주문
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse detail(String depositId) throws Exception {
        return CommerceResponses.of(delegate.detail(depositId));
    }

    /**
     * 입금대기 취소 — 이미 닫힌 주문은 그대로 반환합니다.
     *
     * @param depositId 가맹점 주문번호 또는 부트페이 주문번호
     * @return 입금대기 주문
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse cancel(String depositId) throws Exception {
        return CommerceResponses.of(delegate.cancel(depositId));
    }

    /**
     * 입금대기 취소 (Idempotency-Key 지정).
     *
     * @param depositId 가맹점 주문번호 또는 부트페이 주문번호
     * @param idempotencyKey 미지정시 자동 생성 (Idempotency-Key 헤더)
     * @return 입금대기 주문
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse cancel(String depositId, String idempotencyKey) throws Exception {
        return CommerceResponses.of(delegate.cancel(depositId, idempotencyKey));
    }
}
