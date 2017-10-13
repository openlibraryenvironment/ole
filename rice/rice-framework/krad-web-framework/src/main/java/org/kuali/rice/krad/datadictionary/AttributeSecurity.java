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

import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.mask.MaskFormatter;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBeanBase;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;

/**
 * Defines a set of restrictions that are possible on an attribute
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "attributeSecurity-bean")
public class AttributeSecurity extends UifDictionaryBeanBase {
    private static final long serialVersionUID = -7923499408946975318L;

    private boolean readOnly = false;
    private boolean hide = false;
    private boolean mask = false;
    private boolean partialMask = false;

    private MaskFormatter partialMaskFormatter;
    private MaskFormatter maskFormatter;

    /**
     * @return the readOnly
     */
    @BeanTagAttribute(name = "readOnly")
    public boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * @return the hide
     */
    @BeanTagAttribute(name = "hide")
    public boolean isHide() {
        return this.hide;
    }

    /**
     * @param hide the hide to set
     */
    public void setHide(boolean hide) {
        this.hide = hide;
    }

    /**
     * @return the mask
     */
    @BeanTagAttribute(name = "mask")
    public boolean isMask() {
        return this.mask;
    }

    /**
     * @param mask the mask to set
     */
    public void setMask(boolean mask) {
        this.mask = mask;
    }

    /**
     * @return the partialMask
     */
    @BeanTagAttribute(name = "partialMask")
    public boolean isPartialMask() {
        return this.partialMask;
    }

    /**
     * @param partialMask the partialMask to set
     */
    public void setPartialMask(boolean partialMask) {
        this.partialMask = partialMask;
    }

    /**
     * @return the maskFormatter
     */
    @BeanTagAttribute(name = "maskFormatter", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public MaskFormatter getMaskFormatter() {
        return this.maskFormatter;
    }

    /**
     * @param maskFormatter the maskFormatter to set
     */
    public void setMaskFormatter(MaskFormatter maskFormatter) {
        this.maskFormatter = maskFormatter;
    }

    /**
     * @return the partialMaskFormatter
     */
    @BeanTagAttribute(name = "partialMaskFormatter", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public MaskFormatter getPartialMaskFormatter() {
        return this.partialMaskFormatter;
    }

    /**
     * @param partialMaskFormatter the partialMaskFormatter to set
     */
    public void setPartialMaskFormatter(MaskFormatter partialMaskFormatter) {
        this.partialMaskFormatter = partialMaskFormatter;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryDefinition#completeValidation(java.lang.Class,
     *      java.lang.Class)
     */
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass) {

        if (mask && maskFormatter == null) {
            throw new AttributeValidationException("MaskFormatter is required");
        }
        if (partialMask && partialMaskFormatter == null) {
            throw new AttributeValidationException("PartialMaskFormatter is required");
        }
    }

    /**
     * Directly validate simple fields
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryEntry#completeValidation(org.kuali.rice.krad.datadictionary.validator.ValidationTrace)
     */
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass,
            ValidationTrace tracer) {
        tracer.addBean(this.getClass().getSimpleName(), ValidationTrace.NO_BEAN_ID);

        if (mask && maskFormatter == null) {
            String currentValues[] = {"mask = " + mask, "maskFormatter = " + maskFormatter};
            tracer.createError("MaskFormatter is required", currentValues);
        }
        if (partialMask && partialMaskFormatter == null) {
            String currentValues[] = {"partialMask = " + partialMask, "partialMaskFormatter = " + partialMaskFormatter};
            tracer.createError("PartialMaskFormatter is required", currentValues);
        }

    }

    /**
     * Returns whether any of the restrictions defined in this class are true.
     */
    public boolean hasAnyRestriction() {
        return readOnly || mask || partialMask || hide;
    }

    /**
     * Returns whether any of the restrictions defined in this class indicate that the attribute value potentially
     * needs
     * to be not shown to the user (i.e. masked, partial mask, hide).  Note that readonly does not fall in this
     * category.
     *
     * @return
     */
    public boolean hasRestrictionThatRemovesValueFromUI() {
        return mask || partialMask || hide;
    }

    /**
     * Returns a copy of the component.
     *
     * @return AttributeSecurity copy of the component
     */
    public <T> T copy() {
        T copiedClass = null;
        try {
            copiedClass = (T)this.getClass().newInstance();
        }
        catch(Exception exception) {
            throw new RuntimeException();
        }

        copyProperties(copiedClass);

        return copiedClass;
    }

    /**
     * Copies the properties over for the copy method
     *
     */
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        AttributeSecurity attributeSecurityCopy = ((AttributeSecurity)component);
        attributeSecurityCopy.setHide(this.hide);
        attributeSecurityCopy.setMask(this.mask);
        attributeSecurityCopy.setPartialMask(this.partialMask);
        attributeSecurityCopy.setReadOnly(this.readOnly);
        attributeSecurityCopy.setMaskFormatter(this.maskFormatter);
        attributeSecurityCopy.setPartialMaskFormatter(this.partialMaskFormatter);
    }
}
