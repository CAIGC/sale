package com.qywenji.sale.module.userInfo.bean;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by CAI_GC on 2016/11/30.
 */
@Entity
@Table
public class UserInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer subscribe = 0;
    private String openId;
    private String nickname;
    private Integer sex;
    private String city;
    private String province;
    private String country;
    private Long subscribeTime = 0L;
    private String headImgUrl;
    private Date ctime = new Date();

    public UserInfo() {
    }

    public UserInfo(Integer subscribe, String openId, String nickname, Integer sex, String city, String province, String country, Long subscribeTime, String headImgUrl) {
        this.subscribe = subscribe;
        this.openId = openId;
        this.nickname = nickname;
        this.sex = sex;
        this.city = city;
        this.province = province;
        this.country = country;
        this.subscribeTime = subscribeTime;
        this.headImgUrl = headImgUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Long subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }
}
