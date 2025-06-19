package kr.co.bootpay.store.model.pojo;

import java.util.List;

public class SOrderSubscriptionBill {

    public String orderSubscriptionBillId;
    public String orderSubscriptionId;
    public String userId;
    public String userGroupId;

    public int subscriptionBillingType; // 결제수단
    public String orderName; // 주문명
    public String paidWalletId; // 실제 결제에 사용된 wallet_id
    public String reservedWalletId; // 예약에 사용된 reserve_wallet_id

    public String orderNumber;
    public String orderPreId;
    public String orderId;
    public int duration; // 회차
    public int totalSubscriptionDuration; // 총 회차

    public Double oneUnitPrice; // 상품 한 개의 가격
    public Double oneUnitTaxFreePrice; // 상품 한 개의 비과세 가격
    public Double setupPrice; // 초기설정 금액 (1회차인 경우만 존재)

    public Double price; // 본래 상품 결제 금액
    public Double taxFreePrice; // 본래 상품 비과세 결제 금액
    public int unit; // 통화 단위

    public Double purchasePrice; // 지불 금액
    public Double purchaseTaxFreePrice; // 지불 비과세 금액

    public Double cancelledPrice; // 취소된 금액
    public Double cancelledTaxFreePrice; // 취소된 비과세 금액
    public Double cancelledFee; // 취소 수수료 (예: 배송상품 환불/수거 비용)

    public int membershipType; // 멤버십 유형

    public String addressId;
    public String userAddress;
    public String username;
    public String userPhone;
    public String userEmail;
    public String userCompanyName;
    public String userBusinessNumber;

    public List<String> productIds; // 구독할 상품 목록
    public List<String> productOptionIds; // 구독할 상품 옵션 목록
    public List<String> productSnapshotIds; // 구독할 상품 스냅샷 목록
    public List<String> productOptionSnapshotIds; // 구독할 상품 옵션 스냅샷 목록
    public int productType; // 한 종류의 상품만 담을 수 있음
    public int quantity; // 구독 수량

    public String reservePaymentAt; // 예약 결제일
    public String purchasedAt; // 결제일
    public String revokedAt; // 취소일
    public String lastErrorAt; // 마지막 에러 발생일

    public int status; // 상태
    public int cancelStatus;
    public String testCode; // 테스트 코드

    // 고객 정보 표시용
    public String serviceStartAt; // 해당 회차 시작일
    public String serviceEndAt; // 해당 회차 종료일

}
