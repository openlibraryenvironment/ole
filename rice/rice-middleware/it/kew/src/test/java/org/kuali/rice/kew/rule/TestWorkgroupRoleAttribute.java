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
package org.kuali.rice.kew.rule;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.workgroup.GroupNameId;


/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TestWorkgroupRoleAttribute extends AbstractRoleAttribute {

	private static final long serialVersionUID = 7650315462958972080L;


	public List<RoleName> getRoleNames() {
		List<RoleName> roles = new ArrayList<RoleName>();
		roles.add(new RoleName(getClass().getName(), "workgroup", "workgroup label"));
		return roles;
	}

	public List<String> getQualifiedRoleNames(String roleName, DocumentContent documentContent) {
		List<String> qualRoleNames = new ArrayList<String>();
		qualRoleNames.add("KR-WKFLW:TestWorkgroup");
		return qualRoleNames;
	}


	public ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, String roleName, String qualifiedRole) {
		List<Id> recipients = new ArrayList<Id>();
		recipients.add(new GroupNameId(qualifiedRole));
		return new ResolvedQualifiedRole("workgroup role lable", recipients);
	}



}
