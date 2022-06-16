package kr.co.bootpay.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.BootpayObject;
import kr.co.bootpay.model.BankCode;
import kr.co.bootpay.model.request.Cancel;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;

public class CancelService {
    static public HashMap<String, Object> receiptCancel(BootpayObject bootpay, Cancel cancel) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(cancel.receiptId == null || cancel.receiptId.isEmpty()) throw new Exception("receiptId 값을 입력해주세요.");

//        String temp = BankCode.강원.name();

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("cancel", new StringEntity(gson.toJson(cancel), "UTF-8"));
        post.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(post);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<HashMap<String, Object>>(){}.getType();
        return new Gson().fromJson(str, resType);
    }
}
