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
package org.kuali.rice.kew.identity.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.actionrequest.KimPrincipalRecipient;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.identity.EmployeeId;
import org.kuali.rice.kew.api.identity.PrincipalId;
import org.kuali.rice.kew.api.identity.PrincipalName;
import org.kuali.rice.kew.api.user.UserId;
import org.kuali.rice.kew.identity.service.IdentityHelperService;
import org.kuali.rice.kew.workgroup.GroupId;
import org.kuali.rice.kew.workgroup.GroupNameId;
import org.kuali.rice.kew.workgroup.WorkflowGroupId;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class IdentityHelperServiceImpl implements IdentityHelperService {

	private static final Log logger = LogFactory.getLog(IdentityHelperServiceImpl.class);

		public String getIdForPrincipalName(String principalName) {
		if (principalName == null) {
			throw new RiceIllegalArgumentException("Can't lookup a principal ID for a null principal name.");
		}
		Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalName);
		if (principal == null) {
			throw new RiceIllegalArgumentException("Given principal name of '" + principalName + "' was invalid.  Failed to lookup a corresponding principal ID.");
		}
		return principal.getPrincipalId();
	}

		public void validatePrincipalId(String principalId) {
			// the getPrincipal method attempts to load the principal with the id and throws an exception if it fails
			getPrincipal(principalId);
		}

	public String getIdForGroupName(String namespace, String groupName) {
		Group group = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(namespace, groupName);
		if (group == null) {
			throw new RiceIllegalArgumentException("Given namespace of '" + namespace + "' and name of '" + groupName + "' was invalid.  Failed to lookup a corresponding group ID.");
		}
		return group.getId();
	}


	public Recipient getPrincipalRecipient(String principalId) {
		Principal principal = getPrincipal(principalId);
		return new KimPrincipalRecipient(principal);
	}

	public Principal getPrincipal(String principalId) {
		Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
		if (principal == null) {
			throw new RiceIllegalArgumentException("Could not locate a principal with the given principalId of " + principalId);
		}
		return principal;
	}

	public Principal getPrincipalByPrincipalName(String principalName) {
		Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalName);
		if (principal == null) {
			throw new RiceIllegalArgumentException("Could not locate a principal with the given principalName of " + principalName);
		}
		return principal;
	}

	public Group getGroupByName(String namespaceCode, String name) {
		Group group = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(namespaceCode, name);
		if (group == null) {
			throw new RiceIllegalArgumentException("Could not locate a group with the given namspace of '" + namespaceCode + "' and group name of '" + name + "'");
		}
		return group;
	}

	public Person getPerson(String principalId) {
		Person person = KimApiServiceLocator.getPersonService().getPerson(principalId);
		if (person == null) {
			throw new RiceIllegalArgumentException("Could not locate a person with the given principal id of " + principalId);
		}
		return person;
	}

	public Person getPersonByPrincipalName(String principalName) {
		Person person = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(principalName);
		if (person == null) {
			throw new RiceIllegalArgumentException("Could not locate a person with the given principal name of " + principalName);
		}
		return person;
	}

	public Person getPersonByEmployeeId(String employeeId) {
		Person person = KimApiServiceLocator.getPersonService().getPersonByEmployeeId(employeeId);
		if (person == null) {
			throw new RiceIllegalArgumentException("Could not locate a person with the given employee id of " + employeeId);
		}
		return person;
	}


	public Group getGroup(String groupId) {
		Group group = KimApiServiceLocator.getGroupService().getGroup(groupId);
		if (group == null) {
			throw new RiceIllegalArgumentException("Could not locate a group with the given groupId of " + groupId);
		}
		return group;
	}

	public Group getGroup(GroupId groupId) {
		if (groupId == null || groupId.isEmpty()) {
			return null;
		} else if (groupId instanceof WorkflowGroupId) {
			return KimApiServiceLocator.getGroupService().getGroup(""+((WorkflowGroupId)groupId).getGroupId());
		} else if (groupId instanceof GroupNameId) {
			return KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(
                    ((GroupNameId) groupId).getNamespace(), ((GroupNameId) groupId).getNameId());
		}
		throw new RiceIllegalArgumentException("Invalid GroupId type was passed: " + groupId);
	}

	public Principal getPrincipal(UserId userId) {
		if (userId == null) {
			return null;
		} else if (userId instanceof PrincipalId) {
			String principalId = ((PrincipalId)userId).getPrincipalId();
			return KimApiServiceLocator.getIdentityService().getPrincipal(principalId);
		} else if (userId instanceof PrincipalName) {
			String principalName = ((PrincipalName)userId).getId();
			return KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalName);
		} else if (userId instanceof EmployeeId) {
			String employeeId = ((EmployeeId)userId).getEmployeeId();
			Person person = getPersonByEmployeeId(employeeId);
			return getPrincipal(person.getPrincipalId());
		}
		throw new RiceIllegalArgumentException("Invalid UserIdDTO type was passed: " + userId);
	}
	
	public Principal getSystemPrincipal() {
		return getPrincipalByPrincipalName(KewApiConstants.SYSTEM_USER);
	}

}
