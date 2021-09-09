package kr.co.bootpay.model.request;

import java.util.List;

public class Extra {
    public boolean escrow; // 에스크로 연동 시 true, 기본값 false
    public List<Integer> quota; // 결제금액이 5만원 이상시 할부개월 허용범위를 설정할 수 있음, ex) "0,2,3" 지정시 - [0(일시불), 2개월, 3개월] 허용, 미설정시 PG사별 기본값 적용, 1 지정시 에러가 발생할 수 있음
    public boolean dispCashResult; // 현금영수증 노출할지 말지 (가상계좌 이용시)
    public String offerPeriod; //통합결제창, PG 정기결제창에서 표시되는 '월 자동결제'에 해당하는 문구를 지정한 값으로 변경, 지원하는 PG사만 적용 가능
    public String theme; // 통합결제창 테마, [ red, purple(기본), custom ] 중 택 1
    public String customBackground;  // 통합결제창 배경색,  ex) "#00a086" theme가 custom 일 때 background 색상 지정 가능
    public String customFontColor; // 통합결제창 글자색,  ex) "#ffffff" theme가 custom 일 때 font color 색상 지정 가능
}
