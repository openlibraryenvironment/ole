package org.kuali.ole.service;

import org.kuali.ole.deliver.bo.*;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.impl.MaintenanceDocumentServiceImpl;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *OlePatronMaintenanceDocumentServiceImpl generates maintenance object and perform copy operation.
 */
public class OlePatronMaintenanceDocumentServiceImpl extends MaintenanceDocumentServiceImpl implements OlePatronMaintenanceDocumentService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronMaintenanceDocumentServiceImpl.class);


    private DocumentService documentService;

    /**
     * Gets the value of documentService which is of type DocumentService
     * @return documentService(DocumentService)
     */
    protected DocumentService getDocumentService() {
        return this.documentService;
    }
    /**
     * Sets the value for documentService which is of type DocumentService
     * @param documentService(DocumentService)
     */
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }
    /**
     * This method will set the patron object from the request parameters
     * @param document
     * @param maintenanceAction
     * @param requestParameters
     */
    @Override
    public void setupMaintenanceObject(MaintenanceDocument document, String maintenanceAction,
                                       Map<String, String[]> requestParameters) {
        LOG.debug("Inside setupMaintenanceObject method");
        document.getNewMaintainableObject().setMaintenanceAction(maintenanceAction);
        document.getOldMaintainableObject().setMaintenanceAction(maintenanceAction);

        // if action is edit or copy first need to retrieve the old record
        if (!KRADConstants.MAINTENANCE_NEW_ACTION.equals(maintenanceAction) &&
                !KRADConstants.MAINTENANCE_NEWWITHEXISTING_ACTION.equals(maintenanceAction)) {
            Object oldDataObject = retrieveObjectForMaintenance(document, requestParameters);

            // enhancement to indicate fields to/not to copy
            Object newDataObject = ObjectUtils.deepCopy((Serializable) oldDataObject);

            // process further object preparations for copy action
            if (KRADConstants.MAINTENANCE_COPY_ACTION.equals(maintenanceAction)) {
                processMaintenanceObjectForCopy(document, newDataObject, requestParameters);
                processEntityForCopy(newDataObject);
            } else {
                checkMaintenanceActionAuthorization(document, oldDataObject, maintenanceAction, requestParameters);
            }

            // set object instance for editing
            document.getOldMaintainableObject().setDataObject(oldDataObject);
            document.getNewMaintainableObject().setDataObject(newDataObject);
        }

        if (KRADConstants.MAINTENANCE_NEWWITHEXISTING_ACTION.equals(maintenanceAction)) {
            Object newBO = document.getNewMaintainableObject().getDataObject();
            Map<String, String> parameters =
                    buildKeyMapFromRequest(requestParameters, document.getNewMaintainableObject().getDataObjectClass());
            ObjectPropertyUtils.copyPropertiesToObject(parameters, newBO);
            if (newBO instanceof PersistableBusinessObject) {
                ((PersistableBusinessObject) newBO).refresh();
            }

            document.getNewMaintainableObject().setupNewFromExisting(document, requestParameters);
        } else if (KRADConstants.MAINTENANCE_NEW_ACTION.equals(maintenanceAction)) {
            document.getNewMaintainableObject().processAfterNew(document, requestParameters);
        }
    }

    /**
     * @see org.kuali.rice.krad.service.impl.MaintenanceDocumentServiceImpl#setupMaintenanceObject
     */
    /**
     * This method creates maintenance object for delete operation using maintenanceAction.
     * @param document
     * @param maintenanceAction
     * @param requestParameters
     */
    public void setupMaintenanceObjectForDelete(MaintenanceDocument document, String maintenanceAction,
                                                Map<String, String[]> requestParameters) {
        document.getNewMaintainableObject().setMaintenanceAction(maintenanceAction);
        document.getOldMaintainableObject().setMaintenanceAction(maintenanceAction);

        Object oldDataObject = retrieveObjectForMaintenance(document, requestParameters);
        Object newDataObject = ObjectUtils.deepCopy((Serializable) oldDataObject);

        document.getOldMaintainableObject().setDataObject(oldDataObject);
        document.getNewMaintainableObject().setDataObject(newDataObject);
    }

    /**
     * This method will remove the primary key and the object Id's of the entity object and its child object which is used in copy functionality
     * @param maintenanceObject
     */
    protected void processEntityForCopy(Object maintenanceObject) {
        LOG.debug("Inside processEntityForCopy method");
        OlePatronDocument newPatron = (OlePatronDocument) maintenanceObject;
        newPatron.setOlePatronId(null);
        boolean  isValueSet=false;
        EntityBo kimEntity = newPatron.getEntity();
        if(kimEntity.getNames().size() > 0 ) {
            List<EntityAffiliationBo> affiliationBos = (List<EntityAffiliationBo>) kimEntity.getAffiliations();
            for(EntityAffiliationBo affiliationBo : affiliationBos) {
                affiliationBo.setId(null);
                affiliationBo.setObjectId(null);
                affiliationBo.setVersionNumber(null);
                affiliationBo.setEntityId(null);
            }
            List<EntityEmploymentBo> employmentBos = (List<EntityEmploymentBo>) kimEntity.getEmploymentInformation();
            for(EntityEmploymentBo employmentBo : employmentBos){
                employmentBo.setId(null);
                employmentBo.setObjectId(null);
                employmentBo.setVersionNumber(null);
                employmentBo.setEntityId(null);
            }
            EntityNameBo entityName = (EntityNameBo) ObjectUtils.deepCopy((Serializable)kimEntity.getNames().get(0));
            entityName.setId(null);
            entityName.setEntityId(null);
            entityName.setObjectId(null);
            entityName.setVersionNumber(null);
            if(kimEntity.getEntityTypeContactInfos().size() > 0) {
                EntityTypeContactInfoBo entityContactInfo = (EntityTypeContactInfoBo) ObjectUtils.deepCopy((Serializable)kimEntity.getEntityTypeContactInfos().get(0));
                List<EntityPhoneBo> phones = entityContactInfo.getPhoneNumbers();
                if(phones.size() > 0) {
                    for(EntityPhoneBo phone : phones) {
                        phone.setId(null);
                        phone.setEntityId(null);
                        phone.setObjectId(null);
                        phone.setVersionNumber(null);
                    }
                }

                List<EntityEmailBo> emails = entityContactInfo.getEmailAddresses();
                if(emails.size() > 0 ) {
                    for(EntityEmailBo email : emails) {
                        email.setId(null);
                        email.setEntityId(null);
                        email.setObjectId(null);
                        email.setVersionNumber(null);
                    }
                }
                List<EntityAddressBo> addresses = entityContactInfo.getAddresses();
                if(addresses.size() > 0) {
                    for(EntityAddressBo addr : addresses) {
                        addr.setId(null);
                        addr.setEntityId(null);
                        addr.setObjectId(null);
                        addr.setVersionNumber(null);
                    }
                }
                List<OlePatronNotes> notes = newPatron.getNotes();
                if(notes.size() > 0) {
                    for(OlePatronNotes note : notes) {
                        note.setPatronNoteId(null);
                        note.setOlePatronId(null);
                        note.setObjectId(null);
                        note.setVersionNumber(null);
                    }
                }
                newPatron.setPhones(phones);
                newPatron.setEmails(emails);
                newPatron.setEntity(null);
                newPatron.setNotes(notes);
            }
            newPatron.setName(entityName);
            isValueSet=true;
        } else {

            EntityBo entityBo = (EntityBo) ObjectUtils.deepCopy((Serializable) newPatron.getEntity());
            entityBo.setId(null);
            entityBo.setObjectId(null);
            entityBo.setVersionNumber(null);
            List<EntityPhoneBo> phones = newPatron.getPhones();
            if (phones!=null && phones.size() > 0) {
                for (EntityPhoneBo phone : phones) {
                    phone.setId(null);
                    phone.setEntityId(null);
                    phone.setObjectId(null);
                    phone.setVersionNumber(null);
                }
            }

            List<EntityEmailBo> emails = newPatron.getEmails();
            if (emails!=null && emails.size() > 0) {
                for (EntityEmailBo email : emails) {
                    email.setId(null);
                    email.setEntityId(null);
                    email.setObjectId(null);
                    email.setVersionNumber(null);
                }
            }
            List<EntityAddressBo> addresses = newPatron.getAddresses();
            if (addresses!=null && addresses.size() > 0) {
                for (EntityAddressBo addr : addresses) {
                    addr.setId(null);
                    addr.setEntityId(null);
                    addr.setObjectId(null);
                    addr.setVersionNumber(null);
                }
            }
            List<OlePatronNotes> notes = newPatron.getNotes();
            if (notes!=null && notes.size() > 0) {
                for (OlePatronNotes note : notes) {
                    note.setPatronNoteId(null);
                    note.setOlePatronId(null);
                    note.setObjectId(null);
                    note.setVersionNumber(null);
                }
            }

            if (!isValueSet) {
                newPatron.setPhones(phones);
                newPatron.setEmails(emails);
                newPatron.setEntity(entityBo);
                newPatron.setNotes(notes);
            }
        }
        List<OleProxyPatronDocument> oleProxyPatron = newPatron.getOleProxyPatronDocuments();
        if (oleProxyPatron.size() > 0) {
            for (OleProxyPatronDocument proxyPatron : oleProxyPatron) {
                proxyPatron.setOleProxyPatronDocumentId(null);
                proxyPatron.setOlePatronId(null);
                proxyPatron.setObjectId(null);
                proxyPatron.setVersionNumber(null);
            }
        }
        newPatron.setOleProxyPatronDocuments(oleProxyPatron);

        List<EntityEmploymentBo> employmentBoList = newPatron.getEmployments();
        if(employmentBoList.size() > 0) {
            for(EntityEmploymentBo employmentBo : employmentBoList ) {
                employmentBo.setId(null);
                employmentBo.setEntityId(null);
                employmentBo.setObjectId(null);
                employmentBo.setVersionNumber(null);
            }
        }
        newPatron.setEmployments(employmentBoList);
        EntityAffiliationBo entityAffiliationBo = new EntityAffiliationBo();
        List<OlePatronAffiliation> affiliationBoList = newPatron.getPatronAffiliations();
        //List<EntityAffiliationBo> entityAffiliationBoList = kimEntity.getAffiliations();
        if(affiliationBoList.size() > 0) {
            for(OlePatronAffiliation affiliationBo : affiliationBoList) {
                affiliationBo.setEntityAffiliationId(null);
                affiliationBo.setVersionNumber(null);
                affiliationBo.setObjectId(null);
                entityAffiliationBo = affiliationBo.getEntityAffliationBo();
                entityAffiliationBo.setId(null);
                entityAffiliationBo.setEntityId(null);
                entityAffiliationBo.setObjectId(null);
                entityAffiliationBo.setVersionNumber(null);


            }
        }
        newPatron.setPatronAffiliations(affiliationBoList);
    }
}