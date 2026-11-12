package kr.co.bootpay.store.model.request.deposit;

/** 구매자 알림톡 옵션 (유료) — 하나라도 true 면 buyerPhone 필수, link 는 https 주소입니다. */
public class DepositNotifyParams {
    public Boolean depositGuide;
    public Boolean depositPaid;
    /** 알림톡 버튼 링크 (https, 500자 이하) */
    public String link;
}
