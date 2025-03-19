package kr.co.bootpay.pg.model.response;

import kr.co.bootpay.pg.model.response.data.WalletDataPart;

import java.util.List;

public class WalletResponseData {

    List<WalletDataPart> data;
    int http_status;
}