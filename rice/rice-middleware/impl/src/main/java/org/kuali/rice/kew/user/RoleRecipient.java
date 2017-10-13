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
package org.kuali.rice.kew.user;

import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.rule.ResolvedQualifiedRole;

/**
 * Marks a Role to be used in an ActionRequestValue.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RoleRecipient implements Recipient {

	private static final long serialVersionUID = -8694150869017306593L;

	private String roleName;
	private String qualifiedRoleName;
	private ResolvedQualifiedRole resolvedQualifiedRole;
	private Recipient targetRecipient;

	public RoleRecipient(){}

	public RoleRecipient(String roleName) {
		this.roleName = roleName;
	}

	public RoleRecipient(String roleName, String qualifiedRoleName, ResolvedQualifiedRole resolvedQualifiedRole) {
		this.roleName = roleName;
		this.qualifiedRoleName = qualifiedRoleName;
		this.resolvedQualifiedRole = resolvedQualifiedRole;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getQualifiedRoleName() {
		return qualifiedRoleName;
	}

	public void setQualifiedRoleName(String qualifiedRoleName) {
		this.qualifiedRoleName = qualifiedRoleName;
	}

	public ResolvedQualifiedRole getResolvedQualifiedRole() {
		return resolvedQualifiedRole;
	}

	public void setResolvedQualifiedRole(ResolvedQualifiedRole resolvedQualifiedRole) {
		this.resolvedQualifiedRole = resolvedQualifiedRole;
	}

	public Recipient getTarget() {
		return targetRecipient;
	}

	public void setTarget(Recipient target) {
		this.targetRecipient = target;
	}
}
