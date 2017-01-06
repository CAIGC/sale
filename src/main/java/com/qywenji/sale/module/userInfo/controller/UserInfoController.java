package com.qywenji.sale.module.userInfo.controller;

import com.qywenji.sale.commons.controller.BaseController;
import com.qywenji.sale.module.userInfo.bean.UserInfo;
import com.qywenji.sale.module.userInfo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;

/**
 * Created by CAI_GC on 2016/11/30.
 */
@Controller
@RequestMapping(value = "/userInfo")
public class UserInfoController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;

    @ResponseBody
    @RequestMapping(value = "/login")
    public Object setCookieForTest(HttpServletRequest request,HttpServletResponse response,String openId){
        UserInfo userInfo = userInfoService.getByOpenId(openId);
        if(userInfo != null){
            userInfoService.setUserInfoInCookieAndRedis(request,response,userInfo);
        }
        return super.success();
    }

    @RequestMapping(value = "/userCenter")
    public String userCenter(HttpServletRequest request,Model model) throws Exception{
        UserInfo userInfo = userInfoService.getUserInfoFromReids(request);
        model.addAttribute("userInfo",userInfo);
        return "/content/user_center/user_center";
    }
}
