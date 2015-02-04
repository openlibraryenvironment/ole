package org.kuali.ole.deliver.api;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimConstants;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/24/12
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = org.kuali.ole.deliver.api.OlePatronLocalIdentificationDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = org.kuali.ole.deliver.api.OlePatronLocalIdentificationDefinition.Constants.TYPE_NAME, propOrder = {
        org.kuali.ole.deliver.api.OlePatronLocalIdentificationDefinition.Elements.OLE_PTRN_LOCAL_SEQ_ID,
        org.kuali.ole.deliver.api.OlePatronLocalIdentificationDefinition.Elements.OLE_PTRN_ID,
        org.kuali.ole.deliver.api.OlePatronLocalIdentificationDefinition.Elements.LOCAL_ID,
        //OlePatronDefinition.Elements.OLE_BORROWER_TYPE,
        //OlePatronDefinition.Elements.ENTITY,

        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class OlePatronLocalIdentificationDefinition extends AbstractDataTransferObject implements org.kuali.ole.deliver.api.OlePatronLocalIdentificationContract {

    private static final long serialVersionUID = 1L;


    @XmlElement(name = Elements.OLE_PTRN_LOCAL_SEQ_ID, required = false)
    private final String patronLocalSeqId;

    @XmlElement(name = Elements.OLE_PTRN_ID, required = false)
    private final String olePatronId;

    @XmlElement(name = Elements.LOCAL_ID, required = false)
    private final String localId;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;


    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OlePatronLocalIdentificationDefinition() {
        this.patronLocalSeqId = null;
        this.olePatronId = null;
        this.localId = null;
        this.versionNumber = null;
        this.objectId = null;
    }


    private OlePatronLocalIdentificationDefinition(Builder builder) {
        this.patronLocalSeqId = builder.getPatronLocalSeqId();
        this.olePatronId = builder.getOlePatronId();
        this.localId = builder.getLocalId();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }


    @Override
    public String getPatronLocalSeqId() {
        return this.patronLocalSeqId;
    }

    @Override
    public String getLocalId() {
        return this.localId;
    }

    @Override
    public String getOlePatronId() {
        return this.olePatronId;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getId() {
        return this.patronLocalSeqId;
    }

    public static class Builder
            implements Serializable, ModelBuilder, org.kuali.ole.deliver.api.OlePatronLocalIdentificationContract {
        private String patronLocalSeqId;
        private String localId;
        private String olePatronId;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OlePatronLocalIdentificationContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getPatronLocalSeqId() != null) {
                builder.setPatronLocalSeqId(contract.getPatronLocalSeqId());
            }
            if (contract.getLocalId() != null) {
                builder.setLocalId(contract.getLocalId());
            }
            if (contract.getOlePatronId() != null) {
                builder.setOlePatronId(contract.getOlePatronId());
            }

            builder.setObjectId(contract.getObjectId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setId(contract.getId());
            return builder;
        }


        public org.kuali.ole.deliver.api.OlePatronLocalIdentificationDefinition build() {
            return new org.kuali.ole.deliver.api.OlePatronLocalIdentificationDefinition(this);
        }

        public String getPatronLocalSeqId() {
            return patronLocalSeqId;
        }

        public void setPatronLocalSeqId(String patronLocalSeqId) {
            this.patronLocalSeqId = patronLocalSeqId;
        }

        public String getLocalId() {
            return localId;
        }

        public void setLocalId(String localId) {
            this.localId = localId;
        }

        public String getOlePatronId() {
            return this.olePatronId;
        }

        public void setOlePatronId(String olePatronId) {
            this.olePatronId = olePatronId;
        }

        @Override
        public String getId() {
            return this.patronLocalSeqId;
        }


        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.patronLocalSeqId = id;
        }

        public Long getVersionNumber() {
            return this.versionNumber;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }
    }

    static class Constants {

        final static String ROOT_ELEMENT_NAME = "olePatronLocalIdentification";
        final static String TYPE_NAME = "OlePatronLocalIdentificationType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String OLE_PTRN_LOCAL_SEQ_ID = "patronLocalSeqId";
        final static String LOCAL_ID = "localId";
        final static String OLE_PTRN_ID = "olePatronId";
    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
