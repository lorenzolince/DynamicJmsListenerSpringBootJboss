/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llh.jms.util;

import com.llh.jms.consumer.TransmissionReceiver;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 *
 * @author lorenzolince
 */
@Component
public class DynamicConsumer {

    private final String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private String host;
    private String port;
    private String user;
    private String passsword;
    private Long keyConection;
    private static Logger logger = LoggerFactory.getLogger(DynamicConsumer.class);

    private final ConcurrentHashMap<Long, DefaultMessageListenerContainer> closeConnectDynamicListener = new ConcurrentHashMap<>();

    @Autowired
    private JmsListenerEndpointRegistry registry;

    @Autowired
    AutowireCapableBeanFactory beanFactory;

    public void createDynamicJmsListener() {

        try {

            if (registry.getListenerContainer(host) == null) {
                System.setProperty("spring.jms.id.container", host);
                beanFactory.createBean(TransmissionReceiver.class, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
            }
            Properties prop = createProperties();
            InitialContext initialContext
                    = new InitialContext(prop);

            ConnectionFactory connectionFactory
                    = createConnectionFactory(initialContext);
            logger.info("connection: {}", prop.get(Context.PROVIDER_URL));

            DefaultMessageListenerContainer dynamic = (DefaultMessageListenerContainer) registry.getListenerContainer(host);
            dynamic.setConnectionFactory(connectionFactory);
            dynamic.initialize();
            dynamic.setClientId(host);
            dynamic.start();
            closeConnectDynamicListener.put(keyConection, dynamic);
        } catch (NamingException | BeansException | JmsException e) {
            logger.error("createDynamicJmsListener --> ", e);
        }

    }

    private Properties createProperties() {

        Properties properties = new Properties();

        properties.put(
                Context.INITIAL_CONTEXT_FACTORY,
                "org.jboss.naming.remote.client.InitialContextFactory");

        properties.put(
                Context.PROVIDER_URL,
                "http-remoting://" + host + ":" + port + "");
        properties.put(
                Context.SECURITY_PRINCIPAL,
                user);
        properties.put(
                Context.SECURITY_CREDENTIALS,
                passsword);

        return properties;

    }

    private ConnectionFactory createConnectionFactory(
            InitialContext initialContext)
            throws NamingException {

        return (ConnectionFactory) initialContext.lookup(CONNECTION_FACTORY);

    }

    public void closseConectionDynamicListener(Long key) {
        try {

            DefaultMessageListenerContainer conn = (DefaultMessageListenerContainer) closeConnectDynamicListener.get(key);
            logger.info("shutdown: {}", conn.getClientId());
            conn.shutdown();
            closeConnectDynamicListener.remove(key);
        } catch (JmsException ex) {
            logger.error("closseConectionDynamicListener --> ", ex);
        }
    }

    public DynamicConsumer setHost(String host) {
        this.host = host;
        return this;
    }

    public DynamicConsumer setPort(String port) {
        this.port = port;
        return this;
    }

    public DynamicConsumer setUser(String user) {
        this.user = user;
        return this;
    }

    public DynamicConsumer setPasssword(String passsword) {
        this.passsword = passsword;
        return this;
    }

    public DynamicConsumer setKeyConection(Long keyConection) {
        this.keyConection = keyConection;
        return this;
    }

    public ConcurrentHashMap<Long, DefaultMessageListenerContainer> getCloseConnectDynamicListener() {
        return closeConnectDynamicListener;
    }

}
