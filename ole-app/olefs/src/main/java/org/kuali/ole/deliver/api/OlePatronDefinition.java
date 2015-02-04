package org.kuali.ole.deliver.api;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.api.jaxb.DateAdapter;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.kuali.rice.kim.api.identity.address.EntityAddressContract;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.api.identity.email.EntityEmailContract;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.kuali.rice.kim.api.identity.phone.EntityPhoneContract;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/24/12
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = OlePatronDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OlePatronDefinition.Constants.TYPE_NAME, propOrder = {
        OlePatronDefinition.Elements.PATRON_ID,
        OlePatronDefinition.Elements.BARCODE,
        OlePatronDefinition.Elements.BORROWER_TYPE,
        OlePatronDefinition.Elements.ACITVE_INICATOR,
        OlePatronDefinition.Elements.GENERAL_BLOCK,
        OlePatronDefinition.Elements.PAGING_PRIVILEGE,
        OlePatronDefinition.Elements.COURTESY_NOTICE,
        OlePatronDefinition.Elements.DELIVERYPRIVILEGE,
        OlePatronDefinition.Elements.EXPIRATION_DATE,
        OlePatronDefinition.Elements.ACTIVATION_DATE,
        OlePatronDefinition.Elements.OLE_BORROWER_TYPE,
        OlePatronDefinition.Elements.PHONES,
        OlePatronDefinition.Elements.ADDRESSES,
        OlePatronDefinition.Elements.NAME,
        OlePatronDefinition.Elements.EMAILS,
        OlePatronDefinition.Elements.NOTES,
        OlePatronDefinition.Elements.LOST_BARCODES,
        OlePatronDefinition.Elements.OLE_ENTITY_ADDRESSES,
        OlePatronDefinition.Elements.GENERAL_BLOCK_NOTES,
        OlePatronDefinition.Elements.PATRON_AFFILIATIONS,
        OlePatronDefinition.Elements.OLE_PROXY_PATRON_DOCS,
        // OlePatronDefinition.Elements.OLE_PATRON_DOCS,
        OlePatronDefinition.Elements.OLE_SOURCE,
        OlePatronDefinition.Elements.OLE_STATISTICAL_CAT,
        OlePatronDefinition.Elements.OLE_ADDRESSES,
        OlePatronDefinition.Elements.OLE_PATRON_LOCAL_IDS,
        CoreConstants.CommonElements.VERSION_NUMBER,
        //CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class OlePatronDefinition extends AbstractDataTransferObject implements OlePatronContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.PATRON_ID, required = false)
    private final String olePatronId;

    @XmlElement(name = Elements.BARCODE, required = false)
    private final String barcode;

    @XmlElement(name = Elements.BORROWER_TYPE, required = false)
    private final String borrowerType;

    @XmlElement(name = Elements.ACITVE_INICATOR, required = false)
    private final boolean activeIndicator;

    @XmlElement(name = Elements.GENERAL_BLOCK, required = false)
    private final boolean generalBlock;

    @XmlElement(name = Elements.PAGING_PRIVILEGE, required = false)
    private final boolean pagingPrivilege;

    @XmlElement(name = Elements.COURTESY_NOTICE, required = false)
    private final boolean courtesyNotice;

    @XmlElement(name = Elements.DELIVERYPRIVILEGE, required = false)
    private final boolean deliveryPrivilege;

    @XmlElement(name = Elements.EXPIRATION_DATE, required = false)
    @XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
    private final Date expirationDate;

    @XmlElement(name = Elements.ACTIVATION_DATE, required = false)
    @XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
    private final Date activationDate;

    @XmlElementWrapper(name = Elements.PHONES, required = false)
    @XmlElement(name = Elements.PHONE, required = false)
    private final List<EntityPhone> phones;

    @XmlElementWrapper(name = Elements.ADDRESSES, required = false)
    @XmlElement(name = Elements.ADDRESS, required = false)
    private final List<EntityAddress> addresses;

    @XmlElement(name = Elements.NAME, required = false)
    private final EntityName name;

    @XmlElementWrapper(name = Elements.EMAILS, required = false)
    @XmlElement(name = Elements.EMAIL, required = false)
    private final List<EntityEmail> emails;

    @XmlElementWrapper(name = Elements.NOTES, required = false)
    @XmlElement(name = Elements.NOTE, required = false)
    private final List<OlePatronNotesDefinition> notes;

    @XmlElementWrapper(name = Elements.LOST_BARCODES, required = false)
    @XmlElement(name = Elements.LOST_BARCODE, required = false)
    private final List<OlePatronLostBarcodeDefinition> lostBarcodes;

    @XmlElementWrapper(name = Elements.OLE_ENTITY_ADDRESSES, required = false)
    @XmlElement(name = Elements.OLE_ENTITY_ADDRESS, required = false)
    private final List<OleEntityAddressDefinition> oleEntityAddressBo;

    @XmlElementWrapper(name = Elements.PATRON_AFFILIATIONS, required = false)
    @XmlElement(name = Elements.PATRON_AFFILIATION, required = false)
    private final List<OlePatronAffiliationDefinition> patronAffiliations;

    @XmlElementWrapper(name = Elements.OLE_PROXY_PATRON_DOCS, required = false)
    @XmlElement(name = Elements.OLE_PROXY_PATRON_DOC, required = false)
    private final List<OleProxyPatronDefinition> oleProxyPatronDocuments;

    @XmlElementWrapper(name = Elements.OLE_ADDRESSES, required = false)
    @XmlElement(name = Elements.OLE_ADDRESS, required = false)
    private final List<OleAddressDefinition> oleAddresses;

    @XmlElement(name = Elements.GENERAL_BLOCK_NOTES, required = false)
    private final String generalBlockNotes;

    @XmlElement(name = Elements.OLE_SOURCE, required = false)
    private final String source;


    @XmlElement(name = Elements.OLE_STATISTICAL_CAT, required = false)
    private final String statisticalCategory;

    @XmlElementWrapper(name = Elements.OLE_PATRON_LOCAL_IDS, required = false)
    @XmlElement(name = Elements.OLE_PATRON_LOCAL_ID, required = false)
    private final List<OlePatronLocalIdentificationDefinition> olePatronLocalIds;

    @XmlElement(name = Elements.OLE_BORROWER_TYPE, required = false)
    private final OleBorrowerTypeDefinition oleBorrowerType;

    //@XmlElement(name = Elements.ENTITY, required = false)
    private final Entity entity;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    /*@XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;*/
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OlePatronDefinition() {
        this.olePatronId = null;
        this.barcode = null;
        this.borrowerType = null;
        this.activeIndicator = false;
        this.generalBlock = false;
        this.pagingPrivilege = false;
        this.courtesyNotice = false;
        this.deliveryPrivilege = false;
        this.expirationDate = null;
        this.activationDate = null;

        this.phones = null;
        this.addresses = null;
        this.name = null;
        this.emails = null;
        this.notes = null;
        this.lostBarcodes = null;
        this.oleEntityAddressBo = null;
        this.patronAffiliations = null;
        this.oleBorrowerType = null;
        this.entity = null;
        this.generalBlockNotes = null;
        this.oleProxyPatronDocuments = null;
        //this.olePatronDocuments = null;
        this.source = null;
        this.statisticalCategory = null;
        this.oleAddresses = null;
        this.olePatronLocalIds = null;
        this.versionNumber = null;
        //this.objectId = null;
    }


    private OlePatronDefinition(Builder builder) {
        this.olePatronId = builder.getOlePatronId();
        this.barcode = builder.getBarcode();
        this.borrowerType = builder.getBorrowerType();
        this.activeIndicator = builder.isActiveIndicator();
        this.generalBlock = builder.isGeneralBlock();
        this.pagingPrivilege = builder.isPagingPrivilege();
        this.courtesyNotice = builder.isCourtesyNotice();
        this.deliveryPrivilege = builder.isDeliveryPrivilege();
        this.expirationDate = builder.getExpirationDate();
        this.activationDate = builder.getActivationDate();
        this.generalBlockNotes = builder.getGeneralBlockNotes();
        this.source = builder.getSource();
        this.statisticalCategory = builder.getStatisticalCategory();
        this.oleBorrowerType = builder.getOleBorrowerType().build();

        this.addresses = new ArrayList<EntityAddress>();
        if (!CollectionUtils.isEmpty(builder.getAddresses())) {
            for (EntityAddress.Builder address : builder.getAddresses()) {
                this.addresses.add(address.build());
            }
        }

        this.emails = new ArrayList<EntityEmail>();
        if (!CollectionUtils.isEmpty(builder.getEmails())) {
            for (EntityEmail.Builder email : builder.getEmails()) {
                this.emails.add(email.build());
            }
        }
        this.phones = new ArrayList<EntityPhone>();
        if (!CollectionUtils.isEmpty(builder.getPhones())) {
            for (EntityPhone.Builder phoneNumber : builder.getPhones()) {
                this.phones.add(phoneNumber.build());
            }
        }
        this.name = builder.getName().build();

        this.notes = new ArrayList<OlePatronNotesDefinition>();
        if (!CollectionUtils.isEmpty(builder.getNotes())) {
            for (OlePatronNotesDefinition.Builder note : builder.getNotes()) {
                this.notes.add(note.build());
            }
        }

        this.lostBarcodes = new ArrayList<OlePatronLostBarcodeDefinition>();
        if (!CollectionUtils.isEmpty(builder.getLostBarcodes())) {
            for (OlePatronLostBarcodeDefinition.Builder lostBarcode : builder.getLostBarcodes()) {
                this.lostBarcodes.add(lostBarcode.build());
            }
        }

        this.oleEntityAddressBo = new ArrayList<OleEntityAddressDefinition>();
        if (!CollectionUtils.isEmpty(builder.getOleEntityAddressBo())) {
            for (OleEntityAddressDefinition.Builder oleEntityAddress : builder.getOleEntityAddressBo()) {
                this.oleEntityAddressBo.add(oleEntityAddress.build());
            }
        }

        this.patronAffiliations = new ArrayList<OlePatronAffiliationDefinition>();
        if (!CollectionUtils.isEmpty(builder.getPatronAffiliations())) {
            for (OlePatronAffiliationDefinition.Builder oleAffiliation : builder.getPatronAffiliations()) {
                this.patronAffiliations.add(oleAffiliation.build());
            }
        }

        this.oleProxyPatronDocuments = new ArrayList<OleProxyPatronDefinition>();
        if (!CollectionUtils.isEmpty(builder.getOleProxyPatronDocuments())) {
            for (OleProxyPatronDefinition.Builder oleProxyPatron : builder.getOleProxyPatronDocuments()) {
                this.oleProxyPatronDocuments.add(oleProxyPatron.build());
            }
        }

        this.oleAddresses = new ArrayList<OleAddressDefinition>();
        if (!CollectionUtils.isEmpty(builder.getOleAddresses())) {
            for (OleAddressDefinition.Builder oleAddress : builder.getOleAddresses()) {
                this.oleAddresses.add(oleAddress.build());
            }
        }

        this.olePatronLocalIds = new ArrayList<OlePatronLocalIdentificationDefinition>();
        if (!CollectionUtils.isEmpty(builder.getOlePatronLocalIds())) {
            for (OlePatronLocalIdentificationDefinition.Builder olePatronLocalIds : builder.getOlePatronLocalIds()) {
                this.olePatronLocalIds.add(olePatronLocalIds.build());
            }
        }

        this.entity = builder.getEntity().build();

        this.versionNumber = builder.getVersionNumber();
        //this.objectId = builder.getObjectId();
    }

    @Override
    public String getOlePatronId() {
        return this.olePatronId;
    }

    @Override
    public String getBarcode() {
        return this.barcode;
    }

    @Override
    public String getBorrowerType() {
        return this.borrowerType;
    }

    @Override
    public boolean isActiveIndicator() {
        return this.activeIndicator;
    }

    @Override
    public boolean isGeneralBlock() {
        return this.generalBlock;
    }

    @Override
    public boolean isPagingPrivilege() {
        return this.pagingPrivilege;
    }

    @Override
    public boolean isCourtesyNotice() {
        return this.courtesyNotice;
    }

    @Override
    public boolean isDeliveryPrivilege() {
        return this.deliveryPrivilege;
    }

    @Override
    public Date getExpirationDate() {
        return this.expirationDate;
    }

    @Override
    public Date getActivationDate() {
        return this.activationDate;
    }

    @Override
    public List<EntityAddress> getAddresses() {
        return this.addresses;
    }

    @Override
    public List<EntityEmail> getEmails() {
        return this.emails;
    }

    @Override
    public EntityName getName() {
        return this.name;
    }

    @Override
    public List<EntityPhone> getPhones() {
        return this.phones;
    }

    @Override
    public List<OleEntityAddressDefinition> getOleEntityAddressBo() {
        return this.oleEntityAddressBo;
    }

    @Override
    public List<OlePatronAffiliationDefinition> getPatronAffiliations() {
        return this.patronAffiliations;
    }

    @Override
    public List<OleProxyPatronDefinition> getOleProxyPatronDocuments() {
        return this.oleProxyPatronDocuments;
    }

    @Override
    public List<OleAddressDefinition> getOleAddresses() {
        return this.oleAddresses;
    }

    @Override
    public List<OlePatronLocalIdentificationDefinition> getOlePatronLocalIds() {
        return this.olePatronLocalIds;
    }

    @Override
    public String getGeneralBlockNotes() {
        return this.generalBlockNotes;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public String getStatisticalCategory() {
        return this.statisticalCategory;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public String getId() {
        return this.olePatronId;
    }

    /* @Override
    public boolean isActive() {
        return this.activeIndicator;
    }*/

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public List<OlePatronNotesDefinition> getNotes() {
        return this.notes;
    }

    @Override
    public List<OlePatronLostBarcodeDefinition> getLostBarcodes() {
        return this.lostBarcodes;
    }

    //@Override
    public OleBorrowerTypeDefinition getOleBorrowerType() {
        return this.oleBorrowerType;
    }

    public static class Builder
            implements Serializable, ModelBuilder, OlePatronContract {
        private String olePatronId;
        private String barcode;
        private String borrowerType;
        private boolean activeIndicator;
        private boolean generalBlock;
        private boolean pagingPrivilege;
        private boolean courtesyNotice;
        private boolean deliveryPrivilege;
        private Date expirationDate;
        private Date activationDate;
        private Date invalidBarcodeNumEffDate;
        private String generalBlockNotes;
        private String source;
        private String statisticalCategory;
        private String addressSource;

        private List<EntityPhone.Builder> phones;
        private List<EntityAddress.Builder> addresses;
        private EntityName.Builder name;
        private List<EntityEmail.Builder> emails;
        private List<OlePatronNotesDefinition.Builder> notes;
        private List<OlePatronLostBarcodeDefinition.Builder> lostBarcodes;
        private OleBorrowerTypeDefinition.Builder oleBorrowerType;
        private OleSourceDefinition.Builder sourceBo;
        private OleStatisticalCategoryDefinition.Builder statisticalCategoryBo;
        private Entity.Builder entity;
        private List<OleEntityAddressDefinition.Builder> oleEntityAddressBo;
        private List<OlePatronAffiliationDefinition.Builder> patronAffiliations;
        private List<OleProxyPatronDefinition.Builder> oleProxyPatronDocuments;
        private List<Builder> olePatronDocuments;
        private List<OlePatronLocalIdentificationDefinition.Builder> olePatronLocalIds;
        private List<OleAddressDefinition.Builder> oleAddresses;

        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OlePatronContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getOlePatronId() != null) {
                builder.setOlePatronId(contract.getOlePatronId());
            }
            if (contract.getBarcode() != null) {
                builder.setBarcode(contract.getBarcode());
            }
            if (contract.getBorrowerType() != null) {
                builder.setBorrowerType(contract.getBorrowerType());
            }
            if (contract.isActiveIndicator()) {
                builder.setActiveIndicator(contract.isActiveIndicator());
                builder.setActive(contract.isActiveIndicator());
            }
            if (contract.isGeneralBlock()) {
                builder.setGeneralBlock(contract.isGeneralBlock());
            }
            if (contract.isDeliveryPrivilege()) {
                builder.setDeliveryPrivilege(contract.isDeliveryPrivilege());
            }
            if (contract.isPagingPrivilege()) {
                builder.setPagingPrivilege(contract.isPagingPrivilege());
            }
            if (contract.isCourtesyNotice()) {
                builder.setCourtesyNotice(contract.isCourtesyNotice());
            }
            if (contract.getExpirationDate() != null) {
                builder.setExpirationDate(contract.getExpirationDate());
            }
            if (contract.getActivationDate() != null) {
                builder.setActivationDate(contract.getActivationDate());
            }
            if (contract.getGeneralBlockNotes() != null) {
                builder.setGeneralBlockNotes(contract.getGeneralBlockNotes());
            }
            if (contract.getSource() != null) {
                builder.setSource(contract.getSource());
            }
            if (contract.getStatisticalCategory() != null) {
                builder.setStatisticalCategory(contract.getStatisticalCategory());
            }
            if (contract.getEntity() != null) {
                builder.setEntity(Entity.Builder.create(contract.getEntity()));
            }
            builder.addresses = new ArrayList<EntityAddress.Builder>();
            if (!CollectionUtils.isEmpty(contract.getAddresses())) {
                for (EntityAddressContract addressContract : contract.getAddresses()) {
                    builder.addresses.add(EntityAddress.Builder.create(addressContract));
                }
            } else if (contract.getEntity() != null) {
                if (!CollectionUtils.isEmpty(contract.getEntity().getEntityTypeContactInfos()) &&
                        !CollectionUtils.isEmpty(contract.getEntity().getEntityTypeContactInfos().get(0).getAddresses())) {
                    for (EntityAddressContract addressContract : contract.getEntity().getEntityTypeContactInfos().get(0).getAddresses()) {
                        builder.addresses.add(EntityAddress.Builder.create(addressContract));
                    }
                }

            }
            builder.emails = new ArrayList<EntityEmail.Builder>();
            if (!CollectionUtils.isEmpty(contract.getEmails())) {
                for (EntityEmailContract emailContract : contract.getEmails()) {
                    builder.emails.add(EntityEmail.Builder.create(emailContract));
                }
            } else if (contract.getEntity() != null) {
                if (!CollectionUtils.isEmpty(contract.getEntity().getEntityTypeContactInfos()) &&
                        !CollectionUtils.isEmpty(contract.getEntity().getEntityTypeContactInfos().get(0).getEmailAddresses())) {
                    for (EntityEmailContract emailContract : contract.getEntity().getEntityTypeContactInfos().get(0).getEmailAddresses()) {
                        builder.emails.add(EntityEmail.Builder.create(emailContract));
                    }
                }

            }
            builder.phones = new ArrayList<EntityPhone.Builder>();
            if (!CollectionUtils.isEmpty(contract.getPhones())) {
                for (EntityPhoneContract phoneContract : contract.getPhones()) {
                    builder.phones.add(EntityPhone.Builder.create(phoneContract));
                }
            } else if (contract.getEntity() != null) {
                if (!CollectionUtils.isEmpty(contract.getEntity().getEntityTypeContactInfos()) &&
                        !CollectionUtils.isEmpty(contract.getEntity().getEntityTypeContactInfos().get(0).getPhoneNumbers())) {
                    for (EntityPhoneContract phoneContract : contract.getEntity().getEntityTypeContactInfos().get(0).getPhoneNumbers()) {
                        builder.phones.add(EntityPhone.Builder.create(phoneContract));
                    }
                }

            }
            builder.oleEntityAddressBo = new ArrayList<OleEntityAddressDefinition.Builder>();
            if (!CollectionUtils.isEmpty(contract.getOleEntityAddressBo())) {
                for (OleEntityAddressContract oleEntityAddressContract : contract.getOleEntityAddressBo()) {
                    builder.oleEntityAddressBo.add(OleEntityAddressDefinition.Builder.create(oleEntityAddressContract));
                }
            }
            builder.oleAddresses = new ArrayList<OleAddressDefinition.Builder>();
            if (!CollectionUtils.isEmpty(contract.getOleAddresses())) {
                for (OleAddressContract oleAddressContract : contract.getOleAddresses()) {
                    builder.oleAddresses.add(OleAddressDefinition.Builder.create(oleAddressContract));
                }
            }
            if (contract.getName() != null && contract.getName().getFirstName() != null) {
                builder.setName(EntityName.Builder.create(contract.getName()));
            } else if (contract.getEntity() != null) {
                if (!CollectionUtils.isEmpty(contract.getEntity().getNames())) {
                    builder.setName(EntityName.Builder.create(contract.getEntity().getNames().get(0)));
                }

            }
            if (contract.getOleBorrowerType() != null) {
                builder.setOleBorrowerType(OleBorrowerTypeDefinition.Builder.create(contract.getOleBorrowerType()));
            }
            builder.notes = new ArrayList<OlePatronNotesDefinition.Builder>();
            if (!CollectionUtils.isEmpty(contract.getNotes())) {
                for (OlePatronNotesContract noteContract : contract.getNotes()) {
                    builder.notes.add(OlePatronNotesDefinition.Builder.create(noteContract));
                }
            }
            builder.lostBarcodes = new ArrayList<OlePatronLostBarcodeDefinition.Builder>();
            if (!CollectionUtils.isEmpty(contract.getLostBarcodes())) {
                for (OlePatronLostBarcodeContract lostBarcodeContract : contract.getLostBarcodes()) {
                    builder.lostBarcodes.add(OlePatronLostBarcodeDefinition.Builder.create(lostBarcodeContract));
                }
            }
            builder.patronAffiliations = new ArrayList<OlePatronAffiliationDefinition.Builder>();
            if (!CollectionUtils.isEmpty(contract.getPatronAffiliations())) {
                for (OlePatronAffiliationContract affiliationContract : contract.getPatronAffiliations()) {
                    builder.patronAffiliations.add(OlePatronAffiliationDefinition.Builder.create(affiliationContract));
                }
            }

            builder.oleProxyPatronDocuments = new ArrayList<OleProxyPatronDefinition.Builder>();
            if (!CollectionUtils.isEmpty(contract.getOleProxyPatronDocuments())) {
                for (OleProxyPatronContract proxyContract : contract.getOleProxyPatronDocuments()) {
                    builder.oleProxyPatronDocuments.add(OleProxyPatronDefinition.Builder.create(proxyContract));
                }
            }
            builder.olePatronLocalIds = new ArrayList<OlePatronLocalIdentificationDefinition.Builder>();
            if (!CollectionUtils.isEmpty(contract.getOlePatronLocalIds())) {
                for (OlePatronLocalIdentificationContract patornLocalIdContract : contract.getOlePatronLocalIds()) {
                    builder.olePatronLocalIds.add(OlePatronLocalIdentificationDefinition.Builder.create(patornLocalIdContract));
                }
            }
            builder.setVersionNumber(contract.getVersionNumber());
            /*builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());*/
            builder.setId(contract.getId());
            return builder;
        }


        public OlePatronDefinition build() {
            return new OlePatronDefinition(this);
        }

        @Override
        public String getOlePatronId() {
            return olePatronId;
        }

        public void setOlePatronId(String olePatronId) {
            this.olePatronId = olePatronId;
        }

        @Override
        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        @Override
        public String getBorrowerType() {
            return borrowerType;
        }


        public void setBorrowerType(String borrowerType) {
            this.borrowerType = borrowerType;
        }

        @Override
        public boolean isActiveIndicator() {
            return activeIndicator;
        }

        public void setActiveIndicator(boolean activeIndicator) {
            this.activeIndicator = activeIndicator;
        }

        @Override
        public boolean isGeneralBlock() {
            return generalBlock;
        }

        public void setGeneralBlock(boolean generalBlock) {
            this.generalBlock = generalBlock;
        }

        @Override
        public boolean isPagingPrivilege() {
            return pagingPrivilege;
        }

        public void setPagingPrivilege(boolean pagingPrivilege) {
            this.pagingPrivilege = pagingPrivilege;
        }

        @Override
        public boolean isCourtesyNotice() {
            return courtesyNotice;
        }

        public void setCourtesyNotice(boolean courtesyNotice) {
            this.courtesyNotice = courtesyNotice;
        }

        @Override
        public boolean isDeliveryPrivilege() {
            return deliveryPrivilege;
        }

        public void setDeliveryPrivilege(boolean deliveryPrivilege) {
            this.deliveryPrivilege = deliveryPrivilege;
        }

        @Override
        public Date getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
        }

        @Override
        public Date getActivationDate() {
            return activationDate;
        }

        public void setActivationDate(Date activationDate) {
            this.activationDate = activationDate;
        }

        @Override
        public List<EntityPhone.Builder> getPhones() {
            return phones;
        }

        public void setPhones(List<EntityPhone.Builder> phones) {
            this.phones = phones;
        }

        @Override
        public List<EntityAddress.Builder> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<EntityAddress.Builder> addresses) {
            this.addresses = addresses;
        }

        public List<OleProxyPatronDefinition.Builder> getOleProxyPatronDocuments() {
            return oleProxyPatronDocuments;
        }

        public void setOleProxyPatronDocuments(List<OleProxyPatronDefinition.Builder> oleProxyPatronDocuments) {
            this.oleProxyPatronDocuments = oleProxyPatronDocuments;
        }

        @Override
        public EntityName.Builder getName() {
            return name;
        }

        public void setName(EntityName.Builder name) {
            this.name = name;
        }

        @Override
        public List<EntityEmail.Builder> getEmails() {
            return emails;
        }

        public void setEmails(List<EntityEmail.Builder> emails) {
            this.emails = emails;
        }


        public List<OlePatronNotesDefinition.Builder> getNotes() {
            return notes;
        }

        public void setNotes(List<OlePatronNotesDefinition.Builder> notes) {
            this.notes = notes;
        }

        public List<OlePatronLostBarcodeDefinition.Builder> getLostBarcodes() {
            return lostBarcodes;
        }

        public void setLostBarcodes(List<OlePatronLostBarcodeDefinition.Builder> lostBarcodes) {
            this.lostBarcodes = lostBarcodes;
        }

        public OleBorrowerTypeDefinition.Builder getOleBorrowerType() {
            return oleBorrowerType;
        }

        public void setOleBorrowerType(OleBorrowerTypeDefinition.Builder oleBorrowerType) {
            this.oleBorrowerType = oleBorrowerType;
        }

        public Date getInvalidBarcodeNumEffDate() {
            return invalidBarcodeNumEffDate;
        }

        public void setInvalidBarcodeNumEffDate(Date invalidBarcodeNumEffDate) {
            this.invalidBarcodeNumEffDate = invalidBarcodeNumEffDate;
        }

        public String getGeneralBlockNotes() {
            return generalBlockNotes;
        }

        public void setGeneralBlockNotes(String generalBlockNotes) {
            this.generalBlockNotes = generalBlockNotes;
        }

        @Override
        public Entity.Builder getEntity() {
            return entity;
        }

        public void setEntity(Entity.Builder entity) {
            this.entity = entity;
        }

        public List<OleEntityAddressDefinition.Builder> getOleEntityAddressBo() {
            return oleEntityAddressBo;
        }

        public void setOleEntityAddressBo(List<OleEntityAddressDefinition.Builder> oleEntityAddressBo) {
            this.oleEntityAddressBo = oleEntityAddressBo;
        }

        public List<OlePatronAffiliationDefinition.Builder> getPatronAffiliations() {
            return patronAffiliations;
        }

        public void setPatronAffiliations(List<OlePatronAffiliationDefinition.Builder> patronAffiliations) {
            this.patronAffiliations = patronAffiliations;
        }

        public List<OlePatronLocalIdentificationDefinition.Builder> getOlePatronLocalIds() {
            return olePatronLocalIds;
        }

        public void setOlePatronLocalIds(List<OlePatronLocalIdentificationDefinition.Builder> olePatronLocalIds) {
            this.olePatronLocalIds = olePatronLocalIds;
        }

        public List<Builder> getOlePatronDocuments() {
            return olePatronDocuments;
        }

        public void setOlePatronDocuments(List<Builder> olePatronDocuments) {
            this.olePatronDocuments = olePatronDocuments;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getStatisticalCategory() {
            return statisticalCategory;
        }

        public void setStatisticalCategory(String statisticalCategory) {
            this.statisticalCategory = statisticalCategory;
        }

        public OleSourceDefinition.Builder getSourceBo() {
            return sourceBo;
        }

        public void setSourceBo(OleSourceDefinition.Builder sourceBo) {
            this.sourceBo = sourceBo;
        }

        public OleStatisticalCategoryDefinition.Builder getStatisticalCategoryBo() {
            return statisticalCategoryBo;
        }

        public void setStatisticalCategoryBo(OleStatisticalCategoryDefinition.Builder statisticalCategoryBo) {
            this.statisticalCategoryBo = statisticalCategoryBo;
        }

        public String getAddressSource() {
            return addressSource;
        }

        public void setAddressSource(String addressSource) {
            this.addressSource = addressSource;
        }

        public List<OleAddressDefinition.Builder> getOleAddresses() {
            return oleAddresses;
        }

        public void setOleAddresses(List<OleAddressDefinition.Builder> oleAddresses) {
            this.oleAddresses = oleAddresses;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        /*@Override
        public String getObjectId() {
            return objectId;
        }*/

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        @Override
        public String getId() {
            return this.olePatronId;
        }

        public boolean isActive() {
            return this.activeIndicator;
        }

        public void setActive(boolean active) {
            this.activeIndicator = active;
        }

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.olePatronId = id;
        }
    }

    static class Constants {

        final static String ROOT_ELEMENT_NAME = "olePatron";
        final static String TYPE_NAME = "OlePatronType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String PATRON_ID = "olePatronId";
        //final static String ENTITY_ID = "entityId";
        final static String BARCODE = "barcode";
        final static String BORROWER_TYPE = "borrowerType";
        final static String ACITVE_INICATOR = "activeIndicator";
        final static String GENERAL_BLOCK = "generalBlock";
        final static String PAGING_PRIVILEGE = "pagingPrivilege";
        final static String COURTESY_NOTICE = "courtesyNotice";
        final static String DELIVERYPRIVILEGE = "deliveryPrivilege";
        final static String EXPIRATION_DATE = "expirationDate";
        final static String ACTIVATION_DATE = "activationDate";

        final static String PHONES = "phones";
        final static String PHONE = "phone";
        final static String ADDRESSES = "addresses";
        final static String ADDRESS = "address";
        final static String NAME = "name";
        final static String EMAILS = "emails";
        final static String EMAIL = "email";
        final static String NOTES = "notes";
        final static String NOTE = "note";
        final static String LOST_BARCODES = "lostBarcodes";
        final static String LOST_BARCODE = "lostBarcode";
        final static String OLE_BORROWER_TYPE = "oleBorrowerType";
        final static String ENTITY = "entity";
        final static String OLE_ENTITY_ADDRESSES = "oleEntityAddressBo";
        final static String OLE_ENTITY_ADDRESS = "oleEntityAddress";
        final static String GENERAL_BLOCK_NOTES = "generalBlockNotes";
        final static String PATRON_AFFILIATIONS = "patronAffiliations";
        final static String PATRON_AFFILIATION = "patronAffiliation";
        final static String OLE_PROXY_PATRON_DOCS = "oleProxyPatronDocuments";
        final static String OLE_PROXY_PATRON_DOC = "oleProxyPatronDocument";
        //final static String OLE_PATRON_DOCS = "olePatronDocuments";
        //final static String OLE_PATRON_DOC = "olePatronDocument";
        final static String OLE_SOURCE = "source";
        final static String OLE_STATISTICAL_CAT = "statisticalCategory";
        final static String OLE_ADDRESSES = "oleAddresses";
        final static String OLE_ADDRESS = "oleAddress";
        final static String OLE_PATRON_LOCAL_IDS = "olePatronLocalIds";
        final static String OLE_PATRON_LOCAL_ID = "olePatronLocalId";

    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}