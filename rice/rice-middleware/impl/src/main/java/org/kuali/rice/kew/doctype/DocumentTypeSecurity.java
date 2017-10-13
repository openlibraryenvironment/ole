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
package org.kuali.rice.kew.doctype;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.impex.xml.XmlConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class DocumentTypeSecurity implements Serializable {

  private static final long serialVersionUID = -1886779857180381404L;

  private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentTypeSecurity.class);
  
  private Boolean active;

  private Boolean initiatorOk;
  private Boolean routeLogAuthenticatedOk;
  private List<KeyValue> searchableAttributes = new ArrayList<KeyValue>();
  private List<Group> workgroups = new ArrayList<Group>();
  private List<SecurityPermissionInfo> permissions = new ArrayList<SecurityPermissionInfo>();
  private List<String> allowedRoles = new ArrayList<String>();
  private List<String> disallowedRoles = new ArrayList<String>();
  private List<String> securityAttributeExtensionNames = new ArrayList<String>();
  private List<String> securityAttributeClassNames = new ArrayList<String>();

  private static XPath xpath = XPathHelper.newXPath();

  public DocumentTypeSecurity() {}

  /** parse <security> XML to populate security object
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException */
  public DocumentTypeSecurity(String standardApplicationId, String documentTypeSecurityXml)
  {
    try {
      if (org.apache.commons.lang.StringUtils.isEmpty(documentTypeSecurityXml)) {
        return;
      }

      InputSource inputSource = new InputSource(new BufferedReader(new StringReader(documentTypeSecurityXml)));
      Element securityElement = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource).getDocumentElement();

      String active = (String) xpath.evaluate("./@active", securityElement, XPathConstants.STRING);
      if (org.apache.commons.lang.StringUtils.isEmpty(active) || "true".equals(active.toLowerCase())) {
        // true is the default
        this.setActive(Boolean.valueOf(true));
      }
      else {
        this.setActive(Boolean.valueOf(false));
      }

      // there should only be one <initiator> tag
      NodeList initiatorNodes = (NodeList) xpath.evaluate("./initiator", securityElement, XPathConstants.NODESET);
      if (initiatorNodes != null && initiatorNodes.getLength()>0) {
        Node initiatorNode = initiatorNodes.item(0);
        String value = initiatorNode.getTextContent();
        if (org.apache.commons.lang.StringUtils.isEmpty(value) || value.toLowerCase().equals("true")) {
          this.setInitiatorOk(Boolean.valueOf(true));
        }
        else {
          this.initiatorOk = Boolean.valueOf(false);
        }
      }

      // there should only be one <routeLogAuthenticated> tag
      NodeList routeLogAuthNodes = (NodeList) xpath.evaluate("./routeLogAuthenticated", securityElement, XPathConstants.NODESET);
      if (routeLogAuthNodes != null && routeLogAuthNodes.getLength()>0) {
        Node routeLogAuthNode = routeLogAuthNodes.item(0);
        String value = routeLogAuthNode.getTextContent();
        if (org.apache.commons.lang.StringUtils.isEmpty(value) || value.toLowerCase().equals("true")) {
          this.routeLogAuthenticatedOk = Boolean.valueOf(true);
        }
        else {
          this.routeLogAuthenticatedOk = Boolean.valueOf(false);
        }
      }

      NodeList searchableAttributeNodes = (NodeList) xpath.evaluate("./searchableAttribute", securityElement, XPathConstants.NODESET);
      if (searchableAttributeNodes != null && searchableAttributeNodes.getLength()>0) {
        for (int i = 0; i < searchableAttributeNodes.getLength(); i++) {
          Node searchableAttributeNode = searchableAttributeNodes.item(i);
          String name = (String) xpath.evaluate("./@name", searchableAttributeNode, XPathConstants.STRING);
          String idType = (String) xpath.evaluate("./@idType", searchableAttributeNode, XPathConstants.STRING);
          if (!org.apache.commons.lang.StringUtils.isEmpty(name) && !org.apache.commons.lang.StringUtils.isEmpty(idType)) {
            KeyValue searchableAttribute = new ConcreteKeyValue(name, idType);
            searchableAttributes.add(searchableAttribute);
          }
        }
      }

      NodeList workgroupNodes = (NodeList) xpath.evaluate("./workgroup", securityElement, XPathConstants.NODESET);
      if (workgroupNodes != null && workgroupNodes.getLength()>0) {
    	LOG.warn("Document Type Security XML is using deprecated element 'workgroup', please use 'groupName' instead.");
        for (int i = 0; i < workgroupNodes.getLength(); i++) {
          Node workgroupNode = workgroupNodes.item(i);
          String value = workgroupNode.getTextContent().trim();
          if (!org.apache.commons.lang.StringUtils.isEmpty(value)) {
        	value = Utilities.substituteConfigParameters(value);
            String namespaceCode = Utilities.parseGroupNamespaceCode(value);
            String groupName = Utilities.parseGroupName(value);
        	Group groupObject = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(namespaceCode,
                    groupName);
        	if (groupObject == null) {
        		throw new WorkflowException("Could not find group: " + value);
        	}
            workgroups.add(groupObject);
          }
        }
      }

      NodeList groupNodes = (NodeList) xpath.evaluate("./groupName", securityElement, XPathConstants.NODESET);
      if (groupNodes != null && groupNodes.getLength()>0) {
        for (int i = 0; i < groupNodes.getLength(); i++) {
          Node groupNode = groupNodes.item(i);
          if (groupNode.getNodeType() == Node.ELEMENT_NODE) {
        	String groupName = groupNode.getTextContent().trim();
            if (!org.apache.commons.lang.StringUtils.isEmpty(groupName)) {
              groupName = Utilities.substituteConfigParameters(groupName).trim();
              String namespaceCode = Utilities.substituteConfigParameters(((Element) groupNode).getAttribute(XmlConstants.NAMESPACE)).trim();
              Group groupObject = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(namespaceCode,
                      groupName);
              
              
              if (groupObject != null) {
            	  workgroups.add(groupObject); 
              } else {
            	  LOG.warn("Could not find group with name '" + groupName + "' and namespace '" + namespaceCode + "' which was defined on Document Type security");
              }
//                if (groupObject == null) {
//                  throw new WorkflowException("Could not find group with name '" + groupName + "' and namespace '" + namespaceCode + "'");
//                }
         
              
            }
          }
        }
      }

      NodeList permissionNodes = (NodeList) xpath.evaluate("./permission", securityElement, XPathConstants.NODESET);
      if (permissionNodes != null && permissionNodes.getLength()>0) {
        for (int i = 0; i < permissionNodes.getLength(); i++) {
          Node permissionNode = permissionNodes.item(i);
          if (permissionNode.getNodeType() == Node.ELEMENT_NODE) {
        	  SecurityPermissionInfo securityPermission = new SecurityPermissionInfo();
        	securityPermission.setPermissionName(Utilities.substituteConfigParameters(((Element) permissionNode).getAttribute(XmlConstants.NAME)).trim());
        	securityPermission.setPermissionNamespaceCode(Utilities.substituteConfigParameters(((Element) permissionNode).getAttribute(XmlConstants.NAMESPACE)).trim());
        	if (!StringUtils.isEmpty(securityPermission.getPermissionName()) && !StringUtils.isEmpty(securityPermission.getPermissionNamespaceCode())) {
        		//get details and qualifications
        		if (permissionNode.hasChildNodes()) {
        			NodeList permissionChildNodes = permissionNode.getChildNodes();
        			for (int j = 0; j <permissionChildNodes.getLength(); j++) {
        				Node permissionChildNode = permissionChildNodes.item(j);
        				if (permissionChildNode.getNodeType() == Node.ELEMENT_NODE) {
	        				String childAttributeName = Utilities.substituteConfigParameters(((Element) permissionChildNode).getAttribute(XmlConstants.NAME)).trim();
	        				String childAttributeValue = permissionChildNode.getTextContent().trim();
	        				if (!StringUtils.isEmpty(childAttributeValue)) {
	        					childAttributeValue = Utilities.substituteConfigParameters(childAttributeValue).trim();
	        				}
	        				if (!StringUtils.isEmpty(childAttributeValue)) {
	        					childAttributeValue = Utilities.substituteConfigParameters(childAttributeValue).trim();
	        				}
	        				if (permissionChildNode.getNodeName().trim().equals("permissionDetail")) {
	        					securityPermission.getPermissionDetails().put(childAttributeName, childAttributeValue);
	        				}
	        				if (permissionChildNode.getNodeName().trim().equals("qualification")) {
	        					securityPermission.getQualifications().put(childAttributeName, childAttributeValue);
	        				}
        				}
        			}
        		}
        		
              //if ( KimApiServiceLocator.getPermissionService().isPermissionDefined(securityPermission.getPermissionNamespaceCode(), securityPermission.getPermissionName(), securityPermission.getPermissionDetails())) {
            	  permissions.add(securityPermission); 
              //} else {
            	//  LOG.warn("Could not find permission with name '" + securityPermission.getPermissionName() + "' and namespace '" + securityPermission.getPermissionNamespaceCode() + "' which was defined on Document Type security");
              //}
            }
          }
        }
      }
      
      NodeList roleNodes = (NodeList) xpath.evaluate("./role", securityElement, XPathConstants.NODESET);
      if (roleNodes != null && roleNodes.getLength()>0) {
        for (int i = 0; i < roleNodes.getLength(); i++) {
          Element roleElement = (Element)roleNodes.item(i);
          String value = roleElement.getTextContent().trim();
          String allowedValue = roleElement.getAttribute("allowed");
          if (StringUtils.isBlank(allowedValue)) {
        	  allowedValue = "true";
          }
          if (!org.apache.commons.lang.StringUtils.isEmpty(value)) {
        	  if (Boolean.parseBoolean(allowedValue)) {
        		  allowedRoles.add(value);
        	  } else {
        		  disallowedRoles.add(value);
        	  }
          }
        }
      }

      NodeList attributeNodes = (NodeList) xpath.evaluate("./securityAttribute", securityElement, XPathConstants.NODESET);
      if (attributeNodes != null && attributeNodes.getLength()>0) {
          for (int i = 0; i < attributeNodes.getLength(); i++) {
            Element attributeElement = (Element)attributeNodes.item(i);
            NamedNodeMap elemAttributes = attributeElement.getAttributes();
            // can be an attribute name or an actual classname
            String attributeOrClassName = null;
            String applicationId = standardApplicationId;
            if (elemAttributes.getNamedItem("name") != null) {
                // found a name attribute so find the class name
                String extensionName = elemAttributes.getNamedItem("name").getNodeValue().trim();
                this.securityAttributeExtensionNames.add(extensionName);
            } else if (elemAttributes.getNamedItem("class") != null) {
                // class name defined
                String className = elemAttributes.getNamedItem("class").getNodeValue().trim();
                this.securityAttributeClassNames.add(className);
            } else {
                throw new WorkflowException("Cannot find attribute 'name' or attribute 'class' for securityAttribute Node");
            }
          }
        }
    } catch (Exception err) {
      throw new WorkflowRuntimeException(err);
    }
  }

  public List<String> getSecurityAttributeExtensionNames() {
    return this.securityAttributeExtensionNames;
  }

  public void setSecurityAttributeExtensionNames(List<String> securityAttributeExtensionNames) {
    this.securityAttributeExtensionNames = securityAttributeExtensionNames;
  }

    public List<String> getSecurityAttributeClassNames() {
        return securityAttributeClassNames;
    }

    public void setSecurityAttributeClassNames(List<String> securityAttributeClassNames) {
        this.securityAttributeClassNames = securityAttributeClassNames;
    }

    public Boolean getInitiatorOk() {
    return initiatorOk;
  }
  public void setInitiatorOk(Boolean initiatorOk) {
    this.initiatorOk = initiatorOk;
  }

  public Boolean getRouteLogAuthenticatedOk() {
    return routeLogAuthenticatedOk;
  }
  public void setRouteLogAuthenticatedOk(Boolean routeLogAuthenticatedOk) {
    this.routeLogAuthenticatedOk = routeLogAuthenticatedOk;
  }

  public List<String> getAllowedRoles() {
	return allowedRoles;
  }

  public void setAllowedRoles(List<String> allowedRoles) {
	this.allowedRoles = allowedRoles;
  }

  public List<String> getDisallowedRoles() {
	return disallowedRoles;
  }

  public void setDisallowedRoles(List<String> disallowedRoles) {
	this.disallowedRoles = disallowedRoles;
  }

  public List<KeyValue> getSearchableAttributes() {
	return searchableAttributes;
  }

  public void setSearchableAttributes(List<KeyValue> searchableAttributes) {
	this.searchableAttributes = searchableAttributes;
  }

  public List<Group> getWorkgroups() {
	return workgroups;
  }

  public void setWorkgroups(List<Group> workgroups) {
	this.workgroups = workgroups;
  }
  
  public List<SecurityPermissionInfo> getPermissions() {
    return this.permissions;
  }

  public void setPermissions(List<SecurityPermissionInfo> permissions) {
	this.permissions = permissions;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public boolean isActive() {
    if (active != null) {
      return active.booleanValue();
    }
    else {
      return false;
    }
  }
}
