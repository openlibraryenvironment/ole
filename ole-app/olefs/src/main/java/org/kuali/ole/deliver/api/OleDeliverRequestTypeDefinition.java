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
@XmlRootElement(name = org.kuali.ole.deliver.api.OleDeliverRequestTypeDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = org.kuali.ole.deliver.api.OleDeliverRequestTypeDefinition.Constants.TYPE_NAME, propOrder = {
        org.kuali.ole.deliver.api.OleDeliverRequestTypeDefinition.Elements.REQUEST_TYPE_ID,
        org.kuali.ole.deliver.api.OleDeliverRequestTypeDefinition.Elements.REQUEST_TYPE_CD,
        org.kuali.ole.deliver.api.OleDeliverRequestTypeDefinition.Elements.REQUEST_TYPE_NAME,
        org.kuali.ole.deliver.api.OleDeliverRequestTypeDefinition.Elements.REQUEST_TYPE_DESC,
        org.kuali.ole.deliver.api.OleDeliverRequestTypeDefinition.Elements.ACTIVE,
        CoreConstants.CommonElements.VERSION_NUMBER,
        //CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class OleDeliverRequestTypeDefinition extends AbstractDataTransferObject implements OleDeliverRequestTypeContract {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = Elements.REQUEST_TYPE_ID, required = false)
    private final String requestTypeId;

    @XmlElement(name = Elements.REQUEST_TYPE_CD, required = false)
    private final String requestTypeCode;

    @XmlElement(name = Elements.REQUEST_TYPE_DESC, required = false)
    private final String requestTypeDescription;

    @XmlElement(name = Elements.REQUEST_TYPE_NAME, required = false)
    private final String requestTypeName;

    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;


    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    /*@XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;*/
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OleDeliverRequestTypeDefinition() {
        this.requestTypeId = null;
        this.requestTypeCode = null;
        this.requestTypeDescription = null;
        this.requestTypeName = null;
        this.active = false;
        this.versionNumber = null;

        //this.objectId = null;
    }


    private OleDeliverRequestTypeDefinition(Builder builder) {
        this.requestTypeId = builder.getRequestTypeId();
        this.requestTypeCode = builder.getRequestTypeCode();
        this.requestTypeDescription = builder.getRequestTypeDescription();
        this.requestTypeName = builder.getRequestTypeName();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
        //this.objectId = builder.getObjectId();
    }


    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getId() {
        return this.requestTypeId;
    }

    @Override
    public String getRequestTypeId() {
        return this.requestTypeId;
    }

    @Override
    public String getRequestTypeCode() {
        return this.requestTypeCode;
    }

    @Override
    public String getRequestTypeName() {
        return this.requestTypeName;
    }

    @Override
    public String getRequestTypeDescription() {
        return this.requestTypeDescription;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }


    //@Override
    /*public OleBorrowerTypeDefinition getOleBorrowerType() {
        return this.oleBorrowerType;
    }*/

    public static class Builder
            implements Serializable, ModelBuilder, OleDeliverRequestTypeContract {
        private String requestTypeId;
        private String requestTypeCode;
        private String requestTypeName;
        private String requestTypeDescription;
        private boolean active;
        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OleDeliverRequestTypeContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getRequestTypeId() != null) {
                builder.setRequestTypeId(contract.getRequestTypeId());
            }
            if (contract.isActive()) {
                builder.setActive(contract.isActive());
            }
            if (contract.getRequestTypeCode() != null) {
                builder.setRequestTypeCode(contract.getRequestTypeCode());
            }
            if (contract.getRequestTypeDescription() != null) {
                builder.setRequestTypeDescription(contract.getRequestTypeDescription());
            }
            if (contract.getRequestTypeName() != null) {
                builder.setRequestTypeName(contract.getRequestTypeName());
            }
            if (contract.getId() != null) {
                builder.setId(contract.getId());
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


        public org.kuali.ole.deliver.api.OleDeliverRequestTypeDefinition build() {
            return new org.kuali.ole.deliver.api.OleDeliverRequestTypeDefinition(this);
        }

        public String getRequestTypeId() {
            return requestTypeId;
        }

        public void setRequestTypeId(String requestTypeId) {
            this.requestTypeId = requestTypeId;
        }

        public String getRequestTypeCode() {
            return requestTypeCode;
        }

        public void setRequestTypeCode(String requestTypeCode) {
            this.requestTypeCode = requestTypeCode;
        }

        public String getRequestTypeName() {
            return requestTypeName;
        }

        public void setRequestTypeName(String requestTypeName) {
            this.requestTypeName = requestTypeName;
        }

        public String getRequestTypeDescription() {
            return requestTypeDescription;
        }

        public void setRequestTypeDescription(String requestTypeDescription) {
            this.requestTypeDescription = requestTypeDescription;
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
            return this.requestTypeId;
        }


        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.requestTypeId = id;
        }
    }

    static class Constants {

        final static String ROOT_ELEMENT_NAME = "oleDeliverRequestType";
        final static String TYPE_NAME = "OleDeliverRequestTypeType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String REQUEST_TYPE_ID = "requestTypeId";
        final static String REQUEST_TYPE_CD = "requestTypeCode";
        final static String REQUEST_TYPE_NAME = "requestTypeName";
        final static String REQUEST_TYPE_DESC = "requestTypeDescription";
        final static String ACTIVE = "active";


    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
