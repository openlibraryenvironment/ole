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
@XmlRootElement(name = org.kuali.ole.deliver.api.OleSourceDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = org.kuali.ole.deliver.api.OleSourceDefinition.Constants.TYPE_NAME, propOrder = {
        org.kuali.ole.deliver.api.OleSourceDefinition.Elements.OLE_SOURCE_ID,
        org.kuali.ole.deliver.api.OleSourceDefinition.Elements.OLE_SOURCE_CODE,
        org.kuali.ole.deliver.api.OleSourceDefinition.Elements.OLE_SOURCE_NAME,
        org.kuali.ole.deliver.api.OleSourceDefinition.Elements.OLE_SOURCE_DESC,
        org.kuali.ole.deliver.api.OleSourceDefinition.Elements.ACTIVE_INDICATOR,

        CoreConstants.CommonElements.VERSION_NUMBER,
        //CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class OleSourceDefinition extends AbstractDataTransferObject implements OleSourceContract {

    private static final long serialVersionUID = 1L;


    @XmlElement(name = Elements.OLE_SOURCE_ID, required = false)
    private final String oleSourceId;

    @XmlElement(name = Elements.OLE_SOURCE_CODE, required = false)
    private final String oleSourceCode;

    @XmlElement(name = Elements.OLE_SOURCE_NAME, required = false)
    private final String oleSourceName;

    @XmlElement(name = Elements.OLE_SOURCE_DESC, required = false)
    private final String oleSourceDesc;

    @XmlElement(name = Elements.ACTIVE_INDICATOR, required = false)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    /*@XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;*/
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OleSourceDefinition() {
        this.oleSourceId = null;
        this.oleSourceCode = null;
        this.oleSourceName = null;
        this.active = false;
        this.oleSourceDesc = null;
        this.versionNumber = null;
        //this.objectId = null;
    }


    private OleSourceDefinition(Builder builder) {
        this.oleSourceId = builder.getOleSourceId();
        this.oleSourceCode = builder.getOleSourceCode();
        this.oleSourceName = builder.getOleSourceName();
        this.oleSourceDesc = builder.getOleSourceDesc();
        this.active = builder.isActive();

        this.versionNumber = builder.getVersionNumber();
        //this.objectId = builder.getObjectId();
    }


    @Override
    public String getOleSourceId() {
        return this.oleSourceId;
    }

    @Override
    public String getOleSourceCode() {
        return this.oleSourceCode;
    }

    @Override
    public String getOleSourceName() {
        return this.oleSourceName;
    }

    @Override
    public String getOleSourceDesc() {
        return this.oleSourceDesc;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    ;


    @Override
    public String getId() {
        return this.oleSourceId;
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
            implements Serializable, ModelBuilder, OleSourceContract {

        private String oleSourceId;
        private String oleSourceCode;
        private String oleSourceName;
        private String oleSourceDesc;
        private boolean active;

        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OleSourceContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getOleSourceId() != null) {
                builder.setOleSourceId(contract.getOleSourceId());
            }
            if (contract.getOleSourceCode() != null) {
                builder.setOleSourceCode(contract.getOleSourceCode());
            }
            if (contract.getOleSourceName() != null) {
                builder.setOleSourceName(contract.getOleSourceName());
            }
            if (contract.isActive()) {
                builder.setActive(contract.isActive());
            }
            if (contract.getOleSourceDesc() != null) {
                builder.setOleSourceDesc(contract.getOleSourceDesc());
            }

            builder.setVersionNumber(contract.getVersionNumber());
            /*builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());*/
            builder.setId(contract.getId());
            return builder;
        }


        public org.kuali.ole.deliver.api.OleSourceDefinition build() {
            return new org.kuali.ole.deliver.api.OleSourceDefinition(this);
        }

        public String getOleSourceId() {
            return oleSourceId;
        }

        public void setOleSourceId(String oleSourceId) {
            this.oleSourceId = oleSourceId;
        }

        public String getOleSourceCode() {
            return oleSourceCode;
        }

        public void setOleSourceCode(String oleSourceCode) {
            this.oleSourceCode = oleSourceCode;
        }

        public String getOleSourceName() {
            return oleSourceName;
        }

        public void setOleSourceName(String oleSourceName) {
            this.oleSourceName = oleSourceName;
        }

        public String getOleSourceDesc() {
            return oleSourceDesc;
        }

        public void setOleSourceDesc(String oleSourceDesc) {
            this.oleSourceDesc = oleSourceDesc;
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
            return this.oleSourceId;
        }


        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.oleSourceId = id;
        }
    }

    static class Constants {

        final static String ROOT_ELEMENT_NAME = "oleSource";
        final static String TYPE_NAME = "OleSourceType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String OLE_SOURCE_ID = "oleSourceId";
        final static String OLE_SOURCE_CODE = "oleSourceCode";
        final static String OLE_SOURCE_NAME = "oleSourceName";
        final static String OLE_SOURCE_DESC = "oleSourceDesc";
        final static String ACTIVE_INDICATOR = "active";
        //final static String ENTITY_ID = "entityId";

    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
