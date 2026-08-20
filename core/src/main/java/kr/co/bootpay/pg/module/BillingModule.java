package kr.co.bootpay.pg.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Subscribe;
import kr.co.bootpay.pg.model.request.SubscribePayload;
import kr.co.bootpay.pg.service.BillingService;

/**
 * 빌링키 발급 / 정기결제 모듈.
 *
 * <pre>{@code
 * BootpayResponse issued = bootpay.billing.issue(subscribe);
 * bootpay.billing.pay(subscribePayload);
 * bootpay.billing.destroy(billingKey);
 * }</pre>
 *
 * @since 3.3.0
 */
public class BillingModule {

    private final BootpayObject bootpay;

    public BillingModule(BootpayObject bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 카드 빌링키 발급.
     *
     * @param subscribe 빌링키 발급 요청 정보
     * @return 발급된 빌링키 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse issue(Subscribe subscribe) throws Exception {
        return BootpayResponse.ofPg(BillingService.getBillingKey(bootpay, subscribe));
    }

    /**
     * 계좌 자동이체 빌링키 발급 요청.
     *
     * @param subscribe 자동이체 발급 요청 정보
     * @return 발급 요청 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse issueTransfer(Subscribe subscribe) throws Exception {
        return BootpayResponse.ofPg(BillingService.getBillingKeyTransfer(bootpay, subscribe));
    }

    /**
     * 계좌 자동이체 빌링키 발행 확정.
     *
     * @param receiptId 발급 요청 시 받은 영수증 id
     * @return 발행된 빌링키 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse publishTransfer(String receiptId) throws Exception {
        return BootpayResponse.ofPg(BillingService.publishBillingKeyTransfer(bootpay, receiptId));
    }

    /**
     * 빌링키 조회 (빌링키 값 기준).
     *
     * @param billingKey 빌링키
     * @return 빌링키 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse get(String billingKey) throws Exception {
        return BootpayResponse.ofPg(BillingService.lookupBillingKeyByKey(bootpay, billingKey));
    }

    /**
     * 빌링키 조회 (발급 영수증 id 기준).
     *
     * @param receiptId 빌링키 발급 시 받은 영수증 id
     * @return 빌링키 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse getByReceiptId(String receiptId) throws Exception {
        return BootpayResponse.ofPg(BillingService.lookupBillingKey(bootpay, receiptId));
    }

    /**
     * 우선순위(순차) 결제 빌링키 조회.
     *
     * @param widgetKey  위젯 키
     * @param billingKey 빌링키
     * @param userId     조회 대상 회원 ID (서버가 빌링키 소유자 검증에 사용)
     * @return 빌링키 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse getSequential(String widgetKey, String billingKey, String userId) throws Exception {
        return BootpayResponse.ofPg(BillingService.lookupSequentialBillingKey(bootpay, widgetKey, billingKey, userId));
    }

    /**
     * 빌링키 삭제.
     *
     * @param billingKey 빌링키
     * @return 삭제 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse destroy(String billingKey) throws Exception {
        return BootpayResponse.ofPg(BillingService.destroyBillingKey(bootpay, billingKey));
    }

    /**
     * 빌링키로 즉시 결제.
     *
     * @param payload 정기결제 요청 정보
     * @return 결제 건 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse pay(SubscribePayload payload) throws Exception {
        return BootpayResponse.ofPg(BillingService.requestSubscribe(bootpay, payload));
    }

    /**
     * 빌링키로 예약 결제 등록.
     *
     * @param payload 예약 결제 요청 정보 (reserve_execute_at 필수)
     * @return 예약 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse reserve(SubscribePayload payload) throws Exception {
        return BootpayResponse.ofPg(BillingService.reserveSubscribe(bootpay, payload));
    }

    /**
     * 예약 결제 조회.
     *
     * @param reserveId 예약 id
     * @return 예약 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse getReserve(String reserveId) throws Exception {
        return BootpayResponse.ofPg(BillingService.reserveSubscribeLookup(bootpay, reserveId));
    }

    /**
     * 예약 결제 취소.
     *
     * @param reserveId 예약 id
     * @return 취소 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse cancelReserve(String reserveId) throws Exception {
        return BootpayResponse.ofPg(BillingService.reserveCancelSubscribe(bootpay, reserveId));
    }
}
