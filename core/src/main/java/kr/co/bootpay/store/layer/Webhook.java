package kr.co.bootpay.store.layer;

import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.request.webhook.TestWebhookParams;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.service.webhook.SWebhookService;

public class Webhook {
    private final BootpayStore bootpay;

    public Webhook(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse sendTest() throws Exception {
        return SWebhookService.sendTestWebhook(bootpay);
    }

    public BootpayStoreResponse sendTest(TestWebhookParams params) throws Exception {
        return SWebhookService.sendTestWebhook(bootpay, params);
    }
}
