package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.form.OleCirculationDeskDetailForm;
import org.kuali.ole.deliver.service.OleCirculationDeskDetailServiceImpl;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.exception.DocumentAuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;
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
 * The OleCirculationDeskDetailController is the controller class for processing all the actions that corresponds to the Circulation Desk functionality in OLE.
 * The request mapping tag takes care of mapping the individual action to the corresponding functions.
 */
@Controller
@RequestMapping(value = "/circulationDeskDetailController")
public class OleCirculationDeskDetailController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(OleCirculationDeskDetailController.class);
    private OleCirculationDeskDetailServiceImpl oleCirculationDeskDetailService;

    private OleCirculationDeskDetailServiceImpl getCirculationDeskDetailService() {
        if (null == oleCirculationDeskDetailService) {
            oleCirculationDeskDetailService = GlobalResourceLoader.getService("oleCirculationDeskDetailService");
        }
        return oleCirculationDeskDetailService;
    }

    /**
     * This method creates new OleCirculationDeskDetailForm
     *
     * @param request
     * @return OleCirculationDeskDetailForm
     */
    @Override
    protected OleCirculationDeskDetailForm createInitialForm(HttpServletRequest request) {
        return new OleCirculationDeskDetailForm();
    }

    /**
     * This method is for setting the form to the screen
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OleCirculationDeskDetailForm oleCirculationDeskDetailForm = (OleCirculationDeskDetailForm) form;

        PermissionService service = KimApiServiceLocator.getPermissionService();
        boolean isAuthorized = service.hasPermission(GlobalVariables.getUserSession().getPrincipalId(), "OLE-PTRN", "Mapping Circulation Desk");
        if (!isAuthorized) {
            throw new DocumentAuthorizationException(GlobalVariables.getUserSession().getPrincipalId(), "not Authorized to map ", "CirculationDesk");
            //oleCirculationDeskDetailForm.setErrorAuthrisedUserMessage(OLEConstants.OleCirculationDeskDetail.OLE_CIRCULATION_UNAUTHORISED_USER_MSG);
            // return new OLEKRADAuthorizationResolver().resolveException(request,response,null,new Exception("is not authorized"));
        }
        oleCirculationDeskDetailForm.setMessage(null);
        oleCirculationDeskDetailForm.setOperatorId(null);

        oleCirculationDeskDetailForm.setOleCirculationDetailsCreateList(getCirculationDeskDetailService().populateCreateList());
        return getUIFModelAndView(oleCirculationDeskDetailForm, "OleCirculationDeskDetailViewPage");
    }

    /**
     * This method is for searching the circulation details
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OleCirculationDeskDetailForm oleCirculationDeskDetailForm = (OleCirculationDeskDetailForm) form;
        OleCirculationDeskDetailForm deskDetailForm = getCirculationDeskDetailService().performSearch(oleCirculationDeskDetailForm);
        deskDetailForm .setViewId("OleCirculationDeskDetailView");
        return getUIFModelAndView(deskDetailForm, "OleCirculationDeskDetailViewPage");
    }

    /**
     * This method is for saving the circulation desk details for the operator
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {


        OleCirculationDeskDetailForm oleCirculationDeskDetailForm = (OleCirculationDeskDetailForm) form;
        OleCirculationDeskDetailForm deskDetailForm = getCirculationDeskDetailService().performSave(oleCirculationDeskDetailForm);
        return getUIFModelAndView(deskDetailForm, "OleCirculationDeskDetailViewPage");
    }

    /**
     * This method is overridden for performing the search when the operator value is populated
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    @Override
    @RequestMapping(params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        form.setView(getViewService().getViewById("OleCirculationDeskDetailView"));
        super.refresh(form, result, request, response);
        return search(form, result, request, response);
    }
}
