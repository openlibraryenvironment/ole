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
package org.kuali.rice.kew.xml;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.api.type.KimTypeAttribute;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.kuali.rice.core.api.impex.xml.XmlConstants.*;


/**
 * Parses groups from XML.
 *
 * @see Group
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class GroupXmlParser {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(GroupXmlParser.class);
    private static final boolean DEFAULT_ACTIVE_VALUE = true;
    private static final String DEFAULT_GROUP_DESCRIPTION = "";
    private HashMap<String, List<String>> memberGroupIds = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> memberGroupNames = new HashMap<String, List<String>>();
    private HashMap<String, List<String>> memberPrincipalIds = new HashMap<String, List<String>>();
    private Map<String, String> groupAttributes = new HashMap<String, String>();

    public List<Group> parseGroups(InputStream input) throws IOException, XmlException {
        try {
            Document doc = XmlHelper.trimSAXXml(input);
            Element root = doc.getRootElement();
            return parseGroups(root);
        } catch (JDOMException e) {
            throw new XmlException("Parse error.", e);
        } catch (SAXException e){
            throw new XmlException("Parse error.",e);
        } catch(ParserConfigurationException e){
            throw new XmlException("Parse error.",e);
        }
    }


    /**
     * Parses and saves groups
     * @param element top-level 'data' element which should contain a <groups> child element
     * @return a list of parsed and saved, current, groups;
     * @throws XmlException
     */
    @SuppressWarnings("unchecked")
	public List<Group> parseGroups(Element element) throws XmlException {
        List<Group> groups = new ArrayList<Group>();
        for (Element groupsElement: (List<Element>) element.getChildren(GROUPS, GROUP_NAMESPACE)) {

            for (Element groupElement: (List<Element>) groupsElement.getChildren(GROUP, GROUP_NAMESPACE)) {
                groups.add(parseGroup(groupElement));
            }
        }
        for (Group group : groups) {
            GroupService groupService = KimApiServiceLocator.getGroupService();
            // check if group already exists
            Group foundGroup = groupService.getGroupByNamespaceCodeAndName(group.getNamespaceCode(), group.getName());

            if (foundGroup == null) {
                if ( LOG.isInfoEnabled() ) {
                	LOG.info("Group named '" + group.getName() + "' not found, creating new group named '" + group.getName() + "'");
                }
                try {
                    Group newGroup =  groupService.createGroup(group);

                    String key = newGroup.getNamespaceCode().trim() + KewApiConstants.KIM_GROUP_NAMESPACE_NAME_DELIMITER_CHARACTER + newGroup.getName().trim();
                    addGroupMembers(newGroup, key);
                } catch (Exception e) {
                    throw new RuntimeException("Error creating group with name '" + group.getName() + "'", e);
                }
            } else {
            	if ( LOG.isInfoEnabled() ) {
            		LOG.info("Group named '" + group.getName() + "' found, creating a new version");
            	}
                try {
                    Group.Builder builder = Group.Builder.create(foundGroup);
                    builder.setActive(group.isActive());
                    builder.setDescription(group.getDescription());
                    builder.setKimTypeId(group.getKimTypeId());

                    //builder.setVersionNumber(foundGroup.getVersionNumber());
                    group = builder.build();
                    groupService.updateGroup(foundGroup.getId(), group);

                    //delete existing group members and replace with new
                    groupService.removeAllMembers(foundGroup.getId());

                    String key = group.getNamespaceCode().trim() + KewApiConstants.KIM_GROUP_NAMESPACE_NAME_DELIMITER_CHARACTER + group.getName().trim();
                    addGroupMembers(group, key);

                } catch (Exception e) {
                    throw new RuntimeException("Error updating group.", e);
                }
            }
        }
        return groups;
    }

    @SuppressWarnings("unchecked")
	private Group parseGroup(Element element) throws XmlException {


        // Type element and children (namespace and name)

        String typeId = null;
        KimType kimTypeInfo;
        List<KimTypeAttribute> kimTypeAttributes = new ArrayList<KimTypeAttribute>();
        if (element.getChild(TYPE, GROUP_NAMESPACE) != null) {
            Element typeElement = element.getChild(TYPE, GROUP_NAMESPACE);
            String typeNamespace = typeElement.getChildText(NAMESPACE, GROUP_NAMESPACE);
            String typeName = typeElement.getChildText(NAME, GROUP_NAMESPACE);
            kimTypeInfo = KimApiServiceLocator.getKimTypeInfoService().findKimTypeByNameAndNamespace(typeNamespace, typeName);
            if (kimTypeInfo != null) {
            	typeId = kimTypeInfo.getId();
                kimTypeAttributes = kimTypeInfo.getAttributeDefinitions();
            } else  {
                throw new XmlException("Invalid type name and namespace specified.");
            }
        } else { //set to default type
            kimTypeInfo = KimApiServiceLocator.getKimTypeInfoService().findKimTypeByNameAndNamespace(KimConstants.KIM_TYPE_DEFAULT_NAMESPACE, KimConstants.KIM_TYPE_DEFAULT_NAME);
            if (kimTypeInfo != null) {
            	typeId = kimTypeInfo.getId();
                kimTypeAttributes = kimTypeInfo.getAttributeDefinitions();
            } else {
            	throw new RuntimeException("Failed to locate the 'Default' group type!  Please ensure that it's in your database.");
            }
        }
        //groupInfo.setKimTypeId(typeId);

        String groupNamespace = element.getChildText(NAMESPACE, GROUP_NAMESPACE);
        if (groupNamespace == null) {
            throw new XmlException("Namespace must have a value.");
        }

        String groupName = element.getChildText(NAME, GROUP_NAMESPACE);
        if (groupName == null) {
            throw new XmlException("Name must have a value.");
        }

        Group.Builder groupInfo = Group.Builder.create(groupNamespace, groupName, typeId);
        IdentityService identityService = KimApiServiceLocator.getIdentityService();
        //groupInfo.setGroupName(element.getChildText(NAME, GROUP_NAMESPACE));

        String id = element.getChildText(ID, GROUP_NAMESPACE);
        if (id != null) {
            groupInfo.setId(id.trim());
        } else {
        	
        }

        String description = element.getChildText(DESCRIPTION, GROUP_NAMESPACE);
        if (description != null && !description.trim().equals("")) {
            groupInfo.setDescription(description);
        }

        //Active Indicator
        groupInfo.setActive(DEFAULT_ACTIVE_VALUE);
        if (element.getChildText(ACTIVE, GROUP_NAMESPACE) != null) {
            String active = element.getChildText(ACTIVE, GROUP_NAMESPACE).trim();
            if (active.toUpperCase().equals("N") || active.toUpperCase().equals("FALSE")) {
                groupInfo.setActive(false);
            }
        }

        //Get list of attribute keys
        List<String> validAttributeKeys = new ArrayList<String>();
        for (KimTypeAttribute attribute : kimTypeAttributes) {
            validAttributeKeys.add(attribute.getKimAttribute().getAttributeName());
        }
        //Group attributes
        if (element.getChild(ATTRIBUTES, GROUP_NAMESPACE) != null) {
            List<Element> attributes = element.getChild(ATTRIBUTES, GROUP_NAMESPACE).getChildren();

            Map<String, String> attrMap = new HashMap<String, String>();
            for (Element attr : attributes ) {
                attrMap.put(attr.getAttributeValue(KEY), attr.getAttributeValue(VALUE));
                if (!validAttributeKeys.contains(attr.getAttributeValue(KEY))) {
                    throw new XmlException("Invalid attribute specified.");
                }
            }
            Map<String, String> groupAttributes = attrMap;
            if (!groupAttributes.isEmpty()) {
                groupInfo.setAttributes(groupAttributes);
            }
        }

        //Group members

        List<Element> members = null;
        if(element.getChild(MEMBERS, GROUP_NAMESPACE) == null) {
            members = new ArrayList<Element>();
        }
        else {
            members = element.getChild(MEMBERS, GROUP_NAMESPACE).getChildren();
        }
        for (Element member : members) {
            String elementName = member.getName().trim();
            if (elementName.equals(PRINCIPAL_NAME)) {
                String principalName = member.getText().trim();
                Principal principal = identityService.getPrincipalByPrincipalName(principalName);
                if (principal != null) {
                    addPrincipalToGroup(groupInfo.getNamespaceCode(), groupInfo.getName(), principal.getPrincipalId());
                } else {
                    throw new XmlException("Principal Name "+principalName+" cannot be found.");
                }
            } else if (elementName.equals(PRINCIPAL_ID)) {
                String xmlPrincipalId = member.getText().trim();
                Principal principal = identityService.getPrincipal(xmlPrincipalId);
                if (principal != null) {
                    addPrincipalToGroup(groupInfo.getNamespaceCode(), groupInfo.getName(), principal.getPrincipalId());
                } else {
                    throw new XmlException("Principal Id "+xmlPrincipalId+" cannot be found.");
                }
            // Groups are handled differently since the member group may not be saved yet.  Therefore they need to be validated after the groups are saved.
            } else if (elementName.equals(GROUP_ID)) {
                String xmlGroupId = member.getText().trim();
                addGroupToGroup(groupInfo.getNamespaceCode(), groupInfo.getName(), xmlGroupId);
            } else if (elementName.equals(GROUP_NAME)) {
                String xmlGroupName = member.getChildText(NAME, GROUP_NAMESPACE).trim();
                String xmlGroupNamespace = member.getChildText(NAMESPACE, GROUP_NAMESPACE).trim();
                addGroupNameToGroup(groupInfo.getNamespaceCode(), groupInfo.getName(), xmlGroupNamespace, xmlGroupName);
            } else {
                LOG.error("Unknown member element: " + elementName);
            }


        }

        return groupInfo.build();

    }

    private void addPrincipalToGroup(String groupNamespace, String groupName, String principalId) {
        String key = groupNamespace.trim() + KewApiConstants.KIM_GROUP_NAMESPACE_NAME_DELIMITER_CHARACTER + groupName.trim();
        List<String> principalIds = memberPrincipalIds.get(key);
        if (principalIds == null) {
            principalIds = new ArrayList<String>();
        }
        principalIds.add(principalId);
        memberPrincipalIds.put(key, principalIds);
    }

    private void addGroupToGroup(String groupNamespace, String groupName, String groupId) {
        String key = groupNamespace.trim() + KewApiConstants.KIM_GROUP_NAMESPACE_NAME_DELIMITER_CHARACTER + groupName.trim();
        List<String> groupIds = memberGroupIds.get(key);
        if (groupIds == null) {
            groupIds = new ArrayList<String>();
        }
        groupIds.add(groupId);
        memberGroupIds.put(key, groupIds);
    }

    private void addGroupNameToGroup(String groupNamespace, String groupName, String memberGroupNamespace, String memberGroupName) {
        String key = groupNamespace.trim() + KewApiConstants.KIM_GROUP_NAMESPACE_NAME_DELIMITER_CHARACTER + groupName.trim();
        List<String> groupNames = memberGroupNames.get(key);
        if (groupNames == null) {
            groupNames = new ArrayList<String>();
        }
        groupNames.add(memberGroupNamespace.trim() + KewApiConstants.KIM_GROUP_NAMESPACE_NAME_DELIMITER_CHARACTER + memberGroupName.trim());
        memberGroupNames.put(key, groupNames);
    }

    private void addGroupMembers(Group groupInfo, String key) throws XmlException {
        GroupService groupService = KimApiServiceLocator.getGroupService();
        List<String> groupIds = memberGroupIds.get(key);
        if (groupIds != null) {
            for (String groupId : groupIds) {
                Group group = groupService.getGroup(groupId);
                if (group != null) {
                    groupService.addGroupToGroup(group.getId(), groupInfo.getId());
                } else {
                    throw new XmlException("Group Id "+groupId+" cannot be found.");
                }
            }
        }
        List<String> groupNames = memberGroupNames.get(key);
        if (groupNames != null) {
            for (String groupName : groupNames) {
                Group group = groupService.getGroupByNamespaceCodeAndName(Utilities.parseGroupNamespaceCode(groupName),
                        Utilities.parseGroupName(groupName));
                if (group != null) {
                	groupService.addGroupToGroup(group.getId(), groupInfo.getId());
                } else {
                    throw new XmlException("Group "+groupName+" cannot be found.");
                }
            }
        }
        List<String> principalIds = memberPrincipalIds.get(key);
        if (principalIds != null) {
            for (String principalId : principalIds) {
            	groupService.addPrincipalToGroup(principalId, groupInfo.getId());
            }
        }

    }
}
