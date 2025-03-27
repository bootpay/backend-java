package kr.co.bootpay.store.service.user_groups;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.layer.UserGroup;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SUserGroupService {


    static public HashMap<String, Object> list(BootpayStoreObject bootpay, Optional<String> keyword, Optional<Integer> page, Optional<Integer> limit) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // 파라미터 맵 초기화
        Map<String, Object> params = new HashMap<>();
        keyword.ifPresent(value -> params.put("keyword", value));
        page.ifPresent(value -> params.put("page", value));
        limit.ifPresent(value -> params.put("limit", value));

        // 파라미터를 URL 쿼리 문자열로 변환
        StringBuilder query = new StringBuilder("user_groups?");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        // 마지막 '&' 제거
        if (query.charAt(query.length() - 1) == '&') {
            query.deleteCharAt(query.length() - 1);
        }

        // GET 요청 객체 생성
        HttpGet get = bootpay.httpGet(query.toString());

        // HTTP 요청 전송 및 응답 수신
        HttpResponse response = client.execute(get);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        HashMap<String, Object> result = new Gson().fromJson(str, resType);
        if(result == null) {
            result = new HashMap<>();
        }

        result.put("http_status", response.getStatusLine().getStatusCode());
        return result;
    }

    static public HashMap<String, Object> detail(BootpayStoreObject bootpay, String userGroupId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();


        HttpGet get = bootpay.httpGet("user_groups/" + userGroupId);

        HttpResponse response = client.execute(get);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        HashMap<String, Object> result = new Gson().fromJson(str, resType);
        if(result == null) {
            result = new HashMap<>();
        }

        result.put("http_status", response.getStatusLine().getStatusCode());
        return result;
    }

    static public HashMap<String, Object> update(BootpayStoreObject bootpay, SUserGroup userGroup) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();


        HttpPut put = bootpay.httpPut("user_groups/" + userGroup.userGroupId, new StringEntity(gson.toJson(userGroup), "UTF-8"));

        HttpResponse response = client.execute(put);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        System.out.println(str);
        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        HashMap<String, Object> result = new Gson().fromJson(str, resType);
        if(result == null) {
            result = new HashMap<>();
        }

        result.put("http_status", response.getStatusLine().getStatusCode());
        return result;
    }
}
