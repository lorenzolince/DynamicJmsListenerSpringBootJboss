/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llh.jms.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author lorenzo
 */
@Component
public class SenderJms {

    private final Logger loger = LoggerFactory.getLogger(SenderJms.class);
    private final String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private String host;
    private String port;
    private String user;
    private String passsword;
    private String queeName;

    private JmsTemplate jmsTemplate;

    public void send(String text) throws Exception {
        try {
            InitialContext initialContext
                    = new InitialContext(createProperties());

            ConnectionFactory connectionFactory
                    = createConnectionFactory(initialContext);
            this.jmsTemplate = new JmsTemplate(connectionFactory);
            Map data = new HashMap<>();
            data.put("DATA", text);
            jmsTemplate.convertAndSend(queeName, data);
        } catch (NamingException | JmsException e) {
            loger.error("send: ",e);
        }
        loger.info("send <" + text + ">");

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

    public SenderJms setHost(String host) {
        this.host = host;
        return this;
    }

    public SenderJms setPort(String port) {
        this.port = port;
        return this;
    }

    public SenderJms setUser(String user) {
        this.user = user;
        return this;
    }

    public SenderJms setPasssword(String passsword) {
        this.passsword = passsword;
        return this;
    }

    public SenderJms setQueeName(String queeName) {
        this.queeName = queeName;
        return this;
    }
}
