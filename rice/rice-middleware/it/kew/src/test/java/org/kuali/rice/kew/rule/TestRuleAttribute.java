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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeErrorContract;
import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.identity.PrincipalId;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentContent;


/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TestRuleAttribute implements WorkflowRuleAttribute, RoleAttribute, WorkflowAttributeXmlValidator {

	private static final long serialVersionUID = -220808609566348066L;

	public static boolean VALID_CLIENT_ROUTING_DATA_CALLED = false;
	
	private static Map<String, Map<String, List<String>>> roles = new HashMap<String, Map<String, List<String>>>();
	private boolean required;
		
    public boolean isMatch(DocumentContent documentContent, List ruleExtensions) {
        return true;
    }
    
    public List getRoleNames() {
        List roleNames = new ArrayList();
        for (String roleName : roles.keySet()) {
            roleNames.add(new RoleName(getClass().getName(), roleName, roleName));
        }
    	return roleNames;
    }

    public List getRuleRows() {
    	return new ArrayList();
    }

    public List getRoutingDataRows() {
    	return new ArrayList();
    }

    public String getDocContent() {
    	return "<testRuleAttributeContent/>";
    }

    public List getRuleExtensionValues() {
    	return new ArrayList();
    }

    public List validateRoutingData(Map paramMap) {
    	return new ArrayList();
    }
    
    public String getAttributeLabel(){
        return "";
    }

    public List<RemotableAttributeError> validateRuleData(Map paramMap) {
    	return new ArrayList<RemotableAttributeError>();
    }

    public void setRequired(boolean required) {
    	this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

	public List<String> getQualifiedRoleNames(String roleName, DocumentContent documentContent) {
		List<String> qualifiedRoleNames = new ArrayList<String>();
		Map<String, List<String>> qualifiedRoles = roles.get(roleName);
		if (qualifiedRoles != null) {
			qualifiedRoleNames.addAll(qualifiedRoles.keySet());
		} else {
			throw new IllegalArgumentException("TestRuleAttribute does not support the given role " + roleName);
		}
		return qualifiedRoleNames;
	}

	public ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, String roleName, String qualifiedRole) {
		ResolvedQualifiedRole resolved = new ResolvedQualifiedRole();
		Map<String, List<String>> qualifiedRoles = (Map<String, List<String>>)roles.get(roleName);
		if (qualifiedRoles != null) {
			List<String> recipients = (List<String>)qualifiedRoles.get(qualifiedRole);
			if (recipients != null) {
				resolved.setQualifiedRoleLabel(qualifiedRole);
				resolved.setRecipients(convertPrincipalIdList(recipients));
			} else {
				throw new IllegalArgumentException("TestRuleAttribute does not support the qualified role " + qualifiedRole);
			}
		} else {
			throw new IllegalArgumentException("TestRuleAttribute does not support the given role " + roleName);
		}
		return resolved;
	}
	
	private static List<Id> convertPrincipalIdList(List<String> principalIds) {
		List<Id> idList = new ArrayList<Id>();
		for (String principalId : principalIds) {
			idList.add(new PrincipalId(principalId));
		}
		return idList;
	}
	
	public static void addRole(String roleName) {
		roles.put(roleName, new HashMap<String, List<String>>());
	}
	
	public static void removeRole(String roleName) {
		roles.remove(roleName);
	}
	
	public static Map<String, List<String>> getRole(String roleName) {
		return (Map<String, List<String>>)roles.get(roleName);
	}
	
	public static void addQualifiedRole(String roleName, String qualifiedRoleName) {
		getRole(roleName).put(qualifiedRoleName, new ArrayList<String>());
	}
	
	public static void removeQualifiedRole(String roleName, String qualifiedRoleName) {
		getRole(roleName).remove(qualifiedRoleName);
	}
	
	/**
	 * All you need to call now.  Simplies the previous 3 step process of adding role, qual role then recipients
	 * 
	 * @param roleName
	 * @param qualifiedRoleName
	 * @param recipients
	 */
	public static void setRecipientPrincipalIds(String roleName, String qualifiedRoleName, List<String> principalIds) {
		Map<String, List<String>> qualifiedRoles = getRole(roleName);
		if (qualifiedRoles == null) {
		    addRole(roleName);
		}
		if (qualifiedRoles == null) {
		    addRole(roleName);
		    addQualifiedRole(roleName, qualifiedRoleName);
		    qualifiedRoles = getRole(roleName);
		}
		qualifiedRoles.put(qualifiedRoleName, principalIds);
	}
	
	public static List<String> getRecipientPrincipalIds(String roleName, String qualifiedRoleName) {
		Map<String, List<String>> qualifiedRoles = getRole(roleName);
		return (List<String>)qualifiedRoles.get(qualifiedRoleName);
	}

	public List<RemotableAttributeError> validateClientRoutingData() {
		return new ArrayList<RemotableAttributeError>();
	}

}
