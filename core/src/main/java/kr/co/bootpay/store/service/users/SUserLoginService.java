package kr.co.bootpay.store.service.users;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.context.RequestContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.util.HashMap;
import java.util.Map;

public class SUserLoginService {

    static public BootpayStoreResponse token(BootpayStoreObject bootpay, String userId) throws Exception {
        return token(bootpay, userId, "", "", null);
    }

    static public BootpayStoreResponse token(BootpayStoreObject bootpay, String userId, String corporateType, String membershipType) throws Exception {
        return token(bootpay, userId, corporateType, membershipType, null);
    }

    static public BootpayStoreResponse token(BootpayStoreObject bootpay, String userId, String corporateType, String membershipType, RequestContext context) throws Exception {
        bootpay.requireCommerceCredentials();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        if(corporateType != null && !corporateType.isEmpty()) {
            params.put("corporate_type", corporateType);
        }
        if(membershipType != null && !membershipType.isEmpty()) {
            params.put("membership_type", membershipType);
        }

        HttpPost post = bootpay.httpPost("users/login/token", new StringEntity(gson.toJson(params), "UTF-8"), context);

        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }


    static public BootpayStoreResponse login(BootpayStoreObject bootpay, String loginId, String loginPw) throws Exception {
        return login(bootpay, loginId, loginPw, null);
    }

    static public BootpayStoreResponse login(BootpayStoreObject bootpay, String loginId, String loginPw, RequestContext context) throws Exception {
        bootpay.requireCommerceCredentials();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Map<String, Object> params = new HashMap<>();
        params.put("login_id", loginId);
        params.put("login_pw", loginPw);


        HttpPost post = bootpay.httpPost("users/login", new StringEntity(gson.toJson(params), "UTF-8"), context);

        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 회원 로그인 (V1 Mall API)
     * POST /v1/users/login
     * ⚠️ 서버(LoginService)는 login_id/password 만 읽는다. corporate_type 은 전달돼도 무시된다.
     * @param corporateType 미지정(null)시 0
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse userLogin(BootpayStoreObject bootpay, String loginId, String password, Integer corporateType, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Map<String, Object> params = new HashMap<>();
        params.put("login_id", loginId);
        params.put("password", password);
        params.put("corporate_type", corporateType == null ? 0 : corporateType);

        HttpPost post = bootpay.httpPost("users/login", new StringEntity(gson.toJson(params), "UTF-8"), mallContext(null, idempotencyKey));

        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 회원 세션 조회 (V1 Mall API)
     * GET /v1/users/session
     * @param userJwt 로그인시 발급받은 회원 JWT (Bootpay-User-JWT 헤더로 전송)
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse userSession(BootpayStoreObject bootpay, String userJwt, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpGet get = bootpay.httpGet("users/session", mallContext(userJwt, idempotencyKey));

        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 회원 로그아웃 (V1 Mall API)
     * DELETE /v1/users/session
     * @param userJwt 로그인시 발급받은 회원 JWT (Bootpay-User-JWT 헤더로 전송)
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse userLogout(BootpayStoreObject bootpay, String userJwt, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpDelete delete = bootpay.httpDelete("users/session", mallContext(userJwt, idempotencyKey));

        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * V1 Mall API 요청 컨텍스트
     * Idempotency-Key 는 미지정시 매 호출마다 생성되고, Bootpay-User-JWT 는 값이 있을 때만 붙는다.
     */
    static RequestContext mallContext(String userJwt, String idempotencyKey) {
        return RequestContext.builder()
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .userJwt(userJwt)
                .build();
    }
}
