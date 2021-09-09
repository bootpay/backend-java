package kr.co.bootpay.model.request;

import java.util.List;

public class SubscribePayload {
    public String billingKey; // 발급받은 빌링키
    public String itemName; // 결제할 상품명
    public long price; // 결제할 상품금액
    public int taxFree; // 면세 상품일 경우 해당만큼의 금액을 설정
    public String orderId; // 개발사에서 지정하는 고유주문번호
    public int quota; // 5만원 이상 결제건에 적용하는 할부개월수. 0-일시불, 1은 지정시 에러 발생함, 2-2개월, 3-3개월... 12까지 지정가능
    public int interest; // 웰컴페이먼츠 전용, 무이자여부를 보내는 파라미터가 있다
    public User userInfo; // 구매자 정보, 특정 PG사의 경우 구매자 휴대폰 번호를 필수로 받는다
    public List<Item> items; // 구매할 상품정보, 통계용
    public String feedbackUrl; // webhook 통지시 받으실 url 주소 (localhost 사용 불가)
    public String feedbackContentType; // webhook 통지시 받으실 데이터 타입 (json 또는 urlencoded, 기본값 urlencoded)
    public SubscribeExtra extra;
    public String schedulerType; //정기결제 예약시 - oneshot 으로 지정해야함.
    public long executeAt; //정기결제 예약시
}
