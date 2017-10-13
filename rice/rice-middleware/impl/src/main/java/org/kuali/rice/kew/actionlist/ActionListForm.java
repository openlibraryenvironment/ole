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

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.actionitem.ActionItemActionListExtension;
import org.kuali.rice.kew.actionlist.ActionToTake;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.util.WebFriendlyRecipient;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.displaytag.pagination.PaginatedList;
import org.kuali.rice.kns.web.ui.ExtraButton;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.rice.kew.api.action.ActionItem;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Action List form implementation for the action list.
 *
 * <p>
 *   Holds properties necessary to determine the {@code View} instance that
 *   will be used to render the UI for the action list.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionListForm extends UifFormBase {

    private static final long serialVersionUID = -6246391732337228107L;

    private String delegator;
    private String methodToCall = "";
    private String helpDeskActionListUserName;
    private String docType;
    private String filterLegend;
    private String actionListType;
    private Boolean customActionList;
    private String defaultActionToTake;
    private List<ActionToTake> actionsToTake = new ArrayList<ActionToTake>();
    private Map<?, ?> defaultActions = new HashMap<Object, Object>();
    private String delegationId;
    private List<?> delegators;
    private Boolean hasCustomActions;
    private Boolean routeLogPopup;
    private Boolean documentPopup;
    private List<WebFriendlyRecipient> primaryDelegates;
    private String primaryDelegateId;
    private Boolean hasDisplayParameters;

    private String cssFile = "kuali.css";
    private String logoAlign = "left";
    private String viewOutbox;
    private String[] outboxItems;
    private boolean outBoxEmpty;
    private Boolean showOutbox;

    //moved from session
    private boolean requeryActionList;
    private Preferences preferences;
    private boolean updateActionList;
    private boolean outBoxMode;
    private Principal helpDeskActionListPrincipal;
    private Person helpDeskActionListPerson;

    //moved from filter
    private static String CREATE_DATE_FROM = "createDateFrom";
    private static String CREATE_DATE_TO = "createDateTo";
    private static String LAST_ASSIGNED_DATE_FROM = "lastAssignedDateFrom";
    private static String LAST_ASSIGNED_DATE_TO = "lastAssignedDateTo";
    private ActionListFilter filter;
    private ActionListFilter oldFilter;
    private String createDateFrom;
    private String createDateTo;
    private String lastAssignedDateTo;
    private String lastAssignedDateFrom;
    private String lookupableImplServiceName;
    private String lookupType;
    private String docTypeFullName;
    private List userWorkgroups;

    public String getCreateDateTo() {
        return createDateTo;
    }
    public void setCreateDateTo(String createDateTo) {
        if(createDateTo == null){
            createDateTo = "";
        }
        else{
            this.createDateTo = createDateTo.trim();
        }
    }

    public String getLastAssignedDateFrom() {
        return lastAssignedDateFrom;
    }

    public void setLastAssignedDateFrom(String lastAssignedDateFrom) {
        if(lastAssignedDateFrom == null){
            lastAssignedDateFrom = "";
        }
        else{
            this.lastAssignedDateFrom = lastAssignedDateFrom.trim();
        }
    }

    public String getCreateDateFrom() {
        return createDateFrom;
    }

    public void setCreateDateFrom(String createDate) {
        if(createDate == null){
            createDate = "";
        }
        else{
            this.createDateFrom = createDate.trim();
        }
    }

    public ActionListFilter getFilter() {
        return filter;
    }

    public void setFilter(ActionListFilter filter) {
        this.filter = filter;
        if (filter.getCreateDateFrom() != null) {
            setCreateDateFrom(RiceConstants.getDefaultDateFormat().format(filter.getCreateDateFrom()));
        }
        if (filter.getCreateDateTo() != null) {
            setCreateDateTo(RiceConstants.getDefaultDateFormat().format(filter.getCreateDateTo()));
        }
        if (filter.getLastAssignedDateFrom() != null) {
            setLastAssignedDateFrom(RiceConstants.getDefaultDateFormat().format(filter.getLastAssignedDateFrom()));
        }
        if (filter.getLastAssignedDateTo() != null) {
            setLastAssignedDateTo(RiceConstants.getDefaultDateFormat().format(filter.getLastAssignedDateTo()));
        }
    }

    public String getLastAssignedDateTo() {
        return lastAssignedDateTo;
    }

    public void setLastAssignedDateTo(String lastAssignedDate) {
        if(lastAssignedDate == null){
            lastAssignedDate = "";
        }
        else{
            this.lastAssignedDateTo = lastAssignedDate.trim();
        }
    }

    public ActionListFilter getOldFilter() {
        return oldFilter;
    }

    public void setOldFilter(ActionListFilter oldFilter) {
        this.oldFilter = oldFilter;
    }

    public void validateDates() {
        //List errors = new ArrayList();
        //ActionErrors errors = new ActionErrors();
        if (getCreateDateFrom() != null && getCreateDateFrom().length() != 0) {
            try {
                RiceConstants.getDefaultDateFormat().parse(getCreateDateFrom());
            } catch (ParseException e) {
                GlobalVariables.getMessageMap().putError(CREATE_DATE_FROM, "general.error.fieldinvalid", "Create Date From");
            }
        }
        if (getCreateDateTo() != null && getCreateDateTo().length() != 0) {
            try {
                RiceConstants.getDefaultDateFormat().parse(getCreateDateTo());
            } catch (ParseException e) {
                GlobalVariables.getMessageMap().putError(CREATE_DATE_TO, "general.error.fieldinvalid", "Create Date To");
            }
        }
        if (getLastAssignedDateFrom() != null && getLastAssignedDateFrom().length() != 0) {
            try {
                RiceConstants.getDefaultDateFormat().parse(getLastAssignedDateFrom());
            } catch (ParseException e1) {
                GlobalVariables.getMessageMap().putError(LAST_ASSIGNED_DATE_FROM, "general.error.fieldinvalid", "Last Assigned Date From");
            }
        }
        if (getLastAssignedDateTo() != null && getLastAssignedDateTo().length() != 0) {
            try {
                RiceConstants.getDefaultDateFormat().parse(getLastAssignedDateTo());
            } catch (ParseException e1) {
                GlobalVariables.getMessageMap().putError(LAST_ASSIGNED_DATE_TO, "general.error.fieldinvalid", "Last Assigned Date To");
            }
        }
    }

    public ActionListFilter getLoadedFilter()/* throws ParseException*/ {
        try {
            if (getCreateDateFrom() != null && getCreateDateFrom().length() != 0) {
                filter.setCreateDateFrom(RiceConstants.getDefaultDateFormat().parse(getCreateDateFrom()));
            }
            if (getCreateDateTo() != null && getCreateDateTo().length() != 0) {
                filter.setCreateDateTo(RiceConstants.getDefaultDateFormat().parse(getCreateDateTo()));
            }
            if (getLastAssignedDateFrom() != null && getLastAssignedDateFrom().length() != 0) {
                filter.setLastAssignedDateFrom(RiceConstants.getDefaultDateFormat().parse(getLastAssignedDateFrom()));
            }
            if (getLastAssignedDateTo() != null && getLastAssignedDateTo().length() != 0) {
                filter.setLastAssignedDateTo(RiceConstants.getDefaultDateFormat().parse(getLastAssignedDateTo()));
            }
            if (getDocTypeFullName() != null && ! "".equals(getDocTypeFullName())) {
                filter.setDocumentType(getDocTypeFullName());
            }
        } catch (ParseException e) {
            //error caught and displayed in validateDates()
        }

        return filter;
    }

    public String getLookupableImplServiceName() {
        return lookupableImplServiceName;
    }

    public void setLookupableImplServiceName(String lookupableImplServiceName) {
        this.lookupableImplServiceName = lookupableImplServiceName;
    }

    public String getLookupType() {
        return lookupType;
    }

    public void setLookupType(String lookupType) {
        this.lookupType = lookupType;
    }

    public String getDocTypeFullName() {
        return docTypeFullName;
    }

    public void setDocTypeFullName(String docTypeFullName) {
        this.docTypeFullName = docTypeFullName;
    }

    public List getUserWorkgroups() {
        return userWorkgroups;
    }

    public void setUserWorkgroups(List userWorkgroups) {
        this.userWorkgroups = userWorkgroups;
    }

    public Principal getHelpDeskActionListPrincipal() {
        return helpDeskActionListPrincipal;
    }

    public void setHelpDeskActionListPrincipal(Principal helpDeskActionListPrincipal) {
        this.helpDeskActionListPrincipal = helpDeskActionListPrincipal;
    }

    public Person getHelpDeskActionListPerson() {
        return helpDeskActionListPerson;
    }

    public void setHelpDeskActionListPerson(Person helpDeskActionListPerson) {
        this.helpDeskActionListPerson = helpDeskActionListPerson;
    }

    public boolean isOutBoxMode() {
        return outBoxMode;
    }

    public void setOutBoxMode(boolean outBoxMode) {
        this.outBoxMode = outBoxMode;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    private String user;

    public boolean isNoRefresh() {
        return noRefresh;
    }

    public void setNoRefresh(boolean noRefresh) {
        this.noRefresh = noRefresh;
    }

    private boolean noRefresh;

    public boolean isHelpDeskActionList() {
        return helpDeskActionList;
    }

    public void setHelpDeskActionList(boolean helpDeskActionList) {
        this.helpDeskActionList = helpDeskActionList;
    }

    private boolean helpDeskActionList;
    private List<ActionItemActionListExtension> actionList;
    private List<ActionItem> ApiActionItems;

    public List<ActionItemActionListExtension> getActionList() {
        return actionList;
    }
    public void setActionList(ArrayList<ActionItemActionListExtension> actionList) {
        this.actionList = actionList;
    }

    public boolean isRequeryActionList() {
        return requeryActionList;
    }

    public void setRequeryActionList(boolean requeryActionList) {
        this.requeryActionList = requeryActionList;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public boolean isUpdateActionList() {
        return updateActionList;
    }

    public void setUpdateActionList(boolean updateActionList) {
        this.updateActionList = updateActionList;
    }

    public String getHelpDeskActionListUserName() {
        return helpDeskActionListUserName;
    }

    public void setHelpDeskActionListUserName(String helpDeskActionListUserName) {
        this.helpDeskActionListUserName = helpDeskActionListUserName;
    }

    @Override
    public String getMethodToCall() {
        return methodToCall;
    }

    @Override
    public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }

    public String getDelegator() {
        return delegator;
    }

    public void setDelegator(String delegator) {
        this.delegator = delegator;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getFilterLegend() {
        return filterLegend;
    }

    public void setFilterLegend(String filterLegend) {
        this.filterLegend = filterLegend;
    }

    public String getActionListType() {
        if (actionListType == null) {
            setActionListType("all");
        }
        return actionListType;
    }

    public void setActionListType(String actionListType) {
        this.actionListType = actionListType;
    }

    public Boolean getCustomActionList() {
        return customActionList;
    }

    public void setCustomActionList(Boolean customActionList) {
        this.customActionList = customActionList;
    }

    public String getDefaultActionToTake() {
        return defaultActionToTake;
    }

    public void setDefaultActionToTake(String defaultActionToTake) {
        this.defaultActionToTake = defaultActionToTake;
    }

    public List<ActionToTake> getActionsToTake() {
        return actionsToTake;
    }

    public void setActionsToTake(List<ActionToTake> actionsToTake) {
        this.actionsToTake = actionsToTake;
    }

    public ActionToTake getActions(int index) {
        while (getActionsToTake().size() <= index) {
            getActionsToTake().add(new ActionToTake());
        }
        return getActionsToTake().get(index);
    }

    public Map<?, ?> getDefaultActions() {
        return defaultActions;
    }

    public void setDefaultActions(Map<?, ?> defaultActions) {
        this.defaultActions = defaultActions;
    }

    public String getDelegationId() {
        return delegationId;
    }

    public void setDelegationId(String delegationId) {
        this.delegationId = delegationId;
    }

    public List<?> getDelegators() {
        return delegators;
    }

    public void setDelegators(List<?> delegators) {
        this.delegators = delegators;
    }

    public Boolean getHasCustomActions() {
        return hasCustomActions;
    }

    public void setHasCustomActions(Boolean hasCustomActions) {
        this.hasCustomActions = hasCustomActions;
    }

    public String getCssFile() {
        return cssFile;
    }

    public void setCssFile(String cssFile) {
        this.cssFile = cssFile;
    }

    public String getLogoAlign() {
        return logoAlign;
    }

    public void setLogoAlign(String logoAlign) {
        this.logoAlign = logoAlign;
    }

    public String getViewOutbox() {
        return this.viewOutbox;
    }

    public void setViewOutbox(String viewOutbox) {
        this.viewOutbox = viewOutbox;
    }

    public String[] getOutboxItems() {
        return outboxItems;
    }

    public void setOutboxItems(String[] outboxItems) {
        this.outboxItems = outboxItems;
    }

    public boolean isOutBoxEmpty() {
        return this.outBoxEmpty;
    }

    public void setOutBoxEmpty(boolean outBoxEmpty) {
        this.outBoxEmpty = outBoxEmpty;
    }

    public Boolean getShowOutbox() {
        return this.showOutbox;
    }

    public void setShowOutbox(Boolean showOutbox) {
        this.showOutbox = showOutbox;
    }

    public String getMenuBar() {
        String url = "";
        Properties parameters = new Properties();
        url = UrlFactory.parameterizeUrl(KRADConstants.MAINTENANCE_ACTION, parameters);
        String krBaseUrl = ConfigContext.getCurrentContextConfig().getKRBaseURL();
        url = "<a href=\""
                + url
                + "\"><img src=\""
                + krBaseUrl
                + "/images/tinybutton-preferences.gif\" alt=\"create new\" width=\"70\" height=\"15\"/></a>";

        return url;
    }

    @Override
    public void postBind(HttpServletRequest request) {

        //add the preferences to the form
        setPreferences((Preferences)(GlobalVariables.getUserSession().retrieveObject(KewApiConstants.PREFERENCES)));

        String principalId = GlobalVariables.getUserSession().getPrincipalId();
        final Principal hdalPrinc = (Principal) GlobalVariables.getUserSession().retrieveObject(
                KewApiConstants.HELP_DESK_ACTION_LIST_PRINCIPAL_ATTR_NAME);
        if (hdalPrinc != null) {
            setHelpDeskActionListUserName(hdalPrinc.getPrincipalName());
        }
        boolean isHelpDeskAuthorized = KimApiServiceLocator.getPermissionService().isAuthorized(principalId,
                KewApiConstants.KEW_NAMESPACE, KewApiConstants.PermissionNames.VIEW_OTHER_ACTION_LIST,
                new HashMap<String, String>());
        if (isHelpDeskAuthorized) {
            setHelpDeskActionList(true);
        }

        super.postBind(request);
    }

    public Boolean getRouteLogPopup() {
        return this.routeLogPopup;
    }

    public Boolean getDocumentPopup() {
        return this.documentPopup;
    }

    public void setRouteLogPopup(Boolean routeLogPopup) {
        this.routeLogPopup = routeLogPopup;
    }

    public void setDocumentPopup(Boolean documentPopup) {
        this.documentPopup = documentPopup;
    }

    public Boolean getHasDisplayParameters() {
        return this.hasDisplayParameters;
    }

    public void setHasDisplayParameters(Boolean hasDisplayParameters) {
        this.hasDisplayParameters = hasDisplayParameters;
    }

    public List<WebFriendlyRecipient> getPrimaryDelegates() {
        return this.primaryDelegates;
    }

    public void setPrimaryDelegates(List<WebFriendlyRecipient> primaryDelegates) {
        this.primaryDelegates = primaryDelegates;
    }

    public String getPrimaryDelegateId() {
        return this.primaryDelegateId;
    }

    public void setPrimaryDelegateId(String primaryDelegateId) {
        this.primaryDelegateId = primaryDelegateId;
    }

    // convert a List of org.kuali.rice.kew.actionitem.ActionItemS to org.kuali.rice.kew.api.action.ActionItemS
    public List<ActionItem> getApiActionList() {
        List<org.kuali.rice.kew.api.action.ActionItem> apiActionItems = new ArrayList<org.kuali.rice.kew.api.action.ActionItem>(actionList.size());

        for (ActionItemActionListExtension actionItemObj : actionList) {
            apiActionItems.add(
                    org.kuali.rice.kew.api.action.ActionItem.Builder.create(actionItemObj).build());
        }

        return apiActionItems;
    }
}
