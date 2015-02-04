package org.kuali.ole.service;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.ingest.OlePatronRecordHandler;
import org.kuali.ole.ingest.OlePatronXMLSchemaValidator;
import org.kuali.ole.ingest.pojo.*;
import org.kuali.ole.deliver.api.OlePatronDefinition;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.constant.OLEPatronConstant;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.address.EntityAddressTypeBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationTypeBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailTypeBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentStatusBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentTypeBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameTypeBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneTypeBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.location.impl.campus.CampusBo;
import org.kuali.rice.location.impl.country.CountryBo;
import org.kuali.rice.location.impl.state.StateBo;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.*;

/**
 * OlePatronConverterService generates list of patron to perform ole patron operation.
 */
public class OlePatronConverterService {

    private static final Logger LOG = Logger.getLogger(OlePatronConverterService.class);

    private BusinessObjectService businessObjectService;
    private OlePatronRecordHandler olePatronRecordHandler;
    private OlePatronService olePatronService;
    private String patronAddressSource="";

    public String getPatronAddressSource() {
        return patronAddressSource;
    }

    public void setPatronAddressSource(String patronAddressSource) {
        this.patronAddressSource = patronAddressSource;
    }

    /**
     * Gets the value of olePatronService which is of type OlePatronService
     * @return olePatronService(OlePatronService)
     */
    public OlePatronService getOlePatronService() {
        return this.olePatronService;
    }

    /**
     * Sets the value for olePatronService which is of type OlePatronService
     * @param olePatronService(OlePatronService)
     */
    public void setOlePatronService(OlePatronService olePatronService) {
        this.olePatronService = olePatronService;
    }

    /**
     * This method will get the values from the ingested xml file, check the entity id and addUnmatchedPatron (from screen) .
     * @param fileContent
     * @param addUnMatchedPatronFlag
     * @param fileName
     * @param olePatronIngestSummaryRecord
     * @return list of OlePatronDocument
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     */
    public List<OlePatronDocument> persistPatronFromFileContent(String fileContent, boolean addUnMatchedPatronFlag, String fileName, OlePatronIngestSummaryRecord olePatronIngestSummaryRecord,String addressSource,String principalName) throws IOException, URISyntaxException {
        List<OlePatronDocument> savedOlePatronDocuments = new ArrayList<OlePatronDocument>();
        Map<String,String> sourceMap = new HashMap<String,String>();
        sourceMap.put(OLEConstants.OlePatron.OLE_SOURCE_ID,addressSource);
        List<OleSourceBo> sourceBoList = (List<OleSourceBo>)KRADServiceLocator.getBusinessObjectService().findMatching(OleSourceBo.class,sourceMap);
        if(sourceBoList.size()>0){
            this.patronAddressSource= sourceBoList.get(0).getOleSourceCode();
        }
        OlePatronGroup olePatronGroup = new OlePatronGroup();
        OlePatronGroup patron = getOlePatronRecordHandler().buildPatronFromFileContent(fileContent);
        List<OlePatron> createPatronList = new ArrayList<OlePatron>();
        List<OlePatron> updatePatronList = new ArrayList<OlePatron>();
        List<OlePatron> rejectedPatronList = new ArrayList<OlePatron>();
        List<OlePatron> failedPatronList = new ArrayList<OlePatron>();
        List<OlePatron> deletePatronList = new ArrayList<OlePatron>();
        int patronTotCount = patron.getPatronGroup().size();

        for (int i = 0; i < patron.getPatronGroup().size(); i++) {
            if (patron.getPatronGroup().get(i).getPatronID() != null && !"".equals(patron.getPatronGroup().get(i).getPatronID())) {
                if (isPatronExist(patron.getPatronGroup().get(i).getPatronID())) {
                    updatePatronList.add(patron.getPatronGroup().get(i));
                } else {
                    if (addUnMatchedPatronFlag) {
                        createPatronList.add(patron.getPatronGroup().get(i));
                    } else {
                        rejectedPatronList.add(patron.getPatronGroup().get(i));
                    }
                }
            } else {
                String patronId = "";
                if (addUnMatchedPatronFlag) {
                    patronId = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber(OLEConstants.OlePatron.KRIM_ENTITY_ID_S).toString();
                    patron.getPatronGroup().get(i).setPatronID(patronId);
                    createPatronList.add(patron.getPatronGroup().get(i));
                } else {
                    rejectedPatronList.add(patron.getPatronGroup().get(i));
                }
            }
        }
        if (createPatronList.size() > 0) {
            savedOlePatronDocuments.addAll(createOlePatronDocument(createPatronList, failedPatronList));
            olePatronIngestSummaryRecord.setPatronCreateCount(createPatronList.size());
            createPatronList.clear();
        }
        if (updatePatronList.size() > 0) {
            savedOlePatronDocuments.addAll(updateOlePatronDocument(updatePatronList, failedPatronList));
            olePatronIngestSummaryRecord.setPatronUpdateCount(updatePatronList.size());
            updatePatronList.clear();
        }
        if (rejectedPatronList.size() > 0) {
            olePatronIngestSummaryRecord.setPatronRejectCount(rejectedPatronList.size());
            rejectedPatronList.clear();
        }
        if (failedPatronList.size() > 0) {
            olePatronIngestSummaryRecord.setPatronFailedCount(failedPatronList.size());
        }
        olePatronIngestSummaryRecord.setPatronTotCount(patronTotCount);

        //deletePatronCount = 0;
        olePatronIngestSummaryRecord.setFileName(fileName);
        olePatronIngestSummaryRecord.setPrincipalName(principalName);
        olePatronIngestSummaryRecord.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        getBusinessObjectService().save(olePatronIngestSummaryRecord);
        if (failedPatronList.size() > 0) {
            olePatronGroup.setPatronGroup(failedPatronList);
            String patronXML = getOlePatronRecordHandler().toXML(failedPatronList);
            olePatronIngestSummaryRecord.setFailureRecords(patronXML);
            //saveFailureRecordsForAttachment(patronXML, olePatronIngestSummaryRecord.getOlePatronSummaryId());
            failedPatronList.clear();
        }
        return savedOlePatronDocuments;
    }

