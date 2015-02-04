package org.kuali.ole.deliver.batch;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.mail.MailerImpl;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 4/23/13
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleMailer extends MailerImpl {

    protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleMailer.class);
    private static final String USERNAME_PROPERTY = "mail.smtp.username";
    private static final String PASSWORD_PROPERTY = "mail.smtp.password";
    private static final String HOST_PROPERTY = "mail.smtp.host";
    private static final String PORT_PROPERTY = "mail.smtp.port";
    private static final String MAIL_PREFIX = "mail";


    /**
     * Send an email to a single recipient with the specified subject and message. This is a convenience
     * method for simple message addressing.
     *
     * @param from    sender of the message
     * @param to      list of addresses to which the message is sent
     * @param subject subject of the message
     * @param body    body of the message
     */
    @Override
    public void sendEmail(EmailFrom from, EmailTo to, EmailSubject subject, EmailBody body, boolean htmlMessage) {
        JavaMailSenderImpl JavaMailSenderImpl = GlobalResourceLoader.getService("mailSender");
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
        LoanProcessor loanProcessor = new LoanProcessor();
        String host = loanProcessor.getParameter(HOST_PROPERTY);
        String port = loanProcessor.getParameter(PORT_PROPERTY);
        String userName = loanProcessor.getParameter(USERNAME_PROPERTY);
        String password = loanProcessor.getParameter(PASSWORD_PROPERTY);
        if (LOG.isDebugEnabled()){
            LOG.debug("Mail Parameters :" + "Host : " + host + " Port :" + port + " User Name :" + userName + " Password" + password);
        }
        if (host != null && !host.trim().isEmpty()) {
            JavaMailSenderImpl.setHost(host);
        } else if (host == null || (host != null && host.trim().isEmpty())) {
            host = properties.getProperty(HOST_PROPERTY);
            JavaMailSenderImpl.setHost(host);
        }


        if (port != null && !port.trim().isEmpty()) {
            JavaMailSenderImpl.setPort(Integer.parseInt(port));
        } else if (port == null || (port != null && port.trim().isEmpty())) {
            port = properties.getProperty(PORT_PROPERTY);
            if (port != null) {
                JavaMailSenderImpl.setPort(Integer.parseInt(port));
            }
        }


        if ((userName != null && !userName.trim().isEmpty()) || (password != null && !password.trim().isEmpty())) {
            JavaMailSenderImpl.setUsername(userName);
            JavaMailSenderImpl.setPassword(password);
        } else if ((userName == null || (userName != null && userName.trim().isEmpty())) || ((password == null || (password != null && password.trim().isEmpty())))) {
            userName = properties.getProperty(USERNAME_PROPERTY);
            password = properties.getProperty(PASSWORD_PROPERTY);
            JavaMailSenderImpl.setUsername(userName);
            JavaMailSenderImpl.setPassword(password);
        }

        if (to.getToAddress() == null) {
            LOG.warn("No To address specified. Refraining from sending mail.");
            return;
        }
        try {
            Address[] singleRecipient = {new InternetAddress(to.getToAddress())};
            super.sendMessage(from.getFromAddress(),
                    singleRecipient,
                    subject.getSubject(),
                    body.getBody(),
                    null,
                    null,
                    htmlMessage);
        } catch (Exception e) {
            LOG.error("Exception occured while sending the mail : " + e.getMessage());
            //throw new RuntimeException(e);
        }
    }


    public void  SendEMail(String toAddress, String fromAddress,List fileNameList,String subject,String messageBody) throws MessagingException {

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
        LoanProcessor loanProcessor = new LoanProcessor();
        String host = loanProcessor.getParameter(HOST_PROPERTY);
        String port = loanProcessor.getParameter(PORT_PROPERTY);
        String userName = loanProcessor.getParameter(USERNAME_PROPERTY);
        String password = loanProcessor.getParameter(PASSWORD_PROPERTY);
        if (LOG.isDebugEnabled()){
            LOG.debug("Mail Parameters :" + "Host : " + host + " Port :" + port + " User Name :" + userName + " Password" + password);
        }
        if (host != null && !host.trim().isEmpty()) {
            host = host;
        } else if (host == null || (host != null && host.trim().isEmpty())) {
            host = properties.getProperty(HOST_PROPERTY);
        }
        if (port != null && !port.trim().isEmpty()) {
            port = port;
        } else if (port == null || (port != null && port.trim().isEmpty())) {
            port = properties.getProperty(PORT_PROPERTY);
        }
        if ((userName != null && !userName.trim().isEmpty()) || (password != null && !password.trim().isEmpty())) {
            userName = userName;
            password = password;
        } else if ((userName == null || (userName != null && userName.trim().isEmpty())) || ((password == null || (password != null && password.trim().isEmpty())))) {
            userName = properties.getProperty(USERNAME_PROPERTY);
            password = properties.getProperty(PASSWORD_PROPERTY);
        }
        Properties props = System.getProperties();
        props.put(OLEConstants.SMTP_HOST, host);
        props.put(OLEConstants.SMTP_AUTH, properties.getProperty(OLEConstants.SMTP_AUTH));
        props.put(OLEConstants.SMTP_STARTTLS,properties.getProperty(OLEConstants.SMTP_STARTTLS));
        Session session = Session.getInstance(props, null);

        MimeMessage message = new MimeMessage(session);
        if (fromAddress != null) {
            message.setFrom(new InternetAddress(fromAddress));
        }
        if (toAddress != null) {
            message.setRecipients(Message.RecipientType.TO, toAddress);
        }
        message.setSubject(subject);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(messageBody);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        for (int i = 0; i < fileNameList.size(); i++) {
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource((String) fileNameList.get(i));
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName((String) fileNameList.get(i));
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
        }
        try {
            String transferProtocol = properties.getProperty(OLEConstants.TRANSPORT_PROTOCOL);
            Transport tr = session.getTransport(transferProtocol);
            tr.connect(host, Integer.parseInt(port), userName, password);
            tr.sendMessage(message, message.getAllRecipients());
            LOG.debug("Mail Sent Successfully");
            tr.close();
        } catch (SendFailedException sfe) {
            sfe.printStackTrace();
        }
    }

}
