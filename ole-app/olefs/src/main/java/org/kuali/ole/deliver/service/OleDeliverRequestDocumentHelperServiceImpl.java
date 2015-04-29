package org.kuali.ole.deliver.service;


import com.itextpdf.text.Document;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.asr.ASRConstants;
import org.kuali.asr.service.ASRHelperServiceImpl;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleDeliverBatchServiceImpl;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.batch.OleSms;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.impl.OLEDeliverNoticeHelperServiceImpl;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.bo.OleLocationLevel;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.ingest.pojo.MatchBo;
import org.kuali.ole.ncip.bo.OLEPlaceRequest;
import org.kuali.ole.ncip.converter.OLEPlaceRequestConverter;
import org.kuali.ole.service.OleCirculationPolicyService;
import org.kuali.ole.service.OleCirculationPolicyServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.dao.impl.PersistenceDaoOjb;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.engine.*;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/24/12
 * Time: 10:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverRequestDocumentHelperServiceImpl {
    private static final Logger LOG = Logger.getLogger(OleDeliverRequestDocumentHelperServiceImpl.class);
    private static final String NAMESPACE_CODE_SELECTOR = "namespaceCode";
    private static final String NAME_SELECTOR = "name";
    private final static String RULE_EVALUATED = "Rule Evaluated";
    private final static String ROUTED_EXTERNAL = "Routed External";
    private BusinessObjectService businessObjectService;
    private int queuePosition = 0;
    private LoanProcessor loanProcessor;
    private DocstoreUtil docstoreUtil;
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor;
    private OLEDeliverNoticeHelperService oleDeliverNoticeHelperService ;
    private DocumentService documentService = GlobalResourceLoader.getService(OLEConstants.DOCUMENT_HEADER_SERVICE);
    private PersonService personService = KimApiServiceLocator.getPersonService();
    private OleCirculationPolicyService oleCirculationPolicyService;

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

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }


    public void setLoanProcessor(LoanProcessor loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    public void setDocstoreUtil(DocstoreUtil docstoreUtil) {
        this.docstoreUtil = docstoreUtil;
    }

    public void setDocstoreClientLocator(DocstoreClientLocator docstoreClientLocator) {
        this.docstoreClientLocator = docstoreClientLocator;
    }

    public DocstoreUtil getDocstoreUtil() {
        if(docstoreUtil == null){
            docstoreUtil = SpringContext.getBean(DocstoreUtil.class);
        }
        return docstoreUtil;
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

    public LoanProcessor getLoanProcessor() {
        if(loanProcessor == null){
            loanProcessor = SpringContext.getBean(LoanProcessor.class);
        }
        return loanProcessor;
    }

    public ItemOlemlRecordProcessor getItemOlemlRecordProcessor() {
        if(itemOlemlRecordProcessor == null){
            itemOlemlRecordProcessor = SpringContext.getBean(ItemOlemlRecordProcessor.class);
        }
        return itemOlemlRecordProcessor;
    }
    public OLEDeliverNoticeHelperService getOleDeliverNoticeHelperService() {
        if(oleDeliverNoticeHelperService ==null){
            oleDeliverNoticeHelperService = SpringContext.getBean(OLEDeliverNoticeHelperServiceImpl.class);
        }
        return oleDeliverNoticeHelperService;
    }

    public void setOleDeliverNoticeHelperService(OLEDeliverNoticeHelperService oleDeliverNoticeHelperService) {
        this.oleDeliverNoticeHelperService = oleDeliverNoticeHelperService;
    }


    /**
     * Gets the oleCirculationPolicyService attribute.
     *
     * @return Returns the oleCirculationPolicyService
     */
    public OleCirculationPolicyService getOleCirculationPolicyService() {
        if (null == oleCirculationPolicyService) {
            oleCirculationPolicyService = SpringContext.getBean(OleCirculationPolicyServiceImpl.class);
        }
        return oleCirculationPolicyService;
    }
    /**
     * This method is used to change the request type based on selection of  pick up  location
     *
     * @param oleDeliverRequestBo
     * @return oleDeliverRequestBo
     */
    public OleDeliverRequestBo processRequestType(OleDeliverRequestBo oleDeliverRequestBo) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Inside processRequestType for the Request id :" + oleDeliverRequestBo.getRequestId());
        }
        oleDeliverRequestBo = processPatron(oleDeliverRequestBo);
        oleDeliverRequestBo = processItem(oleDeliverRequestBo);
        oleDeliverRequestBo = processRequestTypeByPickUpLocation(oleDeliverRequestBo);
        return oleDeliverRequestBo;
    }

    private OleDeliverRequestBo processRequestTypeByPickUpLocation(OleDeliverRequestBo oleDeliverRequestBo){
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
            List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
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
                List<OlePatronDocument> oleProxyPatronDocumentList = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, proxyPatronMap);
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
            List<OleProxyPatronDocument> proxyPatronDocuments = (List<OleProxyPatronDocument>) getBusinessObjectService().findMatching(OleProxyPatronDocument.class, proxyMap);
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
        List<OleDeliverRequestBo> deliverRequestBos = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
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
        List<OleLoanDocument> loanDocuments = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
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
            itemStatuslist = getLoanProcessor().getParameter(OLEConstants.RECALL_DELIVERY_ITEM_STATUS);
        } else if (requestTypeCode.equals("Recall/Hold Request")) {
            itemStatuslist = getLoanProcessor().getParameter(OLEConstants.RECALL_HOLD_ITEM_STATUS);
        } else if (requestTypeCode.equals("Hold/Delivery Request")) {
            itemStatuslist = getLoanProcessor().getParameter(OLEConstants.HOLD_DELIVERY_ITEM_STATUS);
        } else if (requestTypeCode.equals("Hold/Hold Request")) {
            itemStatuslist = getLoanProcessor().getParameter(OLEConstants.HOLD_HOLD_ITEM_STATUS);
        } else if (requestTypeCode.equals("Page/Delivery Request")) {
            itemStatuslist = getLoanProcessor().getParameter(OLEConstants.PAGE_DELIVERY_ITEM_STATUS);
        } else if (requestTypeCode.equals("Page/Hold Request")) {
            itemStatuslist = getLoanProcessor().getParameter(OLEConstants.PAGE_HOLD_ITEM_STATUS);
        } else if (requestTypeCode.equals("Copy Request")) {
            itemStatuslist = getLoanProcessor().getParameter(OLEConstants.COPY_REQUEST_ITEM_STATUS);
        } else if (requestTypeCode.equals("ASR Request")) {
            itemStatuslist = getLoanProcessor().getParameter(OLEConstants.ASR_REQUEST_ITEM_STATUS);
        }else {
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
        List<OleLoanDocument> loanItemDocuments = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, loanItemMap);
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

        OlePatronDocument olePatronDocument=oleDeliverRequestBo.getOlePatron();
        if(olePatronDocument==null){
            Map<String, String> patronMap = new HashMap<String, String>();
            patronMap.put(OLEConstants.OleDeliverRequest.PATRON_ID, oleDeliverRequestBo.getBorrowerId());
            List<OlePatronDocument> olePatronDocuments = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
            if (olePatronDocuments != null && olePatronDocuments.size() > 0) {
               olePatronDocument = olePatronDocuments.get(0);
            }
        }
        if(olePatronDocument!=null){
            SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OleDeliverRequest.DATE_FORMAT);
            Date expirationDate = olePatronDocument.getExpirationDate();
            Date activationDate = olePatronDocument.getActivationDate();
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
        List<OleDeliverRequestBo> deliverRequestBos = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
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
        List<OleDeliverRequestBo> deliverRequestBos = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        if (deliverRequestBos.size() > 0) {
            exist = true;
        }
        return exist;
    }


    /**
     * This method is used to cancel the request document using ASR/NCIP.
     *
     * @param oleDeliverRequestBo
     */
    public void cancelDocument(OleDeliverRequestBo oleDeliverRequestBo){
        try{
        List<OleNoticeBo> oleNoticeBos = cancelRequestForItem(oleDeliverRequestBo.getItemUuid(), oleDeliverRequestBo.getBorrowerId());
        ASRHelperServiceImpl asrHelperService = new ASRHelperServiceImpl();
        LOG.debug("Inside cancelDocument");
        Map<String, String> itemMap = new HashMap<String, String>();
        itemMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_ID, oleDeliverRequestBo.getRequestId());
        getBusinessObjectService().deleteMatching(OleDeliverRequestBo.class, requestMap);
        List<OleDeliverRequestBo> oleDeliverRequestDocumentsList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, itemMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        getBusinessObjectService().delete(oleDeliverRequestDocumentsList);
        int queuePosition = 1;
        for (int i = 0; i < oleDeliverRequestDocumentsList.size(); i++) {
            oleDeliverRequestDocumentsList.get(i).setBorrowerQueuePosition(queuePosition);
            queuePosition = queuePosition + 1;
        }
        getBusinessObjectService().save(oleDeliverRequestDocumentsList);
        asrHelperService.deleteASRTypeRequest(oleDeliverRequestBo.getRequestId());
        sendCancelNotice(oleNoticeBos);
        }catch (Exception e){
            LOG.error("Cancellation of Request"+e.getMessage());
        }
    }

    /**
     * Build the cancellation notice template.
     * @param itemUuid
     * @param patronId
     * @return
     * @throws Exception
     */
    public List<OleNoticeBo> cancelRequestForItem(String itemUuid,String patronId) throws Exception{
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.ITEM_UUID,itemUuid);
        requestMap.put(OLEConstants.OleDeliverRequest.BORROWER_ID,patronId);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        List<OleNoticeBo> oleNoticeBos = new ArrayList<OleNoticeBo>();
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(oleDeliverRequestBoList)){
            OleDeliverRequestBo oleDeliverRequestBo = oleDeliverRequestBoList.get(0);
            //cancelDocument(oleDeliverRequestBo);
            OleItemSearch itemSearch = getDocstoreUtil().getOleItemSearchList(itemUuid);
            EntityTypeContactInfoBo entityTypeContactInfoBo = oleDeliverRequestBo.getOlePatron().getEntity().getEntityTypeContactInfos().get(0);

            OleNoticeBo oleNoticeBo = new OleNoticeBo();
            oleNoticeBo.setNoticeName(OLEConstants.CANCELLATION_NOTICE);
            oleNoticeBo.setPatronName(oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getFirstName() + " " + oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getLastName());
            oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
            oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
            oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
            oleNoticeBo.setAuthor(itemSearch.getAuthor() != null ? itemSearch.getAuthor() : "");
            oleNoticeBo.setItemCallNumber(itemSearch.getCallNumber() != null ? itemSearch.getCallNumber() : "");
            if(itemSearch.getShelvingLocation()!=null && itemSearch.getShelvingLocation().toString().contains("/")){
                String [] location=itemSearch.getShelvingLocation().split("/");
                if(location!=null && location.length>0)
                    oleNoticeBo.setItemShelvingLocation(location[1]!=null ? location[1] : "");
            } else {
                oleNoticeBo.setItemShelvingLocation("");
            }
            oleNoticeBo.setItemId(itemSearch.getItemBarCode() != null ? itemSearch.getItemBarCode() : "");
            oleNoticeBo.setTitle(itemSearch.getTitle() != null ? itemSearch.getTitle() : "");
            oleNoticeBo.setOleItem(getItem(oleDeliverRequestBo.getItemUuid()));
            if (oleNoticeBo.getPatronEmailAddress() != null && !oleNoticeBo.getPatronEmailAddress().isEmpty()) {
               oleNoticeBos.add(oleNoticeBo);
            }
        }
       return oleNoticeBos;
    }


    /**
     * send the email to respective patron.
     * @param oleNoticeBos
     * @throws Exception
     */
    public void sendCancelNotice(List<OleNoticeBo> oleNoticeBos)throws Exception{
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        for(OleNoticeBo oleNoticeBo : oleNoticeBos){
                List list = oleDeliverBatchService.getNoticeForPatron(oleNoticeBos);
                String content = list.toString();
                content = content.replace('[', ' ');
                content = content.replace(']', ' ');
                if (!content.trim().equals("")) {
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    String replyToEmail = getLoanProcessor().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
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




    public void cancelPendingRequestForClaimsReturnedItem(String itemUuid) throws Exception {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.ITEM_UUID, itemUuid);
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBoList) {
            cancelDocument(oleDeliverRequestBo);
            /*OleItemSearch itemSearch = getDocstoreUtil().getOleItemSearchList(itemUuid);
            EntityTypeContactInfoBo entityTypeContactInfoBo = oleDeliverRequestBo.getOlePatron().getEntity().getEntityTypeContactInfos().get(0);
            List<OleNoticeBo> oleNoticeBos = new ArrayList<OleNoticeBo>();
            OleNoticeBo oleNoticeBo = new OleNoticeBo();
            oleNoticeBo.setNoticeName(OLEConstants.CANCELLATION_NOTICE);
            oleNoticeBo.setPatronName(oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getFirstName() + " " + oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getLastName());
            oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
            oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
            oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
            oleNoticeBo.setAuthor(itemSearch.getAuthor() != null ? itemSearch.getAuthor() : "");
            oleNoticeBo.setItemCallNumber(itemSearch.getCallNumber() != null ? itemSearch.getCallNumber() : "");
            if(itemSearch.getShelvingLocation()!=null && itemSearch.getShelvingLocation().toString().contains("/")){
                String [] location=itemSearch.getShelvingLocation().split("/");
                if(location!=null && location.length>0)
                    oleNoticeBo.setItemShelvingLocation(location[1]!=null ? location[1] : "");
            } else {
                oleNoticeBo.setItemShelvingLocation("");
            }
            oleNoticeBo.setItemId(itemSearch.getItemBarCode() != null ? itemSearch.getItemBarCode() : "");
            oleNoticeBo.setTitle(itemSearch.getTitle() != null ? itemSearch.getTitle() : "");
            oleNoticeBo.setOleItem(getItem(oleDeliverRequestBo.getItemUuid()));
            String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
            if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                fromAddress = OLEConstants.KUALI_MAIL;
            }
            if (oleNoticeBo.getPatronEmailAddress() != null && !oleNoticeBo.getPatronEmailAddress().isEmpty()) {
                oleNoticeBos.add(oleNoticeBo);
                List list = oleDeliverBatchService.getNoticeForPatron(oleNoticeBos);
                String content = list.toString();
                content = content.replace('[', ' ');
                content = content.replace(']', ' ');
                if (!content.trim().equals("")) {
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.CANCELLATION_NOTICE), new EmailBody(content), true);
                }
            }*/
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
            OleItemSearch itemSearchList = getDocstoreUtil().getOleItemSearchList(oleDeliverRequestBo.getItemUuid());
            try {
                //org.kuali.ole.docstore.common.document.Item item=getDocstoreClientLocator().getDocstoreClient().retrieveItem( oleDeliverRequestBo.getItemUuid());


                if (itemSearchList != null) {
                    oleItemSearch = itemSearchList;
                    oleDeliverRequestBo.setTitle(itemSearchList.getTitle());
                    oleDeliverRequestBo.setAuthor(itemSearchList.getAuthor());
                    oleDeliverRequestBo.setCallNumber(itemSearchList.getCallNumber());
                    oleDeliverRequestBo.setItemType(itemSearchList.getItemType());
                    oleDeliverRequestBo.setItemLocation(itemSearchList.getShelvingLocation());
                    oleDeliverRequestBo.setShelvingLocation(itemSearchList.getShelvingLocation());
                    oleDeliverRequestBo.setCopyNumber(itemSearchList.getCopyNumber());
                    oleDeliverRequestBo.setItemStatus(itemSearchList.getItemStatus());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String itemXml = getLoanProcessor().getItemXML(oleDeliverRequestBo.getItemUuid());
                Item oleItem = getLoanProcessor().getItemPojo(itemXml);
                if (oleItemSearch == null) {
                    oleItemSearch = new OleItemSearch();
                    oleItemSearch.setCopyNumber(oleItem.getCopyNumber());
                    if (oleItem.getItemStatus() != null) {
                        oleItemSearch.setItemStatus(oleItem.getItemStatus().getCodeValue());
                    }
                    oleItemSearch.setShelvingLocation(getShelvingLocation(oleItem.getLocation()));
                    oleDeliverRequestBo.setCopyNumber(oleItem.getCopyNumber());

                    if (oleItem.getItemStatus() != null) {
                        oleDeliverRequestBo.setItemStatus(oleItem.getItemStatus().getCodeValue());
                    }
                    oleDeliverRequestBo.setShelvingLocation(oleItemSearch.getShelvingLocation());
                }
                // Map docStoreDetails = loanProcessor.getItemDetails(oleDeliverRequestBo.getItemId());
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
        List<OleInstanceItemType> oleInstanceItemTypeList = (List<OleInstanceItemType>) getBusinessObjectService().findMatching(OleInstanceItemType.class, itemMap);
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
    private String getShelvingLocation(Location oleLocation) {

        LOG.debug("Inside getShelvingLocation");
        String locationLevelName = "";
        if (oleLocation != null) {
            LocationLevel locationLevel =
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
        List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
        if (oleLoanDocumentList != null && oleLoanDocumentList.size() > 0 && oleLoanDocumentList.get(0) != null && oleLoanDocumentList.get(0).getOleRequestId() == null) {
            if (oleLoanDocumentList.get(0).getLoanId() != null) {
                oleLoanDocumentList.get(0).setOleRequestId(oleDeliverRequestBo.getRequestId());
                oleDeliverRequestBo.setLoanTransactionRecordNumber(oleLoanDocumentList.get(0).getLoanId());
            }
           businessObjectService.save(oleLoanDocumentList.get(0));

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
        Map<String,String> titleLevelRequestMap = new HashMap<String,String>();
        List<OleDeliverRequestBo> titleLevelRequestBoList = null;
        requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_ID, requestId);
        OleDeliverRequestBo oleDeliverRequestBo = createRequestHistoryRecord(requestId, operatorId, loanTransactionNumber);
        if(oleDeliverRequestBo!=null){
        if(oleDeliverRequestBo.getRequestLevel().equalsIgnoreCase("Item Level")){
        Map<String, String> itemMap = new HashMap<String, String>();
        itemMap.put(OLEConstants.ITEM_UUID, itemUUID);
        List<OleDeliverRequestBo> oleDeliverRequestDocumentsList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, itemMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        getBusinessObjectService().delete(oleDeliverRequestDocumentsList);
        int queuePosition = 1;
        for (int i = 0; i < oleDeliverRequestDocumentsList.size(); i++) {
            oleDeliverRequestDocumentsList.get(i).setBorrowerQueuePosition(queuePosition);
            queuePosition = queuePosition + 1;
        }
        getBusinessObjectService().save(oleDeliverRequestDocumentsList);
        }else if(oleDeliverRequestBo.getRequestLevel().equalsIgnoreCase("Title Level")){
           requestMap = new HashMap<String,String>();
           requestMap.put("bibId",oleDeliverRequestBo.getBibId());
           requestMap.put("borrowerBarcode",oleDeliverRequestBo.getBorrowerBarcode());
           List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>)getBusinessObjectService().findMatching(OleDeliverRequestBo.class,requestMap);
           if(oleDeliverRequestBoList.size()>0){
               for(OleDeliverRequestBo oleDeliverRequestBo1 : oleDeliverRequestBoList){
                   titleLevelRequestMap.put(OLEConstants.ITEM_UUID,oleDeliverRequestBo1.getItemUuid());
                   getBusinessObjectService().delete(oleDeliverRequestBo1);
                   titleLevelRequestBoList = (List<OleDeliverRequestBo>)getBusinessObjectService().findMatching(OleDeliverRequestBo.class,titleLevelRequestMap);
                   int queuePosition = 1;
                   for (int i = 0; i < titleLevelRequestBoList.size(); i++) {
                       titleLevelRequestBoList.get(i).setBorrowerQueuePosition(queuePosition);
                       queuePosition = queuePosition + 1;
                   }
                   getBusinessObjectService().save(titleLevelRequestBoList);
               }
           }
        }
        }

    }



    public void deleteTitleLevelIndividualRequests(OleDeliverRequestBo oleDeliverRequestBo){
        Map<String, String> requestMap = new HashMap<String, String>();
        Map<String,String> titleLevelRequestMap = new HashMap<String,String>();
        List<OleDeliverRequestBo> titleLevelRequestBoList = null;
        requestMap = new HashMap<String,String>();
        requestMap.put("bibId",oleDeliverRequestBo.getBibId());
        requestMap.put("borrowerBarcode",oleDeliverRequestBo.getBorrowerBarcode());
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>)getBusinessObjectService().findMatching(OleDeliverRequestBo.class,requestMap);
        if(oleDeliverRequestBoList.size()>0){
            for(OleDeliverRequestBo oleDeliverRequestBo1 : oleDeliverRequestBoList){
                titleLevelRequestMap.put(OLEConstants.ITEM_UUID,oleDeliverRequestBo1.getItemUuid());
                getBusinessObjectService().delete(oleDeliverRequestBo1);
                titleLevelRequestBoList = (List<OleDeliverRequestBo>)getBusinessObjectService().findMatching(OleDeliverRequestBo.class,titleLevelRequestMap);
                int queuePosition = 1;
                for (int i = 0; i < titleLevelRequestBoList.size(); i++) {
                    titleLevelRequestBoList.get(i).setBorrowerQueuePosition(queuePosition);
                    queuePosition = queuePosition + 1;
                }
                getBusinessObjectService().save(titleLevelRequestBoList);
            }
        }
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
            String itemXml = getLoanProcessor().getItemXML(itemUUID);
            Item oleItem = getLoanProcessor().getItemPojo(itemXml);
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
            OleItemSearch itemSearchList = getDocstoreUtil().getOleItemSearchList(itemUUID);
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
        Map<String, String> asrRequestMap = new HashMap<String, String>();
        asrRequestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        asrRequestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "9");
        List<OleDeliverRequestBo> recallList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, recallRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> recallHoldList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, recallHoldRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> holdList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, holdRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> holdHoldList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, holdHoldRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> pageList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, pageRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> pageHoldList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, pageHoldRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> copyList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, copyRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> inTransitList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, inTransitRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
        List<OleDeliverRequestBo> asrList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatchingOrderBy(OleDeliverRequestBo.class, asrRequestMap, OLEConstants.OleDeliverRequest.QUEUE_POSITION, true);
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
        }else if (oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains(OLEConstants.OleDeliverRequest.ASR_REQUEST)) {
            asrList.add(oleDeliverRequestBo);
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
            if (orderQueue.get(i).equals(OLEConstants.OleDeliverRequest.ASR_REQUEST) && asrList.size() > 0) {
                for (int x = 0; x < asrList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(asrList.get(x));
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
        getBusinessObjectService().save(finalList);
        this.queuePosition = 0;
        oleDeliverRequestBo.setRequestId(KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("OLE_DLVR_RQST_S").toString());
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
            search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, oleDeliverRequestBo.getItemId()), ""));


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
            LOG.error("Exception", e);
            //To change body of catch statement use File | Settings | File Templates.
        }
        // itemMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo.getItemUuid());
        OleItemSearch itemSearchList = getDocstoreUtil().getOleItemSearchList(oleDeliverRequestBo.getItemUuid());
        if (itemSearchList != null) {
            oleDeliverRequestBo.setTitle(itemSearchList.getTitle());
            oleDeliverRequestBo.setAuthor(itemSearchList.getAuthor());
            oleDeliverRequestBo.setCallNumber(itemSearchList.getCallNumber());
            oleDeliverRequestBo.setItemType(itemSearchList.getItemType());
        }
        try {
            // Map docStoreDetails = loanProcessor.getItemDetailsByUUID(oleDeliverRequestBo.getItemId());
            String itemXml = getLoanProcessor().getItemXML(oleDeliverRequestBo.getItemUuid());
            Item oleItem = getLoanProcessor().getItemPojo(itemXml);

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
        List<OleLoanDocument> loanDocuments = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
        OleLoanDocument oleLoanDocument = loanDocuments.get(0);
        Map<String, String> patronMap = new HashMap<String, String>();
        patronMap.put(OLEConstants.OleDeliverRequest.PATRON_ID, oleLoanDocument.getPatronId());
        List<OlePatronDocument> patronDocumentList = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        Item item;
        if (patronDocumentList.size() > 0) {
            OlePatronDocument olePatronDocument = patronDocumentList.get(0);
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);
            oleNoticeBo.setPatronName(olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " +olePatronDocument.getEntity().getNames().get(0).getLastName());
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
        oleNoticeBo.setOriginalDueDate(oleDeliverRequestBo.getOriginalDueDate());
        oleNoticeBo.setNewDueDate(oleDeliverRequestBo.getNewDueDate());
        String volumeNumber = oleDeliverRequestBo.getEnumeration() != null ? oleDeliverRequestBo.getEnumeration() : "";
        String chronology=oleDeliverRequestBo.getChronology()!=null ?oleDeliverRequestBo.getChronology():"";
        String copyNumber = oleDeliverRequestBo.getCopyNumber()!=null ? oleDeliverRequestBo.getCopyNumber() : "";
        oleNoticeBo.setVolumeIssueCopyNumber(volumeNumber + "/" +chronology+"/"+ copyNumber);
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
                    String replyToEmail = getLoanProcessor().getReplyToEmail(oleNoticeBo.getItemShelvingLocation());
                    if (replyToEmail != null) {
                        oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(content), true);
                    } else {
                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(content), true);
                    }
                } else {
                    for (OleNoticeBo oleNoticeBo1 : oleNoticeBos) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Notice Type :" + oleNoticeBo1.getNoticeName() + "  " + "Item Barcode : " + oleNoticeBo1.getItemId() + " " + "Patron Name :" + oleNoticeBo1.getPatronName());
                        }
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                }
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
            if (LOG.isDebugEnabled()) {
                LOG.debug("Recall Notice Pdf generated for item Id" + oleNoticeBo.getItemId());
            }
        }
        oleDeliverRequestBo = (OleDeliverRequestBo) ObjectUtils.deepCopy(oleDeliverRequestBo);
        oleDeliverRequestBo.setRecallNoticeSentDate(new java.sql.Date(System.currentTimeMillis()));
        return oleDeliverRequestBo;
    }


    public List<String> getList(String[] arrays){
        List<String> resultList = new ArrayList<>();
        if(arrays != null && arrays.length>0){
            for(String arrayObj : arrays){
                resultList.add(arrayObj);
            }
        }
       return resultList;
    }

    public Map<String,String> getMap(String[] arrays){
        Map<String,String> resultMap = new HashMap<String,String>();
        if(arrays != null && arrays.length>0){
            for(String arrayObj : arrays){
                resultMap.put(arrayObj,arrayObj);
            }
        }
        return resultMap;
    }


    public void generateOnHoldNotice() throws Exception {

        List<OleDeliverRequestBo> finalDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb)SpringContext.getBean("oleLoanDao");
        String requestTypeParameter= getLoanProcessor().getParameter(OLEConstants.ON_HOLD_NOTICE_REQUEST_TYPE);
        String onHoldItemStatusParameter = getLoanProcessor().getParameter(OLEConstants.ON_HOLD_NOTICE_ITEM_STATUS);
        List<String> requestTypeIds = new ArrayList<String>();
        List<String> requestTypeCodes = new ArrayList<String>();
        Map<String,String> itemStatuses = new HashMap<String,String>();
        if(requestTypeParameter!=null && !requestTypeParameter.trim().isEmpty()){
            String[] requestType = requestTypeParameter.split(";");
            requestTypeCodes = getList(requestType);
            requestTypeIds = oleLoanDocumentDaoOjb.getRequestTypeIdsForHoldNotice(requestTypeCodes);
        }
        if(onHoldItemStatusParameter!=null && !onHoldItemStatusParameter.trim().isEmpty()){
            String[] itemStatus =  onHoldItemStatusParameter.split(";");
            itemStatuses = getMap(itemStatus);
        }
        Collection oleDeliverRequestBoList = oleLoanDocumentDaoOjb.getHoldRequests(requestTypeIds);
        OleDeliverRequestBo oleDeliverRequestBo;
        Set<String> circulationDeskIds = new HashSet<>();
        List<OleNoticeBo> noticesList = new ArrayList<OleNoticeBo>();
        EntityTypeContactInfoBo entityTypeContactInfoBo;
        OleNoticeBo oleNoticeBo;
        Item item;
        boolean firstTime = true;
        String patronId ="";
        String noticeTypeParam = getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.ONHOLD_NOTICE_TYPE);
        for (Object obj : oleDeliverRequestBoList) {
            OleDeliverRequestBo deliverRequestBo = (OleDeliverRequestBo) obj;
            if(firstTime){
                patronId= deliverRequestBo.getBorrowerId();
                firstTime = false;
            }
            if (getDocstoreUtil().isItemAvailableInDocStore(deliverRequestBo)) {
                item = deliverRequestBo.getOleItem();
                OlePatronDocument olePatronDocument = deliverRequestBo.getOlePatron();
                if (item != null && item.getItemStatus() != null && item.getItemStatus().getCodeValue() != null  && deliverRequestBo.getOnHoldNoticeSentDate() == null) {
                    if(itemStatuses.containsKey(item.getItemStatus().getCodeValue())){
                    entityTypeContactInfoBo = deliverRequestBo.getOlePatron().getEntity().getEntityTypeContactInfos().get(0);
                    oleNoticeBo = new OleNoticeBo();
                    oleNoticeBo.setAuthor(deliverRequestBo.getAuthor());
                    oleNoticeBo.setCirculationDeskAddress("");
                    oleNoticeBo.setCirculationDeskName("");
                    oleNoticeBo.setCirculationDeskEmailAddress("");
                    oleNoticeBo.setCirculationDeskPhoneNumber("");
                    oleNoticeBo.setPatronName(deliverRequestBo.getOlePatron().getEntity().getNames().get(0).getFirstName() + " " + deliverRequestBo.getOlePatron().getEntity().getNames().get(0).getLastName());
                    oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setItemCallNumber(deliverRequestBo.getCallNumber() != null ? deliverRequestBo.getCallNumber() : "");
                    oleNoticeBo.setItemShelvingLocation(deliverRequestBo.getShelvingLocation() != null ? deliverRequestBo.getShelvingLocation() : "");
                    oleNoticeBo.setItemId(deliverRequestBo.getItemId() != null ? deliverRequestBo.getItemId() : "");
                    oleNoticeBo.setTitle(deliverRequestBo.getTitle() != null ? deliverRequestBo.getTitle() : "");
                    oleNoticeBo.setOleItem(item);
                    oleNoticeBo.setOlePatron(deliverRequestBo.getOlePatron());
                    oleNoticeBo.setVolumeNumber(item.getVolumeNumber() != null ? item.getVolumeNumber() : "");
                    oleNoticeBo.setNewDueDate(new Date());
                    oleNoticeBo.setOriginalDueDate(new Date());
                    oleNoticeBo.setNoticeName(OLEConstants.NOTICE_ONHOLD);
                    oleNoticeBo.setCirculationDeskName(deliverRequestBo.getOlePickUpLocation().getCirculationDeskPublicName());
                    oleNoticeBo.setCirculationDeskReplyToEmail(deliverRequestBo.getOlePickUpLocation().getReplyToEmail());
                    String circulationDeskId = deliverRequestBo.getPickUpLocationId();
                    int noDays = 0;
                    Map<String, String> mapCirculationDesk = new HashMap<String, String>();
                    mapCirculationDesk.put(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_ID, circulationDeskId);
                    List<OleCirculationDesk> oleCirculationDesks = (List<OleCirculationDesk>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDesk.class, mapCirculationDesk);
                    if (oleCirculationDesks.size() > 0) {
                        OleCirculationDesk oleCirculationDesk = oleCirculationDesks.get(0);
                        noDays = Integer.parseInt(oleCirculationDesk.getOnHoldDays());
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, noDays);
                    Date date = calendar.getTime();
                    oleNoticeBo.setExpiredOnHoldDate(date);
                    String maxNumOfDays = deliverRequestBo.getOlePickUpLocation().getOnHoldDays() != null ? deliverRequestBo.getOlePickUpLocation().getOnHoldDays() : getLoanProcessor().getParameter(OLEConstants.MAX_NO_OF_DAYS_ON_HOLD);
                    Integer maxNumberOfDaysOnHold = new Integer(maxNumOfDays);
                    oleNoticeBo.setOnHoldDueDate(dateAdd(deliverRequestBo.getCreateDate(), maxNumberOfDaysOnHold));
                    String noticeContent = getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.ONHOLD_BODY);
                    oleNoticeBo.setNoticeSpecificContent(noticeContent);
                    String agendaName = OLEConstants.BATCH_PROGRAM_AGENDA;
                    HashMap<String, Object> termValues = new HashMap<String, Object>();
                    OleCirculationDesk oleCirculationDesk = deliverRequestBo.getOlePickUpLocation();
                    String deskLocation = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskCode() : "";
                    String deskLocationName = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : "";
                    termValues.put(OLEConstants.BORROWER_TYPE, olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                    termValues.put(OLEConstants.DESK_LOCATION, deskLocation);
                    termValues.put(OLEConstants.NOTICE, OLEConstants.NOTICE_ONHOLD);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("termValues.toString()" + termValues.toString());
                    }
                    EngineResults engineResults = loanProcessor.getEngineResults(agendaName, termValues);
                    String noticeType = (String) engineResults.getAttribute(OLEConstants.NOTICE_TYPE);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("**************" + noticeType);
                    }
                    noticeType = noticeType != null ? noticeType : noticeTypeParam;
                    oleNoticeBo.setNoticeType(noticeType);
                    if(!patronId.equalsIgnoreCase(deliverRequestBo.getBorrowerId()) && noticesList.size()>0){
                        String replyToEmail = null;
                        if (circulationDeskIds.size() == 1) {
                            OleCirculationDesk circulationDesk = getLoanProcessor().getOleCirculationDesk(circulationDeskIds.iterator().next());
                            if (circulationDesk != null&& StringUtils.isNotBlank(circulationDesk.getReplyToEmail())) {
                                replyToEmail = circulationDesk.getReplyToEmail();
                            }
                        }
                        generateNoticesBasedOnNoticeType(noticesList,OLEConstants.NOTICE_ONHOLD, replyToEmail);
                        patronId = deliverRequestBo.getBorrowerId();
                        circulationDeskIds = new HashSet<>();
                        noticesList =  new ArrayList<>();
                    }
                        if (deliverRequestBo.getOlePickUpLocation() != null && StringUtils.isNotBlank(deliverRequestBo.getOlePickUpLocation().getCirculationDeskId())) {
                            circulationDeskIds.add(deliverRequestBo.getOlePickUpLocation().getCirculationDeskId());
                        }
                    noticesList.add(oleNoticeBo);
                    // To do send Notice
                    deliverRequestBo.setOleItem(null);
                    oleDeliverRequestBo = (OleDeliverRequestBo) ObjectUtils.deepCopy(deliverRequestBo);
                    oleDeliverRequestBo.setOnHoldNoticeSentDate(new java.sql.Date(System.currentTimeMillis()));
                    finalDeliverRequestBoList.add(oleDeliverRequestBo);
                }}
            }
        }
        if(noticesList.size()>0){
            String replyToEmail = null;
            if (circulationDeskIds.size() == 1) {
                OleCirculationDesk circulationDesk = getLoanProcessor().getOleCirculationDesk(circulationDeskIds.iterator().next());
                if (circulationDesk != null && StringUtils.isNotBlank(circulationDesk.getReplyToEmail())) {
                    replyToEmail = circulationDesk.getReplyToEmail();
                }
            }
            generateNoticesBasedOnNoticeType(noticesList,OLEConstants.NOTICE_ONHOLD, replyToEmail);
        }
        getBusinessObjectService().save(finalDeliverRequestBoList);
    }

    private void generateNoticesBasedOnNoticeType(List<OleNoticeBo> noticesList,String noticeName, String replyToEmail) throws Exception{
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        OleNoticeBo oleNoticeBo = noticesList.get(0);
        String noticeType = oleNoticeBo.getNoticeType();
        if (noticeType != null && (noticeType.equalsIgnoreCase(OLEConstants.EMAIL) || noticeType.equalsIgnoreCase(OLEConstants.MAIL))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("noticesList.size()" + noticesList.size());
            }
            oleDeliverBatchService.getPdfNoticeForPatron(noticesList);
        }
        if (noticeType != null && noticeType.equalsIgnoreCase(OLEConstants.EMAIL)) {
            if (oleNoticeBo.getPatronEmailAddress() != null && !oleNoticeBo.getPatronEmailAddress().isEmpty()) {
                List list = oleDeliverBatchService.getNoticeForPatron(noticesList);
                String content = list.toString();
                content = content.replace('[', ' ');
                content = content.replace(']', ' ');
                if (!content.trim().equals("")) {
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    if (replyToEmail != null) {
                        oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(content), true);
                    } else {
                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(content), true);
                    }
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Notice Details" + noticesList);
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Mail send successfully to " + oleNoticeBo.getPatronEmailAddress());
                }
            }
        } else if (noticeType != null && noticeType.equalsIgnoreCase(OLEConstants.SMS)) {
            Map map = oleDeliverBatchService.getSMSForPatron(noticesList);
            HashMap sms = (HashMap) map.get(noticeName);
            Iterator it = sms.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                String patronPhoneNumber = oleNoticeBo.getPatronPhoneNumber();
                OleSms oleSms = new OleSms();
                oleSms.sendSms("", patronPhoneNumber, (String) pairs.getValue());
            }
        }
    }
    public void generateRequestExpirationNotice() throws Exception {
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb)SpringContext.getBean("oleLoanDao");
        Collection oleDeliverRequestBoList = oleLoanDocumentDaoOjb.getExpiredRequests();
        SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OleDeliverRequest.DATE_FORMAT);
        EntityTypeContactInfoBo entityTypeContactInfoBo;
        List<OleNoticeBo> noticesList = new ArrayList<OleNoticeBo>();
        Set<String> itemLocations = new HashSet<>();
        Item item;
        String noticeType = null;
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        boolean firstTime = true;
        String patronId ="";
        String noticeTypeParam = getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.RQST_EXPR_NOTICE_TYPE);
        if (oleDeliverRequestBoList.size() > 0) {
            for (Object obj : oleDeliverRequestBoList) {
                OleDeliverRequestBo deliverRequestBo = (OleDeliverRequestBo) obj;
                if(firstTime){
                    patronId= deliverRequestBo.getBorrowerId();
                    firstTime = false;
                }
                if (getDocstoreUtil().isItemAvailableInDocStore(deliverRequestBo)) {
                    //  processItem(deliverRequestBo);
                    item = deliverRequestBo.getOleItem();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Expiration Date :" + deliverRequestBo.getRequestExpiryDate());
                    }
                    if (deliverRequestBo.getOlePatron().isCourtesyNotice()) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Inside Expiration Date :" + deliverRequestBo.getRequestExpiryDate());
                        }
                        entityTypeContactInfoBo = deliverRequestBo.getOlePatron().getEntity().getEntityTypeContactInfos().get(0);
                        oleNoticeBo = new OleNoticeBo();
                        oleNoticeBo.setAuthor(deliverRequestBo.getAuthor());
                        oleNoticeBo.setCirculationDeskAddress("");
                        oleNoticeBo.setCirculationDeskName("");
                        oleNoticeBo.setCirculationDeskEmailAddress("");
                        oleNoticeBo.setCirculationDeskPhoneNumber("");
                        oleNoticeBo.setPatronName(deliverRequestBo.getOlePatron().getEntity().getNames().get(0).getFirstName() + " " + deliverRequestBo.getOlePatron().getEntity().getNames().get(0).getLastName());
                        oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
                        oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
                        oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
                        oleNoticeBo.setItemCallNumber(deliverRequestBo.getCallNumber() != null ? deliverRequestBo.getCallNumber() : "");
                        oleNoticeBo.setItemShelvingLocation(deliverRequestBo.getShelvingLocation() != null ? deliverRequestBo.getShelvingLocation() : "");
                        oleNoticeBo.setItemId(deliverRequestBo.getItemId() != null ? deliverRequestBo.getItemId() : "");
                        oleNoticeBo.setTitle(deliverRequestBo.getTitle() != null ? deliverRequestBo.getTitle() : "");
                        oleNoticeBo.setOleItem(item);
                        oleNoticeBo.setOlePatron(deliverRequestBo.getOlePatron());
                        oleNoticeBo.setVolumeNumber(item.getVolumeNumber() != null ? item.getVolumeNumber() : "");
                        oleNoticeBo.setNewDueDate(new Date());
                        oleNoticeBo.setOriginalDueDate(new Date());
                        oleNoticeBo.setNoticeName(OLEConstants.OleDeliverRequest.EXPIRED_REQUEST);
                        String noticeContent = getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.EXP_HOLD_NOTICE_CONTENT);
                        oleNoticeBo.setNoticeSpecificContent(noticeContent);
                      //  noticesList.add(oleNoticeBo);
                        String agendaName = OLEConstants.BATCH_PROGRAM_AGENDA;
                        HashMap<String, Object> termValues = new HashMap<String, Object>();
                        OleCirculationDesk oleCirculationDesk = deliverRequestBo.getOlePickUpLocation();
                        OlePatronDocument olePatronDocument = deliverRequestBo.getOlePatron();
                        String deskLocation = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskCode() : "";
                        String deskLocationName = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : "";
                        termValues.put(OLEConstants.BORROWER_TYPE, olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                        termValues.put(OLEConstants.DESK_LOCATION, deskLocation);
                        termValues.put(OLEConstants.NOTICE, OLEConstants.OleDeliverRequest.EXPIRED_REQUEST);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("termValues.toString()" + termValues.toString());
                        }
                        EngineResults engineResults = getLoanProcessor().getEngineResults(agendaName, termValues);
                        noticeType = (String) engineResults.getAttribute(OLEConstants.NOTICE_TYPE);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("**************" + noticeType);
                        }
                        noticeType = noticeType != null ? noticeType : noticeTypeParam;
                        oleNoticeBo.setNoticeType(noticeType);
                        oleNoticeBo.setNoticeType(noticeType);
                        if(!patronId.equalsIgnoreCase(deliverRequestBo.getBorrowerId()) && noticesList.size()>0){
                            String replyToEmail = null;
                            if (itemLocations.size() == 1) {
                                replyToEmail = getLoanProcessor().getReplyToEmail(itemLocations.iterator().next());
                            }
                            generateNoticesBasedOnNoticeType(noticesList,OLEConstants.OleDeliverRequest.EXPIRED_REQUEST, replyToEmail);
                            patronId = deliverRequestBo.getBorrowerId();
                            noticesList =  new ArrayList<>();
                            itemLocations = new HashSet<>();
                        }
                        noticesList.add(oleNoticeBo);
                        itemLocations.add(oleNoticeBo.getItemShelvingLocation());
                    }
                }
            }
            if(noticesList.size()>0){
                String replyToEmail = null;
                if (itemLocations.size() == 1) {
                    replyToEmail = getLoanProcessor().getReplyToEmail(itemLocations.iterator().next());
                }
                generateNoticesBasedOnNoticeType(noticesList,OLEConstants.OleDeliverRequest.EXPIRED_REQUEST, replyToEmail);
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
        LOG.debug("Inside deletingExpiredRequests");
        List<OleDeliverRequestBo> oleDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> newOleDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        try {
            oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findAll(OleDeliverRequestBo.class);
            getBusinessObjectService().delete(oleDeliverRequestBoList);
            SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OleDeliverRequest.DATE_FORMAT);
            for (int i = 0; i < oleDeliverRequestBoList.size(); i++) {
            try{
                if ((fmt.format(oleDeliverRequestBoList.get(i).getRequestExpiryDate())).compareTo(fmt.format(new Date(System.currentTimeMillis()))) > 0) {
                    newOleDeliverRequestBoList.add(oleDeliverRequestBoList.get(i));
                }
            }catch(Exception e){
                LOG.info("Exception occured while deleting the request with request Id : " + oleDeliverRequestBoList.get(i).getRequestId());
                LOG.error(e,e);
            }
            }
            getBusinessObjectService().save(newOleDeliverRequestBoList);
        } catch (Exception e) {
            getBusinessObjectService().save(oleDeliverRequestBoList);
            LOG.error("Exception while deleting expired requests", e);
        }
    }

    private Item getItem(String itemUUID) {
        LOG.debug("Inside getItem");
        try {
            // Map  docStoreDetails= loanProcessor.getItemDetails(itemBarCode);
            String itemXml = getLoanProcessor().getItemXML(itemUUID);
            Item oleItem = getLoanProcessor().getItemPojo(itemXml);
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


    private OleDeliverRequestBo createRequestHistoryRecord(String requestId, String OperatorId, String loanTransactionNumber) {
        LOG.debug("Inside createRequestHistoryRecord");
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_ID, requestId);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) businessObjectService.findMatching(OleDeliverRequestBo.class, requestMap);
        OleDeliverRequestBo oleDeliverRequestBo = null;
        if (oleDeliverRequestBoList.size() > 0) {
             oleDeliverRequestBo = oleDeliverRequestBoList.get(0);
            OleDeliverRequestHistoryRecord oleDeliverRequestHistoryRecord = new OleDeliverRequestHistoryRecord();
            oleDeliverRequestHistoryRecord.setRequestId(oleDeliverRequestBo.getRequestId());
            oleDeliverRequestHistoryRecord.setItemId(oleDeliverRequestBo.getItemId());
            oleDeliverRequestHistoryRecord.setArchiveDate(new java.sql.Date(System.currentTimeMillis()));
            oleDeliverRequestHistoryRecord.setCreateDate(oleDeliverRequestBo.getCreateDate());
            oleDeliverRequestHistoryRecord.setPickUpLocationCode(oleDeliverRequestBo.getPickUpLocationCode());
            oleDeliverRequestHistoryRecord.setOperatorId(OperatorId);
            oleDeliverRequestHistoryRecord.setDeliverRequestTypeCode(oleDeliverRequestBo.getRequestTypeCode());
            oleDeliverRequestHistoryRecord.setPoLineItemNumber("");
            oleDeliverRequestHistoryRecord.setLoanTransactionId(loanTransactionNumber);
            //oleDeliverRequestHistoryRecord.setMachineId(""); //commented for jira OLE-5675
            getBusinessObjectService().save(oleDeliverRequestHistoryRecord);
            getBusinessObjectService().delete(oleDeliverRequestBoList);
        }
       return oleDeliverRequestBo;
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
                if (entityTypeContactInfoBo.getEmailAddresses().get(j).getDefaultValue()) {
                    emailId = (entityTypeContactInfoBo.getEmailAddresses().get(j).getEmailAddress());
                    break;
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
        Long b1 = System.currentTimeMillis();
        List<String> itemUUIDS = new ArrayList<String>();
        if (LOG.isDebugEnabled()) {
            LOG.debug("************---------START TIME OF GENERATE NOTICE SERVICE------" + System.currentTimeMillis());
        }
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        String courtesyInterval = getIntervalForCourtesyNotice();
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb)SpringContext.getBean("oleLoanDao");
        List<OleLoanDocument> courtesyNotice = new ArrayList<>();
        List<OleLoanDocument> overdueNotice = new ArrayList<>();
        try {
            overdueNotice=oleLoanDocumentDaoOjb.getLoanDocumentsForNoticeGeneration(OLEConstants.NOTICE_OVERDUE);
            try{
            overdueNotice = getLoanDocumentWithItemInfo((List<OleLoanDocument>)overdueNotice);
            }catch (Exception e){
                LOG.error(e,e);
                throw new Exception("Exception occured while fetching data from solr");
            }
            List<OleLoanDocument> documents = new ArrayList<>();
            List<String> itemIds = new ArrayList<>();
            if (overdueNotice != null && overdueNotice.size()>0) {
                boolean firstTime = true;
                String patronId="";
                for (Object obj : overdueNotice) {
                    OleLoanDocument loanDocument = (OleLoanDocument) obj;
                    if(firstTime){
                       patronId=loanDocument.getPatronId();
                    }
                    if(patronId!=null && patronId.equals(loanDocument.getPatronId()) ){
                        documents.add(loanDocument);
                        itemIds.add(loanDocument.getItemUuid());
                    } else {
                        generateNoticeForOverdueAndCourtesy(patronId,documents,true,itemUUIDS);
                        documents = new ArrayList<>();
                        itemIds =  new ArrayList<>();
                        documents.add(loanDocument);
                        itemIds.add(loanDocument.getItemUuid());
                        patronId=loanDocument.getPatronId();
                    }
                    firstTime=false;
                }
                generateNoticeForOverdueAndCourtesy(patronId,documents,true,itemUUIDS);
            }
            courtesyNotice=oleLoanDocumentDaoOjb.getLoanDocumentsForNoticeGeneration(OLEConstants.NOTICE_COURTESY);
            try{
            courtesyNotice=getLoanDocumentWithItemInfo((List<OleLoanDocument>)courtesyNotice);
        }catch (Exception e){
            LOG.error(e,e);
            throw new Exception("Exception occured while fetching data from solr");
        }
            documents = new ArrayList<>();
            itemIds = new ArrayList<>();
            if (courtesyNotice != null && courtesyNotice.size()>0) {
                boolean firstTime = true;
                String patronId="";
                for (Object obj : courtesyNotice) {
                    OleLoanDocument loanDocument = (OleLoanDocument) obj;
                    if(firstTime){
                        patronId=loanDocument.getPatronId();
                    }
                    if(patronId!=null && patronId.equals(loanDocument.getPatronId()) ){
                        documents.add(loanDocument);
                        itemIds.add(loanDocument.getItemUuid());
                    } else {
                        generateNoticeForOverdueAndCourtesy(patronId,documents,false,itemUUIDS);
                        documents = new ArrayList<>();
                        itemIds =  new ArrayList<>();
                        documents.add(loanDocument);
                        itemIds.add(loanDocument.getItemUuid());
                        patronId=loanDocument.getPatronId();
                    }
                    firstTime=false;
                }
                generateNoticeForOverdueAndCourtesy(patronId,documents,false,itemUUIDS);
            }
        } catch (Exception e){
           LOG.error("Error while preparing Patron Map using PersistenceBroker ----> generateOverDueNotice()"+e.getMessage() +e);
        }

        if(itemUUIDS.size()>0){
            itemStatusBulkUpdate(itemUUIDS);
        }
        Long b2 = System.currentTimeMillis();
        Long total = b2 - b1;
        LOG.info("The time generating overdue/courtesy notice time:"+total);

    }

    public void generateNoticeForOverdueAndCourtesy(String patronId, List<OleLoanDocument> oleLoanDocuments,boolean overdue,List<String> itemUUIDS){
        Map<String, String> patronMap = new HashMap<String, String>();
        patronMap.put("olePatronId",patronId);
        List<OlePatronDocument> olePatronDocuments=(List<OlePatronDocument>)KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,patronMap);
        if(olePatronDocuments!=null && olePatronDocuments.size()>0){
            OlePatronDocument olePatronDocument=olePatronDocuments.get(0);
            if(oleLoanDocuments!=null && oleLoanDocuments.size()>0){
                getNoticeList(oleLoanDocuments,olePatronDocument,overdue,itemUUIDS);
            }
        }
    }

    public void generateHoldCourtesyNotice() throws Exception {
        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findAll(OleDeliverRequestBo.class);
        for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBos) {
            List<OleNoticeBo> oleNoticeBos = new ArrayList<OleNoticeBo>();
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            if (getDocstoreUtil().isItemAvailableInDocStore(oleDeliverRequestBo)) {
                Item oleItem = oleDeliverRequestBo.getOleItem();
                OleNoticeBo oleNoticeBo = new OleNoticeBo();
                Date currentDate = new Date();
                DateFormat formatter = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE_NOTICE);
                Date itemStatusEffectiveDate = (Date) formatter.parse(oleItem.getItemStatusEffectiveDate());
                Integer numberOfDaysOnHold = determineDifferenceInDays(currentDate, itemStatusEffectiveDate);
                Integer maxNumberOfDaysOnHold = 0;
                OleCirculationDesk oleCirculationDesk = null;
                if (oleDeliverRequestBo.getPickUpLocationId() != null) {
                    oleCirculationDesk = getLoanProcessor().getOleCirculationDesk(oleDeliverRequestBo.getPickUpLocationId());
                    String maxNumOfDays = oleCirculationDesk.getOnHoldDays() != null ? oleCirculationDesk.getOnHoldDays() : getLoanProcessor().getParameter(OLEConstants.MAX_NO_OF_DAYS_ON_HOLD);
                    maxNumberOfDaysOnHold = new Integer(maxNumOfDays);
                }
                String itemTypeName = null;
                if (oleItem.getTemporaryItemType() != null && oleItem.getTemporaryItemType().getCodeValue() != "") {
                    OleInstanceItemType oleInstanceItemType = getLoanProcessor().getItemTypeIdByItemType(oleItem.getTemporaryItemType().getCodeValue());
                    itemTypeName = oleInstanceItemType.getInstanceItemTypeCode();
                }
                else if (oleItem.getItemType() != null && oleItem.getItemType().getCodeValue() != "") {
                    OleInstanceItemType oleInstanceItemType = getLoanProcessor().getItemTypeIdByItemType(oleItem.getItemType().getCodeValue());
                    itemTypeName = oleInstanceItemType.getInstanceItemTypeCode();
                }
                OlePatronDocument olePatronDocument = oleDeliverRequestBo.getOlePatron();
                String agendaName = OLEConstants.OleDeliverRequest.NOTICE_VALIDATION;
                String patronId = olePatronDocument.getOlePatronId()!=null ?  olePatronDocument.getOlePatronId() : "";
                String itemId = oleDeliverRequestBo.getItemId()!=null ?  oleDeliverRequestBo.getItemId() : "";
                dataCarrierService.removeData(patronId+itemId);
                HashMap<String, Object> termValues = new HashMap<String, Object>();
                Date expirationDate = olePatronDocument!=null ? olePatronDocument.getExpirationDate():null;
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
                termValues.put(OLEConstants.REQUEST_TYPE,oleDeliverRequestBo.getRequestTypeCode());
                termValues.put(OLEConstants.EXPIR_DATE,expirationDate);
                termValues.put(OLEConstants.PATRON_ID_POLICY, patronId);
                termValues.put(OLEConstants.ITEM_ID_POLICY, itemId);
                if (oleItem.getItemStatus() != null)
                    termValues.put(OLEConstants.ITEM_STATUS, oleItem.getItemStatus().getCodeValue());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("termValues.toString()" + termValues.toString());
                }
                EngineResults engineResults = getLoanProcessor().getEngineResults(agendaName, termValues);
                String notice = (String) engineResults.getAttribute(OLEConstants.NOTICE);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("notice" + notice);
                }
                agendaName = OLEConstants.BATCH_PROGRAM_AGENDA;
                termValues = new HashMap<String, Object>();
                String deskLocation = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskCode() : "";
                String deskLocationName = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : "";
                termValues.put(OLEConstants.BORROWER_TYPE, olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                termValues.put(OLEConstants.DESK_LOCATION, deskLocation);
                termValues.put(OLEConstants.NOTICE, notice);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("termValues.toString()" + termValues.toString());
                }
                engineResults = getLoanProcessor().getEngineResults(agendaName, termValues);
                dataCarrierService.removeData(patronId+itemId);
                String noticeType = (String) engineResults.getAttribute(OLEConstants.NOTICE_TYPE);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("**************" + noticeType);
                }
                OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
                if (notice != null) {
                    oleNoticeBo.setNoticeName(notice);
                    if (notice.equalsIgnoreCase(OLEConstants.NOTICE_HOLD_COURTESY)) {
                        oleNoticeBo = getExpiredHoldNotice(oleDeliverRequestBo);
                        oleNoticeBo.setCirculationDeskName(deskLocationName);
                        oleNoticeBos.add(oleNoticeBo);
                        noticeType = noticeType == null ? getLoanProcessor().getParameter("HOLDCOURTESY_NOTICE_TYPE") : noticeType;
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
                                oleMailer.sendEmail(new EmailFrom(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail()), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(noticeContent), true);
                            } else {
                                String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                                if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                                    fromAddress = OLEConstants.KUALI_MAIL;
                                }
                                oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(noticeContent), true);
                            }
                        } else {
                            for (OleNoticeBo oleNoticeBo1 : oleNoticeBos) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("Notice Type :" + oleNoticeBo1.getNoticeName() + "  " + "Item Barcode : " + oleNoticeBo1.getItemId() + " " + "Patron Name :" + oleNoticeBo1.getPatronName());
                                }
                            }
                        }

                        if (LOG.isDebugEnabled()) {
                            LOG.debug("olePatronDocument.getEmailAddress()" + olePatronDocument.getEmailAddress());
                        }
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
                termValues.put(OLEConstants.OleDeliverRequest.IS_TEMPORARY_HISTORY_RECORD, String.valueOf(Boolean.TRUE));
                DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
                dataCarrierService.addData(OLEConstants.DATE_CHECK_IN, oleTemporaryCirculationHistory.getCheckInDate());
                EngineResults engineResults = getLoanProcessor().getEngineResults(agendaName, termValues);
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
        Item oleItem = oleDeliverRequestBo.getOleItem();
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
        oleNoticeBo.setNoticeSpecificContent(getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.EXP_HOLD_NOTICE_CONTENT));
        oleNoticeBo.setTitle(item.getHolding().getBib().getTitle());
        oleNoticeBo.setAuthor(item.getHolding().getBib().getAuthor());
        /*oleNoticeBo.setTitle((String) bibInformation.get(OLEConstants.TITLE) != null ? (String) bibInformation.get(OLEConstants.TITLE) : "");
        oleNoticeBo.setAuthor((String) bibInformation.get(OLEConstants.AUTHOR) != null ? (String) bibInformation.get(OLEConstants.AUTHOR) : "");*/
        oleNoticeBo.setVolumeNumber(item.getVolumeNumber());
        oleNoticeBo.setItemShelvingLocation(shelvingLocation != null ? shelvingLocation : "");
        //oleNoticeBo.setItemCallNumber((String) docStoreDetails.get(OLEConstants.CALL_NUM) != null ? (String) docStoreDetails.get(OLEConstants.CALL_NUM) : "");
        oleNoticeBo.setItemCallNumber((String) oleItem.getCallNumber().getNumber() != null && !oleItem.getCallNumber().getNumber().equals("") ? oleItem.getCallNumber().getNumber() : "");
        oleNoticeBo.setItemId(oleDeliverRequestBo.getItemId() != null ? oleDeliverRequestBo.getItemId() : "");
        oleNoticeBo.setCirculationDeskName(oleDeliverRequestBo.getOlePickUpLocation().getCirculationDeskPublicName());
        oleNoticeBo.setCirculationDeskReplyToEmail(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail());
        oleNoticeBo.setOleItem(oleItem);
        oleNoticeBo.setOlePatron(oleDeliverRequestBo.getOlePatron());
      //  oleNoticeBo = setPatronDetailsForNotice(oleNoticeBo, oleDeliverRequestBo.getOlePatron());
        oleDeliverRequestBo.getOlePatron().setEmailAddress(oleNoticeBo.getPatronEmailAddress());
        return oleNoticeBo;
    }

    private OleNoticeBo getCourtesyNotice(OleLoanDocument oleLoanDocument) throws Exception {
        LOG.debug("Courtesy Notice");
        oleLoanDocument.setCourtesyNoticeFlag(true);
        Map<String, String> map = new HashMap<String, String>();
        map.put(OLEConstants.LOAN_ID, oleLoanDocument.getLoanId());
        KRADServiceLocator.getBusinessObjectService().save(oleLoanDocument);
        EntityTypeContactInfoBo entityTypeContactInfoBo = oleLoanDocument.getOlePatron().getEntity().getEntityTypeContactInfos().get(0);
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        //String itemId  = oleLoanDocument.getItemId();
        Item oleItem = oleLoanDocument.getOleItem();
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
        oleNoticeBo.setNoticeName(OLEConstants.NOTICE_COURTESY);
        oleNoticeBo.setNoticeSpecificContent(getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.COURTESY_NOTICE_CONTENT));
       /* oleNoticeBo.setTitle((String) bibInformation.get(OLEConstants.TITLE) != null ? (String) bibInformation.get(OLEConstants.TITLE) : "");
        oleNoticeBo.setAuthor((String) bibInformation.get(OLEConstants.AUTHOR) != null ? (String) bibInformation.get(OLEConstants.AUTHOR) : "");*/
        oleNoticeBo.setTitle(oleLoanDocument.getTitle());
        oleNoticeBo.setAuthor(oleLoanDocument.getAuthor());
        oleNoticeBo.setVolumeNumber(oleLoanDocument.getItemVolumeNumber());
        oleNoticeBo.setItemShelvingLocation(shelvingLocation != null ? shelvingLocation : "");
        // oleNoticeBo.setItemCallNumber((String) docStoreDetails.get(OLEConstants.CALL_NUM) != null ? (String) docStoreDetails.get(OLEConstants.CALL_NUM) : "");
        oleNoticeBo.setItemCallNumber(oleLoanDocument.getItemCallNumber());
        oleNoticeBo.setItemId(oleLoanDocument.getItemId());
        oleLoanDocument.getOlePatron().setEmailAddress(oleNoticeBo.getPatronEmailAddress());
        if (LOG.isDebugEnabled()) {
            LOG.debug("oleNoticeBo.getPatronEmailAddress()" + oleNoticeBo.getPatronEmailAddress());
        }
        return oleNoticeBo;
    }

    private OleNoticeBo getOverdueNotice(OleLoanDocument oleLoanDocument) throws Exception {
        LOG.debug("Overdue Notice");
        EntityTypeContactInfoBo entityTypeContactInfoBo = oleLoanDocument.getOlePatron().getEntity().getEntityTypeContactInfos().get(0);
        OleNoticeBo oleNoticeBo = new OleNoticeBo();
        //   String itemId  = oleLoanDocument.getItemId();
        Item oleItem = oleLoanDocument.getOleItem();
        //  String itemUuid = oleItem.getItemIdentifier();
        //String shelvingLocation = oleLoanDocument.getItemLocation();
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
        oleNoticeBo.setNoticeName(OLEConstants.NOTICE_OVERDUE);
        oleNoticeBo.setNoticeSpecificContent(getLoanProcessor().getParameter(OLEConstants.OleDeliverRequest.OVERDUE_NOTICE_CONTENT));
        oleNoticeBo.setTitle(oleLoanDocument.getTitle());
        oleNoticeBo.setAuthor(oleLoanDocument.getAuthor());
        /*oleNoticeBo.setTitle((String) bibInformation.get(OLEConstants.TITLE) != null ? (String) bibInformation.get(OLEConstants.TITLE) : "");
        oleNoticeBo.setAuthor((String) bibInformation.get(OLEConstants.AUTHOR) != null ? (String) bibInformation.get(OLEConstants.AUTHOR) : "");*/

        //oleNoticeBo.setVolumeNumber((String) docStoreDetails.get(OLEConstants.VOL_NUM) != null ? (String) docStoreDetails.get(OLEConstants.VOL_NUM) : "");
        String volume = (String) oleItem.getEnumeration() != null && !oleItem.getEnumeration().equals("") ? oleItem.getEnumeration() : "";
        String issue = new String(" ");
        String copyNumber = (String) oleItem.getCopyNumber() != null && !oleItem.getCopyNumber().equals("") ? oleItem.getCopyNumber() : "";
        oleNoticeBo.setVolumeIssueCopyNumber(volume + "/" + issue + "/" + copyNumber);
        oleNoticeBo.setItemShelvingLocation(oleLoanDocument.getItemLocation());
        /*if (oleItem.getCallNumber().getNumber() != null && !oleItem.getCallNumber().getNumber().equals("")) {
            oleNoticeBo.setItemCallNumber((String) oleItem.getCallNumber().getNumber() != null && !oleItem.getCallNumber().getNumber().equals("") ? oleItem.getCallNumber().getNumber() : "");
        } else {
            oleNoticeBo.setItemCallNumber(getLoanProcessor().getItemCallNumber(oleItem, oleLoanDocument.getInstanceUuid()));
        }*/
        oleNoticeBo.setItemCallNumber(getLoanProcessor().getItemCallNumber(oleItem, oleLoanDocument.getInstanceUuid()));
        //oleNoticeBo.setItemCallNumber((String) docStoreDetails.get(OLEConstants.CALL_NUM) != null ? (String) docStoreDetails.get(OLEConstants.CALL_NUM) : "");
        oleNoticeBo.setItemId(oleLoanDocument.getItemId());
        //oleNoticeBo.setDueDate(oleLoanDocument.getLoanDueDate()!=null ? oleLoanDocument.getLoanDueDate().toString().substring(0, 10):null);
        oleNoticeBo.setDueDate(oleLoanDocument.getLoanDueDate()!=null ? oleLoanDocument.getLoanDueDate():null);

        oleLoanDocument.getOlePatron().setEmailAddress(oleNoticeBo.getPatronEmailAddress());
        if (LOG.isDebugEnabled()) {
            LOG.debug("oleNoticeBo.getPatronEmailAddress()" + oleNoticeBo.getPatronEmailAddress());
        }
        int noOfOverdueNoticeSent = Integer.parseInt(oleLoanDocument.getNumberOfOverdueNoticesSent() != null ? oleLoanDocument.getNumberOfOverdueNoticesSent() : "0");
        noOfOverdueNoticeSent = noOfOverdueNoticeSent + 1;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updated Loan Record : " + oleLoanDocument);
        }
        oleLoanDocument.setNumberOfOverdueNoticesSent(Integer.toString(noOfOverdueNoticeSent));
        oleLoanDocument.setOverDueNoticeDate(new java.sql.Date(System.currentTimeMillis()));
        getBusinessObjectService().save(oleLoanDocument);
        return oleNoticeBo;
    }

    public void updateItem(Item oleItem, String itemUuid) throws Exception {
        ItemStatus itemStatus = new ItemStatus();
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

    public String getShelvingLocation(LocationLevel oleLocationLevel) {
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

    public String placeRequest(String patronBarcode, String operatorId, String itemBarcode, String requestType, String pickUpLocation, String itemIdentifier,String itemLocation,String itemType,String title,String author,String callNumber,boolean externalItem,String bibId,String requestLevel,java.sql.Date requestExpiryDate) {
        OLEPlaceRequest olePlaceRequest = new OLEPlaceRequest();
        OLEPlaceRequestConverter olePlaceRequestConverter = new OLEPlaceRequestConverter();
        ASRHelperServiceImpl asrHelperService = new ASRHelperServiceImpl();
        MaintenanceDocument newDocument = null;
        try {
            try {
                if (null == GlobalVariables.getUserSession()) {
                    Person person = personService.getPerson(operatorId);
                    String principalName = person.getPrincipalName();
                    UserSession userSession = new UserSession(principalName);
                    GlobalVariables.setUserSession(userSession);
                }
                newDocument = (MaintenanceDocument) documentService.getNewDocument(OLEConstants.REQUEST_DOC_TYPE);
            } catch (WorkflowException e) {
                e.printStackTrace();
                olePlaceRequest.setBlockOverride(true);
                olePlaceRequest.setMessage("Cannot create");
                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
            }
            OleDeliverRequestBo oleDeliverRequestBo = null;
            oleDeliverRequestBo = (OleDeliverRequestBo) newDocument.getNewMaintainableObject().getDataObject();
            oleDeliverRequestBo.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
            oleDeliverRequestBo.setRequestLevel(requestLevel);
            oleDeliverRequestBo.setBibId(bibId);
            if(requestExpiryDate!=null){
                oleDeliverRequestBo.setRequestExpiryDate(requestExpiryDate);
            }
            OlePatronDocument olePatronDocument = null;
            Map<String, String> patronMap = new HashMap<String, String>();
            patronMap.put(OLEConstants.BARCODE, patronBarcode);
            OleNoticeBo oleNoticeBo = new OleNoticeBo();
            List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
            if (olePatronDocumentList.size() > 0) {
                olePatronDocument = olePatronDocumentList.get(0);
                olePatronDocument.setOleBorrowerType((OleBorrowerType) SpringContext.getBean(PersistenceDaoOjb.class).resolveProxy(olePatronDocument.getOleBorrowerType()));
                oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
                oleDeliverRequestBo.setBorrowerBarcode(olePatronDocument.getBarcode());
                oleDeliverRequestBo.setOlePatron(olePatronDocument);
                EntityTypeContactInfoBo entityTypeContactInfoBo = olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);
                try {
                    oleNoticeBo.setPatronName(olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " + oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getLastName());
                    oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
                } catch (Exception e) {
                    LOG.error("Exception", e);
                    olePlaceRequest.setBlockOverride(true);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Exception Occured while setting the patron information for the patron . Patron Barcode : " + oleDeliverRequestBo.getBorrowerBarcode());
                    }
                }
            } else {
                olePlaceRequest.setBlockOverride(true);
                olePlaceRequest.setCode("002");
                olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
            }
            if (requestType != null) {
                Map<String, String> requestTypeMap = new HashMap<String, String>();
                requestTypeMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_CD, requestType);
                List<OleDeliverRequestType> oleDeliverRequestTypeList = (List<OleDeliverRequestType>) getBusinessObjectService().findMatching(OleDeliverRequestType.class, requestTypeMap);
                if (oleDeliverRequestTypeList != null && (oleDeliverRequestTypeList.size() > 0)) {
                    oleDeliverRequestBo.setRequestTypeId(oleDeliverRequestTypeList.get(0).getRequestTypeId());
                    oleDeliverRequestBo.setOleDeliverRequestType(oleDeliverRequestTypeList.get(0));
                } else {
                    olePlaceRequest.setBlockOverride(true);
                    olePlaceRequest.setCode("012");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_RQST_TYP));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }
            }
            if (pickUpLocation != null) {
                Map<String, String> circulationDeskMap = new HashMap<String, String>();
                circulationDeskMap.put(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_CD, pickUpLocation);
                List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>) getBusinessObjectService().findMatching(OleCirculationDesk.class, circulationDeskMap);
                if (oleCirculationDeskList != null && oleCirculationDeskList.size() > 0) {
                    oleDeliverRequestBo.setPickUpLocationId(oleCirculationDeskList.get(0).getCirculationDeskId());
                    oleDeliverRequestBo.setPickUpLocationCode(oleCirculationDeskList.get(0).getCirculationDeskCode());
                    oleDeliverRequestBo.setOlePickUpLocation(oleCirculationDeskList.get(0));
                } else {
                    olePlaceRequest.setBlockOverride(true);
                    olePlaceRequest.setCode("013");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_PK_UP_LOCN));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }

            }
            try {
                if (itemBarcode == null || (itemBarcode != null && itemBarcode.isEmpty())) {
                    olePlaceRequest.setBlockOverride(true);
                    olePlaceRequest.setCode("014");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_DOESNOT_EXISTS));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);

                }
                oleDeliverRequestBo.setItemId(itemBarcode);
                oleDeliverRequestBo.setItemUuid(itemIdentifier);
                oleDeliverRequestBo.setItemStatus(OLEConstants.AVAILABLE);
                oleDeliverRequestBo.setItemType(itemType);
                oleDeliverRequestBo.setItemLocation(itemLocation);
                if (itemIdentifier == null || itemLocation == null || itemType == null) {
                    Thread.sleep(500);
                    String itemUUID = null;
                    String holdingsId = null;
                    if (itemIdentifier == null) {
                        try {
                            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                            org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
                            SearchResponse searchResponse = null;
                            search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, oleDeliverRequestBo.getItemId()), ""));
                            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "id"));
                            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), "id"));
                            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
                            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                                    String fieldName = searchResultField.getFieldName();
                                    String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                                    if (fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("holdings")) {
                                        holdingsId = fieldValue;
                                    } else {
                                        oleDeliverRequestBo.setItemUuid(fieldValue);
                                        itemUUID = fieldValue;
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_EXIST));
                            LOG.error(OLEConstants.ITEM_EXIST + ex);
                        }
                    }
                    if (itemUUID == null) {
                        olePlaceRequest.setBlockOverride(true);
                        olePlaceRequest.setCode("014");
                        olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_DOESNOT_EXISTS));
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    }
                    if (itemType == null || itemLocation == null) {
                        Map<String, Object> detailMap = retrieveBIbItemHoldingData(itemUUID);
                        Bib bib = (Bib) detailMap.get(OLEConstants.BIB);
                        Item item = (Item) detailMap.get(OLEConstants.ITEM);
                        OleHoldings oleHoldings = (OleHoldings) detailMap.get(OLEConstants.HOLDING);
                        org.kuali.ole.docstore.common.document.Item item1 = (org.kuali.ole.docstore.common.document.Item) detailMap.get("documentItem");
                        if (item != null) {
                            oleDeliverRequestBo.setOleItem(item);
                            if (item.getCallNumber() != null && item.getCallNumber().getNumber() != null) {
                                oleDeliverRequestBo.setCallNumber(item.getCallNumber().getNumber());
                            }
                            if (item.getCopyNumber() != null) {
                                oleDeliverRequestBo.setCopyNumber(item.getCopyNumber());
                            }
                            oleDeliverRequestBo.setVolumeNumber(item.getEnumeration() != null ? item.getEnumeration() : "");
                        }
                        if (oleHoldings != null) {
                            if (oleDeliverRequestBo.getCallNumber() == null && oleHoldings.getCallNumber() != null && oleHoldings.getCallNumber().getNumber() != null) {
                                oleDeliverRequestBo.setCallNumber(oleHoldings.getCallNumber().getNumber());
                            }
                            if (oleDeliverRequestBo.getCopyNumber() == null && oleHoldings.getCopyNumber() != null) {
                                oleDeliverRequestBo.setCopyNumber(oleHoldings.getCopyNumber());
                            }
                        }
                        if (itemLocation == null) {
                            if (item1.getLocation() == null || (item1.getLocation() != null && item1.getLocation().trim().isEmpty())) {
                                itemLocation = getDocstoreUtil().getLocation(oleHoldings.getLocation(), new StringBuffer(""));
                            } else {
                                itemLocation = item1.getLocation();
                            }
                            oleDeliverRequestBo.setItemLocation(itemLocation);
                        }
                        if (item.getItemType() != null) {
                            oleDeliverRequestBo.setItemType(item.getItemType().getCodeValue());
                        }
                        if (item.getItemStatus() != null) {
                            oleDeliverRequestBo.setItemStatus(item.getItemStatus().getCodeValue());
                        }

                        if (bib != null) {
                            oleDeliverRequestBo.setTitle(bib.getTitle());
                            oleDeliverRequestBo.setAuthor(bib.getAuthor());
                        }
                    }
                }

                if (itemLocation != null) {
                    if (asrHelperService.isAnASRItem(itemLocation) && oleDeliverRequestBo.getItemStatus().equals(getLoanProcessor().getParameter(ASRConstants.ASR_REQUEST_ITEM_STATUS)) && !oleDeliverRequestBo.getRequestTypeCode().equals(getLoanProcessor().getParameter(ASRConstants.ASR_TYP_RQST))) {
                        olePlaceRequest.setCode("700");
                        olePlaceRequest.setBlockOverride(true);
                        olePlaceRequest.setMessage("Cannot create " + oleDeliverRequestBo.getRequestTypeCode() + " for this item");
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    } else if (asrHelperService.isAnASRItem(itemLocation) && !oleDeliverRequestBo.getItemStatus().equals(getLoanProcessor().getParameter(ASRConstants.ASR_REQUEST_ITEM_STATUS)) && oleDeliverRequestBo.getRequestTypeCode().equals(getLoanProcessor().getParameter(ASRConstants.ASR_TYP_RQST))) {
                        olePlaceRequest.setCode("701");
                        olePlaceRequest.setBlockOverride(true);
                        olePlaceRequest.setMessage("Cannot create " + oleDeliverRequestBo.getRequestTypeCode() + " for this item");
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    }
                    Map<String, String> locationMap = getLocationMap(itemLocation);
                    oleDeliverRequestBo.setItemLibrary(locationMap.get(OLEConstants.ITEM_LIBRARY));
                    oleDeliverRequestBo.setItemInstitution(locationMap.get(OLEConstants.ITEM_INSTITUTION));
                    oleDeliverRequestBo.setItemCampus(locationMap.get(OLEConstants.ITEM_CAMPUS));
                    oleDeliverRequestBo.setItemCollection(locationMap.get(OLEConstants.ITEM_COLLECTION));
                    oleDeliverRequestBo.setShelvingLocation(locationMap.get(OLEConstants.ITEM_SHELVING));
                }
                oleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_OPERATOR);
                oleDeliverRequestBo.setOperatorCreateId(operatorId);
                oleDeliverRequestBo.setItemId(itemBarcode);
                if (!processOperator(operatorId)) {
                    olePlaceRequest.setCode("001");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }
                processRequestTypeByPickUpLocation(oleDeliverRequestBo);
                String message = this.patronRecordExpired(oleDeliverRequestBo);
                if (message != null) {
                    olePlaceRequest.setCode("015");
                    olePlaceRequest.setMessage(message);
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }
                boolean requestRaised = this.isRequestAlreadyRaisedByPatron(oleDeliverRequestBo);
                if (requestRaised) {
                    {
                        olePlaceRequest.setBlockOverride(true);
                        olePlaceRequest.setCode("016");
                        olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RQST_ALRDY_RAISD));
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    }
                }
                boolean itemEligible = this.isItemEligible(oleDeliverRequestBo);
                if (!itemEligible) {
                    {
                        olePlaceRequest.setBlockOverride(true);
                        olePlaceRequest.setCode("017");
                        olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITM_NOT_LOAN));
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    }
                }
                boolean alreadyLoaned = this.isAlreadyLoaned(oleDeliverRequestBo);
                if (alreadyLoaned) {
                    olePlaceRequest.setBlockOverride(true);
                    olePlaceRequest.setCode("018");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITM_LOAN_BY_PTRN));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }
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
                        if ((oleDeliverRequestBo.getMessage() != null && !oleDeliverRequestBo.getMessage().isEmpty())) {
                            olePlaceRequest.setCode("500");
                            olePlaceRequest.setMessage(oleDeliverRequestBo.getMessage().replaceAll("<br/>", ""));
                            return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                        }

                    }
                }

                OleDeliverRequestBo oleDeliverRequestBo1 = null;
                boolean asrItem = false;
                try {
                    oleDeliverRequestBo1 = oleDeliverRequestBo;
                    if (isItemAvailable(oleDeliverRequestBo1)) {
                        olePlaceRequest.setBlockOverride(true);
                        olePlaceRequest.setCode("019");
                        olePlaceRequest.setMessage(oleDeliverRequestBo1.getRequestTypeCode() + OLEConstants.RQST_CONDITION + oleDeliverRequestBo1.getItemStatus());
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    }
                    oleDeliverRequestBo.setOleItem(null);
                    asrItem = asrHelperService.isAnASRItem(oleDeliverRequestBo.getItemLocation());
                    if (asrItem && oleDeliverRequestBo.getItemStatus().equals(getLoanProcessor().getParameter(ASRConstants.ASR_REQUEST_ITEM_STATUS))) {
                        oleDeliverRequestBo.setAsrFlag(true);
                        oleDeliverRequestBo.setRequestStatus("1");
                    }
                    newDocument.getDocumentHeader().setDocumentDescription(OLEConstants.NEW_REQUEST_DOC);
                    newDocument.getNewMaintainableObject().setDataObject(oleDeliverRequestBo1);

                    newDocument = (MaintenanceDocument) documentService.routeDocument(newDocument, null, null);
                    oleDeliverRequestBo = (OleDeliverRequestBo) newDocument.getNewMaintainableObject().getDataObject();
                } catch (WorkflowException e) {
                    e.printStackTrace();
                }


                String requestId = "";
                try {
                    requestId = ":" + OLEConstants.OleDeliverRequest.REQUEST_ID + ":" + oleDeliverRequestBo.getRequestId();

                    if (!oleDeliverRequestBo.getRequestTypeId().equals("8")) {
                        oleDeliverRequestBo = updateLoanDocument(oleDeliverRequestBo);
                        oleDeliverRequestBo.setOlePatron(null);
                        oleDeliverRequestBo.setOleProxyPatron(null);
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Request Raised Succesfully" + requestId);
                    }
                    if (externalItem) {
                        String noticeSendParameter = getLoanProcessor().getParameter(OLEParameterConstants.NCIP_ACCEPT_ITEM_NOTICE_INDICATOR);
                        if (noticeSendParameter != null && (noticeSendParameter.trim().isEmpty() || noticeSendParameter.equalsIgnoreCase("Y"))) {
                            oleNoticeBo.setNoticeName(OLEConstants.PICKUP_NOTICE);
                            Date pickupDate = new java.sql.Date(System.currentTimeMillis());
                            if (oleDeliverRequestBo.getOlePickUpLocation().getOnHoldDays() != null) {
                                pickupDate = addDate(new java.sql.Date(System.currentTimeMillis()), new Integer(oleDeliverRequestBo.getOlePickUpLocation().getOnHoldDays()));
                            }
                            oleNoticeBo.setNoticeSpecificContent(OLEConstants.PICKUP_NOTICE_START_CONTENT + oleDeliverRequestBo.getOlePickUpLocation().getCirculationDeskPublicName() + OLEConstants.PICKUP_NOTICE_MIDDLE_CONTENT + pickupDate + OLEConstants.PICKUP_NOTICE_FINAL_CONTENT);
                            oleNoticeBo.setAuthor(author);
                            oleNoticeBo.setItemCallNumber(callNumber);
                            oleNoticeBo.setItemId(itemBarcode);
                            oleNoticeBo.setTitle(title);
                            oleNoticeBo.setCirculationDeskName(oleDeliverRequestBo.getOlePickUpLocation().getCirculationDeskPublicName());
                            oleNoticeBo.setCirculationDeskReplyToEmail(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail());
                            OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
                            String content = oleDeliverBatchService.getEmailPickUpNotice(oleNoticeBo);
                            try {
                                if (!content.trim().equals("")) {
                                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                                    if (oleDeliverRequestBo.getOlePickUpLocation() != null && StringUtils.isNotBlank(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail())) {
                                        oleMailer.sendEmail(new EmailFrom(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail()), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(content), true);
                                    } else {
                                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                                            fromAddress = OLEConstants.KUALI_MAIL;
                                        }
                                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(content), true);
                                    }
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("Mail send Successfully to " + oleNoticeBo.getPatronEmailAddress());
                                    }
                                    oleDeliverBatchService.getPdfPickUpNotice(oleNoticeBo);
                                } else {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("Notice Type :" + oleNoticeBo.getNoticeName() + "  " + "Item Barcode : " + oleNoticeBo.getItemId() + " " + "Patron Name :" + oleNoticeBo.getPatronName());
                                    }
                                }
                            } catch (Exception e) {
                                LOG.error("Exception", e);
                                olePlaceRequest.setCode("020");
                                olePlaceRequest.setQueuePosition(String.valueOf(oleDeliverRequestBo.getBorrowerQueuePosition()));
                                olePlaceRequest.setMessage(OLEConstants.RQST_SUCCESS + requestId + OLEConstants.NTCE_PRBLM);
                                olePlaceRequest.setRequestId(oleDeliverRequestBo.getRequestId());
                                olePlaceRequest.setAvailableDate(getAvailableDate(itemBarcode));
                                olePlaceRequest.setBlockOverride(true);
                                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                            }
                        }
                    }

                } catch (Exception e) {
                    LOG.error("Exception", e);
                    olePlaceRequest.setCode("020");
                    olePlaceRequest.setQueuePosition(String.valueOf(oleDeliverRequestBo.getBorrowerQueuePosition()));
                    olePlaceRequest.setMessage(OLEConstants.RQST_SUCCESS + requestId + OLEConstants.RQST_PRBLM);
                    olePlaceRequest.setRequestId(oleDeliverRequestBo.getRequestId());
                    olePlaceRequest.setAvailableDate(getAvailableDate(itemBarcode));
                    olePlaceRequest.setBlockOverride(true);
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);

                }
                olePlaceRequest.setCode("021");
                olePlaceRequest.setRequestId(oleDeliverRequestBo.getRequestId());
                olePlaceRequest.setAvailableDate(getAvailableDate(itemBarcode));
                olePlaceRequest.setQueuePosition(String.valueOf(oleDeliverRequestBo.getBorrowerQueuePosition()));
                olePlaceRequest.setMessage(OLEConstants.RQST_SUCCESS + requestId);
                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
            } catch (Exception e) {
                LOG.error("Exception", e);
                olePlaceRequest.setBlockOverride(true);
                if (e.getMessage() != null && e.getMessage().equalsIgnoreCase("Item barcode does not exist.")) {
                    olePlaceRequest.setCode("014");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_DOESNOT_EXISTS));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }
                olePlaceRequest.setCode("023");
                olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RQST_FAIL));
                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
            olePlaceRequest.setBlockOverride(true);
            olePlaceRequest.setCode("023");
            olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RQST_FAIL));
            return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
        }
    }


    public String overridePlaceRequest(String patronBarcode, String operatorId, String itemBarcode, String requestType, String pickUpLocation, String itemIdentifier,String itemLocation,String itemType,String title,String author,String callNumber,boolean externalItem,String bibId,String requestLevel,java.sql.Date requestExpiryDate) {
        OLEPlaceRequest olePlaceRequest = new OLEPlaceRequest();
        OLEPlaceRequestConverter olePlaceRequestConverter = new OLEPlaceRequestConverter();
        ASRHelperServiceImpl asrHelperService = new ASRHelperServiceImpl();
        MaintenanceDocument newDocument = null;
        try {
            try {
                if (null == GlobalVariables.getUserSession()) {
                    Person person = personService.getPerson(operatorId);
                    String principalName = person.getPrincipalName();
                    UserSession userSession = new UserSession(principalName);
                    GlobalVariables.setUserSession(userSession);
                }
                newDocument = (MaintenanceDocument) documentService.getNewDocument(OLEConstants.REQUEST_DOC_TYPE);
            } catch (WorkflowException e) {
                e.printStackTrace();
                olePlaceRequest.setMessage("Cannot create");
                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
            }
            OleDeliverRequestBo oleDeliverRequestBo = null;
            oleDeliverRequestBo = (OleDeliverRequestBo) newDocument.getNewMaintainableObject().getDataObject();
            oleDeliverRequestBo.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
            oleDeliverRequestBo.setRequestLevel(requestLevel);
            oleDeliverRequestBo.setBibId(bibId);
            if(requestExpiryDate!=null){
                oleDeliverRequestBo.setRequestExpiryDate(requestExpiryDate);
            }
            OlePatronDocument olePatronDocument = null;
            Map<String, String> patronMap = new HashMap<String, String>();
            patronMap.put(OLEConstants.BARCODE, patronBarcode);
            OleNoticeBo oleNoticeBo = new OleNoticeBo();
            List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
            if (olePatronDocumentList.size() > 0) {
                olePatronDocument = olePatronDocumentList.get(0);
                oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
                oleDeliverRequestBo.setBorrowerBarcode(olePatronDocument.getBarcode());
                oleDeliverRequestBo.setOlePatron(olePatronDocument);
                EntityTypeContactInfoBo entityTypeContactInfoBo = olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);
                try {
                    oleNoticeBo.setPatronName(olePatronDocument.getEntity().getNames().get(0).getFirstName() + " " + oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getLastName());
                    oleNoticeBo.setPatronAddress(getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getPatronPreferredAddress(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setPatronEmailAddress(getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setPatronPhoneNumber(getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
                } catch (Exception e) {
                    LOG.error("Exception", e);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Exception Occured while setting the patron information for the patron . Patron Barcode : " + oleDeliverRequestBo.getBorrowerBarcode());
                    }
                }
            } /*else {
                olePlaceRequest.setCode("002");
                olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.NO_PATRON_INFO));
                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
            }*/
            if (requestType != null) {
                Map<String, String> requestTypeMap = new HashMap<String, String>();
                requestTypeMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_CD, requestType);
                List<OleDeliverRequestType> oleDeliverRequestTypeList = (List<OleDeliverRequestType>) getBusinessObjectService().findMatching(OleDeliverRequestType.class, requestTypeMap);
                if (oleDeliverRequestTypeList != null && (oleDeliverRequestTypeList.size() > 0)) {
                    oleDeliverRequestBo.setRequestTypeId(oleDeliverRequestTypeList.get(0).getRequestTypeId());
                    oleDeliverRequestBo.setOleDeliverRequestType(oleDeliverRequestTypeList.get(0));
                } /*else {
                    olePlaceRequest.setCode("012");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_RQST_TYP));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }*/
            }
            if (pickUpLocation != null) {
                Map<String, String> circulationDeskMap = new HashMap<String, String>();
                circulationDeskMap.put(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_CD, pickUpLocation);
                List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>) getBusinessObjectService().findMatching(OleCirculationDesk.class, circulationDeskMap);
                if (oleCirculationDeskList != null && oleCirculationDeskList.size() > 0) {
                    oleDeliverRequestBo.setPickUpLocationId(oleCirculationDeskList.get(0).getCirculationDeskId());
                    oleDeliverRequestBo.setPickUpLocationCode(oleCirculationDeskList.get(0).getCirculationDeskCode());
                    oleDeliverRequestBo.setOlePickUpLocation(oleCirculationDeskList.get(0));
                } /*else {
                    olePlaceRequest.setCode("013");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_PK_UP_LOCN));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }*/

            }
            try {
               /* if (itemBarcode == null || (itemBarcode != null && itemBarcode.isEmpty())) {
                    olePlaceRequest.setCode("014");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_DOESNOT_EXISTS));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);

                }*/
                oleDeliverRequestBo.setItemId(itemBarcode);
                oleDeliverRequestBo.setItemUuid(itemIdentifier);
                oleDeliverRequestBo.setItemStatus(OLEConstants.AVAILABLE);
                oleDeliverRequestBo.setItemType(itemType);
                oleDeliverRequestBo.setItemLocation(itemLocation);
                if (itemIdentifier == null || itemLocation == null || itemType == null) {
                    Thread.sleep(500);
                    String itemUUID = null;
                    String holdingsId = null;
                    if (itemIdentifier == null) {
                        try {
                            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                            org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
                            SearchResponse searchResponse = null;
                            search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, oleDeliverRequestBo.getItemId()), ""));
                            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "id"));
                            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.HOLDINGS.getCode(), "id"));
                            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
                            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                                    String fieldName = searchResultField.getFieldName();
                                    String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                                    if (fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("holdings")) {
                                        holdingsId = fieldValue;
                                    } else {
                                        oleDeliverRequestBo.setItemUuid(fieldValue);
                                        itemUUID = fieldValue;
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_EXIST));
                            LOG.error(OLEConstants.ITEM_EXIST + ex);
                        }
                    }
                   /* if (itemUUID == null) {
                        olePlaceRequest.setCode("014");
                        olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_DOESNOT_EXISTS));
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    }*/
                    if (itemType == null || itemLocation == null) {
                        Map<String, Object> detailMap = retrieveBIbItemHoldingData(itemUUID);
                        Bib bib = (Bib) detailMap.get(OLEConstants.BIB);
                        Item item = (Item) detailMap.get(OLEConstants.ITEM);
                        OleHoldings oleHoldings = (OleHoldings) detailMap.get(OLEConstants.HOLDING);
                        org.kuali.ole.docstore.common.document.Item item1 = (org.kuali.ole.docstore.common.document.Item) detailMap.get("documentItem");
                        if (item != null) {
                            oleDeliverRequestBo.setOleItem(item);
                            if (item.getCallNumber() != null && item.getCallNumber().getNumber() != null) {
                                oleDeliverRequestBo.setCallNumber(item.getCallNumber().getNumber());
                            }
                            if (item.getCopyNumber() != null) {
                                oleDeliverRequestBo.setCopyNumber(item.getCopyNumber());
                            }
                            oleDeliverRequestBo.setVolumeNumber(item.getEnumeration() != null ? item.getEnumeration() : "");
                        }
                        if (oleHoldings != null) {
                            if (oleDeliverRequestBo.getCallNumber() == null && oleHoldings.getCallNumber() != null && oleHoldings.getCallNumber().getNumber() != null) {
                                oleDeliverRequestBo.setCallNumber(oleHoldings.getCallNumber().getNumber());
                            }
                            if (oleDeliverRequestBo.getCopyNumber() == null && oleHoldings.getCopyNumber() != null) {
                                oleDeliverRequestBo.setCopyNumber(oleHoldings.getCopyNumber());
                            }
                        }
                        if (itemLocation == null) {
                            if (item1.getLocation() == null || (item1.getLocation() != null && item1.getLocation().trim().isEmpty())) {
                                itemLocation = getDocstoreUtil().getLocation(oleHoldings.getLocation(), new StringBuffer(""));
                            } else {
                                itemLocation = item1.getLocation();
                            }
                            oleDeliverRequestBo.setItemLocation(itemLocation);
                        }
                        if (item.getItemType() != null) {
                            oleDeliverRequestBo.setItemType(item.getItemType().getCodeValue());
                        }
                        if (item.getItemStatus() != null) {
                            oleDeliverRequestBo.setItemStatus(item.getItemStatus().getCodeValue());
                        }

                        if (bib != null) {
                            oleDeliverRequestBo.setTitle(bib.getTitle());
                            oleDeliverRequestBo.setAuthor(bib.getAuthor());
                        }
                    }
                }

                if (itemLocation != null) {
                    if (asrHelperService.isAnASRItem(itemLocation) && oleDeliverRequestBo.getItemStatus().equals(getLoanProcessor().getParameter(ASRConstants.ASR_REQUEST_ITEM_STATUS)) && !oleDeliverRequestBo.getRequestTypeCode().equals(getLoanProcessor().getParameter(ASRConstants.ASR_TYP_RQST))) {
                        olePlaceRequest.setCode("700");
                        olePlaceRequest.setMessage("Cannot create " + oleDeliverRequestBo.getRequestTypeCode() + " for this item");
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    } else if (asrHelperService.isAnASRItem(itemLocation) && !oleDeliverRequestBo.getItemStatus().equals(getLoanProcessor().getParameter(ASRConstants.ASR_REQUEST_ITEM_STATUS)) && oleDeliverRequestBo.getRequestTypeCode().equals(getLoanProcessor().getParameter(ASRConstants.ASR_TYP_RQST))) {
                        olePlaceRequest.setCode("701");
                        olePlaceRequest.setMessage("Cannot create " + oleDeliverRequestBo.getRequestTypeCode() + " for this item");
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    }
                    Map<String, String> locationMap = getLocationMap(itemLocation);
                    oleDeliverRequestBo.setItemLibrary(locationMap.get(OLEConstants.ITEM_LIBRARY));
                    oleDeliverRequestBo.setItemInstitution(locationMap.get(OLEConstants.ITEM_INSTITUTION));
                    oleDeliverRequestBo.setItemCampus(locationMap.get(OLEConstants.ITEM_CAMPUS));
                    oleDeliverRequestBo.setItemCollection(locationMap.get(OLEConstants.ITEM_COLLECTION));
                    oleDeliverRequestBo.setShelvingLocation(locationMap.get(OLEConstants.ITEM_SHELVING));
                }
                oleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_OPERATOR);
                oleDeliverRequestBo.setOperatorCreateId(operatorId);
                oleDeliverRequestBo.setItemId(itemBarcode);
                /*if (!processOperator(operatorId)) {
                    olePlaceRequest.setCode("001");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }*/
                processRequestTypeByPickUpLocation(oleDeliverRequestBo);
                //String message = this.patronRecordExpired(oleDeliverRequestBo);
               /* if (message != null) {
                    olePlaceRequest.setCode("015");
                    olePlaceRequest.setMessage(message);
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }*/

              /*  boolean requestRaised = this.isRequestAlreadyRaisedByPatron(oleDeliverRequestBo);
                if (requestRaised) {
                    {
                        olePlaceRequest.setCode("016");
                        olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RQST_ALRDY_RAISD));
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    }
                }*/
               /* boolean itemEligible = this.isItemEligible(oleDeliverRequestBo);
                if (!itemEligible) {
                    {
                        olePlaceRequest.setCode("017");
                        olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITM_NOT_LOAN));
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    }
                }*/
              /*  boolean alreadyLoaned = this.isAlreadyLoaned(oleDeliverRequestBo);
                if (alreadyLoaned) {
                    olePlaceRequest.setCode("018");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITM_LOAN_BY_PTRN));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }*/
               // Removed KRMS Rules

                OleDeliverRequestBo oleDeliverRequestBo1 = null;
                boolean asrItem = false;
                try {
                    oleDeliverRequestBo1 = oleDeliverRequestBo;
                    /*if (isItemAvailable(oleDeliverRequestBo1)) {
                        olePlaceRequest.setCode("019");
                        block
                        olePlaceRequest.setMessage(oleDeliverRequestBo1.getRequestTypeCode() + OLEConstants.RQST_CONDITION + oleDeliverRequestBo1.getItemStatus());
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    }*/
                    oleDeliverRequestBo.setOleItem(null);
                    asrItem = asrHelperService.isAnASRItem(oleDeliverRequestBo.getItemLocation());
                    if (asrItem && oleDeliverRequestBo.getItemStatus().equals(getLoanProcessor().getParameter(ASRConstants.ASR_REQUEST_ITEM_STATUS))) {
                        oleDeliverRequestBo.setAsrFlag(true);
                        oleDeliverRequestBo.setRequestStatus("1");
                    }
                    newDocument.getDocumentHeader().setDocumentDescription(OLEConstants.NEW_REQUEST_DOC);
                    newDocument.getNewMaintainableObject().setDataObject(oleDeliverRequestBo1);

                    newDocument = (MaintenanceDocument) documentService.routeDocument(newDocument, null, null);
                    oleDeliverRequestBo = (OleDeliverRequestBo) newDocument.getNewMaintainableObject().getDataObject();
                } catch (WorkflowException e) {
                    e.printStackTrace();
                }


                String requestId = "";
                try {
                    requestId = ":" + OLEConstants.OleDeliverRequest.REQUEST_ID + ":" + oleDeliverRequestBo.getRequestId();

                    if (!oleDeliverRequestBo.getRequestTypeId().equals("8")) {
                        oleDeliverRequestBo = updateLoanDocument(oleDeliverRequestBo);
                        oleDeliverRequestBo.setOlePatron(null);
                        oleDeliverRequestBo.setOleProxyPatron(null);
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Request Raised Succesfully" + requestId);
                    }
                    if (externalItem) {
                        String noticeSendParameter = getLoanProcessor().getParameter(OLEParameterConstants.NCIP_ACCEPT_ITEM_NOTICE_INDICATOR);
                        if (noticeSendParameter != null && (noticeSendParameter.trim().isEmpty() || noticeSendParameter.equalsIgnoreCase("Y"))) {
                            oleNoticeBo.setNoticeName(OLEConstants.PICKUP_NOTICE);
                            Date pickupDate = new java.sql.Date(System.currentTimeMillis());
                            if (oleDeliverRequestBo.getOlePickUpLocation().getOnHoldDays() != null) {
                                pickupDate = addDate(new java.sql.Date(System.currentTimeMillis()), new Integer(oleDeliverRequestBo.getOlePickUpLocation().getOnHoldDays()));
                            }
                            oleNoticeBo.setNoticeSpecificContent(OLEConstants.PICKUP_NOTICE_START_CONTENT + oleDeliverRequestBo.getOlePickUpLocation().getCirculationDeskPublicName() + OLEConstants.PICKUP_NOTICE_MIDDLE_CONTENT + pickupDate + OLEConstants.PICKUP_NOTICE_FINAL_CONTENT);
                            oleNoticeBo.setAuthor(author);
                            oleNoticeBo.setItemCallNumber(callNumber);
                            oleNoticeBo.setItemId(itemBarcode);
                            oleNoticeBo.setTitle(title);
                            oleNoticeBo.setCirculationDeskName(oleDeliverRequestBo.getOlePickUpLocation().getCirculationDeskPublicName());
                            oleNoticeBo.setCirculationDeskReplyToEmail(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail());
                            OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
                            String content = oleDeliverBatchService.getEmailPickUpNotice(oleNoticeBo);
                            try {
                                if (!content.trim().equals("")) {
                                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                                    if (oleDeliverRequestBo.getOlePickUpLocation() != null && StringUtils.isNotBlank(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail())) {
                                        oleMailer.sendEmail(new EmailFrom(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail()), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(content), true);
                                    } else {
                                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                                            fromAddress = OLEConstants.KUALI_MAIL;
                                        }
                                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(content), true);
                                    }
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("Mail send Successfully to " + oleNoticeBo.getPatronEmailAddress());
                                    }
                                    oleDeliverBatchService.getPdfPickUpNotice(oleNoticeBo);
                                } else {
                                    if (LOG.isDebugEnabled()) {
                                        LOG.debug("Notice Type :" + oleNoticeBo.getNoticeName() + "  " + "Item Barcode : " + oleNoticeBo.getItemId() + " " + "Patron Name :" + oleNoticeBo.getPatronName());
                                    }
                                }
                            } catch (Exception e) {
                                LOG.error("Exception", e);
                                olePlaceRequest.setCode("020");
                                olePlaceRequest.setQueuePosition(String.valueOf(oleDeliverRequestBo.getBorrowerQueuePosition()));
                                olePlaceRequest.setMessage(OLEConstants.RQST_SUCCESS + requestId + OLEConstants.NTCE_PRBLM);
                                olePlaceRequest.setRequestId(oleDeliverRequestBo.getRequestId());
                                olePlaceRequest.setAvailableDate(getAvailableDate(itemBarcode));
                                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                            }
                        }
                    }

                } catch (Exception e) {
                    LOG.error("Exception", e);
                    olePlaceRequest.setCode("020");
                    olePlaceRequest.setQueuePosition(String.valueOf(oleDeliverRequestBo.getBorrowerQueuePosition()));
                    olePlaceRequest.setMessage(OLEConstants.RQST_SUCCESS + requestId + OLEConstants.RQST_PRBLM);
                    olePlaceRequest.setRequestId(oleDeliverRequestBo.getRequestId());
                    olePlaceRequest.setAvailableDate(getAvailableDate(itemBarcode));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);

                }
                olePlaceRequest.setCode("021");
                olePlaceRequest.setRequestId(oleDeliverRequestBo.getRequestId());
                olePlaceRequest.setAvailableDate(getAvailableDate(itemBarcode));
                olePlaceRequest.setQueuePosition(String.valueOf(oleDeliverRequestBo.getBorrowerQueuePosition()));
                olePlaceRequest.setMessage(OLEConstants.RQST_SUCCESS + requestId);
                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
            } catch (Exception e) {
                LOG.error("Exception", e);
               /* if (e.getMessage() != null && e.getMessage().equalsIgnoreCase("Item barcode does not exist.")) {
                    olePlaceRequest.setCode("014");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_DOESNOT_EXISTS));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }*/
                olePlaceRequest.setCode("023");
                olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RQST_FAIL));
                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
            olePlaceRequest.setCode("023");
            olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.RQST_FAIL));
            return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
        }
    }

    public String getAvailableDate(String itemBarcode) {
        Map<String, String> loanMap = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern(OLEConstants.GREGORIAN_PATTERN);
        loanMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, itemBarcode);
        String date = null;
        List<OleLoanDocument> loanDocuments = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
        if (loanDocuments.size() > 0) {
            if (loanDocuments.get(0).getLoanDueDate() != null) {
                date = loanDocuments.get(0).getLoanDueDate().toString();
            } else {
                date = simpleDateFormat.format(new Timestamp(2025, 1, 1, 1, 1, 1, 1));
            }
        } else {
            date = simpleDateFormat.format(new Timestamp(new java.sql.Date(System.currentTimeMillis()).getTime()));
        }
        return date;
    }


    public java.sql.Date addDate(java.sql.Date in, int daysToAdd) {
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
        List<OleDeliverRequestBo> asrList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> requestsByBorrower = new ArrayList<OleDeliverRequestBo>();
        Engine engine = KrmsApiServiceLocator.getEngine();
        ContextDefinition contextDefinition = KrmsRepositoryServiceLocator.getContextBoService().getContextByNameAndNamespace("OLE-CONTEXT","OLE");
        AgendaDefinition agendaDefinition = KrmsRepositoryServiceLocator.getAgendaBoService().getAgendaByNameAndContextId(OLEConstants.REQUEST_AGENDA_NM,contextDefinition.getId());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(OLEConstants.AGENDA_NAME,agendaDefinition.getName());
        List<MatchBo> matchBos = (List<MatchBo>) getBusinessObjectService().findMatching(MatchBo.class, map);

        SelectionCriteria selectionCriteria =
                SelectionCriteria.createCriteria(null, getSelectionContext(contextDefinition.getName()), getAgendaContext(OLEConstants.REQUEST_AGENDA_NM));
        EngineResults engineResult = null;

        if (agendaDefinition != null ) {

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
            OleLoanDocument oleLoanDocument = getLoanProcessor().getOleLoanDocumentUsingItemUUID(oleDeliverRequestBo.getItemUuid());
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
            dataCarrierService.addData(OLEConstants.LOANED_DATE, oleLoanDocument != null ? oleLoanDocument.getCreateDate() : null);
            dataCarrierService.addData(OLEConstants.DUE_DATE,oleLoanDocument!=null?oleLoanDocument.getLoanDueDate():null);
            String patronId = oleDeliverRequestBo.getBorrowerId()!=null ?  oleDeliverRequestBo.getBorrowerId() : "";
            String itemId = oleDeliverRequestBo.getItemId()!=null ?  oleDeliverRequestBo.getItemId() : "";
            dataCarrierService.removeData(patronId+itemId);
            String borrowerId = oleDeliverRequestBo.getBorrowerId();

            List<FeeType> feeTypeList = getOleCirculationPolicyService().getPatronBillPayment(oleDeliverRequestBo.getBorrowerId());
            Integer overdueFineAmt = 0;
            Integer replacementFeeAmt = 0;
            Integer serviceFeeAmt = 0;
            for (FeeType feeType : feeTypeList) {
                Integer fineAmount = feeType.getFeeAmount().subtract(feeType.getPaidAmount()).intValue();
                overdueFineAmt += feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.OVERDUE_FINE) ? fineAmount : 0;
                replacementFeeAmt += feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.REPLACEMENT_FEE) ? fineAmount : 0;
                serviceFeeAmt += feeType.getOleFeeType().getFeeTypeName().equalsIgnoreCase(OLEConstants.SERVICE_FEE) ? fineAmount : 0;
            }
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
            }else if (requestTypeId != null && (requestTypeId.equals("9"))) {
                requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "9");
                asrList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
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
            termValues.put(OLEConstants.MAX_NO_OF_ASR_REQUEST, new Integer(asrList.size()) + 1);
            termValues.put(OLEConstants.FINE_AMOUNT, overdueFineAmt + replacementFeeAmt + serviceFeeAmt);
            // termValues.put("maxNumberOfRequestByBorrower",requestsByBorrower.size());
            termValues.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, requestTypeId);
            termValues.put(OLEConstants.REQUEST_TYPE, requestType);
            termValues.put(OLEConstants.PATRON_ID_POLICY, patronId);
            termValues.put(OLEConstants.ITEM_ID_POLICY, itemId);

            for (Iterator<MatchBo> matchBoIterator = matchBos.iterator(); matchBoIterator.hasNext(); ) {
                MatchBo matchBo = matchBoIterator.next();
                factBuilder.addFact(matchBo.getTermName(), termValues.get((matchBo.getTermName())));
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("termValues.toString()" + termValues.toString());
            }
            engineResult = engine.execute(selectionCriteria, factBuilder.build(), executionOptions);
            dataCarrierService.removeData(patronId+itemId);
            List<String> errorMessage = (List<String>) engineResult.getAttribute(OLEConstants.ERROR_ACTION);
            java.sql.Date d = (java.sql.Date) engineResult.getAttribute(OLEConstants.REQ_EXPIRATION_DATE);
            Timestamp recallDueDate = (Timestamp) engineResult.getAttribute(OLEConstants.RECALL_DUE_DATE);
            String notice = (String) engineResult.getAttribute(OLEConstants.NOTICE);
            oleDeliverRequestBo.setNoticeType(notice);
            if(oleDeliverRequestBo.getRequestExpiryDate()==null){
            oleDeliverRequestBo.setRequestExpiryDate(d);
            }
            StringBuffer failures = new StringBuffer();
            if (errorMessage != null && errorMessage.size() > 0) {
                int i = 1;
                for (String errMsg : errorMessage) {
                    failures.append(i++ + ". " + errMsg + OLEConstants.BREAK);
                }
            }
            if (!failures.toString().isEmpty()) {
                oleDeliverRequestBo.setMessage(failures.toString());
            }else if(failures.toString().trim().isEmpty()){

            if(oleLoanDocument!=null && (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2"))){
            Timestamp itemDueDate = null;
            if(ObjectUtils.isNotNull(oleLoanDocument)){
              itemDueDate = oleLoanDocument.getLoanDueDate();
            }
            Item oleItem = oleDeliverRequestBo.getOleItem();
            if(itemDueDate == null && recallDueDate!=null){
               // oleDeliverRequestBo.setOriginalDueDate((new java.sql.Date(itemDueDate.getTime())));
                oleDeliverRequestBo.setNewDueDate(new java.sql.Date(recallDueDate.getTime()));
                oleLoanDocument.setLoanDueDate(recallDueDate);
                oleDeliverRequestBo.setRecallDueDate(recallDueDate);
                oleItem.setDueDateTime(recallDueDate.toString());
                getBusinessObjectService().save(oleLoanDocument);
                OleCirculationDesk oleCirculationDesk = oleLoanDocument.getCirculationLocationId() != null ?
                        getLoanProcessor().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId()) : null;
                oleLoanDocument.setOleCirculationDesk(oleCirculationDesk);
                OLEDeliverNoticeHelperService oleDeliverNoticeHelperService =getOleDeliverNoticeHelperService();
                oleDeliverNoticeHelperService.deleteDeliverNotices(oleLoanDocument.getLoanId());
                try{
/*                oleDeliverNoticeHelperService.generateDeliverNotices(oleLoanDocument.getPatronId(), oleLoanDocument.getItemUuid(),
                        oleLoanDocument.getOleCirculationDesk()!=null ? oleLoanDocument.getOleCirculationDesk().getCirculationDeskCode() : null,
                        oleLoanDocument.getBorrowerTypeCode(),itemType, oleDeliverRequestBo.getItemStatus(),
                        oleLoanDocument.isClaimsReturnedIndicator() ? OLEConstants.TRUE : OLEConstants.FALSE,
                        oleLoanDocument.getRepaymentFeePatronBillId() != null ? OLEConstants.TRUE : OLEConstants.FALSE,
                        oleDeliverRequestBo.getShelvingLocation(), oleDeliverRequestBo.getItemCollection(), oleDeliverRequestBo.getItemLibrary(),
                        oleDeliverRequestBo.getItemCampus(), oleDeliverRequestBo.getItemInstitution(), oleLoanDocument.getLoanDueDate(),oleLoanDocument.getLoanId());*/
                    List<OLEDeliverNotice> deliverNotices = (List<OLEDeliverNotice>) engineResult.getAttribute("deliverNotices");
                    if(deliverNotices!=null){
                        for(OLEDeliverNotice deliverNotice : deliverNotices){
                            deliverNotice.setLoanId(oleLoanDocument.getLoanId());
                            deliverNotice.setPatronId(oleLoanDocument.getPatronId());
                        }
                        getBusinessObjectService().save(deliverNotices);
                    }
                }catch(Exception e){
                    LOG.info("Exception occured while updating the date in notice table");
                    LOG.error(e,e);
                }
                    oleItem.setDueDateTime(getLoanProcessor().convertDateToString(recallDueDate,"MM/dd/yyyy HH:mm:ss"));
                try{
                   updateItem(oleItem);
                }catch(Exception e){
                    if(LOG.isInfoEnabled()){
                        LOG.info("Exception occured while updating the item . " +e.getMessage() );
                    }
                    LOG.error(e,e);
                }
            }
            if (recallDueDate != null && itemDueDate!=null ) {
               // if(itemDueDate.compareTo(recallDueDate) > 0){
                oleDeliverRequestBo.setOriginalDueDate((new java.sql.Date(itemDueDate.getTime())));
                oleDeliverRequestBo.setNewDueDate(new java.sql.Date(recallDueDate.getTime()));
                oleLoanDocument.setLoanDueDate(recallDueDate);
                oleDeliverRequestBo.setRecallDueDate(recallDueDate);
                getBusinessObjectService().save(oleLoanDocument);
                    OleCirculationDesk oleCirculationDesk = oleLoanDocument.getCirculationLocationId() != null ?
                            getLoanProcessor().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId()) : null;
                    oleLoanDocument.setOleCirculationDesk(oleCirculationDesk);
                    OLEDeliverNoticeHelperService oleDeliverNoticeHelperService =getOleDeliverNoticeHelperService();
                    oleDeliverNoticeHelperService.deleteDeliverNotices(oleLoanDocument.getLoanId());
                    try{
/*                    oleDeliverNoticeHelperService.generateDeliverNotices(oleLoanDocument.getPatronId(), oleLoanDocument.getItemUuid(),
                            oleLoanDocument.getOleCirculationDesk()!=null ? oleLoanDocument.getOleCirculationDesk().getCirculationDeskCode() : null,
                            oleLoanDocument.getBorrowerTypeCode(),itemType, oleDeliverRequestBo.getItemStatus(),
                            oleLoanDocument.isClaimsReturnedIndicator() ? OLEConstants.TRUE : OLEConstants.FALSE,
                            oleLoanDocument.getRepaymentFeePatronBillId() != null ? OLEConstants.TRUE : OLEConstants.FALSE,
                            oleDeliverRequestBo.getShelvingLocation(), oleDeliverRequestBo.getItemCollection(), oleDeliverRequestBo.getItemLibrary(),
                            oleDeliverRequestBo.getItemCampus(), oleDeliverRequestBo.getItemInstitution(), oleLoanDocument.getLoanDueDate(),oleLoanDocument.getLoanId());*/
                        List<OLEDeliverNotice> deliverNotices = (List<OLEDeliverNotice>) engineResult.getAttribute("deliverNotices");
                        if(deliverNotices!=null){
                            for(OLEDeliverNotice deliverNotice : deliverNotices){
                                deliverNotice.setLoanId(oleLoanDocument.getLoanId());
                                deliverNotice.setPatronId(oleLoanDocument.getPatronId());
                            }
                            getBusinessObjectService().save(deliverNotices);
                        }
                    }catch(Exception e){
                        LOG.info("Exception occured while updating the date in notice table");
                        LOG.error(e,e);
                    }
                        oleItem.setDueDateTime(getLoanProcessor().convertDateToString(recallDueDate,"MM/dd/yyyy HH:mm:ss"));
                    try{
                        updateItem(oleItem);
                    }catch(Exception e){
                        if(LOG.isInfoEnabled()){
                            LOG.info("Exception occured while updating the item . " +e.getMessage() );
                        }
                        LOG.error(e,e);
                    }
               /* }else{
                    oleDeliverRequestBo.setNewDueDate((new java.sql.Date(oleLoanDocument.getLoanDueDate().getTime())));
                    oleDeliverRequestBo.setOriginalDueDate((new java.sql.Date(oleLoanDocument.getLoanDueDate().getTime())));
                }*/
            }
          }
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

    public OleCirculationDesk getOleCirculationDesk(String circulationDeskId) {
        Map<String, String> circulationDeskMap = new HashMap<String, String>();
        circulationDeskMap.put(OLEConstants.CIRCULATION_DESK_ID, circulationDeskId);
        List<OleCirculationDesk> oleCirculationDeskList = (List<OleCirculationDesk>) getBusinessObjectService().findMatching(OleCirculationDesk.class, circulationDeskMap);
        if (oleCirculationDeskList != null && oleCirculationDeskList.size() > 0) {
            return oleCirculationDeskList.get(0);
        } else
            return null;
    }

    private RoleService getRoleService() {
        RoleService service = KimApiServiceLocator.getRoleService();
        return service;
    }
    public boolean checkForOverdueNotice(Date expDate){
        Date curDat = new Date();
        if(expDate!=null && curDat.compareTo(expDate)<=0){
            return  false;
        }
        return true;
    }


    public Map<String,String> getLocationMap(String itemLocation){
        Map<String,String> locationMap = new HashMap<String,String>();
        String[] locationArray =  itemLocation.split("['/']");
        List<String> locationList = Arrays.asList(locationArray);
        for(String value : locationList){
            Map<String,String> requestMap =  new HashMap<>();
            requestMap.put(OLEConstants.LOCATION_CODE,value);
            List<OleLocation> oleLocations = (List<OleLocation>)getBusinessObjectService().findMatching(OleLocation.class,requestMap);
            if(oleLocations!=null && oleLocations.size()>0){
                String locationLevelId = oleLocations.get(0).getLevelId();
                requestMap.clear();
                requestMap.put(OLEConstants.LEVEL_ID,locationLevelId);
                List<OleLocationLevel> oleLocationLevels = (List<OleLocationLevel>)getBusinessObjectService().findMatching(OleLocationLevel.class,requestMap);
                if(oleLocationLevels!=null && oleLocationLevels.size()>0){
                    OleLocationLevel oleLocationLevel = new OleLocationLevel();
                    oleLocationLevel = oleLocationLevels.get(0);
                    if(oleLocationLevel.getLevelCode().equals(OLEConstants.OLEBatchProcess.LOCATION_LEVEL_CAMPUS)){
                        locationMap.put(OLEConstants.ITEM_CAMPUS, value);
                    }else if(oleLocationLevel.getLevelCode().equals(OLEConstants.OLEBatchProcess.LOCATION_LEVEL_INSTITUTION)){
                        locationMap.put(OLEConstants.ITEM_INSTITUTION,value);
                    }else if(oleLocationLevel.getLevelCode().equals(OLEConstants.OLEBatchProcess.LOCATION_LEVEL_COLLECTION)){
                        locationMap.put(OLEConstants.ITEM_COLLECTION,value);
                    }else if(oleLocationLevel.getLevelCode().equals(OLEConstants.OLEBatchProcess.LOCATION_LEVEL_LIBRARY)){
                        locationMap.put(OLEConstants.ITEM_LIBRARY,value);
                    }else if(oleLocationLevel.getLevelCode().equals(OLEConstants.OLEBatchProcess.LOCATION_LEVEL_SHELVING)){
                        locationMap.put(OLEConstants.ITEM_SHELVING,value);
                    }
                }
            }
        }
        return locationMap;
    }



    public  org.kuali.ole.docstore.common.document.Item retrieveItemWithBibAndHoldingData(String itemUUID){
        org.kuali.ole.docstore.common.document.Item item = null;
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        HoldingOlemlRecordProcessor holdingsOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        try{
            //retrieve the item information from docstore
            item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemUUID);
            //retrieve item content
            Item item1 = itemOlemlRecordProcessor.fromXML(item.getContent());
            item = populateInfoFromInstanceItemToDocItem(item1,item);
            OleHoldings oleHoldings = holdingsOlemlRecordProcessor.fromXML(item.getHolding().getContent());
            Holdings holdings = populateInfoFromInstanceHoldingToDocHoldings(oleHoldings,item.getHolding());
            item.setHolding(holdings);
        }catch (Exception e){
            LOG.error(e);
        }
        return item;
    }



    public org.kuali.ole.docstore.common.document.Item populateInfoFromInstanceItemToDocItem(Item instanceItem,org.kuali.ole.docstore.common.document.Item  documentItem){

        documentItem.setBarcode(instanceItem.getAccessInformation().getBarcode());
        documentItem.setAnalytic(Boolean.valueOf(instanceItem.getAnalytic()));
        if(instanceItem.getCallNumber()!=null){
            documentItem.setCallNumber(instanceItem.getCallNumber().getNumber());
            documentItem.setCallNumberType(instanceItem.getCallNumber().getType());
            documentItem.setCallNumberPrefix(instanceItem.getCallNumber().getPrefix());
        }
        documentItem.setChronology(instanceItem.getChronology());
        documentItem.setCopyNumber(instanceItem.getCopyNumber());
        if(instanceItem.getItemStatus()!=null){
            documentItem.setItemStatus(instanceItem.getItemStatus().getCodeValue());
        }
        if(instanceItem.getItemType()!=null){
            documentItem.setItemType(instanceItem.getItemType().getCodeValue());
        }
        documentItem.setEnumeration(instanceItem.getEnumeration());
        documentItem.setVolumeNumber(instanceItem.getVolumeNumber());

        return documentItem;
    }

    public org.kuali.ole.docstore.common.document.Holdings populateInfoFromInstanceHoldingToDocHoldings(OleHoldings oleHoldings ,Holdings holdings) {
        if(oleHoldings.getCallNumber()!=null) {
            holdings.setCallNumber(oleHoldings.getCallNumber().getNumber());
            holdings.setCallNumberPrefix(oleHoldings.getCallNumber().getPrefix());
            holdings.setCallNumberType(oleHoldings.getCallNumber().getType());
        }
        holdings.setCopyNumber(oleHoldings.getCopyNumber());
        holdings.setHoldingsType(oleHoldings.getHoldingsType());
      //  holdings.setLocationName(getLocation(oleHoldings.getLocation()));
        return holdings;
    }



    public Map retrieveBIbItemHoldingData(String itemUUID){
        Map<String,Object> bibMap  = new HashMap<String,Object>();
        org.kuali.ole.docstore.common.document.Item item = null;
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        HoldingOlemlRecordProcessor holdingsOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        try{
            item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemUUID); // retrieving item object using itemUUID.
            bibMap.put(OLEConstants.DOCUMENT_ITEM,item);
            bibMap.put(OLEConstants.BIB,item.getHolding().getBib()); // retrieving bib object from holding.
            Item item1 = itemOlemlRecordProcessor.fromXML(item.getContent());
            bibMap.put(OLEConstants.ITEM,item1);
            OleHoldings oleHoldings = holdingsOlemlRecordProcessor.fromXML(item.getHolding().getContent());
            bibMap.put(OLEConstants.HOLDING,oleHoldings); // retrieving holding object.
        }catch (Exception e){
            LOG.error(e);
        }
        return bibMap; // set all the objects in Map.
    }

    private void getNoticeList(List<OleLoanDocument> oleLoanDocuments, OlePatronDocument olePatronDocument, boolean overdue,List<String> itemUUIDS) {
        Long b1 = System.currentTimeMillis();
        List<OleLoanDocument> oleOverDueNoticeBoList = new ArrayList<>();
        List<OleLoanDocument> oleCourtesyNoticeList = new ArrayList<>();
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        StringBuffer mailContent = new StringBuffer();
        Document document=null;
        OutputStream outputStream = null;
        Set<String> overdueItemLocations = new HashSet<>();
        Set<String> courtesyItemLocations = new HashSet<>();
        try {
            String overdueNoticeType = getLoanProcessor().getParameter(OLEParameterConstants.OVERDUE_NOTICE_TYPE);
            String courtesyNoticeType = getLoanProcessor().getParameter(OLEParameterConstants.COURTESY_NOTICE_TYPE);
            DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);

     //          HashMap<String, org.kuali.ole.docstore.common.document.Item> itemMap = getDocstoreClientLocator().getDocstoreClient().retrieveItemMap(itemIds);

            mailContent.append(oleDeliverBatchService.getHeaderAndPatronContent(olePatronDocument, overdue));
            oleDeliverBatchService.getHeaderAndPatronPDFContent(olePatronDocument,overdue);
            document=oleDeliverBatchService.getOverdueDocument();
            outputStream=oleDeliverBatchService.getOverdueOutPutStream();
            for (OleLoanDocument oleLoanDocument : oleLoanDocuments) {
/*                org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                item = itemMap.get(oleLoanDocument.getItemUuid());
                oleLoanDocument.setInstanceUuid(item.getHolding().getId());
                String itemXml = item.getContent();
                oleLoanDocument.setTitle(item.getHolding().getBib().getTitle());
                oleLoanDocument.setAuthor(item.getHolding().getBib().getAuthor());
                oleLoanDocument.setItemVolumeNumber(item.getVolumeNumber());*/
/*                Item oleItem = getLoanProcessor().getItemPojo(itemXml);
                if (oleItem != null && oleItem.getCallNumber() != null) {
                    oleLoanDocument.setItemCallNumber(oleItem.getCallNumber().getNumber());
                }
                oleLoanDocument.setOleItem(oleItem);*/
                oleLoanDocument.setOlePatron(olePatronDocument);
                Date dueDate = oleLoanDocument.getLoanDueDate();
                Date currentDate = new Date();
                Integer dueDateDiffInterval = determineDifferenceInDays(dueDate, currentDate);
                DateFormat formatter = new SimpleDateFormat(OLEConstants.DAT_FORMAT_EFFECTIVE_NOTICE);
                if (LOG.isInfoEnabled()) {
                    LOG.info("oleLoanDocument---->ItemID--"+oleLoanDocument.getItemId()+": Patron Barcode--"+oleLoanDocument.getPatronBarcode());
                    LOG.info("oleItem.getItemStatusEffectiveDate()" + oleLoanDocument.getItemStatusEffectiveDate());
                }
                Date itemStatusEffectiveDate = (Date) formatter.parse(oleLoanDocument.getItemStatusEffectiveDate());
                Integer numberOfDaysOnHold = determineDifferenceInDays(itemStatusEffectiveDate, currentDate);
                Integer dueDateSumInterval = determineDifferenceInDays(currentDate, dueDate);
                Integer overdueNoticeInterval = Integer.parseInt(getIntervalForOverdueNotice());
                Integer intervalWithNoticeCount = 0;
                Integer loanNoOfOverdueNoticeSent = oleLoanDocument.getNumberOfOverdueNoticesSent() != null ? Integer.parseInt(oleLoanDocument.getNumberOfOverdueNoticesSent()) : 0;
                if (oleLoanDocument.getNumberOfOverdueNoticesSent() != null) {
                    intervalWithNoticeCount = Integer.parseInt(oleLoanDocument.getNumberOfOverdueNoticesSent()) + 1;
                } else {
                    intervalWithNoticeCount = intervalWithNoticeCount + 1;
                }
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
                    oleCirculationDesk = getLoanProcessor().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId());
                    String maxNumOfDays = oleCirculationDesk.getOnHoldDays() != null ? oleCirculationDesk.getOnHoldDays() : getLoanProcessor().getParameter(OLEConstants.MAX_NO_OF_DAYS_ON_HOLD);
                    maxNumberOfDaysOnHold = new Integer(maxNumOfDays);
                }
                if(oleLoanDocument.getItemTypeName()!=null){
                    oleLoanDocument.setItemType(getItemTypeCodeByName(oleLoanDocument.getItemTypeName()));
                }

