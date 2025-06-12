package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import kr.co.bootpay.store.model.request.TokenKey;
import kr.co.bootpay.store.model.request.UserListParams;

import java.util.HashMap;


public class User {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        TokenKey tokenKey = new TokenKey("JQxrL34gHuXXNsKg2X9I0g", "cws0oh0wcw5hK4t-FG7SfreniHxxftYapD9JNIVGfno=");
        bootpayStore = new BootpayStore(tokenKey, "DEVELOPMENT");

        System.out.println("start");
        getToken();
        joinIndividual();
//        joinCorporate();
//        authByUserStandbyId();
//        login();
//        list();
//        detail();
//        update();
//        withdraw();
//        userToken();
        // emailExist();
//        idExist();
//        phoneExist();
//        groupBusinessNumberExist();
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

//    {client_key=67c92fb8d01640bb9859c611, user_standby_id=67e0f47d03d0cb4e4117b082, http_status=200, status=2.0}
//     {client_key=67c92fb8d01640bb9859c611, user_standby_id=67e64daa2739fbb9b5057cbe, http_status=200, status=2.0}
    public static void joinIndividual() {
        try {
            SUser user = new SUser();
            user.loginId = "ehowlsla18";
            user.loginPw = "km1178km";
            user.email = "ehowlsla@bootpay.co.kr";
            user.phone = "01000000000";
            user.name = "홍길동";
            user.group = new SUserGroup();
            user.group.corporateType = SUserGroup.CORPORATE_TYPE_INDIVIDUAL;

            HashMap<String, Object> res = bootpayStore.user.join(user);
            if(res.get("error_code") == null) { //success
                System.out.println("join success: " + res);
            } else {
                System.out.println("join false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void userToken() {
        try {
            String userId = "67c9428f7b47af25bee631e7";
            HashMap<String, Object> res = bootpayStore.user.token(userId);
            if(res.get("error_code") == null) { //success
                System.out.println("token success: " + res);
            } else {
                System.out.println("token false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

                

    public static void joinCorporate() {
        try {
            SUser user = new SUser();
            user.loginId = "ehowlsla6";
            user.loginPw = "km1178km";
            user.email = "ehowlsla@bootpay.co.kr";
            user.phone = "01000000000";
            user.name = "홍길동";
            user.group = new SUserGroup();
            user.group.businessNumber = "1088603663";
            user.group.companyName = "섹시다이나마이트";
            user.group.ceoName = "홍길동";
            user.group.zipcode = "12345";
            user.group.address = "서울특별시 강남구 역삼동 123-45";
            user.group.addressDetail = "강남빌딩 1234호";
            user.group.phone = "01000000000";
            user.group.email = "help@sexydynamite.com";
            user.group.corporateType = SUserGroup.CORPORATE_TYPE_CORPORATE;

            HashMap<String, Object> res = bootpayStore.user.join(user);
            if(res.get("error_code") == null) { //success
                System.out.println("join success: " + res);
            } else {
                System.out.println("join false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void authByUserStandbyId() {
        try {
            HashMap<String, Object> res = bootpayStore.user.authenticationData("67e0f47d03d0cb4e4117b082");
            if(res.get("error_code") == null) { //success
                System.out.println("authByUserStandbyId success: " + res);
            } else {
                System.out.println("authByUserStandbyId false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    {http_status=200, token=eyJhbGciOiJSUzI1NiJ9.eyJ1dWlkIjpudWxsLCJpcCI6IjIyMy4xMzAuODIuNTgiLCJwdWJsaXNoZWRfYXQiOiIyMDI1LTAzLTI0VDE2OjEzOjUyKzA5OjAwIiwia2V5IjoiNjdlMTA2MzAwM2QwY2I0ZTQxMTdiMGE2IiwiZW5jcnlwdGVkX2tleSI6IjYwODY5NGQ0NjQxZDM1MTFkOTgxNGU3MmIyOTNiMTFkIn0.PU3kaYsvSwUfOg4d-PwgQGU8YEW11iP7L-UZMiG3GU5CdZm655nyiDaiXK0l0aNKG4J4jz6ZWsqfXev-l5jaEVb2z1RRMYN1yNAuoInWaC4aeCoKJlyzYlIyQj9w0Xl_p6hrFgFUKX__zwHO8XPTi_vhVpPubsRXPt52BVAu-LSLfNT746TvbIc5OJs--Udgp5pVGFGVmYHLFKcwZFtQtttNBBt4y6A5KPGksj7yDa0xZpqDrBBmP7mRr47W1--8NnConheia1AVs7ir7NBNOMXco8tZGAb5DwTKk6y2bUhMqvww4xEhZySq3yVcgsiEl0dVXNqeMQYRYQz3bJXS0Q}
    public static void login() {
        try {
            HashMap<String, Object> res = bootpayStore.user.login("ehowlsla5", "km1178km");
            if(res.get("error_code") == null) { //success
                System.out.println("login success: " + res);
            } else {
                System.out.println("login false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void emailExist() {
        try {
            HashMap<String, Object> res = bootpayStore.user.checkExist("email-exist", "ehowlsla@bootpay.co.kr");
            if(res.get("error_code") == null) { //success
                System.out.println("emailExist success: " + res);
            } else {
                System.out.println("emailExist false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void idExist() {
        try {
            HashMap<String, Object> res = bootpayStore.user.checkExist("id-exist", "ehowlsla2");
            if(res.get("error_code") == null) { //success
                System.out.println("idExist success: " + res);
            } else {
                System.out.println("idExist false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void phoneExist() {
        try {
            HashMap<String, Object> res = bootpayStore.user.checkExist("phone-exist", "01000000000");
            if(res.get("error_code") == null) { //success
                System.out.println("phoneExist success: " + res);
            } else {
                System.out.println("phoneExist false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void groupBusinessNumberExist() {
        try {
            HashMap<String, Object> res = bootpayStore.user.checkExist("group-business-number-exist", "1088603663");
            if(res.get("error_code") == null) { //success
                System.out.println("groupBusinessNumberExist success: " + res);
            } else {
                System.out.println("groupBusinessNumberExist false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    {count=5.0, http_status=200, list=[{login_id=ehowlsla5, name=홍길동, phone=null, email=null, membership_type=1.0, gender=null, birth=null, comment=null, group_tags=[], metadata=null, auth_sms=false, auth_phone=false, auth_email=false, count=0.0, status=1.0, individual_extension=null, updated_at=2025-03-24T16:13:52+09:00, created_at=2025-03-24T16:12:47+09:00, user_id=67e105ef03d0cb4e4117b0a1}, {login_id=ehowlsla4, name=홍길동, phone=null, email=null, membership_type=1.0, gender=null, birth=null, comment=null, group_tags=[], metadata=null, auth_sms=false, auth_phone=false, auth_email=false, count=0.0, status=1.0, individual_extension=null, updated_at=2025-03-24T16:09:07+09:00, created_at=2025-03-24T16:09:07+09:00, user_id=67e1051303d0cb4e4117b09b}, {login_id=ehowlsla3, name=홍길동, phone=null, email=null, membership_type=1.0, gender=null, birth=null, comment=null, group_tags=[], metadata=null, auth_sms=false, auth_phone=false, auth_email=false, count=0.0, status=1.0, individual_extension=null, updated_at=2025-03-24T14:58:21+09:00, created_at=2025-03-24T14:58:21+09:00, user_id=67e0f47d03d0cb4e4117b083}, {login_id=ehowlsla2, name=홍길동, phone=null, email=null, membership_type=1.0, gender=null, birth=null, comment=null, group_tags=[], metadata=null, auth_sms=false, auth_phone=false, auth_email=false, count=0.0, status=1.0, individual_extension=null, updated_at=2025-03-24T14:35:26+09:00, created_at=2025-03-24T14:35:26+09:00, user_id=67e0ef1e03d0cb4e4117b07d}, {login_id=ehowlsla, name=윤태섭, phone=null, email=null, membership_type=1.0, gender=null, birth=null, comment=null, group_tags=[], metadata=null, auth_sms=false, auth_phone=false, auth_email=false, count=0.0, status=1.0, individual_extension=null, updated_at=2025-03-07T13:03:37+09:00, created_at=2025-03-06T15:37:03+09:00, user_id=67c9428f7b47af25bee631e7}]}
    public static void list() {
        UserListParams params = new UserListParams();
        params.type = "user";
        params.keyword = "홍길동"; //type에 따라 지원하는 검색어가 다르다

        try {
            HashMap<String, Object> res = bootpayStore.user.list(params);
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
            HashMap<String, Object> res = bootpayStore.user.detail("67e0f47d03d0cb4e4117b083");
            if(res.get("error_code") == null) { //success
                System.out.println("detail success: " + res);
            } else {
                System.out.println("detail false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void

    public static void update() {
        SUser user = new SUser();
        user.userId = "67e105ef03d0cb4e4117b0a1";
        user.name = "홍상순2";

        try {
            HashMap<String, Object> res = bootpayStore.user.update(user);
            if(res.get("error_code") == null) { //success
                System.out.println("update success: " + res);
            } else {
                System.out.println("update false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void withdraw() {
        try {
            HashMap<String, Object> res = bootpayStore.user.withdraw("67e64dab2739fbb9b5057cbf");
            if(res.get("error_code") == null) { //success
                System.out.println("withdraw success: " + res);
            } else {
                System.out.println("withdraw false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

