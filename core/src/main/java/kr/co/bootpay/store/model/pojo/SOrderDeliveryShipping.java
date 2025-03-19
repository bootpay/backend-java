package kr.co.bootpay.store.model.pojo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SOrderDeliveryShipping {
    public String orderDeliveryShippingId;
    public boolean isBundleShipping;
    public String shippingId;
    public String shippingSnapshotId;

    // 상품 관련 필드
    public List<String> productIds;
    public List<String> productSnapshotIds;
    public List<String> productOptionIds;
    public List<String> productOptionSnapshotIds;

    public double totalFee;
    public double baseFee;
    public double areaJejuFee;
    public double areaRemoteFee;

    public String productId;
    public String productSnapshotId;
    public String productName;
    public String optionId;
    public String optionSnapshotId;
    public String optionName;
    public int quantity;

    // 배송 관련 필드
    public LocalDateTime orderConfirmedAt;
    public LocalDateTime orderUnconfirmedAt;
    public LocalDateTime sentAt;
    public LocalDateTime deliveredAt;
    public LocalDateTime pickupAt;
    public LocalDateTime delayDispatchNotifiedAt;
    public LocalDateTime desiredDeliveryAt;
    public LocalDateTime deliveryDeadlineAt;

    public String deliveryCompanyCode;
    public int deliveryMethod;
    public int deliveryTrackingStatus;
    public String trackingNumber;
    public boolean isWrongTrackingNumber;
    public LocalDateTime wrongTrackingNumberAt;
    public String wrongTrackingNumberReason;
    public int deliveryStatus;

    // 생성자
}

