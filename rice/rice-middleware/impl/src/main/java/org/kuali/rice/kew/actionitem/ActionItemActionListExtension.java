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

import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.actionlist.DisplayParameters;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValueActionListExtension;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.web.RowStyleable;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.kew.api.WorkflowDocument;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Alternate model object for action list fetches that do not automatically use
 * ojb collections.  This is here to make action list faster.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@MappedSuperclass
public class ActionItemActionListExtension extends ActionItem implements RowStyleable {

    private static final long serialVersionUID = -8801104028828059623L;

    @Transient
    private Timestamp lastApprovedDate;
    @Transient
    private Map<String, String> customActions = new HashMap<String, String>();
    @Transient
    private String rowStyleClass;
    @Transient
    private Integer actionListIndex;

    @Transient
    private String delegatorName = "";
    @Transient
    private String groupName = "";
    @Transient
    private DisplayParameters displayParameters;
    @Transient
    private boolean isInitialized = false;
    @Transient
    private DocumentRouteHeaderValueActionListExtension routeHeader;

    @Transient
    private boolean lastApprovedDateInitialized = false;
    @Transient
    private boolean delegatorNameInitialized = false;
    @Transient
    private boolean groupNameInitialized = false;



    //KRAD ActionList Stuff TODO
    private String routeHeaderRouteStatus;
    private Timestamp routeHeaderCreateDate;
    private String routeHeaderInitiatorName;
    private Timestamp routeHeaderApprovedDate;
    private String routeHeaderCurrentRouteLevelName;
    private String routeHeaderInitiatorWorkflowId;
    private String actionSelected;

    public String getActionSelected() {
        return "NONE";
    }

    public void setActionSelected(String actionSelected) {
        this.actionSelected = actionSelected;
    }

    public String getRouteHeaderRouteStatus() {
        return routeHeader.getDocRouteStatus();
    }

    public Timestamp getRouteHeaderCreateDate() {
        return routeHeader.getCreateDate();
    }

    public String getRouteHeaderInitiatorName() {
        return routeHeader.getInitiatorName();
    }

    public Timestamp getRouteHeaderApprovedDate() {
        return routeHeader.getApprovedDate();
    }

    public String getRouteHeaderCurrentRouteLevelName() {
        return routeHeader.getCurrentRouteLevelName();
    }

    public String getRouteHeaderInitiatorWorkflowId() {
        return routeHeader.getInitiatorWorkflowId();
    }

    public Integer getActionListIndex() {
        return actionListIndex;
    }

    public void setActionListIndex(Integer actionListIndex) {
        this.actionListIndex = actionListIndex;
    }

    public Timestamp getLastApprovedDate() {
        initializeLastApprovedDate();
        return this.lastApprovedDate;
    }

    public Map<String, String> getCustomActions() {
        return customActions;
    }

    public void setCustomActions(Map<String, String> customActions) {
        this.customActions = customActions;
    }

    public String getRowStyleClass() {
        return rowStyleClass;
    }

    public void setRowStyleClass(String rowStyleClass) {
        this.rowStyleClass = rowStyleClass;
    }

    public String getDelegatorName() {
        initializeDelegatorName();
        return delegatorName;
    }

    public String getGroupName() {
        initializeGroupName();
        return groupName;
    }

    public void initialize(Preferences preferences) {
        // always re-initialize row style class, just in case they changed a preference!
        initializeRowStyleClass(preferences);
    	if (isInitialized) {
    		return;
    	}
        if (KewApiConstants.PREFERENCES_YES_VAL.equals(preferences.getShowWorkgroupRequest())) {
            initializeGroupName();
        }
        if (KewApiConstants.PREFERENCES_YES_VAL.equals(preferences.getShowDelegator())) {
            initializeDelegatorName();
        }
        if (KewApiConstants.PREFERENCES_YES_VAL.equals(preferences.getShowDateApproved())) {
        	initializeLastApprovedDate();
        }
        this.routeHeader.initialize(preferences);
        isInitialized = true;
    }

