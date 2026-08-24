package kr.co.bootpay.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.bootpay.Version;
import kr.co.bootpay.http.HttpDeleteWithBody;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
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
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.net.URI;
import java.util.*;


public class BootpayStoreObject {
    private String token;
    private String role = "user"; // 기본값을 user로 설정
//    public String secretKey;
//    public String secretKey;
//    public String privateKey;
    public TokenPayload tokenPayload = new TokenPayload();
    public String baseUrl;

    public final String DEVELOPMENT = "https://dev-api.bootapi.com/v1/";
    public final String TEST = "https://test-api.bootapi.com/v1/";
    public final String STAGE = "https://stage-api.bootapi.com/v1/";
    public final String PRODUCTION = "https://api.bootapi.com/v1/";

    public BootpayStoreObject() {}
    public BootpayStoreObject(TokenPayload tokenPayload) {
        this.tokenPayload = tokenPayload;
        this.baseUrl = PRODUCTION;
    }

    public BootpayStoreObject(TokenPayload tokenPayload, String devMode) {
        this.tokenPayload = tokenPayload;
        if("DEVELOPMENT".equalsIgnoreCase(devMode)) {
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

    /**
     * @deprecated Commerce requests use client_key/secret_key Basic authentication. This method
     *             remains for source compatibility and does not control request authorization.
     */
    @Deprecated
    public String getTokenValue() {
        return "Bearer " + token;
    }

    // token getter만 제공 (setter는 제거하여 직접 설정 차단)
    public String getToken() {
        return token;
    }

    // API를 통해 발급받은 토큰을 설정하는 메서드 (내부용)
    public void setTokenFromAPI(String token) {
        this.token = token;
    }

    // role getter와 setter 추가
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * client_key/secret_key 로 만든 Basic 인증 값을 반환한다. 키가 없으면 빈 문자열이다.
     *
     * @return "Basic {base64(client_key:secret_key)}", 키가 없으면 ""
     */
    public String requestAccessToken() {
        boolean hasClientKey = tokenPayload.clientKey != null && !tokenPayload.clientKey.isEmpty();
        boolean hasSecretKey = tokenPayload.secretKey != null && !tokenPayload.secretKey.isEmpty();
        if (!hasClientKey && !hasSecretKey) return "";
        if (!hasClientKey) throw new IllegalStateException("clientKey 값이 비어있습니다. clientKey 와 secretKey 는 함께 지정해야 합니다.");
        if (!hasSecretKey) throw new IllegalStateException("secretKey 값이 비어있습니다. clientKey 와 secretKey 는 함께 지정해야 합니다.");
        String credentials = tokenPayload.clientKey + ":" + tokenPayload.secretKey;
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encoded;
    }

    /** Validates the only supported Commerce credential pair before a request is sent. */
    public void requireCommerceCredentials() {
        boolean hasClientKey = tokenPayload.clientKey != null && !tokenPayload.clientKey.isEmpty();
        boolean hasSecretKey = tokenPayload.secretKey != null && !tokenPayload.secretKey.isEmpty();
        if (!hasClientKey && !hasSecretKey) {
            throw new IllegalStateException("Commerce API에는 clientKey와 secretKey가 필요합니다.");
        }
        if (!hasClientKey) throw new IllegalStateException("clientKey 값이 비어있습니다. clientKey 와 secretKey 는 함께 지정해야 합니다.");
        if (!hasSecretKey) throw new IllegalStateException("secretKey 값이 비어있습니다. clientKey 와 secretKey 는 함께 지정해야 합니다.");
    }

    /** Commerce requests always use client_key/secret_key Basic authentication. */
    public String authorizationHeader(RequestContext context) {
        String basic = requestAccessToken();
        return basic.isEmpty() ? null : basic;
    }

    /** @return instance-level Authorization header, or null when no credentials are set. */
    public String authorizationHeader() {
        return authorizationHeader(null);
    }

    private void applyAuthHeader(HttpRequestBase request, RequestContext context) {
        String authorization = authorizationHeader(context);
        if (authorization != null) {
            request.setHeader("Authorization", authorization);
        }
    }

    /** Executes a Commerce request after its Basic-only authorization header is attached. */
    public HttpResponse execute(HttpRequestBase request) throws Exception {
        requireCommerceCredentials();
        HttpClient client = HttpClientBuilder.create().build();
        return client.execute(request);
    }

    // RequestContext에 지정된 부가 헤더 부착 — Idempotency-Key, Bootpay-User-JWT는 값이 있을 때만 붙는다
    private void applyContextHeaders(HttpRequestBase request, RequestContext context) {
        if (context == null) return;
        if (context.getIdempotencyKey() != null && !context.getIdempotencyKey().isEmpty()) {
            request.setHeader("Idempotency-Key", context.getIdempotencyKey());
        }
        if (context.getUserJwt() != null && !context.getUserJwt().isEmpty()) {
            request.setHeader("Bootpay-User-JWT", context.getUserJwt());
        }
    }

    public HttpGet httpGet(String url) throws Exception {
        return httpGet(url, (RequestContext) null);
    }

    public HttpGet httpGet(String url, RequestContext context) throws Exception {
        HttpGet get = new HttpGet(this.baseUrl + url);
        URI uri = new URIBuilder(get.getURI()).build();
        get.setHeader("Accept", "application/json");
        get.setHeader("Content-Type", "application/json");
        get.setHeader("Accept-Charset", "utf-8");
        get.setHeader("BOOTPAY-API-VERSION", Version.COMMERCE_API_VERSION);
        get.setHeader("BOOTPAY-SDK-VERSION", Version.COMMERCE_SDK_VERSION);
        get.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);

        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        get.setHeader("BOOTPAY-ROLE", roleToUse);

        applyAuthHeader(get, context);
        applyContextHeaders(get, context);

        get.setURI(uri);
        return get;
    }

    public HttpGet httpGet(String url, List<NameValuePair> nameValuePairList) throws Exception {
        return httpGet(url, nameValuePairList, null);
    }

    public HttpGet httpGet(String url, List<NameValuePair> nameValuePairList, RequestContext context) throws Exception {
        HttpGet get = new HttpGet(this.baseUrl +url);
        get.setHeader("Accept", "application/json");
        get.setHeader("Content-Type", "application/json");
        get.setHeader("Accept-Charset", "utf-8");
        get.setHeader("BOOTPAY-API-VERSION", Version.COMMERCE_API_VERSION);
        get.setHeader("BOOTPAY-SDK-VERSION", Version.COMMERCE_SDK_VERSION);
        get.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);

        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        get.setHeader("BOOTPAY-ROLE", roleToUse);

        applyAuthHeader(get, context);
        applyContextHeaders(get, context);

        URI uri = new URIBuilder(get.getURI()).addParameters(nameValuePairList).build();
        get.setURI(uri);
        return get;
    }

