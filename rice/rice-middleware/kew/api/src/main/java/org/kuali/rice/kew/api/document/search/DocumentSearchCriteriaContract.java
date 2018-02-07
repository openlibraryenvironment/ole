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
package org.kuali.rice.kew.api.document.search;

import org.joda.time.DateTime;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentStatusCategory;

import java.util.List;
import java.util.Map;

/**
 * Defines the contract for criteria used to perform lookups of workflow document data.  None of the elements that can
 * be defined on the criteria are required.  Therefore, any method on this class may return a null value, though in the
 * case of collections, an empty collection will be returned instead.
 *
 * <p>In general, the different  values on the criteria allow the standard lookup "operators" as defined by
 * {@link org.kuali.rice.core.api.search.SearchOperator} unless otherwise noted.  The primary place where this differs
 * is on principal name-based criteria (see below).</p>
 *
 * <p><On criteria which contains a principal name, the rules are as follows:</p>
 *
 * <ul>
 *   <li>only literal principal names that resolve to a valid principal are allowed</li>
 *   <li>however, if the principal names don't resolve to valid prinicpals, this simply means the lookup will return no results</li>
 *   <li>"!" is allowed before a principal name</li>
 *   <li>when wanting to search for more than one principal, "|" and "&&" is allowed though they cannot be used together</li>
 * </ul>
 *
 * <p>Wildcards, ranges, and other "inequality" operators (such as ">", "<", etc.) are not permitted on principal names.</p>
 *
 * <p>In cases where a criteria element takes a list of values, this should be treated as an implicit "OR" by the lookup
 * implementation.  This is true of document attribute values as well, which are passed as a map keyed off the
 * document attribute name with a list of values representing the document attribute values to be searched for.</p>
 *
 * <p>The optional "save name" on the search defines a name under which the criteria can be stored so that it can be
 * recalled and reused later.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentSearchCriteriaContract {

    /**
     * Returns the document id criteria to search against when executing the document search.
     *
     * @return the document id criteria
     */
    String getDocumentId();

    /**
     * Returns an unmodifiable list of document statuses to search against when executing the document search.  If there
     * is more than one of these, then the search should treat this as an "OR" case (i.e. search for documents with one
     * or more of these statuses).
     *
     * @return the document status criteria
     */
    List<DocumentStatus> getDocumentStatuses();

    /**
     * Returns an unmodifiable list of document status categories to search against when executing the document search.
     * If there is more than one of these, then the search should treat this as an "OR" case (i.e. search for documents
     * that have a status contained in one or more of these categories).
     *
     * @return the document status category criteria
     */
    List<DocumentStatusCategory> getDocumentStatusCategories();

    /**
     * Returns the document title criteria to search against when executing the document search.
     *
     * @return the title criteria
     */
    String getTitle();

    /**
     * Returns the application document id criteria to search against when executing the document search.
     *
     * @return the application document id criteria
     */
    String getApplicationDocumentId();

    /**
     * Returns the application document status criteria to search against when executing the document search.
     *
     * @return the application document status criteria
     * @deprecated please use {@link #getApplicationDocumentStatuses()} instead
     */
    @Deprecated
    String getApplicationDocumentStatus();

    /**
     * Returns the criteria for the principal name of the document initiator to search against when executing the
     * document search.  Follows the rules for principal name criteria (see class-level documentation).
     *
     * @return the initiator principal name criteria
     */
    String getInitiatorPrincipalName();

    /**
     * Returns the criteria for the principal id of the document initiator to search against when executing the
     * document search.
     *
     * @return the initiator principal id criteria
     */
    String getInitiatorPrincipalId();

    /**
     * Returns the criteria for the principal name of a "viewer" of a document (someone who received an action request
     * related to the document) to search against when executing the document search.  Follows the rules for principal
     * name criteria (see class-level documentation).
     *
     * @return the viewer principal name criteria
     */
    String getViewerPrincipalName();

    /**
     * Returns the criteria for the principal id of a "viewer" of a document (someone who received an action request
     * related to the document) to search against when executing the document search.
     *
     * @return the viewer principal id criteria
     */
    String getViewerPrincipalId();

    /**
     * Returns the criteria for the id of a group who is a "viewer" of a document (a group who received an action request
     * related to the document) to search against when executing the document search.  Group id criteria follows rules
     * similar to principal name criteria:
     *
     * <ul>
     *   <li>only literal group ids that resolve to a valid group are allowed</li>
     *   <li>however, if the group ids don't resolve to valid groups, this simply means the search will return no results</li>
     *   <li>"!" is allowed before a group id</li>
     *   <li>when wanting to search on more than one viewer group id, use of "|" and "&&" is allowed, though they cannot be used together</li>
     * </ul>
     *
     * @return the viewer principal name criteria
     */
    String getGroupViewerId();
    
    /**
     * Returns the criteria for the name of a group who is a "viewer" of a document (a group who received an action request
     * related to the document) to search against when executing the document search.  Group name criteria follows rules
     * similar to principal name criteria:
     *
     * <ul>
     *   <li>only literal group names that resolve to a valid group are allowed</li>
     *   <li>however, if the group names don't resolve to valid groups, this simply means the search will return no results</li>
     *   <li>"!" is allowed before a group id</li>
     *   <li>when wanting to search on more than one viewer group name, use of "|" and "&&" is allowed, though they cannot be used together</li>
     * </ul>
     *
     * @return the viewer principal name criteria
     */
    String getGroupViewerName();

    /**
     * Returns the criteria for the principal name of an "approver" of a document (someone who took action against
     * the document) to search against when executing the document search.  Follows the rules for principal name
     * criteria (see class-level documentation).
     *
     * @return the viewer principal name criteria
     */
    String getApproverPrincipalName();

    /**
     * Returns the criteria for the principal id of an "approver" of a document (someone who took action against
     * the document) to search against when executing the document search.
     *
     * @return the viewer principal id criteria
     */
    String getApproverPrincipalId();

    /**
     * Returns the route node name criteria to search against when executing the document search.  By default this will
     * match only documents which are at the node with the given name, unless {@link #getRouteNodeLookupLogic()} returns
     * a non-null value that specifies different criteria for how the route node-based lookup should be performed.
     *
     * @return the route node name criteria
     */
    String getRouteNodeName();

    /**
     * Returns the logic that should be used when performing a document search against the route name.  This essentially
     * allows for the criteria to specify whether or not it should look at documents which are currently before, exactly
     * at, or after the specified route node.  This value only has an effect if the route node name is also defined
     * on this criteria.
     *
     * @return the route node lookup logic to use in conjunction with the route node name criteria
     */
    RouteNodeLookupLogic getRouteNodeLookupLogic();

    /**
     * Returns the document type name criteria to search against when executing the document search.  If the document
     * type name matches a single document type exactly, this might trigger document search customizations which are
     * tied to that document type (assuming the document type has such customizations configured).
     *
     * <p>In order for the map of document attribute values to be properly searchable, this document type name should
     * result to a valid document type.  This is because the document type itself defines information about custom
     * document attributes and the parameters around how searches against those attributes can be executed.</p>
     *
     * <p>Note that searches against a document type name should be document type hierarchy-aware.  Meaning that the
     * search should also return results for any documents that have document types that are children of the specified
     * document type name (assuming that the specified document type name is valid and not wildcarded at all).</p>
     *
     * @return the document type name criteria
     */
    String getDocumentTypeName();

    /**
     * Returns an optional list of additional document type name criteria against which to search.  The search should
     * effectively return all documents that have a document type name within the set of the main document type name on
     * the criteria as well as any additional document type names.
     *
     * <p>As with {@link #getDocumentTypeName()}, the additional document type name criteria is document type hierarchy
     * aware.</p>
     *
     * @return the list of additional document type names to use on the search criteria
     */
    List<String> getAdditionalDocumentTypeNames();

    /**
     * Returns the inclusive lower end of the date created criteria to search against when executing the document search.
     *
     * @return the date created "from" criteria
     */
    DateTime getDateCreatedFrom();

    /**
     * Returns the inclusive upper end of the date created criteria to search against when executing the document search.
     *
     * @return the date created "to" criteria
     */
    DateTime getDateCreatedTo();

    /**
     * Returns the inclusive lower end of the date last modified criteria to search against when executing the document search.
     *
     * @return the date last modified "from" criteria
     */
    DateTime getDateLastModifiedFrom();

    /**
     * Returns the inclusive upper end of the date last modified criteria to search against when executing the document search.
     *
     * @return the date last modified "to" criteria
     */
    DateTime getDateLastModifiedTo();

    /**
     * Returns the inclusive lower end of the date approved criteria to search against when executing the document search.
     *
     * @return the date approved "from" criteria
     */
    DateTime getDateApprovedFrom();

    /**
     * Returns the inclusive upper end of the date approved criteria to search against when executing the document search.
     *
     * @return the date approved "tp" criteria
     */
    DateTime getDateApprovedTo();

    /**
     * Returns the inclusive lower end of the date finalized criteria to search against when executing the document search.
     *
     * @return the date finalized "from" criteria
     */
    DateTime getDateFinalizedFrom();

    /**
     * Returns the inclusive upper end of the date finalized criteria to search against when executing the document search.
     *
     * @return the date finalized "to" criteria
     */
    DateTime getDateFinalizedTo();

    /**
     * Returns the inclusive lower end of the date of application document status change criteria to search against when executing the document search.
     *
     * @return the date application document status changed "from" criteria
     */
    DateTime getDateApplicationDocumentStatusChangedFrom();

    /**
     * Returns the inclusive upper end of the date of application document status change criteria to search against when executing the document search.
     *
     * @return the date application document status changed "to" criteria
     */
    DateTime getDateApplicationDocumentStatusChangedTo();

    /**
     * Returns a map of document attribute values to search against when executing the document search.  The key of the
     * map is the name of the document attribute, while the list of values contains values of those attributes to search
     * against.  These individual attribute values support the different search operations where appropriate.  The
     * resulting List of criteria values however should ultimately be "or"-ed together when executing the document
     * search.
     *
     * <p>In order for the document attribute values to be processed as part of the criteria during the search, the
     * {@link #getDocumentTypeName()} must return a valid name of a document type which is configured to understand the
     * attributes passed as part of the document attribute values map.</p>
     * @return
     */
    Map<String, List<String>> getDocumentAttributeValues();

    /**
     * Returns a map of custom options for document search when either executing the document search or returning results.
     * The key of the map is the name of the document attribute, while the list of values contains values of those
     * attributes to customize against.
     *
     * <p>In order for the search options to be processed as part of the criteria during the search, a custom document
     * search customizer must be used to fill and process these values.
     * @since 2.1.1
     * @return
     */
    Map<String, List<String>> getSearchOptions();

    /**
     * Return the name under which to save this criteria so that it can be recalled and used again in the future.  If no
     * save name is specified, then this criteria will not be saved for future use.
     * @return
     */
    String getSaveName();

    /**
     * Returns the 0-based index in the result set at which to start returning results from a document search which is
     * performed using this criteria.  If not specified, results from the search should be returned starting at the
     * beginning of the result set.  If this index is larger then the total number of results returned by the actual
     * search, then no values should be returned.
     *
     * @return the index in the result set at which to begin returning results
     */
	Integer getStartAtIndex();

    /**
     * Returns the requested maximum number of documents that should be returned from a document search performed using this
     * criteria.  If not specified, it is up to the document search implementation to decide how many results to return.
     * It is likely in such cases that the implementation will use a default result cap in order to prevent too many
     * documents from being returned.
     *
     * <p>It is important to note that this value is meant simply as a request to the document search for the number of
     * results to return.  The implementation may return fewer results then requested if it decides to impose it's own
     * internal cap on results.</p>
     *
     * @return the requested number of maximum document results that should be returned from the search
     */
	Integer getMaxResults();

    /**
     * Returns a string that indicates if a query was run in advanced mode.
     *
     * @return whether or not the search was run in advanced mode
     */
    String getIsAdvancedSearch();

    /**
     * Return the list of application document statuses to match when executing the document search.
     *
     * @since 2.1.2
     * @return the list of application document statuses to match
     */
    List<String> getApplicationDocumentStatuses();

    /**
     * Returns the principalId of the user performing the search
     *
     * @since 2.1.2
     * @return
     */
    String getDocSearchUserId();
}
