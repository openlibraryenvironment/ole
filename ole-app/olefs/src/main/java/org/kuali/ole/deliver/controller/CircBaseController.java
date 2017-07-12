package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.controller.checkout.CheckoutUIController;
import org.kuali.ole.deliver.form.CheckinForm;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.keyvalue.CirculationDeskChangeKeyValue;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.exception.DocumentAuthorizationException;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 * Created by hemalathas on 6/21/15.
 */
public class CircBaseController extends OLEUifControllerBase{

    private static final Logger LOG = Logger.getLogger(CircBaseController.class);

    private CircDeskLocationResolver circDeskLocationResolver;
    private OlePatronHelperService olePatronHelperService;
    public volatile static String fastAddBarcode = "";
    //TODO: Clean up the map once session timeout is implemented.
    private Map<String, CheckoutUIController> controllerMap = new HashMap<>();

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
                /* OLE-8186 : Modified for fixing browser backward and forward button functionality.*/
                if(circForm.getView() == null) {
                    circForm = (CircForm) form;
                }
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
        String maxTimeoutCount = ParameterValueResolver.getInstance().getParameter(OLEConstants
                .APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.MAX_TIME_LOAN);
        circForm.setMaxSessionTime(maxTimeoutCount);
        String url = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
        circForm.setUrlBase(url);
        circForm.setViewBillUrl(OLEConstants.OlePatron.PATRON_VIEW_BILL_URL);
        circForm.setCreateBillUrl(OLEConstants.OlePatron.PATRON_CREATE_BILL_URL);
        return super.start(circForm, result, request, response);
    }


    @RequestMapping(params = "methodToCall=goToReturn")
    public ModelAndView goToReturn(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response){

        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
        String url = baseUrl + "/portal.do?channelTitle=Return&channelUrl=" + baseUrl + "/ole-kr-krad/checkincontroller?viewId=checkinView&methodToCall=start";
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        return performRedirect(form, url,props);
    }


    @RequestMapping(params = "methodToCall=resetUI")
    public ModelAndView resetUI(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        circForm.reset();
        return getUIFModelAndView(form, "circViewPage");
    }

    @RequestMapping(params = "methodToCall=resetItemForNextTrans")
    public ModelAndView resetItemForNextTrans(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        resetItemInfoForNextTrans(circForm);
        circForm.setErrorMessage(new ErrorMessage());
        return getUIFModelAndView(form, "circViewPage");
    }

    @RequestMapping(params = "methodToCall=resetUIForAutoCheckout")
    public ModelAndView resetUIForAutoCheckout(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        circForm.resetForAutoCheckout();
        return getUIFModelAndView(form, "circViewPage");
    }

    @RequestMapping(params = "methodToCall=refreshScreen")
    public ModelAndView refreshScreen(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.refresh(form, result, request, response);
    }

    @RequestMapping(params = "methodToCall=clearUI")
    public ModelAndView clearUI(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        circForm.resetAll();
        circForm.setLightboxScript("jq('#barcodeFieldSection_control').focus();");
        return getUIFModelAndView(form, "circViewPage");
    }

    protected void resetItemInfoForNextTrans(CircForm circForm) {
        circForm.setItemBarcode("");
        circForm.setItemRecord(null);
        circForm.setDroolsExchange(null);
        circForm.setItemValidationDone(false);
        circForm.setCustomDueDateMap(null);
        circForm.setCustomDueDateTime(null);
        circForm.setMissingPieceMatchCheck("");
        circForm.setMissingPieces("");
        circForm.setMismatchedMissingPieceNote("");
        circForm.setRecordNoteForMissingPiece(false);
        circForm.setRecordNoteForDamagedItem(false);
        circForm.setRecordNoteForClaimsReturn(false);
        circForm.setItemFoundInLibrary(false);
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

    public void showHoldErrorMessageDialog(UifFormBase form, HttpServletRequest request, HttpServletResponse response) {
        showDialog("holdValidationErrorMessageDialog", form, request, response);
    }

    @RequestMapping(params = "methodToCall=clearSession")
    public ModelAndView clearSession(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        circForm.resetAll();
        controllerMap.remove(circForm.getFormKey());
        return getUIFModelAndView(circForm);
    }

    @RequestMapping(params = "methodToCall=redirectHomePage")
    public ModelAndView redirect(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {
        CircForm circForm = (CircForm) form;
        List<OleLoanDocument> oleLoanDocumentList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(circForm.getLoanDocumentListForCurrentSession())) {
            oleLoanDocumentList.addAll(circForm.getLoanDocumentListForCurrentSession());
        }
        if (CollectionUtils.isNotEmpty(circForm.getLoanDocumentsForRenew())) {
            oleLoanDocumentList.addAll(circForm.getLoanDocumentsForRenew());
        }
        if(CollectionUtils.isNotEmpty(oleLoanDocumentList)) {
            try {
                getOlePatronHelperService().sendMailToPatron(oleLoanDocumentList);
            } catch (Exception e) {
                LOG.error("Error while sending sendout Receipt"+e.getLocalizedMessage());
            }
        }
        String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
        String url = baseUrl + "/portal.do";
        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        return performRedirect(form, url, props);
    }

    public CheckoutUIController getCheckoutUIController(String formKey) {
        if (controllerMap.containsKey(formKey)) {
            return controllerMap.get(formKey);
        } else {
            CheckoutUIController checkoutUIController = new CheckoutUIController();
            controllerMap.put(formKey, checkoutUIController);
        }
        return controllerMap.get(formKey);
    }

    public OlePatronHelperService getOlePatronHelperService(){
        if(olePatronHelperService==null)
            olePatronHelperService=new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }
}