    /**
     * This method is for getting the uploaded process message
     * @param olePatronIngestSummaryRecord
     * @return message as a string
     */
    public String getUploadProcessMessage(OlePatronIngestSummaryRecord olePatronIngestSummaryRecord) {

        String uploadProcessMessage = OLEConstants.PATRON_RECORD_SUCCESS + OLEConstants.OlePatron.TOTAL_RECORD + olePatronIngestSummaryRecord.getPatronTotCount() +
                OLEConstants.OlePatron.CREATED_RECORD + olePatronIngestSummaryRecord.getPatronCreateCount() +
                OLEConstants.OlePatron.UPDATED_RECORD + olePatronIngestSummaryRecord.getPatronUpdateCount() +
                OLEConstants.OlePatron.REJECTED_RECORD + olePatronIngestSummaryRecord.getPatronRejectCount() +
                OLEConstants.OlePatron.FAILED_RECORD + olePatronIngestSummaryRecord.getPatronFailedCount();
        return uploadProcessMessage;
    }

    /**
     * This method will check the entity id from the database , whether it is existing or not.
     * @param patronId
     * @return a boolean flag
     */
    public boolean isPatronExist(String patronId) {
        boolean flag = false;
        if (getPatron(patronId) != null) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * This method will get the entity id from the EntityBo Object
     * @param patronId
     * @return EntityBo object
     */
    private EntityBo getPatron(String patronId) {
        Map<String, Object> criteria = new HashMap<String, Object>(4);
        criteria.put(OLEConstants.OlePatron.ENTITY_BO_ID, patronId);

        return getBusinessObjectService().findByPrimaryKey(EntityBo.class, criteria);
    }

    /**
     * This method is for creating a OlePatronDocument
     * @param createPatronList
     * @param failedPatronList
     * @return list of OlePatronDocument
     */
    public List<OlePatronDocument> createOlePatronDocument(List<OlePatron> createPatronList, List<OlePatron> failedPatronList) {
        LOG.debug(" Inside createOlePatronDocument for patron ingest");
        List<OlePatronDocument> newPatrons = new ArrayList<OlePatronDocument>();
        OlePatronDocument olePatronDocument = new OlePatronDocument();
        OlePatron olePatron;
        int createPatron = createPatronList.size();
        for (int i = 0; i < createPatronList.size(); i++) {
            boolean patronCreateFlag = true;
            olePatron = new OlePatron();
            olePatron = createPatronList.get(i);
            EntityBo kimEntity = new EntityBo();
            kimEntity.setId(olePatron.getPatronID());
            olePatronDocument.setEntity(kimEntity);
            if (olePatron.getBarcode() != null) {
                if (checkDuplicateBarcode(olePatron.getBarcode())) {
                    patronCreateFlag=false;
                    olePatron.setErrorMessage(OLEPatronConstant.DUP_PATRON_BARCODE_ERROR);
                } else {
                    olePatronDocument.setBarcode(olePatron.getBarcode());
                }
            } else {
                patronCreateFlag=false;
                olePatron.setErrorMessage(OLEPatronConstant.PATRON_BARCODE_EMPTY);
            }
            olePatronDocument.setExpirationDate(olePatron.getExpirationDate());
            olePatronDocument.setActivationDate(olePatron.getActivationDate());
            olePatronDocument.setCourtesyNotice(olePatron.getPatronLevelPolicies().isReceivesCourtesyNotice());
            olePatronDocument.setDeliveryPrivilege(olePatron.getPatronLevelPolicies().isHasDeliveryPrivilege());
            olePatronDocument.setGeneralBlock(olePatron.getPatronLevelPolicies().isGenerallyBlocked());
            olePatronDocument.setGeneralBlockNotes(olePatron.getPatronLevelPolicies().getGeneralBlockNotes());
            olePatronDocument.setPagingPrivilege(olePatron.getPatronLevelPolicies().isHasPagingPrivilege());
            patronCreateFlag &= persistPatronNames(olePatron, olePatronDocument);
            if (patronCreateFlag)
                patronCreateFlag &= persistPatronBorrowerType(olePatron, olePatronDocument);
            if (patronCreateFlag)
                patronCreateFlag &= persistPatronSource(olePatron, olePatronDocument);
            if (patronCreateFlag)
                patronCreateFlag &= persistPatronStatisticalCategory(olePatron, olePatronDocument);
            if (patronCreateFlag)
                patronCreateFlag &= persistPatronPostalAddress(olePatron, olePatronDocument);
            if (patronCreateFlag)
                patronCreateFlag &= persistPatronPhoneNumbers(olePatron, olePatronDocument);
            if (patronCreateFlag)
                patronCreateFlag &= persistPatronEmailAddress(olePatron, olePatronDocument);
            if (patronCreateFlag)
                patronCreateFlag &= persistPatronAffiliations(olePatron, olePatronDocument);
            if (patronCreateFlag)
                patronCreateFlag &= persistPatronEmployments(olePatron, olePatronDocument);
            if (patronCreateFlag)
                patronCreateFlag &= persistPatronNotes(olePatron, olePatronDocument);
            olePatronDocument.setActiveIndicator(olePatron.isActive());
            if (patronCreateFlag) {
                OlePatronDefinition patron = getOlePatronService().createPatron(OlePatronDocument.to(olePatronDocument));
                newPatrons.add(OlePatronDocument.from(patron));
            } else {
                failedPatronList.add(olePatron);
            }
        }
        createPatronList.removeAll(failedPatronList);
        return newPatrons;
    }

    /**
     * This method is for updating the OlePatronDocument
     * @param updatePatronList
     * @param failedPatronList
     * @return list of OlePatronDocument
     */
    public List<OlePatronDocument> updateOlePatronDocument(List<OlePatron> updatePatronList, List<OlePatron> failedPatronList) {
        LOG.debug(" Inside updateOlePatronDocument for patron ingest");
        List<OlePatronDocument> updatedPatrons = new ArrayList<OlePatronDocument>();
        OlePatronDocument olePatronDocument = new OlePatronDocument();
        OlePatron olePatron;
        Map criteria = new HashMap<String, String>();
        for (int i = 0; i < updatePatronList.size(); i++) {
            olePatron = updatePatronList.get(i);
            criteria.put(OLEConstants.OlePatron.PATRON_ID, olePatron.getPatronID());
            List<OlePatronDocument> patronImpls = (List<OlePatronDocument>) getBusinessObjectService().findMatching(OlePatronDocument.class, criteria);
            OlePatronDocument patronDocument;
            for (Iterator<OlePatronDocument> patronIterator = patronImpls.iterator(); patronIterator.hasNext(); ) {
                boolean patronUpdateFlag = true;
                patronDocument = patronIterator.next();
                olePatronDocument.setOlePatronId(patronDocument.getOlePatronId());
                olePatronDocument.setBarcode(olePatron.getBarcode());
                olePatronDocument.setExpirationDate(olePatron.getExpirationDate());
                olePatronDocument.setActivationDate(olePatron.getActivationDate());
                olePatronDocument.setCourtesyNotice(olePatron.getPatronLevelPolicies().isReceivesCourtesyNotice());
                olePatronDocument.setDeliveryPrivilege(olePatron.getPatronLevelPolicies().isHasDeliveryPrivilege());
                olePatronDocument.setGeneralBlock(olePatron.getPatronLevelPolicies().isGenerallyBlocked());
                olePatronDocument.setGeneralBlockNotes(olePatron.getPatronLevelPolicies().getGeneralBlockNotes());
                olePatronDocument.setPagingPrivilege(olePatron.getPatronLevelPolicies().isHasPagingPrivilege());
                olePatronDocument.setActiveIndicator(olePatron.isActive());
                olePatronDocument.setObjectId(patronDocument.getObjectId());
                olePatronDocument.setVersionNumber(patronDocument.getVersionNumber());
                olePatronDocument.setOleProxyPatronDocuments(patronDocument.getOleProxyPatronDocuments());
                olePatronDocument.setOlePatronLocalIds(patronDocument.getOlePatronLocalIds());
                olePatronDocument.setLostBarcodes(patronDocument.getLostBarcodes());
                patronUpdateFlag &= persistPatronNames(olePatron, olePatronDocument);
                if (patronUpdateFlag)
                    patronUpdateFlag &= persistPatronBorrowerType(olePatron, olePatronDocument);
                if (patronUpdateFlag)
                    patronUpdateFlag &= persistPatronSource(olePatron, olePatronDocument);
                if (patronUpdateFlag)
                    patronUpdateFlag &= persistPatronStatisticalCategory(olePatron, olePatronDocument);
                if (patronUpdateFlag)
                    patronUpdateFlag &= persistPatronPostalAddress(olePatron, olePatronDocument);
                if (patronUpdateFlag)
                    patronUpdateFlag &= persistPatronPhoneNumbers(olePatron, olePatronDocument);
                if (patronUpdateFlag)
                    patronUpdateFlag &= persistPatronEmailAddress(olePatron, olePatronDocument);
                if (patronUpdateFlag)
                    patronUpdateFlag &= persistPatronAffiliations(olePatron, olePatronDocument);
                if (patronUpdateFlag)
                    patronUpdateFlag &= persistPatronEmployments(olePatron, olePatronDocument);
                if (patronUpdateFlag)
                    patronUpdateFlag &= persistPatronNotes(olePatron, olePatronDocument);
                if (patronUpdateFlag) {
                    OlePatronDefinition olePatronDefinition = getOlePatronService().updatePatron(OlePatronDocument.to(olePatronDocument));
                    updatedPatrons.add(OlePatronDocument.from(olePatronDefinition));
                } else {
                    failedPatronList.add(olePatron);
                }
            }
        }
        updatePatronList.removeAll(failedPatronList);
        return updatedPatrons;
    }

   /**
     * This method is for getting the object of OlePatronGroup from the ingested xml (used for testCase).
     * @param fileContent
     * @return OlePatronGroup object
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     */
    public OlePatronGroup buildPatronFromFileContent(String fileContent) throws URISyntaxException, IOException {
        return getOlePatronRecordHandler().buildPatronFromFileContent(fileContent);
    }

    /**
     * This method will get the resource file(xml file) and convert object from the xml file (used for testCase)
     * @param fileName
     * @return OlePatronGroup object (converted from xml)
     * @throws java.net.URISyntaxException
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */
    public OlePatronGroup buildPatron(String fileName) throws URISyntaxException, IOException, SAXException {
        URL resource = getClass().getResource(fileName);
        File file = new File(resource.toURI());
        String fileContent = new FileUtil().readFile(file);
        if (validProfileXML(fileContent)) {
            return getOlePatronRecordHandler().buildPatronFromFileContent(fileContent);
        }
        return null;
    }

    /**
     * This method will validate the ingested xml against schema.
     * @param fileContent
     * @return boolean
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */
    private boolean validProfileXML(String fileContent) throws IOException, SAXException {
        return new OlePatronXMLSchemaValidator().validateContentsAgainstSchema(null);
    }

    /**
     * Persist phone numbers of the patron and set the values from the patron telephone numbers to entity phone bo  object
     * @param olePatron
     * @param olePatronDocument
     * @return boolean flag, to check whether the patron phone number type is valid .
     */
    private boolean persistPatronPhoneNumbers(OlePatron olePatron, OlePatronDocument olePatronDocument) {
        boolean phoneFlag = false;
        List<EntityPhoneBo> phones = new ArrayList<EntityPhoneBo>();
        EntityPhoneBo entityPhoneBo;
        List<OlePatronTelePhoneNumber> olePatronTelePhoneNumbers = olePatron.getTelephoneNumbers();
        for (Iterator<OlePatronTelePhoneNumber> iterator = olePatronTelePhoneNumbers.iterator(); iterator.hasNext(); ) {
            OlePatronTelePhoneNumber phoneNumbers = iterator.next();
            entityPhoneBo = new EntityPhoneBo();
            entityPhoneBo.setPhoneNumber(phoneNumbers.getTelephoneNumber());
            Map criteria = new HashMap<String, String>();
            Map criteriaCountry = new HashMap<String, String>();
            if (!phoneNumbers.getTelephoneNumberType().equals("")) {
                criteria.put(OLEConstants.CODE, phoneNumbers.getTelephoneNumberType());
                List<EntityPhoneTypeBo> entityType = (List<EntityPhoneTypeBo>) getBusinessObjectService().findMatching(EntityPhoneTypeBo.class, criteria);
                if (entityType.size() > 0) {
                    entityPhoneBo.setPhoneType(entityType.get(0));
                    entityPhoneBo.setPhoneTypeCode(entityType.get(0).getCode());
                    if (phoneNumbers.getExtension() != null && !phoneNumbers.getExtension().equals("")) {
                        entityPhoneBo.setExtensionNumber(phoneNumbers.getExtension());
                    }
                    if (phoneNumbers.getCountry() != null && !phoneNumbers.getCountry().equals("")) {
                        criteriaCountry.put(OLEConstants.CODE, phoneNumbers.getCountry());
                        List<CountryBo> countryList = (List<CountryBo>) getBusinessObjectService().findMatching(CountryBo.class, criteriaCountry);
                        if (countryList.size() > 0) {
                            entityPhoneBo.setCountryCode(phoneNumbers.getCountry());
                        } else {
                            olePatron.setErrorMessage(OLEPatronConstant.COUNTRY_PHONE_ERROR);
                            return false;
                        }
                    }
                    entityPhoneBo.setActive(phoneNumbers.isActive());
                    entityPhoneBo.setDefaultValue(phoneNumbers.isDefaults());
                    phones.add(entityPhoneBo);
                    boolean defaultValue = checkPhoneMultipleDefault(phones);
                    if (defaultValue) {
                        olePatronDocument.setPhones(phones);
                        phoneFlag = true;
                    } else {
                        olePatron.setErrorMessage(OLEPatronConstant.PHONE_DEFAULT_VALUE_ERROR);
                        phoneFlag = false;
                    }
                } else {
                    olePatron.setErrorMessage(OLEPatronConstant.PHONETYPE_ERROR);
                }
            } else {
                olePatron.setErrorMessage(OLEPatronConstant.PHONETYPE_BLANK_ERROR);
            }
        }
        return phoneFlag;
    }

    /**
     * Persist email Address of the patron and set the values from the patron email address to entity email bo  object
     * @param olePatron
     * @param olePatronDocument
     * @return boolean flag ,to check whether the patron email type is valid .
     */
    private boolean persistPatronEmailAddress(OlePatron olePatron, OlePatronDocument olePatronDocument) {
        boolean emailFlag = false;
        List<EntityEmailBo> email = new ArrayList<EntityEmailBo>();
        EntityEmailBo entityEmailBo;
        List<OlePatronEmailAddress> olePatronEmailAddresses = olePatron.getEmailAddresses();
        for (Iterator<OlePatronEmailAddress> iterator = olePatronEmailAddresses.iterator(); iterator.hasNext(); ) {
            OlePatronEmailAddress emailAddresses = iterator.next();
            entityEmailBo = new EntityEmailBo();
            entityEmailBo.setEmailAddress(emailAddresses.getEmailAddress());
            if (!emailAddresses.getEmailAddressType().equals("")) {
                Map criteria = new HashMap<String, String>();
                criteria.put(OLEConstants.CODE, emailAddresses.getEmailAddressType());
                List<EntityEmailTypeBo> entityType = (List<EntityEmailTypeBo>) getBusinessObjectService().findMatching(EntityEmailTypeBo.class, criteria);
                if (entityType.size() > 0) {
                    entityEmailBo.setEmailType(entityType.get(0));
                    entityEmailBo.setEmailTypeCode(entityType.get(0).getCode());
                    entityEmailBo.setActive(emailAddresses.isActive());
                    entityEmailBo.setDefaultValue(emailAddresses.isDefaults());
                    email.add(entityEmailBo);
                    boolean defaultValue = checkEmailMultipleDefault(email);
                    if (defaultValue) {
                        olePatronDocument.setEmails(email);
                        emailFlag = true;
                    } else {
                        olePatron.setErrorMessage(OLEPatronConstant.EMAIL_DEFAULT_VALUE_ERROR);
                        emailFlag = false;
                    }
                } else {
                    olePatron.setErrorMessage(OLEPatronConstant.EMAILTYPE_ERROR);
                }
            } else {
                olePatron.setErrorMessage(OLEPatronConstant.EMAILTYPE_BLANK_ERROR);
            }
        }
        return emailFlag;
    }

    /**
     * Persist postal address of the patron and set the values from the patron postal address to entity address bo  object
     * @param olePatron
     * @param olePatronDocument
     * @return boolean flag , to check whether the patron address type is valid .
     */
    private boolean persistPatronPostalAddress(OlePatron olePatron, OlePatronDocument olePatronDocument) {
        boolean addressFlag = false;
        List<OleEntityAddressBo> addressBo = new ArrayList<OleEntityAddressBo>();
        OleEntityAddressBo oleEntityAddressBo;
        List<EntityAddressBo> address = new ArrayList<EntityAddressBo>();
        List<OleAddressBo> oleAddress = new ArrayList<OleAddressBo>();
        EntityAddressBo entityAddressBo;
        OleAddressBo oleAddressBo;
        List<OlePatronPostalAddress> olePatronPostalAddresses = olePatron.getPostalAddresses();
        for (Iterator<OlePatronPostalAddress> iterator = olePatronPostalAddresses.iterator(); iterator.hasNext(); ) {
            OlePatronPostalAddress postalAddresses = iterator.next();
            oleEntityAddressBo = new OleEntityAddressBo();
            oleAddressBo = new OleAddressBo();
            entityAddressBo = new EntityAddressBo();
            /*if(oleEntityAddressBo.getEntityAddressBo().getId() == null) {
            String entityAddressSeq = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("krim_entity_addr_id_s").toString();
            entityAddressBo.setId(entityAddressSeq);
            }*/
            List<OleAddressLine> addressLineList = postalAddresses.getAddressLinesList();
            if (addressLineList.size() > 0) {
                for (int i = 0; i < addressLineList.size(); i++) {
                    if (i == 0) {
                        entityAddressBo.setLine1(addressLineList.get(i).getAddressLine());
                    }
                    if (i == 1) {
                        entityAddressBo.setLine2(addressLineList.get(i).getAddressLine());
                    }
                    if (i == 2) {
                        entityAddressBo.setLine3(addressLineList.get(i).getAddressLine());
                    }
                }
            }
            Map criteria = new HashMap<String, String>();
            Map criteriaState = new HashMap<String, String>();
            Map criteriaCountry = new HashMap<String, String>();
            if (postalAddresses.getPostalAddressType() != null && !postalAddresses.getPostalAddressType().equals("")) {
                criteria.put(OLEConstants.CODE, postalAddresses.getPostalAddressType());
                List<EntityAddressTypeBo> entityType = (List<EntityAddressTypeBo>) getBusinessObjectService().findMatching(EntityAddressTypeBo.class, criteria);
                if (entityType.size() > 0) {
                    entityAddressBo.setAddressType(entityType.get(0));
                    entityAddressBo.setAddressTypeCode(entityType.get(0).getCode());
                } else {
                    olePatron.setErrorMessage(OLEPatronConstant.ADDRESSTYPE_ERROR);
                    return false;
                }
            } else {
                olePatron.setErrorMessage(OLEPatronConstant.ADDRESS_TYPE_BLANK_ERROR);
                return false;
            }
            if (postalAddresses.getStateProvince() != null && !postalAddresses.getStateProvince().equals("")) {
                criteriaState.put(OLEConstants.CODE, postalAddresses.getStateProvince());
                List<StateBo> stateBoList = (List<StateBo>) getBusinessObjectService().findMatching(StateBo.class, criteriaState);
                if (stateBoList.size() > 0) {
                    entityAddressBo.setStateProvinceCode(postalAddresses.getStateProvince());
                } else {
                    olePatron.setErrorMessage(OLEPatronConstant.STATE_ERROR);
                    return false;
                }
            }
            if (postalAddresses.getCountry() != null && !postalAddresses.getCountry().equals("")) {
                criteriaCountry.put(OLEConstants.CODE, postalAddresses.getCountry());
                List<CountryBo> countryList = (List<CountryBo>) getBusinessObjectService().findMatching(CountryBo.class, criteriaCountry);
                if (countryList.size() > 0) {
                    entityAddressBo.setCountryCode(postalAddresses.getCountry());
                } else {
                    olePatron.setErrorMessage(OLEPatronConstant.COUNTRY_ADDRESS_ERROR);
                    return false;
                }
            }
            entityAddressBo.setCity(postalAddresses.getCity());
            entityAddressBo.setPostalCode(postalAddresses.getPostalCode());
            entityAddressBo.setActive(postalAddresses.isActive());
            entityAddressBo.setDefaultValue(postalAddresses.isDefaults());
            EntityAddressBo entity = getBusinessObjectService().save(entityAddressBo);
            oleAddressBo.setOleAddressId(KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber(OLEConstants.OlePatron.OLE_DLVR_ADD_S).toString());
            oleAddressBo.setId(entity.getId());
            oleAddressBo.setAddressVerified(true);
            if (postalAddresses.isAddressVerified() || postalAddresses.getAddressSource() != null) {
                oleAddressBo.setAddressValidFrom(postalAddresses.getAddressValidFrom());
                oleAddressBo.setAddressValidTo(postalAddresses.getAddressValidTo());
                oleAddressBo.setAddressVerified(postalAddresses.isAddressVerified());
                String addressSource = postalAddresses.getAddressSource();
                String addressId;
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(OLEConstants.OlePatron.OLE_ADD_SRC_CD, addressSource);
                List<OleAddressSourceBo> addressSourceList = (List<OleAddressSourceBo>) getBusinessObjectService().findMatching(OleAddressSourceBo.class, map);
                if (addressSourceList.size() > 0) {
                    addressId = addressSourceList.get(0).getOleAddressSourceId();
                    oleAddressBo.setAddressSource(addressId);
                    oleAddressBo.setAddressSourceBo(addressSourceList.get(0));
                } else {
                    olePatron.setErrorMessage(OLEPatronConstant.ADDRESS_SOURCE_ERROR);
                    return false;
                }
            }
            oleAddress.add(oleAddressBo);
            oleAddressBo.setEntityAddress(entityAddressBo);
            oleEntityAddressBo.setEntityAddressBo(entityAddressBo);
            address.add(entityAddressBo);
            oleEntityAddressBo.setOleAddressBo(oleAddressBo);
            addressBo.add(oleEntityAddressBo);
            boolean defaultValue = checkAddressMultipleDefault(addressBo);
            if (defaultValue && oleAddressBo.getAddressSource()!=null) {
                olePatronDocument.setAddresses(address);
                olePatronDocument.setOleAddresses(oleAddress);
                olePatronDocument.setOleEntityAddressBo(addressBo);
                addressFlag = true;
            } else {
                if (oleAddressBo.getAddressSource() == null) {
                    olePatron.setErrorMessage(OLEPatronConstant.PATRON_ADDRESS_SOURCE_REQUIRED);
                } else {
                    olePatron.setErrorMessage(OLEPatronConstant.ADDRESS_DEFAULT_VALUE_ERROR);
                }
                addressFlag = false;
            }
        }
        return addressFlag;
    }

    /**
     * Persist names of the patron and set the values from the patron names to entity name bo  object
     * @param olePatron
     * @param olePatronDocument
     * @return boolean flag , to check whether the patron name type is valid .
     */
    private boolean persistPatronNames(OlePatron olePatron, OlePatronDocument olePatronDocument) {
        boolean nameFlag = false;
        EntityNameBo names = new EntityNameBo();
        Map criteria = new HashMap<String, String>();
        criteria.put(OLEConstants.NAME, OLEConstants.PREFERRED);
        List<EntityNameTypeBo> entityType = (List<EntityNameTypeBo>) getBusinessObjectService().findMatching(EntityNameTypeBo.class, criteria);
        if (entityType.size() > 0) {
            names.setNameType(entityType.get(0));
            names.setNameCode(entityType.get(0).getCode());
            if (!olePatron.getName().getFirst().equals("") && !olePatron.getName().getSurname().equals("")) {
                names.setFirstName(olePatron.getName().getFirst());
                names.setLastName(olePatron.getName().getSurname());
                if (olePatron.getName().getTitle() != null && !olePatron.getName().getTitle().equals("")) {
                    names.setNamePrefix(olePatron.getName().getTitle());
                }
                if (olePatron.getName().getSuffix() != null && !olePatron.getName().getSuffix().equals("")) {
                    names.setNameSuffix(olePatron.getName().getSuffix());
                }
                names.setActive(true);
                names.setDefaultValue(true);
                olePatronDocument.setName(names);
                nameFlag = true;
            } else {
                if (olePatron.getName().getFirst().equals("")) {
                    olePatron.setErrorMessage(OLEPatronConstant.FIRSTNAME_BLANK_ERROR);
                }
                if (olePatron.getName().getSurname().equals("")) {
                    olePatron.setErrorMessage(OLEPatronConstant.SURNAME_BLANK_ERROR);
                }
            }
        }
        return nameFlag;
    }

    /**
     * Persist notes of the patron and set the values from the patron notes ( converted object from xml) to Patron notes bo  object
     * @param olePatron
     * @param olePatronDocument
     * @return boolean flag , to check whether the patron note type is valid .
     */
    private boolean persistPatronNotes(OlePatron olePatron, OlePatronDocument olePatronDocument) {
        boolean notesFlag = false;
        List<OlePatronNotes> olePatronNotesList = new ArrayList<OlePatronNotes>();
        OlePatronNotes olePatronNotes;
        List<OlePatronNote> olePatronNoteList = olePatron.getNotes();
        for (Iterator<OlePatronNote> iterator = olePatronNoteList.iterator(); iterator.hasNext(); ) {
            OlePatronNote olePatronNote = iterator.next();
            olePatronNotes = new OlePatronNotes();
            if (olePatronNote.getNoteType() != null && !"".equals(olePatronNote.getNoteType())) {
                Map criteria = new HashMap<String, String>();
                criteria.put(OLEConstants.PATRON_NOTE_TYPE_CODE, olePatronNote.getNoteType());
                List<OlePatronNoteType> olePatronNoteTypes = (List<OlePatronNoteType>) getBusinessObjectService().findMatching(OlePatronNoteType.class, criteria);
                if (olePatronNoteTypes.size() > 0) {
                    olePatronNotes.setOlePatronNoteType(olePatronNoteTypes.get(0));
                    olePatronNotes.setPatronNoteTypeId(olePatronNoteTypes.get(0).getPatronNoteTypeId());
                    olePatronNotes.setPatronNoteText(olePatronNote.getNote());
                    olePatronNotes.setActive(olePatronNote.isActive());
                    olePatronNotesList.add(olePatronNotes);
                    olePatronDocument.setNotes(olePatronNotesList);
                    notesFlag = true;
                } else {
                    olePatron.setErrorMessage(OLEPatronConstant.NOTETYPE_ERROR);
                }

            } else {
                olePatron.setErrorMessage(OLEPatronConstant.NOTETYPE_BLANK_ERROR);
            }
        }
        if (olePatronNoteList.isEmpty()) {
            notesFlag = true;
        }
        return notesFlag;
    }

    /**
     * This method is for persisting the borrower Type of the patron
     * @param olePatron
     * @param olePatronDocument
     * @return boolean borrowerTypeFlag, to check whether the borrower Type is valid.
     */
    private boolean persistPatronBorrowerType(OlePatron olePatron, OlePatronDocument olePatronDocument) {
        boolean borrowerTypeFlag = false;
        String borroweTypeId;
        String borrowerTypeName;
        borrowerTypeName = olePatron.getBorrowerType();
        if (borrowerTypeName != null && !borrowerTypeName.equals("")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(OLEConstants.BORROWER_TYPE_CODE, borrowerTypeName);
            List<OleBorrowerType> borrowerTypes = (List<OleBorrowerType>) getBusinessObjectService().findMatching(OleBorrowerType.class, map);
            if (borrowerTypes.size() > 0) {
                borroweTypeId = borrowerTypes.get(0).getBorrowerTypeId();
                olePatronDocument.setBorrowerType(borroweTypeId);
                olePatronDocument.setOleBorrowerType(borrowerTypes.get(0));
                borrowerTypeFlag = true;
            } else {
                olePatron.setErrorMessage(OLEPatronConstant.BORROWERTYPE_ERROR);
            }
        } else {
            olePatron.setErrorMessage(OLEPatronConstant.BORROWERTYPE_BLANK_ERROR);
        }
        return borrowerTypeFlag;
    }

    /**
     * This method is for persisting the Source of the patron
     * @param olePatron
     * @param olePatronDocument
     * @return boolean sourceFlag, to check whether the Source is valid.
     */
    private boolean persistPatronSource(OlePatron olePatron, OlePatronDocument olePatronDocument) {
        boolean sourceFlag = false;
        String sourceId;
        String sourceCode;
        sourceCode = this.patronAddressSource;
        if (sourceCode != null && !sourceCode.equals("")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(OLEConstants.SOURCE_CODE, sourceCode);
            List<OleSourceBo> sourceList = (List<OleSourceBo>) getBusinessObjectService().findMatching(OleSourceBo.class, map);
            if (sourceList.size() > 0) {
                sourceId = sourceList.get(0).getOleSourceId();
                olePatronDocument.setSource(sourceId);
                olePatronDocument.setSourceBo(sourceList.get(0));
                sourceFlag = true;
            } else {
                olePatron.setErrorMessage(OLEPatronConstant.SOURCE_CODE_ERROR);
            }
        } else {
            sourceFlag = true;
        }
        return sourceFlag;
    }


    /**
     * This method is for persisting the Statistical Category of the patron
     * @param olePatron
     * @param olePatronDocument
     * @return boolean statisticalCategoryFlag, to check whether the Statistical Category is valid.
     */
    private boolean persistPatronStatisticalCategory(OlePatron olePatron, OlePatronDocument olePatronDocument) {
        boolean statisticalCategoryFlag = false;
        String statisticalCategoryId;
        String statisticalCategoryCode;
        statisticalCategoryCode = olePatron.getStatisticalCategory();
        if (statisticalCategoryCode != null && !statisticalCategoryCode.equals("")) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(OLEConstants.STATISTICAL_CATEGORY_CODE, statisticalCategoryCode);
            List<OleStatisticalCategoryBo> statisticalCategoryList = (List<OleStatisticalCategoryBo>) getBusinessObjectService().findMatching(OleStatisticalCategoryBo.class, map);
            if (statisticalCategoryList.size() > 0) {
                statisticalCategoryId = statisticalCategoryList.get(0).getOleStatisticalCategoryId();
                olePatronDocument.setStatisticalCategory(statisticalCategoryId);
                olePatronDocument.setStatisticalCategoryBo(statisticalCategoryList.get(0));
                statisticalCategoryFlag = true;
            } else {
                olePatron.setErrorMessage(OLEPatronConstant.STATISTICAL_CATEGORY_CODE_ERROR);
            }
        } else {
            statisticalCategoryFlag = true;
        }
        return statisticalCategoryFlag;
    }

