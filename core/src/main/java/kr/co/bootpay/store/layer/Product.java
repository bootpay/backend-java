package kr.co.bootpay.store.layer;


import kr.co.bootpay.store.BootpayStore;
import kr.co.bootpay.store.model.response.BootpayStoreResponse;
import kr.co.bootpay.store.model.pojo.SProduct;
import kr.co.bootpay.store.model.request.product.ProductListParams;
import kr.co.bootpay.store.model.request.product.ProductStatusParams;
import kr.co.bootpay.store.service.products.SProductService;

import java.net.URL;
import java.util.List;

public class Product {
    private final BootpayStore bootpay;

    public Product(BootpayStore bootpay) {
        this.bootpay = bootpay;
    }

    public BootpayStoreResponse list(ProductListParams params)  throws Exception {
        return SProductService.list(
                bootpay,
                params
        );
    }

    public BootpayStoreResponse create(SProduct product, List<URL> imagePaths)  throws Exception {
        return SProductService.create(
                bootpay,
                product,
                imagePaths
        );
    }

    public BootpayStoreResponse update(SProduct product)  throws Exception {
        return SProductService.update(
                bootpay,
                product
        );
    }

    public BootpayStoreResponse detail(String productId) throws Exception {
        return SProductService.detail(
                bootpay,
                productId
        );
    }

    public BootpayStoreResponse status(ProductStatusParams params) throws Exception {
        return SProductService.status(
                bootpay,
                params
        );
    }

    public BootpayStoreResponse delete(String productId) throws Exception {
        return SProductService.delete(
                bootpay,
                productId
        );
    }


}
