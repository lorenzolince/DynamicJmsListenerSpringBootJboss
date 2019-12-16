/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llh.jms.conf;

import javax.jms.ConnectionFactory;
import javax.jms.Session;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
//import org.springframework.transaction.jta.JtaTransactionManager;

/**
 *
 * @author lorenzolince
 */
@Configuration
@EnableJms
public class JmsAppConfiguration {

//    @Autowired
//    private JtaTransactionManager transactionManager;
    @Bean(name = "dynamicJmsContainerFactory")
    public JmsListenerContainerFactory<?> dynamicJmsContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionTransacted(true);
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("1-5");
        factory.setSessionAcknowledgeMode(Session.SESSION_TRANSACTED);
        factory.setAutoStartup(false);
//      factory.setTransactionManager(transactionManager);
        return factory;
    }

}