    /**
     * This method is for persisting the Affiliations of the patron
     * @param olePatron
     * @param olePatronDocument
     * @return boolean affiliationFlag, to check whether the Affiliations is valid.
     */
    private boolean persistPatronAffiliations(OlePatron olePatron, OlePatronDocument olePatronDocument) {
        boolean affiliationFlag = false;
        List<OlePatronAffiliation> patronAffiliations = new ArrayList<OlePatronAffiliation>();
        OlePatronAffiliation patronAffiliation;
        if (olePatron.getAffiliations() != null) {
            List<OlePatronAffiliations> olePatronAffiliationsList = olePatron.getAffiliations();
            for (Iterator<OlePatronAffiliations> iterator = olePatronAffiliationsList.iterator(); iterator.hasNext(); ) {
                OlePatronAffiliations affiliation = iterator.next();
                patronAffiliation = new OlePatronAffiliation();
                String affiliationSeq = KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("krim_entity_afltn_id_s").toString();
                if (patronAffiliation.getEntityAffiliationId() == null)
                    patronAffiliation.setEntityAffiliationId(affiliationSeq);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(OLEConstants.CODE, affiliation.getAffiliationType());
                List<EntityAffiliationTypeBo> patronAffiliationList = (List<EntityAffiliationTypeBo>) getBusinessObjectService().findMatching(EntityAffiliationTypeBo.class, map);
                if (patronAffiliationList.size() > 0) {
                    patronAffiliation.setAffiliationTypeCode(patronAffiliationList.get(0).getCode());
                    patronAffiliation.setAffiliationType(patronAffiliationList.get(0));
                } else {
                    olePatron.setErrorMessage(OLEPatronConstant.AFFILIATION_CODE_ERROR);
                    return false;
                }
                HashMap<String, String> campusMap = new HashMap<String, String>();
                campusMap.put(OLEConstants.CODE, affiliation.getCampusCode());
                List<CampusBo> campusBos = (List<CampusBo>) getBusinessObjectService().findMatching(CampusBo.class, campusMap);
                if (campusBos.size() > 0) {
                    patronAffiliation.setCampusCode(affiliation.getCampusCode());
                } else {
                    olePatron.setErrorMessage(OLEPatronConstant.CAMPUS_CODE_ERROR);
                    return false;
                }
                patronAffiliation.setCampusCode(affiliation.getCampusCode());
                patronAffiliations.add(patronAffiliation);
                olePatronDocument.setPatronAffiliations(patronAffiliations);
                affiliationFlag = true;
            }
        } else {
            affiliationFlag = true;
        }
        return affiliationFlag;
    }


