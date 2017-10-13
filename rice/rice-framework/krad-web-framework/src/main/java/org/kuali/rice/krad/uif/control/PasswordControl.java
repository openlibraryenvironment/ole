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
package org.kuali.rice.krad.uif.control;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.DatePicker;

import java.util.List;

/**
 * Represents a HTML password text control, rendered as a input field of type
 * 'password'. This can receive and display a mask for a single value.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "passwordControl-bean", parent = "Uif-PasswordControl")
public class PasswordControl extends ControlBase implements SizedControl {
    private static final long serialVersionUID = -8267606288443759880L;

    private int size;
    private Integer maxLength;
    private Integer minLength;

    private String watermarkText = StringUtils.EMPTY;

    public PasswordControl() {
        super();
    }

    /**
     * The following actions are performed:
     *
     * <ul>
     * <li>Defaults maxLength, minLength (if not set) to maxLength of parent field</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        if (parent instanceof InputField) {
            InputField field = (InputField) parent;
            if (getMaxLength() == null) {
                setMaxLength(field.getMaxLength());
            }

            if (getMinLength() == null) {
                setMinLength(field.getMinLength());
            }
        }
    }

    /**
     * @see SizedControl#getSize()
     */
    @BeanTagAttribute(name = "size")
    public int getSize() {
        return this.size;
    }

    /**
     * @see SizedControl#setSize(int)
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Maximum number of characters that can be inputted
     *
     * <p>If not set on control, max length of field will be used</p>
     *
     * @return max number of characters
     */
    @BeanTagAttribute(name = "maxLength")
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * Setter for the max number of input characters
     *
     * @param maxLength
     */
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Minimum number of characters that can be inputted
     *
     * <p>If not set on control, min length of field will be used</p>
     *
     * @return max number of characters
     */
    @BeanTagAttribute(name = "minLength")
    public Integer getMinLength() {
        return minLength;
    }

    /**
     * Setter for the min number of input characters
     *
     * @param minLength
     */
    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    /**
     * Gets the watermark text for this PasswordControl.
     *
     * <p>
     * A watermark typically appears as light gray text within the Password input element whenever the
     * element is empty and does not have focus. This provides a hint to the user as to what the input
     * is used for, or the type of input that is required.
     * </p>
     *
     * @return the watermarkText
     */
    @BeanTagAttribute(name = "watermarkText")
    public String getWatermarkText() {
        return this.watermarkText;
    }

    /**
     * Sets the watermark text for this PasswordControl
     *
     * @param watermarkText the watermarkText to set
     */
    public void setWatermarkText(String watermarkText) {
        //to avoid users from putting in the same value as the watermark adding some spaces here
        //see watermark troubleshooting for more info
        if (StringUtils.isNotEmpty(watermarkText)) {
            watermarkText = watermarkText + "   ";
        }

        this.watermarkText = watermarkText;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        PasswordControl passwordControlCopy = (PasswordControl) component;
        passwordControlCopy.setSize(this.size);
        passwordControlCopy.setMaxLength(this.maxLength);
        passwordControlCopy.setMinLength(this.minLength);
        passwordControlCopy.setWatermarkText(this.watermarkText);
    }
}
