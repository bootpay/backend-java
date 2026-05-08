package kr.co.bootpay;

import kr.co.bootpay.pg.Bootpay;
import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.TokenPayload;

/**
 * 테스트용 글로벌 설정 클래스.
 * 환경변수 BOOTPAY_ENV 값에 따라 development / production 키를 전환합니다.
 * 기본값은 "production" 입니다.
 */
public class TestConfig {
    private static final java.util.Map<String, String> DOTENV = loadDotEnv();

    private static java.util.Map<String, String> loadDotEnv() {
        java.util.Map<String, String> values = new java.util.HashMap<>();
        for (String path : new String[]{".env", "../.env"}) {
            java.io.File file = new java.io.File(path);
            if (!file.exists()) continue;
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#") || !line.contains("=")) continue;
                    String[] parts = line.split("=", 2);
                    String value = parts[1].trim();
                    if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                        value = value.substring(1, value.length() - 1);
                    }
                    values.putIfAbsent(parts[0].trim(), value);
                }
            } catch (java.io.IOException ignored) {
            }
        }
        return values;
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        if (value != null && !value.isEmpty()) return value;
        value = DOTENV.get(key);
        return value != null && !value.isEmpty() ? value : fallback;
    }


    // ── 환경 판별 ──────────────────────────────────────────────
    private static final String ENV = env("BOOTPAY_ENV", "production");

    // PG 인증 방식: "new" (client_key/secret_key) 또는 "legacy" (application_id/private_key).
    // 매 실행 시 BOOTPAY_AUTH_MODE 환경변수로 토글한다.
    public static final String AUTH_MODE = env("BOOTPAY_AUTH_MODE", "new").toLowerCase();

    public static boolean isProduction() {
        return "production".equalsIgnoreCase(ENV);
    }

    public static String getDevMode() {
        return isProduction() ? "PRODUCTION" : "DEVELOPMENT";
    }

    // ── PG API 키 ─────────────────────────────────────────────
    // ck/sk 는 .env / 환경변수 로 주입한다 (.env.example 참고). legacy application_id/private_key 만 SDK 내부에 둔다.
    private static final String PG_APP_ID_DEV = "59bfc738e13f337dbd6ca48a";
    private static final String PG_PRIVATE_KEY_DEV = "pDc0NwlkEX3aSaHTp/PPL/i8vn5E/CqRChgyEp/gHD0=";
    private static final String PG_APP_ID_PROD = "5b8f6a4d396fa665fdc2b5ea";
    private static final String PG_PRIVATE_KEY_PROD = "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=";

    public static String getPgClientKey() {
        return isProduction() ? env("BOOTPAY_PG_CLIENT_KEY_PROD", "") : env("BOOTPAY_PG_CLIENT_KEY_DEV", "");
    }

    public static String getPgSecretKey() {
        return isProduction() ? env("BOOTPAY_PG_SECRET_KEY_PROD", "") : env("BOOTPAY_PG_SECRET_KEY_DEV", "");
    }

    public static String getPgAppId() {
        return isProduction() ? PG_APP_ID_PROD : PG_APP_ID_DEV;
    }

    public static String getPgPrivateKey() {
        return isProduction() ? PG_PRIVATE_KEY_PROD : PG_PRIVATE_KEY_DEV;
    }

    // ── Commerce API 키 ───────────────────────────────────────
    // ck/sk 는 .env / 환경변수 로 주입한다 (.env.example 참고).
    public static String getCommerceClientKey() {
        return isProduction() ? env("BOOTPAY_COMMERCE_CLIENT_KEY_PROD", "") : env("BOOTPAY_COMMERCE_CLIENT_KEY_DEV", "");
    }

    public static String getCommerceSecretKey() {
        return isProduction() ? env("BOOTPAY_COMMERCE_SECRET_KEY_PROD", "") : env("BOOTPAY_COMMERCE_SECRET_KEY_DEV", "");
    }

    // ── 인스턴스 생성 헬퍼 ────────────────────────────────────

    /**
     * BOOTPAY_AUTH_MODE 에 따라 ck/sk(default) 또는 legacy application_id/private_key 로 PG Bootpay 인스턴스 생성.
     */
    public static Bootpay createBootpay() {
        if ("legacy".equals(AUTH_MODE)) {
            System.out.println("[BOOTPAY_AUTH_MODE=legacy] PG: application_id/private_key (Bearer) | env=" + ENV);
            return new Bootpay(getPgAppId(), getPgPrivateKey(), getDevMode());
        }
        System.out.println("[BOOTPAY_AUTH_MODE=new] PG: client_key/secret_key (Basic Auth) | env=" + ENV);
        return Bootpay.withClientKey(getPgClientKey(), getPgSecretKey(), getDevMode());
    }

    /**
     * PG API Bootpay 인스턴스를 생성하고 토큰을 발급합니다.
     * legacy application_id 방식에서만 토큰 발급 필요. ck/sk 는 매 요청 Basic Auth 헤더로 직접 인증되므로 호출 불필요.
     */
    public static Bootpay createBootpayWithToken() throws Exception {
        Bootpay bootpay = createBootpay();
        if ("legacy".equals(AUTH_MODE)) {
            bootpay.getAccessToken();
        }
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
