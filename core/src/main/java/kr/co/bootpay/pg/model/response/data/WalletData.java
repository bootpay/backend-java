package kr.co.bootpay.pg.model.response.data;

import java.util.List;

public class WalletData {
    public WalletBatchData success;
    public List<WalletBatchData> failure;
}