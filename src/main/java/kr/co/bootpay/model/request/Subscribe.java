package kr.co.bootpay.model.request;

public class Subscribe {
    public String orderId;
    public String pg;
    public String itemName;

    public String cardNo; // 카드 일련번호
    public String cardPw; // 카드 비밀번호 앞 2자리
    public String expireYear; // 카드 유효기간 년
    public String expireMonth; // 카드 유효기간 월
    public String identifyNumber; // 주민등록번호 또는 사업자번호
    public User userInfo; // 구매자 정보
    public SubscribeExtra extra;
}
