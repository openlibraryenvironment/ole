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

import java.util.List;

/**
 * This is a description of what this class does - mpham don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "searchingTypeDefinition-bean")
public class SearchingTypeDefinition extends DataDictionaryDefinitionBase {
    private static final long serialVersionUID = -8779609937539520677L;

    private SearchingAttribute searchingAttribute;
    private List<String> paths;

    /**
     * @return the searchingAttribute
     */
    @BeanTagAttribute(name = "searchAttribute", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public SearchingAttribute getSearchingAttribute() {
        return this.searchingAttribute;
    }

    /**
     * @return the documentValues
     */
    @BeanTagAttribute(name = "documentValues", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getDocumentValues() {
        return this.paths;
    }

    /**
     * @param searchingAttribute the searchingAttribute to set
     */
    public void setSearchingAttribute(SearchingAttribute searchingAttribute) {
        this.searchingAttribute = searchingAttribute;
    }

    /**
     * @param documentValues the documentValues to set
     */
    public void setDocumentValues(List<String> paths) {
        this.paths = paths;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryDefinition#completeValidation(java.lang.Class,
     *      java.lang.Class)
     */
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass) {

    }
}
