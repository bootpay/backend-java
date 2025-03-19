package kr.co.bootpay.store.model.pojo;

import java.util.List;
import java.util.Map;

public class SOrderSubscriptionHistoryData {
    public String orderSubscriptionHistoryDataId;
    public String orderName; // 주문명

    public int totalSubscriptionDuration; // 총회차
    public int status;
    public int renewalStatus;
    public int quantity; // 구독 수량

    public List<String> productOptionIds; // 구독 상품 옵션 ID 목록
    public List<String> productOptionSnapshotIds; // 구독 상품 옵션 스냅샷 ID 목록

    public String addressId; // 배송지 ID
    public Map<String, Object> userAddress; // 사용자 주소 정보 저장
    public String username; // 사용자 이름
    public String userPhone; // 사용자 전화번호
    public String userEmail; // 사용자 이메일

    public boolean useFreeTrial; // 무료 체험 여부
    public int freeTrialDay; // 무료 체험 기간 (일 단위)

    public String serviceStartAt; // 서비스 시작일
    public String serviceEndAt; // 서비스 종료일

    public int serviceDelayDay; // 서비스 연장일 (+: 이번 회차 미루기, -: 빨리 받기)

    public String paymentNextAt; // 다음 예상 결제일
    public String paymentNextCursorAt; // 현재까지 계산된 다음 결제일
}
