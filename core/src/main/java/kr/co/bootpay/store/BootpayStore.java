package kr.co.bootpay.store;


import kr.co.bootpay.store.service.STokenService;

import java.util.HashMap;

public class BootpayStore extends BootpayStoreObject {
    public BootpayStore() { }

    public BootpayStore(String serverKey, String privateKey) {
        super(serverKey, privateKey);
    }

    public BootpayStore(String restApplicationId, String privateKey, String devMode) {
        super(restApplicationId, privateKey, devMode);
    }

    //token
    public HashMap<String, Object> getAccessToken() throws Exception {
        return STokenService.getAccessToken(this);
    }
}
