package kr.co.bootpay.store.service.categories;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.request.category.CategoryCreateParams;
import kr.co.bootpay.store.model.request.category.CategoryUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

public class SCategoryService {

    static public BootpayStoreResponse list(BootpayStoreObject bootpay) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpGet get = bootpay.httpGet("categories");
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String categoryId) throws Exception {
        bootpay.requireCommerceCredentials();
        if (categoryId == null || categoryId.isEmpty()) {
            throw new Exception("categoryId 값이 비어있습니다.");
        }

        HttpGet get = bootpay.httpGet("categories/" + categoryId);
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse create(BootpayStoreObject bootpay, CategoryCreateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) {
            throw new Exception("params 값이 비어있습니다.");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("categories", new StringEntity(gson.toJson(params), "UTF-8"),
                supervisorContext());
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, CategoryUpdateParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if (params == null) {
            throw new Exception("params 값이 비어있습니다.");
        }
        if (params.categoryId == null || params.categoryId.isEmpty()) {
            throw new Exception("categoryId 값이 비어있습니다.");
        }

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // categoryId는 URL 에 포함하고 body 에서는 제외하기 위해 임시 객체 사용
        CategoryUpdateParams body = new CategoryUpdateParams();
        body.name = params.name;
        body.parentCategoryId = params.parentCategoryId;
        body.statusDisplay = params.statusDisplay;
        body.statusBest = params.statusBest;
        body.filterColor = params.filterColor;
        body.filterSize = params.filterSize;

        HttpPut put = bootpay.httpPut("categories/" + params.categoryId, new StringEntity(gson.toJson(body), "UTF-8"),
                supervisorContext());
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse delete(BootpayStoreObject bootpay, String categoryId) throws Exception {
        bootpay.requireCommerceCredentials();
        if (categoryId == null || categoryId.isEmpty()) {
            throw new Exception("categoryId 값이 비어있습니다.");
        }

        HttpDelete delete = bootpay.httpDelete("categories/" + categoryId, supervisorContext());
        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 카테고리 쓰기(등록/수정/삭제) 요청 컨텍스트 — 서버가 supervisor scope 를 요구한다.
     */
    private static RequestContext supervisorContext() {
        return RequestContext.builder()
                .role("supervisor")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(null))
                .build();
    }
}
