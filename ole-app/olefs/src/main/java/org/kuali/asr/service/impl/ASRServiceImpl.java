package org.kuali.asr.service.impl;

import org.apache.log4j.Logger;
import org.kuali.asr.ASRConstants;
import org.kuali.asr.bo.*;
import org.kuali.asr.service.ASRHelperServiceImpl;
import org.kuali.asr.service.ASRService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.ASRTypeRequest;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.ncip.bo.OLEPlaceRequest;
import org.kuali.ole.ncip.converter.OLEPlaceRequestConverter;
import org.kuali.ole.ncip.service.impl.OLECirculationServiceImpl;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 12/24/13
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class contains implementation of the services which were used by the exposed web services for ASR
 */
public class ASRServiceImpl implements ASRService {
    private static final Logger LOG = Logger.getLogger(ASRServiceImpl.class);
    private ASRHelperServiceImpl asrHelperService = getAsrHelperService();
    private BusinessObjectService businessObjectService=getBusinessObjectService();
    private OLECirculationServiceImpl oleCirculationService=getOleCirculationService();
    private OLEASRPlaceRequestHelperServiceImpl oleasrPlaceRequestHelperService=new OLEASRPlaceRequestHelperServiceImpl();
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;
    private LoanProcessor loanProcessor = new LoanProcessor();

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
         if(oleDeliverRequestDocumentHelperService==null){
             oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
         }
        return oleDeliverRequestDocumentHelperService ;
    }

    public void setOleDeliverRequestDocumentHelperService(OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService) {
        this.oleDeliverRequestDocumentHelperService = oleDeliverRequestDocumentHelperService;
    }




    public ASRHelperServiceImpl getAsrHelperService(){
        if(asrHelperService==null){
            asrHelperService = new ASRHelperServiceImpl();
        }
        return asrHelperService;
    }

    public BusinessObjectService getBusinessObjectService(){
        if(businessObjectService == null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public OLECirculationServiceImpl getOleCirculationService(){
        if(oleCirculationService == null){
            oleCirculationService = new OLECirculationServiceImpl();
        }
        return oleCirculationService;
    }



    /**
     * This method returns the ASRItems if there ia any item or return the ASRResponseBo if any error occurs
     * @param operatorId
     * @return object
     */
    @Override
    public Object lookupNewASRItems(String operatorId) {
        LOG.info("Inside the lookupNewASRItems  method . OperatorId : "+operatorId);
        //check whether the operator id send is a valid one or not
        if(!asrHelperService.isAuthorized(operatorId)){
            return asrHelperService.generateResponse(ASRConstants.OPERATOR_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.OPERATOR_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        //retrieve all the item whose location is changed to ASRLocation or item created with ASR Location
        ASRItems asrItems =  asrHelperService.getNewASRItems();
        if(asrItems.getAsrItems().size()>0){
            return asrItems;
        }else{
            return asrHelperService.generateResponse(ASRConstants.ITEM_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
    }

    /**
     * This method is used to remove an asr item and returns the ASRResponseBo with successful or failure message
     * @param barcode
     * @return  ASRResponseBo
     */
    @Override
    public ASRResponseBo removeASRItem(String barcode) {
        LOG.info("Inside the  removeASRItem  method . Item Barcode : "+barcode);
        //check whether the item is in the temporary table
        Map<String,String> itemMap = new HashMap<String,String>();
        itemMap.put("itemBarcode",barcode);
        List<org.kuali.ole.deliver.bo.ASRItem> asrItems = (List<org.kuali.ole.deliver.bo.ASRItem>)businessObjectService.findMatching(org.kuali.ole.deliver.bo.ASRItem.class,itemMap);
       if(asrItems.size() == 0){
           return asrHelperService.generateResponse(ASRConstants.ITEM_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
       }
       businessObjectService.delete(asrItems);
        //remove the item from the temporary table
        return asrHelperService.generateResponse(ASRConstants.ITEM_DELETED_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_DELETED) , ASRConstants.SUCCESS_STATUS);
    }

    /**
     * This method updates the itemStatus to available and returns the ASRResponseBo only if there is any error otherwise it will return null
     * @param updateASRItemRequestBo
     * @return object
     */
    @Override
    public Object updateASRItemStatusToAvailable(UpdateASRItemRequestBo updateASRItemRequestBo) {
        LOG.info("Inside the updateASRItemStatusToAvailable  method . Item Barcode : " + updateASRItemRequestBo.getItemBarcode());
        //identify whether the item barcode send is valid one or not
        if(!asrHelperService.isAuthorized(updateASRItemRequestBo.getOperatorId())){
            return asrHelperService.generateResponse(ASRConstants.OPERATOR_NOT_FOUND_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.OPERATOR_NOT_FOUND) , ASRConstants.SUCCESS_STATUS);
        }
        if(!(updateASRItemRequestBo.getItemStatus().equals(asrHelperService.getParameter(ASRConstants.ASR_ITEM_AVAILABLE)))){
            return asrHelperService.generateResponse(ASRConstants.STATUS_NOT_MATCH_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.STATUS_NOT_MATCH), ASRConstants.SUCCESS_STATUS);
        }
        List<String> resultList = new ArrayList<String>();
        resultList.add(ASRConstants.LOCATION_LEVEL_DISPLAY);
        String itemLocation = asrHelperService.getLocation(updateASRItemRequestBo.getItemBarcode());
        if(itemLocation == null){
            return asrHelperService.generateResponse(ASRConstants.ITEM_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        if(itemLocation!=null &&!asrHelperService.isAnASRItem(itemLocation)){
            return asrHelperService.generateResponse(ASRConstants.NOT_ASR_ITEM_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.NOT_ASR_ITEM), ASRConstants.SUCCESS_STATUS);
        }
        //update the item status according to the ststus defined in the input
        if(!asrHelperService.updateItemStatus(updateASRItemRequestBo.getItemBarcode(),updateASRItemRequestBo.getItemStatus())) {
            return asrHelperService.generateResponse(ASRConstants.ITEM_STATUS_NOT_UPDATED_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_STATUS_NOT_UPDATED), ASRConstants.SUCCESS_STATUS);
        }else{
            removeASRItem(updateASRItemRequestBo.getItemBarcode());
        }
        return null;
    }

    /**
     * This method place a request to the asr item with the request type as ASR and returns the ASRResponseBo with  success or failure message
     * @param placeRequestASRItemBo
     * @return ASRResponseBo
     */
    @Override
    public ASRResponseBo placeRequestOnASRItem(PlaceASRItemRequestBo placeRequestASRItemBo) {
        LOG.info("Inside the  placeRequestOnASRItem  method . Item Barcode : "+placeRequestASRItemBo.getItemBarcode() );
        //identify whether the item barcode send is valid one or not
        if(!asrHelperService.isAuthorized(placeRequestASRItemBo.getOperatorId())){
            return asrHelperService.generateResponse(ASRConstants.OPERATOR_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.OPERATOR_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        //verify whether the item is an asr item
        String itemLocation = asrHelperService.getLocation(placeRequestASRItemBo.getItemBarcode());
        if(itemLocation == null){
            return asrHelperService.generateResponse(ASRConstants.ITEM_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        if(itemLocation!=null &&!asrHelperService.isAnASRItem(itemLocation)){
            return asrHelperService.generateResponse(ASRConstants.NOT_ASR_ITEM_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.NOT_ASR_ITEM), ASRConstants.SUCCESS_STATUS);
        }
        if(!asrHelperService.validatePickupLocation(placeRequestASRItemBo.getPickUpLocation())){
            return asrHelperService.generateResponse(ASRConstants.INVALID_PKUP_LOCN_CD, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.INVALID_PKUP_LOCN), ASRConstants.SUCCESS_STATUS);
        }
        //place the request to the asr item
        String requestType = loanProcessor.getParameter(ASRConstants.ASR_TYP_RQST);
         //String message = oleasrPlaceRequestHelperService.placeRequest(placeRequestASRItemBo.getPatronId(),placeRequestASRItemBo.getOperatorId(),placeRequestASRItemBo.getItemBarcode(),"ASR Request",placeRequestASRItemBo.getPickUpLocation());
         String message = getOleDeliverRequestDocumentHelperService().placeRequest(placeRequestASRItemBo.getPatronId(),placeRequestASRItemBo.getOperatorId(),placeRequestASRItemBo.getItemBarcode(),requestType,placeRequestASRItemBo.getPickUpLocation(),null,null,null,null,null,null,false,null,OLEConstants.ITEM_LEVEL,null, placeRequestASRItemBo.getRequestNote());
        //update the asr response with the corresponding code according to the circulation policy message
        OLEPlaceRequestConverter olePlaceRequestConverter = new OLEPlaceRequestConverter();
        OLEPlaceRequest olePlaceRequest = (OLEPlaceRequest)olePlaceRequestConverter.generatePlaceRequestObject(message);
        message = olePlaceRequest.getMessage();
        if(message.contains(OLEConstants.RQST_SUCCESS)){
            return asrHelperService.generateResponse(ASRConstants.HOLD_PLACED_CODE, message, ASRConstants.SUCCESS_STATUS);
        }
          return asrHelperService.generateResponse("106",message,ASRConstants.SUCCESS_STATUS);
    }

    /**
     * This method is used to cancel the ASRRequest and return the ASRSResponseBO with the success or failure message
     * @param holdId
     * @param operatorId
     * @return ASRResponseBo
     */
    @Override
    public ASRResponseBo cancelASRRequest(String holdId, String operatorId) {
        LOG.info("Inside the ASRResponseBo  method . Hold Id : "+holdId + ". OperatorId : " +operatorId);
        //check the operator id send is an operator
        if(!asrHelperService.isAuthorized(operatorId)){
            return asrHelperService.generateResponse(ASRConstants.OPERATOR_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.OPERATOR_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        //check whether the hold id is present
        //it should be cancelled if the request status is 1
        Map<String,String> requestMap = new HashMap<String,String>();
        requestMap.put("requestId",holdId);
        requestMap.put("requestStatus","1");
        OleDeliverRequestBo oleDeliverRequestBo=null;
        List<OleDeliverRequestBo> oleDeliverRequestBos = asrHelperService.getDeliverRequest(requestMap);
       if(oleDeliverRequestBos ==  null ||(oleDeliverRequestBos!=null && oleDeliverRequestBos.size()==0)){
           return asrHelperService.generateResponse(ASRConstants.REQUEST_NOT_FOUND_FOR_HOLD_ID_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.REQUEST_NOT_FOUND_FOR_HOLD_ID) +" "+holdId, ASRConstants.SUCCESS_STATUS);
       }else if((oleDeliverRequestBos!=null && oleDeliverRequestBos.size()>0) && businessObjectService.findMatching(ASRTypeRequest.class,requestMap).size()>0){
           //businessObjectService.delete(oleDeliverRequestBos.get(0));
           getOleDeliverRequestDocumentHelperService().cancelDocument(oleDeliverRequestBos.get(0));
           return asrHelperService.generateResponse(ASRConstants.REQUEST_CANCELED_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.REQUEST_CANCELED) , ASRConstants.SUCCESS_STATUS);
       } else{
           return asrHelperService.generateResponse("109","Hold is processed",404);
       }
    }

    /**
     * This method is used to retrieve the asrRequests .and returns the ASRSRequests
     * @param operatorId
     * @param asrLocation
     * @return  ASRRequests
     */
    @Override
    public Object lookupASRTypeRequest(String operatorId, String asrLocation) {
        LOG.info("Inside the  lookupASRTypeRequest  method . OperatorId : "+operatorId +". ASRLocation :" +asrLocation);
        //check the operator id send is an operator
        if(!asrHelperService.isAuthorized(operatorId)){
            return asrHelperService.generateResponse(ASRConstants.OPERATOR_NOT_FOUND_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.OPERATOR_NOT_FOUND) , ASRConstants.SUCCESS_STATUS);
        }
        //return the request with the request type as ASR with request status = 1
        ASRRequests asrRequests= asrHelperService.getASRTypeRequest("1");
        if(asrRequests!=null)
            asrRequests=asrHelperService.getASRRequestBasedOnLocation(asrRequests,asrLocation);
        if(asrRequests!=null && asrRequests.getAsrRequests()!=null && asrRequests.getAsrRequests().size()>0){
            return asrRequests;
        }else{
            return asrHelperService.generateResponse(ASRConstants.REQUEST_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.REQUEST_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
       //return
    }

    /**
     * This method is used to update the request status and returns ASRResponseBo only if error occurs
     * @param updateASRRequestStatusBo
     * @return Object
     */
    @Override
    public Object updateASRRequestStatus(UpdateASRRequestStatusBo updateASRRequestStatusBo) {
        LOG.info("Inside the  updateASRItemStatusToMissing method . Request Id  : "+updateASRRequestStatusBo.getHoldId() + ". OperatorId : "+updateASRRequestStatusBo.getOperatorId()  );
         ASRTypeRequest asrTypeRequest;
        //check the operator id send is an operator
        if(!asrHelperService.isAuthorized(updateASRRequestStatusBo.getOperatorId())){
            return asrHelperService.generateResponse(ASRConstants.OPERATOR_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.OPERATOR_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        if((updateASRRequestStatusBo.getStatus().equals(ASRConstants.ASR_REQUEST_IN_PROCESS)) || updateASRRequestStatusBo.getStatus().equals(ASRConstants.ASR_REQUEST_FAILURE)){

        } else{
            return asrHelperService.generateResponse(ASRConstants.STATUS_NOT_MATCH_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.STATUS_NOT_MATCH), ASRConstants.SUCCESS_STATUS);
        }
        //check whether the hold id is present
        List<ASRTypeRequest> asrTypeRequests = getAsrHelperService().getASRRequest("requestId",updateASRRequestStatusBo.getHoldId());
        if(asrTypeRequests==null || (asrTypeRequests!=null && asrTypeRequests.size()==0)){
            return asrHelperService.generateResponse(ASRConstants.REQUEST_NOT_FOUND_FOR_HOLD_ID_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.REQUEST_NOT_FOUND_FOR_HOLD_ID) +" "+updateASRRequestStatusBo.getHoldId(), ASRConstants.SUCCESS_STATUS);
        }else {
            asrTypeRequest = asrTypeRequests.get(0);
        }
        //update the status as defined in the input
        if(!asrHelperService.updateRequestStatus(asrTypeRequest,updateASRRequestStatusBo.getHoldId(),updateASRRequestStatusBo.getStatus())){
            return asrHelperService.generateResponse(ASRConstants.ITEM_STATUS_NOT_UPDATED_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_STATUS_NOT_UPDATED), ASRConstants.SUCCESS_STATUS);
        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This method is used to update the request status and returns ASRResponseBo only if error occurs
     * @param updateASRItemStatusBo
     * @return Object
     */
    @Override
    public Object updateASRItemStatusToBeingRetrieved(UpdateASRItemStatusBo updateASRItemStatusBo) {
        LOG.info("Inside the  updateASRItemStatusToMissing method . Item Barcode : "+updateASRItemStatusBo.getItemBarcode() + ". OperatorId : "+updateASRItemStatusBo.getOperatorId()  );
        //check the operator id send is an operator
        if(!asrHelperService.isAuthorized(updateASRItemStatusBo.getOperatorId())){
            return asrHelperService.generateResponse(ASRConstants.OPERATOR_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.OPERATOR_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        if(!(updateASRItemStatusBo.getItemStatus().equals(asrHelperService.getParameter(ASRConstants.ASR_ITEM_RETRIVED)))){
            return asrHelperService.generateResponse(ASRConstants.STATUS_NOT_MATCH_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.STATUS_NOT_MATCH), ASRConstants.SUCCESS_STATUS);
        }
        //identify whether the item barcode send is valid one or not
        String itemLocation = asrHelperService.getLocation(updateASRItemStatusBo.getItemBarcode());
        if(itemLocation == null){
            return asrHelperService.generateResponse(ASRConstants.ITEM_NOT_FOUND_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_NOT_FOUND) , ASRConstants.SUCCESS_STATUS);
        }
        //verify whether the item is an asr item
        if(itemLocation!=null &&!asrHelperService.isAnASRItem(itemLocation)){
            return asrHelperService.generateResponse(ASRConstants.NOT_ASR_ITEM_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.NOT_ASR_ITEM), ASRConstants.SUCCESS_STATUS);
        }
        //update the item status according to the status defined in the input
        if(!asrHelperService.updateItemStatus(updateASRItemStatusBo.getItemBarcode(),updateASRItemStatusBo.getItemStatus())) {
            return asrHelperService.generateResponse(ASRConstants.ITEM_STATUS_NOT_UPDATED_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_STATUS_NOT_UPDATED) , ASRConstants.SUCCESS_STATUS);
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This method is used to update the request status and returns ASRResponseBo only if error occurs
     * @param updateASRItemStatusBo
     * @return Object
     */
    @Override
    public Object updateASRItemStatusToMissing(UpdateASRItemStatusBo updateASRItemStatusBo) {
        LOG.info("Inside the  updateASRItemStatusToMissing method . Item Barcode : "+updateASRItemStatusBo.getItemBarcode() + ". OperatorId : "+updateASRItemStatusBo.getOperatorId()  );
        //check the operator id send is an operator
        if(!asrHelperService.isAuthorized(updateASRItemStatusBo.getOperatorId())){
            return asrHelperService.generateResponse(ASRConstants.OPERATOR_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.OPERATOR_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        if(!(updateASRItemStatusBo.getItemStatus().equals(asrHelperService.getParameter(ASRConstants.ASR_ITEM_MISSING)))){
            return asrHelperService.generateResponse(ASRConstants.STATUS_NOT_MATCH_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.STATUS_NOT_MATCH), ASRConstants.SUCCESS_STATUS);
        }
        String itemLocation = asrHelperService.getLocation(updateASRItemStatusBo.getItemBarcode());
        if(itemLocation == null){
            return asrHelperService.generateResponse(ASRConstants.ITEM_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        //verify whether the item is an asr item
        if(itemLocation!=null &&!asrHelperService.isAnASRItem(itemLocation)){
            return asrHelperService.generateResponse(ASRConstants.NOT_ASR_ITEM_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.NOT_ASR_ITEM), ASRConstants.SUCCESS_STATUS);
        }
        //update the item status according to the status defined in the input
        if(!asrHelperService.updateItemStatus(updateASRItemStatusBo.getItemBarcode(),updateASRItemStatusBo.getItemStatus())) {
            return asrHelperService.generateResponse(ASRConstants.ITEM_STATUS_NOT_UPDATED_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_STATUS_NOT_UPDATED), ASRConstants.SUCCESS_STATUS);
        }
        //identify the holds for the
         asrHelperService.deleteASRRequest(updateASRItemStatusBo.getItemBarcode());
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This method is used to create a new ASRItem and returns the ASRResponseBo with the success or failure message
     * @param asrItem
     * @return ASRResponseBo
     */
    @Override
    public ASRResponseBo addNewASRItem(ASRItem asrItem) {
        LOG.info("Inside the addNewASRItem  method . Item Barcode : "+asrItem.getItemBarcode());
        //create ASR Item
        //check item barcode is valid one
        String itemLocation = asrHelperService.getLocation(asrItem.getItemBarcode());
        if(itemLocation == null){
            return asrHelperService.generateResponse(ASRConstants.ITEM_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        //verify whether the item is an asr item
        if(itemLocation!=null &&!asrHelperService.isAnASRItem(itemLocation)){
            return asrHelperService.generateResponse(ASRConstants.NOT_ASR_ITEM_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.NOT_ASR_ITEM), ASRConstants.SUCCESS_STATUS);
        }
        if(asrHelperService.saveASRItem(asrItem)){
            return asrHelperService.generateResponse(ASRConstants.SUCESS_REQUEST_CD,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.SUCESS_ITEM_MESG),ASRConstants.SUCCESS_STATUS);
        }


        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     *  This method is used to create a new ASRItem and returns the ASRResponseBo with the success or failure message
     * @param asrRequestBo
     * @return ASRResponseBo
     */
    @Override
    public ASRResponseBo sendASRRequest(ASRRequestBo asrRequestBo) {
        LOG.info("Inside the  sendASRRequest method . Item Barcode : "+asrRequestBo.getItemBarcode());
        //check item barcode is valid one
        String itemLocation = asrHelperService.getLocation(asrRequestBo.getItemBarcode());
        if(itemLocation == null){
            return asrHelperService.generateResponse(ASRConstants.ITEM_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        //verify whether the item is an asr item
        if(itemLocation!=null &&!asrHelperService.isAnASRItem(itemLocation)){
            return asrHelperService.generateResponse(ASRConstants.NOT_ASR_ITEM_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.NOT_ASR_ITEM), ASRConstants.SUCCESS_STATUS);
        }
        //check patron barcode valid one
            if(!asrHelperService.validatePatron(asrRequestBo.getPatronBarcode())){
                return asrHelperService.generateResponse(ASRConstants.PATRON_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.PATRON_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
            }

        //check pick up location a valid one
         if(!asrHelperService.validatePickupLocation(asrRequestBo.getPickupLocation())){
             return asrHelperService.generateResponse(ASRConstants.INVALID_PKUP_LOCN_CD, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.INVALID_PKUP_LOCN), ASRConstants.SUCCESS_STATUS);
         }
        //place a request with request type asr
        if(asrHelperService.saveASRRequest(asrRequestBo)){
            return asrHelperService.generateResponse(ASRConstants.SUCESS_REQUEST_CD,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.SUCESS_REQUEST_MESG),ASRConstants.SUCCESS_STATUS);
        }else{
            return asrHelperService.generateResponse("",ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.FAILURE_REQUEST_MESG),ASRConstants.FAILURE_STATUS);
        }
    }

    /**
     * This method is used to retrieve the request for the provided item barcode and returns the  LookupASRRequestResponseBO with the request details
     * @param operatorId
     * @param itemBarcode
     * @return LookupASRRequestResponseBO
     */
    @Override
    public Object lookupAsrRequest(String operatorId,String itemBarcode) {
        LOG.info("Inside the lookupAsrRequest method . Operator Id : "+operatorId +". Item Barcode : "+itemBarcode);
        LookupASRRequestResponseBO lookupASRRequestResponseBO = new LookupASRRequestResponseBO();

        //check the operator id send is an operator
        if(!asrHelperService.isAuthorized(operatorId)){
            return asrHelperService.generateResponse(ASRConstants.OPERATOR_NOT_FOUND_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.OPERATOR_NOT_FOUND) , ASRConstants.SUCCESS_STATUS);
        }
        //check item barcode as valid one
        String itemLocation = asrHelperService.getLocation(itemBarcode);
        if(itemLocation == null){
            return asrHelperService.generateResponse(ASRConstants.ITEM_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        //verify whether the item is an asr item
        if(itemLocation!=null &&!asrHelperService.isAnASRItem(itemLocation)){
            return asrHelperService.generateResponse(ASRConstants.NOT_ASR_ITEM_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.NOT_ASR_ITEM), ASRConstants.SUCCESS_STATUS);
        }
        ASRRequestDetailsBo asrRequestDetailsBo = asrHelperService.getRequests(itemBarcode);
        if(asrRequestDetailsBo==null){
            /*lookupASRRequestResponseBO.setCode("000");
            lookupASRRequestResponseBO.setMessage("Successful query, holds not found");
            lookupASRRequestResponseBO.setStatusCode(200);*/
            lookupASRRequestResponseBO.setCode(ASRConstants.REQUEST_NOT_FOUND_CODE);
            lookupASRRequestResponseBO.setMessage(ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.REQUEST_NOT_FOUND));
            lookupASRRequestResponseBO.setStatusCode(ASRConstants.SUCCESS_STATUS);
        } else if(asrRequestDetailsBo!=null && asrRequestDetailsBo.getAsrRequestDetailBos()!=null && asrRequestDetailsBo.getAsrRequestDetailBos().size()>0){
            lookupASRRequestResponseBO.setCode("000");
            lookupASRRequestResponseBO.setMessage("Successful query, holds found");
            lookupASRRequestResponseBO.setStatusCode(ASRConstants.SUCCESS_STATUS);
            lookupASRRequestResponseBO.setAsrRequestDetailsBo(asrRequestDetailsBo);
            return lookupASRRequestResponseBO;
        }
        return lookupASRRequestResponseBO;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     *  This method is used to change the item status and returns the ASRResponseBo with the success or failure message
     * @param receiveTransitRequestBo
     * @return  ASRResponseBo
     */
    @Override
    public ASRResponseBo receiveASRItemTransit(ReceiveTransitRequestBo receiveTransitRequestBo) {
        LOG.info("Inside the  receiveASRItemTransit  method . Barcode : "+receiveTransitRequestBo.getBarcode() + " Operator Id : "+receiveTransitRequestBo.getOperatorId());
        //check the operator id send is an operator
        if(!asrHelperService.isAuthorized(receiveTransitRequestBo.getOperatorId())){
            return asrHelperService.generateResponse(ASRConstants.OPERATOR_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.OPERATOR_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        //check item barcode as valid one
        String itemLocation = asrHelperService.getLocation(receiveTransitRequestBo.getBarcode());
        if(itemLocation == null){
            return asrHelperService.generateResponse(ASRConstants.ITEM_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        //verify whether the item is an asr item
        if(itemLocation!=null &&!asrHelperService.isAnASRItem(itemLocation)){
            return asrHelperService.generateResponse(ASRConstants.NOT_ASR_ITEM_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.NOT_ASR_ITEM), ASRConstants.SUCCESS_STATUS);
        }
        if(!asrHelperService.isAuthorized(receiveTransitRequestBo.getOperatorId())){
            return asrHelperService.generateResponse(ASRConstants.OPERATOR_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.OPERATOR_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        //check whether the operator circulation desk and the item home location is a valid one
        boolean validLocation = asrHelperService.validateLocation(receiveTransitRequestBo.getOperatorId(),itemLocation);
        if(validLocation){
            if(asrHelperService.updateItemStatusForInTransit(receiveTransitRequestBo.getBarcode(),ASRConstants.AVAILABLE))
                return asrHelperService.generateResponse(ASRConstants.ITEM_STATUS_UPDATED_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_STATUS_UPDATED), ASRConstants.SUCCESS_STATUS);
            else
                return asrHelperService.generateResponse(ASRConstants.ITEM_NOT_IN_TRANSIT_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_NOT_IN_TRANSIT), ASRConstants.SUCCESS_STATUS);
        }else{
            return asrHelperService.generateResponse(ASRConstants.LOCATION_MISMATCH_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.LOCATION_MISMATCH),ASRConstants.SUCCESS_STATUS);
        }
    }

    /**
     * This method is used the change the item status of the item during the check-in process and returns the ASRResponseBo with success of failure message
     * @param asrCheckInBo
     * @return  ASRResponseBo
     */
    @Override
    public ASRResponseBo checkInASRItem(ASRCheckInBo asrCheckInBo) {
        LOG.info("Inside the  checkInASRItem  method : Item Barcode : "+asrCheckInBo.getItemBarcode());
        //check whether item barcode is a valid one and asr item
        String itemLocation = asrHelperService.getLocation(asrCheckInBo.getItemBarcode());
        if(itemLocation == null){
            return asrHelperService.generateResponse(ASRConstants.ITEM_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }
        //verify whether the item is an asr item
        if(itemLocation!=null &&!asrHelperService.isAnASRItem(itemLocation)){
            return asrHelperService.generateResponse(ASRConstants.NOT_ASR_ITEM_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.NOT_ASR_ITEM), ASRConstants.SUCCESS_STATUS);
        }
        //identify any hold
        OleDeliverRequestBo oleDeliverRequestBo = asrHelperService.getPrioritizedRequest(asrCheckInBo.getItemBarcode());
        //if the hold pick up location and item location are same then change the status to hold
        if(oleDeliverRequestBo!=null){
            boolean validLocation=asrHelperService.isCirculationDesksLocation(itemLocation,oleDeliverRequestBo.getPickUpLocationId());
            if(validLocation){
                asrHelperService.updateItemStatus(asrCheckInBo.getItemBarcode(), ASRConstants.ON_HOLD);
                return asrHelperService.generateResponse("200",ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_STATUS_UPDATED_HOLD)+asrCheckInBo.getItemBarcode(),ASRConstants.SUCCESS_STATUS);
            }else if(!validLocation){
                asrHelperService.updateItemStatus(asrCheckInBo.getItemBarcode(), ASRConstants.INTRANSIT_FOR_HOLD);
                return asrHelperService.generateResponse(ASRConstants.ITEM_STATUS_UPDATED_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.ITEM_STATUS_UPDATED)+" "+asrCheckInBo.getItemBarcode(), ASRConstants.SUCCESS_STATUS);

            }
        }else{
            return asrHelperService.generateResponse(ASRConstants.REQUEST_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.REQUEST_NOT_FOUND), ASRConstants.SUCCESS_STATUS);
        }

        //if not change the status to in transit
        return asrHelperService.generateResponse(ASRConstants.REQUEST_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.REQUEST_NOT_FOUND), ASRConstants.SUCCESS_STATUS);  //To change body of implemented methods use File | Settings | File Templates.
    }


}
