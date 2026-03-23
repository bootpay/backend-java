package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.WalletPayment;

import java.util.HashMap;

public class WalletService {

    private static void validateToken(BootpayObject bootpay) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
    }

    static public HashMap<String, Object> getUserWallets(BootpayObject bootpay, String userId, boolean sandbox) throws Exception {
        validateToken(bootpay);
        if (userId == null || userId.isEmpty()) {
            throw new Exception("userId 값을 입력해주세요.");
        }

        String sandboxStr = sandbox ? "true" : "false";
        return bootpay.doGet("wallet?user_id=" + userId + "&sandbox=" + sandboxStr);
    }

    static public HashMap<String, Object> requestWalletPayment(BootpayObject bootpay, WalletPayment walletPayment) throws Exception {
        validateToken(bootpay);
        if (walletPayment.userId == null || walletPayment.userId.isEmpty()) throw new Exception("userId 값을 입력해주세요.");
        if (walletPayment.orderName == null || walletPayment.orderName.isEmpty()) throw new Exception("order_name 값을 입력해주세요.");
        if (walletPayment.price == null || walletPayment.price <= 0) throw new Exception("price 금액을 설정을 해주세요.");
        if (walletPayment.orderId == null || walletPayment.orderId.isEmpty()) throw new Exception("order_id 주문번호를 설정해주세요.");

        return bootpay.doPost("wallet/payment", walletPayment);
    }
}
