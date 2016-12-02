package com.qywenji.sale.module.userInfo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qywenji.sale.commons.service.BaseService;
import com.qywenji.sale.commons.utils.CookieUtil;
import com.qywenji.sale.commons.utils.RealIpUtil;
import com.qywenji.sale.commons.utils.RedisUtil;
import com.qywenji.sale.module.userInfo.bean.UserInfo;
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

    public UserInfo geByOpenId(String openId) {
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
                userInfo = this.geByOpenId(value);
                RedisUtil.set(userInfo.getOpenId(), JSONObject.toJSONString(userInfo), 60 * 60 * 24 * 1);
            }
        }
        if(userInfo == null){
            logger.info(String.format("***********userInfo is null*********** Path from %s,IP: from %s ",request.getRequestURI(), RealIpUtil.getIpAddr(request)));
        }
        return userInfo;
    }
}
