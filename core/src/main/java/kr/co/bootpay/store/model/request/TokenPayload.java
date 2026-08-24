package kr.co.bootpay.store.model.request;

public class TokenPayload {
    public TokenPayload() {}
    public TokenPayload(String clientKey, String secretKey) {
        this.clientKey = clientKey;
        this.secretKey = secretKey;
    }

    public String clientKey;
    public String secretKey;

    /**
     * @deprecated Commerce API does not support application_id/private_key authentication.
     *             This field remains only for source compatibility and is ignored.
     */
    @Deprecated
    public String privateKey;
}
