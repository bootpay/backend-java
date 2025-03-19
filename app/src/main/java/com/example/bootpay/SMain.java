package com.example.bootpay;

import kr.co.bootpay.store.BootpayStore;
import org.json.simple.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;


public class SMain {

    static BootpayStore bootpay;
    public static void main(String[] args) {
        bootpay = new BootpayStore("67c92fb8d01640bb9859c612", "ugaqkJ8/Yd2HHjM+W1TF6FZQPTmvx1rny5OIrMqcpTY=", "DEVELOPMENT");
        goGetToken();
    }

    public static void goGetToken() {
        try {
            HashMap<String, Object> res = bootpay.getAccessToken();
            if(res.get("error_code") == null) { //success
                System.out.println("goGetToken success: " + res);
            } else {
                System.out.println("goGetToken false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

