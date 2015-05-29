package org.kuali.ole.select.controller;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.controller.OleTransactionalDocumentControllerBase;
import org.kuali.ole.module.purap.PurapKeyConstants;
import org.kuali.ole.select.bo.*;
import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.ole.select.form.OLEPlatformRecordForm;
import org.kuali.ole.select.gokb.OleGokbOrganization;
import org.kuali.ole.select.gokb.OleGokbPlatform;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.ole.service.OLEPlatformService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.ole.vnd.businessobject.VendorEventLog;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by chenchulakshmig on 9/10/14.
 * The OLEPlatformRecordController is the controller class for processing all the actions that corresponds to the Platform functionality in OLE.
 * The request mapping tag takes care of mapping the individual action to the corresponding functions.
 */
@Controller
@RequestMapping(value = "/platformRecordController")
public class OLEPlatformRecordController extends OleTransactionalDocumentControllerBase {

    private BusinessObjectService businessObjectService;
    private OLEPlatformService olePlatformService;
    private OLEEResourceSearchService oleEResourceSearchService = null;

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public OLEPlatformService getOlePlatformService() {
        if (olePlatformService == null) {
            olePlatformService = GlobalResourceLoader.getService(OLEConstants.PLATFORM_SERVICE);
        }
        return olePlatformService;
    }

    public OLEEResourceSearchService getOleEResourceSearchService() {
        if (oleEResourceSearchService == null) {
            oleEResourceSearchService = GlobalResourceLoader.getService(OLEConstants.OLEEResourceRecord.ERESOURSE_SEARCH_SERVICE);
        }
        return oleEResourceSearchService;
    }

    /**
     * This method creates new olePlatformRecordForm
     *
     * @return olePlatformRecordForm
     */
    @Override
    protected DocumentFormBase createInitialForm(HttpServletRequest request) {
        OLEPlatformRecordForm olePlatformRecordForm = new OLEPlatformRecordForm();
        return olePlatformRecordForm;
    }

