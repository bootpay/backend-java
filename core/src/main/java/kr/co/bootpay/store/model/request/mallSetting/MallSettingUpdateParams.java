package kr.co.bootpay.store.model.request.mallSetting;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

// 몰 설정 수정 요청 파라미터
// 요청 바디는 flatten 형식이며 값이 설정된(non-null) 필드만 서버로 전송된다
public class MallSettingUpdateParams {
    // Idempotency-Key 헤더로 전송되므로 body에는 포함하지 않는다
    public transient String idempotencyKey;

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
    @SerializedName("addr_1")
    public String addr1;
    @SerializedName("addr_2")
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

    @SerializedName("use_age_accept_19")
    public Boolean useAgeAccept19;
    @SerializedName("use_age_accept_14")
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

    public Boolean useCorporateDepartment;
    public Integer subGroupType;
    public Boolean useCorporateSignupApproval;
    public List<String> corporateEmailDomains;
    public Boolean useCorporateAutoApprove;
    public Boolean useCorporateInviteOnly;

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
    // 서버 필드명이 use_oder_cancel_approval 이므로 그대로 전송한다
    @SerializedName("use_oder_cancel_approval")
    public Boolean useOrderCancelApproval;
    public List<String> orderCancelReasons;
    public Integer orderCancelReasonRequiredType;
    public String orderCancelRequestMessage;
    public String orderCancelDoneMessage;

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
    public Boolean usePointTransaction;
    public String pointDisplayName;
    public Double pointMinBalance;
    public List<Integer> pointNotCondition;
    public Integer pointCondition;
    public Boolean usePointMaxRate;
    public Double pointMaxRate;
    public Boolean usePointMaxAmount;
    public Double pointMaxAmount;
    public Double pointRate;
    public Integer pointCalcType1;
    public Integer pointCalcType2;
    public Boolean usePointAdvanceDiscount;
    public Double pointAdvanceDiscountRate;
    public Boolean usePointExpire;
    public Integer pointExpireType;
    public Integer pointIssueEventType;
    public Integer pointIssueDelayDays;

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
    public Boolean useWithdrawalGuideMessageAfter;
    public String withdrawalGuideMessageAfter;
    public Boolean useWithdrawalAuto;
    public Integer withdrawalAutoYear;

    public Boolean useSubscriptionAggregateTransaction;
    public Integer subscriptionMonthDay;
    public Integer subscriptionWeekDay;

    public Boolean useLimit;
    public Double limitMonthPurchase;
    public Double limitWeekPurchase;
    public Boolean useLimitPayment;
    public Boolean useLimitMessage;

    public String termsOfService;
    public String termsOfPrivacyPolicy;
    public String termsOfPrivacyCollect;
    public String termsOfPrivacyThird;

    public Integer paymentTimeout;
    public Integer productSortType;
    public Integer mallThemeType;

    public Integer catalogDisplayType;
    public String catalogHeadline;
    public String catalogBgColor;
    public Integer catalogViewTypePc;
    public Integer catalogViewTypeMobile;
    public Integer catalogProductSortType;

    public Boolean useCart;
    public Integer cartStoragePeriod;
    public Integer cartMaxLimit;
    public Integer cartAddAction;
    public Boolean cartDirectPurchase;
    public Boolean cartOptionChange;
    public Boolean cartDiscountDisplay;

    public Boolean useWishlist;
    public Integer wishlistMaxLimit;
    public Boolean cartWishlistDisplay;
}
