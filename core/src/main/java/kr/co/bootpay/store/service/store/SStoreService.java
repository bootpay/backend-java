package kr.co.bootpay.store.service.store;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.context.RequestContext;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class SStoreService {

    /**
     * 가맹점 기본 정보 조회
     *
     * @param bootpay BootpayStoreObject 인스턴스
     * @return BootpayStoreResponse 가맹점 기본 정보
     */
    static public BootpayStoreResponse info(BootpayStoreObject bootpay) throws Exception {
        return info(bootpay, null);
    }

    /**
     * 가맹점 기본 정보 조회
     * @param idempotencyKey 미지정시 자동 생성 (Idempotency-Key 헤더로 전송)
     */
    static public BootpayStoreResponse info(BootpayStoreObject bootpay, String idempotencyKey) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("store", storeContext(idempotencyKey));
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 가맹점 상세 정보 조회
     *
     * @param bootpay BootpayStoreObject 인스턴스
     * @return BootpayStoreResponse 가맹점 상세 정보
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay) throws Exception {
        return detail(bootpay, null);
    }

    /**
     * 가맹점 상세 정보 조회
     * @param idempotencyKey 미지정시 자동 생성 (Idempotency-Key 헤더로 전송)
     */
    static public BootpayStoreResponse detail(BootpayStoreObject bootpay, String idempotencyKey) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("store/detail", storeContext(idempotencyKey));
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    /**
     * 가맹점 정보 조회 요청 컨텍스트 — Idempotency-Key 는 미지정시 매 호출마다 생성된다.
     */
    private static RequestContext storeContext(String idempotencyKey) {
        return RequestContext.builder()
                .idempotencyKey(RequestContext.idempotencyKeyOrGenerate(idempotencyKey))
                .build();
    }
}
