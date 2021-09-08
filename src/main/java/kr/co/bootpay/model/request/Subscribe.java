package kr.co.bootpay.model.request;

public class Subscribe {
    public String orderId; // 개발사에서 관리하는 고유 주문 번호
    public String pg; // PG사의 Alias ex) danal, kcp, inicis 등
    public String itemName; // 상품명

    public String cardNo; // 카드 일련번호
    public String cardPw; // 카드 비밀번호 앞 2자리
    public String expireYear; // 카드 유효기간 년
    public String expireMonth; // 카드 유효기간 월
    public String identifyNumber; // 주민등록번호 또는 사업자번호
    public User userInfo; // 구매자 정보
    public SubscribeExtra extra;
}
