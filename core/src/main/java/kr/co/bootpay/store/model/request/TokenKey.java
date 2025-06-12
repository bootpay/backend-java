package kr.co.bootpay.store.model.request;

public class TokenKey {
    public TokenKey() {}
    public TokenKey(String clientKey, String secretKey) {
        this.clientKey = clientKey;
        this.secretKey = secretKey;
    }

    public String clientKey;
    public String secretKey;
    public String serverKey;
    public String privateKey;
}
