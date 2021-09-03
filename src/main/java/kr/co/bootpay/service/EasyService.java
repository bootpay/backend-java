package kr.co.bootpay.service;

import com.google.gson.reflect.TypeToken;
import kr.co.bootpay.BootpayObject;
import kr.co.bootpay.model.request.UserToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.model.response.ResDefault;
import kr.co.bootpay.model.response.data.EasyUserTokenData;
import kr.co.bootpay.model.response.data.TokenData;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.lang.reflect.Type;

//간편결제창, 생체인증 기반 간편 결제 등
public class EasyService {
    static public ResDefault<EasyUserTokenData> getUserToken(BootpayObject bootpay, UserToken userToken) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(userToken.userId == null || userToken.userId.isEmpty()) throw new Exception("userId 값을 입력해주세요.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("request/user/token", new StringEntity(gson.toJson(userToken), "UTF-8"));

        post.setHeader("Authorization", bootpay.token);
        HttpResponse response = client.execute(post);

        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");

        Type resType = new TypeToken<ResDefault<EasyUserTokenData>>(){}.getType();
        ResDefault<EasyUserTokenData> res = new Gson().fromJson(str, resType);
        return res;
    }
}
