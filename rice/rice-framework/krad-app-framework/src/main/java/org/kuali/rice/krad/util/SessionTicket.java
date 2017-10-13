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
package org.kuali.rice.krad.util;

import java.io.Serializable;
import java.util.Map;

/**
 * Holds information on an action (ticket type name) and context (ticketContext) that has been performed and can be placed in the
 * UserSession objectMap. This can be checked for by subsequent session requests to determine if the action has already taken place (for
 * example a Question or document action where the action is not recorded on the document or form).
 */
public class SessionTicket implements Serializable {
	private String ticketTypeName;
	private Map<String, String> ticketContext;

	public SessionTicket(String ticketTypeName) {
		this.ticketTypeName = ticketTypeName;
	}

	public String getTicketTypeName() {
		return this.ticketTypeName;
	}

	public void setTicketTypeName(String ticketTypeName) {
		this.ticketTypeName = ticketTypeName;
	}

	public Map<String, String> getTicketContext() {
		return this.ticketContext;
	}

	public void setTicketContext(Map<String, String> ticketContext) {
		this.ticketContext = ticketContext;
	}
}
