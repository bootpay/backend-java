package kr.co.bootpay.store.model.pojo;

public class SRole {
    public static int LV_GOD = 100;
    public static int LV_ADMIN = 10;
    public static int LV_MANAGER = 5;
    public static int LV_USER = 1;

    public static String toString(int role) {
        if(role == LV_GOD) return "";

        return "";
    }


//    MEMBER_LV_GOD     = 100.freeze
//            MEMBER_LV_ADMIN   = 10.freeze
//            MEMBER_LV_MANAGER = 5.freeze
//            MEMBER_LV_USER    = 1.freeze
}
