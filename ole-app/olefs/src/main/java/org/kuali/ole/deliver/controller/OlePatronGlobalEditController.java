package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.form.OlePatronMergeForm;
import org.kuali.ole.deliver.form.PatronGlobalEditForm;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.service.OlePatronConverterService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/11/12
 * Time: 12:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/patronGlobalEditController")
public class OlePatronGlobalEditController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(OlePatronGlobalEditController.class);
    private BusinessObjectService businessObjectService;

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new PatronGlobalEditForm();
    }

    @RequestMapping(params = "methodToCall=start")
    @Override
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        PatronGlobalEditForm patronGlobalEditForm = (PatronGlobalEditForm) form;
        return super.start(patronGlobalEditForm, result, request, response);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @RequestMapping(params = "methodToCall=saveGlobalEditPatron")
    public ModelAndView saveGlobalEditPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        PatronGlobalEditForm patronGlobalEditForm = (PatronGlobalEditForm) form;
        List<OlePatronDocument> selectedPatrons = new ArrayList<>();
        try {
            OlePatronConverterService olePatronConverterService = GlobalResourceLoader.getService(OLEConstants.PATRON_CONVERTER_SERVICE);
            List<String> patronIds =  (List<String>) request.getSession().getAttribute("selectedPatronId");
            olePatronConverterService.saveGlobalEditPatrons(patronIds, patronGlobalEditForm);
            GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, "marc.editor.success.message");
        }catch(Exception e){
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, "record.save.fail.message");
        }
        ModelAndView modelAndView = getUIFModelAndView(patronGlobalEditForm);
        return modelAndView;
    }
}