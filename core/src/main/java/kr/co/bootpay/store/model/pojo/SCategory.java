package kr.co.bootpay.store.model.pojo;

import java.util.List;

public class SCategory {
    public String categoryId;
    public String sellerId;
    public String projectId;

    public Integer index;

    public String parentCategoryId;
    public List<String> parentCategories;
    public String name;

    public Boolean statusDisplay;
    public Boolean statusBest;

    public Integer filterColor;
    public Integer filterSize;
}
