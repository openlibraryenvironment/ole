package org.kuali.ole.select.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLESearchCondition;
import org.kuali.ole.select.bo.OLESearchParams;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.form.OLEEResourceSearchForm;
import org.kuali.ole.service.impl.OLEEResourceSearchServiceImpl;
import org.kuali.ole.service.impl.OleLicenseRequestServiceImpl;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/26/13
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/searchEResourceController")
public class OLEEResourceSearchController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(OLEEResourceSearchController.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(OLEConstants.OLEEResourceRecord.CREATED_DATE_FORMAT);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT);

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        return new OLEEResourceSearchForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceSearchForm oleSearchForm = (OLEEResourceSearchForm) form;
        List<OLESearchCondition> oleSearchConditions = oleSearchForm.getOleSearchParams().getSearchFieldsList();
        for (OLESearchCondition oleSearchCondition : oleSearchConditions) {
            oleSearchCondition.setOperator(OLEConstants.OLEEResourceRecord.AND);
        }
        return super.navigate(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=addSearchCriteria")
    public ModelAndView addSearchCriteria(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceSearchForm oleSearchForm = (OLEEResourceSearchForm) form;
        List<OLESearchCondition> oleSearchConditions = new ArrayList<OLESearchCondition>();
        oleSearchConditions = oleSearchForm.getOleSearchParams().getSearchFieldsList();
        oleSearchConditions.add(new OLESearchCondition());
        for (OLESearchCondition oleSearchCondition : oleSearchConditions) {
            if (oleSearchCondition.getOperator() == null) {
                oleSearchCondition.setOperator(OLEConstants.OLEEResourceRecord.AND);
            }
        }
        return super.navigate(oleSearchForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceSearchForm oleSearchForm = (OLEEResourceSearchForm) form;
        OLEEResourceSearchServiceImpl oleEResourceSearchService = GlobalResourceLoader.getService(OLEConstants.OLEEResourceRecord.ERESOURSE_SEARCH_SERVICE);
        List<OLESearchCondition> oleSearchConditionsList = oleSearchForm.getOleSearchParams().getSearchFieldsList();
        List<OLEEResourceRecordDocument> eresourceDocumentList = new ArrayList<OLEEResourceRecordDocument>();
        List<OLEEResourceRecordDocument> eresourceList = new ArrayList<OLEEResourceRecordDocument>();
        List<OLEEResourceRecordDocument> eresourceRecordDocumentList = new ArrayList<OLEEResourceRecordDocument>();
        try {
            eresourceList = oleEResourceSearchService.performSearch(oleSearchConditionsList);
        } catch (Exception e) {
            LOG.error("Exception while hitting the docstore time" + e.getMessage());
        }

        for (OLEEResourceRecordDocument oleEResourceRecordDocument: eresourceList){
            if (oleEResourceRecordDocument.getOleERSIdentifier()!=null){
                OLEEResourceRecordDocument document = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OLEEResourceRecordDocument.class, oleEResourceRecordDocument.getOleERSIdentifier());
                if (document!=null){
                    eresourceRecordDocumentList.add(document);
                }
            }
        }
        eresourceList = eresourceRecordDocumentList;

        if (oleSearchForm.getStatus() != null) {
            eresourceList = oleEResourceSearchService.statusNotNull(eresourceList, oleSearchForm.getStatus());
        }
        if (oleSearchForm.iseResStatusDate()) {
            try {
                Date beginDate = oleSearchForm.getBeginDate();
                String begin = null;
                if (beginDate != null) {
                    begin = dateFormat.format(beginDate);
                }
                Date endDate = oleSearchForm.getEndDate();
                String end = null;
                if (endDate != null) {
                    end = dateFormat.format(endDate);
                }
                boolean isValid = false;
                eresourceDocumentList.clear();
                for (OLEEResourceRecordDocument oleEResourceRecordDocumentList : eresourceList) {
                    String status = oleEResourceRecordDocumentList.getStatusDate();
                    Date statusDate = simpleDateFormat.parse(status);
                    OleLicenseRequestServiceImpl oleLicenseRequestService = GlobalResourceLoader.getService(OLEConstants.OleLicenseRequest.LICENSE_REQUEST_SERVICE);
                    isValid = oleLicenseRequestService.validateDate(statusDate, begin, end);
                    if (isValid) {
                        eresourceDocumentList.add(oleEResourceRecordDocumentList);
                    }
                }
            } catch (Exception e) {
                LOG.error("Exception while calling the licenseRequest service" + e.getMessage());
                throw new RuntimeException(e);
            }
            oleSearchForm.setEresourceDocumentList(eresourceDocumentList);
        } else {
            oleSearchForm.setEresourceDocumentList(eresourceList);
        }
        List<OLEEResourceRecordDocument> eresDocumentList = oleSearchForm.getEresourceDocumentList();
        removeDuplicateEresDocumentsFromList(eresDocumentList);
        if (!GlobalVariables.getMessageMap().hasMessages()){
            if (oleSearchForm.getEresourceDocumentList().size()==0)
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
        }
        else {
            oleSearchForm.setEresourceDocumentList(null);
        }
        return getUIFModelAndView(oleSearchForm);
    }

    @RequestMapping(params = "methodToCall=clearSearch")
    public ModelAndView clearSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceSearchForm oleSearchForm = (OLEEResourceSearchForm) form;
        List<OLESearchCondition> oleSearchConditions = oleSearchForm.getOleSearchParams().getSearchFieldsList();
        int searchConditionSize = oleSearchConditions.size();
        oleSearchForm.setOleSearchParams(new OLESearchParams());
        oleSearchConditions = oleSearchForm.getOleSearchParams().getSearchFieldsList();
        for (int ersCount = 0; ersCount < searchConditionSize; ersCount++) {
            oleSearchConditions.add(new OLESearchCondition());
        }
        for (OLESearchCondition oleSearchCondition : oleSearchConditions) {
            oleSearchCondition.setOperator(OLEConstants.OLEEResourceRecord.AND);
        }
        oleSearchForm.setEresourceDocumentList(null);
        oleSearchForm.seteResStatusDate(false);
        oleSearchForm.setBeginDate(null);
        oleSearchForm.setEndDate(null);
        oleSearchForm.setStatus(null);
        return getUIFModelAndView(oleSearchForm);
    }

    @RequestMapping(params = "methodToCall=cancel")
    public ModelAndView cancel(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLEEResourceSearchForm oleSearchForm = (OLEEResourceSearchForm) form;
        return super.cancel(oleSearchForm, result, request, response);
    }

    private void removeDuplicateEresDocumentsFromList(List<OLEEResourceRecordDocument> eresourceDocumentList) {
        Map eresourceMap = new HashMap();
        List eResourceList = new ArrayList();
        for (OLEEResourceRecordDocument oleEResourceRecordDocument : eresourceDocumentList) {
            eresourceMap.put(oleEResourceRecordDocument.getDocumentNumber(), oleEResourceRecordDocument);
        }
        eResourceList.addAll((Set) eresourceMap.keySet());
        eresourceDocumentList.clear();
        for (int eResourceCount = 0; eResourceCount < eResourceList.size(); eResourceCount++) {
            eresourceDocumentList.add((OLEEResourceRecordDocument) eresourceMap.get(eResourceList.get(eResourceCount)));
        }
    }

}

