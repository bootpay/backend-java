package kr.co.bootpay.store.model.pojo;

import java.util.List;
import java.util.Map;

public class SProduct {

    public String productId;
    public String categoryId;
    public String projectId;
    public String sellerId;
    public String subscriptionSettingId;
    public String deliveryShippingId;
    public String brandId;
    public String manufacturerId;

    public String name; // 상품명
    public String description; // 요약 설명
    public List<String> images; // 이미지 리스트
    public int type; // 상품 유형
    public int taxType; // 과세 유형
    public boolean useStock; // 재고 관리 여부
    public int stock; // 남은 재고
    public boolean useOptionStock; // 옵션별 재고 관리 여부
    public boolean useStockSafe; // 안전 재고 관리 여부
    public int stockSafe; // 안전 재고 임계값

    public double displayPrice; // 판매가
    public double taxFreePrice; // 비과세 가격
    public boolean useDiscount; // 할인 사용 여부
    public double discountPrice; // 할인가
    public int discountPriceType; // 할인 유형 (정률, 정액)
    public boolean useDiscountPeriod; // 할인 기간 사용 여부
    public String discountStartAt; // 할인 시작일
    public String discountEndAt; // 할인 종료일

    public boolean useAccumulation; // 적립금 사용 여부
    public double accumulationPoint; // 적립금 값
    public int accumulationPointType; // 적립금 유형

    public boolean statusDisplay; // 쇼핑몰 전시 여부
    public boolean useDisplayPeriod; // 전시 기간 사용 여부
    public String displayStartAt; // 전시 시작일
    public String displayEndAt; // 전시 종료일
    public boolean statusSale; // 판매 상태
    public boolean useSalePeriod; // 판매 기간 사용 여부
    public String saleStartAt; // 판매 시작일
    public String saleEndAt; // 판매 종료일

    public int countSale; // 판매 횟수
    public int countQna; // 상품 문의 수
    public int countLike; // 좋아요 수
    public int countReview; // 상품 리뷰 수

    public String barcode; // 바코드 (ISBN, UPC, GTIN)
    public String sku; // SKU 관리 코드
    public List<String> searchTags; // 검색 태그
    public List<String> eventTags; // 이벤트 태그
    public List<String> targetUserTags; // 고객 타겟 태그
    public List<String> deliveryTags; // 배송 태그
    public List<String> emotionTags; // 감성 태그

    public boolean useCoupon; // 쿠폰 사용 가능 여부
    public boolean useMinor; // 미성년자 구매 가능 여부
    public boolean useFreeGift; // 무료 사은품 사용 여부
    public String freeGift; // 무료 사은품 정보

    public boolean useBulkPurchaseDiscount; // 복수 구매 할인 정책 사용 여부
    public Map<String, Object> bulkPurchaseDiscount; // 복수 구매 할인 정책 정보

    public boolean useReviewPoint; // 리뷰 포인트 사용 여부
    public Map<String, Object> reviewPoint; // 리뷰 포인트 정보

    public boolean useSeo; // SEO 사용 여부
    public String seoPageTitle; // SEO 페이지 타이틀
    public String seoMetaDescription; // SEO 메타 설명
    public List<String> seoMetaTags; // SEO 메타 태그

    public String modelId;
    public String modelName;
    public String manufacturerName;
    public String brandName;
    public String originCode;
    public String originName;
    public String importer; // 수입자 정보

    public boolean used; // 중고 상품 여부
    public String expiredAt; // 유효기간
    public String manufacturedAt; // 제조일자

    public boolean useSetupFee; // 추가비용/설치비용 여부
    public double setupFeeValue; // 추가비용 기본값
    public int setupFeeType; // 설치비 유형
    public String setupFeeName; // 설치비 이름
    public String setupFeeText; // 설치비 설명

    public boolean useDeliveryShipping; // 배송 여부
    public int deliveryShippingFeeType; // 배송비 유형
    public boolean useOverseasShipping; // 해외배송 여부
    public boolean useDeliveryShippingBundle; // 묶음 배송 여부
    public String deliveryShippingBundleId; // 묶음 배송 정책 ID

    public boolean useSubscription; // 구독 상품 여부
    public boolean useSubscriptionTimes; // 구독 횟수 여부
    public boolean useProductPrice; // 상품 전액 가격 사용 여부

    public boolean useCancel; // 취소 가능 여부
    public boolean useAbleRefund; // 환불 가능 여부
    public boolean useAbleCart; // 장바구니 사용 여부

    public String createdAt;
    public String updatedAt;

}
