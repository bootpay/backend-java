package kr.co.bootpay.store.model.pojo;

import java.util.List;

public class SChosenProductOption {
    public String chosenProductOptionId;

    public Double totalOriginPrice;
    public Double totalPrice;
    public Double totalTaxFreePrice;

    public String productId;
    public String productSnapshotId;
    public String productName;

    public String name;
    public Double originPrice;
    public Double price;
    public Double taxFreePrice;
    public Double setupPrice;

    public Integer totalSubscriptionDuration;

    public String optionId;
    public String optionSnapshotId;

    public Integer productOptionType;
    public String productOptionSku;
    public List<String> productOptionName;
    public List<String> productOptionKeys;
    public Double productOptionPrice;
    public Double productOptionTaxFreePrice;

    public Integer quantity;
}
