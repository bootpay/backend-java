package kr.co.bootpay;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SUser;
import kr.co.bootpay.store.model.request.user.UserListParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Commerce API - User")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommerceUserTest {

    private static BootpayStore store;
    private static String createdUserId;

    @BeforeAll
    static void setUp() throws Exception {
        store = TestConfig.createBootpayStoreWithToken();
    }

    // ── 사용자 토큰 발급 ──────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("user.token - 사용자 토큰 발급")
    void testUserToken() throws Exception {
        BootpayStoreResponse res = store.user.token("test_user_001");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("user.token 응답: " + res);
    }

    @Test
    @Order(2)
    @DisplayName("user.token - 회원 유형 지정 토큰 발급")
    void testUserTokenWithType() throws Exception {
        BootpayStoreResponse res = store.user.token("test_user_001", "individual", "member");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("user.token (유형지정) 응답: " + res);
    }

    // ── 회원가입 ──────────────────────────────────────────────

    @Test
    @Order(3)
    @DisplayName("user.join - 회원가입")
    void testUserJoin() throws Exception {
        SUser user = new SUser();
        user.loginId = "test_java_user_" + System.currentTimeMillis();
        user.loginPw = "test1234!";
        user.name = "자바테스트유저";
        user.email = "java_test@example.com";
        user.phone = "01099998888";
        user.gender = 1;
        user.birth = "1990-01-01";
        user.externalUid = "java_ext_" + System.currentTimeMillis();

        BootpayStoreResponse res = store.user.join(user);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("user.join 응답: " + res);

        // 생성된 userId 저장 (후속 테스트용)
        if (res.isSuccess() && res.getData() != null && res.getData().containsKey("user_id")) {
            createdUserId = (String) res.getData().get("user_id");
            System.out.println("생성된 user_id: " + createdUserId);
        }
    }

    // ── 중복 체크 ─────────────────────────────────────────────

    @Test
    @Order(4)
    @DisplayName("user.checkExist - ID 중복 체크")
    void testCheckExist() throws Exception {
        BootpayStoreResponse res = store.user.checkExist("id-exist", "test_java_user_check");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("user.checkExist 응답: " + res);
    }

    @Test
    @Order(5)
    @DisplayName("user.checkExist - 이메일 중복 체크")
    void testCheckExistEmail() throws Exception {
        BootpayStoreResponse res = store.user.checkExist("email-exist", "java_test@example.com");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("user.checkExist (email) 응답: " + res);
    }

    // ── 로그인 ────────────────────────────────────────────────

    @Test
    @Order(6)
    @DisplayName("user.login - 로그인")
    void testLogin() throws Exception {
        BootpayStoreResponse res = store.user.login("test_login_id", "test1234!");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("user.login 응답: " + res);
    }

    // ── 사용자 목록 조회 ──────────────────────────────────────

    @Test
    @Order(7)
    @DisplayName("user.list - 사용자 목록 조회")
    void testUserList() throws Exception {
        UserListParams params = new UserListParams();
        params.page = 1;
        params.limit = 10;

        BootpayStoreResponse res = store.user.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("user.list 응답: " + res);
    }

    @Test
    @Order(8)
    @DisplayName("user.list - 키워드 검색")
    void testUserListWithKeyword() throws Exception {
        UserListParams params = new UserListParams();
        params.page = 1;
        params.limit = 10;
        params.keyword = "test";

        BootpayStoreResponse res = store.user.list(params);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("user.list (keyword) 응답: " + res);
    }

    // ── 사용자 상세 조회 ──────────────────────────────────────

    @Test
    @Order(9)
    @DisplayName("user.detail - 사용자 상세 조회")
    void testUserDetail() throws Exception {
        String userId = (createdUserId != null) ? createdUserId : "test_user_001";
        BootpayStoreResponse res = store.user.detail(userId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("user.detail 응답: " + res);
    }

    // ── 사용자 정보 수정 ──────────────────────────────────────

    @Test
    @Order(10)
    @DisplayName("user.update - 사용자 정보 수정")
    void testUserUpdate() throws Exception {
        String userId = (createdUserId != null) ? createdUserId : "test_user_001";

        SUser user = new SUser();
        user.userId = userId;
        user.name = "수정된이름";
        user.email = "updated@example.com";

        BootpayStoreResponse res = store.user.update(user);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("user.update 응답: " + res);
    }

    // ── 본인인증 데이터 조회 ──────────────────────────────────

    @Test
    @Order(11)
    @DisplayName("user.authenticationData - 본인인증 데이터 조회")
    void testAuthenticationData() throws Exception {
        BootpayStoreResponse res = store.user.authenticationData("test_stand_id");
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("user.authenticationData 응답: " + res);
    }

    // ── 회원 탈퇴 ─────────────────────────────────────────────

    @Test
    @Order(12)
    @DisplayName("user.delete - 회원 탈퇴")
    void testUserDelete() throws Exception {
        String userId = (createdUserId != null) ? createdUserId : "test_user_to_delete";
        BootpayStoreResponse res = store.user.delete(userId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("user.delete 응답: " + res);
    }
}
