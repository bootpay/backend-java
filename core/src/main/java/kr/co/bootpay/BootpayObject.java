package kr.co.bootpay;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BootpayObject {
    public String token;
    public String application_id;
    public String private_key;
    public String baseUrl;

    public BootpayObject() {}
    public BootpayObject(String rest_application_id, String private_key) {
        this.application_id = rest_application_id;
        this.private_key = private_key;
        this.baseUrl = BootpayConfig.PRODUCTION;
    }

    public BootpayObject(String rest_application_id, String private_key, String devMode) {
        this.application_id = rest_application_id;
        this.private_key = private_key;
        this.baseUrl = devMode;
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
        URI uri = new URIBuilder(get.getURI()).addParameters(nameValuePairList).build();
        get.setURI(uri);
        return get;
    }

    public HttpPost httpPost(String url, StringEntity entity) {
        HttpPost post = new HttpPost(this.baseUrl + url);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept-Charset", "utf-8");
        post.setEntity(entity);
        return post;
    }

    public HttpDelete httpDelete(String url) {
        HttpDelete delete = new HttpDelete(this.baseUrl + url);
        delete.setHeader("Accept", "application/json");
        delete.setHeader("Content-Type", "application/json");
        delete.setHeader("Accept-Charset", "utf-8");
        return delete;
    }

    public HttpDeleteWithBody httpDeleteWithBody(String url, StringEntity entity) {
        HttpDeleteWithBody delete = new HttpDeleteWithBody(this.baseUrl + url);
        delete.setHeader("Accept", "application/json");
        delete.setHeader("Content-Type", "application/json");
        delete.setHeader("Accept-Charset", "utf-8");
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
        Type resType = new TypeToken<List<HashMap<String, Object>>>(){}.getType();
        List<HashMap<String, Object>> data = new Gson().fromJson(str, resType);
        if(data == null) {
            data = new ArrayList<>();
        }

        result.put("http_status", response.getStatusLine().getStatusCode());
        result.put("data", data);
        return result;
    }

    public HashMap<String, Object> responseToJson(HttpResponse response)  throws Exception {
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        HashMap<String, Object> result = new Gson().fromJson(str, resType);

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