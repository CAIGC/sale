package com.qywenji.sale.module.order.bean;

/**
 * Created by CAI_GC on 2016/12/27.
 */
public class OrderInfo {

    private Integer id;
    /*订单编号*/
    private String orderCode;
    /*订单状态：0未支付，1：已支付，2：配送中，3：已完成*/
    private Byte status;
    /*订单总额*/
    private Float totalMoney;
    /*下单人openId*/
    private String openId;
    /*下单人昵称*/
    private String nickname;
    /*下单人电话*/
    private String phone;
    /*支付时间*/
    private String payTime;
    /*订单创建时间*/
    private String ctime;
    /*订单完成时间*/
    private String finishTime;
    /*微信支付回调id*/
    private String transactionId;
    /*支付方式，暂时只有微信支付*/
    private Byte payType;
}
