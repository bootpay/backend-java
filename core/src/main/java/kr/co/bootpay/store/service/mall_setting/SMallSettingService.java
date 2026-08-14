package kr.co.bootpay.store.service.mall_setting;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.mallSetting.MallSettingUpdateParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.UUID;

public class SMallSettingService {
    // 몰 설정 조회 (GET mall-setting)
    // supervisor scope 토큰 전용
    static public BootpayStoreResponse getMallSetting(BootpayStoreObject bootpay, String idempotencyKey) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = getRequest(bootpay, idempotencyKey);
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse getMallSetting(BootpayStoreObject bootpay) throws Exception {
        return getMallSetting(bootpay, null);
    }

    static public BootpayStoreResponse get(BootpayStoreObject bootpay, String idempotencyKey) throws Exception {
        return getMallSetting(bootpay, idempotencyKey);
    }

    static public BootpayStoreResponse get(BootpayStoreObject bootpay) throws Exception {
        return getMallSetting(bootpay, null);
    }

    // 몰 설정 수정 (PUT mall-setting)
    // supervisor scope 토큰 전용
    // 요청 바디는 flatten 형식이며 값이 설정된(non-null) 필드만 서버로 전송된다
    static public BootpayStoreResponse updateMallSetting(BootpayStoreObject bootpay, MallSettingUpdateParams params) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPut put = updateRequest(bootpay, params);
        HttpResponse response = client.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse update(BootpayStoreObject bootpay, MallSettingUpdateParams params) throws Exception {
        return updateMallSetting(bootpay, params);
    }

    static HttpGet getRequest(BootpayStoreObject bootpay, String idempotencyKey) throws Exception {
        validateAuthorization(bootpay);

        HttpGet get = bootpay.httpGet("mall-setting");
        setSupervisorHeaders(get, idempotencyKey);
        return get;
    }

    static HttpPut updateRequest(BootpayStoreObject bootpay, MallSettingUpdateParams params) throws Exception {
        validateAuthorization(bootpay);
        if (params == null) throw new Exception("params 값이 비어있습니다");

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("mall-setting", new StringEntity(gson.toJson(params), "UTF-8"));
        setSupervisorHeaders(put, params.idempotencyKey);
        return put;
    }

    // 토큰이 없다면 client key / secret key 기반의 Basic 인증을 사용한다
    static private void validateAuthorization(BootpayStoreObject bootpay) throws Exception {
        if (bootpay.getAuthorizationHeader(null) == null) throw new Exception("token 값이 비어있습니다.");
    }

    static private void setSupervisorHeaders(HttpRequestBase request, String idempotencyKey) {
        request.setHeader("Idempotency-Key", (idempotencyKey == null || idempotencyKey.isEmpty()) ? UUID.randomUUID().toString() : idempotencyKey);
        request.setHeader("BOOTPAY-ROLE", "supervisor");
    }
}
