package kr.co.bootpay.pg;

import kr.co.bootpay.TestConfig;

import kr.co.bootpay.pg.Bootpay;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PG API - Token")
class PgTokenTest {

    @Test
    @DisplayName("getAccessToken (ck/sk) - Option A no-op 합성 응답")
    void testGetAccessTokenCkSk() throws Exception {
        // ck/sk 모드는 매 요청 Basic Auth 로 인증하므로 getAccessToken 은 HTTP 호출 없이 합성 응답을 돌려준다.
        // BOOTPAY_AUTH_MODE 와 무관하게 직접 ck/sk 인스턴스를 만들어 검증한다.
        Bootpay bootpay = Bootpay.withClientKey(TestConfig.getPgClientKey(), TestConfig.getPgSecretKey(), TestConfig.getDevMode());
        HashMap<String, Object> res = bootpay.getAccessToken();
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("ck/sk getAccessToken 응답: " + res);

        assertEquals("", res.get("access_token"), "ck/sk 합성 access_token 은 빈 문자열이어야 합니다");
        Object expireIn = res.get("expire_in");
        assertTrue(expireIn instanceof Number && ((Number) expireIn).intValue() == 0,
                "ck/sk 합성 expire_in 은 0 이어야 합니다");
        assertNull(bootpay.token, "ck/sk 모드에서 internal token 은 비어 있어야 합니다");
    }

    @Test
    @DisplayName("getAccessToken (legacy) - 실제 access_token 발급")
    void testGetAccessTokenLegacy() throws Exception {
        // TestConfig 팩토리를 우회해 실서버로 직행 — base URL 오버라이드가 적용되지 않으므로 development 전용
        TestConfig.assumeDirectLiveAllowed();
        // legacy application_id/private_key 모드는 request/token 을 호출하여 실제 토큰을 발급한다.
        Bootpay legacy = new Bootpay(TestConfig.getPgAppId(), TestConfig.getPgPrivateKey(), TestConfig.getDevMode());
        HashMap<String, Object> res = legacy.getAccessToken();
        assertNotNull(res, "legacy 응답이 null이면 안 됩니다");
        System.out.println("legacy getAccessToken 응답: " + res);

        Object accessToken = res.get("access_token");
        assertTrue(accessToken instanceof String, "access_token 은 String 타입이어야 합니다");
        assertFalse(((String) accessToken).isEmpty(), "legacy access_token 은 비어 있으면 안 됩니다");
        assertEquals(accessToken, legacy.token, "internal token 이 발급된 access_token 과 일치해야 합니다");

        Object expireIn = res.get("expire_in");
        assertTrue(expireIn instanceof Number && ((Number) expireIn).intValue() > 0,
                "legacy expire_in 은 양수여야 합니다");
    }

    @Test
    @DisplayName("getAccessToken - 잘못된 키로 요청 시 실패 처리")
    void testGetAccessTokenWithInvalidKey() throws Exception {
        Bootpay invalidBootpay = Bootpay.withClientKey("invalid_client_key", "invalid_secret_key", TestConfig.getDevMode());
        HashMap<String, Object> res = invalidBootpay.getAccessToken();
        assertNotNull(res, "실패 응답도 null이면 안 됩니다");
        System.out.println("잘못된 키 응답: " + res);
    }
}
