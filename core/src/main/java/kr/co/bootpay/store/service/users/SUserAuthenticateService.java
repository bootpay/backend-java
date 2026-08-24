package kr.co.bootpay.store.service.users;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

public class SUserAuthenticateService {

    /**
     * Registers an individual user by creating an SUser object and sending the registration request to Bootpay.
     * The method handles both success and error scenarios.
     */
    static public BootpayStoreResponse authenticationData(BootpayStoreObject bootpay, String standId) throws Exception {
        bootpay.requireCommerceCredentials();
        // URL 구조: users/join/:path?pk=:pk
        String url = String.format("users/authenticate/%s", standId);
        HttpGet get = bootpay.httpGet(url);
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }
}
