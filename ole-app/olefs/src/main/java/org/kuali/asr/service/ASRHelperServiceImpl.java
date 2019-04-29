package org.kuali.asr.service;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.kuali.asr.ASRConstants;
import org.kuali.asr.bo.*;
import org.kuali.asr.bo.ASRItem;
import org.kuali.asr.service.impl.OLEASRPlaceRequestHelperServiceImpl;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.ItemStatus;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.model.bo.WorkItemDocument;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 1/8/14
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */

public class ASRHelperServiceImpl {
    private static final Logger LOG = Logger.getLogger(ASRHelperServiceImpl.class);
    private BusinessObjectService businessObjectService=getBusinessObjectService();
    private DocstoreClientLocator docstoreClientLocator=getDocstoreClientLocator();
    private OLEASRPlaceRequestHelperServiceImpl oleasrPlaceRequestHelperService=new OLEASRPlaceRequestHelperServiceImpl();
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = getOleDeliverRequestDocumentHelperService();

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return (DocstoreClientLocator) SpringContext.getService("docstoreClientLocator");
        }
        return docstoreClientLocator;
    }

    public BusinessObjectService getBusinessObjectService(){
        if(businessObjectService == null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if(oleDeliverRequestDocumentHelperService==null){
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }
    //  private OLEASRPlaceRequestHelperServiceImpl oleasrPlaceReqestHelperService=new OLEASRPlaceRequestHelperServiceImpl();


    public boolean isAuthorized(String principalId) {
        /*PermissionService service = KimApiServiceLocator.getPermissionService();
        return service.hasPermission(principalId, "OLE-PTRN", "Mapping Circulation Desk");*/
        return oleasrPlaceRequestHelperService.processOperator(principalId);
    }

    public boolean validatePatron(String patronId){
        Map<String, String> patronMap = new HashMap<String, String>();
        patronMap.put("olePatronId", patronId);
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
        return (olePatronDocumentList!=null && olePatronDocumentList.size()>0);
    }

    public boolean validatePickupLocation(String pickupLocation){
        Map<String,String> circulationDeskMap = new HashMap<String,String>();
        circulationDeskMap.put("circulationDeskCode",pickupLocation);
        circulationDeskMap.put("asrPickupLocation","true");
        List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>)KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDesk.class,circulationDeskMap);
        return (oleCirculationDeskList!=null && oleCirculationDeskList.size()>0);
    }

    public SearchParams buildSearchParams(Map<String, String> queryParams) {
        SearchParams searchParams = new SearchParams();
        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        List<SearchCondition>  searchConditions = new ArrayList<>();
        if (null != queryParams) {
            for (Iterator<String> keys = queryParams.keySet().iterator(); keys.hasNext(); ) {
                String key = keys.next();
                String value = queryParams.get(key);
                if(org.apache.commons.lang.StringUtils.isNotEmpty(value)) {
                    //searchConditions.add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField("item", key, value), "AND"));
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField("item", key, value), "AND"));
                }
            }
        }
        return searchParams;
    }

    public ASRResponseBo generateResponse(String code, String message, int statusCode){
        ASRResponseBo asrResponseBo=new ASRResponseBo();
        asrResponseBo.setCode(code);
        asrResponseBo.setMessage(message);
        asrResponseBo.setStatusCode(statusCode);
        return asrResponseBo;
    }

    public ASRItems generateASRItems(List<WorkItemDocument> workItemDocumentList){
        ASRItems asrItems=new ASRItems();
        List<ASRItem> asrItemList=new ArrayList<>();
        ASRItem asrItem=null;
        for(WorkItemDocument workItemDocument:workItemDocumentList){
            asrItem=new ASRItem();
            asrItem.setAuthor(workItemDocument.getItemStatus());
            asrItem.setCallNumber(workItemDocument.getCallNumber());
            asrItem.setItemBarcode(workItemDocument.getBarcode());
            asrItem.setTitle(workItemDocument.getBibTitle());
            asrItemList.add(asrItem);
        }
        asrItems.setAsrItems(asrItemList);
        return asrItems;
    }




    public boolean updateItemStatus(Item oleItem, String itemStatus) throws Exception {
        LOG.debug("Inside the updateItemStatus method");
        if(oleItem==null){
            return false;
        }
        try {
            String itemUuid = oleItem.getItemIdentifier();
            String itemXmlContent = buildItemContentWithItemStatus(oleItem, itemStatus);
            LOG.info("itemXmlContent" + itemXmlContent);
            String itemRecordUpdateResponse =  "";
                //    getDocstoreHelperService().updateInstanceRecord(itemUuid, OLEConstants.ITEM_DOC_TYPE, itemXmlContent);
            LOG.info(itemRecordUpdateResponse);
            return true;

        } catch (Exception e) {
            LOG.error(OLEConstants.ITM_STS_TO_DOC_FAIL + e, e);
            return false;

        }
    }

    public String buildItemContentWithItemStatus(Item oleItem, String itemStatus) throws Exception {
        LOG.debug("Inside the buildItemContentWithItemStatus method");
        ItemStatus itemStatus1 = new ItemStatus();
        itemStatus1.setCodeValue(itemStatus);
        itemStatus1.setFullValue(itemStatus);
        oleItem.setItemStatus(itemStatus1);
        oleItem.setItemStatusEffectiveDate(String.valueOf(new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE).format(new Date())));
        String itemContent = new ItemOlemlRecordProcessor().toXML(oleItem);
        return itemContent;
    }




    public Object cancelRequest(String operatorId,String requestId){
        if(!isAuthorized(operatorId)){
            return generateResponse(ASRConstants.OPERATOR_NOT_FOUND_CODE, ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.OPERATOR_NOT_FOUND),ASRConstants.OPERATOR_NOT_FOUND_STATUS);
        }
        Map<String,String> requestMap = new HashMap<String,String>();
        requestMap.put("requestId",requestId);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>)businessObjectService.findMatching(OleDeliverRequestBo.class,requestMap);
        if(oleDeliverRequestBoList!=null && oleDeliverRequestBoList.size()>0){
            OleDeliverRequestBo oleDeliverRequestBo = oleDeliverRequestBoList.get(0);
            oleDeliverRequestBo.setOperatorCreateId(operatorId);
            if(!true){
                return generateResponse(ASRConstants.REQUEST_NOT_FOUND_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.REQUEST_NOT_FOUND),ASRConstants.REQUEST_NOT_FOUND_STATUS);
            }
          //  oleasrPlaceReqestHelperService.cancelDocument(oleDeliverRequestBo);
            return generateResponse(ASRConstants.REQUEST_CANCELED_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.REQUEST_CANCELED),ASRConstants.REQUEST_CANCELED_STATUS);
        }  else{
            return generateResponse(ASRConstants.REQUEST_NOT_FOUND_CODE,ConfigContext.getCurrentContextConfig().getProperty(ASRConstants.REQUEST_NOT_FOUND),ASRConstants.REQUEST_NOT_FOUND_STATUS);
        }
    }

    public String getParameter(String name) {
        String parameter = "";
        try {
            Map<String, String> criteriaMap = new HashMap<String, String>();
            criteriaMap.put("namespaceCode", OLEConstants.DLVR_NMSPC);
            criteriaMap.put("componentCode", OLEConstants.DLVR_CMPNT);
            criteriaMap.put("name", name);
            List<ParameterBo> parametersList = (List<ParameterBo>) businessObjectService.findMatching(ParameterBo.class, criteriaMap);
            for (ParameterBo parameterBo : parametersList) {
                parameter = parameterBo.getValue();
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return parameter;
    }




   public ASRItems getNewASRItems(){
      ASRItems asrItems = new ASRItems();
       List<ASRItem> asrItemList = new ArrayList<ASRItem>();
       List<org.kuali.ole.deliver.bo.ASRItem> asrItemsList = (List<org.kuali.ole.deliver.bo.ASRItem>)businessObjectService.findAll(org.kuali.ole.deliver.bo.ASRItem.class);
      ASRItem asrItem;
       if(asrItemsList.size()>0){
           for(org.kuali.ole.deliver.bo.ASRItem asrItemBo : asrItemsList){
              asrItem = new ASRItem();
               asrItem.setItemBarcode(asrItemBo.getItemBarcode());
               asrItem.setTitle(asrItemBo.getTitle());
               asrItem.setAuthor(asrItemBo.getAuthor());
               asrItem.setCallNumber(asrItemBo.getCallNumber());
               asrItemList.add(asrItem);
           }
       }
       asrItems.setAsrItems(asrItemList);
       if(asrItemList!=null && asrItemList.size()>0){
           businessObjectService.delete(asrItemsList);
       }
       return asrItems;
   }


   public Map<String, String> getItemFieldValue(String searchField, String searchFieldValue, List<String> resultFields) {
       Map<String, String> resultMap = new HashMap<>();
       Map<String,String> searchParamsMap = new HashMap<String,String>();
       searchParamsMap.put(searchField,searchFieldValue);
       SearchParams searchParams = buildSearchParams(searchParamsMap);
       for(String resultField :resultFields) {
             searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", resultField));
       }
       try {

       SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
       for(SearchResult searchResult : searchResponse.getSearchResults()){
           for(SearchResultField searchResultField : searchResult.getSearchResultFields()) {
               if(searchResultField.getDocType().equalsIgnoreCase(DocType.ITEM.getCode())) {
                   for(String resultField : resultFields) {
                       if(searchResultField.getFieldName().equalsIgnoreCase(resultField)) {
                           resultMap.put(resultField, searchResultField.getFieldValue());
                       }
                   }
               }
           }
       }
       }catch(Exception e){
           Log.error(e,e);
       }
       return resultMap;
   }

    public Map<String, String> getHoldingFieldValue(String searchField, String searchFieldValue, List<String> resultFields) {
        Map<String, String> resultMap = new HashMap<>();
        Map<String,String> searchParamsMap = new HashMap<String,String>();
        searchParamsMap.put(searchField,searchFieldValue);
        SearchParams searchParams = buildSearchParamsForHoldings(searchParamsMap);
        for(String resultField :resultFields) {
            searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings", resultField));
        }
        try {

            SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            for(SearchResult searchResult : searchResponse.getSearchResults()){
                for(SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    if(searchResultField.getDocType().equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
                        for(String resultField : resultFields) {
                            if(searchResultField.getFieldName().equalsIgnoreCase(resultField)) {
                                resultMap.put(resultField, searchResultField.getFieldValue());
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            Log.error(e,e);
        }
        return resultMap;
    }


    public org.kuali.ole.docstore.common.document.Item getItem(String itemUUID){
        org.kuali.ole.docstore.common.document.Item item = null;
        try {
            item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemUUID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public org.kuali.ole.docstore.common.document.Item getItemUsingBarcode(String barcode){
        org.kuali.ole.docstore.common.document.Item item = null;
        List<String> searchList = new ArrayList<String>();
        searchList.add("id");
        Map<String,String> resultMap =  getItemFieldValue("ItemBarcode_display", barcode, searchList);
        String itemUUID = resultMap.get("id");
        if(itemUUID!=null){
            item = getItem(itemUUID);
        }
        return item;
    }

    public boolean isAnASRItem(String itemLocation) {
        boolean asrItem = false;
        String[] libraryArray = getParameter("ASR_LOCATIONS").split("[';']");
        for (String library : libraryArray) {
            if (itemLocation != null) {
                for (String location : itemLocation.split("['/']")) {
                    if (location.equals(library)) {
                        asrItem = true;
                        break;
                    }
                }
            }
        }
        return asrItem;

    }


    public List<ASRTypeRequest> getASRRequest(String searchParameter,String value){
        Map<String,String> requestMap = new HashMap<String,String>();
        requestMap.put(searchParameter,value);
        List<ASRTypeRequest> asrTypeRequests = (List<ASRTypeRequest>)businessObjectService.findMatching(ASRTypeRequest.class,requestMap);
        if(asrTypeRequests.size()>0){
            return asrTypeRequests;
        }
        return null;
    }

    public boolean updateRequestStatus(ASRTypeRequest asrTypeRequest,String holdId,String requestStatus){
        boolean updated = false;
        Map<String,String> parameterMap = new HashMap<>();
        parameterMap.put("requestId",holdId);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>)businessObjectService.findMatching(OleDeliverRequestBo.class,parameterMap);
        if(oleDeliverRequestBoList!=null && oleDeliverRequestBoList.size()>0){
            OleDeliverRequestBo oleDeliverRequestBo = oleDeliverRequestBoList.get(0);
            oleDeliverRequestBo.setRequestStatus(requestStatus);
            businessObjectService.save(oleDeliverRequestBo);
            List<ASRTypeRequest> asrTypeRequests;
            if(asrTypeRequest == null){
                asrTypeRequests = getASRRequest("requestId", holdId);
                if(asrTypeRequests!=null && asrTypeRequests.size()>0){
                    asrTypeRequest = asrTypeRequests.get(0);

                } else{
                    return false;
                }
            }
            asrTypeRequest.setRequestStatus(requestStatus);
            ASRTypeRequest asrTypeRequest1=businessObjectService.save(asrTypeRequest);
            if(asrTypeRequest1!=null){
                updated = true;
            }
        }
       return updated;
    }

    public boolean updateItemStatus(String itemBarcode,String status){
        boolean updated = false;
        Item items = null;
        org.kuali.ole.docstore.common.document.Item item = getItemUsingBarcode(itemBarcode);
        if(item!=null){
        String itemContent = item.getContent();
         if(itemContent!=null){
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
             items=itemOlemlRecordProcessor.fromXML(itemContent);
        if(items!=null && items.getItemStatus()!=null){
            ItemStatus itemStatus = new ItemStatus();
            itemStatus.setCodeValue(status);
            items.setItemStatus(itemStatus);
            item.setContent(itemOlemlRecordProcessor.toXML(items));
            try{
           getDocstoreClientLocator().getDocstoreClient().updateItem(item);
                updated=true;
            } catch(Exception e){
                Log.error(e,e);
            }
        }
         }
        }

      return updated;
    }

    public boolean updateItemStatusForInTransit(String itemBarcode,String status){
        boolean updated = false;
        Item items = null;
        org.kuali.ole.docstore.common.document.Item item = getItemUsingBarcode(itemBarcode);
        if(item!=null){
            String itemContent = item.getContent();
            if(itemContent!=null){
                ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
                items=itemOlemlRecordProcessor.fromXML(itemContent);
                if(items!=null && items.getItemStatus()!=null && items.getItemStatus().getCodeValue().equals(ASRConstants.IN_TRANSIT)){
                    ItemStatus itemStatus = new ItemStatus();
                    itemStatus.setCodeValue(status);
                    items.setItemStatus(itemStatus);
                    item.setContent(itemOlemlRecordProcessor.toXML(items));
                    try{
                        getDocstoreClientLocator().getDocstoreClient().updateItem(item);
                        updated=true;
                    } catch(Exception e){
                        Log.error(e,e);
                    }
                }
            }
        }

        return updated;
    }

    public ASRRequests getASRRequest(List<ASRTypeRequest> asrRequestTypeList){
        ASRRequests asrRequests=new ASRRequests();
        List<ASRRequest> asrRequestList=new ArrayList<>();
        ASRRequest asrRequest=null;
        for(ASRTypeRequest asrTypeRequest:asrRequestTypeList){
            asrRequest=new ASRRequest();
            asrRequest.setHoldId(asrTypeRequest.getRequestId());
            asrRequest.setItemBarcode(asrTypeRequest.getItemId());
            asrRequestList.add(asrRequest);
        }
        asrRequests.setAsrRequests(asrRequestList);

        return asrRequests;
    }


    public ASRRequests getASRTypeRequest(String status){
        ASRRequests asrRequests = new ASRRequests();
        Map<String,String> requestMap = new HashMap<String,String>();
            requestMap.put("requestStatus",status);
         List<ASRTypeRequest> requestsList = (List<ASRTypeRequest>)businessObjectService.findMatching(ASRTypeRequest.class,requestMap);
        if(requestsList.size()>0){
                     asrRequests = getASRRequest(requestsList);
        }
        return asrRequests;

    }


    public void deleteASRRequest(String itemBarcode){
        OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        Map<String,String> requestMap = new HashMap<String,String>();
        requestMap.put("itemId",itemBarcode);
        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>)businessObjectService.findMatching(OleDeliverRequestBo.class,requestMap);
        if(oleDeliverRequestBos.size()>0){
            for(OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBos){
                oleDeliverRequestDocumentHelperService.cancelDocument(oleDeliverRequestBo);
            }
        }
        List<ASRTypeRequest> asrTypeRequests = (List<ASRTypeRequest>)businessObjectService.findMatching(ASRTypeRequest.class,requestMap);
        businessObjectService.delete(asrTypeRequests);

    }


  public ASRRequestDetailBo generateASRDetailBo(OleDeliverRequestBo oleDeliverRequestBo){
      ASRRequestDetailBo asrRequestDetailBo = new ASRRequestDetailBo();
      String[] availableDates;
      asrRequestDetailBo.setRequestId(oleDeliverRequestBo.getRequestId());
      asrRequestDetailBo.setAvailable(false);
      Map<String, String> loanMap = new HashMap<String, String>();
      loanMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, oleDeliverRequestBo.getItemId());
      List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
      if (oleLoanDocumentList.size() > 0) {
          if (oleLoanDocumentList.get(0).getLoanDueDate() != null) {
              availableDates = oleLoanDocumentList.get(0).getLoanDueDate().toString().split(" ");
              if(availableDates!=null && availableDates.length>0){
                  asrRequestDetailBo.setDateAvailable(availableDates[0]);
              } else{
                  asrRequestDetailBo.setDateAvailable(oleLoanDocumentList.get(0).getLoanDueDate().toString());
              }
              if(oleDeliverRequestBo.getPickUpLocationId()!=null){
                  Map<String, String> deskMap = new HashMap<>();
                  deskMap.put(OLEConstants.CIRCULATION_DESK_ID, oleDeliverRequestBo.getPickUpLocationId());
                  List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDesk.class, deskMap);
                  if(oleCirculationDeskList.size()>0){

                      asrRequestDetailBo.setDateAvailableExpires(oleDeliverRequestDocumentHelperService.addDate(new java.sql.Date(oleLoanDocumentList.get(0).getLoanDueDate().getTime()), Integer.parseInt(oleCirculationDeskList.get(0).getOnHoldDays())).toString());
                  }
              }
          }  else{
              asrRequestDetailBo.setDateAvailable(OLEConstants.INDEFINITE);
              asrRequestDetailBo.setDateAvailableExpires(OLEConstants.INDEFINITE);
          }
      }
      asrRequestDetailBo.setDateExpires(oleDeliverRequestBo.getRequestExpiryDate().toString());
      if(oleDeliverRequestBo.getRecallDueDate()!=null)
          asrRequestDetailBo.setDateRecalled(oleDeliverRequestBo.getRecallDueDate().toString());
      asrRequestDetailBo.setPatronId(oleDeliverRequestBo.getBorrowerBarcode());
      asrRequestDetailBo.setPickupLocation(oleDeliverRequestBo.getPickUpLocationCode());
      //asrRequestDetailBo.setRecallStatus();
      if(oleDeliverRequestBo.getBorrowerQueuePosition()!=null)
          asrRequestDetailBo.setPriority(oleDeliverRequestBo.getBorrowerQueuePosition().toString());
      asrRequestDetailBo.setRequestType(oleDeliverRequestBo.getRequestTypeCode());
      asrRequestDetailBo.setRequestStatus(oleDeliverRequestBo.getRequestStatus());
      return asrRequestDetailBo;
  }


   public ASRRequestDetailsBo generateASRequestDetailsBo(List<OleDeliverRequestBo> oleDeliverRequestBos){
       ASRRequestDetailsBo asrRequestDetailsBo = new ASRRequestDetailsBo();
       List<ASRRequestDetailBo> asrRequestDetailBoList;
       if(oleDeliverRequestBos.size()>0){
             asrRequestDetailBoList = new ArrayList<ASRRequestDetailBo>();
        for(OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBos){
            asrRequestDetailBoList.add(generateASRDetailBo(oleDeliverRequestBo));
        }
        asrRequestDetailsBo.setAsrRequestDetailBos(asrRequestDetailBoList);
           return asrRequestDetailsBo;
       }
       return null;
   }

    public ASRRequestDetailsBo getRequests(String itemBarcode){
        List<OleDeliverRequestBo> oleDeliverRequestBos =null;
        Map<String,String> requestMap = new HashMap<String,String>();
        requestMap.put("itemId",itemBarcode);
        List<ASRTypeRequest> asrTypeRequests =  (List<ASRTypeRequest>)businessObjectService.findMatching(ASRTypeRequest.class,requestMap);
        if(asrTypeRequests.size()>0){
            oleDeliverRequestBos = new ArrayList<OleDeliverRequestBo>();
               for(ASRTypeRequest asrTypeRequest : asrTypeRequests){
                   requestMap.put("itemId",asrTypeRequest.getItemId());
                      List<OleDeliverRequestBo> oleDeliverRequestBos1 = (List<OleDeliverRequestBo>)businessObjectService.findMatching(OleDeliverRequestBo.class,requestMap);
                         if(oleDeliverRequestBos1.size()>0){
                                   oleDeliverRequestBos.addAll(oleDeliverRequestBos1);
                             break;
                         }
               }
        }
        if(oleDeliverRequestBos!=null && oleDeliverRequestBos.size()>0){
            ASRRequestDetailsBo asrRequestDetailsBo = generateASRequestDetailsBo(oleDeliverRequestBos);
            if(asrRequestDetailsBo !=null){
                return asrRequestDetailsBo;
            }
        }
        return null;
    }

    public OleDeliverRequestBo getPrioritizedRequest(String itemBarcode){
        OleDeliverRequestBo oleDeliverRequestBo = null;
        Map<String,String> requestMap = new HashMap<String,String>();
        requestMap.put("itemId",itemBarcode);
        requestMap.put("borrowerQueuePosition","1");
        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>)businessObjectService.findMatching(OleDeliverRequestBo.class,requestMap);
        if(oleDeliverRequestBos.size()>0){
               oleDeliverRequestBo = oleDeliverRequestBos.get(0);
        }
        return oleDeliverRequestBo;
    }


    public boolean validateLocation(String operatorId,String itemLocation){
        boolean valid=false;
        Map<String,String> circulationDeskDetailMap = new HashMap<String,String>();
        Map<String,String> circulationLocationMap = new HashMap<String,String>();
        circulationDeskDetailMap.put("operatorId",operatorId);
        circulationDeskDetailMap.put("defaultLocation","true");
        List<OleCirculationDeskDetail> oleCirculationDeskDetails = (List<OleCirculationDeskDetail>)businessObjectService.findMatching(OleCirculationDeskDetail.class,circulationDeskDetailMap);
        if(oleCirculationDeskDetails.size()>0){
            circulationLocationMap.put("circulationDeskId",oleCirculationDeskDetails.get(0).getCirculationDeskId());
        List<OleCirculationDeskLocation> oleCirculationDeskLocations = (List<OleCirculationDeskLocation>)businessObjectService.findMatching(OleCirculationDeskLocation.class,circulationLocationMap);
        if(oleCirculationDeskLocations.size()>0){
                   for(OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDeskLocations){
                       //for(String location:oleCirculationDeskLocation.getLocation().getFullLocationPath().split("['/']")){
                       if(oleCirculationDeskLocation.getCirculationPickUpDeskLocation()==null || (oleCirculationDeskLocation.getCirculationPickUpDeskLocation()!=null && oleCirculationDeskLocation.getCirculationPickUpDeskLocation().trim().isEmpty())){
                           if(oleCirculationDeskLocation.getLocation().getFullLocationPath().equals(itemLocation)){
                               valid = true;
                               break;
                           }
                       }
                       //}
                       /*if(oleCirculationDeskLocation.getLocation().getFullLocationPath().contains(itemLocation)){
                            valid = true;
                       }*/
                   }
        }
      }
       return valid;
    }



    public boolean validPatron(String patronId){
        boolean valid=false;
        Map<String,String> patronMap = new HashMap<String,String>();
        patronMap.put("olePatronId",patronId);
        List<OlePatronDocument> olePatronDocuments = (List<OlePatronDocument>)businessObjectService.findMatching(OlePatronDocument.class,patronMap);
        if(olePatronDocuments!=null && olePatronDocuments.size()>0){
            valid=true;
        }
        return valid;
    }



   public boolean saveASRRequest(ASRRequestBo asrRequestBo){
       boolean saved = false;
       ASRTypeRequest asrTypeRequest = new ASRTypeRequest();
       asrTypeRequest.setItemId(asrRequestBo.getItemBarcode());
       asrTypeRequest.setPatronId(asrRequestBo.getPatronBarcode());
       asrTypeRequest.setPickUpLocation(asrRequestBo.getPickupLocation());
       asrTypeRequest.setPatronName(asrRequestBo.getPatronName());
       asrTypeRequest.setRequestNote(asrRequestBo.getRequestNote());
       ASRTypeRequest asrTypeRequest1 = businessObjectService.save(asrTypeRequest);
       if(asrTypeRequest1!=null){
           saved =true;
       }
       return saved;
   }


    public boolean saveASRItem(ASRItem asrItem){
        boolean saved = false;
        org.kuali.ole.deliver.bo.ASRItem asrItems = new org.kuali.ole.deliver.bo.ASRItem();
        asrItems.setItemBarcode(asrItem.getItemBarcode());
        asrItems.setCallNumber(asrItem.getCallNumber());
        asrItems.setAuthor(asrItem.getAuthor());
        asrItems.setTitle(asrItem.getTitle());

        org.kuali.ole.deliver.bo.ASRItem asrItem1 =   businessObjectService.save(asrItems);
        if(asrItem1!=null){
            saved = true;
        }
        return saved;
    }

    public List<OleDeliverRequestBo> getDeliverRequest(Map<String,String> requestMap){
        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>)businessObjectService.findMatching(OleDeliverRequestBo.class,requestMap);
        if(oleDeliverRequestBos.size()>0){
            return oleDeliverRequestBos;
        }
        return null;
    }


 public void deleteASRTypeRequest(String requestId){
    Map<String,String> asrTypeMap = new HashMap<String,String>();
    asrTypeMap.put("requestId",requestId);
    businessObjectService.deleteMatching(ASRTypeRequest.class,asrTypeMap);
 }

    public boolean isCirculationDesksLocation(String itemLocation,String circulationDeskId){
        boolean valid=false;
        Map<String,String> circulationLocationMap = new HashMap<String,String>();
        circulationLocationMap.put("circulationDeskId",circulationDeskId);
        List<OleCirculationDeskLocation> oleCirculationDeskLocations = (List<OleCirculationDeskLocation>)businessObjectService.findMatching(OleCirculationDeskLocation.class,circulationLocationMap);
        if(oleCirculationDeskLocations.size()>0){
            for(OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDeskLocations){
                //for(String location:oleCirculationDeskLocation.getLocation().getFullLocationPath().split("['/']")){
              if(oleCirculationDeskLocation.getCirculationPickUpDeskLocation()==null || (oleCirculationDeskLocation.getCirculationPickUpDeskLocation()!=null && oleCirculationDeskLocation.getCirculationPickUpDeskLocation().trim().isEmpty())){
                if(oleCirculationDeskLocation.getLocation().getFullLocationPath().equals(itemLocation)){
                    valid = true;
                    break;
                }
              }
                //}
                       /*if(oleCirculationDeskLocation.getLocation().getFullLocationPath().contains(itemLocation)){
                            valid = true;
                       }*/
            }
        }
        return valid;
    }

    public String getLocation(String itemBarcode){
        List<String> resultList = new ArrayList<String>();
        resultList.add(ASRConstants.LOCATION_LEVEL_DISPLAY);
        resultList.add("holdingsIdentifier");
        Map<String,String> resultMap = getItemFieldValue(ASRConstants.ITEM_BARCODE_DISPLAY,itemBarcode,resultList);
        String location = resultMap.get(ASRConstants.LOCATION_LEVEL_DISPLAY);
        if(location == null){
            String holdingsId = resultMap.get("holdingsIdentifier");
            resultList.clear();
            resultList.add(ASRConstants.LOCATION_LEVEL_DISPLAY);
            Map<String,String> resultMap1 = getHoldingFieldValue("id",holdingsId, resultList);
            location = resultMap1.get(ASRConstants.LOCATION_LEVEL_DISPLAY);
        }
        return location;

    }

    public SearchParams buildSearchParamsForHoldings(Map<String, String> queryParams) {
        SearchParams searchParams = new SearchParams();
        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        List<SearchCondition>  searchConditions = new ArrayList<>();
        if (null != queryParams) {
            for (Iterator<String> keys = queryParams.keySet().iterator(); keys.hasNext(); ) {
                String key = keys.next();
                String value = queryParams.get(key);
                if(org.apache.commons.lang.StringUtils.isNotEmpty(value)) {
                    //searchConditions.add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField("item", key, value), "AND"));
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField("holdings", key, value), "AND"));
                }
            }
        }
        return searchParams;
    }

    public ASRRequests getASRRequestBasedOnLocation(ASRRequests asrRequests,String location){
        List<ASRRequest> requestList = new ArrayList<ASRRequest>();
        if(asrRequests.getAsrRequests()!=null){
            for(ASRRequest asrRequest:asrRequests.getAsrRequests()){
                String itemBarcode = asrRequest.getItemBarcode();
                String itemLocation = getLocation(itemBarcode);
                if(isLocationMatch(itemLocation,location)){
                    requestList.add(asrRequest);
                }
            }
        }
        requestList = processASRRequestInformation(requestList);
        asrRequests.setAsrRequests(requestList);
        return asrRequests;


    }

    public boolean isLocationMatch(String itemLocation,String asrLocation) {
        boolean asrItem = false;
        if(itemLocation!=null){
            String[] libraryArray = itemLocation.split("['/']");
            for (String library : libraryArray) {
                if (asrLocation.equals(library)) {
                    asrItem = true;
                }
            }
        }


        return asrItem;

    }

    public boolean deleteAllASRItems(ASRItems asrItems){
        boolean isAllDeleted = false;
        List<org.kuali.ole.deliver.bo.ASRItem> deleteList = new ArrayList<>();
        List<org.kuali.ole.deliver.bo.ASRItem> asrItemList = new ArrayList<>();
        asrItemList=(List<org.kuali.ole.deliver.bo.ASRItem>)getBusinessObjectService().findAll(org.kuali.ole.deliver.bo.ASRItem.class);
        for(ASRItem asrItem:asrItems.getAsrItems()){
            for(org.kuali.ole.deliver.bo.ASRItem dlvrASRItem:asrItemList){
                if(dlvrASRItem.getItemBarcode() !=null && dlvrASRItem.getItemBarcode().equals(asrItem.getItemBarcode())){
                    deleteList.add(dlvrASRItem);
                    break;
                }
            }
        }
        getBusinessObjectService().delete(deleteList);
        isAllDeleted=true;
        return isAllDeleted;
    }


    public List<ASRRequest> processASRRequestInformation(List<ASRRequest> asrRequests){
      if(asrRequests!=null && asrRequests.size()>0){
             for(ASRRequest asrRequest : asrRequests){
                 Map<String,String> requestMap = new HashMap<String,String>();
                 requestMap.put("requestId",asrRequest.getHoldId());
                 List<OleDeliverRequestBo> deliverRequestBos = (List<OleDeliverRequestBo>)businessObjectService.findMatching(OleDeliverRequestBo.class,requestMap);
                 if(deliverRequestBos.size()>0){
                     OleDeliverRequestBo oleDeliverRequestBo = deliverRequestBos.get(0);
                     oleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_PATRON);
                     asrRequest.setPickupLocation(oleDeliverRequestBo.getPickUpLocationCode());
                     asrRequest.setRequestDate(oleDeliverRequestBo.getCreateDate().toString());
                     asrRequest.setPatronBarcode(oleDeliverRequestBo.getBorrowerBarcode());
                     oleDeliverRequestBo = oleDeliverRequestDocumentHelperService.processItem(oleDeliverRequestBo);
                     oleDeliverRequestBo = oleDeliverRequestDocumentHelperService.processPatron(oleDeliverRequestBo);
                     asrRequest.setAuthor(oleDeliverRequestBo.getAuthor());
                     asrRequest.setTitle(oleDeliverRequestBo.getTitle());
                     asrRequest.setCallNumber(oleDeliverRequestBo.getCallNumber());
                     asrRequest.setPatronName(oleDeliverRequestBo.getBorrowerName());
                     asrRequest.setRequestStatus(oleDeliverRequestBo.getRequestStatus());
                     asrRequest.setRequestNote(oleDeliverRequestBo.getRequestNote());
                 }
             }
      }
        return asrRequests;
    }


}
