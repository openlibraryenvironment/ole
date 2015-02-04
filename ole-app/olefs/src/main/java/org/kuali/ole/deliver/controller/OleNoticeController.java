package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.form.OleNoticeForm;
import org.kuali.ole.deliver.service.OLEDeliverNoticeHelperService;
import org.kuali.ole.deliver.service.impl.OLEDeliverNoticeHelperServiceImpl;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by maheswarang on 9/18/14.
 */
@Controller
@RequestMapping(value = "/oleNoticeController")
public class OleNoticeController extends UifControllerBase {
    private static final Logger LOG = Logger.getLogger(OleNoticeController.class);

    private OLEDeliverNoticeHelperService oleDeliverNoticeHelperService;

    public OLEDeliverNoticeHelperService getOleDeliverNoticeHelperService() {
        if(oleDeliverNoticeHelperService == null){
            oleDeliverNoticeHelperService = new OLEDeliverNoticeHelperServiceImpl();
        }
        return oleDeliverNoticeHelperService;
    }

    public void setOleDeliverNoticeHelperService(OLEDeliverNoticeHelperService oleDeliverNoticeHelperService) {
        this.oleDeliverNoticeHelperService = oleDeliverNoticeHelperService;
    }


    @Override
    protected OleNoticeForm createInitialForm(HttpServletRequest request) {
        return new OleNoticeForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=run")
    public ModelAndView run(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        try{
            getOleDeliverNoticeHelperService().updateDeliverNoticeForUnprocessedLoans();
        }catch (Exception e){
            LOG.info("Exception occured while running the job" + e.getMessage());
        }
        return getUIFModelAndView(form);
    }
}
