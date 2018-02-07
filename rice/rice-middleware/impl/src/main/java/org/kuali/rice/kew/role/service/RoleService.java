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
package org.kuali.rice.kew.role.service;

import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;


/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RoleService {

	/**
	 * Re-resolves the given qualified role for all documents for the given document type (including children).
	 * This methods executes asynchronously.
	 */
	public void reResolveRole(DocumentType documentType, String roleName);

	/**
	 * Re-resolves the given qualified role for all documents for the given document type (including children).
	 * This methods executes asynchronously.
	 */
	public void reResolveQualifiedRole(DocumentType documentType, String roleName, String qualifiedRoleNameLabel);

	/**
	 * Re-resolves the given qualified role on the given document.  This method executes synchronously.
	 */
	public void reResolveQualifiedRole(DocumentRouteHeaderValue routeHeader, String roleName, String qualifiedRoleNameLabel);

	/**
	 * Re-resolves the given role on the given document.  This method executes synchronously.
	 */
	public void reResolveRole(DocumentRouteHeaderValue routeHeader, String roleName);

}
