package kr.co.bootpay.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BankCode {
    private static Map<String, String> bankMap = createMap();

    private static Map<String, String> createMap() {
        Map<String, String> result = new HashMap<>();
        result.put("한국은행", "001");
        result.put("기업은행", "003");
        result.put("외환은행", "005");
        result.put("수협은행", "007");
        result.put("농협은행", "011");
        result.put("우리은행", "020");
        result.put("SC은행", "023");
        result.put("대구은행", "031");
        result.put("부산은행", "032");
        result.put("광주은행", "034");
        result.put("경남은행", "039");
        result.put("우체국", "071");
        result.put("KEB하나은행", "081");
        result.put("신한은행", "088");
        result.put("케이뱅크", "089");
        result.put("카카오뱅크", "090");
        return Collections.unmodifiableMap(result);
    }

    public static String getCode(String name) {
        if(name == null) return "";
        return bankMap.get(name);
    }
}
