package com.qywenji.sale.module.order.controller;

import com.qywenji.sale.commons.controller.BaseController;
import com.qywenji.sale.module.product.bean.ProductDto;
import com.qywenji.sale.module.product.service.ProductService;
import com.qywenji.sale.module.userInfo.bean.UserInfo;
import com.qywenji.sale.module.userInfo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by CAI_GC on 2016/12/27.
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseController {


    @Autowired
    private ProductService productService;

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/quick/{itemId}/{amount}")
    public String orderQuickly(@PathVariable Integer itemId,@PathVariable Integer amount){
        return "redirect:/order/viewOrderDetail?itemId="+itemId+"&amount="+amount;
    }
    @RequestMapping(value = "/viewOrderDetail")
    public String viewOrderDetail(Integer itemId,Integer amount,Model model){
        ProductDto productDto = productService.getProductDtoByItemId(itemId);
        model.addAttribute("product",productDto);
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/pay",method = RequestMethod.POST)
    public Object pay(HttpServletRequest request,Integer itemId,Integer amount,Integer couponId,String phone,Integer deliveryType){
        UserInfo userInfo = userInfoService.getUserInfoFromReids(request);
        ProductDto productDto = productService.getProductDtoByItemId(itemId);



        return super.success();
    }

    @ResponseBody
    @RequestMapping(value = "/getProductDtoByItemId")
    public Object getProductDtoByItemId(Integer itemId){
        ProductDto productDto = productService.getProductDtoByItemId(itemId);
        return super.success(productDto);
    }
}
