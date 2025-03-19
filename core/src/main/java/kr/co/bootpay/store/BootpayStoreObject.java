package kr.co.bootpay.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.bootpay.Version;
import kr.co.bootpay.http.HttpDeleteWithBody;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BootpayStoreObject {
    public String token;
    public String serverKey;
    public String privateKey;
    public String baseUrl;

    public final String DEVELOPMENT = "https://dev-api.bootapi.com/v1/";
    public final String TEST = "https://test-api.bootapi.com/v1/";
    public final String STAGE = "https://stage-api.bootapi.com/v1/";
    public final String PRODUCTION = "https://api.bootapi.com/v1/";

    public BootpayStoreObject() {}
    public BootpayStoreObject(String serverKey, String privateKey) {
        this.serverKey = serverKey;
        this.privateKey = privateKey;
        this.baseUrl = PRODUCTION;
    }

    public BootpayStoreObject(String serverKey, String privateKey, String devMode) {
        this.serverKey = serverKey;
        this.privateKey = privateKey;
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

    public HttpGet httpGet(String url) throws Exception {
        HttpGet get = new HttpGet(this.baseUrl + url);
        URI uri = new URIBuilder(get.getURI()).build();
        get.setURI(uri);
        return get;
    }

    public HttpGet httpGet(String url, List<NameValuePair> nameValuePairList) throws Exception {
        HttpGet get = new HttpGet(this.baseUrl +url);
        get.setHeader("Accept", "application/json");
        get.setHeader("Content-Type", "application/json");
        get.setHeader("Accept-Charset", "utf-8");
        get.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
        get.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
        get.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        URI uri = new URIBuilder(get.getURI()).addParameters(nameValuePairList).build();
        get.setURI(uri);
        return get;
    }

    public HttpPost httpPost(String url, StringEntity entity) {
        HttpPost post = new HttpPost(this.baseUrl + url);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept-Charset", "utf-8");
        post.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
        post.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
        post.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        post.setEntity(entity);
        return post;
    }

    public HttpDelete httpDelete(String url) {
        HttpDelete delete = new HttpDelete(this.baseUrl + url);
        delete.setHeader("Accept", "application/json");
        delete.setHeader("Content-Type", "application/json");
        delete.setHeader("Accept-Charset", "utf-8");
        delete.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
        delete.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
        delete.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
        return delete;
    }

    public HttpDeleteWithBody httpDeleteWithBody(String url, StringEntity entity) {
        HttpDeleteWithBody delete = new HttpDeleteWithBody(this.baseUrl + url);
        delete.setHeader("Accept", "application/json");
        delete.setHeader("Content-Type", "application/json");
        delete.setHeader("Accept-Charset", "utf-8");
        delete.setHeader("BOOTPAY-API-VERSION", Version.API_VERSION);
        delete.setHeader("BOOTPAY-SDK-VERSION", Version.SDK_VERSION);
        delete.setHeader("BOOTPAY-SDK-TYPE", Version.SDK_TYPE);
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
        System.out.println(str);

        HashMap<String, Object>  result = new HashMap<>();
//        Type resType = new TypeToken<List<HashMap<String, Object>>>(){}.getType();
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

        result.put("http_status", response.getStatusLine().getStatusCode());
        return result;
    }
}