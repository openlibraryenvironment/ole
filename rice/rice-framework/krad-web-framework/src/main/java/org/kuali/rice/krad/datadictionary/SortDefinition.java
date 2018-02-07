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
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The defaultSort element specifies the sequence in which the lookup search results should be displayed
 *
 * <p>It contains an ascending/descending indicator and a list of attribute names.
 * JSTL: defaultSort is a Map with the following keys:
 * sortAscending (boolean String) and sortAttributes (Map).
 * By the time JSTL export occurs, the optional attributeName from the defaultSort
 * tag will have been converted into the first contained sortAttribute.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SortDefinition extends DataDictionaryDefinitionBase {
    private static final long serialVersionUID = -1092811342186612461L;

    protected boolean sortAscending = true;
    protected List<String> attributeNames = new ArrayList<String>();

    public SortDefinition() {

    }

    /**
     * The sortAttribute element defines one part of the sort key.
     * The full sort key is comprised of the sortAttribute's in the
     * order in which they have been defined.
     *
     * DD: See SortAttributesDefinition.java.
     *
     * JSTL: sortAttribute is a Map which is accessed using a
     * key of the attributeName of the sortAttribute.
     * It contains a single entry with the following key:
     * "attributeName"
     *
     * The associated value is the attributeName of the sortAttribute.
     * See LookupMapBuilder.java
     *
     * @throws IllegalArgumentException if the given attributeName is blank
     */
    public void setAttributeName(String attributeName) {
        if (StringUtils.isBlank(attributeName)) {
            throw new IllegalArgumentException("invalid (blank) attributeName");
        }
        if (!attributeNames.isEmpty()) {
            throw new IllegalStateException(
                    "unable to set sort attributeName when sortAttributes have already been added");
        }

        attributeNames.add(attributeName);
    }

    /**
     * @return the List of associated attribute names as Strings
     */
    public List<String> getAttributeNames() {
        return this.attributeNames;
    }

    /**
     * Indicates that the items must be sorted in ascending order
     *
     * @return true if items should sort in ascending order
     */
    public boolean getSortAscending() {
        return sortAscending;
    }

    /**
     * Setter for the flag to indicate ascending sorting of items
     *
     * @param sortAscending
     */
    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    /**
     * Directly validate simple fields
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryDefinition#completeValidation(Class, Class)
     */
    @Override
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass) {
        for (String attributeName : attributeNames) {
            if (!DataDictionary.isPropertyOf(rootBusinessObjectClass, attributeName)) {
                throw new AttributeValidationException("unable to find sort attribute '"
                        + attributeName
                        + "' in rootBusinessObjectClass '"
                        + rootBusinessObjectClass.getName()
                        + "' ("
                        + ")");
            }
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder attrList = new StringBuilder("[");
        for (Iterator<String> i = attributeNames.iterator(); i.hasNext(); ) {
            attrList.append(i.next());
            if (i.hasNext()) {
                attrList.append(",");
            }
        }
        attrList.append("]");

        return "SortDefinition :  " + attrList;
    }

    /**
     * The sortAttributes element allows a multiple-part sort key to be defined
     *
     * JSTL: sortAttributes is a Map which is accessed using a key of "sortAttributes". This map contains an entry for
     * sort attribute.  The key is: attributeName of a sort field. The associated value is a sortAttribute ExportMap.
     */
    public void setAttributeNames(List<String> attributeNames) {
        this.attributeNames = attributeNames;
    }

}
