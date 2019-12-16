/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llh.jms.controller;

import com.llh.jms.dto.ParamSenderMessages;
import com.llh.jms.dto.ParamCunsumerDto;
import com.llh.jms.util.DynamicConsumer;
import com.llh.jms.util.SenderJms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lorenzolince
 */
@RestController
@RequestMapping("/api/jms")
public class JmsController {

    private static Logger logger = LoggerFactory.getLogger(JmsController.class);
    @Autowired
    SenderJms SenderJms;
    @Autowired
    DynamicConsumer dynamicConsumer;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public void sendJms(@RequestBody ParamSenderMessages param) {
        logger.info("################ sendJms  ######################");
        try {

            SenderJms.setHost(param.getHost())
                    .setPort(param.getPort())
                    .setQueeName("jms.queue.transmissionQueue")
                    .setUser(param.getUser())
                    .setPasssword(param.getPassword())
                    .send(param.getMessages());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    @RequestMapping(value = "/JmsListener", method = RequestMethod.POST)
    public void creatConsumer2(@RequestBody ParamCunsumerDto param) {
        logger.info("################ consumer  ######################");
        try {

            dynamicConsumer.setHost(param.getHost())
                    .setPort(param.getPort())
                    .setKeyConection(param.getKey())
                    .setUser(param.getUser())//user-test
                    .setPasssword(param.getPassword()) //123456.
                    .createDynamicJmsListener();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    @RequestMapping(value = "/closeJmsListener", method = RequestMethod.GET)
    public void sendJms(@RequestParam(name = "key", required = true) Long key) {
        dynamicConsumer.closseConectionDynamicListener(key);
    }

    @RequestMapping(value = "/isRecovering", method = RequestMethod.GET)
    public void getConsumerActive(@RequestParam(name = "key", required = true) Long key) {
        logger.info("" + dynamicConsumer.getCloseConnectDynamicListener().get(key).isActive());
        logger.info("" + dynamicConsumer.getCloseConnectDynamicListener().get(key).isRecovering());
    }
}
