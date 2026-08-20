package kr.co.bootpay.store.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SToken;
import kr.co.bootpay.store.model.response.STokenResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;

public class STokenService {
    static public BootpayStoreResponse getAccessToken(BootpayStoreObject bootpay) throws Exception {

        if(bootpay.tokenPayload == null) {
            throw new Exception("tokenPayload 값이 비어있습니다.");
        }
        boolean clientKeyEmpty = bootpay.tokenPayload.clientKey == null || bootpay.tokenPayload.clientKey.isEmpty();
        boolean secretKeyEmpty = bootpay.tokenPayload.secretKey == null || bootpay.tokenPayload.secretKey.isEmpty();
        boolean privateKeyEmpty = bootpay.tokenPayload.privateKey == null || bootpay.tokenPayload.privateKey.isEmpty();

        SToken token = new SToken();
        if(clientKeyEmpty || secretKeyEmpty) {
            if(secretKeyEmpty && privateKeyEmpty) {
                if(clientKeyEmpty) throw new Exception("clientKey 값이 비어있습니다.");
                else throw new Exception("secretKey 값이 비어있습니다.");
            }
            if(secretKeyEmpty) throw new Exception("secretKey 값이 비어있습니다.");
            if(privateKeyEmpty) throw new Exception("privateKey 값이 비어있습니다.");
            token.secretKey = bootpay.tokenPayload.secretKey;
            token.privateKey = bootpay.tokenPayload.privateKey;
        }
        bootpay.setTokenFromAPI(null);

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("request/token", new StringEntity(gson.toJson(token), "UTF-8"));
        HttpResponse response = client.execute(post);

        // 한 번만 스트림을 읽어 두 가지 용도로 활용한다 — 두 번째 read 는 closed stream 예외를 던진다.
        int httpStatus = response.getStatusLine().getStatusCode();
        String body = response.getEntity() != null && response.getEntity().getContent() != null
                ? IOUtils.toString(response.getEntity().getContent(), "UTF-8") : "";

        if (body == null || body.trim().isEmpty() || "null".equalsIgnoreCase(body.trim())) {
            return new BootpayStoreResponse(httpStatus, httpStatus >= 200 && httpStatus < 300, null, null);
        }

        STokenResponse res = new Gson().fromJson(body, STokenResponse.class);
        if (res != null && res.access_token != null && !res.access_token.isEmpty()) {
            bootpay.setTokenFromAPI(res.access_token);
        }

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> data;
        try {
            data = mapper.readValue(body, new TypeReference<HashMap<String, Object>>() {});
        } catch (Exception e) {
            data = null;
        }
        boolean success = httpStatus >= 200 && httpStatus < 300;
        return new BootpayStoreResponse(httpStatus, success, data, success ? null : body);
    }
}
