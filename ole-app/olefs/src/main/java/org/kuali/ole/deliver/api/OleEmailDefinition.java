package org.kuali.ole.deliver.api;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by angelind on 10/13/15.
 */
@XmlRootElement(name = OleEmailDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OleEmailDefinition.Constants.TYPE_NAME, propOrder = {
        OleEmailDefinition.Elements.OLE_EMAIL_ID,
        OleEmailDefinition.Elements.OLE_PTRN_ID,
        OleEmailDefinition.Elements.ID,
        OleEmailDefinition.Elements.EMAIL_SOURCE,
        OleEmailDefinition.Elements.OLE_EMAIL_SOURCE_BO,

        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class OleEmailDefinition extends AbstractDataTransferObject implements OleEmailContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.OLE_EMAIL_ID, required = false)
    private final String oleEmailId;

    @XmlElement(name = Elements.OLE_PTRN_ID, required = false)
    private final String olePatronId;

    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElement(name = Elements.EMAIL_SOURCE, required = false)
    private final String emailSource;

    @XmlElement(name = Elements.OLE_EMAIL_SOURCE_BO, required = false)
    private final OleAddressSourceDefinition oleEmailSourceBo;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OleEmailDefinition() {
        this.oleEmailId = null;
        this.olePatronId = null;
        this.id = null;
        this.emailSource = null;
        this.oleEmailSourceBo = null;
        this.versionNumber = null;
    }

    private OleEmailDefinition(Builder builder) {
        this.oleEmailId = builder.getOleEmailId();
        this.olePatronId = builder.getOlePatronId();
        this.id = builder.getId();
        this.emailSource = builder.getEmailSource();
        if (builder.getOleEmailSourceBo() != null)
            this.oleEmailSourceBo = builder.getOleEmailSourceBo().build();
        else
            this.oleEmailSourceBo = new OleAddressSourceDefinition();
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
    public String getOleEmailId() {
        return oleEmailId;
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
    public String getEmailSource() {
        return emailSource;
    }

    @Override
    public OleAddressSourceDefinition getOleEmailSourceBo() {
        return oleEmailSourceBo;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    public static class Builder
            implements Serializable, ModelBuilder, OleEmailContract {
        private String oleEmailId;
        private String olePatronId;
        private String id;
        private String emailSource;
        private OleAddressSourceDefinition.Builder oleEmailSourceBo;
        private EntityEmail.Builder entityEmail;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OleEmailContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getOleEmailId() != null) {
                builder.setOleEmailId(contract.getOleEmailId());
            }
            if (contract.getOlePatronId() != null) {
                builder.setOlePatronId(contract.getOlePatronId());
            }
            if (contract.getId() != null) {
                builder.setId(contract.getId());
            }
            if (contract.getEmailSource() != null) {
                builder.setEmailSource(contract.getEmailSource());
            }
            if (contract.getOleEmailSourceBo() != null) {
                builder.setOleEmailSourceBo(OleAddressSourceDefinition.Builder.create(contract.getOleEmailSourceBo()));
            }
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setId(contract.getId());
            return builder;
        }


        public OleEmailDefinition build() {
            return new OleEmailDefinition(this);
        }

        public String getOleEmailId() {
            return oleEmailId;
        }

        public void setOleEmailId(String oleEmailId) {
            this.oleEmailId = oleEmailId;
        }

        public String getOlePatronId() {
            return olePatronId;
        }

        public void setOlePatronId(String olePatronId) {
            this.olePatronId = olePatronId;
        }

        public String getEmailSource() {
            return emailSource;
        }

        public void setEmailSource(String emailSource) {
            this.emailSource = emailSource;
        }

        public OleAddressSourceDefinition.Builder getOleEmailSourceBo() {
            return oleEmailSourceBo;
        }

        public void setOleEmailSourceBo(OleAddressSourceDefinition.Builder oleEmailSourceBo) {
            this.oleEmailSourceBo = oleEmailSourceBo;
        }

        public EntityEmail.Builder getEntityEmail() {
            return entityEmail;
        }

        public void setEntityEmail(EntityEmail.Builder entityEmail) {
            this.entityEmail = entityEmail;
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

        final static String ROOT_ELEMENT_NAME = "oleEmail";
        final static String TYPE_NAME = "OleEmailType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String OLE_EMAIL_ID = "oleEmailId";
        final static String OLE_PTRN_ID = "olePatronId";
        final static String ID = "id";
        final static String EMAIL_SOURCE = "emailSource";
        final static String OLE_EMAIL_SOURCE_BO = "oleEmailSourceBo";
    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
