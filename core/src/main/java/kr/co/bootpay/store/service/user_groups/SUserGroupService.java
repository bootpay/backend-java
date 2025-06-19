package kr.co.bootpay.store.service.user_groups;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import kr.co.bootpay.store.model.request.userGroup.UserGroupListParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class SUserGroupService {

    static public BootpayStoreResponse create(BootpayStoreObject bootpay, SUserGroup userGroup) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("user_groups", new StringEntity(gson.toJson(userGroup), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse list(BootpayStoreObject bootpay, UserGroupListParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        // 파라미터 맵 초기화
        Map<String, Object> payload = new HashMap<>();
        if (params.corporateType != null) payload.put("corporate_type", params.corporateType);
        if (params.keyword != null) payload.put("keyword", params.keyword);
        if (params.page != null) payload.put("page", params.page);
        if (params.limit != null) payload.put("limit", params.limit);

        // 파라미터를 URL 쿼리 문자열로 변환
        StringBuilder query = new StringBuilder("user_groups?");
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            String encodedValue = URLEncoder.encode(entry.getValue().toString(), "UTF-8");
            query.append(entry.getKey()).append("=").append(encodedValue).append("&");
//            query.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        // 마지막 '&' 제거
        if (query.charAt(query.length() - 1) == '&') {
            query.deleteCharAt(query.length() - 1);
        }

        // GET 요청 객체 생성
        HttpGet get = bootpay.httpGet(query.toString());

        // HTTP 요청 전송 및 응답 수신
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String userGroupId) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();


        HttpGet get = bootpay.httpGet("user_groups/" + userGroupId);

        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, SUserGroup userGroup) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("user_groups/" + userGroup.userGroupId, new StringEntity(gson.toJson(userGroup), "UTF-8"));

        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse userCreate(BootpayStoreObject bootpay, String userGroupId, String userId) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);

        HttpPost post = bootpay.httpPost("user_groups/" + userGroupId + "/add_user", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse userDelete(BootpayStoreObject bootpay, String userGroupId, String userId) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        HttpDelete delete = bootpay.httpDelete("user_groups/" + userGroupId + "/remove_user?user_id=" + userId);

        HttpResponse response = client.execute(delete);
        return bootpay.responseToJsonObject(response);
    }
}
