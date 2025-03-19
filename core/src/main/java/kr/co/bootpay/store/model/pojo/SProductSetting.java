package kr.co.bootpay.store.model.pojo;

public class SProductSetting {
    public String productSettingId;
    public String projectId;
    public String sellerId;

    // 카테고리 관련 설정
    public boolean useCategory;
    public boolean useDisplayCategory;

    // 상품명 관련 설정
    public boolean useName;
    public boolean useSummaryDescription;
    public boolean useKeyword;
    public boolean useDisplayStatus;
    public boolean useSaleStatus;

    // 판매가 관련 설정
    public boolean useSalePrice;
    public boolean useDiscountPrice;
    public boolean useCostPrice;
    public boolean useTimeSale;
    public boolean useTaxType;

    // 재고 관련 설정
    public boolean useStock;

    // 주문 옵션 관련 설정
    public boolean useProductOption;
    public boolean useProductAdd;
    public boolean useOrderRequest;

    // 상품 이미지 관련 설정
    public boolean useImage;

    // 상세 설명·안내 관련 설정
    public boolean useDetailDescription;
    public boolean useDeliveryInfo;
    public boolean useReturnRefundInfo;
    public boolean useAfterService;

    // 상품 주요 정보 관련 설정
    public boolean useSupplier;
    public boolean useModelName;
    public boolean useBrand;
    public boolean useManufacturer;
    public boolean useOrigin;
    public boolean useManufactureDate;
    public boolean useReleaseDate;
    public boolean useExpirationDate;
    public boolean useColorFilter;
    public boolean useUnitFilter;
    public boolean useWeight;

    // 상품 정보 제공 고시 관련 설정
    public boolean useProductDisplay;
    public boolean useKC;

    // 배송 관련 설정
    public boolean useDeliveryShipping;
    public boolean useDeliveryShippingBundle;
    public boolean useDeliveryShippingAdd;
    public boolean useDeliveryShippingImportCustoms;

    // 반품/교환 관련 설정
    public boolean useReturnRefund;
    public boolean useReturnRefundDetail;

    // 구매 혜택/제한 관련 설정
    public boolean useIcon;
    public boolean useCouponDownload;
    public boolean useMultipleDiscount;
    public boolean useGiftProduct;
    public boolean useOrderQuantity;
    public boolean useAccessAuthority;
    public boolean useCouponApply;
    public boolean useAccumulationPoint;
    public boolean useMemberDiscount;
    public boolean useMinor;
    public boolean useGiftOption;

    // 검색 엔진 최적화(SEO) 관련 설정
    public boolean useSEO;
    public boolean useSKU;
    public boolean useBarcode;

    // 정기 구독 관련 설정
    public boolean useSubscription;

    // 추가 정보
    public int informationType;
    public String createdAt;
    public String updatedAt;
}
