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
package org.kuali.rice.kew.rule;

import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.group.GroupBo;

/**
 * Represents a rule responsibility which points to a group.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class GroupRuleResponsibility extends RuleResponsibilityBo {
    protected Group group;
	private String namespaceCode;
	private String name;

	public String getNamespaceCode() {
		return this.namespaceCode;
	}

	public void setNamespaceCode(String namespaceCode) {
		this.namespaceCode = namespaceCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @Override
	public Group getGroup() {
        if (this.group == null) {
	        this.group = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(getNamespaceCode(),
                    getName());
        }
        return this.group;
	}

    public GroupBo getGroupBo() {
        Group grp = null;
        if (isUsingGroup()) {
            grp = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(getNamespaceCode(),getName());
        }
        if(null != grp){
            return GroupBo.from(grp);
        }  else {
            return null;
        }
    }
}
