package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Cancel;
import kr.co.bootpay.pg.model.request.CashReceipt;

import java.util.HashMap;

public class CashService {

    private static void validateToken(BootpayObject bootpay) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
    }

    // 현금 영수증 발행하기 (부트페이 결제건)
    static public HashMap<String, Object> requestCashReceiptByBootpay(BootpayObject bootpay, CashReceipt cashReceipt) throws Exception {
        validateToken(bootpay);
        if (cashReceipt.receiptId == null || cashReceipt.receiptId.isEmpty()) throw new Exception("receiptId 값을 입력해주세요.");
        if (cashReceipt.username == null || cashReceipt.username.isEmpty()) throw new Exception("username 값을 입력해주세요.");
        if (cashReceipt.phone == null || cashReceipt.phone.isEmpty()) throw new Exception("phone 값을 입력해주세요.");
        if (cashReceipt.identityNo == null || cashReceipt.identityNo.isEmpty()) throw new Exception("identityNo 값을 입력해주세요.");
        if (cashReceipt.cashReceiptType == null || cashReceipt.cashReceiptType.isEmpty()) throw new Exception("cashReceiptType 값을 입력해주세요.");

        return bootpay.doPost("request/receipt/cash/publish", cashReceipt);
    }

    // 현금 영수증 발행 취소하기 (부트페이 결제건)
    static public HashMap<String, Object> requestCashReceiptCancelByBootpay(BootpayObject bootpay, Cancel cancel) throws Exception {
        validateToken(bootpay);
        if (cancel == null || cancel.receiptId == null || cancel.receiptId.isEmpty()) throw new Exception("receiptId 값이 비어있습니다.");
        if (cancel.cancelUsername == null || cancel.cancelUsername.isEmpty()) throw new Exception("cancelUsername 값이 비어있습니다.");
        if (cancel.cancelMessage == null || cancel.cancelMessage.isEmpty()) throw new Exception("cancelMessage 값이 비어있습니다.");

        return bootpay.doDeleteWithBody("request/receipt/cash/cancel/" + cancel.receiptId, cancel);
    }

    // 현금 영수증 발행하기 (별건)
    static public HashMap<String, Object> requestCashReceipt(BootpayObject bootpay, CashReceipt cashReceipt) throws Exception {
        validateToken(bootpay);
        if (cashReceipt == null) throw new Exception("cashReceipt 모델이 비어있습니다. 데이터를 채워주세요");
        if (cashReceipt.pg == null || cashReceipt.pg.isEmpty()) throw new Exception("pg 값을 입력해주세요.");
        if (cashReceipt.orderName == null || cashReceipt.orderName.isEmpty()) throw new Exception("orderName 값을 입력해주세요.");
        if (cashReceipt.orderId == null || cashReceipt.orderId.isEmpty()) throw new Exception("orderId 값을 입력해주세요.");
        if (cashReceipt.identityNo == null || cashReceipt.identityNo.isEmpty()) throw new Exception("identityNo 값을 입력해주세요.");
        if (cashReceipt.cashReceiptType == null || cashReceipt.cashReceiptType.isEmpty()) throw new Exception("cashReceiptType 값을 입력해주세요.");

        return bootpay.doPost("request/cash/receipt", cashReceipt);
    }

    // 현금영수증 발행 취소 (별건)
    static public HashMap<String, Object> requestCashReceiptCancel(BootpayObject bootpay, Cancel cancel) throws Exception {
        validateToken(bootpay);
        if (cancel == null || cancel.receiptId == null || cancel.receiptId.isEmpty()) throw new Exception("receiptId 값이 비어있습니다.");
        if (cancel.cancelUsername == null || cancel.cancelUsername.isEmpty()) throw new Exception("cancelUsername 값이 비어있습니다.");
        if (cancel.cancelMessage == null || cancel.cancelMessage.isEmpty()) throw new Exception("cancelMessage 값이 비어있습니다.");

        return bootpay.doDeleteWithBody("request/cash/receipt/" + cancel.receiptId, cancel);
    }
}
