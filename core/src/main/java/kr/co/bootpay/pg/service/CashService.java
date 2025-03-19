package kr.co.bootpay.pg.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kr.co.bootpay.http.HttpDeleteWithBody;
import kr.co.bootpay.pg.BootpayObject;
import kr.co.bootpay.pg.model.request.Cancel;
import kr.co.bootpay.pg.model.request.CashReceipt;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;

public class CashService {
    //현금 영수증 발행하기 (부트페이 결제건)
    //cash_receipt_publish_on_receipt

    static public HashMap<String, Object> requestCashReceiptByBootpay(BootpayObject bootpay, CashReceipt cashReceipt) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(cashReceipt.receiptId == null || cashReceipt.receiptId.isEmpty()) throw new Exception("receiptId 값을 입력해주세요.");
//        if(cashReceipt.user == null) throw new Exception("user 모델이 비어있습니다. 데이터를 채워주세요");
        if(cashReceipt.username == null || cashReceipt.username.isEmpty()) throw new Exception("username 값을 입력해주세요.");
        if(cashReceipt.phone == null || cashReceipt.phone.isEmpty()) throw new Exception("phone 값을 입력해주세요.");
        if(cashReceipt.identityNo == null || cashReceipt.identityNo.isEmpty()) throw new Exception("identityNo 값을 입력해주세요.");
        if(cashReceipt.cashReceiptType == null || cashReceipt.cashReceiptType.isEmpty()) throw new Exception("cashReceiptType 값을 입력해주세요.");

//        if(subscribe.subscriptionId == null || subscribe.subscriptionId.isEmpty()) throw new Exception("order_id 주문번호를 설정해주세요.");
//        if(subscribe.pg == null || subscribe.pg.isEmpty()) throw new Exception("결제하고자 하는 pg alias를 입력해주세요.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("request/receipt/cash/publish", new StringEntity(gson.toJson(cashReceipt), "UTF-8"));

        post.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(post);
        return bootpay.responseToJson(response);
    }


    //현금 영수증 발행 취소하기 (부트페이 결제건)
    //cash_receipt_cancel_on_receipt
    static public HashMap<String, Object> requestCashReceiptCancelByBootpay(BootpayObject bootpay, Cancel cancel) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(cancel == null || cancel.receiptId == null || cancel.receiptId.isEmpty()) throw new Exception("receiptId 값이 비어있습니다.");
        if(cancel == null || cancel.cancelUsername == null || cancel.cancelUsername.isEmpty()) throw new Exception("cancelUsername 값이 비어있습니다.");
        if(cancel == null || cancel.cancelMessage == null || cancel.cancelMessage.isEmpty()) throw new Exception("cancelMessage 값이 비어있습니다.");

//        if(subscribe.subscriptionId == null || subscribe.subscriptionId.isEmpty()) throw new Exception("order_id 주문번호를 설정해주세요.");
//        if(subscribe.pg == null || subscribe.pg.isEmpty()) throw new Exception("결제하고자 하는 pg alias를 입력해주세요.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpDeleteWithBody delete = bootpay.httpDeleteWithBody("request/receipt/cash/cancel/" + cancel.receiptId, new StringEntity(gson.toJson(cancel), "UTF-8"));

        delete.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(delete);
        return bootpay.responseToJson(response);
    }



    //현금 영수증 발행하기 (별건)
    static public HashMap<String, Object> requestCashReceipt(BootpayObject bootpay, CashReceipt cashReceipt) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(cashReceipt == null) throw new Exception("cashReceipt 모델이 비어있습니다. 데이터를 채워주세요");
        if(cashReceipt.pg == null || cashReceipt.pg.isEmpty()) throw new Exception("cashReceiptType 값을 입력해주세요.");
        if(cashReceipt.orderName == null || cashReceipt.orderName.isEmpty()) throw new Exception("orderName 값을 입력해주세요.");
        if(cashReceipt.orderId == null || cashReceipt.orderId.isEmpty()) throw new Exception("orderId 값을 입력해주세요.");
        if(cashReceipt.identityNo == null || cashReceipt.identityNo.isEmpty()) throw new Exception("identityNo 값을 입력해주세요.");
        if(cashReceipt.cashReceiptType == null || cashReceipt.cashReceiptType.isEmpty()) throw new Exception("cashReceiptType 값을 입력해주세요.");


//        if(subscribe.subscriptionId == null || subscribe.subscriptionId.isEmpty()) throw new Exception("order_id 주문번호를 설정해주세요.");
//        if(subscribe.pg == null || subscribe.pg.isEmpty()) throw new Exception("결제하고자 하는 pg alias를 입력해주세요.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        HttpPost post = bootpay.httpPost("request/cash/receipt", new StringEntity(gson.toJson(cashReceipt), "UTF-8"));

        post.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(post);
        return bootpay.responseToJson(response);
    }

    /*
   # 현금영수증 발행 취소 (별건)
    # Comment by Gosomi
    # Date: 2021-12-16
    def cancel_cash_receipt(receipt_id:, cancel_username:, cancel_message:)
     */
    static public HashMap<String, Object> requestCashReceiptCancel(BootpayObject bootpay, Cancel cancel) throws Exception {
        if(bootpay.token == null || bootpay.token.isEmpty()) throw new Exception("token 값이 비어있습니다.");
        if(cancel == null || cancel.receiptId == null || cancel.receiptId.isEmpty()) throw new Exception("receiptId 값이 비어있습니다.");
        if(cancel == null || cancel.cancelUsername == null || cancel.cancelUsername.isEmpty()) throw new Exception("cancelUsername 값이 비어있습니다.");
        if(cancel == null || cancel.cancelMessage == null || cancel.cancelMessage.isEmpty()) throw new Exception("cancelMessage 값이 비어있습니다.");

        HttpClient client = HttpClientBuilder.create().build();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HttpDeleteWithBody delete = bootpay.httpDeleteWithBody("request/cash/receipt/" + cancel.receiptId, new StringEntity(gson.toJson(cancel), "UTF-8"));



        delete.setHeader("Authorization", bootpay.getTokenValue());
        HttpResponse response = client.execute(delete);
        return bootpay.responseToJson(response);
    }

}
