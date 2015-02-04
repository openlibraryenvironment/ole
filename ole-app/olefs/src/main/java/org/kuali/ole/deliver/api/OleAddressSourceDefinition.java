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
@XmlRootElement(name = org.kuali.ole.deliver.api.OleAddressSourceDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = org.kuali.ole.deliver.api.OleAddressSourceDefinition.Constants.TYPE_NAME, propOrder = {
        org.kuali.ole.deliver.api.OleAddressSourceDefinition.Elements.OLE_ADDRESS_SOURCE_ID,
        org.kuali.ole.deliver.api.OleAddressSourceDefinition.Elements.OLE_ADDRESS_SOURCE_CODE,
        org.kuali.ole.deliver.api.OleAddressSourceDefinition.Elements.OLE_ADDRESS_SOURCE_NAME,
        org.kuali.ole.deliver.api.OleAddressSourceDefinition.Elements.OLE_ADDRESS_SOURCE_DESC,
        org.kuali.ole.deliver.api.OleAddressSourceDefinition.Elements.ACTIVE_INDICATOR,

        CoreConstants.CommonElements.VERSION_NUMBER,
        //CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class OleAddressSourceDefinition extends AbstractDataTransferObject implements OleAddressSourceContract {

    private static final long serialVersionUID = 1L;


    @XmlElement(name = Elements.OLE_ADDRESS_SOURCE_ID, required = false)
    private final String oleAddressSourceId;

    @XmlElement(name = Elements.OLE_ADDRESS_SOURCE_CODE, required = false)
    private final String oleAddressSourceCode;

    @XmlElement(name = Elements.OLE_ADDRESS_SOURCE_NAME, required = false)
    private final String oleAddressSourceName;

    @XmlElement(name = Elements.OLE_ADDRESS_SOURCE_DESC, required = false)
    private final String oleAddressSourceDesc;

    @XmlElement(name = Elements.ACTIVE_INDICATOR, required = false)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    /*@XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;*/
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OleAddressSourceDefinition() {
        this.oleAddressSourceId = null;
        this.oleAddressSourceCode = null;
        this.oleAddressSourceName = null;
        this.active = false;
        this.oleAddressSourceDesc = null;
        this.versionNumber = null;
        //this.objectId = null;
    }


    private OleAddressSourceDefinition(Builder builder) {
        this.oleAddressSourceId = builder.getOleAddressSourceId();
        this.oleAddressSourceCode = builder.getOleAddressSourceCode();
        this.oleAddressSourceName = builder.getOleAddressSourceName();
        this.oleAddressSourceDesc = builder.getOleAddressSourceDesc();
        this.active = builder.isActive();

        this.versionNumber = builder.getVersionNumber();
        //this.objectId = builder.getObjectId();
    }


    @Override
    public String getOleAddressSourceId() {
        return this.oleAddressSourceId;
    }

    @Override
    public String getOleAddressSourceCode() {
        return this.oleAddressSourceCode;
    }

    @Override
    public String getOleAddressSourceName() {
        return this.oleAddressSourceName;
    }

    @Override
    public String getOleAddressSourceDesc() {
        return this.oleAddressSourceDesc;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    ;


    @Override
    public String getId() {
        return this.oleAddressSourceId;
    }


    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }


    //@Override
    /*public OleBorrowerTypeDefinition getOleBorrowerType() {
        return this.oleBorrowerType;
    }*/

    public static class Builder
            implements Serializable, ModelBuilder, OleAddressSourceContract {

        private String oleAddressSourceId;
        private String oleAddressSourceCode;
        private String oleAddressSourceName;
        private String oleAddressSourceDesc;
        private boolean active;

        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OleAddressSourceContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getOleAddressSourceId() != null) {
                builder.setOleAddressSourceId(contract.getOleAddressSourceId());
            }
            if (contract.getOleAddressSourceCode() != null) {
                builder.setOleAddressSourceCode(contract.getOleAddressSourceCode());
            }
            if (contract.getOleAddressSourceName() != null) {
                builder.setOleAddressSourceName(contract.getOleAddressSourceName());
            }
            if (contract.isActive()) {
                builder.setActive(contract.isActive());
            }
            if (contract.getOleAddressSourceDesc() != null) {
                builder.setOleAddressSourceDesc(contract.getOleAddressSourceDesc());
            }

            builder.setVersionNumber(contract.getVersionNumber());
            /*builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());*/
            builder.setId(contract.getId());
            return builder;
        }


        public org.kuali.ole.deliver.api.OleAddressSourceDefinition build() {
            return new org.kuali.ole.deliver.api.OleAddressSourceDefinition(this);
        }

        public String getOleAddressSourceId() {
            return oleAddressSourceId;
        }

        public void setOleAddressSourceId(String oleAddressSourceId) {
            this.oleAddressSourceId = oleAddressSourceId;
        }

        public String getOleAddressSourceCode() {
            return oleAddressSourceCode;
        }

        public void setOleAddressSourceCode(String oleAddressSourceCode) {
            this.oleAddressSourceCode = oleAddressSourceCode;
        }

        public String getOleAddressSourceName() {
            return oleAddressSourceName;
        }

        public void setOleAddressSourceName(String oleAddressSourceName) {
            this.oleAddressSourceName = oleAddressSourceName;
        }

        public String getOleAddressSourceDesc() {
            return oleAddressSourceDesc;
        }

        public void setOleAddressSourceDesc(String oleAddressSourceDesc) {
            this.oleAddressSourceDesc = oleAddressSourceDesc;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
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
            return this.oleAddressSourceId;
        }


        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.oleAddressSourceId = id;
        }
    }

    static class Constants {

        final static String ROOT_ELEMENT_NAME = "oleAddressSource";
        final static String TYPE_NAME = "OleAddressSourceType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String OLE_ADDRESS_SOURCE_ID = "oleAddressSourceId";
        final static String OLE_ADDRESS_SOURCE_CODE = "oleAddressSourceCode";
        final static String OLE_ADDRESS_SOURCE_NAME = "oleAddressSourceName";
        final static String OLE_ADDRESS_SOURCE_DESC = "oleAddressSourceDesc";
        final static String ACTIVE_INDICATOR = "active";
        //final static String ENTITY_ID = "entityId";

    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
