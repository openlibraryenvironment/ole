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
package org.kuali.rice.kew.util;

import java.io.Serializable;

import org.kuali.rice.kew.actionrequest.ActionRequestValue;


/**
 * A user, workgroup, or role who is responsible for an Action Request.
 * 
 * @see ActionRequestValue
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ResponsibleParty implements Serializable {

	private static final long serialVersionUID = 6788236688949489851L;

	private String principalId;
    private String groupId;
    private String roleName;

    public ResponsibleParty() {
    }

    public static ResponsibleParty fromGroupId(String groupId) {
		ResponsibleParty responsibleParty = new ResponsibleParty();
		responsibleParty.setGroupId(groupId);
		return responsibleParty;
	}
	
	public static ResponsibleParty fromPrincipalId(String principalId) {
		ResponsibleParty responsibleParty = new ResponsibleParty();
		responsibleParty.setPrincipalId(principalId);
		return responsibleParty;
	}
	
	public static ResponsibleParty fromRoleName(String roleName) {
		ResponsibleParty responsibleParty = new ResponsibleParty();
		responsibleParty.setRoleName(roleName);
		return responsibleParty;
	}

    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        if (principalId != null) {
            sb.append("user=");
            sb.append(principalId.toString());
        } else if (groupId != null) {
            sb.append("groupID=");
            sb.append(groupId.toString());
        } else if (roleName != null) {
            sb.append("roleName=");
            sb.append(roleName);
        }
        sb.append("]");
        return sb.toString();
    }

    public String getGroupId() {
        return groupId;
    }

    public String getPrincipalId() {
        return principalId;
    }
    
    public void setPrincipalId(String principalId) {
        this.principalId = principalId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isPrincipal() {
        return getPrincipalId() != null;
    }

    public boolean isGroup() {
        return getGroupId() != null;
    }

    public boolean isRole() {
        return getRoleName() != null;
    }
    
}
