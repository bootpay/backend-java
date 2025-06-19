package kr.co.bootpay.pg.model.request;

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

    public Double price; //빌링키 발급과 동시에 첫 결제할 금액
    public Double taxFree; //빌링키 발급과 동시에 첫 결제할 비과세금액
    public Map<String, Object> metadata = new HashMap<>();
    public User user; // 구매자 정보
    public SubscribeExtra extra;

    //     계좌 자동 결제

    public String username; // 계좌주 이름을 입력합니다.
    public String authType = "ARS"; //출금동의 방식을 입력합니다. ARS 또는 간편인증
    public String bankName; // 계좌 은행명을 입력합니다.
    public String bankAccount; // 계좌 번호를 입력합니다.
    public String identityNo; // 계좌주의 생년월일 6자리 또는 사업자번호 10자리를 입력합니다.

    public String cashReceiptType = "소득공제"; // 현금영수증 발행 유형을 입력합니다.
    public String cashReceiptIdentityNo; // 현금영수증 발행을 위한 인증번호 값 입니다. 소득공제일 경우 휴대폰번호, 지출증빙 일 경우 지출증빙 값을 입력합니다.
    public String phone; // 출금 동의 수신할 전화번호를 입력합니다.
}
