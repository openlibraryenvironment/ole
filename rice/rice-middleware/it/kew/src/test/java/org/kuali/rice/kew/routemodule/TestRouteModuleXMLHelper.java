/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.routemodule;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.kew.api.exception.ResourceUnavailableException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TestRouteModuleXMLHelper {

    /**
     * 
     */
    public static String toXML(TestDocContent docContent) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<docContent>");
        for (Iterator rlIt = docContent.getRouteLevels().iterator(); rlIt.hasNext();) {
            TestRouteLevel routeLevel = (TestRouteLevel) rlIt.next();
            buffer.append("<routeLevel priority=\"").append(routeLevel.getPriority()).append("\">");
            for (Iterator respIt = routeLevel.getResponsibilities().iterator(); respIt.hasNext();) {
                TestResponsibility responsibility = (TestResponsibility) respIt.next();
                buffer.append("<responsibility actionRequested=\"").append(responsibility.getActionRequested()).append("\" priority=\"").append(responsibility.getPriority()).append("\">");
                TestRecipient recipient = responsibility.getRecipient();
                buffer.append("<recipient type=\"").append(recipient.getType()).append("\" id=\"").append(recipient.getId()).append("\"/>");
                if (!responsibility.getDelegations().isEmpty()) {
                    buffer.append("<delegations>");
                }
                for (Iterator delIt = responsibility.getDelegations().iterator(); delIt.hasNext();) {
                    TestDelegation delegation = (TestDelegation) delIt.next();
                    buffer.append("<delegation type=\"").append(delegation.getType().getCode()).append("\">");
                    TestResponsibility delResp = delegation.getResponsibility();
                    buffer.append("<delegateResponsibility>");
                    TestRecipient delRecipient = delResp.getRecipient();
                    buffer.append("<recipient type=\"").append(delRecipient.getType()).append("\" id=\"").append(delRecipient.getId()).append("\"/>");
                    buffer.append("</delegateResponsibility>");
                    buffer.append("</delegation>");
                }
                if (!responsibility.getDelegations().isEmpty()) {
                    buffer.append("</delegations>");
                }
                buffer.append("</responsibility>");
            }
            buffer.append("</routeLevel>");
        }
        buffer.append("</docContent>");
        return buffer.toString();
    }
    
    public static TestRouteLevel parseCurrentRouteLevel(DocumentRouteHeaderValue routeHeader) throws ResourceUnavailableException, WorkflowException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new BufferedInputStream(new ByteArrayInputStream(routeHeader.getDocContent().getBytes())));
            NodeList nodes = document.getElementsByTagName(TestRouteModuleConstants.ROUTE_LEVEL_ELEMENT);
            Node routeLevelNode = null;
            Integer priority = null;
            for (int index = 0; index < nodes.getLength(); index++) {
                Node node = nodes.item(index);
                NamedNodeMap nodeMap = node.getAttributes();
                Node priorityNode = nodeMap.getNamedItem(TestRouteModuleConstants.PRIORITY_ATTRIBUTE);
                try {
                    priority = Integer.valueOf(priorityNode.getNodeValue());
                } catch (NumberFormatException e) {
                    throw new WorkflowException("Invalid route level priority '"+priorityNode.getNodeValue()+"'", e);
                }
                if (priorityNode != null && routeHeader.getDocRouteLevel().equals(priority)) {
                    routeLevelNode = node;
                    break;
                }
            }
            if (routeLevelNode == null) {
                return null;
            }
            TestRouteLevel routeLevel = new TestRouteLevel();
            routeLevel.setPriority(priority.intValue());
            List responsibilityNodes = new ArrayList();
            NodeList rlNodes = routeLevelNode.getChildNodes();
            for (int index = 0; index < rlNodes.getLength(); index++) {
                Node node = rlNodes.item(index);
                if (node.getNodeName().equals(TestRouteModuleConstants.RESPONSIBILITY_ELEMENT)) {
                    responsibilityNodes.add(node);
                }
            }
            routeLevel.setResponsibilities(parseResponsibilities(responsibilityNodes));
            return routeLevel;
        } catch (ParserConfigurationException e) {
            throw new ResourceUnavailableException("Could not configure DOM parser for route level " + routeHeader.getDocRouteLevel());
        } catch (IOException e) {
            throw new ResourceUnavailableException("Could not parse XML doc content for route level " + routeHeader.getDocRouteLevel());
        } catch (SAXException e) {
            throw new ResourceUnavailableException("Could not parse XML doc content for route level " + routeHeader.getDocRouteLevel());
        }
    }
    
    private static List parseResponsibilities(List responsibilityNodes) throws WorkflowException {
        List responsibilities = new ArrayList();
        for (Iterator iterator = responsibilityNodes.iterator(); iterator.hasNext();) {
            Node node = (Node) iterator.next();
            responsibilities.add(parseResponsibility(node));
        }
        return responsibilities;
    }
    
    private static TestResponsibility parseResponsibility(Node responsibilityNode) throws WorkflowException {
        TestResponsibility responsibility = new TestResponsibility();
        NamedNodeMap attributes = responsibilityNode.getAttributes();
        Node actionRequestedNode = attributes.getNamedItem(TestRouteModuleConstants.ACTION_REQUESTED_ATTRIBUTE);
        Node priorityNode = attributes.getNamedItem(TestRouteModuleConstants.PRIORITY_ATTRIBUTE);
        responsibility.setActionRequested(actionRequestedNode.getNodeValue());
        try {
            responsibility.setPriority(Integer.parseInt(priorityNode.getNodeValue()));
        } catch (NumberFormatException e) {
            throw new WorkflowException("Invalid responsibility priority '"+priorityNode.getNodeValue()+"'", e);
        }
        NodeList childNodes = responsibilityNode.getChildNodes();
        for (int index = 0; index < childNodes.getLength(); index++) {
            Node node = childNodes.item(index);
            if (node.getNodeName().equals(TestRouteModuleConstants.RECIPIENT_ELEMENT)) {
                responsibility.setRecipient(parseRecipient(node));
            } else if (node.getNodeName().equals(TestRouteModuleConstants.DELEGATIONS_ELEMENT)) {
                responsibility.setDelegations(parseDelegations(node));
            }
        }
        return responsibility;
    }
    
    private static TestRecipient parseRecipient(Node recipientNode) {
        TestRecipient recipient = new TestRecipient();
        NamedNodeMap attributes = recipientNode.getAttributes();
        recipient.setType(attributes.getNamedItem(TestRouteModuleConstants.TYPE_ATTRIBUTE).getNodeValue());
        recipient.setId(attributes.getNamedItem(TestRouteModuleConstants.ID_ATTRIBUTE).getNodeValue());
        return recipient;
    }
    
    private static List parseDelegations(Node delegationsNode) {
        List delegations = new ArrayList();
        NodeList childNodes = delegationsNode.getChildNodes();
        for (int index = 0; index < childNodes.getLength(); index++) {
            Node node = childNodes.item(index);
            if (node.getNodeName().equals(TestRouteModuleConstants.DELEGATION_ELEMENT)) {
                delegations.add(parseDelegation(node));
            }
        }
        return delegations;
    }
    
    private static TestDelegation parseDelegation(Node delegationNode) {
        TestDelegation delegation = new TestDelegation();
        NamedNodeMap attributes = delegationNode.getAttributes();
        delegation.setType(DelegationType.fromCode(attributes.getNamedItem(TestRouteModuleConstants.TYPE_ATTRIBUTE)
                .getNodeValue()));
        NodeList childNodes = delegationNode.getChildNodes();
        for (int index = 0; index < childNodes.getLength(); index++) {
            Node node = childNodes.item(index);
            if (node.getNodeName().equals(TestRouteModuleConstants.DELEGATE_RESPONSIBILITY_ELEMENT)) {
                delegation.setResponsibility(parseDelegateResponsibility(node));
            }
        }
        return delegation;
    }
    
    private static TestResponsibility parseDelegateResponsibility(Node delegateResponsibilityNode) {
        TestResponsibility responsibility = new TestResponsibility();
        NodeList childNodes = delegateResponsibilityNode.getChildNodes();
        for (int index = 0; index < childNodes.getLength(); index++) {
            Node node = childNodes.item(index);
            if (node.getNodeName().equals(TestRouteModuleConstants.RECIPIENT_ELEMENT)) {
                responsibility.setRecipient(parseRecipient(node));
            }
        }
        return responsibility;
    }
    
}
