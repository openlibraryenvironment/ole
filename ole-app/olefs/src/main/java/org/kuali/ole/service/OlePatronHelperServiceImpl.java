package org.kuali.ole.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.PatronBillPayment;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.controller.checkout.CircUtilController;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.service.impl.CheckoutReceiptNoticeExecutor;
import org.kuali.ole.deliver.util.ItemInfoUtil;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
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
public class OlePatronHelperServiceImpl  implements OlePatronHelperService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronHelperServiceImpl.class);

    protected static final String ENTITY_EMAIL_PROPERTY_PREFIX = "entity.entityTypeContactInfos.emailAddresses.";
    protected static final String ENTITY_NAME_PROPERTY_PREFIX = "entity.names.";
    protected static final String BILL_ENTITY_NAME_PROPERTY_PREFIX = "olePatron.entity.names.";
    private BusinessObjectService businessObjectService;
    private ParameterValueResolver parameterValueResolver;
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
        List<EntityPhoneBo> phoneList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(patronDocument.getOleEntityPhoneBo())) {
            for(OleEntityPhoneBo oleEntityPhoneBo : patronDocument.getOleEntityPhoneBo()) {
                if(oleEntityPhoneBo.getEntityPhoneBo() != null && oleEntityPhoneBo.getEntityPhoneBo().getPhoneType() != null) {
                    oleEntityPhoneBo.getEntityPhoneBo().setPhoneTypeCode(oleEntityPhoneBo.getEntityPhoneBo().getPhoneType().getCode());
                }
                phoneList.add(oleEntityPhoneBo.getEntityPhoneBo());
            }
        }
        List<EntityEmailBo> emailList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(patronDocument.getOleEntityEmailBo())) {
            for(OleEntityEmailBo oleEntityEmailBo : patronDocument.getOleEntityEmailBo()) {
                if(oleEntityEmailBo.getEntityEmailBo() != null && oleEntityEmailBo.getEntityEmailBo().getEmailType() != null) {
                    oleEntityEmailBo.getEntityEmailBo().setEmailTypeCode(oleEntityEmailBo.getEntityEmailBo().getEmailType().getCode());
                }
                emailList.add(oleEntityEmailBo.getEntityEmailBo());
            }
        }
        patronDocument.setEmployments(employeeList);
        entityTypes.clear();
        entityType.setAddresses(addressList);
        entityType.setEmailAddresses(emailList);
        entityType.setPhoneNumbers(phoneList);
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
        List<EntityPhoneBo> entityPhoneBoList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(patronDocument.getOleEntityPhoneBo())) {
            for(OleEntityPhoneBo oleEntityPhoneBo : patronDocument.getOleEntityPhoneBo()) {
                oleEntityPhoneBo.getEntityPhoneBo().setId(null);
                oleEntityPhoneBo.getEntityPhoneBo().setVersionNumber(null);
                oleEntityPhoneBo.getEntityPhoneBo().setObjectId(null);
                oleEntityPhoneBo.getEntityPhoneBo().setEntityId(kimEntity.getId());
                if(oleEntityPhoneBo.getEntityPhoneBo() != null && oleEntityPhoneBo.getEntityPhoneBo().getPhoneType() != null) {
                    oleEntityPhoneBo.getEntityPhoneBo().setPhoneTypeCode(oleEntityPhoneBo.getEntityPhoneBo().getPhoneType().getCode());
                }
                entityPhoneBoList.add(oleEntityPhoneBo.getEntityPhoneBo());
            }
        }
        List<EntityEmailBo> entityEmailBoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(patronDocument.getOleEntityEmailBo())) {
            for (OleEntityEmailBo oleEntityEmailBo : patronDocument.getOleEntityEmailBo()) {
                oleEntityEmailBo.getEntityEmailBo().setId(null);
                oleEntityEmailBo.getEntityEmailBo().setVersionNumber(null);
                oleEntityEmailBo.getEntityEmailBo().setObjectId(null);
                oleEntityEmailBo.getEntityEmailBo().setEntityId(kimEntity.getId());
                if(oleEntityEmailBo.getEntityEmailBo() != null && oleEntityEmailBo.getEntityEmailBo().getEmailType() != null) {
                    oleEntityEmailBo.getEntityEmailBo().setEmailTypeCode(oleEntityEmailBo.getEntityEmailBo().getEmailType().getCode());
                }
                entityEmailBoList.add(oleEntityEmailBo.getEntityEmailBo());
            }
        }
        entityType.setAddresses(addressList);
        entityType.setEmailAddresses(entityEmailBoList);
        entityType.setPhoneNumbers(entityPhoneBoList);
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
        List<EntityAddressBo> addressBos = new ArrayList<>();
        List<OleEntityAddressBo> oleEntityAddressBos = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(entityBo.getEntityTypeContactInfos())){
            addressBos = entityBo.getEntityTypeContactInfos().get(0).getAddresses();
        }
        boolean emptyOleEntityAddress = false;
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
            if (CollectionUtils.isNotEmpty(patronDocument.getOleEntityAddressBo()) && !emptyOleEntityAddress) {
                oleAddressBo.setAddressValidFrom(patronDocument.getOleEntityAddressBo().get(i).getOleAddressBo().getAddressValidFrom());
                oleAddressBo.setAddressValidTo(patronDocument.getOleEntityAddressBo().get(i).getOleAddressBo().getAddressValidTo());
                oleAddressBo.setAddressVerified(patronDocument.getOleEntityAddressBo().get(i).getOleAddressBo().isAddressVerified());
                oleAddressBo.setAddressSource(patronDocument.getOleEntityAddressBo().get(i).getOleAddressBo().getAddressSource());
                oleAddressBo.setId(patronDocument.getOleEntityAddressBo().get(i).getOleAddressBo().getId());
                oleAddressBo.setDeliverAddress(patronDocument.getOleEntityAddressBo().get(i).getOleAddressBo().isDeliverAddress());
            }else{
                emptyOleEntityAddress = true;
                OleEntityAddressBo oleEntityAddressBo = new OleEntityAddressBo();
                if(StringUtils.isBlank(oleAddressBo.getId())){
                    oleAddressBo.setId(entityAddressBo.getId());
                    if(CollectionUtils.isNotEmpty(patronDocument.getOleAddresses()) && patronDocument.getOleAddresses().size() > i && patronDocument.getOleAddresses().get(i) != null){
                        oleAddressBo.setAddressValidFrom(patronDocument.getOleAddresses().get(i).getAddressValidFrom());
                        oleAddressBo.setAddressValidTo(patronDocument.getOleAddresses().get(i).getAddressValidTo());
                        oleAddressBo.setAddressVerified(patronDocument.getOleAddresses().get(i).isAddressVerified());
                        oleAddressBo.setAddressSource(patronDocument.getOleAddresses().get(i).getAddressSource());
                        oleAddressBo.setDeliverAddress(patronDocument.getOleAddresses().get(i).isDeliverAddress());
                    }
                }
                oleEntityAddressBo.setOleAddressBo(oleAddressBo);
                oleEntityAddressBo.setEntityAddressBo(entityAddressBo);
                oleEntityAddressBos.add(oleEntityAddressBo);
            }
            oleAddressBos.add(oleAddressBo);

        }

        if(emptyOleEntityAddress){
            patronDocument.setOleEntityAddressBo(oleEntityAddressBos);
        }
        return oleAddressBos;
    }

    public List<OlePhoneBo> retrieveOlePhoneBo(EntityBo entityBo, OlePatronDocument patronDocument) {
        List<EntityPhoneBo> phoneBos = new ArrayList<>();
        List<OleEntityPhoneBo> oleEntityPhoneBos = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(entityBo.getEntityTypeContactInfos())){
            phoneBos = entityBo.getEntityTypeContactInfos().get(0).getPhoneNumbers();
        }
        boolean emptyOleEntityPhone = false;
        List<OlePhoneBo> olePhoneBos = new ArrayList<>();
        for (int i = 0; i < phoneBos.size(); i++) {
            EntityPhoneBo entityPhoneBo = phoneBos.get(i);
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(OLEConstants.OlePatron.ENTITY_BO_ID, entityPhoneBo.getId());
            OlePhoneBo olePhoneBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePhoneBo.class, criteria);
            if (olePhoneBo == null) {
                olePhoneBo = new OlePhoneBo();
            }
            olePhoneBo.setOlePatronId(entityBo.getId());
            olePhoneBo.setOlePhoneId(KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("OLE_DLVR_PHONE_S").toString());
            olePhoneBo.setEntityPhoneBo(entityPhoneBo);
            if (CollectionUtils.isNotEmpty(patronDocument.getOleEntityPhoneBo()) && !emptyOleEntityPhone) {
                olePhoneBo.setPhoneSource(patronDocument.getOleEntityPhoneBo().get(i).getOlePhoneBo().getPhoneSource());
                olePhoneBo.setId(patronDocument.getOleEntityPhoneBo().get(i).getOlePhoneBo().getId());
            }else{
                emptyOleEntityPhone = true;
                OleEntityPhoneBo oleEntityPhoneBo = new OleEntityPhoneBo();
                if(StringUtils.isBlank(olePhoneBo.getId())){
                    olePhoneBo.setId(entityPhoneBo.getId());
                    if(CollectionUtils.isNotEmpty(patronDocument.getOlePhones()) && patronDocument.getOlePhones().size() > i && patronDocument.getOlePhones().get(i) != null){
                        olePhoneBo.setPhoneSource(patronDocument.getOlePhones().get(i).getPhoneSource());
                    }
                }
                oleEntityPhoneBo.setOlePhoneBo(olePhoneBo);
                oleEntityPhoneBo.setEntityPhoneBo(entityPhoneBo);
                oleEntityPhoneBos.add(oleEntityPhoneBo);
            }
            olePhoneBos.add(olePhoneBo);

        }

        if(emptyOleEntityPhone){
            patronDocument.setOleEntityPhoneBo(oleEntityPhoneBos);
        }
        return olePhoneBos;
    }

    public List<OleEmailBo> retrieveOleEmailBo(EntityBo entityBo, OlePatronDocument patronDocument) {
        List<EntityEmailBo> emailBos = new ArrayList<>();
        List<OleEntityEmailBo> oleEntityEmailBos = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(entityBo.getEntityTypeContactInfos())){
            emailBos = entityBo.getEntityTypeContactInfos().get(0).getEmailAddresses();
        }
        boolean emptyOleEntityEmail = false;
        List<OleEmailBo> oleEmailBos = new ArrayList<>();
        for (int i = 0; i < emailBos.size(); i++) {
            EntityEmailBo entityEmailBo = emailBos.get(i);
            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put(OLEConstants.OlePatron.ENTITY_BO_ID, entityEmailBo.getId());
            OleEmailBo oleEmailBo = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleEmailBo.class, criteria);
            if (oleEmailBo == null) {
                oleEmailBo = new OleEmailBo();
            }
            oleEmailBo.setOlePatronId(entityBo.getId());
            oleEmailBo.setOleEmailId(KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("OLE_DLVR_EMAIL_S").toString());
            oleEmailBo.setEntityEmailBo(entityEmailBo);
            if (CollectionUtils.isNotEmpty(patronDocument.getOleEntityEmailBo()) && !emptyOleEntityEmail) {
                oleEmailBo.setEmailSource(patronDocument.getOleEntityEmailBo().get(i).getOleEmailBo().getEmailSource());
                oleEmailBo.setId(patronDocument.getOleEntityEmailBo().get(i).getOleEmailBo().getId());
            }else{
                emptyOleEntityEmail = true;
                OleEntityEmailBo oleEntityEmailBo = new OleEntityEmailBo();
                if(StringUtils.isBlank(oleEmailBo.getId())){
                    oleEmailBo.setId(entityEmailBo.getId());
                    if(CollectionUtils.isNotEmpty(patronDocument.getOleEmails()) && patronDocument.getOleEmails().size() > i && patronDocument.getOleEmails().get(i) != null){
                        oleEmailBo.setEmailSource(patronDocument.getOleEmails().get(i).getEmailSource());
                    }
                }
                oleEntityEmailBo.setOleEmailBo(oleEmailBo);
                oleEntityEmailBo.setEntityEmailBo(entityEmailBo);
                oleEntityEmailBos.add(oleEntityEmailBo);
            }
            oleEmailBos.add(oleEmailBo);

        }

        if(emptyOleEntityEmail){
            patronDocument.setOleEntityEmailBo(oleEntityEmailBos);
        }
        return oleEmailBos;
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
                if ((olePatronDocument.getOleTemporaryCirculationHistoryRecords() == null || olePatronDocument
                        .getOleTemporaryCirculationHistoryRecords().size() == 0)) {
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
                            List<OlePhoneBo> phoneBos = olePatronDocument.getOlePhones();
                            if(CollectionUtils.isNotEmpty(phoneBos)) {
                                for(OlePhoneBo phoneBo : phoneBos) {
                                    if(StringUtils.isBlank(phoneBo.getPhoneSource())) {
                                        phoneBo.setPhoneSource(null);
                                    }
                                }
                            }
                            List<OleEmailBo> emailBos = olePatronDocument.getOleEmails();
                            if(CollectionUtils.isNotEmpty(emailBos)) {
                                for(OleEmailBo emailBo : emailBos) {
                                    if(StringUtils.isBlank(emailBo.getEmailSource())) {
                                        emailBo.setEmailSource(null);
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

    public boolean checkPhoneSource(List<OlePhoneBo> olePhones) {

        boolean valid = true;
        for (OlePhoneBo olePhone : olePhones) {
            if (StringUtils.isBlank(olePhone.getPhoneSource())) {
                valid = false;
            }
        }
        return valid;
    }

    public boolean checkEmailSource(List<OleEmailBo> oleEmails) {

        boolean valid = true;
        for (OleEmailBo oleEmail : oleEmails) {
            if (StringUtils.isBlank(oleEmail.getEmailSource())) {
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
    public boolean checkPhoneMultipleDefault(List<OleEntityPhoneBo> phoneBoList) {
        boolean valid = true;
        boolean isDefaultSet = false;
        int i = 0;
        for (OleEntityPhoneBo phone : phoneBoList) {
            EntityPhoneBo entityPhoneBo = phone.getEntityPhoneBo();
            if (null != entityPhoneBo && entityPhoneBo.isDefaultValue()) {
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
    public boolean checkEmailMultipleDefault(List<OleEntityEmailBo> emailBoList) {
        boolean valid = true;
        boolean isDefaultSet = false;
        int i = 0;
        for (OleEntityEmailBo email : emailBoList) {
            EntityEmailBo entityEmailBo = email.getEntityEmailBo();
            if (null != entityEmailBo && entityEmailBo.isDefaultValue()) {
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
        List<OleEntityEmailBo> emailBoList = patronDoc.getOleEntityEmailBo();
        List<OleEntityPhoneBo> phoneBoList = patronDoc.getOleEntityPhoneBo();
        if (addressBoList.size() == 1) {
            OleEntityAddressBo oleEntityAddressBo = addressBoList.get(0);
            oleEntityAddressBo.getEntityAddressBo().setDefaultValue(true);
        }
        if (CollectionUtils.isNotEmpty(emailBoList) && emailBoList.size() == 1) {
            OleEntityEmailBo oleEntityEmailBo = emailBoList.get(0);
            oleEntityEmailBo.getEntityEmailBo().setDefaultValue(true);
        }
        if (CollectionUtils.isNotEmpty(phoneBoList) && phoneBoList.size() == 1) {
            OleEntityPhoneBo oleEntityPhoneBo = phoneBoList.get(0);
            oleEntityPhoneBo.getEntityPhoneBo().setDefaultValue(true);
        }
        if (!checkPhoneMultipleDefault(patronDoc.getOleEntityPhoneBo())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OlePatron.PHONE_SECTION_ID, OLEConstants.OlePatron.ERROR_SELECTION_PREFERRED_PHONE);
            valid &= false;
        }
        if (!checkAddressMultipleDefault(patronDoc.getOleEntityAddressBo())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OlePatron.ADDRESS_SECTION_ID, OLEConstants.OlePatron.ERROR_SELECTION_PREFERRED_ADDRESS);
            valid &= false;
        }
        if (!checkEmailMultipleDefault(patronDoc.getOleEntityEmailBo())) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OlePatron.EMAIL_SECTION_ID, OLEConstants.OlePatron.ERROR_SELECTION_PREFERRED_EMAIL);
            valid &= false;
        }
        if (!checkAddressMultipleDeliverAddress(patronDoc.getOleEntityAddressBo(), "oleEntityAddressBo")) {
            // GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OlePatron.ADDRESS_SECTION_ID, OLEConstants.OlePatron.ERROR_SELECTION_PREFERRED_DELIVER_ADDRESS);
            valid &= false;
        }
        return valid;
    }

    protected boolean checkAddressMultipleDeliverAddress(List<OleEntityAddressBo> addrBoList, String listName) {
        boolean valid = true;
        boolean isDefaultSet = false;
        boolean isAtleastOneChecked=false;
        int i = 0;
        for (OleEntityAddressBo addr : addrBoList) {
            OleAddressBo oleAddressBo = addr.getOleAddressBo();
            if (oleAddressBo.isDeliverAddress()) {
                isAtleastOneChecked=true;
                if (isDefaultSet) {
                    //this.putFieldError("dataObject." + listName + "[" + i + "].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_MULIT_DELIVER_ADDRESS);
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.OlePatron.ERROR_PATRON_MULIT_DELIVER_ADDRESS);
                    valid = false;
                } else {
                    isDefaultSet = true;
                }
            }
            i++;
        }
        if(!isAtleastOneChecked){
            valid=true;
        } else {
            if (!addrBoList.isEmpty() && !isDefaultSet) {
                //this.putFieldError("dataObject."+listName+"[0].defaultValue",RiceKeyConstants.ERROR_NO_DEFAULT_SELETION);
                valid = false;
            }
        }
        return valid;
    }

    public boolean validateRequiredField(OlePatronDocument olePatronDocument) {
        boolean valid = true;
        List<OleEntityAddressBo> addressBoList = olePatronDocument.getOleEntityAddressBo();
        List<OleEntityEmailBo> entityEmailBos = olePatronDocument.getOleEntityEmailBo();
        if ((!(addressBoList.size() > 0)) && (!(entityEmailBos.size() > 0))) {
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
            /*if (oleEntityAddressBo.getEntityAddressBo().isDefaultValue()) {
                if (oleEntityAddressBo.getOleAddressBo().getAddressValidFrom() != null || oleEntityAddressBo.getOleAddressBo().getAddressValidTo() != null) {
                    GlobalVariables.getMessageMap().putError("dataObject." + addressBos + "[0].defaultValue", OLEConstants.OlePatron.ERROR_PATRON_ADDRESS_DEFAULT_DATE);
                    flag= false;
                }
            }*/
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

    public String getPatronPreferredAddress(EntityTypeContactInfoBo entityTypeContactInfoBo) throws Exception {
        LOG.debug("Inside the getPatronPreferredAddress method");
        String address = "";
        if (entityTypeContactInfoBo!=null && entityTypeContactInfoBo.getAddresses() != null) {
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
        if (entityTypeContactInfoBo!=null && entityTypeContactInfoBo.getPhoneNumbers() != null) {
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
        if (entityTypeContactInfoBo!=null && entityTypeContactInfoBo.getEmailAddresses() != null) {
            for (int j = 0; j < entityTypeContactInfoBo.getEmailAddresses().size(); j++) {
                if (entityTypeContactInfoBo.getEmailAddresses().get(j).getDefaultValue()) {
                    emailId = (entityTypeContactInfoBo.getEmailAddresses().get(j).getEmailAddress());
                    break;
                }
            }
        }
        return emailId;
    }

    public void generateCheckoutReceiptNotice(List<OleLoanDocument> oleLoanDocument) throws Exception {
        if(!oleLoanDocument.get(0).getOlePatron().isCheckoutReceiptOptOut()) {
            Map claimMap = new HashMap();
            claimMap.put(OLEConstants.LOAN_DOCUMENTS, oleLoanDocument);
            Runnable checkoutReceiptNoticeExecutor = new CheckoutReceiptNoticeExecutor(claimMap);
            checkoutReceiptNoticeExecutor.run();
        }

    }

    public void sendMailToPatron(List<OleLoanDocument> oleLoanDocumentList) throws Exception {
        Boolean isSendCheckoutReceipt = getParameterValueResolver().getParameterAsBoolean(OLEConstants.APPL_ID_OLE, OLEConstants
                .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.SEND_CHECKOUT_RECEIPT_NOTICE);
        if(isSendCheckoutReceipt) {
            for (Iterator<OleLoanDocument> iterator = oleLoanDocumentList.iterator(); iterator.hasNext(); ) {
                OleLoanDocument oleLoanDocument = iterator.next();
                generateContentforCheckoutReceipt(oleLoanDocument);
            }
            generateCheckoutReceiptNotice(oleLoanDocumentList);
        }
    }

    private void generateContentforCheckoutReceipt(OleLoanDocument oleLoanDocument) {
        CircUtilController circUtilController = new CircUtilController();
        ItemRecord itemRecord = circUtilController.getItemRecordByBarcode(oleLoanDocument.getItemId());
        if (itemRecord != null && !itemRecord.getClaimsReturnedFlag()) {
            OleItemRecordForCirc oleItemRecordForCirc = ItemInfoUtil.getInstance().getOleItemRecordForCirc(itemRecord, null);
            if (StringUtils.isBlank(oleLoanDocument.getItemFullLocation())) {
                oleLoanDocument.setItemFullLocation(oleItemRecordForCirc.getItemFullPathLocation());
            }
            if (oleItemRecordForCirc != null) {
                List<OLEDeliverNotice> oleDeliverNoticeList = createNotices(oleLoanDocument.getPatronId(), oleLoanDocument.getItemId());
                if (CollectionUtils.isNotEmpty(oleDeliverNoticeList)) {
                    oleLoanDocument.getDeliverNotices().addAll(oleDeliverNoticeList);
                }
            }
        }
    }

    private List<OLEDeliverNotice> createNotices(String patronId, String itemId) {
        List<OLEDeliverNotice> oleDeliverNoticeList = new ArrayList<>();
        String noticeContentConfigName = OLEConstants.CHECKOUT_RECEIPT_NOTICE;
        OLEDeliverNotice oleDeliverNotice = new OLEDeliverNotice();
        oleDeliverNotice.setNoticeSendType(OLEConstants.EMAIL);
        oleDeliverNotice.setPatronId(patronId);
        oleDeliverNotice.setItemBarcode(itemId);
        oleDeliverNotice.setNoticeType(OLEConstants.CHECKOUT_RECEIPT_NOTICE);
        oleDeliverNotice.setNoticeContentConfigName(noticeContentConfigName);
        oleDeliverNoticeList.add(oleDeliverNotice);
        return oleDeliverNoticeList;
    }

    public ParameterValueResolver getParameterValueResolver() {
        if (null == parameterValueResolver) {
            parameterValueResolver = ParameterValueResolver.getInstance();
        }
        return parameterValueResolver;
    }

    public void setParameterValueResolver(ParameterValueResolver parameterValueResolver) {
        this.parameterValueResolver = parameterValueResolver;
    }

}