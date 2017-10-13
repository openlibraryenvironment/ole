/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.ksb.testclient1;

import org.kuali.rice.ksb.messaging.remotedservices.Inbox;
import org.kuali.rice.ksb.messaging.remotedservices.InboxResource;
import org.kuali.rice.ksb.messaging.remotedservices.MessageResource;

/**
 * service implementation for {@link InboxResource}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class InboxResourceImpl implements InboxResource {

	private Storage storage;
	private MessageResource messageResource;

	/**
	 * @see org.kuali.rice.ksb.messaging.remotedservices.InboxResource#createInbox(org.kuali.rice.ksb.messaging.remotedservices.Inbox)
	 */
	public Inbox createInbox(Inbox inbox) {
		return storage.storeInbox(inbox);
	}

	/**
	 * @see org.kuali.rice.ksb.messaging.remotedservices.InboxResource#deleteInbox(java.lang.String)
	 */
	public void deleteInbox(String id) {
		storage.deleteInbox(id);
	}

	/**
	 * @see org.kuali.rice.ksb.messaging.remotedservices.InboxResource#retrieveInbox(java.lang.String)
	 */
	public Inbox retrieveInbox(String id) {
		return storage.retrieveInbox(id);
	}

	/**
	 * @see org.kuali.rice.ksb.messaging.remotedservices.InboxResource#updateInbox(org.kuali.rice.ksb.messaging.remotedservices.Inbox)
	 */
	public void updateInbox(Inbox inbox) {
		storage.storeInbox(inbox);
	}

	/**
	 * @return the storage
	 */
	public Storage getStorage() {
		return this.storage;
	}

	/**
	 * @param storage the storage to set
	 */
	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	/**
	 * @return the messageResource
	 */
	public MessageResource getMessageResource() {
		return this.messageResource;
	}

	/**
	 * @param messageResource the messageResource to set
	 */
	public void setMessageResource(MessageResource messageResource) {
		this.messageResource = messageResource;
	}

}
