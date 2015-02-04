package org.kuali.ole.service;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.ole.deliver.bo.*;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.service.UiDocumentService;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OlePatronHelperServiceImpl converts PatronProperties to EntityProperties and generate new search criteria.
 */
public class OlePatronHelperServiceImpl extends LookupableImpl implements OlePatronHelperService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronHelperServiceImpl.class);

    protected static final String ENTITY_EMAIL_PROPERTY_PREFIX = "entity.entityTypeContactInfos.emailAddresses.";
    protected static final String ENTITY_NAME_PROPERTY_PREFIX = "entity.names.";
    protected static final String BILL_ENTITY_NAME_PROPERTY_PREFIX = "olePatron.entity.names.";
    private BusinessObjectService businessObjectService;
    protected Map<String, String> criteriaConversion = new HashMap<String, String>();

    {
        criteriaConversion.put(KIMPropertyConstants.Person.FIRST_NAME, "entity.names.firstName");
        criteriaConversion.put(KIMPropertyConstants.Person.MIDDLE_NAME, "entity.names.middleName");
        criteriaConversion.put(KIMPropertyConstants.Person.LAST_NAME, "entity.names.lastName");
        criteriaConversion.put(KIMPropertyConstants.Person.EMAIL_ADDRESS, "entity.entityTypeContactInfos.emailAddresses.emailAddress");
    }

    protected Map<String, String> billCriteriaConversion = new HashMap<String, String>();

    {
        billCriteriaConversion.put(KIMPropertyConstants.Person.FIRST_NAME, "olePatron.entity.names.firstName");
        billCriteriaConversion.put(KIMPropertyConstants.Person.LAST_NAME, "olePatron.entity.names.lastName");
        billCriteriaConversion.put(KIMPropertyConstants.Person.EMAIL_ADDRESS, "olePatron.entity.entityTypeContactInfos.emailAddresses.emailAddress");
    }



    public EntityBo editAndSaveEntityBo(OlePatronDocument patronDocument) {
        LOG.debug("Inside the editAndSaveEntityBo method");
        EntityBo kimEntity;
        List<EntityTypeContactInfoBo> entityTypes;
        EntityTypeContactInfoBo entityType;
        kimEntity = patronDocument.getEntity();
        if (kimEntity.getId() == null) {
            String kimEntityId=KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("krim_entity_id_s").toString();
            kimEntity.setId(kimEntityId);
            entityTypes = new ArrayList<EntityTypeContactInfoBo>();
            entityType = new EntityTypeContactInfoBo();
            kimEntity.setActive(true);
            entityType.setEntityTypeCode(KimConstants.EntityTypes.PERSON);
            entityType.setActive(true);
            entityType.setEntityId(kimEntity.getId());
        } else {
            kimEntity.setActive(true);
            entityTypes = kimEntity.getEntityTypeContactInfos();
            if (!entityTypes.isEmpty()) {
                entityType = entityTypes.get(0);
                entityType.setActive(true);
            } else {
                entityType = new EntityTypeContactInfoBo();
                entityType.setEntityTypeCode(KimConstants.EntityTypes.PERSON);
            }
        }

        List<EntityAffiliationBo> affiliationBoList = new ArrayList<EntityAffiliationBo>();
        List<EntityEmploymentBo> employeeList = new ArrayList<EntityEmploymentBo>();
        String affiliationSeq = null;
        if (patronDocument.getPatronAffiliations().size() > 0) {
            for (OlePatronAffiliation patronAffiliation : patronDocument.getPatronAffiliations()) {
                patronAffiliation.setEntityId(kimEntity.getId());
                EntityAffiliationBo entityAffiliationBo = patronAffiliation.getEntityAffliationBo();
                if (entityAffiliationBo.getId() == null) {
                    affiliationSeq = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("krim_entity_afltn_id_s").toString();
                    entityAffiliationBo.setId(affiliationSeq);
                }
                for (EntityEmploymentBo employmentBo : patronAffiliation.getEmployments()) {
                    employmentBo.setEntityAffiliation(entityAffiliationBo);
                    employmentBo.setEntityAffiliationId(entityAffiliationBo.getId());
                    employeeList.add(employmentBo);
                }
                affiliationBoList.add(entityAffiliationBo);
            }
        }
        kimEntity.setAffiliations(affiliationBoList);
        kimEntity.setEmploymentInformation(employeeList);
        kimEntity.setNames(Arrays.asList(patronDocument.getName()));
        List<EntityAddressBo> addressList = new ArrayList<EntityAddressBo>();
        if (patronDocument.getOleEntityAddressBo().size() > 0) {
            for (OleEntityAddressBo oleEntityAddress : patronDocument.getOleEntityAddressBo()) {
                if(oleEntityAddress.getEntityAddressBo()!=null && oleEntityAddress.getEntityAddressBo().getAddressType()!=null){
                    oleEntityAddress.getEntityAddressBo().setAddressTypeCode(oleEntityAddress.getEntityAddressBo().getAddressType().getCode());
                }
                addressList.add(oleEntityAddress.getEntityAddressBo());
            }
        }
        patronDocument.setEmployments(employeeList);
        entityTypes.clear();
        entityType.setAddresses(addressList);
        entityType.setEmailAddresses(patronDocument.getEmails());
        entityType.setPhoneNumbers(patronDocument.getPhones());
        entityTypes.add(entityType);
        kimEntity.setEntityTypeContactInfos(entityTypes);
        try {
          patronDocument.getIdentityService().updateEntity(EntityBo.to(kimEntity));
        } catch (Exception e) {
            LOG.error("Unable to save edited patron Record" + e);
        }

        return kimEntity;
    }

    public EntityBo copyAndSaveEntityBo(OlePatronDocument patronDocument) {
        LOG.debug("Inside the copyAndSaveEntityBo method");

        EntityBo kimEntity;
        List<EntityTypeContactInfoBo> entityTypes;
        EntityTypeContactInfoBo entityType;
        kimEntity = patronDocument.getEntity();
        kimEntity.setId(null);
        kimEntity.setObjectId(null);
        kimEntity.setVersionNumber(null);
        boolean isUpdatePatron=false;
        if (kimEntity.getId() == null) {
            String kimEntityId=KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("krim_entity_id_s").toString();
            kimEntity.setId(kimEntityId);
            entityTypes = new ArrayList<EntityTypeContactInfoBo>();
            entityType = new EntityTypeContactInfoBo();
            kimEntity.setActive(true);
            entityType.setEntityTypeCode(KimConstants.EntityTypes.PERSON);
            entityType.setActive(true);
            entityType.setEntityId(kimEntity.getId());
        } else {
            isUpdatePatron=true;
            kimEntity.setActive(true);
            entityTypes = kimEntity.getEntityTypeContactInfos();
            if (!entityTypes.isEmpty()) {
                entityType = entityTypes.get(0);
                entityType.setActive(true);
            } else {
                entityType = new EntityTypeContactInfoBo();
                entityType.setEntityTypeCode(KimConstants.EntityTypes.PERSON);
            }
            entityType.setEntityId(kimEntity.getId());
        }

        List<EntityAffiliationBo> affiliationBoList = new ArrayList<EntityAffiliationBo>();
        List<EntityEmploymentBo> employeeList = new ArrayList<EntityEmploymentBo>();
        String affiliationSeq = null;
        if (patronDocument.getPatronAffiliations().size() > 0) {
            for (OlePatronAffiliation patronAffiliation : patronDocument.getPatronAffiliations()) {
                EntityAffiliationBo entityAffiliationBo = patronAffiliation.getEntityAffliationBo();
                entityAffiliationBo.setEntityId(kimEntity.getId());
                entityAffiliationBo.setObjectId(null);
                entityAffiliationBo.setVersionNumber(null);
                if (entityAffiliationBo.getId() == null) {
                    affiliationSeq = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("krim_entity_afltn_id_s").toString();
                    entityAffiliationBo.setId(affiliationSeq);
                }
                for (EntityEmploymentBo employmentBo : patronAffiliation.getEmployments()) {
                    employmentBo.setEntityAffiliation(entityAffiliationBo);
                    employmentBo.setEntityAffiliationId(entityAffiliationBo.getId());
                    employeeList.add(employmentBo);
                }
                affiliationBoList.add(entityAffiliationBo);
            }
        }
        kimEntity.setAffiliations(affiliationBoList);
        kimEntity.setEmploymentInformation(employeeList);
        if (patronDocument.getName() != null) {
            patronDocument.getName().setId(null);
            patronDocument.getName().setVersionNumber(null);
            patronDocument.getName().setObjectId(null);
            patronDocument.getName().setEntityId(null);
        }
        kimEntity.setNames(Arrays.asList(patronDocument.getName()));

        List<EntityAddressBo> addressList = new ArrayList<EntityAddressBo>();
        if (patronDocument.getOleEntityAddressBo() != null) {
            for (OleEntityAddressBo oleEntityAddress : patronDocument.getOleEntityAddressBo()) {
                oleEntityAddress.getEntityAddressBo().setId(null);
                oleEntityAddress.getEntityAddressBo().setVersionNumber(null);
                oleEntityAddress.getEntityAddressBo().setObjectId(null);
                oleEntityAddress.getEntityAddressBo().setEntityId(kimEntity.getId());
                if(oleEntityAddress.getEntityAddressBo()!=null && oleEntityAddress.getEntityAddressBo().getAddressType()!=null){
                    oleEntityAddress.getEntityAddressBo().setAddressTypeCode(oleEntityAddress.getEntityAddressBo().getAddressType().getCode());
                }
                addressList.add(oleEntityAddress.getEntityAddressBo());
            }
        }
        List<EntityPhoneBo> entityPhoneBos = new ArrayList<EntityPhoneBo>();
        if (patronDocument.getPhones() != null) {
            for (EntityPhoneBo entityPhoneBo : patronDocument.getPhones()) {
                entityPhoneBo.setObjectId(null);
                entityPhoneBo.setVersionNumber(null);
                entityPhoneBo.setId(null);
                if(entityPhoneBo.getPhoneType()!=null){
                    entityPhoneBo.setPhoneTypeCode(entityPhoneBo.getPhoneType().getCode());
                }
                entityPhoneBo.setEntityId(kimEntity.getId());
                entityPhoneBos.add(entityPhoneBo);
            }
        }
        List<EntityEmailBo> entityEmailBos = new ArrayList<>();
        if (patronDocument.getEmails() != null) {
            for (EntityEmailBo emailBo : patronDocument.getEmails()) {
                emailBo.setObjectId(null);
                emailBo.setVersionNumber(null);
                emailBo.setId(null);
                if(emailBo.getEmailType()!=null){
                    emailBo.setEmailTypeCode(emailBo.getEmailType().getCode());
                }
                emailBo.setEntityId(kimEntity.getId());
                entityEmailBos.add(emailBo);
            }
        }
        entityType.setAddresses(addressList);
        entityType.setEmailAddresses(entityEmailBos);
        entityType.setPhoneNumbers(entityPhoneBos);
        entityTypes.add(entityType);
        kimEntity.setEntityTypeContactInfos(entityTypes);
        try {
            if (isUpdatePatron) {
                Entity entity = patronDocument.getIdentityService().updateEntity(EntityBo.to(kimEntity));
            } else {
                Entity entity = patronDocument.getIdentityService().createEntity(EntityBo.to(kimEntity));
            }

        } catch (Exception e) {
            LOG.error("Copied to save edited patron Record" + e);
        }
        return kimEntity;
    }

    public List<OleAddressBo> retrieveOleAddressBo(EntityBo entityBo, OlePatronDocument patronDocument) {
        List<EntityAddressBo> addressBos = entityBo.getEntityTypeContactInfos().get(0).getAddresses();
        List<OleAddressBo> oleAddressBos = new ArrayList<OleAddressBo>();
        for (int i = 0; i < addressBos.size(); i++) {
            EntityAddressBo entityAddressBo = addressBos.get(i);
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(OLEConstants.OlePatron.ENTITY_BO_ID, entityAddressBo.getId());
            OleAddressBo oleAddressBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleAddressBo.class, criteria);
            if (oleAddressBo == null) {
                oleAddressBo = new OleAddressBo();
            }
            oleAddressBo.setOlePatronId(entityBo.getId());
            oleAddressBo.setOleAddressId(KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("OLE_DLVR_ADD_S").toString());
            oleAddressBo.setEntityAddress(entityAddressBo);
            oleAddressBo.setAddressValidFrom(patronDocument.getOleEntityAddressBo().get(i).getOleAddressBo().getAddressValidFrom());
            oleAddressBo.setAddressValidTo(patronDocument.getOleEntityAddressBo().get(i).getOleAddressBo().getAddressValidTo());
            oleAddressBo.setAddressVerified(patronDocument.getOleEntityAddressBo().get(i).getOleAddressBo().isAddressVerified());
            oleAddressBo.setAddressSource(patronDocument.getOleEntityAddressBo().get(i).getOleAddressBo().getAddressSource());
            oleAddressBo.setId(patronDocument.getOleEntityAddressBo().get(i).getOleAddressBo().getId());
            oleAddressBos.add(oleAddressBo);

        }
        return oleAddressBos;
    }


    /**
     * Check whether name entity is present in search criteria
     *
     * @param propertyName
     * @return boolean
     */
    protected boolean isNameEntityCriteria(String propertyName) {
        LOG.debug("Inside the isNameEntityCriteria method");
        return propertyName.startsWith(ENTITY_NAME_PROPERTY_PREFIX);
    }

    /**
     * Check whether email entity is present in search criteria
     *
     * @param propertyName
     * @return boolean
     */
    protected boolean isEmailEntityCriteria(String propertyName) {
        LOG.debug("Inside the isEmailEntityCriteria method");
        return propertyName.startsWith(ENTITY_EMAIL_PROPERTY_PREFIX);
    }


    public boolean deletePatron(OlePatronDocument olePatronDocument) {
        LOG.debug("Inside the deletePatron method");
        boolean deleteFlag = false;
        SimpleDateFormat fmt = new SimpleDateFormat(OLEConstants.OlePatron.PATRON_MAINTENANCE_DATE_FORMAT);
        Map billMap = new HashMap();
        billMap.put(OLEConstants.OlePatron.PAY_BILL_PATRON_ID, olePatronDocument.getOlePatronId());
        List<PatronBillPayment> patronBillPayments = (List<PatronBillPayment>) KRADServiceLocator.getBusinessObjectService().findMatching(PatronBillPayment.class, billMap);

        if (olePatronDocument != null && olePatronDocument.getOlePatronId() != null) {
            if (olePatronDocument.getOleLoanDocuments() == null || olePatronDocument.getOleLoanDocuments().size() == 0) {
                if ((olePatronDocument.getOleTemporaryCirculationHistoryRecords() == null || olePatronDocument.getOleTemporaryCirculationHistoryRecords().size() == 0)) {
                    if ((olePatronDocument.getOleDeliverRequestBos() == null || olePatronDocument.getOleDeliverRequestBos().size() == 0)) {
                        if ((patronBillPayments == null || patronBillPayments.size() == 0)) {
                            olePatronDocument.setActiveIndicator(false);
                            List<OleAddressBo> addressBos = olePatronDocument.getOleAddresses();
                            if (addressBos.size() > 0) {
                                for (OleAddressBo addressBo : addressBos) {
                                    if (addressBo.getAddressSource().isEmpty() || "".equals(addressBo.getAddressSource())) {
                                        addressBo.setAddressSource(null);
                                    }
                                }
                            }
                            KRADServiceLocator.getBusinessObjectService().save(olePatronDocument);
                            deleteFlag = true;
                        }
                    }
                }
            }
        }
        return deleteFlag;
    }

    /**
     * This method is for checking the added address source or not
     *
     * @param oleAddresses
     * @return true , if the address source has value in a single patron record, else false
     */
    public boolean checkAddressSource(List<OleAddressBo> oleAddresses) {

        boolean valid = true;
        for (OleAddressBo oleAddress : oleAddresses) {
            if ((oleAddress.getAddressSource() != null) && (oleAddress.getAddressSource().equals(""))) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * This method is for checking the entity phone has multiple defaults
     *
     * @param phoneBoList
     * @return true , if the entity phone has only one default in a single patron record, else false
     */
    public boolean checkPhoneMultipleDefault(List<EntityPhoneBo> phoneBoList) {
        boolean valid = true;
        boolean isDefaultSet = false;
        int i = 0;
        for (EntityPhoneBo phone : phoneBoList) {
            if (phone.isDefaultValue()) {
                if (isDefaultSet) {
                    valid = false;
                } else {
                    isDefaultSet = true;
                }
            }
            i++;
        }
        if (!phoneBoList.isEmpty() && !isDefaultSet) {
            valid = false;
        }
        return valid;
    }

    /**
     * This method is for checking the entity address has multiple defaults
     *
     * @param addrBoList
     * @return true , if the entity address has only one default in a single patron record, else false
     */
    public boolean checkAddressMultipleDefault(List<OleEntityAddressBo> addrBoList) {
        boolean valid = true;
        boolean isDefaultSet = false;
        int i = 0;
        for (OleEntityAddressBo addr : addrBoList) {
            EntityAddressBo entityAddressBo = addr.getEntityAddressBo();
            if (entityAddressBo.isDefaultValue()) {
                if (isDefaultSet) {
                    valid = false;
                } else {
                    isDefaultSet = true;
                }
            }
            i++;
        }
        if (!addrBoList.isEmpty() && !isDefaultSet) {
            valid = false;
        }
        return valid;
    }

    /**
     * This method is for checking the entity email address has multiple defaults
     *
     * @param emailBoList
     * @return true , if the entity email address has only one default in a single patron record, else false
     */
    public boolean checkEmailMultipleDefault(List<EntityEmailBo> emailBoList) {
        boolean valid = true;
        boolean isDefaultSet = false;
        int i = 0;
        for (EntityEmailBo email : emailBoList) {
            if (email.isDefaultValue()) {
                if (isDefaultSet) {
                    valid = false;
                } else {
                    isDefaultSet = true;
                }
            }
            i++;
        }
        if (!emailBoList.isEmpty() && !isDefaultSet) {
            valid = false;
        }
        return valid;
    }

    public boolean isBorrowerTypeActive(OlePatronDocument olePatronDocument) {
        boolean isPatronTypeActive = false;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(OLEConstants.BORROWER_TYPE_ID, olePatronDocument.getBorrowerType());
        List<OleBorrowerType> borrowerTypes = (List<OleBorrowerType>) getBusinessObjectService().findMatching(OleBorrowerType.class, map);
        if (borrowerTypes.size() > 0) {
            if (borrowerTypes.get(0).isActive()) {
                isPatronTypeActive = true;
            }
        }
        return isPatronTypeActive;
    }

    public boolean validatePatron(OlePatronDocument patronDocument) {
        boolean isValid = true;
        isValid &= validateEntityInformation(patronDocument);
        isValid &= validateAddress(patronDocument, "oleEntityAddressBo");
        isValid &= validateRequiredField(patronDocument);
        isValid &=checkDuplicateBarcode(patronDocument);
        return isValid;
    }

    private boolean validateEntityInformation( OlePatronDocument patronDoc) {
        boolean valid = true;
        List<OleEntityAddressBo> addressBoList = patronDoc.getOleEntityAddressBo();
        List<EntityEmailBo> emailBoList = patronDoc.getEmails();
        List<EntityPhoneBo> phoneBoList = patronDoc.getPhones();
        if (addressBoList.size() == 1) {
            OleEntityAddressBo oleEntityAddressBo = addressBoList.get(0);
            oleEntityAddressBo.getEntityAddressBo().setDefaultValue(true);
        }
        if (emailBoList.size() == 1) {
            EntityEmailBo entityEmailBo = emailBoList.get(0);
            entityEmailBo.setDefaultValue(true);
        }
        if (phoneBoList.size() == 1) {
            EntityPhoneBo entityPhoneBo = phoneBoList.get(0);
            entityPhoneBo.setDefaultValue(true);
        }
        if (!checkPhoneMultipleDefault(patronDoc.getPhones())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OlePatron.PHONE_SECTION_ID, OLEConstants.OlePatron.ERROR_SELECTION_PREFERRED_PHONE);
            valid &= false;
        }
        if (!checkAddressMultipleDefault(patronDoc.getOleEntityAddressBo())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OlePatron.ADDRESS_SECTION_ID, OLEConstants.OlePatron.ERROR_SELECTION_PREFERRED_ADDRESS);
            valid &= false;
        }
        if (!checkEmailMultipleDefault(patronDoc.getEmails())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OlePatron.EMAIL_SECTION_ID, OLEConstants.OlePatron.ERROR_SELECTION_PREFERRED_EMAIL);
            valid &= false;
        }
        return valid;
    }

    public boolean validateRequiredField(OlePatronDocument olePatronDocument) {
        boolean valid = true;
        List<OleEntityAddressBo> addressBoList = olePatronDocument.getOleEntityAddressBo();
        List<EntityEmailBo> emailBoList = olePatronDocument.getEmails();
        if (!(addressBoList.size() > 0 || emailBoList.size() > 0)) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.ERROR_PATRON_REQUIRED_ADDRESS);
            valid = false;
        }
        return valid;
    }

    public boolean validateAddress(OlePatronDocument olePatronDocument, String addressBos) {
        List<OleEntityAddressBo> addressBoList = olePatronDocument.getOleEntityAddressBo();
        OleEntityAddressBo oleEntityAddressBo;
        boolean valid = true;
        boolean flag=true;
        boolean dateExist = true;
        Map<Date,Date> map=new HashMap<>();
        for (int i = 0; i < addressBoList.size(); i++) {
            oleEntityAddressBo = addressBoList.get(i);
            if (oleEntityAddressBo.getEntityAddressBo().isDefaultValue()) {
                if (oleEntityAddressBo.getOleAddressBo().getAddressValidFrom() != null || oleEntityAddressBo.getOleAddressBo().getAddressValidTo() != null) {
                    GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_ADDRESS_DEFAULT_DATE);
                    flag= false;
                }
            }
            if(oleEntityAddressBo.getOleAddressBo().getAddressValidFrom()!=null && oleEntityAddressBo.getOleAddressBo().getAddressValidTo()!=null && oleEntityAddressBo.getOleAddressBo().getAddressValidFrom().compareTo(oleEntityAddressBo.getOleAddressBo().getAddressValidTo())>0){
                GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_VALID_ADDRESS_TO_DATE);
                flag= false;
            }

            if(!oleEntityAddressBo.getEntityAddressBo().isDefaultValue()){
                if(oleEntityAddressBo.getOleAddressBo().getAddressValidFrom()==null && oleEntityAddressBo.getOleAddressBo().getAddressValidTo()!=null){
                    GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_REQUIRED_ADDRESS_FROM_DATE, "ValidFrom");
                    flag= false;
                }
                if(oleEntityAddressBo.getOleAddressBo().getAddressValidTo()==null && oleEntityAddressBo.getOleAddressBo().getAddressValidFrom()!=null){
                    GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_REQUIRED_ADDRESS_FROM_DATE, "ValidTo");
                    flag= false;
                }
                for(Map.Entry<Date,Date> entry:map.entrySet()){
                    if(entry.getKey()!=null&&entry.getValue()!=null&&oleEntityAddressBo.getOleAddressBo().getAddressValidFrom()!=null&&oleEntityAddressBo.getOleAddressBo().getAddressValidTo()!=null){
                        if(oleEntityAddressBo.getOleAddressBo().getAddressValidFrom().compareTo(entry.getKey())>=0 && oleEntityAddressBo.getOleAddressBo().getAddressValidFrom().compareTo(entry.getValue())<=0
                                || oleEntityAddressBo.getOleAddressBo().getAddressValidTo().compareTo(entry.getKey())>=0 && oleEntityAddressBo.getOleAddressBo().getAddressValidTo().compareTo(entry.getValue())<=0){
                            GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue",OLEConstants.OlePatron.ERROR_PATRON_ADDRESS_FROM_TO_DATE_OVERLAP);
                            flag= false;
                        }
                        if(entry.getKey().compareTo(oleEntityAddressBo.getOleAddressBo().getAddressValidFrom())>=0 && entry.getKey().compareTo(oleEntityAddressBo.getOleAddressBo().getAddressValidTo())<=0
                                || entry.getValue().compareTo(oleEntityAddressBo.getOleAddressBo().getAddressValidFrom())>=0 && entry.getValue().compareTo(oleEntityAddressBo.getOleAddressBo().getAddressValidTo())<=0){
                            GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_ADDRESS_FROM_TO_DATE_OVERLAP);
                            flag= false;
                        }
                    }
                }
            }else if (oleEntityAddressBo.getOleAddressBo().getAddressValidFrom() != null || oleEntityAddressBo.getOleAddressBo().getAddressValidTo() != null) {

                if (dateExist) {
                    dateExist = false;
                } else {
                    GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_ADDRESS_SINGLE_DATE);
                    flag= false;
                }
            }
            map.put(oleEntityAddressBo.getOleAddressBo().getAddressValidFrom(),oleEntityAddressBo.getOleAddressBo().getAddressValidTo());
        }
        if(!flag){
            return false;
        }
        return valid;
    }

    private UiDocumentService uiDocumentService;
    /**
     * Gets the value of uiDocumentService property
     *
     * @return uiDocumentService(UiDocumentService)
     */
    public UiDocumentService getUIDocumentService() {
        if (uiDocumentService == null) {
            uiDocumentService = KIMServiceLocatorInternal.getUiDocumentService();
        }
        return uiDocumentService;
    }

    protected boolean checkDuplicateBarcode(OlePatronDocument patronDoc) {
        boolean valid = true;
        Map barcodeMap = new HashMap();
        barcodeMap.put(OLEConstants.OlePatron.BARCODE, patronDoc.getBarcode());
        List<OlePatronDocument> olePatronDocuments = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, barcodeMap);
        if (olePatronDocuments.size() > 0) {
            for (OlePatronDocument olePatronDocument : olePatronDocuments) {
                if (patronDoc.getOlePatronId() == null || !(patronDoc.getOlePatronId().equals(olePatronDocument.getOlePatronId()))) {
                    GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.PATRON_DUPLICATE_BARCODE);
                    valid = false;
                }
            }
        }
        return valid;
    }

    /**
     * Gets the instance of BusinessObjectService
     *
     * @return businessObjectService(BusinessObjectService)
     */
    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }


}