package org.kuali.ole.service.impl;


import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.OLESruItemHandler;
import org.kuali.ole.deliver.bo.FeeType;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.pojo.OLESruItem;
import org.kuali.ole.service.OleCirculationPolicyService;
import org.kuali.ole.service.OleCirculationPolicyServiceImpl;
import org.kuali.ole.service.OleLoanDocumentWebService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.engine.EngineResults;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User:
 * Date: 7/10/13
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleLoanDocumentWebServiceImpl implements OleLoanDocumentWebService {

    private BusinessObjectService businessObjectService;
    private OLESruItemHandler oleSruItemHandler;
    private DocstoreUtil docstoreUtil;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;
    private LoanProcessor loanProcessor;
    private OleCirculationPolicyService oleCirculationPolicyService;
    private CircDeskLocationResolver circDeskLocationResolver;

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public BusinessObjectService getBusinessObjectService(){
        if(businessObjectService==null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public OLESruItemHandler getOleSruItemHandler(){
        if(oleSruItemHandler==null){
            oleSruItemHandler = new OLESruItemHandler();
        }
        return oleSruItemHandler;
    }

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperServiceImpl(){
        if(oleDeliverRequestDocumentHelperService==null){
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }
    public LoanProcessor getLoanProcessor(){
        if(loanProcessor==null){
            loanProcessor = new LoanProcessor();
        }
        return loanProcessor;
    }

    public DocstoreUtil getDocstoreUtil() {
        if(docstoreUtil == null){
            docstoreUtil = new DocstoreUtil();
        }
        return docstoreUtil;
    }

    public OleCirculationPolicyService getOleCirculationPolicyService() {
        if (null == oleCirculationPolicyService) {
            oleCirculationPolicyService = SpringContext.getBean(OleCirculationPolicyServiceImpl.class);
        }
        return oleCirculationPolicyService;
    }

    public String getItemDueDate(String itemUuId) {
        String dueDate = "";
        try {
            HashMap<String, String> loanMap = new HashMap<String, String>();
            loanMap.put(OLEConstants.ITEM_UUID, itemUuId);
            List<OleLoanDocument> oleLoanDocument = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
            if (oleLoanDocument != null && oleLoanDocument.size() > 0) {
                if (oleLoanDocument.get(0).getLoanDueDate() != null) {
                    dueDate = oleLoanDocument.get(0).getLoanDueDate().toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dueDate;
    }

    public boolean getItemRenewable(String itemUuId, String itemType, String location) {
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        boolean errorMessage = false;
        try {
            OleDeliverRequestBo oleDeliverRequestBo = getLoanProcessor().getPrioritizedRequest(itemUuId);
            if (oleDeliverRequestBo != null) {
                errorMessage = false;
                return errorMessage;
            } else {
                HashMap<String, String> loanMap = new HashMap<String, String>();
                loanMap.put(OLEConstants.ITEM_UUID, itemUuId);
                List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
                loanMap.put(OLEConstants.ITEM_UUID, itemUuId);
                if (oleLoanDocuments != null && oleLoanDocuments.size() > 0) {
                    HashMap<String, String> patronMap = new HashMap<String, String>();
                    String patronId = oleLoanDocuments.get(0).getPatronId();
                    String itemId = oleLoanDocuments.get(0).getItemId();
                    patronMap.put(OLEConstants.OleDeliverRequest.PATRON_ID, patronId);
                    List<OlePatronDocument> olePatronDocuments = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
                    if (olePatronDocuments != null && olePatronDocuments.size() > 0) {
                        Map<String, Object> detailMap = getOleDeliverRequestDocumentHelperServiceImpl().retrieveBIbItemHoldingData(oleLoanDocuments.get(0).getItemUuid());
                        OleHoldings oleHoldings = (OleHoldings) detailMap.get(OLEConstants.HOLDING);
                        org.kuali.ole.docstore.common.document.Item item = (org.kuali.ole.docstore.common.document.Item) detailMap.get(OLEConstants.DOCUMENT_ITEM);
                        String itemLocation = null;
                        if (item.getLocation() == null || (item.getLocation() != null && item.getLocation().trim().isEmpty())) {
                            itemLocation = getDocstoreUtil().getLocation(oleHoldings.getLocation(), new StringBuffer(""));
                        } else {
                            itemLocation = item.getLocation();
                        }
                        Map<String, String> locationMap = getCircDeskLocationResolver().getLocationMap(itemLocation);
                        String borrowerType = olePatronDocuments.get(0).getOleBorrowerType().getBorrowerTypeCode();
                        String numberOfClaimsReturned = String.valueOf(olePatronDocuments.get(0).getNumberOfClaimsReturned());
                        String numberOfRenewals = oleLoanDocuments.get(0).getNumberOfRenewals();
                        Integer noOfRenewals = Integer.parseInt(numberOfRenewals);
                        String digitRoutine = getLoanProcessor().getParameter(OLEParameterConstants.ITEM_DIGIT_ROUTINE);
                        Integer overdueFineAmt = 0;
                        Integer replacementFeeAmt = 0;
                        Integer serviceFeeAmt = 0;
                        DateFormat formatter = new SimpleDateFormat(OLEConstants.DDMMYYYYHHMMSS);
                        Date loanDueDate = oleLoanDocuments.get(0).getLoanDueDate() != null ? new Date(oleLoanDocuments.get(0).getLoanDueDate().getTime()) : null;
                        String dateToString = oleLoanDocuments.get(0).getLoanDueDate() != null ? formatter.format(oleLoanDocuments.get(0).getLoanDueDate()) : "null";
                        List<FeeType> feeTypeList = getOleCirculationPolicyService().getPatronBillPayment(oleLoanDocuments.get(0).getPatronId());
                        if (feeTypeList != null & feeTypeList.size() > 0) {
                            for (FeeType feeType : feeTypeList) {
                                Integer fineAmount = feeType.getFeeAmount().subtract(feeType.getPaidAmount()).intValue();
                                overdueFineAmt += feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.OVERDUE_FINE) ? fineAmount : 0;
                                replacementFeeAmt += feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.REPLACEMENT_FEE) ? fineAmount : 0;
                                serviceFeeAmt += feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.SERVICE_FEE) ? fineAmount : 0;
                            }
                        }
                        dataCarrierService.addData(OLEConstants.GROUP_ID, OLEConstants.HUNDRED);
                        dataCarrierService.removeData(patronId+itemId);
                        HashMap keyLoanMap=new HashMap();
                        keyLoanMap=oleCirculationPolicyService.getLoanedKeyMap(patronId,false);
                        List<Integer> listOfOverDueDays = (List<Integer>)keyLoanMap.get(OLEConstants.LIST_OF_OVERDUE_DAYS);
                        dataCarrierService.addData(OLEConstants.LIST_OVERDUE_DAYS, listOfOverDueDays);
                        dataCarrierService.addData(OLEConstants.LIST_RECALLED_OVERDUE_DAYS, (List<Integer>) keyLoanMap.get(OLEConstants.LIST_RECALLED_OVERDUE_DAYS));
                        HashMap<String, Object> termValues = new HashMap<String, Object>();
                        termValues.put(OLEConstants.BORROWER_TYPE, borrowerType);
                        termValues.put(OLEConstants.ITEM_TYPE, itemType);
                        termValues.put(OLEConstants.ITEM_SHELVING, locationMap.get(OLEConstants.ITEM_SHELVING));
                        termValues.put(OLEConstants.ITEM_COLLECTION, locationMap.get(OLEConstants.ITEM_COLLECTION));
                        termValues.put(OLEConstants.ITEM_LIBRARY, locationMap.get(OLEConstants.ITEM_LIBRARY));
                        termValues.put(OLEConstants.ITEM_CAMPUS, locationMap.get(OLEConstants.ITEM_CAMPUS));
                        termValues.put(OLEConstants.ITEM_INSTITUTION, locationMap.get(OLEConstants.ITEM_INSTITUTION));
                        termValues.put(OLEConstants.DIGIT_ROUTINE, digitRoutine);
                        termValues.put(OLEConstants.ITEMS_DUE_DATE_STRING, dateToString);
                        termValues.put(OLEConstants.NUM_CLAIMS_RETURNED, numberOfClaimsReturned);
                        termValues.put(OLEConstants.OVERDUE_FINE_AMT, overdueFineAmt);
                        termValues.put(OLEConstants.REPLACEMENT_FEE_AMT, replacementFeeAmt);
                        termValues.put(OLEConstants.ALL_CHARGES, overdueFineAmt + replacementFeeAmt + serviceFeeAmt);
                        termValues.put(OLEConstants.NUM_RENEWALS, noOfRenewals);
                        termValues.put(OLEConstants.ITEMS_DUE_DATE, loanDueDate);
                        termValues.put(OLEConstants.PATRON_ID_POLICY, patronId);
                        termValues.put(OLEConstants.ITEM_ID_POLICY, itemId);
                        EngineResults engineResults = getLoanProcessor().getEngineResults(OLEConstants.RENEWAL_AGENDA_NM, termValues);
                        dataCarrierService.removeData(patronId+itemId);
                        List<String> errorMessages = (List<String>) engineResults.getAttribute(OLEConstants.ERROR_ACTION);
                        HashMap<String, String> errorsAndPermission = (HashMap<String, String>) engineResults.getAttribute(OLEConstants.ERRORS_AND_PERMISSION);
                        dataCarrierService.addData(OLEConstants.ERROR_ACTION, null);
                        dataCarrierService.addData(OLEConstants.ERRORS_AND_PERMISSION, null);
                        if ((errorMessages != null && errorMessages.size() > 0) || (errorsAndPermission != null && errorsAndPermission.size() > 0)) {
                            errorMessage = false;
                        }else{
                            errorMessage=true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorMessage;
    }

    @Override
    public String getItemInformation(String itemUUID, String itemType, String itemShelvingLocation ,String shelvingLocation, String localLocation) {
        OLESruItem oleSruItem = new OLESruItem();
       // oleSruItem.setItemDueDate(getItemDueDate(itemUUID));
      //  oleSruItem.setRenewable(getItemRenewable(itemUUID,itemType,itemShelvingLocation));
        if(StringUtils.isNotBlank(shelvingLocation)){
            oleSruItem.setShelvingLocation(getLocationName(shelvingLocation));
        }
        if(StringUtils.isNotBlank(localLocation)){
            oleSruItem.setLocalLocation(getLocationName(localLocation));
        }
        return getOleSruItemHandler().generateXmlFromObject(oleSruItem);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getLocationName(String locationCode) {
        String locationName = "";
        Map<String, String> locationMap = new HashMap<String, String>();
        locationMap.put(OLEConstants.LOC_CD, locationCode);
        List<OleLocation> oleLocationList = (List<OleLocation>) getBusinessObjectService().findMatching(OleLocation.class, locationMap);
        if (oleLocationList.size() > 0) {
            locationName = oleLocationList.get(0).getLocationName();
        }
        return locationName;
    }


}
