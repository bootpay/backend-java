package kr.co.bootpay.common;

/**
 * Bootpay API 호출 대상 환경.
 *
 * <p>기존 표면이 사용하던 {@code String devMode} 를 대체합니다. 문자열 오타로 baseUrl 이 비는 사고를
 * 컴파일 타임에 막고, PG / Commerce 양쪽이 같은 타입을 쓰도록 통일하는 것이 목적입니다.</p>
 *
 * <p>기본값은 항상 {@link #PRODUCTION} 입니다.</p>
 *
 * @since 3.3.0
 */
public enum BootpayMode {
    DEVELOPMENT,
    TEST,
    STAGE,
    PRODUCTION;

    /**
     * 문자열을 모드로 변환합니다. 대소문자를 가리지 않으며, 인식할 수 없는 값은 {@link #PRODUCTION} 으로
     * 처리합니다 (기존 PG 표면의 resolveBaseUrl 과 동일한 fallback 규칙).
     *
     * @param value "development", "STAGE" 등의 환경 문자열. null 이면 PRODUCTION
     * @return 대응하는 모드, 인식 불가 시 PRODUCTION
     */
    public static BootpayMode of(String value) {
        if (value == null) return PRODUCTION;
        for (BootpayMode mode : values()) {
            if (mode.name().equalsIgnoreCase(value.trim())) return mode;
        }
        return PRODUCTION;
    }

    /**
     * 기존 표면의 devMode 문자열 값을 반환합니다.
     *
     * @return "DEVELOPMENT" / "TEST" / "STAGE" / "PRODUCTION"
     */
    public String value() {
        return name();
    }
}
