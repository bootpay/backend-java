package kr.co.bootpay.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.BootpayObject;
import kr.co.bootpay.model.response.ResDefault;
import kr.co.bootpay.model.response.data.CertificateData;
import kr.co.bootpay.model.response.data.TokenData;
import kr.co.bootpay.model.response.data.VerificationData;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;

public class VerificationService {
    static public ResDefault<VerificationData> verify(BootpayObject bootpay, String receiptId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = bootpay.httpGet("receipt/" + receiptId);
        get.setHeader("Authorization", bootpay.token);
        HttpResponse response = client.execute(get);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<ResDefault<VerificationData>>(){}.getType();
        ResDefault<VerificationData> res = new Gson().fromJson(str, resType);
        return res;
    }

    static public ResDefault<CertificateData> certificate(BootpayObject bootpay, String receiptId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = bootpay.httpGet("certificate/" + receiptId);
        get.setHeader("Authorization", bootpay.token);
        HttpResponse response = client.execute(get);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<ResDefault<CertificateData>>(){}.getType();
        ResDefault<CertificateData> res = new Gson().fromJson(str, resType);
        return res;
    }
}
