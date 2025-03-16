package kr.co.bootpay.model.response;

import kr.co.bootpay.model.response.data.CardData;
import kr.co.bootpay.model.response.data.WalletDataPart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletResponseData {

    List<WalletDataPart> data;
    int http_status;
}