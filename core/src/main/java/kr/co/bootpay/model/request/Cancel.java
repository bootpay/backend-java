package kr.co.bootpay.model.request;

public class Cancel {
    public String receiptId; // 부트페이에서 발급한 영수증 id
    public String name; // 취소 요청자 이름
    public String reason; // 취소 요청 사유
    public Double price; // (선택사항) 부분취소 요청시 금액을 지정, 미지정시 전액 취소 (부분취소가 가능한 PG사, 결제수단에 한해 적용됨)
    public Double taxFree; // 취소할 비과세 금액
    public String cancelId; // (선택사항) 부분취소 요청시 중복 요청을 방지하기 위한 고유값
    public RefundData refund; // (선택사항) 가상계좌 환불요청시, 전제조건으로 PG사와 CMS 특약이 체결되어 있을 경우에만 환불요청 가능, 기본적으로 가상계좌는 결제취소가 안됨
}
