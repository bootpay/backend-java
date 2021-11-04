package kr.co.bootpay.service;

import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.BootpayObject;
import kr.co.bootpay.model.request.Submit;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.model.response.ResDefault;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;

public class SubmitService {
    static public ResDefault<HashMap<String, Object>> submit(BootpayObject bootpay, String receiptId) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(receiptId == null || receiptId.isEmpty()) throw new Exception("userId 값을 입력해주세요.");

        Submit submit = new Submit();
        submit.receiptId = receiptId;

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("submit", new StringEntity(gson.toJson(submit), "UTF-8"));

        post.setHeader("Authorization", bootpay.token);
        HttpResponse response = client.execute(post);
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<ResDefault<HashMap<String, Object>>>(){}.getType();
        ResDefault res = new Gson().fromJson(str, resType);
        return res;
    }
}
