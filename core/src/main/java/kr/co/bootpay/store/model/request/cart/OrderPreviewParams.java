package kr.co.bootpay.store.model.request.cart;

import java.util.List;

public class OrderPreviewParams {
    /** "guest" 또는 "member" */
    public String memberMode;
    public List<CartItemPayload> cartItems;
    public ShippingAddressPayload shippingAddress;
    public List<String> couponIds;
    public Double pointAmount;
    public String userGroupId;
}
