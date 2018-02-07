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
package org.kuali.rice.kew.actionitem;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Outbox item.  An extension of {@link ActionItemActionListExtension} for OJB.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@Entity
@Table(name="KREW_OUT_BOX_ITM_T")
@AttributeOverrides({
	@AttributeOverride(name="id", column=@Column(name="ACTN_ITM_ID")),
	@AttributeOverride(name="principalId", column=@Column(name="PRNCPL_ID")),
	@AttributeOverride(name="dateAssigned", column=@Column(name="ASND_DT")),
	@AttributeOverride(name="actionRequestCd", column=@Column(name="RQST_CD")),
	@AttributeOverride(name="actionRequestId", column=@Column(name="ACTN_RQST_ID")),
	@AttributeOverride(name="documentId", column=@Column(name="DOC_HDR_ID")),//, insertable=false, updatable=false)),
	@AttributeOverride(name="responsibilityId", column=@Column(name="RSP_ID")),
	@AttributeOverride(name="groupId", column=@Column(name="GRP_ID")),
	@AttributeOverride(name="roleName", column=@Column(name="ROLE_NM")),
	@AttributeOverride(name="docTitle", column=@Column(name="DOC_HDR_TTL")),
	@AttributeOverride(name="docLabel", column=@Column(name="DOC_TYP_LBL")),
	@AttributeOverride(name="docHandlerURL", column=@Column(name="DOC_HDLR_URL")),
	@AttributeOverride(name="docName", column=@Column(name="DOC_TYP_NM")),
	@AttributeOverride(name="delegatorPrincipalId", column=@Column(name="DLGN_PRNCPL_ID")),
	@AttributeOverride(name="delegatorGroupId", column=@Column(name="DLGN_GRP_ID")),
	@AttributeOverride(name="delegationType", column=@Column(name="DLGN_TYP")),
	@AttributeOverride(name="requestLabel", column=@Column(name="RQST_LBL"))
})

public class OutboxItemActionListExtension extends ActionItemActionListExtension {

	private static final long serialVersionUID = 5776214610837858304L;

	public OutboxItemActionListExtension() {}

	public OutboxItemActionListExtension(ActionItem actionItem) {
		this.setActionRequestCd(actionItem.getActionRequestCd());
		this.setActionRequestId(actionItem.getActionRequestId());
		this.setDateAssigned(actionItem.getDateAssigned());
		this.setDelegationType(actionItem.getDelegationType());
		this.setDelegatorPrincipalId(actionItem.getDelegatorPrincipalId());
		this.setDelegatorGroupId(actionItem.getDelegatorGroupId());
		this.setDocHandlerURL(actionItem.getDocHandlerURL());
		this.setDocLabel(actionItem.getDocLabel());
		this.setDocName(actionItem.getDocName());
		this.setDocTitle(actionItem.getDocTitle());
		this.setResponsibilityId(actionItem.getResponsibilityId());
		this.setRoleName(actionItem.getRoleName());
		this.setDocumentId(actionItem.getDocumentId());
		this.setPrincipalId(actionItem.getPrincipalId());
		this.setGroupId(actionItem.getGroupId());
		this.setRequestLabel(actionItem.getRequestLabel());
	}

}
