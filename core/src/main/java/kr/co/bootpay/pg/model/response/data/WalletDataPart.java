package kr.co.bootpay.pg.model.response.data;

public class WalletDataPart {
    public String wallet_id;
    public int type;
    public boolean sandbox; //0: live, 1:sandbox
    public int order;
    public int payment_status;

    public String card_code;


    public String expired_at;

    public String latest_purchased_at;
    public WalletBatchData batch_data;

    
}