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
package org.kuali.rice.kew.mail.service.impl;

import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.mail.EmailContent;
import org.kuali.rice.coreservice.api.style.StyleService;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.util.CodeTranslator;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.feedback.web.FeedbackForm;
import org.kuali.rice.kew.mail.CustomEmailAttribute;
import org.kuali.rice.kew.mail.EmailStyleHelper;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.service.RouteHeaderService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.user.UserUtils;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.core.io.DefaultResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



/**
 * EmailContentService that serves EmailContent customizable via XSLT style sheets
 * The global email style name is: kew.email.style
 * If this style is not found, the resource 'defaultEmailStyle.xsl' will be retrieved
 * relative to this class.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class StyleableEmailContentServiceImpl extends BaseEmailContentServiceImpl {
    private static final Logger LOG = Logger.getLogger(StyleableEmailContentServiceImpl.class);

    protected final String DEFAULT_EMAIL_STYLESHEET_RESOURCE_LOC = "defaultEmailStyle.xsl";

    protected StyleService styleService;
    protected EmailStyleHelper styleHelper = new EmailStyleHelper();
    protected String globalEmailStyleSheet = KewApiConstants.EMAIL_STYLESHEET_NAME;

    protected RouteHeaderService routeHeaderService;

    public void setStyleService(StyleService styleService) {
        this.styleService = styleService;
    }

    public void setGlobalEmailStyleSheet(String globalEmailStyleSheet) {
        this.globalEmailStyleSheet = globalEmailStyleSheet;
    }

    protected static DocumentBuilder getDocumentBuilder(boolean coalesce) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setCoalescing(coalesce);
            return dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            String message = "Error constructing document builder";
            LOG.error(message, e);
            throw new WorkflowRuntimeException(message, e);
        }
    }

    protected static void addObjectXML(Document doc, Object o, Node node, String name) throws Exception {
        Element element = XmlHelper.propertiesToXml(doc, o, name);

        if (LOG.isDebugEnabled()) {
            LOG.debug(XmlJotter.jotNode(element));
        }

        if (node == null) {
            node = doc;
        }

        node.appendChild(element);
    }

    protected static void addTextElement(Document doc, Element baseElement, String elementName, Object elementData) {
        Element element = doc.createElement(elementName);
        String dataValue = "";
        if (elementData != null) {
        	dataValue = elementData.toString();
        }
        element.appendChild(doc.createTextNode(dataValue));
        baseElement.appendChild(element);
    }

    protected static void addCDataElement(Document doc, Element baseElement, String elementName, Object elementData) {
        Element element = doc.createElement(elementName);
        String dataValue = "";
        if (elementData != null) {
            dataValue = elementData.toString();
        }
        element.appendChild(doc.createCDATASection(dataValue));
        baseElement.appendChild(element);
    }

    protected static void addTimestampElement(Document doc, Element baseElement, String elementName, Date elementData) {
        addTextElement(doc, baseElement, elementName, RiceConstants.getDefaultDateFormat().format(elementData));
    }

    protected static void addDelegatorElement(Document doc, Element baseElement, ActionItem actionItem) {
        Element delegatorElement = doc.createElement("delegator");
        if ( (actionItem.getDelegatorPrincipalId() != null) && (actionItem.getDelegatorPrincipalId() != null) ) {
            // add empty delegator element
            baseElement.appendChild(delegatorElement);
            return;
        }
        String delegatorType = "";
        String delegatorId = "";
        String delegatorDisplayValue = "";
        if (actionItem.getDelegatorPrincipalId() != null) {
            delegatorType = "user";
            delegatorId = actionItem.getDelegatorPrincipalId();
            Principal delegator = KimApiServiceLocator.getIdentityService().getPrincipal(delegatorId);
            
            if (delegator == null) {
            	LOG.error("Cannot find user for id " + delegatorId);
            	delegatorDisplayValue = "USER NOT FOUND";
            } else {
            	delegatorDisplayValue = UserUtils.getTransposedName(GlobalVariables.getUserSession(), delegator);
            }
        } else if (actionItem.getDelegatorPrincipalId() != null) {
            delegatorType = "workgroup";
            delegatorId = actionItem.getDelegatorGroupId().toString();
            delegatorDisplayValue = KimApiServiceLocator.getGroupService().getGroup(actionItem.getDelegatorGroupId()).getName();
        }
        delegatorElement.setAttribute("type", delegatorType);
        // add the id element
        Element idElement = doc.createElement("id");
        idElement.appendChild(doc.createTextNode(delegatorId));
        delegatorElement.appendChild(idElement);
        // add the display value element
        Element displayValElement = doc.createElement("displayValue");
        displayValElement.appendChild(doc.createTextNode(delegatorDisplayValue));
        delegatorElement.appendChild(displayValElement);
        baseElement.appendChild(delegatorElement);
    }

    protected static void addWorkgroupRequestElement(Document doc, Element baseElement, ActionItem actionItem) {
        Element workgroupElement = doc.createElement("workgroupRequest");
        if (actionItem.getGroupId() != null) {
            // add the id element
            Element idElement = doc.createElement("id");
            idElement.appendChild(doc.createTextNode(actionItem.getGroupId()));
            workgroupElement.appendChild(idElement);
            // add the display value element
            Element displayValElement = doc.createElement("displayValue");
            displayValElement.appendChild(doc.createTextNode(actionItem.getGroupId()));
            workgroupElement.appendChild(displayValElement);
        }
        baseElement.appendChild(workgroupElement);
    }

    /**
     * This method is used to add the given {@link ActionItem} to the given {@link org.w3c.dom.Document} in a summarized
     * form for use in weekly or daily type reminder e-mails.
     *
     * @param doc - Document to have the ActionItem added to
     * @param actionItem - the action item being added
     * @param user - the current user
     * @param node - the node object to add the actionItem XML to (defaults to the doc variable if null is passed in)
     * @throws Exception
     */
    protected void addSummarizedActionItem(Document doc, ActionItem actionItem, Person user, Node node, DocumentRouteHeaderValue routeHeader) throws Exception {
        if (node == null) {
            node = doc;
        }

        Element root = doc.createElement("summarizedActionItem");

        // add in all items from action list as preliminary default dataset
        addTextElement(doc, root, "documentId", actionItem.getDocumentId());
        addTextElement(doc, root, "docName", actionItem.getDocName());
        addCDataElement(doc, root, "docLabel", actionItem.getDocLabel());
        addCDataElement(doc, root, "docTitle", actionItem.getDocTitle());
        //DocumentRouteHeaderValue routeHeader = getRouteHeader(actionItem);
        addTextElement(doc, root, "docRouteStatus", routeHeader.getDocRouteStatus());
        addCDataElement(doc, root, "routeStatusLabel", routeHeader.getRouteStatusLabel());
        addTextElement(doc, root, "actionRequestCd", actionItem.getActionRequestCd());
        addTextElement(doc, root, "actionRequestLabel", CodeTranslator.getActionRequestLabel(
                actionItem.getActionRequestCd()));
        addDelegatorElement(doc, root, actionItem);
        addTimestampElement(doc, root, "createDate", routeHeader.getCreateDate());
        addWorkgroupRequestElement(doc, root, actionItem);
        if (actionItem.getDateTimeAssigned() != null)
            addTimestampElement(doc, root, "dateAssigned", actionItem.getDateTimeAssigned().toDate());

        node.appendChild(root);
    }

    public DocumentRouteHeaderValue getRouteHeader(ActionItem actionItem) {
    	if (routeHeaderService == null) {
    		routeHeaderService = KEWServiceLocator.getRouteHeaderService();
    	}
        return routeHeaderService.getRouteHeader(actionItem.getDocumentId());
    }

    protected Map<String,DocumentRouteHeaderValue> getRouteHeaders(Collection<ActionItem> actionItems) {
    	if (routeHeaderService == null) {
    		routeHeaderService = KEWServiceLocator.getRouteHeaderService();
    	}
    	return routeHeaderService.getRouteHeadersForActionItems(actionItems);
    }
    
    protected static String transform(Templates style, Document doc) {
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        try {
            style.newTransformer().transform(new DOMSource(doc), result);
            return writer.toString();
        } catch (TransformerException te) {
            String message = "Error transforming DOM";
            LOG.error(message, te);
            throw new WorkflowRuntimeException(message, te);
        }
    }

    /**
     * This method retrieves the style from the system using the given name. If none is found the default style xsl file
     * defined by {@link #DEFAULT_EMAIL_STYLESHEET_RESOURCE_LOC} is used.
     *
     * @param styleName
     * @return a valid {@link javax.xml.transform.Templates} using either the given styleName or the default xsl style file
     */
    protected Templates getStyle(String styleName) {
        Templates style = null;
        try {
            style = styleService.getStyleAsTranslet(styleName);
        } catch (TransformerConfigurationException tce) {
            String message = "Error obtaining style '" + styleName + "', using default";
            LOG.error(message, tce);
            // throw new WorkflowRuntimeException("Error obtaining style '" + styleName + "'", tce);
        }

        if (style == null) {
            LOG.warn("Could not find specified style, " + styleName + ", using default");
            try {

                style = TransformerFactory.newInstance().newTemplates(new StreamSource(new DefaultResourceLoader().getResource("classpath:org/kuali/rice/kew/mail/" + DEFAULT_EMAIL_STYLESHEET_RESOURCE_LOC).getInputStream()));
            } catch (Exception tce) {
                String message = "Error obtaining default style from resource: " + DEFAULT_EMAIL_STYLESHEET_RESOURCE_LOC;
                LOG.error(message, tce);
                throw new WorkflowRuntimeException("Error obtaining style '" + styleName + "'", tce);
            }
        }
        return style;
    }

    protected EmailContent generateEmailContent(String styleName, Document doc) {
        Templates style = getStyle(styleName);
        return styleHelper.generateEmailContent(style, doc);
    }

    protected EmailContent generateReminderForActionItems(Person user, Collection<ActionItem> actionItems, String name, String style) {
        DocumentBuilder db = getDocumentBuilder(false);
        Document doc = db.newDocument();
        Element element = doc.createElement(name);
        Map<String,DocumentRouteHeaderValue> routeHeaders = getRouteHeaders(actionItems);
        
        setStandardAttributes(element);
        doc.appendChild(element);

        try {
            addObjectXML(doc, user, element, "user");
            for (ActionItem actionItem: actionItems) {
                try {
                    addSummarizedActionItem(doc, actionItem, user, element, routeHeaders.get(actionItem.getDocumentId()));
                } catch (Exception e) {
                    String message = "Error generating XML for action item: " + actionItem;
                    LOG.error(message, e);
                    throw new WorkflowRuntimeException(e);
                }
            }

        } catch (Exception e) {
            String message = "Error generating XML for action items: " + actionItems;
            LOG.error(message, e);
            throw new WorkflowRuntimeException(e);
        }

        return generateEmailContent(style, doc);
    }

    protected void setStandardAttributes(Element e) {
        e.setAttribute("env", getDeploymentEnvironment());
        e.setAttribute("applicationEmailAddress", getApplicationEmailAddress());
        e.setAttribute("actionListUrl", getActionListUrl());
        e.setAttribute("preferencesUrl", getPreferencesUrl());
    }

    /**
     * This method generates an {@link EmailContent} object using the given parameters.  Part of this operation includes
     * serializing the given {@link ActionItem} to XML. The following objects and methods are included in the serialization:
     *
     * <ul>
     * <li>{@link Person}</li>
     * <li>{@link Person#getPrincipalName()}</li>
     * <li>{@link DocumentRouteHeaderValue}</li>
     * <li>{@link DocumentRouteHeaderValue#getInitiatorUser()}</li>
     * <li>{@link DocumentRouteHeaderValue#getDocumentType()}</li>
     * <li>{@link Person}</li>
     * </ul>
     *
     * @param user - the current user
     * @param actionItem - the action item being added
     * @param documentType - the document type that the custom email style sheet will come from
     * @param node - the node object to add the actionItem XML to (defaults to the doc variable if null is passed in)
     * @throws Exception
     */
    @Override
	public EmailContent generateImmediateReminder(Person user, ActionItem actionItem, DocumentType documentType) {
        if (user != null) {
            LOG.info("Starting generation of immediate email reminder...");
       	    LOG.info("Action Id: " + actionItem.getId() +
    			 ";  ActionRequestId: " + actionItem.getActionRequestId() + 
    			 ";  Action Item Principal Id: " + actionItem.getPrincipalId());
            LOG.info("User Principal Id: " + user.getPrincipalId());
            // change style name based on documentType when configurable email style on document is implemented...
            String styleSheet = documentType.getCustomEmailStylesheet();
            LOG.debug(documentType.getName() + " style: " + styleSheet);
            if (styleSheet == null) {
                styleSheet = globalEmailStyleSheet;
            }

            LOG.info("generateImmediateReminder using style sheet: "+ styleSheet + " for Document Type " + documentType.getName());
            // return generateReminderForActionItems(user, actionItems, "immediateReminder", styleSheet);
            DocumentBuilder db = getDocumentBuilder(false);
            Document doc = db.newDocument();
            Element element = doc.createElement("immediateReminder");
            setStandardAttributes(element);
            doc.appendChild(element);

            try {
                addObjectXML(doc, user, element, "user");
                // addActionItem(doc, actionItem, user, node);
                Node node = element;
                if (node == null) {
                    node = doc;
                }

                Element root = doc.createElement("actionItem");
                // append the custom body and subject if they exist
                try {
                    CustomEmailAttribute customEmailAttribute = getCustomEmailAttribute(user, actionItem);
                    if (customEmailAttribute != null) {
                        String customBody = customEmailAttribute.getCustomEmailBody();
                        if (!org.apache.commons.lang.StringUtils.isEmpty(customBody)) {
                            Element bodyElement = doc.createElement("customBody");
                            bodyElement.appendChild(doc.createTextNode(customBody));
                            root.appendChild(bodyElement);
                        }
                        String customEmailSubject = customEmailAttribute.getCustomEmailSubject();
                        if (!org.apache.commons.lang.StringUtils.isEmpty(customEmailSubject)) {
                            Element subjectElement = doc.createElement("customSubject");
                            subjectElement.appendChild(doc.createTextNode(customEmailSubject));
                            root.appendChild(subjectElement);
                        }
                    }
                } catch (Exception e) {
                    LOG.error("Error when checking for custom email body and subject.", e);
                }
                Person person = KimApiServiceLocator.getPersonService().getPerson(actionItem.getPrincipalId());
                DocumentRouteHeaderValue header = getRouteHeader(actionItem);
                // keep adding stuff until we have all the xml we need to formulate the message :/
                addObjectXML(doc, actionItem, root, "actionItem");
                addObjectXML(doc, person, root, "actionItemPerson");
                addTextElement(doc, root, "actionItemPrincipalId", person.getPrincipalId());
                addTextElement(doc, root, "actionItemPrincipalName", person.getPrincipalName());
                addDocumentHeaderXML(doc, header, root, "doc");
                addObjectXML(doc, header.getInitiatorPrincipal(), root, "docInitiator");
                addTextElement(doc, root, "docInitiatorDisplayName", header.getInitiatorDisplayName());
                addObjectXML(doc, header.getDocumentType(), root, "documentType");

                node.appendChild(root);
            } catch (Exception e) {
                String message = "Error generating immediate reminder XML for action item: " + actionItem;
                LOG.error(message, e);
                throw new WorkflowRuntimeException(e);
            }
            LOG.info("Leaving generation of immeidate email reminder...");
            return generateEmailContent(styleSheet, doc);
        }
        LOG.info("Skipping generation of immediate email reminder due to the user being null");
        return null;
    }
    
    /**
     * This method handles converting the DocumentRouteHeaderValue into an XML representation.  The reason we can't just use
     * propertiesToXml like we have elsewhere is because the doc header has a String attached to it that has the XML document
     * content in it.  The default serialization of this will serialize this as a String so we will end up with escaped XML
     * in our output which we won't be able to process with the email stylesheet.  So we need to read the xml content from
     * the document and parse it into a DOM object so it can be appended to our output.
     */
    protected void addDocumentHeaderXML(Document document, DocumentRouteHeaderValue documentHeader, Node node, String elementName) throws Exception {
    	Element element = XmlHelper.propertiesToXml(document, documentHeader, elementName);
    	// now we need to "fix" the xml document content because it's going to be in there as escaped XML
    	Element docContentElement = (Element)element.getElementsByTagName("docContent").item(0);
    	String documentContent = docContentElement.getTextContent();
    	
    	if (!StringUtils.isBlank(documentContent) && documentContent.startsWith("<")) {
    		Document documentContentXML = XmlHelper.readXml(documentContent);
    		Element documentContentElement = documentContentXML.getDocumentElement();
    		documentContentElement = (Element)document.importNode(documentContentElement, true);
    	
    		// remove the old, bad text content
    		docContentElement.removeChild(docContentElement.getFirstChild());
    	
    		// replace with actual XML
    		docContentElement.appendChild(documentContentElement);
        } else {
            // in this case it means that the XML is encrypted, unfortunately, we have no way to decrypt it since
            // the key is stored in the client application.  We will just include the doc content since none of our
            // current IU clients will be using this feature right away

            // remove the old, bad text content
            docContentElement.removeChild(docContentElement.getFirstChild());
        }
    	
    	if (LOG.isDebugEnabled()) {
            LOG.debug(XmlJotter.jotNode(element));
        }

        node.appendChild(element);
    }

    @Override
	public EmailContent generateWeeklyReminder(Person user, Collection<ActionItem> actionItems) {
        return generateReminderForActionItems(user, actionItems, "weeklyReminder", globalEmailStyleSheet);
    }

    @Override
	public EmailContent generateDailyReminder(Person user, Collection<ActionItem> actionItems) {
        return generateReminderForActionItems(user, actionItems, "dailyReminder", globalEmailStyleSheet);
    }

    @Override
	public EmailContent generateFeedback(FeedbackForm form) {
        DocumentBuilder db = getDocumentBuilder(true);
        Document doc = db.newDocument();
        String styleSheet = globalEmailStyleSheet;

        // if the doc type is specified, see if that doc has a custom email stylesheet and use it
        // NOTE: do we need to do this for feedback? presumably feedback will be going back to admins
        /*String docTypeName = form.getDocumentType();
        if (!StringUtils.isBlank(docTypeName)) {
            DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName(docTypeName);
            if (docType == null) {
                LOG.error("User specified document type '" + docTypeName + "' in feedback form, but the document type was not found in the system");
            } else {
                if (docType.getCustomEmailStylesheet() != null) {
                    styleSheet = docType.getCustomEmailStylesheet();
                }
            }
        }*/
        LOG.info("form: " + form.getDocumentId());
        try {
            addObjectXML(doc, form, null, "feedback");
        } catch (Exception e) {
            String message = "Error generating XML for feedback form: " + form;
            LOG.error(message, e);
            throw new WorkflowRuntimeException(message, e);
        }
        setStandardAttributes(doc.getDocumentElement());

        return generateEmailContent(styleSheet, doc);
    }
}
