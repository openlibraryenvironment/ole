package org.kuali.ole.deliver.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.api.OlePatronDefinition;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.form.OleMyAccountForm;
import org.kuali.ole.deliver.service.OleMyAccountProcess;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.ole.service.OlePatronService;
import org.kuali.ole.service.OlePatronServiceImpl;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The OleMyAccountController is the controller class for processing all the actions that corresponds to the renewal by patron  functionality in OLE.
 * The request mapping tag takes care of mapping the individual action to the corresponding functions.
 */
@Controller
@RequestMapping(value = "/myaccountcontroller")
public class OleMyAccountController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(OleMyAccountController.class);

    private BusinessObjectService boService;
    private OleMyAccountProcess oleMyAccountProcess;
    private OlePatronHelperService olePatronHelperService = new OlePatronHelperServiceImpl();

    /**
     * This method creates new oleMyAccountForm form
     *
     * @param request
     * @return oleMyAccountForm
     */
    @Override
    protected OleMyAccountForm createInitialForm(HttpServletRequest request) {
        return new OleMyAccountForm();
    }

    /**
     * This method converts UifFormBase to oleMyAccountForm
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
        OleMyAccountForm oleMyAccountForm = (OleMyAccountForm) form;
        return super.start(oleMyAccountForm, result, request, response);
    }

   /* *//**
     *  This method performs the my account login
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     *//*

    @RequestMapping(params = "methodToCall=logIn")
    public ModelAndView logIn(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OleMyAccountForm oleMyAccountForm = (OleMyAccountForm) form;
        String patrinId=oleMyAccountForm.getPatronId();
        String password=oleMyAccountForm.getPatronPassword();
        oleMyAccountForm.setViewId("RenewalItemView");
        oleMyAccountForm.setMethodToCall("start");
        oleMyAccountForm.setValidPatronFlag(true);
        return this.start(oleMyAccountForm, result, request, response);


    }*/


    /**
     * This method displays information about a patron in UI.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=searchPatron")
    public ModelAndView searchPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the searchPatron method");
        OleMyAccountForm oleMyAccountForm = (OleMyAccountForm) form;
        oleMyAccountForm.setExistingLoanList(new ArrayList<OleRenewalLoanDocument>());
        oleMyAccountForm.setInformation("");
        oleMyAccountForm.setMessage("");
        try {
            OlePatronDefinition olePatronDefinition = getOleMyAccountProcess().getPatronInfo(oleMyAccountForm.getPatronBarcode());
            oleMyAccountForm.setBorrowerType(olePatronDefinition.getOleBorrowerType().getBorrowerTypeName());
            oleMyAccountForm.setPatronName(olePatronDefinition.getEntity().getNames().get(0).getFirstName());
            oleMyAccountForm.setPatronId(olePatronDefinition.getOlePatronId());
            oleMyAccountForm.setBorrowerTypeId(olePatronDefinition.getOleBorrowerType().getBorrowerTypeId());
            oleMyAccountForm.setExistingLoanList(getOleMyAccountProcess().getPatronLoanedItems(olePatronDefinition.getOlePatronId()));
        } catch (Exception e) {
            oleMyAccountForm.setInformation(e.getMessage());
            LOG.error("Exception", e);
        }
        if (oleMyAccountForm.getExistingLoanList().size() == 0) {
            oleMyAccountForm.setInformation("No item currently checked out.");
            oleMyAccountForm.setPatronName("");
        }
        return getUIFModelAndView(oleMyAccountForm, "RenewalItemViewPage");
    }

    /**
     * This method search Patron document and display in the screen.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=myAccountPatronSearch")
    public ModelAndView myAccountPatronSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside myAccountPatronSearch");
        OleMyAccountForm oleMyAccountForm = (OleMyAccountForm) form;
        oleMyAccountForm.setExistingLoanList(new ArrayList<OleRenewalLoanDocument>());
        oleMyAccountForm.setInformation("");
        try {
            OlePatronDefinition olePatronDefinition = getOleMyAccountProcess().getPatronInfo(oleMyAccountForm.getPatronBarcode());
            if (olePatronDefinition == null) {
                oleMyAccountForm.setMessage("");
            }
            OlePatronDocument olePatronDocument = new OlePatronDocument();
            olePatronDocument = olePatronDocument.from(olePatronDefinition);
            List<EntityAddressBo> entityAddressBos = olePatronDocument.getAddresses();
            List<OleEntityAddressBo> oleEntityAddressBos = new ArrayList<OleEntityAddressBo>();
            OleEntityAddressBo oleEntityAddressBo = new OleEntityAddressBo();
            Map<String, String> criteriaMap;
            if (entityAddressBos.size() > 0) {
                for (EntityAddressBo entityAddressBo : entityAddressBos) {
                    oleEntityAddressBo = new OleEntityAddressBo();
                    criteriaMap = new HashMap<String, String>();
                    criteriaMap.put(OLEConstants.OlePatron.ENTITY_ADDRESS_ID, entityAddressBo.getId());
                    List<OleAddressBo> oleAddressBos = (List<OleAddressBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleAddressBo.class, criteriaMap);
                    if (oleAddressBos.size() > 0) {
                        oleEntityAddressBo.setOleAddressBo(oleAddressBos.get(0));
                    }
                    oleEntityAddressBo.setEntityAddressBo(entityAddressBo);
                    oleEntityAddressBos.add(oleEntityAddressBo);
                }
            }
            olePatronDocument.setOleEntityAddressBo(oleEntityAddressBos);
            String patronId = "";
            List<OleProxyPatronDocument> oleProxyPatronDocuments = olePatronDocument.getOleProxyPatronDocuments();
            if (oleProxyPatronDocuments.size() > 0) {
                for (OleProxyPatronDocument oleProxyPatronDocument : oleProxyPatronDocuments) {
                    patronId = oleProxyPatronDocument.getProxyPatronId();
                    OlePatronDefinition patronDefinition = getOleMyAccountProcess().getPatronInfo(patronId);
                    oleProxyPatronDocument.setProxyPatronFirstName(patronDefinition.getName().getFirstName());
                    oleProxyPatronDocument.setProxyPatronLastName(patronDefinition.getName().getLastName());
                    oleProxyPatronDocument.setProxyPatronBarcode(patronDefinition.getBarcode());

                }
            }
            oleMyAccountForm.setOlePatronDocument(olePatronDocument);
            oleMyAccountForm.setBarcode(olePatronDocument.getBarcode());
            //setEmails(oleMyAccountForm,olePatronDocument);
            searchPatron(oleMyAccountForm, result, request, response);
        } catch (Exception e) {
            oleMyAccountForm.setInformation(e.getMessage());
            LOG.error("Exception", e);
        }

        return getUIFModelAndView(oleMyAccountForm, "RenewalItemViewPage");
    }

    /**
     * This method performs the renewal item
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=renewalItem")
    public ModelAndView renewalItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside renewalItem method");
        OleMyAccountForm oleMyAccountForm = (OleMyAccountForm) form;
        oleMyAccountForm.setInformation("");
        String renewalItemId = oleMyAccountForm.getItem();
        OleRenewalLoanDocument oleRenewalLoanDocument = null;
        boolean renewalFlag = false;
        for (int i = 0; i < oleMyAccountForm.getExistingLoanList().size(); i++) {
            oleRenewalLoanDocument = oleMyAccountForm.getExistingLoanList().get(i);
            if (oleRenewalLoanDocument.getItemBarcode().equals(renewalItemId)) {
                renewalFlag = true;
                break;
            }
        }
        if (renewalFlag) {
            List<OleRenewalLoanDocument> oleRenewalLoanDocumentList = new ArrayList<OleRenewalLoanDocument>();
            oleRenewalLoanDocumentList.add(oleRenewalLoanDocument);
            oleRenewalLoanDocumentList = getOleMyAccountProcess().performRenewalItem(oleRenewalLoanDocumentList);
            oleRenewalLoanDocument = oleRenewalLoanDocumentList.get(0);
            oleMyAccountForm.setInformation(oleRenewalLoanDocument.getMessageInfo());

        } else
            oleMyAccountForm.setInformation("Select an item(s).");

        oleMyAccountForm.setExistingLoanList(getOleMyAccountProcess().getPatronLoanedItems(oleMyAccountForm.getPatronId()));
        return getUIFModelAndView(oleMyAccountForm, "RenewalItemViewPage");
    }

    /**
     * This method performs the selected  renewal items operation
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=renewalItems")
    public ModelAndView renewalItems(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside renewalItems method");
        OleMyAccountForm oleMyAccountForm = (OleMyAccountForm) form;
        oleMyAccountForm.setInformation("");
        boolean renewalFlag = false;
        List<OleRenewalLoanDocument> oleRenewalLoanDocumentList = new ArrayList<OleRenewalLoanDocument>();
        for (int i = 0; i < oleMyAccountForm.getExistingLoanList().size(); i++) {
            OleRenewalLoanDocument oleRenewalLoanDocument = oleMyAccountForm.getExistingLoanList().get(i);
            if (oleRenewalLoanDocument.isItemCheckFlag()) {
                renewalFlag = true;
                oleRenewalLoanDocumentList.add(oleRenewalLoanDocument);
            }
        }
        if (renewalFlag) {
            oleRenewalLoanDocumentList = getOleMyAccountProcess().performRenewalItem(oleRenewalLoanDocumentList);
            for (int i = 0; i < oleRenewalLoanDocumentList.size(); i++) {
                String errMsg = oleRenewalLoanDocumentList.get(i).getMessageInfo();
                oleMyAccountForm.setInformation(oleMyAccountForm.getInformation() == null ? "" : oleMyAccountForm.getInformation() + "\n" + (i + 1) + ". " + errMsg + "  (" + oleRenewalLoanDocumentList.get(i).getItemBarcode() + ")<br/>");

            }

        } else
            oleMyAccountForm.setInformation("Select an item(s).");
        oleMyAccountForm.setExistingLoanList(getOleMyAccountProcess().getPatronLoanedItems(oleMyAccountForm.getPatronId()));

        return getUIFModelAndView(oleMyAccountForm, "RenewalItemViewPage");
    }

    /**
     * This method clear UI for next borrower session..
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=saveAndClear")
    public ModelAndView clearPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside clearPatron method");
        OleMyAccountForm oleLoanForm = (OleMyAccountForm) form;
        oleLoanForm.setInformation("");
        oleLoanForm.setPatronBarcode(null);
        oleLoanForm.setPatronName(null);
        oleLoanForm.setExistingLoanList(null);

        return getUIFModelAndView(oleLoanForm, "RenewalItemViewPage");
    }


    public OleMyAccountProcess getOleMyAccountProcess() {

        if (oleMyAccountProcess == null)
            oleMyAccountProcess = new OleMyAccountProcess();
        return oleMyAccountProcess;
    }

    /**
     * This method will save or update the patron.document from the myaccount screen.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=savePatron")
    public ModelAndView savePatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Inside savePatron method");
        OleMyAccountForm oleMyAccountForm = (OleMyAccountForm) form;
        OlePatronService olePatronService = new OlePatronServiceImpl();
        OlePatronDocument olePatronDocument = oleMyAccountForm.getOlePatronDocument();
        EntityBo entity = olePatronDocument.getEntity();
        List<EntityAddressBo> entityAddressBos = new ArrayList<EntityAddressBo>();
        EntityAddressBo entityAddressBo = new EntityAddressBo();
        List<OleAddressBo> oleAddressBos = new ArrayList<OleAddressBo>();
        OleAddressBo oleAddressBo = new OleAddressBo();
        List<OleEntityAddressBo> oleEntityAddressBos = olePatronDocument.getOleEntityAddressBo();
        if (oleEntityAddressBos.size() > 0) {
            for (OleEntityAddressBo oleEntityAddressBo : oleEntityAddressBos) {
                oleAddressBo = oleEntityAddressBo.getOleAddressBo();
                oleAddressBos.add(oleAddressBo);
                entityAddressBo = oleEntityAddressBo.getEntityAddressBo();
                entityAddressBos.add(entityAddressBo);
            }
            olePatronDocument.setOleAddresses(oleAddressBos);
            olePatronDocument.setAddresses(entityAddressBos);
        }
        List<EntityPhoneBo> entityPhoneBos = new ArrayList<>();
        EntityPhoneBo entityPhoneBo = new EntityPhoneBo();
        List<OlePhoneBo> olePhoneBos = new ArrayList<>();
        OlePhoneBo olePhoneBo = new OlePhoneBo();
        List<OleEntityPhoneBo> oleEntityPhoneBos = olePatronDocument.getOleEntityPhoneBo();
        if(CollectionUtils.isNotEmpty(oleEntityPhoneBos)) {
            for(OleEntityPhoneBo oleEntityPhoneBo : oleEntityPhoneBos) {
                olePhoneBo = oleEntityPhoneBo.getOlePhoneBo();
                olePhoneBos.add(olePhoneBo);
                entityPhoneBo = oleEntityPhoneBo.getEntityPhoneBo();
                entityPhoneBos.add(entityPhoneBo);
            }
            olePatronDocument.setOlePhones(olePhoneBos);
            olePatronDocument.setPhones(entityPhoneBos);
        }
        List<EntityEmailBo> entityEmailBos = new ArrayList<>();
        EntityEmailBo entityEmailBo = new EntityEmailBo();
        List<OleEmailBo> oleEmailBos = new ArrayList<>();
        OleEmailBo oleEmailBo = new OleEmailBo();
        List<OleEntityEmailBo> oleEntityEmailBos = olePatronDocument.getOleEntityEmailBo();
        if(CollectionUtils.isNotEmpty(oleEntityEmailBos)) {
            for(OleEntityEmailBo oleEntityEmailBo : oleEntityEmailBos) {
                oleEmailBo = oleEntityEmailBo.getOleEmailBo();
                oleEmailBos.add(oleEmailBo);
                entityEmailBo = oleEntityEmailBo.getEntityEmailBo();
                entityEmailBos.add(entityEmailBo);
            }
            olePatronDocument.setOleEmails(oleEmailBos);
            olePatronDocument.setEmails(entityEmailBos);
        }
        boolean addressSource = olePatronHelperService.checkAddressSource(olePatronDocument.getOleAddresses());
        boolean phoneSource = olePatronHelperService.checkPhoneSource(olePatronDocument.getOlePhones());
        boolean emailSource = olePatronHelperService.checkEmailSource(olePatronDocument.getOleEmails());
        boolean emailDefault = olePatronHelperService.checkEmailMultipleDefault(oleEntityEmailBos);
        boolean phoneDefault = olePatronHelperService.checkPhoneMultipleDefault(oleEntityPhoneBos);
        boolean addressDefault = olePatronHelperService.checkAddressMultipleDefault(oleEntityAddressBos);
        if (addressSource && phoneSource && emailSource) {
            if (emailDefault && phoneDefault && addressDefault) {
                OlePatronDefinition olePatronDefinition = olePatronService.updatePatron(OlePatronDocument.to(oleMyAccountForm.getOlePatronDocument()));
                cancel(oleMyAccountForm, result, request, response);
            } else {
                oleMyAccountForm.setMessage(OLEConstants.OlePatron.ERROR_DEFAULT_MESSAGE);
                return getUIFModelAndView(oleMyAccountForm);
            }
        } else {
            if(phoneSource) {
                oleMyAccountForm.setMessage(OLEConstants.OlePatron.ERROR_PHONE_SOURCE_REQUIRED);
                return getUIFModelAndView(oleMyAccountForm);
            } else if(emailSource){
                oleMyAccountForm.setMessage(OLEConstants.OlePatron.ERROR_EMAIL_SOURCE_REQUIRED);
                return getUIFModelAndView(oleMyAccountForm);
            } else {
                oleMyAccountForm.setMessage(OLEConstants.OlePatron.ERROR_ADDRESS_SOURCE_REQUIRED);
                return getUIFModelAndView(oleMyAccountForm);
            }
        }
        oleMyAccountForm.setMessage(OLEConstants.OlePatron.SAVE_SUCCESSFUL_MSG);
        //updatePatron(olePatronForm,olePatronDefinition);
        //setProxyPatrons(olePatronForm, olePatronDefinition);
        return getUIFModelAndView(oleMyAccountForm);
    }

    /**
     * Just returns as if return with no value was selected.
     */
    @Override
    @RequestMapping(params = "methodToCall=cancel")
    public ModelAndView cancel(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Inside the cancel method");
        OleMyAccountForm oleMyAccountForm = (OleMyAccountForm) form;
        oleMyAccountForm.setBarcode(null);
        //oleMyAccountForm.setMessage();
        return getUIFModelAndView(oleMyAccountForm);
    }

    /**
     * Just returns as if return with no value was selected.
     */
    @Override
    @RequestMapping(params = "methodToCall=back")
    public ModelAndView back(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        return super.back(form, result, request, response);
    }

}
