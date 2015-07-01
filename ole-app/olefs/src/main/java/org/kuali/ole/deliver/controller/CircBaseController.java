package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.keyvalue.CirculationDeskChangeKeyValue;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.exception.DocumentAuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by hemalathas on 6/21/15.
 */
public class CircBaseController extends UifControllerBase{

    private static final Logger LOG = Logger.getLogger(CircBaseController.class);

    private CircDeskLocationResolver circDeskLocationResolver;
    public volatile static String fastAddBarcode = "";

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest httpServletRequest) {
        return new CircForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {

        CircForm circForm = null;

        if (form.getViewId().equalsIgnoreCase("circView")) {
            String formKey = request.getParameter("formKey");

            if (null == formKey) {
                circForm = (CircForm) form;
            } else {
                circForm = (CircForm) GlobalVariables.getUifFormManager().getSessionForm(formKey);
            }

            setPrincipalInfo(circForm);

            List<KeyValue> keyValues = new CirculationDeskChangeKeyValue().getKeyValues();
            if (keyValues.isEmpty()) {
                throw new DocumentAuthorizationException(GlobalVariables.getUserSession().getPrincipalId(), "not Authorized", form.getViewId());
            } else {
                circForm.setSelectedCirculationDesk(keyValues.get(0).getValue());
                circForm.setPreviouslySelectedCirculationDesk(circForm.getSelectedCirculationDesk());
            }
        }
        return super.start(circForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=clearUI")
    public ModelAndView clearUI(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        circForm.reset();
        return getUIFModelAndView(form, "circViewPage");
    }

    private void setPrincipalInfo(CircForm circForm) {
        if (org.apache.commons.lang3.StringUtils.isBlank(circForm.getLoggedInUser()))
            circForm.setLoggedInUser(GlobalVariables.getUserSession().getPrincipalId());
    }

    public String getCircDesk(String principalId) {
        String circulationDeskId = getCircDeskLocationResolver().getCircDeskForOpertorId(principalId).getCirculationDeskId();
        return circulationDeskId;

    }

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void showErrorMessageDialog(UifFormBase form, HttpServletRequest request, HttpServletResponse response) {
        showDialog("ptrnValidationErrorMessageDialog", form, request, response);
    }
}
