package kr.co.bootpay.pg;

import kr.co.bootpay.common.BootpayMode;

/**
 * {@link Bootpay} 생성 빌더.
 *
 * <p>기존 표면은 신규 인증이 static factory({@code Bootpay.withClientKey}), legacy 인증이
 * 생성자({@code new Bootpay(...)}) 로 갈라져 있고, 환경은 문자열이라 오타를 잡을 수 없었습니다.
 * 이 빌더는 두 인증 방식을 같은 형태로 통일하고, 환경을 {@link BootpayMode} 로 받으며,
 * 키 누락을 {@link #build()} 시점에 즉시 알려줍니다.</p>
 *
 * <pre>{@code
 * // 신규 인증 (client_key / secret_key)
 * Bootpay bootpay = Bootpay.builder()
 *         .clientKey(clientKey)
 *         .secretKey(secretKey)
 *         .mode(BootpayMode.PRODUCTION)
 *         .build();
 *
 * // legacy 인증 (application_id / private_key)
 * Bootpay legacy = Bootpay.builder()
 *         .applicationId(applicationId)
 *         .privateKey(privateKey)
 *         .build();
 * }</pre>
 *
 * <p>환경을 지정하지 않으면 {@link BootpayMode#PRODUCTION} 입니다.</p>
 *
 * @since 3.3.0
 */
public class BootpayBuilder {

    private String clientKey;
    private String secretKey;
    private String applicationId;
    private String privateKey;
    private BootpayMode mode = BootpayMode.PRODUCTION;

    /**
     * 신규 인증의 client_key 를 지정합니다.
     *
     * @param clientKey client_key
     * @return this
     */
    public BootpayBuilder clientKey(String clientKey) {
        this.clientKey = clientKey;
        return this;
    }

    /**
     * 신규 인증의 secret_key 를 지정합니다.
     *
     * @param secretKey secret_key
     * @return this
     */
    public BootpayBuilder secretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    /**
     * legacy 인증의 application_id 를 지정합니다.
     *
     * @param applicationId REST application_id
     * @return this
     */
    public BootpayBuilder applicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    /**
     * legacy 인증의 private_key 를 지정합니다.
     *
     * @param privateKey private_key
     * @return this
     */
    public BootpayBuilder privateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    /**
     * 호출 대상 환경을 지정합니다. 미지정 시 {@link BootpayMode#PRODUCTION} 입니다.
     *
     * @param mode 환경
     * @return this
     */
    public BootpayBuilder mode(BootpayMode mode) {
        this.mode = mode == null ? BootpayMode.PRODUCTION : mode;
        return this;
    }

    /**
     * 호출 대상 환경을 문자열로 지정합니다. 인식할 수 없는 값은 PRODUCTION 으로 처리합니다.
     *
     * @param mode "development" / "test" / "stage" / "production"
     * @return this
     */
    public BootpayBuilder mode(String mode) {
        return mode(BootpayMode.of(mode));
    }

    /**
     * 인스턴스를 생성합니다.
     *
     * <p>client_key/secret_key 를 모두 지정하면 신규 인증(Basic Auth) 으로, application_id/private_key 를
     * 모두 지정하면 legacy 인증(Bearer) 으로 생성합니다. 둘 다 지정한 경우 신규 인증을 우선합니다.</p>
     *
     * @return 생성된 인스턴스
     * @throws IllegalStateException 키가 짝을 이루지 않거나 아무 키도 지정되지 않은 경우
     */
    public Bootpay build() {
        boolean hasClientKey = isFilled(clientKey);
        boolean hasSecretKey = isFilled(secretKey);
        boolean hasApplicationId = isFilled(applicationId);
        boolean hasPrivateKey = isFilled(privateKey);

        if (hasClientKey && hasSecretKey) {
            return Bootpay.withClientKey(clientKey, secretKey, mode.value());
        }
        if (hasClientKey) {
            throw new IllegalStateException("secretKey 값이 비어있습니다. clientKey 와 secretKey 는 함께 지정해야 합니다.");
        }
        if (hasSecretKey) {
            throw new IllegalStateException("clientKey 값이 비어있습니다. clientKey 와 secretKey 는 함께 지정해야 합니다.");
        }

        if (hasApplicationId && hasPrivateKey) {
            return new Bootpay(applicationId, privateKey, mode.value());
        }
        if (hasApplicationId) {
            throw new IllegalStateException("privateKey 값이 비어있습니다. applicationId 와 privateKey 는 함께 지정해야 합니다.");
        }
        if (hasPrivateKey) {
            throw new IllegalStateException("applicationId 값이 비어있습니다. applicationId 와 privateKey 는 함께 지정해야 합니다.");
        }

        throw new IllegalStateException(
                "인증 정보가 없습니다. clientKey/secretKey 또는 applicationId/privateKey 를 지정하세요.");
    }

    private static boolean isFilled(String value) {
        return value != null && !value.isEmpty();
    }
}
