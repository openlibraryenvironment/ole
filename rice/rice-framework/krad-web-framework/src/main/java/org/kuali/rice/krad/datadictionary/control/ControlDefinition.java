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
package org.kuali.rice.krad.datadictionary.control;

import org.kuali.rice.krad.datadictionary.DataDictionaryDefinition;

/**
 * ControlDefinition
 */
@Deprecated
public interface ControlDefinition extends DataDictionaryDefinition {

    public boolean isDatePicker();

    public void setDatePicker(boolean datePicker);

    public boolean isExpandedTextArea();

    public void setExpandedTextArea(boolean eTextArea);

    /**
     * @return true if this ControlDefinition instance represents an HTML checkbox control
     */
    public boolean isCheckbox();

    /**
     * @return true if this ControlDefinition instance represents an HTML hidden control
     */
    public boolean isHidden();

    /**
     * @return true if this ControlDefinition instance represents an HTML radiobutton control
     */
    public boolean isRadio();

    /**
     * @return true if this ControlDefinition instance represents an HTML select control
     */
    public boolean isSelect();

    /**
     * @return true if this ControlDefinition instance represents an HTML select control
     */
    public boolean isMultiselect();

    /**
     * @return true if this ControlDefinition instance represents an HTML text control
     */
    public boolean isText();

    /**
     * @return true if this ControlDefinition instance represents an HTML textarea control
     */
    public boolean isTextarea();

    /**
     * @return any Script associated with this control
     */
    public String getScript();

    /**
     * @return true if the ControlDefinition instance represents a currency control
     */
    public boolean isCurrency();

    /**
     * @return true if the ControlDefinition instance represents a kualiUser control
     */
    public boolean isKualiUser();

    /**
     * @return true if the ControlDefinition instance represents a workflow workgroup control
     */
    public boolean isWorkflowWorkgroup();

    /**
     * @return true if this ControlDefinition instance represents an HTML File control
     */
    public boolean isFile();

    /**
     * @return true if the ControlDefinition instance represents a lookupHidden control
     */
    public boolean isLookupHidden();

    /**
     * @return true if the ControlDefinition instance represents a lookupReadonly control
     */
    public boolean isLookupReadonly();

    /**
     * @return true if the ControlDefinition instance represents a button control
     */
    public boolean isButton();

    /**
     * @return true if the ControlDefinition instance represents a link control
     */
    public boolean isLink();

    /**
     * @return true if the ControlDefinition instance represents a ranged (will render from and to fields) date control
     */
    public boolean isRanged();

    /**
     * Sets the Class used to retrieve the complete range of values for radiobutton and select controls.
     *
     * @param valuesFinderClass
     */
    public void setValuesFinderClass(String valuesFinderClass);

    /**
     * Sets the BO Class used for the KeyLabelBusinessObjectValueFinder to retrieve the complete range of values for
     * radiobutton and select controls.
     *
     * @param businessObjectClass
     */
    public void setBusinessObjectClass(String businessObjectClass);

    /**
     * Sets the keyAttribute used for building radiobutton and select controls.
     *
     * @param keyAttribute
     */
    public void setKeyAttribute(String keyAttribute);

    /**
     * Sets the labelAttribute used for building radiobutton and select controls.
     *
     * @param labelAttribute
     */
    public void setLabelAttribute(String labelAttribute);

    public void setIncludeBlankRow(Boolean includeBlankRow);

    /**
     * @param includeKeyInLabel whether to include the key with the label to be displayed or not.
     */
    public void setIncludeKeyInLabel(Boolean includeKeyInLabel);

    /**
     * Sets the Script
     *
     * @param script
     */
    public void setScript(String script);

    /**
     * @return Class used to retrieve the complete range of values for radiobutton and select controls.
     */
    public String getValuesFinderClass();

    /**
     * @return BO Class used for the KeyLabelBusinessObjectValueFinder to retrieve the complete range of values for
     *         radiobutton and select controls.
     */
    public String getBusinessObjectClass();

    /**
     * @return the keyAttribute used for radiobutton and select controls.
     */
    public String getKeyAttribute();

    /**
     * @return the labelAttribute used for radiobutton and select controls.
     */
    public String getLabelAttribute();

    public Boolean getIncludeBlankRow();

    /**
     * Gets the flag that indicates if the labels the ValuesFinder class returns should include the key.
     *
     * @param includeKeyInLabel
     */
    public Boolean getIncludeKeyInLabel();

    /**
     * Sets the size parameter for text controls.
     *
     * @param size
     */
    public void setSize(Integer size);

    /**
     * @return size parameters for text controls
     */
    public Integer getSize();

    /**
     * Sets the rows parameter for textarea controls.
     *
     * @param rows
     */
    public void setRows(Integer rows);

    /**
     * @return rows parameters for textarea controls
     */
    public Integer getRows();

    /**
     * Sets the cols parameter for textarea controls.
     *
     * @param cols
     */
    public void setCols(Integer cols);

    /**
     * @return cols parameter for textarea controls.
     */
    public Integer getCols();

}
