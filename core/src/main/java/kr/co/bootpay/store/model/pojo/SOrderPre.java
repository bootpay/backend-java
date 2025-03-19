package kr.co.bootpay.store.model.pojo;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

public class SOrderPre {
    private String orderPreId;
    private String orderId;
    private LocalDateTime expiredAt;
    private boolean useRedirect;
    private String redirectUrl;
    private String requestId;
    private String webhookUrl;
    private int headerContentType;
    private int webhookRetryCount;
    private int webhookStatus;
    private String errorData;
    private String testExtra;
    private List<SChosenProductOption> chosenProductOptions;

//    public SOrderPre() {
//        this.id = UUID.randomUUID().toString();
//        this.expiredAt = LocalDateTime.now().plusHours(2);
//        this.webhookStatus = Const.READY;
//    }
//
//    public boolean isValid() {
//        return orderId != null && !orderId.isEmpty();
//    }
//
//    public void updateExpiration(LocalDateTime newExpiration) {
//        this.expiredAt = newExpiration;
//    }
//
//    public void setWebhookStatus(int status) {
//        this.webhookStatus = status;
//    }

}

