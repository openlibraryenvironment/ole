package org.kuali.ole.deliver.maintenance;

import org.apache.ojb.broker.metadata.ClassNotPersistenceCapableException;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.form.OlePatronMaintenanceDocumentForm;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.krad.OleComponentUtils;
import org.kuali.ole.service.OlePatronService;
import org.kuali.ole.service.OlePatronServiceImpl;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameTypeBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.util.ProcessLogger;

import java.util.*;

/**
 * OlePatronMaintenanceImpl performs save ,edit,copy  and create new operation for OlePatron.
 */
public class OlePatronMaintenanceImpl extends MaintainableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronMaintenanceImpl.class);
    OlePatronService olePatronService = new OlePatronServiceImpl();


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
        OleAddressBo oleAddressBo = new OleAddressBo();
        List<OleEntityAddressBo> oleEntityAddressList = new ArrayList<OleEntityAddressBo>();
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
                    oleAddressBo = getBusinessObjectService().findByPrimaryKey(OleAddressBo.class, addMap);
                    entityAddressBo.setOleAddressBo(oleAddressBo);
                    entityAddressBo.setEntityAddressBo(entityAdd);
                    oleEntityAddressList.add(entityAddressBo);
                }
                olePatron.setOleEntityAddressBo(oleEntityAddressList);
                olePatron.setPhones(entity.getEntityTypeContactInfos().get(0).getPhoneNumbers());
                olePatron.setEmails(entity.getEntityTypeContactInfos().get(0).getEmailAddresses());
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
        String url = "temporaryCirculationRecord?viewId=OleTemporaryCirculationHistoryRecordView&amp;methodToCall=viewTempCircRecords&amp;patronId=" + olePatronId;
        return url;
    }

    public String getLoanedRecords(String olePatronId) {
        String url = "patronLoanedRecord?viewId=OlePatronLoanedRecordView&amp;methodToCall=viewLoanedRecords&amp;patronId=" + olePatronId;
        return url;
    }

    public String getRequestedRecords(String olePatronId) {
        String url = "patronRequestedRecord?viewId=OlePatronRequestedRecordView&amp;methodToCall=viewRequestedRecords&amp;patronId=" + olePatronId;
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
