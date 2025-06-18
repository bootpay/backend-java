package kr.co.bootpay.store;

import java.util.HashMap;
import java.util.List;

public class BootpayStoreResponse {
    private int httpStatus;
    private boolean success;
    private Object data;
    private String error;

    public BootpayStoreResponse(int httpStatus, boolean success, Object data, String error) {
        this.httpStatus = httpStatus;
        this.success = success;
        this.data = data;
        this.error = error;
    }

    // HTTP 상태 코드 반환
    public int getHttpStatus() {
        return httpStatus;
    }

    // 성공 여부 확인
    public boolean isSuccess() {
        return success;
    }

    // 실패 여부 확인
    public boolean isFailed() {
        return !success;
    }

    // 데이터 반환 (타입 자동 판단)
    @SuppressWarnings("unchecked")
    public <T> T getData() {
        if (data instanceof HashMap) {
            return (T) data;
        } else if (data instanceof List) {
            return (T) data;
        } else {
            return (T) data;
        }
    }

    // 데이터가 HashMap인지 확인
    public boolean isDataMap() {
        return data instanceof HashMap;
    }

    // 데이터가 List인지 확인
    public boolean isDataList() {
        return data instanceof List;
    }

    // 데이터가 null인지 확인
    public boolean isDataNull() {
        return data == null;
    }

    // 데이터를 HashMap으로 캐스팅하여 반환
    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getDataAsMap() {
        if (data instanceof HashMap) {
            return (HashMap<String, Object>) data;
        }
        return null;
    }

    // 데이터를 List로 캐스팅하여 반환
    @SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> getDataAsList() {
        if (data instanceof List) {
            return (List<HashMap<String, Object>>) data;
        }
        return null;
    }

    // 에러 메시지 반환
    public String getError() {
        return error;
    }

    // 에러가 있는지 확인
    public boolean hasError() {
        return error != null && !error.isEmpty();
    }

    // HashMap으로 변환 (기존 코드와의 호환성을 위해)
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("http_status", httpStatus);
        result.put("success", success);
        result.put("data", data);
        if (error != null) {
            result.put("error", error);
        }
        return result;
    }

    @Override
    public String toString() {
        return "BootpayStoreResponse{" +
                "httpStatus=" + httpStatus +
                ", success=" + success +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
} 