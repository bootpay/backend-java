package kr.co.bootpay.store.model.request.invoice;


import kr.co.bootpay.store.model.pojo.SInvoiceItem;

import java.util.List;

public class InvoiceCreateParams {
    public String name;
    public String memo;
    public Integer productType;
    //상품형 아이템
    public List<String> selectedUsers;
    public List<SInvoiceItem> invoiceItems;
    //수기형 아이템 - 상품형 아이템이 없을 경우 이 정보들을 활용한다
    public Double price;
    public Boolean useMemo; //구매자로부터 메모를 받을 지
    public Boolean useRedirect;
    public String redirectUrl; //결제완료 후 redirect할 url 설정
    public String requestId; //가맹점이 관리하는 요청 ID
    public Boolean isOpenLink;
    public String expiredAt;
}
