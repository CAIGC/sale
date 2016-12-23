package com.qywenji.sale.module.userInfo.mq;

import com.alibaba.fastjson.JSONObject;
import org.apache.activemq.command.ActiveMQDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by CAI_GC on 2016/12/23.
 */
@Component
public class UserInfoQueueMessageProducer {
    private static JmsTemplate jmsTemplate;
    private static ActiveMQDestination destination;

    @Resource(name="userInfoQueue")
    public  void setDestination(ActiveMQDestination destination) {
        UserInfoQueueMessageProducer.destination = destination;
    }
    @Autowired
    public  void setJmsTemplate(JmsTemplate jmsTemplate) {
        UserInfoQueueMessageProducer.jmsTemplate = jmsTemplate;
    }

    public static void send(Object object) {
        jmsTemplate.convertAndSend(destination, JSONObject.toJSONString(object));
    }
}