    private boolean persistPatronEmployments(OlePatron olePatron, OlePatronDocument olePatronDocument) {
        boolean employmentFlag = false;
        List<EntityEmploymentBo> patronEmploymentBoList = new ArrayList<EntityEmploymentBo>();
        EntityEmploymentBo patronEmploymentBo;
        if (olePatron.getAffiliations() != null) {
            for (int i = 0; i < olePatron.getAffiliations().size(); i++) {
                if (olePatron.getAffiliations().get(i).getEmployments() != null) {
                    OlePatronAffiliation olePatronAffiliation = olePatronDocument.getPatronAffiliations().get(i);
                    List<OlePatronEmployments> patronEmploymentList = olePatron.getAffiliations().get(i).getEmployments();
                    for (Iterator<OlePatronEmployments> iterator = patronEmploymentList.iterator(); iterator.hasNext(); ) {
                        OlePatronEmployments employment = iterator.next();
                        patronEmploymentBo = new EntityEmploymentBo();
                        patronEmploymentBo.setEntityAffiliationId(olePatronAffiliation.getEntityAffiliationId());
                        patronEmploymentBo.setEntityAffiliation(olePatronAffiliation.getEntityAffliationBo());
                        patronEmploymentBo.setEmployeeId(employment.getEmployeeId());
                        patronEmploymentBo.setPrimary(employment.getPrimary());
                        patronEmploymentBo.setBaseSalaryAmount((employment.getBaseSalaryAmount()));
                        patronEmploymentBo.setPrimaryDepartmentCode(employment.getPrimaryDepartmentCode());
                        patronEmploymentBo.setActive(employment.isActive());
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(OLEConstants.CODE, employment.getEmployeeStatusCode());
                        List<EntityEmploymentStatusBo> patronEmploymentStatusList = (List<EntityEmploymentStatusBo>) getBusinessObjectService().findMatching(EntityEmploymentStatusBo.class, map);
                        if (patronEmploymentStatusList.size() > 0) {
                            patronEmploymentBo.setEmployeeStatusCode(patronEmploymentStatusList.get(0).getCode());
                            patronEmploymentBo.setEmployeeStatus(patronEmploymentStatusList.get(0));
                        } else {
                            olePatron.setErrorMessage(OLEPatronConstant.EMPLOYMENT_STATUS_ERROR);
                            return false;
                        }
                        HashMap<String, String> empMap = new HashMap<String, String>();
                        empMap.put(OLEConstants.CODE, employment.getEmployeeTypeCode());
                        List<EntityEmploymentTypeBo> employmentTypeBoList = (List<EntityEmploymentTypeBo>) getBusinessObjectService().findMatching(EntityEmploymentTypeBo.class, empMap);
                        if (employmentTypeBoList.size() > 0) {
                            patronEmploymentBo.setEmployeeTypeCode(employmentTypeBoList.get(0).getCode());
                            patronEmploymentBo.setEmployeeType(employmentTypeBoList.get(0));
                        } else {
                            olePatron.setErrorMessage(OLEPatronConstant.EMPLOYMENT_TYPE_ERROR);
                            return false;
                        }
                        patronEmploymentBo.setEmployeeTypeCode(employment.getEmployeeTypeCode());
                        patronEmploymentBoList.add(patronEmploymentBo);
                        olePatronDocument.getPatronAffiliations().get(i).setEmployments(patronEmploymentBoList);
                        employmentFlag = true;
                    }
                }
            }
        } else {
            employmentFlag = true;
        }
        return employmentFlag;
    }

