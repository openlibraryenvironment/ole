package org.kuali.ole.describe.controller;


import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.form.CallNumberBrowseForm;
import org.kuali.ole.describe.service.BrowseService;
import org.kuali.ole.describe.service.impl.BrowseServiceImpl;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
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
import java.util.Collection;
import java.util.List;


@Controller
@RequestMapping(value = "/callnumberBrowseController")
public class CallNumberBrowseController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(CallNumberBrowseController.class);

    private BrowseService browseService;

    public BrowseService getBrowseService() {
        if (browseService == null)  {
            browseService = new BrowseServiceImpl();
        }
        return browseService;
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new CallNumberBrowseForm();
    }

    private boolean performCallNumberBrowse(String principalId) {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, OLEConstants.CAT_NAMESPACE, OLEConstants.CALL_NUMBER_BROWSE);
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the start method");
        CallNumberBrowseForm callNumberBrowseForm = (CallNumberBrowseForm) form;
        boolean hasPermission = performCallNumberBrowse(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasPermission) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_AUTHORIZATION);
            return getUIFModelAndView(callNumberBrowseForm);
        }
        return super.start(callNumberBrowseForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=rowsBrowse")
    public ModelAndView rowsBrowse(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside the browse method");
        CallNumberBrowseForm callNumberBrowseForm = (CallNumberBrowseForm) form;
        if (callNumberBrowseForm.getDocType().equalsIgnoreCase("item")) {
            List<Item> itemList = getBrowseService().callNumberBrowse(callNumberBrowseForm);
            callNumberBrowseForm.setItemList(itemList);
        } else {
            List<Holdings> holdingsList = getBrowseService().callNumberBrowse(callNumberBrowseForm);
            callNumberBrowseForm.setHoldingsList(holdingsList);
        }
        setPageNextPrevoiusAndEntriesIno(callNumberBrowseForm);
        return getUIFModelAndView(callNumberBrowseForm, "CallNumberBrowseViewPage");
    }

    @RequestMapping(params = "methodToCall=browse")
    public ModelAndView browse(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside the browse method");
        CallNumberBrowseForm callNumberBrowseForm = (CallNumberBrowseForm) form;
        boolean hasPermission = performCallNumberBrowse(GlobalVariables.getUserSession().getPrincipalId());
        if (!hasPermission) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_INFO, OLEConstants.ERROR_AUTHORIZATION);
            return getUIFModelAndView(callNumberBrowseForm);
        }
        String location = validateLocation(callNumberBrowseForm.getLocation());
        callNumberBrowseForm.setLocation(location);
        callNumberBrowseForm.setPageSize(10);
        if (callNumberBrowseForm.getDocType().equalsIgnoreCase("item")) {
            List<Item> itemList = getBrowseService().callNumberBrowse(callNumberBrowseForm);
            callNumberBrowseForm.setItemList(itemList);
        } else {
            List<Holdings> holdingsList = getBrowseService().callNumberBrowse(callNumberBrowseForm);
            callNumberBrowseForm.setHoldingsList(holdingsList);
        }
        setPageNextPrevoiusAndEntriesIno(callNumberBrowseForm);
        return getUIFModelAndView(callNumberBrowseForm, "CallNumberBrowseViewPage");
    }

    @RequestMapping(params = "methodToCall=previous")
    public ModelAndView previous(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside the browse method");
        CallNumberBrowseForm callNumberBrowseForm = (CallNumberBrowseForm) form;
        if (callNumberBrowseForm.getDocType().equalsIgnoreCase("item")) {
            List<Item> itemList = getBrowseService().callNumberBrowsePrev(callNumberBrowseForm);
            callNumberBrowseForm.setItemList(itemList);
        } else {
            List<Holdings> holdingsList = getBrowseService().callNumberBrowsePrev(callNumberBrowseForm);
            callNumberBrowseForm.setHoldingsList(holdingsList);
        }
        setPageNextPrevoiusAndEntriesIno(callNumberBrowseForm);
        return getUIFModelAndView(callNumberBrowseForm, "CallNumberBrowseViewPage");
    }

    @RequestMapping(params = "methodToCall=next")
    public ModelAndView next(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside the browse method");
        CallNumberBrowseForm callNumberBrowseForm = (CallNumberBrowseForm) form;
        if (callNumberBrowseForm.getDocType().equalsIgnoreCase("item")) {
            List<Item> itemList = getBrowseService().callNumberBrowseNext(callNumberBrowseForm);
            callNumberBrowseForm.setItemList(itemList);
        } else {
            List<Holdings> holdingsList = getBrowseService().callNumberBrowseNext(callNumberBrowseForm);
            callNumberBrowseForm.setHoldingsList(holdingsList);
        }
        setPageNextPrevoiusAndEntriesIno(callNumberBrowseForm);
        return getUIFModelAndView(callNumberBrowseForm, "CallNumberBrowseViewPage");
    }


    @RequestMapping(params = "methodToCall=clear")
    public ModelAndView clear(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the clear method");
        CallNumberBrowseForm callNumberBrowseForm = (CallNumberBrowseForm) form;
        callNumberBrowseForm.setMessage("");
        callNumberBrowseForm.setInformation("");
        callNumberBrowseForm.setHoldingsList(null);
        callNumberBrowseForm.setItemList(null);
        callNumberBrowseForm.setPageSize(10);
        callNumberBrowseForm.setPreviousFlag(false);
        callNumberBrowseForm.setNextFlag(false);
        callNumberBrowseForm.setCallNumberBrowseText("");
        //callNumberBrowseForm.setCloseBtnShowFlag(false);
        return super.start(callNumberBrowseForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=back")
    public ModelAndView back(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the clear method");
        return super.back(form, result, request, response);
    }

    public void setPageNextPrevoiusAndEntriesIno(CallNumberBrowseForm callNumberBrowseForm) {
        callNumberBrowseForm.setPreviousFlag(getBrowseService().getPreviosFlag());
        callNumberBrowseForm.setNextFlag(getBrowseService().getNextFlag());
        callNumberBrowseForm.setPageShowEntries(getBrowseService().getPageShowEntries());
    }

    public String validateLocation(String locationString) {
        if (locationString != null) {
            String[] arr = locationString.split("/");
            for (String location : arr) {
                if (isValidLibraryLevel(location)) {
                    return location;
                }
            }
        }
        return null;
    }

    public boolean isValidLibraryLevel(String location) {
        boolean valid = false;
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Collection<OleLocation> oleLocationCollection = businessObjectService.findAll(OleLocation.class);
        for (OleLocation oleLocation : oleLocationCollection) {
            String locationCode = oleLocation.getLocationCode();
            String levelCode = oleLocation.getOleLocationLevel().getLevelCode();
            if ("LIBRARY".equals(levelCode) && (locationCode.equals(location))) {
                valid = true;
                return valid;
            }
        }
        return valid;
    }

}