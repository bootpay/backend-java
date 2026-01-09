package kr.co.bootpay.store.service.projects;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class SProjectService {

    /**
     * 현재 연결된 프로젝트 정보를 조회합니다.
     * SDK 초기화 시 설정한 application_id와 private_key로 인증된 프로젝트 정보를 반환합니다.
     *
     * @param bootpay BootpayStoreObject 인스턴스
     * @return BootpayStoreResponse 프로젝트 정보 (project_id, name, status 등)
     */
    static public BootpayStoreResponse me(BootpayStoreObject bootpay) throws Exception {
        if (bootpay.getToken() == null || bootpay.getToken().isEmpty()) {
            throw new Exception("token 값이 비어있습니다.");
        }

        HttpClient client = HttpClientBuilder.create().build();

        HttpGet get = bootpay.httpGet("projects/me");
        HttpResponse response = client.execute(get);
        return bootpay.responseToJsonObject(response);
    }
}
