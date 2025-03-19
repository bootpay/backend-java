package kr.co.bootpay.pg.model.request;

import java.util.HashMap;
import java.util.Map;

//현금영수증 모델
public class CashReceipt {
    public String receiptId; //부트페이에서 발급한 고유결제번호
    public String orderId; // 개발사에서 관리하는 고유결제번호
    public String orderName; // 결제할 상품명
    public String identityNo; //전화번호
    public String purchasedAt;
    public String cashReceiptType; //소득공제
    public double price;
    public double taxFree;
    public Map<String, Object> metadata = new HashMap<>();
    public Map<String, Object> extra = new HashMap<>();

    /* 구매자 정보 */
    public String username;
    public String email;
    public String phone;

    /* 별건 요청 파라미터 */
    public String pg;
    public User user;

}
