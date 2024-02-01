package kr.co.bootpay.model.request;

import java.util.HashMap;
import java.util.Map;

public class Authentication {
    public String pg;
    public String method;
    public String orderName;
    public String authenticateId;
    public String username;
    public String identityNo; //생년월일 + 주민등록번호 뒷 1자리
    public String carrier; //통신사
    public String phone;
    public String siteUrl; //본인인증 하는 url 또는 App 명

    public String authenticateType;

    public Map<String, Object> metadata = new HashMap<>();

    public String authenticationId;
}
