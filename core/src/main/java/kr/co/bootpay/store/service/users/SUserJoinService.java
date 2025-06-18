package kr.co.bootpay.store.service.users;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SToken;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.response.STokenResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class SUserJoinService {

    static public BootpayStoreResponse join(BootpayStoreObject bootpay, SUser user) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
//        if(user.group == null) throw new Exception("group 값이 비었습니다.");


        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("users/join", new StringEntity(gson.toJson(user), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse checkExist(BootpayStoreObject bootpay, String path, String pk) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        // URL 인코딩 처리
        // String encodedPk = URLEncoder.encode(pk, StandardCharsets.UTF_8);
        String encodedPk = URLEncoder.encode(pk, "UTF-8");

        HttpClient client = HttpClientBuilder.create().build();
        // URL 구조: users/join/:path?pk=:pk
        String url = String.format("users/join/%s?pk=%s", path, encodedPk);
        HttpGet get = bootpay.httpGet(url);
        HttpResponse response = client.execute(get);

        return bootpay.responseToJsonObject(response);
    }
}
