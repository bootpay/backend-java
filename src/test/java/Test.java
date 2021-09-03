
import com.google.gson.Gson;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.*;
import kr.co.bootpay.model.response.ResToken;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;


public class Test {
    static Bootpay bootpay;
    public static void main(String[] args) {
        bootpay = new Bootpay("5b8f6a4d396fa665fdc2b5ea", "rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=");

        System.out.println("2134");
        goGetToken();
        getBillingKey();
//        requestSubscribe();
//        reserveSubscribe();
//        destroyBillingKey();
//        reserveCancelSubscribe();
//        receiptCancel();
//        getUserToken();
//        requestLink();
//        submit();
//        goVerfity();
//        certificate();
    }

    public static void goGetToken() {
        try {
            HttpResponse res = bootpay.getAccessToken();

            String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            ResToken resToken = new Gson().fromJson(str, ResToken.class);
            System.out.println(resToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getBillingKey() {
        Subscribe subscribeBilling = new Subscribe();
        subscribeBilling.itemName = "정기결제 테스트 아이템";
        subscribeBilling.orderId = "" + (System.currentTimeMillis() / 1000);
        subscribeBilling.pg = "nicepay";
        subscribeBilling.cardNo = "5570**********1074"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribeBilling.cardPw = "**"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribeBilling.expireYear = "**"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribeBilling.expireMonth = "**"; //실제 테스트시에는 *** 마스크처리가 아닌 숫자여야 함
        subscribeBilling.identifyNumber = ""; //주민등록번호

        try {
            HttpResponse res = bootpay.getBillingKey(subscribeBilling);
            String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroyBillingKey() {
        try {
            HttpResponse res = bootpay.destroyBillingKey("6100e7ea0d681b001fd4de69");
            String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestSubscribe() {
        SubscribePayload payload = new SubscribePayload();
        payload.billingKey = "6100e8c80d681b001dd4e0d7";
        payload.itemName = "아이템01";
        payload.price = 1000;
        payload.orderId = "" + (System.currentTimeMillis() / 1000);


        try {
            HttpResponse res = bootpay.requestSubscribe(payload);
            String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reserveSubscribe() {
        SubscribePayload payload = new SubscribePayload();

        payload.billingKey = "6100e77a0d681b002ad4e5d9";
        payload.itemName = "아이템01";
        payload.price = 1000;
        payload.orderId = "" + (System.currentTimeMillis() / 1000);
        payload.executeAt = (System.currentTimeMillis() / 1000) + 10000;

        try {
            HttpResponse res = bootpay.reserveSubscribe(payload);
            String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reserveCancelSubscribe() {
        try {
            HttpResponse res = bootpay.reserveCancelSubscribe("6100e892019943002150fef3");
            String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void receiptCancel() {
        Cancel cancel = new Cancel();
        cancel.receiptId = "6100e77a019943003650f4d5";
        cancel.name = "관리자";
        cancel.reason = "테스트 결제";

//        String receipt_id = "";
        try {
            HttpResponse res = bootpay.receiptCancel(cancel);
            String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getUserToken() {
        UserToken userToken = new UserToken();
        try {
            HttpResponse res = bootpay.getUserToken(userToken);
            String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void requestLink() {
        Payload payload = new Payload();
        try {
            HttpResponse res = bootpay.requestLink(payload);
            String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void submit() {
        try {
            HttpResponse res = bootpay.submit("");
            String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void goVerfity() {
        try {
            HttpResponse res = bootpay.verify("6100e8e7019943003850f9b0");
            String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void certificate() {
        try {
            HttpResponse res = bootpay.certificate("593f8febe13f332431a8ddae");
            String str = IOUtils.toString(res.getEntity().getContent(), "UTF-8");
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
