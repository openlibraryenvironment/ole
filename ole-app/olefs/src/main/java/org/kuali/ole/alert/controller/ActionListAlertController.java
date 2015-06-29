package org.kuali.ole.alert.controller;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.kuali.ole.alert.bo.ActionListAlertBo;
import org.kuali.ole.alert.form.AlertForm;
import org.kuali.ole.alert.service.AlertHelperService;
import org.kuali.ole.alert.service.impl.AlertHelperServiceImpl;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 11/4/14.
 */
@Controller
@RequestMapping(value = "/actionListAlertController")
public class ActionListAlertController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(ActionListAlertController.class);

    private AlertHelperServiceImpl alertHelperService = new AlertHelperServiceImpl();

    public AlertHelperServiceImpl getAlertHelperService() {
        return alertHelperService;
    }

    public void setAlertHelperService(AlertHelperServiceImpl alertHelperService) {
        this.alertHelperService = alertHelperService;
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        AlertForm alertForm = (AlertForm)form;
        try{
        String userId= GlobalVariables.getUserSession().getPrincipalId();
        alertHelperService.processApprovalPendingDocuments(userId);
        List<ActionListAlertBo> actionListAlertBos = alertHelperService.getActionListAlertsByUserId(userId);
        alertForm.setActionListAlertList(actionListAlertBos);
        }catch(NullPointerException npe){
            LOG.info("Exception occured while getting the principal id from the session " + npe.getMessage());
            Log.error(npe,npe);
            return getUIFModelAndView(alertForm);
        }catch(Exception e){
            LOG.info("Exception occured while getting the principal id from the session " + e.getMessage());
            Log.error(e,e);
            return getUIFModelAndView(alertForm);
        }
        return getUIFModelAndView(alertForm,"ActionListAlertPage");

    }


    @Override
    protected AlertForm createInitialForm(HttpServletRequest request) {
        return new AlertForm();
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=approveAlert")
    public ModelAndView approveAlert(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        AlertForm alertForm = (AlertForm)form;
        List<ActionListAlertBo> actionListAlertBos = new ArrayList<>();
        int index = Integer.parseInt(alertForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        for (int i=0;i<alertForm.getActionListAlertList().size();i++){
            ActionListAlertBo actionListAlertBo = alertForm.getActionListAlertList().get(i);
            if(index == i){
                alertHelperService.approveActionListAlert(actionListAlertBo);
            }else{
            actionListAlertBos.add(actionListAlertBo);
            }
        }
        /*ModelAndView modelAndView =  getCollectionControllerService().deleteLine(form);*/
        alertForm.setActionListAlertList(actionListAlertBos);
        return  getUIFModelAndView(alertForm,"ActionListAlertPage");
    }




}
