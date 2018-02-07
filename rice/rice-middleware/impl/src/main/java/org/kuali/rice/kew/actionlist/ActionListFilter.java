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
package org.kuali.rice.kew.actionlist;

import java.io.Serializable;
import java.util.Date;

import org.kuali.rice.kew.api.KewApiConstants;


/**
 * model for the action list filter preferences
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionListFilter implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = -365729646389290478L;
	private String filterLegend;
    private String documentTitle = "";
    private boolean excludeDocumentTitle;
    private String docRouteStatus = KewApiConstants.ALL_CODE;
    private boolean excludeRouteStatus;
    private String actionRequestCd = KewApiConstants.ALL_CODE;
    private boolean excludeActionRequestCd;
    private String groupId;
    private String groupIdString = KewApiConstants.NO_FILTERING;
    private String groupName = "";
    private boolean excludeGroupId;
    private String documentType = "";
    private boolean excludeDocumentType;
    private Date createDateFrom;
    private Date createDateTo;
    private boolean excludeCreateDate;
    private Date lastAssignedDateFrom;
    private Date lastAssignedDateTo;
    private boolean excludeLastAssignedDate;
    private String delegatorId = "";
    private String primaryDelegateId = "";
    private boolean excludeDelegatorId;
    private String delegationType;
    private boolean excludeDelegationType;
    private boolean filterOn;

    public ActionListFilter() {}

    public ActionListFilter(ActionListFilter another) {
        this.filterLegend = another.filterLegend;
        this.documentTitle = another.documentTitle;
        this.excludeDocumentTitle = another.excludeDocumentTitle;
        this.docRouteStatus = another.docRouteStatus;
        this.excludeRouteStatus = another.excludeRouteStatus;
        this.actionRequestCd = another.actionRequestCd ;
        this.excludeActionRequestCd = another.excludeActionRequestCd ;
        this.groupId = another.groupId ;
        this.groupIdString = another.groupIdString ;
        this.groupName = another.groupName ;
        this.excludeGroupId = another.excludeGroupId ;
        this.documentType = another.documentType ;
        this.excludeDocumentType = another.excludeDocumentType ;
        this.createDateFrom = another.createDateFrom ;
        this.createDateTo = another.createDateTo ;
        this.excludeCreateDate = another.excludeCreateDate ;
        this.lastAssignedDateFrom = another.lastAssignedDateFrom ;
        this.lastAssignedDateTo = another.lastAssignedDateTo ;
        this.excludeLastAssignedDate = another.excludeLastAssignedDate ;
        this.delegatorId = another.delegatorId ;
        this.primaryDelegateId = another.primaryDelegateId ;
        this.excludeDelegatorId = another.excludeDelegatorId ;
        this.delegationType = another.delegationType ;
        this.excludeDelegationType = another.excludeDelegationType ;
        this.filterOn = another.filterOn ;
    }

    public String getActionRequestCd() {
        return actionRequestCd;
    }
    public void setActionRequestCd(String actionRequestCd) {
        this.actionRequestCd = actionRequestCd;
    }
    public Date getCreateDateFrom() {
        return createDateFrom;
    }
    public void setCreateDateFrom(Date createDate) {
        this.createDateFrom = createDate;
    }
    public String getDocRouteStatus() {
        return docRouteStatus;
    }
    public void setDocRouteStatus(String docRouteStatus) {
        this.docRouteStatus = docRouteStatus;
    }
    public String getDocumentTitle() {
        return documentTitle;
    }
    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }
    public String getDocumentType() {
        return documentType;
    }
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    public boolean isExcludeCreateDate() {
        return excludeCreateDate;
    }
    public void setExcludeCreateDate(boolean excludeCreateDate) {
        this.excludeCreateDate = excludeCreateDate;
    }
    public boolean isExcludeDocumentType() {
        return excludeDocumentType;
    }
    public void setExcludeDocumentType(boolean excludeDocument) {
        this.excludeDocumentType = excludeDocument;
    }
    public boolean isExcludeDocumentTitle() {
        return excludeDocumentTitle;
    }
    public void setExcludeDocumentTitle(boolean excludeDocumentTitle) {
        this.excludeDocumentTitle = excludeDocumentTitle;
    }
    public boolean isExcludeLastAssignedDate() {
        return excludeLastAssignedDate;
    }
    public void setExcludeLastAssignedDate(boolean excludeLastAssignedDate) {
        this.excludeLastAssignedDate = excludeLastAssignedDate;
    }
    public boolean isExcludeActionRequestCd() {
        return excludeActionRequestCd;
    }
    public void setExcludeActionRequestCd(boolean excludeRequestCd) {
        this.excludeActionRequestCd = excludeRequestCd;
    }
    public boolean isExcludeRouteStatus() {
        return excludeRouteStatus;
    }
    public void setExcludeRouteStatus(boolean excludeRouteStatus) {
        this.excludeRouteStatus = excludeRouteStatus;
    }
    public boolean isExcludeGroupId() {
        return excludeGroupId;
    }
    public void setExcludeGroupId(boolean excludeGroupId) {
        this.excludeGroupId = excludeGroupId;
    }
    public Date getLastAssignedDateTo() {
        return lastAssignedDateTo;
    }
    public void setLastAssignedDateTo(Date lastAssignedDate) {
        this.lastAssignedDateTo = lastAssignedDate;
    }
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getCreateDateTo() {
        return createDateTo;
    }
    public void setCreateDateTo(Date createDateTo) {
        this.createDateTo = createDateTo;
    }
    public Date getLastAssignedDateFrom() {
        return lastAssignedDateFrom;
    }
    public void setLastAssignedDateFrom(Date lastAssignedDateFrom) {
        this.lastAssignedDateFrom = lastAssignedDateFrom;
    }
    public String getDelegatorId() {
        return delegatorId;
    }
    public void setDelegatorId(String delegatorId) {
        this.delegatorId = delegatorId;
    }

    public String getPrimaryDelegateId() {
		return this.primaryDelegateId;
	}

    public void setPrimaryDelegateId(String primaryDelegateId) {
		this.primaryDelegateId = primaryDelegateId;
	}

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFilterLegend() {
        return filterLegend;
    }

    public void setFilterLegend(String filterLegend) {
        this.filterLegend = filterLegend;
    }

    public String getGroupIdString() {
        return groupIdString;
    }

    public void setGroupIdString(String groupIdString) {
        this.groupIdString = groupIdString;
    }

    public boolean isExcludeDelegatorId() {
        return excludeDelegatorId;
    }

    public void setExcludeDelegatorId(boolean excludeDelegatorId) {
        this.excludeDelegatorId = excludeDelegatorId;
    }

    public String getDelegationType() {
        return delegationType;
    }

    public void setDelegationType(String delegationType) {
        this.delegationType = delegationType;
    }

    public boolean isExcludeDelegationType() {
        return excludeDelegationType;
    }

    public void setExcludeDelegationType(boolean excludeDelegationType) {
        this.excludeDelegationType = excludeDelegationType;
    }

    public boolean isFilterOn() {
        return filterOn;
    }

    public void setFilterOn(boolean filterOn) {
        this.filterOn = filterOn;
    }
}
