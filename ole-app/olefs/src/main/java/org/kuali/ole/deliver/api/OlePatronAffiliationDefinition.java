package org.kuali.ole.deliver.api;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationType;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/24/12
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = org.kuali.ole.deliver.api.OlePatronAffiliationDefinition.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = org.kuali.ole.deliver.api.OlePatronAffiliationDefinition.Constants.TYPE_NAME, propOrder = {
        org.kuali.ole.deliver.api.OlePatronAffiliationDefinition.Elements.ENTITY_AFFILIATION_ID,
        org.kuali.ole.deliver.api.OlePatronAffiliationDefinition.Elements.ENTITY_ID,
        org.kuali.ole.deliver.api.OlePatronAffiliationDefinition.Elements.AFFILIATION_TYPE_CODE,
        org.kuali.ole.deliver.api.OlePatronAffiliationDefinition.Elements.AFFILIATION_TYPE,
        org.kuali.ole.deliver.api.OlePatronAffiliationDefinition.Elements.CAMPUS_CODE,
        org.kuali.ole.deliver.api.OlePatronAffiliationDefinition.Elements.EMPLOYMENTS,
        org.kuali.ole.deliver.api.OlePatronAffiliationDefinition.Elements.DEFAULT_VALUE,
        org.kuali.ole.deliver.api.OlePatronAffiliationDefinition.Elements.ACTIVE_INDICATOR,

        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class OlePatronAffiliationDefinition extends AbstractDataTransferObject implements OlePatronAffiliationContract {

    @XmlElement(name = Elements.ENTITY_AFFILIATION_ID, required = false)
    private final String entityAffiliationId;

    @XmlElement(name = Elements.ENTITY_ID, required = false)
    private final String entityId;

    @XmlElement(name = Elements.AFFILIATION_TYPE_CODE, required = false)
    private final String affiliationTypeCode;

    @XmlElement(name = Elements.CAMPUS_CODE, required = false)
    private final String campusCode;

    @XmlElement(name = Elements.AFFILIATION_TYPE, required = false)
    private final EntityAffiliationType affiliationType;


    @XmlElement(name = Elements.ACTIVE_INDICATOR, required = false)
    private final boolean active;

    @XmlElement(name = Elements.DEFAULT_VALUE, required = false)
    private final boolean defaultValue;

    @XmlElementWrapper(name = Elements.EMPLOYMENTS, required = false)
    @XmlElement(name = Elements.EMPLOYMENTS, required = false)
    private final List<EntityEmployment> employments;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    public OlePatronAffiliationDefinition() {
        this.entityAffiliationId = null;
        this.entityId = null;
        this.affiliationTypeCode = null;
        this.campusCode = null;
        this.affiliationType = null;
        this.employments = null;
        this.defaultValue = false;
        this.active = false;

        this.versionNumber = null;
        this.objectId = null;
    }


    private OlePatronAffiliationDefinition(Builder builder) {
        this.entityAffiliationId = builder.getEntityAffiliationId();
        this.affiliationTypeCode = builder.getAffiliationTypeCode();
        this.campusCode = builder.getCampusCode();
        this.defaultValue = builder.isDefaultValue();
        this.active = builder.isActive();
        this.entityId = builder.getEntityId();


        this.employments = new ArrayList<EntityEmployment>();
        if (!CollectionUtils.isEmpty(builder.getEmployments())) {
            for (EntityEmployment.Builder employment : builder.getEmployments()) {
                this.employments.add(employment.build());
            }
        }

        this.affiliationType = builder.getAffiliationType().build();
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
    }

    public String getEntityAffiliationId() {
        return this.entityAffiliationId;
    }

    public String getAffiliationTypeCode() {
        return this.affiliationTypeCode;
    }

    public String getCampusCode() {
        return this.campusCode;
    }

    public boolean isDefaultValue() {
        return this.defaultValue;
    }

    public EntityAffiliationType getAffiliationType() {
        return this.affiliationType;
    }

    public List<EntityEmployment> getEmployments() {
        return this.employments;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getId() {
        return this.getEntityAffiliationId();
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    /*@Override
    public EntityAffiliation getEntityAffliationBo() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }*/

    @Override
    public String getEntityId() {
        return entityId;
    }

    //@Override
    /*public OleBorrowerTypeDefinition getOleBorrowerType() {
        return this.oleBorrowerType;
    }*/

    public static class Builder
            implements Serializable, ModelBuilder, OlePatronAffiliationContract {
        private String entityAffiliationId;
        private String affiliationTypeCode;
        private String campusCode;
        private boolean defaultValue;
        private boolean active;
        private EntityAffiliationType.Builder affiliationType;
        private List<EntityEmployment.Builder> employments;
        private String entityId;

        private Long versionNumber;
        private String objectId;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(OlePatronAffiliationContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getEntityAffiliationId() != null) {
                builder.setEntityAffiliationId(contract.getEntityAffiliationId());
            }
            if (contract.getAffiliationTypeCode() != null) {
                builder.setAffiliationTypeCode(contract.getAffiliationTypeCode());
            }
            if (contract.getCampusCode() != null) {
                builder.setCampusCode(contract.getCampusCode());
            }
            if (contract.isActive()) {
                builder.setActive(contract.isActive());
            }
            if (contract.isDefaultValue()) {
                builder.setDefaultValue(contract.isDefaultValue());
            }

            builder.employments = new ArrayList<EntityEmployment.Builder>();
            if (!CollectionUtils.isEmpty(contract.getEmployments())) {
                for (EntityEmploymentContract entityEmploymentContract : contract.getEmployments()) {
                    builder.employments.add(EntityEmployment.Builder.create(entityEmploymentContract));
                }
            }
            if (contract.getAffiliationType() != null) {
                builder.setAffiliationType(EntityAffiliationType.Builder.create(contract.getAffiliationType()));
            }

            builder.setEntityId(contract.getEntityId());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());
            builder.setId(contract.getId());
            return builder;
        }


        public org.kuali.ole.deliver.api.OlePatronAffiliationDefinition build() {
            return new org.kuali.ole.deliver.api.OlePatronAffiliationDefinition(this);
        }

        public String getEntityAffiliationId() {
            return entityAffiliationId;
        }

        public void setEntityAffiliationId(String entityAffiliationId) {
            this.entityAffiliationId = entityAffiliationId;
        }

        public String getAffiliationTypeCode() {
            return affiliationTypeCode;
        }

        public void setAffiliationTypeCode(String affiliationTypeCode) {
            this.affiliationTypeCode = affiliationTypeCode;
        }

        public String getCampusCode() {
            return campusCode;
        }

        public void setCampusCode(String campusCode) {
            this.campusCode = campusCode;
        }

        public boolean isDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(boolean defaultValue) {
            this.defaultValue = defaultValue;
        }

        public EntityAffiliationType.Builder getAffiliationType() {
            return affiliationType;
        }

        public void setAffiliationType(EntityAffiliationType.Builder affiliationType) {
            this.affiliationType = affiliationType;
        }

        public List<EntityEmployment.Builder> getEmployments() {
            return employments;
        }

        public void setEmployments(List<EntityEmployment.Builder> employments) {
            this.employments = employments;
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

        @Override
        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.entityAffiliationId = id;
        }

        @Override
        public String getId() {
            return this.entityAffiliationId;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }
    }

    static class Constants {

        final static String ROOT_ELEMENT_NAME = "olePatronAffiliation";
        final static String TYPE_NAME = "OlePatronAffiliationType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[]{CoreConstants.CommonElements.FUTURE_ELEMENTS};
    }

    static class Elements {
        final static String ENTITY_AFFILIATION_ID = "entityAffiliationId";
        //final static String ENTITY_ID = "entityId";
        final static String AFFILIATION_TYPE_CODE = "affiliationTypeCode";
        final static String CAMPUS_CODE = "campusCode";
        final static String AFFILIATION_TYPE = "affiliationType";
        final static String EMPLOYMENTS = "employments";
        final static String DEFAULT_VALUE = "defaultValue";
        final static String ACTIVE_INDICATOR = "active";
        final static String ENTITY_ID = "entityId";


    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Constants.TYPE_NAME;
    }
}