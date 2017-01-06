package com.qywenji.sale.module.product.service;

import com.qywenji.sale.module.product.bean.ProductDto;
import org.springframework.stereotype.Service;

/**
 * Created by CAI_GC on 2016/12/28.
 */
@Service
public class ProductService {



    public ProductDto getItemByItemId(Integer itemId){
        ProductDto productDto = new ProductDto();

        /**伪数据*/
        productDto.setItemId(123);
        productDto.setProductId(8000000);
        productDto.setProductName("清远鸡");
        productDto.setItemName("家鸡1500g");
        productDto.setItemTypeName("规格");
        productDto.setProductNum("QYWJ-001");
        productDto.setMid(1);
        productDto.setShopId(1);
        productDto.setOnShelf(true);
        productDto.setPrice(20.0F);
        productDto.setItemImageUrl("http://pic.youzhipai.cn/pic/readPic?begin=277880501&end=278674681");

        /**伪数据*/

        return productDto;
    }
}
