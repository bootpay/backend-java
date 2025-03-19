package kr.co.bootpay.pg.model.request;
 
public class SubscribeExtra {
    public String cardQuota; //만원이상 결제 할 경우 카드 할부 개월수를 설정합니다. ex)  0,2,3,4,5,6
    public int subscribeTestPayment = 0;  // 100원 결제 후 결제가 되면 billing key를 발행, 결제가 실패하면 에러
    public int rawData = 0; //PG 오류 코드 및 메세지까지 리턴
}
