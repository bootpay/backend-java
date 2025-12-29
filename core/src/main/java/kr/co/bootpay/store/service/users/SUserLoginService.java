package kr.co.bootpay.store.service.users;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.context.RequestContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;
import java.util.Map;

public class SUserLoginService {

    static public BootpayStoreResponse token(BootpayStoreObject bootpay, String userId) throws Exception {
        return token(bootpay, userId, "", "", null);
    }

    static public BootpayStoreResponse token(BootpayStoreObject bootpay, String userId, String corporateType, String membershipType) throws Exception {
        return token(bootpay, userId, corporateType, membershipType, null);
    }

    static public BootpayStoreResponse token(BootpayStoreObject bootpay, String userId, String corporateType, String membershipType, RequestContext context) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        if(corporateType != null && !corporateType.isEmpty()) {
            params.put("corporate_type", corporateType);
        }
        if(membershipType != null && !membershipType.isEmpty()) {
            params.put("membership_type", membershipType);
        }

        HttpPost post = bootpay.httpPost("users/login/token", new StringEntity(gson.toJson(params), "UTF-8"), context);

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }


    static public BootpayStoreResponse login(BootpayStoreObject bootpay, String loginId, String loginPw) throws Exception {
        return login(bootpay, loginId, loginPw, null);
    }

    static public BootpayStoreResponse login(BootpayStoreObject bootpay, String loginId, String loginPw, RequestContext context) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Map<String, Object> params = new HashMap<>();
        params.put("login_id", loginId);
        params.put("login_pw", loginPw);


        HttpPost post = bootpay.httpPost("users/login", new StringEntity(gson.toJson(params), "UTF-8"), context);

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }


}
