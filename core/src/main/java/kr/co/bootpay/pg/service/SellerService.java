package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;

import java.util.HashMap;

// 판매자 관련 서비스
public class SellerService {
    static public HashMap<String, Object> lookupPaymentMethods(BootpayObject bootpay) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        return bootpay.doGetArray("seller/payment/method");
    }
}
