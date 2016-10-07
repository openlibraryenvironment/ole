package org.kuali.ole.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.*;
import org.kuali.ole.batch.controller.OLEBatchProcessProfileController;
import org.kuali.ole.coa.businessobject.*;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.ingest.LineItemOrderMatcherForBib;
import org.kuali.ole.ingest.OleTxRecordBuilder;
import org.kuali.ole.module.purap.businessobject.*;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;
import org.kuali.ole.pojo.edi.EDIOrder;
import org.kuali.ole.pojo.edi.LineItemOrder;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.businessobject.OLERequestorPatronDocument;
import org.kuali.ole.select.businessobject.OleFormatType;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.select.document.service.OleSelectDocumentService;
import org.kuali.ole.service.OleOrderRecordService;
import org.kuali.ole.sys.businessobject.Building;
import org.kuali.ole.sys.businessobject.Room;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.*;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 2/28/13
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleOrderRecordServiceImpl implements OleOrderRecordService {


    private DocstoreClientLocator docstoreClientLocator;
    private BusinessObjectService businessObjectService;
    private OleSelectDocumentService oleSelectDocumentService;
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleOrderRecordServiceImpl.class);
    private OleDocstoreHelperService oleDocstoreHelperService;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService==null)
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        return businessObjectService;
    }

    public OleSelectDocumentService getOleSelectDocumentService() {
        if (oleSelectDocumentService == null) {
            oleSelectDocumentService = SpringContext.getBean(OleSelectDocumentService.class);
        }
        return oleSelectDocumentService;
    }

    public OleDocstoreHelperService getOleDocstoreHelperService() {
        if (oleDocstoreHelperService == null) {
            oleDocstoreHelperService = SpringContext.getBean(OleDocstoreHelperService.class);
        }
        return oleDocstoreHelperService;
    }

    private Map<String,String> fundDetails = new HashMap<String,String>();
    private List<String> failureRecords = new ArrayList<>();

    public LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }
    //DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);

    @Override
    public OleOrderRecord fetchOleOrderRecordForMarcEdi(String bibId,EDIOrder ediOrder,BibMarcRecord bibMarcRecord,int recordPosition, OLEBatchProcessJobDetailsBo job) throws Exception{
        LOG.debug("----Inside fetchOleOrderRecordForMarcEdi()------------------------------");
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        OleOrderRecord oleOrderRecord = new OleOrderRecord();
        oleOrderRecord.setOriginalRecord(bibMarcRecord);
        oleOrderRecord.setOriginalEdi(ediOrder);
        boolean validBFNFlag=isValidBFN(ediOrder,bibMarcRecord);
        if (validBFNFlag) {
            OleTxRecord oleTxRecord = getMatchingTxRecord(ediOrder, bibMarcRecord);
            oleTxRecord.setItemType(OLEConstants.ITEM_TYP);
            oleTxRecord.setAccountNumber(fundDetails.keySet().iterator().next());
            oleTxRecord.setObjectCode(fundDetails.get(oleTxRecord.getAccountNumber()));
            Map<String,String> accLinesMap = new HashMap<>();
            accLinesMap.put(OLEConstants.ACCOUNT_NUMBER,oleTxRecord.getAccountNumber());
            List<Account> accountList = (List) getBusinessObjectService().findMatching(Account.class, accLinesMap);
            if(accountList != null && accountList.size() > 0){
                oleTxRecord.setItemChartCode(accountList.get(0).getChartOfAccountsCode());
            }
            fundDetails.clear();
            setDefaultAndConstantValuesToTxnRecord(oleTxRecord,null,job);
            setAccountingLinesDetails(oleTxRecord,recordPosition,job);
            validateChartCodeAndOrgCodeCombination(oleTxRecord.getChartCode(),oleTxRecord.getOrgCode(),recordPosition,job);
            validateDeliveryDetails(oleTxRecord.getBuildingCode(), oleTxRecord.getDeliveryCampusCode(), recordPosition, oleTxRecord.getDeliveryBuildingRoomNumber(), job);
            //validateVendorCustomerNumber(oleTxRecord);
            oleTxRecord.setRequisitionSource(OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST);
            oleOrderRecord.setOleTxRecord(oleTxRecord);
            oleOrderRecord.addMessageToMap(OLEConstants.IS_VALID_BFN,validBFNFlag);
        }  else {
            oleOrderRecord.addMessageToMap(OLEConstants.IS_VALID_BFN,OLEConstants.FALSE);
            if(bibId!=null){
                try{
                    getDocstoreClientLocator().getDocstoreClient().deleteBib(bibId);
                }catch (Exception e){
                    LOG.error("Exception while deleting bib " +bibId);
                }
            }
            OleTxRecord oleTxRecord = getMatchingTxRecord(ediOrder, bibMarcRecord);
            setDefaultAndConstantValuesToTxnRecord(oleTxRecord,null,job);
            setAccountingLinesDetails(oleTxRecord,recordPosition,job);
            validateChartCodeAndOrgCodeCombination(oleTxRecord.getChartCode(),oleTxRecord.getOrgCode(),recordPosition,job);
            validateDeliveryDetails(oleTxRecord.getBuildingCode(), oleTxRecord.getDeliveryCampusCode(), recordPosition, oleTxRecord.getDeliveryBuildingRoomNumber(), job);
            //validateVendorCustomerNumber(oleTxRecord);
            oleTxRecord.setRequisitionSource(OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST);
            oleOrderRecord.setOleTxRecord(oleTxRecord);
        }
        //dataCarrierService.addData(OLEConstants.OLE_TX_RECORD,oleOrderRecord.getOleTxRecord());
        orderImportHelperBo.setOleTxRecord(oleOrderRecord.getOleTxRecord());
        LOG.debug("----End of fetchOleOrderRecordForMarcEdi()------------------------------");
        return oleOrderRecord;
    }


    @Override
    public OleOrderRecord fetchOleOrderRecordForMarc(String bibId, BibMarcRecord bibMarcRecord,int recordPosition, OLEBatchProcessJobDetailsBo job) throws Exception{
        LOG.debug("----Inside fetchOleOrderRecordForMarc()------------------------------");
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        OleOrderRecord oleOrderRecord = new OleOrderRecord();
        oleOrderRecord.setOriginalRecord(bibMarcRecord);
        OleTxRecord oleTxRecord = new OleTxRecord();
        oleTxRecord.setBibId(bibId);
        oleTxRecord.setItemType(OLEConstants.ITEM_TYP);
        Map<String,String> failureMsges = new HashMap<>();
        failureMsges = mapDataFieldsToTxnRecord(oleTxRecord, bibMarcRecord,recordPosition, job);
        failureMsges = setDefaultAndConstantValuesToTxnRecord(oleTxRecord,failureMsges, job);
        validateChartCodeAndOrgCodeCombination(oleTxRecord.getChartCode(),oleTxRecord.getOrgCode(),recordPosition, job);
        validateDeliveryDetails(oleTxRecord.getBuildingCode(), oleTxRecord.getDeliveryCampusCode(), recordPosition, oleTxRecord.getDeliveryBuildingRoomNumber(), job);
        //validateVendorCustomerNumber(oleTxRecord);
        Collection failureList = failureMsges.values();
        if(failureList != null && failureList.size() > 0){
            //List reasonForFailure = (List) dataCarrierService.getData(OLEConstants.FAILURE_REASON);
            List reasonForFailure = orderImportHelperBo.getFailureReason();
            if(reasonForFailure != null){
                reasonForFailure.addAll(failureList);
                //dataCarrierService.addData(OLEConstants.FAILURE_REASON,reasonForFailure);
                orderImportHelperBo.setFailureReason(reasonForFailure);
                failureList.clear();
                failureRecords.clear();
            }
        }
        if (null == oleTxRecord.getRequisitionSource()) {
            oleTxRecord.setRequisitionSource(OleSelectConstant.REQUISITON_SRC_TYPE_AUTOINGEST);
        }
        if(oleTxRecord.getOrderType() == null){
            oleTxRecord.setOrderType(OLEConstants.ORDER_TYPE_VALUE);
        }
        setAccountingLinesDetails(oleTxRecord,recordPosition,job);
        oleOrderRecord.setOleTxRecord(oleTxRecord);
        //dataCarrierService.addData(OLEConstants.OLE_TX_RECORD,oleOrderRecord.getOleTxRecord());
        orderImportHelperBo.setOleTxRecord(oleOrderRecord.getOleTxRecord());
        LOG.debug("----End of fetchOleOrderRecordForMarc()------------------------------");
        return oleOrderRecord;
    }

    /**
     * This method populate the value for vendor profile code from profile.
     * @param oleBatchProcessProfileBo
     * @return Vendor Profile Code.
     */
    private String getValueForVendorProfileCodeFromProfile(OLEBatchProcessProfileBo oleBatchProcessProfileBo){
        LOG.debug("----Inside getValueForVendorProfileCodeFromProfile()------------------------------");
        List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsBoList = oleBatchProcessProfileBo.getOleBatchProcessProfileMappingOptionsList();
        for (OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo : oleBatchProcessProfileMappingOptionsBoList) {
            List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = oleBatchProcessProfileMappingOptionsBo.getOleBatchProcessProfileDataMappingOptionsBoList();
            for (int dataMapCount = 0;dataMapCount<oleBatchProcessProfileDataMappingOptionsBoList.size();dataMapCount++) {
                String sourceField = oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getSourceField();
                String sourceFields[] = sourceField.split("\\$");
                if (sourceFields.length == 2) {
                    if (OLEConstants.OLEBatchProcess.VENDOR_PROFILE_CODE.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                        return oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationFieldValue();
                    }
                }
            }
        }
        List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsBoList = oleBatchProcessProfileBo.getOleBatchProcessProfileConstantsList();
        for (OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo : oleBatchProcessProfileConstantsBoList) {
            if (StringUtils.isNotBlank(oleBatchProcessProfileConstantsBo.getDataType()) && OLEConstants.OLEBatchProcess.ORDER_IMPORT.equalsIgnoreCase(oleBatchProcessProfileConstantsBo.getDataType())
                    && StringUtils.isNotBlank(oleBatchProcessProfileConstantsBo.getAttributeValue()) && StringUtils.isNotBlank(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                if (OLEConstants.OLEBatchProcess.CONSTANT.equals(oleBatchProcessProfileConstantsBo.getDefaultValue())) {
                    if (OLEConstants.OLEBatchProcess.VENDOR_PROFILE_CODE.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        return oleBatchProcessProfileConstantsBo.getAttributeValue();
                    }
                }
                else if (OLEConstants.OLEBatchProcess.DEFAULT.equals(oleBatchProcessProfileConstantsBo.getDefaultValue())) {
                    if (OLEConstants.OLEBatchProcess.VENDOR_PROFILE_CODE.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        return oleBatchProcessProfileConstantsBo.getAttributeValue();
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method is for checking the availability of chart code and org code combination.
     * @param chartCode
     * @param orgCode
     */
    private void validateChartCodeAndOrgCodeCombination(String chartCode,String orgCode,int recordPosition, OLEBatchProcessJobDetailsBo job){
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        Map<String,String> orgCodeMap = new HashMap<>();
        orgCodeMap.put(OLEConstants.OLEBatchProcess.ORGANIZATION_CODE, orgCode);
        orgCodeMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, chartCode);
        List<Organization> organizationCodeList = (List) getBusinessObjectService().findMatching(Organization.class, orgCodeMap);
        if(organizationCodeList == null || organizationCodeList.size() == 0){
            //List<String> reasonForFailure = (List<String>)dataCarrierService.getData(OLEConstants.FAILURE_REASON);
            List<String> reasonForFailure = orderImportHelperBo.getFailureReason();
            if(reasonForFailure != null){
                reasonForFailure.add(OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.OLEBatchProcess.INVALID_ORGCD_CHARTCD_COMBINATION);
                //dataCarrierService.addData(OLEConstants.FAILURE_REASON,reasonForFailure);
                orderImportHelperBo.setFailureReason(reasonForFailure);
            }
        }
    }

    private void validateDeliveryDetails(String buildingCode, String campusCode, int recordPosition, String deliveryBuildingRoomNumber, OLEBatchProcessJobDetailsBo job){
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        Map<String,String> deliveryMap = new HashMap<>();
        deliveryMap.put(OLEConstants.OLEBatchProcess.BUILDING_CODE, buildingCode);
        deliveryMap.put(OLEConstants.OLEBatchProcess.CAMPUS_CODE, campusCode);
        deliveryMap.put(OLEConstants.BUILDING_ROOM_NUMBER, deliveryBuildingRoomNumber);
        List<Room> roomList = (List) getBusinessObjectService().findMatching(Room.class, deliveryMap);
        if(roomList == null || roomList.size() == 0){
            //List<String> reasonForFailure = (List<String>)dataCarrierService.getData(OLEConstants.FAILURE_REASON);
            List<String> reasonForFailure = orderImportHelperBo.getFailureReason();
            if(reasonForFailure != null){
                reasonForFailure.add(OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.OLEBatchProcess.INVALID_BLDGCD_CMPCD_ROOM_COMBINATION);
                //dataCarrierService.addData(OLEConstants.FAILURE_REASON, reasonForFailure);
                orderImportHelperBo.setFailureReason(reasonForFailure);
            }
        }
    }

    private void validateVendorCustomerNumber(OleTxRecord oleTxRecord, OLEBatchProcessJobDetailsBo job){
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        String[] vendorDetail = oleTxRecord.getVendorNumber().split("-");
        String vendorHeaderGeneratedIdentifier = vendorDetail[0];
        String vendorDetailAssignedIdentifier = vendorDetail[1];
        try {
            Map<String,Object> vendorCustomerMap = new HashMap<>();
            vendorCustomerMap.put(OLEConstants.VENDOR_HEADER_GENERATED_ID, vendorHeaderGeneratedIdentifier);
            vendorCustomerMap.put(OLEConstants.VENDOR_DETAILED_ASSIGNED_ID, vendorDetailAssignedIdentifier);
            vendorCustomerMap.put(OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR,oleTxRecord.getVendorInfoCustomer());
            List<VendorCustomerNumber> vendorCustomerList = (List) getBusinessObjectService().findMatching(VendorCustomerNumber.class, vendorCustomerMap);
            if(vendorCustomerList == null || vendorCustomerList.size() == 0){
                List reasonForFailure = new ArrayList();
                reasonForFailure.add(OLEConstants.OLEBatchProcess.INVALID_VNDRNBR_CUSTNBR_COMBINATION);
                //dataCarrierService.addData(OLEConstants.FAILURE_REASON,reasonForFailure);
                orderImportHelperBo.setFailureReason(reasonForFailure);
            }
        }
        catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
    }

    /**
     * This method is for validating BFN value.
     * @param ediOrder
     * @param bibMarcRecord
     * @return
     * @throws Exception
     */
    private boolean isValidBFN(EDIOrder ediOrder, BibMarcRecord bibMarcRecord) throws Exception {
        LOG.debug("----Inside isValidBFN()------------------------------");
        LineItemOrderMatcherForBib lineItemOrderMatcherForBib = new LineItemOrderMatcherForBib();
        String vendorProfileCode = OLEConstants.PROFILE_AGENDA_NM;
        LineItemOrder lineItemOrder = lineItemOrderMatcherForBib.getLineItemOrder(ediOrder.getLineItemOrder(), bibMarcRecord, vendorProfileCode);
        String bfn=null;
        if(lineItemOrder!=null){
            Map<String, String> accountInfo = OleTxRecordBuilder.getInstance().getAccountInfo(lineItemOrder);
            if(accountInfo != null && accountInfo.size()>0) {
                fundDetails.putAll(accountInfo);
            }
            bfn = accountInfo!=null?"1":null;
        }
        if(bfn!=null)
            return  true;
        return false;
    }

    /**
     * This method gets the oleTxRecord based on lineItemOrder,ediOrder.
     * @param ediOrder
     * @param bibMarcRecord
     */
    private OleTxRecord getMatchingTxRecord(EDIOrder ediOrder, BibMarcRecord bibMarcRecord) throws Exception {
        LineItemOrderMatcherForBib lineItemOrderMatcherForBib = new LineItemOrderMatcherForBib();
        String vendorProfileCode = OLEConstants.PROFILE_AGENDA_NM;
        LineItemOrder lineItemOrder = lineItemOrderMatcherForBib.getLineItemOrder(ediOrder.getLineItemOrder(), bibMarcRecord, vendorProfileCode);
        OleTxRecord oleTxRecord = OleTxRecordBuilder.getInstance().build(lineItemOrder, ediOrder);
        return oleTxRecord;
    }

    protected Map<String,String> mapDataFieldsToTxnRecord(OleTxRecord oleTxRecord, BibMarcRecord bibMarcRecord,int recordPosition, OLEBatchProcessJobDetailsBo job) {
        Map<String,String> mappingFailures = new HashMap<String,String>();
        LOG.debug("----Inside mapDataFieldsToTxnRecord()------------------------------");
        String destinationField = null;
        List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsBoList = job.getOrderImportHelperBo().getOleBatchProcessProfileBo().getOleBatchProcessProfileMappingOptionsList();
        for (OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo : oleBatchProcessProfileMappingOptionsBoList) {
            List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = oleBatchProcessProfileMappingOptionsBo.getOleBatchProcessProfileDataMappingOptionsBoList();
            Collections.sort(oleBatchProcessProfileDataMappingOptionsBoList, new Comparator<OLEBatchProcessProfileDataMappingOptionsBo>() {
                @Override
                public int compare(OLEBatchProcessProfileDataMappingOptionsBo obj1, OLEBatchProcessProfileDataMappingOptionsBo obj2) {
                    int result = obj1.getDestinationField().compareTo(obj2.getDestinationField());
                    if(result != 0){
                        return result;
                    }
                    return obj1.getPriority() > obj2.getPriority() ? -1 : obj1.getPriority() < obj2.getPriority() ? 1 : 0;
                }
            });
            List<String> donors=new ArrayList<>();
            List<String> miscellaneousNotes = new ArrayList<>();
            List<String> receiptNotes = new ArrayList<>();
            List<String> requestorNotes = new ArrayList<>();
            List<String> selectorNotes = new ArrayList<>();
            List<String> splProcessInstrNotes = new ArrayList<>();
            List<String> vendorInstrNotes = new ArrayList<>();
            for (int dataMapCount = 0;dataMapCount<oleBatchProcessProfileDataMappingOptionsBoList.size();dataMapCount++) {
                destinationField = oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField();
                String sourceField = oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getSourceField();
                String sourceFields[] = sourceField.split("\\$");
                if (sourceFields.length == 2) {
                    String dataField = sourceFields[0].trim();
                    String tagField = sourceFields[1].trim();
                    if (OLEConstants.OLEBatchProcess.CHART_CODE.equals(destinationField)) {
                        String chartCode = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(chartCode)) {
                            Map<String,String> chartCodeMap = new HashMap<>();
                            chartCodeMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, chartCode);
                            List<Organization> chartCodeList = (List) getBusinessObjectService().findMatching(Organization.class, chartCodeMap);
                            if(chartCodeList!= null && chartCodeList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_CHART_CD + "  " + dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + chartCode);
                                chartCode = null;
                            }
                            oleTxRecord.setChartCode(chartCode);
                        }
                        else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_CHART_CODE + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.ORG_CODE.equals(destinationField)) {
                        String orgCode = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(orgCode)){
                            Map<String,String> orgCodeMap = new HashMap<>();
                            orgCodeMap.put(OLEConstants.OLEBatchProcess.ORGANIZATION_CODE, orgCode);
                            List<Organization> organizationCodeList = (List) getBusinessObjectService().findMatching(Organization.class, orgCodeMap);
                            if(organizationCodeList != null && organizationCodeList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.ORGANIZATION_CODE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_ORG_CD + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + orgCode);
                                orgCode = null;
                            }
                            oleTxRecord.setOrgCode(orgCode);
                        }
                        else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.ORGANIZATION_CODE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_ORG_CODE + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.CONTRACT_MANAGER.equals(destinationField)) {
                        String contractManager = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(contractManager)){
                            Map<String,String> contractManagerMap = new HashMap<>();
                            contractManagerMap.put(OLEConstants.OLEBatchProcess.CONTRACT_MANAGER_NAME, contractManager);
                            List<ContractManager> contractManagerList = (List) getBusinessObjectService().findMatching(ContractManager.class, contractManagerMap);
                            if(contractManagerList != null && contractManagerList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.CONTRACT_MANAGER_NAME, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_CONTRACT_MANAGER_NM + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + contractManager);
                                contractManager = null;
                            }
                            oleTxRecord.setContractManager(contractManager);
                        }
                        else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.CONTRACT_MANAGER_NAME,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_CONTRACT_MGR_NM + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.ASSIGN_TO_USER.equals(destinationField)) {
                        String assignToUser = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(assignToUser)){
                            Person assignedUser = SpringContext.getBean(PersonService.class).getPersonByPrincipalName(assignToUser);
                            if(assignedUser == null){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.ASSIGN_TO_USER,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_ASSIGN_TO_USER + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + assignToUser);
                                assignToUser = null;
                            }
                            oleTxRecord.setAssignToUser(assignToUser);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.ASSIGN_TO_USER,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_ASSIGN_TO_USER + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.ORDER_TYPE.equals(destinationField)) {
                        String orderType = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(orderType)){
                            Map<String,String> orderTypeMap = new HashMap<>();
                            orderTypeMap.put(OLEConstants.OLEBatchProcess.PURCHASE_ORDER_TYPE, orderType);
                            List<PurchaseOrderType> purchaseOrderTypeList = (List) getBusinessObjectService().findMatching(PurchaseOrderType.class, orderTypeMap);
                            if(purchaseOrderTypeList != null && purchaseOrderTypeList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.PURCHASE_ORDER_TYPE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_ORDER_TYPE + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + orderType);
                                orderType = null;
                            }
                            oleTxRecord.setOrderType(orderType);
                        }
                        else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.PURCHASE_ORDER_TYPE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_ORDER_TYPE + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.FUNDING_SOURCE.equals(destinationField)) {
                        String fundingSource = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(fundingSource)){
                            Map<String,String> fundingSourceMap = new HashMap<>();
                            fundingSourceMap.put(OLEConstants.OLEBatchProcess.FUNDING_SOURCE_CODE, fundingSource);
                            List<FundingSource> fundingSourceList = (List) getBusinessObjectService().findMatching(FundingSource.class, fundingSourceMap);
                            if(fundingSourceList != null && fundingSourceList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.FUNDING_SOURCE_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_FUNDING_SOURCE_CD + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + fundingSource);
                                fundingSource = null;
                            }
                            oleTxRecord.setFundingSource(fundingSource);
                        }
                        else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.FUNDING_SOURCE_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_FUNDING_SOURCE+ " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE.equals(destinationField)) {
                        String deliveryCampusCode = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(deliveryCampusCode)){
                            Map<String,String> campusCodeMap = new HashMap<>();
                            campusCodeMap.put(OLEConstants.OLEBatchProcess.CAMPUS_CODE, deliveryCampusCode);
                            List<Building> campusList= (List) getBusinessObjectService().findMatching(Building.class, campusCodeMap);
                            if(campusList != null && campusList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.CAMPUS_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_CAMPUS_CODE + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +  "  " + deliveryCampusCode);
                                deliveryCampusCode = null;
                            }
                            oleTxRecord.setDeliveryCampusCode(deliveryCampusCode);
                        }
                        else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.CAMPUS_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_DELIVERY_CAMPUS_CODE + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.BUILDING_CODE.equals(destinationField)) {
                        String buildingCode = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(buildingCode)){
                            Map<String,String> buildingCodeMap = new HashMap<>();
                            buildingCodeMap.put(OLEConstants.OLEBatchProcess.BUILDING_CODE, buildingCode);
                            List<Building> buildingList= (List) getBusinessObjectService().findMatching(Building.class, buildingCodeMap);
                            if(buildingList != null && buildingList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.BUILDING_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_BUILDING_CD + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +  "  " + buildingCode);
                                buildingCode = null;
                            }
                            oleTxRecord.setBuildingCode(buildingCode);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.BUILDING_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_BUILDING_CD + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER.equals(destinationField)) {
                        String deliveryBuildingRoomNumber = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(deliveryBuildingRoomNumber)){
                            Map<String,String> deliveryBuildingRoomNumberMap = new HashMap<>();
                            deliveryBuildingRoomNumberMap.put(OLEConstants.BUILDING_ROOM_NUMBER, deliveryBuildingRoomNumber);
                            List<Room> roomList= (List) getBusinessObjectService().findMatching(Room.class, deliveryBuildingRoomNumberMap);
                            if(roomList != null && roomList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_DELIVERY_BUILDING_ROOM_NUMBER + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +  "  " + deliveryBuildingRoomNumber);
                                deliveryBuildingRoomNumber = null;
                            }
                            oleTxRecord.setDeliveryBuildingRoomNumber(deliveryBuildingRoomNumber);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_DELIVERY_BUILDING_ROOM_NUMBER + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.VENDOR_CHOICE.equals(destinationField)) {
                        String vendorChoice = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(vendorChoice)){
                            Map<String,String> vendorChoiceMap = new HashMap<>();
                            vendorChoiceMap.put(OLEConstants.OLEBatchProcess.PO_VENDOR_CHOICE_CODE, vendorChoice);
                            List<PurchaseOrderVendorChoice> purchaseOrderVendorChoiceList = (List) getBusinessObjectService().findMatching(PurchaseOrderVendorChoice.class, vendorChoiceMap);
                            if(purchaseOrderVendorChoiceList != null && purchaseOrderVendorChoiceList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.PO_VENDOR_CHOICE_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_VENDOR_CHOICE_CD + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +  "  " + vendorChoice);
                                vendorChoice = null;
                            }
                            oleTxRecord.setVendorChoice(vendorChoice);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.PO_VENDOR_CHOICE_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_VENDOR_CHOICE + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION.equals(destinationField)) {
                        String methodOfPOTransmission = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(methodOfPOTransmission)){
                            Map<String,String> methodOfPOTransmissionMap = new HashMap<>();
                            methodOfPOTransmissionMap.put(OLEConstants.OLEBatchProcess.PO_TRANSMISSION_METHOD_CODE, methodOfPOTransmission);
                            List<PurchaseOrderTransmissionMethod> purchaseOrderTransmissionMethodList = (List) getBusinessObjectService().findMatching(PurchaseOrderTransmissionMethod.class, methodOfPOTransmissionMap);
                            if(purchaseOrderTransmissionMethodList != null && purchaseOrderTransmissionMethodList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.PO_TRANSMISSION_METHOD_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_METHOD_OF_PO_TRANSMISSION_CD + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + methodOfPOTransmission);
                                methodOfPOTransmission = null;
                            }
                            oleTxRecord.setMethodOfPOTransmission(methodOfPOTransmission);
                        }
                        else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.PO_TRANSMISSION_METHOD_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_METHOD_OF_PO_TRANSMISSION + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP.equals(destinationField)) {
                        String recurringPaymentType = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(recurringPaymentType)){
                            Map<String,String> recurringPaymentTypeMap = new HashMap<>();
                            recurringPaymentTypeMap.put(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP_CODE, recurringPaymentType);
                            List<RecurringPaymentType> recurringPaymentTypeList = (List) getBusinessObjectService().findMatching(RecurringPaymentType.class, recurringPaymentTypeMap);
                            if(recurringPaymentTypeList != null && recurringPaymentTypeList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_RECURRING_PAYMENT_TYP_CD + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + recurringPaymentType);
                                recurringPaymentType = null;
                            }
                            oleTxRecord.setMethodOfPOTransmission(recurringPaymentType);
                        }
                        else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_RECURRING_PAYMENT_TYP_CD + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_BEGIN_DT.equals(destinationField)) {
                        String recurringPaymentBeginDate = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(recurringPaymentBeginDate)){
                            boolean validDate = new OLEBatchProcessProfileController().validateRecurringPaymentDate(recurringPaymentBeginDate);
                            if(!validDate){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_BEGIN_DT,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_RECURRING_BEGIN_DT + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + recurringPaymentBeginDate);
                                recurringPaymentBeginDate = null;
                            }
                            oleTxRecord.setRecurringPaymentBeginDate(recurringPaymentBeginDate);
                        }
                        else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_BEGIN_DT,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.RECURRING_BEGIN_DT + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_END_DT.equals(destinationField)) {
                        String recurringPaymentEndDate = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(recurringPaymentEndDate)){
                            boolean validDate = new OLEBatchProcessProfileController().validateRecurringPaymentDate(recurringPaymentEndDate);
                            if(!validDate){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_END_DT,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_RECURRING_END_DT + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + recurringPaymentEndDate);
                                recurringPaymentEndDate = null;
                            }
                            oleTxRecord.setRecurringPaymentEndDate(recurringPaymentEndDate);
                        }
                        else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_END_DT,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.RECURRING_END_DT + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.COST_SOURCE.equals(destinationField)) {
                        String costSource = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(costSource)){
                            Map<String,String> costSourceMap = new HashMap<>();
                            costSourceMap.put(OLEConstants.OLEBatchProcess.PO_COST_SOURCE_CODE, costSource);
                            List<PurchaseOrderCostSource> purchaseOrderCostSourceList= (List) getBusinessObjectService().findMatching(PurchaseOrderCostSource.class, costSourceMap);
                            if(purchaseOrderCostSourceList != null && purchaseOrderCostSourceList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.PO_COST_SOURCE_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_COST_SOURCE_CD + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + costSource);
                                costSource = null;
                            }
                            oleTxRecord.setCostSource(costSource);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.PO_COST_SOURCE_CODE,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_COST_SOURCE + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.PERCENT.equals(destinationField)) {
                        String percent = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if(!StringUtils.isBlank(percent)){
                            boolean validPercent = validateForPercentage(percent);
                            if(!validPercent){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.PERCENT,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_PERCENT + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + percent);
                                percent = null;
                            }
                            oleTxRecord.setPercent(percent);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.PERCENT,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_PERCENT + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.DEFAULT_LOCATION.equals(destinationField)) {
                        String defaultLocation = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(defaultLocation)){
                            boolean validDefaultLocation = validateDefaultLocation(defaultLocation);
                            if(!validDefaultLocation){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_LOCN_NM + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + defaultLocation);
                                defaultLocation = null;
                            }
                            oleTxRecord.setDefaultLocation(defaultLocation);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_LOCATION_NM + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.LIST_PRICE.equals(destinationField)) {
                        String listPrice = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(listPrice)){
                            boolean validListPrice = validateDestinationFieldValues(listPrice);
                            if(!validListPrice){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.LIST_PRICE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_LIST_PRICE + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +"  " + listPrice);
                                listPrice = null;
                            }
                            else {
                                listPrice = Float.parseFloat(listPrice) + "";
                            }
                            oleTxRecord.setListPrice(listPrice);
                        }
                        else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.LIST_PRICE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_LIST_PRICE +  " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.VENDOR_NUMBER.equals(destinationField)) {
                        String vendorNumber = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(vendorNumber)){
                            boolean validVendorNumber = validateVendorNumber(vendorNumber);
                            if(!validVendorNumber){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.VENDOR_NUMBER, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_VENDOR_NUMBER + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + vendorNumber);
                                vendorNumber = null;
                            }
                            oleTxRecord.setVendorNumber(vendorNumber);
                        }
                        else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.VENDOR_NUMBER, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_VENDOR_NUMBER + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME.equals(destinationField)) {
                        String vendorAliasName = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if (!StringUtils.isBlank(vendorAliasName)) {
                            Map<String, String> vendorAliasMap = new HashMap<>();
                            vendorAliasMap.put(OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME, vendorAliasName);
                            List<VendorAlias> vendorAliasList = (List) getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
                            if (vendorAliasList != null && vendorAliasList.size() == 0) {
                                mappingFailures.put(OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition + 1) + "  " + OLEConstants.INVALID_VENDOR_ALIAS_NAME + "  " + dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + vendorAliasName);
                                vendorAliasName = null;
                            }
                            oleTxRecord.setVendorAliasName(vendorAliasName);
                        } else {
                            mappingFailures.put(OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition + 1) + "  " + OLEConstants.REQUIRED_VENDOR_ALIAS_NAME + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR.equals(destinationField)) {
                        String vendorCustomerNumber = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(vendorCustomerNumber)){
                            Map<String,String> vendorCustomerMap = new HashMap<>();
                            vendorCustomerMap.put(OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR, vendorCustomerNumber);
                            List<VendorCustomerNumber> vendorCustomerList = (List) getBusinessObjectService().findMatching(VendorCustomerNumber.class, vendorCustomerMap);
                            if(vendorCustomerList != null && vendorCustomerList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_VENDOR_CUST_NBR + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +  "  " + vendorCustomerNumber);
                                vendorCustomerNumber = null;
                            }
                            oleTxRecord.setVendorInfoCustomer(vendorCustomerNumber);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR,OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_VENDOR_CUST_NBR + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.QUANTITY.equals(destinationField)) {
                        String quantity = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(quantity)){
                            boolean validQuantity = validateForNumber(quantity);
                            if(!validQuantity){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.QUANTITY, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_QTY + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + quantity);
                                quantity = null;
                            }
                            oleTxRecord.setQuantity(quantity);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.QUANTITY, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_QTY + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS.equals(destinationField)) {
                        String itemNoOfParts = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(itemNoOfParts)){
                            boolean validNoOfParts = validateForNumber(itemNoOfParts);
                            if(!validNoOfParts){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_NO_OF_PARTS + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + itemNoOfParts);
                                itemNoOfParts = null;
                            }
                            oleTxRecord.setItemNoOfParts(itemNoOfParts);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_NO_OF_PARTS + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.VENDOR_REFERENCE_NUMBER.equals(destinationField)) {
                        String vendorReferenceNumber = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(vendorReferenceNumber)){
                            oleTxRecord.setVendorItemIdentifier(vendorReferenceNumber);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.VENDOR_REFERENCE_NUMBER, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_VENDOR_REF_NMBR + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.RECEIVING_REQUIRED.equals(destinationField)) {
                        String receivingRequired = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!receivingRequired.equalsIgnoreCase(OLEConstants.OLEBatchProcess.TRUE) && !receivingRequired.equalsIgnoreCase(OLEConstants.OLEBatchProcess.FALSE)){
                            mappingFailures.put(OLEConstants.OLEBatchProcess.RECEIVING_REQUIRED, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_RECEIVING_REQUIRED + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + receivingRequired);
                        }
                        oleTxRecord.setReceivingRequired(Boolean.parseBoolean(receivingRequired));
                    }
                    else if (OLEConstants.OLEBatchProcess.USE_TAX_INDICATOR.equals(destinationField)) {
                        String useTaxIndicator = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if(!useTaxIndicator.equalsIgnoreCase(OLEConstants.OLEBatchProcess.TRUE) && !useTaxIndicator.equalsIgnoreCase(OLEConstants.OLEBatchProcess.FALSE)){
                            mappingFailures.put(OLEConstants.OLEBatchProcess.USE_TAX_INDICATOR, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition + 1) + "  " + OLEConstants.INVALID_USE_TAX_INDICATOR + "  " + dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + useTaxIndicator);
                        }
                        oleTxRecord.setUseTaxIndicator(Boolean.parseBoolean(useTaxIndicator));
                    }
                    else if (OLEConstants.OLEBatchProcess.PREQ_POSITIVE_APPROVAL_REQ.equals(destinationField)) {
                        String payReqPositiveApprovalReq = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if(!payReqPositiveApprovalReq.equalsIgnoreCase(OLEConstants.OLEBatchProcess.TRUE) && !payReqPositiveApprovalReq.equalsIgnoreCase(OLEConstants.OLEBatchProcess.FALSE)){
                            mappingFailures.put(OLEConstants.OLEBatchProcess.PREQ_POSITIVE_APPROVAL_REQ, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition + 1) + "  " + OLEConstants.INVALID_PREQ_POSITIVE_APPROVAL_REQ + "  " + dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + payReqPositiveApprovalReq);
                        }
                        oleTxRecord.setPayReqPositiveApprovalReq(Boolean.parseBoolean(payReqPositiveApprovalReq));
                    }
                    else if (OLEConstants.OLEBatchProcess.PO_CONFIRMATION_INDICATOR.equals(destinationField)) {
                        String purchaseOrderConfirmationIndicator = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if(!purchaseOrderConfirmationIndicator.equalsIgnoreCase(OLEConstants.OLEBatchProcess.TRUE) && !purchaseOrderConfirmationIndicator.equalsIgnoreCase(OLEConstants.OLEBatchProcess.FALSE)){
                            mappingFailures.put(OLEConstants.OLEBatchProcess.PO_CONFIRMATION_INDICATOR, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_PO_CONFIRMATION_INDICATOR + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + purchaseOrderConfirmationIndicator);
                        }
                        oleTxRecord.setPurchaseOrderConfirmationIndicator(Boolean.parseBoolean(purchaseOrderConfirmationIndicator));
                    }
                    else if (OLEConstants.OLEBatchProcess.ROUTE_TO_REQUESTOR.equals(destinationField)) {
                        String routeToRequestor = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if(!routeToRequestor.equalsIgnoreCase(OLEConstants.OLEBatchProcess.TRUE) && !routeToRequestor.equalsIgnoreCase(OLEConstants.OLEBatchProcess.FALSE)){
                            mappingFailures.put(OLEConstants.OLEBatchProcess.ROUTE_TO_REQUESTOR, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition + 1) + "  " + OLEConstants.INVALID_ROUTE_TO_REQUESTOR + "  " + dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + routeToRequestor);
                        }
                        oleTxRecord.setRouteToRequestor(Boolean.parseBoolean(routeToRequestor));
                    }
                    else if (OLEConstants.DONOR_CODE.equals(destinationField)) {
                        String donorCode = setDataMappingValuesForDonorAndNotesSection(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if (donorCode != null && !donorCode.isEmpty()) {
                            Map<String, String> donorCodeMap = new HashMap<>();
                            donorCodeMap.put(OLEConstants.DONOR_CODE, donorCode);
                            List<OLEDonor> donorCodeList = (List) KRADServiceLocator.getBusinessObjectService().findMatching(OLEDonor.class, donorCodeMap);
                            if (donorCodeList != null && donorCodeList.size() > 0) {
                                donors.add(donorCodeList.get(0).getDonorCode());
                            } else {
                                mappingFailures.put(OLEConstants.DONOR_CODE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_DONOR_CODE + "  " + dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + " " + donorCode);
                            }
                        }
                        //Commented for jira OLE-7587
                            /*
                            else{
                                mappingFailures.put(OLEConstants.DONOR_CODE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition + 1) + "  " + OLEConstants.REQUIRED_DONOR_CD + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                            }*/
                    }
                    else if (OLEConstants.OLEBatchProcess.REQUESTOR_NAME.equals(destinationField)) {
                        String requestorName = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if(!StringUtils.isBlank(requestorName)){
                            boolean validRequestorName =  checkRequestorName(requestorName);
                            if(!validRequestorName){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.REQUESTOR_NAME, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_REQUESTOR_NAME + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +"  " + requestorName);
                                requestorName = null;
                            }
                            oleTxRecord.setRequestorName(requestorName);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.REQUESTOR_NAME, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition + 1) + "  " + OLEConstants.REQUIRED_REQUESTOR_NM + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.ITEM_STATUS.equals(destinationField)) {
                        String itemStatus = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord, dataField, tagField);
                        if(!StringUtils.isBlank(itemStatus)){
                            boolean validItemStatus = validateItemStatus(itemStatus);
                            if(!validItemStatus){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.ITEM_STATUS, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_ITEM_STATUS + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +" " + itemStatus);
                                itemStatus = null;
                            }
                            oleTxRecord.setItemStatus(itemStatus);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.ITEM_STATUS, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_ITEM_STATUS + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.DISCOUNT.equals(destinationField)) {
                        String discount = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(discount)){
                            boolean validDiscount = validateDestinationFieldValues(discount);
                            if(!validDiscount){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.DISCOUNT, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_DISCOUNT + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +"  " + discount);
                                discount = null;
                            }
                            oleTxRecord.setDiscount(discount);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.DISCOUNT, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_DISCOUNT + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.DISCOUNT_TYPE.equals(destinationField)) {
                        String discountType = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(discountType)){
                            if(!discountType.equalsIgnoreCase(OLEConstants.PERCENTAGE) && !discountType.equalsIgnoreCase(OLEConstants.DELIMITER_HASH)){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.DISCOUNT_TYPE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_DISCOUNT_TYPE  + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +"  " + discountType);
                                discountType = null;
                            }
                            oleTxRecord.setDiscountType(discountType);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.DISCOUNT_TYPE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_DISCOUNT_TYPE+ " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER.equals(destinationField)) {
                        String accountNumber = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(accountNumber)){
                            Map<String,String> accountNumberMap = new HashMap<>();
                            accountNumberMap.put(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER, accountNumber);
                            List<Account> accountNumberList = (List) getLookupService().findCollectionBySearchHelper(Account.class, accountNumberMap,true);
                            if(accountNumberList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_ACCOUNT_NUMBER + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +"  " + accountNumber);
                                accountNumber = null;
                            }
                            oleTxRecord.setAccountNumber(accountNumber);
                            if (StringUtils.isNotBlank(accountNumber)) {
                                oleTxRecord.setAccountNumber(accountNumberList.get(0).getAccountNumber());
                            }
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_ACCOUNT_NUMBER + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.OBJECT_CODE.equals(destinationField)) {
                        String objectCode = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(objectCode)) {
                            Map<String,String> objectCodeMap = new HashMap<>();
                            objectCodeMap.put(OLEConstants.OLEBatchProcess.OBJECT_CODE, objectCode);
                            List<ObjectCode> objectCodeList = (List) getBusinessObjectService().findMatching(ObjectCode.class, objectCodeMap);
                            if(objectCodeList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.OBJECT_CODE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_OBJECT_CODE + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +"  " + objectCode);
                                objectCode = null;
                            }
                            oleTxRecord.setObjectCode(objectCode);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.OBJECT_CODE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_OBJECT_CODE + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.ITEM_CHART_CODE.equals(destinationField)) {
                        String itemChartCode = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(itemChartCode)) {
                            Map<String,String> itemChartCodeMap = new HashMap<>();
                            itemChartCodeMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE, itemChartCode);
                            List<Chart> itemChartCodeList = (List) getBusinessObjectService().findMatching(Chart.class, itemChartCodeMap);
                            if(itemChartCodeList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_ITEM_CHART_CD + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +"  " + itemChartCode);
                                itemChartCode = null;
                            }
                            oleTxRecord.setItemChartCode(itemChartCode);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_ITEM_CHART_CD + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEEResourceRecord.FUND_CODE.equals(destinationField)) {
                        String fundCode = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(StringUtils.isNotBlank(fundCode)) {
                            Map<String,String> fundCodeMap = new HashMap<>();
                            fundCodeMap.put(OLEConstants.OLEEResourceRecord.FUND_CODE, fundCode);
                            List<OleFundCode> fundCodeList = (List) getBusinessObjectService().findMatching(OleFundCode.class, fundCodeMap);
                            if(CollectionUtils.isEmpty(fundCodeList)){
                                mappingFailures.put(OLEConstants.OLEEResourceRecord.FUND_CODE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_FUND_CD + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +"  " + fundCode);
                                fundCode = null;
                            }
                            oleTxRecord.setFundCode(fundCode);
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEEResourceRecord.FUND_CODE, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_FUND_CODE + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.FORMAT_TYP_NM.equals(destinationField)) {
                        String formatTypeName = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        if(!StringUtils.isBlank(formatTypeName)) {
                            Map<String,String> formatTypeMap = new HashMap<>();
                            formatTypeMap.put(OLEConstants.OLEBatchProcess.FORMAT_TYP_NM, formatTypeName);
                            List<OleFormatType> formatTypeList = (List) getBusinessObjectService().findMatching(OleFormatType.class, formatTypeMap);
                            if(formatTypeList == null || formatTypeList.size() == 0){
                                mappingFailures.put(OLEConstants.OLEBatchProcess.FORMAT_TYP_NM, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.INVALID_FORMAT + "  " +dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField +"  " + formatTypeName);
                            }
                            else{
                                oleTxRecord.setFormatTypeId(formatTypeList.get(0).getFormatTypeId().toString());
                            }
                        }
                        else{
                            mappingFailures.put(OLEConstants.OLEBatchProcess.FORMAT_TYP_NM, OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.REQUIRED_FORMAT + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.MISC_NOTE.equals(destinationField)) {
                        String miscellaneousNote = setDataMappingValuesForDonorAndNotesSection(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if (!StringUtils.isBlank(miscellaneousNote)) {
                            if (miscellaneousNote.length() > 2000) {
                                miscellaneousNote = miscellaneousNote.substring(0, 2000);
                            }
                            miscellaneousNotes.add(miscellaneousNote);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.RCPT_NOTE.equals(destinationField)) {
                        String receiptNote = setDataMappingValuesForDonorAndNotesSection(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if (!StringUtils.isBlank(receiptNote)) {
                            if (receiptNote.length() > 2000) {
                                receiptNote = receiptNote.substring(0, 2000);
                            }
                            receiptNotes.add(receiptNote);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.RQST_NOTE.equals(destinationField)) {
                        String requestorNote = setDataMappingValuesForDonorAndNotesSection(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if (!StringUtils.isBlank(requestorNote)) {
                            if (requestorNote.length() > 2000) {
                                requestorNote = requestorNote.substring(0, 2000);
                            }
                            requestorNotes.add(requestorNote);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.SELECTOR_NOTE.equals(destinationField)) {
                        String selectorNote = setDataMappingValuesForDonorAndNotesSection(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if (!StringUtils.isBlank(selectorNote)) {
                            if (selectorNote.length() > 2000) {
                                selectorNote = selectorNote.substring(0, 2000);
                            }
                            selectorNotes.add(selectorNote);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.SPL_PROCESS_NOTE.equals(destinationField)) {
                        String splProcessInstrNote = setDataMappingValuesForDonorAndNotesSection(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if (!StringUtils.isBlank(splProcessInstrNote)) {
                            if (splProcessInstrNote.length() > 2000) {
                                splProcessInstrNote = splProcessInstrNote.substring(0, 2000);
                            }
                            splProcessInstrNotes.add(splProcessInstrNote);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.VNDR_INSTR_NOTE.equals(destinationField)) {
                        String vendorInstrNote = setDataMappingValuesForDonorAndNotesSection(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        if (!StringUtils.isBlank(vendorInstrNote)) {
                            if (vendorInstrNote.length() > 2000) {
                                vendorInstrNote = vendorInstrNote.substring(0, 2000);
                            }
                            vendorInstrNotes.add(vendorInstrNote);
                        }
                    }
                    else if (OLEConstants.OLEBatchProcess.CAPTION.equals(destinationField)) {
                        String caption = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList,dataMapCount,bibMarcRecord,dataField,tagField);
                        oleTxRecord.setCaption(caption);
                    }
                    else if (OLEConstants.OLEBatchProcess.VOLUME_NUMBER.equals(destinationField)) {
                        String volumeNumber = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                        oleTxRecord.setVolumeNumber(volumeNumber);
                    }
                }
            }
            if (donors.size()>0){
                oleTxRecord.setOleDonors(donors);
            }
            if(miscellaneousNotes.size() > 0){
                oleTxRecord.setMiscellaneousNote(OLEConstants.OLEBatchProcess.MISC_NOTE);
                oleTxRecord.setMiscellaneousNotes(miscellaneousNotes);
            }
            if(receiptNotes.size() > 0){
                oleTxRecord.setReceiptNote(OLEConstants.OLEBatchProcess.RCPT_NOTE);
                oleTxRecord.setReceiptNotes(receiptNotes);
            }
            if(requestorNotes.size() > 0){
                oleTxRecord.setRequestorNote(OLEConstants.OLEBatchProcess.RQST_NOTE);
                oleTxRecord.setRequestorNotes(requestorNotes);
            }
            if(selectorNotes.size() > 0){
                oleTxRecord.setSelectorNote(OLEConstants.OLEBatchProcess.SELECTOR_NOTE);
                oleTxRecord.setSelectorNotes(selectorNotes);
            }
            if(splProcessInstrNotes.size() > 0){
                oleTxRecord.setSplProcessInstrNote(OLEConstants.OLEBatchProcess.SPL_PROCESS_NOTE);
                oleTxRecord.setSplProcessInstrNotes(splProcessInstrNotes);
            }
            if(vendorInstrNotes.size() > 0){
                oleTxRecord.setVendorInstrNote(OLEConstants.OLEBatchProcess.VNDR_INSTR_NOTE);
                oleTxRecord.setVendorInstrNotes(vendorInstrNotes);
            }
            if (LOG.isDebugEnabled()){
                LOG.debug("*******************FailureRecords**************" + failureRecords.toString());
            }
        }
        LOG.debug("----End of mapDataFieldsToTxnRecord()------------------------------");
        return mappingFailures;
    }

    private boolean validateDate(String recurringDate){
        SimpleDateFormat dateFromRawFile = new SimpleDateFormat(org.kuali.ole.OLEConstants.DATE_FORMAT);
        try {
            dateFromRawFile.parse(recurringDate);
            return true;
        }
        catch (ParseException e) {
            return false;
        }
    }

    public String setDataMappingValues(List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList,int dataMapCount,BibMarcRecord bibMarcRecord,String dataField,String tagField){
        String subFieldValue = getSubFieldValueFor(bibMarcRecord, dataField, tagField);
        if (StringUtils.isBlank(subFieldValue)) {
            OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo = oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount);
            if (dataMapCount+1 <= oleBatchProcessProfileDataMappingOptionsBoList.size()) {
                if(dataMapCount+1 == oleBatchProcessProfileDataMappingOptionsBoList.size()) {
                    subFieldValue = oleBatchProcessProfileDataMappingOptionsBo.getDestinationFieldValue();
                }
                else if(!oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount+1).getDestinationField().equalsIgnoreCase(oleBatchProcessProfileDataMappingOptionsBo.getDestinationField())){
                    subFieldValue = oleBatchProcessProfileDataMappingOptionsBo.getDestinationFieldValue();
                }
            }
        }
        return subFieldValue;
    }

    public String setDataMappingValuesForDonorAndNotesSection(List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList, int dataMapCount, BibMarcRecord bibMarcRecord, String dataField, String tagField) {
        String subFieldValue = getSubFieldValueFor(bibMarcRecord, dataField, tagField);
        if (StringUtils.isBlank(subFieldValue)) {
            OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo = oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount);
            while (dataMapCount + 1 <= oleBatchProcessProfileDataMappingOptionsBoList.size()) {
                if (dataMapCount + 1 == oleBatchProcessProfileDataMappingOptionsBoList.size()) {
                    subFieldValue = oleBatchProcessProfileDataMappingOptionsBo.getDestinationFieldValue();
                    break;
                } else if (!oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount + 1).getDestinationField().equalsIgnoreCase(oleBatchProcessProfileDataMappingOptionsBo.getDestinationField())) {
                    subFieldValue = oleBatchProcessProfileDataMappingOptionsBo.getDestinationFieldValue();
                    break;
                }
                dataMapCount++;
            }
        }
        return subFieldValue;
    }

    private String getSubFieldValueFor(BibMarcRecord bibMarcRecord, String dataField, String tag) {
        String subFieldValue = null;
        org.kuali.ole.docstore.common.document.content.bib.marc.DataField dataFieldForTag = getDataFieldForTag(bibMarcRecord, dataField);
        if (null != dataFieldForTag) {
            List<org.kuali.ole.docstore.common.document.content.bib.marc.SubField> subfields = dataFieldForTag.getSubFields();
            for (Iterator<org.kuali.ole.docstore.common.document.content.bib.marc.SubField> iterator = subfields.iterator(); iterator.hasNext(); ) {
                org.kuali.ole.docstore.common.document.content.bib.marc.SubField marcSubField = iterator.next();
                if (marcSubField.getCode().equals(tag)) {
                    subFieldValue = marcSubField.getValue();
                    return subFieldValue;
                }
            }
        }
        return subFieldValue;
    }

    public org.kuali.ole.docstore.common.document.content.bib.marc.DataField getDataFieldForTag(BibMarcRecord bibMarcRecord, String tag) {
        for (Iterator<org.kuali.ole.docstore.common.document.content.bib.marc.DataField> iterator = bibMarcRecord.getDataFields().iterator(); iterator.hasNext(); ) {
            org.kuali.ole.docstore.common.document.content.bib.marc.DataField marcDataField = iterator.next();
            if (marcDataField.getTag().equalsIgnoreCase(tag)) {
                return marcDataField;
            }
        }
        return null;
    }


    /**
     * This method is for setting accounting line details of Ole Transaction document.
     * @param oleTxRecord
     */
    private void setAccountingLinesDetails(OleTxRecord oleTxRecord,int recordPosition, OLEBatchProcessJobDetailsBo job){
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        if(oleTxRecord.getFundCode() == null && oleTxRecord.getAccountNumber() != null && oleTxRecord.getObjectCode() != null && oleTxRecord.getItemChartCode() != null){
            Map<String,String> accLinesMap = new HashMap<>();
            accLinesMap.put(OLEConstants.ACCOUNT_NUMBER,oleTxRecord.getAccountNumber());
            accLinesMap.put(OLEConstants.OLEBatchProcess.CHART_OF_ACCOUNTS_CODE,oleTxRecord.getItemChartCode());
            List<Account> accountList = (List) getBusinessObjectService().findMatching(Account.class, accLinesMap);
            //List<String> reasonForFailure = (List<String>)dataCarrierService.getData(OLEConstants.FAILURE_REASON);
            List<String> reasonForFailure = orderImportHelperBo.getFailureReason();
            if(accountList != null && accountList.size() > 0){
                Account account = accountList.get(0);
                if (account.isClosed()){
                    reasonForFailure.add(OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + oleTxRecord.getAccountNumber() +OLEConstants.OLEBatchProcess.CLOSED_ACC_NO);
                }
                accLinesMap.remove(OLEConstants.ACCOUNT_NUMBER);
                accLinesMap.put(OLEConstants.OLEBatchProcess.OBJECT_CODE,oleTxRecord.getObjectCode());
                List<ObjectCode> objectCodeList = (List) getBusinessObjectService().findMatching(ObjectCode.class, accLinesMap);
                if(objectCodeList == null || objectCodeList.size() == 0){
                    if(reasonForFailure != null){
                        reasonForFailure.add(OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.OLEBatchProcess.INVALID_ACNO_OBJCD_COMBINATION);
                        //dataCarrierService.addData(OLEConstants.FAILURE_REASON,reasonForFailure);
                    }
                }
                orderImportHelperBo.setFailureReason(reasonForFailure);
            }
            else{
                if(reasonForFailure != null){
                    reasonForFailure.add(OLEConstants.OLEBatchProcess.REC_POSITION + (recordPosition+1)  + "  " + OLEConstants.OLEBatchProcess.INVALID_ACNO_OBJCD_COMBINATION);
                    //dataCarrierService.addData(OLEConstants.FAILURE_REASON,reasonForFailure);
                    orderImportHelperBo.setFailureReason(reasonForFailure);
                }
            }
        }
    }

    /**
     * This method sets the value for OLE transaction record by getting the value from Constant and default section of profile.
     * @param oleTxRecord
     */
    protected Map<String,String> setDefaultAndConstantValuesToTxnRecord(OleTxRecord oleTxRecord, Map<String,String> failureRecords, OLEBatchProcessJobDetailsBo job) {
        if (failureRecords == null) {
            failureRecords = new HashMap<String,String>();
        }
        String attributeName = null;
        String attributeValue = null;
        LOG.debug("----Inside setDefaultAndConstantValuesToTxnRecord()------------------------------");
        List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsBoList = job.getOrderImportHelperBo().getOleBatchProcessProfileBo().getOleBatchProcessProfileConstantsList();
        for (OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo : oleBatchProcessProfileConstantsBoList) {
            attributeName = oleBatchProcessProfileConstantsBo.getAttributeName();
            attributeValue = oleBatchProcessProfileConstantsBo.getAttributeValue();
            if (OLEConstants.OLEBatchProcess.CONSTANT.equals(oleBatchProcessProfileConstantsBo.getDefaultValue())) {

                if (OLEConstants.OLEBatchProcess.CHART_CODE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.CHART_CODE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.CHART_CODE);
                    }
                    oleTxRecord.setChartCode(attributeValue);
                } else if (OLEConstants.OLEBatchProcess.ORG_CODE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ORG_CODE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ORG_CODE);
                    }
                    oleTxRecord.setOrgCode(attributeValue);
                }
                else if(OLEConstants.OLEBatchProcess.RECEIVING_REQUIRED.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.RECEIVING_REQUIRED)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.RECEIVING_REQUIRED);
                    }
                    oleTxRecord.setReceivingRequired(Boolean.parseBoolean(attributeValue));
                }
                else if (OLEConstants.OLEBatchProcess.CONTRACT_MANAGER.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.CONTRACT_MANAGER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.CONTRACT_MANAGER);
                    }
                    oleTxRecord.setContractManager(attributeValue);
                } else if (OLEConstants.OLEBatchProcess.ASSIGN_TO_USER.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ASSIGN_TO_USER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ASSIGN_TO_USER);
                    }
                    oleTxRecord.setAssignToUser(attributeValue);
                }
                else if(OLEConstants.OLEBatchProcess.USE_TAX_INDICATOR.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.USE_TAX_INDICATOR)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.USE_TAX_INDICATOR);
                    }
                    oleTxRecord.setUseTaxIndicator(Boolean.parseBoolean(attributeValue));
                }
                else if (OLEConstants.OLEBatchProcess.ORDER_TYPE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ORDER_TYPE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ORDER_TYPE);
                    }
                    oleTxRecord.setOrderType(attributeValue);
                } else if (OLEConstants.OLEBatchProcess.FUNDING_SOURCE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.FUNDING_SOURCE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.FUNDING_SOURCE);
                    }
                    oleTxRecord.setFundingSource(attributeValue);
                }
                else if(OLEConstants.OLEBatchProcess.PREQ_POSITIVE_APPROVAL_REQ.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.PREQ_POSITIVE_APPROVAL_REQ)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.PREQ_POSITIVE_APPROVAL_REQ);
                    }
                    oleTxRecord.setPayReqPositiveApprovalReq(Boolean.parseBoolean(attributeValue));
                }
                else if(OLEConstants.OLEBatchProcess.PO_CONFIRMATION_INDICATOR.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.PO_CONFIRMATION_INDICATOR)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.PO_CONFIRMATION_INDICATOR);
                    }
                    oleTxRecord.setPurchaseOrderConfirmationIndicator(Boolean.parseBoolean(attributeValue));
                }
                else if (OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE);
                    }
                    oleTxRecord.setDeliveryCampusCode(attributeValue);
                } else if (OLEConstants.OLEBatchProcess.BUILDING_CODE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.BUILDING_CODE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.BUILDING_CODE);
                    }
                    oleTxRecord.setBuildingCode(attributeValue);
                } else if (OLEConstants.OLEBatchProcess.VENDOR_CHOICE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.VENDOR_CHOICE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.VENDOR_CHOICE);
                    }
                    oleTxRecord.setVendorChoice(attributeValue);
                }
                else if(OLEConstants.OLEBatchProcess.ROUTE_TO_REQUESTOR.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ROUTE_TO_REQUESTOR)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ROUTE_TO_REQUESTOR);
                    }
                    oleTxRecord.setRouteToRequestor(Boolean.parseBoolean(attributeValue));
                }
                else if (OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION);
                    }
                    oleTxRecord.setMethodOfPOTransmission(attributeValue);
                } else if (OLEConstants.OLEBatchProcess.COST_SOURCE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.COST_SOURCE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.COST_SOURCE);
                    }
                    oleTxRecord.setCostSource(attributeValue);
                } else if (OLEConstants.OLEBatchProcess.PERCENT.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.PERCENT)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.PERCENT);
                    }
                    oleTxRecord.setPercent(attributeValue);
                } else if (OLEConstants.OLEBatchProcess.DEFAULT_LOCATION.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION);
                    }
                    oleTxRecord.setDefaultLocation(attributeValue);
                } else if (OLEConstants.OLEBatchProcess.LIST_PRICE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.LIST_PRICE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.LIST_PRICE);
                    }
                    oleTxRecord.setListPrice(attributeValue);
                } else if (OLEConstants.OLEBatchProcess.VENDOR_NUMBER.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.VENDOR_NUMBER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.VENDOR_NUMBER);
                    }
                    oleTxRecord.setVendorNumber(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME);
                    }
                    oleTxRecord.setVendorAliasName(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR);
                    }
                    oleTxRecord.setVendorInfoCustomer(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.QUANTITY.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.QUANTITY)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.QUANTITY);
                    }
                    oleTxRecord.setQuantity(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS);
                    }
                    oleTxRecord.setItemNoOfParts(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.VENDOR_REFERENCE_NUMBER.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.VENDOR_REFERENCE_NUMBER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.VENDOR_REFERENCE_NUMBER);
                    }
                    oleTxRecord.setVendorItemIdentifier(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.REQUESTOR_NAME.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.REQUESTOR_NAME)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.REQUESTOR_NAME);
                    }
                    oleTxRecord.setRequestorName(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.ITEM_STATUS.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ITEM_STATUS)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ITEM_STATUS);
                    }
                    oleTxRecord.setItemStatus(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.DISCOUNT.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.DISCOUNT)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.DISCOUNT);
                    }
                    oleTxRecord.setDiscount(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.DISCOUNT_TYPE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.DISCOUNT_TYPE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.DISCOUNT_TYPE);
                    }
                    oleTxRecord.setDiscountType(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER);
                    }
                    oleTxRecord.setAccountNumber(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.OBJECT_CODE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.OBJECT_CODE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.OBJECT_CODE);
                    }
                    oleTxRecord.setObjectCode(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.ITEM_CHART_CODE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE);
                    }
                    oleTxRecord.setItemChartCode(attributeValue);
                }
                else if (OLEConstants.OLEEResourceRecord.FUND_CODE.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEEResourceRecord.FUND_CODE)) {
                        failureRecords.remove(OLEConstants.OLEEResourceRecord.FUND_CODE);
                    }
                    oleTxRecord.setFundCode(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.REQUEST_SRC.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.REQUEST_SRC)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.REQUEST_SRC);
                    }
                    oleTxRecord.setRequestSourceType(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.CAPTION.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.CAPTION)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.CAPTION);
                    }
                    oleTxRecord.setCaption(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.VOLUME_NUMBER.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.VOLUME_NUMBER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.VOLUME_NUMBER);
                    }
                    oleTxRecord.setVolumeNumber(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER);
                    }
                    oleTxRecord.setDeliveryBuildingRoomNumber(attributeValue);
                }else if(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP);
                    }
                    oleTxRecord.setRecurringPaymentType(attributeValue);
                }else if(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_BEGIN_DT.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_BEGIN_DT)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_BEGIN_DT);
                    }
                    oleTxRecord.setRecurringPaymentBeginDate(attributeValue);
                }else if(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_END_DT.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_END_DT)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_END_DT);
                    }
                    oleTxRecord.setRecurringPaymentEndDate(attributeValue);
                }

            } else if (OLEConstants.OLEBatchProcess.DEFAULT.equals(oleBatchProcessProfileConstantsBo.getDefaultValue())) {

                if (OLEConstants.OLEBatchProcess.CHART_CODE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getChartCode())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.CHART_CODE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.CHART_CODE);
                    }
                    oleTxRecord.setChartCode(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.ORG_CODE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getOrgCode())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ORG_CODE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ORG_CODE);
                    }
                    oleTxRecord.setOrgCode(attributeValue);
                }
                else if(OLEConstants.OLEBatchProcess.RECEIVING_REQUIRED.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.RECEIVING_REQUIRED)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.RECEIVING_REQUIRED);
                    }
                    oleTxRecord.setReceivingRequired(Boolean.parseBoolean(attributeValue));
                }
                else if (OLEConstants.OLEBatchProcess.CONTRACT_MANAGER.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getContractManager())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.CONTRACT_MANAGER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.CONTRACT_MANAGER);
                    }
                    oleTxRecord.setContractManager(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.ASSIGN_TO_USER.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getAssignToUser())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ASSIGN_TO_USER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ASSIGN_TO_USER);
                    }
                    oleTxRecord.setAssignToUser(attributeValue);
                }
                else if(OLEConstants.OLEBatchProcess.USE_TAX_INDICATOR.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.USE_TAX_INDICATOR)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.USE_TAX_INDICATOR);
                    }
                    oleTxRecord.setUseTaxIndicator(Boolean.parseBoolean(attributeValue));
                }
                else if (OLEConstants.OLEBatchProcess.ORDER_TYPE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getOrderType())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ORDER_TYPE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ORDER_TYPE);
                    }
                    oleTxRecord.setOrderType(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.FUNDING_SOURCE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getFundingSource())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.FUNDING_SOURCE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.FUNDING_SOURCE);
                    }
                    oleTxRecord.setFundingSource(attributeValue);
                }
                else if(OLEConstants.OLEBatchProcess.PREQ_POSITIVE_APPROVAL_REQ.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.PREQ_POSITIVE_APPROVAL_REQ)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.PREQ_POSITIVE_APPROVAL_REQ);
                    }
                    oleTxRecord.setPayReqPositiveApprovalReq(Boolean.parseBoolean(attributeValue));
                }
                else if(OLEConstants.OLEBatchProcess.PO_CONFIRMATION_INDICATOR.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.PO_CONFIRMATION_INDICATOR)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.PO_CONFIRMATION_INDICATOR);
                    }
                    oleTxRecord.setPurchaseOrderConfirmationIndicator(Boolean.parseBoolean(attributeValue));
                }
                else if (OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getDeliveryCampusCode())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.DELIVERY_CAMPUS_CODE);
                    }
                    oleTxRecord.setDeliveryCampusCode(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.BUILDING_CODE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getBuildingCode())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.BUILDING_CODE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.BUILDING_CODE);
                    }
                    oleTxRecord.setBuildingCode(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.VENDOR_CHOICE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getVendorChoice())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.VENDOR_CHOICE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.VENDOR_CHOICE);
                    }
                    oleTxRecord.setVendorChoice(attributeValue);
                }
                else if(OLEConstants.OLEBatchProcess.ROUTE_TO_REQUESTOR.equals(attributeName)) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ROUTE_TO_REQUESTOR)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ROUTE_TO_REQUESTOR);
                    }
                    oleTxRecord.setRouteToRequestor(Boolean.parseBoolean(attributeValue));
                }
                else if (OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getMethodOfPOTransmission())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.METHOD_OF_PO_TRANSMISSION);
                    }
                    oleTxRecord.setMethodOfPOTransmission(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.COST_SOURCE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getCostSource())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.COST_SOURCE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.COST_SOURCE);
                    }
                    oleTxRecord.setCostSource(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.PERCENT.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getPercent())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.PERCENT)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.PERCENT);
                    }
                    oleTxRecord.setPercent(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.DEFAULT_LOCATION.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getDefaultLocation())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION);
                    }
                    oleTxRecord.setDefaultLocation(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.LIST_PRICE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getListPrice())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.LIST_PRICE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.LIST_PRICE);
                    }
                    oleTxRecord.setListPrice(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.VENDOR_NUMBER.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getVendorNumber())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.VENDOR_NUMBER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.VENDOR_NUMBER);
                    }
                    oleTxRecord.setVendorNumber(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getVendorAliasName())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.VENDOR_ALIAS_NAME);
                    }
                    oleTxRecord.setVendorAliasName(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getVendorInfoCustomer())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.VENDOR_CUST_NBR);
                    }
                    oleTxRecord.setVendorInfoCustomer(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.QUANTITY.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getQuantity())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.QUANTITY)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.QUANTITY);
                    }
                    oleTxRecord.setQuantity(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getItemNoOfParts())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS);
                    }
                    oleTxRecord.setItemNoOfParts(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.VENDOR_REFERENCE_NUMBER.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getVendorItemIdentifier())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.VENDOR_REFERENCE_NUMBER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.VENDOR_REFERENCE_NUMBER);
                    }
                    oleTxRecord.setVendorItemIdentifier(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.REQUESTOR_NAME.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getRequestorName())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.REQUESTOR_NAME)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.REQUESTOR_NAME);
                    }
                    oleTxRecord.setRequestorName(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.ITEM_STATUS.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getItemStatus())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ITEM_STATUS)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ITEM_STATUS);
                    }
                    oleTxRecord.setItemStatus(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.DISCOUNT.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getDiscount())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.DISCOUNT)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.DISCOUNT);
                    }
                    oleTxRecord.setDiscount(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.DISCOUNT_TYPE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getDiscountType())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.DISCOUNT_TYPE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.DISCOUNT_TYPE);
                    }
                    oleTxRecord.setDiscountType(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getAccountNumber())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ACCOUNT_NUMBER);
                    }
                    oleTxRecord.setAccountNumber(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.OBJECT_CODE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getObjectCode())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.OBJECT_CODE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.OBJECT_CODE);
                    }
                    oleTxRecord.setObjectCode(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.ITEM_CHART_CODE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getItemChartCode())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.ITEM_CHART_CODE);
                    }
                    oleTxRecord.setItemChartCode(attributeValue);
                }
                else if (OLEConstants.OLEEResourceRecord.FUND_CODE.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getFundCode())) {
                    if (failureRecords.containsKey(OLEConstants.OLEEResourceRecord.FUND_CODE)) {
                        failureRecords.remove(OLEConstants.OLEEResourceRecord.FUND_CODE);
                    }
                    oleTxRecord.setFundCode(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.REQUEST_SRC.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getRequestSourceType())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.REQUEST_SRC)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.REQUEST_SRC);
                    }
                    oleTxRecord.setRequestSourceType(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.CAPTION.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getCaption())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.CAPTION)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.CAPTION);
                    }
                    oleTxRecord.setCaption(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.VOLUME_NUMBER.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getVolumeNumber())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.VOLUME_NUMBER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.VOLUME_NUMBER);
                    }
                    oleTxRecord.setVolumeNumber(attributeValue);
                }
                else if (OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getVolumeNumber())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.DELIVERY_BUILDING_ROOM_NUMBER);
                    }
                    oleTxRecord.setDeliveryBuildingRoomNumber(attributeValue);
                }else if(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getRecurringPaymentType())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_TYP);
                    }
                    oleTxRecord.setRecurringPaymentType(attributeValue);
                }else if(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_BEGIN_DT.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getRecurringPaymentBeginDate())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_BEGIN_DT)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_BEGIN_DT);
                    }
                    oleTxRecord.setRecurringPaymentBeginDate(attributeValue);
                }else if(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_END_DT.equals(attributeName) && StringUtils.isBlank(oleTxRecord.getRecurringPaymentEndDate())) {
                    if (failureRecords.containsKey(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_END_DT)) {
                        failureRecords.remove(OLEConstants.OLEBatchProcess.RECURRING_PAYMENT_END_DT);
                    }
                    oleTxRecord.setRecurringPaymentEndDate(attributeValue);
                }

            }
        }
        LOG.debug("----End of setDefaultAndConstantValuesToTxnRecord()------------------------------");
        return failureRecords;
    }

    /**
     * This method is for validating requestor name.
     * @param requestorName
     * @return
     */
    public boolean checkRequestorName(String requestorName){
        LOG.debug("----Inside checkRequestorName() of Order Record Import------------------------------");
        boolean requestorAvailable = false;
        String[] requestorNames = requestorName.split(", ");
        if(requestorNames.length == 2){
            String lastName = requestorNames[0];
            String firstName = requestorNames[1];
            Map<String,String> requestorNameMap = new HashMap<>();
            requestorNameMap.put(org.kuali.ole.sys.OLEConstants.OlePersonRequestorLookupable.FIRST_NAME,firstName);
            requestorNameMap.put(org.kuali.ole.sys.OLEConstants.OlePersonRequestorLookupable.LAST_NAME,lastName);
            List<OLERequestorPatronDocument> olePatronDocumentList = getOleSelectDocumentService().getPatronDocumentListFromWebService();
            if(olePatronDocumentList != null && olePatronDocumentList.size()>0){
                for(int recCount = 0;recCount < olePatronDocumentList.size();recCount++) {
                    if(olePatronDocumentList.get(recCount).getFirstName().equalsIgnoreCase(firstName) && olePatronDocumentList.get(recCount).getLastName().equalsIgnoreCase(lastName)){
                        requestorAvailable = true;
                        break;
                    }
                }
            }
        }
        return requestorAvailable;

    }

    public boolean validateItemStatus(String itemStatus) {
        List<KeyValue> itemStatusList = getKeyValues();
        boolean validItemStatus = false;
        for(int itemStatusCount = 0; itemStatusCount < itemStatusList.size(); itemStatusCount++){
            if(itemStatusList.get(itemStatusCount).getKey().equalsIgnoreCase(itemStatus)) {
                validItemStatus = true;
                break;
            }
        }
        return validItemStatus;
    }

    public List<KeyValue> getKeyValues() {

        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleItemAvailableStatus> oleItemAvailableStatuses = KRADServiceLocator.getBusinessObjectService().findAll(OleItemAvailableStatus.class);
        String excludeItemStatus = getParameter(OLEConstants.EXCLUDE_ITEM_STATUS);
        Map<String,String> map = new HashMap<>();
        if(excludeItemStatus!=null && !excludeItemStatus.isEmpty()){
            String[] itemStatusList = excludeItemStatus.split(",");
            for(String itemStatus : itemStatusList){
                map.put(itemStatus,itemStatus);
            }
        }
        for (OleItemAvailableStatus type : oleItemAvailableStatuses) {
            if (type.isActive() && !map.containsKey(type.getItemAvailableStatusCode())) {
                options.add(new ConcreteKeyValue(type.getItemAvailableStatusCode(), type.getItemAvailableStatusName()));
            }
        }
        return options;
    }

    public String getParameter(String name){
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.SELECT_NMSPC, OLEConstants.SELECT_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter!=null?parameter.getValue():null;
    }

    public boolean validateForNumber(String fieldValue){
        try {
            Integer quantity = Integer.parseInt(fieldValue);
            if(quantity <= 0){
                return false;
            }
        }
        catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }

    public boolean validateDestinationFieldValues(String destinationFieldValue){
        try {
            Float fieldValue = Float.parseFloat(destinationFieldValue);
            if(fieldValue < 0){
                return false;
            }
        }
        catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }

    public boolean validateForPercentage(String percentage){
        try {
            Float fieldValue = Float.parseFloat(percentage);
            if(fieldValue != 100){
                return false;
            }
        }
        catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }

    public boolean validateDefaultLocation(String defaultLocation){
        List<String> locationList = getItemLocation();
        Boolean isLocationAvailable = false;
        for(int locationCount = 0;locationCount < locationList.size();locationCount++) {
            if(locationList.get(locationCount).equalsIgnoreCase(defaultLocation)){
                isLocationAvailable = true;
                break;
            }
        }
        return isLocationAvailable;
    }

    public boolean validateVendorNumber(String vendorNumber){
        LOG.debug("----Inside validateVendorNumber()------------------------------");
        boolean isValidVendor = false;
        String[] vendorDetail = vendorNumber.split("-");
        if(vendorDetail.length == 2){
            String vendorHeaderGeneratedIdentifier = vendorDetail[0];
            String vendorDetailAssignedIdentifier = vendorDetail[1];
            try {
                Map<String,Integer> vendorMap = new HashMap<>();
                vendorMap.put(OLEConstants.VENDOR_HEADER_GENERATED_ID, Integer.parseInt(vendorHeaderGeneratedIdentifier));
                vendorMap.put(OLEConstants.VENDOR_DETAILED_ASSIGNED_ID, Integer.parseInt(vendorDetailAssignedIdentifier));
                List<VendorDetail> vendorDetailList = (List) getBusinessObjectService().findMatching(VendorDetail.class, vendorMap);
                if(vendorDetail != null && vendorDetailList.size() > 0){
                    return true;
                }
            }
            catch (NumberFormatException nfe) {
                return false;
            }
        }
        else {
            return false;
        }
        return isValidVendor;
    }

    public List<String> getItemLocation() {
        LOG.debug(" Inside get Item Location Method of Order Record Import");
        List<String> locationList = new ArrayList<String>();
        List<KeyValue> options = new ArrayList<KeyValue>();
        Map parentCriteria1 = new HashMap();
        parentCriteria1.put(OLEConstants.OLEBatchProcess.LEVEL_CODE, OLEConstants.LOC_LEVEL_SHELVING);
        List<OleLocationLevel> oleLocationLevel = (List<OleLocationLevel>) getBusinessObjectService().findMatching(OleLocationLevel.class, parentCriteria1);
        String shelvingId = oleLocationLevel.get(0).getLevelId();
        Map parentCriteria = new HashMap();
        parentCriteria.put(OLEConstants.LEVEL_ID, shelvingId);
        Collection<OleLocation> oleLocationCollection = getBusinessObjectService().findMatching(OleLocation.class, parentCriteria);
        for (OleLocation oleLocation : oleLocationCollection) {
            String locationName = oleLocation.getLocationName();
            String levelCode = oleLocation.getLocationCode();
            boolean parentId = oleLocation.getParentLocationId() != null ? true : false;
            while (parentId) {
                Map criteriaMap = new HashMap();
                criteriaMap.put(OLEConstants.LOCATION_ID, oleLocation.getParentLocationId());
                OleLocation location = getBusinessObjectService().findByPrimaryKey(OleLocation.class,
                        criteriaMap);
                if (locationName != null) {
                    locationName = location.getLocationName() + OLEConstants.SLASH + locationName;
                }
                if (levelCode != null) {
                    levelCode = location.getLocationCode() + OLEConstants.SLASH + levelCode;
                }
                parentId = location.getParentLocationId() != null ? true : false;
                oleLocation = location;
            }
            options.add(new ConcreteKeyValue(levelCode, levelCode));
        }
        Map<String, String> map = new HashMap<String, String>();
        for (KeyValue option : options) {
            map.put(option.getKey(), option.getValue());
        }
        Map<String, Object> map1 = sortByLocation(map);
        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            locationList.add((String) entry.getValue());
        }
        return locationList;
    }

    private Map<String, Object> sortByLocation(Map<String, String> parentCriteria) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<String> keyList = new ArrayList<String>(parentCriteria.keySet());
        List<String> valueList = new ArrayList<String>(parentCriteria.values());
        Set<String> sortedSet = new TreeSet<String>(valueList);
        Object[] sortedArray = sortedSet.toArray();
        int size = sortedArray.length;
        for (int i = 0; i < size; i++) {
            map.put(keyList.get(valueList.indexOf(sortedArray[i])), sortedArray[i]);
        }
        return map;
    }

    public List<OleTxRecord> getQuantityItemPartsLocation(List<BibMarcRecord> bibMarcRecords, OLEBatchProcessJobDetailsBo job) {
        OrderImportHelperBo orderImportHelperBo = job.getOrderImportHelperBo();
        List<BibMarcRecord> bibMarcRecordList = new ArrayList<>();
        List<OleTxRecord> oleTxRecordList = new ArrayList<>();
        for (BibMarcRecord bibMarcRecord : bibMarcRecords) {
            Map<String, String> mappingFailures = new HashMap<String, String>();
            OleTxRecord oleTxRecord = new OleTxRecord();
            List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsBoList = job.getOrderImportHelperBo().getOleBatchProcessProfileBo().getOleBatchProcessProfileMappingOptionsList();
            for (OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo : oleBatchProcessProfileMappingOptionsBoList) {
                List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = oleBatchProcessProfileMappingOptionsBo.getOleBatchProcessProfileDataMappingOptionsBoList();
                for (int dataMapCount = 0; dataMapCount < oleBatchProcessProfileDataMappingOptionsBoList.size(); dataMapCount++) {
                    String sourceField = oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getSourceField();
                    String sourceFields[] = sourceField.split("\\$");
                    if (sourceFields.length == 2) {
                        String dataField = sourceFields[0].trim();
                        String tagField = sourceFields[1].trim();
                        if (OLEConstants.OLEBatchProcess.QUANTITY.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String quantity = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                            if (!StringUtils.isBlank(quantity)) {
                                boolean validQuantity = validateForNumber(quantity);
                                if (!validQuantity) {
                                    mappingFailures.put(OLEConstants.OLEBatchProcess.QUANTITY, OLEConstants.INVALID_QTY + "  " + dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + quantity);
                                    quantity = null;
                                }
                                oleTxRecord.setQuantity(quantity);
                            } else {
                                mappingFailures.put(OLEConstants.OLEBatchProcess.QUANTITY, OLEConstants.REQUIRED_QTY + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        } else if (OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String itemNoOfParts = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                            if (!StringUtils.isBlank(itemNoOfParts)) {
                                boolean validNoOfParts = validateForNumber(itemNoOfParts);
                                if (!validNoOfParts) {
                                    mappingFailures.put(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS, OLEConstants.INVALID_NO_OF_PARTS + "  " + dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + itemNoOfParts);
                                    itemNoOfParts = null;
                                }
                                oleTxRecord.setItemNoOfParts(itemNoOfParts);
                            } else {
                                mappingFailures.put(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS, OLEConstants.REQUIRED_NO_OF_PARTS + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        } else if (OLEConstants.OLEBatchProcess.DEFAULT_LOCATION.equals(oleBatchProcessProfileDataMappingOptionsBoList.get(dataMapCount).getDestinationField())) {
                            String defaultLocation = setDataMappingValues(oleBatchProcessProfileDataMappingOptionsBoList, dataMapCount, bibMarcRecord, dataField, tagField);
                            if (!StringUtils.isBlank(defaultLocation)) {
                                boolean validDefaultLocation = validateDefaultLocation(defaultLocation);
                                if (!validDefaultLocation) {
                                    mappingFailures.put(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION, OLEConstants.INVALID_LOCN_NM + "  " + dataField + " " + OLEConstants.DELIMITER_DOLLAR + tagField + "  " + defaultLocation);
                                    defaultLocation = null;
                                }
                                oleTxRecord.setDefaultLocation(defaultLocation);
                            } else {
                                mappingFailures.put(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION, OLEConstants.REQUIRED_LOCATION_NM + " " + dataField + OLEConstants.DELIMITER_DOLLAR + tagField + " " + OLEConstants.NULL_VALUE_MESSAGE);
                            }
                        }
                    }
                }
            }

            List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsBoList = job.getOrderImportHelperBo().getOleBatchProcessProfileBo().getOleBatchProcessProfileConstantsList();
            for (OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo : oleBatchProcessProfileConstantsBoList) {
                if (OLEConstants.OLEBatchProcess.CONSTANT.equals(oleBatchProcessProfileConstantsBo.getDefaultValue())) {
                    if (OLEConstants.OLEBatchProcess.QUANTITY.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        if (mappingFailures.containsKey(OLEConstants.OLEBatchProcess.QUANTITY)) {
                            mappingFailures.remove(OLEConstants.OLEBatchProcess.QUANTITY);
                        }
                        oleTxRecord.setQuantity(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    } else if (OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        if (mappingFailures.containsKey(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS)) {
                            mappingFailures.remove(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS);
                        }
                        oleTxRecord.setItemNoOfParts(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    } else if (OLEConstants.OLEBatchProcess.DEFAULT_LOCATION.equals(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                        if (mappingFailures.containsKey(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION)) {
                            mappingFailures.remove(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION);
                        }
                        oleTxRecord.setDefaultLocation(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                } else if (OLEConstants.OLEBatchProcess.DEFAULT.equals(oleBatchProcessProfileConstantsBo.getDefaultValue())) {
                    if (OLEConstants.OLEBatchProcess.QUANTITY.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleTxRecord.getQuantity()) && !mappingFailures.containsKey(OLEConstants.OLEBatchProcess.QUANTITY)) {
                        oleTxRecord.setQuantity(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    } else if (OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleTxRecord.getItemNoOfParts()) && !mappingFailures.containsKey(OLEConstants.OLEBatchProcess.ITEM_NO_OF_PARTS)) {
                        oleTxRecord.setItemNoOfParts(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    } else if (OLEConstants.OLEBatchProcess.DEFAULT_LOCATION.equals(oleBatchProcessProfileConstantsBo.getAttributeName()) && StringUtils.isBlank(oleTxRecord.getDefaultLocation()) && !mappingFailures.containsKey(OLEConstants.OLEBatchProcess.DEFAULT_LOCATION)) {
                        oleTxRecord.setDefaultLocation(oleBatchProcessProfileConstantsBo.getAttributeValue());
                    }
                }
            }
            if (mappingFailures.size() == 0) {
                oleTxRecordList.add(oleTxRecord);

            } else {
                bibMarcRecordList.add(bibMarcRecord);
                Collection failureList = mappingFailures.values();
                if (failureList != null && failureList.size() > 0) {
                    //List reasonForFailure = (List) dataCarrierService.getData(OLEConstants.FAILURE_REASON);
                    List reasonForFailure = orderImportHelperBo.getFailureReason();
                    if (reasonForFailure == null) {
                        reasonForFailure = new ArrayList();
                    }
                    reasonForFailure.addAll(failureList);
                    //dataCarrierService.addData(OLEConstants.FAILURE_REASON, reasonForFailure);
                    orderImportHelperBo.setFailureReason(reasonForFailure);
                    failureList.clear();
                }
            }
        }
        bibMarcRecords.removeAll(bibMarcRecordList);
        return oleTxRecordList;
    }

    public List<String> getFailureRecords() {
        return failureRecords;
    }

    public void setFailureRecords(List<String> failureRecords) {
        this.failureRecords = failureRecords;
    }
}
