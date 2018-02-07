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
package org.kuali.rice.kew.dto;

import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.reflect.DataDefinition;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.reflect.PropertyDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.uif.RemotableAttributeErrorContract;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.api.document.DocumentContentUpdate;
import org.kuali.rice.kew.api.document.DocumentDetail;
import org.kuali.rice.kew.api.document.InvalidDocumentContentException;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.extension.ExtensionUtils;
import org.kuali.rice.kew.definition.AttributeDefinition;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.framework.document.attribute.SearchableAttribute;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.StandardDocumentContent;
import org.kuali.rice.kew.rule.WorkflowRuleAttribute;
import org.kuali.rice.kew.rule.WorkflowAttributeXmlValidator;
import org.kuali.rice.kew.rule.XmlConfiguredAttribute;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.xmlrouting.GenericXMLRuleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Translates Workflow server side beans into client side VO beans.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DTOConverter {
    private static final Logger LOG = Logger.getLogger(DTOConverter.class);

    public static String buildUpdatedDocumentContent(String existingDocContent,
            DocumentContentUpdate documentContentUpdate, String documentTypeName) {
        if (existingDocContent == null) {
            existingDocContent = KewApiConstants.DEFAULT_DOCUMENT_CONTENT;
        }
        String documentContent = KewApiConstants.DEFAULT_DOCUMENT_CONTENT;
        StandardDocumentContent standardDocContent = new StandardDocumentContent(existingDocContent);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement(KewApiConstants.DOCUMENT_CONTENT_ELEMENT);
            document.appendChild(root);
            Element applicationContentElement = standardDocContent.getApplicationContent();
            if (documentContentUpdate.getApplicationContent() != null) {
                // application content has changed
                if (!StringUtils.isEmpty(documentContentUpdate.getApplicationContent())) {
                    applicationContentElement = document.createElement(KewApiConstants.APPLICATION_CONTENT_ELEMENT);
                    XmlHelper.appendXml(applicationContentElement, documentContentUpdate.getApplicationContent());
                } else {
                    // they've cleared the application content
                    applicationContentElement = null;
                }
            }
            Element attributeContentElement = createDocumentContentSection(document,
                    standardDocContent.getAttributeContent(), documentContentUpdate.getAttributeDefinitions(),
                    documentContentUpdate.getAttributeContent(), KewApiConstants.ATTRIBUTE_CONTENT_ELEMENT,
                    documentTypeName);
            Element searchableContentElement = createDocumentContentSection(document,
                    standardDocContent.getSearchableContent(), documentContentUpdate.getSearchableDefinitions(),
                    documentContentUpdate.getSearchableContent(), KewApiConstants.SEARCHABLE_CONTENT_ELEMENT,
                    documentTypeName);
            if (applicationContentElement != null) {
                root.appendChild(applicationContentElement);
            }
            if (attributeContentElement != null) {
                root.appendChild(attributeContentElement);
            }
            if (searchableContentElement != null) {
                root.appendChild(searchableContentElement);
            }
            documentContent = XmlJotter.jotNode(document);
        } catch (ParserConfigurationException e) {
            throw new RiceRuntimeException("Failed to initialize XML parser.", e);
        } catch (SAXException e) {
            throw new InvalidDocumentContentException("Failed to parse XML.", e);
        } catch (IOException e) {
            throw new InvalidDocumentContentException("Failed to parse XML.", e);
        } catch (TransformerException e) {
            throw new InvalidDocumentContentException("Failed to parse XML.", e);
        }
        return documentContent;
    }

    private static Element createDocumentContentSection(Document document, Element existingAttributeElement,
            List<WorkflowAttributeDefinition> definitions, String content, String elementName,
            String documentTypeName) throws TransformerException, SAXException, IOException, ParserConfigurationException {
        Element contentSectionElement = existingAttributeElement;
        // if they've updated the content, we're going to re-build the content section element from scratch
        if (content != null) {
            if (!org.apache.commons.lang.StringUtils.isEmpty(content)) {
                contentSectionElement = document.createElement(elementName);
                // if they didn't merely clear the content, let's build the content section element by combining the children
                // of the incoming XML content
                Element incomingAttributeElement = XmlHelper.readXml(content).getDocumentElement();
                NodeList children = incomingAttributeElement.getChildNodes();
                for (int index = 0; index < children.getLength(); index++) {
                    contentSectionElement.appendChild(document.importNode(children.item(index), true));
                }
            } else {
                contentSectionElement = null;
            }
        }
        // if they have new definitions we're going to append those to the existing content section
        if (definitions != null && !definitions.isEmpty()) {
            String errorMessage = "";
            boolean inError = false;
            if (contentSectionElement == null) {
                contentSectionElement = document.createElement(elementName);
            }
            for (WorkflowAttributeDefinition definitionVO : definitions) {
                AttributeDefinition definition = convertWorkflowAttributeDefinition(definitionVO);
                ExtensionDefinition extensionDefinition = definition.getExtensionDefinition();

                Object attribute = null;
                attribute = GlobalResourceLoader.getObject(definition.getObjectDefinition());
                if (attribute == null) {
                    attribute = GlobalResourceLoader.getService(QName.valueOf(
                            definition.getExtensionDefinition().getResourceDescriptor()));
                }

                if (attribute instanceof XmlConfiguredAttribute) {
                    ((XmlConfiguredAttribute)attribute).setExtensionDefinition(definition.getExtensionDefinition());
                }
                boolean propertiesAsMap = false;
                if (KewApiConstants.RULE_XML_ATTRIBUTE_TYPE.equals(extensionDefinition.getType())) {
                    propertiesAsMap = true;
                }
                if (propertiesAsMap) {
                    for (org.kuali.rice.kew.api.document.PropertyDefinition propertyDefinitionVO : definitionVO
                            .getPropertyDefinitions()) {
                        if (attribute instanceof GenericXMLRuleAttribute) {
                            ((GenericXMLRuleAttribute) attribute).getParamMap().put(propertyDefinitionVO.getName(),
                                    propertyDefinitionVO.getValue());
                        }
                    }
                }

                // validate inputs from client application if the attribute is capable
                if (attribute instanceof WorkflowAttributeXmlValidator) {
                    List<? extends RemotableAttributeErrorContract> errors =
                            ((WorkflowAttributeXmlValidator) attribute).validateClientRoutingData();
                    if (!errors.isEmpty()) {
                        inError = true;
                        errorMessage += "Error validating attribute " + definitionVO.getAttributeName() + " ";
                        errorMessage += Joiner.on("; ").join(Iterables.transform(errors, Functions.toStringFunction()));
                    }
                }
                // dont add to xml if attribute is in error
                if (!inError) {
                    if (attribute instanceof WorkflowRuleAttribute) {
                        String attributeDocContent = ((WorkflowRuleAttribute) attribute).getDocContent();
                        if (!StringUtils.isEmpty(attributeDocContent)) {
                            XmlHelper.appendXml(contentSectionElement, attributeDocContent);
                        }
                    } else if (attribute instanceof SearchableAttribute) {
                        SearchableAttribute searchableAttribute = (SearchableAttribute) attribute;
                        String searchableAttributeContent = searchableAttribute.generateSearchContent(extensionDefinition, documentTypeName,
                                definitionVO);
                        if (!StringUtils.isBlank(searchableAttributeContent)) {
                            XmlHelper.appendXml(contentSectionElement, searchableAttributeContent);
                        }
                    }
                }
            }
            if (inError) {
                throw new WorkflowRuntimeException(errorMessage);
            }

        }
        if (contentSectionElement != null) {
            // always be sure and import the element into the new document, if it originated from the existing doc content
            // and
            // appended to it, it will need to be imported
            contentSectionElement = (Element) document.importNode(contentSectionElement, true);
        }
        return contentSectionElement;
    }

    /**
     * New for Rice 2.0
     */
    public static AttributeDefinition convertWorkflowAttributeDefinition(WorkflowAttributeDefinition definition) {
        if (definition == null) {
            return null;
        }
        //KULRICE-7643
        ExtensionDefinition extensionDefinition = null;
        List<RuleAttribute> ruleAttribute = KEWServiceLocator.getRuleAttributeService().findByClassName(definition.getAttributeName());
        if (ruleAttribute == null || ruleAttribute.isEmpty()) {
            extensionDefinition = KewApiServiceLocator.getExtensionRepositoryService().getExtensionByName(definition.getAttributeName());
        }else{
            //TODO: Should we do something more intelligent here? Rice 1.x returned only a single entry but we can now have a list
            RuleAttribute tmpAttr = ruleAttribute.get(0);
            extensionDefinition = RuleAttribute.to(tmpAttr);
            if(ruleAttribute.size() > 1){
                LOG.warn("AttributeDefinition lookup (findByClassName) returned multiple attribute for the same class name. This should not happen, investigation recommended for classname: " 
            + definition.getAttributeName() + " which has " + ruleAttribute.size() + " entries.");
            }
        }
        
        if (extensionDefinition == null) {
            throw new WorkflowRuntimeException("Extension " + definition.getAttributeName() + " not found");
        }
        /*RuleAttribute ruleAttribute = KEWServiceLocator.getRuleAttributeService().findByName(definition.getAttributeName());
        if (ruleAttribute == null) {
            throw new WorkflowRuntimeException("Attribute " + definition.getAttributeName() + " not found");
        }*/

        ObjectDefinition objectDefinition = new ObjectDefinition(extensionDefinition.getResourceDescriptor());
        if (definition.getParameters() != null) {
            for (String parameter : definition.getParameters()) {
                objectDefinition.addConstructorParameter(new DataDefinition(parameter, String.class));
            }
        }
        boolean propertiesAsMap = KewApiConstants.RULE_XML_ATTRIBUTE_TYPE.equals(extensionDefinition.getType()) || KewApiConstants
                .SEARCHABLE_XML_ATTRIBUTE_TYPE.equals(extensionDefinition.getType());
        if (!propertiesAsMap && definition.getPropertyDefinitions() != null) {
            for (org.kuali.rice.kew.api.document.PropertyDefinition propertyDefinition : definition
                    .getPropertyDefinitions()) {
                objectDefinition.addProperty(new PropertyDefinition(propertyDefinition.getName(), new DataDefinition(
                        propertyDefinition.getValue(), String.class)));
            }
        }

        return new AttributeDefinition(extensionDefinition, objectDefinition);
    }

    /**
     * Interface for a simple service providing RouteNodeInstanceS based on their IDs
     */
    public static interface RouteNodeInstanceLoader {
        RouteNodeInstance load(String routeNodeInstanceID);
    }


    public static DocumentDetail convertDocumentDetailNew(DocumentRouteHeaderValue routeHeader) {
        if (routeHeader == null) {
            return null;
        }
        org.kuali.rice.kew.api.document.Document document = DocumentRouteHeaderValue.to(routeHeader);
        DocumentDetail.Builder detail = DocumentDetail.Builder.create(document);
        Map<String, RouteNodeInstance> nodeInstances = new HashMap<String, RouteNodeInstance>();
        List<ActionRequest> actionRequestVOs = new ArrayList<ActionRequest>();
        List<ActionRequestValue> rootActionRequests = KEWServiceLocator.getActionRequestService().getRootRequests(
                routeHeader.getActionRequests());
        for (Iterator<ActionRequestValue> iterator = rootActionRequests.iterator(); iterator.hasNext(); ) {
            ActionRequestValue actionRequest = iterator.next();
            actionRequestVOs.add(ActionRequestValue.to(actionRequest));
            RouteNodeInstance nodeInstance = actionRequest.getNodeInstance();
            if (nodeInstance == null) {
                continue;
            }
            if (nodeInstance.getRouteNodeInstanceId() == null) {
                throw new IllegalStateException(
                        "Error creating document detail structure because of NULL node instance id.");
            }
            nodeInstances.put(nodeInstance.getRouteNodeInstanceId(), nodeInstance);
        }
        detail.setActionRequests(actionRequestVOs);
        List<org.kuali.rice.kew.api.document.node.RouteNodeInstance> nodeInstanceVOs =
                new ArrayList<org.kuali.rice.kew.api.document.node.RouteNodeInstance>();
        for (Iterator<RouteNodeInstance> iterator = nodeInstances.values().iterator(); iterator.hasNext(); ) {
            RouteNodeInstance nodeInstance = iterator.next();
            nodeInstanceVOs.add(RouteNodeInstance.to(nodeInstance));
        }
        detail.setRouteNodeInstances(nodeInstanceVOs);
        List<ActionTaken> actionTakenVOs = new ArrayList<ActionTaken>();
        for (Object element : routeHeader.getActionsTaken()) {
            ActionTakenValue actionTaken = (ActionTakenValue) element;
            actionTakenVOs.add(ActionTakenValue.to(actionTaken));
        }
        detail.setActionsTaken(actionTakenVOs);
        return detail.build();
    }

}
