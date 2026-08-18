package kr.co.bootpay.store.model.request.orderSubscription.request.ing;

public class OrderSubscriptionTransferParams {
    public String orderSubscriptionId;
    public String newUserId; //승계받을 회원 id
    public String newUsername; //승계받을 회원 이름
    public String newUserEmail; //승계받을 회원 이메일
    public String newUserPhone; //승계받을 회원 연락처
    public String newUserAddress; //승계받을 회원 주소
    public String walletId; //승계 이후 사용할 결제수단 id
    public String reason;
}
