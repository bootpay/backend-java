package kr.co.bootpay.pg.model.request;

public class Item {
    public String name; // 상품명
    public int qty; // 수량
    public String id; // 상품 고유키
    public double price; // 상품단가
    public String cat1; // 카테고리 상
    public String cat2; // 카테고리 중
    public String cat3; // 카테고리 하

    public String categoryType; // 결제 상품 유형을 입력합니다. 네이버페이 결제시 사용됩니다.

    public String categoryCode; // 결제 상품 유형을 입력합니다. 네이버페이 문서상 categoryId 값과 동일합니다.

    public String startDate; // 이용 시작일을 입력합니다. 결제 상품이 공연, 영화, 보험, 여행, 항공, 숙박인 경우 입력을 권장합니다. yyyyMMdd, 20240701

    public String endDate; // 이용기간 종료일을 입력합니다. 결제 상품이 공연, 영화, 보험, 여행, 항공, 숙박인 경우 입력을 권장합니다.  yyyyMMdd, 20240701
}
