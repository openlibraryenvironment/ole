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

import java.util.List;

/**
 * This is a description of what this class does - mpham don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentCollectionPath extends DataDictionaryDefinitionBase {
    private static final long serialVersionUID = -8165456163213868710L;

    private String collectionPath;
    private List<String> paths;
    private DocumentCollectionPath nestedCollection;

    /**
     * @return the documentValues
     */
    public List<String> getDocumentValues() {
        return this.paths;
    }

    /**
     * @return the documentCollectionPath
     */
    public DocumentCollectionPath getNestedCollection() {
        return this.nestedCollection;
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
    public void setNestedCollection(DocumentCollectionPath documentCollectionPath) {
        this.nestedCollection = documentCollectionPath;
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

    /**
     * @return the collectionPath
     */
    public String getCollectionPath() {
        return this.collectionPath;
    }

    /**
     * @param collectionPath the collectionPath to set
     */
    public void setCollectionPath(String collectionPath) {
        this.collectionPath = collectionPath;
    }
}
