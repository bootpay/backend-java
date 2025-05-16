package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.pojo.SProduct;
import kr.co.bootpay.store.model.pojo.SUserGroup;
import kr.co.bootpay.store.model.request.ListParams;
import kr.co.bootpay.store.service.products.SProductService;
import kr.co.bootpay.store.service.user_groups.SUserGroupService;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Product {
    private final BootpayStore bootpay;

    public Product(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public HashMap<String, Object> list(ListParams params)  throws Exception {
        return SProductService.list(
                bootpay,
                params
        );
    }

    public HashMap<String, Object> create(SProduct product, List<URL> imagePaths)  throws Exception {
        return SProductService.create(
                bootpay,
                product,
                imagePaths
        );
    }

    public HashMap<String, Object> update(SProduct product)  throws Exception {
        return SProductService.update(
                bootpay,
                product
        );
    }

    public HashMap<String, Object> detail(String productId) throws Exception {
        return SProductService.detail(
                bootpay,
                productId
        );
    }


}
