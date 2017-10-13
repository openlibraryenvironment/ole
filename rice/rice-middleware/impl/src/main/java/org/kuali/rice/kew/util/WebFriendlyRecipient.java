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

import org.kuali.rice.kew.actionrequest.KimGroupRecipient;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.role.KimRoleRecipient;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.Person;

/**
 * Class was declared multiple times in different classes.  Removed
 * from classes and placed here.  Also changed some of the logic
 * so that we can stop using workflowUser
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class WebFriendlyRecipient implements Recipient{

	private static final long serialVersionUID = 2259350039081951688L;

	private String displayName;
	private String recipientId;

     public WebFriendlyRecipient(String recipientId, String displayName) {
    	 this.recipientId = recipientId;
    	 this.displayName = displayName;
     }

     public WebFriendlyRecipient(Object recipient) {
    	 if (recipient instanceof WebFriendlyRecipient) {
             recipientId = ((WebFriendlyRecipient) recipient).getRecipientId();
             displayName = ((WebFriendlyRecipient) recipient).getDisplayName();

         // NOTE: ActionItemDAO code is constructing WebFriendlyRecipient directly w/ Person objects
         // this should probably be changed to return only Recipients from DAO tier to web tier
         } else if(recipient instanceof Person){
         	recipientId = ((Person)recipient).getPrincipalId();
        	displayName = ((Person)recipient).getLastName() + ", " + ((Person)recipient).getFirstName();

         } else if(recipient instanceof KimGroupRecipient){
             recipientId = ((KimGroupRecipient)recipient).getGroupId();
             displayName = ((KimGroupRecipient)recipient).getGroup().getNamespaceCode() + ":" + ((KimGroupRecipient)recipient).getGroup().getName();

         }else {
        	throw new IllegalArgumentException("Must pass in type Recipient or Person");
        }
     }

     public String getRecipientId() {
         return recipientId;
     }

	/**
	 *
	 * @see org.kuali.rice.kew.actionrequest.Recipient#getDisplayName()
	 */
	public String getDisplayName() {
		return this.displayName;
	}

}
