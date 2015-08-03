package org.kuali.ole.service;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.OleLoanDocumentsFromSolrBuilder;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OleTemporaryCirculationHistory;
import org.kuali.ole.deliver.api.OleDeliverRequestDefinition;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.ingest.pojo.OlePatron;
import org.kuali.ole.deliver.bo.OlePatronLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronLoanDocuments;
import org.kuali.ole.deliver.bo.OleRenewalLoanDocument;
import org.kuali.ole.deliver.api.*;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.ObjectUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OlePatronServiceImpl performs patron operation(create,update).
 */
public class OlePatronServiceImpl implements OlePatronService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronServiceImpl.class);
    private BusinessObjectService businessObjectService;
    private IdentityService identityService;
    private CriteriaLookupService criteriaLookupService;
    private LoanProcessor loanProcessor;
    private OlePatronHelperService olePatronHelperService;
    private OleLoanDocumentsFromSolrBuilder oleLoanDocumentsFromSolrBuilder;

    protected  OlePatronHelperService getOlePatronHelperService(){
        if(olePatronHelperService==null)
            olePatronHelperService=new OlePatronHelperServiceImpl();
        return olePatronHelperService;
    }

    /**
     * This method initiate LoanProcessor.
     * @return LoanProcessor
     */
    protected LoanProcessor getLoanProcessor() {
        if(loanProcessor==null)
            loanProcessor=new LoanProcessor();
        return loanProcessor;
    }

    /**
     * Gets the instance of BusinessObjectService
     * @return businessObjectService(BusinessObjectService)
     */
    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
    /**
     * Gets the instance of IdentityService
     * @return identityService(IdentityService)
     */
    protected IdentityService getIdentityService() {
        if (identityService == null) {
            identityService = (IdentityService) SpringContext.getBean("kimIdentityDelegateService");
        }
        return identityService;
    }

    /**
     * Gets the instance of CriteriaLookupService
     * @return criteriaLookupService(CriteriaLookupService)
     */
    protected CriteriaLookupService getCriteriaLookupService() {
        if(criteriaLookupService == null) {
            criteriaLookupService = GlobalResourceLoader.getService(OLEConstants.OlePatron.CRITERIA_LOOKUP_SERVICE);
        }
        return criteriaLookupService;
    }
    /**
     * Based on patron Id it will return the patron object
     * @param patronId
     * @return OlePatronDefinition
     */
    @Override
    public OlePatronDefinition getPatron(String patronId) {
        LOG.debug("Inside the getPatron method");
        Map<String, Object> criteria = new HashMap<String, Object>(4);
        criteria.put(OLEConstants.OlePatron.PATRON_ID, patronId);
        return OlePatronDocument.to(getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, criteria));
    }
    /**
     * This method will create and persist the patron document
     * @param olePatron
     * @return savedOlePatronDefinition(OlePatronDefinition)
     */
    @Override
    public OlePatronDefinition createPatron(OlePatronDefinition olePatron) {
        LOG.debug(" Inside create patron ");
        OlePatronDefinition savedOlePatronDefinition = new OlePatronDefinition();
        try{
            BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
            OlePatronDocument olePatronDocument = OlePatronDocument.from(olePatron);
            EntityBo kimEntity = olePatronDocument.getEntity();
            EntityBo entity2 = getBusinessObjectService().save(kimEntity);
            List<OleAddressBo> oleAddressBoList = getOlePatronHelperService().retrieveOleAddressBo(entity2,olePatronDocument);
            olePatronDocument.setOleAddresses(oleAddressBoList);
            olePatronDocument.setOlePatronId(entity2.getId());
            olePatronDocument.setEntity(kimEntity);
            populateOperatorIdAndTimeStampForNotes(olePatronDocument, false);
            OlePatronDocument savedPatronDocument = businessObjectService.save(olePatronDocument);
            savedOlePatronDefinition = OlePatronDocument.to(savedPatronDocument);
        }catch(Exception e){
            e.printStackTrace();
        }

        return savedOlePatronDefinition;
    }

    private void populateOperatorIdAndTimeStampForNotes(OlePatronDocument olePatronDocument, boolean isUpdate) {
        List<OlePatronNotes> notes = olePatronDocument.getNotes();
        if(CollectionUtils.isNotEmpty(notes)){
            if (!isUpdate) {
                populateCurrentTimeStamp(notes);
            }else{
                Map<String,String> notesMap=new HashMap<String,String>();
                notesMap.put("olePatronId", olePatronDocument.getOlePatronId());
                List<OlePatronNotes>  olePatronNotesList = (List<OlePatronNotes>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronNotes.class,notesMap);
                if(CollectionUtils.isNotEmpty(olePatronNotesList)){
                    Map<String,OlePatronNotes> patronNotesMap = new HashMap<>();
                    for(OlePatronNotes olePatronNotes  : olePatronNotesList){
                        patronNotesMap.put(olePatronNotes.getPatronNoteId(),olePatronNotes);
                    }
                    if(CollectionUtils.isNotEmpty(olePatronDocument.getNotes())){
                        for(OlePatronNotes olePatronNotes : olePatronDocument.getNotes()){
                            if(patronNotesMap.containsKey(olePatronNotes.getPatronNoteId())){
                                OlePatronNotes patronDbNote = patronNotesMap.get(olePatronNotes.getPatronNoteId());
                                if(!patronDbNote.getPatronNoteTypeId().equals(olePatronNotes.getPatronNoteTypeId()) ||
                                        !patronDbNote.getPatronNoteText().equals(olePatronNotes.getPatronNoteText())){
                                    olePatronNotes.setOperatorId(OLEConstants.PATRON_NOTE_DEFAULT_OPERATOR);
                                    olePatronNotes.setNoteCreatedOrUpdatedDate(new Timestamp(System.currentTimeMillis()));
                                }
                            }
                        }
                    }
                }else{
                    populateCurrentTimeStamp(olePatronDocument.getNotes());
                }
            }
        }
    }

    private void populateCurrentTimeStamp(List<OlePatronNotes> notes) {
        for (Iterator<OlePatronNotes> iterator = notes.iterator(); iterator.hasNext(); ) {
            OlePatronNotes olePatronNotes = iterator.next();
            olePatronNotes.setOperatorId(OLEConstants.PATRON_NOTE_DEFAULT_OPERATOR);
            olePatronNotes.setNoteCreatedOrUpdatedDate(new Timestamp(System.currentTimeMillis()));
        }
    }

    /**
     * This method will update the patron object which is already persist
     * @param olePatronDefinition
     * @return OlePatronDefinition
     */
    @Override
    public OlePatronDefinition updatePatron(OlePatronDefinition olePatronDefinition) {
        LOG.debug("Inside the updatePatron method");
        boolean doc = false;
        String documentNumber = "";
        OlePatronDocument updatedPatronDocument = null;

        try {
            if (olePatronDefinition.getOlePatronId() != null) {
                OlePatronDocument newPatronBo = OlePatronDocument.from(olePatronDefinition);
                Map<String, Object> criteria = new HashMap<String, Object>(4);
                criteria.put(OLEConstants.OlePatron.PATRON_ID,olePatronDefinition.getOlePatronId());
                OlePatronDocument olePatronBo = getBusinessObjectService().findByPrimaryKey(OlePatronDocument.class, criteria);
                if (olePatronBo != null) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(OLEConstants.BORROWER_TYPE_ID, newPatronBo.getBorrowerType());
                    List<OleBorrowerType> borrowerTypes = (List<OleBorrowerType>) getBusinessObjectService().findMatching(OleBorrowerType.class, map);
                    if (borrowerTypes.size() > 0) {
                        newPatronBo.setOleBorrowerType(borrowerTypes.get(0));
                    }
                    EntityBo kimEntity = (EntityBo)ObjectUtils.deepCopy((Serializable)olePatronBo.getEntity());
                    newPatronBo.getEntity().setId(kimEntity.getId());
                    List<EntityTypeContactInfoBo> entityTypeContactInfoBoList = (List<EntityTypeContactInfoBo>)ObjectUtils.deepCopy((Serializable) kimEntity.getEntityTypeContactInfos());
                    newPatronBo.getEntity().getNames().get(0).setId(kimEntity.getNames().get(0).getId());
                    kimEntity.getNames().get(0).setFirstName(newPatronBo.getEntity().getNames().get(0).getFirstName());
                    kimEntity.getNames().get(0).setLastName(newPatronBo.getEntity().getNames().get(0).getLastName());
                    kimEntity.getNames().get(0).setNamePrefix(newPatronBo.getEntity().getNames().get(0).getNamePrefix());
                    kimEntity.getNames().get(0).setNameSuffix(newPatronBo.getEntity().getNames().get(0).getNameSuffix());
                    entityTypeContactInfoBoList.get(0).setAddresses((List<EntityAddressBo>)
                            ObjectUtils.deepCopy((Serializable)newPatronBo.getEntity().getEntityTypeContactInfos().get(0).getAddresses()));
                    entityTypeContactInfoBoList.get(0).setEmailAddresses((List<EntityEmailBo>)
                            ObjectUtils.deepCopy((Serializable) newPatronBo.getEntity().getEntityTypeContactInfos().get(0).getEmailAddresses()));
                    entityTypeContactInfoBoList.get(0).setPhoneNumbers((List<EntityPhoneBo>)
                            ObjectUtils.deepCopy((Serializable) newPatronBo.getEntity().getEntityTypeContactInfos().get(0).getPhoneNumbers()));
                    kimEntity.setEntityTypeContactInfos(entityTypeContactInfoBoList);
                    newPatronBo.setEntity(kimEntity);
                    List<EntityAddressBo> addressBoList = olePatronBo.getEntity().getEntityTypeContactInfos().get(0).getAddresses();
                    if(addressBoList.size() > 0) {
                        for(int i=0;i<addressBoList.size();i++){
                            EntityAddressBo entityAddressBo = addressBoList.get(i);
                            Map<String, Object> addressMap = new HashMap<String, Object>();
                            addressMap.put(OLEConstants.OlePatron.ENTITY_BO_ID,entityAddressBo.getId());
                            //List<OleAddressBo> oleAddressBo = (List<OleAddressBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleAddressBo.class,criteria);
                            OleAddressBo oleAddressBo =  KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleAddressBo.class,addressMap);
                            if(entityAddressBo != null) {
                                KRADServiceLocator.getBusinessObjectService().delete(oleAddressBo);
                                //KRADServiceLocator.getBusinessObjectService().delete(entityAddressBo);
                            }
                        }
                    }

                    List<EntityPhoneBo> phoneBoList = olePatronBo.getEntity().getEntityTypeContactInfos().get(0).getPhoneNumbers();
                    if(phoneBoList.size() > 0) {
                        KRADServiceLocator.getBusinessObjectService().delete(phoneBoList);
                    }
                    List<EntityEmailBo> emailBoList = olePatronBo.getEntity().getEntityTypeContactInfos().get(0).getEmailAddresses();
                    if(emailBoList.size() > 0) {
                        KRADServiceLocator.getBusinessObjectService().delete(emailBoList);
                    }
                    List<OlePatronNotes> patronNotesList = olePatronBo.getNotes();
                    if(patronNotesList.size() > 0) {
                        KRADServiceLocator.getBusinessObjectService().delete(patronNotesList);
                    }
                    List<OlePatronLostBarcode> lostBarcodeList = olePatronBo.getLostBarcodes();
                    if(lostBarcodeList.size() > 0) {
                        KRADServiceLocator.getBusinessObjectService().delete(lostBarcodeList);
                    }
                    List<EntityEmploymentBo> employmentBoList = olePatronBo.getEntity().getEmploymentInformation();
                    if(employmentBoList.size() > 0) {
                        KRADServiceLocator.getBusinessObjectService().delete(employmentBoList);
                    }
                    List<EntityAffiliationBo> affiliationBoList = olePatronBo.getEntity().getAffiliations();
                    if(affiliationBoList.size() > 0) {
                        KRADServiceLocator.getBusinessObjectService().delete(affiliationBoList);
                    }
                    List<OleProxyPatronDocument> proxyPatronDocuments = olePatronBo.getOleProxyPatronDocuments();
                    if(proxyPatronDocuments.size() > 0) {
                        KRADServiceLocator.getBusinessObjectService().delete(proxyPatronDocuments);
                    }
                    List<OlePatronLocalIdentificationBo> patronLocalIdentificationBos = olePatronBo.getOlePatronLocalIds();
                    if(patronLocalIdentificationBos.size() > 0) {
                        KRADServiceLocator.getBusinessObjectService().delete(patronLocalIdentificationBos);
                    }

                    //EntityBo entity = getBusinessObjectService().save(kimEntity);
                    newPatronBo.setEntity(kimEntity);
                    List<OleAddressBo> oleAddressBos = new ArrayList<OleAddressBo>();
                    List<OleEntityAddressBo> oleEntityAddressBos = newPatronBo.getOleEntityAddressBo();
                    newPatronBo.setEntity(kimEntity);
                    List<EntityAddressBo> entityAddresses = kimEntity.getEntityTypeContactInfos().get(0).getAddresses();
                    for(int i=0;i<entityAddresses.size();i++){
                        if (i < oleEntityAddressBos.size()) {
                            oleEntityAddressBos.get(i).setEntityAddressBo(entityAddresses.get(i));
                        }
                    }
                    if(oleEntityAddressBos.size() > 0) {
                        for(int i=0;i<oleEntityAddressBos.size();i++){
                            OleAddressBo addressBo = oleEntityAddressBos.get(i).getOleAddressBo();
                            EntityAddressBo entityAddressBo = oleEntityAddressBos.get(i).getEntityAddressBo();
                                Map<String, Object> addMap = new HashMap<String, Object>();
                                addMap.put(OLEConstants.OlePatron.ENTITY_BO_ID,entityAddressBo.getId());
                                OleAddressBo oleAddressBo =  KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleAddressBo.class,addMap);
                                if(oleAddressBo==null){
                                    oleAddressBo = new OleAddressBo();
                                }
                                oleAddressBo.setId(entityAddressBo.getId());
                                oleAddressBo.setOlePatronId(kimEntity.getId());
                                oleAddressBo.setOleAddressId(KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("OLE_DLVR_ADD_S").toString());
                                oleAddressBo.setAddressValidFrom(addressBo.getAddressValidFrom());
                                oleAddressBo.setAddressValidTo(addressBo.getAddressValidTo());
                                oleAddressBo.setAddressVerified(addressBo.isAddressVerified());
                                oleAddressBo.setAddressSource(addressBo.getAddressSource());
                                oleAddressBos.add(oleAddressBo);

                            }
                        newPatronBo.setOleAddresses(oleAddressBos);
                        }
                    populateOperatorIdAndTimeStampForNotes(newPatronBo,true);
                    updatedPatronDocument =  getBusinessObjectService().save(newPatronBo);
                }
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
        LOG.debug("Leaving the updatePatron method");
        return OlePatronDocument.to(updatedPatronDocument);
    }

    /**
     * If the EntityNameBo is not null,it will add to the EntityBo object
     * @param name
     * @param entity
     */
    private void addName(EntityNameBo name, EntityBo entity) {
        LOG.debug("Inside the addName method");
        List<EntityNameBo> entityName = entity.getNames();
        if (name != null) {
            //name.setEntityId(entity.getId());
            entityName.add(name);
            entity.setNames(entityName);
        }
    }

    /**
     * Add the EntityName object to the Entity object
     * @param name
     * @param entity
     */
    @Override
    public void addNameToEntity(EntityName name, Entity entity) {
        LOG.debug("Inside the addNameToEntity method");
        EntityBo entityBo = EntityBo.from(entity);
        EntityNameBo nameBo = EntityNameBo.from(name);
        addName(nameBo, entityBo);
    }
    /**
     * Add the EntityEmail object to the EntityTypeContactInfo object
     * @param emails
     * @param entityTypeContactInfo
     */
    @Override
    public void addEmailToEntity(List<EntityEmail> emails, EntityTypeContactInfo entityTypeContactInfo) {
        LOG.debug("Inside the addEmailToEntity method");
        List<EntityEmailBo> emailBos = new ArrayList<EntityEmailBo>();
        for (EntityEmail email : emails) {
            emailBos.add(EntityEmailBo.from(email));
        }
        addEmail(emailBos, EntityTypeContactInfoBo.from(entityTypeContactInfo));
    }

    /**
     * If the EntityEmailBo is not null,it will add to the EntityTypeContactInfoBo object
     * @param emails
     * @param entityTypeContactInfoBo
     */
    private void addEmail(List<EntityEmailBo> emails, EntityTypeContactInfoBo entityTypeContactInfoBo) {
        LOG.debug("Inside the addEmail method");
        if (emails != null) {
            entityTypeContactInfoBo.setEmailAddresses(emails);
        }
    }
    /**
     * If the EntityAddressBo is not null,it will add to the EntityTypeContactInfoBo object
     * @param entityAddress
     * @param entityTypeContactInfoBo
     */
    private void addAddress(List<EntityAddressBo> entityAddress, EntityTypeContactInfoBo entityTypeContactInfoBo) {
        LOG.debug("Inside the addAddress method");
        if (entityAddress != null) {
            entityTypeContactInfoBo.setAddresses(entityAddress);
        }
    }
    /**
     * Add the EntityAddress object to the EntityTypeContactInfo object
     * @param oleEntityAddress
     * @param entityTypeContactInfo
     */
    @Override
    public void addAddressToEntity(List<OleEntityAddressDefinition> oleEntityAddress, EntityTypeContactInfo entityTypeContactInfo) {
        List<EntityAddressBo> addrBos = new ArrayList<EntityAddressBo>();
        List<OleEntityAddressBo> oleAddrBos = new ArrayList<OleEntityAddressBo>();
        for (OleEntityAddressDefinition oleAddr : oleEntityAddress) {
            EntityAddressBo address = EntityAddressBo.from(oleAddr.getEntityAddressBo());
            OleAddressBo oleAddress = OleAddressBo.from(oleAddr.getOleAddressBo());

            oleAddrBos.add(OleEntityAddressBo.from(oleAddr));
            addrBos.add(address);
        }
        addAddress(addrBos, EntityTypeContactInfoBo.from(entityTypeContactInfo));
    }
    /**
     * If the EntityPhoneBo is not null,it will add to the EntityTypeContactInfoBo object
     * @param entityPhone
     * @param entityTypeContactInfoBo
     */
    public void addPhone(List<EntityPhoneBo> entityPhone, EntityTypeContactInfoBo entityTypeContactInfoBo) {
        LOG.debug("Inside the addPhoneToEntity method");
        if (entityPhone != null) {
            entityTypeContactInfoBo.setPhoneNumbers(entityPhone);
        }
    }
    /**
     * Add the EntityPhone object to the EntityTypeContactInfo object
     * @param entityPhone
     * @param entityTypeContactInfo
     */
    @Override
    public void addPhoneToEntity(List<EntityPhone> entityPhone, EntityTypeContactInfo entityTypeContactInfo) {
        LOG.debug("Inside the addPhoneToEntity method");
        List<EntityPhoneBo> phoneBos = new ArrayList<EntityPhoneBo>();
        for (EntityPhone phone : entityPhone) {
            phoneBos.add(EntityPhoneBo.from(phone));
        }
        addPhone(phoneBos, EntityTypeContactInfoBo.from(entityTypeContactInfo));
    }

    /**
     * Returns EntityBo based on entityId
     * @param entityId
     * @return entityImpl(EntityBo)
     */
    private EntityBo getEntityBo(String entityId) {
        LOG.debug("Inside the getEntityBo method");
        EntityBo entityImpl = getBusinessObjectService().findBySinglePrimaryKey(EntityBo.class, entityId);
        if (entityImpl != null && entityImpl.getEntityTypeContactInfos() != null) {
            for (EntityTypeContactInfoBo et : entityImpl.getEntityTypeContactInfos()) {
                et.refresh();
            }
        }
        return entityImpl;
    }

    /**
     * This method will set the active indicator of the patron object as false
     * @param patronId
     * @return OlePatronDefinition
     */
    @Override
    public OlePatronDefinition inactivatePatron(String patronId) {
        LOG.debug("Inside the inactivatePatron method");
        OlePatronDocument patronDocument = OlePatronDocument.from(getPatron(patronId));
        patronDocument.setActiveIndicator(false);
        return (OlePatronDocument.to(patronDocument));
    }

    /**
     * This method will update the EntityName object
     * @param name
     * @return EntityName
     */
    @Override
    public EntityName updateName(EntityName name) {
        LOG.debug("Inside the updateName method");
        EntityNameBo entityName = EntityNameBo.from(getIdentityService().updateName(name));
        return EntityNameBo.to(entityName);
    }
    /**
     * This method will set the active indicator of the EntityNameBo object as false
     * @param nameId
     * @return
     */
    @Override
    public boolean inactivateName(String nameId) {
        LOG.debug("Inside the inactivateName method");
        EntityNameBo entityName = EntityNameBo.from(getIdentityService().inactivateName(nameId));
        if (entityName != null) {
            return true;
        }
        return false;
    }

    /**
     * This method will update the EntityEmail object
     * @param entityEmail
     * @return
     */
    @Override
    public boolean updateEmail(EntityEmail entityEmail) {
        LOG.debug("Inside the updateEmail method");
        EntityEmailBo email = EntityEmailBo.from(getIdentityService().updateEmail(entityEmail));
        if (email != null) {
            return true;
        }
        return false;
    }
    /**
     * This method will set the active indicator of the EntityEmailBo object as false
     * @param emailId
     * @return
     */
    @Override
    public boolean inactivateEmail(String emailId) {
        LOG.debug("Inside the inactivateEmail method");
        EntityEmailBo entityEmail = EntityEmailBo.from(getIdentityService().inactivateEmail(emailId));
        if (entityEmail != null) {
            return true;
        }
        return false;
    }
    /**
     * This method will update the EntityAddress object
     * @param entityAddress
     * @return boolean
     */
    @Override
    public boolean updateAddress(EntityAddress entityAddress) {
        LOG.debug("Inside the updateAddress method");
        EntityAddressBo address = EntityAddressBo.from(getIdentityService().updateAddress(entityAddress));
        if (address != null) {
            return true;
        }
        return false;
    }
    /**
     * This method will set the active indicator of the EntityAddressBo object as false
     * @param addressId
     * @return true,if entityAddress is not equal to null otherwise returns false
     */
    @Override
    public boolean inactivateAddress(String addressId) {
        LOG.debug("Inside the inactivateAddress method");
        EntityAddressBo entityAddress = EntityAddressBo.from(getIdentityService().inactivateAddress(addressId));
        if (entityAddress != null) {
            return true;
        }
        return false;
    }

    /**
     * This method will update the EntityPhone object
     * @param entityPhone
     * @return true,if phone is not equal to null otherwise returns false
     */
    @Override
    public boolean updatePhone(EntityPhone entityPhone) {
        LOG.debug("Inside the updatePhone method");
        EntityPhoneBo phone = EntityPhoneBo.from(getIdentityService().updatePhone(entityPhone));
        if (phone != null) {
            return true;
        }
        return false;
    }

    /**
     * This method will set the active indicator of the EntityPhoneBo object as false
     * @param phoneId
     * @return true,if entityPhone is not equal to null otherwise returns false
     */
    @Override
    public boolean inactivatePhone(String phoneId) {
        LOG.debug("Inside the inactivatePhone method");
        EntityPhoneBo entityPhone = EntityPhoneBo.from(getIdentityService().inactivatePhone(phoneId));
        if (entityPhone != null) {
            return true;
        }
        return false;
    }
    /**
     * This method will Persist the OlePatronNotes object
     * @param patronNote
     * @return boolean
     */
    @Override
    public boolean addNoteToPatron(OlePatronNotesDefinition patronNote) {
        LOG.debug("Inside the addNoteToPatron method");
        OlePatronNotes patronNoteBo = OlePatronNotes.from(patronNote);
        if (patronNoteBo.getOlePatronId() != null && patronNoteBo.getOlePatronNoteType() != null) {
            getBusinessObjectService().save(patronNoteBo);
            return true;
        }
        return false;
    }
    /**
     * This method will Update the OlePatronNotes object
     * @param patronNote
     * @return boolean
     */
    @Override
    public boolean updateNote(OlePatronNotesDefinition patronNote) {
        LOG.debug("Inside the updateNote method");
        OlePatronNotes patronNoteBo = OlePatronNotes.from(patronNote);
        if (patronNoteBo.getOlePatronId() != null && patronNoteBo.getPatronNoteId() != null) {
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(OLEConstants.OlePatron.PATRON_NOTE_ID, patronNote.getPatronNoteId());
            if (getBusinessObjectService().findByPrimaryKey(OlePatronNotes.class, criteria) != null) {
                getBusinessObjectService().save(patronNoteBo);
                return true;
            }
        }
        return false;
    }

    /**
     * Set the active indicator of the OlePatronNotes object as false
     * @param patronNoteId
     * @return boolean
     */
    @Override
    public boolean inactivateNote(String patronNoteId) {
        LOG.debug("Inside the inactivateNote method");
        if (patronNoteId != null) {
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(OLEConstants.OlePatron.PATRON_NOTE_ID, patronNoteId);
            OlePatronNotes patronNotes = getBusinessObjectService().findByPrimaryKey(OlePatronNotes.class, criteria);
            patronNotes.setActive(false);
            getBusinessObjectService().save(patronNotes);
            return true;
        }
        return false;
    }

    /**
     * This method will return all the patron documents
     * @return OlePatronQueryResults
     */
    @Override
    public OlePatronQueryResults getPatrons() {
        LOG.debug("Inside the findPatron method");
        GenericQueryResults<OlePatronDocument> results = getCriteriaLookupService().lookup(OlePatronDocument.class, QueryByCriteria.Builder.create().build());
        OlePatronQueryResults.Builder builder = OlePatronQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<OlePatronDefinition.Builder> ims = new ArrayList<OlePatronDefinition.Builder>();
        for (OlePatronDocument bo : results.getResults()) {
            ims.add(OlePatronDefinition.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();

    }
    /**
     * This method will return patron objects based on search criteria
     * @param queryCriteria
     * @return OlePatronQueryResults
     */
    @Override
    public OlePatronQueryResults findPatron(QueryByCriteria queryCriteria) {
        LOG.debug("Inside the findPatron method");
        GenericQueryResults<OlePatronDocument> results = getCriteriaLookupService().lookup(OlePatronDocument.class, QueryByCriteria.Builder.create().build());
        OlePatronQueryResults.Builder builder = OlePatronQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<OlePatronDefinition.Builder> ims = new ArrayList<OlePatronDefinition.Builder>();
        for (OlePatronDocument bo : results.getResults()) {
            ims.add(OlePatronDefinition.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();
    }

    /**
     * This method will return Patron loan items list  information
     * @param patronBarcode
     * @return OleLoanDocument
     */
    @Override
    public OlePatronLoanDocuments getPatronLoanedItems(String patronBarcode) {


        List<OleLoanDocument> oleLoanDocumentList=new ArrayList<OleLoanDocument>();
        List<OlePatronLoanDocument> olePatronLoanItemList=new ArrayList<OlePatronLoanDocument>();
        try{
            oleLoanDocumentList= getOleLoanDocumentsFromSolrBuilder().getPatronLoanedItemBySolr(patronBarcode, null);
            OlePatronLoanDocuments olePatronLoanDocuments=convertPatronLoanDocuments(oleLoanDocumentList);
            return olePatronLoanDocuments;
        }
        catch(Exception e){
            LOG.error("Exception while getting patron loaned items----->  "+e);
        }

        return  null;
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

    public List<OleDeliverRequestDefinition> getPatronRequestItems(String patronId) {


        List<OleDeliverRequestBo> oleDeliverRequestBos =new ArrayList<OleDeliverRequestBo>();
        List<OleDeliverRequestDefinition> oleDeliverRequestDefinitions =new ArrayList<OleDeliverRequestDefinition>();
        try{
            oleDeliverRequestBos = getLoanProcessor().getPatronRequestRecords(patronId);
            OleDeliverRequestDefinition oleDeliverRequestDefinition = new OleDeliverRequestDefinition();
            for(OleDeliverRequestBo oleDeliverRequestBo : oleDeliverRequestBos) {
             oleDeliverRequestDefinition = OleDeliverRequestBo.to(oleDeliverRequestBo);
                oleDeliverRequestDefinitions.add(oleDeliverRequestDefinition);
            }
            return oleDeliverRequestDefinitions;
        }
        catch(Exception e){
            LOG.error("Exception while getting patron requested items----->  "+e);
        }

        return  null;
    }



    public OlePatronLoanDocuments performRenewalItems(OlePatronLoanDocuments olePatronLoanDocuments){

        List<OleLoanDocument> oleLoanDocumentList=new ArrayList<OleLoanDocument>(0);
        List<OleRenewalLoanDocument>  oleRenewalLoanDocumentList=convertRenewalLoanDocuments(olePatronLoanDocuments);
        try{

            for(int i=0;i<oleRenewalLoanDocumentList.size();i++){
                OleRenewalLoanDocument oleRenewalLoanDocument=oleRenewalLoanDocumentList.get(i);
                OleLoanDocument oleLoanDocument=getLoanProcessor().getPatronRenewalItem(oleRenewalLoanDocument.getItemBarcode());
                if(!getLoanProcessor().checkPendingRequestforItem(oleLoanDocument.getItemId())){
                        oleLoanDocument.setRenewalItemFlag(true);
                        oleLoanDocument = getLoanProcessor().addLoan(oleLoanDocument.getPatronBarcode(),oleLoanDocument.getItemId(),oleLoanDocument,null);
                }
                else
                  oleLoanDocument.setErrorMessage(OLEConstants.PENDING_RQST_RENEWAL_ITM_INFO+"( Title: "+oleLoanDocument.getTitle()+" , Author: "+oleLoanDocument.getAuthor()+" , Item : "+oleLoanDocument.getItemId()+" )");
                oleLoanDocumentList.add(oleLoanDocument);
            }
            olePatronLoanDocuments=convertPatronLoanDocuments(oleLoanDocumentList);
            return olePatronLoanDocuments;
        }
        catch(Exception e){
            LOG.error("Exception while performing renewal---> "+e);
        }
        return null;
    }


    private OlePatronLoanDocuments convertPatronLoanDocuments(List<OleLoanDocument> oleLoanDocumentList) {


        List<OlePatronLoanDocument> olePatronLoanItemList=new ArrayList<OlePatronLoanDocument>();

        for(int i=0;i<oleLoanDocumentList.size();i++)  {
            OleLoanDocument oleLoanDocument=oleLoanDocumentList.get(i);
            OleRenewalLoanDocument oleRenewalLoanDocument=new OleRenewalLoanDocument();
            oleRenewalLoanDocument.setAuthor(oleLoanDocument.getAuthor());
            oleRenewalLoanDocument.setTitle(oleLoanDocument.getTitle());
            oleRenewalLoanDocument.setCallNumber(oleLoanDocument.getItemCallNumber());
            oleRenewalLoanDocument.setDueDate(oleLoanDocument.getLoanDueDate());
            oleRenewalLoanDocument.setItemBarcode(oleLoanDocument.getItemId());
            oleRenewalLoanDocument.setLocation(oleLoanDocument.getLocation());

            if(oleLoanDocument.getErrorMessage() == null){
                oleRenewalLoanDocument.setMessageInfo(OLEConstants.RENEWAL_ITM_SUCCESS_INFO);
            }else
            {
                String errMsg="";
                if(oleLoanDocument.getErrorMessage().contains("(OR)"))
                   errMsg=oleLoanDocument.getErrorMessage().substring(0,oleLoanDocument.getErrorMessage().lastIndexOf("(OR)"));
                else
                   errMsg= oleLoanDocument.getErrorMessage();
                oleRenewalLoanDocument.setMessageInfo(errMsg);
            }
            OlePatronLoanDocument olePatronLoanDocument=OlePatronLoanDocuments.to(oleRenewalLoanDocument);
            olePatronLoanItemList.add(olePatronLoanDocument);
        }
        OlePatronLoanDocuments olePatronLoanDocuments=getOlePatronLoanDocuments(olePatronLoanItemList);
        return olePatronLoanDocuments;

    }


    private List<OleRenewalLoanDocument>  convertRenewalLoanDocuments(OlePatronLoanDocuments olePatronLoanDocuments){

        List<OleRenewalLoanDocument> oleRenewalLoanDocumentList=new ArrayList<OleRenewalLoanDocument>();
        for(int i=0;i<olePatronLoanDocuments.getOlePatronLoanDocuments().size();i++){
            OlePatronLoanDocument olePatronLoanDocument=(OlePatronLoanDocument)olePatronLoanDocuments.getOlePatronLoanDocuments().get(i);
            OleRenewalLoanDocument oleRenewalLoanDocument=new OleRenewalLoanDocument();
            oleRenewalLoanDocument.setItemBarcode(olePatronLoanDocument.getItemBarcode());
            oleRenewalLoanDocument.setCallNumber(olePatronLoanDocument.getCallNumber());
            oleRenewalLoanDocument.setDueDate(olePatronLoanDocument.getDueDate());
            oleRenewalLoanDocument.setLocation(olePatronLoanDocument.getLocation());
            oleRenewalLoanDocument.setTitle(olePatronLoanDocument.getTitle());
            oleRenewalLoanDocument.setAuthor(olePatronLoanDocument.getAuthor());
            oleRenewalLoanDocumentList.add(oleRenewalLoanDocument);
        }
        return oleRenewalLoanDocumentList;
    }

    private  OlePatronLoanDocuments convertOlePatronLoanDocuments(List<OleRenewalLoanDocument> oleRenewalLoanDocumentList){
        List<OlePatronLoanDocument> olePatronLoanItemList=new ArrayList<OlePatronLoanDocument>();
        for(int i=0;i<oleRenewalLoanDocumentList.size();i++){
            OleRenewalLoanDocument oleRenewalLoanDocument= oleRenewalLoanDocumentList.get(i);
            OlePatronLoanDocument olePatronLoanDocument=OlePatronLoanDocuments.to(oleRenewalLoanDocument);
            olePatronLoanItemList.add(olePatronLoanDocument);
        }
        OlePatronLoanDocuments olePatronLoanDocuments=getOlePatronLoanDocuments(olePatronLoanItemList);
        return olePatronLoanDocuments;
    }

    private OlePatronLoanDocuments  getOlePatronLoanDocuments(List<OlePatronLoanDocument> olePatronLoanItemList) {
        OleRenewalLoanDocument oleRenewalLoanDocument=new OleRenewalLoanDocument();
        if(olePatronLoanItemList.size()!=0) {
            oleRenewalLoanDocument.setOlePatronLoanDocuments(olePatronLoanItemList);
            OlePatronLoanDocuments olePatronLoanDocuments=OlePatronLoanDocuments.Builder.create(oleRenewalLoanDocument).build();
            return olePatronLoanDocuments;
        }

        return null;

    }



    public void deletePatronBatchProgram() {
        LOG.debug("Inside deletePatronDocument (Batch Delete Program)");
        OlePatronDocument olePatronDocument = new OlePatronDocument();
        SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OlePatron.PATRON_MAINTENANCE_DATE_FORMAT);
        OlePatron olePatron;
        List<OlePatronDocument> patronImpls = (List<OlePatronDocument>) getBusinessObjectService().findAll(OlePatronDocument.class);
        for (Iterator<OlePatronDocument> patronIterator = patronImpls.iterator(); patronIterator.hasNext(); ) {
            olePatronDocument = patronIterator.next();
            if ((olePatronDocument.getExpirationDate() != null && fmt.format(new Date(System.currentTimeMillis())).compareTo(fmt.format(olePatronDocument.getExpirationDate())) > 0) || (olePatronDocument.getExpirationDate() == null)) {
                List<OleLoanDocument> oleLoanDocuments = olePatronDocument.getOleLoanDocuments();
                List<OleTemporaryCirculationHistory> oleTemporaryCirculationHistories = olePatronDocument.getOleTemporaryCirculationHistoryRecords();
                List<OleDeliverRequestBo> oleDeliverRequestBos = olePatronDocument.getOleDeliverRequestBos();
                Map billMap = new HashMap();
                billMap.put(OLEConstants.OlePatron.PAY_BILL_PATRON_ID, olePatronDocument.getOlePatronId());
                List<PatronBillPayment> patronBillPayments = (List<PatronBillPayment>) KRADServiceLocator.getBusinessObjectService().findMatching(PatronBillPayment.class, billMap);
                if ((oleLoanDocuments.size() == 0 && oleTemporaryCirculationHistories.size() == 0 && oleDeliverRequestBos.size() == 0 && patronBillPayments.size() == 0)) {
                    olePatronDocument.setActiveIndicator(false);
                    KRADServiceLocator.getBusinessObjectService().save(olePatronDocument);
                }
            }
        }
    }

}