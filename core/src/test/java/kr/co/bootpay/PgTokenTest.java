package kr.co.bootpay;

import kr.co.bootpay.pg.Bootpay;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PG API - Token")
class PgTokenTest {

    private static Bootpay bootpay;

    @BeforeAll
    static void setUp() {
        bootpay = TestConfig.createBootpay();
    }

    @Test
    @DisplayName("getAccessToken - 토큰 발급 성공")
    void testGetAccessToken() throws Exception {
        HashMap<String, Object> res = bootpay.getAccessToken();
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("getAccessToken 응답: " + res);

        // 토큰이 설정되었는지 확인
        assertNotNull(bootpay.token, "토큰이 설정되어야 합니다");
        assertFalse(bootpay.token.isEmpty(), "토큰이 비어있으면 안 됩니다");
    }

    @Test
    @DisplayName("getAccessToken - 잘못된 키로 요청 시 실패 처리")
    void testGetAccessTokenWithInvalidKey() throws Exception {
        Bootpay invalidBootpay = new Bootpay("invalid_app_id", "invalid_private_key", TestConfig.getDevMode());
        HashMap<String, Object> res = invalidBootpay.getAccessToken();
        assertNotNull(res, "실패 응답도 null이면 안 됩니다");
        System.out.println("잘못된 키 응답: " + res);
    }
}
