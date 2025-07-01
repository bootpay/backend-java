package kr.co.bootpay.store.service.orders;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelActionParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelListParams;
import kr.co.bootpay.store.model.request.order.cancel.OrderCancelParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SOrderCancelService {
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, OrderCancelListParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        String url = "order/cancel";
        if(params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if (params.orderId != null) nameValuePairList.add(new BasicNameValuePair("order_id", params.orderId));
            if (params.orderNumber != null) nameValuePairList.add(new BasicNameValuePair("order_number", params.orderNumber));

            HttpGet get = bootpay.httpGet(url, nameValuePairList);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }


    static public BootpayStoreResponse request(BootpayStoreObject bootpay, OrderCancelParams orderCancelParams) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

//        if(orderCancelParams.isSupervisor) role = "supervisor" + "/";
        HttpPost post = bootpay.httpPost("order/cancel", new StringEntity(gson.toJson(orderCancelParams), "UTF-8"));

        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }

    static public BootpayStoreResponse withdraw(BootpayStoreObject bootpay, String orderCancelRequestHistoryId) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // 파일 업로드 요청 (여러 파일)
        HttpPut put = bootpay.httpPut("order/cancel/" + orderCancelRequestHistoryId + "/withdraw", new StringEntity(gson.toJson(""), "UTF-8"));
        HttpResponse response = client.execute(put);

        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        System.out.println(str);

        return bootpay.responseToJsonObject(response);
        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }

    static public BootpayStoreResponse approve(BootpayStoreObject bootpay, OrderCancelActionParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // 파일 업로드 요청 (여러 파일)
        HttpPut put = bootpay.httpPut("order/cancel/" + params.orderCancelRequestHistoryId + "/approve", new StringEntity(gson.toJson(params), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);

        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }

    static public BootpayStoreResponse reject(BootpayStoreObject bootpay, OrderCancelActionParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // 파일 업로드 요청 (여러 파일)
        HttpPut put = bootpay.httpPut("order/cancel/" + params.orderCancelRequestHistoryId + "/reject", new StringEntity(gson.toJson(params), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);

        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }
}
