package kr.co.bootpay.store.model.pojo;
import java.util.List;

public class SDeliveryShipping {
    public String deliveryShippingId;
    public String templateName;
    public boolean useParcel = true;
    public boolean useDirect = false;
    public boolean useQuick = false;
    public boolean usePickup = false;

    public List<Integer> attributeTypeArray;
    public String deliveryCompanyCode;

    public boolean useMade = false;
    public int deliveryAfter = 2;

    public boolean useBundleShipping;
    public List<String> quickServiceAreas;

    public int feeType; // Const.DELIVERY_FEE_TYPE_FREE
    public int feePayType; // Const.FEE_PAY_TYPE_PREPAID

    public double baseFee;
    public double areaJejuFee;
    public double areaRemoteFee;
    public double freeShippingThreshold;
    public double weightSurchargeThreshold;

    public int expectedDeliveryPeriodType;
    public String expectedDeliveryDirectInput;

    public String shippingAddressId;
    public String returnAddressId;
    public String pickupAddressId;

    public int claimReturnType; // Const.DELIVERY_METHOD_RETURN_INDIVIDUAL
    public double claimReturnDeliveryFee;
    public double claimExchangeDeliveryFee;
    public String claimReturnDeliveryCompanyCode;

    public int status; // Const.ABLE
}
