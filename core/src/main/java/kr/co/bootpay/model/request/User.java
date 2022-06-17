package kr.co.bootpay.model.request;


public class User {
    public String id; //개발사가 발급한 고유 아이디
    public String username;  // 구매자 명
    public String birth; //생년월일 "1986-10-14"
    public String email; //구매자의 이메일 정보
    public int gender = -1; //1:남자 0:여자
    public String area; // [서울,인천,대구,광주,부산,울산,경기,강원,충청북도,충북,충청남도,충남,전라북도,전북,전라남도,전남,경상북도,경북,경상남도,경남,제주,세종,대전] 중 택 1
    public String phone; //구매자의 전화번호 (페이앱 필수)
    public String addr;
}
