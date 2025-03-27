package kr.co.bootpay.store.service.orders;

import com.google.gson.Gson;
import kr.co.bootpay.store.BootpayStoreObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static kr.co.bootpay.BootpayResponse.responseJson;

public class SOrderService {
    static public HashMap<String, Object> list(BootpayStoreObject bootpay, Optional<String> keyword, Optional<Integer> page, Optional<Integer> limit) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        // 파라미터 맵 초기화
        Map<String, Object> params = new HashMap<>();
        keyword.ifPresent(value -> params.put("keyword", value));
        page.ifPresent(value -> params.put("page", value));
        limit.ifPresent(value -> params.put("limit", value));

        // 파라미터를 URL 쿼리 문자열로 변환
        StringBuilder query = new StringBuilder("orders?");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        // 마지막 '&' 제거
        if (query.charAt(query.length() - 1) == '&') {
            query.deleteCharAt(query.length() - 1);
        }

        // GET 요청 객체 생성
        HttpGet get = bootpay.httpGet(query.toString());

        // HTTP 요청 전송 및 응답 수신
        HttpResponse response = client.execute(get);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }


    static public HashMap<String, Object> detail(BootpayStoreObject bootpay, String orderId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("orders/" + orderId);

        HttpResponse response = client.execute(get);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        return responseJson(new Gson(), str, response.getStatusLine().getStatusCode());
    }
}