/*                if (oleItem.getTemporaryItemType() != null && oleItem.getTemporaryItemType().getCodeValue() != "") {
                    OleInstanceItemType oleInstanceItemType = getLoanProcessor().getItemTypeIdByItemType(oleItem.getTemporaryItemType().getCodeValue());
                    oleLoanDocument.setItemTypeName(oleInstanceItemType.getInstanceItemTypeCode());
                } else if (oleItem.getItemType() != null && oleItem.getItemType().getCodeValue() != "") {
                    OleInstanceItemType oleInstanceItemType = getLoanProcessor().getItemTypeIdByItemType(oleItem.getItemType().getCodeValue());
                    oleLoanDocument.setItemTypeName(oleInstanceItemType.getInstanceItemTypeCode());
                }*/
                OleDeliverRequestBo oleDeliverRequestBo = getLoanProcessor().getPrioritizedRequest(oleLoanDocument.getItemUuid());
     /*           getLoanProcessor().getLocation(oleItem, oleLoanDocument);*/

/*                String agendaName = OLEConstants.NOTICE_AGENDA_NM;
                String patronId = oleLoanDocument.getPatronId()!=null ?  oleLoanDocument.getPatronId() : "";
                String itemId = oleLoanDocument.getItemId()!=null ?  oleLoanDocument.getItemId() : "";
                dataCarrierService.removeData(patronId+itemId);
                HashMap<String, Object> termValues = new HashMap<String, Object>();
                Date expirationDate = olePatronDocument != null ? olePatronDocument.getExpirationDate() : null;
                termValues.put(OLEConstants.BORROWER_TYPE, olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                termValues.put(OLEConstants.ITEM_TYPE, oleLoanDocument.getItemType());
                termValues.put(OLEConstants.OVERLAY_ITEM_LOCATION, oleLoanDocument.getItemLocation());
                termValues.put(OLEConstants.DUE_DATE_DIFF_INTERVAL, dueDateDiffInterval);
                if (oleLoanDocument.getItemStatus() != null)
                    termValues.put(OLEConstants.ITEM_STATUS, oleLoanDocument.getItemStatus());
                termValues.put(OLEConstants.NO_OF_DAYS_ON_HOLD, numberOfDaysOnHold);
                termValues.put(OLEConstants.MAX_NO_OF_DAYS_ONHOLD, maxNumberOfDaysOnHold);
                termValues.put(OLEConstants.OleDeliverRequest.CLAIM_RETURNED, oleLoanDocument.isClaimsReturnedIndicator() ? "true" : "false");
                termValues.put(OLEConstants.OleDeliverRequest.REPLACEMENT_FEE_PATRON_BILL, oleLoanDocument.getRepaymentFeePatronBillId() != null ? "true" : "false");
                termValues.put(OLEConstants.OleDeliverRequest.NUBER_OF_OVER_DUE_SENT, loanNoOfOverdueNoticeSent);
                termValues.put(OLEConstants.OleDeliverRequest.DUE_DATE_SUM_INTERVAL, dueDateSumInterval);
                termValues.put(OLEConstants.OleDeliverRequest.CONFIGURABLE_INTERVAL, Integer.parseInt(courtesyInterval));
                termValues.put(OLEConstants.OleDeliverRequest.INTERVAL_WITH_NOTICE_COUNT, intervalWithNoticeCount);
                termValues.put(OLEConstants.ITEM_SHELVING, oleLoanDocument.getItemLocation());
                termValues.put(OLEConstants.ITEM_COLLECTION, oleLoanDocument.getItemCollection());
                termValues.put(OLEConstants.ITEM_LIBRARY, oleLoanDocument.getItemLibrary());
                termValues.put(OLEConstants.ITEM_CAMPUS, oleLoanDocument.getItemCampus());
                termValues.put(OLEConstants.ITEM_INSTITUTION, oleLoanDocument.getItemInstitution());
                termValues.put(OLEConstants.REQUEST_TYPE, oleDeliverRequestBo != null ? oleDeliverRequestBo.getRequestTypeCode() : null);
                termValues.put(OLEConstants.EXPIR_DATE, expirationDate);
                termValues.put(OLEConstants.PATRON_ID_POLICY, patronId);
                termValues.put(OLEConstants.ITEM_ID_POLICY, itemId);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("termValues.toString()" + termValues.toString());
                }
                EngineResults engineResults = getLoanProcessor().getEngineResults(agendaName, termValues);
                dataCarrierService.removeData(patronId+itemId);
                String notice = (String) engineResults.getAttribute(OLEConstants.NOTICE);
                String itemStatus = (String) engineResults.getAttribute(OLEConstants.ITEM_STATUS);
                BigDecimal replacementBill = (BigDecimal) engineResults.getAttribute(OLEConstants.REPLACEMENT_BILL);
                List<String> errorMessage = (List<String>) engineResults.getAttribute(OLEConstants.ERROR_ACTION);
                if (errorMessage != null && LOG.isDebugEnabled()) {
                    LOG.debug("errorMessage" + errorMessage.toString());
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("notice" + notice);
                }
                agendaName = OLEConstants.BATCH_PROGRAM_AGENDA;
                termValues = new HashMap<String, Object>();
                String deskLocation = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskCode() : "";
                String deskLocationName = oleCirculationDesk != null ? oleCirculationDesk.getCirculationDeskPublicName() : "";
                termValues.put(OLEConstants.BORROWER_TYPE, olePatronDocument.getOleBorrowerType().getBorrowerTypeCode());
                termValues.put(OLEConstants.DESK_LOCATION, deskLocation);
                termValues.put(OLEConstants.NOTICE, notice);
                engineResults = getLoanProcessor().getEngineResults(agendaName, termValues);
                String noticeType = (String) engineResults.getAttribute(OLEConstants.NOTICE_TYPE);
                errorMessage = (List<String>) engineResults.getAttribute(OLEConstants.ERROR_ACTION);
                if (errorMessage != null && LOG.isDebugEnabled()) {
                    LOG.debug("errorMessage" + errorMessage);
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("noticeType" + noticeType);
                }*/
                String noticeType = oleLoanDocument.getNoticeType();
                if (oleLoanDocument.getReplacementBill()!=null  && oleLoanDocument.getReplacementBill().intValue() > 0 && noticeType.equalsIgnoreCase(OLEConstants.NOTICE_LOST)) {
                    int noOfOverdueNoticeSent = Integer.parseInt(oleLoanDocument.getNumberOfOverdueNoticesSent() != null ? oleLoanDocument.getNumberOfOverdueNoticesSent() : "0");
                    noOfOverdueNoticeSent = noOfOverdueNoticeSent + 1;
                    oleLoanDocument.setNumberOfOverdueNoticesSent(Integer.toString(noOfOverdueNoticeSent));
                    oleLoanDocument.setOverDueNoticeDate(new java.sql.Date(System.currentTimeMillis()));
                    String billNumber = getLoanProcessor().generatePatronBillPayment(oleLoanDocument, OLEConstants.REPLACEMENT_FEE, oleLoanDocument.getReplacementBill());
                    oleLoanDocument.setRepaymentFeePatronBillId(billNumber);
                    getBusinessObjectService().save(oleLoanDocument);
                     itemUUIDS.add(oleLoanDocument.getItemUuid());
                  /*  List<String> itemUUIDs = new ArrayList<>();
                    itemUUIDs.add(oleLoanDocument.getItemUuid());
                    HashMap<String, org.kuali.ole.docstore.common.document.Item> itemMaps = getDocstoreClientLocator().getDocstoreClient().retrieveItemMap(itemUUIDs);
                   org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                    item = itemMaps.get(oleLoanDocument.getItemUuid());
                    String itemXml = item.getContent();
                     Item oleItem = getLoanProcessor().getItemPojo(itemXml);
                     getLoanProcessor().updateItemStatus(oleItem, OLEConstants.ITEM_STATUS_LOST);
                  */  getBusinessObjectService().delete(oleLoanDocument.getOleDeliverNotice());
                    saveOLEDeliverNoticeHistory(oleLoanDocument);
                }

                    if (noticeType.equalsIgnoreCase(OLEConstants.NOTICE_OVERDUE)) {
                        oleOverDueNoticeBoList.add(oleLoanDocument);
                        mailContent.append(oleDeliverBatchService.getOverdueNoticeHTMLContent(oleLoanDocument));
                        overdueItemLocations.add(oleLoanDocument.getItemLocation());
                        noticeType = noticeType == null ? overdueNoticeType : noticeType;
                        oleDeliverBatchService.getOverdueNoticePDFContent(oleLoanDocument,overdue,document);
                        int noOfOverdueNoticeSent = Integer.parseInt(oleLoanDocument.getNumberOfOverdueNoticesSent() != null ? oleLoanDocument.getNumberOfOverdueNoticesSent() : "0");
                        noOfOverdueNoticeSent = noOfOverdueNoticeSent + 1;
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Updated Loan Record : " + oleLoanDocument);
                        }
                        oleLoanDocument.setNumberOfOverdueNoticesSent(Integer.toString(noOfOverdueNoticeSent));
                        oleLoanDocument.setOverDueNoticeDate(new java.sql.Date(System.currentTimeMillis()));
                        getBusinessObjectService().save(oleLoanDocument);
                        getBusinessObjectService().delete(oleLoanDocument.getOleDeliverNotice());
                        saveOLEDeliverNoticeHistory(oleLoanDocument);
                    } else if (noticeType.equalsIgnoreCase(OLEConstants.NOTICE_COURTESY) && olePatronDocument.isCourtesyNotice() && !oleLoanDocument.isCourtesyNoticeFlag()) {
                        oleCourtesyNoticeList.add(oleLoanDocument);
                        mailContent.append(oleDeliverBatchService.getOverdueNoticeHTMLContent(oleLoanDocument));
                        courtesyItemLocations.add(oleLoanDocument.getItemLocation());
                        oleDeliverBatchService.getOverdueNoticePDFContent(oleLoanDocument,overdue,document);
                        noticeType = noticeType == null ? courtesyNoticeType : noticeType;
                        oleLoanDocument.setCourtesyNoticeFlag(!overdue);
                        getBusinessObjectService().save(oleLoanDocument);
                        getBusinessObjectService().delete(oleLoanDocument.getOleDeliverNotice());
                        saveOLEDeliverNoticeHistory(oleLoanDocument);
                    }

                olePatronDocument = oleLoanDocument.getOlePatron();
                if (noticeType != null && noticeType.equalsIgnoreCase(OLEConstants.SMS)) {
                    //TODO : sms in progress.
                }
            }

        } catch (Exception e) {
            LOG.error("Exception in generateNotices()" + e.getMessage(), e);
        }
        Long b2 = System.currentTimeMillis();
        Long b3 = b2 - b1;
        LOG.info("Time taken to send the notices " + b3);
        boolean isFileNeedToDelete=true;
        if (overdue) {
            if (oleOverDueNoticeBoList != null && oleOverDueNoticeBoList.size() > 0) {
                String replyToEmail = null;
                if (overdueItemLocations.size() == 1) {
                    replyToEmail = getLoanProcessor().getReplyToEmail(overdueItemLocations.iterator().next());
                }
                if (replyToEmail != null) {
                    sendMailsToPatron(olePatronDocument, mailContent.toString(), replyToEmail);
                } else {
                    String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                    sendMailsToPatron(olePatronDocument, mailContent.toString(), fromAddress);
                }
                try {
                    if (document != null && outputStream!=null) {
                        oleDeliverBatchService.getPdfFooter(document,outputStream);
                        isFileNeedToDelete=false;
                    }
                } catch (Exception e){
                    LOG.error("PDF overdue notice error");
                }

            }
        } else {
            if (oleCourtesyNoticeList != null && oleCourtesyNoticeList.size() > 0) {
                String replyToEmail = null;
                if (courtesyItemLocations.size() == 1) {
                    replyToEmail = getLoanProcessor().getReplyToEmail(courtesyItemLocations.iterator().next());
                }
                if (replyToEmail != null) {
                    sendMailsToPatron(olePatronDocument, mailContent.toString(), replyToEmail);
                } else {
                    String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                    sendMailsToPatron(olePatronDocument, mailContent.toString(), fromAddress);
                }
                try {
                    if (document != null && outputStream!=null) {
                        oleDeliverBatchService.getPdfFooter(document,outputStream);
                        isFileNeedToDelete=false;
                    }
                } catch (Exception e){
                    LOG.error("PDF courtesy notice error");
                }
            }
        }
        if(isFileNeedToDelete){
            oleDeliverBatchService.cleanZeroByteFiles();
        }

    }

    public void sendMailsToPatron(OlePatronDocument olePatronDocument, String noticeContent, String fromAddress) {

        Long b1 = System.currentTimeMillis();
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        String emailAddress = "";
        try {
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);
            emailAddress = getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getPatronHomeEmailId(entityTypeContactInfoBo) : "";
        } catch (Exception e) {
            LOG.error("Unable to get Patron Email Address --sendMailsToPatron--" + e.getMessage(), e);
        }
        try {
            if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                fromAddress = OLEConstants.KUALI_MAIL;
            }
            //   oleDeliverBatchService.generatePdfForOverdueCourtesyNotice(oleLoanDocuments,true,olePatronDocument);
            if (emailAddress != null && !emailAddress.isEmpty()) {
                noticeContent = noticeContent.replace('[', ' ');
                noticeContent = noticeContent.replace(']', ' ');
                if (!noticeContent.trim().equals("")) {
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(emailAddress), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(noticeContent), true);
                }
            } else {
                LOG.error("Patron Email Address --sendMailsToPatron-- is Empty or Null");
            }
        } catch (Exception e) {
            LOG.error("Exception in generateNotices()" + e.getMessage(), e);
        }
        Long b2 = System.currentTimeMillis();
        Long total = b2 - b1;
        LOG.info("The time taken sending mail :" + total);
    }

    /**
     * This method is used to set the item information in the loan documents
     * @param oleLoanDocuments
     * @return
     */
    public List<OleLoanDocument> getLoanDocumentWithItemInfo(List<OleLoanDocument> oleLoanDocuments)throws Exception{
       Long startTime = System.currentTimeMillis();
        List<OleLoanDocument> loanDocumentsWithItemInfo = new ArrayList<OleLoanDocument>();
        if(oleLoanDocuments!=null && oleLoanDocuments.size()>0){
            Map<String,OleLoanDocument> loanDocumentMap = new HashMap<String,OleLoanDocument>();
            SearchParams searchParams = new SearchParams();
            List<SearchCondition> searchConditions = new ArrayList<>();
            SearchResponse searchResponse = new SearchResponse();
            String numberOfRecords = getLoanProcessor().getParameter(OLEConstants.NUMBER_OF_ITEM_INFO);
            List<List<OleLoanDocument>> slicedList = (List<List<OleLoanDocument>>)splitListToSubList(oleLoanDocuments,Integer.valueOf(numberOfRecords).intValue());
            for(List<OleLoanDocument> oleLoanDocumentList : slicedList  ){
                try{
                searchConditions = new ArrayList<>();
                searchResponse = new SearchResponse();
                searchParams = new SearchParams();
                for(OleLoanDocument oleLoanDocument : oleLoanDocumentList){
                    loanDocumentMap.put(oleLoanDocument.getItemUuid(),oleLoanDocument);
                    searchConditions.add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField("item", "id", oleLoanDocument.getItemUuid()), "OR"));
                }
                searchParams.setPageSize(Integer.parseInt(OLEConstants.MAX_PAGE_SIZE_FOR_LOAN));
                buildSearchParams(searchParams);
                searchParams.getSearchConditions().addAll(searchConditions);
                try{
                    searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                }catch(Exception e){
                    LOG.error(e,e);
                    throw new  Exception("Exception occured while fetching data from solr");
                }try{
                    List<OleLoanDocument> processedLoanDocuments = buildSearchResultsFields(searchResponse,loanDocumentMap);
                    loanDocumentsWithItemInfo.addAll(processedLoanDocuments);
                }catch(Exception e){
                    LOG.error(e,e);
                        throw new Exception("Exception occured while setting the item information to loan documents");
                }
            }catch(Exception e){
                LOG.info("Exception occured while setting the item information to the loan documents");
                LOG.error(e,e);
                }
            }
        }
        Long endTime =System.currentTimeMillis();
        Long timeDifference = endTime-startTime;
        LOG.info("Time Taken to set the item information in the loan records in milliseconds : " + timeDifference);
        return loanDocumentsWithItemInfo;
    }

    /**
     * This method is used to set the values from the docstore response
     * @param searchResponse
     * @param loanDocumentMap
     * @return
     * @throws Exception
     */
    public List<OleLoanDocument> buildSearchResultsFields(SearchResponse searchResponse,Map<String,OleLoanDocument> loanDocumentMap)throws Exception{
        int count = 0;
        Map<String,String> itemTypeNameMap = new HashMap<String,String>();
        List<OleInstanceItemType> instanceItemTypeList = (List<OleInstanceItemType>)getBusinessObjectService().findAll(OleInstanceItemType.class);
        if(instanceItemTypeList != null && instanceItemTypeList.size()>0){
            for(OleInstanceItemType oleInstanceItemType : instanceItemTypeList){
                itemTypeNameMap.put(oleInstanceItemType.getInstanceItemTypeName(),oleInstanceItemType.getInstanceItemTypeCode());
            }
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        List<OleLoanDocument> oleLoanDocuments = new ArrayList<>();
        if(searchResponse!=null){
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                try{
                OleLoanDocument oleLoanDocument = null;
                boolean found = false;
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    if(searchResultField.getFieldValue()!=null){
                        if(searchResultField.getFieldName().equalsIgnoreCase("id")) {
                           found = loanDocumentMap.containsKey(searchResultField.getFieldValue());
                        }
                        if(found){
                         if(searchResultField.getFieldName().equalsIgnoreCase("id")) {
                            oleLoanDocument = loanDocumentMap.get(searchResultField.getFieldValue());
                            oleLoanDocument.setItemUuid(searchResultField.getFieldValue());
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("ItemBarcode_display")) {
                            oleLoanDocument.setItemId(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                            oleLoanDocument.setBibUuid(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                            oleLoanDocument.setTitle(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("Author_display")) {
                            oleLoanDocument.setAuthor(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                            oleLoanDocument.setInstanceUuid(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("claimsReturnedNote")) {
                            oleLoanDocument.setClaimsReturnNote(searchResultField.getFieldValue());
                            //  count++;
                        }

                        else if (searchResultField.getFieldName().equalsIgnoreCase("ClaimsReturnedFlag_display") && searchResultField.getFieldValue().equalsIgnoreCase("true")) {
                            oleLoanDocument.setClaimsReturnedIndicator(true);
                            count++;
                        }
                        else  if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                            getLoanProcessor().getLocationBySolr(searchResultField, oleLoanDocument);
                             oleLoanDocument.setItemFullLocation(searchResultField.getFieldValue());
                             Map<String, String> locationMap = getLocationMap(oleLoanDocument.getItemFullLocation());
                             oleLoanDocument.setItemInstitution(locationMap.get(OLEConstants.ITEM_INSTITUTION));
                             oleLoanDocument.setItemCampus(locationMap.get(OLEConstants.ITEM_CAMPUS));
                             oleLoanDocument.setItemCollection(locationMap.get(OLEConstants.ITEM_COLLECTION));
                             oleLoanDocument.setItemLibrary(locationMap.get(OLEConstants.ITEM_LIBRARY));
                             oleLoanDocument.setItemLocation(locationMap.get(OLEConstants.ITEM_SHELVING));

                        }
                        else  if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsLocation_search") &&
                                (oleLoanDocument.getItemLocation()==null || oleLoanDocument.getItemLocation().isEmpty())) {
                            getLoanProcessor().getLocationBySolr(searchResultField,oleLoanDocument);
                             oleLoanDocument.setItemFullLocation(searchResultField.getFieldValue());
                             Map<String, String> locationMap = getLocationMap(oleLoanDocument.getItemFullLocation());
                             oleLoanDocument.setItemInstitution(locationMap.get(OLEConstants.ITEM_INSTITUTION));
                             oleLoanDocument.setItemCampus(locationMap.get(OLEConstants.ITEM_CAMPUS));
                             oleLoanDocument.setItemCollection(locationMap.get(OLEConstants.ITEM_COLLECTION));
                             oleLoanDocument.setItemLibrary(locationMap.get(OLEConstants.ITEM_LIBRARY));
                             oleLoanDocument.setItemLocation(locationMap.get(OLEConstants.ITEM_SHELVING));

                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("claimsReturnedFlagCreateDate")) {
                            String[] formatStrings = new String[]{"MM/dd/yyyy hh:mm:ss","MM/dd/yyyy","yyyy-MM-dd hh:mm:ss"};
                            Date date =getLoanProcessor().tryParse(formatStrings,searchResultField.getFieldValue());
                            oleLoanDocument.setClaimsReturnedDate(new Timestamp(date.getTime()));

                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("CallNumber_display")) {
                            oleLoanDocument.setItemCallNumber(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("TemporaryItemTypeFullValue_search")) {
                            oleLoanDocument.setItemTypeName(searchResultField.getFieldValue());
                            oleLoanDocument.setItemType(itemTypeNameMap.get(oleLoanDocument.getItemTypeName()));
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("ItemTypeFullValue_display") &&
                                (oleLoanDocument.getItemTypeName()==null || oleLoanDocument.getItemTypeName().isEmpty())) {
                            oleLoanDocument.setItemTypeName(searchResultField.getFieldValue());
                             oleLoanDocument.setItemType(itemTypeNameMap.get(oleLoanDocument.getItemTypeName()));
                         }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("Enumeration_display")) {
                            oleLoanDocument.setEnumeration(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("Chronology_display")) {
                            oleLoanDocument.setChronology(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("ItemStatus_display")) {
                            oleLoanDocument.setItemStatus(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("ItemDamagedStatus_display")) {
                            oleLoanDocument.setItemDamagedStatus(searchResultField.getFieldValue().equalsIgnoreCase("true"));
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("DamagedItemNote_search")) {
                            oleLoanDocument.setItemDamagedNote(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("MissingPieceFlagNote_search")) {
                            oleLoanDocument.setMissingPieceNote(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("MissingPieceFlag_display")) {
                            oleLoanDocument.setMissingPieceFlag(searchResultField.getFieldValue().equalsIgnoreCase("true"));
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("CopyNumber_search")) {
                            oleLoanDocument.setItemCopyNumber(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsCopyNumber_search") &&
                                (oleLoanDocument.getItemCopyNumber()==null || oleLoanDocument.getItemCopyNumber().isEmpty())) {
                            oleLoanDocument.setItemCopyNumber(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsCallNumber_search") &&
                                (oleLoanDocument.getItemCallNumber()==null || oleLoanDocument.getItemCallNumber().isEmpty())) {
                            oleLoanDocument.setItemCallNumber(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("MissingPieceCount_search")) {
                            oleLoanDocument.setMissingPiecesCount(searchResultField.getFieldValue());
                        }
                        else if (searchResultField.getFieldName().equalsIgnoreCase("NumberOfPieces_search")) {
                            oleLoanDocument.setItemNumberOfPieces(Integer.parseInt(searchResultField.getFieldValue()));
                            oleLoanDocument.setBackUpNoOfPieces(searchResultField.getFieldValue());
                        }
                        else if(searchResultField.getFieldName().equalsIgnoreCase("ClaimsReturnedFlag_search")) {
                             oleLoanDocument.setClaimsReturnedIndicator(Boolean.valueOf(searchResultField.getFieldValue()));
                         }else if(searchResultField.getFieldName().equalsIgnoreCase("itemStatusEffectiveDate")) {
                             oleLoanDocument.setItemStatusEffectiveDate(searchResultField.getFieldValue());
                            // LOG.info("Item status Effective date from solr : " + searchResultField.getFieldValue() );
                         }
                    }
                  }

                }
                found =false;
                if(oleLoanDocument.getOlePatron()!=null){
                    oleLoanDocument.getOlePatron().setNumberOfClaimsReturned(count);
                }
                oleLoanDocuments.add(oleLoanDocument);
            }catch(Exception e){
                    LOG.info("Exception occured while setting the item info for the loan ");
                }
            }
        }
        return oleLoanDocuments;
    }


    public String getItemTypeCodeByName(String itemTypeName){
       String itemTypeCode = "";
        List<OleInstanceItemType> instanceItemTypeList = null;
        Map<String,String> instanceItemTypeMap = new HashMap<String,String>();
        instanceItemTypeMap.put("instanceItemTypeName",itemTypeName);
        instanceItemTypeList = (List<OleInstanceItemType>)getBusinessObjectService().findMatching(OleInstanceItemType.class,instanceItemTypeMap);
        if(instanceItemTypeList != null && instanceItemTypeList.size()>0){
           itemTypeCode = instanceItemTypeList.get(0).getInstanceItemTypeCode();
        }
        return itemTypeCode;
    }

    /**
     * This method is to update the item
     * @param oleItem
     * @throws Exception
     */
    public void updateItem(Item oleItem)throws Exception{
        String itemUuid = oleItem.getItemIdentifier();
        String itemXmlContent = getItemOlemlRecordProcessor().toXML(oleItem);
         try{
        org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
        item.setId(itemUuid);
        item.setContent(itemXmlContent);
        item.setCategory(OLEConstants.WORK_CATEGORY);
        item.setType(DocType.ITEM.getCode());
        item.setFormat(OLEConstants.OLEML_FORMAT);
        getDocstoreClientLocator().getDocstoreClient().updateItem(item);
    } catch (Exception e) {
        LOG.error(OLEConstants.ITM_STS_TO_DOC_FAIL + e, e);
        throw new Exception(OLEConstants.ITM_STS_TO_DOC_FAIL);
    }

    }


    public List splitListToSubList(List<OleLoanDocument> parentList, int childListSize) {
        List<List<OleLoanDocument>> childList = new ArrayList<List<OleLoanDocument>>();
        List<OleLoanDocument> tempList = new ArrayList<OleLoanDocument>();
        int count = 0;
        if (parentList != null) {
            for (OleLoanDocument obj : parentList) {
                if (count < childListSize) {
                    count = count + 1;
                    tempList.add(obj);
                } else {
                    childList.add(tempList);
                    tempList = new ArrayList<OleLoanDocument>();
                    tempList.add(obj);
                    count = 1;
                }
            }
            if (tempList.size() <= childListSize) {
                childList.add(tempList);
            }
        }
        return childList;
    }


    public void buildSearchParams(SearchParams searchParams){
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "id"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemBarcode_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "bibIdentifier"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Title_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Author_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "LocalId_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "holdingsIdentifier"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ClaimsReturnedFlag_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "claimsReturnedFlagCreateDate"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "claimsReturnedNote"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CallNumber_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "TemporaryItemTypeFullValue_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemTypeFullValue_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Enumeration_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Chronology_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemStatus_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ItemDamagedStatus_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "DamagedItemNote_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "MissingPieceFlagNote_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "Location_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "HoldingsCopyNumber_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "HoldingsCallNumber_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "HoldingsLocation_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "MissingPieceCount_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "NumberOfPieces_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CallNumberPrefix_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CopyNumber_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "proxyBorrower"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "VolumeNumberLabel_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "dueDateTime"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "NumberOfRenew_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item" , "checkOutDateTime"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item","ClaimsReturnedFlag_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item","itemStatusEffectiveDate"));
    }


    public void saveOLEDeliverNoticeHistory(OleLoanDocument oleLoanDocument){
        OLEDeliverNoticeHistory oleDeliverNoticeHistory = new OLEDeliverNoticeHistory();
        oleDeliverNoticeHistory.setLoanId(oleLoanDocument.getLoanId());
        oleDeliverNoticeHistory.setNoticeType(oleLoanDocument.getNoticeType());
        oleDeliverNoticeHistory.setNoticeSentDate(new Timestamp(new Date().getTime()));
        oleDeliverNoticeHistory.setPatronId(oleLoanDocument.getPatronId());
        oleDeliverNoticeHistory.setNoticeSendType(oleLoanDocument.getNoticeSendType());
        getBusinessObjectService().save(oleDeliverNoticeHistory);
    }


    private void itemStatusBulkUpdate(List<String> itemUUIDs){
        List<org.kuali.ole.docstore.common.document.Item> items = null;
        try{
            items = getDocstoreClientLocator().getDocstoreClient().retrieveItems(itemUUIDs);
        }catch(Exception e){
            StringBuffer itemUUIDsBuffer = new StringBuffer();
            for(String itemUUID : itemUUIDs){
                itemUUIDsBuffer.append(itemUUID + ",");
            }
            LOG.info("Exception occured while retrieving the item for updating the item status to the following itemIds : " + itemUUIDsBuffer.toString());
            LOG.error(e,e);
        }

        BibTrees bibTrees = new BibTrees();
        for(org.kuali.ole.docstore.common.document.Item item : items){
            Item oleItem = (Item) item.getContentObject();
            String itemXml=null;
            try{
                itemXml = getLoanProcessor().buildItemContentWithItemStatus(oleItem, OLEConstants.ITEM_STATUS_LOST);
            }catch(Exception e){
                LOG.info("Exception occured while updating the item status for the item id : " +  item.getId() + "and barcode : " + item.getBarcode());
                LOG.error(e,e);
            }
            if(itemXml !=null){
                item.setContent(itemXml);
                item.setOperation(DocstoreDocument.OperationType.UPDATE);
                BibTree bibTree = new BibTree();
                bibTree.setBib(item.getHolding().getBib());
                HoldingsTree holdingsTree = new HoldingsTree();
                holdingsTree.setHoldings(item.getHolding());
                holdingsTree.getItems().add(item);
                bibTree.getHoldingsTrees().add(holdingsTree);
                bibTrees.getBibTrees().add(bibTree);
            }
        }
        try {
            getDocstoreClientLocator().getDocstoreClient().processBibTrees(bibTrees);
        }catch(Exception e){
            LOG.error(e,e);
            StringBuffer itemUUIDsBuffer = new StringBuffer();
            for(String itemUUID : itemUUIDs){
                itemUUIDsBuffer.append(itemUUID + ",");
            }
            LOG.info("Exception occured while updating item status to the following itemIds : " + itemUUIDsBuffer.toString());
        }

    }

    public Map<String,List<String>> getItemStatusBasedOnRequestTypeMap(){
        String recallDeliveryRequestStatus = getLoanProcessor().getParameter(OLEConstants.RECALL_DELIVERY_ITEM_STATUS);
        String recallHoldRequestStatus = getLoanProcessor().getParameter(OLEConstants.RECALL_HOLD_ITEM_STATUS);
        String holdDeliveryRequestStatus = getLoanProcessor().getParameter(OLEConstants.HOLD_DELIVERY_ITEM_STATUS);
        String holdHoldRequestStatus = getLoanProcessor().getParameter(OLEConstants.HOLD_HOLD_ITEM_STATUS);
        String pageDeliveryRequestStatus = getLoanProcessor().getParameter(OLEConstants.PAGE_DELIVERY_ITEM_STATUS);
        String pageHoldRequestStatus = getLoanProcessor().getParameter(OLEConstants.PAGE_HOLD_ITEM_STATUS);
        String copyRequestStatus = getLoanProcessor().getParameter(OLEConstants.COPY_REQUEST_ITEM_STATUS);


        Map<String,List<String>> itemStatusByRequestTypeMap = new HashMap<String,List<String>>();
        itemStatusByRequestTypeMap.put("recall", Arrays.asList(recallDeliveryRequestStatus.split(";")));
        itemStatusByRequestTypeMap.put("hold",Arrays.asList(holdDeliveryRequestStatus.split(";")));
        itemStatusByRequestTypeMap.put("page",Arrays.asList(pageDeliveryRequestStatus.split(";")));
        itemStatusByRequestTypeMap.put("copy",Arrays.asList(copyRequestStatus.split(";")));

        return itemStatusByRequestTypeMap;
    }


    public boolean deliverAddressExist(String patronId) throws Exception {
        boolean found=false;
        Map<String, String> criteria = new HashMap<String, String>();
        Map<String, String> addressCriteria = new HashMap<String, String>();
        criteria.put("olePatronId", patronId);
        List<OlePatronDocument> olePatronDocument = (List<OlePatronDocument>)  KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,criteria);
        if(olePatronDocument != null && olePatronDocument.size() >0){
            List<OleAddressBo> oleAddressBos   = olePatronDocument.get(0).getOleAddresses();
            if(oleAddressBos != null && oleAddressBos.size() >0){
                for(int address=0;address<oleAddressBos.size();address ++ ){
                    if(oleAddressBos != null && oleAddressBos.size() > 0 && oleAddressBos.get(address).isDeliverAddress()){
                        found = true;
                    }
                }
            }

        }
        return found;
    }

}






