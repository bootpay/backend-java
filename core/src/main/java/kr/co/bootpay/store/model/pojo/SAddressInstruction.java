package kr.co.bootpay.store.model.pojo;

import java.util.Map;

public class SAddressInstruction {
    public String addressInstructionId;
    public String userId;

    public String commonEntranceAccessNumber;
    public Boolean commonEntranceAccessAble;

    public Integer receiveDeliveryCommonType;
    public Integer receiveDeliveryDawnType;
    public Integer receiveDeliveryDawnSubType;
    public Map<String, Object> receiveDeliveryMetadata;

    public Integer receiveDawnPushType;
}