    /**
     * This method is for saving the platform details
     *
     * @param form
     * @return ModelAndView
     */
    @Override
    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) form;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        olePlatformRecordForm.setTempId(null);
        StringBuffer errorMessage = new StringBuffer();
        if (StringUtils.isNotBlank(olePlatformRecordDocument.getName())) {
            if (olePlatformRecordDocument.getName().length() < 40) {
                olePlatformRecordDocument.getDocumentHeader().setDocumentDescription(olePlatformRecordDocument.getName());
            } else {
                String documentDescription = olePlatformRecordDocument.getName().substring(0, 39);
                olePlatformRecordDocument.getDocumentHeader().setDocumentDescription(documentDescription);
            }
        }
        if (!olePlatformRecordDocument.isActive() && olePlatformRecordDocument.getLinkedEResources().size() > 0) {
            olePlatformRecordDocument.setActive(true);
            errorMessage.append(olePlatformRecordDocument.getLinkedEResources().size() + " " + SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(PurapKeyConstants.ERROR_ERESOURCE_LINKED_TO_PLATFORM));
            errorMessage.append(OLEConstants.BREAK);
        }
        if (olePlatformRecordDocument.getGokbId()!=null) {
            Map gokbMap = new HashMap();
            gokbMap.put(OLEConstants.GOKB_ID, olePlatformRecordDocument.getGokbId());
            List<OLEPlatformRecordDocument> tempDocuments = (List<OLEPlatformRecordDocument>) getBusinessObjectService().findMatching(OLEPlatformRecordDocument.class, gokbMap);
            if (tempDocuments != null && tempDocuments.size() > 0) {
                for (OLEPlatformRecordDocument tempDocument : tempDocuments) {
                    if (StringUtils.isBlank(olePlatformRecordDocument.getOlePlatformId()) || (StringUtils.isNotBlank(olePlatformRecordDocument.getOlePlatformId()) && !olePlatformRecordDocument.getOlePlatformId().equals(tempDocument.getOlePlatformId()))) {
                        errorMessage.append(" Platform record " + tempDocument.getOlePlatformId() + " " + SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(PurapKeyConstants.ERROR_PLATFORM_LINKED_TO_GOKB_ID));
                        errorMessage.append(OLEConstants.BREAK);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(olePlatformRecordDocument.getName())) {
            Map platformNameMap = new HashMap();
            platformNameMap.put(OLEConstants.PLATFORM_NAME, olePlatformRecordDocument.getName());
            List<OLEPlatformRecordDocument> tempDocuments = (List<OLEPlatformRecordDocument>) getBusinessObjectService().findMatching(OLEPlatformRecordDocument.class, platformNameMap);
            if (tempDocuments != null && tempDocuments.size() > 0) {
                for (OLEPlatformRecordDocument tempDocument : tempDocuments) {
                    if (StringUtils.isBlank(olePlatformRecordDocument.getOlePlatformId()) || (StringUtils.isNotBlank(olePlatformRecordDocument.getOlePlatformId()) && !olePlatformRecordDocument.getOlePlatformId().equals(tempDocument.getOlePlatformId()))) {
                        errorMessage.append(" Platform record " + tempDocument.getOlePlatformId() + " " + SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(PurapKeyConstants.ERROR_PLATFORM_SAME_NAME));
                        errorMessage.append(OLEConstants.BREAK);
                    }

                }
            }
        }
        if (StringUtils.isNotBlank(olePlatformRecordDocument.getPlatformProviderName())) {
            Map vendorMap = new HashMap();
            vendorMap.put(OLEConstants.VENDOR_NAME, olePlatformRecordDocument.getPlatformProviderName());
            List<VendorDetail> vendorDetails = (List<VendorDetail>) getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
            if (vendorDetails != null && vendorDetails.size() > 0) {
                olePlatformRecordDocument.setVendorId(vendorDetails.get(0).getVendorNumber());
            } else {
                errorMessage.append("Invalid Platform Provider " + olePlatformRecordDocument.getPlatformProviderName());
                errorMessage.append(OLEConstants.BREAK);
            }
        }
        if (StringUtils.isNotBlank(errorMessage)) {
            olePlatformRecordForm.setMessage(errorMessage.toString());
            olePlatformRecordDocument.setSaveValidationFlag(true);
            return getUIFModelAndView(olePlatformRecordForm);
        }

        getOlePlatformService().processEventAttachments(olePlatformRecordDocument.getEventLogs());

        if (StringUtils.isBlank(olePlatformRecordDocument.getStatusId())){
            olePlatformRecordDocument.setStatusId("1");
        }
        if (olePlatformRecordDocument.getGokbId()!=null && olePlatformRecordDocument.getPlatformProviderId()!=null){
            OleGokbOrganization oleGokbOrganization = getBusinessObjectService().findBySinglePrimaryKey(OleGokbOrganization.class, olePlatformRecordDocument.getPlatformProviderId());
            if (oleGokbOrganization != null && oleGokbOrganization.getGokbOrganizationId() != null) {
                Map vendorMap = new HashMap();
                vendorMap.put(OLEConstants.GOKB_ID, oleGokbOrganization.getGokbOrganizationId());
                List<VendorDetail> vendorDetails = (List<VendorDetail>) getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
                if (vendorDetails != null && vendorDetails.size() > 0) {
                    getOleEResourceSearchService().updateVendor(vendorDetails.get(0), oleGokbOrganization.getOrganizationName());
                    olePlatformRecordDocument.setVendorId(vendorDetails.get(0).getVendorNumber());
                } else {
                    getOleEResourceSearchService().createVendor(oleGokbOrganization.getOrganizationName(), oleGokbOrganization.getGokbOrganizationId(), oleGokbOrganization.getVariantName());
                    olePlatformRecordDocument.setPlatformProviderFlag(true);
                }
            }
            olePlatformRecordDocument.setPlatformProviderId(null);
        }

        if (StringUtils.isNotBlank(olePlatformRecordDocument.getOlePlatformId())) {
            getOlePlatformService().getNewPlatformDoc(olePlatformRecordDocument.getOlePlatformId());
        }
        if (olePlatformRecordDocument.getAdminUrls().size() > 0) {
            olePlatformRecordDocument.setAdminUrls(getOlePlatformService().saveUrls(olePlatformRecordDocument.getAdminUrls()));
        }
        if (olePlatformRecordDocument.getEventLogs().size() > 0) {
            olePlatformRecordDocument.setEventLogs(getOlePlatformService().saveEvents(olePlatformRecordDocument.getEventLogs()));
        }
        if (StringUtils.isNotBlank(olePlatformRecordDocument.getVendorId())) {
            String[] vendorId = olePlatformRecordDocument.getVendorId().split("-");
            if (vendorId != null && vendorId.length == 2) {
                olePlatformRecordDocument.setVendorHeaderGeneratedIdentifier(Integer.parseInt(vendorId[0]));
                olePlatformRecordDocument.setVendorDetailAssignedIdentifier(Integer.parseInt(vendorId[1]));
            }
        } else {
            olePlatformRecordDocument.setVendorHeaderGeneratedIdentifier(null);
            olePlatformRecordDocument.setVendorDetailAssignedIdentifier(null);
        }
        if (olePlatformRecordDocument.getVendorHeaderGeneratedIdentifier() != null && olePlatformRecordDocument.getVendorDetailAssignedIdentifier() != null) {
            Map vendorMap = new HashMap();
            vendorMap.put(OLEConstants.VENDOR_HEADER_GENERATED_ID, olePlatformRecordDocument.getVendorHeaderGeneratedIdentifier());
            vendorMap.put(OLEConstants.VENDOR_DETAILED_ASSIGNED_ID, olePlatformRecordDocument.getVendorDetailAssignedIdentifier());
            VendorDetail vendor = getBusinessObjectService().findByPrimaryKey(VendorDetail.class, vendorMap);
            if (vendor != null) {
                VendorEventLog vendorEventLog = new VendorEventLog();
                vendorEventLog.setLogTypeId("3");
                vendorEventLog.setDate(new Timestamp((new Date()).getTime()));
                vendorEventLog.setUserId(GlobalVariables.getUserSession().getPrincipalId());
                vendorEventLog.setNote("This vendor is associated with the platform '"+olePlatformRecordDocument.getName()+"'");
                vendor.getEventLogs().add(vendorEventLog);
            }
        }
        return super.save(olePlatformRecordForm, result, request, response);
    }

    /**
     * This method is to add general note
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addGeneralNote")
    public ModelAndView addGeneralNote(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        index++;
        List<OLEPlatformGeneralNote> olePlatformGeneralNotesList = olePlatformRecordDocument.getGeneralNotes();
        olePlatformRecordDocument.getGeneralNotes().add(index, new OLEPlatformGeneralNote());
        olePlatformRecordDocument.setGeneralNotes(olePlatformGeneralNotesList);
        olePlatformRecordForm.setDocument(olePlatformRecordDocument);
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    /**
     * This method is to delete general note
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=deleteGeneralNote")
    public ModelAndView deleteGeneralNote(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        List<OLEPlatformGeneralNote> olePlatformGeneralNotesList = olePlatformRecordDocument.getGeneralNotes();
        if (olePlatformGeneralNotesList.size() > 1) {
            olePlatformGeneralNotesList.remove(index);
        }
        olePlatformRecordForm.setDocument(olePlatformRecordDocument);
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    /**
     * This method is to add administrative url
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addAdministrativeUrl")
    public ModelAndView addAdministrativeUrl(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                             HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        olePlatformRecordDocument.getAdminUrls().add(new OLEPlatformAdminUrl());
        return super.navigate(uifForm, result, request, response);
    }

    /**
     * This method is to save admin url
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=saveAdminUrl")
    public ModelAndView saveAdminUrl(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        olePlatformRecordDocument.getAdminUrls().get(index).setSaveFlag(true);
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    /**
     * This method is to edit admin url
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=editAdminUrl")
    public ModelAndView editAdminUrl(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        olePlatformRecordDocument.getAdminUrls().get(index).setSaveFlag(false);
        return super.navigate(uifForm, result, request, response);
    }

    /**
     * This method is to delete admin url
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=deleteAdminUrl")
    public ModelAndView deleteAdminUrl(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if (olePlatformRecordDocument.getAdminUrls().size() > index) {
            olePlatformRecordDocument.getAdminUrls().remove(index);
        }
        return super.navigate(uifForm, result, request, response);
    }

    /**
     * This method is to add event
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addEvent")
    public ModelAndView addEvent(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                 HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        String selectedCollectionPath = olePlatformRecordForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = uifForm.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object eventObject = ObjectPropertyUtils.getPropertyValue(uifForm, addLinePath);
        OLEPlatformEventLog olePlatformEventLog = (OLEPlatformEventLog) eventObject;
        boolean flag = true;
        flag = getOlePlatformService().addAttachmentFile(olePlatformEventLog, "OLEPlatformEventLogTab-eventLogSection");

        if (StringUtils.isBlank(olePlatformEventLog.getEventNote())) {
            GlobalVariables.getMessageMap().putErrorForSectionId("OLEPlatformEventLogTab-eventLogSection", OLEConstants.ERROR_NOTE_REQUIRED);
            flag = false;
        }
        if (!flag){
            return super.navigate(olePlatformRecordForm, result, request, response);
        }
        if (olePlatformRecordForm.getTempId() == null) {
            olePlatformRecordForm.setTempId(0);
        } else {
            olePlatformRecordForm.setTempId(olePlatformRecordForm.getTempId() + 1);
        }
        olePlatformEventLog.setEventTempId(olePlatformRecordForm.getTempId());
        if (StringUtils.isNotBlank(olePlatformEventLog.getLogTypeName()) && olePlatformEventLog.getLogTypeName().equalsIgnoreCase("Event")) {
            olePlatformEventLog.setProblemTypeId(null);
            olePlatformEventLog.setEventStatus(null);
            olePlatformEventLog.setEventResolvedDate(null);
            olePlatformEventLog.setEventResolution(null);
        } else if (StringUtils.isNotBlank(olePlatformEventLog.getLogTypeName()) && olePlatformEventLog.getLogTypeName().equalsIgnoreCase("Problem")) {
            olePlatformEventLog.setEventTypeId(null);
        }
        olePlatformEventLog.setSaveFlag(true);
        return addLine(olePlatformRecordForm, result, request, response);
    }

    /**
     * This method is to add filter event
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addFilterEvent")
    public ModelAndView addFilterEvent(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        String selectedCollectionPath = olePlatformRecordForm.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = uifForm.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object eventObject = ObjectPropertyUtils.getPropertyValue(uifForm, addLinePath);
        OLEPlatformEventLog olePlatformEventLog = (OLEPlatformEventLog) eventObject;
        boolean flag = true;
        flag = getOlePlatformService().addAttachmentFile(olePlatformEventLog, "OLEPlatformEventLogTab-filterEventLogSection");

        if (StringUtils.isBlank(olePlatformEventLog.getEventNote())) {
            GlobalVariables.getMessageMap().putErrorForSectionId("OLEPlatformEventLogTab-filterEventLogSection", OLEConstants.ERROR_NOTE_REQUIRED);
            flag = false;
        }
        if (!flag){
            return super.navigate(olePlatformRecordForm, result, request, response);
        }
        if (olePlatformRecordForm.getTempId() == null) {
            olePlatformRecordForm.setTempId(0);
        } else {
            olePlatformRecordForm.setTempId(olePlatformRecordForm.getTempId() + 1);
        }
        olePlatformEventLog.setEventTempId(olePlatformRecordForm.getTempId());
        if (StringUtils.isNotBlank(olePlatformEventLog.getLogTypeName()) && olePlatformEventLog.getLogTypeName().equalsIgnoreCase("Event")) {
            olePlatformEventLog.setProblemTypeId(null);
            olePlatformEventLog.setEventStatus(null);
            olePlatformEventLog.setEventResolvedDate(null);
            olePlatformEventLog.setEventResolution(null);
        } else if (StringUtils.isNotBlank(olePlatformEventLog.getLogTypeName()) && olePlatformEventLog.getLogTypeName().equalsIgnoreCase("Problem")) {
            olePlatformEventLog.setEventTypeId(null);
        }
        olePlatformEventLog.setSaveFlag(true);
        OLEPlatformEventLog platformEventLog = (OLEPlatformEventLog) ObjectUtils.deepCopy(olePlatformEventLog);
        olePlatformRecordDocument.getEventLogs().add(platformEventLog);
        return addLine(olePlatformRecordForm, result, request, response);
    }


    /**
     * This method is to edit event
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=editEvent")
    public ModelAndView editEvent(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        OLEPlatformEventLog olePlatformEventLog = olePlatformRecordDocument.getEventLogs().get(index);
        olePlatformEventLog.setSaveFlag(false);
        return super.navigate(uifForm, result, request, response);
    }

    /**
     * This method is to edit filter event
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=editFilterEvent")
    public ModelAndView editFilterEvent(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        OLEPlatformEventLog olePlatformEventLog = olePlatformRecordDocument.getFilterEventLogs().get(index);
        olePlatformEventLog = getFilterPlatformEventLog(olePlatformEventLog, olePlatformRecordDocument.getFilterEventLogs());
        if (olePlatformEventLog != null) {
            olePlatformEventLog.setSaveFlag(false);
        }
        return super.navigate(uifForm, result, request, response);
    }

    /**
     * This method is to save event
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=saveEvent")
    public ModelAndView saveEvent(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        OLEPlatformEventLog olePlatformEventLog = olePlatformRecordDocument.getEventLogs().get(index);
        if (StringUtils.isBlank(olePlatformEventLog.getEventNote())) {
            GlobalVariables.getMessageMap().putErrorForSectionId("OLEPlatformEventLogTab-eventLogSection", OLEConstants.ERROR_NOTE_REQUIRED);
            return super.navigate(olePlatformRecordForm, result, request, response);
        }
        if (StringUtils.isNotBlank(olePlatformEventLog.getLogTypeName()) && olePlatformEventLog.getLogTypeName().equalsIgnoreCase("Event")) {
            olePlatformEventLog.setProblemTypeId(null);
            olePlatformEventLog.setEventStatus(null);
            olePlatformEventLog.setEventResolvedDate(null);
            olePlatformEventLog.setEventResolution(null);
        } else if (StringUtils.isNotBlank(olePlatformEventLog.getLogTypeName()) && olePlatformEventLog.getLogTypeName().equalsIgnoreCase("Problem")) {
            olePlatformEventLog.setEventTypeId(null);
        }
        olePlatformEventLog.setSaveFlag(true);
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    /**
     * This method is to save filter event
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=saveFilterEvent")
    public ModelAndView saveFilterEvent(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        OLEPlatformEventLog olePlatformEventLog = olePlatformRecordDocument.getFilterEventLogs().get(index);
        olePlatformEventLog = getFilterPlatformEventLog(olePlatformEventLog, olePlatformRecordDocument.getFilterEventLogs());
        if (StringUtils.isBlank(olePlatformEventLog.getEventNote())) {
            GlobalVariables.getMessageMap().putErrorForSectionId("OLEPlatformEventLogTab-filterEventLogSection", OLEConstants.ERROR_NOTE_REQUIRED);
            return super.navigate(olePlatformRecordForm, result, request, response);
        }
        if (olePlatformEventLog != null && StringUtils.isNotBlank(olePlatformEventLog.getLogTypeName()) && olePlatformEventLog.getLogTypeName().equalsIgnoreCase("Event")) {
            olePlatformEventLog.setProblemTypeId(null);
            olePlatformEventLog.setEventStatus(null);
            olePlatformEventLog.setEventResolvedDate(null);
            olePlatformEventLog.setEventResolution(null);
            olePlatformEventLog.setSaveFlag(true);
        } else if (olePlatformEventLog != null && StringUtils.isNotBlank(olePlatformEventLog.getLogTypeName()) && olePlatformEventLog.getLogTypeName().equalsIgnoreCase("Problem")) {
            olePlatformEventLog.setEventTypeId(null);
            olePlatformEventLog.setSaveFlag(true);
        }
        return super.navigate(olePlatformRecordForm, result, request, response);
    }


    /**
     * This method is to delete event
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=deleteEvent")
    public ModelAndView deleteEvent(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        if (olePlatformRecordDocument.getEventLogs().size() > index) {
            olePlatformRecordDocument.getEventLogs().remove(index);
        }
        return super.navigate(uifForm, result, request, response);
    }

    /**
     * This method is to delete filter event
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=deleteFilterEvent")
    public ModelAndView deleteFilterEvent(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        OLEPlatformEventLog olePlatformEventLog = olePlatformRecordDocument.getFilterEventLogs().get(index);
        olePlatformEventLog = getFilterPlatformEventLog(olePlatformEventLog, olePlatformRecordDocument.getFilterEventLogs());
        if (olePlatformEventLog != null) {
            olePlatformRecordDocument.getFilterEventLogs().remove(olePlatformEventLog);
            olePlatformRecordDocument.getEventLogs().remove(olePlatformEventLog);
        }
        return super.navigate(uifForm, result, request, response);
    }

    /**
     * This method is to copy the platform
     *
     * @param form
     * @return ModelAndView
     */
    @Override
    @RequestMapping(params = "methodToCall=copy")
    public ModelAndView copy(@ModelAttribute("KualiForm") TransactionalDocumentFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) form;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        super.copy(olePlatformRecordForm, result, request, response);
        return getUIFModelAndView(form);
    }

    /**
     * This method is to clear gokb id, platform name, platform provider and software values
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=clearGOKbId")
    public ModelAndView clearGOKbId(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        olePlatformRecordDocument.setGokbId(null);
        olePlatformRecordDocument.setPlatformProviderId(null);
        olePlatformRecordDocument.setPlatformProviderFlag(false);
        olePlatformRecordDocument.setVendorHeaderGeneratedIdentifier(null);
        olePlatformRecordDocument.setVendorDetailAssignedIdentifier(null);
        olePlatformRecordDocument.setVendorId(null);
        olePlatformRecordDocument.setStatusId(null);
        olePlatformRecordDocument.setPlatformProviderName(null);
        olePlatformRecordDocument.setName(null);
        olePlatformRecordDocument.setSoftware(null);
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    /**
     * This method is to add variant title
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addVariantTitle")
    public ModelAndView addVariantTitle(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        index++;
        List<OLEPlatformVariantTitle> olePlatformVariantTitleList = olePlatformRecordDocument.getVariantTitles();
        olePlatformRecordDocument.getVariantTitles().add(index, new OLEPlatformVariantTitle());
        olePlatformRecordDocument.setVariantTitles(olePlatformVariantTitleList);
        olePlatformRecordForm.setDocument(olePlatformRecordDocument);
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    /**
     * This method is to delete variant title
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=deleteVariantTitle")
    public ModelAndView deleteVariantTitle(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                           HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        List<OLEPlatformVariantTitle> olePlatformVariantTitleList = olePlatformRecordDocument.getVariantTitles();
        if (olePlatformVariantTitleList.size() > 1) {
            olePlatformVariantTitleList.remove(index);
        }
        olePlatformRecordForm.setDocument(olePlatformRecordDocument);
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=filter")
    public ModelAndView filter(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        List<OLEPlatformEventLog> olePlatformEventLogList = olePlatformRecordDocument.getEventLogs();
        olePlatformEventLogList = getOlePlatformService().filterByReportedDate(olePlatformRecordForm.getFilterReportedBeginDate(), olePlatformRecordForm.getFilterReportedEndDate(), olePlatformEventLogList);
        olePlatformEventLogList = getOlePlatformService().filterByResolvedDate(olePlatformRecordForm.getFilterResolvedBeginDate(), olePlatformRecordForm.getFilterResolvedEndDate(), olePlatformEventLogList);
        if (StringUtils.isNotBlank(olePlatformRecordForm.getStatus()) && !olePlatformRecordForm.getStatus().equals("All")) {
            olePlatformEventLogList = getOlePlatformService().filterByStatus(olePlatformEventLogList, olePlatformRecordForm.getStatus());
        }
        if (StringUtils.isNotBlank(olePlatformRecordForm.getLogType()) && !olePlatformRecordForm.getLogType().equals("All")) {
            olePlatformEventLogList = getOlePlatformService().filterByLogType(olePlatformEventLogList, olePlatformRecordForm.getLogType());
        }
        if (StringUtils.isNotBlank(olePlatformRecordForm.getEventType()) && !olePlatformRecordForm.getEventType().equals("All")) {
            olePlatformEventLogList = getOlePlatformService().filterByEventType(olePlatformEventLogList, olePlatformRecordForm.getEventType());
        }
        if (StringUtils.isNotBlank(olePlatformRecordForm.getProblemType()) && !olePlatformRecordForm.getProblemType().equals("All")) {
            olePlatformEventLogList = getOlePlatformService().filterByProblemType(olePlatformEventLogList, olePlatformRecordForm.getProblemType());
        }
        olePlatformRecordDocument.setFilterEventLogs(olePlatformEventLogList);
        olePlatformRecordForm.setFilterEventLog(true);
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=clearFilter")
    public ModelAndView clearFilter(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) uifForm;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        olePlatformRecordForm.setEventType(null);
        olePlatformRecordForm.setProblemType(null);
        olePlatformRecordForm.setLogType(null);
        olePlatformRecordForm.setStatus(null);
        olePlatformRecordForm.setFilterReportedBeginDate(null);
        olePlatformRecordForm.setFilterReportedEndDate(null);
        olePlatformRecordForm.setFilterResolvedBeginDate(null);
        olePlatformRecordForm.setFilterResolvedEndDate(null);
        olePlatformRecordDocument.setFilterEventLogs(new ArrayList<OLEPlatformEventLog>());
        olePlatformRecordForm.setFilterEventLog(false);
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    private OLEPlatformEventLog getFilterPlatformEventLog(OLEPlatformEventLog olePlatformEventLog, List<OLEPlatformEventLog> filterEventLogs) {
        if (StringUtils.isNotBlank(olePlatformEventLog.getPlatformEventLogId())) {
            for (OLEPlatformEventLog platformEventLog : filterEventLogs) {
                if (StringUtils.isNotBlank(platformEventLog.getPlatformEventLogId()) && platformEventLog.getPlatformEventLogId().equals(olePlatformEventLog.getPlatformEventLogId())) {
                    return platformEventLog;
                }
            }
        } else if (olePlatformEventLog.getEventTempId() != null) {
            for (OLEPlatformEventLog platformEventLog : filterEventLogs) {
                if (platformEventLog.getEventTempId() != null && platformEventLog.getEventTempId().equals(olePlatformEventLog.getEventTempId())) {
                    return platformEventLog;
                }
            }
        }
        return null;
    }

    @RequestMapping(params = "methodToCall=downloadEventAttachment1")
    public ModelAndView downloadEventAttachment1(@ModelAttribute("KualiForm") TransactionalDocumentFormBase form, BindingResult result,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) form;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        OLEPlatformEventLog olePlatformEventLog = olePlatformRecordDocument.getEventLogs().get(index);
        getOlePlatformService().downloadAttachment(response, olePlatformEventLog.getPlatformEventLogId(), olePlatformEventLog.getAttachmentFileName1(), olePlatformEventLog.getAttachmentContent1(), olePlatformEventLog.getAttachmentMimeType1());
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=downloadEventAttachment2")
    public ModelAndView downloadEventAttachment2(@ModelAttribute("KualiForm") TransactionalDocumentFormBase form, BindingResult result,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) form;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        OLEPlatformEventLog olePlatformEventLog = olePlatformRecordDocument.getEventLogs().get(index);
        getOlePlatformService().downloadAttachment(response, olePlatformEventLog.getPlatformEventLogId(), olePlatformEventLog.getAttachmentFileName2(), olePlatformEventLog.getAttachmentContent2(), olePlatformEventLog.getAttachmentMimeType2());
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=downloadFilterEventAttachment1")
    public ModelAndView downloadFilterEventAttachment1(@ModelAttribute("KualiForm") TransactionalDocumentFormBase form, BindingResult result,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) form;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        OLEPlatformEventLog olePlatformEventLog = olePlatformRecordDocument.getFilterEventLogs().get(index);
        getOlePlatformService().downloadAttachment(response, olePlatformEventLog.getPlatformEventLogId(), olePlatformEventLog.getAttachmentFileName1(), olePlatformEventLog.getAttachmentContent1(), olePlatformEventLog.getAttachmentMimeType1());
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=downloadFilterEventAttachment2")
    public ModelAndView downloadFilterEventAttachment2(@ModelAttribute("KualiForm") TransactionalDocumentFormBase form, BindingResult result,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) form;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        olePlatformRecordDocument.setSaveValidationFlag(false);
        int index = Integer.parseInt(olePlatformRecordForm.getActionParamaterValue(UifParameters.SELECTED_LINE_INDEX));
        OLEPlatformEventLog olePlatformEventLog = olePlatformRecordDocument.getFilterEventLogs().get(index);
        getOlePlatformService().downloadAttachment(response, olePlatformEventLog.getPlatformEventLogId(), olePlatformEventLog.getAttachmentFileName2(), olePlatformEventLog.getAttachmentContent2(), olePlatformEventLog.getAttachmentMimeType2());
        return super.navigate(olePlatformRecordForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=updateVendorDetails")
    public ModelAndView updateVendorDetails(@ModelAttribute("KualiForm") TransactionalDocumentFormBase form, BindingResult result,
                                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLEPlatformRecordForm olePlatformRecordForm = (OLEPlatformRecordForm) form;
        OLEPlatformRecordDocument olePlatformRecordDocument = (OLEPlatformRecordDocument) olePlatformRecordForm.getDocument();
        OleGokbPlatform oleGokbPlatform = getBusinessObjectService().findBySinglePrimaryKey(OleGokbPlatform.class, olePlatformRecordDocument.getGokbId());
        if (oleGokbPlatform!=null && oleGokbPlatform.getPlatformProviderId()!=null){
            OleGokbOrganization oleGokbOrganization =  getBusinessObjectService().findBySinglePrimaryKey(OleGokbOrganization.class, oleGokbPlatform.getPlatformProviderId());
            if (oleGokbOrganization != null && oleGokbOrganization.getGokbOrganizationId() != null) {
                Map<String, Integer> vendorMap = new HashMap();
                vendorMap.put(OLEConstants.GOKB_ID, oleGokbOrganization.getGokbOrganizationId());
                List<VendorDetail> vendorDetails = (List<VendorDetail>) KRADServiceLocator.getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
                if (vendorDetails != null && vendorDetails.size() > 0) {
                    olePlatformRecordDocument.setVendorHeaderGeneratedIdentifier(vendorDetails.get(0).getVendorHeaderGeneratedIdentifier());
                    olePlatformRecordDocument.setVendorDetailAssignedIdentifier(vendorDetails.get(0).getVendorDetailAssignedIdentifier());
                    olePlatformRecordDocument.setVendorId(vendorDetails.get(0).getVendorNumber());
                    olePlatformRecordDocument.setPlatformProviderName(vendorDetails.get(0).getVendorName());
                }
            }
        }
        olePlatformRecordDocument.setPlatformProviderFlag(false);
        return super.save(form,result,request,response);
    }



}
