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

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.select.businessobject.OleAcquisitionSearchResult;
import org.kuali.ole.select.document.OleAcquisitionsSearchDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kns.web.struts.action.KualiTransactionalDocumentActionBase;
import org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the Action Class for Acquisitions Search
 */
public class OleAcquisitionsSearchAction extends KualiTransactionalDocumentActionBase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleAcquisitionsSearchAction.class);

    /**
     * This method returns the list of requisition items that satisfy the search criteria
     * input from Acquisitions Search page.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward search(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside searchRequisitions of OleAcquisitionsSearchAction");
        Long begin = System.currentTimeMillis();
        OleAcquisitionsSearchForm acqForm = (OleAcquisitionsSearchForm) form;
        OleAcquisitionsSearchDocument acqDoc = (OleAcquisitionsSearchDocument) acqForm.getDocument();

        // Modified for Jira Ole-2563 by Aditya Starts

        boolean isToDateLesserThanFromDate;
        isToDateLesserThanFromDate = acqDoc.isToDateLesserThanFromDate();
        if (!isToDateLesserThanFromDate) {
            List<OleAcquisitionSearchResult> displayList = acqDoc.searchResults();
            if (displayList.size() > 0) {
                acqDoc.setAcqSearchResults(displayList);
            } else {
                acqDoc.setAcqSearchResults(new ArrayList<OleAcquisitionSearchResult>());
                GlobalVariables.getMessageMap().clearErrorMessages();
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_NO_RESULTS_FOUND);
            }
        } else {
            GlobalVariables.getMessageMap().putError(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_DATE_TO_NOT_LESSER_THAN_DATE_FROM, new String[]{});
        }
        Long end = System.currentTimeMillis();
        Long timeTaken = end-begin;
        LOG.info("The Time Taken for Fetching Acquisition Search : "+timeTaken);

        // Modified for Jira Ole-2563 by Aditya Ends

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

    /**
     * This method clears the search criteria's that is on the Acquisitions Search Page
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OleAcquisitionsSearchForm searchForm = (OleAcquisitionsSearchForm) form;
        OleAcquisitionsSearchDocument oleAcqSearchDoc = (OleAcquisitionsSearchDocument) searchForm.getDocument();
        oleAcqSearchDoc.setDocumentType(null);
        oleAcqSearchDoc.setDocNumber(null);
        oleAcqSearchDoc.setPurapDocumentIdentifier(null);
        oleAcqSearchDoc.setVendorName(null);
        oleAcqSearchDoc.setDateFrom(null);
        oleAcqSearchDoc.setDateTo(null);
        oleAcqSearchDoc.setInitiator(null);
        oleAcqSearchDoc.setRequestorName(null);
        oleAcqSearchDoc.setAccountNumber(null);
        oleAcqSearchDoc.setOrganizationCode(null);
        oleAcqSearchDoc.setChartOfAccountsCode(null);
        oleAcqSearchDoc.setTitle(null);
        oleAcqSearchDoc.setPublisher(null);
        oleAcqSearchDoc.setIsbn(null);
        oleAcqSearchDoc.setAuthor(null);
        oleAcqSearchDoc.setLocalIdentifier(null);
        oleAcqSearchDoc.setAcqSearchResults(new ArrayList<OleAcquisitionSearchResult>());
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

        LOG.debug("Inside performPRLookup of OleAcquisitionsSearchAction");
        ActionForward forward = super.performLookup(mapping, form, request, response);
        String path = forward.getPath();
        if (path.contains("kr/" + KRADConstants.LOOKUP_ACTION)) {
            path = path.replace("kr/" + KRADConstants.LOOKUP_ACTION, OLEConstants.PR_LOOKUP_ACTION);
        } else if (path.contains(KRADConstants.LOOKUP_ACTION)) {
            path = path.replace(KRADConstants.LOOKUP_ACTION, OLEConstants.PR_LOOKUP_ACTION);
        }
        forward.setPath(path);

        LOG.debug("Leaving performPRLookup of OleAcquisitionsSearchAction");
        return forward;

    }

    /**
     * This method... find the List Of All POs search for batch upload
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward docHandler(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside searchRequisitions of OleAcquisitionsSearchAction");
        ActionForward af = super.docHandler(mapping, form, request, response);
        String loadSumId = ((OleAcquisitionsSearchForm) form).getLoadSumId();
        if (StringUtils.isNotEmpty(loadSumId)) {
            OleAcquisitionsSearchDocument acqDoc = (OleAcquisitionsSearchDocument) ((OleAcquisitionsSearchForm) form).getDocument();
            List<OleAcquisitionSearchResult> displayList = acqDoc.listOfPOsSearchResults(loadSumId);
            if (displayList.size() > 0) {
                acqDoc.setAcqSearchResults(displayList);
            } else {
                acqDoc.setAcqSearchResults(new ArrayList<OleAcquisitionSearchResult>());
                GlobalVariables.getMessageMap().putInfo(OLEConstants.OrderQueue.REQUISITIONS, OLEKeyConstants.ERROR_NO_RESULTS_FOUND);
            }
            ((OleAcquisitionsSearchForm) form).setLoadSumId(null);
        }
        return af;

    }


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = null;
        if(((OleAcquisitionsSearchForm) form).getMethodToCall() != null && ((OleAcquisitionsSearchForm) form).getMethodToCall().equalsIgnoreCase("docHandler")){
            ((OleAcquisitionsSearchForm) form).setDocId(null);
        }
        return super.execute(mapping, form, request, response);
    }

}
