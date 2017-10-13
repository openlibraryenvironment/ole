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
package org.kuali.rice.kew.framework.document.search;

import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;

import java.util.List;

/**
 * An abstract implementation of a {@link DocumentSearchCustomizer} which classes can extend from and override the
 * individual methods that they require in order to perform desired customization.  All of the base method
 * implementations in this class perform the default operation of doing no customization.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class DocumentSearchCustomizerBase implements DocumentSearchCustomizer {

    /**
     * Always returns a null reference which instructs the document search framework that the criteria was not
     * customized.
     *
     * @param documentSearchCriteria the criteria on which to perform customization
     * @return a null reference indicating that no customization was performed
     */
    @Override
    public DocumentSearchCriteria customizeCriteria(DocumentSearchCriteria documentSearchCriteria) {
        return null;
    }

    /**
     * Always returns a null reference which instructs the document search framework that custom criteria clearing was not
     * performed.
     *
     * @param documentSearchCriteria the criteria on which to perform a customized clear
     * @return a null reference indicating that no customization was performed
     */
    @Override
    public DocumentSearchCriteria customizeClearCriteria(DocumentSearchCriteria documentSearchCriteria) {
        return null;
    }

    /**
     * Always returns a null reference which instructs the document search framework that the customization of results
     * was not performed.
     *
     * @param documentSearchCriteria the search criteria
     * @param defaultResults the results obtained when executing the search
     * @return a null reference indicating that no customization was performed
     */
    @Override
    public DocumentSearchResultValues customizeResults(DocumentSearchCriteria documentSearchCriteria,
            List<DocumentSearchResult> defaultResults) {
        return null;
    }

    /**
     * Always returns a null reference which instructs the document search framework that the customization of result
     * set fields was not performed.
     *
     * @param documentSearchCriteria the search criteria
     * @return a null reference indicating that no customization was performed
     */
    @Override
    public DocumentSearchResultSetConfiguration customizeResultSetConfiguration(
            DocumentSearchCriteria documentSearchCriteria) {
        return null;
    }

    /**
     * Always returns false indicating that criteria customization is disabled and should not be performed.
     *
     * @param documentTypeName the name of the document type under consideration
     * @return false to indicate that no customization should be performed
     */
    @Override
    public boolean isCustomizeCriteriaEnabled(String documentTypeName) {
        return false;
    }

    /**
     * Always returns false indicating that criteria clearing customization is disabled and should not be performed.
     *
     * @param documentTypeName the name of the document type under consideration
     * @return false to indicate that no customization should be performed
     */
    @Override
    public boolean isCustomizeClearCriteriaEnabled(String documentTypeName) {
        return false;
    }

    /**
     * Always returns false indicating that results customization is disabled and should not be performed.
     *
     * @param documentTypeName the name of the document type under consideration
     * @return false to indicate that no customization should be performed
     */
    @Override
    public boolean isCustomizeResultsEnabled(String documentTypeName) {
        return false;
    }

    /**
     * Always returns false indicating that result set field customization is disabled and should not be performed.
     *
     * @param documentTypeName the name of the document type under consideration
     * @return false to indicate that no customization should be performed
     */
    @Override
    public boolean isCustomizeResultSetFieldsEnabled(String documentTypeName) {
        return false;
    }

}
