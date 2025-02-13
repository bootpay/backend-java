package kr.co.bootpay.model.response.data;

public class WalletBatchData {
    public String card_no;
    public String card_company;
    public String card_company_code;
    public int card_type; //0: 신용카드, 1: 체크카드
    public String bank_code;
    public String bank_account;
    public String bank_username;
}