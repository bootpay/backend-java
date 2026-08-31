package kr.co.bootpay.pg.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Cancel;
import kr.co.bootpay.pg.model.request.CashReceipt;
import kr.co.bootpay.pg.service.CashService;

/**
 * 현금영수증 모듈.
 *
 * <pre>{@code
 * bootpay.cash.request(cashReceipt);
 * bootpay.cash.cancel(cancel);
 * }</pre>
 *
 * @since 3.3.0
 */
public class CashModule {

    private final BootpayObject bootpay;

    public CashModule(BootpayObject bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 현금영수증 단독 발행 (결제 건과 무관하게 직접 발행).
     *
     * @param cashReceipt 발행 요청 정보 (pg 는 선택값이며, 생략하면 기본 PG사로 발행된다)
     * @return 발행 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse request(CashReceipt cashReceipt) throws Exception {
        return BootpayResponse.ofPg(CashService.requestCashReceipt(bootpay, cashReceipt));
    }

    /**
     * 현금영수증 단독 발행 건 취소.
     *
     * @param cancel 취소 요청 정보
     * @return 취소 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse cancel(Cancel cancel) throws Exception {
        return BootpayResponse.ofPg(CashService.requestCashReceiptCancel(bootpay, cancel));
    }

    /**
     * 부트페이 결제 건에 대한 현금영수증 발행.
     *
     * @param cashReceipt 발행 요청 정보 (receipt_id 필수)
     * @return 발행 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse requestByBootpay(CashReceipt cashReceipt) throws Exception {
        return BootpayResponse.ofPg(CashService.requestCashReceiptByBootpay(bootpay, cashReceipt));
    }

    /**
     * 부트페이 결제 건에 대한 현금영수증 발행 취소.
     *
     * @param cancel 취소 요청 정보 (receipt_id 필수)
     * @return 취소 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse cancelByBootpay(Cancel cancel) throws Exception {
        return BootpayResponse.ofPg(CashService.requestCashReceiptCancelByBootpay(bootpay, cancel));
    }
}
