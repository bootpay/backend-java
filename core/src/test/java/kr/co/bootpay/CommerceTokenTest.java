package kr.co.bootpay;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Commerce API - Token")
class CommerceTokenTest {

    private static BootpayStore store;

    @BeforeAll
    static void setUp() {
        store = TestConfig.createBootpayStore();
    }

    @Test
    @DisplayName("getAccessToken - Commerce 토큰 발급 성공")
    void testGetAccessToken() throws Exception {
        BootpayStoreResponse res = store.getAccessToken();
        assertNotNull(res, "응답이 null이면 안 됩니다");
        System.out.println("getAccessToken 응답: " + res);
    }

    @Test
    @DisplayName("withToken - 토큰 발급 후 체이닝 확인")
    void testWithToken() throws Exception {
        BootpayStore chainStore = TestConfig.createBootpayStore();
        BootpayStore result = chainStore.withToken();

        assertNotNull(result, "체이닝 결과가 null이면 안 됩니다");
        assertSame(chainStore, result, "withToken은 동일한 인스턴스를 반환해야 합니다");
        assertTrue(chainStore.hasToken(), "토큰이 설정되어 있어야 합니다");
        assertNotNull(chainStore.getCurrentToken(), "토큰 값이 null이면 안 됩니다");
        System.out.println("발급된 토큰: " + chainStore.getCurrentToken());
    }

    @Test
    @DisplayName("getAccessToken - 잘못된 키로 요청 시 처리")
    void testGetAccessTokenWithInvalidKey() throws Exception {
        TokenPayload invalidPayload = new TokenPayload("invalid_key", "invalid_secret");
        BootpayStore invalidStore = new BootpayStore(invalidPayload, TestConfig.getDevMode());

        BootpayStoreResponse res = invalidStore.getAccessToken();
        assertNotNull(res, "실패 응답도 null이면 안 됩니다");
        System.out.println("잘못된 키 응답: " + res);
    }

    @Test
    @DisplayName("Role 설정 메서드 체이닝 확인")
    void testRoleChaining() throws Exception {
        BootpayStore roleStore = TestConfig.createBootpayStoreWithToken();

        // 기본 role은 user
        assertEquals("user", roleStore.getCurrentRole());

        // 체이닝 테스트
        roleStore.asManager();
        assertEquals("manager", roleStore.getCurrentRole());

        roleStore.asPartner();
        assertEquals("partner", roleStore.getCurrentRole());

        roleStore.asSupervisor();
        assertEquals("supervisor", roleStore.getCurrentRole());

        roleStore.asVendor();
        assertEquals("vendor", roleStore.getCurrentRole());

        roleStore.clearRole();
        assertEquals("user", roleStore.getCurrentRole());

        System.out.println("Role 체이닝 테스트 통과");
    }
}
