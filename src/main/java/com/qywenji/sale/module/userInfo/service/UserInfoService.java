package com.qywenji.sale.module.userInfo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qywenji.sale.commons.service.BaseService;
import com.qywenji.sale.commons.utils.CookieUtil;
import com.qywenji.sale.commons.utils.RealIpUtil;
import com.qywenji.sale.commons.utils.RedisUtil;
import com.qywenji.sale.module.userInfo.bean.UserInfo;
import com.qywenji.sale.module.userInfo.constant.UserInfoConstant;
import com.qywenji.sale.module.userInfo.dao.UserInfoDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by CAI_GC on 2016/11/30.
 */
@Service
public class UserInfoService extends BaseService<UserInfo>{

    private final Logger logger = LoggerFactory.getLogger(UserInfoService.class);

    @Value("${user_cookie_name}")
    private String userInfoCookieName;

    @Autowired
    private UserInfoDao userInfoDao;

    public UserInfo getByOpenId(String openId) {
        return userInfoDao.getByOpenId(openId);
    }

    public void setUserInfoInCookieAndRedis(HttpServletRequest request,HttpServletResponse response,UserInfo userInfo){
        CookieUtil.setCookie(request, response, userInfoCookieName, userInfo.getOpenId());
        RedisUtil.set(userInfo.getOpenId(), JSONObject.toJSONString(userInfo), 60 * 60 * 24 * 1);
    }

    public UserInfo getUserInfoFromReids(HttpServletRequest request){
        UserInfo userInfo = null;
        String value = CookieUtil.getCookieByName(request,userInfoCookieName);
        if(StringUtils.isNotBlank(value)){
            String UserInfoStr = RedisUtil.get(value);
            if(StringUtils.isNotBlank(UserInfoStr)){
                userInfo = JSON.parseObject(UserInfoStr, UserInfo.class);
            }else{
                userInfo = this.getByOpenId(value);
                RedisUtil.set(userInfo.getOpenId(), JSONObject.toJSONString(userInfo), 60 * 60 * 24 * 1);
            }
        }
        if(userInfo == null){
            logger.info(String.format("***********userInfo is null*********** Path from %s,IP: from %s ",request.getRequestURI(), RealIpUtil.getIpAddr(request)));
        }
        return userInfo;
    }

    public void checkAndSave(UserInfo userInfo) {
        String openId = userInfo.getOpenId();
        UserInfo user = this.getByOpenId(openId);
        if(user == null){
            this.save(userInfo);
        }else{
            user.setNickname(userInfo.getNickname());
            user.setHeadImgUrl(userInfo.getHeadImgUrl());
            user.setSubscribe(userInfo.getSubscribe());
            user.setSubscribeTime(userInfo.getSubscribeTime());
            user.setSex(userInfo.getSex());
            user.setProvince(userInfo.getProvince());
            user.setCity(userInfo.getCity());
            user.setCountry(userInfo.getCountry());
            this.save(user);
        }
        RedisUtil.delByKey(userInfo.getOpenId());
        /*Boolean flag = false;
        if(user == null){
            Long lock = RedisUtil.increa(UserInfoConstant.LOCK + openId);
            RedisUtil.set(UserInfoConstant.LOCK + openId, lock + "", UserInfoConstant.LOCK_TIME);
            if (lock == 1) {
                this.save(userInfo);
                RedisUtil.set(UserInfoConstant.SUBSCRIBE_CONSUMER, JSONObject.toJSONString(userInfo), UserInfoConstant.SUBSCRIBE_CONSUMER_TIME);
            }else {
                String userInfoStr = RedisUtil.get(UserInfoConstant.SUBSCRIBE_CONSUMER);
                if (StringUtils.isNotBlank(userInfoStr)) {
                    userInfo = JSONObject.parseObject(userInfoStr, UserInfo.class);
                    if (userInfo.getId() == null) {
                        this.save(userInfo);
                        return;
                    }
                }
                user = this.getByOpenId(openId);
                if (user == null) {
                    this.save(userInfo);
                } else {
                    flag = true;
                }
            }
        }else {
            flag = true;
        }
        if (flag) {
            user.setNickname(userInfo.getNickname());
            user.setHeadImgUrl(userInfo.getHeadImgUrl());
            user.setSubscribe(userInfo.getSubscribe());
            user.setSubscribeTime(userInfo.getSubscribeTime());
            user.setSex(userInfo.getSex());
            user.setProvince(userInfo.getProvince());
            user.setCity(userInfo.getCity());
            user.setCountry(userInfo.getCountry());
            this.save(user);
        }*/
    }
}
