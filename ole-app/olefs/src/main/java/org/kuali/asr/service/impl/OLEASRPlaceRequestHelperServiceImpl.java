package org.kuali.asr.service.impl;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.LoanUtil;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleDeliverBatchServiceImpl;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.batch.OleSms;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.notice.service.OleNoticeService;
import org.kuali.ole.deliver.notice.service.impl.OleNoticeServiceImpl;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.ingest.pojo.MatchBo;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.engine.*;
import org.kuali.rice.krms.impl.repository.AgendaBo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 1/15/14
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEASRPlaceRequestHelperServiceImpl {
    private static final Logger LOG = Logger.getLogger(OLEASRPlaceRequestHelperServiceImpl.class);
    private static final String NAMESPACE_CODE_SELECTOR = "namespaceCode";
    private static final String NAME_SELECTOR = "name";
    private final static String RULE_EVALUATED = "Rule Evaluated";
    private final static String ROUTED_EXTERNAL = "Routed External";
    private BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
    private int queuePosition = 0;
    private LoanProcessor loanProcessor = new LoanProcessor();
    private DocstoreUtil docstoreUtil = new DocstoreUtil();
    private CircDeskLocationResolver circDeskLocationResolver;
    private OleNoticeService noticeService=new OleNoticeServiceImpl();

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    private PermissionService getPermissionService() {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service;
    }

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
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

    public void setbusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public LoanProcessor getLoanProcessor() {
        return loanProcessor;
    }

    public void setLoanProcessor(LoanProcessor loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    /**
     * This method is used to change the request type based on selection of  pick up  location
     *
     * @param oleDeliverRequestBo
     * @return oleDeliverRequestBo
     */
    public OleDeliverRequestBo processRequestType(OleDeliverRequestBo oleDeliverRequestBo) {

        LOG.debug("Inside processRequestType for the Request id :" + oleDeliverRequestBo.getRequestId());
        oleDeliverRequestBo = processPatron(oleDeliverRequestBo);
        oleDeliverRequestBo = processItem(oleDeliverRequestBo);
        if (oleDeliverRequestBo.getRequestTypeId() != null) {
            if (oleDeliverRequestBo.getRequestTypeId().equals("3") && oleDeliverRequestBo.getPickUpLocationId() != null && !oleDeliverRequestBo.getPickUpLocationId().isEmpty()) {
                oleDeliverRequestBo.setRequestTypeId("4");
            } else if (oleDeliverRequestBo.getRequestTypeId().equals("5") && oleDeliverRequestBo.getPickUpLocationId() != null && !oleDeliverRequestBo.getPickUpLocationId().isEmpty()) {
                oleDeliverRequestBo.setRequestTypeId("6");
            } else if (oleDeliverRequestBo.getRequestTypeId().equals("1") && oleDeliverRequestBo.getPickUpLocationId() != null && !oleDeliverRequestBo.getPickUpLocationId().isEmpty()) {
                oleDeliverRequestBo.setRequestTypeId("2");
            }
        }
        return oleDeliverRequestBo;
    }


    /**
     * This is to create a new patron object if the user enters the operator Id manually
     *
     * @param oleDeliverRequestBo
     * @return deliver
     */
    public OleDeliverRequestBo processPatron(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside processPatron");
        OleDeliverRequestBo deliver = oleDeliverRequestBo;

        if (oleDeliverRequestBo.getBorrowerBarcode() != null || oleDeliverRequestBo.getProxyBorrowerBarcode() != null) {
            Map<String, String> patronMap = new HashMap<String, String>();
            patronMap.put(OLEConstants.OleDeliverRequest.PATRON_BARCODE, oleDeliverRequestBo.getBorrowerBarcode());
            List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) businessObjectService.findMatching(OlePatronDocument.class, patronMap);
            if (olePatronDocumentList != null && olePatronDocumentList.size() > 0) {
                deliver.setOlePatron(olePatronDocumentList.get(0));
                deliver.setBorrowerName(olePatronDocumentList.get(0).getPatronName());
                deliver.setBorrowerId(olePatronDocumentList.get(0).getOlePatronId());
            } else if (olePatronDocumentList != null && olePatronDocumentList.size() == 0) {
                deliver.setBorrowerId(null);
                deliver.setFirstName(null);
                deliver.setLastName(null);
                deliver.setOlePatron(null);
                deliver.setBorrowerName(null);
                deliver.setBorrowerBarcode(null);
            }
            if (oleDeliverRequestBo.getRequestCreator().equals(OLEConstants.OleDeliverRequest.REQUESTER_PROXY_PATRON)) {
                Map<String, String> proxyPatronMap = new HashMap<String, String>();
                proxyPatronMap.put(OLEConstants.OleDeliverRequest.PATRON_BARCODE, oleDeliverRequestBo.getProxyBorrowerBarcode());
                List<OlePatronDocument> oleProxyPatronDocumentList = (List<OlePatronDocument>) businessObjectService.findMatching(OlePatronDocument.class, proxyPatronMap);
                if (oleProxyPatronDocumentList != null && oleProxyPatronDocumentList.size() > 0) {
                    deliver.setOleProxyPatron(oleProxyPatronDocumentList.get(0));
                    deliver.setProxyBorrowerName(oleProxyPatronDocumentList.get(0).getPatronName());
                    deliver.setProxyBorrowerId(oleProxyPatronDocumentList.get(0).getOlePatronId());
                } else if (oleProxyPatronDocumentList != null && oleProxyPatronDocumentList.size() == 0) {
                    deliver.setProxyBorrowerId(null);
                    deliver.setProxyBorrowerName(null);
                    deliver.setOleProxyPatron(null);
                }
            }

        }

        return deliver;
    }

    public boolean processOperator(String principalId) {
        /*if (getPermissionService().hasPermission(oleDeliverRequestBo.getOperatorCreateId(), OLEConstants.OlePatron.PATRON_NAMESPACE, OLEConstants.CAN_LOAN)) {
            return true;
        }*/
        // Modified as per comments in Jira OLE-4901
        boolean isOperator = true;
        Collection<String> roles = getRoleService().getRoleMemberPrincipalIds(OLEConstants.OlePatron.PATRON_NAMESPACE, OLEConstants.OleDeliverRequest.REQUESTER_OPERATOR, null);
        if (roles != null) {
            isOperator = roles.contains(principalId);
        }
        return isOperator;
    }

    /**
     * This is to check whether the pick up location is selected for the hold type of Request
     *
     * @param oleDeliverRequestBo
     * @return validRequest
     */
    public boolean validateRequestType(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside validateRequestType");
        boolean validRequest = true;
        if ((oleDeliverRequestBo.getRequestTypeId().equals("2") || oleDeliverRequestBo.getRequestTypeId().equals("4") || oleDeliverRequestBo.getRequestTypeId().equals("6")) && (oleDeliverRequestBo.getPickUpLocationId() == null) || (oleDeliverRequestBo.getPickUpLocationId() != null && oleDeliverRequestBo.getPickUpLocationId().isEmpty())) {
            validRequest = false;
        }
        return validRequest;
    }


    /**
     * This is to check whether the patron is having delivery privilege or not
     *
     * @param oleDeliverRequestBo
     * @return hasDeliveryPrivilege
     */
    public boolean validateDeliveryPrivilege(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside validateDeliveryPrivilege");
        boolean hasDeliveryPrivilege = true;
        if (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("3") || oleDeliverRequestBo.getRequestTypeId().equals("5")) {
            if (oleDeliverRequestBo.getOlePatron() != null && !oleDeliverRequestBo.getOlePatron().isDeliveryPrivilege()) {
                hasDeliveryPrivilege = false;
            }
        } else if (oleDeliverRequestBo.getRequestTypeId().equals("2") || oleDeliverRequestBo.getRequestTypeId().equals("4") || oleDeliverRequestBo.getRequestTypeId().equals("6")) {
            if (oleDeliverRequestBo.getOlePatron() != null && !oleDeliverRequestBo.getOlePatron().isDeliveryPrivilege() && oleDeliverRequestBo.getPickUpLocationId() == null) {
                hasDeliveryPrivilege = false;
            }
        }
        return hasDeliveryPrivilege;
    }


    /**
     * This is to check whether the patron is having paging privilege or not
     *
     * @param oleDeliverRequestBo
     * @return hasPagePrivilege
     */
    public boolean validatePagingPrivilege(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside validatePagingPrivilege");
        boolean hasPagePrivilege = true;
        if (oleDeliverRequestBo.getRequestTypeId().equals("5") || oleDeliverRequestBo.getRequestTypeId().equals("6")) {
            if (!oleDeliverRequestBo.getOlePatron().isPagingPrivilege()) {
                hasPagePrivilege = false;
            }
        }
        return hasPagePrivilege;
    }


    /**
     * This for setting the null values for the unselected requester
     *
     * @param oleDeliverRequestBo
     * @return oleDeliverRequestBo
     */
    public OleDeliverRequestBo processRequester(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside processRequester");
        if (oleDeliverRequestBo.getRequestCreator().equals(OLEConstants.OleDeliverRequest.REQUESTER_PATRON)) {
            oleDeliverRequestBo.setProxyBorrowerId(null);
            oleDeliverRequestBo.setOleProxyPatron(null);
            oleDeliverRequestBo.setOperatorCreateId(null);
            oleDeliverRequestBo.setOperatorCreator(null);
        } else if (oleDeliverRequestBo.getRequestCreator().equals(OLEConstants.OleDeliverRequest.REQUESTER_PROXY_PATRON)) {
            oleDeliverRequestBo.setOperatorCreateId(null);
            oleDeliverRequestBo.setOperatorCreator(null);
        } else if (oleDeliverRequestBo.getRequestCreator().equals(OLEConstants.OleDeliverRequest.REQUESTER_OPERATOR)) {
            oleDeliverRequestBo.setProxyBorrowerId(null);
            oleDeliverRequestBo.setOleProxyPatron(null);
        }
        return oleDeliverRequestBo;
    }


    /**
     * This is to check whether the login patron is the proxy patron for the real patron
     *
     * @param oleDeliverRequestBo
     * @return validProxy
     */
    public boolean isValidProxyPatron(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside isValidProxyPatron");
        boolean validProxy = true;
        Map<String, String> proxyMap = new HashMap<String, String>();
        proxyMap.put(OLEConstants.OleDeliverRequest.PATRON_ID, oleDeliverRequestBo.getBorrowerId());
        proxyMap.put(OLEConstants.OleDeliverRequest.PROXY_PATRON_ID, oleDeliverRequestBo.getProxyBorrowerId());
        if (oleDeliverRequestBo.getProxyBorrowerId() != null && !oleDeliverRequestBo.getProxyBorrowerId().isEmpty()) {
            List<OleProxyPatronDocument> proxyPatronDocuments = (List<OleProxyPatronDocument>) businessObjectService.findMatching(OleProxyPatronDocument.class, proxyMap);
            if (proxyPatronDocuments.size() == 0) {
                validProxy = false;
            }
        }
        return validProxy;
    }


    /**
     * This is to check whether the request is already raised by the patron for this item
     *
     * @param oleDeliverRequestBo
     * @return alreadyExist
     */
    public boolean isRequestAlreadyRaisedByPatron(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside isRequestAlreadyRaised");
        boolean alreadyExist = false;
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.OleDeliverRequest.BORROWER_ID, oleDeliverRequestBo.getBorrowerId());
        requestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        List<OleDeliverRequestBo> deliverRequestBos = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, requestMap);
        if (deliverRequestBos != null && deliverRequestBos.size() > 0) {
            alreadyExist = true;
        }
        return alreadyExist;
    }


    /**
     * This is to check whether the requested item is currently in loan to the requesting patron
     *
     * @param oleDeliverRequestBo
     * @return alreadyLoaned
     */
    public boolean isAlreadyLoaned(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside isAlreadyLoaned");
        boolean alreadyLoaned = false;
        Map<String, String> loanMap = new HashMap<String, String>();
        loanMap.put(OLEConstants.OleDeliverRequest.LOAN_PATRON_ID, oleDeliverRequestBo.getBorrowerId());
        loanMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        List<OleLoanDocument> loanDocuments = (List<OleLoanDocument>) businessObjectService.findMatching(OleLoanDocument.class, loanMap);
        if (loanDocuments != null && loanDocuments.size() > 0) {
            alreadyLoaned = true;
        }
        return alreadyLoaned;
    }


    /**
     * This is to check whether the item is available in the desk or not
     *
     * @param oleDeliverRequestBo
     * @return itemAvailable
     */
    public boolean isItemAvailable(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside isItemAvailable");
        boolean itemAvailable = true;

        String itemStatuslist = null;
        String requestTypeCode = oleDeliverRequestBo.getRequestTypeCode();
        if (requestTypeCode.equals("Recall/Delivery Request")) {
            itemStatuslist = loanProcessor.getParameter(OLEConstants.RECALL_DELIVERY_ITEM_STATUS);
        } else if (requestTypeCode.equals("Recall/Hold Request")) {
            itemStatuslist = loanProcessor.getParameter(OLEConstants.RECALL_HOLD_ITEM_STATUS);
        } else if (requestTypeCode.equals("Hold/Delivery Request")) {
            itemStatuslist = loanProcessor.getParameter(OLEConstants.HOLD_DELIVERY_ITEM_STATUS);
        } else if (requestTypeCode.equals("Hold/Hold Request")) {
            itemStatuslist = loanProcessor.getParameter(OLEConstants.HOLD_HOLD_ITEM_STATUS);
        } else if (requestTypeCode.equals("Page/Delivery Request")) {
            itemStatuslist = loanProcessor.getParameter(OLEConstants.PAGE_DELIVERY_ITEM_STATUS);
        } else if (requestTypeCode.equals("Page/Hold Request")) {
            itemStatuslist = loanProcessor.getParameter(OLEConstants.PAGE_HOLD_ITEM_STATUS);
        } else if (requestTypeCode.equals("Copy Request")) {
            itemStatuslist = loanProcessor.getParameter(OLEConstants.COPY_REQUEST_ITEM_STATUS);
        } else {
            GlobalVariables.getMessageMap().putError(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, OLEConstants.OleDeliverRequest.REQUEST_ID_INVALID);
        }
        String[] str = itemStatuslist.split(";");
        for (String itemStatus : str) {
            if ((itemStatus != null) && (!itemStatus.isEmpty()) && (itemStatus.equals(oleDeliverRequestBo.getItemStatus()))) {
                itemAvailable = false;
            }
        }
        return itemAvailable;
    }


    /**
     * This is to check whether the item is available in the desk or not
     *
     * @param oleDeliverRequestBo
     * @return itemAvailable
     */
    public boolean isItemAvailableForLoan(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside isItemAvailable");
        boolean itemAvailable = false;
        Map<String, String> loanItemMap = new HashMap<String, String>();
        loanItemMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        List<OleLoanDocument> loanItemDocuments = (List<OleLoanDocument>) businessObjectService.findMatching(OleLoanDocument.class, loanItemMap);
        if (loanItemDocuments.size() == 0) {
            itemAvailable = true;
        }
        return itemAvailable;
    }


    /**
     * This is to check whether the item is eligible to loan
     *
     * @param oleDeliverRequestBo
     * @return itemEligible
     */
    public boolean isItemEligible(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside isItemEligible");
        boolean itemEligible = true;
        Map<String, String> statusMap = new HashMap<String, String>();
        statusMap.put(OLEConstants.OleDeliverRequest.LOSTBILLREPLACEMENT, OLEConstants.OleDeliverRequest.LOSTBILLREPLACEMENT);
        statusMap.put(OLEConstants.OleDeliverRequest.MISSINGFINALOVERDUE, OLEConstants.OleDeliverRequest.MISSINGFINALOVERDUE);
        statusMap.put(OLEConstants.OleDeliverRequest.WITHDRAWNCLIAMSRETURN, OLEConstants.OleDeliverRequest.WITHDRAWNCLIAMSRETURN);

        if (statusMap.containsKey(oleDeliverRequestBo.getItemStatus())) {
            itemEligible = false;
        }
        return itemEligible;
    }

    /**
     * This is to check whether the patron record is alive or expired
     *
     * @param oleDeliverRequestBo
     * @return expired
     */
    public String patronRecordExpired(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside patronRecordExpired");
        String expired = null;
        Map<String, String> patronMap = new HashMap<String, String>();
        patronMap.put(OLEConstants.OleDeliverRequest.PATRON_ID, oleDeliverRequestBo.getBorrowerId());
        List<OlePatronDocument> olePatronDocuments = (List<OlePatronDocument>) businessObjectService.findMatching(OlePatronDocument.class, patronMap);
        if (olePatronDocuments != null && olePatronDocuments.size() > 0) {
            SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OleDeliverRequest.DATE_FORMAT);
            Date expirationDate = olePatronDocuments.get(0).getExpirationDate();
            Date activationDate = olePatronDocuments.get(0).getActivationDate();
            if ((fmt.format(activationDate)).compareTo(fmt.format(new Date(System.currentTimeMillis()))) <= 0) {
                if (expirationDate != null) {
                    if ((fmt.format(expirationDate)).compareTo(fmt.format(new Date(System.currentTimeMillis()))) < 0) {
                        expired = OLEConstants.OleDeliverRequest.PATRON_RECORD_EXPIRE;
                    }
                }
            } else {
                expired = OLEConstants.OleDeliverRequest.PATRON_RECORD_FUTURE;
            }
        }
        return expired;
    }


    /**
     * This is for retrieving the request raised by  the patron
     *
     * @param olePatronId
     * @return deliverRequestBos
     */
    public List<OleDeliverRequestBo> getRequestedItems(String olePatronId) {
        LOG.debug("Inside getRequestedItems");
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.OleDeliverRequest.BORROWER_ID, olePatronId);
        List<OleDeliverRequestBo> deliverRequestBos = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, requestMap);
        for (int i = 0; i < deliverRequestBos.size(); i++) {
            processItem(deliverRequestBos.get(i));
        }
        return deliverRequestBos;

    }


    /**
     * This is to check the  duplicate in the queue Position while re -ordering
     *
     * @param itemList
     * @return exist
     */
    public String validateQueuePosition(List<OleDeliverRequestBo> itemList) {
        LOG.debug("Inside validateQueuePosition");
        String message = OLEConstants.OleDeliverRequest.REORDER_SUCCESS;
        List<Integer> queuePositionList = new ArrayList<Integer>();
        if (itemList.get(0).getBorrowerQueuePosition() < 1) {
            return OLEConstants.OleDeliverRequest.POSITIVE_QUEUE_POSITION;
        }
        queuePositionList.add(itemList.get(0).getBorrowerQueuePosition());

        for (int i = 1; i < itemList.size(); i++) {
            for (int j = 0; j < queuePositionList.size(); j++) {
                if (itemList.get(i).getBorrowerQueuePosition() != null) {
                    if ((itemList.get(i).getBorrowerQueuePosition() >= 1) && (itemList.get(i).getBorrowerQueuePosition() == queuePositionList.get(j))) {
                        return OLEConstants.OleDeliverRequest.QUEUE_DUPLICATE;
                    } else if ((itemList.get(i).getBorrowerQueuePosition() <= 0)) {
                        return OLEConstants.OleDeliverRequest.POSITIVE_QUEUE_POSITION;
                    }
                }
            }
            queuePositionList.add(itemList.get(i).getBorrowerQueuePosition());
        }
        return message;
    }

    //

    /**
     * This is to check whether any request raised for the item
     *
     * @param oleDeliverRequestBo
     * @return exist
     */
    public boolean isRequestRaised(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside isRequestRaised");
        boolean exist = false;
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        List<OleDeliverRequestBo> deliverRequestBos = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, requestMap);
        if (deliverRequestBos.size() > 0) {
            exist = true;
        }
        return exist;
    }


    /**
     * This method is used to cancel the request document
     *
     * @param oleDeliverRequestBo
     */
    public void cancelDocument(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside cancelDocument");
        Map<String, String> itemMap = new HashMap<String, String>();
        itemMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_ID, oleDeliverRequestBo.getRequestId());
        businessObjectService.deleteMatching(OleDeliverRequestBo.class, requestMap);
        List<OleDeliverRequestBo> oleDeliverRequestDocumentsList = (List<OleDeliverRequestBo>) businessObjectService.findMatchingOrderBy(OleDeliverRequestBo.class, itemMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        businessObjectService.delete(oleDeliverRequestDocumentsList);
        int queuePosition = 1;
        for (int i = 0; i < oleDeliverRequestDocumentsList.size(); i++) {
            oleDeliverRequestDocumentsList.get(i).setBorrowerQueuePosition(queuePosition);
            queuePosition = queuePosition + 1;
        }
        businessObjectService.save(oleDeliverRequestDocumentsList);
    }

    public void cancelPendingRequestForClaimsReturnedItem(String itemUuid) throws Exception {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("itemUuid", itemUuid);
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, requestMap);
        for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBoList) {
            cancelDocument(oleDeliverRequestBo);
            OleItemSearch itemSearch = docstoreUtil.getOleItemSearchList(itemUuid);
            EntityTypeContactInfoBo entityTypeContactInfoBo = oleDeliverRequestBo.getOlePatron().getEntity().getEntityTypeContactInfos().get(0);
            List<OleNoticeBo> oleNoticeBos = new ArrayList<OleNoticeBo>();
            OleNoticeBo oleNoticeBo = new OleNoticeBo();
            oleNoticeBo.setNoticeName(OLEConstants.CANCELLATION_NOTICE);
            oleNoticeBo.setAuthor(itemSearch.getAuthor() != null ? itemSearch.getAuthor() : "");
            oleNoticeBo.setItemCallNumber(itemSearch.getCallNumber() != null ? itemSearch.getCallNumber() : "");
            oleNoticeBo.setItemShelvingLocation(itemSearch.getShelvingLocation() != null ? itemSearch.getShelvingLocation() : "");
            oleNoticeBo.setItemId(itemSearch.getItemUUID() != null ? itemSearch.getItemUUID() : "");
            oleNoticeBo.setTitle(itemSearch.getTitle() != null ? itemSearch.getTitle() : "");
            oleNoticeBo.setOleItem(getItem(oleDeliverRequestBo.getItemUuid()));
            oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
            if (oleNoticeBo.getPatronEmailAddress() != null && !oleNoticeBo.getPatronEmailAddress().isEmpty()) {
                oleNoticeBos.add(oleNoticeBo);
                List list = oleDeliverBatchService.getNoticeForPatron(oleNoticeBos);
                String content = list.toString();
                content = content.replace('[', ' ');
                content = content.replace(']', ' ');
                if (!content.trim().equals("")) {
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                    if (replyToEmail != null) {
                        oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.CANCELLATION_NOTICE), new EmailBody(content), true);
                    } else {
                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.CANCELLATION_NOTICE), new EmailBody(content), true);
                    }
                }
            }
        }
    }

    /**
     * This method is to set the item values from docstore
     *
     * @param oleDeliverRequestBo
     * @return oleDeliverRequestBo
     */
    public OleDeliverRequestBo processItem(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside processItem");
        OleItemSearch oleItemSearch = null;
        if (oleDeliverRequestBo.getItemType() == null) {
            // Map<String, String> itemMap = new HashMap<String, String>();
            //itemMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
            OleItemSearch itemSearchList = docstoreUtil.getOleItemSearchList(oleDeliverRequestBo.getItemUuid());
            try {
                //org.kuali.ole.docstore.common.document.Item item=getDocstoreClientLocator().getDocstoreClient().retrieveItem( oleDeliverRequestBo.getItemUuid());


                if (itemSearchList != null) {
                    oleItemSearch = itemSearchList;
                    oleDeliverRequestBo.setTitle(itemSearchList.getTitle());
                    oleDeliverRequestBo.setAuthor(itemSearchList.getAuthor());
                    oleDeliverRequestBo.setCallNumber(itemSearchList.getCallNumber());
                    oleDeliverRequestBo.setItemType(itemSearchList.getItemType());
                    oleDeliverRequestBo.setItemLocation(itemSearchList.getShelvingLocation());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (oleItemSearch == null) {
                    oleItemSearch = new OleItemSearch();
                }
                // Map docStoreDetails = loanProcessor.getItemDetails(oleDeliverRequestBo.getItemId());
                String itemXml = loanProcessor.getItemXML(oleDeliverRequestBo.getItemUuid());
                org.kuali.ole.docstore.common.document.content.instance.Item oleItem = loanProcessor.getItemPojo(itemXml);
                oleItemSearch.setCopyNumber(oleItem.getCopyNumber());
                if (oleItem.getItemStatus() != null) {
                    oleItemSearch.setItemStatus(oleItem.getItemStatus().getCodeValue());
                }
                oleItemSearch.setShelvingLocation(getShelvingLocation(oleItem.getLocation()));
                oleDeliverRequestBo.setCopyNumber(oleItem.getCopyNumber());
                if (oleItem.getItemStatus() != null) {
                    oleDeliverRequestBo.setItemStatus(oleItem.getItemStatus().getCodeValue());
                }
                oleDeliverRequestBo.setShelvingLocation(getShelvingLocation(oleItem.getLocation()));
                if (oleDeliverRequestBo.getRequestTypeId().equals("8")) {
                    oleDeliverRequestBo.setInTransitCheckInNote(oleItem.getCheckinNote());
                }
            } catch (Exception e) {
                LOG.error(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVAL_LOC), e);  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        oleDeliverRequestBo.setOleItemSearch(oleItemSearch);
        oleDeliverRequestBo = processItemType(oleDeliverRequestBo);
        return oleDeliverRequestBo;
    }

    /**
     * This method is to set the item type name  based on the item type code
     *
     * @param oleDeliverRequestBo
     * @return
     */
    public OleDeliverRequestBo processItemType(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside process Item Type");
        Map<String, String> itemMap = new HashMap<String, String>();
        itemMap = new HashMap<String, String>();
        itemMap.put(OLEConstants.OleDeliverRequest.ITEM_TYPE_CODE, oleDeliverRequestBo.getItemType());
        List<OleInstanceItemType> oleInstanceItemTypeList = (List<OleInstanceItemType>) businessObjectService.findMatching(OleInstanceItemType.class, itemMap);
        if (oleInstanceItemTypeList != null && oleInstanceItemTypeList.size() > 0) {
            OleInstanceItemType oleInstanceItemType = oleInstanceItemTypeList.get(0);
            oleDeliverRequestBo.setItemTypeName(oleInstanceItemType.getInstanceItemTypeName());
        }

        return oleDeliverRequestBo;
    }

    /**
     * This method is to retrieve the shelving location from item
     *
     * @param oleLocation
     * @return locationLevelName
     */
    private String getShelvingLocation(org.kuali.ole.docstore.common.document.content.instance.Location oleLocation) {

        LOG.debug("Inside getShelvingLocation");
        String locationLevelName = "";
        if (oleLocation != null) {
            org.kuali.ole.docstore.common.document.content.instance.LocationLevel locationLevel =
                    oleLocation.getLocationLevel();
            if (locationLevel != null) {
                while (locationLevel.getLocationLevel() != null && !locationLevel.getLevel().equalsIgnoreCase(OLEConstants.OleDeliverRequest.SHELVING)) {
                    locationLevel = locationLevel.getLocationLevel();
                }
                locationLevelName = locationLevel.getName();
            }
        }
        return locationLevelName;
    }


    /**
     * This method is to check whether the selected  request can be raised for the item
     *
     * @param oleDeliverRequestBo
     * @return canRaiseRequest
     */
    public boolean canRaiseRequest(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside canRaiseRequest");
        boolean canRaiseRequest = true;

        if (isItemAvailable(oleDeliverRequestBo)) {
            canRaiseRequest = false;


        }
        return canRaiseRequest;
    }


    /**
     * racle
     * This method is to update the request id in the loan document
     *
     * @param oleDeliverRequestBo
     */
    public OleDeliverRequestBo updateLoanDocument(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside updateLoanDocument");
        Map<String, String> loanMap = new HashMap<String, String>();
        loanMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) businessObjectService.findMatching(OleLoanDocument.class, loanMap);
        if (oleLoanDocumentList != null && oleLoanDocumentList.size() > 0 && oleLoanDocumentList.get(0) != null && oleLoanDocumentList.get(0).getOleRequestId() == null) {
            if (oleLoanDocumentList.get(0).getLoanId() != null) {
                // oleLoanDocumentList.get(0).setOleRequestId(oleDeliverRequestBo.getRequestId());
                oleDeliverRequestBo.setLoanTransactionRecordNumber(oleLoanDocumentList.get(0).getLoanId());
            }
            //  businessObjectService.save(oleLoanDocumentList.get(0));

        }
        return oleDeliverRequestBo;
    }

    /**
     * @param requestId
     * @param itemUUID
     * @param operatorId
     * @param loanTransactionNumber
     */
    public void deleteRequest(String requestId, String itemUUID, String operatorId, String loanTransactionNumber) {
        LOG.debug("Inside deleteRequest");
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_ID, requestId);
        createRequestHistoryRecord(requestId, operatorId, loanTransactionNumber);
        Map<String, String> itemMap = new HashMap<String, String>();
        itemMap.put(OLEConstants.ITEM_UUID, itemUUID);
        List<OleDeliverRequestBo> oleDeliverRequestDocumentsList = (List<OleDeliverRequestBo>) businessObjectService.findMatchingOrderBy(OleDeliverRequestBo.class, itemMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        businessObjectService.delete(oleDeliverRequestDocumentsList);
        int queuePosition = 1;
        for (int i = 0; i < oleDeliverRequestDocumentsList.size(); i++) {
            oleDeliverRequestDocumentsList.get(i).setBorrowerQueuePosition(queuePosition);
            queuePosition = queuePosition + 1;
        }
        businessObjectService.save(oleDeliverRequestDocumentsList);

    }

    /**
     * This for retrieving the item Details from docstore
     *
     * @param itemUUID
     * @return oleItemSearch
     */
    public OleItemSearch getItemDetails(String itemUUID) {
        LOG.debug("Inside getItemDetails");
        OleItemSearch oleItemSearch = getItemDetailsForPatron(itemUUID);
        try {
            // Map  docStoreDetails= loanProcessor.getItemDetails(itemBarCode);
            String itemXml = loanProcessor.getItemXML(itemUUID);
            org.kuali.ole.docstore.common.document.content.instance.Item oleItem = loanProcessor.getItemPojo(itemXml);
            if (oleItem != null && oleItem.getItemType() != null) {
                oleItemSearch.setItemType(processItemType(oleItem.getItemType().getCodeValue()));
            }
            oleItemSearch.setCopyNumber(oleItem.getCopyNumber());
            if (oleItem.getItemStatus() != null) {
                oleItemSearch.setItemStatus(oleItem.getItemStatus().getCodeValue());
            }
            oleItemSearch.setShelvingLocation(getShelvingLocation(oleItem.getLocation()));
            oleItemSearch.setVolumeNumber(oleItem.getVolumeNumber());
        } catch (Exception e) {
            LOG.error("Not able to retrieve information from the docstore for the Item Barcode : " + itemUUID, e);  //To change body of catch statement use File | Settings | File Templates.
        }
        return oleItemSearch;
    }

    /**
     * This method is for retrieving the item details for displaying in the patron screen
     *
     * @param itemUUID
     * @return OleItemSearch
     */
    public OleItemSearch getItemDetailsForPatron(String itemUUID) {
        LOG.debug("Inside getItemDetailsForPatron");
        OleItemSearch oleItemSearch;
        try {

            Map<String, String> itemMap = new HashMap<String, String>();
            // itemMap.put(OLEConstants.OleDeliverRequest.ITEM_UUID, itemUUID);
            OleItemSearch itemSearchList = docstoreUtil.getOleItemSearchList(itemUUID);
            if (itemSearchList != null) {
                return oleItemSearch = itemSearchList;
            }
        } catch (NullPointerException e) {
            LOG.error("No item details available for the Item Barcode : " + itemUUID, e);
        }

        return new OleItemSearch();

    }

    /**
     * This method is to re-order the queuePosition based on the priority while creating the request
     *
     * @param oleDeliverRequestBo
     * @return OleDeliverRequestBo
     */
    public OleDeliverRequestBo reOrderQueuePosition(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside reOrderQueuePosition");
        List<OleDeliverRequestBo> existingRequest = new ArrayList<OleDeliverRequestBo>();
        String queue = getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.REQUEST_QUEUE);
        List<String> orderQueue = new ArrayList<String>();
        String[] queueArray = queue.split(";");
        for (int i = 0; i < queueArray.length; i++) {
            orderQueue.add(queueArray[i]);
        }
        List<OleDeliverRequestBo> finalList = new ArrayList<OleDeliverRequestBo>();
        Map<String, String> recallRequestMap = new HashMap<String, String>();
        recallRequestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        recallRequestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "1");
        Map<String, String> recallHoldRequestMap = new HashMap<String, String>();
        recallHoldRequestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        recallHoldRequestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "2");
        Map<String, String> holdRequestMap = new HashMap<String, String>();
        holdRequestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        holdRequestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "3");
        Map<String, String> holdHoldRequestMap = new HashMap<String, String>();
        holdHoldRequestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        holdHoldRequestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "4");
        Map<String, String> pageRequestMap = new HashMap<String, String>();
        pageRequestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        pageRequestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "5");
        Map<String, String> pageHoldRequestMap = new HashMap<String, String>();
        pageHoldRequestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        pageHoldRequestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "6");
        Map<String, String> copyRequestMap = new HashMap<String, String>();
        copyRequestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        copyRequestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "7");
        Map<String, String> inTransitRequestMap = new HashMap<String, String>();
        inTransitRequestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        inTransitRequestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "8");
        List<OleDeliverRequestBo> recallList = (List<OleDeliverRequestBo>) businessObjectService.findMatchingOrderBy(OleDeliverRequestBo.class, recallRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> recallHoldList = (List<OleDeliverRequestBo>) businessObjectService.findMatchingOrderBy(OleDeliverRequestBo.class, recallHoldRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> holdList = (List<OleDeliverRequestBo>) businessObjectService.findMatchingOrderBy(OleDeliverRequestBo.class, holdRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> holdHoldList = (List<OleDeliverRequestBo>) businessObjectService.findMatchingOrderBy(OleDeliverRequestBo.class, holdHoldRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> pageList = (List<OleDeliverRequestBo>) businessObjectService.findMatchingOrderBy(OleDeliverRequestBo.class, pageRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> pageHoldList = (List<OleDeliverRequestBo>) businessObjectService.findMatchingOrderBy(OleDeliverRequestBo.class, pageHoldRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> copyList = (List<OleDeliverRequestBo>) businessObjectService.findMatchingOrderBy(OleDeliverRequestBo.class, copyRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> inTransitList = (List<OleDeliverRequestBo>) businessObjectService.findMatchingOrderBy(OleDeliverRequestBo.class, inTransitRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        if (oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains(OLEConstants.OleDeliverRequest.RECALL_DELIVERY)) {
            recallList.add(oleDeliverRequestBo);
        } else if (oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains(OLEConstants.OleDeliverRequest.HOLD_DELIVERY)) {
            holdList.add(oleDeliverRequestBo);
        } else if (oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains(OLEConstants.OleDeliverRequest.PAGE_DELIVERY)) {
            pageList.add(oleDeliverRequestBo);
        } else if (oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains(OLEConstants.OleDeliverRequest.COPY)) {
            copyList.add(oleDeliverRequestBo);
        } else if (oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains(OLEConstants.OleDeliverRequest.INTRANSIT)) {
            inTransitList.add(oleDeliverRequestBo);
        } else if (oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains(OLEConstants.OleDeliverRequest.RECALL_HOLD)) {
            recallHoldList.add(oleDeliverRequestBo);
        } else if (oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains(OLEConstants.OleDeliverRequest.HOLD_HOLD)) {
            holdHoldList.add(oleDeliverRequestBo);
        } else if (oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains(OLEConstants.OleDeliverRequest.PAGE_HOLD)) {
            pageHoldList.add(oleDeliverRequestBo);
        }
        OleDeliverRequestBo oleDeliverRequestBo1;
        for (int i = 0; i < orderQueue.size(); i++) {
            if (orderQueue.get(i).equals(OLEConstants.OleDeliverRequest.RECALL) && recallList.size() > 0) {
                for (int x = 0; x < recallList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(recallList.get(x));
                    oleDeliverRequestBo1.setBorrowerQueuePosition(this.queuePosition + 1);
                    this.queuePosition = this.queuePosition + 1;
                    finalList.add(oleDeliverRequestBo1);

                }
            }
            if (orderQueue.get(i).equals(OLEConstants.OleDeliverRequest.HOLD) && holdList.size() > 0) {
                for (int x = 0; x < holdList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(holdList.get(x));
                    oleDeliverRequestBo1.setBorrowerQueuePosition(this.queuePosition + 1);
                    this.queuePosition = this.queuePosition + 1;
                    finalList.add(oleDeliverRequestBo1);

                }
            }
            if (orderQueue.get(i).equals(OLEConstants.OleDeliverRequest.PAGE) && pageList.size() > 0) {
                for (int x = 0; x < pageList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(pageList.get(x));
                    oleDeliverRequestBo1.setBorrowerQueuePosition(this.queuePosition + 1);
                    this.queuePosition = this.queuePosition + 1;
                    finalList.add(oleDeliverRequestBo1);
                }
            }
            if (orderQueue.get(i).equals(OLEConstants.OleDeliverRequest.RECALL) && recallHoldList.size() > 0) {
                for (int x = 0; x < recallHoldList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(recallHoldList.get(x));
                    oleDeliverRequestBo1.setBorrowerQueuePosition(this.queuePosition + 1);
                    this.queuePosition = this.queuePosition + 1;
                    finalList.add(oleDeliverRequestBo1);

                }
            }
            if (orderQueue.get(i).equals(OLEConstants.OleDeliverRequest.HOLD) && holdHoldList.size() > 0) {
                for (int x = 0; x < holdHoldList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(holdHoldList.get(x));
                    oleDeliverRequestBo1.setBorrowerQueuePosition(this.queuePosition + 1);
                    this.queuePosition = this.queuePosition + 1;
                    finalList.add(oleDeliverRequestBo1);

                }
            }
            if (orderQueue.get(i).equals(OLEConstants.OleDeliverRequest.PAGE) && pageHoldList.size() > 0) {
                for (int x = 0; x < pageHoldList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(pageHoldList.get(x));
                    oleDeliverRequestBo1.setBorrowerQueuePosition(this.queuePosition + 1);
                    this.queuePosition = this.queuePosition + 1;
                    finalList.add(oleDeliverRequestBo1);
                }
            }
            if (orderQueue.get(i).equals(OLEConstants.OleDeliverRequest.COPY) && copyList.size() > 0) {
                for (int x = 0; x < copyList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(copyList.get(x));
                    oleDeliverRequestBo1.setBorrowerQueuePosition(this.queuePosition + 1);
                    this.queuePosition = this.queuePosition + 1;
                    finalList.add(oleDeliverRequestBo1);

                }
            }
            if (orderQueue.get(i).equals(OLEConstants.OleDeliverRequest.INTRANSIT) && inTransitList.size() > 0) {
                for (int x = 0; x < inTransitList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(inTransitList.get(x));
                    oleDeliverRequestBo1.setBorrowerQueuePosition(this.queuePosition + 1);
                    this.queuePosition = this.queuePosition + 1;
                    finalList.add(oleDeliverRequestBo1);

                }
            }
        }
        for (int i = 0; i < finalList.size(); i++) {
            if (finalList.get(i).getRequestId() == null) {
                oleDeliverRequestBo.setBorrowerQueuePosition(finalList.get(i).getBorrowerQueuePosition());
                finalList.remove(finalList.get(i));
            }
        }
        businessObjectService.save(finalList);
        this.queuePosition = 0;
        return oleDeliverRequestBo;
    }

    /**
     * Thsi method is to check whether the item is available in doc store or not
     *
     * @param oleDeliverRequestBo
     * @return boolean
     */
    public boolean isItemAvailbleInDocstore(OleDeliverRequestBo oleDeliverRequestBo) {
        LOG.debug("Inside isItemAvailbleInDocstore");
        boolean available = false;
        Map<String, String> itemMap = new HashMap<String, String>();
        try {
            String itemUUID = "";
            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
            org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
            SearchResponse searchResponse = null;
            search_Params.getSearchConditions().add(search_Params.buildSearchCondition("", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, oleDeliverRequestBo.getItemId()), ""));


            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "id"));
            ;

                   /* Map<String,String> map=new HashMap<>();
                    map.put(item.BARCODE, itemBarcode);
                    item=getDocstoreClientLocator().getDocstoreClient().findItem(map);*/

            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    String fieldName = searchResultField.getFieldName();
                    String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";

                    if (fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                        itemUUID = fieldValue;
                    }

                }
            }


            oleDeliverRequestBo.setItemUuid(itemUUID);
        } catch (Exception e) {
            LOG.error(e, e);
            //To change body of catch statement use File | Settings | File Templates.
        }
        // itemMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        OleItemSearch itemSearchList = docstoreUtil.getOleItemSearchList(oleDeliverRequestBo.getItemUuid());
        if (itemSearchList != null) {
            oleDeliverRequestBo.setTitle(itemSearchList.getTitle());
            oleDeliverRequestBo.setAuthor(itemSearchList.getAuthor());
            oleDeliverRequestBo.setCallNumber(itemSearchList.getCallNumber());
            oleDeliverRequestBo.setItemType(itemSearchList.getItemType());
        }
        try {
            // Map docStoreDetails = loanProcessor.getItemDetailsByUUID(oleDeliverRequestBo.getItemId());
            String itemXml = loanProcessor.getItemXML(oleDeliverRequestBo.getItemUuid());
            org.kuali.ole.docstore.common.document.content.instance.Item oleItem = loanProcessor.getItemPojo(itemXml);

            oleDeliverRequestBo.setCopyNumber(oleItem.getCopyNumber());
            if (oleItem.getItemStatus() != null) {
                oleDeliverRequestBo.setItemStatus(oleItem.getItemStatus().getCodeValue());
            }
            oleDeliverRequestBo.setShelvingLocation(getShelvingLocation(oleItem.getLocation()));
            available = true;
        } catch (Exception e) {
            LOG.error(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVAL_LOC), e);  //To change body of catch statement use File | Settings | File Templates.
        }
        return available;
    }

    public OleDeliverRequestBo populateItemDetailsToRequest(OleDeliverRequestBo oleDeliverRequestBo, OleItemSearch oleItemSearch) {
        LOG.debug("Inside populateItemDetailsToRequest");
        if (oleDeliverRequestBo != null && oleItemSearch != null) {
            oleDeliverRequestBo.setAuthor(oleItemSearch.getAuthor());
            oleDeliverRequestBo.setTitle(oleItemSearch.getTitle());
            oleDeliverRequestBo.setCallNumber(oleItemSearch.getCallNumber());
            oleDeliverRequestBo.setCopyNumber(oleItemSearch.getCopyNumber());
            oleDeliverRequestBo.setItemType(oleItemSearch.getItemType());
            oleDeliverRequestBo.setItemStatus(oleItemSearch.getItemStatus());
            oleItemSearch.setShelvingLocation(oleDeliverRequestBo.getShelvingLocation());
        }
        return oleDeliverRequestBo;
    }


    public OleDeliverRequestBo generateRecallNotice(OleDeliverRequestBo oleDeliverRequestBo) throws Exception {
        Map<String, String> loanMap = new HashMap<String, String>();
        loanMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        List<OleLoanDocument> loanDocuments = (List<OleLoanDocument>) businessObjectService.findMatching(OleLoanDocument.class, loanMap);
        OleLoanDocument oleLoanDocument = loanDocuments.get(0);
        Map<String, String> patronMap = new HashMap<String, String>();
        patronMap.put(OLEConstants.OleDeliverRequest.PATRON_ID, oleLoanDocument.getPatronId());
        List<OlePatronDocument> patronDocumentList = (List<OlePatronDocument>) businessObjectService.findMatching(OlePatronDocument.class, patronMap);
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        org.kuali.ole.docstore.common.document.content.instance.Item item;
        if (patronDocumentList.size() > 0) {
            OlePatronDocument olePatronDocument = patronDocumentList.get(0);
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);
            oleNoticeBo.setPatronName(olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " + oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getLastName());
            oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
            oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
            oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
        }
        oleNoticeBo.setAuthor(oleDeliverRequestBo.getAuthor() != null ? oleDeliverRequestBo.getAuthor() : "");
        oleNoticeBo.setCirculationDeskAddress("");
        oleNoticeBo.setCirculationDeskName("");
        oleNoticeBo.setCirculationDeskEmailAddress("");
        oleNoticeBo.setCirculationDeskPhoneNumber("");
        oleNoticeBo.setItemCallNumber(oleDeliverRequestBo.getCallNumber() != null ? oleDeliverRequestBo.getCallNumber() : "");
        oleNoticeBo.setItemShelvingLocation(oleDeliverRequestBo.getShelvingLocation() != null ? oleDeliverRequestBo.getShelvingLocation() : "");
        oleNoticeBo.setItemId(oleDeliverRequestBo.getItemId() != null ? oleDeliverRequestBo.getItemId() : "");
        oleNoticeBo.setTitle(oleDeliverRequestBo.getTitle() != null ? oleDeliverRequestBo.getTitle() : "");
        oleNoticeBo.setOleItem(getItem(oleDeliverRequestBo.getItemUuid()));
        oleNoticeBo.setOlePatron(oleDeliverRequestBo.getOlePatron());
        oleNoticeBo.setVolumeNumber(oleNoticeBo.getOleItem().getVolumeNumber() != null ? oleNoticeBo.getOleItem().getVolumeNumber() : "");
        oleNoticeBo.setOriginalDueDate(oleLoanDocument.getLoanDueDate());
        oleNoticeBo.setNewDueDate(oleDeliverRequestBo.getNewDueDate());
        oleNoticeBo.setOriginalDueDate(oleDeliverRequestBo.getOriginalDueDate());
        /*if (oleDeliverRequestBo.getRecallDueDate() != null) {
            oleNoticeBo.setNewDueDate(oleDeliverRequestBo.getRecallDueDate());
        } else {
            oleNoticeBo.setNewDueDate(oleLoanDocument.getLoanDueDate());
        }*/
        oleNoticeBo.setNoticeName(OLEConstants.NOTICE_RECALL);
        String noticeContent = getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.RECALL_BODY);
        oleNoticeBo.setNoticeSpecificContent(noticeContent);
        //  oleNoticeBo = setPatronDetailsForNotice(oleNoticeBo,oleDeliverRequestBo.getOlePatron());
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        List<OleNoticeBo> oleNoticeBos = new ArrayList<OleNoticeBo>();
        String noticeType = getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.RECALL_NOTICE_TYPE);
        oleNoticeBos.add(oleNoticeBo);
        if (noticeType.equalsIgnoreCase(OLEConstants.EMAIL)) {
            if (oleNoticeBo.getPatronEmailAddress() != null && !oleNoticeBo.getPatronEmailAddress().isEmpty()) {
                List list = oleDeliverBatchService.getNoticeForPatron(oleNoticeBos);
                String content = list.toString();
                content = content.replace('[', ' ');
                content = content.replace(']', ' ');
                if (!content.trim().equals("")) {
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                    if (replyToEmail != null) {
                        oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(noticeService.getNoticeSubjectForNoticeType(oleNoticeBo.getNoticeName())), new EmailBody(content), true);
                    } else {
                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(noticeService.getNoticeSubjectForNoticeType(oleNoticeBo.getNoticeName())), new EmailBody(content), true);
                    }
                } else {
                    for (OleNoticeBo oleNoticeBo1 : oleNoticeBos) {
                        LOG.info("Notice Type :" + oleNoticeBo1.getNoticeName() + "  " + "Item Barcode : " + oleNoticeBo1.getItemId() + " " + "Patron Name :" + oleNoticeBo1.getPatronName());
                    }
                }
                LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
