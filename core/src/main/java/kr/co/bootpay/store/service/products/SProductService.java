package kr.co.bootpay.store.service.products;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.pojo.SProduct;
import kr.co.bootpay.store.model.request.ProductListParams;
import kr.co.bootpay.store.model.request.product.ProductStatusParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class SProductService {

    static public HashMap<String, Object> create(BootpayStoreObject bootpay, SProduct product, List<URL> imagePaths) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // URL 리스트를 파일 리스트로 변환
        List<File> fileList = new ArrayList<>();
        for (URL imageUrl : imagePaths) {
            File tempFile = new File(imageUrl.toURI());
            if (!tempFile.exists()) throw new Exception("파일 경로가 올바르지 않습니다: " + tempFile.getAbsolutePath());
            fileList.add(tempFile);
        }

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        String jsonProduct = gson.toJson(product);

        // JSON 문자열을 Map으로 변환
        HashMap<String, String> params = gson.fromJson(jsonProduct, new TypeToken<HashMap<String, String>>(){}.getType());

        // 파일 업로드 요청 (여러 파일)
        HttpPost post = bootpay.httpPostMultipart("products", fileList, params);
        HttpResponse response = client.execute(post);
        return bootpay.responseToJson(response);

        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return bootpay.responseJson(gson, str, response.getStatusLine().getStatusCode());
    }

    static public HashMap<String, Object> update(BootpayStoreObject bootpay, SProduct product) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();


        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // 파일 업로드 요청 (여러 파일)
//        HttpPost post = bootpay.httpPostMultipart("products", fileList, params);
        HttpPut put = bootpay.httpPut("products/" + product.productId, new StringEntity(gson.toJson(product), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJson(response);

        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }


    static public HashMap<String, Object> list(BootpayStoreObject bootpay, ProductListParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        // 파라미터 맵 초기화
        Map<String, Object> payload = new HashMap<>();
        if(params != null) {
            if (params.type != null) payload.put("type", params.type);
            if (params.periodType != null) payload.put("period_type", params.periodType);
            if (params.sAt != null) payload.put("s_at", params.sAt);
            if (params.eAt != null) payload.put("e_at", params.eAt);
            if (params.categoryCode != null) payload.put("category_code", params.categoryCode);
            if (params.keyword != null) payload.put("keyword", params.keyword);
            if (params.page != null) payload.put("page", params.page);
            if (params.limit != null) payload.put("limit", params.limit);
        }



        // 파라미터를 URL 쿼리 문자열로 변환
        StringBuilder query = new StringBuilder("products?");
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
        return bootpay.responseToJson(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());

//        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
//        HashMap<String, Object> result = new Gson().fromJson(str, resType);
//        if(result == null) {
//            result = new HashMap<>();
//        }
//
//        result.put("http_status", response.getStatusLine().getStatusCode());
//        return result;
    }

    static public HashMap<String, Object> detail(BootpayStoreObject bootpay, String productId) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("products/" + productId);

        HttpResponse response = client.execute(get);
        return bootpay.responseToJson(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }

    static public HashMap<String, Object> status(BootpayStoreObject bootpay, ProductStatusParams params) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(params == null || params.productId == null || params.productId.isEmpty()) throw new Exception("params에 product_id 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("products/" + params.productId + "/status", new StringEntity(gson.toJson(params), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJson(response);
    }

    static public HashMap<String, Object> delete(BootpayStoreObject bootpay, String productId) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");


        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete delete = bootpay.httpDelete("products/" + productId);

        HttpResponse response = client.execute(delete);
        return bootpay.responseToJson(response);

    }
}
