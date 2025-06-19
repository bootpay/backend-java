package kr.co.bootpay.store.model.pojo;

import java.util.List;
import java.util.Map;

public class SStore {
    public String storeId; // ID 필드

    public String sellerId;
    public String projectId;

    public String name;
    public List<String> images; // 첫 번째는 메인 사진
    public String contentHtml;

    public int productHasType; // 전체 상품을 다 판매할지, 별도 관리할지

    public String addressId; // 주소, 빈 값일 수 있음

    public String phone; // 전화번호
    public List<String> availableTime; // 영업 시간 (예: ["09:00 ~ 12:00", "13:00 ~ 18:00"])
    public String availableMemo; // 영업 메모 (사용자에게 노출)
    public List<Integer> availableDay; // 영업 요일 (월~일: [0, 1, 2, 3, 4, 5, 6])

    // 실시간 주문 (사이렌오더) 관련
    public boolean useRealtimeOrder;
    public List<String> realtimeOrderAvailableTime; // 실시간 주문 가능 시간
    public List<Integer> realtimeOrderAvailableDay; // 실시간 주문 가능 요일
    public Double realtimeRadius; // 실시간 주문 반경 (km, 0이면 무제한)
    public Double realtimeMinimumOrderAmount; // 실시간 주문 최소 주문 금액 (0이면 무제한)
    public int realtimeRequiredTime; // 메뉴당 최대 준비 시간 (분)
    public String realtimeMemo; // 실시간 주문 메모 (사용자에게 노출)

    // 예약 주문 관련 (하루 전 마감)
    public boolean useReservation;
    public List<String> reservationAvailableTime; // 예약 주문 가능 시간
    public List<Integer> reservationAvailableDay; // 예약 주문 가능 요일
    public Double reservationMinimumOrderPrice; // 예약 주문 최소 주문 금액 (0이면 무제한)
    public String reservationMemo; // 예약 주문 메모 (사용자에게 노출)

    public Map<String, Object> metadata; // 메타데이터 (예: {"parking": false, "wifi": true, "valet": true})

}
