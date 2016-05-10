package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.form.LoanHistoryUpdateForm;
import org.kuali.ole.deliver.util.LoanHistoryUtil;
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
 * Created by gopalp on 3/18/16.
 */
@Controller
@RequestMapping(value = "/loanHistoryUpdateController")
public class LoanHistoryUpdateController extends UifControllerBase {
    private static final Logger LOG = Logger.getLogger(LoanHistoryUpdateController.class);
    private LoanHistoryUtil loanHistoryUtil;

    public LoanHistoryUtil getLoanHistoryUtil() {
        if(loanHistoryUtil == null){
            loanHistoryUtil = new LoanHistoryUtil();
        }
        return loanHistoryUtil;
    }
    @Override
    protected LoanHistoryUpdateForm createInitialForm(HttpServletRequest request) {
        return new LoanHistoryUpdateForm();
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
        LoanHistoryUpdateForm loanHistoryUpdateForm = (LoanHistoryUpdateForm) form;
        try{
            loanHistoryUpdateForm.setRunning(LoanHistoryUtil.taskRunning);
            if(!loanHistoryUpdateForm.isRunning()) {
                String message = getLoanHistoryUtil().populateCirculationHistoryTable();
                loanHistoryUpdateForm.setMessage(message);
            } else {
                loanHistoryUpdateForm.setMessage("Already process running. Please wait till process complete.");
            }
        }catch (Exception e){
            LOG.info("Exception occured while running the job" + e.getMessage());
        }
        return getUIFModelAndView(form);
    }
}
