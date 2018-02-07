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
package org.kuali.rice.kew.api.identity;

import org.kuali.rice.kew.api.user.UserId;

/**
 * The Employee ID of a Person in KIM
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class EmployeeId implements UserId {
    private static final long serialVersionUID = -1335314734556834644L;
    private String employeeId;

	public EmployeeId() {
	}

	public EmployeeId(String employeeId) {
		setEmployeeId(employeeId);
	}
	
	public String getEmployeeId() {
		return this.employeeId;
	}
	
	public void setEmployeeId(String employeeId) {
		this.employeeId = (employeeId == null ? null : employeeId.trim());
	}

    @Override
    public String getId() {
        return getEmployeeId();
    }

    /**
     * Returns true if this userId has an empty value. Empty userIds can't be used as keys in a Hash, among other things.
     *
     * @return true if this instance doesn't have a value
     */
    @Override
    public boolean isEmpty() {
    	return (employeeId == null || employeeId.trim().length() == 0);
    }

    /**
     * If you make this class non-final, you must rewrite equals to work for subclasses.
     */
    public boolean equals(Object obj) {

        if (obj != null && (obj instanceof EmployeeId)) {
            EmployeeId a = (EmployeeId) obj;

            if (getEmployeeId() == null) {
                return false;
            }

            return employeeId.equals(a.getEmployeeId());
        }

        return false;
    }

    public int hashCode() {
        return employeeId == null ? 0 : employeeId.hashCode();
    }

    public String toString() {
        if (employeeId == null) {
            return "employeeId: null";
        }
        return "employeeId: " + employeeId;
    }

}
