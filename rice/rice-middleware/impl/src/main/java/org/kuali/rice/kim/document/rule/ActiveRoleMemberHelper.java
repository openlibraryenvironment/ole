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
package org.kuali.rice.kim.document.rule;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleMember;
import org.kuali.rice.kim.bo.ui.RoleDocumentDelegationMember;

/**
 * KULRICE-4153: This class is used to receive a set of Role Members and return a set of active Role Members. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ActiveRoleMemberHelper {
	private static final Logger LOG = Logger.getLogger(ActiveRoleMemberHelper.class);

	public List<KimDocumentRoleMember> getActiveRoleMembers (List<KimDocumentRoleMember> roleMembers){
		List<KimDocumentRoleMember> activeRoleMembers = new ArrayList<KimDocumentRoleMember>();
		for (KimDocumentRoleMember roleMember : roleMembers)
		{
			if (roleMember.isActive())
			{
				activeRoleMembers.add(roleMember);
			}
		}		
		return activeRoleMembers;		
	}
	    
    public List<RoleDocumentDelegationMember> getActiveDelegationRoleMembers(List<RoleDocumentDelegationMember> roleMembers) {
        List<RoleDocumentDelegationMember> activeRoleMembers = new ArrayList<RoleDocumentDelegationMember>();
        for (RoleDocumentDelegationMember roleMember : roleMembers) {
            if (roleMember.isActive()) {
                activeRoleMembers.add(roleMember);
            }
        }
        return activeRoleMembers;
    }
}
