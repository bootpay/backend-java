package kr.co.bootpay.service;

import kr.co.bootpay.BootpayObject;
import kr.co.bootpay.model.request.Payload;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.model.response.ResDefault;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;

public class LinkService {
    static public ResDefault<HashMap<String, Object>> requestLink(BootpayObject bootpay, Payload payload) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
//        if(userToken.userId == null || userToken.userId.isEmpty()) throw new Exception("userId 값을 입력해주세요.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("request/payment", new StringEntity(gson.toJson(payload), "UTF-8"));

        post.setHeader("Authorization", bootpay.token);
        HttpResponse response = client.execute(post);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        ResDefault res = new Gson().fromJson(str, ResDefault.class);
        return res;
    }
}
