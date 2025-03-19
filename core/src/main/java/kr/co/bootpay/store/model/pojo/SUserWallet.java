package kr.co.bootpay.store.model.pojo;

import java.util.Map;

public class SUserWallet {
    public String userWallet_id; // ID 필드
    public String user_id; // 사용자 ID
    public String userGroup_id; // 사용자 그룹 ID (optional)
    public String externalWallet_id; // 외부 지갑 ID
    public int priority = 0; // 우선순위 (0이면 기본 결제수단)
    public int type = 1; // 결제 수단 타입 (기본: 카드)
    public String name; // 결제 수단 별칭
    public boolean sandbox = false; // 샌드박스 여부
    public String billingKeyExpiredAt; // 빌링키 만료일 (String 변환)
    public Map<String, Object> billingData; // 카드 정보, 계좌정보 등
    public String hash; // 해시값
    public String latestPurchasedAt; // 마지막 결제일 (String 변환)
    public String revokedAt; // 마지막으로 삭제된 시각 (String 변환)
    public int status = 1; // 상태 (기본: 사용 가능)
}
