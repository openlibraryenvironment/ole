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

/**
 * Event sent to the postprocessor when document locking ids are requested.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentLockingEvent implements IDocumentEvent {

	private static final long serialVersionUID = 1L;
	private String documentId;
	private String appDocId;

	public DocumentLockingEvent(String documentId, String appDocId) {
		this.documentId = documentId;
		this.appDocId = appDocId;
	}
	
	public String getDocumentId() {
		return documentId;
	}

	public String getAppDocId() {
		return appDocId;
	}
	
    public String getDocumentEventCode() {
        return IDocumentEvent.AFTER_PROCESS;
    }

}
