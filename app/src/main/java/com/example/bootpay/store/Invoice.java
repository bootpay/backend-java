package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.model.pojo.SInvoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Invoice {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("4T4tlQq2xpPHioq216K-RQ", "szucYyZ9NtrmUtCu6gtUEm6aMOnhFQqCiSE9AK9I6fo=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
    //        list();
    //        create();
    //        notifyInvoice();
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
            ListParams params = new ListParams();
            params.keyword = "테스트";

            BootpayStoreResponse res = bootpayStore.invoice.list(params);
            if(res.isSuccess()) {
                System.out.println("invoice list success: " + res.getDataAsMap());
            } else {
                System.out.println("invoice list false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void create() {
        try {
            SInvoice invoice = new SInvoice();
            invoice.orderId = "67e4ead95ec892162491d0f3";
            invoice.price = 1000.0;
            invoice.name = "테스트 상품";

            BootpayStoreResponse res = bootpayStore.invoice.create(invoice);
            if(res.isSuccess()) {
                System.out.println("invoice create success: " + res.getDataAsMap());
            } else {
                System.out.println("invoice create false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void notifyInvoice() {
        try {
            List<Integer> sendTypes = List.of(
                    SInvoice.SEND_TYPE_KAKAO,
                    SInvoice.SEND_TYPE_EMAIL
            );

            BootpayStoreResponse res = bootpayStore.invoice.notify("67e4ead95ec892162491d0f3", sendTypes);
            if(res.isSuccess()) {
                System.out.println("invoice notify success: " + res.getDataAsMap());
            } else {
                System.out.println("invoice notify false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detail() {
        try {
            BootpayStoreResponse res = bootpayStore.invoice.detail("67e4ead95ec892162491d0f3");
            if(res.isSuccess()) {
                System.out.println("invoice detail success: " + res.getDataAsMap());
            } else {
                System.out.println("invoice detail false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

