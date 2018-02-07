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
package org.kuali.rice.krad.bo;

import org.kuali.rice.core.framework.persistence.jpa.CompositePrimaryKeyBase;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * PK for SessionDocument.  'Cause we love the JPAness.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SessionDocumentId extends CompositePrimaryKeyBase {
	@Id
	@Column(name="DOC_HDR_ID")
	protected String documentNumber;
	@Id
	@Column(name="SESN_DOC_ID")
	protected String sessionId;
	@Id
	@Column(name="PRNCPL_ID")
	protected String principalId;
	@Id
	@Column(name="IP_ADDR")
	protected String ipAddress;

	public SessionDocumentId() {}

	public SessionDocumentId(String documentNumber, String sessionId, String principalId, String ipAddress) {
		this.documentNumber = documentNumber;
		this.sessionId = sessionId;
		this.principalId = principalId;
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the documentNumber
	 */
	public String getDocumentNumber() {
		return this.documentNumber;
	}
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return this.sessionId;
	}
	/**
	 * @return the principalId
	 */
	public String getPrincipalId() {
		return this.principalId;
	}
	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return this.ipAddress;
	}
	/**
	 * @param documentNumber the documentNumber to set
	 */
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * @param principalId the principalId to set
	 */
	public void setPrincipalId(String principalId) {
		this.principalId = principalId;
	}
	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
