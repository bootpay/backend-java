package kr.co.bootpay.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * PG / Commerce 양쪽이 공유하는 통일 응답 타입.
 *
 * <p>기존 표면은 PG 가 {@code HashMap<String, Object>} 를, Commerce 가
 * {@code BootpayStoreResponse} 를 돌려주어 성공 판정 방식이 서로 달랐습니다. 이 타입은 두 경우 모두
 * {@link #isSuccess()} 하나로 판정하고, 응답 본문은 {@link #getData()} 로 꺼내도록 통일합니다.</p>
 *
 * <pre>{@code
 * BootpayResponse res = bootpay.payment.get(receiptId);
 * if (res.isSuccess()) {
 *     Map<String, Object> data = res.getData();
 *     System.out.println(data.get("status_locale"));
 * } else {
 *     System.out.println(res.getErrorCode() + " " + res.getMessage());
 * }
 * }</pre>
 *
 * <p>기존 코드와 섞어 쓸 때는 {@link #asMap()} 으로 가공 전 원본 맵을 그대로 받을 수 있습니다.</p>
 *
 * @since 3.3.0
 */
public class BootpayResponse {

    private final boolean success;
    private final Map<String, Object> data;
    private final Integer errorCode;
    private final String message;
    private final Map<String, Object> raw;

    private BootpayResponse(boolean success, Map<String, Object> data, Integer errorCode,
                            String message, Map<String, Object> raw) {
        this.success = success;
        this.data = data == null ? Collections.<String, Object>emptyMap() : data;
        this.errorCode = errorCode;
        this.message = message;
        this.raw = raw == null ? Collections.<String, Object>emptyMap() : raw;
    }

    /**
     * 일반 팩토리. 각 API 표면이 자기 응답 형태를 이 타입으로 옮길 때 사용합니다.
     *
     * @param success   성공 여부
     * @param data      응답 본문
     * @param errorCode 에러 코드 (없으면 null)
     * @param message   에러 메시지 (없으면 null)
     * @param raw       가공 전 원본 맵
     * @return 통일 응답
     */
    public static BootpayResponse of(boolean success, Map<String, Object> data, Integer errorCode,
                                     String message, Map<String, Object> raw) {
        return new BootpayResponse(success, data, errorCode, message, raw);
    }

    /**
     * PG API 가 돌려주는 원본 맵을 통일 응답으로 변환합니다.
     *
     * <p>PG 는 실패 시 본문에 {@code error_code} 를 싣고, 성공 시에는 싣지 않습니다. 또한 SDK 가
     * {@code http_status} 를 덧붙이는데, 이는 응답 본문이 아니므로 {@link #getData()} 에서 제외합니다
     * ({@link #asMap()} 에는 그대로 남습니다).</p>
     *
     * @param raw PG 서비스가 돌려준 원본 맵
     * @return 통일 응답
     */
    public static BootpayResponse ofPg(Map<String, Object> raw) {
        if (raw == null) {
            return new BootpayResponse(false, null, null, "응답이 비어있습니다.", null);
        }
        Object errorCodeValue = raw.get("error_code");
        boolean success = errorCodeValue == null;

        Map<String, Object> data = new LinkedHashMap<String, Object>(raw);
        data.remove("http_status");

        return new BootpayResponse(success, data, toInteger(errorCodeValue), toString(raw.get("message")), raw);
    }

    /**
     * 값을 Integer 로 변환합니다. 변환할 수 없으면 null 을 반환합니다.
     *
     * @param value 원본 값
     * @return 변환된 정수, 불가 시 null
     */
    public static Integer toInteger(Object value) {
        if (value instanceof Number) return ((Number) value).intValue();
        if (value instanceof String) {
            try {
                return Integer.valueOf(((String) value).trim());
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    /**
     * 값을 String 으로 변환합니다.
     *
     * @param value 원본 값
     * @return 문자열, null 이면 null
     */
    public static String toString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    /**
     * @return 성공 여부
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return 실패 여부
     */
    public boolean isFailed() {
        return !success;
    }

    /**
     * 응답 본문을 반환합니다. 응답이 없어도 null 이 아닌 빈 맵을 돌려주므로 NPE 방어가 필요 없습니다.
     *
     * @return 응답 본문 (읽기 전용이 아닌 복사본)
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * 응답 본문에서 키 하나를 꺼냅니다.
     *
     * @param key 키
     * @return 값, 없으면 null
     */
    public Object get(String key) {
        return data.get(key);
    }

    /**
     * 응답 본문에서 문자열 값 하나를 꺼냅니다.
     *
     * @param key 키
     * @return 문자열 값, 없으면 null
     */
    public String getString(String key) {
        return toString(data.get(key));
    }

    /**
     * @return 에러 코드, 성공했거나 서버가 코드를 주지 않았으면 null
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     * @return 에러 메시지, 없으면 null
     */
    public String getMessage() {
        return message;
    }

    /**
     * 가공 전 원본 맵을 반환합니다. 기존 {@code HashMap} 기반 코드와 섞어 쓸 때 사용합니다.
     *
     * @return 원본 맵의 복사본
     */
    public HashMap<String, Object> asMap() {
        return new HashMap<String, Object>(raw);
    }

    @Override
    public String toString() {
        return "BootpayResponse{success=" + success
                + ", errorCode=" + errorCode
                + ", message='" + message + '\''
                + ", data=" + data
                + '}';
    }
}
