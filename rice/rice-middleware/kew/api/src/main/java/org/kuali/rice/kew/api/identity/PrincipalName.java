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
 * The name of a Principal in KIM
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PrincipalName implements UserId {

	private String id;
	
	public PrincipalName() {
		super();
	}

	public PrincipalName(String principalName) {
		id = principalName;
	}
	
	public String getPrincipalName() {
		return id;
	}
	
	public void setPrincipalName(String principalName) {
		id = principalName;
	}
	
	@Override
	public String getId() {
		return getPrincipalName();
	}

	@Override
	public boolean isEmpty() {
		return id == null || id.trim().length() == 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrincipalName other = (PrincipalName) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PrincipalName [id=" + this.id + "]";
	}
}
