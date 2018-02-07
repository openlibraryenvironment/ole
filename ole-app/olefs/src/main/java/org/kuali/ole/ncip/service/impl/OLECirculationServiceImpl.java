package org.kuali.ole.ncip.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.bo.*;
import org.kuali.ole.converter.OLECheckInItemConverter;
import org.kuali.ole.converter.OLECheckOutItemConverter;
import org.kuali.ole.converter.OLELookupUserConverter;
import org.kuali.ole.deliver.OleLoanDocumentsFromSolrBuilder;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.deliver.service.impl.OleDeliverDaoJdbc;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.ncip.bo.*;
import org.kuali.ole.ncip.converter.*;
import org.kuali.ole.ncip.service.OLECirculationService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.kim.service.impl.IdentityManagementServiceImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.engine.EngineResults;
import org.kuali.rice.krms.api.engine.ResultEvent;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 7/21/13
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECirculationServiceImpl implements OLECirculationService {
    private static final Logger LOG = Logger.getLogger(OLECirculationServiceImpl.class);
    private BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
    private OLECirculationHelperServiceImpl oleCirculationHelperService = new OLECirculationHelperServiceImpl();
    private OLELookupUserConverter oleLookupUserConverter = new OLELookupUserConverter();
    private OLECheckInItemConverter oleCheckInItemConverter = new OLECheckInItemConverter();
    private OLECheckOutItemConverter oleCheckOutItemConverter = new OLECheckOutItemConverter();
    private OLEHoldsConverter oleHoldsConverter = new OLEHoldsConverter();
    private OLEItemFineConverter oleItemFineConverter = new OLEItemFineConverter();
    private OLECheckoutItemsConverter oleCheckoutItemsConverter = new OLECheckoutItemsConverter();
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
    private LoanProcessor loanProcessor = new LoanProcessor();
    private DocstoreUtil docstoreUtil;
    private ConfigurationService kualiConfigurationService;
    private Map<String,OleCirculationDesk> oleCirculationDeskMap = getAvailableCirculationDesks();
    private Map<String,OleDeliverRequestType> oleDeliverRequestTypeMap = getAvailableRequestTypes();
    private CircDeskLocationResolver circDeskLocationResolver;
    private OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder;

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    public ConfigurationService getKualiConfigurationService() {
        if (kualiConfigurationService == null) {
            kualiConfigurationService = (ConfigurationService) SpringContext.getBean("kualiConfigurationService");
        }
        return kualiConfigurationService;
    }

    public void setKualiConfigurationService(ConfigurationService kualiConfigurationService) {
        this.kualiConfigurationService = kualiConfigurationService;
    }

    public DocstoreUtil getDocstoreUtil() {

        if (docstoreUtil == null) {
            docstoreUtil = SpringContext.getBean(DocstoreUtil.class);

        }
        return docstoreUtil;
    }

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public OLECirculationHelperServiceImpl getOleCirculationHelperService() {
        return oleCirculationHelperService;
    }

    public void setOleCirculationHelperService(OLECirculationHelperServiceImpl oleCirculationHelperService) {
        this.oleCirculationHelperService = oleCirculationHelperService;
    }

    public OLELookupUserConverter getOleLookupUserConverter() {
        return oleLookupUserConverter;
    }

    public void setOleLookupUserConverter(OLELookupUserConverter oleLookupUserConverter) {
        this.oleLookupUserConverter = oleLookupUserConverter;
    }

    public OLECheckInItemConverter getOleCheckInItemConverter() {
        return oleCheckInItemConverter;
    }

    public void setOleCheckInItemConverter(OLECheckInItemConverter oleCheckInItemConverter) {
        this.oleCheckInItemConverter = oleCheckInItemConverter;
    }

    public OLECheckOutItemConverter getOleCheckOutItemConverter() {
        return oleCheckOutItemConverter;
    }

    public void setOleCheckOutItemConverter(OLECheckOutItemConverter oleCheckOutItemConverter) {
        this.oleCheckOutItemConverter = oleCheckOutItemConverter;
    }

    @Override
    public String lookupUser(String patronBarcode, String operator, String agencyId, boolean isSIP2Request) {
        LOG.info("Inside the look up user : patron Barcode : " + patronBarcode + "operator : "+ operator + "agencyId : " + agencyId );
        OLELookupUser lookupUser = new OLELookupUser();
        if (!loanProcessor.hasCirculationDesk(operator)) {
            lookupUser.setCode("001");
            lookupUser.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
        } else {
            Map<String, String> patronMap = new HashMap<String, String>();
            patronMap.put(OLEConstants.BARCODE, patronBarcode);
            List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) businessObjectService.findMatching(OlePatronDocument.class, patronMap);
            if (olePatronDocumentList.size() > 0) {
                OlePatronDocument olePatronDocument = olePatronDocumentList.get(0);
                lookupUser = oleCirculationHelperService.initialiseLookupUser(olePatronDocument, agencyId);
                if (olePatronDocument.isGeneralBlock() || oleCirculationHelperService.isPatronExpired(olePatronDocument) || !olePatronDocument.isActiveIndicator() || oleCirculationHelperService.isPatronActivated(olePatronDocument)){
                    lookupUser.setValidPatron(false);
                }else{
                    lookupUser.setValidPatron(true);
                }
                try {
                    List<OleLoanDocument> oleLoanDocumentList = getOleLoanDocumentsFromSolrBuilder()
                            .getPatronLoanedItemBySolr(olePatronDocument.getOlePatronId(), null);
                    List<OLECheckedOutItem> oleCheckedOutItemList = getPatronCheckedOutItemList(oleLoanDocumentList,olePatronDocument.getOleBorrowerType().getBorrowerTypeCode(),agencyId!=null?false:true);
                    //List<OLECheckedOutItem> checkedOutItemList = getPatronCheckedOutItemList(olePatronDocument.getOleLoanDocuments(),olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                    OLECheckedOutItems oleCheckedOutItems = new OLECheckedOutItems();
                    if (oleCheckedOutItemList != null && oleCheckedOutItemList.size() > 0) {
                        oleCheckedOutItems.setCheckedOutItems(oleCheckedOutItemList);
                    }
                    lookupUser.setOleCheckedOutItems(oleCheckedOutItems);
                } catch (Exception e) {
                    LOG.info("Exception Occurred While Retrieving the checked out items");
                    LOG.error(e);
                }
                try {
                    List<OLEHold> oleHoldList = getHoldsList(olePatronDocument.getOleDeliverRequestBos());
                    OLEHolds oleHolds = new OLEHolds();
                    if (oleHoldList != null && oleHoldList.size() > 0) {
                        oleHolds.setOleHoldList(oleHoldList);
                    }
                    lookupUser.setOleHolds(oleHolds);
                } catch (Exception e) {
                    LOG.info("Exception Occurred While Retrieving the Hold items");
                    LOG.error(e);
                }
                try {
                    OLEItemFines oleItemFines = (OLEItemFines) oleItemFineConverter.generateCheckoutItemObject(getFine(patronBarcode, operator));
                    lookupUser.setOleItemFines(oleItemFines);
                } catch (Exception e) {
                    LOG.info("Exception Occurred While Retrieving Fine");
                    LOG.error(e);
                }
                lookupUser.setCode("000");
                lookupUser.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RTRVD_SUCCESS));
            } else {
                lookupUser.setCode("002");
                lookupUser.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
            }
        }
        String responseMessage = "";
        if(isSIP2Request){
            responseMessage = oleLookupUserConverter.generateLookupUserResponseXmlForSip2(lookupUser);
        }else{
            responseMessage = oleLookupUserConverter.generateLookupUserResponseXml(lookupUser);
        }
        return responseMessage;
    }

    @Override
    public String getCheckedOutItems(String patronBarcode, String operator) throws Exception {
        LOG.info("Inside the look up user : patron Barcode : " + patronBarcode + "operator : "+ operator  );
        LOG.info("Start CHECK out " + System.currentTimeMillis());
        OLECheckedOutItems oleCheckedOutItems = new OLECheckedOutItems();
        String patronType = "";
        String checkoutItemString = "";
        if (!loanProcessor.hasCirculationDesk(operator)) {
            oleCheckedOutItems.setCode("001");
            oleCheckedOutItems.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
            LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
        } else {

            Map<String, String> patronMap = new HashMap<String, String>();
            patronMap.put(OLEConstants.BARCODE, patronBarcode);
            List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) businessObjectService.findMatching(OlePatronDocument.class, patronMap);
            if (olePatronDocumentList.size() > 0) {
                Map<String, String> patronDocMap = new HashMap<String, String>();
                OlePatronDocument olePatronDocument = olePatronDocumentList.get(0);
                patronType = olePatronDocument.getBorrowerType();
                Map<String, String> borrowerTypeMap = new HashMap<String, String>();
                borrowerTypeMap.put(OLEConstants.BORROWER_TYPE_ID, patronType);
                List<OleBorrowerType> oleBorrowerTypeList = (List<OleBorrowerType>) businessObjectService.findMatching(OleBorrowerType.class, borrowerTypeMap);
                if (oleBorrowerTypeList.size() > 0) {
                    patronType = oleBorrowerTypeList.get(0).getBorrowerTypeCode();
                }
                patronDocMap.put(OLEConstants.PATRON_ID, olePatronDocument.getOlePatronId());
                List<OleLoanDocument> oleLoanDocumentList = getOleLoanDocumentsFromSolrBuilder()
                        .getPatronLoanedItemBySolr(olePatronDocument.getOlePatronId(), null);
                if (oleLoanDocumentList != null && oleLoanDocumentList.size() > 0) {
                    List<OLECheckedOutItem> oleCheckedOutItemList = getPatronCheckedOutItemList(oleLoanDocumentList,patronType,true);
                    oleCheckedOutItems.setCheckedOutItems(oleCheckedOutItemList);
                    oleCheckedOutItems.setCode("000");
                    LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RTRVD_SUCCESS));
                    oleCheckedOutItems.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RTRVD_SUCCESS));
                } else {
                    oleCheckedOutItems.setCode("004");
                    LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_LOAN));
                    oleCheckedOutItems.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_LOAN));
                }
            } else {
                oleCheckedOutItems.setCode("002");
                LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
                oleCheckedOutItems.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));

            }
        }
        checkoutItemString = oleCheckoutItemsConverter.generateCheckOutItemXml(oleCheckedOutItems);
        LOG.info("END CHECK out " + System.currentTimeMillis());
        return checkoutItemString;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private OleLoanDocumentsFromSolrBuilder getOleLoanDocumentsFromSolrBuilder() {
        if (null == oleLoanDocumentsFromSolrBuilder) {
            oleLoanDocumentsFromSolrBuilder = new OleLoanDocumentsFromSolrBuilder();
        }
        return oleLoanDocumentsFromSolrBuilder;
    }

    public void setOleLoanDocumentsFromSolrBuilder(OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder) {
        this.oleLoanDocumentsFromSolrBuilder = oleLoanDocumentsFromSolrBuilder;
    }

    @Override
      public String placeRequest(String patronBarcode, String operatorId, String itemBarcode, String itemIdentifier, String requestType, String pickUpLocation, String itemLocation, String bibId, String requestLevel, java.sql.Date requestExpiryDate, String requestNote) {
        String responseMessage = oleDeliverRequestDocumentHelperService.placeRequest(patronBarcode, operatorId, itemBarcode, requestType, pickUpLocation, itemIdentifier, itemLocation, null, null, null, null, false,bibId,requestLevel,requestExpiryDate, requestNote);
        return responseMessage;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String overridePlaceRequest(String patronBarcode, String operatorId, String itemBarcode, String requestType, String pickUpLocation, String itemLocation, String bibId, String requestLevel, java.sql.Date requestExpiryDate, String requestNote) {
        String responseMessage = oleDeliverRequestDocumentHelperService.overridePlaceRequest(patronBarcode, operatorId, itemBarcode, requestType, pickUpLocation, null, itemLocation, null, null, null, null, false,bibId,requestLevel,requestExpiryDate, requestNote);
        return responseMessage;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String cancelRequest(String operator, String patronBarcode, String itemBarcode) {
        LOG.info("Inside cancel Request : Operator : " + operator + "patron Barcode : " + patronBarcode + "item barcode");
        OLECancelRequest oleCancelRequest = new OLECancelRequest();
        OlePatronDocument olePatronDocument = null;
        if (!loanProcessor.hasCirculationDesk(operator)) {
            oleCancelRequest.setCode("001");
            oleCancelRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
            LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
        } else {
            Map<String, String> queryMap = new HashMap<String, String>();
            queryMap.put(OLEConstants.BARCODE, patronBarcode);
            List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) businessObjectService.findMatching(OlePatronDocument.class, queryMap);
            if (olePatronDocumentList.size() > 0) {
                olePatronDocument = olePatronDocumentList.get(0);
            } else if (olePatronDocumentList.size() == 0) {
                oleCancelRequest.setCode("002");
                oleCancelRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
                LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
            }
            if (olePatronDocument != null) {
                queryMap = new HashMap<String, String>();
                queryMap.put(OLEConstants.OleDeliverRequest.BORROWER_ID, olePatronDocument.getOlePatronId());
                queryMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, itemBarcode);
                List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, queryMap);
                if (oleDeliverRequestBoList != null && oleDeliverRequestBoList.size() > 0) {
                    OleDeliverRequestBo oleDeliverRequestBo = oleDeliverRequestBoList.get(0);
                    oleDeliverRequestBo.setOperatorCreateId(operator);
                    oleDeliverRequestDocumentHelperService.cancelDocument(oleDeliverRequestBo);
                    oleCancelRequest.setCode("007");
                    oleCancelRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_SUCCESSFULLEY_CANCELLED));
                    LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_SUCCESSFULLEY_CANCELLED));
                } else {
                    oleCancelRequest.setCode("008");
                    oleCancelRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_REQUEST_FOUND));
                    LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_REQUEST_FOUND));
                    return new OLECancelRequestConverter().generateCancelRequestXml(oleCancelRequest);
                }
            }
        }

        return new OLECancelRequestConverter().generateCancelRequestXml(oleCancelRequest);
    }


    @Override
    public String cancelRequests(String operator, String requestId) {
        LOG.info("Inside cancel request  : Operator : " + operator + "Request id : " + requestId);
        OLECancelRequest oleCancelRequest = new OLECancelRequest();
        if (!loanProcessor.hasCirculationDesk(operator)) {
            oleCancelRequest.setCode("001");
            oleCancelRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
            LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
        } else {
            Map<String, String> requestMap = new HashMap<String, String>();
            requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_ID, requestId);
            List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, requestMap);
            if (oleDeliverRequestBoList != null && oleDeliverRequestBoList.size() > 0) {
                OleDeliverRequestBo oleDeliverRequestBo = oleDeliverRequestBoList.get(0);
                oleDeliverRequestBo.setOperatorCreateId(operator);
                oleDeliverRequestDocumentHelperService.cancelDocument(oleDeliverRequestBo);
                oleCancelRequest.setCode("007");
                oleCancelRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_SUCCESSFULLEY_CANCELLED));
                LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_SUCCESSFULLEY_CANCELLED));
            } else {
                oleCancelRequest.setCode("008");
                oleCancelRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_REQUEST_FOUND_REQUEST_ID));
                LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_REQUEST_FOUND_REQUEST_ID));
                return new OLECancelRequestConverter().generateCancelRequestXml(oleCancelRequest);
            }
        }
        return new OLECancelRequestConverter().generateCancelRequestXml(oleCancelRequest);
    }

    @Override
    public String renewItem(String patronBarcode, String operator, String itemBarcode, boolean isSIP2Request) {
        LOG.info("Inside cancel request  : Operator : " + operator + "Patron Barcode : " + patronBarcode + "Item barcode : "+ itemBarcode);
        String responseMessage = oleCirculationHelperService.renewItem(patronBarcode, operator, itemBarcode, isSIP2Request);
        return responseMessage;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String renewItemList(String patronBarcode, String operator, String itemBarcode, boolean isSIP2Request) {
        LOG.info("Inside cancel request  : Operator : " + operator + "Patron Barcode : " + patronBarcode + "Item barcode : "+ itemBarcode);
        String responseMessage = oleCirculationHelperService.renewItemList(patronBarcode, operator, itemBarcode,isSIP2Request);
        return responseMessage;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String acceptItem(String patronBarcode, String operator, String itemBarcode, String callNumber, String title, String author, String itemType, String itemLocation, String dateExpires, String requestType, String pickUpLocation) {
        OLEAcceptItem oleAcceptItem = new OLEAcceptItem();
        String itemIdentifier = null;
        if (!loanProcessor.hasCirculationDesk(operator)) {
            oleAcceptItem.setCode("026");
            oleAcceptItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CIRCULATION_DESK_NOT_MAPPED_OPERATOR));
            LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CIRCULATION_DESK_NOT_MAPPED_OPERATOR));
            return new OLEAcceptItemConverter().generateAcceptItemXml(oleAcceptItem);
        }  if(!vaildPatron(patronBarcode)){
            oleAcceptItem.setCode("002");
            oleAcceptItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
            LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
            return new OLEAcceptItemConverter().generateAcceptItemXml(oleAcceptItem);
        }

        if (requestType == null || (requestType != null && requestType.trim().isEmpty())) {
            oleAcceptItem.setCode("012");
            oleAcceptItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_RQST_TYP));
            LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_RQST_TYP));
            return new OLEAcceptItemConverter().generateAcceptItemXml(oleAcceptItem);
        }
        if (requestType != null && !requestType.trim().isEmpty()) {
            Map<String, String> requestTypeMap = new HashMap<String, String>();
            requestTypeMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_CD, requestType);
            List<OleDeliverRequestType> oleDeliverRequestTypeList = (List<OleDeliverRequestType>) getBusinessObjectService().findMatching(OleDeliverRequestType.class, requestTypeMap);
            if (oleDeliverRequestTypeList != null && (oleDeliverRequestTypeList.size() == 0)) {
                oleAcceptItem.setCode("012");
                oleAcceptItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_RQST_TYP));
                LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_RQST_TYP));
                return new OLEAcceptItemConverter().generateAcceptItemXml(oleAcceptItem);
            }
        }
        if (pickUpLocation != null) {
            Map<String, String> circulationDeskMap = new HashMap<String, String>();
            circulationDeskMap.put(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_CD, pickUpLocation);
            List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>) getBusinessObjectService().findMatching(OleCirculationDesk.class, circulationDeskMap);
            if (oleCirculationDeskList != null && oleCirculationDeskList.size() == 0) {
                oleAcceptItem.setCode("013");
                oleAcceptItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_PK_UP_LOCN));
                LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_PK_UP_LOCN));
                return new OLEAcceptItemConverter().generateAcceptItemXml(oleAcceptItem);
            }
        }
        try {
            if(StringUtils.isNotBlank(operator)) {
                IdentityManagementServiceImpl identityManagementService = new IdentityManagementServiceImpl();
                Principal principal = identityManagementService.getPrincipal(operator);
                if(principal!=null) {
                    operator = principal.getPrincipalName();
                }
            }
            itemIdentifier = oleCirculationHelperService.acceptItem(itemBarcode, callNumber, title, author, itemType, itemLocation, operator);
            if (null == itemIdentifier) {
                oleAcceptItem.setCode("031");
                oleAcceptItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_EXIST));
                LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_EXIST));
                return new OLEAcceptItemConverter().generateAcceptItemXml(oleAcceptItem);
            }
            if ("".equals(itemIdentifier)) {
                itemIdentifier = null;
            }
                String responseMessage = oleDeliverRequestDocumentHelperService.placeRequest(patronBarcode, operator, itemBarcode, requestType, pickUpLocation, itemIdentifier, itemLocation, itemType, title, author, callNumber, true,null,"Item Level",null, "");
                responseMessage = responseMessage.replaceAll("&lt;br/&gt;", "");
                responseMessage = responseMessage.replaceAll("<br/>", "");
                OLEPlaceRequestConverter olePlaceRequestConverter = new OLEPlaceRequestConverter();
                OLEPlaceRequest olePlaceRequest = (OLEPlaceRequest) olePlaceRequestConverter.generatePlaceRequestObject(responseMessage);
            if(!olePlaceRequest.getMessage().contains(OLEConstants.RQST_SUCCESS)){
                org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemIdentifier);
                String bibId = item.getHolding().getBib().getId();

                getDocstoreClientLocator().getDocstoreClient().deleteBib(bibId);
            }
                oleAcceptItem.setMessage(olePlaceRequest.getMessage());
                oleAcceptItem.setCode(olePlaceRequest.getCode());
                return new OLEAcceptItemConverter().generateAcceptItemXml(oleAcceptItem);

        } catch (Exception e) {
            oleAcceptItem.setCode("033");
            oleAcceptItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_NOT_CREATED));
            LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_NOT_CREATED));
            return new OLEAcceptItemConverter().generateAcceptItemXml(oleAcceptItem);
        }
    }

    @Override
    public String checkInItem(String patronBarcode, String operator, String itemBarcode, String deleteIndicator, boolean isSIP2Request) {
        LOG.info( " Inside check in item : Patron Barcode :"  +patronBarcode + "operator : "+ operator + "item barcode : "+ itemBarcode);
        if (!loanProcessor.hasCirculationDesk(operator)) {
            OLECheckInItem oleCheckInItem = new OLECheckInItem();
            oleCheckInItem.setCode("026");
            oleCheckInItem.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CIRCULATION_DESK_NOT_MAPPED_OPERATOR));
            LOG.info(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.CIRCULATION_DESK_NOT_MAPPED_OPERATOR));
            return oleCheckInItemConverter.generateCheckInItemXml(oleCheckInItem);
        }
        String responseMessage = oleCirculationHelperService.checkInItem(patronBarcode, operator, itemBarcode, deleteIndicator,isSIP2Request);
        return responseMessage;
    }

    @Override
    public String checkOutItem(String patronBarcode, String operator, String itemBarcode, boolean isSIP2Request) {
        LOG.info( " Inside checkOutItem : Patron Barcode :"  +patronBarcode + "operator : "+ operator + "item barcode : "+ itemBarcode);
        String responseMessage = oleCirculationHelperService.checkOutItem(patronBarcode, operator, itemBarcode,isSIP2Request);
        return responseMessage;
    }





    public List<OLECheckedOutItem> getOleCheckOutItemList(List<OleLoanDocument> oleLoanDocumentList, String patronType) throws Exception {
        SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OleDeliverRequest.DATE_FORMAT);
        List<OLECheckedOutItem> checkedOutItemList = new ArrayList<OLECheckedOutItem>();
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        for (OleLoanDocument oleLoanDocument : oleLoanDocumentList) {
            Map<String, Object> detailMap = oleDeliverRequestDocumentHelperService.retrieveBIbItemHoldingData(oleLoanDocument.getItemUuid());
            Bib bib = (Bib) detailMap.get(OLEConstants.BIB);
            Item item = (Item) detailMap.get(OLEConstants.ITEM);
            OleHoldings oleHoldings = (OleHoldings) detailMap.get(OLEConstants.HOLDING);
            org.kuali.ole.docstore.common.document.Item item1 = (org.kuali.ole.docstore.common.document.Item) detailMap.get("documentItem");
            String itemLocation = null;
            if (item1.getLocation() == null || (item1.getLocation() != null && item1.getLocation().trim().isEmpty())) {
                itemLocation = getDocstoreUtil().getLocation(oleHoldings.getLocation(), new StringBuffer(""));
            } else {
                itemLocation = item1.getLocation();
            }
            Map<String, String> locationMap = getCircDeskLocationResolver().getLocationMap(itemLocation);
            oleLoanDocument.setItemInstitution(locationMap.get(OLEConstants.ITEM_INSTITUTION));
            oleLoanDocument.setItemCampus(locationMap.get(OLEConstants.ITEM_CAMPUS));
            oleLoanDocument.setItemCollection(locationMap.get(OLEConstants.ITEM_COLLECTION));
            oleLoanDocument.setItemLibrary(locationMap.get(OLEConstants.ITEM_LIBRARY));
            oleLoanDocument.setItemLocation(locationMap.get(OLEConstants.ITEM_SHELVING));
            oleLoanDocument.setInstanceUuid(oleHoldings.getHoldingsIdentifier());
            oleLoanDocument.setBibUuid(bib.getId());
            oleLoanDocument.setAuthor(bib.getAuthor());
            oleLoanDocument.setTitle(bib.getTitle());
            OLECheckedOutItem oleCheckedOutItem = new OLECheckedOutItem();
            if (item != null) {
                oleLoanDocument.setItemUuid(item.getItemIdentifier());
                if (item.getItemType() != null) {
                    oleCheckedOutItem.setItemType(item.getItemType().getCodeValue());
                }
                if (item.getCallNumber() != null) {
                    oleCheckedOutItem.setCallNumber(item.getCallNumber().getNumber());
                } else if (oleHoldings != null && oleHoldings.getCallNumber() != null) {
                    oleCheckedOutItem.setCallNumber(oleHoldings.getCallNumber().getNumber());
                }
                if (item.getCopyNumber() != null && !item.getCopyNumber().isEmpty()) {
                    oleCheckedOutItem.setCopyNumber(item.getCopyNumber());
                } else if (oleHoldings != null && oleHoldings.getCopyNumber() != null && !oleHoldings.getCopyNumber().isEmpty()) {
                    oleCheckedOutItem.setCopyNumber(oleHoldings.getCopyNumber());
                }
                if(item.getEnumeration()!=null){
                    oleCheckedOutItem.setVolumeNumber(item.getEnumeration());
                }else{
                oleCheckedOutItem.setVolumeNumber(item.getVolumeNumber());
                }
            }
            oleCheckedOutItem.setCatalogueId(bib.getId());
            oleCheckedOutItem.setItemId(oleLoanDocument.getItemId());
            oleCheckedOutItem.setLoanDate(new Timestamp(oleLoanDocument.getCreateDate().getTime()).toString());
            if (oleLoanDocument.getLoanDueDate() != null) {
                oleCheckedOutItem.setDueDate(oleLoanDocument.getLoanDueDate().toString());
                if ((fmt.format(oleLoanDocument.getLoanDueDate())).compareTo(fmt.format(new Date(System.currentTimeMillis()))) > 0) {
                    oleCheckedOutItem.setOverDue(false);
                }
                else{
                    oleCheckedOutItem.setOverDue(true);
                }
            } else {
                oleCheckedOutItem.setDueDate((new java.sql.Timestamp(new Date(2025, 1, 1).getTime()).toString()));
            }

            if (oleLoanDocument.getRenewalLoanDueDate() != null){
                oleCheckedOutItem.setDateRenewed(oleLoanDocument.getRenewalLoanDueDate().toString());
            }
            else{
                oleCheckedOutItem.setDateRenewed("");
            }

            int renewalDaysFromPolicy = getRenewalDays(oleCheckedOutItem.getItemType(), oleLoanDocument, patronType, oleLoanDocument.getNumberOfRenewals());

            oleCheckedOutItem.setNumberOfRenewals(String.valueOf(renewalDaysFromPolicy));
            oleCheckedOutItem.setTitle(oleLoanDocument.getTitle());
            oleCheckedOutItem.setAuthor(oleLoanDocument.getAuthor());
            oleCheckedOutItem.setAcquiredFine("");
            oleCheckedOutItem.setDateRecalled("");
            if (oleLoanDocument.getNoOfOverdueNoticesSentForBorrower() != null) {
                oleCheckedOutItem.setNumberOfOverdueSent(oleLoanDocument.getNoOfOverdueNoticesSentForBorrower());
            } else {
                oleCheckedOutItem.setNumberOfOverdueSent("1");
            }
            //oleCheckedOutItem.setAcquiredFine(oleLoanDocument.getFineRate().toString());
            checkedOutItemList.add(oleCheckedOutItem);
        }

        return checkedOutItemList;
    }

    @Override
    public String getFine(String patronBarcode, String operator) throws Exception {
        LOG.info("Inside Get Fine : Patron Barcode : " + patronBarcode + "Operator : " + operator);
        OLEItemFineConverter oleItemFineConverter = new OLEItemFineConverter();
        OLEItemFines oleItemFines = new OLEItemFines();
        String itemFineString = "";
        if (!loanProcessor.hasCirculationDesk(operator)) {
            oleItemFines.setCode("001");
            oleItemFines.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
        } else {
            Map<String, String> patronMap = new HashMap<String, String>();
            patronMap.put(OLEConstants.BARCODE, patronBarcode);
            List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) businessObjectService.findMatching(OlePatronDocument.class, patronMap);
            if (olePatronDocumentList.size() > 0) {
                Map<String, String> patronDocMap = new HashMap<String, String>();
                OlePatronDocument olePatronDocument = olePatronDocumentList.get(0);
                patronDocMap.put(OLEConstants.PATRON_ID, olePatronDocument.getOlePatronId());
                List<PatronBillPayment> patronBillPaymentList = (List<PatronBillPayment>) businessObjectService.findMatching(PatronBillPayment.class, patronDocMap);
                if (patronBillPaymentList.size() > 0) {
                    List<OLEItemFine> oleItemFineList = getFineItemList(patronBillPaymentList);
                    oleItemFines.setOleItemFineList(oleItemFineList);
                    oleItemFines.setCode("000");
                    oleItemFines.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RTRVD_SUCCESS));
                } else {
               /*itemFineString="<fine><patronId>"+patronId+"</patronId><operatorId>"+operatorId+"</operatorId><message>Not having any fine amount</message></fine>";*/
                    oleItemFines.setCode("005");
                    oleItemFines.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_FINE));
                    itemFineString = itemFineString = oleItemFineConverter.generateCheckOutItemXml(oleItemFines);
                }

            } else {
                oleItemFines.setCode("002");
                oleItemFines.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
            }
        }
        return oleItemFineConverter.generateCheckOutItemXml(oleItemFines);
    }

    public List<OLEItemFine> getFineItemList(List<PatronBillPayment> olePatronBillDocumentList) throws Exception {
        List<OLEItemFine> oleItemFineList = new ArrayList<OLEItemFine>();
        //Fee
        org.kuali.ole.docstore.common.document.Item item = null;
        for (PatronBillPayment olePatronBillPayment : olePatronBillDocumentList) {
            //to do
            List<FeeType> feeTypeList = olePatronBillPayment.getFeeType();
            for (FeeType feeType : feeTypeList) {
                OLEItemFine oleItemFine = new OLEItemFine();
                if (feeType.getItemUuid() != null) {
                    try {
                    item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(feeType.getItemUuid());
                    oleItemFine.setCatalogueId(item.getHolding().getBib().getId());
                    oleItemFine.setTitle(item.getHolding().getBib().getTitle());
                    oleItemFine.setAuthor(item.getHolding().getBib().getAuthor());
                    } catch (Exception e) {
                        LOG.error("Exception " +e);
                    }
                }
                oleItemFine.setPatronBillId(olePatronBillPayment.getBillNumber());
                oleItemFine.setAmount((feeType.getFeeAmount() != null ? feeType.getFeeAmount().bigDecimalValue() : OLEConstants.BIGDECIMAL_DEF_VALUE));
                oleItemFine.setBalance((feeType.getBalFeeAmount() != null ? feeType.getBalFeeAmount().bigDecimalValue() : OLEConstants.BIGDECIMAL_DEF_VALUE));
                oleItemFine.setBillDate(feeType.getBillDate().toString());
                int noOfPayment = feeType.getItemLevelBillPaymentList().size();
                oleItemFine.setNoOfPayments(new Integer(noOfPayment).toString());
                if (feeType.getOleFeeType() != null) {
                    oleItemFine.setReason(feeType.getOleFeeType().getFeeTypeName());
                    oleItemFine.setFeeType(feeType.getOleFeeType().getFeeTypeCode());
                } else {
                    oleItemFine.setReason(feeType.getFeeType());
                    oleItemFine.setFeeType(feeType.getFeeType());
                }
                oleItemFine.setDateCharged(feeType.getBillDate().toString());
                oleItemFineList.add(oleItemFine);
            }
        }

        return oleItemFineList;
    }

    @Override
    public String getHolds(String patronBarcode, String operator) throws Exception {
        LOG.info("Inside Get Holds . Patron Barcode : " +patronBarcode + "Operator : "+ operator );
        OLEHoldsConverter oleHoldConverter = new OLEHoldsConverter();
        OLEHolds oleHolds = new OLEHolds();
        String itemFineString = "";
        if (!loanProcessor.hasCirculationDesk(operator)) {
            oleHolds.setCode("001");
            oleHolds.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
        } else {
            Map<String, String> patronMap = new HashMap<String, String>();
            patronMap.put(OLEConstants.BARCODE, patronBarcode);
            List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) businessObjectService.findMatching(OlePatronDocument.class, patronMap);
            if (olePatronDocumentList.size() > 0) {
                Map<String, String> patronDocMap = new HashMap<String, String>();
                OlePatronDocument olePatronDocument = olePatronDocumentList.get(0);
                patronDocMap.put(OLEConstants.OleDeliverRequest.BORROWER_ID, olePatronDocument.getOlePatronId());
                List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, patronDocMap);
                if (oleDeliverRequestBoList.size() > 0) {
                    List<OLEHold> oleHoldsList = getHoldsList(oleDeliverRequestBoList);
                    oleHolds.setOleHoldList(oleHoldsList);
                    oleHolds.setCode("000");
                    oleHolds.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RTRVD_SUCCESS));
                } else {
                    oleHolds.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_HOLD));
                    oleHolds.setCode("006");
                }
            } else {
                oleHolds.setCode("002");
                oleHolds.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
            }
        }
        return oleHoldConverter.generateHoldsmXml(oleHolds);
    }

    public List<OLEHold> getHoldsList(List<OleDeliverRequestBo> oleDeliverRequestBoList) throws Exception {
        //String availableDate = "";
        //String callNumber="";
        String copyNumber="";

        String[] availableDates;
        List<OLEHold> oleHoldList = new ArrayList<OLEHold>();
        for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBoList) {
            OLEHold oleHold = new OLEHold();
            oleHold.setItemId(oleDeliverRequestBo.getItemId());
            Map<String, Object> detailMap = oleDeliverRequestDocumentHelperService.retrieveBIbItemHoldingData(oleDeliverRequestBo.getItemUuid());
            Bib bib = (Bib) detailMap.get(OLEConstants.BIB);
            Item item = (Item) detailMap.get(OLEConstants.ITEM);
            OleHoldings oleHoldings  = (OleHoldings) detailMap.get(OLEConstants.HOLDING);
            org.kuali.ole.docstore.common.document.Item item1 = (org.kuali.ole.docstore.common.document.Item) detailMap.get("documentItem");
            oleHold.setCatalogueId(bib.getId());
            oleHold.setRequestId(oleDeliverRequestBo.getRequestId());
            if (item.getItemStatus() != null) {
                oleHold.setAvailableStatus(item.getItemStatus().getCodeValue());
            }

            /*if(item.getCallNumber()!=null && !StringUtils.isEmpty(item.getCallNumber().getNumber())){
                callNumber = loanProcessor.getItemCallNumber(item.getCallNumber());
            }else {
                callNumber = loanProcessor.getItemCallNumber(oleHoldings.getCallNumber());
            }*/
            if(item.getCopyNumber()!=null && !item.getCopyNumber().isEmpty()){
                copyNumber=item.getCopyNumber();
            }
            else{
                copyNumber=oleHoldings.getCopyNumber()!=null? oleHoldings.getCopyNumber():"";
            }
            oleHold.setTitle(bib.getTitle());
            oleHold.setAuthor(bib.getAuthor());
            oleHold.setVolumeNumber(item.getEnumeration()!=null ? item.getEnumeration():"");
            oleHold.setCallNumber(loanProcessor.getItemCallNumber(item.getCallNumber(),oleHoldings.getCallNumber()));
            oleHold.setCopyNumber(copyNumber);
            if (item.getItemType() != null)
                oleHold.setItemType(item.getItemType().getCodeValue());
            if (oleDeliverRequestBo.getRequestTypeId() != null && !oleDeliverRequestBo.getRequestTypeId().isEmpty()) {
                if (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2")) {
                    oleHold.setRecallStatus(OLEConstants.YES);
                } else {
                    oleHold.setRecallStatus(OLEConstants.NO);
                }
            }
            if (oleDeliverRequestBo.getRequestExpiryDate() != null) {
                oleHold.setRequestExpiryDate(oleDeliverRequestBo.getRequestExpiryDate().toString());
            }
            if (oleDeliverRequestBo.getCreateDate() != null) {
                oleHold.setCreateDate(oleDeliverRequestBo.getCreateDate().toString());
            }
            if (oleDeliverRequestBo.getBorrowerQueuePosition() != null) {
                oleHold.setPriority(oleDeliverRequestBo.getBorrowerQueuePosition().toString());
            }
            oleHold.setPickupLocation(oleDeliverRequestBo.getPickUpLocationId());
            if (oleDeliverRequestBo.getRecallDueDate() != null) {
                oleHold.setDateRecalled(oleDeliverRequestBo.getRecallDueDate().toString());
            }
            if (oleDeliverRequestBo.getRecallDueDate() != null) {
                oleHold.setDateRecalled(oleDeliverRequestBo.getRecallDueDate().toString());
            }
            if (oleDeliverRequestBo.getHoldExpirationDate() != null) {
                oleHold.setHoldExpiryDate(oleDeliverRequestBo.getHoldExpirationDate().toString());
            }
            if (oleDeliverRequestTypeMap!=null && oleDeliverRequestTypeMap.size() > 0) {
                if(oleDeliverRequestTypeMap.get(oleDeliverRequestBo.getRequestTypeId())!=null){
                    oleHold.setRequestType(oleDeliverRequestTypeMap.get(oleDeliverRequestBo.getRequestTypeId()).getRequestTypeCode());
                }
            }
            Map<String, String> loanMap = new HashMap<String, String>();
            loanMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, oleDeliverRequestBo.getItemId());
            List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
            if (oleLoanDocumentList.size() > 0) {
                if (oleLoanDocumentList.get(0).getLoanDueDate() != null) {
                    availableDates = oleLoanDocumentList.get(0).getLoanDueDate().toString().split(" ");
                    if (availableDates != null && availableDates.length > 0) {
                        oleHold.setAvailableDate(availableDates[0]);
                    } else {
                        oleHold.setAvailableDate(oleLoanDocumentList.get(0).getLoanDueDate().toString());
                    }
                    if (oleDeliverRequestBo.getPickUpLocationId() != null) {
                        if (oleCirculationDeskMap.size()>0 && oleCirculationDeskMap.get(oleDeliverRequestBo.getPickUpLocationId())!=null) {
                            oleHold.setDateAvailableExpires(addDate(new java.sql.Date(oleLoanDocumentList.get(0).getLoanDueDate().getTime()), Integer.parseInt(oleCirculationDeskMap.get(oleDeliverRequestBo.getPickUpLocationId()).getOnHoldDays())).toString());
                        }
                    }
                } else {
                    oleHold.setAvailableDate(OLEConstants.INDEFINITE);
                    oleHold.setDateAvailableExpires(OLEConstants.INDEFINITE);
                }
            }
            if (oleDeliverRequestBo.getRequestTypeId().equals("2") || oleDeliverRequestBo.getRequestTypeId().equals("4") || oleDeliverRequestBo.getRequestTypeId().equals("6")) {
                oleHold.setReserve(true);
            } else {
                oleHold.setReserve(false);
            }

            oleHoldList.add(oleHold);
        }

        return oleHoldList;
    }

    private java.sql.Date addDate(java.sql.Date in, int daysToAdd) {
        if (in == null) {
            return null;
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(in);
        cal.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return new java.sql.Date(cal.getTime().getTime());
    }

    private int getRenewalDays(String itemType, OleLoanDocument oleLoanDocument, String borrowerType, String numberOfRenewals) {
        String agendaName = OLEConstants.RENEWAL_AGENDA_NM;
        int renewalCount = 1;
        boolean renewalExceeds = false;
        Map<String,String> requestMap = new HashMap<String,String>();
        requestMap.put("itemUuid",oleLoanDocument.getItemUuid());
        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>)businessObjectService.findMatching(OleDeliverRequestBo.class,requestMap);
        if(oleDeliverRequestBos != null && oleDeliverRequestBos.size() > 0){
         renewalExceeds =true;
        }   else{
        HashMap<String, Object> termValues = new HashMap<String, Object>();
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        String patronId = oleLoanDocument.getPatronId()!=null ?  oleLoanDocument.getPatronId() : "";
        String itemId = oleLoanDocument.getItemId()!=null ?  oleLoanDocument.getItemId() : "";
        dataCarrierService.removeData(patronId+itemId);
        termValues.put(OLEConstants.BORROWER_TYPE, borrowerType);
        termValues.put(OLEConstants.ITEM_TYPE, itemType);
        termValues.put(OLEConstants.ITEM_STATUS, oleLoanDocument.getItemLoanStatus());
        termValues.put(OLEConstants.ITEM_SHELVING, oleLoanDocument.getItemLocation());
        termValues.put(OLEConstants.ITEM_COLLECTION, oleLoanDocument.getItemCollection());
        termValues.put(OLEConstants.ITEM_LIBRARY, oleLoanDocument.getItemLibrary());
        termValues.put(OLEConstants.ITEM_CAMPUS, oleLoanDocument.getItemCampus());
        termValues.put(OLEConstants.ITEM_INSTITUTION, oleLoanDocument.getItemInstitution());
        Integer noOfRenewals = Integer.parseInt(oleLoanDocument.getNumberOfRenewals());
        termValues.put(OLEConstants.NUM_RENEWALS, noOfRenewals);
        termValues.put(OLEConstants.PATRON_ID_POLICY, patronId);
        termValues.put(OLEConstants.ITEM_ID_POLICY, itemId);
        try {
            EngineResults engineResults = loanProcessor.getEngineResults(agendaName, termValues);
            dataCarrierService.removeData(patronId+itemId);
            List<ResultEvent> allResults = engineResults.getAllResults();
            for (Iterator<ResultEvent> resultEventIterator = allResults.iterator(); resultEventIterator.hasNext(); ) {
                ResultEvent resultEvent = resultEventIterator.next();
                if (resultEvent.getType().equals(OLEConstants.RULE_EVALUATED) && resultEvent.getSource().equals(OLEConstants.RENEWAL_LIMIT)) {
                    renewalExceeds = true;
                }
            }
            if (renewalExceeds) {
                renewalCount = 0;
            } else {
                renewalCount = 1;
            }
        } catch (Exception e) {

        }

        }
        return renewalCount;
    }

    /**
     * This method is used return the operator id from operator name
     *
     * @return operatorId
     */
    public String getOperatorId(String operatorName) {
        Map<String, String> principalMap = new HashMap<String, String>();
        String principalId = null;
        principalMap.put("principalName", operatorName);
        List<PrincipalBo> principalBos = (List<PrincipalBo>) businessObjectService.findMatching(PrincipalBo.class, principalMap);
        if (principalBos != null && principalBos.size() > 0) {
            principalId = principalBos.get(0).getPrincipalId();
        }
        return principalId;
    }

    /**
     * This method is used to check whether the barcode belongs to a patron or not
     * @param patronBarcode
     * @return
     */
    public boolean vaildPatron(String patronBarcode){
        boolean valid= false;
        Map<String,String> patronMap = new HashMap<String,String>();
        patronMap.put("barcode",patronBarcode);
        List<OlePatronDocument> olePatronDocuments =(List<OlePatronDocument>) businessObjectService.findMatching(OlePatronDocument.class,patronMap);
        if(olePatronDocuments!=null && olePatronDocuments.size()>0){
            valid=true;
        }
        return valid;
    }

    /**
     * This method is used to generate the ole checked out item list form the loan document list
     * @param oleLoanDocumentList
     * @return
     */
    public List<OLECheckedOutItem> getPatronCheckedOutItemList(List<OleLoanDocument> oleLoanDocumentList,String patronType,boolean renewableNeeded){
        SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OleDeliverRequest.DATE_FORMAT);
        String renewParameter = loanProcessor.getParameter(OLEConstants.RENEW_INFO_INDICATOR);
        boolean renewInfoNeeded = true;
        if(renewParameter!=null && !renewParameter.isEmpty()){
            if(renewParameter.equalsIgnoreCase("N")){
             renewInfoNeeded = false;
            }
        }
        List<OLECheckedOutItem> oleCheckedOutItems = new ArrayList<OLECheckedOutItem>();
        if(oleLoanDocumentList.size()>0){
            OLECheckedOutItem oleCheckedOutItem = null;
          for(OleLoanDocument oleLoanDocument : oleLoanDocumentList){
             oleCheckedOutItem = new OLECheckedOutItem();
              oleCheckedOutItem.setCopyNumber(oleLoanDocument.getItemCopyNumber());
              oleCheckedOutItem.setVolumeNumber(oleLoanDocument.getEnumeration());
              oleCheckedOutItem.setAcquiredFine("");
              oleCheckedOutItem.setDateRecalled("");
              oleCheckedOutItem.setTitle(oleLoanDocument.getTitle());
              oleCheckedOutItem.setAuthor(oleLoanDocument.getAuthor());
              oleCheckedOutItem.setCallNumber(oleLoanDocument.getItemCallNumber());
              oleCheckedOutItem.setCatalogueId(oleLoanDocument.getBibUuid());
              if (oleLoanDocument.getLoanDueDate() != null) {
                  oleCheckedOutItem.setDueDate(oleLoanDocument.getLoanDueDate().toString());
                  if ((fmt.format(oleLoanDocument.getLoanDueDate())).compareTo(fmt.format(DateUtils.addDays(new Date(System.currentTimeMillis()),-1))) > 0) {
                      oleCheckedOutItem.setOverDue(false);
                  }
                  else{
                      oleCheckedOutItem.setOverDue(true);
                  }
              } else {
                  oleCheckedOutItem.setDueDate((new java.sql.Timestamp(new Date(2025, 1, 1).getTime()).toString()));
              }
              if (oleLoanDocument.getRenewalLoanDueDate() != null){
                  oleCheckedOutItem.setDateRenewed(oleLoanDocument.getRenewalLoanDueDate().toString());
              }
              else{
                  oleCheckedOutItem.setDateRenewed("");
              }
              oleCheckedOutItem.setItemType(oleLoanDocument.getItemType());
              if (null!= oleLoanDocument.getCreateDate()) {
                  oleCheckedOutItem.setLoanDate(new Timestamp(oleLoanDocument.getCreateDate().getTime()).toString());
              }
              oleCheckedOutItem.setItemId(oleLoanDocument.getItemId());
              if (oleLoanDocument.getNoOfOverdueNoticesSentForBorrower() != null) {
                  oleCheckedOutItem.setNumberOfOverdueSent(oleLoanDocument.getNoOfOverdueNoticesSentForBorrower());
              } else {
                  oleCheckedOutItem.setNumberOfOverdueSent("1");
              }
              if (renewableNeeded && renewInfoNeeded) {
              oleCheckedOutItem.setNumberOfRenewals(oleLoanDocument.getNumberOfRenewals());
              }
            oleCheckedOutItems.add(oleCheckedOutItem);
          }
        }
        return oleCheckedOutItems;
    }

    public Map<String,OleCirculationDesk> getAvailableCirculationDesks(){
        Map<String,OleCirculationDesk> circulationDeskMap = new HashMap<String,OleCirculationDesk>();
        List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>)getBusinessObjectService().findAll(OleCirculationDesk.class);
        if(oleCirculationDeskList!=null && oleCirculationDeskList.size()>0){
            for(OleCirculationDesk oleCirculationDesk : oleCirculationDeskList){
                circulationDeskMap.put(oleCirculationDesk.getCirculationDeskId(),oleCirculationDesk);
            }
        }
        return circulationDeskMap;
    }

    public Map<String,OleDeliverRequestType> getAvailableRequestTypes(){
        Map<String,OleDeliverRequestType> requestTypeMap = new HashMap<String,OleDeliverRequestType>();
        List<OleDeliverRequestType> oleDeliverRequestTypeList = (List<OleDeliverRequestType>)getBusinessObjectService().findAll(OleDeliverRequestType.class);
        if(oleDeliverRequestTypeList!=null && oleDeliverRequestTypeList.size()>0){
            for(OleDeliverRequestType oleDeliverRequestType : oleDeliverRequestTypeList){
                requestTypeMap.put(oleDeliverRequestType.getRequestTypeId(),oleDeliverRequestType);
            }
        }
        return requestTypeMap;
    }


    /**
     * This method is used to set set the patron profile information for the given  patron barcode
     * @param patronBarcode
     * @param operator
     * @param agencyId
     * @return
     */
   @Override
    public String lookupUserForNCIP(String patronBarcode, String operator, String agencyId) {
       Long startTime =System.currentTimeMillis();
        LOG.info("Inside the look up user : patron Barcode : " + patronBarcode + "operator : "+ operator + "agencyId : " + agencyId );
        OLELookupUser lookupUser = new OLELookupUser();
        if (!loanProcessor.hasCirculationDesk(operator)) {
            lookupUser.setCode("001");
            lookupUser.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
        } else {
            OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb)SpringContext.getBean("oleLoanDao");
            OleDeliverDaoJdbc oleDeliverDaoJdbc = (OleDeliverDaoJdbc)SpringContext.getBean("oleDeliverDaoJdbc");
           OlePatronDocument olePatronDocument =  oleDeliverDaoJdbc.getPatronDocument(patronBarcode);

            if (olePatronDocument!=null) {
                Long startTimeInit = System.currentTimeMillis();
                lookupUser = oleCirculationHelperService.initialiseLookupUser(olePatronDocument, agencyId);
                Long endTimeInit = System.currentTimeMillis();
                Long timeDiffInit = endTimeInit-startTimeInit;
                LOG.info("Time taken to set the init details : " +timeDiffInit);
                try {
                    Long startTimeLoan = System.currentTimeMillis();
                    //   List<OleLoanDocument> oleLoanDocumentList = loanProcessor.getPatronLoanedItemBySolr(olePatronDocument.getOlePatronId());
                  List<OleLoanDocument> oleLoanDocumentList = oleLoanDocumentDaoOjb.getDeliverLoans(olePatronDocument.getOlePatronId());
                  if(oleLoanDocumentList!=null){
                   List<OLECheckedOutItem> oleCheckedOutItemList =  oleDeliverDaoJdbc.getCheckedOutItemsList(oleLoanDocumentList);
                    //List<OLECheckedOutItem> checkedOutItemList = getPatronCheckedOutItemList(olePatronDocument.getOleLoanDocuments(),olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                    if(oleCheckedOutItemList!=null){
                    OLECheckedOutItems oleCheckedOutItems = new OLECheckedOutItems();
                    if (oleCheckedOutItemList != null && oleCheckedOutItemList.size() > 0) {
                        oleCheckedOutItems.setCheckedOutItems(oleCheckedOutItemList);
                    }
                    lookupUser.setOleCheckedOutItems(oleCheckedOutItems);
                    }
                    }
                    Long endTimeLoan = System.currentTimeMillis();
                    Long timeDiffLoan = endTimeLoan-startTimeLoan;
                    LOG.info("Time taken to set the loan details : " +timeDiffLoan);
                } catch (Exception e) {
                    LOG.info("Exception Occurred While Retrieving the checked out items");
                    LOG.error(e);
                }
                try {
                    Long startTimeHold = System.currentTimeMillis();
                 /*   List<OLEHold> oleHoldList = getHoldRecordsList(olePatronDocument.getOleDeliverRequestBos());*/

                    List<OleDeliverRequestBo> oleDeliverRequestBoList = oleLoanDocumentDaoOjb.getDeliverRequests(patronBarcode);

                    if(oleDeliverRequestBoList!=null){
                    List<OLEHold> oleHoldList = oleDeliverDaoJdbc.getHoldRecordsList(oleDeliverRequestBoList);
                    if(oleHoldList!=null){
                    OLEHolds oleHolds = new OLEHolds();
                    if (oleHoldList != null && oleHoldList.size() > 0) {
                        oleHolds.setOleHoldList(oleHoldList);
                    }
                    lookupUser.setOleHolds(oleHolds);
                    }
                    }
                    Long endTimeHold = System.currentTimeMillis();
                    Long timeDiffHold = endTimeHold-startTimeHold;
                    LOG.info("Time taken to set the hold details : " +timeDiffHold);
                } catch (Exception e) {
                    LOG.info("Exception Occurred While Retrieving the Hold items");
                    LOG.error(e);
                }
                try {
                    Long startTimeFine = System.currentTimeMillis();
                    List<PatronBillPayment> patronBillPaymentList = oleLoanDocumentDaoOjb.getPatronBillPayments(olePatronDocument.getOlePatronId());
                    if(patronBillPaymentList!=null){
                     List<OLEItemFine> oleItemFineList = oleDeliverDaoJdbc.getFineItemLists(patronBillPaymentList);
                        if(oleItemFineList!=null){
                     OLEItemFines oleItemFines = new OLEItemFines();
                     oleItemFines.setOleItemFineList(oleItemFineList);
                    lookupUser.setOleItemFines(oleItemFines);
                        }
                    }
                    Long endTimeFine = System.currentTimeMillis();
                    Long timeDiffFine = endTimeFine-startTimeFine;
                    LOG.info("Time taken to set the fine details : " +timeDiffFine);
                } catch (Exception e) {
                    LOG.info("Exception Occurred While Retrieving Fine");
                    LOG.error(e);
                }
                lookupUser.setCode("000");
                lookupUser.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RTRVD_SUCCESS));
            } else {
                lookupUser.setCode("002");
                lookupUser.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
            }
        }
        String responseMessage = oleLookupUserConverter.generateLookupUserResponseXml(lookupUser);
       Long endTime = System.currentTimeMillis();
       Long timediff = endTime-startTime;
       LOG.info("Time taken to complete the lookup user service " + timediff);
       return responseMessage;
    }


}



