package kr.co.bootpay.store.model.pojo;

import java.util.List;
import java.util.Map;

public class SProject {

    public String projectId;
    public String sellerId;
    public String projectSettingId;
    public String productSettingId;
    public String notificationSettingId;
    public String deliverySettingId;
    public String mallSettingId;

    // 시스템 정보
    public List<String> parentSellers;

    // 노출 정보
    public String name;
    public String sellerName;
    public int status;

    // 심사 정보
    public String homepage;
    public int unit;
    public Map<String, Object> documentScreen;
    public int statusReview;

    // 개발 연동 정보
    public String clientKey;
    public String clientOauthKey;
    public String serverKey;
    public String privateKey;
    public String javascriptKey;
    public String androidKey;
    public String iosKey;
    public String restKey;
    public String corePrivateKey;

    public List<String> keyBox;

    // 웹훅 관련 정보
    public String webhookUrl;
    public List<String> webhookRoles;
    public int webhookRetryCount;
    public int headerContentType;

    // 보안 설정
    public List<String> whiteIpList;
    public boolean useFirewall;
    public int developmentStatus;

    // 정산정보
    public String bankCode;
    public String accountNumber;
    public String accountOwner;
    public boolean statusSettleAble;

    // 배송 관련 설정
    public String todayDelivery;
    public List<String> holidays;

    // 이용약관
    public String serviceUrl;
    public String privacyUrl;
    public Map<String, Object> otherTerms;

    // 쇼핑몰 정보
    public Map<String, Object> timezone;
    public Map<String, Object> sellerCompany;

    // 서비스센터 정보
    public String afterServicePhone;
    public String afterServiceEmail;

    // 이용 요금 결제 관련
    public String billingKeyId;

    // 도메인 관련 설정
    public List<String> domainList;
    public List<String> tags;

    // 현금영수증
    public boolean useReceiptCash;
    public int receiptCashType;

    // 프로젝트 생성 관련
    public String projectReceiptId;
}