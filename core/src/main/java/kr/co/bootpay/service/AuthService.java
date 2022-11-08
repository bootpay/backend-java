package kr.co.bootpay.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.BootpayObject;
import kr.co.bootpay.model.request.Authentication;
import kr.co.bootpay.model.request.AuthenticationParams;
import kr.co.bootpay.model.request.Subscribe;
import kr.co.bootpay.model.request.SubscribePayload;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;

//REST API 본인인증 전용
public class AuthService {
    //본인인증 요청
    static public HashMap<String, Object> requestAuthentication(BootpayObject bootpay, Authentication authentication) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(authentication.orderName == null || authentication.orderName.isEmpty()) throw new Exception("order_name 값을 입력해주세요.");
        if(authentication.authenticationId == null || authentication.authenticationId.isEmpty()) throw new Exception("authenticationId 주문번호를 설정해주세요.");
        if(authentication.identityNo == null || authentication.identityNo.isEmpty()) throw new Exception("생년월일 + 주민등록번호 뒷 1자리를 입력해주세요.");



        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("request/authentication", new StringEntity(gson.toJson(authentication), "UTF-8"));

        post.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(post);
        return bootpay.responseToJson(response);
    }

    //본인인증 승인
    static public HashMap<String, Object> confirmAuthentication(BootpayObject bootpay, String receiptId, String otp) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(receiptId == null || receiptId.isEmpty()) throw new Exception("receiptId 값을 비어있습니다.");
        if(otp == null || otp.isEmpty()) throw new Exception("otp 값을 비어있습니다.");

        AuthenticationParams authenticationParams = new AuthenticationParams();
        authenticationParams.receiptId = receiptId;
        authenticationParams.otp = otp;

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("authenticate/confirm", new StringEntity(gson.toJson(authenticationParams), "UTF-8"));

        post.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(post);
        return bootpay.responseToJson(response);
    }


    //SMS로 본인인증 요청시 SMS 재발송 로직
    static public HashMap<String, Object> realarmAuthentication(BootpayObject bootpay, String receiptId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(receiptId == null || receiptId.isEmpty()) throw new Exception("receiptId 값을 비어있습니다.");


        AuthenticationParams authenticationParams = new AuthenticationParams();
        authenticationParams.receiptId = receiptId;

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("authenticate/realarm", new StringEntity(gson.toJson(authenticationParams), "UTF-8"));

        post.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(post);
        return bootpay.responseToJson(response);
    }
}
