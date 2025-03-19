package kr.co.bootpay.pg.service;

import kr.co.bootpay.pg.BootpayObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;

//간편결제창, 생체인증 기반 간편 결제 등
public class SellerService {
    static public HashMap<String, Object> lookupPaymentMethods(BootpayObject bootpay) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
//        if(userToken.userId == null || userToken.userId.isEmpty()) throw new Exception("userId 값을 입력해주세요.");

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("seller/payment/method" );

        get.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonArray(response);
    }
}
