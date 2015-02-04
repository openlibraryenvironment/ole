package org.kuali.ole.select.controller;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEPOClaimHistory;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.form.OLEClaimingSearchForm;
import org.kuali.ole.select.service.OLEClaimingSearchService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 1/22/14
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/oleClaimingSearchController")
public class OLEClaimingSearchController extends UifControllerBase {
    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEClaimingSearchForm();
    }

    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLEClaimingSearchForm oleClaimingSearchForm = (OLEClaimingSearchForm) form;
        OLEClaimingSearchService oleClaimingSearchService=new OLEClaimingSearchService();
        oleClaimingSearchForm.setClaimErrorMessage("");
        oleClaimingSearchService.getClaimResponse(oleClaimingSearchForm);
        return getUIFModelAndView(oleClaimingSearchForm);
    }

    @RequestMapping(params = "methodToCall=clearSearch")
    public ModelAndView clearSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLEClaimingSearchForm oleClaimingSearchForm = (OLEClaimingSearchForm) form;
        oleClaimingSearchForm.setVendorName("");
        oleClaimingSearchForm.setTitle("");
        oleClaimingSearchForm.setClaimDate(null);
        oleClaimingSearchForm.setSuccessMsg("");
        oleClaimingSearchForm.setClaimErrorMessage("");
        oleClaimingSearchForm.setOleClaimingSearchRecordList(new ArrayList<OLEPOClaimHistory>());
        return getUIFModelAndView(oleClaimingSearchForm);
    }

    @RequestMapping(params = "methodToCall=updateClaimNote")
    public ModelAndView updateClaimNote(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLEClaimingSearchForm oleClaimingSearchForm = (OLEClaimingSearchForm) form;
        OLEClaimingSearchService oleClaimingSearchService=new OLEClaimingSearchService();
        oleClaimingSearchService.updateClaimNote(oleClaimingSearchForm);
        return getUIFModelAndView(oleClaimingSearchForm);
    }
}
