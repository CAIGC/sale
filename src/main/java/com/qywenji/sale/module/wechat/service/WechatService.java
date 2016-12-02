package com.qywenji.sale.module.wechat.service;

import com.alibaba.fastjson.JSONObject;
import com.qywenji.sale.commons.utils.HttpConnHelper;
import com.qywenji.sale.module.userInfo.bean.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CAI_GC on 2016/12/1.
 */
@Service
public class WechatService {

    private final Logger logger = LoggerFactory.getLogger(WechatService.class);

    @Value("${getBaseCodeUrl}")
    private String baseCodeUrl;
    @Value("${getUserInfoCodeUrl}")
    private String userInfoCodeUrl;
    @Value("${getOauthAccessTokenJsonUrl}")
    private String getOauthAccessTokenJsonUrl;
    @Value("${getNotSubscribeUserInfoUrl}")
    private String getNotSubscribeUserInfoUrl;
    @Value("${getSubscribeUserInfoUrl}")
    private String getSubscribeUserInfoUrl;

    public String getBaseCodeUrl(String redirectUrl){
        String url=null;
        try {
            url = String.format(baseCodeUrl, URLEncoder.encode(redirectUrl, "UTF-8"));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return url;
    }

    public String getUserInfoCodeUrl(String redirectUrl) {
        String url=null;
        try {
            url = String.format(userInfoCodeUrl, URLEncoder.encode(redirectUrl, "UTF-8"));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return url;
    }

    /**
     *
     * @param code
     * @return
     */
    public JSONObject getOauthAccessTokenJsonByCode(String code) {
        JSONObject jsonObject=null;
        try {
            Map<String,String> postData = new HashMap<>();
            postData.put("code",code);
            jsonObject = (JSONObject)HttpConnHelper.getData(getOauthAccessTokenJsonUrl,postData);
            logger.info("网页授权获取accessTokenJson:{}",jsonObject.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getOauthOpenIdnByCode(String code) {
        JSONObject jsonObject = this.getOauthAccessTokenJsonByCode(code);
        if(jsonObject == null){
            return null;
        }
        return jsonObject.getString("openid");
    }


    public UserInfo getNotSubscribeUserInfo(String code) {
        String data=null;
        UserInfo userInfo;
        try {
            Map<String,String> postData = new HashMap<>();
            postData.put("code",code);
            data = HttpConnHelper.getData(getNotSubscribeUserInfoUrl,postData).toString();
            userInfo = JSONObject.parseObject(data,UserInfo.class);
        }catch (Exception e){
            logger.info("网页授权获取用户基本信息失败，data:{}",data);
            e.printStackTrace();
            return null;
        }
        return userInfo;
    }

    public UserInfo getSubscribeUserInfo(String openId){
        String data=null;
        UserInfo userInfo;
        try {
            Map<String,String> postData = new HashMap<>();
            postData.put("openId",openId);
            data = HttpConnHelper.getData(getSubscribeUserInfoUrl,postData).toString();
            userInfo = JSONObject.parseObject(data,UserInfo.class);
        }catch (Exception e){
            logger.info("获取关注用户基本信息失败，data:{}",data);
            e.printStackTrace();
            return null;
        }
        return userInfo;
    }
}
