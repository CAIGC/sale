package com.qywenji.sale.module.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.qywenji.sale.commons.controller.BaseController;
import com.qywenji.sale.commons.utils.RedisUtil;
import com.qywenji.sale.module.userInfo.bean.UserInfo;
import com.qywenji.sale.module.userInfo.constant.UserInfoConstant;
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
 * Created by CAI_GC on 2016/12/2.
 */
@Controller
@Scope("prototype")
public class MenuDispatchController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(MenuDispatchController.class);

    @Autowired
    private WechatService wechatService;

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/wechat/menu/redirect")
    public void redirect(@RequestParam("tourl") String tourl, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String redirectUrl = request.getRequestURL().toString().replace(request.getRequestURI(),"") + "/wechat/menu/dispatch?tourl=" + tourl;
        response.sendRedirect(wechatService.getBaseCodeUrl(redirectUrl));
        return;
    }

    @RequestMapping(value = "/wechat/menu/dispatch")
    public String dispatch(String code, @RequestParam("tourl") String tourl, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String openId = wechatService.getOauthOpenIdnByCode(code);
        if (StringUtils.isBlank(openId)) {
            return null;
        }
        UserInfo userInfo = userInfoService.getByOpenId(openId);
        if (userInfo == null) {
            UserInfo user = wechatService.getSubscribeUserInfo(openId);
            if (user == null) {
                logger.info("获取已关注用户基本信息失败");
                return null;
            }else{
                userInfo = user;
                if(userInfo != null){
                    Map<String,Object> mqData = new HashMap<>();
                    mqData.put("type","subscribe");
                    mqData.put("data",JSONObject.toJSONString(userInfo));
                    UserInfoQueueMessageProducer.send(mqData);
                }
                /*Long lock = RedisUtil.increa(UserInfoConstant.LOCK + openId);
                RedisUtil.set(UserInfoConstant.LOCK + openId, lock + "", UserInfoConstant.LOCK_TIME);
                if(lock == 1){
                    userInfo =  userInfoService.save(user);
                    RedisUtil.set(UserInfoConstant.SUBSCRIBE_CONSUMER + openId, JSONObject.toJSONString(userInfo), UserInfoConstant.SUBSCRIBE_CONSUMER_TIME);
                }else{
                    String userInfoStr = RedisUtil.get(UserInfoConstant.SUBSCRIBE_CONSUMER);
                    if(StringUtils.isNotBlank(userInfoStr)){
                        userInfo = JSONObject.parseObject(userInfoStr, UserInfo.class);
                    }else{
                        userInfo = userInfoService.getByOpenId(openId);
                    }

                }*/
            }
        }
        userInfoService.setUserInfoInCookieAndRedis(request, response, userInfo);
        return "redirect:" + tourl;
    }

}
