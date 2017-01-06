package com.qywenji.sale.module.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.qywenji.sale.commons.controller.BaseController;
import com.qywenji.sale.module.userInfo.bean.UserInfo;
import com.qywenji.sale.module.userInfo.mq.UserInfoQueueMessageProducer;
import com.qywenji.sale.module.userInfo.service.UserInfoService;
import com.qywenji.sale.module.wechat.service.WechatService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CAI_GC on 2016/12/1.
 */
@Controller
@Scope("prototype")
public class DispatchController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(MenuDispatchController.class);

    @Autowired
    private WechatService wechatService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * @param tourl
     * @param response
     * @param request
     * @throws IOException
     */
    @RequestMapping(value = "/wechat/notMenu/redirect")
    public void redirect(@RequestParam("tourl") String tourl, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String redirectUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + "/wechat/notMenu/dispatch?tourl=" + tourl;
        response.sendRedirect(wechatService.getBaseCodeUrl(redirectUrl));
        return;
    }

    @RequestMapping(value = "/wechat/notMenu/dispatch")
    public String dispatch(String code, @RequestParam("tourl") String tourl, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String openId = wechatService.getOauthOpenIdnByCode(code);
        if (StringUtils.isBlank(openId)) {
            return null;
        }
        UserInfo userInfo = userInfoService.getByOpenId(openId);
        if (userInfo == null) {
            String redirectUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + "/wechat/getUserInfo/dispatch?tourl=" + tourl;
            response.sendRedirect(wechatService.getUserInfoCodeUrl(redirectUrl));
            return null;
        }
        userInfoService.setUserInfoInCookieAndRedis(request, response, userInfo);
//        CookieUtil.setCookie(request, response, "user_id", userInfo.getOpenId());
        return "redirect:" + tourl;
    }

    @RequestMapping(value = "/wechat/getUserInfo/dispatch")
    public String getUserInfoByOauth2(String code, @RequestParam("tourl") String tourl, HttpServletRequest request, HttpServletResponse response) {
        UserInfo userInfo = wechatService.getNotSubscribeUserInfo(code);
        if (userInfo == null) {
            logger.info("网页授权获取用户基本信息失败");
            return null;
        } else {
            Map<String, Object> mqData = new HashMap<>();
            mqData.put("type", "subscribe");
            mqData.put("data", JSONObject.toJSONString(userInfo));
            UserInfoQueueMessageProducer.send(mqData);
        }
        userInfoService.setUserInfoInCookieAndRedis(request, response, userInfo);
        return "redirect:" + tourl;
    }


}
