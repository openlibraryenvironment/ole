package org.kuali.ole.deliver.bo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.api.*;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationTypeBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * OlePatronDocument provides OlePatronDocument information through getter and setter.
 */
public class OlePatronDocument extends PersistableBusinessObjectBase implements OlePatronContract {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePatronDocument.class);

    private String olePatronId;
    private String patronRecordURL;
    private String barcode;
    private String borrowerType;
    private String affiliationType;
    private boolean activeIndicator;
    private boolean generalBlock;
    private boolean generalBlockPatrn;
    private String generalBlockNotes;
    private boolean pagingPrivilege;
    private boolean courtesyNotice;
    private boolean checkoutReceiptOptOut;
    private boolean deliveryPrivilege;
    private boolean realPatronCheck;
    private boolean selfCheckOut = false;
    private Date expirationDate;
    private Date activationDate;
    private String firstName;
    private String middleName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String borrowerTypeName;
    private String processMessage;
    private String source;
    private String statisticalCategory;
    private String oleSourceName;
    private String oleStatisticalCategoryName;
    private boolean patronBillFlag;
    private String proxyPatronId;
    private byte[] patronPhotograph;
    private boolean loanFlag;
    private boolean tempCircHistoryFlag;
    private boolean requestFlag;
    private boolean upload = true;
    private boolean patronHomePage;
    private boolean pointing;
    private boolean startingIndexExecuted;
    private boolean activateBarcode;
    private boolean deactivateBarcode;
    private String lostStatus;
    private String lostDescription;
    private String lostOperatorId;
    private boolean invalidateBarcode;
    private boolean userNoteExists;
    private boolean reinstateBarcode;
    private boolean skipBarcodeValidation;
    private boolean barcodeChanged;
    private boolean barcodeEditable;
    private boolean expirationFlag=true;
    private boolean popupDialog;
    private String patronMessage;
    private String uiMessageType;
    private boolean reinstated;
    private int numberOfClaimsReturned;
    private boolean showLoanedRecords;
    private boolean showRequestedItems;
    private boolean showTemporaryCirculationHistoryRecords;
    private boolean showBillUrlsFlag;
    private OlePatronDocument selectedProxyForPatron;
    private HashMap<String,String> errorsAndPermission = new HashMap<>();
    private String errorMessage;
    private boolean blockPatron;
    private String realPatronFirstName;

    private String realPatronLastName;
    private int loanCount;
    private int requestedItemRecordsCount;
    private int tempCirculationHistoryCount;

    private transient IdentityService identityService;
    private boolean lostPatron = false;
    private BusinessObjectService businessObjectService;
    private boolean addressVerified;
    private String preferredAddress;
    private String email;
    private boolean deleteImageFlag;
    private BigDecimal overdueFineAmt;
    private BigDecimal replacementFeeAmt;
    private BigDecimal allCharges;
    private List<FeeType> feeTypeList;
    public IdentityService getIdentityService() {
        if (identityService == null) {
            identityService = KimApiServiceLocator.getIdentityService();
        }
        return identityService;
    }

    public boolean isPatronHomePage() {
        return patronHomePage;
    }

    public boolean isExpirationFlag() {
        return expirationFlag;
    }

    public void setExpirationFlag(boolean expirationFlag) {
        this.expirationFlag = expirationFlag;
    }

    public void setPatronHomePage(boolean patronHomePage) {
        this.patronHomePage = patronHomePage;
    }

    private List<OleLoanDocument> oleLoanDocuments = new ArrayList<OleLoanDocument>();
    private List<EntityPhoneBo> phones = new ArrayList<EntityPhoneBo>();
    private List<EntityPhoneBo> deletedPhones = new ArrayList<EntityPhoneBo>();
    private List<EntityAddressBo> addresses = new ArrayList<EntityAddressBo>();
    private List<OleEntityAddressBo> oleEntityAddressBo = new ArrayList<OleEntityAddressBo>();
    private List<OleEntityAddressBo> deletedOleEntityAddressBo = new ArrayList<OleEntityAddressBo>();
    private EntityNameBo name = new EntityNameBo();
    private List<OleAddressBo> oleAddresses = new ArrayList<OleAddressBo>();
    private List<EntityEmailBo> emails = new ArrayList<EntityEmailBo>();
    private List<EntityEmailBo> deletedEmails = new ArrayList<EntityEmailBo>();
    private List<OlePatronNotes> notes = new ArrayList<OlePatronNotes>();
    private List<OlePatronNotes> deletedNotes = new ArrayList<OlePatronNotes>();
    private List<OlePatronLostBarcode> lostBarcodes = new ArrayList<OlePatronLostBarcode>();
    private OleBorrowerType oleBorrowerType;
    private EntityBo entity = new EntityBo();
    private OleSourceBo sourceBo;
    private OleStatisticalCategoryBo statisticalCategoryBo;
    private List<OlePatronAffiliation> patronAffiliations = new ArrayList<OlePatronAffiliation>();
    private List<OlePatronAffiliation> deletedPatronAffiliations = new ArrayList<OlePatronAffiliation>();
    private List<EntityEmploymentBo> employments = new ArrayList<EntityEmploymentBo>();
    private List<EntityEmploymentBo> deletedEmployments = new ArrayList<EntityEmploymentBo>();
    private List<OleDeliverRequestBo> oleDeliverRequestBos = new ArrayList<OleDeliverRequestBo>();
    private List<OleProxyPatronDocument> oleProxyPatronDocuments = new ArrayList<OleProxyPatronDocument>();
    private List<OleProxyPatronDocument> deletedOleProxyPatronDocuments = new ArrayList<OleProxyPatronDocument>();
    private List<OleTemporaryCirculationHistory> oleTemporaryCirculationHistoryRecords = new ArrayList<OleTemporaryCirculationHistory>();
    private List<OlePatronLocalIdentificationBo> olePatronLocalIds = new ArrayList<OlePatronLocalIdentificationBo>();
    private List<OlePatronLocalIdentificationBo> deletedOlePatronLocalIds = new ArrayList<OlePatronLocalIdentificationBo>();
    private List<OleProxyPatronDocument> oleProxyPatronDocumentList = new ArrayList<OleProxyPatronDocument>();
    private List<OlePatronDocument> currentPatronList = new ArrayList<OlePatronDocument>();
    private List<PatronBillPayment> patronBillPayments = new ArrayList<>();
    private OLEPatronEntityViewBo olePatronEntityViewBo;
    private String patronBillFileName;
    private String viewBillUrl;
    private String createBillUrl;
    private String namePrefix;
    private String nameSuffix;
    private OleDeliverRequestBo oleDeliverRequestBo;
    private boolean checkoutForSelf;
    private List<OlePatronNotes> olePatronUserNotes = new ArrayList<>();
    private List<OleEntityPhoneBo> oleEntityPhoneBo = new ArrayList<>();
    private List<OleEntityPhoneBo> deletedOleEntityPhoneBo = new ArrayList<>();
    private List<OlePhoneBo> olePhones = new ArrayList<>();
    private List<OleEntityEmailBo> oleEntityEmailBo = new ArrayList<>();
    private List<OleEntityEmailBo> deletedOleEntityEmailBo = new ArrayList<>();
    private List<OleEmailBo> oleEmails = new ArrayList<>();

    public HashMap<String, String> getErrorsAndPermission() {
        return errorsAndPermission;
    }

    public void setErrorsAndPermission(HashMap<String, String> errorsAndPermission) {
        this.errorsAndPermission = errorsAndPermission;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isBlockPatron() {
        return blockPatron;
    }

    public void setBlockPatron(boolean blockPatron) {
        this.blockPatron = blockPatron;
    }

    public void setBorrowerTypeName(String borrowerTypeName) {
        this.borrowerTypeName = borrowerTypeName;
    }

    private String patronName;

    private String borrowerTypeCode;

    public List<PatronBillPayment> getPatronBillPayments() {
        return patronBillPayments;
    }

    public void setPatronBillPayments(List<PatronBillPayment> patronBillPayments) {
        this.patronBillPayments = patronBillPayments;
    }

    public int getNumberOfClaimsReturned() {
        return numberOfClaimsReturned;
    }

    public void setNumberOfClaimsReturned(int numberOfClaimsReturned) {
        this.numberOfClaimsReturned = numberOfClaimsReturned;
    }

    public String getBorrowerTypeCode() {
        if (borrowerTypeCode == null) {
            borrowerTypeCode =  getOleBorrowerType().getBorrowerTypeCode();
        }
        return borrowerTypeCode;
    }

    public boolean isCheckoutReceiptOptOut() {
        return checkoutReceiptOptOut;
    }

    public void setCheckoutReceiptOptOut(boolean checkoutReceiptOptOut) {
        this.checkoutReceiptOptOut = checkoutReceiptOptOut;
    }

    public void setBorrowerTypeCode(String borrowerTypeCode) {
        this.borrowerTypeCode = borrowerTypeCode;
    }

    public boolean isSelfCheckOut() {
        return selfCheckOut;
    }

    public void setSelfCheckOut(boolean selfCheckOut) {
        this.selfCheckOut = selfCheckOut;
    }

    public String getPatronName() {
        if (null == patronName) {
            List<EntityNameBo> names = getEntity().getNames();
            if (CollectionUtils.isNotEmpty(names)) {
                EntityNameBo entityNameBo = names.get(0);
                patronName =  entityNameBo.getLastName() + "," + entityNameBo.getFirstName();
            }
        }
        return patronName;
    }

    public void setPatronName(String patronName) {
        this.patronName = patronName;
    }

    public OlePatronDocument() {
        this.setActivationDate(new Date());
        this.setDeliveryPrivilege(true);
        this.setPagingPrivilege(true);
        this.setCourtesyNotice(true);
        this.setBarcodeEditable(true);
    }

    public boolean isUserNoteExists() {
        return userNoteExists;
    }

    public void setUserNoteExists(boolean userNoteExists) {
        this.userNoteExists = userNoteExists;
    }

    public boolean isGeneralBlockPatrn() {
        return generalBlockPatrn;
    }

    public void setGeneralBlockPatrn(boolean generalBlockPatrn) {
        this.generalBlockPatrn = generalBlockPatrn;
    }

    public boolean isUpload() {
        return upload;
    }

    public void setUpload(boolean upload) {
        this.upload = upload;
    }

    public List<OleTemporaryCirculationHistory> getOleTemporaryCirculationHistoryRecords() {
        return oleTemporaryCirculationHistoryRecords;
    }

    public void setOleTemporaryCirculationHistoryRecords(List<OleTemporaryCirculationHistory> oleTemporaryCirculationHistoryRecords) {
        this.oleTemporaryCirculationHistoryRecords = oleTemporaryCirculationHistoryRecords;
    }

    public List<OleDeliverRequestBo> getOleDeliverRequestBos() {
        return oleDeliverRequestBos;
    }

    public void setOleDeliverRequestBos(List<OleDeliverRequestBo> oleDeliverRequestBos) {
        this.oleDeliverRequestBos = oleDeliverRequestBos;
    }

    /**
     * This method converts the PersistableBusinessObjectBase OlePatronDocument into immutable object OlePatronDefinition
     *
     * @param bo
     * @return OlePatronDefinition
     */
    public static OlePatronDefinition to(org.kuali.ole.deliver.bo.OlePatronDocument bo) {
        if (bo == null) {
            return null;
        }
        return OlePatronDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OlePatronDefinition into PersistableBusinessObjectBase OlePatronDocument
     *
     * @param immutable
     * @return OlePatronDocument
     */
    public static org.kuali.ole.deliver.bo.OlePatronDocument from(OlePatronDefinition immutable) {
        return fromAndUpdate(immutable, null);
    }

    /**
     * This method will set the PersistableBusinessObjectBase OlePatronDocument from immutable object OlePatronDefinition
     *
     * @param immutable
     * @param toUpdate
     * @return bo(OlePatronDocument)
     */
    static org.kuali.ole.deliver.bo.OlePatronDocument fromAndUpdate(OlePatronDefinition immutable, org.kuali.ole.deliver.bo.OlePatronDocument toUpdate) {
        if (immutable == null) {
            return null;
        }
        org.kuali.ole.deliver.bo.OlePatronDocument bo = toUpdate;
        if (toUpdate == null) {
            bo = new org.kuali.ole.deliver.bo.OlePatronDocument();
        }
        // bo.activeIndicator = immutable.isActiveIndicator();
        bo.olePatronId = immutable.getOlePatronId();

        bo.name = new EntityNameBo();

        if (immutable.getName() != null) {
            bo.name = EntityNameBo.from(immutable.getName());
        }

        bo.barcode = immutable.getBarcode();
        bo.borrowerType = immutable.getBorrowerType();
        bo.courtesyNotice = immutable.isCourtesyNotice();
        bo.generalBlock = immutable.isGeneralBlock();
        bo.deliveryPrivilege = immutable.isDeliveryPrivilege();
        bo.pagingPrivilege = immutable.isPagingPrivilege();
        bo.expirationDate = immutable.getExpirationDate();
        bo.activationDate = immutable.getActivationDate();
        bo.activeIndicator = immutable.isActiveIndicator();
        bo.generalBlockNotes = immutable.getGeneralBlockNotes();
        bo.source = immutable.getSource();
        bo.statisticalCategory = immutable.getStatisticalCategory();
        /* EntityBo entityBo ;

        if (null != bo.getEntity()) {
            entityBo = bo.getEntity();
            entityBo.setActive(true);
        } else {
            entityBo = new EntityBo();
            entityBo.setActive(true);
        }*/
        if (immutable.getEntity() != null) {
            bo.entity = EntityBo.from(immutable.getEntity());
        }
        if (immutable.getOleBorrowerType() != null) {
            bo.oleBorrowerType = OleBorrowerType.from(immutable.getOleBorrowerType());
        }
        if (CollectionUtils.isNotEmpty(immutable.getAddresses())) {
            for (EntityAddress entityAddr : immutable.getAddresses()) {
                if(bo.addresses == null){
                    bo.addresses = new ArrayList<>();
                }
                bo.addresses.add(EntityAddressBo.from(entityAddr));
            }
        }
        if (CollectionUtils.isNotEmpty(immutable.getEmails())) {
            for (EntityEmail entityEmail : immutable.getEmails()) {
                bo.emails.add(EntityEmailBo.from(entityEmail));
            }
        }
        if (CollectionUtils.isNotEmpty(immutable.getPhones())) {
            for (EntityPhone entityPhone : immutable.getPhones()) {
                bo.phones.add(EntityPhoneBo.from(entityPhone));
            }
        }

        if (CollectionUtils.isNotEmpty(immutable.getNotes())) {
            for (OlePatronNotesDefinition note : immutable.getNotes()) {
                if(bo.notes == null){
                    bo.notes = new ArrayList<>();
                }
                bo.notes.add(OlePatronNotes.from(note));
            }
        }
        if (CollectionUtils.isNotEmpty(immutable.getLostBarcodes())) {
            for (OlePatronLostBarcodeDefinition lostBarcode : immutable.getLostBarcodes()) {
                if(bo.lostBarcodes == null){
                    bo.lostBarcodes = new ArrayList<>();
                }
                bo.lostBarcodes.add(OlePatronLostBarcode.from(lostBarcode));
            }
        }
        if (CollectionUtils.isNotEmpty(immutable.getOleEntityAddressBo())) {
            for (OleEntityAddressDefinition address : immutable.getOleEntityAddressBo()) {
                bo.oleEntityAddressBo.add(OleEntityAddressBo.from(address));
            }
        }

        if(CollectionUtils.isNotEmpty(immutable.getOleEntityPhoneBo())) {
            for(OleEntityPhoneDefinition phone : immutable.getOleEntityPhoneBo()) {
                bo.oleEntityPhoneBo.add(OleEntityPhoneBo.from(phone));
            }
        }
        if(CollectionUtils.isNotEmpty(immutable.getOleEntityEmailBo())) {
            for(OleEntityEmailDefinition email : immutable.getOleEntityEmailBo()) {
                bo.oleEntityEmailBo.add(OleEntityEmailBo.from(email));
            }
        }
        if (CollectionUtils.isNotEmpty(immutable.getPatronAffiliations())) {
            for (OlePatronAffiliationDefinition affiliation : immutable.getPatronAffiliations()) {
                bo.patronAffiliations.add(OlePatronAffiliation.from(affiliation));
            }
        }
        if (CollectionUtils.isNotEmpty(immutable.getOleProxyPatronDocuments())) {
            for (OleProxyPatronDefinition proxyPatron : immutable.getOleProxyPatronDocuments()) {
                if(bo.oleProxyPatronDocuments ==  null){
                    bo.oleProxyPatronDocuments = new ArrayList<>();
                }
                bo.oleProxyPatronDocuments.add(OleProxyPatronDocument.from(proxyPatron));
            }
        }
        if (CollectionUtils.isNotEmpty(immutable.getOleAddresses())) {
            for (OleAddressDefinition oleAddress : immutable.getOleAddresses()) {
                if(bo.oleAddresses == null){
                    bo.oleAddresses  = new ArrayList<>();
                }
                bo.oleAddresses.add(OleAddressBo.from(oleAddress));
            }
        }
        if (CollectionUtils.isNotEmpty(immutable.getOlePhones())) {
            for (OlePhoneDefinition olePhone : immutable.getOlePhones()) {
                if(bo.olePhones == null) {
                    bo.olePhones = new ArrayList<>();
                }
                bo.olePhones.add(OlePhoneBo.from(olePhone));
            }
        }
        if (CollectionUtils.isNotEmpty(immutable.getOleEmails())) {
            for (OleEmailDefinition oleEmail : immutable.getOleEmails()) {
                if(bo.oleEmails == null) {
                    bo.oleEmails = new ArrayList<>();
                }
                bo.oleEmails.add(OleEmailBo.from(oleEmail));
            }
        }
        if (CollectionUtils.isNotEmpty(immutable.getOlePatronLocalIds())) {
            for (OlePatronLocalIdentificationDefinition olePatronLocalId : immutable.getOlePatronLocalIds()) {
                if(bo.olePatronLocalIds == null){
                    bo.olePatronLocalIds = new ArrayList<>();
                }
                bo.olePatronLocalIds.add(OlePatronLocalIdentificationBo.from(olePatronLocalId));
            }
        }

        bo.versionNumber = immutable.getVersionNumber();
        //bo.setObjectId(immutable.getObjectId());
        EntityBo entityBo = new EntityBo();
        entityBo.setActive(true);

        if (null != bo.getEntity()) {
            entityBo = bo.getEntity();
            entityBo.setActive(true);
        }
        if (CollectionUtils.isNotEmpty(immutable.getPatronAffiliations())) {
            for (OlePatronAffiliationDefinition affiliation : immutable.getPatronAffiliations()) {
                EntityAffiliationBo entityAffiliationBo = new EntityAffiliationBo();
                entityAffiliationBo.setId(affiliation.getEntityAffiliationId());
                entityAffiliationBo.setAffiliationTypeCode(affiliation.getAffiliationTypeCode());
                entityAffiliationBo.setAffiliationType(EntityAffiliationTypeBo.from(affiliation.getAffiliationType()));
                entityAffiliationBo.setActive(affiliation.isActive());
                entityAffiliationBo.setDefaultValue(affiliation.isDefaultValue());
                entityAffiliationBo.setCampusCode(affiliation.getCampusCode());
                entityAffiliationBo.setObjectId(affiliation.getObjectId());
                entityAffiliationBo.setVersionNumber(affiliation.getVersionNumber());
                entityAffiliationBo.setEntityId(affiliation.getEntityId());

                entityBo.getAffiliations().add(entityAffiliationBo);
                for (EntityEmployment entityEmployment : affiliation.getEmployments()) {
                    entityBo.getEmploymentInformation().add(EntityEmploymentBo.from(entityEmployment));
                }

                bo.patronAffiliations.add(OlePatronAffiliation.from(affiliation));
            }
        }
        entityBo.setNames(Arrays.asList(bo.getName()));
        EntityTypeContactInfoBo entityTypeContactInfoBo = new EntityTypeContactInfoBo();
        if (bo.getEntity() != null && null != bo.getEntity().getEntityTypeContactInfos() && bo.getEntity().getEntityTypeContactInfos().size() > 0) {
                entityTypeContactInfoBo = bo.getEntity().getEntityTypeContactInfos().get(0);
        }
        entityTypeContactInfoBo.setAddresses(bo.getAddresses());
        entityTypeContactInfoBo.setEmailAddresses(bo.getEmails());
        entityTypeContactInfoBo.setPhoneNumbers(bo.getPhones());
        entityTypeContactInfoBo.setEntityTypeCode(KimConstants.EntityTypes.PERSON);
        entityBo.setEntityTypeContactInfos(Arrays.asList(entityTypeContactInfoBo));
        bo.setEntity(entityBo);
        return bo;
    }

    public boolean isRealPatronCheck() {
        return realPatronCheck;
    }

    public void setRealPatronCheck(boolean realPatronCheck) {
        this.realPatronCheck = realPatronCheck;
    }

    /**
     * Gets the value of oleLoanDocuments which is a list of OleLoanDocument
     *
     * @return oleLoanDocuments(list of type OleLoanDocument)
     */
    public List<OleLoanDocument> getOleLoanDocuments() {
        return oleLoanDocuments;
    }

    /**
     * Sets the value for oleLoanDocuments which is a list of OleLoanDocument
     *
     * @param oleLoanDocuments(list of type OleLoanDocument)
     */
    public void setOleLoanDocuments(List<OleLoanDocument> oleLoanDocuments) {
        List<OleLoanDocument> LoanDocumentList = new ArrayList<>();
        List<OleLoanDocument> indefiniteLoanDocumentList = new ArrayList<>();
        for (OleLoanDocument loanDoc : oleLoanDocuments) {
            if (loanDoc.getLoanDueDate() != null && !(loanDoc.getLoanDueDate().toString().isEmpty())) {
                LoanDocumentList.add(loanDoc);
            } else {

                indefiniteLoanDocumentList.add(loanDoc);
            }
        }
        Collections.sort(LoanDocumentList, new Comparator<OleLoanDocument>() {
            public int compare(OleLoanDocument o1, OleLoanDocument o2) {
                return o1.getLoanDueDate().compareTo(o2.getLoanDueDate());
            }
        });
        LoanDocumentList.addAll(indefiniteLoanDocumentList);
        this.oleLoanDocuments = LoanDocumentList;
    }

    /**
     * Gets the value of borrowerTypeName property
     *
     * @return borrowerTypeName
     */
    public String getBorrowerTypeName() {
        if (getOleBorrowerType() != null) {
            return oleBorrowerType.getBorrowerTypeName();
        }
        return null;
    }

    /**
     * Gets the value of entity of type EntityBo
     *
     * @return entity(EntityBo)
     */
    public EntityBo getEntity() {
        if (getOlePatronId() != null) {
            EntityBo entityBo = (EntityBo) EntityBo.from( getIdentityService().getEntity(getOlePatronId()));
            if(entityBo!=null){
                return entityBo;
            }

            return entity;
        } else {
            return entity;
        }
    }

    /**
     * Sets the value for entity of type EntityBo
     *
     * @param entity(EntityBo)
     */
    public void setEntity(EntityBo entity) {
        this.entity = entity;
    }

    public String getAffiliationType() {
        return affiliationType;
    }

    public void setAffiliationType(String affiliationType) {
        this.affiliationType = affiliationType;
    }

    /**
     * Gets the value of notes which is a list of OlePatronNotes
     *
     * @return notes(list of type OlePatronNotes)
     */
    public List<OlePatronNotes> getNotes() {
        return notes;
    }

    /**
     * Sets the value for notes which is a list of OlePatronNotes
     *
     * @param notes(list of type OlePatronNotes)
     */
    public void setNotes(List<OlePatronNotes> notes) {
        this.notes = notes;
    }

    /**
     * Gets the value of barcode property
     *
     * @return barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Sets the value for barcode property
     *
     * @param barcode
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * Gets the value of borrowerType property
     *
     * @return borrowerType
     */
    public String getBorrowerType() {
        return borrowerType;
    }

    /**
     * Sets the value for borrowerType property
     *
     * @param borrowerType
     */
    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }

    /**
     * Gets the boolean value of activeIndicator property
     *
     * @return activeIndicator
     */
    public boolean isActiveIndicator() {
        return activeIndicator;
    }

    /**
     * Sets the boolean value for activeIndicator property
     *
     * @param activeIndicator
     */
    public void setActiveIndicator(boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
    }

    /**
     * Gets the value of olePatronId property
     *
     * @return olePatronId
     */
    public String getOlePatronId() {
        return olePatronId;
    }

    /**
     * Sets the value for olePatronId property
     *
     * @param olePatronId
     */
    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    /**
     * Gets the boolean value of generalBlock property
     *
     * @return generalBlock
     */
    @Override
    public boolean isGeneralBlock() {
        return generalBlock;
    }

    /**
     * Sets the boolean value for generalBlock property
     *
     * @param generalBlock
     */
    public void setGeneralBlock(boolean generalBlock) {
        this.generalBlock = generalBlock;
    }

    /**
     * Gets the boolean value of courtesyNotice property
     *
     * @return courtesyNotice
     */
    public boolean isCourtesyNotice() {
        return courtesyNotice;
    }

    /**
     * Sets the boolean value for courtesyNotice property
     *
     * @param courtesyNotice
     */
    public void setCourtesyNotice(boolean courtesyNotice) {
        this.courtesyNotice = courtesyNotice;
    }

    /**
     * Gets the boolean value of deliveryPrivilege property
     *
     * @return deliveryPrivilege
     */
    public boolean isDeliveryPrivilege() {
        return deliveryPrivilege;
    }

    /**
     * Sets the boolean value for deliveryPrivilege property
     *
     * @param deliveryPrivilege
     */
    public void setDeliveryPrivilege(boolean deliveryPrivilege) {
        this.deliveryPrivilege = deliveryPrivilege;
    }

    /**
     * Gets the boolean value of pagingPrivilege property
     *
     * @return pagingPrivilege
     */
    public boolean isPagingPrivilege() {
        return pagingPrivilege;
    }

    /**
     * Sets the boolean value for pagingPrivilege property
     *
     * @param pagingPrivilege
     */
    public void setPagingPrivilege(boolean pagingPrivilege) {
        this.pagingPrivilege = pagingPrivilege;
    }

    /**
     * Gets the value of expirationDate which is of type Date
     *
     * @return expirationDate(Date)
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the value for expirationDate which is of type Date
     *
     * @param expirationDate
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Gets the value of firstName property
     *
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value for firstName property
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the value of middleName property
     *
     * @return middleName
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the value for middleName property
     *
     * @param middleName
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * Gets the value of lastName property
     *
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value for lastName property
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the value of emailAddress property
     *
     * @return emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the value for emailAddress property
     *
     * @param emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the value of phoneNumber property
     *
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        if(null == phoneNumber){
            List<EntityTypeContactInfoBo> entityTypeContactInfos = getEntity().getEntityTypeContactInfos();
            if (CollectionUtils.isNotEmpty(entityTypeContactInfos)) {
                EntityTypeContactInfoBo entityTypeContactInfoBo = entityTypeContactInfos.get(0);
                if (entityTypeContactInfoBo!=null && entityTypeContactInfoBo.getPhoneNumbers() != null) {
                    for (int j = 0; j < entityTypeContactInfoBo.getPhoneNumbers().size(); j++) {
                        if (entityTypeContactInfoBo.getPhoneNumbers().get(j).getPhoneTypeCode().equalsIgnoreCase("HM")) {
                            phoneNumber = (entityTypeContactInfoBo.getPhoneNumbers().get(j).getPhoneNumber());
                        }
                    }
                }
            }
        }
        return phoneNumber;
    }

    /**
     * Sets the value for phoneNumber property
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the value of phones which is a list of EntityPhoneBo
     *
     * @return phones(list of type EntityPhoneBo)
     */
    public List<EntityPhoneBo> getPhones() {
        return phones;
    }

    /**
     * Sets the value for phones which is a list of EntityPhoneBo
     *
     * @param phones(list of type EntityPhoneBo)
     */
    public void setPhones(List<EntityPhoneBo> phones) {
        this.phones = phones;
    }

    /**
     * Gets the value of addresses which is a list of EntityAddressBo
     *
     * @return addresses(list of type EntityAddressBo)
     */
    public List<EntityAddressBo> getAddresses() {
        return addresses;
    }

    /**
     * Sets the value for addresses which is a list of EntityAddressBo
     *
     * @param addresses(list of type EntityAddressBo)
     */
    public void setAddresses(List<EntityAddressBo> addresses) {
        this.addresses = addresses;
    }

    /**
     * Gets the value of name which is of type EntityNameBo
     *
     * @return name(EntityNameBo)
     */
    public EntityNameBo getName() {
        return name;
    }

    /**
     * Sets the value for name which is of type EntityNameBo
     *
     * @param name(EntityNameBo)
     */
    public void setName(EntityNameBo name) {
        this.name = name;
    }

    /**
     * Gets the value of emails which is a list of EntityEmailBo
     *
     * @return emails(list of type EntityEmailBo)
     */
    public List<EntityEmailBo> getEmails() {
        return emails;
    }

    /**
     * Sets the value for emails which is a list of EntityEmailBo
     *
     * @param emails(list of type EntityEmailBo)
     */
    public void setEmails(List<EntityEmailBo> emails) {
        this.emails = emails;
    }

    /**
     * Gets the value of oleBorrowerType which is of type OleBorrowerType
     *
     * @return oleBorrowerType(OleBorrowerType)
     */
    public OleBorrowerType getOleBorrowerType() {
        return oleBorrowerType;
    }

    /**
     * Sets the value for oleBorrowerType which is of type OleBorrowerType
     *
     * @param oleBorrowerType(OleBorrowerType)
     */
    public void setOleBorrowerType(OleBorrowerType oleBorrowerType) {
        this.oleBorrowerType = oleBorrowerType;
    }

    /**
     * Gets the value of processMessage property
     *
     * @return processMessage
     */
    public String getProcessMessage() {
        return processMessage;
    }

    /**
     * Sets the value for processMessage property
     *
     * @param processMessage
     */
    public void setProcessMessage(String processMessage) {
        this.processMessage = processMessage;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap toStringMap = new LinkedHashMap();
        toStringMap.put("olePatronId", olePatronId);
        return toStringMap;
    }

    /**
     * Gets the value of olePatronId property
     *
     * @return olePatronId
     */
    @Override
    public String getId() {
        return this.getOlePatronId();
    }
    /* *//**
     * Gets the value of olePatronDocuments property
     * @return olePatronDocuments
     *//*
    public List<OlePatronDocument> getOlePatronDocuments() {
        return olePatronDocuments;
    }
    *//**
     * Sets the value for olePatronDocuments property
     * @param olePatronDocuments
     *//*
    public void setOlePatronDocuments(List<OlePatronDocument> olePatronDocuments) {
        this.olePatronDocuments = olePatronDocuments;
    }*/

    /**
     * Gets the value of proxyPatronId property
     *
     * @return proxyPatronId
     */
    public String getProxyPatronId() {
        return proxyPatronId;
    }

    /**
     * Sets the value for proxyPatronId property
     *
     * @param proxyPatronId
     */
    public void setProxyPatronId(String proxyPatronId) {
        this.proxyPatronId = proxyPatronId;
    }

    /**
     * Gets the value of oleProxyPatronDocuments property
     *
     * @return oleProxyPatronDocuments
     */
    public List<OleProxyPatronDocument> getOleProxyPatronDocuments() {
        return oleProxyPatronDocuments;
    }

    /**
     * Sets the value for oleProxyPatronDocuments property
     *
     * @param oleProxyPatronDocuments
     */
    public void setOleProxyPatronDocuments(List<OleProxyPatronDocument> oleProxyPatronDocuments) {
        this.oleProxyPatronDocuments = oleProxyPatronDocuments;
    }

    public OlePatronDocument getSelectedProxyForPatron() {
        return selectedProxyForPatron;
    }

    public void setSelectedProxyForPatron(OlePatronDocument selectedProxyForPatron) {
        this.selectedProxyForPatron = selectedProxyForPatron;
    }

    /**
     * Gets the value of activationDate property
     *
     * @return activationDate
     */
    public Date getActivationDate() {
        return activationDate;
    }

    /**
     * Sets the value for activationDate property
     *
     * @param activationDate
     */
    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    /**
     * Gets the value of generalBlockNotes property
     *
     * @return generalBlockNotes
     */
    public String getGeneralBlockNotes() {
        return generalBlockNotes;
    }

    /**
     * Sets the value for generalBlockNotes property
     *
     * @param generalBlockNotes
     */
    public void setGeneralBlockNotes(String generalBlockNotes) {
        this.generalBlockNotes = generalBlockNotes;
    }

    /**
     * Gets the value of oleEntityAddressBo property
     *
     * @return oleEntityAddressBo
     */
    public List<OleEntityAddressBo> getOleEntityAddressBo() {
        return oleEntityAddressBo;
    }

    /**
     * Sets the value for oleEntityAddressBo property
     *
     * @param oleEntityAddressBo
     */
    public void setOleEntityAddressBo(List<OleEntityAddressBo> oleEntityAddressBo) {
        this.oleEntityAddressBo = oleEntityAddressBo;
    }

    /**
     * Gets the value of patronAffiliations property
     *
     * @return patronAffiliations
     */
    public List<OlePatronAffiliation> getPatronAffiliations() {
        return patronAffiliations;
    }

    /**
     * Sets the value for patronAffiliations property
     *
     * @param patronAffiliations
     */
    public void setPatronAffiliations(List<OlePatronAffiliation> patronAffiliations) {
        this.patronAffiliations = patronAffiliations;
    }

    /**
     * Gets the value of sourceBo property
     *
     * @return sourceBo
     */
    public OleSourceBo getSourceBo() {
        return sourceBo;
    }

    /**
     * Sets the value for sourceBo property
     *
     * @param sourceBo
     */
    public void setSourceBo(OleSourceBo sourceBo) {
        this.sourceBo = sourceBo;
    }

    /**
     * Gets the value of statisticalCategoryBo property
     *
     * @return statisticalCategoryBo
     */
    public OleStatisticalCategoryBo getStatisticalCategoryBo() {
        return statisticalCategoryBo;
    }

    /**
     * Sets the value for statisticalCategoryBo property
     *
     * @param statisticalCategoryBo
     */
    public void setStatisticalCategoryBo(OleStatisticalCategoryBo statisticalCategoryBo) {
        this.statisticalCategoryBo = statisticalCategoryBo;
    }

    /**
     * Gets the value of employments property
     *
     * @return employments
     */
    public List<EntityEmploymentBo> getEmployments() {
        return employments;
    }

    public void setEmployments(List<EntityEmploymentBo> employments) {
        this.employments = employments;
    }

    /**
     * Gets the value of source property
     *
     * @return source
     */
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        if (source != null && source.equals("")) {
            this.source = null;
        } else {
            this.source = source;
        }
    }

    /**
     * Gets the value of statisticalCategory property
     *
     * @return statisticalCategory
     */
    public String getStatisticalCategory() {
        return statisticalCategory;
    }

    public void setStatisticalCategory(String statisticalCategory) {
        if (statisticalCategory != null && statisticalCategory.equals("")) {
            this.statisticalCategory = null;
        } else {
            this.statisticalCategory = statisticalCategory;
        }
    }

    /**
     * Gets the value of sourceName property
     *
     * @return sourceName
     */
    public String getOleSourceName() {
        if (sourceBo != null) {
            return sourceBo.getOleSourceName();
        }
        return null;
    }

    /**
     * Gets the value of statisticalCategoryName property
     *
     * @return statisticalCategoryName
     */
    public String getOleStatisticalCategoryName() {
        if (statisticalCategoryBo != null) {
            return statisticalCategoryBo.getOleStatisticalCategoryName();
        }
        return null;
    }

    /**
     * Gets the value of patronBillFlag property
     *
     * @return patronBillFlag
     */
    public boolean isPatronBillFlag() {
        return patronBillFlag;
    }

    /**
     * Sets the value for patronBillFlag property
     *
     * @param patronBillFlag
     */
    public void setPatronBillFlag(boolean patronBillFlag) {
        this.patronBillFlag = patronBillFlag;
    }

    public List<OlePatronLostBarcode> getLostBarcodes() {
        return lostBarcodes;
    }

    public void setLostBarcodes(List<OlePatronLostBarcode> lostBarcodes) {
        this.lostBarcodes = lostBarcodes;
    }

    public List<OleAddressBo> getOleAddresses() {
        return oleAddresses;
    }

    public void setOleAddresses(List<OleAddressBo> oleAddresses) {
        this.oleAddresses = oleAddresses;
    }

    public byte[] getPatronPhotograph() {
        return patronPhotograph;
    }

    public void setPatronPhotograph(byte[] patronPhotograph) {
        this.patronPhotograph = patronPhotograph;
    }

    public List<OlePatronLocalIdentificationBo> getOlePatronLocalIds() {
        return olePatronLocalIds;
    }

    public void setOlePatronLocalIds(List<OlePatronLocalIdentificationBo> olePatronLocalIds) {
        this.olePatronLocalIds = olePatronLocalIds;
    }

    public boolean isLoanFlag() {
        return loanFlag;
    }

    public void setLoanFlag(boolean loanFlag) {
        this.loanFlag = loanFlag;
    }

    public boolean isTempCircHistoryFlag() {
        return tempCircHistoryFlag;
    }

    public void setTempCircHistoryFlag(boolean tempCircHistoryFlag) {
        this.tempCircHistoryFlag = tempCircHistoryFlag;
    }

    public boolean isRequestFlag() {
        return requestFlag;
    }

    public void setRequestFlag(boolean requestFlag) {
        this.requestFlag = requestFlag;
    }

    public List<OleProxyPatronDocument> getOleProxyPatronDocumentList() {
        return oleProxyPatronDocumentList;
    }

    public void setOleProxyPatronDocumentList(List<OleProxyPatronDocument> oleProxyPatronDocumentList) {
        this.oleProxyPatronDocumentList = oleProxyPatronDocumentList;
    }

    public boolean isPointing() {
        return pointing;
    }

    public void setPointing(boolean pointing) {
        this.pointing = pointing;
    }

    public boolean isStartingIndexExecuted() {
        return startingIndexExecuted;
    }

    public void setStartingIndexExecuted(boolean startingIndexExecuted) {
        this.startingIndexExecuted = startingIndexExecuted;
    }

    public boolean isActivateBarcode() {
        return activateBarcode;
    }

    public void setActivateBarcode(boolean activateBarcode) {
        this.activateBarcode = activateBarcode;
    }

    public boolean isDeactivateBarcode() {
        return deactivateBarcode;
    }

    public void setDeactivateBarcode(boolean deactivateBarcode) {
        this.deactivateBarcode = deactivateBarcode;
    }

    public String getLostStatus() {
        return lostStatus;
    }

    public void setLostStatus(String lostStatus) {
        this.lostStatus = lostStatus;
    }

    public String getLostDescription() {
        return lostDescription;
    }

    public void setLostDescription(String lostDescription) {
        this.lostDescription = lostDescription;
    }

    public boolean isInvalidateBarcode() {
        return invalidateBarcode;
    }

    public void setInvalidateBarcode(boolean invalidateBarcode) {
        this.invalidateBarcode = invalidateBarcode;
    }

    public boolean isReinstateBarcode() {
        return reinstateBarcode;
    }

    public void setReinstateBarcode(boolean reinstateBarcode) {
        this.reinstateBarcode = reinstateBarcode;
    }

    public boolean isSkipBarcodeValidation() {
        return skipBarcodeValidation;
    }

    public void setSkipBarcodeValidation(boolean skipBarcodeValidation) {
        this.skipBarcodeValidation = skipBarcodeValidation;
    }

    public boolean isBarcodeChanged() {
        return barcodeChanged;
    }

    public void setBarcodeChanged(boolean barcodeChanged) {
        this.barcodeChanged = barcodeChanged;
    }

    public boolean isBarcodeEditable() {
        return barcodeEditable;
    }

    public void setBarcodeEditable(boolean barcodeEditable) {
        this.barcodeEditable = barcodeEditable;
    }

    public boolean isPopupDialog() {
        return popupDialog;
    }

    public void setPopupDialog(boolean popupDialog) {
        this.popupDialog = popupDialog;
    }

    public String getPatronMessage() {
        return patronMessage;
    }

    public void setPatronMessage(String patronMessage) {
        this.patronMessage = patronMessage;
    }

    public String getUiMessageType() {
        return uiMessageType;
    }

    public void setUiMessageType(String uiMessageType) {
        this.uiMessageType = uiMessageType;
    }

    public boolean isReinstated() {
        return reinstated;
    }

    public void setReinstated(boolean reinstated) {
        this.reinstated = reinstated;
    }

    public OLEPatronEntityViewBo getOlePatronEntityViewBo() {
        return olePatronEntityViewBo;
    }

    public void setOlePatronEntityViewBo(OLEPatronEntityViewBo olePatronEntityViewBo) {
        this.olePatronEntityViewBo = olePatronEntityViewBo;
    }

    public String getPatronBillFileName() {
        return patronBillFileName;
    }

    public void setPatronBillFileName(String patronBillFileName) {
        this.patronBillFileName = patronBillFileName;
    }

    public String getViewBillUrl() {
        return viewBillUrl;
    }

    public void setViewBillUrl(String viewBillUrl) {
        this.viewBillUrl = viewBillUrl;
    }

    public String getCreateBillUrl() {
        return createBillUrl;
    }

    public void setCreateBillUrl(String createBillUrl) {
        this.createBillUrl = createBillUrl;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public List<EntityPhoneBo> getDeletedPhones() {
        return deletedPhones;
    }

    public void setDeletedPhones(List<EntityPhoneBo> deletedPhones) {
        this.deletedPhones = deletedPhones;
    }

    public List<OleEntityAddressBo> getDeletedOleEntityAddressBo() {
        return deletedOleEntityAddressBo;
    }

    public void setDeletedOleEntityAddressBo(List<OleEntityAddressBo> deletedOleEntityAddressBo) {
        this.deletedOleEntityAddressBo = deletedOleEntityAddressBo;
    }

    public List<EntityEmailBo> getDeletedEmails() {
        return deletedEmails;
    }

    public void setDeletedEmails(List<EntityEmailBo> deletedEmails) {
        this.deletedEmails = deletedEmails;
    }

    public List<OleProxyPatronDocument> getDeletedOleProxyPatronDocuments() {
        return deletedOleProxyPatronDocuments;
    }

    public void setDeletedOleProxyPatronDocuments(List<OleProxyPatronDocument> deletedOleProxyPatronDocuments) {
        this.deletedOleProxyPatronDocuments = deletedOleProxyPatronDocuments;
    }

    public List<OlePatronAffiliation> getDeletedPatronAffiliations() {
        return deletedPatronAffiliations;
    }

    public void setDeletedPatronAffiliations(List<OlePatronAffiliation> deletedPatronAffiliations) {
        this.deletedPatronAffiliations = deletedPatronAffiliations;
    }

    public List<EntityEmploymentBo> getDeletedEmployments() {
        return deletedEmployments;
    }

    public void setDeletedEmployments(List<EntityEmploymentBo> deletedEmployments) {
        this.deletedEmployments = deletedEmployments;
    }

    public List<OlePatronNotes> getDeletedNotes() {
        return deletedNotes;
    }

    public void setDeletedNotes(List<OlePatronNotes> deletedNotes) {
        this.deletedNotes = deletedNotes;
    }

    public List<OlePatronLocalIdentificationBo> getDeletedOlePatronLocalIds() {
        return deletedOlePatronLocalIds;
    }

    public void setDeletedOlePatronLocalIds(List<OlePatronLocalIdentificationBo> deletedOlePatronLocalIds) {
        this.deletedOlePatronLocalIds = deletedOlePatronLocalIds;
    }

    public boolean isShowLoanedRecords() {
        return showLoanedRecords;
    }

    public void setShowLoanedRecords(boolean showLoanedRecords) {
        this.showLoanedRecords = showLoanedRecords;
    }

    public boolean isShowRequestedItems() {
        return showRequestedItems;
    }

    public void setShowRequestedItems(boolean showRequestedItems) {
        this.showRequestedItems = showRequestedItems;
    }

    public boolean isShowTemporaryCirculationHistoryRecords() {
        return showTemporaryCirculationHistoryRecords;
    }

    public void setShowTemporaryCirculationHistoryRecords(boolean showTemporaryCirculationHistoryRecords) {
        this.showTemporaryCirculationHistoryRecords = showTemporaryCirculationHistoryRecords;
    }

    public String getRealPatronFirstName() {
        if(entity!=null && entity.getNames()!=null && entity.getNames().size()>0){
            this.realPatronFirstName=entity.getNames().get(0).getFirstName().replaceAll("'", "\'");
        }
        return realPatronFirstName;
    }

    public void setRealPatronFirstName(String realPatronFirstName) {
        this.realPatronFirstName = realPatronFirstName;
    }

    public String getRealPatronLastName() {
        if(entity!=null && entity.getNames()!=null && entity.getNames().size()>0){
            this.realPatronLastName=entity.getNames().get(0).getLastName().replaceAll("'", "\'");
        }
        return realPatronLastName;
    }

    public void setRealPatronLastName(String realPatronLastName) {
        this.realPatronLastName = realPatronLastName;
    }

    public boolean isShowBillUrlsFlag() {
        return showBillUrlsFlag;
    }

    public void setShowBillUrlsFlag(boolean showBillUrlsFlag) {
        this.showBillUrlsFlag = showBillUrlsFlag;
    }

    public int getLoanCount() {
        return loanCount;
    }

    public void setLoanCount(int loanCount) {
        this.loanCount = loanCount;
    }

    public int getRequestedItemRecordsCount() {
        return requestedItemRecordsCount;
    }

    public void setRequestedItemRecordsCount(int requestedItemRecordsCount) {
        this.requestedItemRecordsCount = requestedItemRecordsCount;
    }

    public int getTempCirculationHistoryCount() {
        return tempCirculationHistoryCount;
    }

    public void setTempCirculationHistoryCount(int tempCirculationHistoryCount) {
        this.tempCirculationHistoryCount = tempCirculationHistoryCount;
    }

    public String getPatronRecordURL() {
        return patronRecordURL;
    }

    public void setPatronRecordURL(String patronRecordURL) {
        this.patronRecordURL = patronRecordURL;
    }

    public boolean isDeleteImageFlag() {
        return deleteImageFlag;
    }

    public void setDeleteImageFlag(boolean deleteImageFlag) {
        this.deleteImageFlag = deleteImageFlag;
    }

    public String getLostOperatorId() {
        return lostOperatorId;
    }

    public void setLostOperatorId(String lostOperatorId) {
        this.lostOperatorId = lostOperatorId;
    }

    public List<OleEntityPhoneBo> getOleEntityPhoneBo() {
        return oleEntityPhoneBo;
    }

    public void setOleEntityPhoneBo(List<OleEntityPhoneBo> oleEntityPhoneBo) {
        this.oleEntityPhoneBo = oleEntityPhoneBo;
    }

    public List<OleEntityPhoneBo> getDeletedOleEntityPhoneBo() {
        return deletedOleEntityPhoneBo;
    }

    public void setDeletedOleEntityPhoneBo(List<OleEntityPhoneBo> deletedOleEntityPhoneBo) {
        this.deletedOleEntityPhoneBo = deletedOleEntityPhoneBo;
    }

    public List<OlePhoneBo> getOlePhones() {
        return olePhones;
    }

    public void setOlePhones(List<OlePhoneBo> olePhones) {
        this.olePhones = olePhones;
    }

    public List<OleEntityEmailBo> getOleEntityEmailBo() {
        return oleEntityEmailBo;
    }

    public void setOleEntityEmailBo(List<OleEntityEmailBo> oleEntityEmailBo) {
        this.oleEntityEmailBo = oleEntityEmailBo;
    }

    public List<OleEntityEmailBo> getDeletedOleEntityEmailBo() {
        return deletedOleEntityEmailBo;
    }

    public void setDeletedOleEntityEmailBo(List<OleEntityEmailBo> deletedOleEntityEmailBo) {
        this.deletedOleEntityEmailBo = deletedOleEntityEmailBo;
    }

    public List<OleEmailBo> getOleEmails() {
        return oleEmails;
    }

    public void setOleEmails(List<OleEmailBo> oleEmails) {
        this.oleEmails = oleEmails;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public boolean isLostPatron() {
        LOG.debug("Inside the checkLostPatronBarcode method");
        Map barMap = new HashMap();
        barMap.put("invalidOrLostBarcodeNumber", barcode);
        List<OlePatronLostBarcode> olePatronLostBarcodes = (List<OlePatronLostBarcode>) getBusinessObjectService().findMatching(OlePatronLostBarcode.class, barMap);

        return olePatronLostBarcodes.size() > 0;
    }

    public boolean isAddressVerified() {
        Map<String, String> addressCriteria = new HashMap<String, String>();
        EntityBo entityBo = getEntity();
        if (null != entityBo) {
            List<EntityTypeContactInfoBo> entityTypeContactInfos = entityBo.getEntityTypeContactInfos();
            if (null != entityTypeContactInfos && entityTypeContactInfos.size() > 0) {
                List<EntityAddressBo> addresses = entityTypeContactInfos.get(0).getAddresses();
                if (addresses != null && addresses
                        .size() > 0) {
                    for (int address = 0; address < addresses.size(); address++) {
                        addressCriteria.put(OLEConstants.OlePatron.ENTITY_ADDRESS_ID, addresses.get(address).getId());
                        addressCriteria.put(OLEConstants.OlePatron.PATRON_ID, this.olePatronId);
                        List<OleAddressBo> oleAddressBos = (List<OleAddressBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleAddressBo.class, addressCriteria);
                        if (oleAddressBos != null && oleAddressBos.size() > 0 && addresses.get(address).isDefaultValue() && oleAddressBos.get(0).isAddressVerified()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public String getPreferredAddress() {
        StringBuilder stringBuilder = new StringBuilder();
        if (null == preferredAddress) {
            EntityTypeContactInfoBo entityTypeContactInfoBo = getEntity().getEntityTypeContactInfos().get(0);
            if (entityTypeContactInfoBo.getAddresses() != null) {
                for (EntityAddressBo entityAddressBo : entityTypeContactInfoBo.getAddresses()) {
                    if (entityAddressBo.isDefaultValue()) {
                        String address = "";
                        if (entityAddressBo.getLine1() != null) {
                            if (!entityAddressBo.getLine1().isEmpty()) {
                                stringBuilder.append(entityAddressBo.getLine1() + OLEConstants.COMMA);
                            }
                        }

                        if (entityAddressBo.getLine2() != null) {
                            if (!entityAddressBo.getLine2().isEmpty()) {
                                stringBuilder.append(entityAddressBo.getLine2() + OLEConstants.COMMA);
                            }
                        }

                        if (entityAddressBo.getLine3() != null) {
                            if (!entityAddressBo.getLine3().isEmpty()) {
                                stringBuilder.append(entityAddressBo.getLine3() + OLEConstants.COMMA);
                            }
                        }

                        if (entityAddressBo.getCity() != null) {
                            if (!entityAddressBo.getCity().isEmpty()) {
                                stringBuilder.append(entityAddressBo.getCity() + OLEConstants.COMMA);
                            }
                        }

                        if (entityAddressBo.getStateProvinceCode() != null) {
                            if (!entityAddressBo.getStateProvinceCode().isEmpty()) {
                                stringBuilder.append(entityAddressBo.getStateProvinceCode() + OLEConstants.COMMA);
                            }
                        }

                        if (entityAddressBo.getCountryCode() != null) {
                            if (!entityAddressBo.getCountryCode().isEmpty()) {
                                stringBuilder.append(entityAddressBo.getCountryCode() + OLEConstants.COMMA);
                            }
                        }

                        if (entityAddressBo.getPostalCode() != null) {
                            if (!entityAddressBo.getPostalCode().isEmpty()) {
                                stringBuilder.append(entityAddressBo.getPostalCode());
                            }
                        }
                        preferredAddress = stringBuilder.toString();
                        break;
                    }
                }
            }
        }
        return preferredAddress;
    }

    public String getEmail() {
        if (null == email) {
            EntityTypeContactInfoBo entityTypeContactInfoBo = getEntity().getEntityTypeContactInfos().get(0);
            if (entityTypeContactInfoBo.getEmailAddresses() != null) {
                for (EntityEmailBo entityEmailBo : entityTypeContactInfoBo.getEmailAddresses()) {
                    if (entityEmailBo.isDefaultValue()) {
                        email = entityEmailBo.getEmailAddress();
                        break;
                    }
                }
            }

        }
        return email;
    }

    public List<OlePatronDocument> getCurrentPatronList() {
        if (CollectionUtils.isEmpty(currentPatronList)) {
            currentPatronList.add(this);
        }
        return currentPatronList;
    }

    public void setCurrentPatronList(List<OlePatronDocument> currentPatronList) {
        this.currentPatronList = currentPatronList;
    }

    public int getLoanedItemsCountByItemType(String itemType) {
        Integer itemCount = 0;
        List<ItemRecord> itemRecords = getItemRecords();
        if (CollectionUtils.isNotEmpty(itemRecords)) {
            Boolean includeTowardsLoanCount = ParameterValueResolver.getInstance().getParameterAsBoolean(OLEConstants.APPL_ID_OLE, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CR_ITEMS_COUNT_TOWARD_LOANED_ITEMS_COUNT);
            for (ItemRecord itemRecord : itemRecords) {
                if (itemRecord != null && itemRecord.getItemTypeRecord().getCode().equalsIgnoreCase(itemType)
                        && (!itemRecord.getClaimsReturnedFlag() || (itemRecord.getClaimsReturnedFlag() && includeTowardsLoanCount))) {
                itemCount = itemCount + 1;
                }
            }
        }
        return itemCount;
    }

    private String getItemTypeFromItemId(String itemId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("barCode", itemId);
        List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, map);
        ItemRecord itemRecord = itemRecords.get(0);
        return itemRecord.getItemTypeRecord().getCode();
    }

    public int getTotalLoanedItemsCount() {
        return getOleLoanDocuments().size();
    }

    public String getRequestTypeCode(String itemUUID) {
        OleDeliverRequestBo prioritizedRequest = getPrioritizedRequest(itemUUID);
        return prioritizedRequest.getRequestTypeCode();
    }

    public OleDeliverRequestBo getPrioritizedRequest(String itemUuid) {
        LOG.debug("Inside the getPrioritizedRequest method");
        Map requestMap = new HashMap();
        requestMap.put("itemUuid", itemUuid);
        requestMap.put("borrowerQueuePosition", 1);
        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>) getBusinessObjectService().findMatching(OleDeliverRequestBo.class, requestMap);
        return oleDeliverRequestBos != null && oleDeliverRequestBos.size() > 0 ?
                oleDeliverRequestBos.get(0) : null;
    }

    public BigDecimal getAllCharges(String... feeTypes) {
        allCharges = new BigDecimal(0);
        if (feeTypes.length == 0) {
            BigDecimal overdueFineAmount = getFeeAmountBasedOnFeeType(OLEConstants.FEE_TYPE_CODE_OVERDUE);
            BigDecimal replacementFineAmount = getFeeAmountBasedOnFeeType(OLEConstants.FEE_TYPE_CODE_REPL_FEE);
            if (null != overdueFineAmount && null != replacementFineAmount) {
                allCharges = overdueFineAmount.add(replacementFineAmount);
            } else {
                allCharges =  new BigDecimal(0);
            }
        } else {
            for(String individualFeeType : feeTypes){
                BigDecimal feeAmountBasedOnFeeType = getFeeAmountBasedOnFeeType(individualFeeType);
                allCharges = allCharges.add(feeAmountBasedOnFeeType);
            }
        }
        return allCharges;
    }

    private BigDecimal getFeeAmountBasedOnFeeType(String feeType) {
        BigDecimal feeAmount = new BigDecimal(0);
        List<FeeType> feeTypeList = getPatronFeeTypes();
        if(CollectionUtils.isNotEmpty(feeTypeList)){
            for (Iterator<FeeType> iterator = feeTypeList.iterator(); iterator.hasNext(); ) {
                FeeType patronFeeType =  iterator.next();
                if (patronFeeType.getOleFeeType().getFeeTypeCode().equalsIgnoreCase(feeType) && !patronFeeType.getOlePaymentStatus().getPaymentStatusCode().equals(OLEConstants.SUSPENDED)) {
                    feeAmount = feeAmount.add(patronFeeType.getBalFeeAmount().bigDecimalValue());
                }
            }
        }
        return feeAmount;
    }

    public BigDecimal getOverdueFineAmount() {
        if (null == overdueFineAmt) {
            overdueFineAmt = getFeeAmountBasedOnFeeType(OLEConstants.FEE_TYPE_CODE_OVERDUE);
        }
        return overdueFineAmt;
    }

    public List<FeeType> getPatronFeeTypes() {
        if (null == feeTypeList) {
            feeTypeList = new ArrayList<FeeType>();
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("patronId", olePatronId);
            List<PatronBillPayment> patronBillPayments = (List<PatronBillPayment>) KRADServiceLocator.getBusinessObjectService().findMatching(PatronBillPayment.class, criteria);
            for (PatronBillPayment patronBillPayment : patronBillPayments) {
                feeTypeList.addAll(patronBillPayment.getFeeType());
            }
        }
        return feeTypeList;
    }

    public BigDecimal getReplacementFineAmount() {
        if (null == replacementFeeAmt) {
            replacementFeeAmt = getFeeAmountBasedOnFeeType(OLEConstants.FEE_TYPE_CODE_REPL_FEE);
        }
        return replacementFeeAmt;
    }

    public List<OleLoanDocument> getOleLoanDocumentsFromDb() {
        if (StringUtils.isNotEmpty(getOlePatronId())) {
            if (StringUtils.isNotEmpty(getOlePatronId())) {
                Map<String, String> parameterMap = new HashMap<>();
                parameterMap.put("patronId", getOlePatronId());
                List<OleLoanDocument> oleLoanDocumentList = (List<OleLoanDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleLoanDocument.class, parameterMap);
                if (CollectionUtils.isNotEmpty(oleLoanDocumentList)) {
                    oleLoanDocuments = oleLoanDocumentList;
                }
            }
        }
        return oleLoanDocuments;
    }

    public Boolean isOverDueDays(Integer allowedOverDueDays) {
        List<OleLoanDocument> oleLoanDocuments = getOleLoanDocuments();
        for (Iterator<OleLoanDocument> iterator = oleLoanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            if (oleLoanDocument.getLoanDueDate() != null) {
                Integer timeDiff = new Integer(getTimeDiff(oleLoanDocument.getLoanDueDate(), new Date()));
                if (timeDiff > allowedOverDueDays) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isRecalledAndOverDue(Integer days) {
        List<OleLoanDocument> oleLoanDocuments = getOleLoanDocuments();
        for (Iterator<OleLoanDocument> iterator = oleLoanDocuments.iterator(); iterator.hasNext(); ) {
            OleLoanDocument oleLoanDocument = iterator.next();
            if (oleLoanDocument.getLoanDueDate() != null) {
                Integer timeDiff = new Integer(getTimeDiff(oleLoanDocument.getLoanDueDate(), new Date()));
                if (timeDiff > days && recallRequestExists(oleLoanDocument)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean recallRequestExists(OleLoanDocument oleLoanDocument) {
        Map<String,String> itemMap=new HashMap<>();
        itemMap.put("itemId",oleLoanDocument.getItemId());
        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>) KRADServiceLocator.getBusinessObjectService().findMatching(OleDeliverRequestBo.class, itemMap);
        for (Iterator<OleDeliverRequestBo> iterator = oleDeliverRequestBos.iterator(); iterator.hasNext(); ) {
            OleDeliverRequestBo oleDeliverRequestBo = iterator.next();
            if(oleDeliverRequestBo.getOleDeliverRequestType().getRequestTypeCode().contains("Recall")){
                return true;
            }
        }
        return false;
    }

    public String getTimeDiff(Date dateOne, Date dateTwo) {
        String diff = "";
        long timeDiff = dateTwo.getTime() - dateOne.getTime();
        diff = String.format("%d", TimeUnit.MILLISECONDS.toDays(timeDiff),
                -TimeUnit.HOURS.toDays(timeDiff));
        return diff;
    }

    public boolean isCheckoutForSelf() {
        return checkoutForSelf;
    }

    public void setCheckoutForSelf(boolean checkoutForSelf) {
        this.checkoutForSelf = checkoutForSelf;
    }

    public List<OlePatronNotes> getOlePatronUserNotes() {
        List<OlePatronNotes> olePatronUserNoteList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(this.getNotes())) {
            String ptrnNoteTypes = ParameterValueResolver.getInstance().getParameter(OLEConstants
                    .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.PATRON_NOTE_TYPES_TO_DISPLAY);
            for(String note:ptrnNoteTypes.split(";")){
                for(OlePatronNotes olePatronNotes : this.getNotes()) {
                    if(olePatronNotes.getOlePatronNoteType() != null && note.equalsIgnoreCase(olePatronNotes.getOlePatronNoteType().getPatronNoteTypeCode())) {
                        olePatronUserNoteList.add(olePatronNotes);
                    }
                }
            }
            this.olePatronUserNotes = olePatronUserNoteList;
        }
        return olePatronUserNotes;
    }

    public void setOlePatronUserNotes(List<OlePatronNotes> olePatronUserNotes) {
        this.olePatronUserNotes = olePatronUserNotes;
    }

    @Override
    public List<Collection<PersistableBusinessObject>> buildListOfDeletionAwareLists() {
        List managedLists = super.buildListOfDeletionAwareLists();
        managedLists.add(getNotes());
        return managedLists;
    }

    public int getTotalOverdueLoanedItemsCount() {
        int overdueCount = 0;
        SimpleDateFormat date = new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT);
        List<ItemRecord> itemRecords = getItemRecords();
        if (CollectionUtils.isNotEmpty(itemRecords)) {
            Boolean includeTowardsLoanCount = ParameterValueResolver.getInstance().getParameterAsBoolean(OLEConstants.APPL_ID_OLE, OLEConstants
                    .DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.CR_ITEMS_COUNT_TOWARD_LOANED_ITEMS_COUNT);
            for (ItemRecord itemRecord : itemRecords) {
                try {
                    //checking overdue and claims returned flag
                    if (itemRecord.getDueDateTime() != null && itemRecord.getDueDateTime().before(date.parse(date.format(new Date())))
                            && (!itemRecord.getClaimsReturnedFlag() || (itemRecord.getClaimsReturnedFlag() && includeTowardsLoanCount))) {
                        overdueCount++;
                    }
                } catch (ParseException e) {
                    LOG.info(e.getMessage());
                }
            }
        }
        return overdueCount;
    }

    private List<ItemRecord> getItemRecords() {
        List<ItemRecord> itemRecords = new ArrayList<>();
        Set<String> itemIds = new HashSet<>();
        for (OleLoanDocument oleLoanDocument : oleLoanDocuments) {
            itemIds.add(oleLoanDocument.getItemId());
        }
        if (CollectionUtils.isNotEmpty(itemIds)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("barCode", itemIds);
            itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, map);
        }
        return itemRecords;
    }
}