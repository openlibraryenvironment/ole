package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.form.LoanHistoryUpdateForm;
import org.kuali.ole.deliver.form.RequestNoticeUpdateForm;
import org.kuali.ole.deliver.util.LoanHistoryUtil;
import org.kuali.ole.deliver.util.RequestNoticeUtil;
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
 * Created by maheswarang on 4/13/16.
 */
@Controller
@RequestMapping(value = "/requestNoticeUpdateController")
public class RequestNoticeUpdateController extends UifControllerBase {
    private static final Logger LOG = Logger.getLogger(RequestNoticeUpdateController.class);
    private RequestNoticeUtil requestNoticeUtil;

    public RequestNoticeUtil getRequestNoticeUtil() {
        if(requestNoticeUtil == null){
            requestNoticeUtil = new RequestNoticeUtil();
        }
        return requestNoticeUtil;
    }
    @Override
    protected RequestNoticeUpdateForm createInitialForm(HttpServletRequest request) {
        return new RequestNoticeUpdateForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=update")
    public ModelAndView update(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        try{
            getRequestNoticeUtil().populateNoticeTableForExistingRequests();
        }catch (Exception e){
            LOG.info("Exception occured while running the job" + e.getMessage());
        }
        return getUIFModelAndView(form);
    }

}