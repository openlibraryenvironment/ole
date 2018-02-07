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
 * Provides various mechanisms for an application to customize the behavior of the Document Search functionality for
 * their document types.  Client applications should provide implementations of this interface if they need to
 * enact some of these customizations on document searches.  These customizers then get mapped to the appropriate
 * document types via the KEW extension framework (see
 * {@link org.kuali.rice.kew.api.extension.ExtensionRepositoryService}).
 *
 * <p>The customization functionality provided by this interface includes:</p>
 *
 * <ul>
 *     <li>The ability to customize document search criteria before it gets submitted.</li>
 *     <li>The ability to customize how document search criteria is cleared.</li>
 *     <li>The ability to customize how result data is processed and presented.</li>
 * </ul>
 *
 * <p>Only one {@code DocumentSearchCustomizer} is supported for a given document type.  It is important to note however
 * that it is permitted that an implementation of this interface could be tied to more than one document type via the
 * extension framework.  This is why information about the specific document type for which the customizations is being
 * applied is passed to all methods on this interface (note that the document type information is available from the
 * {@code DocumentSearchCriteria} as well.</p>
 *
 * <p>Furthermore, the customization hooks afforded by this interface will only be executed when a valid document type
 * has been specified as input to the document search.  This is because the customizer is tied to the document type and
 * the document search must therefore be able to resolve a valid document type from the user-supplied criteria in order
 * to perform these customizations.</p>
 *
 * <p>Since some of the operations on this component could potentially add expense to the overall search process in the
 * cases where customization is only done on certain document types or only certain customization features are being
 * utilized, this interface provides for a set of boolean operations which indicate which customization
 * features should be activated by the document search framework for a given document type.  It's expected that KEW
 * will check each of these flags prior to invoking the corresponding method that it "gaurds" and, if the customization
 * flag is disabled, it should refrain from executing that method.</p>
 *
 * <p></p>Implementations of this interface are expected to return "true" for any of these operations for which they
 * provide a custom implementation.  <strong>It is important to note that callers are permitted to cache the results
 * returned from these boolean methods for an unspecified length of time.</strong>  So if there is any possibility that,
 * for a given document type, the implementation might perform a particular customization, then "true" should be
 * returned from the appropriate boolean method.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentSearchCustomizer {

    /**
     * Performs customization on the given document search criteria.  This method should return the customized version
     * of the criteria, or null if no customizations were performed.  This customization is invoked prior the actual
     * search being executed, and the resulting customized criteria is what gets passed to the search implementation.
     * It is also invoked in situtations where the document search is performed via the api or via the user interface.
     *
     * <p>It is guaranteed that the document type name on the given criteria will never be null and will always
     * represent a valid document type.</p>
     *
     * @param documentSearchCriteria the original criteria against which to perform customization, will never be null
     *
     * @return the customized criteria, or null if no customization was performed
     */
    DocumentSearchCriteria customizeCriteria(DocumentSearchCriteria documentSearchCriteria);

    /**
     * Performs a customized "clear" of the given document search criteria.  Clearing of the criteria is typically
     * invoked whenever the user of the document search user interface clicks the "clear" button.  By default all of
     * the search criteria is cleared, but this method allows for certain criteria data to be preserved on a clear if
     * desired.
     *
     * <p>A common use of this feature is to preserve the document type that has been selected when clearing criteria
     * for a customized document search.
     *
     * <p>It is guaranteed that the document type name on the given criteria will never be null and will always
     * represent a valid document type.</p>
     *
     * @param documentSearchCriteria the criteria to clear
     *
     * @return the cleared criteria, or null if no custom criteria clearing was performed
     */
    DocumentSearchCriteria customizeClearCriteria(DocumentSearchCriteria documentSearchCriteria);

    /**
     * Performs customization of the given list of document search results.  This method is invoked after the search is
     * executed but before results are returned to the caller.  It is executed when a document search is executed via
     * the api or from the document search user interface.
     *
     * <p>This method returns a {@code DocumentSearchResultValues} object which contains a list of
     * {@link DocumentSearchResultValue} objects.  Each of these result values maps to a specific document id and
     * contains a list of {@link org.kuali.rice.kew.api.document.attribute.DocumentAttribute} values which can be used
     * to modify existing document attributes or create new ones that are included as part of the search results.  It
     * is important to note that in order for these custom attribute values to be displayed in the result set in the
     * document search user interface, there must be a corresponding entry in the
     * {@link DocumentSearchResultSetConfiguration} returned by the
     * {@link #customizeResultSetConfiguration(org.kuali.rice.kew.api.document.search.DocumentSearchCriteria)} method
     * on this customizer implementation.</p>
     *
     * <p>It is permissible that implementations of this method may not return result values for all of the document
     * provided in the given list of document search results.  It is important to note however that ommision from the
     * returned result values does not filter or remove the result from the search results.  Generally speaking, this
     * method cannot be used to remove results from the result set.</p>
     *
     * <p>It is guaranteed that the document type name on the given criteria will never be null and will always
     * represent a valid document type.</p>
     *
     * @param documentSearchCriteria the criteria against which the document search was executed
     * @param defaultResults the results that were returned by the execution of the document search
     * 
     * @return customized search result values for any of the document results requiring custom values, or null if no
     * customization was performed
     */
    DocumentSearchResultValues customizeResults(DocumentSearchCriteria documentSearchCriteria, List<DocumentSearchResult> defaultResults);

    /**
     * Performs customization of what result fields should be displayed in the result set.  Allows for hiding of
     * standard fields, addition of custom fields, and the ability to override the default behavior of searchable
     * attributes that are defined for the document type.  Generally speaking, this controls which "columns" of data are
     * displayed in the document search results.
     *
     * <p>This method is only invoked by the document search user interface whenever it is rendering document search
     * results.  It is not invoked when invoking document search using only the api.</p>
     *
     * <p>It is guaranteed that the document type name on the given criteria will never be null and will always
     * represent a valid document type.</p>
     *
     * @param documentSearchCriteria the criteria against which the document search was executed
     * @return the customized result set configuration, or null if no customization was performed
     */
    DocumentSearchResultSetConfiguration customizeResultSetConfiguration(DocumentSearchCriteria documentSearchCriteria);

    /**
     * Indicates if the {@link #customizeCriteria(org.kuali.rice.kew.api.document.search.DocumentSearchCriteria)} on
     * this customizer should be invoked for the document type with the given name.  The caller of this method is
     * permitted to cache the return value for a length of time of their choosing.
     *
     * @param documentTypeName the name of the document type against which this customizer is being applied
     * @return true if the customization method should be executed, false otherwise
     */
    boolean isCustomizeCriteriaEnabled(String documentTypeName);

    /**
     * Indicates if the {@link #customizeClearCriteria(org.kuali.rice.kew.api.document.search.DocumentSearchCriteria)}
     * on this customizer should be invoked for the document type with the given name.  The caller of this method is
     * permitted to cache the return value for a length of time of their choosing.
     *
     * @param documentTypeName the name of the document type against which this customizer is being applied
     * @return true if the customization method should be executed, false otherwise
     */
    boolean isCustomizeClearCriteriaEnabled(String documentTypeName);

    /**
     * Indicates if the {@link #customizeResults(org.kuali.rice.kew.api.document.search.DocumentSearchCriteria, java.util.List)}
     * on this customizer should be invoked for the document type with the given name.  The caller of this method is
     * permitted to cache the return value for a length of time of their choosing.
     *
     * @param documentTypeName the name of the document type against which this customizer is being applied
     * @return true if the customization method should be executed, false otherwise
     */
    boolean isCustomizeResultsEnabled(String documentTypeName);

    /**
     * Indicates if the {@link #customizeResultSetConfiguration(org.kuali.rice.kew.api.document.search.DocumentSearchCriteria)}
     * on this customizer should be invoked for the document type with the given name.  The caller of this method is
     * permitted to cache the return value for a length of time of their choosing.
     *
     * @param documentTypeName the name of the document type against which this customizer is being applied
     * @return true if the customization method should be executed, false otherwise
     */
    boolean isCustomizeResultSetFieldsEnabled(String documentTypeName);

}
