package kr.co.bootpay.store.service.orders;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.order.OrderListParams;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class SOrderService {
    static public BootpayStoreResponse list(BootpayStoreObject bootpay, OrderListParams params) throws Exception {

        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        // 파라미터 맵 초기화
        Map<String, Object> payload = new HashMap<>();
        if (params.keyword != null) payload.put("keyword", params.keyword);
        if (params.csType != null) payload.put("cs_type", params.csType);
        if (params.cssAt != null) payload.put("css_at", params.cssAt);
        if (params.cseAt != null) payload.put("cse_at", params.cseAt);
        if (params.page != null) payload.put("page", params.page);
        if (params.limit != null) payload.put("limit", params.limit);
        if (params.userId != null) payload.put("user_id", params.userId);
        if (params.userGroupId != null) payload.put("user_group_id", params.userGroupId);

        if (params.status != null && !params.status.isEmpty()) {
            String joined = params.status.stream().map(String::valueOf).collect(Collectors.joining(","));
            payload.put("status", joined);
        }

        if (params.paymentStatus != null && !params.paymentStatus.isEmpty()) {
            String joined = params.paymentStatus.stream().map(String::valueOf).collect(Collectors.joining(","));
            payload.put("payment_status", joined);
        }

        // 파라미터를 URL 쿼리 문자열로 변환
        StringBuilder query = new StringBuilder("orders?");
        for (Map.Entry<String, Object> entry : payload.entrySet()) {
            String encodedValue = URLEncoder.encode(entry.getValue().toString(), "UTF-8");
            query.append(entry.getKey()).append("=").append(encodedValue).append("&");
//            query.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        // 마지막 '&' 제거
        if (query.charAt(query.length() - 1) == '&') {
            query.deleteCharAt(query.length() - 1);
        }

        // GET 요청 객체 생성
        HttpGet get = bootpay.httpGet(query.toString());

        // HTTP 요청 전송 및 응답 수신
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }

    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String orderId) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("orders/" + orderId);

        HttpResponse response = client.execute(get);

        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        System.out.println(str);

        return bootpay.responseToJsonObject(response);
//        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
//        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }
}
