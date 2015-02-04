package org.kuali.ole.deliver.api;

import org.apache.commons.lang.StringUtils;
import org.kuali.api.jaxb.DateAdapter;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.address.EntityAddress;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/24/12
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = org.kuali.ole.deliver.api.OleAddressDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = org.kuali.ole.deliver.api.OleAddressDefinition.Constants.TYPE_NAME, propOrder = {
        org.kuali.ole.deliver.api.OleAddressDefinition.Elements.OLE_ADDRESS_ID,
        org.kuali.ole.deliver.api.OleAddressDefinition.Elements.OLE_PTRN_ID,
        org.kuali.ole.deliver.api.OleAddressDefinition.Elements.ID,
        org.kuali.ole.deliver.api.OleAddressDefinition.Elements.ADDRESS_VERIFIED,
        org.kuali.ole.deliver.api.OleAddressDefinition.Elements.ADDRESS_VALID_FROM,
        org.kuali.ole.deliver.api.OleAddressDefinition.Elements.ADDRESS_VALID_TO,
        //OleAddressDefinition.Elements.ENTITY_ADDRESS,
        org.kuali.ole.deliver.api.OleAddressDefinition.Elements.ADDRESS_SOURCE,
        org.kuali.ole.deliver.api.OleAddressDefinition.Elements.ADDRESS_SOURCE_BO,
        //OlePatronDefinition.Elements.OLE_BORROWER_TYPE,
        //OlePatronDefinition.Elements.ENTITY,

        CoreConstants.CommonElements.VERSION_NUMBER,
        //CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class OleAddressDefinition extends AbstractDataTransferObject implements OleAddressContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.OLE_ADDRESS_ID, required = false)
    private final String oleAddressId;

    @XmlElement(name = Elements.OLE_PTRN_ID, required = false)
    private final String olePatronId;

    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.ADDRESS_VALID_FROM, required = false)
    @XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
    private final Date addressValidFrom;

    @XmlElement(name = Elements.ADDRESS_VALID_TO, required = false)
    @XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
    private final Date addressValidTo;

    @XmlElement(name = Elements.ADDRESS_VERIFIED, required = false)
    private final boolean addressVerified;

   /* @XmlElement(name = Elements.ENTITY_ADDRESS, required = false)
    private final EntityAddress entityAddress;*/

    @XmlElement(name = Elements.ADDRESS_SOURCE, required = false)
    private final String addressSource;

    @XmlElement(name = Elements.ADDRESS_SOURCE_BO, required = false)
    private final OleAddressSourceDefinition addressSourceBo;

    /* @XmlElement(name = Elements.EXPIRATION_DATE, required = false)
 @XmlJavaTypeAdapter(value=DateAdapter.class, type=Date.class)
 private final Date expirationDate;*/

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    /*@XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;*/
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OleAddressDefinition() {
        this.oleAddressId = null;
        this.addressVerified = false;
        this.olePatronId = null;
        this.id = null;
        this.addressValidFrom = null;
        this.addressValidTo = null;
        // this.entityAddress = null;
        this.addressSource = null;
        this.addressSourceBo = null;
        this.versionNumber = null;

        //this.objectId = null;
    }


    private OleAddressDefinition(Builder builder) {
        this.oleAddressId = builder.getOleAddressId();
        this.addressVerified = builder.isAddressVerified();
        this.olePatronId = builder.getOlePatronId();
        this.id = builder.getId();
        this.addressValidFrom = builder.getAddressValidFrom();
        this.addressValidTo = builder.getAddressValidTo();
        // this.entityAddress = builder.getEntityAddress().build();
        this.addressSource = builder.getAddressSource();
        if (builder.getAddressSourceBo() != null)
            this.addressSourceBo = builder.getAddressSourceBo().build();
        else
            this.addressSourceBo = new OleAddressSourceDefinition();
        this.versionNumber = builder.getVersionNumber();
        //this.objectId = builder.getObjectId();
    }

    @Override
    public String getOleAddressId() {
        return oleAddressId;
    }

    @Override
    public boolean isAddressVerified() {
        return addressVerified;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOlePatronId() {
        return olePatronId;
    }

    public Date getAddressValidFrom() {
        return addressValidFrom;
    }

    public Date getAddressValidTo() {
        return addressValidTo;
    }

   /* public EntityAddress getEntityAddress() {
        return entityAddress;
    }*/

    public String getAddressSource() {
        return addressSource;
    }

    public OleAddressSourceDefinition getAddressSourceBo() {
        return addressSourceBo;
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
    public String getId() {
        return this.id;
    }


    //@Override
    /*public OleBorrowerTypeDefinition getOleBorrowerType() {
        return this.oleBorrowerType;
    }*/

    public static class Builder
            implements Serializable, ModelBuilder, OleAddressContract {
        private String oleAddressId;
        private String olePatronId;
        private String id;
        private boolean addressVerified;
        private Date addressValidFrom;
        private Date addressValidTo;
        private String addressSource;
        private OleAddressSourceDefinition.Builder addressSourceBo;
        private EntityAddress.Builder entityAddress;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OleAddressContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getOleAddressId() != null) {
                builder.setOleAddressId(contract.getOleAddressId());
            }
            if (contract.isAddressVerified()) {
                builder.setAddressVerified(contract.isAddressVerified());
            }
            if (contract.getAddressValidFrom() != null) {
                builder.setAddressValidFrom(contract.getAddressValidFrom());
            }
            if (contract.getAddressValidTo() != null) {
                builder.setAddressValidTo(contract.getAddressValidTo());
            }
            if (contract.getOlePatronId() != null) {
                builder.setOlePatronId(contract.getOlePatronId());
            }
            if (contract.getId() != null) {
                builder.setId(contract.getId());
            }
            if (contract.getAddressSource() != null) {
                builder.setAddressSource(contract.getAddressSource());
            }
            if (contract.getAddressSourceBo() != null) {
                builder.setAddressSourceBo(OleAddressSourceDefinition.Builder.create(contract.getAddressSourceBo()));
            }
           /* if (contract.getEntityAddress() != null) {
                builder.setEntityAddress(EntityAddress.Builder.create(contract.getEntityAddress()));
            }*/
            builder.setVersionNumber(contract.getVersionNumber());
            /*builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());*/
            builder.setId(contract.getId());
            return builder;
        }


        public org.kuali.ole.deliver.api.OleAddressDefinition build() {
            return new org.kuali.ole.deliver.api.OleAddressDefinition(this);
        }

        public String getOleAddressId() {
            return oleAddressId;
        }

        public void setOleAddressId(String oleAddressId) {
            this.oleAddressId = oleAddressId;
        }

        public String getOlePatronId() {
            return olePatronId;
        }

        public void setOlePatronId(String olePatronId) {
            this.olePatronId = olePatronId;
        }

        public Date getAddressValidFrom() {
            return addressValidFrom;
        }

        public void setAddressValidFrom(Date addressValidFrom) {
            this.addressValidFrom = addressValidFrom;
        }

        public Date getAddressValidTo() {
            return addressValidTo;
        }

        public void setAddressValidTo(Date addressValidTo) {
            this.addressValidTo = addressValidTo;
        }

        public boolean isAddressVerified() {
            return addressVerified;
        }

        public void setAddressVerified(boolean addressVerified) {
            this.addressVerified = addressVerified;
        }

        public EntityAddress.Builder getEntityAddress() {
            return entityAddress;
        }

        public void setEntityAddress(EntityAddress.Builder entityAddress) {
            this.entityAddress = entityAddress;
        }

        public String getAddressSource() {
            return addressSource;
        }

        public void setAddressSource(String addressSource) {
            this.addressSource = addressSource;
        }

        public OleAddressSourceDefinition.Builder getAddressSourceBo() {
            return addressSourceBo;
        }

        public void setAddressSourceBo(OleAddressSourceDefinition.Builder addressSourceBo) {
            this.addressSourceBo = addressSourceBo;
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
            return this.id;
        }


        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.id = id;
        }
    }

    static class Constants {

        final static String ROOT_ELEMENT_NAME = "oleAddress";
        final static String TYPE_NAME = "OleAddressType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String OLE_ADDRESS_ID = "oleAddressId";
        //final static String ENTITY_ID = "entityId";
        final static String OLE_PTRN_ID = "olePatronId";
        final static String ID = "id";
        final static String ADDRESS_VERIFIED = "addressVerified";
        final static String ADDRESS_VALID_FROM = "addressValidFrom";
        final static String ADDRESS_VALID_TO = "addressValidTo";
        final static String ADDRESS_SOURCE = "addressSource";
        //final static String ENTITY_ADDRESS = "entityAddress";
        final static String ADDRESS_SOURCE_BO = "addressSourceBo";
    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
