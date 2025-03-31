package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SUserGroup;

import java.util.HashMap;


public class UserGroup {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        bootpayStore = new BootpayStore("67c92fb8d01640bb9859c612", "ugaqkJ8/Yd2HHjM+W1TF6FZQPTmvx1rny5OIrMqcpTY=", "DEVELOPMENT");
        getToken();
//        list();
        detail();
//        update();
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

//    {count=1.0, http_status=200, list=[{user_group_id=67e2052b03d0cb4e4117b0af, seller_id=67c92fb8d01640bb9859c610, project_id=67c92fb8d01640bb9859c613, u_id=67e2052b03d0cb4e4117b0ac, corporate_type=2.0, bank=null, bank_code=null, count=0.0, last_updated_at=null, status=1.0, company_name=섹시다이나마이트, business_number=1088603663, registration_number=null, manager_name=홍길동, phone=null, email=null, business_type=null, business_category=null, corporate_extension=null, zipcode=12345, address=서울특별시 강남구 역삼동 123-45, address_detail=강남빌딩 1234호, pccc=null, auth_bank=false, auth_company=false, point=null, accumulation=null, marketing_accept_type=-1.0, marketing_accept_create_at=null, marketing_accept_update_at=null, use_subscription_aggregate_transaction=false, subscription_month_day=1.0, subscription_week_day=1.0, use_limit=false, purchase_limit=0.0, subscribed_limit=0.0, ceo_name=윤태섭}]}
    public static void list() {
        try {
            HashMap<String, Object> res = bootpayStore.userGroup.list(null, null, null);
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
            userGroup.companyName = "섹시다이나마이트2";
            userGroup.businessNumber = "1088603663";
            userGroup.managerName = "홍길동";
            userGroup.zipcode = "12345";
            userGroup.address = "서울특별시 강남구 역삼동 123-45";
            userGroup.addressDetail = "강남빌딩 1234호";
            userGroup.ceoName = "윤태섭";
            userGroup.corporateType = SUserGroup.CORPORATE_TYPE_CORPORATE;

            HashMap<String, Object> res = bootpayStore.userGroup.update(userGroup);
            if(res.get("error_code") == null) { //success
                System.out.println("update success: " + res);
            } else {
                System.out.println("update false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

