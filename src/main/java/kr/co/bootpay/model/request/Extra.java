package kr.co.bootpay.model.request;

import java.util.List;

public class Extra {
    public boolean escrow; // 에스크로 연동 시 true, 기본값 false
    public List<Integer> quota;
    public boolean subscribeTestPayment;
    public boolean dispCashResult;
    public boolean offerPeriod;
    public String sellerName;
    public String theme;
    public String customBackground;
    public String customFontColor;
}
