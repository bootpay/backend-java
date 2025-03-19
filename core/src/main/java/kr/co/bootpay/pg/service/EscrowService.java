package kr.co.bootpay.pg.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Shipping;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;

//간편결제창, 생체인증 기반 간편 결제 등
public class EscrowService {
    static public HashMap<String, Object> shippingStart(BootpayObject bootpay, Shipping shipping) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
//        if(userToken.userId == null || userToken.userId.isEmpty()) throw new Exception("userId 값을 입력해주세요.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPut put = bootpay.httpPut("escrow/shipping/start/" + shipping.receiptId, new StringEntity(gson.toJson(shipping), "UTF-8"));


        put.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(put);
        return bootpay.responseToJson(response);
    }
}
