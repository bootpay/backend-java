package kr.co.bootpay.store.service.products;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SProduct;
import kr.co.bootpay.store.model.request.product.MallProductListParams;
import kr.co.bootpay.store.model.request.product.ProductListParams;
import kr.co.bootpay.store.model.request.product.ProductStatusParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.io.File;
import java.net.URL;
import java.util.*;

public class SProductService {

    static public BootpayStoreResponse create(BootpayStoreObject bootpay, SProduct product, List<URL> imagePaths) throws Exception {
        return create(bootpay, product, imagePaths, null);
    }

    /**
     * 상품 생성
     * POST /v1/products
     * imagePaths 가 있으면 multipart/form-data (images[0], images[1] ... 인덱싱), 없으면 JSON 으로 보낸다.
     * 서버가 manager scope 를 요구한다.
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse create(BootpayStoreObject bootpay, SProduct product, List<URL> imagePaths, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // 이미지가 없으면 JSON 으로 전송
        if (imagePaths == null || imagePaths.isEmpty()) {
            HttpPost post = bootpay.httpPost("products", new StringEntity(gson.toJson(product), "UTF-8"), managerContext(idempotencyKey));
            HttpResponse response = bootpay.execute(post);
            return bootpay.responseToJsonObject(response);
        }

        // URL 리스트를 파일 리스트로 변환
        List<File> fileList = new ArrayList<>();
        for (URL imageUrl : imagePaths) {
            File tempFile = new File(imageUrl.toURI());
            if (!tempFile.exists()) throw new Exception("파일 경로가 올바르지 않습니다: " + tempFile.getAbsolutePath());
            fileList.add(tempFile);
        }

        String jsonProduct = gson.toJson(product);

        // JSON 문자열을 Map으로 변환 — 배열/객체 값은 JSON 문자열로, 나머지는 문자열로 보낸다
        HashMap<String, Object> rawParams = gson.fromJson(jsonProduct, new TypeToken<HashMap<String, Object>>(){}.getType());
        HashMap<String, String> params = new HashMap<>();
        for (Map.Entry<String, Object> entry : rawParams.entrySet()) {
            Object value = entry.getValue();
            if (value == null) continue;
            if (value instanceof String) {
                params.put(entry.getKey(), (String) value);
            } else if (value instanceof Map || value instanceof List) {
                params.put(entry.getKey(), gson.toJson(value));
            } else {
                params.put(entry.getKey(), String.valueOf(value));
            }
        }

        // 파일 업로드 요청 (여러 파일)
        HttpPost post = bootpay.httpPostMultipart("products", fileList, params, managerContext(idempotencyKey));
        HttpResponse response = bootpay.execute(post);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, SProduct product) throws Exception {
        return update(bootpay, product, null);
    }

    /**
     * 상품 수정 — 서버가 manager scope 를 요구한다.
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse update(BootpayStoreObject bootpay, SProduct product, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();


        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("products/" + product.productId, new StringEntity(gson.toJson(product), "UTF-8"), managerContext(idempotencyKey));
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }


    static public BootpayStoreResponse list(BootpayStoreObject bootpay, ProductListParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        String url = "products";
        if(params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            // 서버가 읽는 값
            if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if (params.categoryId != null) nameValuePairList.add(new BasicNameValuePair("category_id", params.categoryId));
            if (params.exUid != null) nameValuePairList.add(new BasicNameValuePair("ex_uid", params.exUid));
            if (params.sort != null) nameValuePairList.add(new BasicNameValuePair("sort", params.sort));
            // 아래 4개는 서버가 읽지 않는다 — 기존 호출을 깨지 않으려고 전송만 유지한다
            if (params.type != null) nameValuePairList.add(new BasicNameValuePair("type", params.type.toString()));
            if (params.periodType != null) nameValuePairList.add(new BasicNameValuePair("period_type", params.periodType));
            if (params.sAt != null) nameValuePairList.add(new BasicNameValuePair("s_at", params.sAt));
            if (params.eAt != null) nameValuePairList.add(new BasicNameValuePair("e_at", params.eAt));
            if (params.categoryCode != null) nameValuePairList.add(new BasicNameValuePair("category_code", params.categoryCode));
            if (params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
            if (params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));

            HttpGet get = bootpay.httpGet(url, nameValuePairList);
            HttpResponse response = bootpay.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url);
            HttpResponse response = bootpay.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }

    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String productId) throws Exception {
        return detail(bootpay, productId, null, null);
    }

    /**
     * 상품 상세 조회
     * GET /v1/products/{product_id}
     *
     * <p>{@code productDetail} 과 uri·동작이 같다. 기존 사용자가 있어 남겨두지만 신규 코드는
     * {@code productDetail} 을 쓸 것.</p>
     *
     * @param userJwt 회원 JWT (선택 — 값이 있을 때만 Bootpay-User-JWT 헤더로 전송)
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String productId, String userJwt, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpGet get = bootpay.httpGet("products/" + productId, mallContext(userJwt, idempotencyKey));

        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }

    static public BootpayStoreResponse status(BootpayStoreObject bootpay, ProductStatusParams params) throws Exception {
        bootpay.requireCommerceCredentials();
        if(params == null || params.productId == null || params.productId.isEmpty()) throw new Exception("params에 product_id 값이 비어있습니다.");

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("products/" + params.productId + "/status", new StringEntity(gson.toJson(params), "UTF-8"),
                managerContext(params.idempotencyKey));
        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse delete(BootpayStoreObject bootpay, String productId) throws Exception {
        return delete(bootpay, productId, null);
    }

    /**
     * 상품 삭제 — 서버가 manager scope 를 요구한다.
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse delete(BootpayStoreObject bootpay, String productId, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();
        HttpDelete delete = bootpay.httpDelete("products/" + productId, managerContext(idempotencyKey));

        HttpResponse response = bootpay.execute(delete);
        return bootpay.responseToJsonObject(response);

    }

    /**
     * 상품 목록 조회 (V1 Mall API)
     * GET /v1/products
     * page/limit 은 미지정시 각각 1 / 20 이 적용되고, 나머지 값은 지정된 것만 전송한다.
     * ⚠️ keyword 는 서버가 읽지 않는다 (하위호환용 인자).
     */
    static public BootpayStoreResponse products(BootpayStoreObject bootpay, MallProductListParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        String userJwt = params != null ? params.userJwt : null;
        String idempotencyKey = params != null ? params.idempotencyKey : null;

        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("page", params != null && params.page != null ? params.page.toString() : "1"));
        nameValuePairList.add(new BasicNameValuePair("limit", params != null && params.limit != null ? params.limit.toString() : "20"));
        if (params != null) {
            if (params.categoryId != null) nameValuePairList.add(new BasicNameValuePair("category_id", params.categoryId));
            if (params.exUid != null) nameValuePairList.add(new BasicNameValuePair("ex_uid", params.exUid));
            if (params.sort != null) nameValuePairList.add(new BasicNameValuePair("sort", params.sort));
            if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if (params.type != null) nameValuePairList.add(new BasicNameValuePair("type", params.type.toString()));
            if (params.periodType != null) nameValuePairList.add(new BasicNameValuePair("period_type", params.periodType));
            if (params.sAt != null) nameValuePairList.add(new BasicNameValuePair("s_at", params.sAt));
            if (params.eAt != null) nameValuePairList.add(new BasicNameValuePair("e_at", params.eAt));
            if (params.categoryCode != null) nameValuePairList.add(new BasicNameValuePair("category_code", params.categoryCode));
        }

