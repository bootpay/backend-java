package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.UserListParams;

import java.util.HashMap;


public class User {

    static BootpayStore bootpayStore;
    public static void main(String[] args) {
        TokenPayload tokenPayload = new TokenPayload("4T4tlQq2xpPHioq216K-RQ", "szucYyZ9NtrmUtCu6gtUEm6aMOnhFQqCiSE9AK9I6fo=");
        bootpayStore = new BootpayStore(tokenPayload, "DEVELOPMENT");
 
        getToken();
//        joinIndividual();
//        joinCorporate();
//        authByUserStandbyId();
//        login();
//        list();
//        detail();
//        update();
        delete();
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


//     {login_id=ehowlsla19, auth_sms=false, metadata=null, gender=null, group_tags=[], count=0, birth=null, created_at=2025-06-12T14:16:02+09:00, auth_email=false, individual_extension=null, updated_at=2025-06-12T14:16:02+09:00, phone=01000000000, auth_phone=false, user_id=684a6292b0eacea5cd9745ef, name=홍길동, comment=null, http_status=200, email=ehowlsla@bootpay.co.kr, membership_type=1, status=1}
    public static void joinIndividual() {
        try {
            SUser user = new SUser();
            user.loginId = "ehowlsla21";
            user.loginPw = "km1178km";
            user.email = "ehowlsla@bootpay.co.kr";
            user.phone = "01000000000";
            user.name = "홍길동";

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
            String userId = "684fa4a6b0eacea5cd97464e";
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
            user.loginId = "ehowlsla27";
            user.loginPw = "km1178km";
            user.email = "ehowlsla@bootpay.co.kr";
            user.phone = "01000000000";
            user.name = "홍길동";
            user.userGroupId = "684a76feb0eacea5cd974603";

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
            HashMap<String, Object> res = bootpayStore.user.detail("684fa4a6b0eacea5cd97464e");
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
        user.userId = "684fa4a6b0eacea5cd97464e";
        user.loginId = "ehowlsla28";
        user.loginPw = "km1178km";
        user.email = "ehowlsla1@bootpay.co.kr";
        user.phone = "01000000001";
        user.name = "복떵";

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

    public static void delete() {
        try {
            HashMap<String, Object> res = bootpayStore.user.delete("67e64dab2739fbb9b5057cbf");
            if(res.get("error_code") == null) { //success
                System.out.println("destroy success: " + res);
            } else {
                System.out.println("destroy false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

