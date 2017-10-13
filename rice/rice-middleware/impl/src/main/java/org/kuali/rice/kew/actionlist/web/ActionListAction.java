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

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.struts.action.*;
import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import org.displaytag.util.LookupUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.web.format.DateFormatter;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionitem.ActionItemActionListExtension;
import org.kuali.rice.kew.actionitem.OutboxItemActionListExtension;
import org.kuali.rice.kew.actionlist.ActionListFilter;
import org.kuali.rice.kew.actionlist.ActionToTake;
import org.kuali.rice.kew.actionlist.PaginatedActionList;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.action.ActionInvocation;
import org.kuali.rice.kew.api.action.ActionItemCustomization;
import org.kuali.rice.kew.api.action.ActionSet;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.framework.KewFrameworkServiceLocator;
import org.kuali.rice.kew.framework.actionlist.ActionListCustomizationMediator;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValueActionListExtension;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.PerformanceLogger;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kns.web.struts.action.KualiAction;
import org.kuali.rice.kns.web.ui.ExtraButton;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Action doing Action list stuff
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)a
 *
 */
public class ActionListAction extends KualiAction {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ActionListAction.class);
    protected static final String MAX_ACTION_ITEM_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss.S";

    private static final String ACTION_LIST_KEY = "actionList";
    private static final String ACTION_LIST_PAGE_KEY = "actionListPage";
    private static final String ACTION_LIST_USER_KEY = "actionList.user";
    /*private static final String REQUERY_ACTION_LIST_KEY = "requeryActionList";*/
    private static final String ACTION_ITEM_COUNT_FOR_USER_KEY = "actionList.count";
    private static final String MAX_ACTION_ITEM_DATE_ASSIGNED_FOR_USER_KEY = "actionList.maxActionItemDateAssigned";

    private static final String ACTIONREQUESTCD_PROP = "actionRequestCd";
    private static final String CUSTOMACTIONLIST_PROP = "customActionList";
    private static final String ACTIONITEM_PROP = "actionitem";
    private static final String HELPDESK_ACTIONLIST_USERNAME = "helpDeskActionListUserName";

    private static final String ACTIONITEM_ACTIONREQUESTCD_INVALID_ERRKEY = "actionitem.actionrequestcd.invalid";
    private static final String ACTIONLIST_BAD_CUSTOM_ACTION_LIST_ITEMS_ERRKEY = "actionlist.badCustomActionListItems";
    private static final String ACTIONLIST_BAD_ACTION_ITEMS_ERRKEY = "actionlist.badActionItems";
    private static final String HELPDESK_LOGIN_EMPTY_ERRKEY = "helpdesk.login.empty";
    private static final String HELPDESK_LOGIN_INVALID_ERRKEY = "helpdesk.login.invalid";

    private static final ActionType [] actionListActionTypes =
            { ActionType.APPROVE, ActionType.DISAPPROVE, ActionType.CANCEL, ActionType.ACKNOWLEDGE, ActionType.FYI };

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionListForm frm = (ActionListForm)actionForm;
        request.setAttribute("Constants", getServlet().getServletContext().getAttribute("KewApiConstants"));
        request.setAttribute("preferences", getUserSession().retrieveObject(KewApiConstants.PREFERENCES));
        frm.setHeaderButtons(getHeaderButtons());
        return super.execute(mapping, actionForm, request, response);
    }

    private List<ExtraButton> getHeaderButtons(){
        List<ExtraButton> headerButtons = new ArrayList<ExtraButton>();
        ExtraButton eb = new ExtraButton();
        String krBaseUrl = ConfigContext.getCurrentContextConfig().getKRBaseURL();
        eb.setExtraButtonSource( krBaseUrl + "/images/tinybutton-preferences.gif");
        eb.setExtraButtonOnclick("Preferences.do?returnMapping=viewActionList");

        headerButtons.add(eb);
        eb = new ExtraButton();
        eb.setExtraButtonSource(krBaseUrl + "/images/tinybutton-refresh.gif");
        eb.setExtraButtonProperty("methodToCall.refresh");

        headerButtons.add(eb);
        eb = new ExtraButton();
        eb.setExtraButtonSource(krBaseUrl + "/images/tinybutton-filter.gif");
        eb.setExtraButtonOnclick("javascript: window.open('ActionListFilter.do?methodToCall=start');");
        headerButtons.add(eb);


        return headerButtons;
    }

    @Override
    public ActionForward refresh(ActionMapping mapping,
                                 ActionForm form,
                           		 HttpServletRequest request,
                           		 HttpServletResponse response) throws Exception {
        request.getSession().setAttribute(KewApiConstants.REQUERY_ACTION_LIST_KEY, "true");
        return start(mapping, form, request, response);
    }

    @Override
    protected ActionForward defaultDispatch(ActionMapping mapping,
                                            ActionForm form, HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        return start(mapping, form, request, response);
    }

    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PerformanceLogger plog = new PerformanceLogger();
        plog.log("Starting ActionList fetch");
        ActionListForm form = (ActionListForm) actionForm;
        ActionListService actionListSrv = KEWServiceLocator.getActionListService();


        // process display tag parameters
        Integer page = form.getPage();
        String sortCriterion = form.getSort();
        SortOrderEnum sortOrder = SortOrderEnum.ASCENDING;
        final UserSession uSession = getUserSession();

        if (form.getDir() != null) {
            sortOrder = parseSortOrder(form.getDir());
        }
        else if ( !StringUtils.isEmpty((String) uSession.retrieveObject(KewApiConstants.SORT_ORDER_ATTR_NAME)))     {
            sortOrder = parseSortOrder((String) uSession.retrieveObject(KewApiConstants.SORT_ORDER_ATTR_NAME));
        }
        // if both the page and the sort criteria are null, that means its the first entry into the page, use defaults
        if (page == null && sortCriterion == null) {
            page = 1;
            sortCriterion = ActionItemComparator.ACTION_LIST_DEFAULT_SORT;
        }
        else if ( !StringUtils.isEmpty((String) uSession.retrieveObject(KewApiConstants.SORT_CRITERIA_ATTR_NAME)))     {
            sortCriterion = (String) uSession.retrieveObject(KewApiConstants.SORT_CRITERIA_ATTR_NAME);
        }
        // if the page is still null, that means the user just performed a sort action, pull the currentPage off of the form
        if (page == null) {
            page = form.getCurrentPage();
        }

        // update the values of the "current" display tag parameters
        form.setCurrentPage(page);
        if (!StringUtils.isEmpty(sortCriterion)) {
            form.setCurrentSort(sortCriterion);
            form.setCurrentDir(getSortOrderValue(sortOrder));
        }

        // reset the default action on the form
        form.setDefaultActionToTake("NONE");

        boolean freshActionList = true;
        // retrieve cached action list
        List<? extends ActionItemActionListExtension> actionList = (List<? extends ActionItemActionListExtension>)request.getSession().getAttribute(ACTION_LIST_KEY);
        plog.log("Time to initialize");
        try {
            //UserSession uSession = getUserSession(request);
            String principalId = null;
            if (uSession.retrieveObject(KewApiConstants.ACTION_LIST_FILTER_ATTR_NAME) == null) {
                ActionListFilter filter = new ActionListFilter();
                filter.setDelegationType(DelegationType.SECONDARY.getCode());
                filter.setExcludeDelegationType(true);
                uSession.addObject(KewApiConstants.ACTION_LIST_FILTER_ATTR_NAME, filter);
            }

            final ActionListFilter filter = (ActionListFilter) uSession.retrieveObject(KewApiConstants.ACTION_LIST_FILTER_ATTR_NAME);
            /* 'forceListRefresh' variable used to signify that the action list filter has changed
             * any time the filter changes the action list must be refreshed or filter may not take effect on existing
             * list items... only exception is if action list has not loaded previous and fetching of the list has not
             * occurred yet
             */
            boolean forceListRefresh = request.getSession().getAttribute(KewApiConstants.REQUERY_ACTION_LIST_KEY) != null;
            if (uSession.retrieveObject(KewApiConstants.HELP_DESK_ACTION_LIST_PRINCIPAL_ATTR_NAME) != null) {
                principalId = ((PrincipalContract) uSession.retrieveObject(KewApiConstants.HELP_DESK_ACTION_LIST_PRINCIPAL_ATTR_NAME)).getPrincipalId();
            } else {
                if (!StringUtils.isEmpty(form.getDocType())) {
                    filter.setDocumentType(form.getDocType());
                    filter.setExcludeDocumentType(false);
                    forceListRefresh = true;
                }
                principalId = uSession.getPerson().getPrincipalId();
            }

            final Preferences preferences = (Preferences) getUserSession().retrieveObject(KewApiConstants.PREFERENCES);

            if (!StringUtils.isEmpty(form.getDelegationId())) {
                if (!KewApiConstants.DELEGATION_DEFAULT.equals(form.getDelegationId())) {
                    // If the user can filter by both primary and secondary delegation, and both drop-downs have non-default values assigned,
                    // then reset the primary delegation drop-down's value when the primary delegation drop-down's value has remained unaltered
                    // but the secondary drop-down's value has been altered; but if one of these alteration situations does not apply, reset the
                    // secondary delegation drop-down.
                    if (StringUtils.isNotBlank(form.getPrimaryDelegateId()) && !KewApiConstants.PRIMARY_DELEGATION_DEFAULT.equals(form.getPrimaryDelegateId())){
                        if (form.getPrimaryDelegateId().equals(request.getParameter("oldPrimaryDelegateId")) &&
                                !form.getDelegationId().equals(request.getParameter("oldDelegationId"))) {
                            form.setPrimaryDelegateId(KewApiConstants.PRIMARY_DELEGATION_DEFAULT);
                        } else {
                            form.setDelegationId(KewApiConstants.DELEGATION_DEFAULT);
                        }
                    } else if (StringUtils.isNotBlank(filter.getPrimaryDelegateId()) &&
                            !KewApiConstants.PRIMARY_DELEGATION_DEFAULT.equals(filter.getPrimaryDelegateId())) {
                        // If the primary delegation drop-down is invisible but a primary delegation filter is in place, and if the secondary delegation
                        // drop-down has a non-default value selected, then reset the primary delegation filtering.
                        filter.setPrimaryDelegateId(KewApiConstants.PRIMARY_DELEGATION_DEFAULT);
                    }
                }
                // Enable the secondary delegation filtering.
                filter.setDelegatorId(form.getDelegationId());
                filter.setExcludeDelegatorId(false);
                actionList = null;
            }

            if (!StringUtils.isEmpty(form.getPrimaryDelegateId())) {
                // If the secondary delegation drop-down is invisible but a secondary delegation filter is in place, and if the primary delegation
                // drop-down has a non-default value selected, then reset the secondary delegation filtering.
                if (StringUtils.isBlank(form.getDelegationId()) && !KewApiConstants.PRIMARY_DELEGATION_DEFAULT.equals(form.getPrimaryDelegateId()) &&
                        StringUtils.isNotBlank(filter.getDelegatorId()) &&
                        !KewApiConstants.DELEGATION_DEFAULT.equals(filter.getDelegatorId())) {
                    filter.setDelegatorId(KewApiConstants.DELEGATION_DEFAULT);
                }
                // Enable the primary delegation filtering.
                filter.setPrimaryDelegateId(form.getPrimaryDelegateId());
                filter.setExcludeDelegatorId(false);
                actionList = null;
            }

            // if the user has changed, we need to refresh the action list
            if (!principalId.equals(request.getSession().getAttribute(ACTION_LIST_USER_KEY))) {
                actionList = null;
            }

            if (isOutboxMode(form, request, preferences)) {
                actionList = new ArrayList<OutboxItemActionListExtension>(actionListSrv.getOutbox(principalId, filter));
                form.setOutBoxEmpty(actionList.isEmpty());
            } else {

                    SimpleDateFormat dFormatter = new SimpleDateFormat(MAX_ACTION_ITEM_DATE_FORMAT);
                    if (actionList == null) {
                        List<Object> countAndMaxDate = actionListSrv.getMaxActionItemDateAssignedAndCountForUser(principalId);
                        if (countAndMaxDate.isEmpty() || countAndMaxDate.get(0) == null ) {
                            if (countAndMaxDate.isEmpty()) {
                                countAndMaxDate.add(0, new Date(0));
                                countAndMaxDate.add(1, 0);
                            } else {
                                countAndMaxDate.set(0, new Date(0));
                            }
                        }
                        request.getSession().setAttribute(MAX_ACTION_ITEM_DATE_ASSIGNED_FOR_USER_KEY, dFormatter.format(countAndMaxDate.get(0)));
                        request.getSession().setAttribute(ACTION_ITEM_COUNT_FOR_USER_KEY, (Integer)countAndMaxDate.get(1));
                        // fetch the action list
                        actionList = new ArrayList<ActionItemActionListExtension>(actionListSrv.getActionList(principalId, filter));

                        request.getSession().setAttribute(ACTION_LIST_USER_KEY, principalId);
                    } else if (forceListRefresh) {
                        // force a refresh... usually based on filter change or parameter specifying refresh needed
                        actionList = new ArrayList<ActionItemActionListExtension>(actionListSrv.getActionList(principalId, filter));
                        request.getSession().setAttribute(ACTION_LIST_USER_KEY, principalId);
                        List<Object> countAndMaxDate = actionListSrv.getMaxActionItemDateAssignedAndCountForUser(principalId);
                        if (countAndMaxDate.isEmpty() || countAndMaxDate.get(0) == null ) {
                            if (countAndMaxDate.isEmpty()) {
                                countAndMaxDate.add(0, new Date(0));
                                countAndMaxDate.add(1, 0);
                            } else {
                                countAndMaxDate.set(0, new Date(0));
                            }
                        }
                        request.getSession().setAttribute(MAX_ACTION_ITEM_DATE_ASSIGNED_FOR_USER_KEY, dFormatter.format(countAndMaxDate.get(0)));
                        request.getSession().setAttribute(ACTION_ITEM_COUNT_FOR_USER_KEY, (Integer)countAndMaxDate.get(1));

                    }else if (refreshList(request,principalId)){
                        actionList = new ArrayList<ActionItemActionListExtension>(actionListSrv.getActionList(principalId, filter));
                        request.getSession().setAttribute(ACTION_LIST_USER_KEY, principalId);

                    } else {
                        Boolean update = (Boolean) uSession.retrieveObject(KewApiConstants.UPDATE_ACTION_LIST_ATTR_NAME);
                        if (update == null || !update) {
                            freshActionList = false;
                        }
                    }
                    request.getSession().setAttribute(ACTION_LIST_KEY, actionList);

            }
            // reset the requery action list key
            request.getSession().setAttribute(KewApiConstants.REQUERY_ACTION_LIST_KEY, null);

            // build the drop-down of delegators
            if (KewApiConstants.DELEGATORS_ON_ACTION_LIST_PAGE.equalsIgnoreCase(preferences.getDelegatorFilter())) {
                Collection<Recipient> delegators = actionListSrv.findUserSecondaryDelegators(principalId);
                form.setDelegators(ActionListUtil.getWebFriendlyRecipients(delegators));
                form.setDelegationId(filter.getDelegatorId());
            }

            // Build the drop-down of primary delegates.
            if (KewApiConstants.PRIMARY_DELEGATES_ON_ACTION_LIST_PAGE.equalsIgnoreCase(preferences.getPrimaryDelegateFilter())) {
                Collection<Recipient> pDelegates = actionListSrv.findUserPrimaryDelegations(principalId);
                form.setPrimaryDelegates(ActionListUtil.getWebFriendlyRecipients(pDelegates));
                form.setPrimaryDelegateId(filter.getPrimaryDelegateId());
            }

            form.setFilterLegend(filter.getFilterLegend());
            plog.log("Setting attributes");

            int pageSize = getPageSize(preferences);
            // initialize the action list if necessary
            if (freshActionList) {
                plog.log("calling initializeActionList");
                initializeActionList(actionList, preferences);
                plog.log("done w/ initializeActionList");
                // put this in to resolve EN-112 (http://beatles.uits.indiana.edu:8081/jira/browse/EN-112)
                // if the action list gets "refreshed" in between page switches, we need to be sure and re-sort it, even though we don't have sort criteria on the request
                if (sortCriterion == null) {
                    sortCriterion = form.getCurrentSort();
                    sortOrder = parseSortOrder(form.getCurrentDir());
                }
            }
            // sort the action list if necessary
            if (sortCriterion != null) {
                sortActionList(actionList, sortCriterion, sortOrder);
            }

            plog.log("calling buildCurrentPage");
            PaginatedList currentPage = buildCurrentPage(actionList, form.getCurrentPage(), form.getCurrentSort(),
                    form.getCurrentDir(), pageSize, preferences, form);
            plog.log("done w/ buildCurrentPage");
            request.setAttribute(ACTION_LIST_PAGE_KEY, currentPage);
            synchronized(uSession) {
                uSession.addObject(KewApiConstants.UPDATE_ACTION_LIST_ATTR_NAME, Boolean.FALSE);
                uSession.addObject(KewApiConstants.CURRENT_PAGE_ATTR_NAME, form.getCurrentPage());
                uSession.addObject(KewApiConstants.SORT_CRITERIA_ATTR_NAME, form.getSort());
                uSession.addObject(KewApiConstants.SORT_ORDER_ATTR_NAME, form.getCurrentDir());
            }
            plog.log("finished setting attributes, finishing action list fetch");
        } catch (Exception e) {
            LOG.error("Error loading action list.", e);
        }

        LOG.debug("end start ActionListAction");
        return mapping.findForward("viewActionList");
    }

    /**
     * Sets the maxActionItemDate and actionItemcount for user in the session
     * @param request
     * @param principalId
     * @param actionListSrv
     */
    private void setCountAndMaxDate(HttpServletRequest request,String principalId,ActionListService actionListSrv ){
        SimpleDateFormat dFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        List<Object> countAndMaxDate = actionListSrv.getMaxActionItemDateAssignedAndCountForUser(principalId);
        String maxActionItemDateAssignedForUserKey = "";
        if(countAndMaxDate.get(0)!= null){
           maxActionItemDateAssignedForUserKey = dFormatter.format(countAndMaxDate.get(0));
        }
        request.getSession().setAttribute(MAX_ACTION_ITEM_DATE_ASSIGNED_FOR_USER_KEY, maxActionItemDateAssignedForUserKey);
        request.getSession().setAttribute(ACTION_ITEM_COUNT_FOR_USER_KEY, (Integer)countAndMaxDate.get(1));
    }

    private boolean refreshList(HttpServletRequest request,String principalId ){
        List<Object> maxActionItemDateAssignedAndCount = KEWServiceLocator.getActionListService().getMaxActionItemDateAssignedAndCountForUser(
                principalId);
        int count = (Integer) maxActionItemDateAssignedAndCount.get(1);
        int previousCount = 0;
        Object actionItemCountFromSession = request.getSession().getAttribute(ACTION_ITEM_COUNT_FOR_USER_KEY);
        if ( actionItemCountFromSession != null ) {
            previousCount = Integer.parseInt(actionItemCountFromSession.toString());
        }
        SimpleDateFormat dFormatter = new SimpleDateFormat(MAX_ACTION_ITEM_DATE_FORMAT);
        Date maxActionItemDateAssigned = (Date) maxActionItemDateAssignedAndCount.get(0);
        if ( maxActionItemDateAssigned == null ) {
            maxActionItemDateAssigned = new Date(0);
        }
        Date previousMaxActionItemDateAssigned= null;
        try{
            Object dateAttributeFromSession = request.getSession().getAttribute(MAX_ACTION_ITEM_DATE_ASSIGNED_FOR_USER_KEY);
            if ( dateAttributeFromSession != null ) {
                previousMaxActionItemDateAssigned = dFormatter.parse(dateAttributeFromSession.toString());
            }
        } catch (ParseException e){
            LOG.warn( MAX_ACTION_ITEM_DATE_ASSIGNED_FOR_USER_KEY + " in session did not have expected date format.  "
                    + "Was: " + request.getSession().getAttribute(MAX_ACTION_ITEM_DATE_ASSIGNED_FOR_USER_KEY), e );
        }
        if(previousCount!= count){
            request.getSession().setAttribute(MAX_ACTION_ITEM_DATE_ASSIGNED_FOR_USER_KEY, dFormatter.format(
                    maxActionItemDateAssigned));
            request.getSession().setAttribute(ACTION_ITEM_COUNT_FOR_USER_KEY, count);
            return true;
        }else if(previousMaxActionItemDateAssigned == null || previousMaxActionItemDateAssigned.compareTo(maxActionItemDateAssigned)!=0){
            request.getSession().setAttribute(MAX_ACTION_ITEM_DATE_ASSIGNED_FOR_USER_KEY, dFormatter.format(
                    maxActionItemDateAssigned));
            request.getSession().setAttribute(ACTION_ITEM_COUNT_FOR_USER_KEY, count);
            return true;
        } else{
            return false;
        }

    }

    private SortOrderEnum parseSortOrder(String dir) throws WorkflowException {
        if ("asc".equals(dir)) {
            return SortOrderEnum.ASCENDING;
        } else if ("desc".equals(dir)) {
            return SortOrderEnum.DESCENDING;
        }
        throw new WorkflowException("Invalid sort direction: " + dir);
    }

    private String getSortOrderValue(SortOrderEnum sortOrder) {
        if (SortOrderEnum.ASCENDING.equals(sortOrder)) {
            return "asc";
        } else if (SortOrderEnum.DESCENDING.equals(sortOrder)) {
            return "desc";
        }
        return null;
    }

    private static final String OUT_BOX_MODE = "_OUT_BOX_MODE";

    /**
     * this method is setting 2 props on the {@link ActionListForm} that controls outbox behavior.
     *  alForm.setViewOutbox("false"); -> this is set by user preferences and the actionlist.outbox.off config prop
     *  alForm.setShowOutbox(false); -> this is set by user action clicking the ActionList vs. Outbox links.
     *
     * @param alForm
     * @param request
     * @return boolean indication whether the outbox should be fetched
     */
    private boolean isOutboxMode(ActionListForm alForm, HttpServletRequest request, Preferences preferences) {

        boolean outBoxView = false;

        if (! preferences.isUsingOutbox() || ! ConfigContext.getCurrentContextConfig().getOutBoxOn()) {
            request.getSession().setAttribute(OUT_BOX_MODE, Boolean.FALSE);
            alForm.setViewOutbox("false");
            alForm.setShowOutbox(false);
            return false;
        }

        alForm.setShowOutbox(true);
        if (StringUtils.isNotEmpty(alForm.getViewOutbox())) {
            if (!Boolean.valueOf(alForm.getViewOutbox())) {
                request.getSession().setAttribute(OUT_BOX_MODE, Boolean.FALSE);
                outBoxView = false;
            } else {
                request.getSession().setAttribute(OUT_BOX_MODE, Boolean.TRUE);
                outBoxView = true;
            }
        } else {

            if (request.getSession().getAttribute(OUT_BOX_MODE) == null) {
                outBoxView = false;
            } else {
                outBoxView = (Boolean) request.getSession().getAttribute(OUT_BOX_MODE);
            }
        }
        if (outBoxView) {
            alForm.setViewOutbox("true");
        } else {
            alForm.setViewOutbox("false");
        }
        return outBoxView;
    }

    private void sortActionList(List<? extends ActionItemActionListExtension> actionList, String sortName, SortOrderEnum sortOrder) {
        if (StringUtils.isEmpty(sortName)) {
            return;
        }
        Comparator<ActionItemActionListExtension> comparator = new ActionItemComparator(sortName);
        if (SortOrderEnum.DESCENDING.equals(sortOrder)) {
            comparator = ComparatorUtils.reversedComparator(comparator);
        }
        Collections.sort(actionList, comparator);
        // re-index the action items
        int index = 0;
        for (ActionItemActionListExtension actionItem : actionList) {
            actionItem.setActionListIndex(index++);
        }
    }

    private void initializeActionList(List<? extends ActionItemActionListExtension> actionList, Preferences preferences) {
        List<String> actionItemProblemIds = new ArrayList<String>();
        int index = 0;
        generateActionItemErrors(actionList);
        for (Iterator<? extends ActionItemActionListExtension> iterator = actionList.iterator(); iterator.hasNext();) {
            ActionItemActionListExtension actionItem = iterator.next();
            if (actionItem.getDocumentId() == null) {
                LOG.error("Somehow there exists an ActionItem with a null document id!  actionItemId=" + actionItem.getId());
                iterator.remove();
                continue;
            }
            try {
                actionItem.initialize(preferences);
                actionItem.setActionListIndex(index);
                index++;
            } catch (Exception e) {
                // if there's a problem loading the action item, we don't want to blow out the whole screen but we will remove it from the list
                // and display an appropriate error message to the user
                LOG.error("Error loading action list for action item " + actionItem.getId(), e);
                iterator.remove();
                actionItemProblemIds.add(actionItem.getDocumentId());
            }
        }
        generateActionItemErrors(ACTIONITEM_PROP, ACTIONLIST_BAD_ACTION_ITEMS_ERRKEY, actionItemProblemIds);
    }

    /**
     * Gets the page size of the Action List.  Uses the user's preferences for page size unless the action list
     * has been throttled by an application constant, in which case it uses the smaller of the two values.
     */
    protected int getPageSize(Preferences preferences) {
        return Integer.parseInt(preferences.getPageSize());
    }

    protected PaginatedList buildCurrentPage(List<? extends ActionItemActionListExtension> actionList, Integer page, String sortCriterion, String sortDirection,
                                             int pageSize, Preferences preferences, ActionListForm form) throws WorkflowException {
        List<ActionItemActionListExtension> currentPage = new ArrayList<ActionItemActionListExtension>(pageSize);

        boolean haveCustomActions = false;
        boolean haveDisplayParameters = false;

        final boolean showClearFyi = KewApiConstants.PREFERENCES_YES_VAL.equalsIgnoreCase(preferences.getShowClearFyi());

        // collects all the actions for items on this page
        Set<ActionType> pageActions = new HashSet<ActionType>();

        List<String> customActionListProblemIds = new ArrayList<String>();
        SortOrderEnum sortOrder = parseSortOrder(sortDirection);
        int startIndex = (page - 1) * pageSize;
        int endIndex = startIndex + pageSize;
        generateActionItemErrors(actionList);

        LOG.info("Beginning processing of Action List Customizations (total: " + actionList.size() + " Action Items)");
        long start = System.currentTimeMillis();

        Map<String, ActionItemCustomization>  customizationMap =
                getActionListCustomizationMediator().getActionListCustomizations(
                        getUserSession().getPrincipalId(), convertToApiActionItems(actionList)
                );

        long end = System.currentTimeMillis();
        LOG.info("Finished processing of Action List Customizations (total time: " + (end - start) + " ms)");

        for (int index = startIndex; index < endIndex && index < actionList.size(); index++) {
            ActionItemActionListExtension actionItem = actionList.get(index);
            // evaluate custom action list component for mass actions
            try {
                ActionItemCustomization customization = customizationMap.get(actionItem.getId());
                if (customization != null) {
                    ActionSet actionSet = customization.getActionSet();

                    // If only it were this easy: actionItem.setCustomActions(customization.getActionSet());

                    Map<String, String> customActions = new LinkedHashMap<String, String>();
                    customActions.put("NONE", "NONE");

                    for (ActionType actionType : actionListActionTypes) {
                        if (actionSet.hasAction(actionType.getCode()) &&
                                isActionCompatibleRequest(actionItem, actionType.getCode())) {

                            final boolean isFyi = ActionType.FYI == actionType; // make the conditional easier to read

                            if (!isFyi || (isFyi && showClearFyi)) { // deal with special FYI preference
                                customActions.put(actionType.getCode(), actionType.getLabel());
                                pageActions.add(actionType);
                            }
                        }
                    }

                    if (customActions.size() > 1) {
                        actionItem.setCustomActions(customActions);
                        haveCustomActions = true;
                    }

                    actionItem.setDisplayParameters(customization.getDisplayParameters());

                    haveDisplayParameters = haveDisplayParameters || (actionItem.getDisplayParameters() != null);
                }

            } catch (Exception e) {
                // if there's a problem loading the custom action list attribute, let's go ahead and display the vanilla action item
                LOG.error("Problem loading custom action list attribute", e);
                customActionListProblemIds.add(actionItem.getDocumentId());
            }
            currentPage.add(actionItem);
        }

        // configure custom actions on form
        form.setHasCustomActions(haveCustomActions);

        Map<String, String> defaultActions = new LinkedHashMap<String, String>();
        defaultActions.put("NONE", "NONE");

        for (ActionType actionType : actionListActionTypes) {
            if (pageActions.contains(actionType)) {

                final boolean isFyi = ActionType.FYI == actionType;
                // special logic for FYIs:
                if (isFyi) {
                    // clearing FYIs can be done in any action list not just a customized one
                    if(showClearFyi) {
                        defaultActions.put(actionType.getCode(), actionType.getLabel());
                    }
                } else { // all the other actions
                    defaultActions.put(actionType.getCode(), actionType.getLabel());
                    form.setCustomActionList(Boolean.TRUE);
                }
            }
        }

        if (defaultActions.size() > 1) {
            form.setDefaultActions(defaultActions);
        }

        form.setHasDisplayParameters(haveDisplayParameters);

        generateActionItemErrors(CUSTOMACTIONLIST_PROP, ACTIONLIST_BAD_CUSTOM_ACTION_LIST_ITEMS_ERRKEY, customActionListProblemIds);
        return new PaginatedActionList(currentPage, actionList.size(), page, pageSize, "actionList", sortCriterion, sortOrder);
    }

    // convert a List of org.kuali.rice.kew.actionitem.ActionItemS to org.kuali.rice.kew.api.action.ActionItemS
    private List<org.kuali.rice.kew.api.action.ActionItem> convertToApiActionItems(List<? extends ActionItemActionListExtension> actionList) {
        List<org.kuali.rice.kew.api.action.ActionItem> apiActionItems = new ArrayList<org.kuali.rice.kew.api.action.ActionItem>(actionList.size());

        for (ActionItemActionListExtension actionItemObj : actionList) {
            apiActionItems.add(
                    org.kuali.rice.kew.api.action.ActionItem.Builder.create(actionItemObj).build());
        }
        return apiActionItems;
    }

    private void generateActionItemErrors(String propertyName, String errorKey, List<String> documentIds) {
        if (!documentIds.isEmpty()) {
            String documentIdsString = StringUtils.join(documentIds.iterator(), ", ");
            GlobalVariables.getMessageMap().putError(propertyName, errorKey, documentIdsString);
        }
    }

    private void generateActionItemErrors(List<? extends ActionItemActionListExtension> actionList) {
        for (ActionItemActionListExtension actionItem : actionList) {
            if(!KewApiConstants.ACTION_REQUEST_CODES.containsKey(actionItem.getActionRequestCd())) {
                GlobalVariables.getMessageMap().putError(ACTIONREQUESTCD_PROP,ACTIONITEM_ACTIONREQUESTCD_INVALID_ERRKEY,actionItem.getId()+"");
            }
        }
    }


    public ActionForward takeMassActions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionListForm actionListForm = (ActionListForm) form;
        List<? extends ActionItemActionListExtension> actionList = (List<? extends ActionItemActionListExtension>) request.getSession().getAttribute(ACTION_LIST_KEY);
        if (actionList == null) {
            return start(mapping, new ActionListForm(), request, response);
        }
        ActionMessages messages = new ActionMessages();
        List<ActionInvocation> invocations = new ArrayList<ActionInvocation>();
        int index = 0;
        for (Object element : actionListForm.getActionsToTake()) {
            ActionToTake actionToTake = (ActionToTake) element;
            if (actionToTake != null && actionToTake.getActionTakenCd() != null &&
                    !"".equals(actionToTake.getActionTakenCd()) &&
                    !"NONE".equalsIgnoreCase(actionToTake.getActionTakenCd()) &&
                    actionToTake.getActionItemId() != null) {
                ActionItemActionListExtension actionItem = getActionItemFromActionList(actionList, actionToTake.getActionItemId());
                if (actionItem == null) {
                    LOG.warn("Could not locate the ActionItem to take mass action against in the action list: " + actionToTake.getActionItemId());
                    continue;
                }
                invocations.add(ActionInvocation.create(ActionType.fromCode(actionToTake.getActionTakenCd()), actionItem.getId()));
            }
            index++;
        }
        KEWServiceLocator.getWorkflowDocumentService().takeMassActions(getUserSession().getPrincipalId(), invocations);
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("general.routing.processed"));
        saveMessages(request, messages);
        ActionListForm cleanForm = new ActionListForm();
        request.setAttribute(mapping.getName(), cleanForm);
        request.getSession().setAttribute(KewApiConstants.REQUERY_ACTION_LIST_KEY, "true");
        return start(mapping, cleanForm, request, response);
    }

    protected ActionItemActionListExtension getActionItemFromActionList(List<? extends ActionItemActionListExtension> actionList, String actionItemId) {
        for (ActionItemActionListExtension actionItem : actionList) {
            if (actionItem.getId().equals(actionItemId)) {
                return actionItem;
            }
        }
        return null;
    }

    public ActionForward helpDeskActionListLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionListForm actionListForm = (ActionListForm) form;
        String name = actionListForm.getHelpDeskActionListUserName();
        if (!"true".equals(request.getAttribute("helpDeskActionList"))) {
            throw new AuthorizationException(getUserSession().getPrincipalId(), "helpDeskActionListLogin", getClass().getSimpleName());
        }
        try
        {
            final Principal helpDeskActionListPrincipal = KEWServiceLocator.getIdentityHelperService().getPrincipalByPrincipalName(name);
            final Person helpDeskActionListPerson = KEWServiceLocator.getIdentityHelperService().getPersonByPrincipalName(name);

            GlobalVariables.getUserSession().addObject(KewApiConstants.HELP_DESK_ACTION_LIST_PRINCIPAL_ATTR_NAME, helpDeskActionListPrincipal);
            GlobalVariables.getUserSession().addObject(KewApiConstants.HELP_DESK_ACTION_LIST_PERSON_ATTR_NAME, helpDeskActionListPerson);
        }
        catch (RiceRuntimeException rre)
        {
            GlobalVariables.getMessageMap().putError(HELPDESK_ACTIONLIST_USERNAME, HELPDESK_LOGIN_INVALID_ERRKEY, name);
        }
        catch (RiceIllegalArgumentException e) {
            GlobalVariables.getMessageMap().putError(HELPDESK_ACTIONLIST_USERNAME, HELPDESK_LOGIN_INVALID_ERRKEY, name);
        }
        catch (NullPointerException npe)
        {
            GlobalVariables.getMessageMap().putError("null", HELPDESK_LOGIN_EMPTY_ERRKEY, name);
        }
        actionListForm.setDelegator(null);
        request.getSession().setAttribute(KewApiConstants.REQUERY_ACTION_LIST_KEY, "true");
        return start(mapping, form, request, response);
    }

    public ActionForward clearFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("clearFilter ActionListAction");
        final org.kuali.rice.krad.UserSession commonUserSession = getUserSession();
        commonUserSession.removeObject(KewApiConstants.ACTION_LIST_FILTER_ATTR_NAME);
        request.getSession().setAttribute(KewApiConstants.REQUERY_ACTION_LIST_KEY, "true");
        LOG.debug("end clearFilter ActionListAction");
        return start(mapping, form, request, response);
    }

    public ActionForward clearHelpDeskActionListUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("clearHelpDeskActionListUser ActionListAction");
        GlobalVariables.getUserSession().removeObject(KewApiConstants.HELP_DESK_ACTION_LIST_PRINCIPAL_ATTR_NAME);
        GlobalVariables.getUserSession().removeObject(KewApiConstants.HELP_DESK_ACTION_LIST_PERSON_ATTR_NAME);
        LOG.debug("end clearHelpDeskActionListUser ActionListAction");
        return start(mapping, form, request, response);
    }

    /**
     * Generates an Action List count.
     */
    public ActionForward count(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionListForm alForm = (ActionListForm)form;
        Person user = getUserSession().getPerson();
        alForm.setCount(KEWServiceLocator.getActionListService().getCount(user.getPrincipalId()));
        LOG.info("Fetched Action List count of " + alForm.getCount() + " for user " + user.getPrincipalName());
        return mapping.findForward("count");
    }

    public ActionForward removeOutboxItems(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionListForm alForm = (ActionListForm)form;
        if (alForm.getOutboxItems() != null) {
            KEWServiceLocator.getActionListService().removeOutboxItems(getUserSession().getPrincipalId(), Arrays.asList(alForm.getOutboxItems()));
        }

        alForm.setViewOutbox("true");
        return start(mapping, form, request, response);
    }

    /**
     * Navigate to the Action List Filter page, preserving any newly-modified primary/secondary delegation filters as necessary.
     */
    public ActionForward viewFilter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        start(mapping, actionForm, request, response);
        return mapping.findForward("viewFilter");
    }

    /**
     * Navigate to the user's Preferences page, preserving any newly-modified primary/secondary delegation filters as necessary.
     */
    public ActionForward viewPreferences(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        start(mapping, actionForm, request, response);
        return mapping.findForward("viewPreferences");
    }

    private boolean isActionCompatibleRequest(ActionItemActionListExtension actionItem, String actionTakenCode) {
        boolean actionCompatible = false;
        String requestCd = actionItem.getActionRequestCd();

        //FYI request matches FYI
        if (KewApiConstants.ACTION_REQUEST_FYI_REQ.equals(requestCd) && KewApiConstants.ACTION_TAKEN_FYI_CD.equals(actionTakenCode)) {
            actionCompatible = true || actionCompatible;
        }

        // ACK request matches ACK
        if (KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ.equals(requestCd) && KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD.equals(actionTakenCode)) {
            actionCompatible = true || actionCompatible;
        }

        // APPROVE request matches all but FYI and ACK
        if (KewApiConstants.ACTION_REQUEST_APPROVE_REQ.equals(requestCd) && !(KewApiConstants.ACTION_TAKEN_FYI_CD.equals(actionTakenCode) || KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD.equals(actionTakenCode))) {
            actionCompatible = true || actionCompatible;
        }

        // COMPLETE request matches all but FYI and ACK
        if (KewApiConstants.ACTION_REQUEST_COMPLETE_REQ.equals(requestCd) && !(KewApiConstants.ACTION_TAKEN_FYI_CD.equals(actionTakenCode) || KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD.equals(actionTakenCode))) {
            actionCompatible = true || actionCompatible;
        }

        return actionCompatible;
    }

    private UserSession getUserSession(){
        return GlobalVariables.getUserSession();
    }

    private static class ActionItemComparator implements Comparator<ActionItemActionListExtension> {

        private static final String ACTION_LIST_DEFAULT_SORT = "routeHeader.createDate";

        private final String sortName;

        public ActionItemComparator(String sortName) {
            if (StringUtils.isEmpty(sortName)) {
                sortName = ACTION_LIST_DEFAULT_SORT;
            }
            this.sortName = sortName;
        }

        @Override
        public int compare(ActionItemActionListExtension actionItem1, ActionItemActionListExtension actionItem2) {
            try {
                // invoke the power of the lookup functionality provided by the display tag library, this LookupUtil method allows for us
                // to evaulate nested bean properties (like workgroup.groupNameId.nameId) in a null-safe manner.  For example, in the
                // example if workgroup evaluated to NULL then LookupUtil.getProperty would return null rather than blowing an exception
                Object property1 = LookupUtil.getProperty(actionItem1, sortName);
                Object property2 = LookupUtil.getProperty(actionItem2, sortName);
                if (property1 == null && property2 == null) {
                    return 0;
                } else if (property1 == null) {
                    return -1;
                } else if (property2 == null) {
                    return 1;
                }
                if (property1 instanceof Comparable) {
                    return ((Comparable)property1).compareTo(property2);
                }
                return property1.toString().compareTo(property2.toString());
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException("Could not sort for the given sort name: " + sortName, e);
            }
        }
    }

    // Lazy initialization holder class (see Effective Java Item #71)
    private static class ActionListCustomizationMediatorHolder {
        static final ActionListCustomizationMediator actionListCustomizationMediator =
                KewFrameworkServiceLocator.getActionListCustomizationMediator();
    }

    private ActionListCustomizationMediator getActionListCustomizationMediator() {
        return ActionListCustomizationMediatorHolder.actionListCustomizationMediator;
    }

    /**
     * Simple class which defines the key of a partition of Action Items associated with an Application ID.
     *
     * <p>This class allows direct field access since it is intended for internal use only.</p>
     */
    private static final class PartitionKey {
        String applicationId;
        Set<String> customActionListAttributeNames;

        PartitionKey(String applicationId, Collection<ExtensionDefinition> extensionDefinitions) {
            this.applicationId = applicationId;
            this.customActionListAttributeNames = new HashSet<String>();
            for (ExtensionDefinition extensionDefinition : extensionDefinitions) {
                this.customActionListAttributeNames.add(extensionDefinition.getName());
            }
        }

        List<String> getCustomActionListAttributeNameList() {
            return new ArrayList<String>(customActionListAttributeNames);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof PartitionKey)) {
                return false;
            }
            PartitionKey key = (PartitionKey) o;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(applicationId, key.applicationId);
            builder.append(customActionListAttributeNames, key.customActionListAttributeNames);
            return builder.isEquals();
        }

        @Override
        public int hashCode() {
            HashCodeBuilder builder = new HashCodeBuilder();
            builder.append(applicationId);
            builder.append(customActionListAttributeNames);
            return builder.hashCode();
        }
    }
}
