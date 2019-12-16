/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llh.jms.consumer;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author lorenzolince
 */
@Component
public class TransmissionReceiver {

    private static Logger logger = LoggerFactory.getLogger(TransmissionReceiver.class);

    @JmsListener(id = "${spring.jms.id.container}", containerFactory = "dynamicJmsContainerFactory", destination = "${spring.jms.quee.name}")
    public void onMessage(Message msg) throws JMSException {
        System.out.println("################## onMessage ##############  ");
        MapMessage mapMessage = (MapMessage) msg;
        String data = mapMessage.getString("DATA");
        logger.info("  " + data);
    }
}
