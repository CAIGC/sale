package com.qywenji.sale.module.order.bean;

/**
 * Created by CAI_GC on 2016/12/27.
 */
public class OrderDetailInfo {

    private Integer id;
    /*订单编号*/
    private String orderCode;
    /*订单id*/
    private Integer orderId;
    /*商品单价*/
    private Float price;
    /*商品数量*/
    private Integer amount;
    /*商品编码*/
    private String itemCode;
    /*商品规格*/
    private String productSize;
    /*状态：0：未支付，1已支付*/
    private Byte status;
}
