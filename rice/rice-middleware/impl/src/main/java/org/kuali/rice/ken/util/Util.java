/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.ken.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.framework.persistence.dao.GenericDao;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationChannelBo;
import org.kuali.rice.ken.bo.NotificationContentTypeBo;
import org.kuali.rice.ken.bo.NotificationPriorityBo;
import org.kuali.rice.ken.bo.NotificationProducerBo;
import org.kuali.rice.ken.bo.NotificationRecipientBo;
import org.kuali.rice.ken.bo.NotificationSenderBo;
import org.kuali.rice.ken.service.NotificationContentTypeService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * A general Utility class for the Notification system.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class Util {
    private static final Logger LOG = Logger.getLogger(Util.class);
    
    public static final java.lang.String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    public static final java.lang.String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    public static final NamespaceContext NOTIFICATION_NAMESPACE_CONTEXT
        = new ConfiguredNamespaceContext(Collections.singletonMap("nreq", "ns:notification/NotificationRequest"));

    private static final String ZULU_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final TimeZone ZULU_TZ = TimeZone.getTimeZone("UTC");

    private static final String CURR_TZ_FORMAT = "MM/dd/yyyy hh:mm a";
    
	private Util() {
		throw new UnsupportedOperationException("do not call");
	}

    /**
     * @return the name of the user configured to be the Notification system user
     */
    public static String getNotificationSystemUser() {
        String system_user = ConfigContext.getCurrentContextConfig().getProperty(NotificationConstants.KEW_CONSTANTS.NOTIFICATION_SYSTEM_USER_PARAM);
        if (system_user == null) {
            system_user = NotificationConstants.KEW_CONSTANTS.NOTIFICATION_SYSTEM_USER;
        }
        return system_user;
    }

    /**
     * Parses a date/time string under XSD dateTime type syntax
     * @see #ZULU_FORMAT
     * @param dateTimeString an XSD dateTime-formatted String
     * @return a Date representing the time value of the String parameter 
     * @throws ParseException if an error occurs during parsing 
     */
    public static Date parseXSDDateTime(String dateTimeString) throws ParseException {
            return createZulu().parse(dateTimeString);
    }

    /**
     * Formats a Date into XSD dateTime format
     * @param d the date value to format
     * @return date value formatted into XSD dateTime format
     */
    public static String toXSDDateTimeString(Date d) {
        return createZulu().format(d);
    }
    
    /**
     * Returns the current date formatted for the UI
     * @return the current date formatted for the UI
     */
    public static String getCurrentDateTime() {
        return toUIDateTimeString(new Date());
    }
    
    /**
     * Returns the specified date formatted for the UI
     * @return the specified date formatted for the UI
     */
    public static String toUIDateTimeString(Date d) {
        return createCurrTz().format(d);
    }

    /**
     * Parses the string in UI date time format
     * @return the date parsed from UI date time format
     */
    public static Date parseUIDateTime(String s) throws ParseException {
        return createCurrTz().parse(s);
    }

    /**
     * Returns a compound NamespaceContext that defers to the preconfigured notification namespace context
     * first, then delegates to the document prefix/namespace definitions second.
     * @param doc the Document to use for prefix/namespace resolution
     * @return  compound NamespaceContext
     */
    public static NamespaceContext getNotificationNamespaceContext(Document doc) {
        return new CompoundNamespaceContext(NOTIFICATION_NAMESPACE_CONTEXT, new DocumentNamespaceContext(doc));
    }

    /**
     * Returns an EntityResolver to resolve XML entities (namely schema resources) in the notification system
     * @param notificationContentTypeService the NotificationContentTypeService
     * @return an EntityResolver to resolve XML entities (namely schema resources) in the notification system
     */
    public static EntityResolver getNotificationEntityResolver(NotificationContentTypeService notificationContentTypeService) {
        return new CompoundEntityResolver(new ClassLoaderEntityResolver("schema", "notification"),
                                          new ContentTypeEntityResolver(notificationContentTypeService));
    }

    /**
     * transformContent - transforms xml content in notification to a string
     * using the xsl in the datastore for a given documentType
     * @param notification
     * @return
     */
    public static String transformContent(NotificationBo notification) {
        NotificationContentTypeBo contentType = notification.getContentType();
        String xsl = contentType.getXsl();
        
        LOG.debug("xsl: "+xsl);
        
        XslSourceResolver xslresolver = new XslSourceResolver();
        //StreamSource xslsource = xslresolver.resolveXslFromFile(xslpath);
        StreamSource xslsource = xslresolver.resolveXslFromString(xsl);
        String content = notification.getContent();
        LOG.debug("xslsource:"+xslsource.toString());
        
        String contenthtml = new String();
        try {
          ContentTransformer transformer = new ContentTransformer(xslsource);
          contenthtml = transformer.transform(content);
          LOG.debug("html: "+contenthtml);
        } catch (IOException ex) {
            LOG.error("IOException transforming document",ex);
        } catch (Exception ex) {
            LOG.error("Exception transforming document",ex);
        } 
        return contenthtml;
    }

    /**
     * This method uses DOM to parse the input source of XML.
     * @param source the input source
     * @param validate whether to turn on validation
     * @param namespaceAware whether to turn on namespace awareness
     * @return Document the parsed (possibly validated) document
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Document parse(final InputSource source, boolean validate, boolean namespaceAware, EntityResolver entityResolver) throws ParserConfigurationException, IOException, SAXException {
        // TODO: optimize this
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(validate);
        dbf.setNamespaceAware(namespaceAware);
        dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        DocumentBuilder db = dbf.newDocumentBuilder();
        if (entityResolver != null) {
            db.setEntityResolver(entityResolver);
        }
        db.setErrorHandler(new ErrorHandler() {
            public void warning(SAXParseException se) {
                LOG.warn("Warning parsing xml doc " + source, se);
            }
            public void error(SAXParseException se) throws SAXException {
                LOG.error("Error parsing xml doc " + source, se);
                throw se;
            }
            public void fatalError(SAXParseException se) throws SAXException {
                LOG.error("Fatal error parsing xml doc " + source, se);
                throw se;
            }
        });
        return db.parse(source);
    }

    /**
     * This method uses DOM to parse the input source of XML, supplying a notification-system-specific
     * entity resolver.
     * @param source the input source
     * @param validate whether to turn on validation
     * @param namespaceAware whether to turn on namespace awareness
     * @return Document the parsed (possibly validated) document
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Document parseWithNotificationEntityResolver(final InputSource source, boolean validate, boolean namespaceAware, NotificationContentTypeService notificationContentTypeService) throws ParserConfigurationException, IOException, SAXException {
        return parse(source, validate, namespaceAware, getNotificationEntityResolver(notificationContentTypeService));
    }

    /**
     * Returns a node child with the specified tag name of the specified parent node,
     * or null if no such child node is found. 
     * @param parent the parent node
     * @param name the name of the child node
     * @return child node if found, null otherwise
     */
    public static Element getChildElement(Node parent, String name) {
        NodeList childList = parent.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            Node node = childList.item(i);
            // we must test against NodeName, not just LocalName
            // LocalName seems to be null - I am guessing this is because
            // the DocumentBuilderFactory is not "namespace aware"
            // although I would have expected LocalName to default to
            // NodeName
            if (node.getNodeType() == Node.ELEMENT_NODE
                && (name.equals(node.getLocalName())
                   || name.equals(node.getNodeName()))) {
                return (Element) node;
            }
        }
        return null;
    }
    
    /**
     * This method will clone a given Notification object, one level deep, returning a fresh new instance 
     * without any references.
     * @param notification the object to clone
     * @return Notification a fresh instance
     */
    public static final NotificationBo cloneNotificationWithoutObjectReferences(NotificationBo notification) {
	NotificationBo clone = new NotificationBo();
	
	// handle simple data types first
        if(notification.getCreationDateTime() != null) {
            clone.setCreationDateTimeValue(new Timestamp(notification.getCreationDateTimeValue().getTime()));
        }
	if(notification.getAutoRemoveDateTime() != null) {
	    clone.setAutoRemoveDateTimeValue(new Timestamp(notification.getAutoRemoveDateTimeValue().getTime()));
	}
	clone.setContent(new String(notification.getContent()));
	clone.setDeliveryType(new String(notification.getDeliveryType()));
	if(notification.getId() != null) {
	    clone.setId(new Long(notification.getId()));
	}
	clone.setProcessingFlag(new String(notification.getProcessingFlag()));
	if(notification.getSendDateTimeValue() != null) {
	    clone.setSendDateTimeValue(new Timestamp(notification.getSendDateTimeValue().getTime()));
	}
	
        clone.setTitle(notification.getTitle());
        
	// now take care of the channel
	NotificationChannelBo channel = new NotificationChannelBo();
	channel.setId(new Long(notification.getChannel().getId()));
	channel.setName(new String(notification.getChannel().getName()));
	channel.setDescription(new String(notification.getChannel().getDescription()));
	channel.setSubscribable(new Boolean(notification.getChannel().isSubscribable()).booleanValue());
	clone.setChannel(channel);
	
	// handle the content type
	NotificationContentTypeBo contentType = new NotificationContentTypeBo();
	contentType.setId(new Long(notification.getContentType().getId()));
	contentType.setDescription(new String(notification.getContentType().getDescription()));
	contentType.setName(new String(notification.getContentType().getName()));
	contentType.setNamespace(new String(notification.getContentType().getNamespace()));
	clone.setContentType(contentType);
	
	// take care of the prioirity
	NotificationPriorityBo priority = new NotificationPriorityBo();
	priority.setDescription(new String(notification.getPriority().getDescription()));
	priority.setId(new Long(notification.getPriority().getId()));
	priority.setName(new String(notification.getPriority().getName()));
	priority.setOrder(new Integer(notification.getPriority().getOrder()));
	clone.setPriority(priority);
	
	// take care of the producer
	NotificationProducerBo producer = new NotificationProducerBo();
	producer.setDescription(new String(notification.getProducer().getDescription()));
	producer.setId(new Long(notification.getProducer().getId()));
	producer.setName(new String(notification.getProducer().getName()));
	producer.setContactInfo(new String(notification.getProducer().getContactInfo()));
	clone.setProducer(producer);
	
	// process the list of recipients now
	ArrayList<NotificationRecipientBo> recipients = new ArrayList<NotificationRecipientBo>();
	for(int i = 0; i < notification.getRecipients().size(); i++) {
	    NotificationRecipientBo recipient = notification.getRecipient(i);
	    NotificationRecipientBo cloneRecipient = new NotificationRecipientBo();
	    cloneRecipient.setRecipientId(new String(recipient.getRecipientId()));
	    cloneRecipient.setRecipientType(new String(recipient.getRecipientType()));
	    
	    recipients.add(cloneRecipient);
	}
	clone.setRecipients(recipients);
	
	// process the list of senders now
	ArrayList<NotificationSenderBo> senders = new ArrayList<NotificationSenderBo>();
	for(int i = 0; i < notification.getSenders().size(); i++) {
	    NotificationSenderBo sender = notification.getSender(i);
	    NotificationSenderBo cloneSender = new NotificationSenderBo();
	    cloneSender.setSenderName(new String(sender.getSenderName()));
	    
	    senders.add(cloneSender);
	}
	clone.setSenders(senders);
	
	return clone;
    }
    
    /**
     * This method generically retrieves a reference to foreign key objects that are part of the content, to get 
     * at the reference objects' pk fields so that those values can be used to store the notification with proper 
     * foreign key relationships in the database.
     * @param <T>
     * @param fieldName
     * @param keyName
     * @param keyValue
     * @param clazz
     * @param boDao
     * @return T
     * @throws IllegalArgumentException
     */
    public static <T> T retrieveFieldReference(String fieldName, String keyName, String keyValue, Class clazz, GenericDao boDao) throws IllegalArgumentException {
        LOG.debug(fieldName + " key value: " + keyValue);
        if (StringUtils.isBlank(keyValue)) {
            throw new IllegalArgumentException(fieldName + " must be specified in notification");
        }
        Map<String, Object> keys = new HashMap<String, Object>(1);
        keys.put(keyName, keyValue);
        T reference = (T) boDao.findByPrimaryKey(clazz, keys);
        if (reference == null) {
            throw new IllegalArgumentException(fieldName + " '" + keyValue + "' not found");
        }
        return reference;
    }

    /** date formats are not thread safe so creating a new one each time it is needed. */
    private static DateFormat createZulu() {
        final DateFormat df = new SimpleDateFormat(ZULU_FORMAT);
        df.setTimeZone(ZULU_TZ);
        return df;
    }

    /** date formats are not thread safe so creating a new one each time it is needed. */
    private static DateFormat createCurrTz() {
        return new SimpleDateFormat(CURR_TZ_FORMAT);
    }
}
