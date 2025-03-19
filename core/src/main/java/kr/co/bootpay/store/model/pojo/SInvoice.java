package kr.co.bootpay.store.model.pojo;

import java.util.List;
import java.util.Map;

public class SInvoice {
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
    public List<String> sendTypes;

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
}

