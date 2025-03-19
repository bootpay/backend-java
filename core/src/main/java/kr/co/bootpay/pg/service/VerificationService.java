package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;

public class VerificationService {
    static public HashMap<String, Object> receipt(BootpayObject bootpay, String receiptId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = bootpay.httpGet("receipt/" + receiptId);
        get.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(get);
        return bootpay.responseToJson(response);
    }

    static public HashMap<String, Object> certificate(BootpayObject bootpay, String receiptId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = bootpay.httpGet("certificate/" + receiptId + ".json");
        get.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(get);
        return bootpay.responseToJson(response);
    }
}
