package org.kuali.ole.deliver.service;


import com.itextpdf.text.Document;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.asr.ASRConstants;
import org.kuali.asr.service.ASRHelperServiceImpl;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.DeliverConstants;
import org.kuali.ole.deliver.OleLoanDocumentsFromSolrBuilder;
import org.kuali.ole.deliver.batch.OleDeliverBatchServiceImpl;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.batch.OleSms;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.calendar.service.OleCalendarService;
import org.kuali.ole.deliver.calendar.service.impl.OleCalendarServiceImpl;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.notice.NoticeSolrInputDocumentGenerator;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.executors.*;
import org.kuali.ole.deliver.notice.noticeFormatters.RecallRequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.noticeFormatters.RequestEmailContentFormatter;
import org.kuali.ole.deliver.notice.service.OleNoticeService;
import org.kuali.ole.deliver.notice.service.impl.OleNoticeServiceImpl;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.util.DroolsResponse;
import org.kuali.ole.deliver.util.LoanDateTimeUtil;
import org.kuali.ole.deliver.util.NoticeInfo;
import org.kuali.ole.deliver.util.OlePatronRecordUtil;
import org.kuali.ole.deliver.util.ItemInfoUtil;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.keyvalue.LocationValuesBuilder;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.engine.client.DocstoreLocalClient;
import org.kuali.ole.docstore.engine.service.index.solr.BibMarcIndexer;
import org.kuali.ole.docstore.engine.service.storage.DocstoreRDBMSStorageService;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.ingest.pojo.MatchBo;
import org.kuali.ole.module.purap.util.PurApDateFormatUtils;
import org.kuali.ole.ncip.bo.OLEPlaceRequest;
import org.kuali.ole.ncip.converter.OLEPlaceRequestConverter;
import org.kuali.ole.service.OleCirculationPolicyService;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.coreservice.impl.parameter.ParameterBo;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.UserSession;
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
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private OLEDeliverNoticeHelperService oleDeliverNoticeHelperService;
    private DocumentService documentService;
    private PersonService personService = KimApiServiceLocator.getPersonService();
    private OleCirculationPolicyService oleCirculationPolicyService;
    private DateTimeService dateTimeService;
    private List<OleLoanDocument> laonDocumentsFromLaondId;
    private OlePatronHelperService olePatronHelperService;
    private CircDeskLocationResolver circDeskLocationResolver;
    private OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder;
    private ParameterValueResolver parameterResolverInstance;
    private OleMailer oleMailer;
    private NoticeSolrInputDocumentGenerator noticeSolrInputDocumentGenerator;
    private SolrRequestReponseHandler solrRequestReponseHandler;
    private String numberOfRecords = null;
    private OleNoticeService noticeService=new OleNoticeServiceImpl();
    private Boolean executionFlag = Boolean.FALSE;

    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
                if (null == solrRequestReponseHandler) {
                        solrRequestReponseHandler = new SolrRequestReponseHandler();
                    }
               return solrRequestReponseHandler;
    }

    public NoticeSolrInputDocumentGenerator getNoticeSolrInputDocumentGenerator() {
        if (null == noticeSolrInputDocumentGenerator) {
            noticeSolrInputDocumentGenerator = new NoticeSolrInputDocumentGenerator();
        }
        return noticeSolrInputDocumentGenerator;
    }





    public ParameterValueResolver getParameterResolverInstance() {
        if (null == parameterResolverInstance) {
            parameterResolverInstance = ParameterValueResolver.getInstance();
        }
        return parameterResolverInstance;
    }

    public OleMailer getOleMailer() {
        if (null == oleMailer) {
            oleMailer = GlobalResourceLoader.getService("oleMailer");
        }
        return oleMailer;
    }


    public DateTimeService getDateTimeService() {
        return (DateTimeService) SpringContext.getService("dateTimeService");
    }

    private PermissionService getPermissionService() {
        PermissionService service = KimApiServiceLocator.getPermissionService();
        return service;
    }

    private CircDeskLocationResolver getCircDeskLocationResolver() {
        if (circDeskLocationResolver == null) {
            circDeskLocationResolver = new CircDeskLocationResolver();
        }
        return circDeskLocationResolver;
    }

    public void setCircDeskLocationResolver(CircDeskLocationResolver circDeskLocationResolver) {
        this.circDeskLocationResolver = circDeskLocationResolver;
    }

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = (DocstoreClientLocator)SpringContext.getService("docstoreClientLocator");

        }
        return docstoreClientLocator;
    }

    public OlePatronHelperService getOlePatronHelperService() {
        if (olePatronHelperService == null)
            olePatronHelperService = new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    public void setOlePatronHelperService(OlePatronHelperService olePatronHelperService) {
        this.olePatronHelperService = olePatronHelperService;
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
        if (docstoreUtil == null) {
            docstoreUtil = (DocstoreUtil)SpringContext.getService("docstoreUtil");
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
        if (loanProcessor == null) {
            loanProcessor = (LoanProcessor)SpringContext.getService("loanProcessor");
        }
        return loanProcessor;
    }

    public ItemOlemlRecordProcessor getItemOlemlRecordProcessor() {
        if (itemOlemlRecordProcessor == null) {
            itemOlemlRecordProcessor = (ItemOlemlRecordProcessor) SpringContext.getService("itemOlemlRecordProcessor");
        }
        return itemOlemlRecordProcessor;
    }

    public OLEDeliverNoticeHelperService getOleDeliverNoticeHelperService() {
        if (oleDeliverNoticeHelperService == null) {
            oleDeliverNoticeHelperService = (OLEDeliverNoticeHelperService)SpringContext.getService("oleDeliverNoticeHelperService");
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
            oleCirculationPolicyService = (OleCirculationPolicyService)SpringContext.getService("oleCirculationPolicyService");
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

    private OleDeliverRequestBo processRequestTypeByPickUpLocation(OleDeliverRequestBo oleDeliverRequestBo) {
        if(oleDeliverRequestBo.getRequestTypeId()==null && oleDeliverRequestBo.getRequestTypeCode()!=null){
            Map<String,String> criteriaMap = new HashMap<String,String>();
            criteriaMap.put("requestTypeCode",oleDeliverRequestBo.getRequestTypeCode());
            List<OleDeliverRequestType> oleDeliverRequestTypes = (List<OleDeliverRequestType>)getBusinessObjectService().findMatching(OleDeliverRequestType.class,criteriaMap);
            if(oleDeliverRequestTypes.size()>0){
                oleDeliverRequestBo.setRequestTypeId(oleDeliverRequestTypes.get(0).getRequestTypeId());
            }
        }

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

        OlePatronDocument olePatronDocument = oleDeliverRequestBo.getOlePatron();
        if (olePatronDocument == null) {
            Map<String, String> patronMap = new HashMap<String, String>();
            patronMap.put(OLEConstants.OleDeliverRequest.PATRON_ID, oleDeliverRequestBo.getBorrowerId());
            List<OlePatronDocument> olePatronDocuments = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
            if (olePatronDocuments != null && olePatronDocuments.size() > 0) {
                olePatronDocument = olePatronDocuments.get(0);
            }
        }
        if (olePatronDocument != null) {
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
    public void cancelDocument(OleDeliverRequestBo oleDeliverRequestBo) {
        String operatorId = GlobalVariables.getUserSession().getLoggedInUserPrincipalName();
        String mailContent = null;
        List<OleNoticeBo> oleNoticeBos = null;
        try {
            oleNoticeBos = cancelRequestForItem(oleDeliverRequestBo.getItemUuid(), oleDeliverRequestBo.getBorrowerId());
            ASRHelperServiceImpl asrHelperService = new ASRHelperServiceImpl();
            createRequestHistoryRecord(oleDeliverRequestBo.getRequestId(), operatorId, oleDeliverRequestBo.getLoanTransactionRecordNumber(), ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_CANCELLED));
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
           mailContent =  sendCancelNotice(oleNoticeBos);
        } catch (Exception e) {
                       mailContent = sendCancelNotice(oleNoticeBos);
        }
        if(mailContent!=null){
            List<OleDeliverRequestBo> deliverRequestBos = new ArrayList<OleDeliverRequestBo>();
            deliverRequestBos.add(oleDeliverRequestBo);
            getSolrRequestReponseHandler().updateSolr(org.kuali.common.util.CollectionUtils.singletonList(
                    getNoticeSolrInputDocumentGenerator().getSolrInputDocument(
                            buildMapForIndexToSolr(OLEConstants.CANCELLATION_NOTICE,mailContent, deliverRequestBos))));
        }
    }

    /**
     * Build the cancellation notice template.
     *
     * @param itemUuid
     * @param patronId
     * @return
     * @throws Exception
     */
    public List<OleNoticeBo> cancelRequestForItem(String itemUuid, String patronId) throws Exception {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.ITEM_UUID, itemUuid);
        requestMap.put(OLEConstants.OleDeliverRequest.BORROWER_ID, patronId);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        List<OleNoticeBo> oleNoticeBos = new ArrayList<OleNoticeBo>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(oleDeliverRequestBoList)) {
            OleDeliverRequestBo oleDeliverRequestBo = oleDeliverRequestBoList.get(0);
            //cancelDocument(oleDeliverRequestBo);
            OleItemSearch itemSearch = getDocstoreUtil().getOleItemSearchList(itemUuid);
            EntityTypeContactInfoBo entityTypeContactInfoBo = oleDeliverRequestBo.getOlePatron().getEntity().getEntityTypeContactInfos().get(0);

            OleNoticeBo oleNoticeBo = new OleNoticeBo();
            oleNoticeBo.setNoticeName(OLEConstants.CANCELLATION_NOTICE);
            oleNoticeBo.setPatronName(oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getFirstName() + " " + oleDeliverRequestBo.getOlePatron().getEntity().getNames().get(0).getLastName());
            oleNoticeBo.setPatronAddress(getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) : "");
            oleNoticeBo.setPatronEmailAddress(getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "");
            oleNoticeBo.setPatronPhoneNumber(getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
            oleNoticeBo.setAuthor(itemSearch.getAuthor() != null ? itemSearch.getAuthor() : "");
            oleNoticeBo.setItemCallNumber(itemSearch.getCallNumber() != null ? itemSearch.getCallNumber() : "");
            if (itemSearch.getShelvingLocation() != null && itemSearch.getShelvingLocation().toString().contains("/")) {
                String[] location = itemSearch.getShelvingLocation().split("/");
                if (location != null && location.length > 0)
                    oleNoticeBo.setItemShelvingLocation(location[1] != null ? location[1] : "");
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
     *
     * @param oleNoticeBos
     * @throws Exception
     */
    public String sendCancelNotice(List<OleNoticeBo> oleNoticeBos) {
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        String content = null;
        for (OleNoticeBo oleNoticeBo : oleNoticeBos) {
            try {
                List list = oleDeliverBatchService.getNoticeForPatron(oleNoticeBos);
                content = list.toString();
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
            } catch (Exception e) {
                LOG.info("Exception occured while sending the request cancellation notice   " + e.getMessage());
                LOG.error(e);

            }

        }
        return content;
    }


    public void cancelPendingRequestForClaimsReturnedItem(String itemUuid) throws Exception {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.ITEM_UUID, itemUuid);
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBoList) {
            cancelDocument(oleDeliverRequestBo);
        }
    }

    public void cancelPendingRequestForDamagedItem(String itemUuid) throws Exception {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.ITEM_UUID, itemUuid);
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBoList) {
            cancelDocument(oleDeliverRequestBo);
        }
    }

    public void cancelPendingRequestForMissingPieceItem(String itemUuid) throws Exception {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.ITEM_UUID, itemUuid);
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        for (OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBoList) {
            cancelDocument(oleDeliverRequestBo);
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
                    oleDeliverRequestBo.setItemId(itemSearchList.getItemBarCode());
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
        if (oleLoanDocumentList != null && oleLoanDocumentList.size() > 0 && oleLoanDocumentList.get(0) != null) {
            if (oleLoanDocumentList.get(0).getLoanId() != null) {
                oleDeliverRequestBo.setLoanTransactionRecordNumber(oleLoanDocumentList.get(0).getLoanId());
                if(oleLoanDocumentList.get(0).getOleRequestId()==null){
                    oleLoanDocumentList.get(0).setOleRequestId(oleDeliverRequestBo.getRequestId());
                }
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
    public void deleteRequest(String requestId, String itemUUID, String operatorId, String loanTransactionNumber, String reuestOutCome) {
        LOG.debug("Inside deleteRequest");
        Map<String, String> requestMap = new HashMap<String, String>();
        Map<String, String> titleLevelRequestMap = new HashMap<String, String>();
        List<OleDeliverRequestBo> titleLevelRequestBoList = null;
        requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_ID, requestId);
        OleDeliverRequestBo oleDeliverRequestBo = createRequestHistoryRecord(requestId, operatorId, loanTransactionNumber, reuestOutCome);
        if (oleDeliverRequestBo != null) {
            if (oleDeliverRequestBo.getRequestLevel().equalsIgnoreCase("Item Level")) {
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
            } else if (oleDeliverRequestBo.getRequestLevel().equalsIgnoreCase("Title Level")) {
                requestMap = new HashMap<String, String>();
                requestMap.put("bibId", oleDeliverRequestBo.getBibId());
                requestMap.put("borrowerBarcode", oleDeliverRequestBo.getBorrowerBarcode());
                List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
                if (oleDeliverRequestBoList.size() > 0) {
                    for (OleDeliverRequestBo oleDeliverRequestBo1 : oleDeliverRequestBoList) {
                        titleLevelRequestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo1.getItemUuid());
                        getBusinessObjectService().delete(oleDeliverRequestBo1);
                        titleLevelRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, titleLevelRequestMap);
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


    public void deleteTitleLevelIndividualRequests(OleDeliverRequestBo oleDeliverRequestBo) {
        Map<String, String> requestMap = new HashMap<String, String>();
        Map<String, String> titleLevelRequestMap = new HashMap<String, String>();
        List<OleDeliverRequestBo> titleLevelRequestBoList = null;
        requestMap = new HashMap<String, String>();
        requestMap.put("bibId", oleDeliverRequestBo.getBibId());
        requestMap.put("borrowerBarcode", oleDeliverRequestBo.getBorrowerBarcode());
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        if (oleDeliverRequestBoList.size() > 0) {
            for (OleDeliverRequestBo oleDeliverRequestBo1 : oleDeliverRequestBoList) {
                titleLevelRequestMap.put(OLEConstants.ITEM_UUID, oleDeliverRequestBo1.getItemUuid());
                getBusinessObjectService().delete(oleDeliverRequestBo1);
                titleLevelRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, titleLevelRequestMap);
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
        String itemStatus = oleDeliverRequestBo.getItemStatus();
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
        } else if (oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains(OLEConstants.OleDeliverRequest.ASR_REQUEST)) {
            asrList.add(oleDeliverRequestBo);
        }
        OleDeliverRequestBo oleDeliverRequestBo1;

        setRequestQueuePositionForOnholdItem(finalList, itemStatus, recallHoldList, holdHoldList, pageHoldList, asrList);

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
        try {
            Thread.sleep(1000);
            getBusinessObjectService().save(finalList);
            Thread.sleep(1000);
        }
        catch (Exception e) {
            LOG.error("Error while saving Request " + e.getMessage());
        }

        this.queuePosition = 0;
        oleDeliverRequestBo.setRequestId(KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("OLE_DLVR_RQST_S").toString());
        return oleDeliverRequestBo;
    }

    private void setRequestQueuePositionForOnholdItem(List<OleDeliverRequestBo> finalList, String itemStatus, List<OleDeliverRequestBo> recallHoldList, List<OleDeliverRequestBo> holdHoldList, List<OleDeliverRequestBo> pageHoldList, List<OleDeliverRequestBo> asrList) {
        OleDeliverRequestBo oleDeliverRequestBo1;
        if(StringUtils.isNotBlank(itemStatus) && itemStatus.equalsIgnoreCase(OLEConstants.ITEM_STATUS_ON_HOLD) && (CollectionUtils.isNotEmpty(holdHoldList) || CollectionUtils.isNotEmpty(recallHoldList) || CollectionUtils.isNotEmpty(pageHoldList) || CollectionUtils.isNotEmpty(asrList))) {
            if(CollectionUtils.isNotEmpty(recallHoldList)) {
                for (int x = 0; x < recallHoldList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(recallHoldList.get(x));
                    if(null != oleDeliverRequestBo1.getBorrowerQueuePosition() && oleDeliverRequestBo1.getBorrowerQueuePosition() == 1) {
                        oleDeliverRequestBo1.setBorrowerQueuePosition(this.queuePosition + 1);
                        this.queuePosition = this.queuePosition + 1;
                        finalList.add(oleDeliverRequestBo1);
                        recallHoldList.remove(x);
                        break;
                    }

                }
            }
            if(CollectionUtils.isNotEmpty(holdHoldList)) {
                for (int x = 0; x < holdHoldList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(holdHoldList.get(x));
                    if(null != oleDeliverRequestBo1.getBorrowerQueuePosition() && oleDeliverRequestBo1.getBorrowerQueuePosition() == 1) {
                        oleDeliverRequestBo1.setBorrowerQueuePosition(this.queuePosition + 1);
                        this.queuePosition = this.queuePosition + 1;
                        finalList.add(oleDeliverRequestBo1);
                        holdHoldList.remove(x);
                        break;
                    }

                }
            }
            if(CollectionUtils.isNotEmpty(pageHoldList)) {
                for(int x=0; x < pageHoldList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(pageHoldList.get(x));
                    if(null != oleDeliverRequestBo1.getBorrowerQueuePosition() && oleDeliverRequestBo1.getBorrowerQueuePosition() == 1) {
                        oleDeliverRequestBo1.setBorrowerQueuePosition(this.queuePosition + 1);
                        this.queuePosition = this.queuePosition + 1;
                        finalList.add(oleDeliverRequestBo1);
                        pageHoldList.remove(x);
                        break;
                    }
                }
            }
            if(CollectionUtils.isNotEmpty(asrList)) {
                for(int x=0; x < asrList.size(); x++) {
                    oleDeliverRequestBo1 = (OleDeliverRequestBo) ObjectUtils.deepCopy(asrList.get(x));
                    if(null != oleDeliverRequestBo1.getBorrowerQueuePosition() && oleDeliverRequestBo1.getBorrowerQueuePosition() == 1) {
                        oleDeliverRequestBo1.setBorrowerQueuePosition(this.queuePosition + 1);
                        this.queuePosition = this.queuePosition + 1;
                        finalList.add(oleDeliverRequestBo1);
                        asrList.remove(x);
                        break;
                    }
                }
            }
        }
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
        RequestEmailContentFormatter requestEmailContentFormatter = new RecallRequestEmailContentFormatter();
        setItemInformations(oleDeliverRequestBo);
        List<OleDeliverRequestBo> oleDeliverRequestBos = new ArrayList<>();
        oleDeliverRequestBos.add(oleDeliverRequestBo);
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = getOleNoticeContentConfigurationBo(oleDeliverRequestBo.getRecallNoticeContentConfigName());
        String mailContent = requestEmailContentFormatter.generateMailContentForPatron(oleDeliverRequestBos, oleNoticeContentConfigurationBo);
        if (StringUtils.isNotBlank(mailContent)){
            Map requestMap = new HashMap();
            requestMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, oleDeliverRequestBo.getRecallNoticeContentConfigName());
            requestMap.put(OLEConstants.DELIVER_NOTICES, oleDeliverRequestBo.getDeliverNotices());
            RequestNoticesExecutor noticesExecutor = new RecallNoticesExecutor(requestMap);
            OleLoanDocument oleLoanDocument = getLoanDocument(oleDeliverRequestBo.getItemId());
            if (oleLoanDocument!=null){
                oleDeliverRequestBo.setLoanTransactionRecordNumber(oleLoanDocument.getLoanId());
                OlePatronDocument olePatron = oleLoanDocument.getOlePatron();
                try {
                    EntityTypeContactInfoBo entityTypeContactInfoBo = olePatron.getEntity()
                            .getEntityTypeContactInfos().get(0);
                    String emailAddress = noticesExecutor.getPatronHomeEmailId(entityTypeContactInfoBo) != null ?
                            noticesExecutor.getPatronHomeEmailId(entityTypeContactInfoBo) : "";
                    noticesExecutor.sendMailsToPatron(emailAddress, mailContent, oleDeliverRequestBo.getItemLocation(),oleNoticeContentConfigurationBo.getNoticeSubjectLine());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                noticesExecutor.sendMail(mailContent,oleNoticeContentConfigurationBo.getNoticeSubjectLine());
                saveNoticeHistory(mailContent, OLEConstants.RECALL_NOTICE, oleDeliverRequestBo, olePatron.getOlePatronId());
                noticesExecutor.getSolrRequestReponseHandler().updateSolr(org.kuali.common.util.CollectionUtils.singletonList(
                        noticesExecutor.getNoticeSolrInputDocumentGenerator().getSolrInputDocument(
                                noticesExecutor.buildMapForIndexToSolr(noticesExecutor.getType(),mailContent, oleDeliverRequestBos))));
            }
        }
        return oleDeliverRequestBo;
    }

    private void saveNoticeHistory(String mailContent, String noticeType, OleDeliverRequestBo oleDeliverRequestBo, String olePatronId){
        OLEDeliverNoticeHistory oleDeliverNoticeHistory = new OLEDeliverNoticeHistory();
        oleDeliverNoticeHistory.setLoanId(oleDeliverRequestBo.getLoanTransactionRecordNumber());
        oleDeliverNoticeHistory.setNoticeType(noticeType);
        oleDeliverNoticeHistory.setNoticeSentDate(new Timestamp(new Date().getTime()));
        oleDeliverNoticeHistory.setPatronId(olePatronId);
        oleDeliverNoticeHistory.setNoticeSendType(OLEConstants.EMAIL);
        oleDeliverNoticeHistory.setNoticeContent(mailContent.getBytes());
        oleDeliverNoticeHistory.setRequestId(oleDeliverRequestBo.getRequestId());
        getBusinessObjectService().save(oleDeliverNoticeHistory);
    }

    private OleNoticeContentConfigurationBo getOleNoticeContentConfigurationBo(String recallNoticeContentConfigName) {
        ParameterValueResolver parameterValueResolver = ParameterValueResolver.getInstance();
        OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
        List<OleNoticeContentConfigurationBo> oleNoticeContentConfigurationBoList = null;
        Map<String,String> noticeConfigurationMap = new HashMap<String,String>();
        noticeConfigurationMap.put("noticeType",OLEConstants.RECALL_NOTICE);
        noticeConfigurationMap.put("noticeName",recallNoticeContentConfigName);
        oleNoticeContentConfigurationBoList= (List<OleNoticeContentConfigurationBo>)getBusinessObjectService().findMatching(OleNoticeContentConfigurationBo.class,noticeConfigurationMap);
        if(CollectionUtils.isNotEmpty(oleNoticeContentConfigurationBoList)){
            oleNoticeContentConfigurationBo = oleNoticeContentConfigurationBoList.get(0);
        }else{
            oleNoticeContentConfigurationBo = new OleNoticeContentConfigurationBo();
            oleNoticeContentConfigurationBo.setNoticeType(OLEConstants.RECALL_NOTICE);
            oleNoticeContentConfigurationBo.setNoticeTitle(parameterValueResolver.getParameter(OLEConstants.APPL_ID, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants.RECALL_TITLE));
            oleNoticeContentConfigurationBo.setNoticeBody(parameterValueResolver.getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants.RECALL_BODY));
            oleNoticeContentConfigurationBo.setNoticeFooterBody("");
            oleNoticeContentConfigurationBo.setNoticeSubjectLine(noticeService.getNoticeSubjectForNoticeType(OLEConstants.RECALL_NOTICE));
        }
        return oleNoticeContentConfigurationBo;
    }

    public List<String> getList(String[] arrays) {
        List<String> resultList = new ArrayList<>();
        if (arrays != null && arrays.length > 0) {
            for (String arrayObj : arrays) {
                resultList.add(arrayObj);
            }
        }
        return resultList;
    }

    public Map<String, String> getMap(String[] arrays) {
        Map<String, String> resultMap = new HashMap<String, String>();
        if (arrays != null && arrays.length > 0) {
            for (String arrayObj : arrays) {
                resultMap.put(arrayObj, arrayObj);
            }
        }
        return resultMap;
    }


    public void generateOnHoldNoticesBasedOnPickupLocation(String pickupLocationId) throws Exception {
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService("oleLoanDao");
        Collection finalDeliverNoticeList = oleLoanDocumentDaoOjb.getOnHoldNoticeByPickUpLocation(pickupLocationId);
        int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
        String threadPoolSizeValue = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.NOTICE_THREAD_POOL_SIZE);
        if (StringUtils.isNotBlank(threadPoolSizeValue)) {
            try {
                threadPoolSize = Integer.parseInt(threadPoolSizeValue);
            } catch (Exception e) {
                LOG.error("Invalid thread pool size from SystemParameter. So assigned default thread pool size" + threadPoolSize);
                threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
            }
        }

        Map<String, Map<String, List<OLEDeliverNotice>>> mapofNoticesForEachPatronAndConfigName = buildMapofNoticesForEachPatronAndConfigName((List<OLEDeliverNotice>) finalDeliverNoticeList);

        ExecutorService onHoldNoticesExecutorService = Executors.newFixedThreadPool(threadPoolSize);

        for (Iterator<String> iterator = mapofNoticesForEachPatronAndConfigName.keySet().iterator(); iterator.hasNext(); ) {
            String patronId = iterator.next();
            Map<String, List<OLEDeliverNotice>> configMap = mapofNoticesForEachPatronAndConfigName.get(patronId);
            for (Iterator<String> configIterator = configMap.keySet().iterator(); configIterator.hasNext(); ) {
                String configName = configIterator.next();
                Map requestMap = new HashMap();
                requestMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, configName);
                requestMap.put(OLEConstants.DELIVER_NOTICES, configMap.get(configName));
                Runnable onHoldNoticesExecutor = new OnHoldNoticesExecutor(requestMap);
                onHoldNoticesExecutorService.execute(onHoldNoticesExecutor);
            }
        }
        if(!onHoldNoticesExecutorService.isShutdown()) {
            onHoldNoticesExecutorService.shutdown();
        }
    }


    public void generateOnHoldNotice() throws Exception {

        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService("oleLoanDao");
        Collection finalDeliverNoticeList = oleLoanDocumentDaoOjb.getOnHoldNotice();
        int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
        String threadPoolSizeValue = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.NOTICE_THREAD_POOL_SIZE);
        if (StringUtils.isNotBlank(threadPoolSizeValue)) {
            try {
                threadPoolSize = Integer.parseInt(threadPoolSizeValue);
            } catch (Exception e) {
                LOG.error("Invalid thread pool size from SystemParameter. So assigned default thread pool size" + threadPoolSize);
                threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
            }
        }

        Map<String, Map<String, List<OLEDeliverNotice>>> mapofNoticesForEachPatronAndConfigName = buildMapofNoticesForEachPatronAndConfigName((List<OLEDeliverNotice>) finalDeliverNoticeList);

        ExecutorService onHoldNoticesExecutorService = Executors.newFixedThreadPool(threadPoolSize);

        for (Iterator<String> iterator = mapofNoticesForEachPatronAndConfigName.keySet().iterator(); iterator.hasNext(); ) {
            String patronId = iterator.next();
            Map<String, List<OLEDeliverNotice>> configMap = mapofNoticesForEachPatronAndConfigName.get(patronId);
            for (Iterator<String> configIterator = configMap.keySet().iterator(); configIterator.hasNext(); ) {
                String configName = configIterator.next();
                Map requestMap = new HashMap();
                requestMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, configName);
                requestMap.put(OLEConstants.DELIVER_NOTICES, configMap.get(configName));
                Runnable onHoldNoticesExecutor = new OnHoldNoticesExecutor(requestMap);
                onHoldNoticesExecutorService.execute(onHoldNoticesExecutor);
            }
        }
        if(!onHoldNoticesExecutorService.isShutdown()){
            onHoldNoticesExecutorService.shutdown();
        }
    }
    private void generateNoticesBasedOnNoticeType(List<OleNoticeBo> noticesList, String noticeName, String replyToEmail) throws Exception {
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
                        oleMailer.sendEmail(new EmailFrom(replyToEmail), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(noticeService.getNoticeSubjectForNoticeType(oleNoticeBo.getNoticeName())), new EmailBody(content), true);
                    } else {
                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(noticeService.getNoticeSubjectForNoticeType(oleNoticeBo.getNoticeName())), new EmailBody(content), true);
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
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService("oleLoanDao");
        Collection requestExpirationDeliverNotices = oleLoanDocumentDaoOjb.getRequestExpiredNotice();
        int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
        String threadPoolSizeValue = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.NOTICE_THREAD_POOL_SIZE);
        if (StringUtils.isNotBlank(threadPoolSizeValue)) {
            try {
                threadPoolSize = Integer.parseInt(threadPoolSizeValue);
            } catch (Exception e) {
                LOG.error("Invalid thread pool size from SystemParameter. So assigned default thread pool size" + threadPoolSize);
                threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
            }
        }

        Map<String, Map<String, List<OLEDeliverNotice>>> mapofNoticesForEachPatronAndConfigName = buildMapofNoticesForEachPatronAndConfigName((List<OLEDeliverNotice>) requestExpirationDeliverNotices);

        ExecutorService requestExpirationNoticesExecutorService = Executors.newFixedThreadPool(threadPoolSize);

        for (Iterator<String> iterator = mapofNoticesForEachPatronAndConfigName.keySet().iterator(); iterator.hasNext(); ) {
            String patronId = iterator.next();
            Map<String, List<OLEDeliverNotice>> configMap = mapofNoticesForEachPatronAndConfigName.get(patronId);
            for (Iterator<String> configIterator = configMap.keySet().iterator(); configIterator.hasNext(); ) {
                String configName = configIterator.next();
                Map requestMap = new HashMap();
                requestMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, configName);
                requestMap.put(OLEConstants.DELIVER_NOTICES, configMap.get(configName));
                Runnable requestExpirationNoticesExecutor = new RequestExpirationNoticesExecutor(requestMap);
                requestExpirationNoticesExecutorService.execute(requestExpirationNoticesExecutor);
            }
        }
        if(!requestExpirationNoticesExecutorService.isShutdown()) {
            requestExpirationNoticesExecutorService.shutdown();
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
            //getBusinessObjectService().delete(oleDeliverRequestBoList);
            SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OleDeliverRequest.DATE_FORMAT);
            for (int i = 0; i < oleDeliverRequestBoList.size(); i++) {
                try {
                    if(validItemStatus(oleDeliverRequestBoList.get(i).getItemUuid())) {
                        if (oleDeliverRequestBoList.get(i).getHoldExpirationDate() == null && oleDeliverRequestBoList.get(i).getRequestExpiryDate() != null &&
                                fmt.format(oleDeliverRequestBoList.get(i).getRequestExpiryDate()).compareTo(fmt.format(new Date(System.currentTimeMillis()))) < 0) {
                            deleteRequest(oleDeliverRequestBoList.get(i).getRequestId(), oleDeliverRequestBoList.get(i).getItemUuid(), oleDeliverRequestBoList.get(i).getOperatorCreateId(), oleDeliverRequestBoList.get(i).getLoanTransactionRecordNumber(), ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_EXPIRED));
                        } else if (oleDeliverRequestBoList.get(i).getHoldExpirationDate() != null &&
                                (fmt.format(oleDeliverRequestBoList.get(i).getHoldExpirationDate())).compareTo(fmt.format(new Date(System.currentTimeMillis()))) < 0) {
                            deleteRequest(oleDeliverRequestBoList.get(i).getRequestId(), oleDeliverRequestBoList.get(i).getItemUuid(), oleDeliverRequestBoList.get(i).getOperatorCreateId(), oleDeliverRequestBoList.get(i).getLoanTransactionRecordNumber(), ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_EXPIRED));
                        }
                        else if(oleDeliverRequestBoList.get(i).getHoldExpirationDate()  != null && oleDeliverRequestBoList.get(i).getRequestExpiryDate() != null) {
                            if((fmt.format(oleDeliverRequestBoList.get(i).getRequestExpiryDate()).compareTo(fmt.format(new Date(System.currentTimeMillis()))) < 0) &&
                                    (fmt.format(oleDeliverRequestBoList.get(i).getHoldExpirationDate()).compareTo(fmt.format(new Date(System.currentTimeMillis()))) < 0)) {
                                deleteRequest(oleDeliverRequestBoList.get(i).getRequestId(), oleDeliverRequestBoList.get(i).getItemUuid(), oleDeliverRequestBoList.get(i).getOperatorCreateId(), oleDeliverRequestBoList.get(i).getLoanTransactionRecordNumber(), ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_EXPIRED));
                            }
                        }
                    }
                    else  {
                        if (oleDeliverRequestBoList.get(i).getHoldExpirationDate() == null && oleDeliverRequestBoList.get(i).getRequestExpiryDate() != null &&
                                fmt.format(oleDeliverRequestBoList.get(i).getRequestExpiryDate()).compareTo(fmt.format(new Date(System.currentTimeMillis()))) < 0) {
                            deleteRequest(oleDeliverRequestBoList.get(i).getRequestId(), oleDeliverRequestBoList.get(i).getItemUuid(), oleDeliverRequestBoList.get(i).getOperatorCreateId(), oleDeliverRequestBoList.get(i).getLoanTransactionRecordNumber(), ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_EXPIRED));
                        }
                        else if (oleDeliverRequestBoList.get(i).getHoldExpirationDate() != null &&
                                (fmt.format(oleDeliverRequestBoList.get(i).getHoldExpirationDate())).compareTo(fmt.format(new Date(System.currentTimeMillis()))) < 0) {
                            deleteRequest(oleDeliverRequestBoList.get(i).getRequestId(), oleDeliverRequestBoList.get(i).getItemUuid(), oleDeliverRequestBoList.get(i).getOperatorCreateId(), oleDeliverRequestBoList.get(i).getLoanTransactionRecordNumber(), ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_EXPIRED));
                        }
                        else if(oleDeliverRequestBoList.get(i).getHoldExpirationDate()  != null && oleDeliverRequestBoList.get(i).getRequestExpiryDate() != null) {
                            if((fmt.format(oleDeliverRequestBoList.get(i).getRequestExpiryDate()).compareTo(fmt.format(new Date(System.currentTimeMillis()))) < 0) &&
                                    (fmt.format(oleDeliverRequestBoList.get(i).getHoldExpirationDate()).compareTo(fmt.format(new Date(System.currentTimeMillis()))) < 0)) {
                                deleteRequest(oleDeliverRequestBoList.get(i).getRequestId(), oleDeliverRequestBoList.get(i).getItemUuid(), oleDeliverRequestBoList.get(i).getOperatorCreateId(), oleDeliverRequestBoList.get(i).getLoanTransactionRecordNumber(), ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.REQUEST_EXPIRED));
                            }
                        }
                    }

                } catch (Exception e) {
                    LOG.info("Exception occured while deleting the request with request Id : " + oleDeliverRequestBoList.get(i).getRequestId());
                    LOG.error(e, e);
                }
            }
            //getBusinessObjectService().save(newOleDeliverRequestBoList);
        } catch (Exception e) {
            //getBusinessObjectService().save(oleDeliverRequestBoList);
            LOG.error("Exception while deleting expired requests", e);
        }
    }

    private boolean validItemStatus(String uuid) {
        Map itemIdMap = new HashMap();
        itemIdMap.put(OLEConstants.ITEM_ID, uuid.substring(4));
        ItemRecord itemRecord = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(ItemRecord.class, itemIdMap);
        if (itemRecord.getItemStatusRecord().getCode().equalsIgnoreCase(OLEConstants.ITEM_STATUS_ON_HOLD)) {
            return true;
        }
        return false;
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


    private OleDeliverRequestBo createRequestHistoryRecord(String requestId, String OperatorId, String loanTransactionNumber, String reuestOutCome) {
        LOG.debug("Inside createRequestHistoryRecord");
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_ID, requestId);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>)  KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        OleDeliverRequestBo oleDeliverRequestBo = null;
        if (oleDeliverRequestBoList.size() > 0) {
            oleDeliverRequestBo = oleDeliverRequestBoList.get(0);
            OleDeliverRequestHistoryRecord oleDeliverRequestHistoryRecord = new OleDeliverRequestHistoryRecord();
            oleDeliverRequestHistoryRecord.setRequestId(oleDeliverRequestBo.getRequestId());
            oleDeliverRequestHistoryRecord.setItemId(DocumentUniqueIDPrefix.getDocumentId(oleDeliverRequestBo.getItemUuid()));
            oleDeliverRequestHistoryRecord.setPatronId(oleDeliverRequestBo.getOlePatron() != null ? oleDeliverRequestBo.getOlePatron().getOlePatronId() : null);
            oleDeliverRequestHistoryRecord.setItemBarcode(oleDeliverRequestBo.getItemId());
            oleDeliverRequestHistoryRecord.setArchiveDate(new java.sql.Date(System.currentTimeMillis()));
            oleDeliverRequestHistoryRecord.setPickUpLocationCode(oleDeliverRequestBo.getPickUpLocationCode());
            oleDeliverRequestHistoryRecord.setCreateDate(oleDeliverRequestBo.getCreateDate());
            if (StringUtils.isNotBlank(OperatorId)) {
                oleDeliverRequestHistoryRecord.setOperatorId(OperatorId);
            } else {
                oleDeliverRequestHistoryRecord.setOperatorId(" ");
            }
            oleDeliverRequestHistoryRecord.setDeliverRequestTypeCode(oleDeliverRequestBo.getRequestTypeCode());
            oleDeliverRequestHistoryRecord.setPoLineItemNumber("");
            oleDeliverRequestHistoryRecord.setLoanTransactionId(loanTransactionNumber);
            oleDeliverRequestHistoryRecord.setRequestOutComeStatus(reuestOutCome);
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
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>)  KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        if (oleDeliverRequestBoList.size() > 0)
            return oleDeliverRequestBoList.get(0);
        return null;
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
        //generateOverdueNotice();
        generateCourtesyNotice();
        //generateLostNotice();
    }

    public void generateLostNotice() throws Exception {
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService(OLEConstants.OLE_LOAN_DAO);
        LoanWithNoticesDAO loanWithNoticesDAO = (LoanWithNoticesDAO) SpringContext.getService(OLEConstants.LOAN_WITH_NOTICES_DAO);
        String lostNoticeToDate = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.LOST_NOTICE_TO_DATE);
        List<String> loanIds = loanWithNoticesDAO.getLoanIdsForNoticesByNoticeType(lostNoticeToDate, OLEConstants.NOTICE_LOST);
        if (loanIds.size() > 0) {
            List<OleLoanDocument> loanDocuments = oleLoanDocumentDaoOjb.getLaonDocumentsFromLaondId(loanIds);
            List<OleLoanDocument> loanDocumentsWithItemInfo = getLoanDocumentWithItemInfo(loanDocuments,Boolean.FALSE.toString());
            int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
            String threadPoolSizeValue =ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.NOTICE_THREAD_POOL_SIZE);
            if (StringUtils.isNotBlank(threadPoolSizeValue)) {
                try {
                    threadPoolSize = Integer.parseInt(threadPoolSizeValue);
                } catch (Exception e) {
                    LOG.error("Invalid thread pool size from SystemParameter. So assigned default thread pool size" + threadPoolSize);
                    threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
                }
            }

            Map<String, Map<String, List<OleLoanDocument>>> mapofNoticesForEachPatronAndConfigName = buildMapofNoticesForEachPatronAndConfigName(loanDocumentsWithItemInfo, lostNoticeToDate, OLEConstants.NOTICE_LOST);

            ExecutorService lostNoticesExecutorService = Executors.newFixedThreadPool(threadPoolSize);

            for (Iterator<String> iterator = mapofNoticesForEachPatronAndConfigName.keySet().iterator(); iterator.hasNext(); ) {
                String patronId = iterator.next();
                Map<String, List<OleLoanDocument>> configMap = mapofNoticesForEachPatronAndConfigName.get
                        (patronId);
                for (Iterator<String> configIterator = configMap.keySet().iterator(); configIterator.hasNext(); ) {
                    String configName = configIterator.next();
                    Map lostMap = new HashMap();
                    lostMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, configName);
                    lostMap.put(OLEConstants.LOAN_DOCUMENTS, configMap.get(configName));
                    Runnable deliverLostNoticesExecutor = new LostNoticesExecutor(lostMap);
                    lostNoticesExecutorService.execute(deliverLostNoticesExecutor);
                }
            }
            if(!lostNoticesExecutorService.isShutdown()) {
                if(!executionFlag) {
                    executionFlag = Boolean.TRUE;
                    Thread.sleep(1200000);
                    lostNoticesExecutorService.shutdown();
                    loanIds = loanWithNoticesDAO.getLoanIdsForNoticesByNoticeType(lostNoticeToDate, OLEConstants.NOTICE_LOST);
                    if (loanIds.size() > 0) {
                        loanDocuments = oleLoanDocumentDaoOjb.getLaonDocumentsFromLaondId(loanIds);
                        loanDocumentsWithItemInfo = getLoanDocumentWithItemInfo(loanDocuments, Boolean.FALSE.toString());
                        LOG.info("No of loanDocumentsWithItemInfo" + loanDocumentsWithItemInfo.size());
                        if (loanDocumentsWithItemInfo.size() > 0) {
                            generateLostNotice();
                        }
                    }
                } else {
                    executionFlag = Boolean.FALSE;
                    lostNoticesExecutorService.shutdown();
                }
            }
        }
    }


    public void generateCourtesyNotice() throws Exception {
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService(OLEConstants.OLE_LOAN_DAO);
        LoanWithNoticesDAO loanWithNoticesDAO = (LoanWithNoticesDAO) SpringContext.getService(OLEConstants.LOAN_WITH_NOTICES_DAO);
        String courtesyNoticeToDate = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.COURTESY_NOTICE_TO_DATE);
        List<String> loanIds = loanWithNoticesDAO.getLoanIdsForNoticesByNoticeType(courtesyNoticeToDate, OLEConstants.COURTESY_NOTICE);
        if (loanIds.size() > 0) {
            List<OleLoanDocument> loanDocuments = oleLoanDocumentDaoOjb.getLaonDocumentsFromLaondId(loanIds);
            List<OleLoanDocument> loanDocumentsWithItemInfo = getLoanDocumentWithItemInfo(loanDocuments,Boolean.FALSE.toString());
            int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
            String threadPoolSizeValue = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.NOTICE_THREAD_POOL_SIZE);
            if (StringUtils.isNotBlank(threadPoolSizeValue)) {
                try {
                    threadPoolSize = Integer.parseInt(threadPoolSizeValue);
                } catch (Exception e) {
                    LOG.error("Invalid thread pool size from SystemParameter. So assigned default thread pool size" + threadPoolSize);
                    threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
                }
            }

            Map<String, Map<String, List<OleLoanDocument>>> mapofNoticesForEachPatronAndConfigName = buildMapofNoticesForEachPatronAndConfigName(loanDocumentsWithItemInfo, courtesyNoticeToDate, OLEConstants.COURTESY_NOTICE);

            ExecutorService courtesyNoticesExecutorService = Executors.newFixedThreadPool(threadPoolSize);

            for (Iterator<String> iterator = mapofNoticesForEachPatronAndConfigName.keySet().iterator(); iterator.hasNext(); ) {
                String patronId = iterator.next();
                Map<String, List<OleLoanDocument>> configMap = mapofNoticesForEachPatronAndConfigName.get
                        (patronId);
                for (Iterator<String> configIterator = configMap.keySet().iterator(); configIterator.hasNext(); ) {
                    String configName = configIterator.next();
                    Map courtesyMap = new HashMap();
                    courtesyMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, configName);
                    courtesyMap.put(OLEConstants.LOAN_DOCUMENTS, configMap.get(configName));
                    Runnable deliverCourtesyNoticesExecutor = new CourtesyNoticesExecutor(courtesyMap);
                    courtesyNoticesExecutorService.execute(deliverCourtesyNoticesExecutor);
                }

            }
            if(!courtesyNoticesExecutorService.isShutdown()) {
                if(!executionFlag) {
                    executionFlag = Boolean.TRUE;
                    Thread.sleep(1200000);
                    courtesyNoticesExecutorService.shutdown();
                    loanIds = loanWithNoticesDAO.getLoanIdsForNoticesByNoticeType(courtesyNoticeToDate, OLEConstants.COURTESY_NOTICE);
                    if (loanIds.size() > 0) {
                        loanDocuments = oleLoanDocumentDaoOjb.getLaonDocumentsFromLaondId(loanIds);
                        loanDocumentsWithItemInfo = getLoanDocumentWithItemInfo(loanDocuments, Boolean.FALSE.toString());
                        LOG.info("No of loanDocumentsWithItemInfo" + loanDocumentsWithItemInfo.size());
                        if (loanDocumentsWithItemInfo.size() > 0) {
                            generateCourtesyNotice();
                        }
                    }
                } else {
                    executionFlag = Boolean.FALSE;
                    courtesyNoticesExecutorService.shutdown();
                }
            }
        }
    }

    public void generateOverdueNotice() throws Exception {
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService(OLEConstants.OLE_LOAN_DAO);
        LoanWithNoticesDAO loanWithNoticesDAO = (LoanWithNoticesDAO) SpringContext.getService(OLEConstants.LOAN_WITH_NOTICES_DAO);
        String overdueNoticeToDate = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.OVERDUE_NOTICE_TO_DATE);
        List<String> loanIds = loanWithNoticesDAO.getLoanIdsForNoticesByNoticeType(overdueNoticeToDate, OLEConstants.OVERDUE_NOTICE);
        if (loanIds.size() > 0) {
            List<OleLoanDocument> loanDocuments = oleLoanDocumentDaoOjb.getLaonDocumentsFromLaondId(loanIds);
            List<OleLoanDocument> loanDocumentsWithItemInfo = getLoanDocumentWithItemInfo(loanDocuments,Boolean.FALSE.toString());
            int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
            String threadPoolSizeValue = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.NOTICE_THREAD_POOL_SIZE);
            if (StringUtils.isNotBlank(threadPoolSizeValue)) {
                try {
                    threadPoolSize = Integer.parseInt(threadPoolSizeValue);
                } catch (Exception e) {
                    LOG.error("Invalid thread pool size from SystemParameter. So assigned default thread pool size" + threadPoolSize);
                    threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
                }
            }

            Map<String, Map<String, List<OleLoanDocument>>> mapofNoticesForEachPatronAndConfigName = buildMapofNoticesForEachPatronAndConfigName(loanDocumentsWithItemInfo, overdueNoticeToDate, OLEConstants.OVERDUE_NOTICE);

            ExecutorService overDueNoticesExecutorService = Executors.newFixedThreadPool(threadPoolSize);

            for (Iterator<String> iterator = mapofNoticesForEachPatronAndConfigName.keySet().iterator(); iterator.hasNext(); ) {
                String patronId = iterator.next();
                Map<String, List<OleLoanDocument>> configMap = mapofNoticesForEachPatronAndConfigName.get
                        (patronId);
                for (Iterator<String> configIterator = configMap.keySet().iterator(); configIterator.hasNext(); ) {
                    String configName = configIterator.next();
                    Map overdueMap = new HashMap();
                    overdueMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, configName);
                    overdueMap.put(OLEConstants.LOAN_DOCUMENTS, configMap.get(configName));
                    Runnable deliverOverDueNoticesExecutor = new OverdueNoticesExecutor(overdueMap);
                    overDueNoticesExecutorService.execute(deliverOverDueNoticesExecutor);
                }

            }
            if(!overDueNoticesExecutorService.isShutdown()) {
                if(!executionFlag) {
                    executionFlag = Boolean.TRUE;
                    Thread.sleep(1200000);
                    overDueNoticesExecutorService.shutdown();
                    loanIds = loanWithNoticesDAO.getLoanIdsForNoticesByNoticeType(overdueNoticeToDate, OLEConstants.OVERDUE_NOTICE);
                    if (loanIds.size() > 0) {
                        loanDocuments = oleLoanDocumentDaoOjb.getLaonDocumentsFromLaondId(loanIds);
                        loanDocumentsWithItemInfo = getLoanDocumentWithItemInfo(loanDocuments, Boolean.FALSE.toString());
                        LOG.info("No of loanDocumentsWithItemInfo" + loanDocumentsWithItemInfo.size());
                        if (loanDocumentsWithItemInfo.size() > 0) {
                            generateOverdueNotice();
                        }
                    }
                } else {
                    executionFlag = Boolean.FALSE;
                    overDueNoticesExecutorService.shutdown();
                }
            }
        }
    }


    private Map<String, Map<String, List<OleLoanDocument>>> buildMapofNoticesForEachPatronAndConfigName(List<OleLoanDocument> loanDocuments, String noticeToDate, String noticeType) {
        Map<String, Map<String, List<OleLoanDocument>>> map = new HashMap<>();
        String patronId;
        String noticeContentConfigurationName;
        Timestamp noticetoSendDate = new Timestamp(System.currentTimeMillis());
        if (StringUtils.isNotEmpty(noticeToDate)) {
            noticetoSendDate = new Timestamp(new Date(noticeToDate).getTime());
        }
        //iterating over the loan documents for grouping of loan documents for each patron
        for (Iterator<OleLoanDocument> iterator = loanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            //iterating over deliver notices for identifying the notice to which we need to send mail that falls on the noticeToDate and put in a map with patron id as key
            for (OLEDeliverNotice oleDeliverNotice : oleLoanDocument.getDeliverNotices()) {
                Timestamp toBeSendDate = oleDeliverNotice.getNoticeToBeSendDate();
                if (oleDeliverNotice.getNoticeType().equals(noticeType) && toBeSendDate.compareTo(noticetoSendDate) < 0) {
                    patronId = oleLoanDocument.getPatronId();
                    noticeContentConfigurationName = oleDeliverNotice.getNoticeContentConfigName();
                    //if the map already contains an entry for that patron id then get the map for that patron id which has configurationName as key and list of loan documents as value
                    if (map.containsKey(patronId)) {
                        Map<String, List<OleLoanDocument>> configMap = map.get(patronId);
                        //if the configMap has an entry already for the configuration name then add the current loan document to that list .if there is no entry for that configuration name add a new entry to that configMap with this configuration name along with the loan document
                        if (configMap.containsKey(noticeContentConfigurationName)) {
                            configMap.get(noticeContentConfigurationName).add(oleLoanDocument);
                        } else {
                            List<OleLoanDocument> oleLoanDocumentList = new ArrayList<>();
                            oleLoanDocumentList.add(oleLoanDocument);
                            configMap.put(noticeContentConfigurationName, oleLoanDocumentList);
                        }
                    }
                    //if the map does not have an entry for the patron id add a new entry to the map
                    else {
                        List<OleLoanDocument> oleLoanDocumentList = new ArrayList<>();
                        oleLoanDocumentList.add(oleLoanDocument);
                        Map configMap = new HashMap();
                        configMap.put(noticeContentConfigurationName, oleLoanDocumentList);
                        map.put(patronId, configMap);
                    }
                    break;
                }
            }
        }
        return map;
    }

    private void iterateNotices(List<OleLoanDocument> notices, List<String> itemUUIDS) {
        List<OleLoanDocument> documents = new ArrayList<>();
        List<String> itemIds = new ArrayList<>();
        if (notices != null && notices.size() > 0) {
            boolean firstTime = true;
            String patronId = "";
            for (Object obj : notices) {
                OleLoanDocument loanDocument = (OleLoanDocument) obj;
                if (firstTime) {
                    patronId = loanDocument.getPatronId();
                }
                if (patronId != null && patronId.equals(loanDocument.getPatronId())) {
                    documents.add(loanDocument);
                    itemIds.add(loanDocument.getItemUuid());
                } else {
                    generateNoticeForOverdueAndCourtesy(patronId, documents, true, itemUUIDS);
                    documents = new ArrayList<>();
                    itemIds = new ArrayList<>();
                    documents.add(loanDocument);
                    itemIds.add(loanDocument.getItemUuid());
                    patronId = loanDocument.getPatronId();
                }
                firstTime = false;
            }
            generateNoticeForOverdueAndCourtesy(patronId, documents, true, itemUUIDS);
        }
    }

    public void generateNoticeForOverdueAndCourtesy(String patronId, List<OleLoanDocument> oleLoanDocuments, boolean overdue, List<String> itemUUIDS) {
        Map<String, String> patronMap = new HashMap<String, String>();
        patronMap.put("olePatronId", patronId);
        List<OlePatronDocument> olePatronDocuments = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
        if (olePatronDocuments != null && olePatronDocuments.size() > 0) {
            OlePatronDocument olePatronDocument = olePatronDocuments.get(0);
            if (oleLoanDocuments != null && oleLoanDocuments.size() > 0) {
                getNoticeList(oleLoanDocuments, olePatronDocument, overdue, itemUUIDS);
            }
        }
    }


    public void generateHoldExpirationNotice() throws Exception {
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService(OLEConstants.OLE_LOAN_DAO);
        Collection onHoldExpirationNotices = oleLoanDocumentDaoOjb.getOnHoldExpiredNotice();

        int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
        String threadPoolSizeValue = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.NOTICE_THREAD_POOL_SIZE);
        if (StringUtils.isNotBlank(threadPoolSizeValue)) {
            try {
                threadPoolSize = Integer.parseInt(threadPoolSizeValue);
            } catch (Exception e) {
                LOG.error("Invalid thread pool size from SystemParameter. So assigned default thread pool size" + threadPoolSize);
                threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
            }
        }

        Map<String, Map<String, List<OLEDeliverNotice>>> mapofNoticesForEachPatronAndConfigName = buildMapofNoticesForEachPatronAndConfigName((List<OLEDeliverNotice>) onHoldExpirationNotices);

        ExecutorService onHoldExpirationNoticesExecutorService = Executors.newFixedThreadPool(threadPoolSize);

        for (Iterator<String> iterator = mapofNoticesForEachPatronAndConfigName.keySet().iterator(); iterator.hasNext(); ) {
            String patronId = iterator.next();
            Map<String, List<OLEDeliverNotice>> configMap = mapofNoticesForEachPatronAndConfigName.get(patronId);
            for (Iterator<String> configIterator = configMap.keySet().iterator(); configIterator.hasNext(); ) {
                String configName = configIterator.next();
                Map requestMap = new HashMap();
                requestMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, configName);
                requestMap.put(OLEConstants.DELIVER_NOTICES, configMap.get(configName));
                Runnable onHoldExpirationNoticesExecutor = new HoldExpirationNoticesExecutor(requestMap);
                onHoldExpirationNoticesExecutorService.execute(onHoldExpirationNoticesExecutor);
            }
        }
        if(!onHoldExpirationNoticesExecutorService.isShutdown()) {
            onHoldExpirationNoticesExecutorService.shutdown();
        }
    }

    public void generateOnHoldCourtesyNotice() throws Exception {
        OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService(OLEConstants.OLE_LOAN_DAO);
        Collection onHoldCourtesyNotices = oleLoanDocumentDaoOjb.getOnHoldCourtesyNotice();

        int threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
        String threadPoolSizeValue = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.NOTICE_THREAD_POOL_SIZE);
        if (StringUtils.isNotBlank(threadPoolSizeValue)) {
            try {
                threadPoolSize = Integer.parseInt(threadPoolSizeValue);
            } catch (Exception e) {
                LOG.error("Invalid thread pool size from SystemParameter. So assigned default thread pool size" + threadPoolSize);
                threadPoolSize = OLEConstants.DEFAULT_NOTICE_THREAD_POOL_SIZE;
            }
        }

        Map<String, Map<String, List<OLEDeliverNotice>>> mapofNoticesForEachPatronAndConfigName = buildMapofNoticesForEachPatronAndConfigName((List<OLEDeliverNotice>) onHoldCourtesyNotices);

        ExecutorService onHoldCourtesynNoticesExecutorService = Executors.newFixedThreadPool(threadPoolSize);

        for (Iterator<String> iterator = mapofNoticesForEachPatronAndConfigName.keySet().iterator(); iterator.hasNext(); ) {
            String patronId = iterator.next();
            Map<String, List<OLEDeliverNotice>> configMap = mapofNoticesForEachPatronAndConfigName.get(patronId);
            for (Iterator<String> configIterator = configMap.keySet().iterator(); configIterator.hasNext(); ) {
                String configName = configIterator.next();
                Map requestMap = new HashMap();
                requestMap.put(OLEConstants.NOTICE_CONTENT_CONFIG_NAME, configName);
                requestMap.put(OLEConstants.DELIVER_NOTICES, configMap.get(configName));
                Runnable onHoldCourtesyNoticesExecutor = new HoldCourtesyNoticeExecutor(requestMap);
                onHoldCourtesynNoticesExecutorService.execute(onHoldCourtesyNoticesExecutor);
            }
        }
        if(!onHoldCourtesynNoticesExecutorService.isShutdown()) {
            onHoldCourtesynNoticesExecutorService.shutdown();
        }
    }

    public void deleteExpiredOnHoldNotices(List<OleDeliverRequestBo> expiredOnHoldNoticeBos, String operatorId) {
        for (OleDeliverRequestBo oleDeliverRequestBo : expiredOnHoldNoticeBos) {
            try {
                deleteRequest(oleDeliverRequestBo.getRequestId(), oleDeliverRequestBo.getItemUuid(),operatorId, oleDeliverRequestBo.getLoanTransactionRecordNumber(), ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.HOLD_REQUEST_EXPIRED));
            } catch (Exception e) {

            }
        }
    }

    private boolean isExpiredOnHoldNotice(OleDeliverRequestBo oleDeliverRequestBo) {
        boolean isExpired = false;
        if (oleDeliverRequestBo.getHoldExpirationDate() != null && oleDeliverRequestBo.getHoldExpirationDate().compareTo(getDateTimeService().getCurrentDate()) < 0) {
            isExpired = true;
        }
        return isExpired;
    }

    public void deleteTemporaryHistoryRecord() throws Exception {
        List<OlePatronDocument> patronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findAll(OlePatronDocument.class);
        String temporaryHistoryDay =  getParameterResolverInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.TEMPORARY_HISTORY_RECORD_DAYS);
        int temporaryHistoryDays;
        if(StringUtils.isNotEmpty(temporaryHistoryDay)){
            temporaryHistoryDays = Integer.parseInt(temporaryHistoryDay);
            for (OlePatronDocument olePatronDocument : patronDocumentList) {
                Map<String, String> requestMap = new HashMap<String, String>();
                requestMap.put(OLEConstants.OlePatron.PATRON_ID, olePatronDocument.getOlePatronId());
                List<OleTemporaryCirculationHistory> oleTemporaryCirculationHistoryList = (List<OleTemporaryCirculationHistory>) KRADServiceLocator.getBusinessObjectService().findMatching(OleTemporaryCirculationHistory.class, requestMap);
                List<OleTemporaryCirculationHistory> deleteRecords = new ArrayList<OleTemporaryCirculationHistory>();
                for (OleTemporaryCirculationHistory oleTemporaryCirculationHistory : oleTemporaryCirculationHistoryList) {
                    if(temporaryHistoryDays<determineDifferenceInDays(oleTemporaryCirculationHistory.getCheckInDate())){
                        deleteRecords.add(oleTemporaryCirculationHistory);
                    }
                }
                getBusinessObjectService().delete(deleteRecords);
            }
        }
    }


    private  int determineDifferenceInDays(Date checkInDate) {
        int result = 0;
        if(checkInDate!=null){
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(checkInDate);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(new Date());
            long diffInMillis = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
            result =(int) (diffInMillis / (24* 1000 * 60 * 60));
        }
        return result;
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
        oleNoticeBo.setPatronAddress(getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) : "");
        oleNoticeBo.setPatronEmailAddress(getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "");
        oleNoticeBo.setPatronPhoneNumber(getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
        oleNoticeBo.setNoticeName(OLEConstants.ONHOLD_EXPIRATION_NOTICE);
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
        oleNoticeBo.setPatronAddress(getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) : "");
        oleNoticeBo.setPatronEmailAddress(getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "");
        oleNoticeBo.setPatronPhoneNumber(getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
        oleNoticeBo.setNoticeName(OLEConstants.COURTESY_NOTICE);
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
        oleNoticeBo.setPatronAddress(getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) : "");
        oleNoticeBo.setPatronEmailAddress(getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "");
        oleNoticeBo.setPatronPhoneNumber(getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
        oleNoticeBo.setNoticeName(OLEConstants.OVERDUE_NOTICE);
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
        oleNoticeBo.setDueDate(oleLoanDocument.getLoanDueDate() != null ? oleLoanDocument.getLoanDueDate() : null);

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

    public String placeRequest(String patronBarcode, String operatorId, String itemBarcode, String requestType, String pickUpLocation, String itemIdentifier, String itemLocation, String itemType, String title, String author, String callNumber, boolean externalItem, String bibId, String requestLevel, java.sql.Date requestExpiryDate, String requestNote) {
        OLEPlaceRequest olePlaceRequest = new OLEPlaceRequest();
        OLEPlaceRequestConverter olePlaceRequestConverter = new OLEPlaceRequestConverter();
        ASRHelperServiceImpl asrHelperService = new ASRHelperServiceImpl();
        MaintenanceDocument newDocument = null;
        try {
            Thread.sleep(1000);
            try {
                if (null == GlobalVariables.getUserSession()) {
                    Person person = personService.getPerson(operatorId);
                    String principalName = person.getPrincipalName();
                    UserSession userSession = new UserSession(principalName);
                    GlobalVariables.setUserSession(userSession);
                }
                else {
                    String userName = GlobalVariables.getUserSession().getPrincipalName();
                    Person person = personService.getPerson(operatorId);
                    String principalName = person.getPrincipalName();
                    if(!principalName.equals(userName)) {
                        GlobalVariables.setUserSession(new UserSession(principalName));
                    }
                }
                newDocument = (MaintenanceDocument) getDocumentService().getNewDocument(OLEConstants.REQUEST_DOC_TYPE);
            } catch (WorkflowException e) {
                e.printStackTrace();
                olePlaceRequest.setBlockOverride(true);
                olePlaceRequest.setMessage("Cannot create");
                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
            }
            OleDeliverRequestBo oleDeliverRequestBo = null;
            oleDeliverRequestBo = (OleDeliverRequestBo) newDocument.getNewMaintainableObject().getDataObject();
            oleDeliverRequestBo.setCreateDate(new Timestamp(System.currentTimeMillis()));
            oleDeliverRequestBo.setRequestLevel(requestLevel);
            oleDeliverRequestBo.setRequestNote(requestNote);
            oleDeliverRequestBo.setBibId(bibId);
            if (requestExpiryDate != null) {
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
                    oleNoticeBo.setPatronAddress(getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setPatronEmailAddress(getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setPatronPhoneNumber(getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
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
                if(!StringUtils.isNotBlank(itemIdentifier)){
                    if (!StringUtils.isNotBlank(itemBarcode)) {
                        olePlaceRequest.setBlockOverride(true);
                        olePlaceRequest.setCode("014");
                        olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.ITEM_BARCODE_DOESNOT_EXISTS));
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);

                    }
                }
                oleDeliverRequestBo.setItemId(itemBarcode);
                oleDeliverRequestBo.setItemUuid(itemIdentifier);
                oleDeliverRequestBo.setItemStatus(OLEConstants.AVAILABLE);
                oleDeliverRequestBo.setItemType(itemType);
                oleDeliverRequestBo.setItemLocation(itemLocation);
                if (itemIdentifier == null || itemLocation == null || itemType == null) {
                    Thread.sleep(1000);
                    String itemUUID = itemIdentifier;
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
                    }else if (StringUtils.isBlank(itemBarcode)) {
                        try {
                            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                            org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
                            SearchResponse searchResponse = null;
                            search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ID, (itemIdentifier.contains("wio-") ? itemIdentifier : "wio-"+itemIdentifier)), ""));
                            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "id"));
                            search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "ItemBarcode_display"));

                            searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
                            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                                    String fieldName = searchResultField.getFieldName();
                                    String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                                    if (fieldName.equalsIgnoreCase(OLEConstants.ITEMBARCODE_DISPLAY)) {
                                        itemBarcode = fieldValue;
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
                    } else{
                        if(StringUtils.isNotBlank(itemUUID) && !itemUUID.contains("wio-")){
                            itemUUID = "wio-"+itemUUID;
                            oleDeliverRequestBo.setItemUuid(itemUUID);
                        }
                    }
                    if (itemType == null || itemLocation == null) {
                        Map<String, Object> detailMap = retrieveBIbItemHoldingData(itemUUID);
                        Bib bib = (Bib) detailMap.get(OLEConstants.BIB);
                        Item item = (Item) detailMap.get(OLEConstants.ITEM);
                        OleHoldings oleHoldings = (OleHoldings) detailMap.get(OLEConstants.HOLDING);
                        org.kuali.ole.docstore.common.document.Item item1 = (org.kuali.ole.docstore.common.document.Item) detailMap.get("documentItem");
                        if (item != null) {
                            oleDeliverRequestBo.setOleItem(item);
                            if (item.getCallNumber() != null && StringUtils.isNotBlank(item.getCallNumber().getNumber())) {
                                oleDeliverRequestBo.setCallNumber(item.getCallNumber().getNumber());
                                oleDeliverRequestBo.setCallNumberPrefix(item.getCallNumber().getPrefix());
                            }
                            if (StringUtils.isNotBlank(item.getCopyNumber())) {
                                oleDeliverRequestBo.setCopyNumber(item.getCopyNumber());
                            }
                            oleDeliverRequestBo.setVolumeNumber(item.getVolumeNumber() != null ? item.getVolumeNumber() : "");
                            oleDeliverRequestBo.setEnumeration(item.getEnumeration() != null ? item.getEnumeration() : "");
                            oleDeliverRequestBo.setChronology(item.getChronology() != null ? item.getChronology() : "");
                        }
                        if (oleHoldings != null) {
                            if (StringUtils.isBlank(oleDeliverRequestBo.getCallNumber()) && oleHoldings.getCallNumber() != null && StringUtils.isNotBlank(oleHoldings.getCallNumber().getNumber())) {
                                oleDeliverRequestBo.setCallNumber(oleHoldings.getCallNumber().getNumber());
                                oleDeliverRequestBo.setCallNumber(oleHoldings.getCallNumber().getNumber());
                            }
                            if (StringUtils.isBlank(oleDeliverRequestBo.getCopyNumber()) && StringUtils.isNotBlank(oleHoldings.getCopyNumber())) {
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
                    if (asrHelperService.isAnASRItem(itemLocation) && isValidASRItemStatus(oleDeliverRequestBo.getItemStatus()) && !oleDeliverRequestBo.getRequestTypeCode().equals(getLoanProcessor().getParameter(ASRConstants.ASR_TYP_RQST))) {
                        olePlaceRequest.setCode("700");
                        olePlaceRequest.setBlockOverride(true);
                        olePlaceRequest.setMessage("Cannot create " + oleDeliverRequestBo.getRequestTypeCode() + " for this item");
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    } else if (asrHelperService.isAnASRItem(itemLocation) && !isValidASRItemStatus(oleDeliverRequestBo.getItemStatus()) && oleDeliverRequestBo.getRequestTypeCode().equals(getLoanProcessor().getParameter(ASRConstants.ASR_TYP_RQST))) {
                        olePlaceRequest.setCode("701");
                        olePlaceRequest.setBlockOverride(true);
                        olePlaceRequest.setMessage("Cannot create " + oleDeliverRequestBo.getRequestTypeCode() + " for this item");
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    }
                    Map<String, String> locationMap = getCircDeskLocationResolver().getLocationMap(itemLocation);
                    oleDeliverRequestBo.setItemLibrary(locationMap.get(OLEConstants.ITEM_LIBRARY));
                    oleDeliverRequestBo.setItemInstitution(locationMap.get(OLEConstants.ITEM_INSTITUTION));
                    oleDeliverRequestBo.setItemCampus(locationMap.get(OLEConstants.ITEM_CAMPUS));
                    oleDeliverRequestBo.setItemCollection(locationMap.get(OLEConstants.ITEM_COLLECTION));
                    oleDeliverRequestBo.setShelvingLocation(locationMap.get(OLEConstants.ITEM_SHELVING));
                }
                oleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_OPERATOR);
                oleDeliverRequestBo.setOperatorCreateId(operatorId);
                oleDeliverRequestBo.setItemId(itemBarcode);
               /* if (!processOperator(operatorId)) {
                    olePlaceRequest.setCode("001");
                    olePlaceRequest.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVALID_OPRTR_ID));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }*/
                processRequestTypeByPickUpLocation(oleDeliverRequestBo);
                String message = this.patronRecordExpired(oleDeliverRequestBo)!=null?ConfigContext.getCurrentContextConfig().getProperty(this.patronRecordExpired(oleDeliverRequestBo)):this.patronRecordExpired(oleDeliverRequestBo);
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
                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                boolean valid = false;
                String errorMessage = fireRules(oleDeliverRequestBo,false,false);

                if ((oleDeliverRequestBo.getMessage() != null && !oleDeliverRequestBo.getMessage().isEmpty())) {
                    olePlaceRequest.setCode("500");
                    olePlaceRequest.setMessage(oleDeliverRequestBo.getMessage().replaceAll("<br/>", ""));
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
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
                    if (null == GlobalVariables.getUserSession()) {
                        Person person = personService.getPerson(operatorId);
                        String principalName = person.getPrincipalName();
                        UserSession userSession = new UserSession(principalName);
                        GlobalVariables.setUserSession(userSession);
                    } else {
                        String userName = GlobalVariables.getUserSession().getPrincipalName();
                        Person person = personService.getPerson(operatorId);
                        String principalName = person.getPrincipalName();
                        if (!principalName.equals(userName)) {
                            GlobalVariables.setUserSession(new UserSession(principalName));
                        }
                    }

                    newDocument = (MaintenanceDocument) getDocumentService().routeDocument(newDocument, null, null);
                    oleDeliverRequestBo = (OleDeliverRequestBo) newDocument.getNewMaintainableObject().getDataObject();
                } catch (WorkflowException e) {
                    e.printStackTrace();
                }


                String requestId = "";

                try {
                    requestId = ":" + OLEConstants.OleDeliverRequest.REQUEST_ID + ":" + oleDeliverRequestBo.getRequestId();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Request Raised Succesfully" + requestId);
                    }
                    if (externalItem) {
                        String noticeSendParameter = getLoanProcessor().getParameter(OLEParameterConstants.NCIP_ACCEPT_ITEM_NOTICE_INDICATOR);
                        if (noticeSendParameter != null && (noticeSendParameter.trim().isEmpty() || noticeSendParameter.equalsIgnoreCase("Y"))) {
                            oleNoticeBo.setNoticeName(OLEConstants.PICKUP_NOTICE);
                            Date pickupDate = new java.sql.Date(System.currentTimeMillis());
                            if (oleDeliverRequestBo.getOlePickUpLocation() != null && oleDeliverRequestBo.getOlePickUpLocation().getOnHoldDays() != null) {
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
                                        oleMailer.sendEmail(new EmailFrom(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail()), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.PICKUP_NOTICE_SUBJECT_LINE), new EmailBody(content), true);
                                    } else {
                                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                                            fromAddress = OLEConstants.KUALI_MAIL;
                                        }
                                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.PICKUP_NOTICE_SUBJECT_LINE), new EmailBody(content), true);
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
                                if (oleDeliverRequestBo.getRequestExpiryDate() != null) {
                                    olePlaceRequest.setExpirationDate(fmt.format(oleDeliverRequestBo.getRequestExpiryDate()));
                                }
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
                    if (oleDeliverRequestBo.getRequestExpiryDate() != null) {
                        olePlaceRequest.setExpirationDate(fmt.format(oleDeliverRequestBo.getRequestExpiryDate()));
                    }
                    olePlaceRequest.setBlockOverride(true);
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);

                }
                olePlaceRequest.setCode("021");
                olePlaceRequest.setRequestId(oleDeliverRequestBo.getRequestId());
                olePlaceRequest.setAvailableDate(getAvailableDate(itemBarcode));
                olePlaceRequest.setQueuePosition(String.valueOf(oleDeliverRequestBo.getBorrowerQueuePosition()));
                if (oleDeliverRequestBo.getRequestExpiryDate() != null) {
                    olePlaceRequest.setExpirationDate(fmt.format(oleDeliverRequestBo.getRequestExpiryDate()));
                }
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

    private DocumentService getDocumentService() {
        if (null == documentService) {
            documentService = GlobalResourceLoader.getService(OLEConstants.DOCUMENT_HEADER_SERVICE);
        }
        return documentService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    public String overridePlaceRequest(String patronBarcode, String operatorId, String itemBarcode, String requestType, String pickUpLocation, String itemIdentifier, String itemLocation, String itemType, String title, String author, String callNumber, boolean externalItem, String bibId, String requestLevel, java.sql.Date requestExpiryDate, String requestNote) {
        OLEPlaceRequest olePlaceRequest = new OLEPlaceRequest();
        OLEPlaceRequestConverter olePlaceRequestConverter = new OLEPlaceRequestConverter();
        ASRHelperServiceImpl asrHelperService = new ASRHelperServiceImpl();
        MaintenanceDocument newDocument = null;
        try {
            Thread.sleep(1000);
            try {
                if (null == GlobalVariables.getUserSession()) {
                    Person person = personService.getPerson(operatorId);
                    String principalName = person.getPrincipalName();
                    UserSession userSession = new UserSession(principalName);
                    GlobalVariables.setUserSession(userSession);
                }
                newDocument = (MaintenanceDocument) getDocumentService().getNewDocument(OLEConstants.REQUEST_DOC_TYPE);
            } catch (WorkflowException e) {
                e.printStackTrace();
                olePlaceRequest.setMessage("Cannot create");
                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
            }
            OleDeliverRequestBo oleDeliverRequestBo = null;
            oleDeliverRequestBo = (OleDeliverRequestBo) newDocument.getNewMaintainableObject().getDataObject();
            oleDeliverRequestBo.setCreateDate(new Timestamp(System.currentTimeMillis()));
            oleDeliverRequestBo.setRequestLevel(requestLevel);
            oleDeliverRequestBo.setRequestNote(requestNote);
            oleDeliverRequestBo.setBibId(bibId);
            if (requestExpiryDate != null) {
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
                    oleNoticeBo.setPatronAddress(getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronPreferredAddress(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setPatronEmailAddress(getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "");
                    oleNoticeBo.setPatronPhoneNumber(getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomePhoneNumber(entityTypeContactInfoBo) : "");
                } catch (Exception e) {
                    LOG.error("Exception", e);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Exception Occured while setting the patron information for the patron . Patron Barcode : " + oleDeliverRequestBo.getBorrowerBarcode());
                    }
                }
            }
            if (requestType != null) {
                Map<String, String> requestTypeMap = new HashMap<String, String>();
                requestTypeMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_CD, requestType);
                List<OleDeliverRequestType> oleDeliverRequestTypeList = (List<OleDeliverRequestType>) getBusinessObjectService().findMatching(OleDeliverRequestType.class, requestTypeMap);
                if (oleDeliverRequestTypeList != null && (oleDeliverRequestTypeList.size() > 0)) {
                    oleDeliverRequestBo.setRequestTypeId(oleDeliverRequestTypeList.get(0).getRequestTypeId());
                    oleDeliverRequestBo.setOleDeliverRequestType(oleDeliverRequestTypeList.get(0));
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
                }
            }
            try {
                oleDeliverRequestBo.setItemId(itemBarcode);
                oleDeliverRequestBo.setItemUuid(itemIdentifier);
                oleDeliverRequestBo.setItemStatus(OLEConstants.AVAILABLE);
                oleDeliverRequestBo.setItemType(itemType);
                oleDeliverRequestBo.setItemLocation(itemLocation);
                if (itemIdentifier == null || itemLocation == null || itemType == null) {
                    Thread.sleep(1000);
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
                    if (itemType == null || itemLocation == null) {
                        Map<String, Object> detailMap = retrieveBIbItemHoldingData(itemUUID);
                        Bib bib = (Bib) detailMap.get(OLEConstants.BIB);
                        Item item = (Item) detailMap.get(OLEConstants.ITEM);
                        OleHoldings oleHoldings = (OleHoldings) detailMap.get(OLEConstants.HOLDING);
                        org.kuali.ole.docstore.common.document.Item item1 = (org.kuali.ole.docstore.common.document.Item) detailMap.get("documentItem");
                        if (item != null) {
                            oleDeliverRequestBo.setOleItem(item);
                            if (item.getCallNumber() != null && StringUtils.isNotBlank(item.getCallNumber().getNumber())) {
                                oleDeliverRequestBo.setCallNumber(item.getCallNumber().getNumber());
                                oleDeliverRequestBo.setCallNumberPrefix(item.getCallNumber().getPrefix());
                            }
                            if (StringUtils.isNotBlank(item.getCopyNumber())) {
                                oleDeliverRequestBo.setCopyNumber(item.getCopyNumber());
                            }
                            oleDeliverRequestBo.setVolumeNumber(item.getVolumeNumber() != null ? item.getVolumeNumber() : "");
                            oleDeliverRequestBo.setEnumeration(item.getEnumeration() != null ? item.getEnumeration() : "");
                            oleDeliverRequestBo.setChronology(item.getChronology() != null ? item.getChronology() : "");
                        }
                        if (oleHoldings != null) {
                            if (StringUtils.isBlank(oleDeliverRequestBo.getCallNumber()) && oleHoldings.getCallNumber() != null && StringUtils.isNotBlank(oleHoldings.getCallNumber().getNumber())) {
                                oleDeliverRequestBo.setCallNumber(oleHoldings.getCallNumber().getNumber());
                            }
                            if (StringUtils.isBlank(oleDeliverRequestBo.getCopyNumber()) && oleHoldings.getCopyNumber() != null) {
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
                    if (asrHelperService.isAnASRItem(itemLocation) && isValidASRItemStatus(oleDeliverRequestBo.getItemStatus()) && !oleDeliverRequestBo.getRequestTypeCode().equals(getLoanProcessor().getParameter(ASRConstants.ASR_TYP_RQST))) {
                        olePlaceRequest.setCode("700");
                        olePlaceRequest.setMessage("Cannot create " + oleDeliverRequestBo.getRequestTypeCode() + " for this item");
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    } else if (asrHelperService.isAnASRItem(itemLocation) && !isValidASRItemStatus(oleDeliverRequestBo.getItemStatus()) && oleDeliverRequestBo.getRequestTypeCode().equals(getLoanProcessor().getParameter(ASRConstants.ASR_TYP_RQST))) {
                        olePlaceRequest.setCode("701");
                        olePlaceRequest.setMessage("Cannot create " + oleDeliverRequestBo.getRequestTypeCode() + " for this item");
                        return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                    }
                    Map<String, String> locationMap = getCircDeskLocationResolver().getLocationMap(itemLocation);
                    oleDeliverRequestBo.setItemLibrary(locationMap.get(OLEConstants.ITEM_LIBRARY));
                    oleDeliverRequestBo.setItemInstitution(locationMap.get(OLEConstants.ITEM_INSTITUTION));
                    oleDeliverRequestBo.setItemCampus(locationMap.get(OLEConstants.ITEM_CAMPUS));
                    oleDeliverRequestBo.setItemCollection(locationMap.get(OLEConstants.ITEM_COLLECTION));
                    oleDeliverRequestBo.setShelvingLocation(locationMap.get(OLEConstants.ITEM_SHELVING));
                }
                oleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_OPERATOR);
                oleDeliverRequestBo.setOperatorCreateId(operatorId);
                oleDeliverRequestBo.setItemId(itemBarcode);
                processRequestTypeByPickUpLocation(oleDeliverRequestBo);
                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                boolean valid = false;
                fireRules(oleDeliverRequestBo,false,true);
                if(oleDeliverRequestBo.getMessage()!=null&&oleDeliverRequestBo.getMessage().equals(OLEConstants.NO_RULE_FOUND)){
                    olePlaceRequest.setCode("500");
                    olePlaceRequest.setMessage(oleDeliverRequestBo.getMessage());
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
                }
                OleDeliverRequestBo oleDeliverRequestBo1 = null;
                boolean asrItem = false;
                try {
                    oleDeliverRequestBo1 = oleDeliverRequestBo;
                    oleDeliverRequestBo.setOleItem(null);
                    asrItem = asrHelperService.isAnASRItem(oleDeliverRequestBo.getItemLocation());
                    if (asrItem && oleDeliverRequestBo.getItemStatus().equals(getLoanProcessor().getParameter(ASRConstants.ASR_REQUEST_ITEM_STATUS))) {
                        oleDeliverRequestBo.setAsrFlag(true);
                        oleDeliverRequestBo.setRequestStatus("1");
                    }
                    newDocument.getDocumentHeader().setDocumentDescription(OLEConstants.NEW_REQUEST_DOC);
                    if (StringUtils.isNotBlank(oleDeliverRequestBo1.getRequestTypeId()) && (oleDeliverRequestBo1.getRequestTypeId().equals("1") || oleDeliverRequestBo1.getRequestTypeId().equals("2"))) {
                        oleDeliverRequestBo1.setRecallNoticeSentDate(new java.sql.Date(System.currentTimeMillis()));
                    }
                    newDocument.getNewMaintainableObject().setDataObject(oleDeliverRequestBo1);

                    newDocument = (MaintenanceDocument) getDocumentService().routeDocument(newDocument, null, null);
                    oleDeliverRequestBo = (OleDeliverRequestBo) newDocument.getNewMaintainableObject().getDataObject();
                } catch (WorkflowException e) {
                    e.printStackTrace();
                }


                String requestId = "";
                //  SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                try {
                    requestId = ":" + OLEConstants.OleDeliverRequest.REQUEST_ID + ":" + oleDeliverRequestBo.getRequestId();
                    Map<String,String> criteriaMap = new HashMap<>();
                    criteriaMap.put("itemId",itemBarcode);
                    List<OleCirculationHistory> circulationHistoryRecords = (List<OleCirculationHistory>) getBusinessObjectService().findMatching(OleCirculationHistory.class,criteriaMap);
                    OleCirculationHistory oleCirculationHistory = circulationHistoryRecords.get(circulationHistoryRecords.size()-1);
                    if(!StringUtils.isNotBlank(oleCirculationHistory.getOleRequestId())){
                        oleCirculationHistory.setOleRequestId(oleDeliverRequestBo.getRequestId());
                        businessObjectService.save(oleCirculationHistory);
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
                                        oleMailer.sendEmail(new EmailFrom(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail()), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.PICKUP_NOTICE_SUBJECT_LINE), new EmailBody(content), true);
                                    } else {
                                        String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                                            fromAddress = OLEConstants.KUALI_MAIL;
                                        }
                                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.PICKUP_NOTICE_SUBJECT_LINE), new EmailBody(content), true);
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
                                if (oleDeliverRequestBo.getRequestExpiryDate() != null) {
                                    olePlaceRequest.setExpirationDate(fmt.format(oleDeliverRequestBo.getRequestExpiryDate()));
                                }
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
                    if (oleDeliverRequestBo.getRequestExpiryDate() != null) {
                        olePlaceRequest.setExpirationDate(fmt.format(oleDeliverRequestBo.getRequestExpiryDate()));
                    }
                    return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);

                }
                olePlaceRequest.setCode("021");
                olePlaceRequest.setRequestId(oleDeliverRequestBo.getRequestId());
                olePlaceRequest.setAvailableDate(getAvailableDate(itemBarcode));
                olePlaceRequest.setQueuePosition(String.valueOf(oleDeliverRequestBo.getBorrowerQueuePosition()));
                olePlaceRequest.setMessage(OLEConstants.RQST_SUCCESS + requestId);
                if (oleDeliverRequestBo.getRequestExpiryDate() != null) {
                    olePlaceRequest.setExpirationDate(fmt.format(oleDeliverRequestBo.getRequestExpiryDate()));
                }
                return olePlaceRequestConverter.generatePlaceRequestXml(olePlaceRequest);
            } catch (Exception e) {
                LOG.error("Exception", e);
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


    private boolean isRecallRequestExist(String requestTypeId, String itemBarcode) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("itemId", itemBarcode);
        map.put("requestTypeId", requestTypeId);
        List<OleDeliverRequestBo> matchBos = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, map);
        if (matchBos != null && matchBos.size() > 0) {
            return true;
        }else{
            return false;
        }
    }

    public OleDeliverRequestBo processRequestToExecuteDrools(OleDeliverRequestBo oleDeliverRequestBo){
        List<OleDeliverRequestBo> recallList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> holdList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> pageList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> asrList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> requestsByBorrower = new ArrayList<OleDeliverRequestBo>();
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
        if (oleDeliverRequestBo.getRequestTypeId()!= null && (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2"))) {
            requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "1");
            recallList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
            requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "2");
            recallList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
        } else if (oleDeliverRequestBo.getRequestTypeId() != null && (oleDeliverRequestBo.getRequestTypeId().equals("3") || oleDeliverRequestBo.getRequestTypeId().equals("4"))) {
            //  holdList = (List<OleDeliverRequestBo>)getBusinessObjectService().findMatching(OleDeliverRequestBo.class,requestMap);
            requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "3");
            holdList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
            requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "4");
            holdList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
        } else if (oleDeliverRequestBo.getRequestTypeId() != null && (oleDeliverRequestBo.getRequestTypeId().equals("5") || oleDeliverRequestBo.getRequestTypeId().equals("6"))) {
            // pageList = (List<OleDeliverRequestBo>)getBusinessObjectService().findMatching(OleDeliverRequestBo.class,requestMap);
            requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "5");
            pageList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
            requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "6");
            pageList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
        } else if (oleDeliverRequestBo.getRequestTypeId() != null && (oleDeliverRequestBo.getRequestTypeId().equals("9"))) {
            requestMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_ID, "9");
            asrList.addAll((List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap));
        }
        HashMap<String, Object> termValues = new HashMap<String, Object>();
        oleDeliverRequestBo.setRecallRequestCount(new Integer(recallList.size()));
        oleDeliverRequestBo.setHoldRequestCount(new Integer(holdList.size()));
        oleDeliverRequestBo.setPageRequestCount(new Integer(pageList.size()));
        oleDeliverRequestBo.setAsrRequestCount(new Integer(asrList.size()));
        oleDeliverRequestBo.setFineAmount( overdueFineAmt + replacementFeeAmt + serviceFeeAmt);
        termValues.put(OLEConstants.MAX_NO_OF_HOLD_REQUEST, new Integer(holdList.size()) + 1);
        termValues.put(OLEConstants.MAX_NO_OF_PAGE_REQUEST, new Integer(pageList.size()) + 1);
        termValues.put(OLEConstants.MAX_NO_OF_ASR_REQUEST, new Integer(asrList.size()) + 1);
        termValues.put(OLEConstants.FINE_AMOUNT, overdueFineAmt + replacementFeeAmt + serviceFeeAmt);

        return oleDeliverRequestBo;

    }

    public EngineResults executeEngineResults(OleDeliverRequestBo oleDeliverRequestBo) {
        List<OleDeliverRequestBo> recallList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> holdList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> pageList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> asrList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> requestsByBorrower = new ArrayList<OleDeliverRequestBo>();
        Engine engine = KrmsApiServiceLocator.getEngine();
        ContextDefinition contextDefinition = KrmsRepositoryServiceLocator.getContextBoService().getContextByNameAndNamespace("OLE-CONTEXT", "OLE");
        AgendaDefinition agendaDefinition = KrmsRepositoryServiceLocator.getAgendaBoService().getAgendaByNameAndContextId(OLEConstants.REQUEST_AGENDA_NM, contextDefinition.getId());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(OLEConstants.AGENDA_NAME, agendaDefinition.getName());
        List<MatchBo> matchBos = (List<MatchBo>) getBusinessObjectService().findMatching(MatchBo.class, map);

        SelectionCriteria selectionCriteria =
                SelectionCriteria.createCriteria(null, getSelectionContext(contextDefinition.getName()), getAgendaContext(OLEConstants.REQUEST_AGENDA_NM));
        EngineResults engineResult = null;

        if (agendaDefinition != null) {

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
            dataCarrierService.addData(OLEConstants.DUE_DATE, oleLoanDocument != null ? oleLoanDocument.getLoanDueDate() : null);
            String patronId = oleDeliverRequestBo.getBorrowerId() != null ? oleDeliverRequestBo.getBorrowerId() : "";
            String itemId = oleDeliverRequestBo.getItemId() != null ? oleDeliverRequestBo.getItemId() : "";
            dataCarrierService.removeData(patronId + itemId);
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
            } else if (requestTypeId != null && (requestTypeId.equals("9"))) {
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
            // termValues.put(OLEConstants.ADDR_VERIFIED, isAddressVerified ? OLEConstants.TRUE : OLEConstants.FALSE);
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
            dataCarrierService.removeData(patronId + itemId);
            List<String> errorMessage = (List<String>) engineResult.getAttribute(OLEConstants.ERROR_ACTION);
            java.sql.Date d = (java.sql.Date) engineResult.getAttribute(OLEConstants.REQ_EXPIRATION_DATE);
            Timestamp recallDueDate = (Timestamp) engineResult.getAttribute(OLEConstants.RECALL_DUE_DATE);
            String notice = (String) engineResult.getAttribute(OLEConstants.NOTICE);
            oleDeliverRequestBo.setNoticeType(notice);
            if(oleDeliverRequestBo.getRequestExpiryDate() == null ){
                Map<String,String> locationMap = new HashMap<String,String>();
                locationMap.put("locationCode",oleDeliverRequestBo.getShelvingLocation());
                List<OleLocation> oleLocationBos = (List<OleLocation>)KRADServiceLocator.getBusinessObjectService().findMatching(OleLocation.class,locationMap);
                if(oleLocationBos!=null && oleLocationBos.size()>0){
                    Map<String,String> circulationDeskLocationMap = new HashMap<String,String>();
                    circulationDeskLocationMap.put("circulationDeskLocation",oleLocationBos.get(0).getLocationId());
                    List<OleCirculationDeskLocation> oleCirculationDeskLocationList = (List<OleCirculationDeskLocation>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDeskLocation.class,circulationDeskLocationMap);
                    if(oleCirculationDeskLocationList!=null && oleCirculationDeskLocationList.size()>0){
                        for(OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDeskLocationList){
                            if(oleCirculationDeskLocation.getCirculationPickUpDeskLocation()==null || (oleCirculationDeskLocation.getCirculationPickUpDeskLocation() !=null && oleCirculationDeskLocation.getCirculationPickUpDeskLocation().trim().isEmpty())) {
                                String requestExpirationDays= oleCirculationDeskLocation.getOleCirculationDesk().getRequestExpirationDays();
                                if(requestExpirationDays!=null && !requestExpirationDays.equalsIgnoreCase("0")){
                                    oleDeliverRequestBo.setRequestExpiryDate(addDate(new java.sql.Date(System.currentTimeMillis()), Integer.parseInt(requestExpirationDays)));
                                }
                            }
                        }
                    }

                }
            }
            if(oleDeliverRequestBo.getRequestExpiryDate() == null){
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
            } else if (failures.toString().trim().isEmpty()) {
                boolean overrideKRMS = true;
                if (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2")) {
                    overrideKRMS = isRecallRequestExist(oleDeliverRequestBo.getRequestTypeId(), oleDeliverRequestBo.getItemId());
                }
                if (overrideKRMS) {
                    if (oleLoanDocument != null && (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2"))) {
                        Timestamp itemDueDate = null;
                        if (ObjectUtils.isNotNull(oleLoanDocument)) {
                            itemDueDate = oleLoanDocument.getLoanDueDate();
                        }
                        Item oleItem = oleDeliverRequestBo.getOleItem();
                        if (itemDueDate == null && recallDueDate != null) {
                            oleDeliverRequestBo.setNewDueDate(new java.sql.Date(recallDueDate.getTime()));
                            oleLoanDocument.setLoanDueDate(recallDueDate);
                            oleDeliverRequestBo.setRecallDueDate(recallDueDate);
                            oleItem.setDueDateTime(recallDueDate.toString());
                            getBusinessObjectService().save(oleLoanDocument);
                            OleCirculationDesk oleCirculationDesk = oleLoanDocument.getCirculationLocationId() != null ?
                                    getCircDeskLocationResolver().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId()) : null;
                            oleLoanDocument.setOleCirculationDesk(oleCirculationDesk);
                            OLEDeliverNoticeHelperService oleDeliverNoticeHelperService = getOleDeliverNoticeHelperService();
                            oleDeliverNoticeHelperService.deleteDeliverNotices(oleLoanDocument.getLoanId());
                            try {
                                List<OLEDeliverNotice> deliverNotices = (List<OLEDeliverNotice>) engineResult.getAttribute("deliverNotices");
                                if (deliverNotices != null) {
                                    for (OLEDeliverNotice deliverNotice : deliverNotices) {
                                        deliverNotice.setLoanId(oleLoanDocument.getLoanId());
                                        deliverNotice.setPatronId(oleLoanDocument.getPatronId());
                                    }
                                    getBusinessObjectService().save(deliverNotices);
                                }
                            } catch (Exception e) {
                                LOG.info("Exception occured while updating the date in notice table");
                                LOG.error(e, e);
                            }
                            oleItem.setDueDateTime(getLoanProcessor().convertDateToString(recallDueDate, "MM/dd/yyyy HH:mm:ss"));
                            try {
                                updateItem(oleItem);
                            } catch (Exception e) {
                                if (LOG.isInfoEnabled()) {
                                    LOG.info("Exception occured while updating the item . " + e.getMessage());
                                }
                                LOG.error(e, e);
                            }
                        }
                        if (recallDueDate != null && itemDueDate != null) {
                            // if(itemDueDate.compareTo(recallDueDate) > 0){
                            oleDeliverRequestBo.setOriginalDueDate((new java.sql.Date(itemDueDate.getTime())));
                            oleDeliverRequestBo.setNewDueDate(new java.sql.Date(recallDueDate.getTime()));
                            oleLoanDocument.setLoanDueDate(recallDueDate);
                            oleDeliverRequestBo.setRecallDueDate(recallDueDate);
                            getBusinessObjectService().save(oleLoanDocument);
                            OleCirculationDesk oleCirculationDesk = oleLoanDocument.getCirculationLocationId() != null ?
                                    getCircDeskLocationResolver().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId()) : null;
                            oleLoanDocument.setOleCirculationDesk(oleCirculationDesk);
                            OLEDeliverNoticeHelperService oleDeliverNoticeHelperService = getOleDeliverNoticeHelperService();
                            oleDeliverNoticeHelperService.deleteDeliverNotices(oleLoanDocument.getLoanId());
                            try {
                                List<OLEDeliverNotice> deliverNotices = (List<OLEDeliverNotice>) engineResult.getAttribute("deliverNotices");
                                if (deliverNotices != null) {
                                    for (OLEDeliverNotice deliverNotice : deliverNotices) {
                                        deliverNotice.setLoanId(oleLoanDocument.getLoanId());
                                        deliverNotice.setPatronId(oleLoanDocument.getPatronId());
                                    }
                                    getBusinessObjectService().save(deliverNotices);
                                }
                            } catch (Exception e) {
                                LOG.info("Exception occured while updating the date in notice table");
                                LOG.error(e, e);
                            }
                            oleItem.setDueDateTime(getLoanProcessor().convertDateToString(recallDueDate, "MM/dd/yyyy HH:mm:ss"));
                            try {
                                updateItem(oleItem);
                            } catch (Exception e) {
                                if (LOG.isInfoEnabled()) {
                                    LOG.info("Exception occured while updating the item . " + e.getMessage());
                                }
                                LOG.error(e, e);
                            }
                       /* }else{
                            oleDeliverRequestBo.setNewDueDate((new java.sql.Date(oleLoanDocument.getLoanDueDate().getTime())));
                            oleDeliverRequestBo.setOriginalDueDate((new java.sql.Date(oleLoanDocument.getLoanDueDate().getTime())));
                        }*/
                        }
                    }
                }
            }
            dataCarrierService.addData(OLEConstants.ERROR_ACTION, null);
        }
        return engineResult;
    }


    public EngineResults executeEngineResultsForOverride(OleDeliverRequestBo oleDeliverRequestBo) {
        List<OleDeliverRequestBo> recallList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> holdList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> pageList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> asrList = new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestBo> requestsByBorrower = new ArrayList<OleDeliverRequestBo>();
        Engine engine = KrmsApiServiceLocator.getEngine();
        ContextDefinition contextDefinition = KrmsRepositoryServiceLocator.getContextBoService().getContextByNameAndNamespace("OLE-CONTEXT", "OLE");
        AgendaDefinition agendaDefinition = KrmsRepositoryServiceLocator.getAgendaBoService().getAgendaByNameAndContextId(OLEConstants.REQUEST_AGENDA_NM, contextDefinition.getId());
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(OLEConstants.AGENDA_NAME, agendaDefinition.getName());
        List<MatchBo> matchBos = (List<MatchBo>) getBusinessObjectService().findMatching(MatchBo.class, map);

        SelectionCriteria selectionCriteria =
                SelectionCriteria.createCriteria(null, getSelectionContext(contextDefinition.getName()), getAgendaContext(OLEConstants.REQUEST_AGENDA_NM));
        EngineResults engineResult = null;

        if (agendaDefinition != null) {

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
            dataCarrierService.addData(OLEConstants.DUE_DATE, oleLoanDocument != null ? oleLoanDocument.getLoanDueDate() : null);
            String patronId = oleDeliverRequestBo.getBorrowerId() != null ? oleDeliverRequestBo.getBorrowerId() : "";
            String itemId = oleDeliverRequestBo.getItemId() != null ? oleDeliverRequestBo.getItemId() : "";
            dataCarrierService.removeData(patronId + itemId);
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
            } else if (requestTypeId != null && (requestTypeId.equals("9"))) {
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
            // termValues.put(OLEConstants.ADDR_VERIFIED, isAddressVerified ? OLEConstants.TRUE : OLEConstants.FALSE);
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
            dataCarrierService.removeData(patronId + itemId);
            List<String> errorMessage = (List<String>) engineResult.getAttribute(OLEConstants.ERROR_ACTION);
            java.sql.Date d = (java.sql.Date) engineResult.getAttribute(OLEConstants.REQ_EXPIRATION_DATE);
            Timestamp recallDueDate = (Timestamp) engineResult.getAttribute(OLEConstants.RECALL_DUE_DATE);
            String notice = (String) engineResult.getAttribute(OLEConstants.NOTICE);
            oleDeliverRequestBo.setNoticeType(notice);
            if(oleDeliverRequestBo.getRequestExpiryDate() == null){
                Map<String,String> locationMap = new HashMap<String,String>();
                locationMap.put("locationCode",oleDeliverRequestBo.getShelvingLocation());
                List<OleLocation> oleLocationBos = (List<OleLocation>)KRADServiceLocator.getBusinessObjectService().findMatching(OleLocation.class,locationMap);
                if(oleLocationBos!=null && oleLocationBos.size()>0){
                    Map<String,String> circulationDeskLocationMap = new HashMap<String,String>();
                    circulationDeskLocationMap.put("circulationDeskLocation",oleLocationBos.get(0).getLocationId());
                    List<OleCirculationDeskLocation> oleCirculationDeskLocationList = (List<OleCirculationDeskLocation>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDeskLocation.class,circulationDeskLocationMap);
                    if(oleCirculationDeskLocationList!=null && oleCirculationDeskLocationList.size()>0){
                        for(OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDeskLocationList){
                            if(oleCirculationDeskLocation.getCirculationPickUpDeskLocation()==null || (oleCirculationDeskLocation.getCirculationPickUpDeskLocation() !=null && oleCirculationDeskLocation.getCirculationPickUpDeskLocation().trim().isEmpty())) {
                                String requestExpirationDays= oleCirculationDeskLocation.getOleCirculationDesk().getRequestExpirationDays();
                                if(requestExpirationDays!=null && !requestExpirationDays.equalsIgnoreCase("0")){
                                    oleDeliverRequestBo.setRequestExpiryDate(addDate(new java.sql.Date(System.currentTimeMillis()), Integer.parseInt(requestExpirationDays)));
                                }
                            }
                        }
                    }

                }
            }
            if(oleDeliverRequestBo.getRequestExpiryDate() == null){
                oleDeliverRequestBo.setRequestExpiryDate(d);
            }
            StringBuffer failures = new StringBuffer();


            boolean overrideKRMS = true;
            if (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2")) {
                overrideKRMS = isRecallRequestExist(oleDeliverRequestBo.getRequestTypeId(), oleDeliverRequestBo.getItemId());
            }
            if (overrideKRMS) {
                if (oleLoanDocument != null && (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2"))) {
                    Timestamp itemDueDate = null;
                    if (ObjectUtils.isNotNull(oleLoanDocument)) {
                        itemDueDate = oleLoanDocument.getLoanDueDate();
                    }
                    Item oleItem = oleDeliverRequestBo.getOleItem();
                    if (itemDueDate == null && recallDueDate != null) {
                        oleDeliverRequestBo.setNewDueDate(new java.sql.Date(recallDueDate.getTime()));
                        oleLoanDocument.setLoanDueDate(recallDueDate);
                        oleDeliverRequestBo.setRecallDueDate(recallDueDate);
                        oleItem.setDueDateTime(recallDueDate.toString());
                        getBusinessObjectService().save(oleLoanDocument);
                        OleCirculationDesk oleCirculationDesk = oleLoanDocument.getCirculationLocationId() != null ?
                                getCircDeskLocationResolver().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId()) : null;
                        oleLoanDocument.setOleCirculationDesk(oleCirculationDesk);
                        OLEDeliverNoticeHelperService oleDeliverNoticeHelperService = getOleDeliverNoticeHelperService();
                        oleDeliverNoticeHelperService.deleteDeliverNotices(oleLoanDocument.getLoanId());
                        try {
                            List<OLEDeliverNotice> deliverNotices = (List<OLEDeliverNotice>) engineResult.getAttribute("deliverNotices");
                            if (deliverNotices != null) {
                                for (OLEDeliverNotice deliverNotice : deliverNotices) {
                                    deliverNotice.setLoanId(oleLoanDocument.getLoanId());
                                    deliverNotice.setPatronId(oleLoanDocument.getPatronId());
                                }
                                getBusinessObjectService().save(deliverNotices);
                            }
                        } catch (Exception e) {
                            LOG.info("Exception occured while updating the date in notice table");
                            LOG.error(e, e);
                        }
                        oleItem.setDueDateTime(getLoanProcessor().convertDateToString(recallDueDate, "MM/dd/yyyy HH:mm:ss"));
                        try {
                            updateItem(oleItem);
                        } catch (Exception e) {
                            if (LOG.isInfoEnabled()) {
                                LOG.info("Exception occured while updating the item . " + e.getMessage());
                            }
                            LOG.error(e, e);
                        }
                    }
                    if (recallDueDate != null && itemDueDate != null) {
                        // if(itemDueDate.compareTo(recallDueDate) > 0){
                        oleDeliverRequestBo.setOriginalDueDate((new java.sql.Date(itemDueDate.getTime())));
                        oleDeliverRequestBo.setNewDueDate(new java.sql.Date(recallDueDate.getTime()));
                        oleLoanDocument.setLoanDueDate(recallDueDate);
                        oleDeliverRequestBo.setRecallDueDate(recallDueDate);
                        getBusinessObjectService().save(oleLoanDocument);
                        OleCirculationDesk oleCirculationDesk = oleLoanDocument.getCirculationLocationId() != null ?
                                getCircDeskLocationResolver().getOleCirculationDesk(oleLoanDocument.getCirculationLocationId()) : null;
                        oleLoanDocument.setOleCirculationDesk(oleCirculationDesk);
                        OLEDeliverNoticeHelperService oleDeliverNoticeHelperService = getOleDeliverNoticeHelperService();
                        oleDeliverNoticeHelperService.deleteDeliverNotices(oleLoanDocument.getLoanId());
                        try {
                            List<OLEDeliverNotice> deliverNotices = (List<OLEDeliverNotice>) engineResult.getAttribute("deliverNotices");
                            if (deliverNotices != null) {
                                for (OLEDeliverNotice deliverNotice : deliverNotices) {
                                    deliverNotice.setLoanId(oleLoanDocument.getLoanId());
                                    deliverNotice.setPatronId(oleLoanDocument.getPatronId());
                                }
                                getBusinessObjectService().save(deliverNotices);
                            }
                        } catch (Exception e) {
                            LOG.info("Exception occured while updating the date in notice table");
                            LOG.error(e, e);
                        }
                        oleItem.setDueDateTime(getLoanProcessor().convertDateToString(recallDueDate, "MM/dd/yyyy HH:mm:ss"));
                        try {
                            updateItem(oleItem);
                        } catch (Exception e) {
                            if (LOG.isInfoEnabled()) {
                                LOG.info("Exception occured while updating the item . " + e.getMessage());
                            }
                            LOG.error(e, e);
                        }

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

    public boolean checkForOverdueNotice(Date expDate) {
        Date curDat = new Date();
        if (expDate != null && curDat.compareTo(expDate) <= 0) {
            return false;
        }
        return true;
    }





    public org.kuali.ole.docstore.common.document.Item retrieveItemWithBibAndHoldingData(String itemUUID) {
        org.kuali.ole.docstore.common.document.Item item = null;
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        HoldingOlemlRecordProcessor holdingsOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        try {
            //retrieve the item information from docstore
            item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemUUID);
            //retrieve item content
            Item item1 = itemOlemlRecordProcessor.fromXML(item.getContent());
            item = populateInfoFromInstanceItemToDocItem(item1, item);
            OleHoldings oleHoldings = holdingsOlemlRecordProcessor.fromXML(item.getHolding().getContent());
            Holdings holdings = populateInfoFromInstanceHoldingToDocHoldings(oleHoldings, item.getHolding());
            item.setHolding(holdings);
        } catch (Exception e) {
            LOG.error(e);
        }
        return item;
    }


    public org.kuali.ole.docstore.common.document.Item populateInfoFromInstanceItemToDocItem(Item instanceItem, org.kuali.ole.docstore.common.document.Item documentItem) {

        documentItem.setBarcode(instanceItem.getAccessInformation().getBarcode());
        documentItem.setAnalytic(Boolean.valueOf(instanceItem.getAnalytic()));
        if (instanceItem.getCallNumber() != null) {
            documentItem.setCallNumber(instanceItem.getCallNumber().getNumber());
            documentItem.setCallNumberType(instanceItem.getCallNumber().getType());
            documentItem.setCallNumberPrefix(instanceItem.getCallNumber().getPrefix());
        }
        documentItem.setChronology(instanceItem.getChronology());
        documentItem.setCopyNumber(instanceItem.getCopyNumber());
        if (instanceItem.getItemStatus() != null) {
            documentItem.setItemStatus(instanceItem.getItemStatus().getCodeValue());
        }
        if (instanceItem.getItemType() != null) {
            documentItem.setItemType(instanceItem.getItemType().getCodeValue());
        }
        documentItem.setEnumeration(instanceItem.getEnumeration());
        documentItem.setVolumeNumber(instanceItem.getVolumeNumber());

        return documentItem;
    }

    public org.kuali.ole.docstore.common.document.Holdings populateInfoFromInstanceHoldingToDocHoldings(OleHoldings oleHoldings, Holdings holdings) {
        if (oleHoldings.getCallNumber() != null) {
            holdings.setCallNumber(oleHoldings.getCallNumber().getNumber());
            holdings.setCallNumberPrefix(oleHoldings.getCallNumber().getPrefix());
            holdings.setCallNumberType(oleHoldings.getCallNumber().getType());
        }
        holdings.setCopyNumber(oleHoldings.getCopyNumber());
        holdings.setHoldingsType(oleHoldings.getHoldingsType());
        //  holdings.setLocationName(getLocation(oleHoldings.getLocation()));
        return holdings;
    }


    public Map retrieveBIbItemHoldingData(String itemUUID) {
        Map<String, Object> bibMap = new HashMap<String, Object>();
        org.kuali.ole.docstore.common.document.Item item = null;
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        HoldingOlemlRecordProcessor holdingsOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        try {
            item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemUUID); // retrieving item object using itemUUID.
            bibMap.put(OLEConstants.DOCUMENT_ITEM, item);
            bibMap.put(OLEConstants.BIB, item.getHolding().getBib()); // retrieving bib object from holding.
            Item item1 = itemOlemlRecordProcessor.fromXML(item.getContent());
            bibMap.put(OLEConstants.ITEM, item1);
            OleHoldings oleHoldings = holdingsOlemlRecordProcessor.fromXML(item.getHolding().getContent());
            bibMap.put(OLEConstants.HOLDING, oleHoldings); // retrieving holding object.
        } catch (Exception e) {
            LOG.error(e);
        }
        return bibMap; // set all the objects in Map.
    }

    private void getNoticeList(List<OleLoanDocument> oleLoanDocuments, OlePatronDocument olePatronDocument, boolean overdue, List<String> itemUUIDS) {
        Long b1 = System.currentTimeMillis();
        List<OleLoanDocument> oleOverDueNoticeBoList = new ArrayList<>();
        List<OleLoanDocument> oleCourtesyNoticeList = new ArrayList<>();
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        StringBuffer mailContent = new StringBuffer();
        Document document = null;
        OutputStream outputStream = null;
        Set<String> overdueItemLocations = new HashSet<>();
        Set<String> courtesyItemLocations = new HashSet<>();
        try {
            String overdueNoticeType = getLoanProcessor().getParameter(OLEParameterConstants.OVERDUE_NOTICE_TYPE);
            String courtesyNoticeType = getLoanProcessor().getParameter(OLEParameterConstants.COURTESY_NOTICE_TYPE);
            mailContent.append(oleDeliverBatchService.getHeaderAndPatronContent(olePatronDocument, overdue));
            oleDeliverBatchService.getHeaderAndPatronPDFContent(olePatronDocument, overdue);
            document = oleDeliverBatchService.getOverdueDocument();
            outputStream = oleDeliverBatchService.getOverdueOutPutStream();
            for (OleLoanDocument oleLoanDocument : oleLoanDocuments) {
                boolean overdueExists = false;
                oleLoanDocument.setOlePatron(olePatronDocument);
                if (LOG.isInfoEnabled()) {
                    LOG.info("oleLoanDocument---->ItemID--" + oleLoanDocument.getItemId() + ": Patron Barcode--" + oleLoanDocument.getPatronBarcode());
                    LOG.info("oleItem.getItemStatusEffectiveDate()" + oleLoanDocument.getItemStatusEffectiveDate());
                }
                if (oleLoanDocument.getItemTypeName() != null) {
                    oleLoanDocument.setItemType(getItemTypeCodeByName(oleLoanDocument.getItemTypeName()));
                }
                for (OLEDeliverNotice oleDeliverNotice : oleLoanDocument.getDeliverNotices()) {
                    String noticeType = oleDeliverNotice.getNoticeType();
                    if (oleLoanDocument.getReplacementBill() != null && oleLoanDocument.getReplacementBill().intValue() > 0 && noticeType.equalsIgnoreCase(OLEConstants.NOTICE_LOST)) {
                        int noOfOverdueNoticeSent = Integer.parseInt(oleLoanDocument.getNumberOfOverdueNoticesSent() != null ? oleLoanDocument.getNumberOfOverdueNoticesSent() : "0");
                        noOfOverdueNoticeSent = noOfOverdueNoticeSent + 1;
                        oleLoanDocument.setNumberOfOverdueNoticesSent(Integer.toString(noOfOverdueNoticeSent));
                        oleLoanDocument.setOverDueNoticeDate(new java.sql.Date(System.currentTimeMillis()));
                        String billNumber = getLoanProcessor().generatePatronBillPayment(oleLoanDocument, OLEConstants.REPLACEMENT_FEE, oleLoanDocument.getReplacementBill());
                        oleLoanDocument.setRepaymentFeePatronBillId(billNumber);
                        itemUUIDS.add(oleLoanDocument.getItemUuid());
                        getBusinessObjectService().delete(oleDeliverNotice);
                    }
                    if (noticeType.equalsIgnoreCase(OLEConstants.OVERDUE_NOTICE)) {
                        oleOverDueNoticeBoList.add(oleLoanDocument);
                        if (!overdueExists) {
                            mailContent.append(oleDeliverBatchService.getOverdueNoticeHTMLContent(oleLoanDocument));
                        }
                        overdueItemLocations.add(oleLoanDocument.getItemLocation());
                        noticeType = noticeType == null ? overdueNoticeType : noticeType;
                        if (!overdueExists) {
                            oleDeliverBatchService.getOverdueNoticePDFContent(oleLoanDocument, overdue, document);
                        }
                        int noOfOverdueNoticeSent = Integer.parseInt(oleLoanDocument.getNumberOfOverdueNoticesSent() != null ? oleLoanDocument.getNumberOfOverdueNoticesSent() : "0");
                        noOfOverdueNoticeSent = noOfOverdueNoticeSent + 1;
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Updated Loan Record : " + oleLoanDocument);
                        }
                        oleLoanDocument.setNumberOfOverdueNoticesSent(Integer.toString(noOfOverdueNoticeSent));
                        oleLoanDocument.setOverDueNoticeDate(new java.sql.Date(System.currentTimeMillis()));
                        overdueExists = true;
                        getBusinessObjectService().delete(oleDeliverNotice);
                    } else if (noticeType.equalsIgnoreCase(OLEConstants.COURTESY_NOTICE) && olePatronDocument.isCourtesyNotice() && !oleLoanDocument.isCourtesyNoticeFlag()) {
                        oleCourtesyNoticeList.add(oleLoanDocument);
                        mailContent.append(oleDeliverBatchService.getOverdueNoticeHTMLContent(oleLoanDocument));
                        courtesyItemLocations.add(oleLoanDocument.getItemLocation());
                        oleDeliverBatchService.getOverdueNoticePDFContent(oleLoanDocument, overdue, document);
                        noticeType = noticeType == null ? courtesyNoticeType : noticeType;
                        oleLoanDocument.setCourtesyNoticeFlag(!overdue);
                        getBusinessObjectService().delete(oleDeliverNotice);
                    }

                    olePatronDocument = oleLoanDocument.getOlePatron();
                    if (noticeType != null && noticeType.equalsIgnoreCase(OLEConstants.SMS)) {
                        //TODO : sms in progress.
                    }
                }
                getBusinessObjectService().save(oleLoanDocument);
                saveOLEDeliverNoticeHistory(oleLoanDocument);
            }

        } catch (Exception e) {
            LOG.error("Exception in generateNotices()" + e.getMessage(), e);
        }
        Long b2 = System.currentTimeMillis();
        Long b3 = b2 - b1;
        LOG.info("Time taken to send the notices " + b3);
        boolean isFileNeedToDelete = true;
        if (overdue) {
            if (oleOverDueNoticeBoList != null && oleOverDueNoticeBoList.size() > 0) {
                String replyToEmail = null;
                if (overdueItemLocations.size() == 1) {
                    replyToEmail = getCircDeskLocationResolver().getReplyToEmail(overdueItemLocations.iterator().next());
                }
                if (replyToEmail != null) {
                    sendMailsToPatron(olePatronDocument, mailContent.toString(), replyToEmail,noticeService.getNoticeSubjectForNoticeType(OLEConstants.OVERDUE_NOTICE));
                } else {
                    String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                    sendMailsToPatron(olePatronDocument, mailContent.toString(), fromAddress,noticeService.getNoticeSubjectForNoticeType(OLEConstants.OVERDUE_NOTICE));
                }
                try {
                    if (document != null && outputStream != null) {
                        oleDeliverBatchService.getPdfFooter(document, outputStream);
                        isFileNeedToDelete = false;
                    }
                } catch (Exception e) {
                    LOG.error("PDF overdue notice error");
                }

            }
        } else {
            if (oleCourtesyNoticeList != null && oleCourtesyNoticeList.size() > 0) {
                String replyToEmail = null;
                if (courtesyItemLocations.size() == 1) {
                    replyToEmail = getCircDeskLocationResolver().getReplyToEmail(courtesyItemLocations.iterator().next());
                }
                if (replyToEmail != null) {
                    sendMailsToPatron(olePatronDocument, mailContent.toString(), replyToEmail,noticeService.getNoticeSubjectForNoticeType(OLEConstants.COURTESY_NOTICE));
                } else {
                    String fromAddress = getLoanProcessor().getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                    sendMailsToPatron(olePatronDocument, mailContent.toString(), fromAddress,noticeService.getNoticeSubjectForNoticeType(OLEConstants.COURTESY_NOTICE));
                }
                try {
                    if (document != null && outputStream != null) {
                        oleDeliverBatchService.getPdfFooter(document, outputStream);
                        isFileNeedToDelete = false;
                    }
                } catch (Exception e) {
                    LOG.error("PDF courtesy notice error");
                }
            }
        }
        if (isFileNeedToDelete) {
            oleDeliverBatchService.cleanZeroByteFiles();
        }
    }

    public void sendMailsToPatron(OlePatronDocument olePatronDocument, String noticeContent, String fromAddress,String mailSubject) {

        Long b1 = System.currentTimeMillis();
        OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
        String emailAddress = "";
        try {
            EntityTypeContactInfoBo entityTypeContactInfoBo = olePatronDocument.getEntity().getEntityTypeContactInfos().get(0);
            emailAddress = getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) != null ? getOlePatronHelperService().getPatronHomeEmailId(entityTypeContactInfoBo) : "";
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
                    oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(emailAddress), new EmailSubject(mailSubject), new EmailBody(noticeContent), true);
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
     *
     * @param oleLoanDocuments
     * @return
     */
    public List<OleLoanDocument> getLoanDocumentWithItemInfo(List<OleLoanDocument> oleLoanDocuments, String claimsReturnedFlag) throws Exception {
        Long startTime = System.currentTimeMillis();
        List<OleLoanDocument> loanDocumentsWithItemInfo = new ArrayList<OleLoanDocument>();
        if (oleLoanDocuments != null && oleLoanDocuments.size() > 0) {
            Map<String, OleLoanDocument> loanDocumentMap = new HashMap<String, OleLoanDocument>();
            SearchParams searchParams = new SearchParams();
            List<SearchCondition> searchConditions = new ArrayList<>();
            SearchResponse searchResponse = new SearchResponse();
            String numberOfRecords = getLoanProcessor().getParameter(OLEConstants.NUMBER_OF_ITEM_INFO);
            List<List<OleLoanDocument>> slicedList = (List<List<OleLoanDocument>>) splitListToSubList(oleLoanDocuments, Integer.valueOf(numberOfRecords).intValue());
            for (List<OleLoanDocument> oleLoanDocumentList : slicedList) {
                try {
                    searchConditions = new ArrayList<>();
                    searchResponse = new SearchResponse();
                    searchParams = new SearchParams();
                    for (OleLoanDocument oleLoanDocument : oleLoanDocumentList) {
                        loanDocumentMap.put(oleLoanDocument.getItemUuid(), oleLoanDocument);
                        searchConditions.add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(DocType.ITEM.getCode(), "id", oleLoanDocument.getItemUuid()), "AND"));
                        searchConditions.add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(DocType.ITEM.getCode(), DocstoreConstants.CLAIMS_RETURNED_FLAG_SEARCH, claimsReturnedFlag), "OR"));
                    }
                    searchParams.setPageSize(Integer.parseInt(OLEConstants.MAX_PAGE_SIZE_FOR_LOAN));
                    buildSearchParams(searchParams);
                    searchParams.getSearchConditions().addAll(searchConditions);
                    try {
                        searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                    } catch (Exception e) {
                        LOG.error(e, e);
                        throw new Exception("Exception occured while fetching data from solr");
                    }
                    try {
                        List<OleLoanDocument> processedLoanDocuments = buildSearchResultsFields(searchResponse, loanDocumentMap);
                        if(!processedLoanDocuments.isEmpty())
                        {
                            loanDocumentsWithItemInfo.addAll(processedLoanDocuments);
                        }
                    } catch (Exception e) {
                        LOG.error(e, e);
                        throw new Exception("Exception occured while setting the item information to loan documents");
                    }
                } catch (Exception e) {
                    LOG.info("Exception occured while setting the item information to the loan documents");
                    LOG.error(e, e);
                }
            }
        }
        Long endTime = System.currentTimeMillis();
        Long timeDifference = endTime - startTime;
        LOG.info("Time Taken to set the item information in the loan records in milliseconds : " + timeDifference);
        return loanDocumentsWithItemInfo;
    }

    /**
     * This method is used to set the values from the docstore response
     *
     * @param searchResponse
     * @param loanDocumentMap
     * @return
     * @throws Exception
     */
    public List<OleLoanDocument> buildSearchResultsFields(SearchResponse searchResponse, Map<String, OleLoanDocument> loanDocumentMap) throws Exception {
        int count = 0;
        Map<String, String> itemTypeNameMap = new HashMap<String, String>();
        Map<String, String> itemTypeDescMap = new HashMap<String, String>();
        List<OleInstanceItemType> instanceItemTypeList = (List<OleInstanceItemType>) getBusinessObjectService().findAll(OleInstanceItemType.class);
        if (instanceItemTypeList != null && instanceItemTypeList.size() > 0) {
            for (OleInstanceItemType oleInstanceItemType : instanceItemTypeList) {
                itemTypeNameMap.put(oleInstanceItemType.getInstanceItemTypeName(), oleInstanceItemType.getInstanceItemTypeCode());
                itemTypeDescMap.put(oleInstanceItemType.getInstanceItemTypeCode(),oleInstanceItemType.getInstanceItemTypeDesc());
            }
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        List<OleLoanDocument> oleLoanDocuments = new ArrayList<>();
        if (searchResponse != null) {
            for (SearchResult searchResult : searchResponse.getSearchResults()) {
                try {
                    OleLoanDocument oleLoanDocument = null;
                    boolean found = false;
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        if (searchResultField.getFieldValue() != null) {
                            if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
                                found = loanDocumentMap.containsKey(searchResultField.getFieldValue());
                            }
                            if (found) {
                                if (searchResultField.getFieldName().equalsIgnoreCase("id")) {
                                    oleLoanDocument = loanDocumentMap.get(searchResultField.getFieldValue());
                                    oleLoanDocument.setItemUuid(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("ItemBarcode_display")) {
                                    oleLoanDocument.setItemId(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                                    oleLoanDocument.setBibUuid(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("Title_display")) {
                                    oleLoanDocument.setTitle(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("Author_display")) {
                                    oleLoanDocument.setAuthor(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("holdingsIdentifier")) {
                                    oleLoanDocument.setInstanceUuid(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("claimsReturnedNote")) {
                                    oleLoanDocument.setClaimsReturnNote(searchResultField.getFieldValue());
                                    //  count++;
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("ClaimsReturnedFlag_display") && searchResultField.getFieldValue().equalsIgnoreCase("true")) {
                                    oleLoanDocument.setClaimsReturnedIndicator(true);
                                    count++;
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("Location_display")) {
                                    String location= searchResultField.getFieldValue().split("/")[searchResultField.getFieldValue().split("/").length - 1];
                                    getOleLoanDocumentsFromSolrBuilder().getLocationBySolr(location, oleLoanDocument);
                                    oleLoanDocument.setItemFullLocation(searchResultField.getFieldValue());
                                    Map<String, String> locationMap = getCircDeskLocationResolver().getLocationMap(oleLoanDocument.getItemFullLocation());
                                    oleLoanDocument.setItemInstitution(locationMap.get(OLEConstants.ITEM_INSTITUTION));
                                    oleLoanDocument.setItemCampus(locationMap.get(OLEConstants.ITEM_CAMPUS));
                                    oleLoanDocument.setItemCollection(locationMap.get(OLEConstants.ITEM_COLLECTION));
                                    oleLoanDocument.setItemLibrary(locationMap.get(OLEConstants.ITEM_LIBRARY));
                                    oleLoanDocument.setItemLocation(locationMap.get(OLEConstants.ITEM_SHELVING));

                                } else if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsLocation_display") &&
                                        (oleLoanDocument.getItemLocation() == null || oleLoanDocument.getItemLocation().isEmpty())) {
                                    String location= searchResultField.getFieldValue().split("/")[searchResultField.getFieldValue().split("/").length - 1];
                                    getOleLoanDocumentsFromSolrBuilder().getLocationBySolr(location, oleLoanDocument);
                                    oleLoanDocument.setItemFullLocation(searchResultField.getFieldValue());
                                    Map<String, String> locationMap = getCircDeskLocationResolver().getLocationMap(oleLoanDocument.getItemFullLocation());
                                    oleLoanDocument.setItemInstitution(locationMap.get(OLEConstants.ITEM_INSTITUTION));
                                    oleLoanDocument.setItemCampus(locationMap.get(OLEConstants.ITEM_CAMPUS));
                                    oleLoanDocument.setItemCollection(locationMap.get(OLEConstants.ITEM_COLLECTION));
                                    oleLoanDocument.setItemLibrary(locationMap.get(OLEConstants.ITEM_LIBRARY));
                                    oleLoanDocument.setItemLocation(locationMap.get(OLEConstants.ITEM_SHELVING));

                                } else if (searchResultField.getFieldName().equalsIgnoreCase("claimsReturnedFlagCreateDate")) {
                                    String[] formatStrings = new String[]{"MM/dd/yyyy hh:mm:ss", "MM/dd/yyyy", "yyyy-MM-dd hh:mm:ss"};
                                    Date date = getOleLoanDocumentsFromSolrBuilder().tryParse(formatStrings, searchResultField.getFieldValue());
                                    oleLoanDocument.setClaimsReturnedDate(new Timestamp(date.getTime()));

                                } else if (searchResultField.getFieldName().equalsIgnoreCase("CallNumber_display")) {
                                    oleLoanDocument.setItemCallNumber(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("TemporaryItemTypeFullValue_search")) {
                                    oleLoanDocument.setItemTypeName(searchResultField.getFieldValue());
                                    oleLoanDocument.setItemType(itemTypeNameMap.get(oleLoanDocument.getItemTypeName()));
                                    oleLoanDocument.setItemTypeDesc(itemTypeDescMap.get(oleLoanDocument.getItemType()));
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("ItemTypeFullValue_display") &&
                                        (oleLoanDocument.getItemTypeName() == null || oleLoanDocument.getItemTypeName().isEmpty())) {
                                    oleLoanDocument.setItemTypeName(searchResultField.getFieldValue());
                                    oleLoanDocument.setItemType(itemTypeNameMap.get(oleLoanDocument.getItemTypeName()));
                                    oleLoanDocument.setItemTypeDesc(itemTypeDescMap.get(oleLoanDocument.getItemType()));
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("Enumeration_display")) {
                                    oleLoanDocument.setEnumeration(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("Chronology_display")) {
                                    oleLoanDocument.setChronology(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("ItemStatus_display")) {
                                    oleLoanDocument.setItemStatus(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("ItemDamagedStatus_display")) {
                                    oleLoanDocument.setItemDamagedStatus(searchResultField.getFieldValue().equalsIgnoreCase("true"));
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("DamagedItemNote_search")) {
                                    oleLoanDocument.setItemDamagedNote(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("MissingPieceFlagNote_search")) {
                                    oleLoanDocument.setMissingPieceNote(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("MissingPieceFlag_display")) {
                                    oleLoanDocument.setMissingPieceFlag(searchResultField.getFieldValue().equalsIgnoreCase("true"));
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("CopyNumber_search")) {
                                    oleLoanDocument.setItemCopyNumber(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsCopyNumber_search") &&
                                        (oleLoanDocument.getItemCopyNumber() == null || oleLoanDocument.getItemCopyNumber().isEmpty())) {
                                    oleLoanDocument.setItemCopyNumber(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("HoldingsCallNumber_search") &&
                                        (oleLoanDocument.getItemCallNumber() == null || oleLoanDocument.getItemCallNumber().isEmpty())) {
                                    oleLoanDocument.setItemCallNumber(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("MissingPieceCount_search")) {
                                    oleLoanDocument.setMissingPiecesCount(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("NumberOfPieces_search")) {
                                    oleLoanDocument.setItemNumberOfPieces(Integer.parseInt(searchResultField.getFieldValue()));
                                    oleLoanDocument.setBackUpNoOfPieces(searchResultField.getFieldValue());
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("ClaimsReturnedFlag_search")) {
                                    if(!oleLoanDocument.isClaimsReturnedIndicator()) {
                                        oleLoanDocument.setClaimsReturnedIndicator(Boolean.valueOf(searchResultField.getFieldValue()));
                                    }
                                } else if (searchResultField.getFieldName().equalsIgnoreCase("itemStatusEffectiveDate")) {
                                    oleLoanDocument.setItemStatusEffectiveDate(searchResultField.getFieldValue());
                                    // LOG.info("Item status Effective date from solr : " + searchResultField.getFieldValue() );
                                }else if(searchResultField.getFieldName().equalsIgnoreCase("CallNumberPrefix_display")){
                                    oleLoanDocument.setItemCallNumberPrefix(searchResultField.getFieldValue());
                                }
                            }
                        }

                    }
                    found = false;
                    if (oleLoanDocument.getOlePatron() != null) {
                        oleLoanDocument.getOlePatron().setNumberOfClaimsReturned(count);
                    }
                    oleLoanDocuments.add(oleLoanDocument);
                } catch (Exception e) {

                    LOG.info("Exception occured while setting the item info for the loan ");
                    LOG.error(e,e);
                }
            }
        }
        return oleLoanDocuments;
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

    public String getItemTypeCodeByName(String itemTypeName) {
        String itemTypeCode = "";
        List<OleInstanceItemType> instanceItemTypeList = null;
        Map<String, String> instanceItemTypeMap = new HashMap<String, String>();
        instanceItemTypeMap.put("instanceItemTypeName", itemTypeName);
        instanceItemTypeList = (List<OleInstanceItemType>) getBusinessObjectService().findMatching(OleInstanceItemType.class, instanceItemTypeMap);
        if (instanceItemTypeList != null && instanceItemTypeList.size() > 0) {
            itemTypeCode = instanceItemTypeList.get(0).getInstanceItemTypeCode();
        }
        return itemTypeCode;
    }

    public String getItemTypeDescByName(String itemTypeName) {
        String itemTypeDesc = "";
        List<OleInstanceItemType> instanceItemTypeList = null;
        Map<String, String> instanceItemTypeMap = new HashMap<String, String>();
        instanceItemTypeMap.put("instanceItemTypeName", itemTypeName);
        instanceItemTypeList = (List<OleInstanceItemType>) getBusinessObjectService().findMatching(OleInstanceItemType.class, instanceItemTypeMap);
        if (instanceItemTypeList != null && instanceItemTypeList.size() > 0) {
            itemTypeDesc = instanceItemTypeList.get(0).getInstanceItemTypeDesc();
        }
        return itemTypeDesc;
    }

    public String getItemTypeDescByCode(String itemType) {
        String itemTypeDesc = "";
        List<OleInstanceItemType> instanceItemTypeList = null;
        Map<String, String> instanceItemTypeMap = new HashMap<String, String>();
        instanceItemTypeMap.put("instanceItemTypeCode", itemType);
        instanceItemTypeList = (List<OleInstanceItemType>) getBusinessObjectService().findMatching(OleInstanceItemType.class, instanceItemTypeMap);
        if (instanceItemTypeList != null && instanceItemTypeList.size() > 0) {
            itemTypeDesc = instanceItemTypeList.get(0).getInstanceItemTypeDesc();
        }
        return itemTypeDesc;
    }

    /**
     * This method is to update the item
     *
     * @param oleItem
     * @throws Exception
     */
    public void updateItem(Item oleItem) throws Exception {
        String itemUuid = oleItem.getItemIdentifier();
        String itemXmlContent = getItemOlemlRecordProcessor().toXML(oleItem);
        try {
            org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
            item.setId(itemUuid);
            item.setContent(itemXmlContent);
            item.setCategory(OLEConstants.WORK_CATEGORY);
            item.setType(DocType.ITEM.getCode());
            item.setFormat(OLEConstants.OLEML_FORMAT);
            item.setStaffOnly(oleItem.isStaffOnlyFlag());
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


    public void buildSearchParams(SearchParams searchParams) {
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
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "HoldingsLocation_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "MissingPieceCount_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "NumberOfPieces_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CallNumberPrefix_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "CopyNumber_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "proxyBorrower"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "VolumeNumberLabel_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "dueDateTime"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "NumberOfRenew_display"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "checkOutDateTime"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "ClaimsReturnedFlag_search"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item", "itemStatusEffectiveDate"));
    }


    public void saveOLEDeliverNoticeHistory(OleLoanDocument oleLoanDocument) {
        for (OLEDeliverNotice oleDeliverNotice : oleLoanDocument.getDeliverNotices()) {
            OLEDeliverNoticeHistory oleDeliverNoticeHistory = new OLEDeliverNoticeHistory();
            oleDeliverNoticeHistory.setLoanId(oleLoanDocument.getLoanId());
            oleDeliverNoticeHistory.setNoticeType(oleDeliverNotice.getNoticeType());
            oleDeliverNoticeHistory.setNoticeSentDate(new Timestamp(new Date().getTime()));
            oleDeliverNoticeHistory.setPatronId(oleLoanDocument.getPatronId());
            oleDeliverNoticeHistory.setNoticeSendType(oleDeliverNotice.getNoticeSendType());
            getBusinessObjectService().save(oleDeliverNoticeHistory);
        }
    }


    private void itemStatusBulkUpdate(List<String> itemUUIDs) {
        List<org.kuali.ole.docstore.common.document.Item> items = null;
        DocstoreLocalClient docstoreLocalClient = new DocstoreLocalClient();
        try {
            items = docstoreLocalClient.retrieveItems(itemUUIDs);
            //items = docstoreLocalClient.retrieveItems(itemUUIDs);
        } catch (Exception e) {
            StringBuffer itemUUIDsBuffer = new StringBuffer();
            for (String itemUUID : itemUUIDs) {
                itemUUIDsBuffer.append(itemUUID + ",");
            }
            LOG.info("Exception occured while retrieving the item for updating the item status to the following itemIds : " + itemUUIDsBuffer.toString());
            LOG.error(e, e);
        }

        BibTrees bibTrees = new BibTrees();
        for (org.kuali.ole.docstore.common.document.Item item : items) {
            Item oleItem = (Item) item.getContentObject();
            String itemXml = null;
            try {
                itemXml = getLoanProcessor().buildItemContentWithItemStatus(oleItem, OLEConstants.ITEM_STATUS_LOST);
            } catch (Exception e) {
                LOG.info("Exception occured while updating the item status for the item id : " + item.getId() + "and barcode : " + item.getBarcode());
                LOG.error(e, e);
            }
            if (itemXml != null) {
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
            docstoreLocalClient.processBibTrees(bibTrees);
        } catch (Exception e) {
            LOG.error(e, e);
            StringBuffer itemUUIDsBuffer = new StringBuffer();
            for (String itemUUID : itemUUIDs) {
                itemUUIDsBuffer.append(itemUUID + ",");
            }
            LOG.info("Exception occured while updating item status to the following itemIds : " + itemUUIDsBuffer.toString());
        }

    }

    public Map<String, List<String>> getItemStatusBasedOnRequestTypeMap() {
        String recallDeliveryRequestStatus = getLoanProcessor().getParameter(OLEConstants.RECALL_DELIVERY_ITEM_STATUS);
        String recallHoldRequestStatus = getLoanProcessor().getParameter(OLEConstants.RECALL_HOLD_ITEM_STATUS);
        String holdDeliveryRequestStatus = getLoanProcessor().getParameter(OLEConstants.HOLD_DELIVERY_ITEM_STATUS);
        String holdHoldRequestStatus = getLoanProcessor().getParameter(OLEConstants.HOLD_HOLD_ITEM_STATUS);
        String pageDeliveryRequestStatus = getLoanProcessor().getParameter(OLEConstants.PAGE_DELIVERY_ITEM_STATUS);
        String pageHoldRequestStatus = getLoanProcessor().getParameter(OLEConstants.PAGE_HOLD_ITEM_STATUS);
        String copyRequestStatus = getLoanProcessor().getParameter(OLEConstants.COPY_REQUEST_ITEM_STATUS);

        String asrRequestStatus = getLoanProcessor().getParameter(OLEConstants.ASR_REQUEST_ITEM_STATUS);

        Set<String> recallSet = new HashSet<>();
        recallSet.addAll(convertArrayToList(recallDeliveryRequestStatus.split(";")));
        recallSet.addAll(convertArrayToList(recallHoldRequestStatus.split(";")));

        Set<String> holdSet = new HashSet<>();
        holdSet.addAll(convertArrayToList(holdDeliveryRequestStatus.split(";")));
        holdSet.addAll(convertArrayToList(holdHoldRequestStatus.split(";")));

        Set<String> pageSet = new HashSet<>();
        pageSet.addAll(convertArrayToList(pageDeliveryRequestStatus.split(";")));
        pageSet.addAll(convertArrayToList(pageHoldRequestStatus.split(";")));

        Map<String, List<String>> itemStatusByRequestTypeMap = new HashMap<String, List<String>>();
        itemStatusByRequestTypeMap.put("recall", new ArrayList<>(recallSet));
        itemStatusByRequestTypeMap.put("hold", new ArrayList<>(holdSet));
        itemStatusByRequestTypeMap.put("page", new ArrayList<>(pageSet));
        itemStatusByRequestTypeMap.put("copy", convertArrayToList(copyRequestStatus.split(";")));
        itemStatusByRequestTypeMap.put("asr",convertArrayToList(asrRequestStatus.split(";")));
        return itemStatusByRequestTypeMap;
    }


    public boolean deliverAddressExist(String patronId) throws Exception {
        boolean found = false;
        Map<String, String> criteria = new HashMap<String, String>();
        Map<String, String> addressCriteria = new HashMap<String, String>();
        criteria.put("olePatronId", patronId);
        List<OlePatronDocument> olePatronDocument = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, criteria);
        if (olePatronDocument != null && olePatronDocument.size() > 0) {
            List<OleAddressBo> oleAddressBos = olePatronDocument.get(0).getOleAddresses();
            if (oleAddressBos != null && oleAddressBos.size() > 0) {
                for (int address = 0; address < oleAddressBos.size(); address++) {
                    if (oleAddressBos != null && oleAddressBos.size() > 0 && oleAddressBos.get(address).isDeliverAddress()) {
                        found = true;
                    }
                }
            }

        }
        return found;
    }


    public List getPatronDocumentByPatronBarcode(String patronBarcode) throws Exception {
        Map barMap = new HashMap();
        barMap.put(OLEConstants.OlePatron.BARCODE, patronBarcode);
        List<OlePatronDocument> matching = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, barMap);
        return matching;
    }

    // public void processRequestInformation

    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        if (parameter == null) {
            parameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, name);
            parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        }
        return parameter != null ? parameter.getValue() : null;
    }

    private DataCarrierService dataCarrierService;

    public DataCarrierService getDataCarrierService() {
        if (dataCarrierService == null) {
            dataCarrierService = (DataCarrierService)SpringContext.getService("dataCarrierService");
        }
        return dataCarrierService;
    }

    public OlePatronDocument validateGeneralChecks(OlePatronDocument olePatronDocument) throws Exception {

        boolean isAddressVerified = false;
        try {
            isAddressVerified = (boolean) getOleCirculationPolicyService().isAddressVerified(olePatronDocument, olePatronDocument.getOlePatronId());

        } catch (Exception e) {
            // LOG.error("Exception while checking address verified & blocks", e);
            throw e;
        }
        OlePatronRecordUtil olePatronRecordUtil = new OlePatronRecordUtil();
        DroolsResponse droolsResponse=  olePatronRecordUtil.fireRules(olePatronDocument,null);

        if (droolsResponse!=null && droolsResponse.getErrorMessage()!=null && droolsResponse.getErrorMessage().getErrorMessage()!=null && !droolsResponse.getErrorMessage().getErrorMessage().trim().isEmpty()) {
            olePatronDocument.setErrorMessage(droolsResponse.getErrorMessage().getErrorMessage());
        }
        getDataCarrierService().addData(OLEConstants.ERROR_ACTION, null);
        getDataCarrierService().addData(OLEConstants.ERRORS_AND_PERMISSION, null);
        return olePatronDocument;
    }

    public EngineResults getEngineResults(String agendaName, HashMap<String, Object> termValues) throws Exception {
        LOG.debug("Inside the getEngineResults method");
        EngineResults engineResult = null;
        try {
            Engine engine = KrmsApiServiceLocator.getEngine();
            ContextDefinition contextDefinition = KrmsRepositoryServiceLocator.getContextBoService().getContextByNameAndNamespace("OLE-CONTEXT", "OLE");
            AgendaDefinition agendaDefinition = KrmsRepositoryServiceLocator.getAgendaBoService().getAgendaByNameAndContextId(agendaName, contextDefinition.getId());
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(OLEConstants.AGENDA_NAME, agendaDefinition.getName());
            List<MatchBo> matchBos = (List<MatchBo>) getBusinessObjectService().findMatching(MatchBo.class, map);

            SelectionCriteria selectionCriteria =
                    SelectionCriteria.createCriteria(null, getSelectionContext(contextDefinition.getName()), getAgendaContext(agendaName));
            ExecutionOptions executionOptions = new ExecutionOptions();
            executionOptions.setFlag(ExecutionFlag.LOG_EXECUTION, true);

            Facts.Builder factBuilder = Facts.Builder.create();

            for (Iterator<MatchBo> matchBoIterator = matchBos.iterator(); matchBoIterator.hasNext(); ) {
                MatchBo matchBo = matchBoIterator.next();
                factBuilder.addFact(matchBo.getTermName(), termValues.get((matchBo.getTermName())));
            }
            engineResult = engine.execute(selectionCriteria, factBuilder.build(), executionOptions);
        } catch (Exception krmsException) {
            LOG.error("-----------KRMS EXCEPTION------------------", krmsException);

            throw new RuntimeException(krmsException);
        }
        return engineResult;
    }


    public List<OleDeliverRequestBo> getRequestByItem(String itemBarcode) {
        List<OleDeliverRequestBo> oleDeliverRequestBoList = new ArrayList<OleDeliverRequestBo>();
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("itemId", itemBarcode);
        oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        return oleDeliverRequestBoList;
    }

    public List<String> convertArrayToList(String[] array) {
        List<String> returnArrayList = new ArrayList<>();
        if (array != null && array.length > 0) {
            for (String value : array) {
                if (StringUtils.isNotBlank(value))
                    returnArrayList.add(value.trim());
            }
        }
        return returnArrayList;
    }

    public boolean setItemInformations(OleDeliverRequestBo oleDeliverRequestBo) {
        ASRHelperServiceImpl asrHelperService = new ASRHelperServiceImpl();
        LOG.info("Inside isItemAvailableInDocStore");
        boolean available = false;
        Map<String, String> itemMap = new HashMap<String, String>();
        LocationValuesBuilder locationValuesBuilder = new LocationValuesBuilder();
        String holdingsId = "";
        String bibTitle="";
        String bibAuthor="";
        try {
            try {
                org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
                SearchResponse searchResponse = null;
                search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, oleDeliverRequestBo.getItemId()), ""));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "id"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "holdingsIdentifier"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "Title_display"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "Author_display"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "TemporaryItemTypeFullValue_search"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "ItemTypeFullValue_display"));
                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);
                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        String fieldName = searchResultField.getFieldName();
                        String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";
                        if (fieldName.equalsIgnoreCase("holdingsIdentifier") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode())) {
                            holdingsId = fieldValue;
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("Title_display") &&!fieldValue.isEmpty()) {
                            bibTitle = searchResultField.getFieldValue();
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("Author_display") &&!fieldValue.isEmpty()) {
                            bibAuthor = searchResultField.getFieldValue();
                        } else  if (searchResultField.getFieldName().equalsIgnoreCase("id") &&!fieldValue.isEmpty()){
                            oleDeliverRequestBo.setItemUuid(fieldValue);
                        }else if (searchResultField.getFieldName().equalsIgnoreCase("TemporaryItemTypeFullValue_search")) {
                            oleDeliverRequestBo.setItemTypeName(searchResultField.getFieldValue());
                            oleDeliverRequestBo.setItemTypeDesc(getItemTypeDescByName(oleDeliverRequestBo.getItemTypeName()));
                        } else if (searchResultField.getFieldName().equalsIgnoreCase("ItemTypeFullValue_display") &&
                                (oleDeliverRequestBo.getItemTypeName() == null || oleDeliverRequestBo.getItemTypeName().isEmpty())) {
                            oleDeliverRequestBo.setItemTypeName(searchResultField.getFieldValue());
                            oleDeliverRequestBo.setItemTypeDesc(getItemTypeDescByName(oleDeliverRequestBo.getItemTypeName()));
                        }
                    }
                }
            } catch (Exception ex) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "Item Exists");
                LOG.error(OLEConstants.ITEM_EXIST + ex);
            }
            OleItemSearch itemSearchList = getDocstoreUtil().getOleItemSearchList(oleDeliverRequestBo.getItemUuid());
            if (asrHelperService.isAnASRItem(itemSearchList.getShelvingLocation())) {
                oleDeliverRequestBo.setAsrFlag(true);
            } else {
                oleDeliverRequestBo.setAsrFlag(false);
            }
            if (itemSearchList != null) {
                oleDeliverRequestBo.setTitle(itemSearchList.getTitle());
                oleDeliverRequestBo.setAuthor(itemSearchList.getAuthor());
                oleDeliverRequestBo.setCallNumber(itemSearchList.getCallNumber());
                oleDeliverRequestBo.setItemType(itemSearchList.getItemType());
                oleDeliverRequestBo.setItemLocation(itemSearchList.getShelvingLocation());
            }
            if(StringUtils.isNotEmpty(bibTitle)){
                oleDeliverRequestBo.setTitle(bibTitle);
            }
            if(StringUtils.isNotEmpty(bibAuthor)){
                oleDeliverRequestBo.setAuthor(bibAuthor);
            }
            LoanProcessor loanProcessor = new LoanProcessor();
            String itemXml = loanProcessor.getItemXML(oleDeliverRequestBo.getItemUuid());
            Item oleItem = loanProcessor.getItemPojo(itemXml);
            oleDeliverRequestBo.setOleItem(oleItem);
            oleDeliverRequestBo.setCopyNumber(oleItem.getCopyNumber());
            oleDeliverRequestBo.setEnumeration(oleItem.getEnumeration());
            oleDeliverRequestBo.setChronology(oleItem.getChronology());
            oleDeliverRequestBo.setItemStatus(oleItem.getItemStatus().getCodeValue());
            oleDeliverRequestBo.setClaimsReturnedFlag(oleItem.isClaimsReturnedFlag());
            locationValuesBuilder.getLocation(oleItem, oleDeliverRequestBo, holdingsId);
            available = true;
        } catch (Exception e) {
            LOG.error(ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.INVAL_LOC) + e);
        }
        return available;
    }


    private Map<String, List<OLEDeliverNotice>> buildMapofNoticeForEachPatron(List<OLEDeliverNotice> oleDeliverNotices) {
        Map<String, List<OLEDeliverNotice>> map = new HashMap<>();
        String patronId;
        for (Iterator<OLEDeliverNotice> iterator = oleDeliverNotices.iterator(); iterator.hasNext(); ) {
            OLEDeliverNotice oleliverNotice = iterator.next();
            patronId = oleliverNotice.getPatronId();
            if (map.containsKey(patronId)) {
                map.get(patronId).add(oleliverNotice);
            } else {
                List<OLEDeliverNotice> oleLoanDocumentList = new ArrayList<>();
                oleLoanDocumentList.add(oleliverNotice);
                map.put(patronId, oleLoanDocumentList);
            }
        }

        return map;
    }

    private Map<String, Map<String, List<OLEDeliverNotice>>> buildMapofNoticesForEachPatronAndConfigName(List<OLEDeliverNotice> oleDeliverNotices) {
        Map<String, Map<String, List<OLEDeliverNotice>>> map = new HashMap<>();
        String patronId;
        for (Iterator<OLEDeliverNotice> iterator = oleDeliverNotices.iterator(); iterator.hasNext(); ) {
            OLEDeliverNotice oleDeliverNotice = iterator.next();
            String noticeContentConfigurationName = oleDeliverNotice.getNoticeContentConfigName();
            patronId = oleDeliverNotice.getPatronId();
            if (map.containsKey(patronId)) {
                Map<String, List<OLEDeliverNotice>> configMap = map.get(patronId);
                if (configMap.containsKey(noticeContentConfigurationName)) {
                    configMap.get(noticeContentConfigurationName).add(oleDeliverNotice);
                } else {
                    List<OLEDeliverNotice> oleDeliverNoticeList = new ArrayList<>();
                    oleDeliverNoticeList.add(oleDeliverNotice);
                    configMap.put(noticeContentConfigurationName, oleDeliverNoticeList);
                }
            } else {
                List<OLEDeliverNotice> oleDeliverNoticeList = new ArrayList<>();
                oleDeliverNoticeList.add(oleDeliverNotice);
                Map configMap = new HashMap();
                configMap.put(noticeContentConfigurationName, oleDeliverNoticeList);
                map.put(patronId, configMap);
            }
        }
        return map;
    }

    protected SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE + " " + RiceConstants.SIMPLE_DATE_FORMAT_FOR_TIME);
    }


    /**
     * This method is used to fire the place request rules while placing the request on an item
     * @param oleDeliverRequestBo
     * @param backGroundLoan
     * @param override
     * @return
     */
    public String fireRules(OleDeliverRequestBo oleDeliverRequestBo, boolean backGroundLoan, boolean override) {
        processRequestToExecuteDrools(oleDeliverRequestBo);
        DroolsResponse droolsResponse = new DroolsResponse();
        NoticeInfo noticeInfo = new NoticeInfo();
        buildFactsAndExecuteRules(droolsResponse,noticeInfo,oleDeliverRequestBo);
        processRequestAfterRuleEvaluation(oleDeliverRequestBo, droolsResponse, noticeInfo,backGroundLoan, override);
        return oleDeliverRequestBo.getMessage();
    }


    /**
     *
     * @param oleDeliverRequestBo
     * @param droolsResponse
     * @param noticeInfo
     * @param backGroundLoan
     * @param override
     * @return
     */
    private OleDeliverRequestBo processRequestAfterRuleEvaluation(OleDeliverRequestBo oleDeliverRequestBo,DroolsResponse droolsResponse,NoticeInfo noticeInfo,boolean backGroundLoan,boolean override){
        OleLoanDocument oleLoanDocument =null;
        if (droolsResponse.isRuleMatched() ) {
                droolsResponse.setErrorMessage(null);
                OleDroolsHoldResponseBo oleDroolsHoldResponseBo = generateOleDroolsHoldResponseBo(droolsResponse, null);
                oleDeliverRequestBo.setOleDroolsHoldResponseBo(oleDroolsHoldResponseBo);
                if (oleDeliverRequestBo.getRequestExpiryDate() == null) {
                    Timestamp requestExpirationDate = calculateXDatesBasedOnCalendar(getCalendarGroup(oleDeliverRequestBo.getItemLocation()), String.valueOf(oleDroolsHoldResponseBo.getRequestExpirationDay()), null, true);
                    oleDeliverRequestBo.setRequestExpiryDate(new java.sql.Date(requestExpirationDate.getTime()));
                }
                String recallDeliveryRequestTypeId = null;
                String recallHoldRequestTypeId = null;
                Map<String, String> requestTypeMap = new HashMap<String, String>();
                requestTypeMap.put("requestTypeCode", OLEConstants.RECALL_DELIVERY_REQUEST);
                List<OleDeliverRequestType> oleDeliverRequestTypes = (List<OleDeliverRequestType>) getBusinessObjectService().findMatching(OleDeliverRequestType.class, requestTypeMap);
                if (oleDeliverRequestTypes.size() > 0) {
                    recallDeliveryRequestTypeId = oleDeliverRequestTypes.get(0).getRequestTypeId();
                }
                requestTypeMap.clear();
                requestTypeMap.put("requestTypeCode", OLEConstants.RECALL_HOLD_REQUEST);
                oleDeliverRequestTypes = (List<OleDeliverRequestType>) getBusinessObjectService().findMatching(OleDeliverRequestType.class, requestTypeMap);
                if (oleDeliverRequestTypes.size() > 0) {
                    recallHoldRequestTypeId = oleDeliverRequestTypes.get(0).getRequestTypeId();
                }

                if (oleDeliverRequestBo.getRequestTypeId() != null && ((oleDeliverRequestBo.getRequestTypeId().equals(recallDeliveryRequestTypeId) || oleDeliverRequestBo.getRequestTypeId().equals(recallHoldRequestTypeId)) && (!isRecallRequestExist(recallDeliveryRequestTypeId, oleDeliverRequestBo.getItemId()) && !isRecallRequestExist(recallHoldRequestTypeId, oleDeliverRequestBo.getItemId())) || backGroundLoan)) {
                    oleLoanDocument = updateLoanDocument(oleDeliverRequestBo, noticeInfo, oleDroolsHoldResponseBo.getMinimumLoanPeriod(), oleDroolsHoldResponseBo.getRecallLoanPeriod());
                }
                if (oleLoanDocument != null) {
                    oleDeliverRequestBo.setLoanTransactionRecordNumber(oleLoanDocument.getLoanId());
                    if (oleLoanDocument.getOleRequestId() == null) {
                        oleLoanDocument.setOleRequestId(oleDeliverRequestBo.getRequestId());
                    }
                    getBusinessObjectService().save(oleLoanDocument);
                }
                oleDeliverRequestBo.setRecallNoticeContentConfigName(oleDroolsHoldResponseBo.getRecallNoticeContentConfigName());
                oleDeliverRequestBo.setRequestExpirationNoticeContentConfigName(oleDroolsHoldResponseBo.getRequestExpirationNoticeContentConfigName());
                oleDeliverRequestBo.setOnHoldNoticeContentConfigName(oleDroolsHoldResponseBo.getOnHoldNoticeContentConfigName());
                oleDeliverRequestBo.setOnHoldExpirationNoticeContentConfigName(oleDroolsHoldResponseBo.getOnHoldExpirationNoticeContentConfigName());
                oleDeliverRequestBo.setOnHoldCourtesyNoticeContentConfigName(oleDroolsHoldResponseBo.getOnHoldCourtesyNoticeContentConfigName());
                oleDeliverRequestBo.setOleDroolsHoldResponseBo(null);
                oleDeliverRequestBo.setMessage(droolsResponse.getErrorMessage().getErrorMessage());
        }else{
            oleDeliverRequestBo.setMessage(OLEConstants.NO_RULE_FOUND);
        }
        return oleDeliverRequestBo;
    }

    private void buildFactsAndExecuteRules(DroolsResponse droolsResponse,NoticeInfo noticeInfo,OleDeliverRequestBo oleDeliverRequestBo){
        ArrayList<Object> facts = new ArrayList<Object>();
        facts.add(noticeInfo);
        facts.add(droolsResponse);
        facts.add(oleDeliverRequestBo);
        facts.add(oleDeliverRequestBo.getOlePatron());
        new CircUtilController().fireRules(facts, null, "place-request");
    }


    /**
     * This method is used to calculate the dates based on the calendar
     * @param groupId
     * @param days
     * @param timeToCalculateFrom
     * @param daysAlone
     * @return
     */
    public Timestamp calculateXDatesBasedOnCalendar(String groupId, String days, Timestamp timeToCalculateFrom, boolean daysAlone) {
        OleCalendarService oleCalendarService = new OleCalendarServiceImpl();
        if (timeToCalculateFrom == null) {
            timeToCalculateFrom = new Timestamp(System.currentTimeMillis());
        }
        if (daysAlone) {
            return oleCalendarService.calculateDueDate(groupId, String.valueOf(days), timeToCalculateFrom);
        } else {
            return oleCalendarService.calculateDueDateHrs(groupId, String.valueOf(days), new Timestamp(System.currentTimeMillis()));
        }
    }


    /**
     * This method is used to get the calendar group id based on the item location
     * @param itemLocation
     * @return
     */
    public String getCalendarGroup(String itemLocation) {
        String groupId = null;
        if (StringUtils.isNotBlank(itemLocation)) {
            OleCirculationDesk oleCirculationDesk = getCirculationDesk(itemLocation);
            if (oleCirculationDesk != null) {
                groupId = oleCirculationDesk.getCalendarGroupId();
            }
        }
        return groupId;
    }


    /**
     * This method is used to  get the circulation desk based on the item location
     * @param itemLocation
     * @return
     */
    private OleCirculationDesk getCirculationDesk(String itemLocation) {
        Map circulationDeskMap = new HashMap();
        OleCirculationDesk oleCirculationDesk = null;
        String[] locations = itemLocation.split("/");
        circulationDeskMap.put("location.locationCode", locations[locations.length - 1]);
        List<OleCirculationDeskLocation> oleCirculationDeskLocations = (List<OleCirculationDeskLocation>) getBusinessObjectService().findMatching(OleCirculationDeskLocation.class, circulationDeskMap);
        for (OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDeskLocations) {
            if (oleCirculationDeskLocation.getCirculationFullLocationCode().equals(itemLocation)) {
                oleCirculationDesk = oleCirculationDeskLocation.getOleCirculationDesk();
            }
        }
        return oleCirculationDesk;
    }


    /**
     * This method is used to set dats to the OleDroolsHoldResponseBo from DroolsResponse
     * @param droolsResponse
     * @param oleDroolsHoldResponseBo
     * @return
     */
    private OleDroolsHoldResponseBo generateOleDroolsHoldResponseBo(DroolsResponse droolsResponse, OleDroolsHoldResponseBo oleDroolsHoldResponseBo) {
        if (oleDroolsHoldResponseBo == null) {
            oleDroolsHoldResponseBo = new OleDroolsHoldResponseBo();
        }
        oleDroolsHoldResponseBo.setRuleMatched(droolsResponse.isRuleMatched());
        if (droolsResponse.getDroolsExchange().getFromContext("minimumLoanPeriod") != null) {
            oleDroolsHoldResponseBo.setMinimumLoanPeriod((String) droolsResponse.getDroolsExchange().getFromContext("minimumLoanPeriod"));
        }
        if (droolsResponse.getDroolsExchange().getFromContext("recallLoanPeriod") != null) {
            oleDroolsHoldResponseBo.setRecallLoanPeriod((String) droolsResponse.getDroolsExchange().getFromContext("recallLoanPeriod"));
        }
        if (droolsResponse.getDroolsExchange().getFromContext("requestExpirationDays") != null) {
            oleDroolsHoldResponseBo.setRequestExpirationDay((Integer) droolsResponse.getDroolsExchange().getFromContext("requestExpirationDays"));
        }
        if (droolsResponse.getDroolsExchange().getFromContext(OLEConstants.RECALL_NOTICE_CONTENT_CONFIG_NAME) != null) {
            oleDroolsHoldResponseBo.setRecallNoticeContentConfigName((String) droolsResponse.getDroolsExchange().getFromContext(OLEConstants.RECALL_NOTICE_CONTENT_CONFIG_NAME));
        }
        if (droolsResponse.getDroolsExchange().getFromContext(OLEConstants.REQUEST_EXPIRATION_NOTICE_CONTENT_CONFIG_NAME) != null) {
            oleDroolsHoldResponseBo.setRequestExpirationNoticeContentConfigName((String) droolsResponse.getDroolsExchange().getFromContext(OLEConstants.REQUEST_EXPIRATION_NOTICE_CONTENT_CONFIG_NAME));
        }
        if (droolsResponse.getDroolsExchange().getFromContext(OLEConstants.ON_HOLD_NOTICE_CONTENT_CONFIG_NAME) != null) {
            oleDroolsHoldResponseBo.setOnHoldNoticeContentConfigName((String) droolsResponse.getDroolsExchange().getFromContext(OLEConstants.ON_HOLD_NOTICE_CONTENT_CONFIG_NAME));
        }
        if (droolsResponse.getDroolsExchange().getFromContext(OLEConstants.ON_HOLD_EXPIRATION_NOTICE_CONTENT_CONFIG_NAME) != null) {
            oleDroolsHoldResponseBo.setOnHoldExpirationNoticeContentConfigName((String) droolsResponse.getDroolsExchange().getFromContext(OLEConstants.ON_HOLD_EXPIRATION_NOTICE_CONTENT_CONFIG_NAME));
        }
        if (droolsResponse.getDroolsExchange().getFromContext(OLEConstants.ON_HOLD_COURTESY_NOTICE_CONTENT_CONFIG_NAME) != null) {
            oleDroolsHoldResponseBo.setOnHoldCourtesyNoticeContentConfigName((String) droolsResponse.getDroolsExchange().getFromContext(OLEConstants.ON_HOLD_COURTESY_NOTICE_CONTENT_CONFIG_NAME));
        }
        return oleDroolsHoldResponseBo;
    }

    /**
     * This method is used to get the loan document based on the item id
     * @param itemId
     * @return
     */
    private OleLoanDocument getLoanDocument(String itemId) {
        Timestamp dueDate = null;
        OleLoanDocument oleLoanDocument = null;
        Map<String, String> loanMap = new HashMap<String, String>();
        loanMap.put("itemId", itemId);
        List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) getBusinessObjectService().findMatching(OleLoanDocument.class, loanMap);
        if (oleLoanDocumentList.size() > 0) {
            oleLoanDocument = oleLoanDocumentList.get(0);
        }
        return oleLoanDocument;
    }


    /**
     * This method will update the loan document with altered due date and also update the notice table
     * @param oleDeliverRequestBo
     * @param minimumLoanPeriod
     * @param recallLoanPeriod
     */
    private OleLoanDocument updateLoanDocument(OleDeliverRequestBo oleDeliverRequestBo, NoticeInfo noticeInfo,String minimumLoanPeriod, String recallLoanPeriod) {
        Timestamp dueDate = null;
        OleLoanDocument oleLoanDocument = getLoanDocument(oleDeliverRequestBo.getItemId());
        if (oleLoanDocument != null && (oleDeliverRequestBo.getRequestTypeId().equals("1") || oleDeliverRequestBo.getRequestTypeId().equals("2"))) {
            if (oleLoanDocument.getCirculationLocationId() != null) {
                    Map<String, String> circDeskCriteriaMap = new HashMap<String, String>();
                    circDeskCriteriaMap.put("circulationDeskId", oleLoanDocument.getCirculationLocationId());
                    List<OleCirculationDesk> oleCirculationDesks = (List<OleCirculationDesk>) getBusinessObjectService().findMatching(OleCirculationDesk.class, circDeskCriteriaMap);
                    OleCirculationDesk oleCirculationDesk = null;
                    if (oleCirculationDesks.size() > 0) {
                        oleCirculationDesk = oleCirculationDesks.get(0);
                    }
                    if (oleCirculationDesk != null && oleCirculationDesk.getCalendarGroupId() != null) {
                        LoanDateTimeUtil loanDateTimeUtil = new LoanDateTimeUtil();
                        Date minimumLoanPeriodDate = loanDateTimeUtil.calculateDateTimeByPeriod(minimumLoanPeriod, oleCirculationDesk);
                        Date recallLoanPeriodDate = loanDateTimeUtil.calculateDateTimeByPeriod(recallLoanPeriod, oleCirculationDesk);
                        dueDate = getDueDate(new Timestamp(minimumLoanPeriodDate.getTime()), new Timestamp(recallLoanPeriodDate.getTime()), oleLoanDocument.getCreateDate(), minimumLoanPeriod, oleCirculationDesk);
                        oleLoanDocument.setPastDueDate(oleLoanDocument.getLoanDueDate());
                        oleLoanDocument.setLoanDueDate(dueDate);
                        if (oleLoanDocument.getPastDueDate() != null) {
                            oleDeliverRequestBo.setOriginalDueDate(new java.sql.Date(oleLoanDocument.getPastDueDate().getTime()));
                        }
                        oleDeliverRequestBo.setRecallDueDate(new Timestamp(oleLoanDocument.getLoanDueDate().getTime()));
                        oleDeliverRequestBo.setNewDueDate(new java.sql.Date(oleLoanDocument.getLoanDueDate().getTime()));
                        oleDeliverRequestBo.setNoticeType(noticeInfo.getNoticeType());
                        List<OLEDeliverNotice> deliverNotices = getDeliverNotices(oleDeliverRequestBo, oleLoanDocument,(noticeInfo!=null)?noticeInfo:null);

                        oleLoanDocument.setDeliverNotices(deliverNotices);
                        persistDeliverNotices(deliverNotices, oleLoanDocument.getLoanId());
                        Item oleItem = oleDeliverRequestBo.getOleItem();
                        oleItem.setDueDateTime(getLoanProcessor().convertDateToString(oleLoanDocument.getLoanDueDate(), "MM/dd/yyyy HH:mm:ss"));
                        try {
                            updateItem(oleItem);
                        } catch (Exception e) {
                            if (LOG.isInfoEnabled()) {
                                LOG.info("Exception occured while updating the item . " + e.getMessage());
                            }
                        }

                }
            }

        }
        return oleLoanDocument;

    }


    /**
     * This method is used to get the due date based on the arguments passed to it
     * @param minimumLoanPeriodDate
     * @param recallLoanPeriodDate
     * @param createdDate
     * @param minimumLoanDays
     * @param oleCirculationDesk
     * @return
     */
    private Timestamp getDueDate(Timestamp minimumLoanPeriodDate, Timestamp recallLoanPeriodDate, Date createdDate, String minimumLoanDays, OleCirculationDesk oleCirculationDesk) {
        Timestamp dueDate = null;
        Date minimumLoanDateByLoanedDate = null;
        if (minimumLoanPeriodDate.compareTo(recallLoanPeriodDate) <= 0) {
            dueDate = recallLoanPeriodDate;
        } else {
            LoanDateTimeUtil loanDateTimeUtil = new LoanDateTimeUtil();
            loanDateTimeUtil.setTimeToCalculateFrom(createdDate);
            minimumLoanDateByLoanedDate = loanDateTimeUtil.calculateDateTimeByPeriod(minimumLoanDays, oleCirculationDesk);
            if (recallLoanPeriodDate.compareTo(minimumLoanDateByLoanedDate) >= 0) {
                dueDate = recallLoanPeriodDate;
            } else {
                dueDate = minimumLoanPeriodDate;
            }
        }
        return dueDate;

    }

    /**
     * This method is used to get the item record based on the item barcode
     * @param itemBarcode
     * @return
     */
    public ItemRecord getItemRecordByBarcode(String itemBarcode) {
        ItemRecord itemRecord = null;
        HashMap<String, String> criteriaMap = new HashMap<>();
        criteriaMap.put("barCode", itemBarcode);
        List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class,
                criteriaMap);
        if (null != itemRecords && !itemRecords.isEmpty()) {
            itemRecord = itemRecords.get(0);
        }

        return itemRecord;
    }

    /**
     * This method is used to generate the notices based on the new due dates
     * @param oleDeliverRequestBo
     * @param oleLoanDocument
     * @return
     */
    public List<OLEDeliverNotice> getDeliverNotices(OleDeliverRequestBo oleDeliverRequestBo,OleLoanDocument oleLoanDocument,NoticeInfo noticeInfo){
        ItemRecord itemRecord = getItemRecordByBarcode(oleDeliverRequestBo.getItemId());
        itemRecord.setDueDateTime(oleDeliverRequestBo.getRecallDueDate());
        List<OLEDeliverNotice> oleDeliverNotices = new CircUtilController().processNotices(oleLoanDocument,itemRecord,noticeInfo);
        return oleDeliverNotices;
    }

    public void persistDeliverNotices(List<OLEDeliverNotice> oleDeliverNotices,String loanId){
        Map<String,String> noticeMap = new HashMap<String,String>();
        noticeMap.put("loanId",loanId);
        getBusinessObjectService().deleteMatching(OLEDeliverNotice.class,noticeMap);
        getBusinessObjectService().save(oleDeliverNotices);
    }

    public boolean isValidASRItemStatus(String itemStatus){
        boolean valid = false;
        String parameterValue = getLoanProcessor().getParameter(ASRConstants.ASR_REQUEST_ITEM_STATUS);
        String[] itemStatuses ;
        if(itemStatus!=null){
            itemStatuses = parameterValue.split("[';']");
            if(itemStatuses!=null && itemStatuses.length>0){
                for(String itemStats: itemStatuses){
                    if(itemStatus.equals(itemStats)){
                        valid =true;
                        break;
                    }
                }
            }

        }

        return valid;
    }

    public OleDeliverRequestBo processRequestExpirationDate(OleDeliverRequestBo oleDeliverRequestBo){
        if(oleDeliverRequestBo.getRequestExpiryDate()==null){
            Map<String,String> locationMap = new HashMap<String,String>();
            locationMap.put("locationCode",oleDeliverRequestBo.getShelvingLocation());
            List<OleLocation> oleLocationBos = (List<OleLocation>)KRADServiceLocator.getBusinessObjectService().findMatching(OleLocation.class,locationMap);
            if(oleLocationBos!=null && oleLocationBos.size()>0){
                Map<String,String> circulationDeskLocationMap = new HashMap<String,String>();
                circulationDeskLocationMap.put("circulationDeskLocation",oleLocationBos.get(0).getLocationId());
                List<OleCirculationDeskLocation> oleCirculationDeskLocationList = (List<OleCirculationDeskLocation>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDeskLocation.class,circulationDeskLocationMap);
                if(oleCirculationDeskLocationList!=null && oleCirculationDeskLocationList.size()>0){
                    for(OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDeskLocationList){
                        if(oleCirculationDeskLocation.getCirculationPickUpDeskLocation()==null || (oleCirculationDeskLocation.getCirculationPickUpDeskLocation() !=null && oleCirculationDeskLocation.getCirculationPickUpDeskLocation().trim().isEmpty())) {
                            String requestExpirationDays= oleCirculationDeskLocation.getOleCirculationDesk().getRequestExpirationDays();
                            if(requestExpirationDays!=null && !requestExpirationDays.equalsIgnoreCase("0")){
                                oleDeliverRequestBo.setRequestExpiryDate(addDate(new java.sql.Date(System.currentTimeMillis()), Integer.parseInt(requestExpirationDays)));
                            }
                        }
                    }
                }
            }
        }
        return oleDeliverRequestBo;
    }


    public String sendMailsToPatron(String emailAddress, String noticeContent, String itemLocation) {
        String fromAddress = getCircDeskLocationResolver().getReplyToEmail(itemLocation);

        if (fromAddress == null) {
            fromAddress = getParameterResolverInstance().getParameter(OLEConstants.APPL_ID, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEParameterConstants
                    .NOTICE_FROM_MAIL);
        }
        try {
            if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                fromAddress = OLEConstants.KUALI_MAIL;
            }
            if (emailAddress != null && !emailAddress.isEmpty()) {
                noticeContent = noticeContent.replace('[', ' ');
                noticeContent = noticeContent.replace(']', ' ');
                if (!noticeContent.trim().equals("")) {
                    OleMailer oleMailer = getOleMailer();
                    oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(emailAddress), new EmailSubject(OLEConstants.NOTICE_MAIL), new EmailBody(noticeContent), true);
                }
            } else {
            }
        } catch (Exception e) {
        }

        return noticeContent;
    }


    public void cancelRequests(List<OleDeliverRequestBo> oleDeliverRequestBos){
        if(oleDeliverRequestBos!=null && oleDeliverRequestBos.size()>0){
            for(OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBos){
                cancelDocument(oleDeliverRequestBo);
            }
        }
    }

    public Map buildMapForIndexToSolr(String noticeType, String noticeContent, List<OleDeliverRequestBo> oleDeliverRequestBos) {
        Map parameterMap = new HashMap();
        parameterMap.put("DocType", noticeType);
        parameterMap.put("DocFormat", "Email");
        parameterMap.put("noticeType", noticeType);
        parameterMap.put("noticeContent", noticeContent);
        String patronBarcode = oleDeliverRequestBos.get(0).getOlePatron().getBarcode();
        String patronId = oleDeliverRequestBos.get(0).getOlePatron().getOlePatronId();
        parameterMap.put("patronBarcode", patronBarcode);
        Date dateSent = new Date();
        parameterMap.put("dateSent", dateSent);
        parameterMap.put("uniqueId", patronId+ dateSent.getTime());
        List<String> itemBarcodes = new ArrayList<>();
        for (Iterator<OleDeliverRequestBo> iterator = oleDeliverRequestBos.iterator(); iterator.hasNext(); ) {
            OleDeliverRequestBo oleDeliverRequestBo = iterator.next();
            String itemBarcode = oleDeliverRequestBo.getItemId();
            itemBarcodes.add(itemBarcode);
        }
        parameterMap.put("itemBarcodes",itemBarcodes);
        return parameterMap;
    }

    public OleLoanDocument getLoanDocumentWithItemInfo(OleLoanDocument oleLoanDocument, boolean updateClaimsCount) throws Exception {
        Long startTime = System.currentTimeMillis();
        List<OleLoanDocument> loanDocumentsWithItemInfo = new ArrayList<OleLoanDocument>();
        if (oleLoanDocument != null) {
            Map<String, OleLoanDocument> loanDocumentMap = new HashMap<String, OleLoanDocument>();
            SearchParams searchParams = new SearchParams();
            List<SearchCondition> searchConditions = new ArrayList<>();
            SearchResponse searchResponse = new SearchResponse();
            if (numberOfRecords == null) {
                numberOfRecords = getLoanProcessor().getParameter(OLEConstants.NUMBER_OF_ITEM_INFO);
            }
            try {
                searchConditions = new ArrayList<>();
                searchParams = new SearchParams();
                loanDocumentMap.put(oleLoanDocument.getItemUuid(), oleLoanDocument);
                searchConditions.add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField("item", "id", oleLoanDocument.getItemUuid()), "OR"));
                searchParams.setPageSize(Integer.parseInt(OLEConstants.MAX_PAGE_SIZE_FOR_LOAN));
                buildSearchParams(searchParams);
                searchParams.getSearchConditions().addAll(searchConditions);
                try {
                    searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                    if(searchResponse.getSearchResults().size()==0){
                        ItemRecord itemRecord = ItemInfoUtil.getInstance().getItemRecordByBarcode(oleLoanDocument.getItemId());
                        Map<String, String> criteriaMap = new HashMap();
                        criteriaMap.put("holdingsId", itemRecord.getHoldingsId());
                        List<HoldingsRecord> holdingsRecords = (List<HoldingsRecord>) getBusinessObjectService().findMatching(HoldingsRecord
                                        .class,
                                criteriaMap);
                        BibMarcIndexer bibMarcIndexer = new BibMarcIndexer();
                        DocstoreRDBMSStorageService rdbmsStorageService = new DocstoreRDBMSStorageService();
                        bibMarcIndexer.createTree(rdbmsStorageService.retrieveBibTree(holdingsRecords.get(0).getBibId()));
                        searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                    }
                } catch (Exception e) {
                    LOG.error(e, e);
                    throw new Exception("Exception occured while fetching data from solr");
                }
                try {
                    List<OleLoanDocument> processedLoanDocuments = buildSearchResultsFields(searchResponse, loanDocumentMap);
                    loanDocumentsWithItemInfo.addAll(processedLoanDocuments);
                } catch (Exception e) {
                    LOG.error(e, e);
                    throw new Exception("Exception occured while setting the item information to loan document");
                }
            } catch (Exception e) {
                LOG.info("Exception occured while setting the item information to the loan document");
                LOG.error(e, e);
            }
        }
        // }
        Long endTime = System.currentTimeMillis();
        Long timeDifference = endTime - startTime;
        LOG.info("Time Taken to set the item information in the loan records in milliseconds : " + timeDifference);
        return loanDocumentsWithItemInfo.get(0);
    }

    public void deleteLoanNoticeHistoryRecord() throws Exception {

        List<OLEDeliverNoticeHistory> loanHistoryDocuments = new ArrayList<OLEDeliverNoticeHistory>();
        SimpleDateFormat sdf = PurApDateFormatUtils.getSimpleDateFormat(DeliverConstants.KUALI_SIMPLE_DATE_FORMAT);

        String loanNoticeHistoryDateString = getParameterResolverInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, DeliverConstants.DELETE_LOAN_NOTICE_HISTORY_TO_DATE);
        java.sql.Date loanNoticeHistoryDate = null;
        try {
            loanNoticeHistoryDate = org.kuali.ole.sys.util.KfsDateUtils.convertToSqlDate(sdf.parse(loanNoticeHistoryDateString));
        } catch (Exception e) {
            loanNoticeHistoryDate = null;
        }
        if (loanNoticeHistoryDate != null) {
            OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService("oleLoanDao");
            loanHistoryDocuments = oleLoanDocumentDaoOjb.getLaonHistoryRecords(loanNoticeHistoryDate);
            LOG.info("loanHistoryDocuments size >>>>>>>>>>>" + loanHistoryDocuments.size());
            getBusinessObjectService().delete(loanHistoryDocuments);
            LOG.info("deleteLoanNoticeHistoryRecord job completed. " + loanHistoryDocuments.size() +  "records deleted");
        } else {
            LOG.info("loanNoticeHistoryDate is empty. Provide value for DDELETE_LOAN_NOTICE_HISTORY_TO_DATE parameter");
        }
    }

    public void deleteRenewalHistoryRecord() throws Exception {

        List<OleRenewalHistory> renewalHistoryDocuments = new ArrayList<OleRenewalHistory>();
        SimpleDateFormat sdf = PurApDateFormatUtils.getSimpleDateFormat(DeliverConstants.KUALI_SIMPLE_DATE_FORMAT);

        String renewalHistoryDateString = getParameterResolverInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, DeliverConstants.DELETE_RENEWAL_HISTORY_TO_DATE);
        java.sql.Date renewalHistoryDate = null;
        try {
            renewalHistoryDate = org.kuali.ole.sys.util.KfsDateUtils.convertToSqlDate(sdf.parse(renewalHistoryDateString));
        } catch (Exception e) {
            renewalHistoryDate = null;
        }
        if (renewalHistoryDate != null) {
            OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService("oleLoanDao");
            renewalHistoryDocuments = oleLoanDocumentDaoOjb.getRenewalHistoryRecords(renewalHistoryDate);
            LOG.info("renewalHistoryDocuments size >>>>>>>>>>>" + renewalHistoryDocuments.size());
            getBusinessObjectService().delete(renewalHistoryDocuments);
            LOG.info("deleteRenewalHistoryRecord job completed. " + renewalHistoryDocuments.size() +  "records deleted");
        } else {
            LOG.info("renewalHistoryDate is empty. Provide value for DELETE_RENEWAL_HISTORY_TO_DATE parameter");
        }
    }

    public void deleteReturnHistoryRecord() throws Exception {

        List<OLEReturnHistoryRecord> returnHistoryDocuments = new ArrayList<OLEReturnHistoryRecord>();
        SimpleDateFormat sdf = PurApDateFormatUtils.getSimpleDateFormat(DeliverConstants.KUALI_SIMPLE_DATE_FORMAT);

        String renewalHistoryDateString = getParameterResolverInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, DeliverConstants.DELETE_RETURN_HISTORY_TO_DATE);
        java.sql.Date returnHistoryDate = null;
        try {
            returnHistoryDate = org.kuali.ole.sys.util.KfsDateUtils.convertToSqlDate(sdf.parse(renewalHistoryDateString));
        } catch (Exception e) {
            returnHistoryDate = null;
        }
        if (returnHistoryDate != null) {
            OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService("oleLoanDao");
            returnHistoryDocuments = oleLoanDocumentDaoOjb.getReturnHistoryRecords(returnHistoryDate);
            LOG.info("returnHistoryDocuments size >>>>>>>>>>>" + returnHistoryDocuments.size());
            getBusinessObjectService().delete(returnHistoryDocuments);
            LOG.info("deleteReturnHistoryRecord job completed. " + returnHistoryDocuments.size() + " records deleted");
        } else {
            LOG.info("returnHistoryRecord is empty. Provide value for DELETE_RETURN_HISTORY_TO_DATE parameter");
        }
    }

    public void deleteRequestHistoryRecord() throws Exception {

        List<OleDeliverRequestHistoryRecord> requestHistoryDocuments = new ArrayList<OleDeliverRequestHistoryRecord>();
        SimpleDateFormat sdf = PurApDateFormatUtils.getSimpleDateFormat(DeliverConstants.KUALI_SIMPLE_DATE_FORMAT);

        String requestHistoryDateString = getParameterResolverInstance().getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, DeliverConstants.DELETE_REQUEST_HISTORY_TO_DATE);
        java.sql.Date requestHistoryDate = null;
        try {
            requestHistoryDate = org.kuali.ole.sys.util.KfsDateUtils.convertToSqlDate(sdf.parse(requestHistoryDateString));
        } catch (Exception e) {
            requestHistoryDate = null;
        }
        if (requestHistoryDate != null) {
            OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getService("oleLoanDao");
            requestHistoryDocuments = oleLoanDocumentDaoOjb.getRequestHistoryRecords(requestHistoryDate);
            LOG.info("requestHistoryDocuments size >>>>>>>>>>>" + requestHistoryDocuments.size());
            getBusinessObjectService().delete(requestHistoryDocuments);
            LOG.info("deleteRequestHistoryRecord job completed. " + requestHistoryDocuments.size() + " records deleted");
        } else {
            LOG.info("deleteRequestHistoryRecord is empty. Provide value for DELETE_REQUEST_HISTORY_TO_DATE parameter");
        }
    }


                }






