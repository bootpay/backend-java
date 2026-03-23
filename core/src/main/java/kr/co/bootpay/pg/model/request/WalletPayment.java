package kr.co.bootpay.pg.model.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletPayment {
    public String userId;
    public String orderName;
    public Double price;
    public Double taxFree;
    public String orderId;
    public String webhookUrl;
    public String contentType;
    public List<Item> items;
    public User user;
    public SubscribeExtra extra;
    public Map<String, Object> metadata = new HashMap<>();
    public boolean sandbox;
}