        HttpGet get = bootpay.httpGet("products", nameValuePairList, mallContext(userJwt, idempotencyKey));
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 상품 상세 조회 (V1 Mall API)
     * GET /v1/products/{product_id}
     * @param userJwt 회원 JWT (선택 — 값이 있을 때만 Bootpay-User-JWT 헤더로 전송)
     * @param idempotencyKey 미지정시 자동 생성
     */
    static public BootpayStoreResponse productDetail(BootpayStoreObject bootpay, String productId, String userJwt, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpGet get = bootpay.httpGet("products/" + productId, mallContext(userJwt, idempotencyKey));

        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 상품 쓰기(등록/수정/삭제/상태변경) 요청 컨텍스트 — 서버가 manager scope 를 요구한다.
     * Idempotency-Key 는 미지정시 매 호출마다 생성된다.
     */
    private static RequestContext managerContext(String idempotencyKey) {
        return RequestContext.builder()
                .role("manager")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }

    /**
     * V1 Mall API 요청 컨텍스트
     * Idempotency-Key 는 미지정시 매 호출마다 생성되고, Bootpay-User-JWT 는 값이 있을 때만 붙는다.
     */
    private static RequestContext mallContext(String userJwt, String idempotencyKey) {
        return RequestContext.builder()
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .userJwt(userJwt)
                .build();
    }
}
