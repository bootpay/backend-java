package kr.co.bootpay;

import kr.co.bootpay.common.BootpayMode;
import kr.co.bootpay.common.BootpayResponse;
import kr.co.bootpay.common.BootpayRole;
import kr.co.bootpay.pg.Bootpay;
import kr.co.bootpay.store.BootpayCommerce;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 3.3.0 통일 표면(빌더 / BootpayMode / BootpayRole / BootpayResponse) 검증과,
 * 그 추가가 기존 표면에 아무 영향도 주지 않았음을 확인하는 회귀 검증.
 */
@DisplayName("통일 표면 (3.3.0)")
class UnifiedSurfaceTest {

    @Nested
    @DisplayName("Bootpay.builder()")
    class PgBuilder {

        @Test
        @DisplayName("client_key/secret_key 로 생성하면 Basic Auth 자격을 갖춘다")
        void buildsWithClientKey() {
            Bootpay bootpay = Bootpay.builder()
                    .clientKey("ck")
                    .secretKey("sk")
                    .mode(BootpayMode.PRODUCTION)
                    .build();

            assertEquals("ck", bootpay.client_key);
            assertEquals("sk", bootpay.secret_key);
            assertNull(bootpay.application_id);
            assertEquals(bootpay.PRODUCTION, bootpay.baseUrl);
            assertTrue(bootpay.hasAuth());
        }

        @Test
        @DisplayName("application_id/private_key 로도 같은 형태로 생성된다")
        void buildsWithLegacyKeys() {
            Bootpay bootpay = Bootpay.builder()
                    .applicationId("app")
                    .privateKey("pk")
                    .mode(BootpayMode.DEVELOPMENT)
                    .build();

            assertEquals("app", bootpay.application_id);
            assertEquals("pk", bootpay.private_key);
            assertNull(bootpay.client_key);
            assertEquals(bootpay.DEVELOPMENT, bootpay.baseUrl);
        }

        @Test
        @DisplayName("mode 미지정 시 production 이어야 한다")
        void defaultsToProduction() {
            Bootpay bootpay = Bootpay.builder().clientKey("ck").secretKey("sk").build();
            assertEquals(bootpay.PRODUCTION, bootpay.baseUrl);
        }

        @Test
        @DisplayName("키가 짝을 이루지 않으면 build() 에서 즉시 알려야 한다")
        void rejectsIncompleteCredentials() {
            assertThrows(IllegalStateException.class,
                    () -> Bootpay.builder().clientKey("ck").build());
            assertThrows(IllegalStateException.class,
                    () -> Bootpay.builder().secretKey("sk").build());
            assertThrows(IllegalStateException.class,
                    () -> Bootpay.builder().applicationId("app").build());
            assertThrows(IllegalStateException.class,
                    () -> Bootpay.builder().privateKey("pk").build());
            assertThrows(IllegalStateException.class,
                    () -> Bootpay.builder().build());
        }

        @Test
        @DisplayName("모듈은 항상 준비되어 있어야 한다")
        void modulesAreInitialized() {
            Bootpay bootpay = Bootpay.builder().clientKey("ck").secretKey("sk").build();

            assertNotNull(bootpay.payment);
            assertNotNull(bootpay.billing);
            assertNotNull(bootpay.auth);
            assertNotNull(bootpay.cash);
            assertNotNull(bootpay.escrow);
            assertNotNull(bootpay.user);
        }
    }

    @Nested
    @DisplayName("BootpayCommerce.builder()")
    class CommerceBuilder {

        @Test
        @DisplayName("PG 와 같은 형태로 생성되고 모듈이 준비된다")
        void buildsWithSameShape() {
            BootpayCommerce bootpay = BootpayCommerce.builder()
                    .clientKey("ck")
                    .secretKey("sk")
                    .mode(BootpayMode.STAGE)
                    .role(BootpayRole.MANAGER)
                    .build();

            assertEquals(bootpay.unwrap().STAGE, bootpay.unwrap().baseUrl);
            assertEquals(BootpayRole.MANAGER, bootpay.role());
            assertFalse(bootpay.hasToken());

            assertNotNull(bootpay.user);
            assertNotNull(bootpay.order);
            assertNotNull(bootpay.orderSubscription);
            assertNotNull(bootpay.orderSubscription.requestIng);
            assertNotNull(bootpay.subscriptionSetting);
        }

        @Test
        @DisplayName("기본값은 production + user role 이어야 한다")
        void defaultsToProductionAndUser() {
            BootpayCommerce bootpay = BootpayCommerce.builder().clientKey("ck").secretKey("sk").build();

            assertEquals(bootpay.unwrap().PRODUCTION, bootpay.unwrap().baseUrl);
            assertEquals(BootpayRole.USER, bootpay.role());
        }

        @Test
        @DisplayName("키가 비어 있으면 build() 에서 즉시 알려야 한다")
        void rejectsMissingCredentials() {
            assertThrows(IllegalStateException.class,
                    () -> BootpayCommerce.builder().secretKey("sk").build());
            assertThrows(IllegalStateException.class,
                    () -> BootpayCommerce.builder().clientKey("ck").build());
        }
    }

    @Nested
    @DisplayName("BootpayMode / BootpayRole")
    class Enums {

