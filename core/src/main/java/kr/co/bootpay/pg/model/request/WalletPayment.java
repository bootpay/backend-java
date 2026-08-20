package kr.co.bootpay.pg.model.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @deprecated wallet 엔드포인트는 폐기 예정. 다음 메이저 버전에서 제거됩니다.
 * wallet_id + user_token 흐름으로 전환하세요.
 */
@Deprecated
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
