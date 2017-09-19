package org.kuali.ole.deliver.rule;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.kuali.asr.ASRConstants;
import org.kuali.asr.service.ASRHelperServiceImpl;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.ASRTypeRequest;
import org.kuali.ole.deliver.bo.OLEDeliverNotice;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleCirculationHistory;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.notice.service.OleNoticeService;
import org.kuali.ole.deliver.notice.service.impl.OleNoticeServiceImpl;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/26/12
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverRequestDocumentRule extends MaintenanceDocumentRuleBase {

      private ASRHelperServiceImpl asrHelperService = getAsrHelperService();
      private LoanProcessor loanProcessor =getLoanProcessor();
      private OleNoticeService oleNoticeService = getOleNoticeService();
      private BusinessObjectService businessObjectService;

    public ASRHelperServiceImpl getAsrHelperService(){
        if(asrHelperService == null ){
            asrHelperService = new ASRHelperServiceImpl();
        }
        return asrHelperService;
    }

    public LoanProcessor getLoanProcessor(){
        if(loanProcessor == null){
            loanProcessor = new LoanProcessor();
        }
        return loanProcessor;
    }

    public OleNoticeService getOleNoticeService(){
        if(oleNoticeService == null){
            oleNoticeService = new OleNoticeServiceImpl();
        }
        return oleNoticeService;
    }

    public BusinessObjectService getBusinessObjectService() {
          if(businessObjectService == null){
              businessObjectService = KRADServiceLocator.getBusinessObjectService();
          }
        return businessObjectService;
    }


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        OleDeliverRequestBo oleDeliverRequestBo = (OleDeliverRequestBo) document.getNewMaintainableObject().getDataObject();
        OleDeliverRequestBo oldDeliverRequestBo = (OleDeliverRequestBo)document.getOldMaintainableObject().getDataObject();
        boolean processed = super.processCustomSaveDocumentBusinessRules(document);
        Map<String, String> patronMap = new HashMap<String, String>();
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        patronMap.put(OLEConstants.OleDeliverRequest.PATRON_BARCODE, oleDeliverRequestBo.getBorrowerBarcode());
        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) businessObjectService.findMatching(OlePatronDocument.class, patronMap);
        if(olePatronDocumentList==null || olePatronDocumentList.size()==0){
            GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.INVALID_PATRON_BARCODE);
            return false;
        }
        else{
            oleDeliverRequestBo.setOlePatron(olePatronDocumentList.get(0));
        }
        OleDeliverRequestDocumentHelperServiceImpl service = new OleDeliverRequestDocumentHelperServiceImpl();
        if (processed) {
            if (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("3") || oleDeliverRequestBo.getRequestTypeId().equals("5")) {
                if (!oleDeliverRequestBo.getOlePatron().isDeliveryPrivilege()) {
                    if (oleDeliverRequestBo.getPickUpLocationId() != null) {
                        if (oleDeliverRequestBo.getRequestTypeId().equals("1")) {
                            oleDeliverRequestBo.setRequestTypeId("2");
                        } else if (oleDeliverRequestBo.getRequestTypeId().equals("3")) {
                            oleDeliverRequestBo.setRequestTypeId("4");
                        } else if (oleDeliverRequestBo.getRequestTypeId().equals("5")) {
                            oleDeliverRequestBo.setRequestTypeId("6");
                        }
                    }
                }
            } else if (oleDeliverRequestBo.getRequestTypeId().equals("2") || oleDeliverRequestBo.getRequestTypeId().equals("4") || oleDeliverRequestBo.getRequestTypeId().equals("6")) {

                if (oleDeliverRequestBo.getOlePatron().isDeliveryPrivilege() && oleDeliverRequestBo.getPickUpLocationId() == null) {
                    if (oleDeliverRequestBo.getRequestTypeId().equals("2")) {
                        oleDeliverRequestBo.setRequestTypeId("1");
                    } else if (oleDeliverRequestBo.getRequestTypeId().equals("4")) {
                        oleDeliverRequestBo.setRequestTypeId("3");
                    } else if (oleDeliverRequestBo.getRequestTypeId().equals("6")) {
                        oleDeliverRequestBo.setRequestTypeId("5");
                    }
                }else if(!oleDeliverRequestBo.getOlePatron().isDeliveryPrivilege() && oleDeliverRequestBo.getPickUpLocationId() == null){
                    GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.NO_DELIVERY_PRIVILEGE);
                    return false;
                }
            }
            if (oleDeliverRequestBo.getOperatorModifiedId() == null) {
               service.reOrderQueuePosition(oleDeliverRequestBo);
                String itemId=oleDeliverRequestBo.getItemId();
                Map<String,String> map=new HashMap<String,String>();
                map.put(OLEConstants.OleDeliverRequest.ITEM_ID,itemId);
                map.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID,OLEConstants.REQUEST_RECAL_DELIVERY);
                List<OleDeliverRequestBo> oleDeliverRequestBoList1=(List<OleDeliverRequestBo>)businessObjectService.findMatching(OleDeliverRequestBo.class,map);
                map.clear();
                map.put(OLEConstants.OleDeliverRequest.ITEM_ID,itemId);
                map.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID,OLEConstants.REQUEST_RECAL_HOLD);
                List<OleDeliverRequestBo> oleDeliverRequestBoList2=(List<OleDeliverRequestBo>)businessObjectService.findMatching(OleDeliverRequestBo.class,map);
                if (oleDeliverRequestBo.getNoticeType() != null && oleDeliverRequestBo.getNoticeType().equalsIgnoreCase(OLEConstants.NOTICE_RECALL)&&oleDeliverRequestBoList1.size()==0 && oleDeliverRequestBoList2.size()==0) {
                    try {
                        oleDeliverRequestBo = service.generateRecallNotice(oleDeliverRequestBo);
                        oleDeliverRequestBo.setRecallNoticeSentDate(new Date(System.currentTimeMillis()));
                    } catch (Exception e) {
                        LOG.error("Error occured while generating the notices " + e, e);  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
            if (!oleDeliverRequestBo.getRequestTypeId().equals("8")) {
//                oleDeliverRequestBo = service.updateLoanDocument(oleDeliverRequestBo);
                oleDeliverRequestBo.setOlePatron(null);
                oleDeliverRequestBo.setOleProxyPatron(null);
            }
            else {
                service.processRequestType(oleDeliverRequestBo);
            }

            if(asrHelperService.isAnASRItem(oleDeliverRequestBo.getItemLocation()) && oleDeliverRequestBo.getItemStatus().equals(loanProcessor.getParameter(ASRConstants.ASR_REQUEST_ITEM_STATUS))){

                oleDeliverRequestBo.setAsrFlag(true);
                oleDeliverRequestBo.setRequestStatus("1");
                ASRTypeRequest asrTypeRequest = new ASRTypeRequest();
            //    asrTypeRequest.setPatronName(oleDeliverRequestBo.getBorrowerName());
                asrTypeRequest.setPatronId(oleDeliverRequestBo.getBorrowerId());
                asrTypeRequest.setPickUpLocation(oleDeliverRequestBo.getPickUpLocationCode());
                asrTypeRequest.setItemId(oleDeliverRequestBo.getItemId());
                asrTypeRequest.setRequestStatus("1");
                asrTypeRequest.setRequestId(oleDeliverRequestBo.getRequestId());
                businessObjectService.save(asrTypeRequest);
            }
          getOleNoticeService().processNoticeForRequest(oleDeliverRequestBo);
            Map<String,String> criteriaMap = new HashMap<>();
            criteriaMap.put("itemId",oleDeliverRequestBo.getItemId());
            List<OleCirculationHistory> circulationHistoryRecords = (List<OleCirculationHistory>) getBusinessObjectService().findMatching(OleCirculationHistory.class,criteriaMap);
            if(circulationHistoryRecords.size()>0 && oleDeliverRequestBo.getRequestId() != null) {
                for(OleCirculationHistory oleCirculationHistory : circulationHistoryRecords) {
                    if (StringUtils.isBlank(oleCirculationHistory.getOleRequestId())) {
                        oleCirculationHistory.setOleRequestId(oleDeliverRequestBo.getRequestId());
                        businessObjectService.save(oleCirculationHistory);
                    }
                }
            }
           /* else if (oleDeliverRequestBo.getRequestId() == null || circulationHistoryRecords.size()==0){
                List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, criteriaMap);
                if (oleLoanDocuments.size() > 0) {
                    for (OleLoanDocument oleLoanDocument : oleLoanDocuments) {
                        Map<String,String> loanCriteriaMap = new HashMap<>();
                        loanCriteriaMap.put("loanId", oleLoanDocument.getLoanId());
                        circulationHistoryRecords = (List<OleCirculationHistory>) getBusinessObjectService().findMatching(OleCirculationHistory.class, loanCriteriaMap);
                        if(circulationHistoryRecords.size()>0) {
                            for(OleCirculationHistory oleCirculationHistory : circulationHistoryRecords) {
                                if (StringUtils.isBlank(oleCirculationHistory.getOleRequestId())) {
                                    oleCirculationHistory.setOleRequestId(oleLoanDocument.getOleRequestId());
                                    businessObjectService.save(oleCirculationHistory);
                                }

                            }

                        }
                    }
                }
            }*/
        }

        if((oleDeliverRequestBo.getHoldExpirationDate()!=null && oldDeliverRequestBo.getHoldExpirationDate() == null) ||
                 (oleDeliverRequestBo.getHoldExpirationDate()==null && oldDeliverRequestBo.getHoldExpirationDate() !=null) ||
                (oleDeliverRequestBo.getHoldExpirationDate()!=null && oldDeliverRequestBo.getHoldExpirationDate()!=null && oleDeliverRequestBo.getHoldExpirationDate().compareTo(oldDeliverRequestBo.getHoldExpirationDate())!=0)
                ){
                 if(oleDeliverRequestBo.getDeliverNotices()!=null){
                    for(OLEDeliverNotice oleDeliverNotice:oleDeliverRequestBo.getDeliverNotices()){
                        if(oleDeliverNotice.getNoticeType().equals(OLEConstants.ONHOLD_EXPIRATION_NOTICE)){
                            oleDeliverNotice.setNoticeToBeSendDate(new Timestamp(oleDeliverRequestBo.getHoldExpirationDate().getTime()));
                        }else if(oleDeliverNotice.getNoticeType().equals(OLEConstants.ONHOLD_COURTESY_NOTICE)){
                            int noOfDaysBefore  = Integer.parseInt(ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.OleDeliverRequest.ONHOLD_COURTESY_NOTICE_INTERVAL));
                            oleDeliverNotice.setNoticeToBeSendDate(new Timestamp((oleDeliverRequestBo.getHoldExpirationDate().getTime() - (noOfDaysBefore* 24 * 3600 * 1000l))));
                        }
                    }
                }
            }
        return processed;
    }
}
