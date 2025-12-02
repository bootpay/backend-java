package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;

import java.util.HashMap;

public class PaymentService {
    static public HashMap<String, Object> lookupOrderId(BootpayObject bootpay, String orderId) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if (orderId == null || orderId.isEmpty()) {
            throw new Exception("orderId 값을 입력해주세요.");
        }

        return bootpay.doGetArray("lookup/order/" + orderId);
    }
}
