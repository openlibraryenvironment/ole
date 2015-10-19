package org.kuali.ole.select.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLESearchCondition;
import org.kuali.ole.select.bo.OLESearchParams;
import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.ole.select.form.OLEPlatformSearchForm;
import org.kuali.ole.service.OLEPlatformService;
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
import java.util.*;

/**
 * Created by chenchulakshmig on 9/9/14.
 * The OLEPlatformSearchController is the controller class for processing all the actions that corresponds to the Platform Search functionality in OLE.
 * The request mapping tag takes care of mapping the individual action to the corresponding functions.
 */
@Controller
@RequestMapping(value = "/platformSearchController")
public class OLEPlatformSearchController extends UifControllerBase {

    private OLEPlatformService olePlatformService;

    public OLEPlatformService getOlePlatformService() {
        if (olePlatformService == null) {
            olePlatformService = GlobalResourceLoader.getService(OLEConstants.PLATFORM_SERVICE);
        }
        return olePlatformService;
    }

    /**
     * This method creates new OLEPlatformSearchForm
     *
     * @param request
     * @return OLEPlatformSearchForm
     */
    //@Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEPlatformSearchForm();
    }

    /**
     * This method is for setting the form to the screen
     *
     * @param form
     * @param request
     * @param response
     * @return ModelAndView
     */
    //@Override
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformSearchForm olePlatformSearchForm = (OLEPlatformSearchForm) form;
        List<OLESearchCondition> oleSearchConditions = olePlatformSearchForm.getOleSearchParams().getSearchFieldsList();
        for (OLESearchCondition oleSearchCondition : oleSearchConditions) {
            oleSearchCondition.setOperator(OLEConstants.AND_SEARCH_SCOPE);
        }
        return super.navigate(olePlatformSearchForm, result, request, response);
    }

    /**
     * This method is for searching the platform details
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformSearchForm olePlatformSearchForm = (OLEPlatformSearchForm) form;
        List<OLESearchCondition> oleSearchConditionsList = olePlatformSearchForm.getOleSearchParams().getSearchFieldsList();
        List<OLEPlatformRecordDocument> platformList = getOlePlatformService().performSearch(oleSearchConditionsList);
        Set<String> platformIds = new HashSet<>();
        for (OLEPlatformRecordDocument olePlatformRecordDocument : platformList) {
            if (StringUtils.isNotBlank(olePlatformRecordDocument.getOlePlatformId())) {
                platformIds.add(olePlatformRecordDocument.getOlePlatformId());
            }
        }
        if (CollectionUtils.isNotEmpty(platformIds)) {
            Map platformMap = new HashMap<>();
            platformMap.put("olePlatformId", platformIds);
            List<OLEPlatformRecordDocument> olePlatformRecordDocumentList = (List<OLEPlatformRecordDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEPlatformRecordDocument.class, platformMap);
            olePlatformSearchForm.setPlatformDocumentList(olePlatformRecordDocumentList);
        }
        if (!GlobalVariables.getMessageMap().hasMessages()) {
            if (olePlatformSearchForm.getPlatformDocumentList().size() == 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.NO_RECORD_FOUND);
            }
        } else {
            olePlatformSearchForm.setPlatformDocumentList(null);
        }
        return getUIFModelAndView(olePlatformSearchForm);
    }

    /**
     * This method is to add one more search criteria
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addSearchCriteria")
    public ModelAndView addSearchCriteria(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformSearchForm olePlatformSearchForm = (OLEPlatformSearchForm) form;
        List<OLESearchCondition> oleSearchConditions = new ArrayList<OLESearchCondition>();
        oleSearchConditions = olePlatformSearchForm.getOleSearchParams().getSearchFieldsList();
        oleSearchConditions.add(new OLESearchCondition());
        for (OLESearchCondition oleSearchCondition : oleSearchConditions) {
            if (oleSearchCondition.getOperator() == null) {
                oleSearchCondition.setOperator(OLEConstants.OLEEResourceRecord.AND);
            }
        }
        return super.navigate(olePlatformSearchForm, result, request, response);
    }

    /**
     * This method is to clear the search criteria values
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=clearSearch")
    public ModelAndView clearSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformSearchForm olePlatformSearchForm = (OLEPlatformSearchForm) form;
        List<OLESearchCondition> oleSearchConditions = olePlatformSearchForm.getOleSearchParams().getSearchFieldsList();
        int searchConditionSize = oleSearchConditions.size();
        olePlatformSearchForm.setOleSearchParams(new OLESearchParams());
        oleSearchConditions = olePlatformSearchForm.getOleSearchParams().getSearchFieldsList();
        for (int ersCount = 0; ersCount < searchConditionSize; ersCount++) {
            oleSearchConditions.add(new OLESearchCondition());
        }
        for (OLESearchCondition oleSearchCondition : oleSearchConditions) {
            oleSearchCondition.setOperator(OLEConstants.OLEEResourceRecord.AND);
        }
        return getUIFModelAndView(olePlatformSearchForm);
    }

    @RequestMapping(params = "methodToCall=cancel")
    public ModelAndView cancel(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformSearchForm olePlatformSearchForm = (OLEPlatformSearchForm) form;
        return super.cancel(olePlatformSearchForm, result, request, response);
    }

}
