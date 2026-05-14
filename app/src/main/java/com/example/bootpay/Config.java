package com.example.bootpay;

/**
 * SDK 테스트용 설정 클래스
 */
public class Config {
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

    // 현재 환경: "production" 또는 "development"
    public static final String CURRENT_ENV = env("BOOTPAY_ENV", "production");

    // PG 인증 방식: "new" (client_key/secret_key) 또는 "legacy" (application_id/private_key).
    // 매 실행 시 BOOTPAY_AUTH_MODE 환경변수로 토글한다.
    public static final String AUTH_MODE = env("BOOTPAY_AUTH_MODE", "new").toLowerCase();

    // PG API 키 - ck/sk 와 legacy application_id/private_key 모두 .env / 환경변수 로 주입한다 (.env.example 참고)
    public static class PG {
        public static String getClientKey() {
            return CURRENT_ENV.equals("production") ? env("BOOTPAY_PG_CLIENT_KEY_PROD", "") : env("BOOTPAY_PG_CLIENT_KEY_DEV", "");
        }

        public static String getSecretKey() {
            return CURRENT_ENV.equals("production") ? env("BOOTPAY_PG_SECRET_KEY_PROD", "") : env("BOOTPAY_PG_SECRET_KEY_DEV", "");
        }

        public static String getApplicationId() {
            return CURRENT_ENV.equals("production") ? env("BOOTPAY_PG_APPLICATION_ID_PROD", "") : env("BOOTPAY_PG_APPLICATION_ID_DEV", "");
        }

        public static String getPrivateKey() {
            return CURRENT_ENV.equals("production") ? env("BOOTPAY_PG_PRIVATE_KEY_PROD", "") : env("BOOTPAY_PG_PRIVATE_KEY_DEV", "");
        }

        /**
         * BOOTPAY_AUTH_MODE 에 따라 ck/sk(default) 또는 legacy application_id/private_key 로 Bootpay 인스턴스 생성.
         * 일반 example/테스트는 이 메서드를 사용해 두 모드를 환경변수만으로 전환할 수 있다.
         */
        public static kr.co.bootpay.pg.Bootpay createBootpay() {
            if ("legacy".equals(AUTH_MODE)) {
                System.out.println("[BOOTPAY_AUTH_MODE=legacy] PG: application_id/private_key (Bearer) | env=" + CURRENT_ENV);
                return new kr.co.bootpay.pg.Bootpay(getApplicationId(), getPrivateKey(), CURRENT_ENV);
            }
            System.out.println("[BOOTPAY_AUTH_MODE=new] PG: client_key/secret_key (Basic Auth) | env=" + CURRENT_ENV);
            return kr.co.bootpay.pg.Bootpay.withClientKey(getClientKey(), getSecretKey(), CURRENT_ENV);
        }
    }

    // Commerce API 키 - ck/sk 는 .env / 환경변수 로 주입한다 (.env.example 참고)
    public static class Commerce {
        public static String getClientKey() {
            return CURRENT_ENV.equals("production") ? env("BOOTPAY_COMMERCE_CLIENT_KEY_PROD", "") : env("BOOTPAY_COMMERCE_CLIENT_KEY_DEV", "");
        }

        public static String getSecretKey() {
            return CURRENT_ENV.equals("production") ? env("BOOTPAY_COMMERCE_SECRET_KEY_PROD", "") : env("BOOTPAY_COMMERCE_SECRET_KEY_DEV", "");
        }
    }

    // 테스트 데이터
    public static class TestData {
        public static final String RECEIPT_ID = "628b2206d01c7e00209b6087";
        public static final String RECEIPT_ID_CONFIRM = "62876963d01c7e00209b6028";
        public static final String RECEIPT_ID_CASH = "62e0f11f1fc192036b1b3c92";
        public static final String RECEIPT_ID_ESCROW = "628ae7ffd01c7e001e9b6066";
        public static final String RECEIPT_ID_BILLING = "62c7ccebcf9f6d001b3adcd4";
        public static final String RECEIPT_ID_TRANSFER = "66541bc4ca4517e69343e24c";
        public static final String BILLING_KEY = "628b2644d01c7e00209b6092";
        public static final String BILLING_KEY_2 = "66542dfb4d18d5fc7b43e1b6";
        public static final String RESERVE_ID = "6490149ca575b40024f0b70d";
        public static final String RESERVE_ID_2 = "628b316cd01c7e00219b6081";
        public static final String USER_ID = "1234";
        public static final String CERTIFICATE_RECEIPT_ID = "61b009aaec81b4057e7f6ecd";
    }
}
