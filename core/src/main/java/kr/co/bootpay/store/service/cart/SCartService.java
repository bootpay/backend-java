package kr.co.bootpay.store.service.cart;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.cart.OrderPreviewParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class SCartService {

    static public BootpayStoreResponse orderPreview(BootpayStoreObject bootpay, OrderPreviewParams params) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }
        HttpClient client = HttpClientBuilder.create().build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // params 가 null 이면 빈 객체 전송 (member_mode default 'guest' 는 서버측 처리)
        Object body = params != null ? params : new OrderPreviewParams();
        HttpPost post = bootpay.httpPost("cart/order-preview", new StringEntity(gson.toJson(body), "UTF-8"));
        HttpResponse response = client.execute(post);
        return bootpay.responseToJsonObject(response);
    }
}
