package org.kuali.ole.select.controller;

import org.kuali.ole.select.document.OLEEResourceSynchronizationGokbLog;
import org.kuali.ole.select.form.OLEGOKbConfigForm;
import org.kuali.ole.service.OLEEResourceHelperService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by srirams on 24/9/14.
 */
@Controller
@RequestMapping(value = "/oleGOKbConfigController")
public class OLEGOKbConfigController extends UifControllerBase {

    private OLEEResourceHelperService oleeResourceHelperService = new OLEEResourceHelperService();

    public OLEEResourceHelperService getOleeResourceHelperService() {
        if(oleeResourceHelperService == null) {
            oleeResourceHelperService = new OLEEResourceHelperService();
        }
        return oleeResourceHelperService;
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEGOKbConfigForm();
    }

    /**
     * This method takes the initial request when click on GOKb Configuration
     * @param uifForm
     * @param request
     * @param response
     * @return
     */

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OLEGOKbConfigForm olegoKbConfigForm = (OLEGOKbConfigForm) uifForm;
        return super.start(olegoKbConfigForm,result,request,response);
    }

    /**
     * Saves the GOKb config contained on the form
     * @param uifForm
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        OLEGOKbConfigForm olegoKbConfigForm = (OLEGOKbConfigForm) uifForm;
        return getUIFModelAndView(olegoKbConfigForm);
    }

    @RequestMapping(params = "methodToCall=sync")
    public ModelAndView sync(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        OLEGOKbConfigForm olegoKbConfigForm = (OLEGOKbConfigForm) uifForm;
        oleeResourceHelperService.retrieveAndApplyGokbChanges();
        return getUIFModelAndView(olegoKbConfigForm);
    }

    @RequestMapping(params = "methodToCall=refreshLog")
    public ModelAndView refreshLog(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        OLEGOKbConfigForm olegoKbConfigForm = (OLEGOKbConfigForm) uifForm;
        List<OLEEResourceSynchronizationGokbLog> eResourceSynchronizationGokbLogs = (List<OLEEResourceSynchronizationGokbLog>) KRADServiceLocator.getBusinessObjectService().findAll(OLEEResourceSynchronizationGokbLog.class);
        olegoKbConfigForm.setGokbLogs(eResourceSynchronizationGokbLogs);
        return getUIFModelAndView(olegoKbConfigForm);
    }


}
