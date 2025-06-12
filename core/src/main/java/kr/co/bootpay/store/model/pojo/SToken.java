package kr.co.bootpay.store.model.pojo;

public class SToken {
    public String clientKey; //부트페이 관리자에서 권한 등록 후 발급받은 키. 공개키 개념이다.
    public String secretKey; //부트페이 관리자에서 권한 등록 후 발급받은 키. 최초 생성시에만 확인이 가능함

//    @Deprecated
    public String serverKey; // 부트페이 관리자에서 확인 가능한 rest application id
//    @Deprecated
    public String privateKey; // 부트페이 관리자에서 확인 가능한 private key
}

