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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.net.URI;
import java.util.*;


public class BootpayStoreObject {
    private String token;
    private String role = "user"; // 기본값을 user로 설정
//    public String secretKey;
//    public String serverKey;
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

    public String requestAccessToken() {
        if((tokenPayload.clientKey == null || tokenPayload.clientKey.isEmpty()) && (tokenPayload.secretKey == null || tokenPayload.secretKey.isEmpty())) return "";
        String credentials = tokenPayload.clientKey + ":" + tokenPayload.secretKey;
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encoded;
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
        get.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
        get.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
        get.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        
        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        get.setHeader("BOOTPAY-ROLE", roleToUse);
        
        String tokenToUse = (context != null && context.getToken() != null) ? context.getToken() : this.getToken();
        if(tokenToUse != null) get.setHeader("Authorization", "Bearer " + tokenToUse);
        
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
        get.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
        get.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
        get.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        
        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        get.setHeader("BOOTPAY-ROLE", roleToUse);
        
        String tokenToUse = (context != null && context.getToken() != null) ? context.getToken() : this.getToken();
        if(tokenToUse != null) get.setHeader("Authorization", "Bearer " + tokenToUse);
        
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
        post.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
        post.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
        post.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        
        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        post.setHeader("BOOTPAY-ROLE", roleToUse);
        
        String tokenToUse = (context != null && context.getToken() != null) ? context.getToken() : this.getToken();
        if(tokenToUse != null) {
            post.setHeader("Authorization", "Bearer " + tokenToUse);
        } else { //토큰 발급
            post.setHeader("Authorization", requestAccessToken());
        }
        
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
        post.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
        post.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
        post.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        
        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        post.setHeader("BOOTPAY-ROLE", roleToUse);
        
        String tokenToUse = (context != null && context.getToken() != null) ? context.getToken() : this.getToken();
        if(tokenToUse != null) {
            post.setHeader("Authorization", getTokenValue());
        } else { //토큰 발급
            post.setHeader("Authorization", requestAccessToken());
        }
        
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
        post.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
        post.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
        post.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        
        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        post.setHeader("BOOTPAY-ROLE", roleToUse);
        
        String tokenToUse = (context != null && context.getToken() != null) ? context.getToken() : this.getToken();
        if(tokenToUse != null) {
            post.setHeader("Authorization", "Bearer " + tokenToUse);
        }
        
        // 멀티파트 엔티티 구성
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // 여러 파일 첨부
        if (files != null) {
            for (File file : files) {
                builder.addBinaryBody("images", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
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
        delete.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
        delete.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
        delete.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        
        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        delete.setHeader("BOOTPAY-ROLE", roleToUse);
        
        String tokenToUse = (context != null && context.getToken() != null) ? context.getToken() : this.getToken();
        if(tokenToUse != null) {
            delete.setHeader("Authorization", "Bearer " + tokenToUse);
        }
        
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
        delete.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
        delete.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
        delete.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        
        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        delete.setHeader("BOOTPAY-ROLE", roleToUse);
        
        String tokenToUse = (context != null && context.getToken() != null) ? context.getToken() : this.getToken();
        if(tokenToUse != null) {
            delete.setHeader("Authorization", "Bearer " + tokenToUse);
        }
        
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
        
        // RequestContext가 있으면 우선 사용, 없으면 기본 값 사용
        String roleToUse = (context != null && context.getRole() != null) ? context.getRole() : this.getRole();
        if(roleToUse == null || roleToUse.isEmpty()) {
            roleToUse = "user"; // 기본값
        }
        put.setHeader("BOOTPAY-ROLE", roleToUse);
        
        String tokenToUse = (context != null && context.getToken() != null) ? context.getToken() : this.getToken();
        if(tokenToUse != null) {
            put.setHeader("Authorization", "Bearer " + tokenToUse);
        }
        
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
            System.out.println(str);

            // 빈 문자열인지 확인
            if (str == null || str.trim().isEmpty() || "null".equalsIgnoreCase(str.trim())) {
                result.put("data", null);
                return result;
            }

            ObjectMapper mapper = new ObjectMapper();
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
            HashMap<String, Object> parsedData = mapper.readValue(str, typeRef);

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