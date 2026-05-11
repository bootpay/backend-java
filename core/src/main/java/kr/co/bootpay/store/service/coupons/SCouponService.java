package kr.co.bootpay.store.service.coupons;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.coupon.CouponDownloadParams;
import kr.co.bootpay.store.model.request.coupon.CouponListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SCouponService {

    static public BootpayStoreResponse list(BootpayStoreObject bootpay, CouponListParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        String url = "coupon";
        if (params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if (params.status != null) nameValuePairList.add(new BasicNameValuePair("status", params.status));
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

    static public BootpayStoreResponse available(BootpayStoreObject bootpay) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("coupon/available");
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse download(BootpayStoreObject bootpay, CouponDownloadParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        if (params == null) {
            throw new Exception("params 값이 비어있습니다.");
        }
        if (params.couponTemplateId == null || params.couponTemplateId.isEmpty()) {
            throw new Exception("couponTemplateId 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPost post = bootpay.httpPost("coupon/download", new StringEntity(gson.toJson(params), "UTF-8"));
        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }
}
