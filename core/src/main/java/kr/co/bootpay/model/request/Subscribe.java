package kr.co.bootpay.model.request;

import java.util.HashMap;
import java.util.Map;

public class Subscribe {
    public String pg; // PG사의 Alias ex) danal, kcp, inicis 등
    public String orderName;
    public String subscriptionId; // 개발사에서 관리하는 고유 주문 번호

//    public String itemName; // 상품명

    public String cardNo; // 카드 일련번호
    public String cardPw; // 카드 비밀번호 앞 2자리
    public String cardIdentityNo; // 주민등록번호 또는 사업자번호
    public String cardExpireYear; // 카드 유효기간 년
    public String cardExpireMonth; // 카드 유효기간 월

    public double price; //빌링키 발급과 동시에 첫 결제할 금액
    public double taxFree; //빌링키 발급과 동시에 첫 결제할 비과세금액
    public Map<String, Object> metadata = new HashMap<>();
    public User user; // 구매자 정보
    public SubscribeExtra extra;
}
