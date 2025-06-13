package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.UserGroupListParams;

import java.util.HashMap;


public class UserGroup {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        TokenPayload tokenPayload = new TokenPayload("4T4tlQq2xpPHioq216K-RQ", "szucYyZ9NtrmUtCu6gtUEm6aMOnhFQqCiSE9AK9I6fo=");
        bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
        getToken();
//        list();
//        detail();
//        update();
//        create();
//        addUser();
        removeUser();
    }

    public static void getToken() {
        try {
            HashMap<String, Object> res = bootpayStore.getAccessToken();
            if(res.get("error_code") == null) { //success
                System.out.println("goGetToken success: " + res);
            } else {
                System.out.println("goGetToken false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void create() {
        try {
            SUserGroup userGroup = new SUserGroup();
            userGroup.companyName = "섹시다이나마이트3";
            userGroup.businessNumber = "1088603664";
            userGroup.managerName = "홍길동";
            userGroup.zipcode = "12345";
            userGroup.address = "서울특별시 강남구 역삼동 123-45";
            userGroup.addressDetail = "강남빌딩 1234호";
            userGroup.ceoName = "홍길동";
            userGroup.corporateType = SUserGroup.CORPORATE_TYPE_CORPORATE;

            HashMap<String, Object> res = bootpayStore.userGroup.create(userGroup);
            if(res.get("error_code") == null) { //success
                System.out.println("update success: " + res);
            } else {
                System.out.println("update false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    {count=1, http_status=200, list=[{user_group_id=67e2052b03d0cb4e4117b0af, seller_id=67c92fb8d01640bb9859c610, project_id=67c92fb8d01640bb9859c613, u_id=67e2052b03d0cb4e4117b0ac, corporate_type=2, bank=, bank_code=, count=0, last_updated_at=null, status=1, company_name=섹시다이나마이트3, business_number=1088603663, registration_number=, manager_name=홍길동, phone=, email=, business_type=, business_category=null, corporate_extension=null, zipcode=12345, address=서울특별시 강남구 역삼동 123-45, address_detail=강남빌딩 1234호, pccc=, auth_bank=false, auth_company=false, point=null, accumulation=null, marketing_accept_type=-1, marketing_accept_create_at=null, marketing_accept_update_at=null, use_subscription_aggregate_transaction=null, subscription_month_day=null, subscription_week_day=null, use_limit=false, purchase_limit=, subscribed_limit=, ceo_name=윤태섭}]}
    public static void list() {
        try {
            UserGroupListParams params = new UserGroupListParams();
            // S(prefix) + Model명
            params.corporateType = SUserGroup.CORPORATE_TYPE_CORPORATE;
//            params

            HashMap<String, Object> res = bootpayStore.userGroup.list(params);
            if(res.get("error_code") == null) { //success
                System.out.println("list success: " + res);
            } else {
                System.out.println("list false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detail() {
        try {
            HashMap<String, Object> res = bootpayStore.userGroup.detail("67e2052b03d0cb4e4117b0af");
            if(res.get("error_code") == null) { //success
                System.out.println("detail success: " + res);
            } else {
                System.out.println("detail false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public
    public static void update() {
        try {
            SUserGroup userGroup = new SUserGroup();
            userGroup.userGroupId = "67e2052b03d0cb4e4117b0af";
            userGroup.companyName = "섹시다이나마이트3";
            userGroup.businessNumber = "1088603663";
            userGroup.managerName = "홍길동";
            userGroup.zipcode = "12345";
            userGroup.address = "서울특별시 강남구 역삼동 123-45";
            userGroup.addressDetail = "강남빌딩 1234호";
            userGroup.ceoName = "홍길동";
            userGroup.corporateType = SUserGroup.CORPORATE_TYPE_CORPORATE;

            HashMap<String, Object> res = bootpayStore.userGroup.update(userGroup);
            if(res.get("error_code") == null) { //success
                System.out.println("create success: " + res);
            } else {
                System.out.println("create false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUser() {
        String userGroupId = "684a76feb0eacea5cd974603";
        String userId = "684a6292b0eacea5cd9745ef";

        try {
            HashMap<String, Object> res = bootpayStore.userGroup.addUser(userGroupId, userId);
            if(res.get("error_code") == null) { //success
                System.out.println("addUser success: " + res);
            } else {
                System.out.println("addUser false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeUser() {
        String userGroupId = "684a76feb0eacea5cd974603";
        String userId = "684a6292b0eacea5cd9745ef";

        try {
            HashMap<String, Object> res = bootpayStore.userGroup.removeUser(userGroupId, userId);
            if(res.get("error_code") == null) { //success
                System.out.println("addUser success: " + res);
            } else {
                System.out.println("addUser false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

