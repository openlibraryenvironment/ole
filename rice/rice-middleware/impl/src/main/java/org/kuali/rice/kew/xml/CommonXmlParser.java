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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import static org.kuali.rice.core.api.impex.xml.XmlConstants.*;


/**
 * Parsing routines for XML structures shared across parsers.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CommonXmlParser {
    private static final Logger LOG = Logger.getLogger(CommonXmlParser.class);

    /**
     * Parses, but does not save, a RuleResponsibility from responsibility identifier elements nested in
     * the specified element
     * @param element parent element of responsibility identifier elements
     * @return a parsed RuleResponsibility
     * @throws XmlException
     */
    public static RuleResponsibilityBo parseResponsibilityNameAndType(Element element) throws XmlException {
        RuleResponsibilityBo responsibility = new RuleResponsibilityBo();

        String principalId = element.getChildText(PRINCIPAL_ID, element.getNamespace());
        String principalName = element.getChildText(PRINCIPAL_NAME, element.getNamespace());
        String groupId = element.getChildText(GROUP_ID, element.getNamespace());
        Element groupNameElement = element.getChild(GROUP_NAME, element.getNamespace());
        String role = element.getChildText(ROLE, element.getNamespace());
        Element roleNameElement = element.getChild(ROLE_NAME, element.getNamespace());

        String user = element.getChildText(USER, element.getNamespace());
        String workgroup = element.getChildText(WORKGROUP, element.getNamespace());

        if (!StringUtils.isEmpty(user)) {
            principalName = user;
            LOG.warn("Rule XML is using deprecated element 'user', please use 'principalName' instead.");
        }

        // in code below, we allow core config parameter replacement in responsibilities
        if (!StringUtils.isBlank(principalId)) {
            principalId = Utilities.substituteConfigParameters(principalId);
            Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
            if (principal == null) {
                throw new XmlException("Could not locate principal with the given id: " + principalId);
            }
            responsibility.setRuleResponsibilityName(principalId);
            responsibility.setRuleResponsibilityType(KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID);
        } else if (!StringUtils.isBlank(principalName)) {
            principalName = Utilities.substituteConfigParameters(principalName);
            Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalName);
            if (principal == null) {
                throw new XmlException("Could not locate principal with the given name: " + principalName);
            }
            responsibility.setRuleResponsibilityName(principal.getPrincipalId());
            responsibility.setRuleResponsibilityType(KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID);
        } else if (!StringUtils.isBlank(groupId)) {
            groupId = Utilities.substituteConfigParameters(groupId);
            Group group = KimApiServiceLocator.getGroupService().getGroup(groupId);
            if (group == null) {
                throw new XmlException("Could not locate group with the given id: " + groupId);
            }
            responsibility.setRuleResponsibilityName(groupId);
            responsibility.setRuleResponsibilityType(KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID);
        } else if (groupNameElement != null) {
            String groupName = groupNameElement.getText();
            String groupNamespace = groupNameElement.getAttributeValue(NAMESPACE);
            if (StringUtils.isBlank(groupName)) {
                throw new XmlException("Group name element has no value");
            }
            if (StringUtils.isBlank(groupNamespace)) {
                throw new XmlException("namespace attribute must be specified");
            }
            groupName = Utilities.substituteConfigParameters(groupName);
            groupNamespace = Utilities.substituteConfigParameters(groupNamespace);
            Group group = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(groupNamespace,
                    groupName);
            if (group == null) {
                throw new XmlException("Could not locate group with the given namespace: " + groupNamespace + " and name: " + groupName);
            }
            responsibility.setRuleResponsibilityName(group.getId());
            responsibility.setRuleResponsibilityType(KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID);
        } else if (!StringUtils.isBlank(role)) {
            role = Utilities.substituteConfigParameters(role);
            responsibility.setRuleResponsibilityName(role);
            responsibility.setRuleResponsibilityType(KewApiConstants.RULE_RESPONSIBILITY_ROLE_ID);
        } else if (roleNameElement != null) {
            String roleName = roleNameElement.getText();
            String attributeClassName = roleNameElement.getAttributeValue(ATTRIBUTE_CLASS_NAME);
            if (StringUtils.isBlank(roleName)) {
                throw new XmlException("Role name element has no value");
            }
            if (StringUtils.isBlank(attributeClassName)) {
                throw new XmlException("attributeClassName attribute must be specified");
            }
            roleName = Utilities.substituteConfigParameters(roleName);
            attributeClassName = Utilities.substituteConfigParameters(attributeClassName);
            responsibility.setRuleResponsibilityName(RoleName.constructRoleValue(attributeClassName, roleName));
            responsibility.setRuleResponsibilityType(KewApiConstants.RULE_RESPONSIBILITY_ROLE_ID);
        } else if (!StringUtils.isBlank(workgroup)) {
            LOG.warn("Rule XML is using deprecated element 'workgroup', please use 'groupName' instead.");
            workgroup = Utilities.substituteConfigParameters(workgroup);
            String workgroupNamespace = Utilities.parseGroupNamespaceCode(workgroup);
            String workgroupName = Utilities.parseGroupName(workgroup);

            Group workgroupObject = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(
                    workgroupNamespace, workgroupName);
            if (workgroupObject == null) {
                throw new XmlException("Could not locate workgroup: " + workgroup);
            }
            responsibility.setRuleResponsibilityName(workgroupObject.getId());
            responsibility.setRuleResponsibilityType(KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID);
        } else {
            return null;
        }

        return responsibility;
    }
}
