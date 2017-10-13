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
package org.kuali.rice.kew.identity.service;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.api.user.UserId;
import org.kuali.rice.kew.workgroup.GroupId;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.group.Group;

/**
 * A simple helper service in KEW for interacting with the KIM identity
 * management services.  Some of the methods on here exist solely for
 * the purpose of assisting with the piece-by-piece migration of
 * KEW to use the KIM services.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface IdentityHelperService {

	public String getIdForPrincipalName(String principalName);

	public String getIdForGroupName(String namespace, String groupName);

	/**
	 * Returns the KimPrincipal for the given principal id.  Throws an exception
	 * if the principal id cannot be resolved to a principal.
	 * 
	 * @throws RiceIllegalArgumentException if the principal id cannot be resolved to a principal.
	 */
	public Principal getPrincipal(String principalId);

	/**
	 * Returns the KimPrincipal for the given principal name.  Throws an exception
	 * if the principal name cannot be resolved to a principal.
	 * 
	 * @throws RiceIllegalArgumentException if the principal name cannot be resolved to a principal
	 */
	public Principal getPrincipalByPrincipalName(String principalName);

	/**
	 * Returns the Person for the given principal id.  Throws an exception
	 * if the principal id cannot be resolved to a person.
	 * 
	 * @throws RiceIllegalArgumentException if the principal id cannot be resolved to a person.
	 */
	public Person getPerson(String principalId);

	/**
	 * Returns the Person for the given principal name.  Throws an exception
	 * if the principal name cannot be resolved to a person.
	 * 
	 * @throws RiceIllegalArgumentException if the principal name cannot be resolved to a person.
	 */
	public Person getPersonByPrincipalName(String principalName);

	/**
	 * Returns the Person for the given employee id.  Throws an exception
	 * if the principal name cannot be resolved to a person.
	 * 
	 * @throws RiceIllegalArgumentException if the principal name cannot be resolved to a person.
	 */
	public Person getPersonByEmployeeId(String employeeId);

	/**
	 * Checks that the given principalId is valid.  Throws a RiceRuntimeException if it is not.
	 * 
	 * @throws RiceIllegalArgumentException if the given principalId is valid
	 */
	public void validatePrincipalId(String principalId);

	/**
	 * Returns the principal for the given UserId.
	 * 
	 * @throws RiceIllegalArgumentException if the given UserId does not resolve to a valid principal
	 */
	public Principal getPrincipal(UserId userId);

	/**
	 * Returns the Group for the given groupId.  Throws an exception
	 * if the groupId cannot be resolved to a group.
	 * 
	 * @throws RiceIllegalArgumentException if the groupId cannot be resolved to a group.
	 */
	public Group getGroup(String groupId);

	/**
	 * Returns the Group for the given GroupId.  Throws an exception
	 * if the groupId cannot be resolved to a group.
	 * 
	 * @throws RiceIllegalArgumentException if the GroupId cannot be resolved to a group.
	 */
	public Group getGroup(GroupId groupId);

	/**
	 * Returns the Group for the given namespaceCode and name.  Throws an exception
	 * if the namespaceCode and name cannot be resolved to a group.
	 * 
	 * @throws RiceIllegalArgumentException if the namespaceCode and name cannot be resolved to a group.
	 */
	public Group getGroupByName(String namespaceCode, String name);

	/**
	 * Returns the Recipient for the given principalId.  Throws an exception
	 * if the principalId cannot be resolved to a recipient.
	 * 
	 * @throws RiceIllegalArgumentException if the principalId cannot be resolved to a recipient
	 */
	public Recipient getPrincipalRecipient(String principalId);

	/**
	 * Returns the principal for the "system user".  This is a user
	 * that can be used in the cases where an actual user cannot be
	 * determined.
	 */
	public Principal getSystemPrincipal();
}
