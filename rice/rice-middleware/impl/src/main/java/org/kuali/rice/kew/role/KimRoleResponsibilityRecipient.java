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
package org.kuali.rice.kew.role;

import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kim.api.responsibility.ResponsibilityAction;

import java.util.List;

/**
 * A request recipient represented by a KIM responsibility.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KimRoleResponsibilityRecipient implements Recipient {

	private List<ResponsibilityAction> responsibilities;
	private Recipient target;
	
	public KimRoleResponsibilityRecipient(List<ResponsibilityAction> responsibilities) {
		this.responsibilities = responsibilities;
	}
	
	public List<ResponsibilityAction> getResponsibilities() {
		return this.responsibilities;
	}

	public Recipient getTarget() {
		return this.target;
	}

	public void setTarget(Recipient target) {
		this.target = target;
	}

	public String getDisplayName() {
		throw new UnsupportedOperationException("KimRoleRecipient does not support displayName");
	}

}
