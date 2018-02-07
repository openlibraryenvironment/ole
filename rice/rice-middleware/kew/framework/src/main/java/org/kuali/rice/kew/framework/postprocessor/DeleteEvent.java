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
package org.kuali.rice.kew.framework.postprocessor;

import org.kuali.rice.kew.framework.postprocessor.IDocumentEvent;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 * Signal to the PostProcessor that the routeHeader is being deleted.
 * 
 */
public class DeleteEvent implements IDocumentEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1991987156524201870L;

	private String appDocId;

	private String documentId;

	public DeleteEvent(String documentId, String appDocId) {
		this.documentId = documentId;
		this.appDocId = appDocId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kuali.rice.kew.IDocumentEvent#getDocumentEventCode()
	 */
	public String getDocumentEventCode() {
		return DELETE_CHANGE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kuali.rice.kew.IDocumentEvent#getDocumentId()
	 */
	public String getDocumentId() {
		return documentId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kuali.rice.kew.IDocumentEvent#getAppDocId()
	 */
	public String getAppDocId() {
		return this.appDocId;
	}
}
