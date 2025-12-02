package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Shipping;

import java.util.HashMap;

// 에스크로 서비스
public class EscrowService {
    static public HashMap<String, Object> shippingStart(BootpayObject bootpay, Shipping shipping) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        return bootpay.doPut("escrow/shipping/start/" + shipping.receiptId, shipping);
    }
}
