package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Cancel;
import kr.co.bootpay.pg.model.request.CashReceipt;

import java.util.HashMap;

public class CashService {

    private static void validateToken(BootpayObject bootpay) throws Exception {
        if (!bootpay.hasAuth()) {
            throw new Exception("token 값이 비어있습니다.");
        }
    }

    // 현금 영수증 발행하기 (부트페이 결제건)
    static public HashMap<String, Object> requestCashReceiptByBootpay(BootpayObject bootpay, CashReceipt cashReceipt) throws Exception {
        validateToken(bootpay);
        // username·phone·cancel_username·cancel_message 는 선택값이다 (API 스펙·nodejs parity) — 필수 검사하지 않는다
        if (cashReceipt == null) throw new Exception("cashReceipt 모델이 비어있습니다. 데이터를 채워주세요");
        if (cashReceipt.receiptId == null || cashReceipt.receiptId.isEmpty()) throw new Exception("receiptId 값을 입력해주세요.");
        if (cashReceipt.identityNo == null || cashReceipt.identityNo.isEmpty()) throw new Exception("identityNo 값을 입력해주세요.");
        if (cashReceipt.cashReceiptType == null || cashReceipt.cashReceiptType.isEmpty()) throw new Exception("cashReceiptType 값을 입력해주세요.");

        return bootpay.doPost("request/receipt/cash/publish", cashReceipt);
    }

    // 현금 영수증 발행 취소하기 (부트페이 결제건)
    static public HashMap<String, Object> requestCashReceiptCancelByBootpay(BootpayObject bootpay, Cancel cancel) throws Exception {
        validateToken(bootpay);
        if (cancel == null || cancel.receiptId == null || cancel.receiptId.isEmpty()) throw new Exception("receiptId 값이 비어있습니다.");

        return bootpay.doDeleteWithBody("request/receipt/cash/cancel/" + cancel.receiptId, cancel);
    }

    // 현금 영수증 발행하기 (별건)
    // pg 값을 생략하면 기본 PG사로 발행된다
    static public HashMap<String, Object> requestCashReceipt(BootpayObject bootpay, CashReceipt cashReceipt) throws Exception {
        validateToken(bootpay);
        if (cashReceipt == null) throw new Exception("cashReceipt 모델이 비어있습니다. 데이터를 채워주세요");
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

        return bootpay.doDeleteWithBody("request/cash/receipt/" + cancel.receiptId, cancel);
    }
}
