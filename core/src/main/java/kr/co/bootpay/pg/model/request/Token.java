package kr.co.bootpay.pg.model.request;

public class Token {
    public String application_id; // legacy: 부트페이 관리자에서 확인 가능한 rest application id
    public String private_key; // legacy: 부트페이 관리자에서 확인 가능한 private key
    public String client_key; // 권장: client key
    public String secret_key; // 권장: secret key
}

