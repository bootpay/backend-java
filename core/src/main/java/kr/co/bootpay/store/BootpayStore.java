package kr.co.bootpay.store;


import kr.co.bootpay.store.layer.*;
import kr.co.bootpay.store.model.pojo.SOrderSubscriptionAdjustment;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.STokenService;

import java.util.HashMap;

public class BootpayStore extends BootpayStoreObject {
    public User user;
    public UserGroup userGroup;
    public Product product;
    public Store store;
    public Invoice invoice;
    public Order order;
    public OrderCancel orderCancel;
    public OrderSubscription orderSubscription;
    public OrderSubscriptionBill orderSubscriptionBill;
    public OrderSubscriptionAdjustment orderSubscriptionAdjustment;



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

    private void initModules() {
        this.user = new User(this);
        this.userGroup = new UserGroup(this);
        this.product = new Product(this);
        this.store = new Store(this);
        this.invoice = new Invoice(this);
        this.order = new Order(this);
        this.orderCancel = new OrderCancel(this);
        this.orderSubscription = new OrderSubscription(this);
        this.orderSubscriptionBill = new OrderSubscriptionBill(this);
        this.orderSubscriptionAdjustment = new OrderSubscriptionAdjustment(this);
    }

    //token
    public BootpayStoreResponse getAccessToken() throws Exception {
        return STokenService.getAccessToken(this);
    }
    
    /**
     * 토큰을 발급받아 설정합니다.
     * @return this (메서드 체이닝 지원)
     */
    public BootpayStore withToken() throws Exception {
        BootpayStoreResponse response = getAccessToken();
        if (response.isSuccess()) {
            HashMap<String, Object> data = response.getData();
            if (data != null && data.containsKey("access_token")) {
                this.setTokenFromAPI((String) data.get("access_token"));
            }
        }
        return this;
    }
    
    /**
     * 현재 설정된 토큰을 반환합니다.
     * @return 현재 토큰
     */
    public String getCurrentToken() {
        return this.getToken();
    }
    
    /**
     * 토큰이 설정되어 있는지 확인합니다.
     * @return 토큰 설정 여부
     */
    public boolean hasToken() {
        return this.getToken() != null && !this.getToken().isEmpty();
    }
    
    /**
     * 현재 role을 설정합니다.
     * @param role 설정할 role
     * @return this (메서드 체이닝 지원)
     */
    public BootpayStore withRole(String role) {
        this.setRole(role);
        return this;
    }
    
    /**
     * 일반 사용자 role로 설정합니다.
     * @return this (메서드 체이닝 지원)
     */
    public BootpayStore asUser() {
        return withRole("user");
    }
    
    /**
     * 매니저 role로 설정합니다.
     * @return this (메서드 체이닝 지원)
     */
    public BootpayStore asManager() {
        return withRole("manager");
    }
    
    /**
     * 파트너 role로 설정합니다.
     * @return this (메서드 체이닝 지원)
     */
    public BootpayStore asPartner() {
        return withRole("partner");
    }
    
    /**
     * 벤더 role로 설정합니다.
     * @return this (메서드 체이닝 지원)
     */
    public BootpayStore asVendor() {
        return withRole("vendor");
    }
    
    /**
     * 슈퍼바이저 role로 설정합니다.
     * @return this (메서드 체이닝 지원)
     */
    public BootpayStore asSupervisor() {
        return withRole("supervisor");
    }
    
    /**
     * 현재 role을 반환합니다.
     * @return 현재 설정된 role
     */
    public String getCurrentRole() {
        return this.getRole();
    }
    
    /**
     * role을 기본값(user)으로 초기화합니다.
     * @return this (메서드 체이닝 지원)
     */
    public BootpayStore clearRole() {
        this.setRole("user");
        return this;
    }
}