        @Test
        @DisplayName("문자열 파싱은 대소문자를 가리지 않고, 인식 불가 시 기본값으로 떨어진다")
        void parsesLeniently() {
            assertEquals(BootpayMode.DEVELOPMENT, BootpayMode.of("development"));
            assertEquals(BootpayMode.STAGE, BootpayMode.of("  StAgE "));
            assertEquals(BootpayMode.PRODUCTION, BootpayMode.of("오타"));
            assertEquals(BootpayMode.PRODUCTION, BootpayMode.of(null));

            assertEquals(BootpayRole.SUPERVISOR, BootpayRole.of("SUPERVISOR"));
            assertEquals(BootpayRole.USER, BootpayRole.of("없는역할"));
            assertEquals(BootpayRole.USER, BootpayRole.of(null));
        }

        @Test
        @DisplayName("환경 문자열을 잘못 적어도 baseUrl 이 비지 않아야 한다")
        void badModeStringNeverLeavesBaseUrlNull() {
            Bootpay pg = Bootpay.builder().clientKey("ck").secretKey("sk").mode("오타").build();
            assertEquals(pg.PRODUCTION, pg.baseUrl);

            BootpayCommerce commerce = BootpayCommerce.builder()
                    .clientKey("ck").secretKey("sk").mode("오타").build();
            assertEquals(commerce.unwrap().PRODUCTION, commerce.unwrap().baseUrl);
        }
    }

    @Nested
    @DisplayName("BootpayResponse")
    class Response {

        @Test
        @DisplayName("PG 성공 응답은 http_status 를 본문에서 제외하고 성공으로 판정한다")
        void mapsPgSuccess() {
            HashMap<String, Object> raw = new HashMap<>();
            raw.put("receipt_id", "r1");
            raw.put("status", 1);
            raw.put("http_status", 200);

            BootpayResponse res = BootpayResponse.ofPg(raw);

            assertTrue(res.isSuccess());
            assertFalse(res.isFailed());
            assertEquals("r1", res.getString("receipt_id"));
            assertFalse(res.getData().containsKey("http_status"));
            assertEquals(200, res.asMap().get("http_status"));
            assertNull(res.getErrorCode());
        }

        @Test
        @DisplayName("PG 실패 응답은 error_code 와 message 를 노출한다")
        void mapsPgFailure() {
            HashMap<String, Object> raw = new HashMap<>();
            raw.put("error_code", -401);
            raw.put("message", "인증 실패");
            raw.put("http_status", 401);

            BootpayResponse res = BootpayResponse.ofPg(raw);

            assertFalse(res.isSuccess());
            assertTrue(res.isFailed());
            assertEquals(Integer.valueOf(-401), res.getErrorCode());
            assertEquals("인증 실패", res.getMessage());
        }

        @Test
        @DisplayName("본문이 없어도 getData() 는 null 이 아니어야 한다")
        void neverReturnsNullData() {
            BootpayResponse res = BootpayResponse.ofPg(null);

            assertFalse(res.isSuccess());
            assertNotNull(res.getData());
            assertTrue(res.getData().isEmpty());
            assertNotNull(res.asMap());
        }
    }

    @Nested
    @DisplayName("기존 표면 회귀 — 아무것도 바뀌지 않아야 한다")
    class NoSideEffects {

        @Test
        @DisplayName("기존 Bootpay 생성자와 static factory 는 그대로 동작한다")
        void legacyPgConstructionUnchanged() {
            Bootpay empty = new Bootpay();
            assertNull(empty.application_id);
            assertNull(empty.client_key);
            assertNull(empty.baseUrl);

            Bootpay legacy = new Bootpay("app", "pk");
            assertEquals("app", legacy.application_id);
            assertEquals("pk", legacy.private_key);
            assertEquals(legacy.PRODUCTION, legacy.baseUrl);

            Bootpay withKey = Bootpay.withClientKey("ck", "sk");
            assertEquals("ck", withKey.client_key);
            assertEquals(withKey.PRODUCTION, withKey.baseUrl);
        }

        @Test
        @DisplayName("모듈 필드 추가가 BootpayObject 의 token 필드를 가리지 않아야 한다")
        void moduleFieldsDoNotShadowExistingFields() {
            Bootpay bootpay = new Bootpay("app", "pk");
            bootpay.setToken("legacy_token");

            // token 은 여전히 BootpayObject 의 String 필드여야 한다 (모듈 이름과 충돌 없음)
            String token = bootpay.token;
            assertEquals("legacy_token", token);
            assertEquals("Bearer legacy_token", bootpay.getTokenValue());
        }

        @Test
        @DisplayName("기존 BootpayStore 생성자와 모듈은 그대로 동작한다")
        void legacyCommerceConstructionUnchanged() {
            BootpayStore store = new BootpayStore(new TokenPayload("ck", "sk"));

            assertEquals(store.PRODUCTION, store.baseUrl);
            assertEquals("user", store.getRole());
            assertNull(store.getToken());
            assertFalse(store.hasToken());

            assertNotNull(store.user);
            assertNotNull(store.order);
            assertNotNull(store.orderSubscription);
            assertNotNull(store.orderSubscription.requestIng);

            assertSame(store, store.asManager());
            assertEquals("manager", store.getCurrentRole());
        }

        @Test
        @DisplayName("BootpayCommerce 는 BootpayStore 를 상속하지 않는다 (타입 계층 불변)")
        void commerceDoesNotExtendStore() {
            BootpayCommerce commerce = BootpayCommerce.builder()
                    .clientKey("ck").secretKey("sk").build();

            assertFalse(BootpayStore.class.isAssignableFrom(BootpayCommerce.class),
                    "위임 구조여야 기존 BootpayStore 의 타입 계층에 영향이 없다");
            assertNotNull(commerce.unwrap());
        }
    }
}
