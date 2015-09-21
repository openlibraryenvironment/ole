package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sheiksalahudeenm on 9/9/2015.
 */
@Controller
@RequestMapping(value = "/noticeTypeConfigurationController")
public class OleNoticeTypeConfigurationController extends MaintenanceDocumentController {
    Logger LOG = Logger.getLogger(OleNoticeTypeConfigurationController.class);

    @Override
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm) form;
        ModelAndView modelAndView = super.start(maintenanceDocumentForm, result, request, response);
        MaintenanceDocument document = maintenanceDocumentForm.getDocument();
        document.getDocumentHeader().setDocumentDescription(getDocumentDescriptionForNew());
        return modelAndView;
    }

    @Override
    public ModelAndView maintenanceEdit(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm) form;
        ModelAndView modelAndView = super.maintenanceEdit(maintenanceDocumentForm, result, request, response);
        MaintenanceDocument document = maintenanceDocumentForm.getDocument();
        document.getDocumentHeader().setDocumentDescription(getDocumentDescriptionForEdit());
        return modelAndView;
    }

    private String getDocumentDescriptionForNew() {
        return GlobalVariables.getUserSession().getPrincipalName() + "-" +
                getDateString();

    }

    private String getDocumentDescriptionForEdit() {
        return GlobalVariables.getUserSession().getPrincipalName() + "-" +
                getDateString();
    }

    private String getDateString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return simpleDateFormat.format(new Date());
    }
}
