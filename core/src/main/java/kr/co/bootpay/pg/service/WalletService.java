package kr.co.bootpay.pg.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.WalletPayload;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URI;
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


    static public HashMap<String, Object> userWallets(BootpayObject bootpay, String userId, boolean sandbox) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(userId == null || userId.isEmpty()) throw new Exception("userId 값이 비어있습니다");

        URI uri = new URIBuilder()
                .setPath("wallet")
                .addParameter("user_id", userId)
                .addParameter("sandbox", String.valueOf(sandbox))
                .build();

        System.out.println("userWallets url: " + uri.toString());

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet(uri.toString());

        get.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonArray(response);
    }

}
