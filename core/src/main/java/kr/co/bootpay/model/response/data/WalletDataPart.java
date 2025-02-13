package kr.co.bootpay.model.response.data;

public class WalletDataPart {
    public String wallet_id;
    public int type;
    public int sandbox; //0: live, 1:sandbox
    public int order;
    public int payment_status;

    public WalletBatchData batch_data;

    
}