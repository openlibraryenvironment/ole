package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.form.OlePatronMergeForm;
import org.kuali.ole.ingest.FileUtil;
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
@RequestMapping(value = "/patronMergeController")
public class OlePatronMergeController extends UifControllerBase {

    private static final Logger LOG = Logger.getLogger(OlePatronMergeController.class);
    private BusinessObjectService businessObjectService;

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OlePatronMergeForm();
    }

    @Override
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        return super.start(form, result, request, response);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @RequestMapping(params = "methodToCall=searchPatronMerge")
    public ModelAndView searchPatron(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("inside searchPatronMerge method");
        OlePatronMergeForm patronMergeForm = (OlePatronMergeForm) form;
        List<OlePatronMergeDocument> patronMergeDocumentList = new ArrayList<OlePatronMergeDocument>();
        String firstName = patronMergeForm.getFirstName();
        String lastName = patronMergeForm.getLastName();
        String patronType = patronMergeForm.getPatronType();
        Map barMap = new HashMap();
        List<OlePatronDocument> olePatronDocuments = new ArrayList<OlePatronDocument>();
        boolean checkName = false;
        boolean checkPatronType = false;
        if (!firstName.isEmpty()) {
            barMap.put(OLEConstants.OlePatron.PATRON_FIRST_NAME, firstName);
        }
        if (!lastName.isEmpty()) {
            barMap.put(OLEConstants.OlePatron.PATRON_LAST_NAME, lastName);
        }
        if (!firstName.isEmpty() || !lastName.isEmpty()) {
            List<EntityNameBo> entityNameBoList = (List<EntityNameBo>) getLookupService().findCollectionBySearchHelper(EntityNameBo.class, barMap, true);
            if (entityNameBoList != null && entityNameBoList.size() > 0) {
                checkName = true;
            }
            List<EntityNameBo> entityNameBos = (List<EntityNameBo>) getLookupService().findCollectionBySearchHelper(EntityNameBo.class, barMap, true);
            if (entityNameBos.size() > 0) {
                checkPatronType = true;
            }
            entityNameBos = entityNameBos != null ? entityNameBos : new ArrayList<EntityNameBo>();
            for (EntityNameBo entityNameBo : entityNameBos) {
                barMap = new HashMap();
                barMap.put(OLEConstants.OlePatron.PATRON_ID, entityNameBo.getEntityId());
                OlePatronDocument olePatronDocument = getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, barMap);
                if (olePatronDocument != null) {
                    olePatronDocuments.add(olePatronDocument);
                }
            }
        }
        List<OlePatronDocument> newOlePatronDocuments = new ArrayList<OlePatronDocument>();
        if (!patronType.isEmpty() && patronType != null && olePatronDocuments.size() > 0) {
            for (OlePatronDocument olePatronDocument : olePatronDocuments) {
                if (olePatronDocument != null) {
                    if (olePatronDocument.getBorrowerTypeName().equalsIgnoreCase(patronType)) {
                        newOlePatronDocuments.add(olePatronDocument);
                    }
                }
            }
            olePatronDocuments = newOlePatronDocuments;
        } else if (!patronType.isEmpty()) {
            Map barTypeMap = new HashMap();
            barTypeMap.put(OLEConstants.BORROWER_TYPE_NAME, patronType);
            List<OleBorrowerType> oleBorrowerTypeList = (List<OleBorrowerType>) getLookupService().findCollectionBySearchHelper(OleBorrowerType.class, barTypeMap, true);
            if (oleBorrowerTypeList != null && oleBorrowerTypeList.size() > 0) {
                barTypeMap.put(OLEConstants.BORROWER_TYPE_NAME, oleBorrowerTypeList.get(0).getBorrowerTypeName());
            }
            if (barTypeMap.size() == 0) {
                barTypeMap.put(OLEConstants.BORROWER_TYPE_NAME, "");
            }
            List<OleBorrowerType> oleBorrowerTypes = (List<OleBorrowerType>) getBusinessObjectService().findMatching(OleBorrowerType.class, barTypeMap);
            if (oleBorrowerTypes != null && oleBorrowerTypes.size() > 0) {
                barMap.put(OLEConstants.BORROWER_TYPE, oleBorrowerTypes.get(0).getBorrowerTypeId());
            } else {
                barMap.put(OLEConstants.BORROWER_TYPE, "");
            }
            barMap.remove(OLEConstants.OlePatron.PATRON_FIRST_NAME);
            barMap.remove(OLEConstants.OlePatron.PATRON_LAST_NAME);
            if ((!checkName && !firstName.isEmpty()) || (!checkName && !lastName.isEmpty()) || !checkPatronType) {
                if (firstName.isEmpty() && lastName.isEmpty()) {
                    olePatronDocuments = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, barMap);
                } else {
                    olePatronDocuments = new ArrayList<OlePatronDocument>();
                }
            }
        } else if (firstName.isEmpty() && lastName.isEmpty() && patronType.isEmpty()) {
            olePatronDocuments = (List<OlePatronDocument>) getBusinessObjectService().findAll(OlePatronDocument.class);
        }
        olePatronDocuments = olePatronDocuments != null ? olePatronDocuments : new ArrayList<OlePatronDocument>();
        for (OlePatronDocument olePatronDocument : olePatronDocuments) {
            if (olePatronDocument != null) {
                if (olePatronDocument.isActiveIndicator()) {
                    patronMergeDocumentList.add(new OlePatronMergeDocument(olePatronDocument));
                }
            }
        }
        patronMergeForm.setPatronList(patronMergeDocumentList);
        return getUIFModelAndView(patronMergeForm, OLEConstants.OlePatron.PATRON_MERGE_VIEW_PAGE);
    }

    /**
     * Gets the businessObjectService attribute.
     *
     * @return Returns the businessObjectService
     */
    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    @RequestMapping(params = "methodToCall=saveMerge")
    public ModelAndView saveMerge(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("inside saveMerge method");
        OlePatronMergeForm patronMergeForm = (OlePatronMergeForm) form;
        URL resource = getClass().getResource(OLEConstants.OlePatron.PATRON_MERGE_CONFIG_XML_FILE);
        File file = new File(resource.toURI());
        String patronConfigXML = new FileUtil().readFile(file);
        OlePatronConfigObjectGeneratorFromXML patronConfigObjectGeneratorFromXML = new OlePatronConfigObjectGeneratorFromXML();
        OlePatronConfig patronConfig = patronConfigObjectGeneratorFromXML.buildKrmsFromFileContent(patronConfigXML);
        List<OleConfigDocument> configDocuments = patronConfig.getDocuments();
        Map<String, Object> patronConfigDocuments = OLEConstants.OlePatron.getPatronConfigObject();
        List<OlePatronMergeDocument> patronMergeDocumentList = patronMergeForm.getPatronList();
        List<OlePatronMergeDocument> newPatronMergeDocumentList = new ArrayList<OlePatronMergeDocument>();
        Map barMap = new HashMap();
        List<OleLoanDocument> duplicatePatronLoanDocuments = null;
        List<OleDeliverRequestBo> duplicatePatronRequests = null;
        List<OleTemporaryCirculationHistory> duplicateTemporaryCirculationRecords = null;
        List<PatronBillPayment> duplicatePatronBillPayments = null;
        OlePatronDocument survivorPatronDocument = null;
        int survivor = 0;
        for (OlePatronMergeDocument patronMergeDocument : patronMergeDocumentList) {
            if (patronMergeDocument.isCheckSurvivor()) {
                survivorPatronDocument = patronMergeDocument.getOlePatronDocument();
                survivor++;
            }
            if (patronMergeDocument.isCheckSurvivor() && patronMergeDocument.isCheckDuplicatePatron()) {
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.PATRON_MERGE_DUPLICATE_PATRON_SELECT, OLEConstants.OlePatron.PATRON_MERGE_DUPLICATE_PATRON_SELECT);
                return getUIFModelAndView(patronMergeForm, OLEConstants.OlePatron.PATRON_MERGE_VIEW_PAGE);
            }
        }
        if (survivor > 1 || survivor < 1) {
            GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.PATRON_MERGE_SURVIVOR_SELECT, OLEConstants.OlePatron.PATRON_MERGE_SURVIVOR_SELECT);
            return getUIFModelAndView(patronMergeForm, OLEConstants.OlePatron.PATRON_MERGE_VIEW_PAGE);
        }
        for (OlePatronMergeDocument patronMergeDocument : patronMergeDocumentList) {
            if (patronMergeDocument.isCheckDuplicatePatron()) {
                OlePatronDocument olePatronDocument = patronMergeDocument.getOlePatronDocument();
                for (OleConfigDocument configDocument : configDocuments) {
                    Object object = patronConfigDocuments.get(configDocument.getDocumentName());
                    if (object != null && object.equals(OleLoanDocument.class)) {
                        barMap.put(configDocument.getAttribute(), olePatronDocument.getOlePatronId());
                        duplicatePatronLoanDocuments = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, barMap);
                        if (duplicatePatronLoanDocuments.size() > 0) {
                            for (OleLoanDocument oleLoanDocument : duplicatePatronLoanDocuments) {
                                oleLoanDocument.setPatronId(survivorPatronDocument.getOlePatronId());
                                oleLoanDocument.setOlePatron(survivorPatronDocument);
                                getBusinessObjectService().save(oleLoanDocument);
                            }
                        }
                        barMap.remove(configDocument.getAttribute());
                    }
                    if (object != null && object.equals(OleDeliverRequestBo.class)) {
                        barMap.put(configDocument.getAttribute(), olePatronDocument.getOlePatronId());
                        duplicatePatronRequests = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, barMap);
                        if (duplicatePatronRequests.size() > 0) {
                            for (OleDeliverRequestBo oleDeliverRequestBo : duplicatePatronRequests) {
                                oleDeliverRequestBo.setBorrowerId(survivorPatronDocument.getOlePatronId());
                                oleDeliverRequestBo.setOlePatron(survivorPatronDocument);
                                getBusinessObjectService().save(oleDeliverRequestBo);
                            }
                        }
                        barMap.remove(configDocument.getAttribute());
                    }
                    if (object != null && object.equals(OleTemporaryCirculationHistory.class)) {
                        barMap.put(configDocument.getAttribute(), olePatronDocument.getOlePatronId());
                        duplicateTemporaryCirculationRecords = (List<OleTemporaryCirculationHistory>) getBusinessObjectService().findMatching(OleTemporaryCirculationHistory.class, barMap);
                        if (duplicateTemporaryCirculationRecords.size() > 0) {
                            for (OleTemporaryCirculationHistory oleTemporaryCirculationHistory : duplicateTemporaryCirculationRecords) {
                                oleTemporaryCirculationHistory.setOlePatronId(survivorPatronDocument.getOlePatronId());
                                getBusinessObjectService().save(oleTemporaryCirculationHistory);
                            }
                        }
                        barMap.remove(configDocument.getAttribute());
                    }
                    if (object != null && object.equals(PatronBillPayment.class)) {
                        barMap.put(configDocument.getAttribute(), olePatronDocument.getOlePatronId());
                        duplicatePatronBillPayments = (List<PatronBillPayment>) getBusinessObjectService().findMatching(PatronBillPayment.class, barMap);
                        if (duplicatePatronBillPayments.size() > 0) {
                            for (PatronBillPayment patronBillPayment : duplicatePatronBillPayments) {
                                patronBillPayment.setPatronId(survivorPatronDocument.getOlePatronId());
                                getBusinessObjectService().save(patronBillPayment);
                            }
                        }
                        barMap.remove(configDocument.getAttribute());
                    }
                }
                olePatronDocument.setActiveIndicator(false);
                getBusinessObjectService().linkAndSave(olePatronDocument);

            } else {
                newPatronMergeDocumentList.add(patronMergeDocument);
            }
        }
        patronMergeForm.setPatronList(newPatronMergeDocumentList);
        return getUIFModelAndView(patronMergeForm, OLEConstants.OlePatron.PATRON_MERGE_VIEW_PAGE);
    }


    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }
}