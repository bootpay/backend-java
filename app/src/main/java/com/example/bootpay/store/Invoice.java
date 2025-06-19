package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.model.pojo.SInvoice;

import java.util.List;


public class Invoice {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("4T4tlQq2xpPHioq216K-RQ", "szucYyZ9NtrmUtCu6gtUEm6aMOnhFQqCiSE9AK9I6fo=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
//            list();
//            create();
            _notify();
//            detail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getToken() {
        try {
            BootpayStoreResponse res = bootpayStore.getAccessToken();
            if(res.isSuccess()) {
                System.out.println("goGetToken success: " + res.getData());
            } else {
                System.out.println("goGetToken false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void list() {
        try {
            ListParams params = new ListParams();
//            params.keyword = "테스트";

            BootpayStoreResponse res = bootpayStore.invoice.list(params);
            if(res.isSuccess()) {
                System.out.println("invoice list success: " + res.getData());
            } else {
                System.out.println("invoice list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void create() {
        try {
            SInvoice invoice = new SInvoice();
//            invoice.orderId = "67e4ead95ec892162491d0f3";
            invoice.price = 1000.0;
            invoice.name = "테스트 상품";
            invoice.selectedUsers = List.of("68527d03b0eacea5cd974821");

            BootpayStoreResponse res = bootpayStore.invoice.create(invoice);
            if(res.isSuccess()) {
                System.out.println("invoice create success: " + res.getData());
            } else {
                System.out.println("invoice create false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void _notify() {
        try {
            List<Integer> sendTypes = List.of(
                    SInvoice.SEND_TYPE_KAKAO,
                    SInvoice.SEND_TYPE_EMAIL
            );

            BootpayStoreResponse res = bootpayStore.invoice.notify("6853a79bb0eacea5cd9748da", sendTypes);
            if(res.isSuccess()) {
                System.out.println("invoice notify success: " + res.getData());
            } else {
                System.out.println("invoice notify false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detail() {
        try {
            BootpayStoreResponse res = bootpayStore.invoice.detail("67e4e9ecd01640bb9859c64d");
            if(res.isSuccess()) {
                System.out.println("invoice detail success: " + res.getData());
            } else {
                System.out.println("invoice detail false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

