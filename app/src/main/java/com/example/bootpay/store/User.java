package com.example.bootpay.store;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.request.user.UserListParams;

/**
 * 사용자 관리 예제
 * <p>
 * 사용자 조회/수정/삭제 시 다음 식별자 중 하나를 사용할 수 있습니다:
 * - user_id: 부트페이 시스템 고유 ID (MongoDB ObjectId 형식, 예: "684fa4a6b0eacea5cd97464e")
 * - ex_uid: 가맹점에서 설정한 외부 고유 ID (예: "my_shop_user_12345")
 * - login_id: 로그인 ID (예: "ehowlsla19")
 * <p>
 * 서버에서 user_id → ex_uid → login_id 순서로 검색합니다.
 */
public class User {
    static BootpayStore bootpay;

    public static void main(String[] args) {
        try {
            TokenPayload tokenPayload = new TokenPayload("hxS-Up--5RvT6oU6QJE0JA", "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=");
            bootpay = new BootpayStore(tokenPayload, "DEVELOPMENT");

            getToken();

            // === 회원가입 ===
//            joinIndividual();
//            joinIndividualWithExternalUid(); // ex_uid를 활용한 가입

            // === 중복 체크 ===
//            emailExist();
//            idExist();
//            phoneExist();
//            uidExist(); // ex_uid 중복 체크
//            groupBusinessNumberExist();

            // === 조회 ===
//            list();
//            detail();                     // user_id로 조회
//            detailByExternalUid();        // ex_uid로 조회
//            detailByLoginId();            // login_id로 조회

            // === 수정 ===
//            update();                     // user_id로 수정
//            updateByExternalUid();        // ex_uid로 수정

            // === 삭제 ===
//            delete();                     // user_id로 삭제
            deleteByExternalUid();        // ex_uid로 삭제

            // === 기타 ===
//            userToken();
//            login();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getToken() {
        try {
            BootpayStoreResponse res = bootpay.getAccessToken();
            if (res.isSuccess()) {
                System.out.println("goGetToken success: " + res.getData());
            } else {
                System.out.println("goGetToken false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //     {login_id=ehowlsla19, auth_sms=false, metadata=null, gender=null, group_tags=[], count=0, birth=null, created_at=2025-06-12T14:16:02+09:00, auth_email=false, individual_extension=null, updated_at=2025-06-12T14:16:02+09:00, phone=01000000000, auth_phone=false, user_id=684a6292b0eacea5cd9745ef, name=홍길동, comment=null, http_status=200, email=ehowlsla@bootpay.co.kr, membership_type=1, status=1}
    public static void joinIndividual() {
        try {
            SUser user = new SUser();
            user.loginId = "ehowlsla22";
            user.loginPw = "km1178km";
            user.email = "ehowlsla@bootpay.co.kr";
            user.phone = "01000000000";
            user.name = "홍길동";

            BootpayStoreResponse res = bootpay.user.join(user);
            if (res.isSuccess()) {
                System.out.println("join success: " + res.getData());
            } else {
                System.out.println("join false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 외부 고유 ID(ex_uid)를 활용한 개인 회원 가입
     * <p>
     * ex_uid는 가맹점에서 관리하는 고유 식별자로, 이후 조회/수정/삭제 시 user_id 대신 사용 가능합니다.
     * 서버에서 id 조회 시 user_id → ex_uid → login_id 순서로 검색합니다.
     */
    public static void joinIndividualWithExternalUid() {
        try {
            SUser user = new SUser();
            user.loginId = "exuser003";
            user.loginPw = "password123";
            user.email = "external@example.com";
            user.phone = "01012345678";
            user.name = "외부회원";
            user.exUid = "my_shop_user_12345"; // 가맹점 고유 ID 설정

            BootpayStoreResponse res = bootpay.user.join(user);
            if (res.isSuccess()) {
                System.out.println("joinWithExternalUid success: " + res.getData());
                // 이제 다음 중 어떤 것으로도 조회/수정/삭제 가능:
                // - user_id (응답의 user_id 값)
                // - ex_uid ("my_shop_user_12345")
                // - login_id ("exuser001")
            } else {
                System.out.println("joinWithExternalUid false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void userToken() {
        try {
            String userId = "684fa4a6b0eacea5cd97464e";
            String corporateType = "individual"; // individual: 개인, corporate: 기업
            String membershipType = "guest"; // guest: 게스트, member: 회원
            BootpayStoreResponse res = bootpay.user.token(userId, corporateType, membershipType);
            if (res.isSuccess()) {
                System.out.println("token success: " + res.getData());
            } else {
                System.out.println("token false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void joinCorporate() {
        try {
            SUser user = new SUser();
            user.loginId = "ehowlsla29";
            user.loginPw = "km1178km";
            user.email = "ehowlsla@bootpay.co.kr";
            user.phone = "01000000000";
            user.name = "홍길동";
            user.userGroupId = "684a76feb0eacea5cd974603";

            BootpayStoreResponse res = bootpay.user.join(user);
            if (res.isSuccess()) {
                System.out.println("join success: " + res.getData());
            } else {
                System.out.println("join false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void authByUserStandbyId() {
//        try {
//            BootpayStoreResponse res = bootpay.user.authenticationData("67e0f47d03d0cb4e4117b082");
//            if (res.isSuccess()) {
//                System.out.println("authByUserStandbyId success: " + res.getData());
//            } else {
//                System.out.println("authByUserStandbyId false: " + res.getData());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void emailExist() {
        try {
            BootpayStoreResponse res = bootpay.withToken().user.checkExist("email-exist", "ehowlsla@bootpay.co.kr");
            if (res.isSuccess()) {
                System.out.println("emailExist success: " + res.getData());
            } else {
                System.out.println("emailExist false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void idExist() {
        try {
            BootpayStoreResponse res = bootpay.user.checkExist("id-exist", "ehowlsla2");
            if (res.isSuccess()) {
                System.out.println("idExist success: " + res.getData());
            } else {
                System.out.println("idExist false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void phoneExist() {
        try {
            BootpayStoreResponse res = bootpay.user.checkExist("phone-exist", "01000000000");
            if (res.isSuccess()) {
                System.out.println("phoneExist success: " + res.getData());
            } else {
                System.out.println("phoneExist false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 외부 고유 ID(ex_uid) 존재 여부 체크
     * <p>
     * 회원가입 전 ex_uid 중복 여부를 확인할 때 사용합니다.
     */
    public static void uidExist() {
        try {
            BootpayStoreResponse res = bootpay.user.checkExist("uid-exist", "my_shop_user_12345");
            if (res.isSuccess()) {
                System.out.println("uidExist success: " + res.getData());
                // { exists: true } 또는 { exists: false }
            } else {
                System.out.println("uidExist failed: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void groupBusinessNumberExist() {
        try {
            BootpayStoreResponse res = bootpay.user.checkExist("group-business-number-exist", "1088603663");
            if (res.isSuccess()) {
                System.out.println("groupBusinessNumberExist success: " + res.getData());
            } else {
                System.out.println("groupBusinessNumberExist false: " + res.getData());
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
            BootpayStoreResponse res = bootpay
                    .user.list(params);
            if (res.isSuccess()) {
                System.out.println("list success: " + res.getData());
            } else {
                System.out.println("list false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void detail() {
        try {
            BootpayStoreResponse res = bootpay.user.detail("684fa4a6b0eacea5cd97464e");
            if (res.isSuccess()) {
                System.out.println("detail success: " + res.getData());
            } else {
                System.out.println("detail false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 외부 고유 ID(ex_uid)로 고객 상세 조회
     * <p>
     * detail() 메서드에 user_id, ex_uid, login_id 중 아무거나 전달 가능합니다.
     */
    public static void detailByExternalUid() {
        try {
            // ex_uid로 조회 (회원가입 시 설정한 exUid 값)
            BootpayStoreResponse res = bootpay.user.detail("my_shop_user_12345");
            if (res.isSuccess()) {
                System.out.println("detailByExternalUid success: " + res.getData());
            } else {
                System.out.println("detailByExternalUid failed: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 로그인 ID(login_id)로 고객 상세 조회
     * <p>
     * detail() 메서드에 user_id, ex_uid, login_id 중 아무거나 전달 가능합니다.
     */
    public static void detailByLoginId() {
        try {
            // login_id로 조회
            BootpayStoreResponse res = bootpay.user.detail("exuser001");
            if (res.isSuccess()) {
                System.out.println("detailByLoginId success: " + res.getData());
            } else {
                System.out.println("detailByLoginId failed: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * user_id로 고객 정보 수정
     */
    public static void update() {
        SUser user = new SUser();
        user.userId = "68527d03b0eacea5cd974821"; // user_id 사용
        user.phone = "01040334671";
        user.name = "복떵2";

        try {
            BootpayStoreResponse res = bootpay.user.update(user);
            if (res.isSuccess()) {
                System.out.println("update success: " + res.getData());
            } else {
                System.out.println("update failed: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 외부 고유 ID(ex_uid)로 고객 정보 수정
     * <p>
     * user.userId에 user_id, ex_uid, login_id 중 아무거나 설정 가능합니다.
     */
    public static void updateByExternalUid() {
        SUser user = new SUser();
        user.userId = "my_shop_user_12345"; // ex_uid 사용
        user.phone = "01098765432";
        user.name = "수정된이름";

        try {
            BootpayStoreResponse res = bootpay.user.update(user);
            if (res.isSuccess()) {
                System.out.println("updateByExternalUid success: " + res.getData());
            } else {
                System.out.println("updateByExternalUid failed: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * user_id로 고객 삭제 (회원탈퇴)
     */
    public static void delete() {
        try {
            BootpayStoreResponse res = bootpay.user.delete("684fa4a6b0eacea5cd97464e");
            if (res.isSuccess()) {
                System.out.println("delete success: " + res.getData());
            } else {
                System.out.println("delete failed: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 외부 고유 ID(ex_uid)로 고객 삭제 (회원탈퇴)
     * <p>
     * delete() 메서드에 user_id, ex_uid, login_id 중 아무거나 전달 가능합니다.
     */
    public static void deleteByExternalUid() {
        try {
            // ex_uid로 삭제
            BootpayStoreResponse res = bootpay.user.delete("my_shop_user_12345");
            if (res.isSuccess()) {
                System.out.println("deleteByExternalUid success: " + res.getData());
            } else {
                System.out.println("deleteByExternalUid failed: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void login() {
        try {
            BootpayStoreResponse res = bootpay.user.login("ehowlsla21", "km1178km");
            if (res.isSuccess()) {
                System.out.println("login success: " + res.getData());
            } else {
                System.out.println("login false: " + res.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

