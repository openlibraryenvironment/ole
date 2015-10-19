package org.kuali.ole.deliver.api;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.phone.EntityPhone;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by angelind on 10/8/15.
 */
@XmlRootElement(name = OlePhoneDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OlePhoneDefinition.Constants.TYPE_NAME, propOrder = {
        OlePhoneDefinition.Elements.OLE_PHONE_ID,
        OlePhoneDefinition.Elements.OLE_PTRN_ID,
        OlePhoneDefinition.Elements.ID,
        OlePhoneDefinition.Elements.PHONE_SOURCE,
        OlePhoneDefinition.Elements.OLE_PHONE_SOURCE_BO,

        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class OlePhoneDefinition extends AbstractDataTransferObject implements OlePhoneContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.OLE_PHONE_ID, required = false)
    private final String olePhoneId;

    @XmlElement(name = Elements.OLE_PTRN_ID, required = false)
    private final String olePatronId;

    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.PHONE_SOURCE, required = false)
    private final String phoneSource;

    @XmlElement(name = Elements.OLE_PHONE_SOURCE_BO, required = false)
    private final OleAddressSourceDefinition olePhoneSourceBo;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OlePhoneDefinition() {
        this.olePhoneId = null;
        this.olePatronId = null;
        this.id = null;
        this.phoneSource = null;
        this.olePhoneSourceBo = null;
        this.versionNumber = null;
    }

    private OlePhoneDefinition(Builder builder) {
        this.olePhoneId = builder.getOlePhoneId();
        this.olePatronId = builder.getOlePatronId();
        this.id = builder.getId();
        this.phoneSource = builder.getPhoneSource();
        if (builder.getOlePhoneSourceBo() != null)
            this.olePhoneSourceBo = builder.getOlePhoneSourceBo().build();
        else
            this.olePhoneSourceBo = new OleAddressSourceDefinition();
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
    public String getOlePhoneId() {
        return olePhoneId;
    }

    @Override
    public String getOlePatronId() {
        return olePatronId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getPhoneSource() {
        return phoneSource;
    }

    @Override
    public OleAddressSourceDefinition getOlePhoneSourceBo() {
        return olePhoneSourceBo;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    public static class Builder
            implements Serializable, ModelBuilder, OlePhoneContract {
        private String olePhoneId;
        private String olePatronId;
        private String id;
        private String phoneSource;
        private OleAddressSourceDefinition.Builder olePhoneSourceBo;
        private EntityPhone.Builder entityPhone;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OlePhoneContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getOlePhoneId() != null) {
                builder.setOlePhoneId(contract.getOlePhoneId());
            }
            if (contract.getOlePatronId() != null) {
                builder.setOlePatronId(contract.getOlePatronId());
            }
            if (contract.getId() != null) {
                builder.setId(contract.getId());
            }
            if (contract.getPhoneSource() != null) {
                builder.setPhoneSource(contract.getPhoneSource());
            }
            if (contract.getOlePhoneSourceBo() != null) {
                builder.setOlePhoneSourceBo(OleAddressSourceDefinition.Builder.create(contract.getOlePhoneSourceBo()));
            }
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setId(contract.getId());
            return builder;
        }


        public OlePhoneDefinition build() {
            return new OlePhoneDefinition(this);
        }

        public String getOlePhoneId() {
            return olePhoneId;
        }

        public void setOlePhoneId(String olePhoneId) {
            this.olePhoneId = olePhoneId;
        }

        public String getOlePatronId() {
            return olePatronId;
        }

        public void setOlePatronId(String olePatronId) {
            this.olePatronId = olePatronId;
        }

        public String getPhoneSource() {
            return phoneSource;
        }

        public void setPhoneSource(String phoneSource) {
            this.phoneSource = phoneSource;
        }

        public OleAddressSourceDefinition.Builder getOlePhoneSourceBo() {
            return olePhoneSourceBo;
        }

        public void setOlePhoneSourceBo(OleAddressSourceDefinition.Builder olePhoneSourceBo) {
            this.olePhoneSourceBo = olePhoneSourceBo;
        }

        public EntityPhone.Builder getEntityPhone() {
            return entityPhone;
        }

        public void setEntityPhone(EntityPhone.Builder entityPhone) {
            this.entityPhone = entityPhone;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

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

        final static String ROOT_ELEMENT_NAME = "olePhone";
        final static String TYPE_NAME = "OlePhoneType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String OLE_PHONE_ID = "olePhoneId";
        final static String OLE_PTRN_ID = "olePatronId";
        final static String ID = "id";
        final static String PHONE_SOURCE = "phoneSource";
        final static String OLE_PHONE_SOURCE_BO = "olePhoneSourceBo";
    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
