package kr.co.bootpay;

import kr.co.bootpay.pg.Bootpay;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;

/**
 * 테스트용 글로벌 설정 클래스.
 * 환경변수 BOOTPAY_ENV 값에 따라 development / production 키를 전환합니다.
 * 기본값은 "development" 입니다.
 */
public class TestConfig {

    // ── 환경 판별 ──────────────────────────────────────────────
    private static final String ENV = System.getenv("BOOTPAY_ENV") != null
            ? System.getenv("BOOTPAY_ENV")
            : "development";

    public static boolean isProduction() {
        return "production".equalsIgnoreCase(ENV);
    }

    public static String getDevMode() {
        return isProduction() ? "PRODUCTION" : "DEVELOPMENT";
    }

    // ── PG API 키 ─────────────────────────────────────────────
    // Development
    private static final String PG_APP_ID_DEV = "59bfc738e13f337dbd6ca48a";
    private static final String PG_PRIVATE_KEY_DEV = "pDc0NwlkEX3aSaHTp/PPL/i8vn5E/CqRChgyEp/gHD0=";

    // Production
    private static final String PG_APP_ID_PROD = "5b8f6a4d396fa665fdc2b5ea";
    private static final String PG_PRIVATE_KEY_PROD = "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=";

    public static String getPgAppId() {
        return isProduction() ? PG_APP_ID_PROD : PG_APP_ID_DEV;
    }

    public static String getPgPrivateKey() {
        return isProduction() ? PG_PRIVATE_KEY_PROD : PG_PRIVATE_KEY_DEV;
    }

    // ── Commerce API 키 ───────────────────────────────────────
    // Development
    private static final String COMMERCE_CLIENT_KEY_DEV = "hxS-Up--5RvT6oU6QJE0JA";
    private static final String COMMERCE_SECRET_KEY_DEV = "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=";

    // Production
    private static final String COMMERCE_CLIENT_KEY_PROD = "sEN72kYZBiyMNytA8nUGxQ";
    private static final String COMMERCE_SECRET_KEY_PROD = "rnZLJamENRgfwTccwmI_Uu9cxsPpAV9X2W-Htg73yfU=";

    public static String getCommerceClientKey() {
        return isProduction() ? COMMERCE_CLIENT_KEY_PROD : COMMERCE_CLIENT_KEY_DEV;
    }

    public static String getCommerceSecretKey() {
        return isProduction() ? COMMERCE_SECRET_KEY_PROD : COMMERCE_SECRET_KEY_DEV;
    }

    // ── 인스턴스 생성 헬퍼 ────────────────────────────────────

    /**
     * PG API Bootpay 인스턴스를 생성합니다.
     */
    public static Bootpay createBootpay() {
        return new Bootpay(getPgAppId(), getPgPrivateKey(), getDevMode());
    }

    /**
     * PG API Bootpay 인스턴스를 생성하고 토큰을 발급합니다.
     */
    public static Bootpay createBootpayWithToken() throws Exception {
        Bootpay bootpay = createBootpay();
        bootpay.getAccessToken();
        return bootpay;
    }

    /**
     * Commerce API BootpayStore 인스턴스를 생성합니다.
     */
    public static BootpayStore createBootpayStore() {
        TokenPayload payload = new TokenPayload(getCommerceClientKey(), getCommerceSecretKey());
        return new BootpayStore(payload, getDevMode());
    }

    /**
     * Commerce API BootpayStore 인스턴스를 생성하고 토큰을 발급합니다.
     */
    public static BootpayStore createBootpayStoreWithToken() throws Exception {
        BootpayStore store = createBootpayStore();
        store.withToken();
        return store;
    }
}
