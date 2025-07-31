package kr.co.bootpay.store.service.user_groups;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.userGroup.UserGroupAggregateTransactionParams;
import kr.co.bootpay.store.model.request.userGroup.UserGroupLimitParams;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        HttpPost post = bootpay.httpPost("user-groups", new StringEntity(gson.toJson(userGroup), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse list(BootpayStoreObject bootpay, UserGroupListParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        String url = "user-groups";
        if(params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if (params.corporateType != null) nameValuePairList.add(new BasicNameValuePair("corporate_type", params.corporateType.toString()));
            if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if (params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
            if (params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));

            HttpGet get = bootpay.httpGet(url, nameValuePairList);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }

    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String userGroupId) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();


        HttpGet get = bootpay.httpGet("user-groups/" + userGroupId);

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

        HttpPut put = bootpay.httpPut("user-groups/" + userGroup.userGroupId, new StringEntity(gson.toJson(userGroup), "UTF-8"));

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

        HttpPost post = bootpay.httpPost("user-groups/" + userGroupId + "/add_user", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse userDelete(BootpayStoreObject bootpay, String userGroupId, String userId) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        HttpDelete delete = bootpay.httpDelete("user-groups/" + userGroupId + "/remove_user?user_id=" + userId);

        HttpResponse response = client.execute(delete);
        return bootpay.responseToJsonObject(response);
    }


    static public BootpayStoreResponse limit(BootpayStoreObject bootpay, UserGroupLimitParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if(params == null) {
            throw new Exception("params 값이 비어있습니다");
        }
        if(params.userGroupId == null || params.userGroupId.isEmpty()) {
            throw new Exception("userGroupId 값이 비어있습니다");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPut put = bootpay.httpPut("user-groups/" + params.userGroupId + "/limit", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse aggregateTransaction(BootpayStoreObject bootpay, UserGroupAggregateTransactionParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if(params == null) {
            throw new Exception("params 값이 비어있습니다");
        }
        if(params.userGroupId == null || params.userGroupId.isEmpty()) {
            throw new Exception("userGroupId 값이 비어있습니다");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPut put = bootpay.httpPut("user-groups/" + params.userGroupId + "/aggregate-transaction", new StringEntity(gson.toJson(params), "UTF-8"));

        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }
}
