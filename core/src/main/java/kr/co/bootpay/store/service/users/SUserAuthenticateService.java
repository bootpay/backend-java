package kr.co.bootpay.store.service.users;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.net.URLEncoder;

public class SUserAuthenticateService {

    /**
     * 본인인증(가입 인증 대기) 데이터 조회
     *
     * @deprecated userAuthenticate 를 쓴다 (같은 엔드포인트 GET /v1/users/authenticate/{id}). 호환용으로 남겨 두며 userAuthenticate 로 위임한다.
     */
    @Deprecated
    static public BootpayStoreResponse authenticationData(BootpayStoreObject bootpay, String standId) throws Exception {
        return userAuthenticate(bootpay, standId, null, null);
    }

    /**
     * 가입 이메일 인증 대기 상태 조회
     * GET /v1/users/authenticate/{userStandbyId}?verification_context={verificationContext}
     * 서버는 이 조회에 스코프 검사를 걸지 않는다(기존 계약 유지). role 은 다른 회원 API 와 맞춰 user 로 보낸다.
     * @param userStandbyId 가입 대기 ID
     * @param verificationContext 선택 — null/빈 값이면 전송하지 않는다
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse userAuthenticate(BootpayStoreObject bootpay, String userStandbyId, String verificationContext, String idempotencyKey) throws Exception {
        HttpGet get = userAuthenticateRequest(bootpay, userStandbyId, verificationContext, idempotencyKey);
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 가입 이메일 인증 대기 상태 조회 요청을 구성한다 (전송하지 않는다).
     */
    static HttpGet userAuthenticateRequest(BootpayStoreObject bootpay, String userStandbyId, String verificationContext, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();
        if (userStandbyId == null || userStandbyId.isEmpty()) throw new Exception("userStandbyId 값이 비어있습니다.");

        String url = "users/authenticate/" + userStandbyId;
        if (verificationContext != null && !verificationContext.isEmpty()) {
            url += "?verification_context=" + URLEncoder.encode(verificationContext, "UTF-8");
        }
        return bootpay.httpGet(url, RequestContext.builder()
                .role("user")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build());
    }
}
