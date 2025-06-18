package kr.co.bootpay.store.service.users;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.request.UserListParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SUserService {


//    static public HashMap<String, Object> list(BootpayStoreObject bootpay, Optional<Integer> memberType, Optional<String> type, Optional<String> keyword, Optional<Integer> page, Optional<Integer> limit) throws Exception {
static public BootpayStoreResponse list(BootpayStoreObject bootpay, UserListParams params) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        String url = "users";
        if(params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if(params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
            if(params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));
            if(params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if(params.memberType != null) nameValuePairList.add(new BasicNameValuePair("member_type", params.memberType.toString()));
            if(params.type != null) nameValuePairList.add(new BasicNameValuePair("type", params.type));

            HttpGet get = bootpay.httpGet(url, nameValuePairList);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, SUser user) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("users/" + user.userId, new StringEntity(gson.toJson(user), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String userId) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("users/" + userId);
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    //    회원탈퇴
    static public BootpayStoreResponse destroy(BootpayStoreObject bootpay, String userId) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();

        HttpDelete delete = bootpay.httpDelete("users/" + userId);
        HttpResponse response = client.execute(delete);
        return bootpay.responseToJsonObject(response);
    }

}
