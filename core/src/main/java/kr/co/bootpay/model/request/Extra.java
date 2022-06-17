package kr.co.bootpay.model.request;

import java.util.ArrayList;
import java.util.List;

public class Extra {
    public String cardQuota; //카드 결제시 할부 기간 설정 (5만원 이상 구매시)
    public String sellerName; //노출되는 판매자명 설정
    public int deliveryDay = 1; //배송일자
    public String locale = "ko"; //결제창 언어지원
    public String offerPeriod; //결제창 제공기간에 해당하는 string 값, 지원하는 PG만 적용됨
    public boolean displayCashReceipt = true; // 현금영수증 보일지 말지.. 가상계좌 KCP 옵션
    public String depositExpiration; //가상계좌 입금 만료일자 설정, yyyy-MM-dd

    public String appScheme; //모바일 앱에서 결제 완료 후 돌아오는 옵션 ( 아이폰만 적용 )
    public boolean useCardPoint = true; //카드 포인트 사용 여부 (토스만 가능)
    public String directCard = ""; //해당 카드로 바로 결제창 (토스만 가능)

    public boolean useOrderId = false; //가맹점 order_id로 PG로 전송
    public boolean internationalCardOnly = false; //해외 결제카드 선택 여부 (토스만 가능)
    public String phoneCarrier;  //본인인증 시 고정할 통신사명, SKT,KT,LGT 중 1개만 가능
    public boolean directAppCard = false; //카드사앱으로 direct 호출
    public boolean directSamsungpay = false; //삼성페이 바로 띄우기
    public boolean testDeposit = false;  //가상계좌 모의 입금
    public boolean enableErrorWebhook = false;  //결제 오류시 Feedback URL로 webhook
    public boolean separatelyConfirmed = true; // confirm 이벤트를 호출할지 말지, false일 경우 자동승인
    public boolean confirmOnlyRestApi = false; // REST API로만 승인 처리
    public String openType = "redirect"; //페이지 오픈 type [iframe, popup, redirect] 중 택 1
    public boolean useBootpayInappSdk = true; //native app에서는 redirect를 완성도있게 지원하기 위한 옵션
    public String redirectUrl = "https://api.bootpay.co.kr/v2"; //open_type이 redirect일 경우 페이지 이동할 URL ( 오류 및 결제 완료 모두 수신 가능 )
    public boolean displaySuccessResult = false; // 결제 완료되면 부트페이가 제공하는 완료창으로 보여주기 ( open_type이 iframe, popup 일때만 가능 )
    public boolean displayErrorResult = true; // 결제가 실패하면 부트페이가 제공하는 실패창으로 보여주기 ( open_type이 iframe, popup 일때만 가능 )
    public int disposableCupDeposit = 0; //배달대행 플랫폼을 위한 컵 보증급 가격
    public BootExtraCardEasyOption cardEasyOption = new BootExtraCardEasyOption();
    public List<BrowserOpenType> browserOpenType = new ArrayList<>();
    public boolean useWelcomepayment = false; //웰컴 재판모듈 진행시 true
}
