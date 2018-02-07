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
package org.kuali.rice.kim.api.common.assignee;

import org.kuali.rice.kim.api.common.delegate.DelegateTypeContract;

import java.util.List;

//TODO Get a better description
/**
 * This is the contract for a Permission Assignee.
 * 
 * Permissions are attached to roles. All authorization checks should be done against permissions,
 * never against roles or groups.
 *  
 */
public interface AssigneeContract {

    /**
     * The Principal ID referenced by the Permission Assignee.
     *
     * @return principalId
     */
    String getPrincipalId();

    /**
     * The Group ID referenced by the Permission Assignee.
     * 
     * @return groupId
     */
	String getGroupId() ;
	
   /** 
	 * List of Delegates for a Permission Assignee 
	 * 
	 * @return delegates
	 */
	List<? extends DelegateTypeContract> getDelegates();
}
