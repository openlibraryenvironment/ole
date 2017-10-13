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
package org.kuali.rice.kew.actionlist.web;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.actionlist.ActionToTake;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.WebFriendlyRecipient;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.kns.web.ui.ExtraButton;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Struts form for action ActionListAction
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionListForm extends KualiForm {

    private static final long serialVersionUID = -6246391732337228007L;

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

    // "sticky" parameters for paginated action list
    private Integer currentPage;
    private String currentSort;
    private String currentDir;

    // parameters for paginated action list
    private Integer page;
    private String sort;
    private String dir;

    private int count;
    private String cssFile = "kuali.css";
    private String logoAlign = "left";
    private String viewOutbox;
    private String[] outboxItems;
    private boolean outBoxEmpty;
    private Boolean showOutbox;
    private List<ExtraButton> headerButtons = new ArrayList<ExtraButton>();

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

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(String currentDir) {
        this.currentDir = currentDir;
    }

    public String getCurrentSort() {
        return currentSort;
    }

    public void setCurrentSort(String currentSort) {
        this.currentSort = currentSort;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public List<ExtraButton> getHeaderButtons() {
        return this.headerButtons;
    }

    public void setHeaderButtons(List<ExtraButton> headerButtons) {
        this.headerButtons = headerButtons;
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
    public void populate(HttpServletRequest request) {
        setHeaderButtons(getHeaderButtons());

        // take the UserSession from the HttpSession and add it to the request
        request.setAttribute(KRADConstants.USER_SESSION_KEY, GlobalVariables.getUserSession());

        //refactor actionlist.jsp not to be dependent on this
        request.setAttribute("preferences", GlobalVariables.getUserSession().retrieveObject(KewApiConstants.PREFERENCES));

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
            request.setAttribute("helpDeskActionList", "true");
        }
        //String routeLogPopup = "false";
        //boolean routeLogPopupInd = Utilities.getKNSParameterBooleanValue(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ACTION_LIST_DETAIL_TYPE, KewApiConstants.ACTION_LIST_ROUTE_LOG_POPUP_IND);
        //if (routeLogPopupInd) {
        //	routeLogPopup = "true";
        //}
        //String documentPopup = "false";
        //boolean documentPopupInd = Utilities.getKNSParameterBooleanValue(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ACTION_LIST_DETAIL_TYPE, KewApiConstants.ACTION_LIST_DOCUMENT_POPUP_IND);
        //if (documentPopupInd) {
        //    documentPopup = "true";
        //}
        setRouteLogPopup(CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(
                KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ACTION_LIST_DETAIL_TYPE,
                KewApiConstants.ACTION_LIST_ROUTE_LOG_POPUP_IND));
        setDocumentPopup(CoreFrameworkServiceLocator.getParameterService().getParameterValueAsBoolean(
                KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.ACTION_LIST_DETAIL_TYPE,
                KewApiConstants.ACTION_LIST_DOCUMENT_POPUP_IND));
        request.setAttribute("noRefresh", Boolean.valueOf(ConfigContext.getCurrentContextConfig().getProperty(
                KewApiConstants.ACTION_LIST_NO_REFRESH)));
        super.populate(request);
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

}
