package kr.co.bootpay.pg.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Cancel;
import kr.co.bootpay.pg.model.request.Payload;
import kr.co.bootpay.pg.model.response.ResDefault;
import kr.co.bootpay.pg.service.CancelService;
import kr.co.bootpay.pg.service.ConfirmService;
import kr.co.bootpay.pg.service.LinkService;
import kr.co.bootpay.pg.service.PaymentService;
import kr.co.bootpay.pg.service.SellerService;
import kr.co.bootpay.pg.service.VerificationService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 결제 조회 / 승인 / 취소 모듈.
 *
 * <pre>{@code
 * BootpayResponse res = bootpay.payment.get(receiptId);
 * bootpay.payment.confirm(receiptId);
 * bootpay.payment.cancel(cancel);
 * }</pre>
 *
 * @since 3.3.0
 */
public class PaymentModule {

    private final BootpayObject bootpay;

    public PaymentModule(BootpayObject bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 결제 단건 조회.
     *
     * @param receiptId 부트페이 영수증 id
     * @return 결제 건 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse get(String receiptId) throws Exception {
        return BootpayResponse.ofPg(VerificationService.receipt(bootpay, receiptId));
    }

    /**
     * 결제 단건 조회 (구매자 정보 포함 여부 지정).
     *
     * @param receiptId      부트페이 영수증 id
     * @param lookupUserData 구매자 정보를 함께 조회할지 여부
     * @return 결제 건 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse get(String receiptId, boolean lookupUserData) throws Exception {
        return BootpayResponse.ofPg(VerificationService.receipt(bootpay, receiptId, lookupUserData));
    }

    /**
     * 개발사 주문번호(order_id) 로 결제 건 조회.
     *
     * @param orderId 개발사에서 관리하는 고유 주문번호
     * @return 결제 건 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse getByOrderId(String orderId) throws Exception {
        return BootpayResponse.ofPg(PaymentService.lookupOrderId(bootpay, orderId));
    }

    /**
     * 결제 승인 (수동 승인 건).
     *
     * @param receiptId 부트페이 영수증 id
     * @return 승인된 결제 건 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse confirm(String receiptId) throws Exception {
        return BootpayResponse.ofPg(ConfirmService.confirm(bootpay, receiptId));
    }

    /**
     * 결제 취소 (전체 / 부분).
     *
     * @param cancel 취소 요청 정보
     * @return 취소된 결제 건 정보
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse cancel(Cancel cancel) throws Exception {
        return BootpayResponse.ofPg(CancelService.receiptCancel(bootpay, cancel));
    }

    /**
     * 결제 링크 발급.
     *
     * @param payload 결제 요청 정보
     * @return 발급된 결제 링크 (본문의 {@code data} 키)
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse link(Payload payload) throws Exception {
        ResDefault<String> res = LinkService.requestLink(bootpay, payload);
        if (res == null) {
            return BootpayResponse.of(false, null, null, "응답이 비어있습니다.", null);
        }
        Map<String, Object> raw = new LinkedHashMap<String, Object>();
        raw.put("status", res.status);
        raw.put("error_code", res.error_code == 0 ? null : res.error_code);
        raw.put("message", res.message);
        raw.put("data", res.data);

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("data", res.data);

        boolean success = res.error_code == 0;
        return BootpayResponse.of(success, data, success ? null : res.error_code, res.message, raw);
    }

    /**
     * 가맹점에 설정된 결제수단 목록 조회.
     *
     * @return 결제수단 목록
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse methods() throws Exception {
        return BootpayResponse.ofPg(SellerService.lookupPaymentMethods(bootpay));
    }
}
