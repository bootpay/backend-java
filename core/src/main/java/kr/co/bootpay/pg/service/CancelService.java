package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Cancel;

import java.util.HashMap;

public class CancelService {
    static public HashMap<String, Object> receiptCancel(BootpayObject bootpay, Cancel cancel) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if (cancel.receiptId == null || cancel.receiptId.isEmpty()) {
            throw new Exception("receiptId 값을 입력해주세요.");
        }

        return bootpay.doPost("cancel", cancel);
    }
}