    /**
     * This method is for saving the failed records in a separate location of home directory.
     * @param patronXML
     * @param patronReportId
     */
    private void saveFailureRecordsForAttachment(String patronXML, String patronReportId) {
        OlePatronIngestSummaryRecord olePatronIngestSummaryRecord;
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            String directory = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.STAGING_DIRECTORY) +
                    OLEConstants.PATRON_FILE_DIRECTORY;
            String homeDirectory = System.getProperty(OLEConstants.USER_HOME_DIRECTORY);
            int reportId = Integer.parseInt(patronReportId);
            File file = new File(homeDirectory + directory);
            if (file.isDirectory()) {
                file = new File(homeDirectory + directory + reportId + OLEConstants.FAILED_PATRON_RECORD_NAME);
                file.createNewFile();
                FileUtils.writeStringToFile(file, patronXML);
            } else {
                file.mkdirs();
                if (file.isDirectory()) {
                    File newFile = new File(file, reportId + OLEConstants.FAILED_PATRON_RECORD_NAME);
                    newFile.createNewFile();
                    FileUtils.writeStringToFile(newFile, patronXML);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * This method is for checking the entity phone has multiple defaults
     * @param phoneBoList
     * @return true , if the entity phone has only one default in a single patron record, else false
     */
    protected boolean checkPhoneMultipleDefault(List<EntityPhoneBo> phoneBoList) {

        boolean valid = true;
        int defaultCounter = 0;
        for (EntityPhoneBo entityPhoneBo : phoneBoList) {
            if (entityPhoneBo.isDefaultValue()) {
                defaultCounter++;
            }
        }
        if (defaultCounter != 1 ) {
            valid = false;
        }
        return valid;
    }

    /**
     * This method is for checking the entity address has multiple defaults
     * @param addrBoList
     * @return true , if the entity address has only one default in a single patron record, else false
     */
    protected boolean checkAddressMultipleDefault(List<OleEntityAddressBo> addrBoList) {

        boolean valid = true;
        int defaultCounter = 0;
        for (OleEntityAddressBo oleEntityAddressBo : addrBoList) {
            EntityAddressBo entityAddressBo = oleEntityAddressBo.getEntityAddressBo();
            if (entityAddressBo.isDefaultValue()) {
                defaultCounter++;
            }
        }
        if (defaultCounter != 1) {
            valid = false;
        }
        return valid;
    }

    /**
     * This method is for checking the entity email address has multiple defaults
     * @param emailBoList
     * @return true , if the entity email address has only one default in a single patron record, else false
     */
    protected boolean checkEmailMultipleDefault(List<EntityEmailBo> emailBoList) {

        boolean valid = true;
        int defaultCounter = 0;
        for (EntityEmailBo entityEmailBo : emailBoList) {
            if (entityEmailBo.isDefaultValue()) {
                defaultCounter++;
            }
        }
        if (defaultCounter != 1) {
            valid = false;
        }
        return valid;
    }

    /**
     * Gets the instance of BusinessObjectService
     * @return businessObjectService(BusinessObjectService)
     */
    private BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    /**
     * Gets the instance of OlePatronRecordHandler
     * @return olePatronRecordHandler(OlePatronRecordHandler)
     */
    public OlePatronRecordHandler getOlePatronRecordHandler() {
        if (null == olePatronRecordHandler) {
            olePatronRecordHandler = new OlePatronRecordHandler();
        }
        return olePatronRecordHandler;
    }

    /**
     * Sets the olePatronRecordHandler which is of type OlePatronRecordHandler
     * @param olePatronRecordHandler(OlePatronRecordHandler)
     */
    public void setOlePatronRecordHandler(OlePatronRecordHandler olePatronRecordHandler) {
        this.olePatronRecordHandler = olePatronRecordHandler;
    }

    protected boolean checkDuplicateBarcode(String barcode) {
        boolean valid = false;
        Map barcodeMap = new HashMap();
        barcodeMap.put(OLEConstants.OlePatron.BARCODE, barcode);
        List<OlePatronDocument> olePatronDocuments = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, barcodeMap);
        if (olePatronDocuments.size() > 0) {
            for (OlePatronDocument olePatronDocument : olePatronDocuments) {
                if (barcode == null || !(barcode).equals(olePatronDocument.getOlePatronId())) {
                    /*this.putFieldError("dataObject.barcode", "error.barcode.duplicate");*/
                    valid = true;
                }
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("invalidOrLostBarcodeNumber", barcode);
        List<OlePatronLostBarcode> olePatronLostBarcodes = (List<OlePatronLostBarcode>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronLostBarcode.class, map);
        if (olePatronLostBarcodes.size() > 0) {
            for(OlePatronLostBarcode olePatronLostBarcode:olePatronLostBarcodes){
                if(olePatronLostBarcode.getInvalidOrLostBarcodeNumber()==null || !(olePatronLostBarcode.getInvalidOrLostBarcodeNumber().equalsIgnoreCase(barcode))){
                    valid =true;
                }
            }
        }
        return valid;
    }
}