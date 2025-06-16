package kr.co.bootpay.store.service.users;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.response.STokenResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SUserLoginService {

    static public HashMap<String, Object> token(BootpayStoreObject bootpay, String userId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);

        String role = "user" + "/";
        HttpPost post = bootpay.httpPost(role + "users/login/token", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJson(response);
    }


    static public HashMap<String, Object> login(BootpayStoreObject bootpay, String loginId, String loginPw) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Map<String, Object> params = new HashMap<>();
        params.put("login_id", loginId);
        params.put("login_pw", loginPw);


        String role = "user" + "/";
        HttpPost post = bootpay.httpPost(role + "users/login", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJson(response);
    }


}
