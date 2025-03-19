package kr.co.bootpay.store.model.pojo;

public class SConstant {
    public static final int DELETED = -1;
    public static final int ABLE = 1;
    public static final int UNABLE = 0;
    public static final int READY = 2;
    public static final int DOING = 3;
    public static final int CONFIRM_READY = 4;
    public static final int FAILED = -1;

    public static final int COMPLETE = 1;
    public static final int ESCAPED = -1;
    public static final int LEAVE = -10;

    public static final int MEMBER_LV_GOD = 100;
    public static final int MEMBER_LV_ADMIN = 10;
    public static final int MEMBER_LV_MANAGER = 5;
    public static final int MEMBER_LV_USER = 1;

    // SNS LOGIN
    public static final int LOGIN_TYPE_EMAIL = 1;
    public static final int LOGIN_TYPE_KAKAO = 2;
    public static final int LOGIN_TYPE_NAVER = 3;
    public static final int LOGIN_TYPE_APPLE = 4;
    public static final int LOGIN_TYPE_GOOGLE = 5;

    // LOGIN HISTORY
    public static final int LOGIN_HISTORY_TYPE_NORMAL = 0;
    public static final int LOGIN_HISTORY_TYPE_TEST = 1;
    public static final int LOGIN_HISTORY_TYPE_TICKET = 2;
    public static final int LOGIN_HISTORY_TYPE_AUTO = 3;
    public static final int LOGIN_HISTORY_TYPE_SUPER = 4; // 관리자 강제로그인

    // 리뷰 타입
    public static final int REVIEW_TYPE_PRODUCT = 1;
    public static final int REVIEW_TYPE_PRODUCT_OPTION = 2;
    public static final int REVIEW_TYPE_PRODUCT_SELLER = 3;
    public static final int REVIEW_TYPE_PRODUCT_RESELLER = 4;

    // 심사 상태
    public static final int REVIEW_STATUS_AUTO_CONFIRM = 0;
    public static final int REVIEW_STATUS_WAITING = 1;
    public static final int REVIEW_STATUS_PENDING = 2;
    public static final int REVIEW_STATUS_CONFIRM = 3;
    public static final int REVIEW_STATUS_REJECT = 4;

    // Content Type
    public static final int CONTENT_TYPE_URLENCODED = 1;
    public static final int CONTENT_TYPE_JSON = 2;

    // Member Types
    public static final int MEMBER_TYPE_ONE = 1;
    public static final int MEMBER_TYPE_MERCHANT = 2;
    public static final int MEMBER_TYPE_RESELLER = 3;

    // Login Auth Types
    public static final int LOGIN_AUTH_TYPE_NONE = 0;
    public static final int LOGIN_AUTH_TYPE_DEVICE = 1;
    public static final int LOGIN_AUTH_TYPE_IP = 2;

    // Seller Hierarchy
    public static final int SELLER_HEAD = 5;
    public static final int SELLER_DISTRIBUTOR = 4;
    public static final int SELLER_AGENCY = 3;
    public static final int SELLER_RESELLER = 2;
    public static final int SELLER = 1;

    // Currency Units
    public static final int UNIT_KRW = 1;
    public static final int UNIT_USD = 2;
    public static final int UNIT_CNY = 3;
    public static final int UNIT_JPY = 4;
    public static final int UNIT_EUR = 5;

    // Issue Types
    public static final int ISSUE_TYPE_USER = 7;
    public static final int ISSUE_TYPE_ORDER = 8;
    public static final int ISSUE_TYPE_ORDER_SUBSCRIPTION = 9;

    // Order Status
    public static final int ORDER_STATUS_READY = 0;
    public static final int ORDER_STATUS_DEPOSIT_WAIT = 1;
    public static final int ORDER_STATUS_PAY_DONE = 2;
    public static final int ORDER_STATUS_SUBSCRIPTION_DONE = 3;
    public static final int ORDER_STATUS_SUBSCRIPTION_READY = 4;
    public static final int ORDER_STATUS_PAYMENT_PENDING = 5;
    public static final int ORDER_STATUS_BUY_CONFIRM = 8;
    public static final int ORDER_STATUS_CANCEL_DONE = 10;
    public static final int ORDER_STATUS_RETURN_DONE = 11;
    public static final int ORDER_STATUS_EXCHANGE_DONE = 12;

    // Order Delivery Status
    public static final int ORDER_DELIVERY_STATUS_READY = -101;
    public static final int ORDER_DELIVERY_STATUS_PREPARING = 100;
    public static final int ORDER_DELIVERY_STATUS_PENDING = 101;
    public static final int ORDER_DELIVERY_STATUS_REQUESTED = 102;
    public static final int ORDER_DELIVERY_STATUS_STARTED = 110;
    public static final int ORDER_DELIVERY_STATUS_DELAYED = 106;
    public static final int ORDER_DELIVERY_STATUS_COMPLETED = 107;

    // Subscription Status
    public static final int SUBSCRIPTION_STATUS_PENDING = 0;
    public static final int SUBSCRIPTION_STATUS_TRIAL = -1;
    public static final int SUBSCRIPTION_STATUS_ACTIVE = 1;
    public static final int SUBSCRIPTION_STATUS_PAUSE = -2;
    public static final int SUBSCRIPTION_STATUS_HOLD_ON = -3;
    public static final int SUBSCRIPTION_STATUS_AUTO_END = 15;
    public static final int SUBSCRIPTION_STATUS_TERMINATED = 20;

    // Payment Status
    public static final int PAYMENT_STATUS_READY = 0;
    public static final int PAYMENT_STATUS_COMPLETE = 1;
    public static final int PAYMENT_STATUS_ON_CONFIRM = 2;
    public static final int PAYMENT_STATUS_VBANK_ISSUED = 5;
    public static final int PAYMENT_STATUS_CANCEL_COMPLETE = 20;

    // User Gender
    public static final int USER_GENDER_UNKNOWN = -1;
    public static final int USER_GENDER_UNSPECIFIED = 0;
    public static final int USER_GENDER_MALE = 1;
    public static final int USER_GENDER_FEMALE = 2;

    // Subscription Cycle Types
    public static final int CYCLE_TYPE_1_WEEK = 11;
    public static final int CYCLE_TYPE_2_WEEK = 12;
    public static final int CYCLE_TYPE_1_MONTH = 21;
    public static final int CYCLE_TYPE_3_MONTH = 23;
    public static final int CYCLE_TYPE_6_MONTH = 26;
    public static final int CYCLE_TYPE_1_YEAR = 31;

    // Subscription Type
    public static final int SUBSCRIPTION_TYPE_REGULAR = 1;
    public static final int SUBSCRIPTION_TYPE_DELIVERY = 4;
    public static final int SUBSCRIPTION_TYPE_RENTAL_LEASE = 5;

    // Subscription Bill Status
    public static final int BILLING_STATUS_FAILED = -1;
    public static final int BILLING_STATUS_READY = 2;
    public static final int BILLING_STATUS_DONE = 1;
    public static final int BILLING_STATUS_PENDING = 5;
    public static final int BILLING_STATUS_CANCELLED = 10;
    public static final int BILLING_STATUS_TERMINATED = 20;

}
