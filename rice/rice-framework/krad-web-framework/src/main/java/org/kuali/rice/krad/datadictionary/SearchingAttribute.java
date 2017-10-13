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

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

/**
 * This is a description of what this class does - mpham don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "searchingAttribute-bean")
public class SearchingAttribute extends WorkflowAttributeMetadata {
    private static final long serialVersionUID = -612461988789474893L;

    private String businessObjectClassName;
    private String attributeName;
    private boolean showAttributeInSearchCriteria = true;
    private boolean showAttributeInResultSet = false;

    /**
     * @return the businessObjectClassName
     */
    @BeanTagAttribute(name = "businessObjectClassName")
    public String getBusinessObjectClassName() {
        return this.businessObjectClassName;
    }

    /**
     * @return the attributeName
     */
    @BeanTagAttribute(name = "attributeName")
    public String getAttributeName() {
        return this.attributeName;
    }

    /**
     * @param businessObjectClassName the businessObjectClassName to set
     */
    public void setBusinessObjectClassName(String businessObjectClassName) {
        this.businessObjectClassName = businessObjectClassName;
    }

    /**
     * @param attributeName the attributeName to set
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * Returns whether this attribute should appear in the search criteria
     *
     * @return the showAttributeInSearchCriteria
     */
    @BeanTagAttribute(name = "showAttriubteInSearchCriteria")
    public boolean isShowAttributeInSearchCriteria() {
        return this.showAttributeInSearchCriteria;
    }

    /**
     * Sets whether this attribute should appear in the search criteria
     *
     * @param showAttributeInSearchCriteria the showAttributeInSearchCriteria to set
     */
    public void setShowAttributeInSearchCriteria(boolean showAttributeInSearchCriteria) {
        this.showAttributeInSearchCriteria = showAttributeInSearchCriteria;
    }

    /**
     * Returns whether this attribute should appear in the result set
     *
     * @return the showAttributeInResultSet
     */
    @BeanTagAttribute(name = "ShowAttributeInResultSet")
    public boolean isShowAttributeInResultSet() {
        return this.showAttributeInResultSet;
    }

    /**
     * Sets whether this attribute should appear in the result set
     *
     * @param showAttributeInResultSet the showAttributeInResultSet to set
     */
    public void setShowAttributeInResultSet(boolean showAttributeInResultSet) {
        this.showAttributeInResultSet = showAttributeInResultSet;
    }
}
