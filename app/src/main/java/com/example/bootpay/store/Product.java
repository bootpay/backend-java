package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.BootpayStoreResponse;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.ProductListParams;
import kr.co.bootpay.store.model.request.product.ProductStatusParams;
import kr.co.bootpay.store.model.pojo.SProduct;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Product {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("4T4tlQq2xpPHioq216K-RQ", "szucYyZ9NtrmUtCu6gtUEm6aMOnhFQqCiSE9AK9I6fo=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
    //        create();
    //        list();
    //        update();
    //        detail();
    //        status();
    //        delete();
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

    public static void create() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL imagePath = classLoader.getResource("images/logo.png");
            assert imagePath != null;
            List<URL> imagePaths = List.of(imagePath); //상품 이미지 등록

            SProduct product = new SProduct();
            product.name = "테스트 상품";
            product.displayPrice = 1000;

            BootpayStoreResponse res = bootpayStore.product.create(product, imagePaths);
            if(res.isSuccess()) {
                System.out.println("product create success: " + res.getDataAsMap());
            } else {
                System.out.println("product create false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void list() {
        try {
            ProductListParams params = new ProductListParams();
            params.keyword = "테스트";

            BootpayStoreResponse res = bootpayStore.product.list(params);
            if(res.isSuccess()) {
                System.out.println("product list success: " + res.getDataAsMap());
            } else {
                System.out.println("product list false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        try {
            SProduct product = new SProduct();
            product.productId = "67e4b4425ec892162491d0ec";
            product.name = "수정된 상품명";
            product.displayPrice = 2000;

            BootpayStoreResponse res = bootpayStore.product.update(product);
            if(res.isSuccess()) {
                System.out.println("product update success: " + res.getDataAsMap());
            } else {
                System.out.println("product update false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detail() {
        try {
            BootpayStoreResponse res = bootpayStore.product.detail("67e4b4425ec892162491d0ec");
            if(res.isSuccess()) {
                System.out.println("product detail success: " + res.getDataAsMap());
            } else {
                System.out.println("product detail false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void status() {
        try {
            ProductStatusParams params = new ProductStatusParams();
            params.productId = "67e4b4425ec892162491d0ec";
            params.statusDisplay = false;  // 전시상태 변경
            params.statusSale = false;   // 판매상태 변경

            BootpayStoreResponse res = bootpayStore.product.status(params);
            if(res.isSuccess()) {
                System.out.println("product status success: " + res.getDataAsMap());
            } else {
                System.out.println("product status false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete() {
        try {
            String productId = "67e4b4425ec892162491d0ec";
            BootpayStoreResponse res = bootpayStore.product.delete(productId);
            if(res.isSuccess()) {
                System.out.println("product delete success: " + res.getDataAsMap());
            } else {
                System.out.println("product delete false: " + res.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

