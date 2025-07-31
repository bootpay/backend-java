package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.userGroup.UserGroupAggregateTransactionParams;
import kr.co.bootpay.store.model.request.userGroup.UserGroupLimitParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.userGroup.UserGroupListParams;


public class UserGroup {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("4T4tlQq2xpPHioq216K-RQ", "szucYyZ9NtrmUtCu6gtUEm6aMOnhFQqCiSE9AK9I6fo=");
            bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
            getToken();
//        list();
//        detail();
//        update();
//        create();
//        userCreate();
//        userDelete();
//            updateLimit();
            updateAggregateTransaction();
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

    public static void create() {
        try {
            SUserGroup userGroup = new SUserGroup();
            userGroup.companyName = "테스트 법인";
            userGroup.businessNumber = "1234567890";
            userGroup.address = "서울시 강남구";
            userGroup.phone = "02-1234-5678";
            userGroup.email = "test@example.com";

            BootpayStoreResponse res = bootpayStore.userGroup.create(userGroup);
            if(res.isSuccess()) {
                System.out.println("userGroup create success: " + res.getData());
            } else {
                System.out.println("userGroup create false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void list() {
        try {
            UserGroupListParams params = new UserGroupListParams();
//            params.keyword = "테스트";

            BootpayStoreResponse res = bootpayStore.userGroup.list(params);
            if(res.isSuccess()) {
                System.out.println("userGroup list success: " + res.getData());
            } else {
                System.out.println("userGroup list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detail() {
        try {
            BootpayStoreResponse res = bootpayStore.userGroup.detail("6850d49cb0eacea5cd97470a");
            if(res.isSuccess()) {
                System.out.println("userGroup detail success: " + res.getData());
            } else {
                System.out.println("userGroup detail false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateLimit() {
        try {
            UserGroupLimitParams params = new UserGroupLimitParams();
            params.userGroupId = "6850d49cb0eacea5cd97470a";
            params.useLimit = true;
            params.purchaseLimit = 3000000.0;
//            params.

            BootpayStoreResponse res = bootpayStore.asManager().userGroup.limit(params);
            if(res.isSuccess()) {
                System.out.println("userGroup update success: " + res.getData());
            } else {
                System.out.println("userGroup update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateAggregateTransaction() {
        try {
            UserGroupAggregateTransactionParams params = new UserGroupAggregateTransactionParams();
            params.userGroupId = "6850d49cb0eacea5cd97470a";
            params.useSubscriptionAggregateTransaction = true;
            params.subscriptionMonthDay = 3;
            params.subscriptionWeekDay = 3;


            BootpayStoreResponse res = bootpayStore.asManager().userGroup.aggregateTransaction(params);
            if(res.isSuccess()) {
                System.out.println("userGroup update success: " + res.getData());
            } else {
                System.out.println("userGroup update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        try {
            SUserGroup userGroup = new SUserGroup();
            userGroup.userGroupId = "67e2052b03d0cb4e4117b0af";
            userGroup.companyName = "수정된 법인명";
            userGroup.businessNumber = "0987654321";
            userGroup.address = "서울시 서초구";
            userGroup.phone = "02-8765-4321";
            userGroup.email = "updated@example.com";

            BootpayStoreResponse res = bootpayStore.userGroup.update(userGroup);
            if(res.isSuccess()) {
                System.out.println("userGroup update success: " + res.getData());
            } else {
                System.out.println("userGroup update false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void userCreate() {
        try {
            String userGroupId = "68527f9cb0eacea5cd974835";
            String userId = "68527d03b0eacea5cd974821";

            BootpayStoreResponse res = bootpayStore.asManager().userGroup.userCreate(userGroupId, userId);
            if(res.isSuccess()) {
                System.out.println("userGroup userCreate success: " + res.getData());
            } else {
                System.out.println("userGroup userCreate false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void userDelete() {
        try {
            String userGroupId = "68527f9cb0eacea5cd974835";
            String userId = "68527d03b0eacea5cd974821";

            BootpayStoreResponse res = bootpayStore.asManager().userGroup.userDelete(userGroupId, userId);
            if(res.isSuccess()) {
                System.out.println("userGroup userDelete success: " + res.getData());
            } else {
                System.out.println("userGroup userDelete false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

