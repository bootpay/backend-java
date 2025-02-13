package kr.co.bootpay.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.BootpayObject;
import kr.co.bootpay.model.request.Token;
import kr.co.bootpay.model.request.WalletPayload;
import kr.co.bootpay.model.response.data.TokenData;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;

public class WalletService {
    static public HashMap<String, Object> requestWalletPayment(BootpayObject bootpay, WalletPayload payload) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(payload == null) throw new Exception("payload 객체를 전달해주세요");


        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("wallet/payment", new StringEntity(gson.toJson(payload), "UTF-8"));

        post.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(post);
        return bootpay.responseToJson(response);
    }
}
