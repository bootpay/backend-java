package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Authentication;
import kr.co.bootpay.pg.model.request.AuthenticationParams;

import java.util.HashMap;

// REST API 본인인증 전용
public class AuthService {

    private static void validateToken(BootpayObject bootpay) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
    }

    // 본인인증 요청
    static public HashMap<String, Object> requestAuthentication(BootpayObject bootpay, Authentication authentication) throws Exception {
        validateToken(bootpay);
        if (authentication.orderName == null || authentication.orderName.isEmpty()) throw new Exception("order_name 값을 입력해주세요.");
        if (authentication.authenticationId == null || authentication.authenticationId.isEmpty()) throw new Exception("authenticationId 주문번호를 설정해주세요.");
        if (authentication.identityNo == null || authentication.identityNo.isEmpty()) throw new Exception("생년월일 + 주민등록번호 뒷 1자리를 입력해주세요.");

        return bootpay.doPost("request/authentication", authentication);
    }

    // 본인인증 승인
    static public HashMap<String, Object> confirmAuthentication(BootpayObject bootpay, String receiptId, String otp) throws Exception {
        validateToken(bootpay);
        if (receiptId == null || receiptId.isEmpty()) throw new Exception("receiptId 값을 비어있습니다.");
        if (otp == null || otp.isEmpty()) throw new Exception("otp 값을 비어있습니다.");

        AuthenticationParams authenticationParams = new AuthenticationParams();
        authenticationParams.receiptId = receiptId;
        authenticationParams.otp = otp;

        return bootpay.doPost("authenticate/confirm", authenticationParams);
    }

    // SMS로 본인인증 요청시 SMS 재발송 로직
    static public HashMap<String, Object> realarmAuthentication(BootpayObject bootpay, String receiptId) throws Exception {
        validateToken(bootpay);
        if (receiptId == null || receiptId.isEmpty()) throw new Exception("receiptId 값을 비어있습니다.");

        AuthenticationParams authenticationParams = new AuthenticationParams();
        authenticationParams.receiptId = receiptId;

        return bootpay.doPost("authenticate/realarm", authenticationParams);
    }
}
