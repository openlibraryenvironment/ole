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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group of HTML Radio controls. Provides preset options for the
 * user to choose by a series of radio controls. Only one option can be selected
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "verticalRadioControl-bean", parent = "Uif-VerticalRadioControl"),
        @BeanTag(name = "horizontalRadioControl-bean", parent = "Uif-HorizontalRadioControl")})
public class RadioGroupControl extends MultiValueControlBase {
    private static final long serialVersionUID = 8800478332086081970L;
    private List<String> fieldsetClasses;

    private String delimiter;

    public RadioGroupControl() {
        super();
        fieldsetClasses = new ArrayList<String>();
    }

    /**
     * Delimiter string to be rendered between the radio group options, defaults
     * to none
     *
     * @return delimiter string
     */
    @BeanTagAttribute(name = "delimiter")
    public String getDelimiter() {
        return this.delimiter;
    }

    /**
     * Setter for the string delimiter for each radio option
     *
     * @param delimiter delimeter to render between options
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Get fieldsetClasses which are the classes that will be applied to this component's fieldset when generated
     *
     * @return css classes for the fieldset
     */
    @BeanTagAttribute(name = "fieldsetClasses", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getFieldsetClasses() {
        return fieldsetClasses;
    }

    /**
     * Set fieldsetClasses
     *
     * @param fieldsetClasses css classes for the fieldset
     */
    public void setFieldsetClasses(List<String> fieldsetClasses) {
        this.fieldsetClasses = fieldsetClasses;
    }

    /**
     * Builds the HTML class attribute string by combining the fieldsetClasses list
     * with a space delimiter
     *
     * @return class attribute string
     */
    public String getFieldsetClassesAsString() {
        if (fieldsetClasses != null) {
            return StringUtils.join(fieldsetClasses, " ");
        }

        return "";
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        RadioGroupControl radioGroupControlCopy = (RadioGroupControl) component;

        if(fieldsetClasses != null) {
            radioGroupControlCopy.setFieldsetClasses(new ArrayList<String>(fieldsetClasses));
        }

        radioGroupControlCopy.setDelimiter(this.delimiter);
    }
}
