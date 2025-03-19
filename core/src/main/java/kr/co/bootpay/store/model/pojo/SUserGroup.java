package kr.co.bootpay.store.model.pojo;
import java.util.Map;

public class SUserGroup {
    public String userGroupId;
    public String sellerId;
    public String projectId;
    public Integer corporateType;

    public String bank;
    public String bankCode;

    public Integer count;
    public String lastUpdatedAt;
    public Integer status;

    public String phone;
    public String email;
    public String zipcode;
    public String address;
    public String addressDetail;
    public Map<String, Object> corporateExtension;
    public Boolean authBank;

    public String companyName;
    public String businessNumber;
    public String registrationNumber;
    public String corporateEstablished;
    public String businessType;
    public String businessCategory;
    public String ceoName;
    public Boolean authCompany;

    public String managerName;
    public String managerPhone;
    public String managerEmail;

    public String personalCustomsClearanceCode;

    public Double point;
    public Double accumulation;
    public Integer marketingAcceptType;
    public String marketingAcceptCreateAt;
    public String marketingAcceptUpdateAt;

    public Boolean useSubscriptionAggregateTransaction;
    public Integer subscriptionMonthDay;
    public Integer subscriptionWeekDay;

    public Boolean useLimit;
    public Double purchaseLimit;
    public Double subscribedLimit;
    public String limitMessage;
}

