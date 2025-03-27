package kr.co.bootpay.store;


import kr.co.bootpay.store.layer.*;
import kr.co.bootpay.store.service.STokenService;

import java.util.HashMap;

public class BootpayStore extends BootpayStoreObject {
    public User user;
    public UserGroup userGroup;
    public Product product;
    public Invoice invoice;
    public Order order;
    public OrderSubscription orderSubscription;

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
        this.userGroup = new UserGroup(this);
        this.product = new Product(this);
        this.invoice = new Invoice(this);
        this.order = new Order(this);
        this.orderSubscription = new OrderSubscription(this);
    }

    //token
    public HashMap<String, Object> getAccessToken() throws Exception {
        return STokenService.getAccessToken(this);
    }



}

