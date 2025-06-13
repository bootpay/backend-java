package kr.co.bootpay.store.model.request;

public class TokenPayload {
    public TokenPayload() {}
    public TokenPayload(String clientKey, String secretKey) {
        this.clientKey = clientKey;
        this.secretKey = secretKey;
    }

    public String clientKey;
    public String secretKey;
    public String serverKey;
    public String privateKey;
}
