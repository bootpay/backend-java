package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Token;

import java.util.HashMap;

public class TokenService {
    static public HashMap<String, Object> getAccessToken(BootpayObject bootpay) throws Exception {
        boolean hasClientKey = bootpay.client_key != null && !bootpay.client_key.isEmpty();
        boolean hasSecretKey = bootpay.secret_key != null && !bootpay.secret_key.isEmpty();

        // client_key/secret_key 인증은 매 요청에 Basic Auth 헤더가 자동 부착된다.
        // request/token 호출이 불필요하므로 합성 응답을 즉시 반환한다.
        if (hasClientKey) {
            if (!hasSecretKey) {
                throw new Exception("secret_key 값이 비어있습니다.");
            }
            bootpay.token = null;
            HashMap<String, Object> result = new HashMap<>();
            result.put("access_token", "");
            result.put("expire_in", 0);
            return result;
        }

        if (bootpay.application_id == null || bootpay.application_id.isEmpty()) {
            throw new Exception("application_id 값이 비어있습니다.");
        }
        if (bootpay.private_key == null || bootpay.private_key.isEmpty()) {
            throw new Exception("private_key 값이 비어있습니다.");
        }
        Token token = new Token();
        token.application_id = bootpay.application_id;
        token.private_key = bootpay.private_key;

        HashMap<String, Object> result = bootpay.doPostWithoutAuth("request/token", token);

        // 발급된 토큰을 bootpay 객체에 설정
        if (result != null) {
            Object accessToken = result.get("access_token");
            if (accessToken instanceof String) {
                bootpay.token = (String) accessToken;
            } else {
                bootpay.token = null;
            }
        }

        return result;
    }
}
