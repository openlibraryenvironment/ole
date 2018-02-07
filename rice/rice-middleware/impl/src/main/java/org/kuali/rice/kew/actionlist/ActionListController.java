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
import org.apache.struts.action.*;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import org.displaytag.util.LookupUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.actionitem.ActionItemActionListExtension;
import org.kuali.rice.kew.actionitem.OutboxItemActionListExtension;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.actionlist.web.ActionListUtil;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.action.ActionInvocation;
import org.kuali.rice.kew.api.action.ActionItemCustomization;
import org.kuali.rice.kew.api.action.ActionSet;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.framework.KewFrameworkServiceLocator;
import org.kuali.rice.kew.framework.actionlist.ActionListCustomizationMediator;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.PerformanceLogger;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.web.ui.ExtraButton;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.kuali.rice.krad.web.form.UifFormBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A controller for the action list view.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Controller
@RequestMapping(value = "/new/actionList")
public class ActionListController extends UifControllerBase{
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ActionListController.class);
    protected static final String MAX_ACTION_ITEM_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss.S";

    private static final ActionType [] actionListActionTypes =
            { ActionType.APPROVE, ActionType.DISAPPROVE, ActionType.CANCEL, ActionType.ACKNOWLEDGE, ActionType.FYI };

    @Override
    protected ActionListForm createInitialForm(HttpServletRequest request) {
        return new ActionListForm();
    }

    /**
    * Refresh request mapping.
    *
    * <p>
    * Handles requests where the methodToCall parameter
    * is 'refresh'.
    * </p>
    *
    * @param form - ActionListForm form
    * @param result - Spring form binding result
    * @param request - http request
    * @param response - http response
    * @return start - forwards to start method
    */
    @RequestMapping(params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response){
        ActionListForm actionListForm = (ActionListForm)form;
        actionListForm.setRequeryActionList(true);

        return start(form,result,request,response);
    }

    /**
    * Initializes filter.
    *
    * <p>
    * Sets up the action list filter
    * </p>
    *
    * @param form - ActionListForm form
    */
    protected void initializeFilter(ActionListForm form) {
        if (form.getFilter() == null) {
            ActionListFilter filter = new ActionListFilter();
            filter.setDelegationType(DelegationType.SECONDARY.getCode());
            filter.setExcludeDelegationType(true);
            form.setFilter(filter);
        }
    }

    /**
    * Initializes principal id.
    *
    * <p>
    * Sets up the principal id in the form.
    * </p>
    *
    * @param actionListForm - ActionListForm form
    * @param filter - action list filter
    * @return String
    */
    protected String initializePrinicpalId(ActionListForm actionListForm,ActionListFilter filter) {
        String principalId = null;
        Principal principal = actionListForm.getHelpDeskActionListPrincipal();
        if (principal != null) {
            principalId = principal.getPrincipalId();
        } else {
            if (!StringUtils.isEmpty(actionListForm.getDocType())) {
                initializeDocType(actionListForm,filter);
            }
            final UserSession uSession = getUserSession();
            principalId = uSession.getPerson().getPrincipalId();
        }

        return principalId;
    }

    /**
    * Initializes Document Type.
    *
    * <p>
    * Sets up the document type in the form.
    * </p>
    *
    * @param actionListForm - ActionListForm form
    * @param filter - action list filter
    * @return void
    */
    protected void initializeDocType(ActionListForm actionListForm,ActionListFilter filter) {
        filter.setDocumentType(actionListForm.getDocType());
        filter.setExcludeDocumentType(false);
        actionListForm.setRequeryActionList(true);
    }

    /**
    * Initializes Delegators
    *
    * <p>
    * Sets up the delegators for the form and filter
    * </p>
    *
    * @param actionListForm - ActionListForm form
    * @param filter - action list filter
    * @param actionList - list of action items
    * @param request - http request
    * @return void
    */
    protected void initializeDelegators(ActionListForm actionListForm,ActionListFilter filter,List<? extends ActionItemActionListExtension> actionList,HttpServletRequest request)   {
        if (!KewApiConstants.DELEGATION_DEFAULT.equals(actionListForm.getDelegationId())) {
            // If the user can filter by both primary and secondary delegation, and both drop-downs have non-default values assigned,
            // then reset the primary delegation drop-down's value when the primary delegation drop-down's value has remained unaltered
            // but the secondary drop-down's value has been altered; but if one of these alteration situations does not apply, reset the
            // secondary delegation drop-down.

            if (StringUtils.isNotBlank(actionListForm.getPrimaryDelegateId()) && !KewApiConstants.PRIMARY_DELEGATION_DEFAULT.equals(actionListForm.getPrimaryDelegateId())){
                setDelegationId(actionListForm,request);
            } else if (StringUtils.isNotBlank(filter.getPrimaryDelegateId()) &&
                    !KewApiConstants.PRIMARY_DELEGATION_DEFAULT.equals(filter.getPrimaryDelegateId())) {
                // If the primary delegation drop-down is invisible but a primary delegation filter is in place, and if the secondary delegation
                // drop-down has a non-default value selected, then reset the primary delegation filtering.
                filter.setPrimaryDelegateId(KewApiConstants.PRIMARY_DELEGATION_DEFAULT);
            }
        }
        // Enable the secondary delegation filtering.
        filter.setDelegatorId(actionListForm.getDelegationId());
        filter.setExcludeDelegatorId(false);
        actionList = null;
    }

    /**
     * Sets the delegation id
     *
     * <p>
     * Sets the delegation id on the form
     * </p>
     *
     * @param actionListForm - ActionListForm form
     * @param request - http request
     * @return void
     */
    protected void setDelegationId(ActionListForm actionListForm,HttpServletRequest request)   {
        if (actionListForm.getPrimaryDelegateId().equals(request.getParameter("oldPrimaryDelegateId")) &&
                !actionListForm.getDelegationId().equals(request.getParameter("oldDelegationId"))) {
            actionListForm.setPrimaryDelegateId(KewApiConstants.PRIMARY_DELEGATION_DEFAULT);
        } else {
            actionListForm.setDelegationId(KewApiConstants.DELEGATION_DEFAULT);
        }
    }

    /**
     * Initializes primary delegate.
     *
     * <p>
     * Sets up the primary delegate in the form.
     * </p>
     *
     * @param actionListForm - ActionListForm form
     * @param filter - action list filter
     * @param actionList - list of action items
     * @param request - http request
     * @return void
     */
    protected void initializePrimaryDelegate(ActionListForm actionListForm,ActionListFilter filter,List<? extends ActionItemActionListExtension> actionList,HttpServletRequest request)   {
        if (!StringUtils.isEmpty(actionListForm.getPrimaryDelegateId())) {

            // If the secondary delegation drop-down is invisible but a secondary delegation filter is in place, and if the primary delegation
            // drop-down has a non-default value selected, then reset the secondary delegation filtering.
            if (StringUtils.isBlank(actionListForm.getDelegationId()) && !KewApiConstants.PRIMARY_DELEGATION_DEFAULT.equals(actionListForm.getPrimaryDelegateId()) &&
                    StringUtils.isNotBlank(filter.getDelegatorId()) &&
                    !KewApiConstants.DELEGATION_DEFAULT.equals(filter.getDelegatorId())) {
                filter.setDelegatorId(KewApiConstants.DELEGATION_DEFAULT);
            }

            // Enable the primary delegation filtering.
            filter.setPrimaryDelegateId(actionListForm.getPrimaryDelegateId());
            filter.setExcludeDelegatorId(false);
            actionList = null;
        }
    }

    /**
    * Start request mapping.
    *
    * <p>
    * Handles requests where the methodToCall parameter
    * is 'start'.  Runs on most requests and sets up the
    * basic variables.
    * </p>
    *
    * @param form - ActionListForm form
    * @param result - Spring form binding result
    * @param request - http request
    * @param response - http response
    * @return ModelAndView - uses standard KRAD getUIFModelAndView()
    */
    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        ActionListForm actionListForm = (ActionListForm)form;
        request.setAttribute("preferences", actionListForm.getPreferences());

        PerformanceLogger plog = new PerformanceLogger();
        plog.log("Starting ActionList fetch");
        ActionListService actionListSrv = KEWServiceLocator.getActionListService();

        // reset the default action on the form
        actionListForm.setDefaultActionToTake("NONE");
        boolean freshActionList = true;

        // retrieve cached action list
        List<? extends ActionItemActionListExtension> actionList = (List<? extends ActionItemActionListExtension>)actionListForm.getActionList();
        plog.log("Time to initialize");


        try {

            initializeFilter(actionListForm);
            final ActionListFilter filter = actionListForm.getFilter();

            String principalId = initializePrinicpalId(actionListForm,filter);

            /* 'forceListRefresh' variable used to signify that the action list filter has changed
             * any time the filter changes the action list must be refreshed or filter may not take effect on existing
             * list items... only exception is if action list has not loaded previous and fetching of the list has not
             * occurred yet
             */
            boolean forceListRefresh = actionListForm.isRequeryActionList();

            final Preferences preferences = (Preferences)actionListForm.getPreferences();

            //set primary delegation id
            if (!StringUtils.isEmpty(actionListForm.getDelegationId())) {
                initializeDelegators(actionListForm,filter,actionList,request);
            }

            //set primary delegate
            if (!StringUtils.isEmpty(actionListForm.getPrimaryDelegateId())) {
                initializePrimaryDelegate(actionListForm,filter,actionList,request);
            }

            // if the user has changed, we need to refresh the action list
            if (!principalId.equals(actionListForm.getUser())) {
                actionList = null;
            }

            if (isOutboxMode(actionListForm, request, preferences)) {
                actionList = new ArrayList<OutboxItemActionListExtension>(actionListSrv.getOutbox(principalId, filter));
                actionListForm.setOutBoxEmpty(actionList.isEmpty());
                //added because we now use the actionList rather than the actionListPage
                actionListForm.setActionList((ArrayList) actionList);
            } else {

                if (actionList == null) {
                    // fetch the action list
                    actionList = new ArrayList<ActionItemActionListExtension>(actionListSrv.getActionList(principalId, filter));
                    actionListForm.setUser(principalId);
                } else if (forceListRefresh) {
                    // force a refresh... usually based on filter change or parameter specifying refresh needed
                    actionList = new ArrayList<ActionItemActionListExtension>(actionListSrv.getActionList(principalId, filter));
                    actionListForm.setUser(principalId);
                } else {
                    Boolean update = actionListForm.isUpdateActionList();
                }

                actionListForm.setActionList((ArrayList) actionList);
            }

            // reset the requery action list key
            actionListForm.setRequeryActionList(false);

            // build the drop-down of delegators
            if (KewApiConstants.DELEGATORS_ON_ACTION_LIST_PAGE.equalsIgnoreCase(preferences.getDelegatorFilter())) {
                Collection<Recipient> delegators = actionListSrv.findUserSecondaryDelegators(principalId);
                actionListForm.setDelegators(ActionListUtil.getWebFriendlyRecipients(delegators));
                actionListForm.setDelegationId(filter.getDelegatorId());
            }

            // Build the drop-down of primary delegates.
            if (KewApiConstants.PRIMARY_DELEGATES_ON_ACTION_LIST_PAGE.equalsIgnoreCase(preferences.getPrimaryDelegateFilter())) {
                Collection<Recipient> pDelegates = actionListSrv.findUserPrimaryDelegations(principalId);
                actionListForm.setPrimaryDelegates(ActionListUtil.getWebFriendlyRecipients(pDelegates));
                actionListForm.setPrimaryDelegateId(filter.getPrimaryDelegateId());
            }

            actionListForm.setFilterLegend(filter.getFilterLegend());
            plog.log("Setting attributes");

            int pageSize = getPageSize(preferences);

            // initialize the action list if necessary
            if (freshActionList) {
                plog.log("calling initializeActionList");
                initializeActionList(actionList, preferences);
                plog.log("done w/ initializeActionList");
            }

            plog.log("start addActions");
            addCustomActions(actionList,preferences,actionListForm);
            plog.log("done w/ addCustomActions");
            actionListForm.setUpdateActionList(false);
            plog.log("finished setting attributes, finishing action list fetch");
        } catch (Exception e) {
            LOG.error("Error loading action list.", e);
        }

        LOG.debug("end start ActionListAction");

        String returnPage = "ActionListPage1";
        String methodToCall = actionListForm.getMethodToCall();
        if(methodToCall.equals("clear")) {
           returnPage = "ActionListPage2";
        }

        return getUIFModelAndView(actionListForm,returnPage);
    }

    private static final String OUT_BOX_MODE = "_OUT_BOX_MODE";

    /**
    *  Determines whether the page is in outbox mode.
    *
    *  <p>
    *  This method is setting 2 props on the {@link org.kuali.rice.kew.actionlist.web.ActionListForm} that controls outbox behavior.
    *  alForm.setViewOutbox("false"); -> this is set by user preferences and the actionlist.outbox.off config prop
    *  alForm.setShowOutbox(false); -> this is set by user action clicking the ActionList vs. Outbox links.
    *  </p>
    *
    * @param alForm - action list form
    * @param request - http request
    * @return boolean indication whether the outbox should be fetched
    */
    private boolean isOutboxMode(ActionListForm alForm, HttpServletRequest request, Preferences preferences) {

        boolean outBoxView = false;

        if (! preferences.isUsingOutbox() || ! ConfigContext.getCurrentContextConfig().getOutBoxOn()) {
            alForm.setOutBoxMode(Boolean.FALSE);
            alForm.setViewOutbox("false");
            alForm.setShowOutbox(false);

            return false;
        }

        alForm.setShowOutbox(true);

        if (StringUtils.isNotEmpty(alForm.getViewOutbox())) {
            if (!Boolean.valueOf(alForm.getViewOutbox())) {
                //request.getSession().setAttribute(OUT_BOX_MODE, Boolean.FALSE);
                alForm.setOutBoxMode(Boolean.FALSE);
                outBoxView = false;
            } else {
                //request.getSession().setAttribute(OUT_BOX_MODE, Boolean.TRUE);
                alForm.setOutBoxMode(Boolean.FALSE);
                outBoxView = true;
            }
        } else {
            outBoxView = alForm.isOutBoxMode();
        }

        if (outBoxView) {
            alForm.setViewOutbox("true");
        } else {
            alForm.setViewOutbox("false");
        }

        return outBoxView;
    }

    /**
     *  Initializes the action list.
     *
     *  <p>
     *  Checks for errors in the action list upon initial load.
     *  </p>
     *
     * @param actionList list of action items
     * @param preferences KEW user preferences
     * @return void
     */
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

        generateActionItemErrors("actionitem", "actionlist.badActionItems", actionItemProblemIds);
    }

    /**
    *  Get the action list page size.
    *
    *  <p>
    *  Gets the page size of the Action List.  Uses the user's preferences for page size unless the action list
    *  has been throttled by an application constant, in which case it uses the smaller of the two values.
    *  </p>
    *
    * @param preferences KEW user preferences
    * @return int
    */
    protected int getPageSize(Preferences preferences) {
        return Integer.parseInt(preferences.getPageSize());
    }

    /**
    *  Adds custom actions to action items.
    *
    *  <p>
    *  Goes through each item in the action list and adds the custom actions.  It also adds flags for whether each
    *  item has actions.  Finally, creates list of actions and flag for the entire action list.
    *  </p>
    *
    * @param actionList list of action items
    * @param preferences KEW preferences
    * @form action list form
    * @return void
    */
    protected void addCustomActions(List<? extends ActionItemActionListExtension> actionList,
            Preferences preferences, ActionListForm form) throws WorkflowException {

        boolean haveCustomActions = false;
        boolean haveDisplayParameters = false;

        final boolean showClearFyi = KewApiConstants.PREFERENCES_YES_VAL.equalsIgnoreCase(preferences.getShowClearFyi());

        // collects all the actions for items
        Set<ActionType> pageActions = new HashSet<ActionType>();

        List<String> customActionListProblemIds = new ArrayList<String>();
        generateActionItemErrors(actionList);

        LOG.info("Beginning processing of Action List Customizations (total: " + actionList.size() + " Action Items)");
        long start = System.currentTimeMillis();
        Map<String, ActionItemCustomization> customizationMap =
                getActionListCustomizationMediator().getActionListCustomizations(
                        getUserSession().getPrincipalId(), convertToApiActionItems(actionList)
                );
        long end = System.currentTimeMillis();
        LOG.info("Finished processing of Action List Customizations (total time: " + (end - start) + " ms)");

        for(ActionItemActionListExtension actionItem : actionList ){
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
        }

        // configure custom actions on form
        form.setHasCustomActions(haveCustomActions);

        Map<String, String> defaultActions = new LinkedHashMap<String, String>();
        defaultActions.put("NONE", "NONE");

        for (ActionType actionType : actionListActionTypes) {
            if (pageActions.contains(actionType)) {
                // special logic for FYIs:
                final boolean isFyi = ActionType.FYI == actionType;
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
        generateActionItemErrors("customActionList", "actionlist.badCustomActionListItems", customActionListProblemIds);

    }

    /**
    *  Converts actionItems to list.
    *
    *  <p>
    *  Convert a List of org.kuali.rice.kew.actionitem.ActionItemS to org.kuali.rice.kew.api.action.ActionItemS.
    *  </p>
    *
    * @param actionList list of action items
    * @return List<org.kuali.rice.kew.api.action.ActionItem>
    */
    private List<org.kuali.rice.kew.api.action.ActionItem> convertToApiActionItems(List<? extends ActionItemActionListExtension> actionList) {
        List<org.kuali.rice.kew.api.action.ActionItem> apiActionItems = new ArrayList<org.kuali.rice.kew.api.action.ActionItem>(actionList.size());
        for (ActionItemActionListExtension actionItemObj : actionList) {
            apiActionItems.add(
                    org.kuali.rice.kew.api.action.ActionItem.Builder.create(actionItemObj).build());
        }

        return apiActionItems;
    }

    /**
    *  Creates action item errors.
    *
    *  <p>
    *  Creates an error for each action item that has an empty ID.
    *  </p>
    *
    * @param propertyName the property name
    * @param errorKey  string of the error key
    * @param documentIds list of document IDs
    * @return void
    */
    private void generateActionItemErrors(String propertyName, String errorKey, List<String> documentIds) {
        if (!documentIds.isEmpty()) {
            String documentIdsString = StringUtils.join(documentIds.iterator(), ", ");
            GlobalVariables.getMessageMap().putError(propertyName, errorKey, documentIdsString);
        }
    }

    /**
    *  Creates action item errors.
    *
    *  <p>
    *  Creates an error for each action item that has an empty ID.
    *  </p>
    *
    * @param actionList list of action items.
    * @return void
    */
    private void generateActionItemErrors(List<? extends ActionItemActionListExtension> actionList) {
        for (ActionItemActionListExtension actionItem : actionList) {
            if(!KewApiConstants.ACTION_REQUEST_CODES.containsKey(actionItem.getActionRequestCd())) {
                GlobalVariables.getMessageMap().putError("actionRequestCd","actionitem.actionrequestcd.invalid",actionItem.getId()+"");
            }
        }
    }

    /**
     * Process taking mass action on action items
     *
     * <p>
     * Handles requests where the methodToCall parameter
     * is 'takeMassActions'.  Iterates through action items that have custom actions and process each selected action.
     * </p>
     *
     * @param form - ActionListForm form
     * @param result - Spring form binding result
     * @param request - http request
     * @param response - http response
     * @return start - forwards to the start method
     */
    @RequestMapping(params = "methodToCall=takeMassActions")
    protected ModelAndView takeMassActions(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response){
        ActionListForm actionListForm = (ActionListForm) form;

        Object obj = ObjectPropertyUtils.getPropertyValue(form, "extensionData['actionInputField_actionSelect_line2']");

        List<? extends ActionItemActionListExtension> actionList = (List<? extends ActionItemActionListExtension>) actionListForm.getActionList();
        if (actionList == null) {
            return getUIFModelAndView(form);
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

        org.kuali.rice.kew.actionlist.web.ActionListForm
                cleanForm = new org.kuali.rice.kew.actionlist.web.ActionListForm();
        actionListForm.setRequeryActionList(true);

        return start(actionListForm,result,request,response);
    }

    /**
    * Gets action item from list.
    *
    * <p>
    * Gets the action item from the action item list based on the ID.
    * </p>
    *
    * @param actionList - list of action items
    * @param actionItemId - primary key for action item
    * @return ActionItemActionListExtension or null
    */
    protected ActionItemActionListExtension getActionItemFromActionList(List<? extends ActionItemActionListExtension> actionList, String actionItemId) {
        for (ActionItemActionListExtension actionItem : actionList) {
            if (actionItem.getId().equals(actionItemId)) {
                return actionItem;
            }
        }

        return null;
    }

    /**
     * Sets up view for help desk login.
     *
     * <p>
     * Setups the view for the help desk login.  User can see other's action items but can't take action on them.
     * </p>
     *
     * @param form - ActionListForm form
     * @param result - Spring form binding result
     * @param request - http request
     * @param response - http response
     * @return start() - forwards to start method to refresh action list
     */
    @RequestMapping(params = "methodToCall=helpDeskActionListLogin")
    public ModelAndView helpDeskActionListLogin(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response){
        ActionListForm actionListForm = (ActionListForm) form;

        String name = actionListForm.getHelpDeskActionListUserName();
        if (!actionListForm.isHelpDeskActionList()) {
            throw new AuthorizationException(getUserSession().getPrincipalId(), "helpDeskActionListLogin", getClass().getSimpleName());
        }

        try
        {
            final Principal helpDeskActionListPrincipal = KEWServiceLocator.getIdentityHelperService().getPrincipalByPrincipalName(name);
            final Person helpDeskActionListPerson = KEWServiceLocator.getIdentityHelperService().getPersonByPrincipalName(name);
            actionListForm.setHelpDeskActionListPrincipal(helpDeskActionListPrincipal);
            actionListForm.setHelpDeskActionListPerson(helpDeskActionListPerson);
        }
        catch (RiceRuntimeException rre)
        {
            GlobalVariables.getMessageMap().putError("helpDeskActionListUserName", "helpdesk.login.invalid", name);
        }
        catch (RiceIllegalArgumentException e) {
            GlobalVariables.getMessageMap().putError("helpDeskActionListUserName", "helpdesk.login.invalid", name);
        }
        catch (NullPointerException npe)
        {
            GlobalVariables.getMessageMap().putError("null", "helpdesk.login.empty", name);
        }

        actionListForm.setDelegator(null);
        actionListForm.setRequeryActionList(true);

        return start(actionListForm,result,request,response);
    }

    /**
    * Clears the action list filter.
    *
    * <p>
    * Clears the action list filter so all action items are shown.
    * </p>
    *
    * @param form - ActionListForm form
    * @param result - Spring form binding result
    * @param request - http request
    * @param response - http response
    * @return start() - forwards to start to refresh action list
    */
    @RequestMapping(params = "methodToCall=clearFilter")
    public ModelAndView clearFilter(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response){
        ActionListForm actionListForm = (ActionListForm) form;

        LOG.debug("clearFilter ActionListController");
        final org.kuali.rice.krad.UserSession commonUserSession = getUserSession();
        actionListForm.setFilter(new ActionListFilter());
        ActionListFilter filter = new ActionListFilter();
        filter.setDelegationType(DelegationType.SECONDARY.getCode());
        filter.setExcludeDelegationType(true);
        actionListForm.setFilter(filter);
        LOG.debug("end clearFilter ActionListController");

        return start(actionListForm,result,request,response);
    }

    /**
    * Clears the action list filter.
    *
    * <p>
    * Clears the action list filter so all action items are shown.  Clears filter from secondary page and then
    * forwards to the correct page after the start method runs.
    * </p>
    *
    * @param form - ActionListForm form
    * @param result - Spring form binding result
    * @param request - http request
    * @param response - http response
    * @return clearFilter() - forwards to clearFilter method
    */
    @RequestMapping(params = "methodToCall=clear")
    public ModelAndView clear(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response){
        return clearFilter(form,result,request,response);
    }

    /**
    * Sets the filter.
    *
    * <p>
    * Sets the action list filter in the form.
    * </p>
    *
    * @param form - ActionListForm form
    * @param result - Spring form binding result
    * @param request - http request
    * @param response - http response
    * @return start() forwards to start method to refresh action list
    */
    @RequestMapping(params = "methodToCall=setFilter")
    public ModelAndView setFilter(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response){
        ActionListForm actionListForm = (ActionListForm) form;

        //validate the filter through the actionitem/actionlist service (I'm thinking actionlistservice)
        final UserSession uSession = getUserSession();

        ActionListFilter alFilter = actionListForm.getLoadedFilter();
        if (StringUtils.isNotBlank(alFilter.getDelegatorId()) && !KewApiConstants.DELEGATION_DEFAULT.equals(alFilter.getDelegatorId()) &&
                StringUtils.isNotBlank(alFilter.getPrimaryDelegateId()) && !KewApiConstants.PRIMARY_DELEGATION_DEFAULT.equals(alFilter.getPrimaryDelegateId())){
            // If the primary and secondary delegation drop-downs are both visible and are both set to non-default values,
            // then reset the secondary delegation drop-down to its default value.
            alFilter.setDelegatorId(KewApiConstants.DELEGATION_DEFAULT);
        }

        actionListForm.setFilter(alFilter);
        if (GlobalVariables.getMessageMap().hasNoErrors()) {
            actionListForm.setRequeryActionList(true);
            return start(actionListForm,result,request,response);
        }

        return start(actionListForm,result,request,response);
    }

    /**
    * Clears help desk login.
    *
    * <p>
    * Set the form back to display the logged in user's action list.
    * </p>
    *
    * @param form - ActionListForm form
    * @param result - Spring form binding result
    * @param request - http request
    * @param response - http response
    * @return start() - forwards to start method to refresh the action list
    */
    @RequestMapping(params = "methodToCall=clearHelpDeskActionListUser")
    public ModelAndView clearHelpDeskActionListUser(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response){
        ActionListForm actionListForm = (ActionListForm) form;

        LOG.debug("clearHelpDeskActionListUser ActionListAction");
        actionListForm.setHelpDeskActionListPrincipal(null);
        actionListForm.setHelpDeskActionListPerson(null);
        LOG.debug("end clearHelpDeskActionListUser ActionListAction");

        actionListForm.setRequeryActionList(true);

        return start((UifFormBase)actionListForm,result,request,response);
    }

    /**
    * Removes outbox items.
    *
    * <p>
    * Removes any outbox items that are selected.
    * </p>
    *
    * @param form - ActionListForm form
    * @param result - Spring form binding result
    * @param request - http request
    * @param response - http response
    * @return start() forwards to start to refresh the outbox.
    */
    @RequestMapping(params = "methodToCall=removeOutboxItems")
    public ModelAndView removeOutboxItems(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response){
        ActionListForm actionListForm = (ActionListForm)form;
        Map selectedCollectionLines = actionListForm.getSelectedCollectionLines();
        Object selectedItems = selectedCollectionLines.get("ActionList");

        if (selectedItems != null) {
            List<String> outboxItemsForDeletion = new ArrayList<String>((LinkedHashSet)selectedItems);
            KEWServiceLocator.getActionListService().removeOutboxItems(getUserSession().getPrincipalId(), outboxItemsForDeletion);
            selectedCollectionLines.remove("ActionList");
            actionListForm.setSelectedCollectionLines(selectedCollectionLines);
        }

        actionListForm.setViewOutbox("true");
        actionListForm.setRequeryActionList(true);

        return start(actionListForm,result,request,response);
    }

    /**
    * Navigates to filter view.
    *
    * <p>
    * Navigate to the Action List Filter page, preserving any newly-modified primary/secondary delegation filters as necessary.
    * </p>
    *
    * @param form - ActionListForm form
    * @param result - Spring form binding result
    * @param request - http request
    * @param response - http response
    * @return ModelAndView - forwards to the standard KRAD getUIFModelAndView method
    */
    @RequestMapping(params = "methodToCall=viewFilter")
    public ModelAndView viewFilter(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response){
        ActionListForm actionListForm = (ActionListForm)form;
        actionListForm.setOldFilter(new ActionListFilter(actionListForm.getFilter()));

        return getUIFModelAndView(actionListForm,"ActionListPage2");
    }

    /**
    * Revert to previous filter.
    *
    * <p>
    * When user changes the filter but presses cancel, the filter goes back to the old filter.
    * </p>
    *
    * @param form - ActionListForm form
    * @param result - Spring form binding result
    * @param request - http request
    * @param response - http response
    * @return start() forwards to start method to refresh teh action list
    */
    @RequestMapping(params = "methodToCall=cancelFilter")
    public ModelAndView cancelFilter(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response){
        ActionListForm actionListForm = (ActionListForm)form;
        actionListForm.setFilter(new ActionListFilter(actionListForm.getOldFilter()));

        return start(actionListForm,result,request,response);
    }

    /**
    * Navigate to preferences page.
    *
    * <p>
    * Navigate to the user's Preferences page, preserving any newly-modified primary/secondary delegation filters as
    * necessary.
    * </p>
    *
    * @param form - ActionListForm form
    * @param result - Spring form binding result
    * @param request - http request
    * @param response - http response
    * @return ModelAndView - forwards to KRAD standard getUIFModelAndView method
    */
    @RequestMapping(params = "methodToCall=viewPreferences")
    public ModelAndView viewPreferences(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response){
        return getUIFModelAndView(form,"ActionListPage3");
    }

    /**
    * Is the action item a compatible request.
    *
    * <p>
    * Checks whether the action taken is valid for the action item.
    * </p>
    *
    * @param actionItem - an action item
    * @param actionTakenCode - code of action taken on the action item
    * @return boolean
    */
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

    /**
    * Gets session.
    *
    * <p>
    * Gets the user session object.
    * </p>
    *
    * @return UserSession
    */
    private UserSession getUserSession(){
        return GlobalVariables.getUserSession();
    }

    /**
    * Lazy initialization holder class
    *
    * <p>
    * Lazy initialization holder static class (see Effective Java Item #71)
    * </p>
    *
    */
    private static class ActionListCustomizationMediatorHolder {
        static final ActionListCustomizationMediator actionListCustomizationMediator =
                KewFrameworkServiceLocator.getActionListCustomizationMediator();
    }

    /**
    * Action list customization mediator.
    *
    * <p>
    * Action list customization mediator.
    * </p>
    *
    * @return ActionListCustomizationMediatorHolder.actionListCustomizationMediator
    */
    private ActionListCustomizationMediator getActionListCustomizationMediator() {
        return ActionListCustomizationMediatorHolder.actionListCustomizationMediator;
    }

    /**
    * Simple class which defines the key of a partition of Action Items associated with an Application ID.
    *
    * <p>
    * This class allows direct field access since it is intended for internal use only.
    * </p>
    *
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

