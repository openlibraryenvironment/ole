package org.kuali.ole.deliver.maintenance;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.broker.metadata.ClassNotPersistenceCapableException;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEPropertyConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.service.OlePatronService;
import org.kuali.ole.service.OlePatronServiceImpl;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameTypeBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * OlePatronMaintenanceImpl performs save ,edit,copy  and create new operation for OlePatron.
 */
public class OlePatronMaintenanceImpl extends MaintainableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronMaintenanceImpl.class);
    OlePatronService olePatronService = new OlePatronServiceImpl();

    String baseUrl = ConfigContext.getCurrentContextConfig().getProperty(OLEPropertyConstants.OLE_URL_BASE);
    /**
     * This method populate the patron object used for edit and copy
     *
     * @param document
     * @param dataObjectKeys
     * @return olePatron(Object)
     */
    @Override
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys) {
        LOG.debug("Inside retrieveObjectForEditOrCopy()");
        Object dataObject = null;
        OlePatronDocument olePatron = new OlePatronDocument();
        OleEntityAddressBo entityAddressBo = null;
        OleEntityPhoneBo entityPhoneBo = null;
        OleEntityEmailBo entityEmailBo = null;
        List<OleAddressBo> oleAddressBos = new ArrayList<>();
        List<OlePhoneBo> olePhoneBos = new ArrayList<>();
        List<OleEmailBo> oleEmailBos = new ArrayList<>();
        List<OleEntityAddressBo> oleEntityAddressList = new ArrayList<OleEntityAddressBo>();
        List<OleEntityPhoneBo> oleEntityPhoneBoList = new ArrayList<>();
        List<OleEntityEmailBo> oleEntityEmailBoList = new ArrayList<>();
        try {
            dataObject = getLookupService().findObjectBySearch(getDataObjectClass(), dataObjectKeys);
            olePatron = (OlePatronDocument) dataObject;
            EntityBo entity = olePatron.getEntity();
            if (entity.getNames().size() > 0) {
                EntityNameBo name = entity.getNames().get(0);
                olePatron.setName(name);
            }
            if (!entity.getEntityTypeContactInfos().isEmpty()) {
                List<EntityAddressBo> entityAddressList = entity.getEntityTypeContactInfos().get(0).getAddresses();
                for (EntityAddressBo entityAdd : entityAddressList) {
                    entityAddressBo = new OleEntityAddressBo();
                    Map addMap = new HashMap();
                    addMap.put(OLEConstants.OlePatron.ENTITY_BO_ID, entityAdd.getId());
                    oleAddressBos = (List<OleAddressBo>) getBusinessObjectService().findMatching(OleAddressBo.class, addMap);
                    if(CollectionUtils.isNotEmpty(oleAddressBos)){
                        entityAddressBo.setOleAddressBo(oleAddressBos.get(0));
                    }
                    entityAddressBo.setEntityAddressBo(entityAdd);
                    oleEntityAddressList.add(entityAddressBo);
                }
                olePatron.setOleEntityAddressBo(oleEntityAddressList);
                List<EntityPhoneBo> entityPhoneBoList = entity.getEntityTypeContactInfos().get(0).getPhoneNumbers();
                for(EntityPhoneBo entityPhone : entityPhoneBoList) {
                    entityPhoneBo = new OleEntityPhoneBo();
                    Map map = new HashMap();
                    map.put(OLEConstants.OlePatron.ENTITY_BO_ID, entityPhone.getId());
                    olePhoneBos = (List<OlePhoneBo>) getBusinessObjectService().findMatching(OlePhoneBo.class, map);
                    if(CollectionUtils.isNotEmpty(olePhoneBos)) {
                        entityPhoneBo.setOlePhoneBo(olePhoneBos.get(0));
                    }
                    entityPhoneBo.setEntityPhoneBo(entityPhone);
                    oleEntityPhoneBoList.add(entityPhoneBo);
                }
                olePatron.setOleEntityPhoneBo(oleEntityPhoneBoList);
                List<EntityEmailBo> entityEmailBoList = entity.getEntityTypeContactInfos().get(0).getEmailAddresses();
                for(EntityEmailBo entityEmail : entityEmailBoList) {
                    entityEmailBo = new OleEntityEmailBo();
                    Map map = new HashMap();
                    map.put(OLEConstants.OlePatron.ENTITY_BO_ID, entityEmail.getId());
                    oleEmailBos = (List<OleEmailBo>) getBusinessObjectService().findMatching(OleEmailBo.class, map);
                    if(CollectionUtils.isNotEmpty(oleEmailBos)) {
                        entityEmailBo.setOleEmailBo(oleEmailBos.get(0));
                    }
                    entityEmailBo.setEntityEmailBo(entityEmail);
                    oleEntityEmailBoList.add(entityEmailBo);
                }
                olePatron.setOleEntityEmailBo(oleEntityEmailBoList);
            }
            
            olePatron.setEmployments(entity.getEmploymentInformation());
            List<OlePatronAffiliation> patronAffiliations = new ArrayList<OlePatronAffiliation>();
            olePatron.setPatronAffiliations(getPatronAffiliationFromEntity(entity.getAffiliations(), entity.getEmploymentInformation()));

            if (olePatron.getOlePatronId() != null) {
                LoanProcessor loanProcessor = new LoanProcessor();
               /* OleDeliverRequestDocumentHelperServiceImpl requestService = new OleDeliverRequestDocumentHelperServiceImpl();
                List<OleDeliverRequestBo> oleDeliverRequestBoList = olePatron.getOleDeliverRequestBos();
                if (oleDeliverRequestBoList.size() > 0) {
                    for (int i = 0; i < oleDeliverRequestBoList.size(); i++) {
                        OleItemSearch oleItemSearch = requestService.getItemDetailsForPatron(oleDeliverRequestBoList.get(i).getItemUuid());
                        if (oleItemSearch != null && oleItemSearch.getItemBarCode() != null) {
                            oleDeliverRequestBoList.get(i).setTitle(oleItemSearch.getTitle());
                            oleDeliverRequestBoList.get(i).setCallNumber(oleItemSearch.getCallNumber());
                        }
                    }
                }
                try {
                    olePatron.setOleLoanDocuments(loanProcessor.getPatronLoanedItemBySolr(olePatron.getOlePatronId()));
                    olePatron.setOleTemporaryCirculationHistoryRecords(loanProcessor.getPatronTemporaryCirculationHistoryRecords(olePatron.getOlePatronId()));
                    olePatron.setOleDeliverRequestBos(loanProcessor.getPatronRequestRecords(olePatron.getOlePatronId()));
                } catch (Exception e) {
                    LOG.error("Exception", e);
                }*/
                if (olePatron.getOleLoanDocuments().size() > 0) {
                    olePatron.setLoanFlag(true);
                }
                if (olePatron.getOleTemporaryCirculationHistoryRecords().size() > 0) {
                    olePatron.setTempCircHistoryFlag(true);
                }
                if (olePatron.getOleDeliverRequestBos().size() > 0) {
                    olePatron.setRequestFlag(true);
                }
                OlePatronDocument olePatronDocument;
                OleProxyPatronDocument proxyPatronDocument = null;
                List<OleProxyPatronDocument> proxyPatronDocuments = new ArrayList<OleProxyPatronDocument>();
                Map proxyMap = new HashMap();
                proxyMap.put(OLEConstants.OlePatron.PROXY_PATRON_ID, olePatron.getOlePatronId());
                List<OleProxyPatronDocument> oleProxyPatronDocumentList = (List<OleProxyPatronDocument>) getBusinessObjectService().findMatching(OleProxyPatronDocument.class, proxyMap);
                if (oleProxyPatronDocumentList.size() > 0) {
                    for (OleProxyPatronDocument oleProxyPatronDocument : oleProxyPatronDocumentList) {
                        proxyMap.remove(OLEConstants.OlePatron.PROXY_PATRON_ID);
                        proxyMap.put(OLEConstants.OlePatron.PATRON_ID, oleProxyPatronDocument.getOlePatronId());
                        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, proxyMap);
                        if (olePatronDocumentList.size() > 0) {
                            olePatronDocument = olePatronDocumentList.get(0);
                            proxyPatronDocument = new OleProxyPatronDocument();
                           // if (olePatronDocument.isActiveIndicator()) {
                                proxyPatronDocument.setOlePatronId(olePatronDocument.getOlePatronId());
                                proxyPatronDocument.setProxyForPatronFirstName(olePatronDocument.getEntity().getNames().get(0).getFirstName());
                                proxyPatronDocument.setProxyForPatronLastName(olePatronDocument.getEntity().getNames().get(0).getLastName());
                                proxyPatronDocument.setProxyPatronActivationDate(oleProxyPatronDocument.getProxyPatronActivationDate());
                                proxyPatronDocument.setProxyPatronExpirationDate(oleProxyPatronDocument.getProxyPatronExpirationDate());

                         //   }
                            proxyPatronDocuments.add(proxyPatronDocument);
                        }
                    }
                    olePatron.setOleProxyPatronDocumentList(proxyPatronDocuments);
                }

                if(olePatron.getOleLoanDocuments()!=null){
                    olePatron.setLoanCount(olePatron.getOleLoanDocuments().size());
                }
                if(olePatron.getOleTemporaryCirculationHistoryRecords()!=null){
                    olePatron.setTempCirculationHistoryCount(olePatron.getOleTemporaryCirculationHistoryRecords().size());
                }
                if(olePatron.getOleDeliverRequestBos()!=null){
                    olePatron.setRequestedItemRecordsCount(olePatron.getOleDeliverRequestBos().size());
                }
                if(olePatron.getLostBarcodes()!=null){
                  Collections.sort(olePatron.getLostBarcodes(), new Comparator<OlePatronLostBarcode>() {
                      @Override
                      public int compare(OlePatronLostBarcode o1, OlePatronLostBarcode o2) {

                          return  Integer.parseInt(o2.getId()) - Integer.parseInt(o1.getId());
                      }
                  });

                }
            }
        } catch (ClassNotPersistenceCapableException ex) {
            if (!document.getOldMaintainableObject().isExternalBusinessObject()) {
                LOG.error("Data Object Class: "
                        + getDataObjectClass()
                        + " is not persistable and is not externalizable - configuration error", ex);
                throw new RuntimeException("Data Object Class: "
                        + getDataObjectClass()
                        + " is not persistable and is not externalizable - configuration error");
            }
        }
        return olePatron;
    }

    /**
     * This method will set the default value for the name object and also set the description when a new patron document is created
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterNew(MaintenanceDocument document,
                                Map<String, String[]> requestParameters) {
        LOG.debug("Inside processAfterNew()");
        EntityNameTypeBo nameTypeBo = (EntityNameTypeBo) KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(EntityNameTypeBo.class, OLEConstants.OlePatron.ENTITY_NM_TYP_CD);
        OlePatronDocument patronDocument = (OlePatronDocument) document.getNewMaintainableObject().getDataObject();
        patronDocument.setActiveIndicator(true);
        patronDocument.getName().setNameCode(OLEConstants.OlePatron.ENTITY_NM_TYP_CD);
        patronDocument.getName().setActive(true);
        patronDocument.getName().setDefaultValue(true);
        patronDocument.getName().setNameType(nameTypeBo);
        document.getNewMaintainableObject().setDataObject(patronDocument);
        super.processAfterNew(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription("New Patron Document");

    }

    /**
     * This method will set the description when copy is selected
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterCopy(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        LOG.debug("Inside processAfterCopy()");
        super.processAfterCopy(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription("Copied Patron Document");
    }

    /**
     * This method will set the description for edit operation
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterEdit(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        LOG.debug("Inside processAfterEdit()");
        super.processAfterEdit(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription("Edited Patron Document");
    }

    private List<OlePatronAffiliation> getPatronAffiliationFromEntity(List<EntityAffiliationBo> affiliations,
                                                                      List<EntityEmploymentBo> employeeDetails) {
        List<OlePatronAffiliation> patronAffiliations = new ArrayList<OlePatronAffiliation>();
        for (EntityAffiliationBo entityAffiliationBo : affiliations) {
            OlePatronAffiliation patronAffiliation = new OlePatronAffiliation(entityAffiliationBo);
            List<EntityEmploymentBo> employmentBos = new ArrayList<EntityEmploymentBo>();
            for (EntityEmploymentBo entityEmploymentBo : employeeDetails) {
                if (patronAffiliation.getEntityAffiliationId().equalsIgnoreCase(entityEmploymentBo.getEntityAffiliationId())) {
                    employmentBos.add(entityEmploymentBo);
                }
                patronAffiliation.setEmployments(employmentBos);
            }
            patronAffiliations.add(patronAffiliation);
        }
        return patronAffiliations;
    }

    public String getTempCircRecords(String olePatronId) {
        String url = baseUrl + "/portal.do?channelTitle=Patron&channelUrl=" + baseUrl + "/ole-kr-krad/temporaryCirculationRecord?viewId=OleTemporaryCirculationHistoryRecordView&amp;methodToCall=viewTempCircRecords&amp;patronId=" + olePatronId;
        return url;
    }

    public String getLoanedRecords(String olePatronId) {
        String url = baseUrl + "/portal.do?channelTitle=Patron&channelUrl=" + baseUrl + "/ole-kr-krad/patronLoanedRecord?viewId=OlePatronLoanedRecordView&amp;methodToCall=viewLoanedRecords&amp;patronId=" + olePatronId;
        return url;
    }

    public String getRequestedRecords(String olePatronId) {
        String url = baseUrl + "/portal.do?channelTitle=Patron&channelUrl=" + baseUrl + "/ole-kr-krad/patronRequestedRecord?viewId=OlePatronRequestedRecordView&amp;methodToCall=viewRequestedRecords&amp;patronId=" + olePatronId;
        return url;
    }

    public String getCountOfPendingRequests(String itemId) {
        Map itemMap = new HashMap();
        itemMap.put(OLEConstants.OleDeliverRequest.ITEM_ID, itemId);
        List<OleDeliverRequestBo> oleDeliverRequestBoList = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, itemMap);
        if (oleDeliverRequestBoList != null && oleDeliverRequestBoList.size() > 0) {

            return "View all requests";
        } else
            return " ";
    }
}
