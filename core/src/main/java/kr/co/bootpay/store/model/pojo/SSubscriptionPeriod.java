package kr.co.bootpay.store.model.pojo;

import java.util.List;

public class SSubscriptionPeriod {
    public String subscriptionPeriodId; // ID 필드

    public int duration; // 구독기간 (의무약정기간)
    public int durationType; // 산출된 단위 (예: 연 단위)

    public Double costPrice; // 원가
    public Double displayPrice; // 판매가
    public Double taxFreePrice; // 비과세 판매가

    public boolean useDiscount; // 할인 사용 여부
    public Double discountPrice; // 할인가
    public int discountPriceType; // 할인가 타입 (1: 퍼센트, 2: 고정 가격)

    public boolean defaultField; // 기본 필드 여부

    public boolean useTimesBenefit; // 정기 구독 회차별 혜택 설정 여부
    public List<SSubscriptionTimesBenefit> subscriptionTimesBenefits; // 회차별 혜택 목록

    public Double expectedResidualValue; // 예상 잔존가치 (계약 종료 시점)
}
