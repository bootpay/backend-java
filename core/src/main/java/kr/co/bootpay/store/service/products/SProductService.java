package kr.co.bootpay.store.service.products;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SProduct;
import kr.co.bootpay.store.model.request.product.ProductListParams;
import kr.co.bootpay.store.model.request.product.ProductStatusParams;
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

import java.io.File;
import java.net.URL;
import java.util.*;

public class SProductService {

    static public BootpayStoreResponse create(BootpayStoreObject bootpay, SProduct product, List<URL> imagePaths) throws Exception {
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
        return bootpay.responseToJsonObject(response);

        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return bootpay.responseJson(gson, str, response.getStatusLine().getStatusCode());
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, SProduct product) throws Exception {
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
        return bootpay.responseToJsonObject(response);

        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }


    static public BootpayStoreResponse list(BootpayStoreObject bootpay, ProductListParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        String url = "products";
        if(params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if (params.type != null) nameValuePairList.add(new BasicNameValuePair("type", params.type.toString()));
            if (params.periodType != null) nameValuePairList.add(new BasicNameValuePair("period_type", params.periodType));
            if (params.sAt != null) nameValuePairList.add(new BasicNameValuePair("s_at", params.sAt));
            if (params.eAt != null) nameValuePairList.add(new BasicNameValuePair("e_at", params.eAt));
            if (params.categoryCode != null) nameValuePairList.add(new BasicNameValuePair("category_code", params.categoryCode));
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

    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String productId) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("products/" + productId);

        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }

    static public BootpayStoreResponse status(BootpayStoreObject bootpay, ProductStatusParams params) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(params == null || params.productId == null || params.productId.isEmpty()) throw new Exception("params에 product_id 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("products/" + params.productId + "/status", new StringEntity(gson.toJson(params), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse delete(BootpayStoreObject bootpay, String productId) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");


        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete delete = bootpay.httpDelete("products/" + productId);

        HttpResponse response = client.execute(delete);
        return bootpay.responseToJsonObject(response);

    }
}
