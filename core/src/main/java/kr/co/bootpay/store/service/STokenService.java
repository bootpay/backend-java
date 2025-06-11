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
        if(bootpay.serverKey == null || bootpay.serverKey.isEmpty()) throw new Exception("serverKey 값이 비어있습니다.");
        if(bootpay.privateKey == null || bootpay.privateKey.isEmpty()) throw new Exception("privateKey 값이 비어있습니다.");

        SToken token = new SToken();
        token.serverKey = bootpay.serverKey;
        token.privateKey = bootpay.privateKey;

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("request/token", new StringEntity(gson.toJson(token), "UTF-8"));

        HttpResponse response = client.execute(post);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        System.out.println("goGetToken  " + str);


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
