package kr.co.bootpay.store.service.order_subscription_bill;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.orderSubscriptionBill.OrderSubscriptionBillListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;
import kr.co.bootpay.store.model.request.ListParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class SOrderSubscriptionBillService {
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, OrderSubscriptionBillListParams params) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        String url = "order_subscription_bills";
        if(params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if (params.orderSubscriptionId != null) nameValuePairList.add(new BasicNameValuePair("order_subscription_id", params.orderSubscriptionId));
            if (params.keyword != null) nameValuePairList.add(new BasicNameValuePair("keyword", params.keyword));
            if (params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
            if (params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));
            if (params.status != null && !params.status.isEmpty()) {
                String statusStr = params.status.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
                nameValuePairList.add(new BasicNameValuePair("status", statusStr));
            }

            HttpGet get = bootpay.httpGet(url, nameValuePairList);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url);
            HttpResponse response = client.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }


    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String orderSubscriptionBillId) throws Exception {
        if(bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("order_subscription_bills/" + orderSubscriptionBillId);

        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, SOrderSubscriptionBill orderSubscriptionBill) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // 파일 업로드 요청 (여러 파일)
//        HttpPost post = bootpay.httpPostMultipart("products", fileList, params);
        HttpPut put = bootpay.httpPut("order_subscription_bills/" + orderSubscriptionBill.orderSubscriptionBillId, new StringEntity(gson.toJson(orderSubscriptionBill), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);

        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }
}
