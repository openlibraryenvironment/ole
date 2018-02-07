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
 * The primary ID of a Principal in KIM
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PrincipalId implements UserId {
    private static final long serialVersionUID = -5551723348738932404L;

    private String principalId;

	public PrincipalId() { }

	public PrincipalId(String principalId) {
		setPrincipalId(principalId);
	}
	
	public String getPrincipalId() {
		return this.principalId;
	}
	
	public void setPrincipalId(String principalId) {
		this.principalId = (principalId == null ? null : principalId.trim());
	}

    @Override
    public String getId() {
        return getPrincipalId();
    }


    /**
     * Returns true if this userId has an empty value. Empty userIds can't be used as keys in a Hash, among other things.
     *
     * @return true if this instance doesn't have a value
     */
    @Override
    public boolean isEmpty() {
    	return (principalId == null || principalId.trim().length() == 0);
    }

    /**
     * If you make this class non-final, you must rewrite equals to work for subclasses.
     */
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof PrincipalId)) {
            PrincipalId w = (PrincipalId) obj;

            if (getPrincipalId() == null) {
                return false;
            }
            return principalId.equals(w.getPrincipalId());
        }

        return false;
    }

    public int hashCode() {
        return principalId == null ? 0 : principalId.hashCode();
    }

    public String toString() {
        if (principalId == null) {
            return "principalId: null";
        }
        return "principalId: " + principalId;
    }

}
