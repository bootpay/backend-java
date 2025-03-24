package kr.co.bootpay.store;


import kr.co.bootpay.store.layer.User;
import kr.co.bootpay.store.service.STokenService;

import java.util.HashMap;

public class BootpayStore extends BootpayStoreObject {
    public User user;

    public BootpayStore() {
        super();
        initModules();
    }

    public BootpayStore(String serverKey, String privateKey) {
        super(serverKey, privateKey);
        initModules();
    }

    public BootpayStore(String restApplicationId, String privateKey, String devMode) {
        super(restApplicationId, privateKey, devMode);
        initModules();
    }

    private void initModules() {
        this.user = new User(this);
    }

    //token
    public HashMap<String, Object> getAccessToken() throws Exception {
        return STokenService.getAccessToken(this);
    }



}

