package org.kuali.ole.deliver.batch;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 4/23/13
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleMailSenderFactoryBean extends AbstractFactoryBean {
    protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleMailSenderFactoryBean.class);
    private static final String MAIL_PREFIX = "mail";
    private static final String USERNAME_PROPERTY = "mail.smtp.username";
    private static final String PASSWORD_PROPERTY = "mail.smtp.password";
    private static final String HOST_PROPERTY = "mail.smtp.host";
    private static final String PORT_PROPERTY = "mail.smtp.port";
    private static final String PROTOCOL_PROPERTY = "mail.transport.protocol";
    private Session mailSession;
    private String host;
    private String port;
    private String userName;
    private String password;
    private LoanProcessor loanProcessor = new LoanProcessor();

    public LoanProcessor getLoanProcessor() {
        return loanProcessor;
    }

    public void setLoanProcessor(LoanProcessor loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected Object createInstance() throws Exception {
        // Retrieve "mail.*" properties from the configuration system and construct a Properties object
        Properties properties = new Properties();
        Properties configProps = ConfigContext.getCurrentContextConfig().getProperties();
        LOG.debug("createInstance(): collecting mail properties.");
        for (Object keyObj : configProps.keySet()) {
            if (keyObj instanceof String) {
                String key = (String) keyObj;
                if (key.startsWith(MAIL_PREFIX)) {
                    properties.put(key, configProps.get(key));
                }
            }
        }

        // Construct an appropriate Java Mail Session
        // If username and password properties are found, construct a Session with SMTP authentication


        if (host == null || (host != null && host.trim().isEmpty())) {
            host = properties.getProperty(HOST_PROPERTY);
        }
        if (port == null || (port != null && port.trim().isEmpty())) {
            port = properties.getProperty(PORT_PROPERTY);
        }
        if ((userName == null || (userName != null && userName.trim().isEmpty())) || ((password == null || (password != null && password.trim().isEmpty())))) {
            userName = properties.getProperty(USERNAME_PROPERTY);
            password = properties.getProperty(PASSWORD_PROPERTY);
        }



       /* String username = properties.getProperty(USERNAME_PROPERTY);
        String password = properties.getProperty(PASSWORD_PROPERTY);*/
        if (userName != null && password != null) {
            mailSession = Session.getInstance(properties, new SimpleAuthenticator(userName, password));
            LOG.debug("createInstance(): Initializing mail session using SMTP authentication.");
        } else {
            mailSession = Session.getInstance(properties);
            LOG.debug("createInstance(): Initializing mail session. No SMTP authentication.");
        }

        // Construct and return a Spring Java Mail Sender
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        LOG.debug("createInstance(): setting SMTP host.");
        mailSender.setHost(host);
        if (port != null) {
            LOG.debug("createInstance(): setting SMTP port.");
            int smtpPort = Integer.parseInt(port.trim());
            mailSender.setPort(smtpPort);
        }
        String protocol = properties.getProperty(PROTOCOL_PROPERTY);
        if (StringUtils.isNotBlank(protocol)) {
            if (LOG.isDebugEnabled()){
                LOG.debug("createInstance(  ): setting mail transport protocol = " + protocol);
            }
            mailSender.setProtocol(protocol);
        }
        mailSender.setSession(mailSession);

        LOG.debug("createInstance(): Mail Sender Factory Bean initialized.");
        return mailSender;
    }

    private class SimpleAuthenticator extends Authenticator {

        private final PasswordAuthentication passwordAuthentication;

        private SimpleAuthenticator(String username, String password) {
            this.passwordAuthentication = new PasswordAuthentication(username, password);
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return passwordAuthentication;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class getObjectType() {
        return JavaMailSenderImpl.class;
    }


}
