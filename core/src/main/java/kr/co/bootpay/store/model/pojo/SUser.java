package kr.co.bootpay.store.model.pojo;

import java.util.List;
import java.util.Map;

public class SUser {
    public String userId;
    public String createdAt;
    public String updatedAt;

    // 고객 유형
    public Integer membershipType;

    // 고객 정보
    public String name;
    public String phone;
    public String email;
    public String tel;
    public String nickname;
    public String bankUsername;
    public String bankAccount;
    public String bankCode;
    public String comment;

    // 최종상태
    public Integer count;
    public Integer status;

    // 개인 고객
    public Integer gender;
    public String birth;
    public Map<String, Object> individualExtension;

    // 개발자 메뉴
    public String foreignKey;

    // 쇼핑몰 회원
    public String loginId;
    public String phoneHash;
    public String emailHash;
    public String loginPw;
    public Integer loginType;

    public List<String> groupTags;
    public Map<String, Object> metadata;

    // 인증정보
    public Boolean authSms;
    public Boolean authPhone;
    public Boolean authEmail;
    public String ci;
    public String cd;

    // 2차 인증 정보
//    public List<Map<String, Object>> authLv2Array;
    public String joinAt;
    public Integer joinConfirmType;
    public String lastedAt;

    // 약관 동의
    public Integer marketingAcceptType;
    public String marketingAcceptCreateAt;
    public String marketingAcceptUpdateAt;
    public List<String> termIds;

    public SUserGroup group;
}

