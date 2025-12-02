package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;

import java.util.HashMap;

public class VerificationService {
    static public HashMap<String, Object> receipt(BootpayObject bootpay, String receiptId) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        return bootpay.doGet("receipt/" + receiptId);
    }

    static public HashMap<String, Object> certificate(BootpayObject bootpay, String receiptId) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        return bootpay.doGet("certificate/" + receiptId + ".json");
    }
}
