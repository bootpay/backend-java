package kr.co.bootpay.store.service.store;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class SStoreService {
    public static BootpayStoreResponse info(BootpayStoreObject bootpay) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = bootpay.httpGet("store");
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    public static BootpayStoreResponse detail(BootpayStoreObject bootpay) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) throw new Exception("token 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = bootpay.httpGet("store/detail");
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }
}
