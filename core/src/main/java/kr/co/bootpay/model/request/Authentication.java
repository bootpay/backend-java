package kr.co.bootpay.model.request;

public class Authentication {
    public String pg;
    public String method;
    public String username;
    public String identityNo; //생년월일 + 주민등록번호 뒷 1자리
    public String carrier; //통신사
    public String phone;
    public String stieUrl; //본인인증 하는 url 또는 App 명
    public String orderName;
    public String authenticationId;
}
