package kr.co.bootpay.service;

import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.BootpayObject;
import com.google.gson.Gson;
import kr.co.bootpay.model.request.Token;
import kr.co.bootpay.model.response.ResDefault;
import kr.co.bootpay.model.response.data.TokenData;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;

public class TokenService {
    static public ResDefault<HashMap<String, Object>> getAccessToken(BootpayObject bootpay) throws Exception {
        if(bootpay.application_id == null || bootpay.application_id.isEmpty()) throw new Exception("application_id 값이 비어있습니다.");
        if(bootpay.private_key == null || bootpay.private_key.isEmpty()) throw new Exception("private_key 값이 비어있습니다.");

        Token token = new Token();
        token.application_id = bootpay.application_id;
        token.private_key = bootpay.private_key;

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = bootpay.httpPost("request/token.json", new StringEntity(new Gson().toJson(token), "UTF-8"));

        HttpResponse response = client.execute(post);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resTokenType = new TypeToken<ResDefault<TokenData>>(){}.getType();
        ResDefault<TokenData> resToken = new Gson().fromJson(str, resTokenType);
        if(resToken.status == 200)
            bootpay.token = resToken.data.token;

        Type resType = new TypeToken<ResDefault<HashMap<String, Object>>>(){}.getType();
        ResDefault res = new Gson().fromJson(str, resType);
        return res;
    }
}
