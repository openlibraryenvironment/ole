package org.kuali.ole.deliver.bo;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.deliver.api.OlePatronAffiliationContract;
import org.kuali.ole.deliver.api.OlePatronAffiliationDefinition;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationTypeBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.krad.bo.BusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * OlePatronAffiliation provides OlePatronAffiliation information through getter and setter.
 */
public class OlePatronAffiliation extends BusinessObjectBase implements OlePatronAffiliationContract {

    private String entityAffiliationId;

    private String entityId;

    private String affiliationTypeCode;

    private String campusCode;

    private boolean defaultValue;

    private boolean active;

    private EntityAffiliationTypeBo affiliationType = new EntityAffiliationTypeBo();

    private List<EntityEmploymentBo> employments = new ArrayList<EntityEmploymentBo>();

    private Long versionNumber;

    private String objectId;

    public OlePatronAffiliation() {

    }

    /**
     * Constructor sets the values to entityAffiliationBo object from OlePatronAffiliation
     *
     * @param entityAffiliationBo
     */
    public OlePatronAffiliation(EntityAffiliationBo entityAffiliationBo) {
        this.setEntityAffiliationId(entityAffiliationBo.getId());
        this.setAffiliationTypeCode(entityAffiliationBo.getAffiliationTypeCode());
        this.setAffiliationType(entityAffiliationBo.getAffiliationType());
        this.setCampusCode(entityAffiliationBo.getCampusCode());
        this.setDefaultValue(entityAffiliationBo.getDefaultValue());
        this.setActive(entityAffiliationBo.getActive());
        this.setVersionNumber(entityAffiliationBo.getVersionNumber());
        this.setObjectId(entityAffiliationBo.getObjectId());
    }

    /**
     * Gets the value of entityAffiliationId property
     *
     * @return entityAffiliationId
     */
    public String getEntityAffiliationId() {
        return entityAffiliationId;
    }

    /**
     * Sets the value for entityAffiliationId property
     *
     * @param entityAffiliationId
     */
    public void setEntityAffiliationId(String entityAffiliationId) {
        this.entityAffiliationId = entityAffiliationId;
    }

    /**
     * Gets the value of affiliationTypeCode property
     *
     * @return affiliationTypeCode
     */
    public String getAffiliationTypeCode() {
        return affiliationTypeCode;
    }

    /**
     * Sets the value for affiliationTypeCode property
     *
     * @param affiliationTypeCode
     */
    public void setAffiliationTypeCode(String affiliationTypeCode) {
        this.affiliationTypeCode = affiliationTypeCode;
    }

    /**
     * Gets the value of campusCode property
     *
     * @return campusCode
     */
    public String getCampusCode() {
        return campusCode;
    }

    /**
     * Sets the value for campusCode property
     *
     * @param campusCode
     */
    public void setCampusCode(String campusCode) {
        this.campusCode = campusCode;
    }

    /**
     * Gets the value of defaultValue property
     *
     * @return defaultValue
     */
    public boolean isDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the value for defaultValue property
     *
     * @param defaultValue
     */
    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the value of active property
     *
     * @return active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value for active property
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the value of affiliationType property
     *
     * @return affiliationType
     */
    public EntityAffiliationTypeBo getAffiliationType() {
        return affiliationType;
    }

    /**
     * Sets the value for affiliationType property
     *
     * @param affiliationType
     */
    public void setAffiliationType(EntityAffiliationTypeBo affiliationType) {
        this.affiliationType = affiliationType;
    }

    /**
     * Gets the value of employments property
     *
     * @return employments
     */
    public List<EntityEmploymentBo> getEmployments() {
        return employments;
    }

    /**
     * Sets the value for employments property
     *
     * @param employments
     */
    public void setEmployments(List<EntityEmploymentBo> employments) {
        this.employments = employments;
    }

    /**
     * Gets the value of EntityAffiliation property
     *
     * @return EntityAffiliation
     */
    public EntityAffiliationBo getEntityAffliationBo() {
        EntityAffiliationBo entityAffiliationBo = new EntityAffiliationBo();
        entityAffiliationBo.setId(this.getEntityAffiliationId());
        entityAffiliationBo.setAffiliationTypeCode(this.getAffiliationTypeCode());
        entityAffiliationBo.setAffiliationType(this.getAffiliationType());
        entityAffiliationBo.setActive(this.isActive());
        entityAffiliationBo.setDefaultValue(this.defaultValue);
        entityAffiliationBo.setCampusCode(this.getCampusCode());
        entityAffiliationBo.setObjectId(this.getObjectId());
        entityAffiliationBo.setVersionNumber(this.getVersionNumber());
        entityAffiliationBo.setEntityId(this.getEntityId());
        return entityAffiliationBo;
    }

    /**
     * This method converts the PersistableBusinessObjectBase OleBorrowerType into immutable object OlePatronAffiliationDefinition
     *
     * @param bo(OlePatronAffiliation)
     * @return OlePatronAffiliationDefinition
     */
    static OlePatronAffiliationDefinition to(OlePatronAffiliation bo) {
        if (bo == null) {
            return null;
        }
        return OlePatronAffiliationDefinition.Builder.create(bo).build();
    }

    /**
     * This method converts the immutable object OlePatronAffiliationDefinition into PersistableBusinessObjectBase OlePatronAffiliation
     *
     * @param im(OlePatronAffiliationDefinition)
     *
     * @return bo(OlePatronAffiliation)
     */
    static OlePatronAffiliation from(OlePatronAffiliationDefinition im) {
        if (im == null) {
            return null;
        }

        OlePatronAffiliation bo = new OlePatronAffiliation();
        bo.entityAffiliationId = im.getEntityAffiliationId();
        bo.affiliationTypeCode = im.getAffiliationTypeCode();
        if (im.getAffiliationType() != null) {
            bo.affiliationType = EntityAffiliationTypeBo.from(im.getAffiliationType());
        }
        bo.defaultValue = im.isDefaultValue();
        bo.active = im.isActive();
        bo.versionNumber = im.getVersionNumber();
        bo.entityId = im.getEntityId();
        bo.objectId = im.getObjectId();

        if (CollectionUtils.isNotEmpty(im.getEmployments())) {
            for (EntityEmployment entityEmployment : im.getEmployments()) {
                bo.employments.add(EntityEmploymentBo.from(entityEmployment));
            }
        }
        return bo;
    }

    @Override
    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Gets the value of entityAffiliationId property
     *
     * @return entityAffiliationId
     */
    @Override
    public String getId() {
        return this.entityAffiliationId;
    }

    /**
     * Gets the value of versionNumber property
     *
     * @return versionNumber
     */
    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    /**
     * Sets the value for versionNumber property
     *
     * @param versionNumber
     */
    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    /**
     * Gets the value of objectId property
     *
     * @return objectId
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * Sets the value for objectId property
     *
     * @param objectId
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * Gets the value of entityId property
     *
     * @return entityId
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * Sets the value for entityId property
     *
     * @param entityId
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}