    public HttpPost httpPost(String url, StringEntity entity) {
        return httpPost(url, entity, (RequestContext) null);
    }

    public HttpPost httpPost(String url, StringEntity entity, RequestContext context) {
        HttpPost post = new HttpPost(this.baseUrl + url);

        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept-Charset", "utf-8");
        post.setHeader("BOOTPAY-API-VERSION", Version.COMMERCE_API_VERSION);
        post.setHeader("BOOTPAY-SDK-VERSION", Version.COMMERCE_SDK_VERSION);
        post.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        
        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        post.setHeader("BOOTPAY-ROLE", roleToUse);

        applyAuthHeader(post, context);
        applyContextHeaders(post, context);

        post.setEntity(entity);
        return post;
    }

    public HttpPost httpPost(String url, StringEntity entity, Map<String, String> header) {
        return httpPost(url, entity, header, null);
    }

    public HttpPost httpPost(String url, StringEntity entity, Map<String, String> header, RequestContext context) {
        HttpPost post = new HttpPost(this.baseUrl + url);

        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept-Charset", "utf-8");
        post.setHeader("BOOTPAY-API-VERSION", Version.COMMERCE_API_VERSION);
        post.setHeader("BOOTPAY-SDK-VERSION", Version.COMMERCE_SDK_VERSION);
        post.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        
        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        post.setHeader("BOOTPAY-ROLE", roleToUse);
        
        applyAuthHeader(post, context);
        applyContextHeaders(post, context);

        // 사용자 정의 헤더 추가
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }

