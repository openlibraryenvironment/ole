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
@XmlRootElement(name = OleStatisticalCategoryDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OleStatisticalCategoryDefinition.Constants.TYPE_NAME, propOrder = {
        OleStatisticalCategoryDefinition.Elements.OLE_STATISTICAL_CATEGORY_ID,
        OleStatisticalCategoryDefinition.Elements.OLE_STATISTICAL_CATEGORY_CODE,
        OleStatisticalCategoryDefinition.Elements.OLE_STATISTICAL_CATEGORY_NAME,
        OleStatisticalCategoryDefinition.Elements.OLE_STATISTICAL_CATEGORY_DESC,
        OleStatisticalCategoryDefinition.Elements.ACTIVE_INDICATOR,

        CoreConstants.CommonElements.VERSION_NUMBER,
        //CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class OleStatisticalCategoryDefinition extends AbstractDataTransferObject implements org.kuali.ole.deliver.api.OleStatisticalCategoryContract {

    private static final long serialVersionUID = 1L;


    @XmlElement(name = Elements.OLE_STATISTICAL_CATEGORY_ID, required = false)
    private final String oleStatisticalCategoryId;

    @XmlElement(name = Elements.OLE_STATISTICAL_CATEGORY_CODE, required = false)
    private final String oleStatisticalCategoryCode;

    @XmlElement(name = Elements.OLE_STATISTICAL_CATEGORY_NAME, required = false)
    private final String oleStatisticalCategoryName;

    @XmlElement(name = Elements.OLE_STATISTICAL_CATEGORY_DESC, required = false)
    private final String oleStatisticalCategoryDesc;

    @XmlElement(name = Elements.ACTIVE_INDICATOR, required = false)
    private final boolean active;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    /*@XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;*/
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OleStatisticalCategoryDefinition() {
        this.oleStatisticalCategoryId = null;
        this.oleStatisticalCategoryCode = null;
        this.oleStatisticalCategoryName = null;
        this.active = false;
        this.oleStatisticalCategoryDesc = null;
        this.versionNumber = null;
        //this.objectId = null;
    }


    private OleStatisticalCategoryDefinition(Builder builder) {
        this.oleStatisticalCategoryId = builder.getOleStatisticalCategoryId();
        this.oleStatisticalCategoryCode = builder.getOleStatisticalCategoryCode();
        this.oleStatisticalCategoryName = builder.getOleStatisticalCategoryName();
        this.oleStatisticalCategoryDesc = builder.getOleStatisticalCategoryDesc();
        this.active = builder.isActive();

        this.versionNumber = builder.getVersionNumber();
        //this.objectId = builder.getObjectId();
    }


    @Override
    public String getOleStatisticalCategoryId() {
        return this.oleStatisticalCategoryId;
    }

    @Override
    public String getOleStatisticalCategoryCode() {
        return this.oleStatisticalCategoryCode;
    }

    @Override
    public String getOleStatisticalCategoryName() {
        return this.oleStatisticalCategoryName;
    }

    @Override
    public String getOleStatisticalCategoryDesc() {
        return this.oleStatisticalCategoryDesc;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    ;


    @Override
    public String getId() {
        return this.oleStatisticalCategoryId;
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
            implements Serializable, ModelBuilder, org.kuali.ole.deliver.api.OleStatisticalCategoryContract {

        private String oleStatisticalCategoryId;
        private String oleStatisticalCategoryCode;
        private String oleStatisticalCategoryName;
        private String oleStatisticalCategoryDesc;
        private boolean active;

        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OleStatisticalCategoryContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getOleStatisticalCategoryId() != null) {
                builder.setOleStatisticalCategoryId(contract.getOleStatisticalCategoryId());
            }
            if (contract.getOleStatisticalCategoryCode() != null) {
                builder.setOleStatisticalCategoryCode(contract.getOleStatisticalCategoryCode());
            }
            if (contract.getOleStatisticalCategoryName() != null) {
                builder.setOleStatisticalCategoryName(contract.getOleStatisticalCategoryName());
            }
            if (contract.isActive()) {
                builder.setActive(contract.isActive());
            }
            if (contract.getOleStatisticalCategoryDesc() != null) {
                builder.setOleStatisticalCategoryDesc(contract.getOleStatisticalCategoryDesc());
            }

            builder.setVersionNumber(contract.getVersionNumber());
            /*builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());*/
            builder.setId(contract.getId());
            return builder;
        }


        public OleStatisticalCategoryDefinition build() {
            return new OleStatisticalCategoryDefinition(this);
        }

        public String getOleStatisticalCategoryId() {
            return oleStatisticalCategoryId;
        }

        public void setOleStatisticalCategoryId(String oleStatisticalCategoryId) {
            this.oleStatisticalCategoryId = oleStatisticalCategoryId;
        }

        public String getOleStatisticalCategoryCode() {
            return oleStatisticalCategoryCode;
        }

        public void setOleStatisticalCategoryCode(String oleStatisticalCategoryCode) {
            this.oleStatisticalCategoryCode = oleStatisticalCategoryCode;
        }

        public String getOleStatisticalCategoryName() {
            return oleStatisticalCategoryName;
        }

        public void setOleStatisticalCategoryName(String oleStatisticalCategoryName) {
            this.oleStatisticalCategoryName = oleStatisticalCategoryName;
        }

        public String getOleStatisticalCategoryDesc() {
            return oleStatisticalCategoryDesc;
        }

        public void setOleStatisticalCategoryDesc(String oleStatisticalCategoryDesc) {
            this.oleStatisticalCategoryDesc = oleStatisticalCategoryDesc;
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
            return this.oleStatisticalCategoryId;
        }


        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.oleStatisticalCategoryId = id;
        }
    }

    static class Constants {

        final static String ROOT_ELEMENT_NAME = "oleStatisticalCategory";
        final static String TYPE_NAME = "oleStatisticalCategoryType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String OLE_STATISTICAL_CATEGORY_ID = "oleStatisticalCategoryId";
        final static String OLE_STATISTICAL_CATEGORY_CODE = "oleStatisticalCategoryCode";
        final static String OLE_STATISTICAL_CATEGORY_NAME = "oleStatisticalCategoryName";
        final static String OLE_STATISTICAL_CATEGORY_DESC = "oleStatisticalCategoryDesc";
        final static String ACTIVE_INDICATOR = "active";
        //final static String ENTITY_ID = "entityId";

    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}
