package kr.co.bootpay.store.model.response;

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

    /**
     * HTTP 상태 코드 반환.
     *
     * @deprecated 성공 여부 판단은 {@link #isSuccess()} 를 사용하세요. HTTP status 노출은 다음 메이저 버전에서
     *             제거 예정이며, 기존 사용 코드는 그대로 동작합니다.
     */
    @Deprecated
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
    public HashMap<String, Object> getData() {
        if (data instanceof HashMap) {
            return (HashMap<String, Object>) data;
        }
        return null;
    }

    /**
     * 26-08-21 추가 — 최상위가 배열인 응답의 데이터 반환.
     *
     * commerce-api 의 일부 엔드포인트(예: GET /v1/categories)는 객체가 아니라 배열을
     * 그대로 내려준다. 그 경우 {@link #getData()} 는 null 을 반환하므로 이 메서드를 쓴다.
     * 기존 {@link #getData()} 의 시그니처·동작은 바뀌지 않는다.
     *
     * @return 배열 응답이면 그 목록, 아니면 null
     */
    @SuppressWarnings("unchecked")
    public List<Object> getDataList() {
        if (data instanceof List) {
            return (List<Object>) data;
        }
        return null;
    }

    // 26-08-21 추가 — 응답 본문이 배열인지 여부
    public boolean isDataList() {
        return data instanceof List;
    }

    // 26-08-21 추가 — 타입을 가리지 않고 원본 데이터 반환 (객체·배열 모두)
    public Object getRawData() {
        return data;
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
        // Deprecated: "http_status" 키는 다음 메이저 버전에서 제거 예정 — 성공 여부는 "success" 키 사용
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