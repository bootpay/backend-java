package kr.co.bootpay.store.model.pojo;

import java.util.HashMap;
import java.util.Map;

public class SNotificationSetting {
    public String notificationSettingId;
    public String projectId;
    public String sellerId;

    // 카카오 관련 설정
    public boolean useKakaoSelf = false;
    public String kakaoYellowId;
    public String kakaoYellowPhone;
    public String kakaoProfileKey;
    public boolean kakaoVerified = false;

    // 이메일 관련 설정
    public boolean useEmailSelf = false;
    public String emailId;
    public String emailPassword;
    public String emailSmtpIp;
    public String emailSmtpPort;
    public boolean useEmailTls = true;
    public boolean emailSmtpVerified = false;

    // 문자 관련 설정
    public boolean useMessageSelf = false;
    public String phone;
    public String nationCode = "82";
    public String lastUpdatedById;
    public String lastUpdatedByName;
    public boolean messageVerified = false;

    // 알림 설정 매핑
    public Map<Integer, int[]> userSignupAuthenticate = new HashMap<>();
    public Map<Integer, int[]> userSignupComplete = new HashMap<>();
    public Map<Integer, int[]> userPasswordTemporaryIssued = new HashMap<>();
    public Map<Integer, int[]> orderComplete = new HashMap<>();
    public Map<Integer, int[]> orderCancelled = new HashMap<>();
    public Map<Integer, int[]> deliveryCompleted = new HashMap<>();
    public Map<Integer, int[]> claimRefundCompleted = new HashMap<>();
    public Map<Integer, int[]> couponIssued = new HashMap<>();
    public Map<Integer, int[]> invoiceRequest = new HashMap<>();

    public SNotificationSetting() {
        // 기본 알림 설정 초기화
        userSignupAuthenticate.put(1, new int[]{2, 3});
        userSignupComplete.put(1, new int[]{2, 3});
        userPasswordTemporaryIssued.put(1, new int[]{2, 3});
        orderComplete.put(1, new int[]{2, 3});
        orderCancelled.put(1, new int[]{2, 3});
        deliveryCompleted.put(1, new int[]{2, 3});
        claimRefundCompleted.put(1, new int[]{2, 3});
        couponIssued.put(1, new int[]{2, 3});
        invoiceRequest.put(1, new int[]{2, 3});
    }
}
