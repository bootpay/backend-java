package kr.co.bootpay.pg.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Token;
import kr.co.bootpay.pg.model.response.data.TokenData;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;

public class TokenService {
    static public HashMap<String, Object> getAccessToken(BootpayObject bootpay) throws Exception {
        if(bootpay.application_id == null || bootpay.application_id.isEmpty()) throw new Exception("application_id 값이 비어있습니다.");
        if(bootpay.private_key == null || bootpay.private_key.isEmpty()) throw new Exception("private_key 값이 비어있습니다.");

        Token token = new Token();
        token.application_id = bootpay.application_id;
        token.private_key = bootpay.private_key;

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = bootpay.httpPost("request/token.json", new StringEntity(new Gson().toJson(token), "UTF-8"));

        HttpResponse response = client.execute(post);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");


        TokenData resToken = new Gson().fromJson(str, TokenData.class);
        bootpay.token = resToken.access_token;

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