        post.setEntity(entity);
        return post;
    }

    public HttpPost httpPostMultipart(String url, List<File> files, HashMap<String, String> params) throws Exception {
        return httpPostMultipart(url, files, params, null);
    }

    public HttpPost httpPostMultipart(String url, List<File> files, HashMap<String, String> params, RequestContext context) throws Exception {
        HttpPost post = new HttpPost(this.baseUrl + url);
        post.setHeader("Accept", "application/json");
        post.setHeader("Accept-Charset", "utf-8");
        post.setHeader("BOOTPAY-API-VERSION", Version.COMMERCE_API_VERSION);
        post.setHeader("BOOTPAY-SDK-VERSION", Version.COMMERCE_SDK_VERSION);
        post.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        
        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        post.setHeader("BOOTPAY-ROLE", roleToUse);

        applyAuthHeader(post, context);
        applyContextHeaders(post, context);

        // 멀티파트 엔티티 구성
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // 여러 파일 첨부
        // ⚠️ Rails 는 반복된 `images` 를 배열로 받지 않는다. images[0], images[1] ... 로 인덱싱해야 한다.
        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                builder.addBinaryBody("images[" + i + "]", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
            }
        }

        // 추가 파라미터 첨부
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.TEXT_PLAIN.withCharset("UTF-8"));
            }
        }

        // 엔티티 설정
        post.setEntity(builder.build());
        return post;
    }

//    public HttpPost httpPostMultipart(String url, File file, HashMap<String, String> params) throws Exception {
//        HttpPost post = new HttpPost(this.baseUrl + url);
//        post.setHeader("Accept", "application/json");
//        post.setHeader("Accept-Charset", "utf-8");
//        post.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
//        post.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
//        post.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
//        post.setHeader("Authorization", getTokenValue());
//
//        // 멀티파트 엔티티 구성
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//        // 파일 첨부
//        builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
//
//        // 추가 파라미터 첨부
//        if (params != null) {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.TEXT_PLAIN.withCharset("UTF-8"));
//            }
//        }
//
//        // 엔티티 설정
//        post.setEntity(builder.build());
//        return post;
//    }

    public HttpDelete httpDelete(String url) {
        return httpDelete(url, null);
    }

    public HttpDelete httpDelete(String url, RequestContext context) {
        HttpDelete delete = new HttpDelete(this.baseUrl + url);
        delete.setHeader("Accept", "application/json");
        delete.setHeader("Content-Type", "application/json");
        delete.setHeader("Accept-Charset", "utf-8");
        delete.setHeader("BOOTPAY-API-VERSION", Version.COMMERCE_API_VERSION);
        delete.setHeader("BOOTPAY-SDK-VERSION", Version.COMMERCE_SDK_VERSION);
        delete.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);

        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        delete.setHeader("BOOTPAY-ROLE", roleToUse);

        applyAuthHeader(delete, context);
        applyContextHeaders(delete, context);

        return delete;
    }

    public HttpDeleteWithBody httpDeleteWithBody(String url, StringEntity entity) {
        return httpDeleteWithBody(url, entity, null);
    }

    public HttpDeleteWithBody httpDeleteWithBody(String url, StringEntity entity, RequestContext context) {
        HttpDeleteWithBody delete = new HttpDeleteWithBody(this.baseUrl + url);
        delete.setHeader("Accept", "application/json");
        delete.setHeader("Content-Type", "application/json");
        delete.setHeader("Accept-Charset", "utf-8");
        delete.setHeader("BOOTPAY-API-VERSION", Version.COMMERCE_API_VERSION);
        delete.setHeader("BOOTPAY-SDK-VERSION", Version.COMMERCE_SDK_VERSION);
        delete.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);

        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        delete.setHeader("BOOTPAY-ROLE", roleToUse);

        applyAuthHeader(delete, context);
        applyContextHeaders(delete, context);

        delete.setEntity(entity);
        return delete;
    }

    public HttpPut httpPut(String url, StringEntity entity) {
        return httpPut(url, entity, null);
    }

    public HttpPut httpPut(String url, StringEntity entity, RequestContext context) {
        HttpPut put = new HttpPut(this.baseUrl + url);
        put.setHeader("Accept", "application/json");
        put.setHeader("Content-Type", "application/json");
        put.setHeader("Accept-Charset", "utf-8");
        put.setHeader("BOOTPAY-API-VERSION", Version.COMMERCE_API_VERSION);
        put.setHeader("BOOTPAY-SDK-VERSION", Version.COMMERCE_SDK_VERSION);
        put.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);

        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        put.setHeader("BOOTPAY-ROLE", roleToUse);

        applyAuthHeader(put, context);
        applyContextHeaders(put, context);

        put.setEntity(entity);
        return put;
    }

