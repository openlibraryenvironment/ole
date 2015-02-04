/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.document.web.struts;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.ole.select.document.OleOrderQueueDocument;
import org.kuali.ole.select.document.validation.event.AssignOrderQueueEvent;
import org.kuali.ole.select.document.validation.event.OleAccountFilterEvent;
import org.kuali.ole.select.service.OleDocStoreLookupService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.web.struts.action.KualiTransactionalDocumentActionBase;
import org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class is the action class for Order Holding Queue.
 */
public class OleOrderQueueAction extends KualiTransactionalDocumentActionBase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleOrderQueueAction.class);

    /**
     * This method approves the selected requisition documents.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward massApprove(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside massApprove of OleOrderQueueAction");
        OleOrderQueueForm orderForm = (OleOrderQueueForm) form;
        OleOrderQueueDocument orderQueueDoc = (OleOrderQueueDocument) orderForm.getDocument();
        orderQueueDoc.massApprove();
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, new JSTLConstants(KewApiConstants.class));
        LOG.debug("Leaving massApprove of OleOrderQueueAction");
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    /**
     * This method validates for selector and assigns the selected requisition documents
     * to the selector.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward assign(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside assign of OleOrderQueueAction");
        OleOrderQueueForm orderForm = (OleOrderQueueForm) form;
        OleOrderQueueDocument orderQueueDoc = (OleOrderQueueDocument) orderForm.getDocument();

        boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new AssignOrderQueueEvent(OLEConstants.OrderQueue.PRINCIPAL_NAME, orderQueueDoc));

        if (rulePassed) {
            orderQueueDoc.assign();
        }
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, new JSTLConstants(KewApiConstants.class));
        LOG.debug("Leaving assign of OleOrderQueueAction");
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    /**
     * This method cancels the selected requisition documents.
     * to the selector.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    public ActionForward delete(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside delete of OleOrderQueueAction");
        OleOrderQueueForm orderForm = (OleOrderQueueForm) form;
        OleOrderQueueDocument orderQueueDoc = (OleOrderQueueDocument) orderForm.getDocument();
        orderQueueDoc.delete();
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, new JSTLConstants(KewApiConstants.class));
        LOG.debug("Leaving delete of OleOrderQueueAction");
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    /**
     * This method totals price on the selected requisition documents.
     * to the selector.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward totalSelectedItems(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside totalSelectedItems of OleOrderQueueAction");
        OleOrderQueueForm orderForm = (OleOrderQueueForm) form;
        OleOrderQueueDocument orderQueueDoc = (OleOrderQueueDocument) orderForm.getDocument();
        KualiDecimal totalPrice = orderQueueDoc.getTotalSelectedItemsPrice();
        request.setAttribute(OLEConstants.OrderQueue.TOTAL_PRICE, totalPrice);
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, new JSTLConstants(KewApiConstants.class));
        LOG.debug("Leaving totalSelectedItems of OleOrderQueueAction");
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    /**
     * This method selects all checkboxes from all pages in the result tab.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward selectAll(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside selectAll of OleOrderQueueAction");
        OleOrderQueueForm orderForm = (OleOrderQueueForm) form;
        List<OleRequisitionItem> items = ((OleOrderQueueDocument) orderForm.getDocument()).getRequisitionItems();
        List<OleRequisitionItem> refreshItems = new ArrayList<OleRequisitionItem>(0);

        for (OleRequisitionItem item : items) {
            item.setItemAdded(true);
            refreshItems.add(item);
        }

        ((OleOrderQueueDocument) orderForm.getDocument()).setRequisitionItems(refreshItems);
        LOG.debug("Leaving selectAll of OleOrderQueueAction");
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, new JSTLConstants(KewApiConstants.class));
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    /**
     * This method deselects all checkboxes from all pages in the result tab.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward unselectAll(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside unselectAll of OleOrderQueueAction");
        OleOrderQueueForm orderForm = (OleOrderQueueForm) form;
        List<OleRequisitionItem> items = ((OleOrderQueueDocument) orderForm.getDocument()).getRequisitionItems();
        List<OleRequisitionItem> refreshItems = new ArrayList<OleRequisitionItem>(0);

        for (OleRequisitionItem item : items) {
            item.setItemAdded(false);
            refreshItems.add(item);
        }

        ((OleOrderQueueDocument) orderForm.getDocument()).setRequisitionItems(refreshItems);
        LOG.debug("Leaving unselectAll of OleOrderQueueAction");
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, new JSTLConstants(KewApiConstants.class));
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    /**
     * Takes care of storing the action form in the User session and forwarding to the prlookup action.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward performPRLookup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LOG.debug("Inside performPRLookup of OleOrderQueueAction");
        ActionForward forward = super.performLookup(mapping, form, request, response);
        String path = forward.getPath();
        if (path.contains("kr/" + KRADConstants.LOOKUP_ACTION)) {
            path = path.replace("kr/" + KRADConstants.LOOKUP_ACTION, OLEConstants.PR_LOOKUP_ACTION);
        } else if (path.contains(KRADConstants.LOOKUP_ACTION)) {
            path = path.replace(KRADConstants.LOOKUP_ACTION, OLEConstants.PR_LOOKUP_ACTION);
        }
        forward.setPath(path);

        LOG.debug("Leaving performPRLookup of OleOrderQueueAction");
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, new JSTLConstants(KewApiConstants.class));
        return forward;

    }

    /**
     * This method returns the list of requisition items that satisfy the search criteria
     * input from Order Holding Queue page.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward searchRequisitions(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside searchRequisitions of OleOrderQueueAction");
        OleOrderQueueForm orderForm = (OleOrderQueueForm) form;
        OleOrderQueueDocument orderQueueDoc = (OleOrderQueueDocument) orderForm.getDocument();
        if (orderQueueDoc.getSelectorUserName() == null) {
            orderQueueDoc.setSelectorUserId(null);
        } else {
            Principal selector = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(orderQueueDoc.getSelectorUserName());
            orderQueueDoc.setSelectorUserId(selector != null ? selector.getPrincipalId() : null);
        }

        boolean rulePassed = SpringContext.getBean(KualiRuleService.class).applyRules(new OleAccountFilterEvent(orderQueueDoc,
                orderQueueDoc.getChartOfAccountsCode(), orderQueueDoc.getAccountNumber(), orderQueueDoc.getObjectCode()));
        if (rulePassed) {
            Map<String, String> searchCriteriaMap = orderQueueDoc.populateRequisitionFields();

            // Remove fields from search criteria which do not belong to OleRequisitionItem
            searchCriteriaMap.remove(OLEConstants.OrderQueue.selectorField);
            searchCriteriaMap.remove(OLEConstants.OrderQueue.workflowStatusChangeDateFrom);
            searchCriteriaMap.remove(OLEConstants.OrderQueue.workflowStatusChangeDateTo);

            Collection results = SpringContext.getBean(OleDocStoreLookupService.class).findCollectionBySearch(OleRequisitionItem.class, searchCriteriaMap);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Result size from OleOrderQueueAction.searchRequisitions before filtering - " + results.size());
            }

            // Checks Whether WorkflowStatusChangeDateFrom is greater than WorkflowStatusChangeDateTo if it is then stops filtering
            // process and display error message

            boolean isEndDateSmallerThanBeginDate = false;
            if (orderQueueDoc.getWorkflowStatusChangeDateFrom() != null && orderQueueDoc.getWorkflowStatusChangeDateTo() != null) {
                if (orderQueueDoc.getWorkflowStatusChangeDateFrom().compareTo(orderQueueDoc.getWorkflowStatusChangeDateTo()) > 0) {
                    isEndDateSmallerThanBeginDate = true;
                    GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.WORKFLOWSTATUSCHANGEDATETO_NOT_LESSER_THAN_WORKFLOWSTATUSCHANGEDATEFROM, new String[]{});
                }
            }
            if (!isEndDateSmallerThanBeginDate) {

                // Filter results for other search criteria that are not present in Requisition and Requisition Item related table

                List<OleRequisitionItem> finalSearchResultList = orderQueueDoc.filterOtherSearchCriteria((List<OleRequisitionItem>) results);
                ((OleOrderQueueDocument) orderForm.getDocument()).setRequisitionItems(finalSearchResultList);

                if (finalSearchResultList.size() <= 0) {
                    GlobalVariables.getMessageMap().putInfo(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_NO_REQUISITIONS_FOUND);
                }
            }
        } else {
            ((OleOrderQueueDocument) orderForm.getDocument()).setRequisitionItems(new ArrayList<OleRequisitionItem>());
        }

        LOG.debug("Leaving searchRequisitions of OleOrderQueueAction");
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, new JSTLConstants(KewApiConstants.class));
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    /**
     * @see org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase#cancel(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        KualiDocumentFormBase kualiDocumentFormBase = (KualiDocumentFormBase) form;
        return returnToSender(request, mapping, kualiDocumentFormBase);
    }

    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleOrderQueueForm orderForm = (OleOrderQueueForm) form;
        OleOrderQueueDocument orderQueueDoc = (OleOrderQueueDocument) orderForm.getDocument();
        orderQueueDoc.setPrincipalName(null);
        orderQueueDoc.setRequisitionDocNumber(null);
        orderQueueDoc.setRequisitionStatusCode(null);
        orderQueueDoc.setRequisitionStatus(null);
        orderQueueDoc.setVendorName(null);
        orderQueueDoc.setRequestorName(null);
        orderQueueDoc.setFormatTypeId(null);
        orderQueueDoc.setWorkflowStatusChangeDateFrom(null);
        orderQueueDoc.setWorkflowStatusChangeDateTo(null);
        orderQueueDoc.setTitle(null);
        orderQueueDoc.setAuthor(null);
        orderQueueDoc.setPublisher(null);
        orderQueueDoc.setSelectorUserId(null);
        orderQueueDoc.setSelectedUserId(null);
        orderQueueDoc.setRequisitionItems(null);
        orderQueueDoc.setAccountNumber(null);
        orderQueueDoc.setObjectCode(null);
        orderQueueDoc.setChartOfAccountsCode(null);
        orderQueueDoc.setSelectorUserName(null);
        orderQueueDoc.setIsbn(null);
        //request.setAttribute(OLEConstants.OrderQueue.CONSTANTS, new JSTLConstants(KewApiConstants.class));
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
        if(((OleOrderQueueForm) form).getMethodToCall() != null) {
            if(((OleOrderQueueForm) form).getMethodToCall().equalsIgnoreCase("docHandler")){
                ((OleOrderQueueForm) form).setDocId(null);
            }
        }
        return super.execute(mapping, form, request, response);
    }
}
