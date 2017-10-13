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
package org.kuali.rice.kew.actionrequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.kuali.rice.kew.api.identity.PrincipalName;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.AbstractRoleAttribute;
import org.kuali.rice.kew.rule.ResolvedQualifiedRole;

public class RoleToRoleDelegationRole extends AbstractRoleAttribute {


	private static final long serialVersionUID = 3881730393316239780L;
	public static final String MAIN_ROLE = "MAIN";
	public static final String PRIMARY_DELEGATE_ROLE = "PRIMARY_DELEGATE";
	public static final String SECONDARY_DELEGATE_ROLE = "SECONDARY_DELEGATE";
	
	private static final List ROLE_NAMES = new ArrayList();
	static {
		ROLE_NAMES.add(new RoleName(RoleToRoleDelegationRole.class.getName(), MAIN_ROLE, MAIN_ROLE));
		ROLE_NAMES.add(new RoleName(RoleToRoleDelegationRole.class.getName(), PRIMARY_DELEGATE_ROLE, PRIMARY_DELEGATE_ROLE));
		ROLE_NAMES.add(new RoleName(RoleToRoleDelegationRole.class.getName(), SECONDARY_DELEGATE_ROLE, SECONDARY_DELEGATE_ROLE));
	}
	
	public static List MAIN_USERS = new ArrayList();
	static {
		MAIN_USERS.add("ewestfal");
	}
	
	public static Map PRIMARY_DELEGATES = new HashMap();
	static {
		List primDelegates1 = new ArrayList();
		primDelegates1.add("jhopf");
		PRIMARY_DELEGATES.put("ewestfal", primDelegates1);
		PRIMARY_DELEGATES.put("rkirkend", new ArrayList());
	}
	
	public static Map SECONDARY_DELEGATES = new HashMap();
	static {
		List secondaryDelegates1 = new ArrayList();
		secondaryDelegates1.add("jitrue");
		secondaryDelegates1.add("xqi");
		List secondaryDelegates2 = new ArrayList();
		secondaryDelegates2.add("jhopf");
		secondaryDelegates2.add("bmcgough");
		secondaryDelegates2.add("natjohns");
		SECONDARY_DELEGATES.put("ewestfal", secondaryDelegates1);
		SECONDARY_DELEGATES.put("rkirkend", secondaryDelegates2);
	}
	
	public List getRoleNames() {
		return ROLE_NAMES;
	}

	public List getQualifiedRoleNames(String roleName,
			DocumentContent documentContent) {
		List names = new ArrayList();
		if (MAIN_ROLE.equals(roleName)) {
			names = new ArrayList(MAIN_USERS);
		} else {
			throw new IllegalArgumentException("Can't get qualified role names for role '" + roleName +"'");
		}
		return names;
	}

	public ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, String roleName, String qualifiedRole) {
		List userIds = new ArrayList();
		if (MAIN_ROLE.equals(roleName)) {
			userIds = MAIN_USERS;
		} else if (PRIMARY_DELEGATE_ROLE.equals(roleName)) {
			userIds = new ArrayList((List)PRIMARY_DELEGATES.get(qualifiedRole));
		} else if (SECONDARY_DELEGATE_ROLE.equals(roleName)) {
			userIds = new ArrayList((List)SECONDARY_DELEGATES.get(qualifiedRole));
		} else {
			throw new IllegalArgumentException("Can't resolve qualified role for role '" + roleName +"'");
		}
		List recipients = new ArrayList();
		for (Iterator iterator = userIds.iterator(); iterator.hasNext();) {
			String networkId = (String) iterator.next();
			recipients.add(new PrincipalName(networkId));
		}
		return new ResolvedQualifiedRole(roleName, recipients);
	}

}
