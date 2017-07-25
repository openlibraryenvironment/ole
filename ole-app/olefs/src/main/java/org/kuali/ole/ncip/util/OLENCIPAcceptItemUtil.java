package org.kuali.ole.ncip.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.batch.OleDeliverBatchServiceImpl;
import org.kuali.ole.deliver.batch.OleMailer;
import org.kuali.ole.deliver.batch.OleNoticeBo;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.ole.utility.OleStopWatch;
import org.kuali.rice.core.api.mail.EmailBody;
import org.kuali.rice.core.api.mail.EmailFrom;
import org.kuali.rice.core.api.mail.EmailSubject;
import org.kuali.rice.core.api.mail.EmailTo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 8/17/15.
 */
public class OLENCIPAcceptItemUtil {

    private static final Logger LOG = Logger.getLogger(OLENCIPAcceptItemUtil.class);

    private BusinessObjectService businessObjectService;
    private CircDeskLocationResolver circDeskLocationResolver;
    private PersonService personService;
    private DocumentService documentService;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;
    private OlePatronHelperServiceImpl olePatronHelperService;
    private OLENCIPAcceptItemUtil olencipAcceptItemUtil;

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
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

    public PersonService getPersonService() {
        if (null == personService) {
            personService = KimApiServiceLocator.getPersonService();
        }
        return personService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
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

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (oleDeliverRequestDocumentHelperService == null) {
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }

    public void setOleDeliverRequestDocumentHelperService(OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService) {
        this.oleDeliverRequestDocumentHelperService = oleDeliverRequestDocumentHelperService;
    }

    public OlePatronHelperService getOlePatronHelperService() {
        if (olePatronHelperService == null)
            olePatronHelperService = new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    public void setOlePatronHelperService(OlePatronHelperServiceImpl olePatronHelperService) {
        this.olePatronHelperService = olePatronHelperService;
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

    public String validateRequestType(String requestType) {
        if (StringUtils.isNotBlank(requestType)) {
            Map<String, String> requestTypeMap = new HashMap<String, String>();
            requestTypeMap.put(OLEConstants.OleDeliverRequest.REQUEST_TYPE_CD, requestType);
            List<OleDeliverRequestType> oleDeliverRequestTypeList = (List<OleDeliverRequestType>) getBusinessObjectService().findMatching(OleDeliverRequestType.class, requestTypeMap);
            if (CollectionUtils.isNotEmpty(oleDeliverRequestTypeList)) {
                return oleDeliverRequestTypeList.get(0).getRequestTypeId();
            }
        }
        return null;
    }

    public boolean isValidItemType(String itemType) {
        Map<String, String> itemTypeMap = new HashMap<>();
        itemTypeMap.put(OLEConstants.OleDeliverRequest.ITEM_TYPE_CODE, itemType);
        List<OleInstanceItemType> itemTypeList = (List) getBusinessObjectService().findMatching(OleInstanceItemType.class, itemTypeMap);
        if (CollectionUtils.isNotEmpty(itemTypeList)) {
            return true;
        }
        return false;
    }

    public OleCirculationDesk validatePickUpLocation(String pickUpLocation, String location) {
        if (StringUtils.isNotBlank(pickUpLocation)) {
            Map circulationDeskMap = new HashMap();
            circulationDeskMap.put("oleCirculationDesk.circulationDeskCode", pickUpLocation);
            circulationDeskMap.put("circulationPickUpDeskLocation", location);
            List<OleCirculationDeskLocation> oleCirculationDeskLocations = (List<OleCirculationDeskLocation>) getBusinessObjectService().findMatching(OleCirculationDeskLocation.class, circulationDeskMap);
            if (CollectionUtils.isNotEmpty(oleCirculationDeskLocations)) {
                return oleCirculationDeskLocations.get(0).getOleCirculationDesk();
            }
        }
        return null;
    }

    public OleDeliverRequestBo getOleDeliverRequestBo(String itemType, String location, String requestTypeId, String requestType, OlePatronDocument olePatronDocument) {
        OleDeliverRequestBo oleDeliverRequestBo = new OleDeliverRequestBo();

        oleDeliverRequestBo.setItemType(itemType);

        Map<String, String> locationMap = getCircDeskLocationResolver().getLocationMap(location);
        oleDeliverRequestBo.setItemLibrary(locationMap.get(OLEConstants.ITEM_LIBRARY));
        oleDeliverRequestBo.setItemInstitution(locationMap.get(OLEConstants.ITEM_INSTITUTION));
        oleDeliverRequestBo.setItemCampus(locationMap.get(OLEConstants.ITEM_CAMPUS));
        oleDeliverRequestBo.setItemCollection(locationMap.get(OLEConstants.ITEM_COLLECTION));
        oleDeliverRequestBo.setShelvingLocation(locationMap.get(OLEConstants.ITEM_SHELVING));

        oleDeliverRequestBo.setRequestTypeCode(requestType);
        if (isHoldDelivery(requestTypeId)) {
            oleDeliverRequestBo.setRequestTypeId("4");
            oleDeliverRequestBo.setRequestTypeCode("Hold/Hold Request");
        } else if (isPageDelivery(requestTypeId)) {
            oleDeliverRequestBo.setRequestTypeId("6");
            oleDeliverRequestBo.setRequestTypeCode("Page/Hold Request");
        } else if (isRecallDelivery(requestTypeId)) {
            oleDeliverRequestBo.setRequestTypeId("2");
            oleDeliverRequestBo.setRequestTypeCode("Recall/Hold Request");
        } else {
            oleDeliverRequestBo.setRequestTypeId(requestTypeId);
        }

        oleDeliverRequestBo.setOlePatron(olePatronDocument);
        return oleDeliverRequestBo;
    }

    private boolean isRecallDelivery(String requestTypeId) {
        return requestTypeId.equals("1");
    }

    private boolean isPageDelivery(String requestTypeId) {
        return requestTypeId.equals("5");
    }

    private boolean isHoldDelivery(String requestTypeId) {
        return requestTypeId.equals("3");
    }

    public void savePickupNoticeHistory(String content, String olePatronId) {
        OLEDeliverNoticeHistory oleDeliverNoticeHistory = new OLEDeliverNoticeHistory();
        oleDeliverNoticeHistory.setNoticeType(OLEConstants.PICKUP_NOTICE);
        oleDeliverNoticeHistory.setNoticeSentDate(new Timestamp(new Date().getTime()));
        oleDeliverNoticeHistory.setPatronId(olePatronId);
        oleDeliverNoticeHistory.setNoticeSendType(OLEConstants.EMAIL);
        oleDeliverNoticeHistory.setNoticeContent(content.getBytes());
        getBusinessObjectService().save(oleDeliverNoticeHistory);
    }

    public Map placeRequest(String operatorId, OlePatronDocument olePatronDocument, String itemBarcode, String itemUUID, OleCirculationDesk olePickUpLocation, String requestTypeId, String bibId, String title, String author, String callNumber, String requestExpirationDay, String location, String requestExpirationConfigName, String onHoldConfigName, String onHoldCourtesyConfigName,String onHoldExpirationConfigName) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        Map responseMap = new HashMap();
        MaintenanceDocument newDocument = null;
        try {
            if (null == GlobalVariables.getUserSession()) {
                Person person = getPersonService().getPerson(operatorId);
                String principalName = person.getPrincipalName();
                UserSession userSession = new UserSession(principalName);
                GlobalVariables.setUserSession(userSession);
            }
            newDocument = (MaintenanceDocument) getDocumentService().getNewDocument(OLEConstants.REQUEST_DOC_TYPE);
        } catch (WorkflowException e) {
            e.printStackTrace();
            responseMap.put(OLEConstants.STATUS, OLEConstants.RQST_CREATION_FAIL);
            return responseMap;
        }
        OleDeliverRequestBo oleDeliverRequestBo = null;
        oleDeliverRequestBo = (OleDeliverRequestBo) newDocument.getNewMaintainableObject().getDataObject();
        try {
            oleDeliverRequestBo.setCreateDate(new Timestamp(System.currentTimeMillis()));
            oleDeliverRequestBo.setRequestLevel("Item Level");

            oleDeliverRequestBo.setBorrowerId(olePatronDocument.getOlePatronId());
            oleDeliverRequestBo.setBorrowerBarcode(olePatronDocument.getBarcode());
            oleDeliverRequestBo.setOlePatron(olePatronDocument);

            oleDeliverRequestBo.setRequestTypeId(requestTypeId);

            oleDeliverRequestBo.setBorrowerQueuePosition(1);

            oleDeliverRequestBo.setPickUpLocationId(olePickUpLocation.getCirculationDeskId());
            oleDeliverRequestBo.setOlePickUpLocation(olePickUpLocation);

            oleDeliverRequestBo.setItemId(itemBarcode);
            oleDeliverRequestBo.setItemUuid(itemUUID);

            oleDeliverRequestBo.setBibId(bibId);
            oleDeliverRequestBo.setTitle(title);
            oleDeliverRequestBo.setAuthor(author);
            oleDeliverRequestBo.setCallNumber(callNumber);

            oleDeliverRequestBo.setRequestCreator(OLEConstants.OleDeliverRequest.REQUESTER_OPERATOR);
            oleDeliverRequestBo.setOperatorCreateId(operatorId);

            oleDeliverRequestBo.setItemLocation(location);

            oleDeliverRequestBo.setRequestExpirationNoticeContentConfigName(requestExpirationConfigName);
            oleDeliverRequestBo.setOnHoldNoticeContentConfigName(onHoldConfigName);
            oleDeliverRequestBo.setOnHoldCourtesyNoticeContentConfigName(onHoldCourtesyConfigName);
            oleDeliverRequestBo.setOnHoldExpirationNoticeContentConfigName(onHoldExpirationConfigName);

            if (StringUtils.isNotBlank(requestExpirationDay)) {
                Timestamp requestExpirationDate = getOleDeliverRequestDocumentHelperService().calculateXDatesBasedOnCalendar(getOleDeliverRequestDocumentHelperService().getCalendarGroup(oleDeliverRequestBo.getItemLocation()), requestExpirationDay, null, true);
                oleDeliverRequestBo.setRequestExpiryDate(new java.sql.Date(requestExpirationDate.getTime()));
            }

            newDocument.getDocumentHeader().setDocumentDescription(OLEConstants.NEW_REQUEST_DOC);
            newDocument.getNewMaintainableObject().setDataObject(oleDeliverRequestBo);

            try {
                newDocument = (MaintenanceDocument) getDocumentService().routeDocument(newDocument, null, null);
            } catch (WorkflowException e) {
                LOG.error("Exception " + e);
            }
            oleDeliverRequestBo = (OleDeliverRequestBo) newDocument.getNewMaintainableObject().getDataObject();

            if (oleDeliverRequestBo.getRequestId() != null) {
                responseMap.put(OLEConstants.STATUS, OLEConstants.RQST_SUCCESS);
                responseMap.put(OLEConstants.OleDeliverRequest.REQUEST_ID, oleDeliverRequestBo.getRequestId());
            } else {
                responseMap.put(OLEConstants.STATUS, OLEConstants.RQST_FAIL);
            }
        } catch (Exception e) {
            LOG.error("Exception " + e);
            responseMap.put(OLEConstants.STATUS, OLEConstants.RQST_FAIL);
            return responseMap;
        }
        oleStopWatch.end();
        LOG.info("Time taken to place request: " + oleStopWatch.getTotalTime());
        sendNotice(oleDeliverRequestBo, olePatronDocument);
        return responseMap;
    }

    private void sendNotice(OleDeliverRequestBo oleDeliverRequestBo, OlePatronDocument olePatronDocument) {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.start();
        String noticeSendParameter = getParameter(OLEParameterConstants.NCIP_ACCEPT_ITEM_NOTICE_INDICATOR);
        if (noticeSendParameter != null && (noticeSendParameter.trim().isEmpty() || noticeSendParameter.equalsIgnoreCase("Y"))) {

            OleNoticeBo oleNoticeBo = new OleNoticeBo();
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
            oleNoticeBo.setNoticeName(OLEConstants.PICKUP_NOTICE);
            Date pickupDate = new java.sql.Date(System.currentTimeMillis());
            if (oleDeliverRequestBo.getOlePickUpLocation() != null && oleDeliverRequestBo.getOlePickUpLocation().getOnHoldDays() != null) {
                pickupDate = getOleDeliverRequestDocumentHelperService().addDate(new java.sql.Date(System.currentTimeMillis()), new Integer(oleDeliverRequestBo.getOlePickUpLocation().getOnHoldDays()));
            }
            oleNoticeBo.setNoticeSpecificContent(OLEConstants.PICKUP_NOTICE_START_CONTENT + oleDeliverRequestBo.getOlePickUpLocation().getCirculationDeskPublicName() + OLEConstants.PICKUP_NOTICE_MIDDLE_CONTENT + pickupDate + OLEConstants.PICKUP_NOTICE_FINAL_CONTENT);
            oleNoticeBo.setAuthor(oleDeliverRequestBo.getAuthor());
            oleNoticeBo.setItemCallNumber(oleDeliverRequestBo.getCallNumber());
            oleNoticeBo.setItemId(oleDeliverRequestBo.getItemId());
            oleNoticeBo.setTitle(oleDeliverRequestBo.getTitle());
            oleNoticeBo.setCirculationDeskName(oleDeliverRequestBo.getOlePickUpLocation().getCirculationDeskPublicName());
            oleNoticeBo.setCirculationDeskReplyToEmail(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail());
            OleDeliverBatchServiceImpl oleDeliverBatchService = new OleDeliverBatchServiceImpl();
            try {
                String content = oleDeliverBatchService.getEmailPickUpNotice(oleNoticeBo);
                if (!content.trim().equals("")) {
                    OleMailer oleMailer = GlobalResourceLoader.getService("oleMailer");
                    if (oleDeliverRequestBo.getOlePickUpLocation() != null && StringUtils.isNotBlank(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail())) {
                        oleMailer.sendEmail(new EmailFrom(oleDeliverRequestBo.getOlePickUpLocation().getReplyToEmail()), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.PICKUP_NOTICE_SUBJECT_LINE), new EmailBody(content), true);
                    } else {
                        String fromAddress = getParameter(OLEParameterConstants.NOTICE_FROM_MAIL);
                        if (fromAddress != null && (fromAddress.equals("") || fromAddress.trim().isEmpty())) {
                            fromAddress = OLEConstants.KUALI_MAIL;
                        }
                        oleMailer.sendEmail(new EmailFrom(fromAddress), new EmailTo(oleNoticeBo.getPatronEmailAddress()), new EmailSubject(OLEConstants.PICKUP_NOTICE_SUBJECT_LINE), new EmailBody(content), true);
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Mail send Successfully to " + oleNoticeBo.getPatronEmailAddress());
                    }
                    //oleDeliverBatchService.getPdfPickUpNotice(oleNoticeBo);
                    getOlencipAcceptItemUtil().savePickupNoticeHistory(content, olePatronDocument.getOlePatronId());
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Notice Type :" + oleNoticeBo.getNoticeName() + "  " + "Item Barcode : " + oleNoticeBo.getItemId() + " " + "Patron Name :" + oleNoticeBo.getPatronName());
                    }
                }
            } catch (Exception e) {
                LOG.error("Exception", e);
            }
        }
        oleStopWatch.end();
        LOG.info("Time taken to send notice : " + oleStopWatch.getTotalTime());
    }

    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        if(parameter==null){
            parameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,name);
            parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        }
        return parameter!=null?parameter.getValue():null;
    }
}
