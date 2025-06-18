package kr.co.bootpay.store.model.request.product;

public class ProductStatusUpdateParams {
    public String productId;

    // 기존 필드
    public Boolean statusDisplay;        // 전시 상태 (null이면 전송 안함)
    public Boolean statusSale;           // 판매 상태 (null이면 전송 안함)

    // 추가 필드
    public Boolean statusFrozen;         // 동결 여부
    public Integer statusReview;         // 검수 상태 (숫자형 enum 등으로 사용 가능)

    public Boolean useDisplayPeriod;     // 전시 기간 사용 여부
    public String displayStartAt; // 전시 시작일시
    public String displayEndAt;   // 전시 종료일시

    public Boolean useSalePeriod;        // 판매 기간 사용 여부
    public String saleStartAt;    // 판매 시작일시
    public String saleEndAt;      // 판매 종료일시

    public Boolean useDiscountPeriod;         // 할인 기간 사용 여부
    public String discountStartAt;     // 할인 시작일시
    public String discountEndAt;       // 할인 종료일시

    public Boolean statusTimeSale;       // 타임세일 여부
    public String timeSaleStartAt; // 타임세일 시작일시
    public String timeSaleEndAt;   // 타임세일 종료일시
    public Double timeSaleDiscountPercent; // 타임세일 할인율 (%)

    public Boolean useStock;             // 재고 사용 여부
    public Integer stock;                // 재고 수량

    public Boolean useStockSafe;         // 안전 재고 사용 여부
    public Integer stockSafe;            // 안전 재고 수량

    public Boolean useOptionStock;       // 옵션별 재고 사용 여부
}