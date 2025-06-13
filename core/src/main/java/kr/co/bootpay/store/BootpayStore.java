package kr.co.bootpay.store;


import kr.co.bootpay.store.layer.*;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.service.STokenService;

import java.util.HashMap;

public class BootpayStore extends BootpayStoreObject {
    public User user;
    public UserGroup userGroup;
    public Product product;
    public Invoice invoice;
    public Order order;
    public OrderCancel orderCancel;
    public OrderSubscription orderSubscription;
    public OrderSubscriptionBill orderSubscriptionBill;

    public BootpayStore() {
        super();
        initModules();
    }

    public BootpayStore(TokenPayload tokenPayload) {
        super(tokenPayload);
        initModules();
    }

    public BootpayStore(TokenPayload tokenPayload, String devMode) {
        super(tokenPayload, devMode);
        initModules();
    }

//    public BootpayStore(String serverKey, String privateKey, String devMode) {
//        super(serverKey, privateKey, devMode);
//        initModules();
//    }

    private void initModules() {
        this.user = new User(this);
        this.userGroup = new UserGroup(this);
        this.product = new Product(this);
        this.invoice = new Invoice(this);
        this.order = new Order(this);
        this.orderCancel = new OrderCancel(this);
        this.orderSubscription = new OrderSubscription(this);
        this.orderSubscriptionBill = new OrderSubscriptionBill(this);
    }

    //token
    public HashMap<String, Object> getAccessToken() throws Exception {
        return STokenService.getAccessToken(this);
    }
}

