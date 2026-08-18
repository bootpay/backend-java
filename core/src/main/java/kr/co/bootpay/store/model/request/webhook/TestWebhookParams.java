package kr.co.bootpay.store.model.request.webhook;

public class TestWebhookParams {
    // 웹훅 수신시 사용할 Content-Type (application/json 또는 application/x-www-form-urlencoded)
    // null이면 전송하지 않으며 서버 기본값으로 발송된다
    public String headerContentType;
}
