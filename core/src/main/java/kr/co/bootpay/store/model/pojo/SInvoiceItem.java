package kr.co.bootpay.store.model.pojo;


public class SInvoiceItem {
    public Integer type; //1: price, 2:product
    public String productId;
    public String productOptionId;
    public String name;
    public String description;
    public double price;
    public double originPrice;
    public int quantity;
}

