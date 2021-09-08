package kr.co.bootpay.model.request;
 
public class SubscribeExtra {
    public int subscribeTestPayment = 0;  // 100원 결제 후 결제가 되면 billing key를 발행, 결제가 실패하면 에러
    public int rawData = 0; //PG 오류 코드 및 메세지까지 리턴
}