//    public HashMap<String, Object> responseToJsonArray(HttpResponse response)  throws Exception {
//        HashMap<String, Object> result = new HashMap<>();
//
//        int statusCode = response.getStatusLine().getStatusCode();
//        result.put("http_status", statusCode);
//        result.put("success", statusCode >= 200 && statusCode < 300);
//
//        try {
//            // response.getEntity()가 null인지 확인
//            if (response.getEntity() == null) {
//                result.put("data", new ArrayList<>());
//                return result;
//            }
//
//            // content가 null인지 확인
//            if (response.getEntity().getContent() == null) {
//                result.put("data", new ArrayList<>());
//                return result;
//            }
//
//            String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//
//            // 빈 문자열인지 확인
//            if (str == null || str.trim().isEmpty() || "null".equalsIgnoreCase(str.trim())) {
//                result.put("data", new ArrayList<>());
//                return result;
//            }
//
//            TypeReference<List<HashMap<String, Object>>> typeRef = new TypeReference<List<HashMap<String, Object>>>() {};
//            ObjectMapper mapper = new ObjectMapper();
//            List<HashMap<String, Object>> parsedData = mapper.readValue(str, typeRef);
//            if(parsedData == null) {
//                parsedData = new ArrayList<>();
//            }
//
//            result.put("data", parsedData);
//
//        } catch (Exception e) {
//            // 예외가 발생해도 http_status와 success는 포함하고, data는 빈 배열로 설정
//            result.put("data", new ArrayList<>());
//            result.put("error", e.getMessage());
//        }
//
//        return result;
//    }

    public BootpayStoreResponse responseToJsonObject(HttpResponse response) throws Exception {
        HashMap<String, Object> result = responseToJson(response);
        
        int httpStatus = (Integer) result.get("http_status");
        boolean success = (Boolean) result.get("success");
        Object data = result.get("data");
        String error = (String) result.get("error");
        
        return new BootpayStoreResponse(httpStatus, success, data, error);
    }

//    public BootpayStoreResponse responseToJsonArrayObject(HttpResponse response) throws Exception {
//        HashMap<String, Object> result = responseToJsonArray(response);
//
//        int httpStatus = (Integer) result.get("http_status");
//        boolean success = (Boolean) result.get("success");
//        Object data = result.get("data");
//        String error = (String) result.get("error");
//
//        return new BootpayStoreResponse(httpStatus, success, data, error);
//    }

    public HashMap<String, Object> responseToJson(HttpResponse response)  throws Exception {
        HashMap<String, Object> result = new HashMap<>();

        int statusCode = response.getStatusLine().getStatusCode();
        result.put("http_status", statusCode);
        result.put("success", statusCode >= 200 && statusCode < 300);

        try {
            // response.getEntity()가 null인지 확인
            if (response.getEntity() == null) {
                result.put("data", null);
                return result;
            }

            // content가 null인지 확인
            if (response.getEntity().getContent() == null || "null".equals(response.getEntity().getContent())) {
                result.put("data", null);
                return result;
            }

            String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

            // 빈 문자열인지 확인
            if (str == null || str.trim().isEmpty() || "null".equalsIgnoreCase(str.trim())) {
                result.put("data", null);
                return result;
            }

            ObjectMapper mapper = new ObjectMapper();
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};

            // 26-08-21: 최상위가 배열인 응답(GET /v1/categories 등)도 받도록 폴백 추가.
            // 이전에는 HashMap 파싱만 시도해 MismatchedInputException 이 나고
            // data:null + "Cannot deserialize ... from Array value" 로 끝났다.
            // pg 계층 BootpayObject.responseToJson 과 같은 방식이며,
            // 원소 타입을 고정하지 않으려고 List<Object> 로 받는다.
            Object parsedData;
            try {
                parsedData = mapper.readValue(str, typeRef);
            } catch (com.fasterxml.jackson.databind.exc.MismatchedInputException notObject) {
                TypeReference<List<Object>> listType = new TypeReference<List<Object>>() {};
                List<Object> parsedList = mapper.readValue(str, listType);
                parsedData = (parsedList == null) ? new ArrayList<Object>() : parsedList;
            }

            // 파싱된 데이터를 data 키에 저장
            result.put("data", parsedData);

        } catch (Exception e) {
            // 예외가 발생해도 http_status와 success는 포함하고, data는 null로 설정
            result.put("data", null);
            result.put("error", e.getMessage());
        }

        return result;
    }
}
