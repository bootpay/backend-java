package kr.co.bootpay.store.model.pojo;

import java.util.List;
import java.util.Map;

import java.util.List;
import java.util.Map;

public class SMallSetting {
    public String mallSettingId;
    public String projectId;
    public String sellerId;

    public String normalWidgetKey;
    public String subscriptionWidgetKey;

    public String sellerName;
    public String sellerNameEn;
    public String bizEmail;
    public String bizTel;
    public String bizFax;
    public String registrationNo;
    public String corpRegNo;
    public String mailOrderSalesNumber;
    public String ownerName;
    public String zip;
    public String addr1;
    public String addr2;
    public String privacyName;
    public String privacyEmail;

    public String name;
    public String description;
    public Integer status;
    public String invoiceTitle;

    public Boolean useLogo;
    public String logo;
    public Boolean useFavicon;
    public String favicon;
    public Boolean useOpenGraph;
    public String ogImage;
    public Boolean useSignature;
    public String signature;

    public Boolean useOperationTime;
    public Map<String, Object> customerServiceCenterOperationTime;

    public Integer restStartHour;
    public Integer restStartMinute;
    public Integer restEndHour;
    public Integer restEndMinute;
    public List<String> restDay;

    public String hostingService;
    public Boolean useNonMemberOrder;

    public Boolean useAgeAccept19;
    public Boolean useAgeAccept14;
    public Boolean useAgeAcceptParentName;
    public Boolean useAgeAcceptParentBirth;
    public Boolean useAgeAcceptParentEmail;

    public Boolean useMembershipCollectPhone;
    public Boolean useMembershipCollectTel;
    public Boolean useMembershipCollectEmail;
    public Boolean useMembershipCollectAddress;
    public Boolean useMembershipCollectBank;
    public Boolean useMembershipCollectBirth;
    public Boolean useMembershipCollectGender;
    public Boolean useMembershipCollectInterest;
    public Integer membershipCollectInterestNumber;
    public Boolean useMembershipCollectCustoms;
    public Boolean useMembershipCollectNickname;
    public Boolean useMembershipCollectRecommendId;
    public Double recommendIdPointTo;
    public Double recommendIdPointFrom;

    public Boolean useMembershipCollectBusiness;
    public Boolean useMembershipCollectRegister;
    public Boolean membershipOnlyBusiness;

    public Boolean useMemberInfoPhone;
    public Boolean useMemberInfoTel;
    public Boolean useMemberInfoEmail;
    public Boolean useMemberInfoAddress;
    public Boolean useMemberInfoBank;
    public Boolean useMemberInfoBirth;
    public Boolean useMemberInfoGender;
    public Boolean useMemberInfoCustoms;
    public Boolean useMemberInfoNickname;
    public Boolean useMemberInfoRegister;

    public Boolean ordererCollectPhone;
    public Boolean ordererCollectTel;
    public Boolean ordererCollectEmail;

    public String orderPrefix;
    public Boolean useOrderCancel;
    public Boolean useOrderCancelApproval;

    public Boolean useGeneralMembership;
    public List<String> generalMembershipDuplication;
    public Boolean useCertification;
    public Integer certificationType;
    public List<Integer> generalMembershipIdType;

    public Boolean useMembershipDuplicationEmail;
    public Boolean useMembershipDuplicationPhone;

    public Boolean useSocialMembership;
    public List<Integer> socialMembershipType;

    public Boolean usePoint;
    public List<Integer> pointNotCondition;
    public Integer pointCondition;
    public Integer pointMaxUseLimitType;
    public Double pointMaxUseLimitValue;
    public Integer pointCalcType1;
    public Integer pointCalcType2;
    public Boolean usePointAdvanceDiscount;
    public Double pointAdvanceDiscountRate;
    public Boolean usePointExpire;
    public Integer pointExpireType;

    public Boolean useOpenMarket;
    public Boolean useProductApproval;
    public Boolean useProductReview;
    public Boolean useProductReviewPoint;
    public Double productReviewPoint;
    public Double productReviewPhotoPoint;
    public Boolean useProductReviewAnswer;
    public Boolean useProductReviewAutoAnswer;
    public Integer productReviewAutoAnswerMinute;
    public String productReviewAutoAnswerText;

    public Boolean useProductQna;
    public Integer productQnaMemberAuth;
    public List<Integer> useProductQnaAnswerOption;

    public Boolean useNotice;
    public Boolean useQna;
    public Boolean useFaq;

    public Boolean useChatSupport;
    public Integer chatSupportType;
    public Map<String, Object> chatSupportKey;

    public Boolean useDormant;
    public Integer dormantYear;
    public Integer dormantRestore;

    public Boolean useWithdrawal;
    public Boolean useWithdrawalGuideMessage;
    public String withdrawalGuideMessage;
    public Boolean useWithdrawalGuideMessageAfter;
    public String withdrawalGuideMessageAfter;
    public Boolean useWithdrawalAuto;
    public Integer withdrawalAutoYear;

    public Boolean useSubscriptionAggregateTransaction;
    public Double useLimit;
    public Double limitMonthPurchase;
    public String limitMessage;
    public Boolean useLimitPayment;
    public Boolean useLimitMessage;

    public String termsOfService;
    public String termsOfPrivacyPolicy;
    public String termsOfPrivacyCollect;
    public String termsOfPrivacyThird;
}

