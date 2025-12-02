package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Confirm;

import java.util.HashMap;

public class ConfirmService {
    static public HashMap<String, Object> confirm(BootpayObject bootpay, String receiptId) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if (receiptId == null || receiptId.isEmpty()) {
            throw new Exception("receiptId 값을 입력해주세요.");
        }

        Confirm confirm = new Confirm();
        confirm.receiptId = receiptId;

        return bootpay.doPost("confirm", confirm);
    }
}
