package com.qywenji.sale.module.userInfo.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qywenji.sale.module.userInfo.bean.UserInfo;
import com.qywenji.sale.module.userInfo.service.UserInfoService;
import org.activiti.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by CAI_GC on 2016/12/23.
 */
@Component
public class UserInfoQueueMessageListener implements MessageListener {
    private static Logger logger = LoggerFactory.getLogger(UserInfoQueueMessageListener.class);

    @Autowired
    private UserInfoService userInfoService;
    /**
     * 接收消息
     */
    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage mes = (TextMessage) message;
                String text = mes.getText();
                this.handleMes(text);
            } else {
                logger.info("*******此消息有问题！消息类型不是text数据*********");
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMes(String mes) {
        JSONObject jsonObject = JSON.parseObject(mes);
        String type = jsonObject.getString("type");
        String data = jsonObject.getString("data");
        switch (type) {
            case "subscribe":
                this.subscribeHandle(data);
                break;
            case "cancelSubscribe":
                this.cancelSubscribeHandle(data);
                break;
            default:
                logger.info("*******此消息有问题！*********" + mes);
        }

    }

    /**
     * 关注处理(1、关注保存用户信息，2、通过网页授权保存用户信息)
     * @param data
     */
    private void subscribeHandle(String data) {
        UserInfo userInfo = JSONObject.parseObject(data, UserInfo.class);
        userInfoService.checkAndSave(userInfo);
    }

    /**
     * 取消关注
     *
     * @param openId
     */
    private void cancelSubscribeHandle(String openId) {
        UserInfo userInfo = userInfoService.getByOpenId(openId);
        if(userInfo != null){
            userInfo.setSubscribe(0);
            userInfoService.save(userInfo);
        }
    }

}
