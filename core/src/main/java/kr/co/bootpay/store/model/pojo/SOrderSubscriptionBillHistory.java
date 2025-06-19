package kr.co.bootpay.store.model.pojo;

public class SOrderSubscriptionBillHistory {
    public String orderSubscriptionBillHistoryId;
    public String sellerId;
    public String projectId;
    public String orderSubscriptionBillId;

    public String adminEmail; // 변경한 관리자 이메일 (내부용)
    public String adminId; // 변경한 관리자 ID (내부용)

    public String requester; // 요청자 이름 (API용)
    public String message; // 요청 메시지 (API용)

    public Double cancelPrice; // 취소된 금액, 음수일 경우 취소 원복
    public Double cancelTaxFreePrice; // 취소된 비과세 금액, 음수일 경우 취소 원복
    public Double cancelFee; // 취소 수수료 (예: 배송상품 환불/수거 비용)

    public int actionType; // 변경 유형
    public String actionAt; // 변경 발생 시간

    public int status; // 상태

    public boolean force; // 강제 실행 여부 (주로 관리자 요청)
    public String ip; // 요청한 IP 주소

    public SOrderSubscriptionHistoryData before; // 변경 전 데이터
    public SOrderSubscriptionHistoryData after; // 변경 후 데이터
}