    private void initializeRowStyleClass(Preferences preferences) {
        //set background colors for document statuses
        if (KewApiConstants.ROUTE_HEADER_CANCEL_CD.equalsIgnoreCase(routeHeader.getDocRouteStatus())) {
            setRowStyleClass(KewApiConstants.ACTION_LIST_COLOR_PALETTE.get(preferences.getColorCanceled()));
        } else if (KewApiConstants.ROUTE_HEADER_DISAPPROVED_CD.equalsIgnoreCase(routeHeader.getDocRouteStatus())) {
            setRowStyleClass(KewApiConstants.ACTION_LIST_COLOR_PALETTE.get(preferences.getColorDisapproved()));
        } else if (KewApiConstants.ROUTE_HEADER_ENROUTE_CD.equalsIgnoreCase(routeHeader.getDocRouteStatus())) {
            setRowStyleClass(KewApiConstants.ACTION_LIST_COLOR_PALETTE.get(preferences.getColorEnroute()));
        } else if (KewApiConstants.ROUTE_HEADER_EXCEPTION_CD.equalsIgnoreCase(routeHeader.getDocRouteStatus())) {
            setRowStyleClass(KewApiConstants.ACTION_LIST_COLOR_PALETTE.get(preferences.getColorException()));
        } else if (KewApiConstants.ROUTE_HEADER_FINAL_CD.equalsIgnoreCase(routeHeader.getDocRouteStatus())) {
            setRowStyleClass(KewApiConstants.ACTION_LIST_COLOR_PALETTE.get(preferences.getColorFinal()));
        } else if (KewApiConstants.ROUTE_HEADER_INITIATED_CD.equalsIgnoreCase(routeHeader.getDocRouteStatus())) {
            setRowStyleClass(KewApiConstants.ACTION_LIST_COLOR_PALETTE.get(preferences.getColorInitiated()));
        } else if (KewApiConstants.ROUTE_HEADER_PROCESSED_CD.equalsIgnoreCase(routeHeader.getDocRouteStatus())) {
            setRowStyleClass(KewApiConstants.ACTION_LIST_COLOR_PALETTE.get(preferences.getColorProcessed()));
        } else if (KewApiConstants.ROUTE_HEADER_SAVED_CD.equalsIgnoreCase(routeHeader.getDocRouteStatus())) {
            setRowStyleClass(KewApiConstants.ACTION_LIST_COLOR_PALETTE.get(preferences.getColorSaved()));
        }
    }

    private void initializeGroupName() {
        if (!groupNameInitialized) {
            if (getGroupId() != null) {
                Group group = super.getGroup();
                this.groupName = group.getName();
            }
            groupNameInitialized = true;
        }
    }

    private void initializeDelegatorName() {
        if (!delegatorNameInitialized) {
            if (getDelegatorPrincipalId() != null) {
                EntityNamePrincipalName name = KimApiServiceLocator.getIdentityService().getDefaultNamesForPrincipalId(getDelegatorPrincipalId());
                if (name != null) {
                    this.delegatorName = name.getDefaultName().getCompositeName();
                }
            }
            if (getDelegatorGroupId() != null) {
                Group delegatorGroup = KimApiServiceLocator.getGroupService().getGroup(getDelegatorGroupId());
                if (delegatorGroup !=null)
                    delegatorName = delegatorGroup.getName();
            }
            delegatorNameInitialized = true;
        }
    }

    private void initializeLastApprovedDate() {
        if (!lastApprovedDateInitialized) {
            this.lastApprovedDate = KEWServiceLocator.getActionTakenService().getLastApprovedDate(getDocumentId());
            lastApprovedDateInitialized = true;
        }
    }

	public DisplayParameters getDisplayParameters() {
		return displayParameters;
	}

	public void setDisplayParameters(DisplayParameters displayParameters) {
		this.displayParameters = displayParameters;
	}

	public DocumentRouteHeaderValueActionListExtension getRouteHeader() {
		return this.routeHeader;
	}

	public void setRouteHeader(DocumentRouteHeaderValueActionListExtension routeHeader) {
		this.routeHeader = routeHeader;
	}

}

