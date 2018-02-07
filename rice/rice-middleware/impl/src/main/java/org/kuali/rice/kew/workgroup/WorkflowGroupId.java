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
package org.kuali.rice.kew.workgroup;

/**
 * A {@link GroupId} which is a unique numerical identifier for a {@link Workgroup}.
 * 
 * @see Workgroup
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class WorkflowGroupId implements GroupId {

	private static final long serialVersionUID = -2452096307070075024L;

	private Long groupId;

    public WorkflowGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }

    /**
     * Returns true if this userId has an empty value. Empty userIds can't be used as keys in a Hash, among other things.
     * 
     * @return true if this instance doesn't have a value
     */
    public boolean isEmpty() {
        return groupId == null;
    }

    /**
     * If you make this class non-final, you must rewrite equals to work for subclasses.
     */
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj != null && (obj instanceof WorkflowGroupId)) {
            WorkflowGroupId w = (WorkflowGroupId) obj;

            if (w.getGroupId() != null && getGroupId() != null) {
                return w.getGroupId().equals(getGroupId());
            } else {
                return false;
            }
        }

        return isEqual;
    }

    public int hashCode() {
        if (groupId == null) {
            return 0;
        }
        return groupId.hashCode();
    }

    public String toString() {
        if (groupId != null) {
            return groupId.toString();
        }
        return "null";
    }
}
