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

import java.util.HashMap;
import java.util.Map;

import org.kuali.rice.ksb.messaging.remotedservices.Inbox;
import org.kuali.rice.ksb.messaging.remotedservices.Message;

/**
 * helper class for RESTful service implementations
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class Storage {

	public Map<String, Inbox> inboxes;
	public Map<String, Message> messages;

	private int lastInboxId;
	private int lastMessageId;

	public Storage() {
		inboxes = new HashMap<String, Inbox>();
		messages = new HashMap<String, Message>();
		lastInboxId = 0;
		lastMessageId = 0;
	}

	public synchronized Inbox storeInbox(Inbox inbox) {
		String id = inbox.getId();
		if (id == null) {
			lastInboxId++;
			id = String.valueOf(lastInboxId);
			inbox.setId(id);
		}
		inboxes.put(id, inbox);
		return inbox;
	}

	public synchronized Message storeMessage(Message message) {
		String id = message.getId();
		if (id == null) {
			lastMessageId++;
			id = String.valueOf(lastMessageId);
			message.setId(id);
		}
		messages.put(id, message);
		return message;
	}

	public Inbox retrieveInbox(String id) {
		return inboxes.get(id);
	}

	public Message retrieveMessage(String id) {
		return messages.get(id);
	}

	public synchronized Inbox deleteInbox(String id) {
		return inboxes.remove(id);
	}

	public synchronized Message deleteMessage(String id) {
		return messages.remove(id);
	}
}
