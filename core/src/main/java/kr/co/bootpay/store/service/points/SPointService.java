package kr.co.bootpay.store.service.points;

import kr.co.bootpay.store.BootpayStoreObject;
import kr.co.bootpay.store.model.request.point.PointTransactionsParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SPointService {

    static public BootpayStoreResponse balance(BootpayStoreObject bootpay) throws Exception {
        bootpay.requireCommerceCredentials();

        HttpGet get = bootpay.httpGet("point/balance");
        HttpResponse response = bootpay.execute(get);
        return bootpay.responseToJsonObject(response);
    }

    static public BootpayStoreResponse transactions(BootpayStoreObject bootpay, PointTransactionsParams params) throws Exception {
        bootpay.requireCommerceCredentials();

        String url = "point/transactions";
        if (params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            if (params.page != null) nameValuePairList.add(new BasicNameValuePair("page", params.page.toString()));
            if (params.limit != null) nameValuePairList.add(new BasicNameValuePair("limit", params.limit.toString()));
            if (params.transactionType != null) nameValuePairList.add(new BasicNameValuePair("transaction_type", params.transactionType.toString()));

            HttpGet get = bootpay.httpGet(url, nameValuePairList);
            HttpResponse response = bootpay.execute(get);
            return bootpay.responseToJsonObject(response);
        } else {
            HttpGet get = bootpay.httpGet(url);
            HttpResponse response = bootpay.execute(get);
            return bootpay.responseToJsonObject(response);
        }
    }
}
