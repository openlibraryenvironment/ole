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

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

/**
 * OptionListControl is used for listing out options from an option finder or options list.  This control can show all
 * items in the options or it can show only the selected options (if backed by a propertyName).  One use case for this
 * control is to use it in combination with UifKeyValueLocation to provide a list of locations retrieved through a
 * KeyValuesFinder.
 */
@BeanTag(name = "optionListControl-bean", parent = "Uif-OptionListControl")
public class OptionListControl extends MultiValueControlBase {
    private String itemCssClass;
    private String selectedItemCssClass;
    private boolean showOnlySelected;

    /**
     * The item css class to add to each li element of the list
     *
     * @return the item css class
     */
    @BeanTagAttribute(name = "itemCssClass")
    public String getItemCssClass() {
        return itemCssClass;
    }

    /**
     * Set the itemCssClass
     *
     * @param itemCssClass
     */
    public void setItemCssClass(String itemCssClass) {
        this.itemCssClass = itemCssClass;
    }

    /**
     * When true, only show the "selected" options (items which match a value in the property of the field).  Otherwise,
     * show all options.
     *
     * @return true if only showing selected options, otherwise show all
     */
    @BeanTagAttribute(name = "showOnlySelected")
    public boolean isShowOnlySelected() {
        return showOnlySelected;
    }

    /**
     * Set the showOnlySelected flag
     *
     * @param showOnlySelected
     */
    public void setShowOnlySelected(boolean showOnlySelected) {
        this.showOnlySelected = showOnlySelected;
    }

    /**
     * The css class to add to each item of the list which matches a value in the property
     *
     * @return the selected item css class
     */
    @BeanTagAttribute(name = "selectedItemCssClass")
    public String getSelectedItemCssClass() {
        return selectedItemCssClass;
    }

    /**
     * Set the selectedItemCssClass
     *
     * @param selectedItemCssClass
     */
    public void setSelectedItemCssClass(String selectedItemCssClass) {
        this.selectedItemCssClass = selectedItemCssClass;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        OptionListControl optionListControlCopy = (OptionListControl) component;
        optionListControlCopy.setItemCssClass(this.itemCssClass);
        optionListControlCopy.setSelectedItemCssClass(this.selectedItemCssClass);
        optionListControlCopy.setShowOnlySelected(this.showOnlySelected);
    }
}
