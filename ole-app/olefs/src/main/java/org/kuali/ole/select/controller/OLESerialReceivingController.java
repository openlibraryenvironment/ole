package org.kuali.ole.select.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEKeyConstants;
import org.kuali.ole.describe.keyvalue.LocationValuesBuilder;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.select.bo.OLESerialReceivingDocument;
import org.kuali.ole.select.bo.OLESerialReceivingHistory;
import org.kuali.ole.select.bo.OLESerialReceivingType;
import org.kuali.ole.select.bo.OLESerialRelatedPODocument;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.form.OLESerialReceivingForm;
import org.kuali.ole.service.impl.OLESerialReceivingService;
import org.kuali.ole.service.impl.OLESerialReceivingServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.ole.util.StringUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kew.actionitem.ActionItem;
import org.kuali.rice.kew.actionlist.service.ActionListService;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actionrequest.service.ActionRequestService;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.TransactionalDocumentControllerBase;
import org.kuali.rice.krad.web.form.DocumentFormBase;
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
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 6/28/13
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/serialReceiving")
public class OLESerialReceivingController extends TransactionalDocumentControllerBase {
    @Override
    protected OLESerialReceivingForm createInitialForm(HttpServletRequest request) {
        return new OLESerialReceivingForm();
    }

    private DocstoreClientLocator docstoreClientLocator;

    DocumentService documentService = SpringContext.getBean(DocumentService.class);

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    BusinessObjectService businessObject = SpringContext.getBean(BusinessObjectService.class);

