package org.kuali.ole.gobi.documentController;

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

@Controller
@RequestMapping(value = "/gobiSubAccountDeliveryAddressDocumentController")
public class GobiSubAccountDeliveryAddressDocumentController extends MaintenanceDocumentController {
    Logger LOG = Logger.getLogger(GobiSubAccountDeliveryAddressDocumentController.class);

    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm) form;
        ModelAndView modelAndView = super.start(maintenanceDocumentForm, result, request, response);
        MaintenanceDocument document = maintenanceDocumentForm.getDocument();
        document.getDocumentHeader().setDocumentDescription(getDocumentDescription());
        return modelAndView;
    }

    @Override
    public ModelAndView maintenanceEdit(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm) form;
        ModelAndView modelAndView = super.maintenanceEdit(maintenanceDocumentForm, result, request, response);
        MaintenanceDocument document = maintenanceDocumentForm.getDocument();
        document.getDocumentHeader().setDocumentDescription(getDocumentDescription());
        return modelAndView;
    }

    @Override
    public ModelAndView maintenanceCopy(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceDocumentForm = (MaintenanceDocumentForm) form;
        ModelAndView modelAndView = super.maintenanceCopy(form, result, request, response);
        MaintenanceDocument document = maintenanceDocumentForm.getDocument();
        document.getDocumentHeader().setDocumentDescription(getDocumentDescription());
        return modelAndView;
    }

    private String getDocumentDescription() {
        return GlobalVariables.getUserSession().getPrincipalName() + "-" +
                getDateString();

    }

    private String getDateString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return simpleDateFormat.format(new Date());
    }
}
