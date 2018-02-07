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
package org.kuali.rice.kew.engine;

import org.jdom.Document;
import org.jdom.Element;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.AbstractRoleAttribute;
import org.kuali.rice.kew.rule.ResolvedQualifiedRole;
import org.kuali.rice.kew.workgroup.GroupNameId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class ChartOrgDispatchAttribute extends AbstractRoleAttribute {

    private static final long serialVersionUID = -9114899910238607996L;
	public static final String DISPATCH_ROLE = "DISPATCH";
    private String workgroupName;
    
    public ChartOrgDispatchAttribute() {}
    
    public ChartOrgDispatchAttribute(String workgroupName) {
        this.workgroupName = workgroupName;
    }

    public List getRoleNames() {
        return Arrays.asList(new RoleName[] { new RoleName(getClass().getName(), DISPATCH_ROLE, DISPATCH_ROLE) });
    }

    public List getQualifiedRoleNames(String roleName, DocumentContent documentContent) {
        return parseWorkgroups(documentContent);
    }

    public ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, String roleName, String qualifiedRole) {
        List<Id> recipients = new ArrayList<Id>();
        recipients.add(new GroupNameId(qualifiedRole));
        return new ResolvedQualifiedRole(roleName, recipients);
    }

    public String getDocContent() {
        return "<"+DISPATCH_ROLE+">"+workgroupName+"</"+DISPATCH_ROLE+">";
    }
    
    private List<String> parseWorkgroups(DocumentContent documentContent) {
        Document document = XmlHelper.buildJDocument(documentContent.getDocument());
        Collection<Element> dispatchElements = XmlHelper.findElements(document.getRootElement(), DISPATCH_ROLE);
        List<String> workgroupNames = new ArrayList<String>();
        for (Iterator<Element> iterator = dispatchElements.iterator(); iterator.hasNext();) {
            Element element = (Element) iterator.next();
            workgroupNames.add(element.getText());
        }
        return workgroupNames;
    }
    
    
    
    

}
