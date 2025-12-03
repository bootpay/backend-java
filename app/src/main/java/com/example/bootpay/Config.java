package com.example.bootpay;

/**
 * SDK 테스트용 설정 클래스
 */
public class Config {
    // 현재 환경: "production" 또는 "development"
    public static final String CURRENT_ENV = "production";

    // PG API 키
    public static class PG {
        // Production 환경
        public static final String PRODUCTION_APPLICATION_ID = "5b8f6a4d396fa665fdc2b5ea";
        public static final String PRODUCTION_PRIVATE_KEY = "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=";

        // Development 환경
        public static final String DEV_APPLICATION_ID = "59bfc738e13f337dbd6ca48a";
        public static final String DEV_PRIVATE_KEY = "pDc0NwlkEX3aSaHTp/PPL/i8vn5E/CqRChgyEp/gHD0=";

        public static String getApplicationId() {
            return CURRENT_ENV.equals("production") ? PRODUCTION_APPLICATION_ID : DEV_APPLICATION_ID;
        }

        public static String getPrivateKey() {
            return CURRENT_ENV.equals("production") ? PRODUCTION_PRIVATE_KEY : DEV_PRIVATE_KEY;
        }
    }

    // Commerce API 키
    public static class Commerce {
        // Production 환경
        public static final String PRODUCTION_CLIENT_KEY = "sEN72kYZBiyMNytA8nUGxQ";
        public static final String PRODUCTION_SECRET_KEY = "rnZLJamENRgfwTccwmI_Uu9cxsPpAV9X2W-Htg73yfU=";

        // Development 환경
        public static final String DEV_CLIENT_KEY = "hxS-Up--5RvT6oU6QJE0JA";
        public static final String DEV_SECRET_KEY = "r5zxvDcQJiAP2PBQ0aJjSHQtblNmYFt6uFoEMhti_mg=";

        public static String getClientKey() {
            return CURRENT_ENV.equals("production") ? PRODUCTION_CLIENT_KEY : DEV_CLIENT_KEY;
        }

        public static String getSecretKey() {
            return CURRENT_ENV.equals("production") ? PRODUCTION_SECRET_KEY : DEV_SECRET_KEY;
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
