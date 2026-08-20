package kr.co.bootpay.pg.module;

import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Shipping;
import kr.co.bootpay.pg.service.EscrowService;

/**
 * 에스크로 배송 모듈.
 *
 * <pre>{@code
 * bootpay.escrow.shippingStart(shipping);
 * }</pre>
 *
 * @since 3.3.0
 */
public class EscrowModule {

    private final BootpayObject bootpay;

    public EscrowModule(BootpayObject bootpay) {
        this.bootpay = bootpay;
    }

    /**
     * 배송 시작 등록.
     *
     * @param shipping 배송 정보 (송장번호 / 택배사 필수)
     * @return 등록 결과
     * @throws Exception 통신 실패 또는 인증 정보 누락
     */
    public BootpayResponse shippingStart(Shipping shipping) throws Exception {
        return BootpayResponse.ofPg(EscrowService.shippingStart(bootpay, shipping));
    }
}
