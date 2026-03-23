package kr.co.bootpay.store.service.store;

import kr.co.bootpay.store.BootpayStoreObject;
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
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("store");
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
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("store/detail");
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }
}
