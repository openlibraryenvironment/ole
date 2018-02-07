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
package org.kuali.rice.kew.api.action;

import java.util.List;

import org.joda.time.DateTime;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.mo.common.Identifiable;

public interface ActionRequestContract extends Identifiable {

	String getId();
	
	ActionRequestType getActionRequested();
	
	ActionRequestStatus getStatus();
	
	boolean isCurrent();
	
	DateTime getDateCreated();
	
	String getResponsibilityId();
	
	String getDocumentId();
		
	int getPriority();

    int getRouteLevel();
	
	String getAnnotation();
		
	RecipientType getRecipientType();
	
	String getPrincipalId();
	
	String getGroupId();
	
	ActionRequestPolicy getRequestPolicy();
	
	String getResponsibilityDescription();
	
	boolean isForceAction();
	
	DelegationType getDelegationType();
	
	String getRoleName();
	
	String getQualifiedRoleName();
	
	String getQualifiedRoleNameLabel();
	
	String getRouteNodeInstanceId();
	
	String getNodeName();
	
	String getRequestLabel();
	
	String getParentActionRequestId();
	
	ActionTakenContract getActionTaken();
	
	List<? extends ActionRequestContract> getChildRequests();
		
}
