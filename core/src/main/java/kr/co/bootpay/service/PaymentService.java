package kr.co.bootpay.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.BootpayObject;
import kr.co.bootpay.model.request.Confirm;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;

public class PaymentService {
    static public HashMap<String, Object> lookupOrderId(BootpayObject bootpay, String orderId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(orderId == null || orderId.isEmpty()) throw new Exception("userId 값을 입력해주세요.");


        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("lookup/order/" + orderId);

        get.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonArray(response);
    }
}
