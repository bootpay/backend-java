package kr.co.bootpay.store.model.pojo;

import java.util.List;
import java.util.Map;

public class SInvoice {

    public static int SEND_TYPE_SMS = 1;
    public static int SEND_TYPE_KAKAO = 2;
    public static int SEND_TYPE_EMAIL = 3;
    public static int SEND_TYPE_PUSH = 4;


    public String invoiceId;
    public String projectId;
    public String sellerId;

    public String name;
    public String title;
    public String memo;
    public String productName;

    public String createdOwnerId;
    public Integer createdOwnerType;

    public Integer unit;
    public Map<String, Object> metadata;

    public String requestId;
    public String sku;

    public Boolean useRedirect;
    public String redirectUrl;

    public Integer type;
    public String parentId;

    public Integer subscriptionType;
    public String subscriptionStartAt;
    public String subscriptionEndAt;

    public String expiredAt;
    public Integer status;
    public Boolean deleted;

    public Integer userCollectionType;
    public Boolean useLinkRedirect;

    public String userId;

    public Integer sendStatus;
    public List<Integer> sendTypes;

    public String messageTemplateId;
    public String messageId;
    public String messageFrom;
    public Integer messageType;
    public String messageResponse;

    public String sentAt;
    public String payAt;

    public Double price;
    public Double taxFreePrice;

    public Boolean useEditableUsername;
    public Boolean useEditablePhone;
    public Boolean useEditableEmail;
    public Boolean useMemo;

    public List<String> productIds;
    public List<String> productOptionIds;

    public List<String> tags;

    public String password;
    public String orderId;
    public String uuid;

    public String webhookUrl;
    public Integer headerContentType;
    public Integer webhookRetryCount;

    public Integer productType;
    public Boolean isOpenLink;

    public List<SInvoiceItem> invoiceItems;
    public List<String> selectedUsers;

    // ========================================
    // 청구서 생성 파라미터 (3.3.0~, ruby SDK request_checkout parity)
    // ========================================

    /** SDK 를 통한 생성인지 여부 */
    public Boolean sdk;

    /** 구매자 정보 — 회원이면 userId 만으로 충분하다 */
    public SInvoiceUser user;

    /** 청구할 상품 목록 — 등록된 상품을 참조한다 (invoiceItems 는 이름·금액 직접 기입 방식) */
    public List<SInvoiceProduct> products;

    /** 배송비 */
    public Double deliveryPrice;

    /** 생성과 동시에 구매자에게 안내를 발송할지 여부 */
    public Boolean useNotification;

    /** 청구서 링크 진입 시 자동 로그인 처리 여부 */
    public Boolean useAutoLogin;

    /** 사용량 기반 과금 시 사용량을 조회할 API 주소 */
    public String usageApiUrl;

    /** 부가 옵션 (결제·승인 분리, 주문 즉시 생성 등) */
    public SInvoiceExtra extra;

}

