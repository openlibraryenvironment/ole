package org.kuali.ole.deliver.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;
import org.kuali.rice.krad.web.controller.LookupController;
import org.kuali.rice.krad.web.form.LookupForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by maheswarang on 3/18/16.
 */
@Controller
@RequestMapping(value = "/requestLookup")
public class OleDeliverRequestLookupController extends LookupController {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleDeliverRequestLookupController.class);
private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService ;

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService(){
        if(oleDeliverRequestDocumentHelperService == null){
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=cancelRequests")
    public ModelAndView cancelRequests(@ModelAttribute("KualiForm") LookupForm lookupForm, BindingResult result,
                                 HttpServletRequest request, HttpServletResponse response, final RedirectAttributes redirectAttributes) {
      List<OleDeliverRequestBo> deliverRequestBoList = (List<OleDeliverRequestBo>)lookupForm.getLookupResults();
       List<OleDeliverRequestBo> selectedList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> unSelectedList = new ArrayList<OleDeliverRequestBo>();
        for(OleDeliverRequestBo oleDeliverRequestBo : deliverRequestBoList){
            if(oleDeliverRequestBo.isSelect()){
                selectedList.add(oleDeliverRequestBo);
            }else{
                unSelectedList.add(oleDeliverRequestBo);
            }
        }
        getOleDeliverRequestDocumentHelperService().cancelRequests(selectedList);
        lookupForm.setLookupResults(unSelectedList);
        return start(lookupForm,result,request,response);

    }


}
