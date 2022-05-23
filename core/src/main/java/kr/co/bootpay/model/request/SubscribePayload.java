package kr.co.bootpay.model.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscribePayload {
    public String billingKey; // 발급받은 빌링키
    public String orderName; // 결제할 상품명
    public String orderId; // 개발사에서 지정하는 고유주문번호
    public long price; // 결제할 상품금액
    public int taxFree; // 면세 상품일 경우 해당만큼의 금액을 설정
    public int cardQuota; // 5만원 이상 결제건에 적용하는 할부개월수. 0-일시불, 1은 지정시 에러 발생함, 2-2개월, 3-3개월... 12까지 지정가능
    public int cardInterest; // 웰컴페이먼츠 전용, 무이자여부를 보내는 파라미터가 있다
    public User user; // 구매자 정보, 특정 PG사의 경우 구매자 휴대폰 번호를 필수로 받는다
    public List<Item> items; // 구매할 상품정보, 통계용
    public String feedbackUrl; // webhook 통지시 받으실 url 주소 (localhost 사용 불가)
    public String feedbackContentType; // webhook 통지시 받으실 데이터 타입 (json 또는 urlencoded, 기본값 urlencoded)
    public Map<String, Object> metadata = new HashMap<>();
    public SubscribeExtra extra;
//    public String schedulerType; //정기결제 예약시 - oneshot 으로 지정해야함.
    /**
     * 1. UTC 시간을 사용할 경우 - ex) 2022-04-01 12:00:00 UTC(한국시간 기준 2022-04-01 21:00:00에 결제 진행)
     * 2. TIMEZONE을 사용할 경우 - ex) 2022-04-01T21:00:00 +0900(한국시간 기준 2022-04-01 21:00:00에 결제 진행)
     */
    public long reserveExecuteAt; //정기결제 예약시 결제 수행(예약) 시간
}
