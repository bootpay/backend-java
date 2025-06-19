package kr.co.bootpay.store.model.pojo;

import java.util.List;
import java.util.Map;

public class SOrderSubscriptionTransaction {
    public String orderSubscriptionTransactionId;
    public String sellerId;
    public String projectId;
    public List<String> orderSubscriptionIds; // 그룹 결제일 경우
    public List<String> orderSubscriptionBillIds; // 그룹 결제일 경우

    public String reservedWalletId; // 예약결제 시 사용할 wallet_id
    public String paidWalletId; // 실제 결제에 사용된 wallet_id
    public List<String> failedWalletIds; // 실패한 wallet_id 리스트
    public String userId;
    public List<String> userIds; // 그룹 결제일 경우
    public String userGroupId;

    public int membershipType; // 멤버십 유형
    public int subscriptionBillingType; // 결제 수단
    public String orderName;
    public Map<String, Object> metadata;
    public Map<String, Object> lastErrorData;

    public Map<String, Object> userAddress;
    public String username;
    public String userPhone;
    public String userEmail;
    public String userCompanyName;
    public String userBusinessNumber;

    public String orderNumber;
    public String orderPreId;
    public String orderId;

    public Double purchasePrice;
    public Double purchaseTaxFreePrice;
    public int unit;

    public Double cancelledPrice;
    public Double cancelledTaxFreePrice;
    public Double cancelledFee;

    public String purchasedAt;
    public String revokedAt;

    public int tryCount; // 예약결제 시도 횟수
    public String reservePaymentAt;
    public String latestTryAt;

    public String testCode;
    public Map<String, Object> testExtra;

    public int status; // 현재 결제 상태
}
