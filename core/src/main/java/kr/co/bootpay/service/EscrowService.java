package kr.co.bootpay.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.BootpayObject;
import kr.co.bootpay.model.request.Shipping;
import kr.co.bootpay.model.request.UserToken;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;

//간편결제창, 생체인증 기반 간편 결제 등
public class EscrowService {
    static public HashMap<String, Object> putShippingStart(BootpayObject bootpay, Shipping shipping) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
//        if(userToken.userId == null || userToken.userId.isEmpty()) throw new Exception("userId 값을 입력해주세요.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPut put = bootpay.httpPut("escrow/shipping/start/" + shipping.receiptId, new StringEntity(gson.toJson(shipping), "UTF-8"));


        put.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(put);

        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        return new Gson().fromJson(str, resType);
    }
}