    @RequestMapping(params = "methodToCall=receive")
    public ModelAndView receive(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        try {
            String statusCode = oleSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
            if (statusCode.equalsIgnoreCase(DocumentStatus.INITIATED.getCode()) || statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
                Date date = new Date(System.currentTimeMillis());
                String interval = oleSerialReceivingDocument.getActionInterval();
                if (StringUtils.isNotBlank(interval)) {
                    boolean actIntvlFlag = isNumber(interval);
                    if (actIntvlFlag) {
                        Integer actIntvl = Integer.parseInt(oleSerialReceivingDocument.getActionInterval());
                        Date actDate = DateUtils.addDays(date, actIntvl);
                        oleSerialReceivingDocument.setActionDate(new Timestamp(actDate.getTime()));
                    }
                }
                if (oleSerialReceivingDocument.getUnboundLocation() != null && !isValidLocation(oleSerialReceivingDocument.getUnboundLocation())) {
                    GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_UNBOUND_LOCATION_FIELD, OLEKeyConstants.SERIAL_UNBOUND_LOCATION);
                }
                OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
                if (oleSerialReceivingDocument.isClaim()) {
                    oleSerialReceivingService.validateClaim(oleSerialReceivingDocument);
                }
                if (GlobalVariables.getMessageMap().getErrorCount() > 0) {
                    return getUIFModelAndView(oleSerialReceivingForm);
                }
                String treatmentNote = oleSerialReceivingDocument.getTreatmentInstructionNote();
                if (StringUtils.isBlank(treatmentNote)) {
                    oleSerialReceivingDocument.setTreatmentNoteFlag(false);
                    oleSerialReceivingService.updateEnumValues(oleSerialReceivingDocument);
                    oleSerialReceivingService.createOrUpdateReceivingRecordType(oleSerialReceivingDocument);
                    oleSerialReceivingService.updateEnumCaptionValues(oleSerialReceivingDocument, null);
                    oleSerialReceivingService.validateSerialReceivingDocument(oleSerialReceivingDocument);
                    if (GlobalVariables.getMessageMap().getErrorCount() > 0) {
                        oleSerialReceivingDocument.setItemCheckFlag(false);
                        return getUIFModelAndView(oleSerialReceivingForm);
                    } else {
                        oleSerialReceivingDocument.setItemCheckFlag(true);
                    }
                    oleSerialReceivingService.receiveRecord(oleSerialReceivingDocument, OLEConstants.RECEIVED);
                    save(oleSerialReceivingForm, result, request, response);
                } else {
                    oleSerialReceivingDocument.setTreatmentNoteFlag(true);
                    oleSerialReceivingDocument.setTreatmentDialogNote(treatmentNote);
                }
            } else {
                oleSerialReceivingDocument.setItemCheckFlag(false);
                GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE, OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        oleSerialReceivingDocument.setCurrentActionPerformed("receive");
        return getUIFModelAndView(oleSerialReceivingForm);
    }
    @Override
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        Note note = documentService.createNoteFromDocument(oleSerialReceivingDocument, OLEConstants.SRR_ROUTE_NOTES);;
        oleSerialReceivingDocument.addNote(note);
        ModelAndView modelAndView = super.route(oleSerialReceivingForm, result, request, response);
        try {
            if (oleSerialReceivingDocument.getSubscriptionStatus() != null) {
                PHoldings holdings = (PHoldings) getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleSerialReceivingDocument.getInstanceId());
                OleHoldings oleHoldings = holdings.getContentObject();
                if (!oleSerialReceivingDocument.getSubscriptionStatus().equals(oleHoldings.getReceiptStatus())) {
                    oleHoldings.setReceiptStatus(oleSerialReceivingDocument.getSubscriptionStatus());
                    holdings.serializeContent();
                    getDocstoreClientLocator().getDocstoreClient().updateHoldings(holdings);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception occured while setting Receipt status to OleHoldings :" + e.getMessage());
        }

        if (oleSerialReceivingDocument.getSerialReceivingRecord() == null || oleSerialReceivingDocument.getSerialReceivingRecord().isEmpty()) {
            oleSerialReceivingDocument.setSerialReceivingRecord(oleSerialReceivingDocument.getSerialReceivingRecordId());
            getBusinessObjectService().save(oleSerialReceivingDocument);
        }
        return modelAndView;
    }

    @Override
    @RequestMapping(params = "methodToCall=docHandler")
    public ModelAndView docHandler(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        long begin = System.currentTimeMillis();
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        ModelAndView modelAndView = super.docHandler(oleSerialReceivingForm, result, request, response);
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        String vendorId = oleSerialReceivingDocument.getVendorId();
        oleSerialReceivingDocument.getDocumentHeader().setDocumentDescription(OLEConstants.SERIAL_REC_DESC + oleSerialReceivingForm.getDocument().getDocumentNumber());
        if (oleSerialReceivingDocument.getUrgentNote() != null) {
            oleSerialReceivingDocument.setUrgentNoteFlag(true);
            oleSerialReceivingDocument.setUrgentDialogNote(oleSerialReceivingDocument.getUrgentNote());
        } else {
            oleSerialReceivingDocument.setUrgentNoteFlag(false);
        }
        if (StringUtils.isBlank(oleSerialReceivingDocument.getSerialReceivingRecordId())) {
            oleSerialReceivingDocument.setPublicDisplay(true);
        }
        OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
        oleSerialReceivingService.readReceivingRecordType(oleSerialReceivingDocument);
        oleSerialReceivingService.updateEnumCaptionValues(oleSerialReceivingDocument, null);
        String statusCode = oleSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
        oleSerialReceivingDocument.setStatusCode(statusCode);
        //if (statusCode.equalsIgnoreCase(DocumentStatus.INITIATED.getCode())||statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
        String bibId;
        String instanceId;
        String holdingsId;
        if (request.getParameter(OLEConstants.BIB_ID) != null) {
            bibId = request.getParameter(OLEConstants.BIB_ID);
        } else {
            bibId = oleSerialReceivingDocument.getBibId();
        }
        if (request.getParameter(OLEConstants.INSTANCE_ID) != null) {
            instanceId = request.getParameter(OLEConstants.INSTANCE_ID);
            holdingsId = request.getParameter(OLEConstants.INSTANCE_ID);
        } else {
            instanceId = oleSerialReceivingDocument.getInstanceId();
            holdingsId = oleSerialReceivingDocument.getInstanceId();
        }
        if (bibId != null) {
            oleSerialReceivingDocument.setBibId(bibId);
            oleSerialReceivingDocument.setLocalId(DocumentUniqueIDPrefix.getDocumentId(bibId));
        } else if (oleSerialReceivingDocument.getBibId() != null) {

            oleSerialReceivingDocument.setLocalId(DocumentUniqueIDPrefix.getDocumentId(oleSerialReceivingDocument.getBibId()));
        }
        // LinkedHashMap<String, String> bibIdList = new LinkedHashMap<String, String>();
        //bibIdList.put(DocType.BIB.getDescription(), bibId);
        // List<WorkBibDocument> workBibDocuments = getWorkBibDocuments(bibIdList);
        BibTree bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibId);


        DocstoreUtil docstoreUtil = new DocstoreUtil();
        // WorkBibDocument workBibDocument = workBibDocuments != null && workBibDocuments.size() > 0 ? workBibDocuments.get(0) : null;
        String locationName = null;
        String copyNumber = null;
        String callNumber = null;
        String holdingId = null;
        String receiptSatusId = null;
        if (bibTree != null) {
            if (bibTree.getHoldingsTrees() != null) {
                for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                    if (holdingsTree.getHoldings().getId().equals(holdingsId)) {
                        OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(holdingsTree.getHoldings().getContent());
                        //locationName = workInstanceDocument.getHoldingsDocument().getLocationName();
                        locationName = docstoreUtil.getLocation(oleHoldings.getLocation(), new StringBuffer(""));
                        //copyNumber = workInstanceDocument.getHoldingsDocument().getCopyNumber();
                        //copyNumber=holdingsTree.getHoldings().getCopyNumber();
                        //callNumber = workInstanceDocument.getHoldingsDocument().getCallNumber();
                        callNumber = oleHoldings != null && oleHoldings.getCallNumber() != null && oleHoldings.getCallNumber().getNumber() != null ? oleHoldings.getCallNumber().getNumber() : "";
                        copyNumber = oleHoldings != null && oleHoldings.getCopyNumber() != null ? oleHoldings.getCopyNumber() : "";
                        holdingId = holdingsTree.getHoldings().getId();
                        receiptSatusId = oleHoldings != null && oleHoldings.getReceiptStatus() != null? oleHoldings.getReceiptStatus() : "";
                    }
                }
            }
            oleSerialReceivingDocument.setBoundLocation(locationName != null ? locationName : "");
            //oleSerialReceivingDocument.setCorporateAuthor(workBibDocument.getAuthor() != null ? workBibDocument.getAuthor() : "");
            oleSerialReceivingDocument.setCorporateAuthor(bibTree.getBib() != null ? bibTree.getBib().getAuthor() : "");

            //oleSerialReceivingDocument.setTitle(workBibDocument.getTitle() != null ? workBibDocument.getTitle() : "");
            String title = "";
            if(bibTree.getBib() != null && bibTree.getBib().getTitle() != null){
                title = bibTree.getBib().getTitle();
                title = title.replaceAll("<","&lt;");
                title = title.replaceAll(">","&gt;");
            }
            oleSerialReceivingDocument.setTitle(title);
            //oleSerialReceivingDocument.setTitle(bibTree.getBib() != null ? bibTree.getBib().getTitle() : "");
            //oleSerialReceivingDocument.setIssn(workBibDocument.getIssn() != null ? workBibDocument.getIssn() : "");
            oleSerialReceivingDocument.setIssn(bibTree.getBib() != null ? bibTree.getBib().getIssn() : "");
            // oleSerialReceivingDocument.setPublisher(workBibDocument.getPublisher() != null ? workBibDocument.getPublisher() : "");
            oleSerialReceivingDocument.setPublisher(bibTree.getBib() != null ? bibTree.getBib().getPublisher() : "");
            oleSerialReceivingDocument.setCopyNumber(copyNumber != null ? copyNumber : "");
            oleSerialReceivingDocument.setCallNumber(callNumber != null ? callNumber : "");
            oleSerialReceivingDocument.setSubscriptionStatus(receiptSatusId != null ? receiptSatusId : "");
            if (StringUtils.isNotBlank(instanceId)) {
                //oleSerialReceivingDocument.setInstanceId(instanceId);
                oleSerialReceivingDocument.setInstanceId(holdingsId);

            } else {
                //oleSerialReceivingDocument.setInstanceId(workBibDocument.getInstanceDocument().getInstanceIdentifier() != null ? workBibDocument.getInstanceDocument().getInstanceIdentifier() : "");
                oleSerialReceivingDocument.setInstanceId(holdingId);
            }
            oleSerialReceivingDocument.setTempInstanceId(oleSerialReceivingDocument.getInstanceId());
        }
        if ((oleSerialReceivingDocument.getPoId() == null || oleSerialReceivingDocument.getPoId().isEmpty())) {
            oleSerialReceivingService.updatePOVendorDetail(oleSerialReceivingDocument);
        } else {
            Map<String, String> parentCriterial = new HashMap<>();
            String poDocNum = oleSerialReceivingDocument.getPoId();

            parentCriterial.put("purapDocumentIdentifier", poDocNum);
            List<OlePurchaseOrderDocument> olePurchaseOrderDocument = (List<OlePurchaseOrderDocument>) getBusinessObjectService().findMatching(OlePurchaseOrderDocument.class, parentCriterial);
            if (olePurchaseOrderDocument != null)
                oleSerialReceivingDocument.setPoIdLink(oleSerialReceivingDocument.formPoIdLinkUsingPoDocumentNumber(olePurchaseOrderDocument.get(0).getDocumentNumber()));
        }
        String principalName = GlobalVariables.getUserSession().getPrincipalName();
        oleSerialReceivingDocument.setOperatorId(principalName);
        if (oleSerialReceivingDocument.getSerialReceivingRecord() == null || oleSerialReceivingDocument.getSerialReceivingRecord().isEmpty()) {
            oleSerialReceivingDocument.setSerialReceivingRecord(oleSerialReceivingDocument.getSerialReceivingRecordId());
        }
            /*if (oleSerialReceivingDocument.getOleSerialReceivingTypes() != null && oleSerialReceivingDocument.getOleSerialReceivingTypes().size() > 0) {
                for(OLESerialReceivingType oleSerialReceivingType : oleSerialReceivingDocument.getOleSerialReceivingTypes()) {
                    oleSerialReceivingService.populateEnumerationAndChronologyValues(oleSerialReceivingDocument, oleSerialReceivingType);
                }
            }*/
        // }
        if (vendorId != null && vendorId.length() > 0) {
            oleSerialReceivingService.populateVendorNameFromVendorId(vendorId, oleSerialReceivingDocument);
        }
        List<OLESerialReceivingHistory> oleSerialReceivingHistoryList = oleSerialReceivingDocument.getOleSerialReceivingHistoryList() != null ?
                oleSerialReceivingDocument.getOleSerialReceivingHistoryList() : new ArrayList<OLESerialReceivingHistory>();
        oleSerialReceivingService.sortById(oleSerialReceivingHistoryList);
        oleSerialReceivingService.listOutHistoryBasedOnReceivingRecord(oleSerialReceivingDocument);
        if (oleSerialReceivingDocument.getOleSerialReceivingHistoryList() != null && oleSerialReceivingDocument.getOleSerialReceivingHistoryList().size() > 0) {
            for (int serialReceivingHistoryList = 0; serialReceivingHistoryList < oleSerialReceivingDocument.getOleSerialReceivingHistoryList().size(); serialReceivingHistoryList++) {
                oleSerialReceivingService.setEnumerationAndChronologyValues(oleSerialReceivingDocument.getOleSerialReceivingHistoryList().get(serialReceivingHistoryList));
            }
        }
        long end = System.currentTimeMillis();
        long total = end - begin;
        LOG.info("Time taken Inside docHandler"+total);
        if(oleSerialReceivingDocument.getReceivingRecordType() != null && !oleSerialReceivingDocument.getReceivingRecordType().equals("Main")) {
            String receivingId = oleSerialReceivingDocument.getSerialReceivingRecordId();
            Map map = new HashMap();
            map.put("serialReceivingRecordId",receivingId);
            map.put("receivingRecordType",OLEConstants.MAIN_REC_REC_TYP);
            OLESerialReceivingType serialReceivingType =  getBusinessObjectService().findByPrimaryKey(OLESerialReceivingType.class,map);
            if(serialReceivingType != null) {
                oleSerialReceivingDocument.setReceivingRecordType(OLEConstants.MAIN_REC_REC_TYP);
                oleSerialReceivingDocument.setEnumerationCaptionLevel1(serialReceivingType.getEnumerationCaptionLevel1());
                oleSerialReceivingDocument.setEnumerationCaptionLevel2(serialReceivingType.getEnumerationCaptionLevel2());
                oleSerialReceivingDocument.setEnumerationCaptionLevel3(serialReceivingType.getEnumerationCaptionLevel3());
                oleSerialReceivingDocument.setEnumerationCaptionLevel4(serialReceivingType.getEnumerationCaptionLevel4());
                oleSerialReceivingDocument.setEnumerationCaptionLevel5(serialReceivingType.getEnumerationCaptionLevel5());
                oleSerialReceivingDocument.setEnumerationCaptionLevel6(serialReceivingType.getEnumerationCaptionLevel6());
                oleSerialReceivingDocument.setChronologyCaptionLevel1(serialReceivingType.getChronologyCaptionLevel1());
                oleSerialReceivingDocument.setChronologyCaptionLevel2(serialReceivingType.getChronologyCaptionLevel2());
                oleSerialReceivingDocument.setChronologyCaptionLevel3(serialReceivingType.getChronologyCaptionLevel3());
                oleSerialReceivingDocument.setChronologyCaptionLevel4(serialReceivingType.getChronologyCaptionLevel4());
                //oleSerialReceivingService.receiveRecord(oleSerialReceivingDocument, OLEConstants.RECEIVED);
                //save(oleSerialReceivingForm, result, request, response);
            }else{
                oleSerialReceivingDocument.setReceivingRecordType(OLEConstants.MAIN_REC_REC_TYP);
                oleSerialReceivingDocument.setEnumerationCaptionLevel1("");
                oleSerialReceivingDocument.setEnumerationCaptionLevel2("");
                oleSerialReceivingDocument.setEnumerationCaptionLevel3("");
                oleSerialReceivingDocument.setEnumerationCaptionLevel4("");
                oleSerialReceivingDocument.setEnumerationCaptionLevel5("");
                oleSerialReceivingDocument.setEnumerationCaptionLevel6("");
                oleSerialReceivingDocument.setChronologyCaptionLevel1("");
                oleSerialReceivingDocument.setChronologyCaptionLevel2("");
                oleSerialReceivingDocument.setChronologyCaptionLevel3("");
                oleSerialReceivingDocument.setChronologyCaptionLevel4("");
            }
            oleSerialReceivingService.updateEnumCaptionValues(oleSerialReceivingDocument, null);
            GlobalVariables.getMessageMap().getInfoMessages().clear();
        }
        return modelAndView;
    }


