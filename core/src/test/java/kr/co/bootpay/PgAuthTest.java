package kr.co.bootpay;

import kr.co.bootpay.pg.Bootpay;
import kr.co.bootpay.pg.model.request.Authentication;
import kr.co.bootpay.pg.model.request.UserToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PG API - Authentication (본인인증, 사용자 토큰)")
class PgAuthTest {

    private static Bootpay bootpay;

    @BeforeAll
    static void setUp() throws Exception {
        bootpay = TestConfig.createBootpayWithToken();
    }

    // ── 본인인증 요청 ─────────────────────────────────────────

    @Test
    @DisplayName("requestAuthentication - 본인인증 요청")
    void testRequestAuthentication() throws Exception {
        Authentication auth = new Authentication();
        auth.pg = "danal";
        auth.method = "sms";
        auth.orderName = "테스트 본인인증";
        auth.authenticationId = "test_auth_" + System.currentTimeMillis();
        auth.username = "홍길동";
        auth.identityNo = "9001011";
        auth.carrier = "SKT";
        auth.phone = "01012345678";
        auth.clientIp = "127.0.0.1";
        auth.authenticateType = "sms";

        HashMap<String, Object> res = bootpay.requestAuthentication(auth);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("requestAuthentication 응답: " + res);
    }

    // ── 본인인증 확인 ─────────────────────────────────────────

    @Test
    @DisplayName("confirmAuthentication - 본인인증 확인 (OTP)")
    void testConfirmAuthentication() throws Exception {
        String receiptId = "test_receipt_id_0001";
        String otp = "123456";
        HashMap<String, Object> res = bootpay.confirmAuthentication(receiptId, otp);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("confirmAuthentication 응답: " + res);
    }

    // ── 본인인증 재알림 ───────────────────────────────────────

    @Test
    @DisplayName("realarmAuthentication - 본인인증 재알림")
    void testRealarmAuthentication() throws Exception {
        String receiptId = "test_receipt_id_0001";
        HashMap<String, Object> res = bootpay.realarmAuthentication(receiptId);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("realarmAuthentication 응답: " + res);
    }

    // ── 사용자 토큰 발급 ──────────────────────────────────────

    @Test
    @DisplayName("getUserToken - 사용자 토큰 발급")
    void testGetUserToken() throws Exception {
        UserToken userToken = new UserToken();
        userToken.userId = "test_user_001";
        userToken.email = "test@example.com";
        userToken.username = "테스트유저";
        userToken.gender = 1;
        userToken.birth = "900101";
        userToken.phone = "01012345678";

        HashMap<String, Object> res = bootpay.getUserToken(userToken);
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("getUserToken 응답: " + res);
    }
}
