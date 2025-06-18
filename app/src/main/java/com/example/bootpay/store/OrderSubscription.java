package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.TokenPayload;

import java.util.HashMap;


public class OrderSubscription {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("4T4tlQq2xpPHioq216K-RQ", "szucYyZ9NtrmUtCu6gtUEm6aMOnhFQqCiSE9AK9I6fo=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
    //        list();
    //        detail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getToken() {
        try {
            BootpayStoreResponse res = bootpayStore.getAccessToken();
            if(res.isSuccess()) {
                System.out.println("goGetToken success: " + res.getDataAsMap());
            } else {
                System.out.println("goGetToken false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void list() {
        try {
            BootpayStoreResponse res = bootpayStore.orderSubscription.list(null);
            if(res.isSuccess()) {
                System.out.println("orderSubscription list success: " + res.getDataAsMap());
            } else {
                System.out.println("orderSubscription list false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detail() {
        try {
            BootpayStoreResponse res = bootpayStore.orderSubscription.detail("67e5100c5ec892162491d108");
            if(res.isSuccess()) {
                System.out.println("orderSubscription detail success: " + res.getDataAsMap());
            } else {
                System.out.println("orderSubscription detail false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

