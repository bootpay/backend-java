package kr.co.bootpay.model.request;

import java.util.List;

public class Payload {
    public String pg; // [PG 결제] 사용하고자 하는 PG사의 Alias를 입력. ex) danal, kcp, inicis등, 미 지정시 통합결제창이 오픈
    public String method; //card:카드, phone: 휴대폰, bank: 실시간 계좌이체, vbank: 가상계좌, auth: 본인인증, card_rebill: 정기결제, easy: 카카오,페이코,네이버페이 등의 간편결제, 미지정시 통합결제창 오픈
    public List<String> methods; // 통합결제시 사용할 method 배열 형태
    public Double price; // 결제금액
    public String orderId; // 개발사에서 관리하는 고유결제번호
    public String params; // string 형태로 전달 할 값, 결제 후 똑같이 리턴해드림
    public Double taxFree; // 비과세 금액
    public String name; // 결제할 상품명
    public User user; // 구매자 정보
    public List<Item> items; // 상품정보
    public Extra extra; // 기타 옵션
}
