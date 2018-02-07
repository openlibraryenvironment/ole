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
package org.kuali.rice.kew.xml.export;

import org.jdom.Element;
import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.core.api.util.xml.XmlRenderer;
import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.kew.export.KewExportDataSet;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimType;

import java.util.Iterator;

import static org.kuali.rice.core.api.impex.xml.XmlConstants.*;

/**
 * This is a description of what this class does - jjhanso don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class GroupXmlExporter implements XmlExporter {
    protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(getClass());

    private XmlRenderer renderer = new XmlRenderer(GROUP_NAMESPACE);

	@Override
	public boolean supportPrettyPrint() {
		return true;
	}
    
    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.core.framework.impex.xml.XmlExporter#export(org.kuali.rice.core.api.impex.ExportDataSet)
     */
    public Element export(ExportDataSet exportDataSet) {
    	KewExportDataSet dataSet = KewExportDataSet.fromExportDataSet(exportDataSet);
        if (!dataSet.getGroups().isEmpty()) {
            Element rootElement = renderer.renderElement(null, GROUPS);
            rootElement.setAttribute(SCHEMA_LOCATION_ATTR, GROUP_SCHEMA_LOCATION, SCHEMA_NAMESPACE);
            for (Iterator iterator = dataSet.getGroups().iterator(); iterator.hasNext();) {
                Group group = (Group) iterator.next();
                exportGroup(rootElement, group);
            }
            return rootElement;
        }
        // TODO jjhanso - THIS METHOD NEEDS JAVADOCS
        return null;
    }

    /**
     * This method ...
     *
     * @param rootElement
     * @param group
     * @param object
     */
    private void exportGroup(Element parent, Group group) {
        Element groupElement = renderer.renderElement(parent, GROUP);
        if (group.getName() != null) {
            renderer.renderTextElement(groupElement, NAME, group.getName());
        }
        if (group.getNamespaceCode() != null) {
            renderer.renderTextElement(groupElement, NAMESPACE, group.getNamespaceCode());
        }

        if (group.getDescription() != null && !group.getDescription().trim().equals("")) {
            renderer.renderTextElement(groupElement, DESCRIPTION, group.getDescription());
        }

        renderer.renderTextElement(groupElement, ACTIVE, new Boolean(group.isActive()).toString());

        if (group.getKimTypeId() != null) {
            Element typeElement = renderer.renderElement(groupElement, TYPE);
            KimType kimType = KimApiServiceLocator.getKimTypeInfoService().getKimType(group.getKimTypeId());
            renderer.renderTextElement(typeElement, NAMESPACE, kimType.getNamespaceCode());
            renderer.renderTextElement(typeElement, NAME, kimType.getName());
        }

        if (group.getAttributes().size() > 0) {
            Element attributesElement = renderer.renderElement(groupElement, ATTRIBUTES);
            for (String key : group.getAttributes().keySet()) {
                Element attributeElement = renderer.renderElement(attributesElement, ATTRIBUTE);
                attributeElement.setAttribute(KEY, key);
                attributeElement.setAttribute(VALUE, group.getAttributes().get(key));
            }
        }

        java.util.List<String> memberGroupIds = KimApiServiceLocator.getGroupService().getDirectMemberGroupIds(group.getId());

        java.util.List<String> memberPrincipalIds = KimApiServiceLocator.getGroupService().getDirectMemberPrincipalIds(
                group.getId());

        if (memberGroupIds.size() > 0 || memberPrincipalIds.size() > 0) {
            Element membersElement = renderer.renderElement(groupElement, MEMBERS);
            for (String memberGroupId : memberGroupIds) {
                Group memberGroup = KimApiServiceLocator.getGroupService().getGroup(memberGroupId);
                Element groupNameElement = renderer.renderElement(membersElement, GROUP_NAME);
                renderer.renderTextElement(groupNameElement, NAME, memberGroup.getName());
                renderer.renderTextElement(groupNameElement, NAMESPACE, memberGroup.getNamespaceCode());
            }
            for (String memberPrincipalId : memberPrincipalIds) {
                renderer.renderTextElement(membersElement, PRINCIPAL_NAME, KimApiServiceLocator.getIdentityService().getPrincipal(memberPrincipalId).getPrincipalName());
            }
        }
    }

}
