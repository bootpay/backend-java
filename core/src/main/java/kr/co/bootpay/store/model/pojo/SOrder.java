package kr.co.bootpay.store.model.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SOrder {
    public String orderId;
    public String orderPreId;
    public List<SChosenProductOption> chosenProductOptions;

    // 주문 관련 필드
    public String parentOrderId; // 정기결제의 경우 부모 주문 ID
    public String userId; // 주문한 사용자 ID
    public String sellerId; // 판매자 ID
    public String projectId; // 프로젝트 ID
    public int status; // 주문 상태
    public int currency; // 통화 (예: KRW, USD 등)
    public boolean isSubscription; // 정기결제 여부
    public boolean isLeaf; // 단일 주문 여부
    public Double totalPrice; // 총 가격
    public Double taxFreePrice; // 비과세 금액
    public Double discountAmount; // 할인 금액
    public Double deliveryPrice; // 배송비
    public String paymentMethod; // 결제 수단 (카드, 가상계좌, 포인트 등)
    public String receiptId; // 결제 완료된 경우 영수증 ID
    public String webhookUrl; // 웹훅 URL
    public String createdAt;
    public String updatedAt;

    public List<SOrderCancellationRequestHistory> cancelledRequestHistory = new ArrayList<>();

    public SOrder() {
        this.orderId = UUID.randomUUID().toString();
    }

    // 주문 유효성 검증
    public boolean isValid() {
        return userId != null && totalPrice > 0;
    }

    // 상품 정보 무효화 (예제 메서드)
    public void invalidateProducts() {
        System.out.println("상품 데이터 검증 및 무효화 진행");
    }

    // 주문 생성 메서드
//    public static SOrder createOrder(String userId, String sellerId, double totalPrice) {
//        SOrder order = new SOrder();
//        order.userId = userId;
//        order.sellerId = sellerId;
//        order.totalPrice = totalPrice;
//        return order;
//    }
//
//    public static void main(String[] args) {
//        Order newOrder = Order.createOrder("user_123", "seller_456", 50000.0);
//        System.out.println("새 주문 생성: " + newOrder.orderId);
//    }
}
