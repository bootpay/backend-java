package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.UserToken;

import java.util.HashMap;

// 간편결제창, 생체인증 기반 간편 결제 등
public class EasyService {
    static public HashMap<String, Object> getUserToken(BootpayObject bootpay, UserToken userToken) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if (userToken.userId == null || userToken.userId.isEmpty()) {
            throw new Exception("userId 값을 입력해주세요.");
        }

        return bootpay.doPost("request/user/token", userToken);
    }
}
