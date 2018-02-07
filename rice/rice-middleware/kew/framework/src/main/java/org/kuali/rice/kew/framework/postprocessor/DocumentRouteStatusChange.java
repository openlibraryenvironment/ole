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
 * <p>
 * <Title>
 * </p>
 * <p>
 * <Description>
 * </p>
 * <p>
 * <p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * <p>
 * Company: UIS - Indiana University
 * </p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentRouteStatusChange implements IDocumentEvent {

	private static final long serialVersionUID = -5170568498563302803L;
	private String appDocId;
	private String documentId;
	private String newRouteStatus;
	private String oldRouteStatus;

	public DocumentRouteStatusChange(String documentId, String appDocId, String oldStatus, String newStatus) {
		this.documentId = documentId;
		this.appDocId = appDocId;
		this.newRouteStatus = newStatus;
		this.oldRouteStatus = oldStatus;
	}

	public String getDocumentEventCode() {
		return ROUTE_STATUS_CHANGE;
	}

	public String getDocumentId() {
		return documentId;
	}

	public String getNewRouteStatus() {
		return newRouteStatus;
	}

	public String getOldRouteStatus() {
		return oldRouteStatus;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("DocumentId ").append(documentId);
		buffer.append(" changing from routeStatus ").append(oldRouteStatus);
		buffer.append(" to routeStatus ").append(newRouteStatus);

		return buffer.toString();
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
