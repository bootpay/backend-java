package kr.co.bootpay;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class BootpayResponse {

    static public HashMap<String, Object> responseJson(Gson gson, String response, int httpStatus) {
        Type resType = new TypeToken<HashMap<String, Object>>() {}.getType();
        HashMap<String, Object> result = null;
        try {
            result = gson.fromJson(response, resType);
            if (result == null) {
                result = new HashMap<>();
            }
            result.put("http_status", httpStatus);
        } catch (Exception e) {
            if (result == null) {
                result = new HashMap<>();
            }
            result.put("http_status", httpStatus);
            result.put("data", response);
        }
        return result;
    }
}
