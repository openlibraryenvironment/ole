package org.kuali.ole.deliver.controller;

import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.krad.web.controller.LookupController;
import org.kuali.rice.krad.web.form.LookupForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 3/18/16.
 */
@Controller
@RequestMapping(value = "/patronLookup")
public class OlePatronDocumentLookupController extends LookupController {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronDocumentLookupController.class);


    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=globalEditPatron")
    public ModelAndView globalEditPatron(@ModelAttribute("KualiForm")UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response, final RedirectAttributes redirectAttributes) {

        LookupForm lookupForm = (LookupForm) form;
        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>)lookupForm.getLookupResults();
        List<String> selectedPatrons = new ArrayList<>();
        for(OlePatronDocument olePatronDocument : olePatronDocumentList){
            if(olePatronDocument.isSelect()) {
                selectedPatrons.add(olePatronDocument.getOlePatronId());
            }
        }
        request.getSession().setAttribute("selectedPatronId", selectedPatrons);
        return navigate(lookupForm,result,request,response);
    }

}
