package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Token;

import java.util.HashMap;

public class TokenService {
    static public HashMap<String, Object> getAccessToken(BootpayObject bootpay) throws Exception {
        if (bootpay.application_id == null || bootpay.application_id.isEmpty()) {
            throw new Exception("application_id 값이 비어있습니다.");
        }
        if (bootpay.private_key == null || bootpay.private_key.isEmpty()) {
            throw new Exception("private_key 값이 비어있습니다.");
        }

        Token token = new Token();
        token.application_id = bootpay.application_id;
        token.private_key = bootpay.private_key;

        // 토큰 발급은 인증 없이 호출
        HashMap<String, Object> result = bootpay.doPostWithoutAuth("request/token.json", token);

        // 발급된 토큰을 bootpay 객체에 설정
        if (result != null && result.containsKey("access_token")) {
            bootpay.token = (String) result.get("access_token");
        }

        return result;
    }
}
