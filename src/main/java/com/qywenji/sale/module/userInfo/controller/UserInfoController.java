package com.qywenji.sale.module.userInfo.controller;

import com.alibaba.fastjson.JSONObject;
import com.qywenji.sale.commons.controller.BaseController;
import com.qywenji.sale.module.userInfo.bean.UserInfo;
import com.qywenji.sale.module.userInfo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by CAI_GC on 2016/11/30.
 */
@Controller
@RequestMapping(value = "/userInfo")
public class UserInfoController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;


    /**
     * 微信关注或取消关注
     * @param userInfoJson
     * @return
     */
    @RequestMapping(value = "/wechat/handle")
    @ResponseBody
    public Object wxHandle(String userInfoJson){
        UserInfo userInfo = JSONObject.parseObject(userInfoJson,UserInfo.class);
        if(userInfo == null){
            return super.error("");
        }
        UserInfo user = userInfoService.getByOpenId(userInfo.getOpenId());
        if(user != null){
            user.setHeadImgUrl(userInfo.getHeadImgUrl());
            user.setNickname(userInfo.getNickname());
            user.setProvince(userInfo.getProvince());
            user.setCity(userInfo.getCity());
            user.setSex(userInfo.getSex());
            userInfoService.save(user);
        }else{
            userInfoService.save(userInfo);
        }
        return super.success();
    }
}
