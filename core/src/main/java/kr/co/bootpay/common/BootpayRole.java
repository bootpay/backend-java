package kr.co.bootpay.common;

/**
 * Commerce API 의 {@code BOOTPAY-ROLE} 헤더 값.
 *
 * <p>기존 표면의 {@code withRole("manager")} 같은 문자열 지정을 대체합니다. 오타로 인해 서버가 권한을
 * 거부하는 상황을 컴파일 타임에 막습니다.</p>
 *
 * @since 3.3.0
 */
public enum BootpayRole {
    USER("user"),
    MANAGER("manager"),
    PARTNER("partner"),
    VENDOR("vendor"),
    SUPERVISOR("supervisor");

    private final String value;

    BootpayRole(String value) {
        this.value = value;
    }

    /**
     * @return 헤더에 실리는 실제 문자열 값
     */
    public String value() {
        return value;
    }

    /**
     * 문자열을 role 로 변환합니다. 인식할 수 없는 값은 {@link #USER} 로 처리합니다.
     *
     * @param value "manager", "SUPERVISOR" 등의 role 문자열. null 이면 USER
     * @return 대응하는 role, 인식 불가 시 USER
     */
    public static BootpayRole of(String value) {
        if (value == null) return USER;
        for (BootpayRole role : values()) {
            if (role.value.equalsIgnoreCase(value.trim())) return role;
        }
        return USER;
    }
}
