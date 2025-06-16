package kr.co.bootpay.store.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.pojo.SToken;
import kr.co.bootpay.store.model.response.STokenResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;

public class STokenService {
    static public HashMap<String, Object> getAccessToken(BootpayStoreObject bootpay) throws Exception {

        if(bootpay.tokenPayload == null) {
            throw new Exception("tokenPayload 값이 비어있습니다.");
        }
        boolean clientKeyEmpty = bootpay.tokenPayload.clientKey == null || bootpay.tokenPayload.clientKey.isEmpty();
        boolean secretKeyEmpty = bootpay.tokenPayload.secretKey == null || bootpay.tokenPayload.secretKey.isEmpty();
        boolean serverKeyEmpty = bootpay.tokenPayload.serverKey == null || bootpay.tokenPayload.serverKey.isEmpty();
        boolean privateKeyEmpty = bootpay.tokenPayload.privateKey == null || bootpay.tokenPayload.privateKey.isEmpty();

        SToken token = new SToken();
        if(clientKeyEmpty || secretKeyEmpty) {
            if(serverKeyEmpty && privateKeyEmpty) {
                if(clientKeyEmpty) throw new Exception("clientKey 값이 비어있습니다.");
                else throw new Exception("secretKey 값이 비어있습니다.");
            }
            if(serverKeyEmpty) throw new Exception("serverKey 값이 비어있습니다.");
            if(privateKeyEmpty) throw new Exception("privateKey 값이 비어있습니다.");
            token.serverKey = bootpay.tokenPayload.serverKey;
            token.privateKey = bootpay.tokenPayload.privateKey;
        }
        bootpay.token = null;

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("request/token", new StringEntity(gson.toJson(token), "UTF-8"));

        HttpResponse response = client.execute(post);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        STokenResponse res = new Gson().fromJson(str, STokenResponse.class);
        bootpay.token = res.access_token;

//        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
//        return new Gson().fromJson(str, resType);
        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        HashMap<String, Object> result = new Gson().fromJson(str, resType);
        if(result == null) {
            result = new HashMap<>();
        }

        result.put("http_status", response.getStatusLine().getStatusCode());
        return result;
    }
}
