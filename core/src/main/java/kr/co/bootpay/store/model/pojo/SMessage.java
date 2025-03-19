package kr.co.bootpay.store.model.pojo;

import java.util.List;

public class SMessage {
    public String messageId;
    public String sellerId;
    public String projectId;
    public String userId;

    public String objectId;
    public Integer objectType;

    public Integer originMessageType;
    public Integer messageType;
    public String title;
    public String message;

    public String errorCode;
    public String errorMessage;

    public String reservedAt;
    public String executedAt;
    public String deliveredAt;
    public String syncedAt;

    public Integer traceCount;
    public Integer messageStatus;
    public Integer status;

    public String senderKey;
    public String nationCode;
    public String templateCode;
    public Boolean kakaoResend;
    public String resendMessage;
    public Integer resendMessageType;
    public String buttonMessage;

    public String to;
    public String from;
    public String sender;
    public List<String> recipients;
    public List<String> ccs;
    public List<String> bccs;
    public String subject;
    public String html;
    public String text;
    public List<String> attachments;

    public String email;
    public String emailPassword;
    public String smtpIp;
    public String smtpPort;
    public Boolean useEmailTls;
    public String messageSettingOption;

    public String jobId;
    public Integer msgId;
    public Integer originMsgId;
    public String host;

    public List<String> tags;
}

