package kr.co.bootpay.pg.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Payload;
import kr.co.bootpay.pg.model.response.ResDefault;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;

public class LinkService {
    // 이 메서드는 ResDefault<String> 타입을 반환하므로 기존 패턴 유지 (호환성)
    static public ResDefault<String> requestLink(BootpayObject bootpay, Payload payload) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();
        StringEntity entity = new StringEntity(BootpayObject.gson.toJson(payload), "UTF-8");
        HttpPost post = bootpay.httpPost("request/payment", entity);
        post.setHeader("Authorization", bootpay.getTokenValue());

        HttpResponse response = client.execute(post);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<ResDefault<String>>(){}.getType();
        return new Gson().fromJson(str, resType);
    }
}
