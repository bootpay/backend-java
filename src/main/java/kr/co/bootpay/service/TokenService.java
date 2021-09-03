package kr.co.bootpay.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.BootpayObject;
import kr.co.bootpay.model.request.Payload;
import kr.co.bootpay.model.request.Token;
import kr.co.bootpay.model.response.ResToken;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class TokenService {
    static public HttpResponse getAccessToken(BootpayObject bootpay) throws Exception {
        if(bootpay.application_id == null || bootpay.application_id.isEmpty()) throw new Exception("application_id 값이 비어있습니다.");
        if(bootpay.private_key == null || bootpay.private_key.isEmpty()) throw new Exception("private_key 값이 비어있습니다.");

        Token token = new Token();
        token.application_id = bootpay.application_id;
        token.private_key = bootpay.private_key;

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = bootpay.httpPost("request/token.json", new StringEntity(new Gson().toJson(token), "UTF-8"));

        HttpResponse res = client.execute(post);
        String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
        ResToken resToken = new Gson().fromJson(str, ResToken.class);

        System.out.println(str);
        if(resToken.status == 200)
            bootpay.token = resToken.data.token;

        return res;
    }
}
