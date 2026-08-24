package kr.co.bootpay.store.service.mall_setting;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.pojo.SMallSetting;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

public class SMallSettingService {

    /**
     * 몰 설정 조회
     * GET /v1/mall-setting
     * supervisor scope 토큰 전용
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpGet get = bootpay.httpGet("mall-setting", supervisorContext(idempotencyKey));

        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 몰 설정 수정
     * PUT /v1/mall-setting
     * supervisor scope 토큰 전용
     * 요청 바디는 flatten 형식이며 전달된 값(non-null)만 서버로 전송된다.
     */
    static public BootpayStoreResponse update(BootpayStoreObject bootpay, SMallSetting setting, String idempotencyKey) throws Exception {
        bootpay.requireCommerceCredentials();
        if (setting == null) throw new Exception("setting 값이 비어있습니다.");

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpPut put = bootpay.httpPut("mall-setting", new StringEntity(gson.toJson(setting), "UTF-8"), supervisorContext(idempotencyKey));

        HttpResponse response = bootpay.execute(put);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * supervisor 전용 요청 컨텍스트 — Idempotency-Key 는 미지정시 매 호출마다 생성된다.
     */
    private static RequestContext supervisorContext(String idempotencyKey) {
        return RequestContext.builder()
                .role("supervisor")
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }
}
