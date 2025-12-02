package kr.co.bootpay.pg;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.Version;
import kr.co.bootpay.http.HttpDeleteWithBody;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BootpayObject {
    public String token;
    public String application_id;
    public String private_key;
    public String baseUrl;

    public final String DEVELOPMENT = "https://dev-api.bootpay.co.kr/v2/";
    public final String TEST = "https://test-api.bootpay.co.kr/v2/";
    public final String STAGE = "https://stage-api.bootpay.co.kr/v2/";
    public final String PRODUCTION = "https://api.bootpay.co.kr/v2/";

    // 재사용 가능한 Gson 인스턴스 (snake_case 변환)
    public static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public BootpayObject() {}
    public BootpayObject(String rest_application_id, String private_key) {
        this.application_id = rest_application_id;
        this.private_key = private_key;
        this.baseUrl = PRODUCTION;
    }

    public BootpayObject(String rest_application_id, String private_key, String devMode) {
        this.application_id = rest_application_id;
        this.private_key = private_key;
        if("DEVELOPMENT".equals(devMode)) {
            this.baseUrl = DEVELOPMENT;
        } else if("TEST".equalsIgnoreCase(devMode)) {
            this.baseUrl = TEST;
        } else if("STAGE".equalsIgnoreCase(devMode)) {
            this.baseUrl = STAGE;
        } else if("PRODUCTION".equalsIgnoreCase(devMode)) {
            this.baseUrl = PRODUCTION;
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenValue() {
        return "Bearer " + token;
    }

    // ========================================
    // 공통 헤더 설정 메서드 (내부용)
    // ========================================
    protected void setCommonHeaders(HttpRequestBase request) {
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept-Charset", "utf-8");
        request.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
        request.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
        request.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
    }

    protected void setAuthHeader(HttpRequestBase request) {
        if (this.token != null && !this.token.isEmpty()) {
            request.setHeader("Authorization", getTokenValue());
        }
    }

    // ========================================
    // 고수준 HTTP 요청 메서드 (Service에서 사용)
    // ========================================

    /**
     * GET 요청을 실행하고 결과를 HashMap으로 반환
     * @param url 요청 URL (baseUrl 제외)
     * @return 응답 데이터 (http_status 포함)
     */
    public HashMap<String, Object> doGet(String url) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = httpGet(url);
        setCommonHeaders(get);
        setAuthHeader(get);
        HttpResponse response = client.execute(get);
        return responseToJson(response);
    }

    /**
     * GET 요청을 실행 (쿼리 파라미터 포함)
     */
    public HashMap<String, Object> doGet(String url, List<NameValuePair> params) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = httpGet(url, params);
        setAuthHeader(get);
        HttpResponse response = client.execute(get);
        return responseToJson(response);
    }

    /**
     * POST 요청을 실행하고 결과를 HashMap으로 반환
     * @param url 요청 URL (baseUrl 제외)
     * @param payload 요청 본문 객체 (자동으로 JSON 변환)
     * @return 응답 데이터 (http_status 포함)
     */
    public HashMap<String, Object> doPost(String url, Object payload) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        StringEntity entity = new StringEntity(gson.toJson(payload), "UTF-8");
        HttpPost post = httpPost(url, entity);
        setAuthHeader(post);
        HttpResponse response = client.execute(post);
        return responseToJson(response);
    }

    /**
     * POST 요청 (인증 없이)
     */
    public HashMap<String, Object> doPostWithoutAuth(String url, Object payload) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        StringEntity entity = new StringEntity(gson.toJson(payload), "UTF-8");
        HttpPost post = httpPost(url, entity);
        HttpResponse response = client.execute(post);
        return responseToJson(response);
    }

    /**
     * PUT 요청을 실행하고 결과를 HashMap으로 반환
     */
    public HashMap<String, Object> doPut(String url, Object payload) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        StringEntity entity = new StringEntity(gson.toJson(payload), "UTF-8");
        HttpPut put = httpPut(url, entity);
        setCommonHeaders(put);
        setAuthHeader(put);
        HttpResponse response = client.execute(put);
        return responseToJson(response);
    }

    /**
     * DELETE 요청을 실행하고 결과를 HashMap으로 반환
     */
    public HashMap<String, Object> doDelete(String url) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete delete = httpDelete(url);
        setAuthHeader(delete);
        HttpResponse response = client.execute(delete);
        return responseToJson(response);
    }

    /**
     * DELETE 요청 (본문 포함)
     */
    public HashMap<String, Object> doDeleteWithBody(String url, Object payload) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        StringEntity entity = new StringEntity(gson.toJson(payload), "UTF-8");
        HttpDeleteWithBody delete = httpDeleteWithBody(url, entity);
        setAuthHeader(delete);
        HttpResponse response = client.execute(delete);
        return responseToJson(response);
    }

    /**
     * 배열 응답을 위한 GET 요청
     */
    public HashMap<String, Object> doGetArray(String url) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = httpGet(url);
        setCommonHeaders(get);
        setAuthHeader(get);
        HttpResponse response = client.execute(get);
        return responseToJsonArray(response);
    }

    // ========================================
    // 기존 메서드 (하위 호환성 유지)
    // ========================================

    public HttpGet httpGet(String url) throws Exception {
        HttpGet get = new HttpGet(this.baseUrl + url);
        URI uri = new URIBuilder(get.getURI()).build();
        get.setURI(uri);
        return get;
    }

    public HttpGet httpGet(String url, List<NameValuePair> nameValuePairList) throws Exception {
        HttpGet get = new HttpGet(this.baseUrl +url);
        setCommonHeaders(get);
        URI uri = new URIBuilder(get.getURI()).addParameters(nameValuePairList).build();
        get.setURI(uri);
        return get;
    }

    public HttpPost httpPost(String url, StringEntity entity) {
        HttpPost post = new HttpPost(this.baseUrl + url);
        setCommonHeaders(post);
        post.setEntity(entity);
        return post;
    }

    public HttpDelete httpDelete(String url) {
        HttpDelete delete = new HttpDelete(this.baseUrl + url);
        setCommonHeaders(delete);
        return delete;
    }

    public HttpDeleteWithBody httpDeleteWithBody(String url, StringEntity entity) {
        HttpDeleteWithBody delete = new HttpDeleteWithBody(this.baseUrl + url);
        setCommonHeaders(delete);
        delete.setEntity(entity);
        return delete;
    }

    public HttpPut httpPut(String url, StringEntity entity) {
        HttpPut put = new HttpPut(this.baseUrl + url);
        put.setHeader("Accept", "application/json");
        put.setHeader("Content-Type", "application/json");
        put.setHeader("Accept-Charset", "utf-8");
        put.setEntity(entity);
        return put;
    }

    public HashMap<String, Object> responseToJsonArray(HttpResponse response)  throws Exception {
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        HashMap<String, Object>  result = new HashMap<>();
        TypeReference<List<HashMap<String, Object>>> typeRef = new TypeReference<List<HashMap<String, Object>>>() {};
        ObjectMapper mapper = new ObjectMapper();
        List<HashMap<String, Object>> data = mapper.readValue(str, typeRef);
        if(data == null) {
            data = new ArrayList<>();
        }

        result.put("http_status", response.getStatusLine().getStatusCode());
        result.put("data", data);
        return result;
    }

    public HashMap<String, Object> responseToJson(HttpResponse response)  throws Exception {
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
        HashMap<String, Object> result = mapper.readValue(str, typeRef);
        if (result == null) {
            result = new HashMap<>();
        }

        result.put("http_status", response.getStatusLine().getStatusCode());
        return result;
    }


//    public HttpResponse getAccessToken() throws Exception {
//        if(this.application_id == null || this.application_id.isEmpty()) throw new Exception("application_id 값이 비어있습니다.");
//        if(this.private_key == null || this.private_key.isEmpty()) throw new Exception("private_key 값이 비어있습니다.");
//
//        Token token = new Token();
//        token.application_id = this.application_id;
//        token.private_key = this.private_key;
//
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpPost post = httpPost("request/token.json", new StringEntity(new Gson().toJson(token), "UTF-8"));
//
//        HttpResponse res = client.execute(post);
//        String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
//        ResToken resToken = new Gson().fromJson(str, ResToken.class);
//
//        System.out.println(str);
//        if(resToken.status == 200)
//            this.token = resToken.data.token;
//
//        return res;
//    }
}