package org.kuali.ole.ncip.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.extensiblecatalog.ncip.v2.service.*;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.deliver.util.NoticeInfo;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.BibTrees;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.ncip.service.NCIPAcceptItemResponseBuilder;
import org.kuali.ole.ncip.service.OLEAcceptItemService;
import org.kuali.ole.ncip.util.OLENCIPAcceptItemUtil;
import org.kuali.ole.ncip.util.OLENCIPUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.service.impl.IdentityManagementServiceImpl;

import java.util.*;

/**
 * Created by pvsubrah on 8/13/15.
 */
public class OLENCIPAcceptItemServiceImpl extends OLENCIPUtil implements OLEAcceptItemService {

    private static final Logger LOG = Logger.getLogger(OLENCIPAcceptItemServiceImpl.class);

    private OLECirculationHelperServiceImpl oleCirculationHelperService;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;
    private OLENCIPAcceptItemUtil olencipAcceptItemUtil;
    private DocstoreClientLocator docstoreClientLocator;
    private DocstoreUtil docstoreUtil;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = (DocstoreClientLocator) SpringContext.getService("docstoreClientLocator");
        }
        return docstoreClientLocator;
    }

    public void setDocstoreClientLocator(DocstoreClientLocator docstoreClientLocator) {
        this.docstoreClientLocator = docstoreClientLocator;
    }

    public NCIPAcceptItemResponseBuilder getNCIPAcceptItemResponseBuilder() {
        return new NCIPAcceptItemResponseBuilder();
    }

    public OLECirculationHelperServiceImpl getOleCirculationHelperService() {
        if (null == oleCirculationHelperService) {
            oleCirculationHelperService = GlobalResourceLoader.getService(OLENCIPConstants.CIRCULATION_HELPER_SERVICE);
        }
        return oleCirculationHelperService;
    }

    public void setOleCirculationHelperService(OLECirculationHelperServiceImpl oleCirculationHelperService) {
        this.oleCirculationHelperService = oleCirculationHelperService;
    }

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (oleDeliverRequestDocumentHelperService == null) {
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }

    public void setOleDeliverRequestDocumentHelperService(OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService) {
        this.oleDeliverRequestDocumentHelperService = oleDeliverRequestDocumentHelperService;
    }

    public OLENCIPAcceptItemUtil getOlencipAcceptItemUtil() {
        if (null == olencipAcceptItemUtil) {
            olencipAcceptItemUtil = new OLENCIPAcceptItemUtil();
        }
        return olencipAcceptItemUtil;
    }

    public void setOlencipAcceptItemUtil(OLENCIPAcceptItemUtil olencipAcceptItemUtil) {
        this.olencipAcceptItemUtil = olencipAcceptItemUtil;
    }

    public DocstoreUtil getDocstoreUtil() {
        if (null == docstoreUtil) {
            docstoreUtil = new DocstoreUtil();
        }
        return docstoreUtil;
    }

    public void setDocstoreUtil(DocstoreUtil docstoreUtil) {
        this.docstoreUtil = docstoreUtil;
    }

    @Override
    public AcceptItemResponseData performService(AcceptItemInitiationData acceptItemInitiationData, ServiceContext serviceContext, RemoteServiceManager remoteServiceManager) throws ServiceException {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();

        NCIPAcceptItemResponseBuilder ncipAcceptItemResponseBuilder = new NCIPAcceptItemResponseBuilder();

        AcceptItemResponseData acceptItemResponseData = new AcceptItemResponseData();

        AgencyId agencyId = validateAgency(acceptItemInitiationData.getInitiationHeader(), acceptItemResponseData);
        if (null == agencyId) return acceptItemResponseData;

        boolean userValid = validateUser(acceptItemInitiationData.getUserId(), acceptItemResponseData);
        if (!userValid) return acceptItemResponseData;

        OlePatronDocument olePatronDocument = validPatron(acceptItemInitiationData.getUserId(), acceptItemResponseData);
        if (null == olePatronDocument) return acceptItemResponseData;

        String operatorId = agencyPropertyMap.get(OLENCIPConstants.OPERATOR_ID);
        OleCirculationDesk oleCirculationDesk = validOperator(acceptItemResponseData, operatorId);
        if (null == oleCirculationDesk) return acceptItemResponseData;

        String requestType = agencyPropertyMap.get(OLENCIPConstants.REQUEST_TYPE);
        String requestTypeId = getOlencipAcceptItemUtil().validateRequestType(requestType);
        if (requestTypeId == null) {
            processProblems(acceptItemResponseData, requestType, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_RQST_TYP), OLENCIPConstants.ITEM);
            return acceptItemResponseData;
        }

        String itemType = validItemType(acceptItemResponseData);
        if (null == itemType) return acceptItemResponseData;

        String location = agencyPropertyMap.get(OLENCIPConstants.ITEM_LOCATION);
        boolean locationValid = validLocation(acceptItemResponseData, location);
        if (!locationValid) return acceptItemResponseData;

        String pickUpLocation = acceptItemInitiationData.getPickupLocation() != null && acceptItemInitiationData.getPickupLocation().getValue() != null ? acceptItemInitiationData.getPickupLocation().getValue() : "";
        OleCirculationDesk olePickUpLocation = getOlencipAcceptItemUtil().validatePickUpLocation(pickUpLocation, location);
        if (olePickUpLocation == null) {
            processProblems(acceptItemResponseData, pickUpLocation, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_PICKUP_LOCN), OLENCIPConstants.ITEM);
            return acceptItemResponseData;
        }

        String itemBarcode = acceptItemInitiationData.getItemId().getItemIdentifierValue();
        if (StringUtils.isBlank(itemBarcode)) {
            processProblems(acceptItemResponseData, "", ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.ITEM_IDENTIFIER_VALUE_DOES_NOT_EXIST), OLENCIPConstants.ITEM);
            return acceptItemResponseData;
        }
        Long startDocstoreSerachCallTime = System.currentTimeMillis();
        boolean available = getDocstoreUtil().isItemAvailableInDocStore(itemBarcode);
        Long endDocstoreSerachCallTime = System.currentTimeMillis();
        LOG.info("Time taken to check whether the item barcode is available or not : " + (endDocstoreSerachCallTime - startDocstoreSerachCallTime));
        if (available) {
            processProblems(acceptItemResponseData, itemBarcode, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_EXIST), OLENCIPConstants.ITEM);
            return acceptItemResponseData;
        }

        String callNumber = acceptItemInitiationData.getItemOptionalFields() != null && acceptItemInitiationData.getItemOptionalFields().getItemDescription() != null
                && acceptItemInitiationData.getItemOptionalFields().getItemDescription().getCallNumber() != null ? acceptItemInitiationData.getItemOptionalFields().getItemDescription().getCallNumber() : "";
        String title = acceptItemInitiationData.getItemOptionalFields() != null && acceptItemInitiationData.getItemOptionalFields().getBibliographicDescription() != null
                && acceptItemInitiationData.getItemOptionalFields().getBibliographicDescription().getTitle() != null ? acceptItemInitiationData.getItemOptionalFields().getBibliographicDescription().getTitle() : "";
        String author = acceptItemInitiationData.getItemOptionalFields() != null && acceptItemInitiationData.getItemOptionalFields().getBibliographicDescription() != null
                && acceptItemInitiationData.getItemOptionalFields().getBibliographicDescription().getAuthor() != null ? acceptItemInitiationData.getItemOptionalFields().getBibliographicDescription().getAuthor() : "";

        if (StringUtils.isBlank(title)) {
            processProblems(acceptItemResponseData, "", ConfigContext.getCurrentContextConfig().getProperty(OLENCIPConstants.TITLE_DOES_NOT_EXIST), OLENCIPConstants.ITEM);
            return acceptItemResponseData;
        }

        OleDeliverRequestBo oleDeliverRequestBo = getOlencipAcceptItemUtil().getOleDeliverRequestBo(itemType, location, requestTypeId, requestType, olePatronDocument);

        if (!getOleDeliverRequestDocumentHelperService().isItemAvailable(oleDeliverRequestBo)) {
            processProblems(acceptItemResponseData, itemBarcode, requestType + OLEConstants.RQST_CONDITION + OLEConstants.AVAILABLE, OLENCIPConstants.ITEM);
            return acceptItemResponseData;
        }

        DroolsResponse droolsResponse = firePlaceRequestRules(oleDeliverRequestBo);

        if (!droolsResponse.isRuleMatched()) {
            processProblems(acceptItemResponseData, itemBarcode, "No Rule Found", OLENCIPConstants.ITEM);
            return acceptItemResponseData;
        } else if (StringUtils.isNotBlank(droolsResponse.getErrorMessage().getErrorMessage())) {
            processProblems(acceptItemResponseData, itemBarcode, droolsResponse.getErrorMessage().getErrorMessage(), OLENCIPConstants.ITEM);
            return acceptItemResponseData;
        }

        String requestExpirationDay = null;
        if (droolsResponse.getDroolsExchange().getFromContext("requestExpirationDays") != null) {
            requestExpirationDay = droolsResponse.getDroolsExchange().getFromContext("requestExpirationDays").toString();
        }

        String requestExpirationConfigName = null;
        if (droolsResponse.getDroolsExchange().getFromContext(OLEConstants.REQUEST_EXPIRATION_NOTICE_CONTENT_CONFIG_NAME) != null){
            requestExpirationConfigName = droolsResponse.getDroolsExchange().getFromContext(OLEConstants.REQUEST_EXPIRATION_NOTICE_CONTENT_CONFIG_NAME).toString();
        }

        String onHoldConfigName = null;
        if (droolsResponse.getDroolsExchange().getFromContext(OLEConstants.ON_HOLD_NOTICE_CONTENT_CONFIG_NAME) != null){
            onHoldConfigName = droolsResponse.getDroolsExchange().getFromContext(OLEConstants.ON_HOLD_NOTICE_CONTENT_CONFIG_NAME).toString();
        }

        String onHoldCourtesyConfigName = null;
        if (droolsResponse.getDroolsExchange().getFromContext(OLEConstants.ON_HOLD_COURTESY_NOTICE_CONTENT_CONFIG_NAME) != null){
            onHoldCourtesyConfigName = droolsResponse.getDroolsExchange().getFromContext(OLEConstants.ON_HOLD_COURTESY_NOTICE_CONTENT_CONFIG_NAME).toString();
        }

        String onHoldExpirationConfigName = null;
        if (droolsResponse.getDroolsExchange().getFromContext(OLEConstants.ON_HOLD_EXPIRATION_NOTICE_CONTENT_CONFIG_NAME) != null){
            onHoldExpirationConfigName = droolsResponse.getDroolsExchange().getFromContext(OLEConstants.ON_HOLD_EXPIRATION_NOTICE_CONTENT_CONFIG_NAME).toString();
        }


        String itemIdentifier = null;
        String bibId = null;
        BibTrees bibTrees = null;
        String operatorName = null;
        try {
            if(StringUtils.isNotBlank(operatorId)) {
                IdentityManagementServiceImpl identityManagementService = new IdentityManagementServiceImpl();
                Principal principal = identityManagementService.getPrincipal(operatorId);
                if(principal!=null) {
                    operatorName = principal.getPrincipalName();
                }
            }
            Long startCreationOfItemTime = System.currentTimeMillis();
            if(StringUtils.isNotBlank(operatorName)) {
                bibTrees = getOleCirculationHelperService().createItem(itemBarcode, callNumber, title, author, itemType, location, operatorName);
            }
            else {
                bibTrees = getOleCirculationHelperService().createItem(itemBarcode, callNumber, title, author, itemType, location, operatorId);
            }
            Long endCreationOfItemTime = System.currentTimeMillis();
            LOG.info("Time taken to create item : " + (endCreationOfItemTime - startCreationOfItemTime));
        } catch (Exception e) {
            LOG.error("Exception " + e);
            processProblems(acceptItemResponseData, itemBarcode, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_NOT_CREATED), OLENCIPConstants.ITEM);
            return acceptItemResponseData;
        }
        if (bibTrees != null
                && CollectionUtils.isNotEmpty(bibTrees.getBibTrees()) && CollectionUtils.isNotEmpty(bibTrees.getBibTrees().get(0).getHoldingsTrees())
                && CollectionUtils.isNotEmpty(bibTrees.getBibTrees().get(0).getHoldingsTrees().get(0).getItems())) {

            itemIdentifier = bibTrees.getBibTrees().get(0).getHoldingsTrees().get(0).getItems().get(0).getId();
            bibId = bibTrees.getBibTrees().get(0).getBib().getId();
        } else {
            itemIdentifier = "";
        }

        if (StringUtils.isBlank(itemIdentifier)) {
            processProblems(acceptItemResponseData, itemBarcode, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_NOT_CREATED), OLENCIPConstants.ITEM);
            return acceptItemResponseData;
        }

        Map responseMap = getOlencipAcceptItemUtil().placeRequest(operatorId, olePatronDocument, itemBarcode, itemIdentifier, olePickUpLocation, oleDeliverRequestBo.getRequestTypeId(), bibId, title, author, callNumber, requestExpirationDay, location, requestExpirationConfigName,onHoldConfigName,onHoldCourtesyConfigName,onHoldExpirationConfigName);
        try {
            if (responseMap.get(OLEConstants.STATUS).equals(OLEConstants.RQST_SUCCESS)) {
                ncipAcceptItemResponseBuilder.setRequestId(acceptItemResponseData, agencyId, responseMap.get(OLEConstants.OleDeliverRequest.REQUEST_ID).toString());
                ncipAcceptItemResponseBuilder.setItemId(acceptItemResponseData, agencyId, itemBarcode);
            } else {
                processProblems(acceptItemResponseData, itemBarcode, ConfigContext.getCurrentContextConfig().getProperty(responseMap.get(OLEConstants.STATUS).toString()), OLENCIPConstants.ITEM);
                getDocstoreClientLocator().getDocstoreClient().deleteBib(bibId);
                return acceptItemResponseData;
            }

        } catch (Exception e) {
            LOG.error("Exception " + e);
        }

        oleStopWatch.end();
        LOG.info("Time taken to perform accept item service : " + oleStopWatch.getTotalTime());
        return acceptItemResponseData;
    }

    private DroolsResponse firePlaceRequestRules(OleDeliverRequestBo oleDeliverRequestBo) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();

        DroolsResponse droolsResponse = new DroolsResponse();
        NoticeInfo noticeInfo = new NoticeInfo();
        ArrayList<Object> facts = new ArrayList<Object>();
        facts.add(noticeInfo);
        facts.add(droolsResponse);
        facts.add(oleDeliverRequestBo);
        facts.add(oleDeliverRequestBo.getOlePatron());
        new CircUtilController().fireRules(facts, null, "place-request");

        oleStopWatch.end();

        LOG.info("Time taken for fire rules to place request  : " + oleStopWatch.getTotalTime());
        return droolsResponse;
    }

}


