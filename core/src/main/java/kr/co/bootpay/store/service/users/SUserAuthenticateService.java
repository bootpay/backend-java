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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class SUserAuthenticateService {

    /**
     * Registers an individual user by creating an SUser object and sending the registration request to Bootpay.
     * The method handles both success and error scenarios.
     */
    static public HashMap<String, Object> authenticationData(BootpayStoreObject bootpay, String standId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");


        HttpClient client = HttpClientBuilder.create().build();
        // URL 구조: users/join/:path?pk=:pk
        String role = "user" + "/";
        String url = String.format(role + "users/authenticate/%s", standId);
        HttpGet get = bootpay.httpGet(url);
        HttpResponse response = client.execute(get);
        return bootpay.responseToJson(response);
    }
}
