package kr.co.bootpay.store.service.order_subscription_bill;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionBill;
import kr.co.bootpay.store.model.pojo.SProduct;
import kr.co.bootpay.store.model.request.ListParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class SOrderSubscriptionBillService {
    static public HashMap<String, Object> list(BootpayStoreObject bootpay, ListParams params) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // 파라미터 맵 초기화
        Map<String, Object> payload = new HashMap<>();
        if (params.keyword != null) payload.put("keyword", params.keyword);
        if (params.page != null) payload.put("page", params.page);
        if (params.limit != null) payload.put("limit", params.limit);

        // 파라미터를 URL 쿼리 문자열로 변환
        String role = "user" + "/";
        StringBuilder query = new StringBuilder(role + "order_subscription_bills?");
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            String encodedValue = URLEncoder.encode(entry.getValue().toString(), "UTF-8");
            query.append(entry.getKey()).append("=").append(encodedValue).append("&");
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
    }


    static public HashMap<String, Object> detail(BootpayStoreObject bootpay, String orderSubscriptionBillId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        String role = "user" + "/";
        HttpGet get = bootpay.httpGet(role + "order_subscription_bills/" + orderSubscriptionBillId);

        HttpResponse response = client.execute(get);
        return bootpay.responseToJson(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }

    static public HashMap<String, Object> update(BootpayStoreObject bootpay, SOrderSubscriptionBill orderSubscriptionBill) throws Exception {
        if (bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // Gson을 사용하여 Product 객체를 JSON 문자열로 변환
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // 파일 업로드 요청 (여러 파일)
//        HttpPost post = bootpay.httpPostMultipart("products", fileList, params);
        String role = "user" + "/";
        HttpPut put = bootpay.httpPut(role + "order_subscription_bills/" + orderSubscriptionBill.orderSubscriptionBillId, new StringEntity(gson.toJson(orderSubscriptionBill), "UTF-8"));
        HttpResponse response = client.execute(put);
        return bootpay.responseToJson(response);

        // 응답 처리
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(gson, str, response.getStatusLine().getStatusCode());
    }
}
