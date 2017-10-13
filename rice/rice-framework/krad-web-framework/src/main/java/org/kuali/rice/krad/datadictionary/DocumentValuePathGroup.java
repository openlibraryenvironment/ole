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
@BeanTag(name = "documentValuePathGroup-bean")
public class DocumentValuePathGroup extends DataDictionaryDefinitionBase {
    private static final long serialVersionUID = 6285682208264817105L;

    private List<String> paths;
    private DocumentCollectionPath documentCollectionPath;

    /**
     * @return the documentValues
     */
    @BeanTagAttribute(name = "paths", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getDocumentValues() {
        return this.paths;
    }

    /**
     * @return the documentCollectionPath
     */
    @BeanTagAttribute(name = "documentCollectionPath", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public DocumentCollectionPath getDocumentCollectionPath() {
        return this.documentCollectionPath;
    }

    /**
     * @param documentValues the documentValues to set
     */
    public void setDocumentValues(List<String> paths) {
        this.paths = paths;
    }

    /**
     * @param documentCollectionPath the documentCollectionPath to set
     */
    public void setDocumentCollectionPath(DocumentCollectionPath documentCollectionPath) {
        this.documentCollectionPath = documentCollectionPath;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryDefinition#completeValidation(java.lang.Class,
     *      java.lang.Class)
     */
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass) {
        // TODO mpham - THIS METHOD NEEDS JAVADOCS

    }

}