/*
                Mailer  mailer = CoreApiServiceLocator.getMailer();
                mailer.sendEmail(new EmailFrom(OLEConstants.KUALI_MAIL), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(list.toString()), true);
          */
            }
        } else if (noticeType.equalsIgnoreCase(OLEConstants.SMS)) {
            //oleNoticeBos.add(oleNoticeBo);
            Map map = oleDeliverBatchService.getSMSForPatron(oleNoticeBos);
            HashMap sms = (HashMap) map.get(OLEConstants.OleDeliverRequest.RECALL);
            Iterator it = sms.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                String patronPhoneNumber = oleNoticeBo.getPatronPhoneNumber();
                OleSms oleSms = new OleSms();
                oleSms.sendSms("", patronPhoneNumber, (String) pairs.getValue());
            }

        }
        if (noticeType.equalsIgnoreCase(OLEConstants.MAIL) || noticeType.equalsIgnoreCase(OLEConstants.EMAIL)) {
            //oleNoticeBos.add(oleNoticeBo);
            oleDeliverBatchService.getPdfNoticeForPatron(oleNoticeBos);
            LOG.info("Recall Notice Pdf generated for item Id" + oleNoticeBo.getItemId());
        }
        oleDeliverRequestBo = (OleDeliverRequestBo) ObjectUtils.deepCopy(oleDeliverRequestBo);
        oleDeliverRequestBo.setRecallNoticeSentDate(new java.sql.Date(System.currentTimeMillis()));
        return oleDeliverRequestBo;
    }


    public void generateOnHoldNotice() throws Exception {

        List<OleDeliverRequestBo> finalDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        Map<String, String> onHoldNoticeMap = new HashMap<String, String>();
        onHoldNoticeMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "2");
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, onHoldNoticeMap);
        Map<String, String> onHoldHoldNoticeMap = new HashMap<String, String>();
        onHoldHoldNoticeMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "4");
        List<OleDeliverRequestBo> oleDeliverRequestBoList1 = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, onHoldHoldNoticeMap);
        oleDeliverRequestBoList.addAll(oleDeliverRequestBoList1);
        OleDeliverRequestBo oleDeliverRequestBo;
        List<OleNoticeBo> noticesList = new ArrayList<OleNoticeBo>();
        EntityTypeContactInfoBo entityTypeContactInfoBo;
        OleNoticeBo oleNoticeBo;
        org.kuali.ole.docstore.common.document.content.instance.Item item;
        for (int i = 0; i < oleDeliverRequestBoList.size(); i++) {
            if (docstoreUtil.isItemAvailableInDocStore(oleDeliverRequestBoList.get(i))) {
                item = oleDeliverRequestBoList.get(i).getOleItem();
                if (item != null && item.getItemStatus() != null && item.getItemStatus().getCodeValue() != null && item.getItemStatus().getCodeValue().equals(OLEConstants.ITEM_STATUS_ON_HOLD) && oleDeliverRequestBoList.get(i).getOnHoldNoticeSentDate() == null) {
                    entityTypeContactInfoBo = oleDeliverRequestBoList.get(i).getOlePatron().getEntity().getEntityTypeContactInfos().get(0);
                    oleNoticeBo = new OleNoticeBo();
                    oleNoticeBo.setAuthor(oleDeliverRequestBoList.get(i).getAuthor());
                    oleNoticeBo.setCirculationDeskAddress("");
                    oleNoticeBo.setCirculationDeskName("");
                    oleNoticeBo.setCirculationDeskEmailAddress("");
                    oleNoticeBo.setCirculationDeskPhoneNumber("");
                    oleNoticeBo.setPatronName(oleDeliverRequestBoList.get(i).getOlePatron().getEntity().getNames().get(0).getFirstName() + " " + oleDeliverRequestBoList.get(i).getOlePatron().getEntity().getNames().get(0).getLastName());
                    oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setItemCallNumber(oleDeliverRequestBoList.get(i).getCallNumber() != null ? oleDeliverRequestBoList.get(i).getCallNumber() : "");
                    oleNoticeBo.setItemShelvingLocation(oleDeliverRequestBoList.get(i).getShelvingLocation() != null ? oleDeliverRequestBoList.get(i).getShelvingLocation() : "");
                    oleNoticeBo.setItemId(oleDeliverRequestBoList.get(i).getItemId() != null ? oleDeliverRequestBoList.get(i).getItemId() : "");
                    oleNoticeBo.setTitle(oleDeliverRequestBoList.get(i).getTitle() != null ? oleDeliverRequestBoList.get(i).getTitle() : "");
                    oleNoticeBo.setOleItem(item);
                    oleNoticeBo.setOlePatron(oleDeliverRequestBoList.get(i).getOlePatron());
                    oleNoticeBo.setVolumeNumber(item.getVolumeNumber() != null ? item.getVolumeNumber() : "");
                    oleNoticeBo.setNewDueDate(new Date());
                    oleNoticeBo.setOriginalDueDate(new Date());
                    oleNoticeBo.setNoticeName(OLEConstants.NOTICE_ONHOLD);
                    oleNoticeBo.setCirculationDeskName(oleDeliverRequestBoList.get(i).getOlePickUpLocation().getCirculationDeskPublicName());
                    String circulationDeskId = oleDeliverRequestBoList.get(i).getPickUpLocationId();
                    int noDays = 0;
                    Map<String, String> mapCirculationDesk = new HashMap<String, String>();
                    mapCirculationDesk.put("circulationDeskId", circulationDeskId);
                    List<OleCirculationDesk> oleCirculationDesks = (List<OleCirculationDesk>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDesk.class, mapCirculationDesk);
                    if (oleCirculationDesks.size() > 0) {
                        OleCirculationDesk oleCirculationDesk = oleCirculationDesks.get(0);
                        noDays = Integer.parseInt(oleCirculationDesk.getOnHoldDays());
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, noDays);
                    Date date = calendar.getTime();
                    oleNoticeBo.setExpiredOnHoldDate(date);
                    String maxNumOfDays = oleDeliverRequestBoList.get(i).getOlePickUpLocation().getOnHoldDays() != null ? oleDeliverRequestBoList.get(i).getOlePickUpLocation().getOnHoldDays() : loanProcessor.getParameter(OLEConstants.MAX_NO_OF_DAYS_ON_HOLD);
                    Integer maxNumberOfDaysOnHold = new Integer(maxNumOfDays);
                    oleNoticeBo.setOnHoldDueDate(dateAdd(new java.sql.Date(oleDeliverRequestBoList.get(i).getCreateDate().getTime()), maxNumberOfDaysOnHold));
                    String noticeContent = getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.ONHOLD_BODY);
                    oleNoticeBo.setNoticeSpecificContent(noticeContent);
                    noticesList.add(oleNoticeBo);
                    OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
                    String agendaName = OLEConstants.BATCH_PROGRAM_AGENDA;
                    HashMap<String, Object> termValues = new HashMap<String, Object>();
                    OleCirculationDesk oleCirculationDesk = oleDeliverRequestBoList.get(i).getOlePickUpLocation();
                    OlePatronDocument olePatronDocument = oleDeliverRequestBoList.get(i).getOlePatron();
                    String deskLocation = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskCode() : "";
                    String deskLocationName = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : "";
                    termValues.put(OLEConstants.BORROWER_TYPE, olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                    termValues.put(OLEConstants.DESK_LOCATION, deskLocation);
                    termValues.put(OLEConstants.NOTICE, OLEConstants.NOTICE_ONHOLD);
                    LOG.info("termValues.toString()" + termValues.toString());
                    EngineResults engineResults = loanProcessor.getEngineResults(agendaName, termValues);
                    String noticeType = (String) engineResults.getAttribute(OLEConstants.NOTICE_TYPE);
                    LOG.info("**************" + noticeType);
                    noticeType = noticeType != null ? noticeType : getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.ONHOLD_NOTICE_TYPE);
                    if (noticeType != null && (noticeType.equalsIgnoreCase(OLEConstants.EMAIL) || noticeType.equalsIgnoreCase(OLEConstants.MAIL))) {
                        LOG.info("noticesList.size()" + noticesList.size());
                        oleDeliverBatchService.getPdfNoticeForPatron(noticesList);
                        LOG.info("OnHold Notice Pdf generated for item Id" + oleNoticeBo.getItemId());
                    }
                    if (noticeType != null && noticeType.equalsIgnoreCase(OLEConstants.EMAIL)) {
                        if (oleNoticeBo.getPatronEmailAddress() != null && !oleNoticeBo.getPatronEmailAddress().isEmpty()) {
                            List list = oleDeliverBatchService.getNoticeForPatron(noticesList);
                            String content = list.toString();
                            content = content.replace('[', ' ');
                            content = content.replace(']', ' ');
                            if (!content.trim().equals("")) {
                                OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                                if (oleDeliverRequestBoList.get(i).getOlePickUpLocation() != null && StringUtils.isNotBlank(oleDeliverRequestBoList.get(i).getOlePickUpLocation().getReplyToEmail())) {
                                    oleMailer.sendEmail(new EmailFrom(oleDeliverRequestBoList.get(i).getOlePickUpLocation().getReplyToEmail()), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(noticeService.getNoticeSubjectForNoticeType(oleNoticeBo.getNoticeName())), new EmailBody(content), true);
                                } else {
                                    String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                                    if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                                        fromAddress = OLEConstants.KUALI_MAIL;
                                    }
                                    oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(noticeService.getNoticeSubjectForNoticeType(oleNoticeBo.getNoticeName())), new EmailBody(content), true);
                                }
                            } else {
                                for (OleNoticeBo oleNoticeBo1 : noticesList) {
                                    LOG.info("Notice Type :" + oleNoticeBo1.getNoticeName() + "  " + "Item Barcode : " + oleNoticeBo1.getItemId() + " " + "Patron Name :" + oleNoticeBo1.getPatronName());
                                }
                            }
                            LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                       /* Mailer mailer = CoreApiServiceLocator.getMailer();
                        mailer.sendEmail(new EmailFrom(OLEConstants.KUALI_MAIL), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(list.toString()), true);
                   */
                        }
                    } else if (noticeType != null && noticeType.equalsIgnoreCase(OLEConstants.SMS)) {
                        Map map = oleDeliverBatchService.getSMSForPatron(noticesList);
                        HashMap sms = (HashMap) map.get(OLEConstants.NOTICE_ONHOLD);
                        Iterator it = sms.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pairs = (Map.Entry) it.next();
                            String patronPhoneNumber = oleNoticeBo.getPatronPhoneNumber();
                            OleSms oleSms = new OleSms();
                            oleSms.sendSms("", patronPhoneNumber, (String) pairs.getValue());
                        }
                    }
                    // To do send Notice
                    oleDeliverRequestBoList.get(i).setOleItem(null);
                    oleDeliverRequestBo = (OleDeliverRequestBo) ObjectUtils.deepCopy(oleDeliverRequestBoList.get(i));
                    oleDeliverRequestBo.setOnHoldNoticeSentDate(new java.sql.Date(System.currentTimeMillis()));
                    finalDeliverRequestBoList.add(oleDeliverRequestBo);
                }
            }
            businessObjectService.save(finalDeliverRequestBoList);
        }
    }

    public void generateRequestExpirationNotice() throws Exception {
        List<OleDeliverRequestBo> oleDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findAll(OleDeliverRequestBo.class);
        SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OleDeliverRequest.DATE_FORMAT);
        EntityTypeContactInfoBo entityTypeContactInfoBo;
        List<OleNoticeBo> noticesList = new ArrayList<OleNoticeBo>();
        org.kuali.ole.docstore.common.document.content.instance.Item item;
        String noticeType = null;
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        if (oleDeliverRequestBoList.size() > 0) {
            for (int i = 0; i < oleDeliverRequestBoList.size(); i++) {
                if (docstoreUtil.isItemAvailableInDocStore(oleDeliverRequestBoList.get(i))) {
                    //  processItem(oleDeliverRequestBoList.get(i));
                    item = oleDeliverRequestBoList.get(i).getOleItem();
                    LOG.info("Expiration Date :" + oleDeliverRequestBoList.get(i).getRequestExpiryDate());
                    if (oleDeliverRequestBoList.get(i).getOlePatron().isCourtesyNotice() && (fmt.format(oleDeliverRequestBoList.get(i).getRequestExpiryDate())).compareTo(fmt.format(new Date(System.currentTimeMillis()))) <= 0) {
                        LOG.info("Inside Expiration Date :" + oleDeliverRequestBoList.get(i).getRequestExpiryDate());
                        entityTypeContactInfoBo = oleDeliverRequestBoList.get(i).getOlePatron().getEntity().getEntityTypeContactInfos().get(0);
                        oleNoticeBo = new OleNoticeBo();
                        oleNoticeBo.setAuthor(oleDeliverRequestBoList.get(i).getAuthor());
                        oleNoticeBo.setCirculationDeskAddress("");
                        oleNoticeBo.setCirculationDeskName("");
                        oleNoticeBo.setCirculationDeskEmailAddress("");
                        oleNoticeBo.setCirculationDeskPhoneNumber("");
                        oleNoticeBo.setPatronName(oleDeliverRequestBoList.get(i).getOlePatron().getEntity().getNames().get(0).getFirstName() + " " + oleDeliverRequestBoList.get(i).getOlePatron().getEntity().getNames().get(0).getLastName());
                        oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
                        oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
                        oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
                        oleNoticeBo.setItemCallNumber(oleDeliverRequestBoList.get(i).getCallNumber() != null ? oleDeliverRequestBoList.get(i).getCallNumber() : "");
                        oleNoticeBo.setItemShelvingLocation(oleDeliverRequestBoList.get(i).getShelvingLocation() != null ? oleDeliverRequestBoList.get(i).getShelvingLocation() : "");
                        oleNoticeBo.setItemId(oleDeliverRequestBoList.get(i).getItemId() != null ? oleDeliverRequestBoList.get(i).getItemId() : "");
                        oleNoticeBo.setTitle(oleDeliverRequestBoList.get(i).getTitle() != null ? oleDeliverRequestBoList.get(i).getTitle() : "");
                        oleNoticeBo.setOleItem(item);
                        oleNoticeBo.setOlePatron(oleDeliverRequestBoList.get(i).getOlePatron());
                        oleNoticeBo.setVolumeNumber(item.getVolumeNumber() != null ? item.getVolumeNumber() : "");
                        oleNoticeBo.setNewDueDate(new Date());
                        oleNoticeBo.setOriginalDueDate(new Date());
                        oleNoticeBo.setNoticeName(OLEConstants.OleDeliverRequest.EXPIRED_REQUEST);
                        String noticeContent = getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.EXP_HOLD_NOTICE_CONTENT);
                        oleNoticeBo.setNoticeSpecificContent(noticeContent);
                        noticesList.add(oleNoticeBo);
                        String agendaName = OLEConstants.BATCH_PROGRAM_AGENDA;
                        HashMap<String, Object> termValues = new HashMap<String, Object>();
                        OleCirculationDesk oleCirculationDesk = oleDeliverRequestBoList.get(i).getOlePickUpLocation();
                        OlePatronDocument olePatronDocument = oleDeliverRequestBoList.get(i).getOlePatron();
                        String deskLocation = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskCode() : "";
                        String deskLocationName = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : "";
                        termValues.put(OLEConstants.BORROWER_TYPE, olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                        termValues.put(OLEConstants.DESK_LOCATION, deskLocation);
                        termValues.put(OLEConstants.NOTICE, OLEConstants.OleDeliverRequest.EXPIRED_REQUEST);
                        LOG.info("termValues.toString()" + termValues.toString());
                        EngineResults engineResults = loanProcessor.getEngineResults(agendaName, termValues);
                        noticeType = (String) engineResults.getAttribute(OLEConstants.NOTICE_TYPE);
                        LOG.info("**************" + noticeType);
                        noticeType = noticeType != null ? noticeType : getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.RQST_EXPR_NOTICE_TYPE);
                        if (noticeType != null && noticeType.equalsIgnoreCase(OLEConstants.SMS)) {
                            // noticesList.add(oleNoticeBo);
                            Map map = oleDeliverBatchService.getSMSForPatron(noticesList);
                            HashMap sms = (HashMap) map.get(OLEConstants.OleDeliverRequest.EXPIRED_REQUEST);
                            Iterator it = sms.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pairs = (Map.Entry) it.next();

                                String patronPhoneNumber = oleNoticeBo.getPatronPhoneNumber();
                                OleSms oleSms = new OleSms();
                                oleSms.sendSms("", patronPhoneNumber, (String) pairs.getValue());
                            }
                        }
                    }
                }
                if (noticeType != null && (noticeType.equalsIgnoreCase(OLEConstants.EMAIL) || noticeType.equalsIgnoreCase(OLEConstants.MAIL))) {
                    oleDeliverBatchService.getPdfNoticeForPatron(noticesList);
                    LOG.info("Request Expiration Notice Pdf generated for item Id");
                }
                if (noticeType != null && noticeType.equalsIgnoreCase(OLEConstants.EMAIL)) {
                    //  noticesList.add(oleNoticeBo);
                    List list = (List) oleDeliverBatchService.getNoticeForPatron(noticesList);
                    String noticeContent = list.toString();
                    noticeContent = noticeContent.replace('[', ' ');
                    noticeContent = noticeContent.replace(']', ' ');
                    if (!noticeContent.trim().equals("")) {
                        OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                        String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                        if (replyToEmail != null) {
                            oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.REQUEST_EXPIRATION_NOTICE_SUBJECT), new EmailBody(noticeContent), true);
                        } else {
                            String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                            if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                                fromAddress = OLEConstants.KUALI_MAIL;
                            }
                            oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.REQUEST_EXPIRATION_NOTICE_SUBJECT), new EmailBody(noticeContent), true);
                        }
                    } else {
                        for (OleNoticeBo oleNoticeBo1 : noticesList) {
                            LOG.info("Notice Type :" + oleNoticeBo1.getNoticeName() + "  " + "Item Barcode : " + oleNoticeBo1.getItemId() + " " + "Patron Name :" + oleNoticeBo1.getPatronName());
                        }
                    }
                    LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());

                }
            }
        }
    }

    private java.sql.Date dateAdd(java.sql.Date in, int daysToAdd) {
        if (in == null) {
            return null;
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(in);
        cal.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return new java.sql.Date(cal.getTime().getTime());
    }

    public void deletingExpiredRequests() {
        LOG.info("Inside deletingExpiredRequests");
        List<OleDeliverRequestBo> oleDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> newOleDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        try {
            oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findAll(OleDeliverRequestBo.class);
            businessObjectService.delete(oleDeliverRequestBoList);
            SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OleDeliverRequest.DATE_FORMAT);
            for (int i = 0; i < oleDeliverRequestBoList.size(); i++) {
                if ((fmt.format(oleDeliverRequestBoList.get(i).getRequestExpiryDate())).compareTo(fmt.format(new Date(System.currentTimeMillis()))) > 0) {
                    newOleDeliverRequestBoList.add(oleDeliverRequestBoList.get(i));
                }
            }
            businessObjectService.save(newOleDeliverRequestBoList);
        } catch (Exception e) {
            businessObjectService.save(oleDeliverRequestBoList);
            LOG.error(e, e);
        }
    }

    private org.kuali.ole.docstore.common.document.content.instance.Item getItem(String itemUUID) {
        LOG.debug("Inside getItem");
        try {
            // Map  docStoreDetails= loanProcessor.getItemDetails(itemBarCode);
            String itemXml = loanProcessor.getItemXML(itemUUID);
            org.kuali.ole.docstore.common.document.content.instance.Item oleItem = loanProcessor.getItemPojo(itemXml);
            return oleItem;
        } catch (Exception e) {
            LOG.error("Item not available in doc store", e);
        }
        return null;
    }


    private OleNoticeBo setPatronDetailsForNotice(OleNoticeBo oleNoticeBo, OlePatronDocument olePatronDocument) {
        LOG.debug("Inside setPatronDetailsForNotice");
        if (oleNoticeBo.getAuthor() != null && olePatronDocument.getOlePatronId() != null) {
            oleNoticeBo.setPatronName(olePatronDocument.getName().getFirstName());
            if (olePatronDocument.getAddresses().size() > 0) {
                oleNoticeBo.setPatronAddress(olePatronDocument.getAddresses().get(0).getLine1() + "/n" + olePatronDocument.getAddresses().get(0).getLine2() + "/n" + olePatronDocument.getAddresses().get(0).getCity());
            }
            oleNoticeBo.setPatronEmailAddress(olePatronDocument.getEmailAddress());
            oleNoticeBo.setPatronPhoneNumber(olePatronDocument.getPhoneNumber());
        }
        return oleNoticeBo;
    }


    private void createRequestHistoryRecord(String requestId, String OperatorId, String loanTransactionNumber) {
        LOG.debug("Inside createRequestHistoryRecord");
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_ID, requestId);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, requestMap);
        if (oleDeliverRequestBoList.size() > 0) {
            OleDeliverRequestBo oleDeliverRequestBo = oleDeliverRequestBoList.get(0);
            OleDeliverRequestHistoryRecord oleDeliverRequestHistoryRecord = new OleDeliverRequestHistoryRecord();
            oleDeliverRequestHistoryRecord.setRequestId(oleDeliverRequestBo.getRequestId());
            oleDeliverRequestHistoryRecord.setItemBarcode(oleDeliverRequestBo.getItemId());
            oleDeliverRequestHistoryRecord.setItemId(DocumentUniqueIDPrefix.getDocumentId(oleDeliverRequestBo.getItemUuid()));
            oleDeliverRequestHistoryRecord.setPatronId(oleDeliverRequestBo.getOlePatron()!=null?oleDeliverRequestBo.getOlePatron().getOlePatronId():null);
            oleDeliverRequestHistoryRecord.setArchiveDate(new java.sql.Date(System.currentTimeMillis()));
            oleDeliverRequestHistoryRecord.setPickUpLocationCode(oleDeliverRequestBo.getPickUpLocationCode());
            oleDeliverRequestHistoryRecord.setOperatorId(OperatorId);
            oleDeliverRequestHistoryRecord.setDeliverRequestTypeCode(oleDeliverRequestBo.getRequestTypeCode());
            oleDeliverRequestHistoryRecord.setPoLineItemNumber("");
            oleDeliverRequestHistoryRecord.setLoanTransactionId(loanTransactionNumber);
            //oleDeliverRequestHistoryRecord.setMachineId("");      //commented for jira OLE-5675
            businessObjectService.save(oleDeliverRequestHistoryRecord);
        }
        businessObjectService.delete(oleDeliverRequestBoList);
    }

    public OleDeliverRequestBo getOleDeliverRequestBo(String itemUUID) {

        LOG.debug("Inside getOleDeliverRequestBo method");
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.ITEM_UUID, itemUUID);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, requestMap);
        if (oleDeliverRequestBoList.size() > 0)
            return oleDeliverRequestBoList.get(0);
        return null;
    }

    public String getPatronPreferredAddress(EntityTypeContactInfoBo entityTypeContactInfoBo) throws Exception {
        LOG.debug("Inside the getPatronPreferredAddress method");
        String address = "";
        if (entityTypeContactInfoBo.getAddresses() != null) {
            for (int i = 0; i < entityTypeContactInfoBo.getAddresses().size(); i++) {
                if (entityTypeContactInfoBo.getAddresses().get(i).isDefaultValue()) {
                    if (entityTypeContactInfoBo.getAddresses().get(i).getLine1() != null)
                        if (!entityTypeContactInfoBo.getAddresses().get(i).getLine1().isEmpty())
                            address += entityTypeContactInfoBo.getAddresses().get(i).getLine1() + ",";

                    if (entityTypeContactInfoBo.getAddresses().get(i).getLine2() != null)
                        if (!entityTypeContactInfoBo.getAddresses().get(i).getLine2().isEmpty())
                            address += entityTypeContactInfoBo.getAddresses().get(i).getLine2() + ",";

                    if (entityTypeContactInfoBo.getAddresses().get(i).getLine3() != null)
                        if (!entityTypeContactInfoBo.getAddresses().get(i).getLine3().isEmpty())
                            address += entityTypeContactInfoBo.getAddresses().get(i).getLine3() + ",";

                    if (entityTypeContactInfoBo.getAddresses().get(i).getCity() != null)
                        if (!entityTypeContactInfoBo.getAddresses().get(i).getCity().isEmpty())
                            address += entityTypeContactInfoBo.getAddresses().get(i).getCity() + ",";

                    if (entityTypeContactInfoBo.getAddresses().get(i).getStateProvinceCode() != null)
                        if (!entityTypeContactInfoBo.getAddresses().get(i).getStateProvinceCode().isEmpty())
                            address += entityTypeContactInfoBo.getAddresses().get(i).getStateProvinceCode() + ",";

                    if (entityTypeContactInfoBo.getAddresses().get(i).getCountryCode() != null)
                        if (!entityTypeContactInfoBo.getAddresses().get(i).getCountryCode().isEmpty())
                            address += entityTypeContactInfoBo.getAddresses().get(i).getCountryCode() + ",";

                    if (entityTypeContactInfoBo.getAddresses().get(i).getPostalCode() != null)
                        if (!entityTypeContactInfoBo.getAddresses().get(i).getPostalCode().isEmpty())
                            address += entityTypeContactInfoBo.getAddresses().get(i).getPostalCode();
                }
            }
        }

        return address;
    }

    public String getPatronHomePhoneNumber(EntityTypeContactInfoBo entityTypeContactInfoBo) throws Exception {
        LOG.debug("Inside the getPatronHomePhoneNumber method");
        String phoneNumber = "";
        if (entityTypeContactInfoBo.getPhoneNumbers() != null) {
            for (int j = 0; j < entityTypeContactInfoBo.getPhoneNumbers().size(); j++) {
                if (entityTypeContactInfoBo.getPhoneNumbers().get(j).getPhoneTypeCode().equalsIgnoreCase("HM")) {
                    phoneNumber = (entityTypeContactInfoBo.getPhoneNumbers().get(j).getPhoneNumber());
                }
            }
        }
        return phoneNumber;
    }

    public String getPatronHomeEmailId(EntityTypeContactInfoBo entityTypeContactInfoBo) throws Exception {
        LOG.debug("Inside the getPatronHomeEmailId method");
        String emailId = "";
        if (entityTypeContactInfoBo.getEmailAddresses() != null) {
            for (int j = 0; j < entityTypeContactInfoBo.getEmailAddresses().size(); j++) {
                if (entityTypeContactInfoBo.getEmailAddresses().get(j).getEmailTypeCode().equalsIgnoreCase("HM")) {
                    emailId = (entityTypeContactInfoBo.getEmailAddresses().get(j).getEmailAddress());
                }
            }
        }
        return emailId;
    }

    private static int determineDifferenceInDays(Date currentDate, Date dueDate) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(dueDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(currentDate);
        long diffInMillis = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
        return (int) (diffInMillis / (24 * 1000 * 60 * 60));
    }


    public void generateNotices() throws Exception {
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        List<OlePatronDocument> patronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findAll(OlePatronDocument.class);
        for (OlePatronDocument olePatronDocument : patronDocumentList) {
            Map<String, String> requestMap = new HashMap<String, String>();
            requestMap.put(OLEConstants.OleDeliverRequest.LOAN_PATRON_ID, olePatronDocument.getOlePatronId());
            List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, requestMap);
            for (OleLoanDocument oleLoanDocument : oleLoanDocumentList) {
                try {
                    List<OleNoticeBo> oleNoticeBos = new ArrayList<OleNoticeBo>();
                    org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                    org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
                    SearchResponse searchResponse = null;
                    search_Params.getSearchConditions().add(search_Params.buildSearchCondition("", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, oleLoanDocument.getItemId()), ""));
                    //search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "id"));
                    search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), "id"));
                    // search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "id"));
                   /* Map<String,String> map=new HashMap<>();
                    map.put(item.BARCODE, itemBarcode);
                    item=getDocstoreClientLocator().getDocstoreClient().findItem(map);*/
                    searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
                    for (SearchResult searchResult : searchResponse.getSearchResults()) {
                        for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                            String fieldName = searchResultField.getFieldName();
                            String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";

                            if (fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("holdings")) {
                                oleLoanDocument.setInstanceUuid(fieldValue);
                            }

                        }
                    }
                    String itemXml = loanProcessor.getItemXML(oleLoanDocument.getItemUuid());
                    org.kuali.ole.docstore.common.document.content.instance.Item oleItem = loanProcessor.getItemPojo(itemXml);
                    oleLoanDocument.setOleItem(oleItem);
                    oleLoanDocument.setOlePatron(olePatronDocument);
                    OleNoticeBo oleNoticeBo = new OleNoticeBo();
                    Date dueDate = oleLoanDocument.getLoanDueDate();
                    Date currentDate = new Date();
                    Integer dueDateDiffInterval = dueDate!=null ? determineDifferenceInDays(dueDate, currentDate) : 0;
                    DateFormat formatter = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE_NOTICE);
                    LOG.info("oleItem.getItemStatusEffectiveDate()" + oleItem.getItemStatusEffectiveDate());
                    Date itemStatusEffectiveDate = (Date) formatter.parse(oleItem.getItemStatusEffectiveDate());
                    Integer numberOfDaysOnHold = determineDifferenceInDays(itemStatusEffectiveDate, currentDate);
                    Integer dueDateSumInterval = dueDate!=null ? determineDifferenceInDays(currentDate, dueDate) : 0;
                    Integer overdueNoticeInterval = Integer.parseInt(getIntervalForOverdueNotice());
                    Integer intervalWithNoticeCount = 0;
                    Integer loanNoOfOverdueNoticeSent = oleLoanDocument.getNumberOfOverdueNoticesSent() != null ? Integer.parseInt(oleLoanDocument.getNumberOfOverdueNoticesSent()) : 0;
                    if (oleLoanDocument.getNumberOfOverdueNoticesSent() != null) {
                        intervalWithNoticeCount = Integer.parseInt(oleLoanDocument.getNumberOfOverdueNoticesSent()) + 1;
                    } else {
                        intervalWithNoticeCount = intervalWithNoticeCount + 1;
                    }
                 //   intervalWithNoticeCount = intervalWithNoticeCount * overdueNoticeInterval;
                    dueDateSumInterval = dueDateSumInterval / intervalWithNoticeCount;
                    if (dueDateSumInterval < 0) {
                        dueDateSumInterval = 0;
                    }
                    if (dueDateDiffInterval < 0) {
                        dueDateDiffInterval = 0;
                    }
                    Integer maxNumberOfDaysOnHold = 0;
                    OleCirculationDesk oleCirculationDesk = null;
                    if (oleLoanDocument.getCirculationLocationId() != null) {
                        oleCirculationDesk = getCircDeskLocationResolver().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId());
                        String maxNumOfDays = oleCirculationDesk.getOnHoldDays() != null ? oleCirculationDesk.getOnHoldDays() : loanProcessor.getParameter(OLEConstants.MAX_NO_OF_DAYS_ON_HOLD);
                        maxNumberOfDaysOnHold = new Integer(maxNumOfDays);
                    }
                    if (oleItem.getTemporaryItemType() != null && oleItem.getTemporaryItemType().getCodeValue() != "") {
                        OleInstanceItemType oleInstanceItemType = loanProcessor.getItemTypeIdByItemType(oleItem.getTemporaryItemType().getCodeValue());
                        oleLoanDocument.setItemTypeName(oleInstanceItemType.getInstanceItemTypeCode());
                    }
                    else if (oleItem.getItemType() != null && oleItem.getItemType().getCodeValue() != "") {
                        OleInstanceItemType oleInstanceItemType = loanProcessor.getItemTypeIdByItemType(oleItem.getItemType().getCodeValue());
                        oleLoanDocument.setItemTypeName(oleInstanceItemType.getInstanceItemTypeCode());
                    }
                    oleLoanDocument.setInstanceUuid(oleLoanDocument.getInstanceUuid());
                    loanProcessor.getLocation(oleItem, oleLoanDocument);
                    String agendaName = OLEConstants.NOTICE_AGENDA_NM;
                    dataCarrierService.addData(OLEConstants.CIRC_POLICY_FOUND, null);
                    HashMap<String, Object> termValues = new HashMap<String, Object>();
                    termValues.put(OLEConstants.BORROWER_TYPE, olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                    termValues.put(OLEConstants.ITEM_TYPE, oleLoanDocument.getItemTypeName());
                    termValues.put(OLEConstants.OVERLAY_ITEM_LOCATION, oleLoanDocument.getItemLocation());
                    termValues.put(OLEConstants.DUE_DATE_DIFF_INTERVAL, dueDateDiffInterval);
                    if (oleItem.getItemStatus() != null)
                        termValues.put(OLEConstants.ITEM_STATUS, oleItem.getItemStatus().getCodeValue());
                    termValues.put(OLEConstants.NO_OF_DAYS_ON_HOLD, numberOfDaysOnHold);
                    termValues.put(OLEConstants.MAX_NO_OF_DAYS_ONHOLD, maxNumberOfDaysOnHold);
                    termValues.put(OLEConstants.OleDeliverRequest.CLAIM_RETURNED, oleItem.isClaimsReturnedFlag() ? "true" : "false");
                    termValues.put(OLEConstants.OleDeliverRequest.REPLACEMENT_FEE_PATRON_BILL, oleLoanDocument.getRepaymentFeePatronBillId() != null ? "true" : "false");
                    termValues.put(OLEConstants.OleDeliverRequest.NUBER_OF_OVER_DUE_SENT, loanNoOfOverdueNoticeSent);
                    termValues.put(OLEConstants.OleDeliverRequest.DUE_DATE_SUM_INTERVAL, dueDateSumInterval);
                    termValues.put(OLEConstants.OleDeliverRequest.CONFIGURABLE_INTERVAL, Integer.parseInt(getIntervalForCourtesyNotice()));
                    termValues.put(OLEConstants.OleDeliverRequest.INTERVAL_WITH_NOTICE_COUNT, intervalWithNoticeCount);
                    termValues.put(OLEConstants.ITEM_SHELVING, oleLoanDocument.getItemLocation());
                    termValues.put(OLEConstants.ITEM_COLLECTION, oleLoanDocument.getItemCollection());
                    termValues.put(OLEConstants.ITEM_LIBRARY, oleLoanDocument.getItemLibrary());
                    termValues.put(OLEConstants.ITEM_CAMPUS, oleLoanDocument.getItemCampus());
                    termValues.put(OLEConstants.ITEM_INSTITUTION, oleLoanDocument.getItemInstitution());
                    LOG.info("termValues.toString()" + termValues.toString());
                    EngineResults engineResults = loanProcessor.getEngineResults(agendaName, termValues);
                    String notice = (String) engineResults.getAttribute(OLEConstants.NOTICE);
                    String itemStatus = (String) engineResults.getAttribute(OLEConstants.ITEM_STATUS);
                    BigDecimal replacementBill = (BigDecimal) engineResults.getAttribute(OLEConstants.REPLACEMENT_BILL);
                    List<String> errorMessage = (List<String>) engineResults.getAttribute(OLEConstants.ERROR_ACTION);
                    if (errorMessage != null) {
                        LOG.info("errorMessage" + errorMessage.toString());
                    }
                    LOG.info("notice" + notice);
                    agendaName = OLEConstants.BATCH_PROGRAM_AGENDA;
                    termValues = new HashMap<String, Object>();
                    String deskLocation = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskCode() : "";
                    String deskLocationName = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : "";
                    termValues.put(OLEConstants.BORROWER_TYPE, olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                    termValues.put(OLEConstants.DESK_LOCATION, deskLocation);
                    termValues.put(OLEConstants.NOTICE, notice);
                    engineResults = loanProcessor.getEngineResults(agendaName, termValues);
                    String noticeType = (String) engineResults.getAttribute(OLEConstants.NOTICE_TYPE);
                    errorMessage = (List<String>) engineResults.getAttribute(OLEConstants.ERROR_ACTION);
                    if (errorMessage != null) {
                        LOG.info("errorMessage" + errorMessage);
                    }
                    LOG.info("noticeType" + noticeType);
                    if (itemStatus != null) {
                        int noOfOverdueNoticeSent = Integer.parseInt(oleLoanDocument.getNumberOfOverdueNoticesSent() != null ? oleLoanDocument.getNumberOfOverdueNoticesSent() : "0");
                        noOfOverdueNoticeSent = noOfOverdueNoticeSent + 1;
                        oleLoanDocument.setNumberOfOverdueNoticesSent(Integer.toString(noOfOverdueNoticeSent));
                        oleLoanDocument.setOverDueNoticeDate(new java.sql.Date(System.currentTimeMillis()));
                        String billNumber = loanProcessor.generatePatronBillPayment(oleLoanDocument, OLEConstants.REPLACEMENT_FEE, replacementBill);
                        oleLoanDocument.setRepaymentFeePatronBillId(billNumber);
                        getBusinessObjectService().save(oleLoanDocument);
                        loanProcessor.updateItemStatus(oleItem, itemStatus);
                    }
                    OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
                    if (notice != null) {
                        oleNoticeBo.setNoticeName(notice);
                    /*if(notice.equalsIgnoreCase(OLEConstants.NOTICE_HOLD_COURTESY)){
                        oleNoticeBo = getExpiredHoldNotice(oleLoanDocument);
                    } else */
                        if (notice.equalsIgnoreCase(OLEConstants.OVERDUE_NOTICE)) {
                            oleNoticeBo = getOverdueNotice(oleLoanDocument);
                            noticeType = noticeType == null ? loanProcessor.getParameter(OLEParameterConstants.OVERDUE_NOTICE_TYPE) : noticeType;
                        } else if (notice.equalsIgnoreCase(OLEConstants.COURTESY_NOTICE) && olePatronDocument.isCourtesyNotice() && !oleLoanDocument.isCourtesyNoticeFlag()) {
                            oleNoticeBo = getCourtesyNotice(oleLoanDocument);
                            noticeType = noticeType == null ? loanProcessor.getParameter(OLEParameterConstants.COURTESY_NOTICE_TYPE) : noticeType;
                        }
                        oleNoticeBo.setCirculationDeskName(deskLocationName);
                        if (!olePatronDocument.isCourtesyNotice()) {
                            oleNoticeBo = null;
                        }
                        if (oleNoticeBo != null) {
                            oleNoticeBos.add(oleNoticeBo);
                        }
                        if (oleNoticeBos != null && oleNoticeBos.size() > 0 && noticeType != null && (noticeType.equalsIgnoreCase(OLEConstants.EMAIL) || noticeType.equalsIgnoreCase(OLEConstants.MAIL))) {
                            oleDeliverBatchService.getPdfNoticeForPatron(oleNoticeBos);
                        }
                    }
                    olePatronDocument = oleLoanDocument.getOlePatron();
                    if (oleNoticeBos != null && oleNoticeBos.size() > 0 && oleNoticeBos != null && oleNoticeBos.size() > 0 && noticeType != null && noticeType.equalsIgnoreCase(OLEConstants.EMAIL)) {
                        if (olePatronDocument.getEmailAddress() != null && !olePatronDocument.getEmailAddress().isEmpty()) {
                            List list = oleDeliverBatchService.getNoticeForPatron(oleNoticeBos);
                            String noticeContent = list.toString();
                            noticeContent = noticeContent.replace('[', ' ');
                            noticeContent = noticeContent.replace(']', ' ');
                            if (!noticeContent.trim().equals("")) {
                                OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                                String replyToEmail = getCircDeskLocationResolver().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                                if (replyToEmail != null) {
                                    oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(noticeService.getNoticeSubjectForNoticeType(notice)), new EmailBody(noticeContent), true);
                                } else {
                                    String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                                    if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                                        fromAddress = OLEConstants.KUALI_MAIL;
                                    }
                                    oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(noticeService.getNoticeSubjectForNoticeType(notice)), new EmailBody(noticeContent), true);
                                }
                            } else {
                                for (OleNoticeBo oleNoticeBo1 : oleNoticeBos) {
                                    LOG.info("Notice Type :" + oleNoticeBo1.getNoticeName() + "  " + "Item Barcode : " + oleNoticeBo1.getItemId() + " " + "Patron Name :" + oleNoticeBo1.getPatronName());
                                }
                            }
                            LOG.info("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                      /*  Mailer mailer =CoreApiServiceLocator.getMailer();
                        mailer.sendEmail(new EmailFrom(OLEConstants.KUALI_MAIL), new EmailTo(olePatronDocument.getEmailAddress()), new EmailSubject("Notice Mail"), new EmailBody(list.toString()), true);
                      */
                            LOG.info("olePatronDocument.getEmailAddress()" + olePatronDocument.getEmailAddress());
                        }
                    } else if (noticeType != null && noticeType.equalsIgnoreCase(OLEConstants.SMS)) {
                        //TODO : sms in progress.
                    }
                } catch (Exception e) {
                    LOG.error("Exception in generateNotices()" + e.getMessage(), e);
                }
            }
        }
    }

    public void generateHoldCourtesyNotice() throws Exception {
        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findAll(OleDeliverRequestBo.class);
        for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBos) {
            List<OleNoticeBo> oleNoticeBos = new ArrayList<OleNoticeBo>();
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            if (docstoreUtil.isItemAvailableInDocStore(oleDeliverRequestBo)) {
                org.kuali.ole.docstore.common.document.content.instance.Item oleItem = oleDeliverRequestBo.getOleItem();
                OleNoticeBo oleNoticeBo = new OleNoticeBo();
                Date currentDate = new Date();
                DateFormat formatter = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE_NOTICE);
                Date itemStatusEffectiveDate = (Date) formatter.parse(oleItem.getItemStatusEffectiveDate());
                Integer numberOfDaysOnHold = determineDifferenceInDays(currentDate, itemStatusEffectiveDate);
                Integer maxNumberOfDaysOnHold = 0;
                OleCirculationDesk oleCirculationDesk = null;
                if (oleDeliverRequestBo.getPickUpLocationId() != null) {
                    oleCirculationDesk = getCircDeskLocationResolver().getOleCirculationDesk(oleDeliverRequestBo.getPickUpLocationId());
                    String maxNumOfDays = oleCirculationDesk.getOnHoldDays() != null ? oleCirculationDesk.getOnHoldDays() : loanProcessor.getParameter(OLEConstants.MAX_NO_OF_DAYS_ON_HOLD);
                    maxNumberOfDaysOnHold = new Integer(maxNumOfDays);
                }
                String itemTypeName = null;
                if (oleItem.getTemporaryItemType() != null && oleItem.getTemporaryItemType().getCodeValue() != "") {
                    OleInstanceItemType oleInstanceItemType = loanProcessor.getItemTypeIdByItemType(oleItem.getTemporaryItemType().getCodeValue());
                    itemTypeName = oleInstanceItemType.getInstanceItemTypeCode();
                }else if (oleItem.getItemType() != null && oleItem.getItemType().getCodeValue() != "") {
                    OleInstanceItemType oleInstanceItemType = loanProcessor.getItemTypeIdByItemType(oleItem.getItemType().getCodeValue());
                    itemTypeName = oleInstanceItemType.getInstanceItemTypeCode();
                }
                OlePatronDocument olePatronDocument = oleDeliverRequestBo.getOlePatron();
                String agendaName = "Notice Validation";
                dataCarrierService.addData(OLEConstants.CIRC_POLICY_FOUND, null);
                HashMap<String, Object> termValues = new HashMap<String, Object>();
                termValues.put(OLEConstants.BORROWER_TYPE, olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                termValues.put(OLEConstants.ITEM_TYPE, itemTypeName);
                termValues.put(OLEConstants.OVERLAY_ITEM_LOCATION, oleDeliverRequestBo.getShelvingLocation());
                termValues.put(OLEConstants.NO_OF_DAYS_ON_HOLD, numberOfDaysOnHold);
                termValues.put(OLEConstants.MAX_NO_OF_DAYS_ONHOLD, maxNumberOfDaysOnHold);
                termValues.put(OLEConstants.ITEM_SHELVING, oleDeliverRequestBo.getShelvingLocation());
                termValues.put(OLEConstants.ITEM_COLLECTION, oleDeliverRequestBo.getItemCollection());
                termValues.put(OLEConstants.ITEM_LIBRARY, oleDeliverRequestBo.getItemLibrary());
                termValues.put(OLEConstants.ITEM_CAMPUS, oleDeliverRequestBo.getItemCampus());
                termValues.put(OLEConstants.ITEM_INSTITUTION, oleDeliverRequestBo.getItemInstitution());
                if (oleItem.getItemStatus() != null)
                    termValues.put(OLEConstants.ITEM_STATUS, oleItem.getItemStatus().getCodeValue());
                LOG.info("termValues.toString()" + termValues.toString());
                EngineResults engineResults = loanProcessor.getEngineResults(agendaName, termValues);
                String notice = (String) engineResults.getAttribute(OLEConstants.NOTICE);
                LOG.info("notice" + notice);
                agendaName = OLEConstants.BATCH_PROGRAM_AGENDA;
                termValues = new HashMap<String, Object>();
                String deskLocation = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskCode() : "";
                String deskLocationName = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : "";
                termValues.put(OLEConstants.BORROWER_TYPE, olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                termValues.put(OLEConstants.DESK_LOCATION, deskLocation);
                termValues.put(OLEConstants.NOTICE, notice);
                LOG.info("termValues.toString()" + termValues.toString());
                engineResults = loanProcessor.getEngineResults(agendaName, termValues);
                String noticeType = (String) engineResults.getAttribute(OLEConstants.NOTICE_TYPE);
                LOG.info("**************" + noticeType);
                OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
                if (notice != null) {
                    oleNoticeBo.setNoticeName(notice);
                    if (notice.equalsIgnoreCase(OLEConstants.NOTICE_HOLD_COURTESY)) {
                        oleNoticeBo = getExpiredHoldNotice(oleDeliverRequestBo);
                        oleNoticeBo.setCirculationDeskName(deskLocationName);
                        oleNoticeBos.add(oleNoticeBo);
                        noticeType = noticeType == null ? loanProcessor.getParameter("HOLDCOURTESY_NOTICE_TYPE") : noticeType;
                    }
                    if (oleNoticeBos.size() > 0 && noticeType != null && (noticeType.equalsIgnoreCase(OLEConstants.EMAIL) || noticeType.equalsIgnoreCase(OLEConstants.MAIL))) {
                        oleDeliverBatchService.getPdfNoticeForPatron(oleNoticeBos);
                    }

                }
                olePatronDocument = oleDeliverRequestBo.getOlePatron();
                if (noticeType != null && noticeType.equalsIgnoreCase(OLEConstants.EMAIL)) {
                    if (olePatronDocument.getEmailAddress() != null && !olePatronDocument.getEmailAddress().isEmpty()) {
                        List list = oleDeliverBatchService.getNoticeForPatron(oleNoticeBos);
                        String noticeContent = list.toString();
                        noticeContent = noticeContent.replace('[', ' ');
                        noticeContent = noticeContent.replace(']', ' ');
                        if (!noticeContent.trim().equals("")) {
                            OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                            if (oleDeliverRequestBo.getOlePickUpLocation() != null && StringUtils.isNotBlank(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail())) {
                                oleMailer.sendEmail(new EmailFrom(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail()), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(noticeService.getNoticeSubjectForNoticeType(notice)), new EmailBody(noticeContent), true);
                            } else {
                                String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                                if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                                    fromAddress = OLEConstants.KUALI_MAIL;
                                }
                                oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(noticeService.getNoticeSubjectForNoticeType(notice)), new EmailBody(noticeContent), true);
                            }
                        } else {
                            for (OleNoticeBo oleNoticeBo1 : oleNoticeBos) {
                                LOG.info("Notice Type :" + oleNoticeBo1.getNoticeName() + "  " + "Item Barcode : " + oleNoticeBo1.getItemId() + " " + "Patron Name :" + oleNoticeBo1.getPatronName());
                            }
                        }
/*
                        Mailer mailer =CoreApiServiceLocator.getMailer();
                        mailer.sendEmail(new EmailFrom(OLEConstants.KUALI_MAIL), new EmailTo(olePatronDocument.getEmailAddress()), new EmailSubject("Notice Mail"), new EmailBody(list.toString()), true);
                     */
                        LOG.info("olePatronDocument.getEmailAddress()" + olePatronDocument.getEmailAddress());
                    }
                } else if (noticeType != null && noticeType.equalsIgnoreCase(OLEConstants.SMS)) {
                    //TODO : sms in progress.
                }
            }
        }
    }

    public void deleteTemporaryHistoryRecord() throws Exception {
        List<OlePatronDocument> patronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findAll(OlePatronDocument.class);
        for (OlePatronDocument olePatronDocument : patronDocumentList) {
            Map<String, String> requestMap = new HashMap<String, String>();
            requestMap.put(OLEConstants.OlePatron.PATRON_ID, olePatronDocument.getOlePatronId());
            List<OleTemporaryCirculationHistory> oleTemporaryCirculationHistoryList = (List<OleTemporaryCirculationHistory>) KRADServiceLocator.getBusinessObjectService().findMatching(OleTemporaryCirculationHistory.class, requestMap);
            List<OleTemporaryCirculationHistory> deleteRecords = new ArrayList<OleTemporaryCirculationHistory>();
            for (OleTemporaryCirculationHistory oleTemporaryCirculationHistory : oleTemporaryCirculationHistoryList) {
                String agendaName = OLEConstants.BATCH_PROGRAM_AGENDA;
                HashMap<String, Object> termValues = new HashMap<String, Object>();
                termValues.put(OLEConstants.OleDeliverRequest.IS_TEMPORARY_HISTORY_RECORD, "true");
                DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
                dataCarrierService.addData(OLEConstants.DATE_CHECK_IN, oleTemporaryCirculationHistory.getCheckInDate());
                EngineResults engineResults = loanProcessor.getEngineResults(agendaName, termValues);
                Boolean deleteRecord = (Boolean) engineResults.getAttribute(OLEConstants.OVERLAY_OPTION_DELETE);
                if (deleteRecord != null && deleteRecord) {
                    deleteRecords.add(oleTemporaryCirculationHistory);
                }
            }
            getBusinessObjectService().delete(deleteRecords);
        }
    }

    private OleNoticeBo getExpiredHoldNotice(OleDeliverRequestBo oleDeliverRequestBo) throws Exception {
        LOG.debug("Expired Hold Notice");
        org.kuali.ole.docstore.common.document.content.instance.Item oleItem = oleDeliverRequestBo.getOleItem();
        EntityTypeContactInfoBo entityTypeContactInfoBo = oleDeliverRequestBo.getOlePatron().getEntity().getEntityTypeContactInfos().get(0);
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(oleDeliverRequestBo.getItemUuid());
        String shelvingLocation = oleDeliverRequestBo.getShelvingLocation();
        OleCirculationDesk oleCirculationDesk = getOleCirculationDesk(oleDeliverRequestBo.getPickUpLocationId());
        if (oleCirculationDesk != null)
            oleNoticeBo.setCirculationDeskName(oleCirculationDesk.getCirculationDeskPublicName());
        else
            oleNoticeBo.setCirculationDeskName("");
        oleNoticeBo.setCirculationDeskAddress("");
        oleNoticeBo.setCirculationDeskEmailAddress("");
        oleNoticeBo.setCirculationDeskPhoneNumber("");
        oleNoticeBo.setCirculationDeskPhoneNumber("");
        oleNoticeBo.setPatronName(oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getFirstName() + " " + oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getLastName());
        oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
        oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
        oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
        oleNoticeBo.setNoticeName(OLEConstants.NOTICE_HOLD_COURTESY);
        oleNoticeBo.setNoticeSpecificContent(LoanUtil.getLoanUtil().getProperty(OLEConstants.OleDeliverRequest.EXP_HOLD_NOTICE_CONTENT));
        oleNoticeBo.setTitle(item.getHolding().getBib().getTitle());
        oleNoticeBo.setAuthor(item.getHolding().getBib().getAuthor());
        /*oleNoticeBo.setTitle((String) bibInformation.get(OLEConstants.TITLE) != null ? (String) bibInformation.get(OLEConstants.TITLE) : "");
        oleNoticeBo.setAuthor((String) bibInformation.get(OLEConstants.AUTHOR) != null ? (String) bibInformation.get(OLEConstants.AUTHOR) : "");*/
        oleNoticeBo.setVolumeNumber(item.getVolumeNumber());
        oleNoticeBo.setItemShelvingLocation(shelvingLocation != null ? shelvingLocation : "");
        //oleNoticeBo.setItemCallNumber((String) docStoreDetails.get(OLEConstants.CALL_NUM) != null ? (String) docStoreDetails.get(OLEConstants.CALL_NUM) : "");
        oleNoticeBo.setItemCallNumber((String) oleItem.getCallNumber().getNumber() != null && !oleItem.getCallNumber().getNumber().equals("") ? oleItem.getCallNumber().getNumber() : "");
        oleNoticeBo.setItemId(oleDeliverRequestBo.getItemId() != null ? oleDeliverRequestBo.getItemId() : "");
        oleNoticeBo.setOleItem(oleItem);
        oleNoticeBo.setOlePatron(oleDeliverRequestBo.getOlePatron());
        oleNoticeBo = setPatronDetailsForNotice(oleNoticeBo, oleDeliverRequestBo.getOlePatron());
        oleDeliverRequestBo.getOlePatron().setEmailAddress(oleNoticeBo.getPatronEmailAddress());
        return oleNoticeBo;
    }

    private OleNoticeBo getCourtesyNotice(OleLoanDocument oleLoanDocument) throws Exception {
        LOG.debug("Courtesy Notice");
        oleLoanDocument.setCourtesyNoticeFlag(true);
        Map<String, String> map = new HashMap<String, String>();
        map.put("loanId", oleLoanDocument.getLoanId());
        KRADServiceLocator.getBusinessObjectService().save(oleLoanDocument);
        EntityTypeContactInfoBo entityTypeContactInfoBo = oleLoanDocument.getOlePatron().getEntity().getEntityTypeContactInfos().get(0);
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        //String itemId  = oleLoanDocument.getItemId();
        org.kuali.ole.docstore.common.document.content.instance.Item oleItem = oleLoanDocument.getOleItem();
        org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(oleLoanDocument.getItemUuid());
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item itemContent = itemOlemlRecordProcessor.fromXML(item.getContent());
        String shelvingLocation = oleLoanDocument.getItemLocation();
        OleCirculationDesk oleCirculationDesk = getOleCirculationDesk(oleLoanDocument.getCirculationLocationId());
        if (oleCirculationDesk != null)
            oleNoticeBo.setCirculationDeskName(oleCirculationDesk.getCirculationDeskPublicName());
        else
            oleNoticeBo.setCirculationDeskName("");
        oleNoticeBo.setCirculationDeskAddress("");
        oleNoticeBo.setCirculationDeskEmailAddress("");
        oleNoticeBo.setCirculationDeskPhoneNumber("");
        oleNoticeBo.setPatronName(oleLoanDocument.getOlePatron().getEntity().getNames().get(0).getFirstName() + " " + oleLoanDocument.getOlePatron().getEntity().getNames().get(0).getLastName());
        oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
        oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
        oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
        oleNoticeBo.setNoticeName(OLEConstants.COURTESY_NOTICE);
        oleNoticeBo.setNoticeSpecificContent(LoanUtil.getLoanUtil().getProperty(OLEConstants.OleDeliverRequest.COURTESY_NOTICE_CONTENT));
       /* oleNoticeBo.setTitle((String) bibInformation.get(OLEConstants.TITLE) != null ? (String) bibInformation.get(OLEConstants.TITLE) : "");
        oleNoticeBo.setAuthor((String) bibInformation.get(OLEConstants.AUTHOR) != null ? (String) bibInformation.get(OLEConstants.AUTHOR) : "");*/
        oleNoticeBo.setTitle(item.getHolding().getBib().getTitle());
        oleNoticeBo.setAuthor(item.getHolding().getBib().getAuthor());
        oleNoticeBo.setVolumeNumber(item.getVolumeNumber());
        oleNoticeBo.setItemShelvingLocation(shelvingLocation != null ? shelvingLocation : "");
        // oleNoticeBo.setItemCallNumber((String) docStoreDetails.get(OLEConstants.CALL_NUM) != null ? (String) docStoreDetails.get(OLEConstants.CALL_NUM) : "");
        oleNoticeBo.setItemCallNumber(loanProcessor.getItemCallNumber(itemContent, item.getHolding().getId()));
        oleNoticeBo.setItemId(oleLoanDocument.getItemId());
        oleLoanDocument.getOlePatron().setEmailAddress(oleNoticeBo.getPatronEmailAddress());
        LOG.info("oleNoticeBo.getPatronEmailAddress()" + oleNoticeBo.getPatronEmailAddress());
        return oleNoticeBo;
    }

    private OleNoticeBo getOverdueNotice(OleLoanDocument oleLoanDocument) throws Exception {
        LOG.debug("Overdue Notice");
        EntityTypeContactInfoBo entityTypeContactInfoBo = oleLoanDocument.getOlePatron().getEntity().getEntityTypeContactInfos().get(0);
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        //   String itemId  = oleLoanDocument.getItemId();
        org.kuali.ole.docstore.common.document.content.instance.Item oleItem = oleLoanDocument.getOleItem();
        //  String itemUuid = oleItem.getItemIdentifier();
        //String shelvingLocation = oleLoanDocument.getItemLocation();
        org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(oleLoanDocument.getItemUuid());
        OleCirculationDesk oleCirculationDesk = getOleCirculationDesk(oleLoanDocument.getCirculationLocationId());
        if (oleCirculationDesk != null)
            oleNoticeBo.setCirculationDeskName(oleCirculationDesk.getCirculationDeskPublicName());
        else
            oleNoticeBo.setCirculationDeskName("");
        oleNoticeBo.setCirculationDeskAddress("");
        oleNoticeBo.setCirculationDeskEmailAddress("");
        oleNoticeBo.setCirculationDeskPhoneNumber("");
        oleNoticeBo.setPatronName(oleLoanDocument.getOlePatron().getEntity().getNames().get(0).getFirstName() + " " + oleLoanDocument.getOlePatron().getEntity().getNames().get(0).getLastName());
        oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
        oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
        oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
        oleNoticeBo.setNoticeName(OLEConstants.OVERDUE_NOTICE);
        oleNoticeBo.setNoticeSpecificContent(LoanUtil.getLoanUtil().getProperty(OLEConstants.OleDeliverRequest.OVERDUE_NOTICE_CONTENT));
        oleNoticeBo.setTitle(item.getHolding().getBib().getTitle());
        oleNoticeBo.setAuthor(item.getHolding().getBib().getAuthor());
        /*oleNoticeBo.setTitle((String) bibInformation.get(OLEConstants.TITLE) != null ? (String) bibInformation.get(OLEConstants.TITLE) : "");
        oleNoticeBo.setAuthor((String) bibInformation.get(OLEConstants.AUTHOR) != null ? (String) bibInformation.get(OLEConstants.AUTHOR) : "");*/

        //oleNoticeBo.setVolumeNumber((String) docStoreDetails.get(OLEConstants.VOL_NUM) != null ? (String) docStoreDetails.get(OLEConstants.VOL_NUM) : "");
        String volume = (String) oleItem.getEnumeration() != null && !oleItem.getEnumeration().equals("") ? oleItem.getEnumeration() : "";
        String issue = new String(" ");
        String copyNumber = (String) oleItem.getCopyNumber() != null && !oleItem.getCopyNumber().equals("") ? oleItem.getCopyNumber() : "";
        oleNoticeBo.setVolumeIssueCopyNumber(volume + "/" + issue + "/" + copyNumber);
        oleNoticeBo.setItemShelvingLocation(item.getLocationName());
        /*if (oleItem.getCallNumber().getNumber() != null && !oleItem.getCallNumber().getNumber().equals("")) {
            oleNoticeBo.setItemCallNumber((String) oleItem.getCallNumber().getNumber() != null && !oleItem.getCallNumber().getNumber().equals("") ? oleItem.getCallNumber().getNumber() : "");
        } else {
            oleNoticeBo.setItemCallNumber(getLoanProcessor().getItemCallNumber(oleItem, oleLoanDocument.getInstanceUuid()));
        }*/
        oleNoticeBo.setItemCallNumber(getLoanProcessor().getItemCallNumber(oleItem, oleLoanDocument.getInstanceUuid()));
        //oleNoticeBo.setItemCallNumber((String) docStoreDetails.get(OLEConstants.CALL_NUM) != null ? (String) docStoreDetails.get(OLEConstants.CALL_NUM) : "");
        oleNoticeBo.setItemId(oleLoanDocument.getItemId());
        //oleNoticeBo.setDueDate(oleLoanDocument.getLoanDueDate()!=null ? oleLoanDocument.getLoanDueDate().toString().substring(0, 10) : null);
        oleNoticeBo.setDueDate(oleLoanDocument.getLoanDueDate()!=null ? oleLoanDocument.getLoanDueDate() :null);
        oleLoanDocument.getOlePatron().setEmailAddress(oleNoticeBo.getPatronEmailAddress());
        LOG.info("oleNoticeBo.getPatronEmailAddress()" + oleNoticeBo.getPatronEmailAddress());
        int noOfOverdueNoticeSent = Integer.parseInt(oleLoanDocument.getNumberOfOverdueNoticesSent() != null ? oleLoanDocument.getNumberOfOverdueNoticesSent() : "0");
        noOfOverdueNoticeSent = noOfOverdueNoticeSent + 1;
        LOG.debug("Updated Loan Record : " + oleLoanDocument);
        oleLoanDocument.setNumberOfOverdueNoticesSent(Integer.toString(noOfOverdueNoticeSent));
        oleLoanDocument.setOverDueNoticeDate(new java.sql.Date(System.currentTimeMillis()));
        getBusinessObjectService().save(oleLoanDocument);
        return oleNoticeBo;
    }

    public void updateItem(org.kuali.ole.docstore.common.document.content.instance.Item oleItem, String itemUuid) throws Exception {
        org.kuali.ole.docstore.common.document.content.instance.ItemStatus itemStatus = new org.kuali.ole.docstore.common.document.content.instance.ItemStatus();
        itemStatus.setCodeValue(OLEConstants.OleDeliverRequest.MISSING);
        itemStatus.setFullValue(OLEConstants.OleDeliverRequest.MISSING);
        oleItem.setItemStatus(itemStatus);
        oleItem.setStaffOnlyFlag(true);
        String itemContent = new ItemOlemlRecordProcessor().toXML(oleItem);
        org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
        item.setId(itemUuid);
        item.setContent(itemContent);
        item.setCategory(OLEConstants.WORK_CATEGORY);
        item.setType(DocType.ITEM.getCode());
        item.setFormat(OLEConstants.OLEML_FORMAT);
        getDocstoreClientLocator().getDocstoreClient().updateItem(item);
    }

    public String getShelvingLocation(org.kuali.ole.docstore.common.document.content.instance.LocationLevel oleLocationLevel) {
        String location = null;
        if (oleLocationLevel != null) {
            if (OLEConstants.LOCATION_LEVEL_SHELVING.equalsIgnoreCase(oleLocationLevel.getLevel()))
                location = oleLocationLevel.getName();
            else
                location = getShelvingLocation(oleLocationLevel.getLocationLevel());
        }
        if ("".equals(location) || location == null)
            return null;
        return location;
    }


    public String getIntervalForCourtesyNotice() {
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put(OLEConstants.NAMESPACE_CODE, OLEConstants.DLVR_NMSPC);
        criteriaMap.put(OLEConstants.COMPONENT_CODE, OLEConstants.DLVR_CMPNT);
        criteriaMap.put(OLEConstants.NAME, OLEParameterConstants.COURTESY_NOTICE_INTER);
        List<ParameterBo> parametersList = (List<ParameterBo>) businessObjectService.findMatching(ParameterBo.class, criteriaMap);
        return parametersList.get(0).getValue();
    }

    public String getIntervalForOverdueNotice() {
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put(OLEConstants.NAMESPACE_CODE, OLEConstants.DLVR_NMSPC);
        criteriaMap.put(OLEConstants.COMPONENT_CODE, OLEConstants.DLVR_CMPNT);
        criteriaMap.put(OLEConstants.NAME, OLEConstants.OVERDUE_NOTICE_INTER);
        List<ParameterBo> parametersList = (List<ParameterBo>) businessObjectService.findMatching(ParameterBo.class, criteriaMap);
        return parametersList.get(0).getValue();
    }


    private PatronBillPayment getPatronBillPayment(String patronId) {
        LOG.debug("Inside the getPatronBillPayment method");
        Map billMap = new HashMap();
        billMap.put(OLEConstants.OleDeliverRequest.LOAN_PATRON_ID, patronId);
        List<PatronBillPayment> patronBillPaymentList = (List<PatronBillPayment>) getBusinessObjectService().findMatching(PatronBillPayment.class, billMap);
        return patronBillPaymentList != null && patronBillPaymentList.size() > 0 ? patronBillPaymentList.get(0) : null;
    }

    private OlePaymentStatus getPaymentStatus() {
        LOG.debug("Inside the getPaymentStatus method");
        Map statusMap = new HashMap();
        statusMap.put(OLEConstants.OleDeliverRequest.PAYMENT_STATUS_NAME, OLEConstants.PAYMENT_STATUS_OUTSTANDING);
        List<OlePaymentStatus> olePaymentStatusList = (List<OlePaymentStatus>) getBusinessObjectService().findMatching(OlePaymentStatus.class, statusMap);
        return olePaymentStatusList != null && olePaymentStatusList.size() > 0 ? olePaymentStatusList.get(0) : null;
    }

    private String getFeeTypeId(String feeTypeName) {
        LOG.debug("Inside the getOverdueFeeTypeId method");
        Map feeMap = new HashMap();
        feeMap.put(OLEConstants.FEE_TYPE_NAME, feeTypeName);
        List<OleFeeType> oleFeeTypes = (List<OleFeeType>) getBusinessObjectService().findMatching(OleFeeType.class, feeMap);
        return oleFeeTypes != null && oleFeeTypes.size() > 0 ? oleFeeTypes.get(0).getFeeTypeId() : null;
    }

    public String processItemType(String itemType) {

        LOG.debug("Inside process Item Type");
        Map<String, String> itemMap = new HashMap<String, String>();
        itemMap.put(OLEConstants.OleDeliverRequest.ITEM_TYPE_CODE, itemType);
        List<OleInstanceItemType> oleInstanceItemTypeList = (List<OleInstanceItemType>) businessObjectService.findMatching(OleInstanceItemType.class, itemMap);
        if (oleInstanceItemTypeList != null && oleInstanceItemTypeList.size() > 0) {
            OleInstanceItemType oleInstanceItemType = oleInstanceItemTypeList.get(0);
            return oleInstanceItemType.getInstanceItemTypeName();
        }
        return null;
    }

    public String placeRequest(String patronId, String operatorId, String itemBarcode, String requestType, String pickUpLocation, String itemIdentifier) {
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();
        oleDeliverRequestBo.setCreateDate(new Timestamp(System.currentTimeMillis()));
        OlePatronDocument olePatronDocument = null;
        Map<String, String> patronMap = new HashMap<String, String>();
        patronMap.put("olePatronId", patronId);
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) businessObjectService.findMatching(OlePatronDocument.class, patronMap);
        if (olePatronDocumentList.size() > 0) {
            olePatronDocument = olePatronDocumentList.get(0);
            oleDeliverRequestBo.setBorrowerId(patronId);
            oleDeliverRequestBo.setBorrowerBarcode(olePatronDocument.getBarcode());
            oleDeliverRequestBo.setOlePatron(olePatronDocument);
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);
            try {
                oleNoticeBo.setPatronName(olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " + oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getLastName());
                oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
                oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
                oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
            } catch (Exception e) {
                LOG.error(e, e);
                LOG.info("Exception Occured while setting the patron information for the patron . Patron Barcode : " + oleDeliverRequestBo.getBorrowerBarcode());
            }
        } else {
            return "No patron information";
        }
        if (requestType != null) {
            Map<String, String> requestTypeMap = new HashMap<String, String>();
            requestTypeMap.put("requestTypeCode", requestType);
            List<OleDeliverRequestType> oleDeliverRequestTypeList = (List<OleDeliverRequestType>) businessObjectService.findMatching(OleDeliverRequestType.class, requestTypeMap);
            if (oleDeliverRequestTypeList != null && (oleDeliverRequestTypeList.size() > 0)) {
                oleDeliverRequestBo.setRequestTypeId(oleDeliverRequestTypeList.get(0).getRequestTypeId());
                oleDeliverRequestBo.setOleDeliverRequestType(oleDeliverRequestTypeList.get(0));
            } else
                return "Invalid Request Type Code";

        }
        if (pickUpLocation != null) {
            Map<String, String> circulationDeskMap = new HashMap<String, String>();
            circulationDeskMap.put("circulationDeskCode", pickUpLocation);
            List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>) businessObjectService.findMatching(OleCirculationDesk.class, circulationDeskMap);
            if (oleCirculationDeskList != null && oleCirculationDeskList.size() > 0) {
                oleDeliverRequestBo.setPickUpLocationId(oleCirculationDeskList.get(0).getCirculationDeskId());
                oleDeliverRequestBo.setPickUpLocationCode(oleCirculationDeskList.get(0).getCirculationDeskCode());
                oleDeliverRequestBo.setOlePickUpLocation(oleCirculationDeskList.get(0));
            } else {
                return "Invalid Pick Up Location";
            }

        }
        try {
            if (itemBarcode == null || (itemBarcode != null && itemBarcode.isEmpty())) {
                return "invalid barcode";
            }
            oleDeliverRequestBo.setItemId(itemBarcode);
            oleDeliverRequestBo.setItemUuid(itemIdentifier);
            Thread.sleep(2000);
            docstoreUtil.isItemAvailableInDocStore(oleDeliverRequestBo);
            if (oleDeliverRequestBo.getItemUuid() == null || (oleDeliverRequestBo.getItemUuid() != null && oleDeliverRequestBo.getItemUuid().trim().isEmpty())) {
                return "invalid barcode";
            } else {
                oleDeliverRequestBo.setRequestCreator("Operator");
                oleDeliverRequestBo.setOperatorCreateId(operatorId);
                oleDeliverRequestBo.setBorrowerId(patronId);
                oleDeliverRequestBo.setItemId(itemBarcode);
                oleDeliverRequestBo.setRequestStatus("1");
                if (!processOperator(operatorId)) {
                    return OLEConstants.INVALID_OPERATOR;
                }
                processRequestType(oleDeliverRequestBo);
                String message = this.patronRecordExpired(oleDeliverRequestBo);
                if (message != null)
                    return message;
                boolean requestRaised = this.isRequestAlreadyRaisedByPatron(oleDeliverRequestBo);
                if (requestRaised)
                    return "Request Already Raised For This Item By The Patron ";
                boolean itemEligible = this.isItemEligible(oleDeliverRequestBo);
                if (!itemEligible)
                    return "Item is not eligible for circulation";
                boolean alreadyLoaned = this.isAlreadyLoaned(oleDeliverRequestBo);
                if (alreadyLoaned)
                    return "Item is currently in loan with the requested borrower";

                boolean valid = false;
                EngineResults engineResult = this.executeEngineResults(oleDeliverRequestBo);
                if (engineResult != null) {
                    List<ResultEvent> allResults = engineResult.getAllResults();
                    if (allResults.size() > 0) {
                        for (Iterator<ResultEvent> resultEventIterator = allResults.iterator(); resultEventIterator.hasNext(); ) {
                            ResultEvent resultEvent = resultEventIterator.next();
                            if (resultEvent.getType().equals(RULE_EVALUATED))
                                valid |= resultEvent.getResult();
                        }
                        if ((oleDeliverRequestBo.getMessage() != null && !oleDeliverRequestBo.getMessage().isEmpty()))
                            return oleDeliverRequestBo.getMessage();
                    }
                }
                OleDeliverRequestBo oleDeliverRequestBo1 = oleDeliverRequestBo;
                if (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2") || oleDeliverRequestBo.getRequestTypeId().equals("3") || oleDeliverRequestBo.getRequestTypeId().equals("4")) {
                    if (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2"))
                        oleDeliverRequestBo1 = this.updateLoanDocument(oleDeliverRequestBo);
                    if (isItemAvailable(oleDeliverRequestBo1)) {
                        return "Recall / Hold type of request can be raised only for the item which is currently in circulation";
                    }
                }
                oleDeliverRequestBo.setOleItem(null);
                this.reOrderQueuePosition(oleDeliverRequestBo1);
                getBusinessObjectService().save(oleDeliverRequestBo1);
                Map<String, String> requestMap = new HashMap<String, String>();
                requestMap.put("borrowerId", patronId);
                requestMap.put("itemId", itemBarcode);
                List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, requestMap);
                String requestId = "";
                if (oleDeliverRequestBoList.size() > 0) {
                    requestId = ":Request Id :" + oleDeliverRequestBoList.get(0).getRequestId();
                }
                LOG.info("Request Raised Succesfully" + requestId);
                oleNoticeBo.setNoticeName(OLEConstants.PICKUP_NOTICE);
                Date pickupDate = addDate(new java.sql.Date(System.currentTimeMillis()), new Integer(oleDeliverRequestBo.getOlePickUpLocation().getOnHoldDays()));
                oleNoticeBo.setNoticeSpecificContent(OLEConstants.PICKUP_NOTICE_START_CONTENT + oleDeliverRequestBo.getOlePickUpLocation().getCirculationDeskPublicName() + OLEConstants.PICKUP_NOTICE_MIDDLE_CONTENT + pickupDate + OLEConstants.PICKUP_NOTICE_FINAL_CONTENT);
                oleNoticeBo.setAuthor(oleDeliverRequestBo.getAuthor() != null ? oleDeliverRequestBo.getAuthor() : "");
                oleNoticeBo.setItemCallNumber(oleDeliverRequestBo.getCallNumber() != null ? oleDeliverRequestBo.getCallNumber() : "");
                oleNoticeBo.setItemId(oleDeliverRequestBo.getItemId() != null ? oleDeliverRequestBo.getItemId() : "");
                oleNoticeBo.setTitle(oleDeliverRequestBo.getTitle() != null ? oleDeliverRequestBo.getTitle() : "");
                OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
                String content = oleDeliverBatchService.getEmailPickUpNotice(oleNoticeBo);
                try {
                    if (!content.trim().equals("")) {
                        OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                        if (oleDeliverRequestBo.getOlePickUpLocation() != null && StringUtils.isNotBlank(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail())) {
                            oleMailer.sendEmail(new EmailFrom(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail()), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.PICKUP_NOTICE_SUBJECT_LINE), new EmailBody(content), true);
                        } else {
                            String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                            if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                                fromAddress = OLEConstants.KUALI_MAIL;
                            }
                            oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.PICKUP_NOTICE_SUBJECT_LINE), new EmailBody(content), true);
                        }
                        LOG.info("Mail send Successfully to " + oleNoticeBo.getPatronEmailAddress());
                        oleDeliverBatchService.getPdfPickUpNotice(oleNoticeBo);
                    } else {
                        LOG.info("Notice Type :" + oleNoticeBo.getNoticeName() + "  " + "Item Barcode : " + oleNoticeBo.getItemId() + " " + "Patron Name :" + oleNoticeBo.getPatronName());
                    }
                } catch (Exception e) {
                    LOG.error(e, e);
                    return "Request Raised Succesfully" + requestId + ".Problem occured while sending notice.";
                }
                return "Request Raised Succesfully" + requestId;
            }
        } catch (Exception e) {
            LOG.error(e, e);
            if (e.getMessage().equalsIgnoreCase("Item barcode does not exist.")) {
                return "Item barcode does not exist.";
            }
            return "Request failed";
        }
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

    public EngineResults executeEngineResults(OleDeliverRequestBo oleDeliverRequestBo) {
        List<OleDeliverRequestBo> recallList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> holdList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> pageList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> requestsByBorrower = new ArrayList<OleDeliverRequestBo>();
        Engine engine = KrmsApiServiceLocator.getEngine();
        EngineResults engineResult = null;
        HashMap<String, Object> agendaValue = new HashMap<String, Object>();
        agendaValue.put(OLEConstants.NAME_NM, OLEConstants.REQUEST_AGENDA_NM);
        List<AgendaBo> agendaBos = (List<AgendaBo>) KRADServiceLocator.getBusinessObjectService().findMatching(AgendaBo.class, agendaValue);
        if (agendaBos != null && agendaBos.size() > 0) {
            AgendaBo agendaBo = agendaBos.get(0);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(OLEConstants.AGENDA_NAME, agendaBo.getName());
            List<MatchBo> matchBos = (List<MatchBo>) KRADServiceLocator.getBusinessObjectService().findMatching(MatchBo.class, map);

            SelectionCriteria selectionCriteria =
                    SelectionCriteria.createCriteria(null, getSelectionContext(agendaBo.getContext().getName()),
                            getAgendaContext(OLEConstants.REQUEST_AGENDA_NM));

            ExecutionOptions executionOptions = new ExecutionOptions();
            executionOptions.setFlag(ExecutionFlag.LOG_EXECUTION, true);

            Facts.Builder factBuilder = Facts.Builder.create();

            String borrowerType = "";
            if (oleDeliverRequestBo.getOlePatron() != null && oleDeliverRequestBo.getOlePatron().getOleBorrowerType() != null) {
                borrowerType = oleDeliverRequestBo.getOlePatron().getOleBorrowerType().getBorrowerTypeCode();
            }
            String itemType = oleDeliverRequestBo.getItemType();

            String requestTypeId = oleDeliverRequestBo.getRequestTypeId();

            String requestType = oleDeliverRequestBo.getRequestTypeCode();

            String location = oleDeliverRequestBo.getShelvingLocation();
            LoanProcessor loanProcessor = getLoanProcessor();
            OleLoanDocument oleLoanDocument = loanProcessor.getOleLoanDocumentUsingItemUUID(oleDeliverRequestBo.getItemUuid());
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            dataCarrierService.addData(OLEConstants.LOANED_DATE, oleLoanDocument != null ? oleLoanDocument.getCreateDate() : null);
            dataCarrierService.addData(OLEConstants.CIRC_POLICY_FOUND, null);
            String borrowerId = oleDeliverRequestBo.getBorrowerId();
            Map<String, String> requestMap = new HashMap<String, String>();
            requestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
            if (requestTypeId != null && (requestTypeId.equals("1") || requestTypeId.equals("2"))) {
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "1");
                recallList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "2");
                recallList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
            } else if (requestTypeId != null && (requestTypeId.equals("3") || requestTypeId.equals("4"))) {
                //  holdList = (List<OleDeliverRequestBo>)getBusinessObjectService().findMatching(OleDeliverRequestBo.class,requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "3");
                holdList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "4");
                holdList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
            } else if (requestTypeId != null && (requestTypeId.equals("5") || requestTypeId.equals("6"))) {
                // pageList = (List<OleDeliverRequestBo>)getBusinessObjectService().findMatching(OleDeliverRequestBo.class,requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "5");
                pageList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "6");
                pageList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
            }
            Map<String, String> requestByBorrower = new HashMap<String, String>();
            requestByBorrower.put(OLEConstants.OleDeliverRequest.BORROWER_ID, borrowerId);
            requestsByBorrower = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestByBorrower);
            HashMap<String, Object> termValues = new HashMap<String, Object>();
            termValues.put(OLEConstants.BORROWER_TYPE, borrowerType);
            termValues.put(OLEConstants.ITEM_TYPE, itemType);
            termValues.put(OLEConstants.LOCATION, location);
            termValues.put(OLEConstants.ITEM_SHELVING, oleDeliverRequestBo.getShelvingLocation());
            termValues.put(OLEConstants.ITEM_COLLECTION, oleDeliverRequestBo.getItemCollection());
            termValues.put(OLEConstants.ITEM_LIBRARY, oleDeliverRequestBo.getItemLibrary());
            termValues.put(OLEConstants.ITEM_CAMPUS, oleDeliverRequestBo.getItemCampus());
            termValues.put(OLEConstants.ITEM_INSTITUTION, oleDeliverRequestBo.getItemInstitution());
            termValues.put(OLEConstants.MAX_NO_OF_RECALL_REQUEST, new Integer(recallList.size()) + 1);
            termValues.put(OLEConstants.MAX_NO_OF_HOLD_REQUEST, new Integer(holdList.size()) + 1);
            termValues.put(OLEConstants.MAX_NO_OF_PAGE_REQUEST, new Integer(pageList.size()) + 1);
            // termValues.put("maxNumberOfRequestByBorrower",requestsByBorrower.size());
            termValues.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, requestTypeId);
            termValues.put(OLEConstants.REQUEST_TYPE, requestType);


            for (Iterator<MatchBo> matchBoIterator = matchBos.iterator(); matchBoIterator.hasNext(); ) {
                MatchBo matchBo = matchBoIterator.next();
                factBuilder.addFact(matchBo.getTermName(), termValues.get((matchBo.getTermName())));
            }
            LOG.info("termValues.toString()" + termValues.toString());
            engineResult = engine.execute(selectionCriteria, factBuilder.build(), executionOptions);
            List<String> errorMessage = (List<String>) engineResult.getAttribute(OLEConstants.ERROR_ACTION);
            java.sql.Date d = (java.sql.Date) engineResult.getAttribute(OLEConstants.REQ_EXPIRATION_DATE);
            Timestamp recallDueDate = (Timestamp) engineResult.getAttribute(OLEConstants.RECALL_DUE_DATE);
            String notice = (String) engineResult.getAttribute(OLEConstants.NOTICE);
            oleDeliverRequestBo.setNoticeType(notice);
            if (recallDueDate != null && oleLoanDocument.getLoanDueDate()!=null) {
                oleLoanDocument.setLoanDueDate(recallDueDate);
                oleDeliverRequestBo.setRecallDueDate(recallDueDate);
                getBusinessObjectService().save(oleLoanDocument);
            }
            oleDeliverRequestBo.setRequestExpiryDate(d);
            StringBuffer failures = new StringBuffer();
            if (errorMessage != null && errorMessage.size() > 0) {
                int i = 1;
                for (String errMsg : errorMessage) {
                    failures.append(i++ + ". " + errMsg + OLEConstants.BREAK);
                }
            }
            if (!failures.toString().isEmpty()) {
                oleDeliverRequestBo.setMessage(failures.toString());
            }
            dataCarrierService.addData(OLEConstants.ERROR_ACTION, null);
        }
        return engineResult;
    }

    /**
     * This method returns SelectionContext using contextName.
     *
     * @param contextName
     * @return Map
     */
    protected Map<String, String> getSelectionContext(String contextName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(NAMESPACE_CODE_SELECTOR, OLEConstants.OLE_NAMESPACE);
        selector.put(NAME_SELECTOR, contextName);
        return selector;
    }

    /**
     * This method returns AgendaContext using agendaName..
     *
     * @param agendaName
     * @return Map
     */
    protected Map<String, String> getAgendaContext(String agendaName) {
        Map<String, String> selector = new HashMap<String, String>();
        selector.put(NAME_SELECTOR, agendaName);
        return selector;
    }

    private OleCirculationDesk getOleCirculationDesk(String circulationDeskId) {
        Map<String, String> circulationDeskMap = new HashMap<String, String>();
        circulationDeskMap.put("circulationDeskId", circulationDeskId);
        List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>) businessObjectService.findMatching(OleCirculationDesk.class, circulationDeskMap);
        if (oleCirculationDeskList != null && oleCirculationDeskList.size() > 0) {
            return oleCirculationDeskList.get(0);
        } else
            return null;
    }

    private RoleService getRoleService() {
        RoleService service = KimApiServiceLocator.getRoleService();
        return service;
    }


}






