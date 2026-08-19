package kr.co.bootpay.store.model.pojo;

import com.google.gson.annotations.SerializedName;

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

    // 기업(그룹) 회원
    public Boolean useCorporateDepartment;
    public Integer subGroupType;
    public Boolean useCorporateSignupApproval;
    /** 기업 회원 허용 이메일 도메인 목록 */
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
    /** 취소 승인 사용 여부 (서버 필드명 오타 `use_oder_cancel_approval` 그대로 유지) */
    @SerializedName("use_oder_cancel_approval")
    public Boolean useOrderCancelApproval;
    /** 취소 사유 목록 */
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
    public Integer pointMaxUseLimitType;
    public Double pointMaxUseLimitValue;
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
    public String withdrawalGuideMessage;
    public Boolean useWithdrawalGuideMessageAfter;
    public String withdrawalGuideMessageAfter;
    public Boolean useWithdrawalAuto;
    public Integer withdrawalAutoYear;

    public Boolean useSubscriptionAggregateTransaction;
    public Integer subscriptionMonthDay;
    public Integer subscriptionWeekDay;

    public Double useLimit;
    public Double limitMonthPurchase;
    public Double limitWeekPurchase;
    public String limitMessage;
    public Boolean useLimitPayment;
    public Boolean useLimitMessage;

    public String termsOfService;
    public String termsOfPrivacyPolicy;
    public String termsOfPrivacyCollect;
    public String termsOfPrivacyThird;

    // 결제 / 노출
    public Integer paymentTimeout;
    public Integer productSortType;
    public Integer mallThemeType;
    public Integer catalogDisplayType;
    public String catalogHeadline;
    public String catalogBgColor;
    public Integer catalogViewTypePc;
    public Integer catalogViewTypeMobile;
    public Integer catalogProductSortType;

    // 장바구니 / 위시리스트
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

    public String createdAt;
    public String updatedAt;
}

