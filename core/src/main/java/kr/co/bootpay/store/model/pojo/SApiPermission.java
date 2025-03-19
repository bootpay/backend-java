package kr.co.bootpay.store.model.pojo;

import java.util.List;

public class SApiPermission {
    public String apiPermissionId;
    public String name;
    public String url;
    public List<String> methods;
    public List<String> accessLevels;
    public Integer status;
}

