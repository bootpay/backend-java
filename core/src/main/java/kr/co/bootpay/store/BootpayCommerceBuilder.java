package kr.co.bootpay.store;

import kr.co.bootpay.common.BootpayMode;
import kr.co.bootpay.common.BootpayRole;

/**
 * {@link BootpayCommerce} 생성 빌더.
 *
 * <p>기존 표면은 {@code new BootpayStore(new TokenPayload(ck, sk), "DEVELOPMENT")} 처럼 래퍼 객체와
 * 문자열 환경을 함께 넘겨야 했고, 환경 문자열을 잘못 적으면 baseUrl 이 비어 호출이 실패했습니다.
 * 이 빌더는 PG 의 {@code Bootpay.builder()} 와 같은 형태로 통일하고, 환경을 {@link BootpayMode} 로
 * 받아 그 문제를 없앱니다.</p>
 *
 * <pre>{@code
 * BootpayCommerce bootpay = BootpayCommerce.builder()
 *         .clientKey(clientKey)
 *         .secretKey(secretKey)
 *         .mode(BootpayMode.PRODUCTION)
 *         .role(BootpayRole.USER)
 *         .build();
 * }</pre>
 *
 * <p>환경을 지정하지 않으면 {@link BootpayMode#PRODUCTION}, role 을 지정하지 않으면
 * {@link BootpayRole#USER} 입니다.</p>
 *
 * @since 3.3.0
 */
public class BootpayCommerceBuilder {

    private String clientKey;
    private String secretKey;
    private BootpayMode mode = BootpayMode.PRODUCTION;
    private BootpayRole role = BootpayRole.USER;

    /**
     * client_key 를 지정합니다.
     *
     * @param clientKey client_key
     * @return this
     */
    public BootpayCommerceBuilder clientKey(String clientKey) {
        this.clientKey = clientKey;
        return this;
    }

    /**
     * secret_key 를 지정합니다.
     *
     * @param secretKey secret_key
     * @return this
     */
    public BootpayCommerceBuilder secretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    /**
     * 호출 대상 환경을 지정합니다. 미지정 시 {@link BootpayMode#PRODUCTION} 입니다.
     *
     * @param mode 환경
     * @return this
     */
    public BootpayCommerceBuilder mode(BootpayMode mode) {
        this.mode = mode == null ? BootpayMode.PRODUCTION : mode;
        return this;
    }

    /**
     * 호출 대상 환경을 문자열로 지정합니다. 인식할 수 없는 값은 PRODUCTION 으로 처리합니다.
     *
     * @param mode "development" / "test" / "stage" / "production"
     * @return this
     */
    public BootpayCommerceBuilder mode(String mode) {
        return mode(BootpayMode.of(mode));
    }

    /**
     * {@code BOOTPAY-ROLE} 헤더 값을 지정합니다. 미지정 시 {@link BootpayRole#USER} 입니다.
     *
     * @param role role
     * @return this
     */
    public BootpayCommerceBuilder role(BootpayRole role) {
        this.role = role == null ? BootpayRole.USER : role;
        return this;
    }

    /**
     * {@code BOOTPAY-ROLE} 헤더 값을 문자열로 지정합니다. 인식할 수 없는 값은 USER 로 처리합니다.
     *
     * @param role "user" / "manager" / "partner" / "vendor" / "supervisor"
     * @return this
     */
    public BootpayCommerceBuilder role(String role) {
        return role(BootpayRole.of(role));
    }

    /**
     * 인스턴스를 생성합니다.
     *
     * @return 생성된 인스턴스
     * @throws IllegalStateException clientKey 또는 secretKey 가 비어있는 경우
     */
    public BootpayCommerce build() {
        if (clientKey == null || clientKey.isEmpty()) {
            throw new IllegalStateException("clientKey 값이 비어있습니다.");
        }
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("secretKey 값이 비어있습니다.");
        }
        return new BootpayCommerce(clientKey, secretKey, mode, role);
    }
}