//    private List<WorkBibDocument> getWorkBibDocuments(LinkedHashMap<String, String> bibIdList) {
//        List<LinkedHashMap<String, String>> bibIdMapList = new ArrayList<LinkedHashMap<String, String>>();
//        bibIdMapList.add(bibIdList);
//        QueryService queryService = QueryServiceImpl.getInstance();
//        List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
//        try {
//            workBibDocuments = queryService.getWorkBibRecords(bibIdMapList);
//
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return workBibDocuments;
//    }

    @RequestMapping(params = "methodToCall=returnToSearch")
    public ModelAndView returnToSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        if(!StringUtils.isNotBlank(oleSerialReceivingDocument.getCurrentActionPerformed())&& oleSerialReceivingDocument.getCurrentActionPerformed().equals("recieve")){
            oleSerialReceivingDocument.setCurrentActionPerformed("");
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=confirmReturnToSearch")
    public ModelAndView confirmReturnToSearch(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        oleSerialReceivingDocument.setConfirmMessage(OLEConstants.CONFIRM_MSG_RETURN_TO_SEARCH);
        oleSerialReceivingDocument.setReturnToSearch(true);
        return getUIFModelAndView(oleSerialReceivingForm);
    }


    @RequestMapping(params = "methodToCall=specialIssue")
    public ModelAndView specialIssue(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        String statusCode = oleSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
        if (oleSerialReceivingDocument.getUnboundLocation() != null && !isValidLocation(oleSerialReceivingDocument.getUnboundLocation())) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_UNBOUND_LOCATION_FIELD, OLEKeyConstants.SERIAL_UNBOUND_LOCATION);
            return getUIFModelAndView(oleSerialReceivingForm);
        }
        if (statusCode.equalsIgnoreCase(DocumentStatus.INITIATED.getCode()) || statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
            oleSerialReceivingDocument.setSpecialIssueFlag(true);
        } else {
            oleSerialReceivingDocument.setItemCheckFlag(false);
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE, OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE);
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=claim")
    public ModelAndView claim(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        try {
            String statusCode = oleSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
            if (statusCode.equalsIgnoreCase(DocumentStatus.INITIATED.getCode()) || statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
                OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
                oleSerialReceivingService.updateEnumValues(oleSerialReceivingDocument);
                oleSerialReceivingService.createOrUpdateReceivingRecordType(oleSerialReceivingDocument);
                oleSerialReceivingDocument.setClaimIntervalInformation(oleSerialReceivingDocument.getClaimDialogNote());
                oleSerialReceivingService.receiveRecord(oleSerialReceivingDocument, OLEConstants.CLAIMED);
                save(oleSerialReceivingForm, result, request, response);
            } else {
                oleSerialReceivingDocument.setItemCheckFlag(false);
                GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE, OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @Override
    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        List<OLESerialReceivingHistory> oleSerialReceivingHistoryLists = oleSerialReceivingDocument.getOleSerialReceivingHistoryList();
        if (oleSerialReceivingHistoryLists != null) {
            for(int serialReceivingHistory=0; serialReceivingHistory<oleSerialReceivingHistoryLists.size(); serialReceivingHistory++) {
                if (StringUtils.isBlank(oleSerialReceivingHistoryLists.get(serialReceivingHistory).getEnumerationCaption())
                        && StringUtils.isBlank(oleSerialReceivingHistoryLists.get(serialReceivingHistory).getChronologyCaption())) {
                    GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ENUM_OR_CHRON_REQUIRED, OLEKeyConstants.SERIAL_RECEIVE_ENUM_OR_CHRON_REQUIRED);
                    oleSerialReceivingForm.setJumpToId(OLEConstants.SERIAL_RECEIVING_MAIN_PAGE);
                    return getUIFModelAndView(form);
                }
            }
        }
        OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
        String statusCode = oleSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
        if(StringUtils.isNotEmpty(oleSerialReceivingDocument.getInstanceId())){
            Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleSerialReceivingDocument.getInstanceId());
            String content = holdings.getContent();
            if(StringUtils.isNotEmpty(content)){
                HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
                OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(content);
                if(StringUtils.isNotEmpty(oleSerialReceivingDocument.getSubscriptionStatus())){
                    oleHoldings.setReceiptStatus(oleSerialReceivingDocument.getSubscriptionStatus());
                    String xmlContent = holdingOlemlRecordProcessor.toXML(oleHoldings);
                    holdings.setContent(xmlContent);
                    getDocstoreClientLocator().getDocstoreClient().updateHoldings(holdings);
                }
            }
        }
        if (statusCode.equalsIgnoreCase(DocumentStatus.INITIATED.getCode()) || statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
            Date date = new Date(System.currentTimeMillis());
            String interval = oleSerialReceivingDocument.getActionInterval();
            if (StringUtils.isNotBlank(interval)) {
                boolean actIntvlFlag = isNumber(interval);
                if (actIntvlFlag && oleSerialReceivingDocument.getActionDate() == null) {
                    Integer actIntvl = Integer.parseInt(oleSerialReceivingDocument.getActionInterval());
                    Date actDate = DateUtils.addDays(date, actIntvl);
                    oleSerialReceivingDocument.setActionDate(new Timestamp(actDate.getTime()));
                }
            }
            ModelAndView modelAndView;
            if (oleSerialReceivingDocument.isClaim()) {
                if (StringUtils.isBlank(interval)) {
                    GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ACTIONINTERVAL, OLEKeyConstants.SERIAL_RECEIVE_ACTIONINTERVAL);
                }
                if (oleSerialReceivingDocument.getActionDate() == null) {
                    GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ACTIONDATE, OLEKeyConstants.SERIAL_RECEIVE_ACTIONDATE);
                }
                if (StringUtils.isBlank(oleSerialReceivingDocument.getVendorId())) {
                    GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ID, OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ID);
                }
            }
            if (oleSerialReceivingDocument.getUnboundLocation() != null && !isValidLocation(oleSerialReceivingDocument.getUnboundLocation())) {
                GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_UNBOUND_LOCATION_FIELD, OLEKeyConstants.SERIAL_UNBOUND_LOCATION);
            }
            if (GlobalVariables.getMessageMap().getErrorCount() > 0) {
                return getUIFModelAndView(oleSerialReceivingForm);
            }
            if (oleSerialReceivingDocument.getVendorAliasName() != null && oleSerialReceivingDocument.getVendorAliasName().length() > 0) {
                oleSerialReceivingService.validateVendorDetailsForSave(oleSerialReceivingDocument);
            }
            oleSerialReceivingService.updateEnumValues(oleSerialReceivingDocument);
            oleSerialReceivingService.createOrUpdateReceivingRecordType(oleSerialReceivingDocument);
            modelAndView = super.save(oleSerialReceivingForm, result, request, response);
            if (oleSerialReceivingDocument.getSerialReceivingRecord() == null || oleSerialReceivingDocument.getSerialReceivingRecord().isEmpty()) {
                oleSerialReceivingDocument.setSerialReceivingRecord(oleSerialReceivingDocument.getSerialReceivingRecordId());
                getBusinessObjectService().save(oleSerialReceivingDocument);
            }
            oleSerialReceivingService.updateSerialIdInCopy(oleSerialReceivingDocument);
            oleSerialReceivingDocument.setTempInstanceId(oleSerialReceivingDocument.getInstanceId());
            List<OLESerialReceivingHistory> oleSerialReceivingHistoryList = oleSerialReceivingDocument.getOleSerialReceivingHistoryList() != null ?
                    oleSerialReceivingDocument.getOleSerialReceivingHistoryList() : new ArrayList<OLESerialReceivingHistory>();
            oleSerialReceivingService.sortById(oleSerialReceivingHistoryList);
            oleSerialReceivingService.listOutHistoryBasedOnReceivingRecord(oleSerialReceivingDocument);
            assignActionRequests(oleSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getDocumentId());
            return modelAndView;
        } else {
            oleSerialReceivingDocument.setItemCheckFlag(false);
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE, OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE);
            return getUIFModelAndView(form);
        }
    }

    public static boolean isNumber(String actionInterval) {
        String actStr = actionInterval;
        for (int i = 0; i < actStr.length(); i++) {
            if (!Character.isDigit(actStr.charAt(i)))
                return false;
        }
        return true;
    }

    @RequestMapping(params = "methodToCall=saveNote")
    public ModelAndView saveNote(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        String spclIssueNote = oleSerialReceivingDocument.getSpecialIssueNote();
        if (spclIssueNote != null && !spclIssueNote.isEmpty()) {
            oleSerialReceivingDocument.setEnumerationCaptionLevel1(spclIssueNote);
        }
        OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
        oleSerialReceivingService.receiveRecord(oleSerialReceivingDocument, OLEConstants.RECEIVED);
        save(form, result, request, response);
        oleSerialReceivingDocument.setSpecialIssueFlag(false);
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=claimNote")
    public ModelAndView claimNote(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        try {
            String statusCode = oleSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
            if (statusCode.equalsIgnoreCase(DocumentStatus.INITIATED.getCode()) || statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
                Date date = new Date(System.currentTimeMillis());
                String interval = oleSerialReceivingDocument.getActionInterval();
                if (StringUtils.isNotBlank(interval)) {
                    boolean actIntvlFlag = isNumber(interval);
                    if (actIntvlFlag && oleSerialReceivingDocument.getActionDate() == null) {
                        Integer actIntvl = Integer.parseInt(oleSerialReceivingDocument.getActionInterval());
                        Date actDate = DateUtils.addDays(date, actIntvl);
                        oleSerialReceivingDocument.setActionDate(new Timestamp(actDate.getTime()));
                    }
                }
                /*if (StringUtils.isBlank(interval)) {
                    GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ACTIONINTERVAL, OLEKeyConstants.SERIAL_RECEIVE_ACTIONINTERVAL);
                }
                if (oleSerialReceivingDocument.getActionDate() == null) {
                    GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ACTIONDATE, OLEKeyConstants.SERIAL_RECEIVE_ACTIONDATE);
                }
                if (StringUtils.isBlank(oleSerialReceivingDocument.getVendorId())) {
                    GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ID, OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ID);
                }*/
                OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
                oleSerialReceivingService.validateClaim(oleSerialReceivingDocument);
                if (GlobalVariables.getMessageMap().getErrorCount() > 0) {
                    return getUIFModelAndView(oleSerialReceivingForm);
                }
                oleSerialReceivingDocument.setClaimNoteFlag(true);
                oleSerialReceivingDocument.setClaimAgainNoteFlag(false);
                oleSerialReceivingDocument.setClaimDialogNote(oleSerialReceivingDocument.getClaimIntervalInformation());
            } else {
                oleSerialReceivingDocument.setItemCheckFlag(false);
                GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE, OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }


    @RequestMapping(params = "methodToCall=acknowledgeNote")
    public ModelAndView acknowledgeNote(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        try {
            OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
            oleSerialReceivingService.updateEnumValues(oleSerialReceivingDocument);
            oleSerialReceivingService.createOrUpdateReceivingRecordType(oleSerialReceivingDocument);
            oleSerialReceivingService.validateSerialReceivingDocument(oleSerialReceivingDocument);
            if (GlobalVariables.getMessageMap().getErrorCount() > 0) {
                oleSerialReceivingDocument.setItemCheckFlag(false);
                return getUIFModelAndView(oleSerialReceivingForm);
            }

            oleSerialReceivingService.receiveRecord(oleSerialReceivingDocument, OLEConstants.RECEIVED);
            save(oleSerialReceivingForm, result, request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }


    @RequestMapping(params = "methodToCall=acknowledgeUrgentNote")
    public ModelAndView acknowledgeUrgentNote(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        oleSerialReceivingDocument.setUrgentNote(oleSerialReceivingDocument.getUrgentDialogNote());
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=receiveHistory")
    public ModelAndView receiveHistory(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        try {
            OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
            oleSerialReceivingDocument.setCurrentActionPerformed("");
            String statusCode = oleSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
            if (statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
                Date date = new Date(System.currentTimeMillis());
                for (OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingDocument.getOleSerialReceivingHistoryList()) {
                    if (oleSerialReceivingHistory.getSerialReceivingRecordHistoryId().equalsIgnoreCase(oleSerialReceivingDocument.getSerialReceiptHistoryId())) {
                        oleSerialReceivingHistory.setReceiptStatus(OLEConstants.RECEIVED);
                        oleSerialReceivingHistory.setReceiptDate(new Timestamp(date.getTime()));
                    }
                }
                save(oleSerialReceivingForm, result, request, response);
            } else {
                oleSerialReceivingDocument.setItemCheckFlag(false);
                GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE, OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }


    @RequestMapping(params = "methodToCall=claimHistory")
    public ModelAndView claimHistory(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        try {
            Date date = new Date(System.currentTimeMillis());
            OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
            oleSerialReceivingDocument.setCurrentActionPerformed("");
            String statusCode = oleSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
            if (statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
                for (OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingDocument.getOleSerialReceivingHistoryList()) {
                    if (oleSerialReceivingHistory.getSerialReceivingRecordHistoryId().equalsIgnoreCase(oleSerialReceivingDocument.getSerialReceiptHistoryId())) {
                        oleSerialReceivingHistory.setReceiptStatus(OLEConstants.CLAIMED);
                        oleSerialReceivingHistory.setClaimDate(new Timestamp(date.getTime()));
                        oleSerialReceivingHistory.setClaimType(oleSerialReceivingDocument.getClaimType());
                        oleSerialReceivingHistory.setClaimNote(oleSerialReceivingDocument.getClaimDialogNote());
                        if (oleSerialReceivingHistory.getClaimCount() != null && !oleSerialReceivingHistory.getClaimCount().isEmpty()) {
                            Integer count = Integer.parseInt(oleSerialReceivingHistory.getClaimCount());
                            count++;
                            oleSerialReceivingHistory.setClaimCount(count.toString());
                        } else {
                            oleSerialReceivingHistory.setClaimCount("1");
                        }

                    }
                }
                save(oleSerialReceivingForm, result, request, response);
            } else {
                oleSerialReceivingDocument.setItemCheckFlag(false);
                GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE, OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=claimAgainNote")
    public ModelAndView claimAgainNote(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        try {
            OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
            oleSerialReceivingDocument.setCurrentActionPerformed("");
            String statusCode = oleSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
            if (statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
                for (OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingDocument.getOleSerialReceivingHistoryList()) {
                    if (oleSerialReceivingHistory.getSerialReceivingRecordHistoryId().equalsIgnoreCase(oleSerialReceivingDocument.getSerialReceiptHistoryId())) {
                        oleSerialReceivingDocument.setClaimAgainNoteFlag(true);
                        oleSerialReceivingDocument.setClaimNoteFlag(false);
                        oleSerialReceivingDocument.setClaimType(oleSerialReceivingHistory.getClaimType());
                        oleSerialReceivingDocument.setClaimDialogNote(oleSerialReceivingHistory.getClaimNote());
                    }
                }
            } else {
                oleSerialReceivingDocument.setItemCheckFlag(false);
                GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE, OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=unReceiveHistory")
    public ModelAndView unReceiveHistory(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        try {
            OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
            oleSerialReceivingDocument.setCurrentActionPerformed("");
            String statusCode = oleSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
            if (statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
                for (OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingDocument.getOleSerialReceivingHistoryList()) {
                    if (oleSerialReceivingHistory.getSerialReceivingRecordHistoryId().equalsIgnoreCase(oleSerialReceivingDocument.getSerialReceiptHistoryId())) {
                        getBusinessObjectService().delete(oleSerialReceivingHistory);
                        oleSerialReceivingDocument.getOleSerialReceivingHistoryList().remove(oleSerialReceivingHistory);
                        break;
                    }
                }
                OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
                oleSerialReceivingService.listOutHistoryBasedOnReceivingRecord(oleSerialReceivingDocument);
            } else {
                oleSerialReceivingDocument.setItemCheckFlag(false);
                GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE, OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=cancelClaim")
    public ModelAndView cancelClaim(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        try {
            OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
            oleSerialReceivingDocument.setCurrentActionPerformed("");
            String statusCode = oleSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getStatus().getCode();
            if (statusCode.equalsIgnoreCase(DocumentStatus.SAVED.getCode())) {
                for (OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingDocument.getOleSerialReceivingHistoryList()) {
                    if (oleSerialReceivingHistory.getSerialReceivingRecordHistoryId().equalsIgnoreCase(oleSerialReceivingDocument.getSerialReceiptHistoryId())) {
                        oleSerialReceivingHistory.setReceiptStatus(OLEConstants.RCV_CANCELLED);
                        break;
                    }
                }
            } else {
                oleSerialReceivingDocument.setItemCheckFlag(false);
                GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE, OLEKeyConstants.SERIAL_RECEIVE_EN_ROUTE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }


    @RequestMapping(params = "methodToCall=selectVendor")
    public ModelAndView selectVendor(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        if (oleSerialReceivingDocument.getVendorAliasName() != null && oleSerialReceivingDocument.getVendorAliasName().length() > 0) {
            OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
            oleSerialReceivingService.validateVendorDetailsForSelect(oleSerialReceivingDocument);
        } else {
            oleSerialReceivingDocument.setVendorId(null);
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ALIAS_NOT_FOUND, OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ALIAS_NOT_FOUND);
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=searchVendor")
    public ModelAndView searchVendor(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
        oleSerialReceivingService.populateVendorAliasNameFromVendorName(oleSerialReceivingDocument);
        return getUIFModelAndView(oleSerialReceivingForm);
    }


    /**
     * Performs the disapprove workflow action on the form document instance
     *
     * @param form - document form base containing the document instance that will be disapproved
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=disapprove")
    public ModelAndView disapprove(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oldSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oldSerialReceivingDocument.setCurrentActionPerformed("");
        performWorkflowAction(form, UifConstants.WorkflowAction.DISAPPROVE, true);
        //ModelAndView modelAndView = super.disapprove(form, result, request, response);
        form.setDocId(null);
        form.setCommand(KewApiConstants.INITIATE_COMMAND);
        super.docHandler(form, result, request, response);
        OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
        String user = oldSerialReceivingDocument.getDocumentHeader().getWorkflowDocument().getInitiatorPrincipalId();
        Person person = SpringContext.getBean(PersonService.class).getPerson(user);
        GlobalVariables.setUserSession(new UserSession(person.getPrincipalName()));
        DocumentService documentService = GlobalResourceLoader.getService(OLEConstants.DOCUMENT_HEADER_SERVICE);
        OLESerialReceivingDocument newDocument = (OLESerialReceivingDocument) documentService.getNewDocument("OLE_SER_RECV_REC");
        newDocument.getDocumentHeader().setDocumentDescription(OLEConstants.SERIAL_REC_DESC + form.getDocument().getDocumentNumber());
        oleSerialReceivingService.disapproveCreateNewWithExisting(newDocument, oldSerialReceivingDocument);
        documentService.saveDocument(newDocument);
        form.setDocument(newDocument);
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=linkPO")
    public ModelAndView linkPO(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        List<OLESerialRelatedPODocument> oleSerialRelatedPODocuments = oleSerialReceivingDocument.getOleSerialRelatedPODocuments();
        for (OLESerialRelatedPODocument oleSerialRelatedPODocument : oleSerialRelatedPODocuments) {
            if (oleSerialRelatedPODocument.isSelectPO()) {
                oleSerialReceivingDocument.setPoId(oleSerialRelatedPODocument.getPoId());
                oleSerialReceivingDocument.setPoIdLink(oleSerialRelatedPODocument.getPoIdLink());
                oleSerialReceivingDocument.setVendorId(oleSerialRelatedPODocument.getVendorId());
                oleSerialReceivingDocument.setVendorAliasName(oleSerialRelatedPODocument.getVendorAliasName());
                oleSerialReceivingDocument.setVendorName(oleSerialRelatedPODocument.getVendorName());
                oleSerialReceivingDocument.setActionInterval(oleSerialRelatedPODocument.getActionInterval());
                break;
            }
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=noLinkPO")
    public ModelAndView noLinkPO(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                 HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=linkPOErrMsg")
    public ModelAndView linkPOErrMsg(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        oleSerialReceivingDocument.setSerialPOErrMsg("Select At least One PO");
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=refreshReceivingRecordType")
    public ModelAndView refreshReceivingRecordType(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                   HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
        oleSerialReceivingService.readReceivingRecordType(oleSerialReceivingDocument);
        oleSerialReceivingService.updateEnumCaptionValues(oleSerialReceivingDocument, null);
        return getUIFModelAndView(oleSerialReceivingForm);
    }


    /**
     * This method assigns specified document to the selector.
     */
    private void assignActionRequests(String routeHeaderId) {
        LOG.debug("Inside assignActionRequests");
        Timestamp currentTime = SpringContext.getBean(DateTimeService.class).getCurrentTimestamp();
        OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
        String principalId = oleSerialReceivingService.getParameter("SAVE_ACTION_USR");
        // TODO Need to check principalId from DB.
        if (principalId == null || principalId.isEmpty()) {
            principalId = ConfigContext.getCurrentContextConfig().getProperty("save.action.usr");
        }
        ActionListService actionListSrv = KEWServiceLocator.getActionListService();
        ActionRequestService actionReqSrv = KEWServiceLocator.getActionRequestService();
        List<ActionRequestValue> actionReqValues = actionReqSrv.findAllPendingRequests(routeHeaderId);
        for (ActionRequestValue actionRequest : actionReqValues) {
            List<ActionItem> actionItems = actionRequest.getActionItems();
            for (ActionItem actionItem : actionItems) {
                actionItem.setPrincipalId(principalId);
                actionItem.setDateAssigned(currentTime);
                actionListSrv.saveActionItem(actionItem);
            }
            actionRequest.setPrincipalId(principalId);
            actionRequest.setCreateDate(currentTime);
            actionReqSrv.saveActionRequest(actionRequest);
        }
        LOG.debug("Leaving assignActionRequests");
    }

    @RequestMapping(params = "methodToCall=saveEnumerationChronology")
    public ModelAndView saveEnumerationChronology(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                      HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        for (OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingDocument.getOleSerialReceivingHistoryList()) {
            if (StringUtils.isNotBlank(oleSerialReceivingHistory.getSerialReceivingRecordHistoryId())
                    && StringUtils.isNotBlank(oleSerialReceivingDocument.getSerialReceiptHistoryId()) ) {
                if (oleSerialReceivingHistory.getSerialReceivingRecordHistoryId().equalsIgnoreCase(oleSerialReceivingDocument.getSerialReceiptHistoryId())) {
                    oleSerialReceivingService.updateEnumerationAndChronologyValues(oleSerialReceivingDocument,oleSerialReceivingHistory);
                    oleSerialReceivingService.setEnumerationAndChronologyValues(oleSerialReceivingHistory);
                }
            }
        }
        try {
           if(CollectionUtils.isNotEmpty(oleSerialReceivingDocument.getOleSerialReceivingHistoryList())){
               getBusinessObjectService().save(oleSerialReceivingDocument.getOleSerialReceivingHistoryList());
           }
        } catch (Exception ex){
            throw ex;
        }

        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=resetEnumerationAndChronology")
    public ModelAndView resetEnumerationAndChronology(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) {
        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=setEnumerationChronology")
    public ModelAndView setEnumerationChronology(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                  HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingService oleSerialReceivingService = new OLESerialReceivingServiceImpl();
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        for (OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingDocument.getOleSerialReceivingHistoryList()) {
            if (StringUtils.isNotBlank(oleSerialReceivingHistory.getSerialReceivingRecordHistoryId())
                    && StringUtils.isNotBlank(oleSerialReceivingDocument.getSerialReceiptHistoryId()) ) {
                if (oleSerialReceivingHistory.getSerialReceivingRecordHistoryId().equalsIgnoreCase(oleSerialReceivingDocument.getSerialReceiptHistoryId())) {
                    oleSerialReceivingService.setEnumerationAndChronologyValues(oleSerialReceivingDocument,oleSerialReceivingHistory);
                }
            }
        }
        return getUIFModelAndView(oleSerialReceivingForm);
    }
    private boolean isValidLocation(String location) {
        List<String> locationList = LocationValuesBuilder.retrieveLocationDetailsForSuggest(location);
        if (locationList != null && locationList.size() > 0) {
            for (String locationValue : locationList) {
                if (locationValue.equalsIgnoreCase(location)) {
                    return true;
                }
            }
        }
        return false;
    }

    @RequestMapping(params = "methodToCall=loadHistoryRecords")
    public ModelAndView loadHistoryRecords(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                                HttpServletRequest request, HttpServletResponse response) {
        OLESerialReceivingForm oleSerialReceivingForm = (OLESerialReceivingForm) form;
        OLESerialReceivingDocument oleSerialReceivingDocument = (OLESerialReceivingDocument) oleSerialReceivingForm.getDocument();
        oleSerialReceivingDocument.setCurrentActionPerformed("");
        return getUIFModelAndView(oleSerialReceivingForm);
    }

    @RequestMapping(params = "methodToCall=approve")
    public ModelAndView approve(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) throws Exception {
        performWorkflowAction(form, UifConstants.WorkflowAction.APPROVE, true);

        return getUIFModelAndView(form);
    }


}
