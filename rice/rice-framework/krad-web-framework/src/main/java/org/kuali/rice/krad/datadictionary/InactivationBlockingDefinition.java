/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.datadictionary;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

/**
 * This is a description of what this class does - wliang don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "inactivationBlockingDefinition-bean")
public class InactivationBlockingDefinition extends DataDictionaryDefinitionBase implements InactivationBlockingMetadata {
    private static final long serialVersionUID = -8765429636173190984L;

    protected Class<? extends BusinessObject> blockingReferenceBusinessObjectClass;
    protected String blockedReferencePropertyName;
    protected Class<? extends BusinessObject> blockedBusinessObjectClass;
    protected String inactivationBlockingDetectionServiceBeanName;
    protected String relationshipLabel;
    protected Class<? extends BusinessObject> businessObjectClass;

    public InactivationBlockingDefinition() {
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryDefinition#completeValidation(java.lang.Class,
     *      java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass) {
        if (StringUtils.isBlank(inactivationBlockingDetectionServiceBeanName)) {
            if (StringUtils.isBlank(blockedReferencePropertyName)) {
                // the default inactivation blocking detection service (used when inactivationBlockingDetectionServiceBeanName is blank) requires that the property name be set
                throw new AttributeValidationException(
                        "inactivationBlockingDetectionServiceBeanName and  blockedReferencePropertyName can't both be blank in InactivationBlockingDefinition for class "
                                + rootBusinessObjectClass.getClass().getName());
            }
        }
        if (getBlockedBusinessObjectClass() == null) {
            throw new AttributeValidationException(
                    "Unable to determine blockedReferenceBusinessObjectClass in InactivationBlockingDefinition for class "
                            + rootBusinessObjectClass.getClass().getName());
        }
        if (!BusinessObject.class.isAssignableFrom(getBlockedBusinessObjectClass())) {
            throw new AttributeValidationException(
                    "InactivationBlockingDefinitions must block a reference of type BusinessObject.  Class name: "
                            +
                            rootBusinessObjectClass.getClass().getName()
                            + " blockedReferencePropertyName "
                            + blockedReferencePropertyName
                            +
                            " class that should have been a BusinessObject: "
                            + getBlockedBusinessObjectClass());
        }
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.datadictionary.InactivationBlockingMetadata#getBlockedReferencePropertyName()
     */
    @BeanTagAttribute(name = "blockedReferencePropertyName")
    public String getBlockedReferencePropertyName() {
        return this.blockedReferencePropertyName;
    }

    public void setBlockedReferencePropertyName(String blockedReferencePropertyName) {
        this.blockedReferencePropertyName = blockedReferencePropertyName;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.datadictionary.InactivationBlockingMetadata#getBlockedBusinessObjectClass()
     */
    @BeanTagAttribute(name = "blockedBusinessObjectClass")
    public Class<? extends BusinessObject> getBlockedBusinessObjectClass() {
        return this.blockedBusinessObjectClass;
    }

    public void setBlockedBusinessObjectClass(Class<? extends BusinessObject> blockedBusinessObjectClass) {
        this.blockedBusinessObjectClass = blockedBusinessObjectClass;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.datadictionary.InactivationBlockingMetadata#getInactivationBlockingDetectionServiceBeanName()
     */
    @BeanTagAttribute(name = "inactivationBlockingDetectionServiceBeanName")
    public String getInactivationBlockingDetectionServiceBeanName() {
        return this.inactivationBlockingDetectionServiceBeanName;
    }

    public void setInactivationBlockingDetectionServiceBeanName(String inactivationBlockingDetectionServiceImpl) {
        this.inactivationBlockingDetectionServiceBeanName = inactivationBlockingDetectionServiceImpl;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.datadictionary.InactivationBlockingMetadata#getBlockingReferenceBusinessObjectClass()
     */
    @BeanTagAttribute(name = "blockingReferenceBusinessObjectClass")
    public Class<? extends BusinessObject> getBlockingReferenceBusinessObjectClass() {
        return this.blockingReferenceBusinessObjectClass;
    }

    public void setBlockingReferenceBusinessObjectClass(
            Class<? extends BusinessObject> blockingReferenceBusinessObjectClass) {
        this.blockingReferenceBusinessObjectClass = blockingReferenceBusinessObjectClass;
    }

    @BeanTagAttribute(name = "relationshipLabel")
    public String getRelationshipLabel() {
        return this.relationshipLabel;
    }

    public void setRelationshipLabel(String relationshipLabel) {
        this.relationshipLabel = relationshipLabel;
    }

    @BeanTagAttribute(name = "businessObjectClass")
    public Class<? extends BusinessObject> getBusinessObjectClass() {
        return this.businessObjectClass;
    }

    public void setBusinessObjectClass(Class<? extends BusinessObject> businessObjectClass) {
        this.businessObjectClass = businessObjectClass;
    }

    @Override
    public String toString() {
        return "InactivationBlockingDefinition: blockedClass="
                + blockedBusinessObjectClass.getName()
                + " /blockingReferenceProperty="
                + blockedReferencePropertyName
                + " /blockingClass="
                + blockingReferenceBusinessObjectClass.getName();
    }
}
