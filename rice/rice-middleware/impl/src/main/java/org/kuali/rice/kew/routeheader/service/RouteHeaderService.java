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
package org.kuali.rice.kew.routeheader.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.docsearch.SearchableAttributeValue;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValueContent;


/**
 * A service providing data access for documents (a.k.a route headers).
 *
 * @see DocumentRouteHeaderValue
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RouteHeaderService {

    public DocumentRouteHeaderValue getRouteHeader(String documentId);
    public DocumentRouteHeaderValue getRouteHeader(String documentId, boolean clearCache);
    public Collection<DocumentRouteHeaderValue> getRouteHeaders (Collection<String> documentIds);
    public Collection<DocumentRouteHeaderValue> getRouteHeaders (Collection<String> documentIds, boolean clearCache);
    public Map<String,DocumentRouteHeaderValue> getRouteHeadersForActionItems(Collection<ActionItem> actionItems);
    public void lockRouteHeader(String documentId, boolean wait);
    public void saveRouteHeader(DocumentRouteHeaderValue routeHeader);
    public void deleteRouteHeader(DocumentRouteHeaderValue routeHeader);
    public String getNextDocumentId();
    public void validateRouteHeader(DocumentRouteHeaderValue routeHeader);
    public Collection findPendingByResponsibilityIds(Set responsibilityIds);
    public Collection findByDocTypeAndAppId(String documentTypeName, String appId);
    
    /**
     * Removes all SearchableAttributeValues associated with the RouteHeader.
     * @param routeHeader
     */
    public void clearRouteHeaderSearchValues(String documentId);

    /**
     * Updates the searchable attribute values for the document with the given id to the given values.
     * This method will clear existing search attribute values and replace with the ones given.
     */
    public void updateRouteHeaderSearchValues(String documentId, List<SearchableAttributeValue> searchAttributes);
    
    /**
     * Returns the application id of the {@link DocumentType} for the Document with the given ID.
     */
    public String getApplicationIdByDocumentId(String documentId);

    public DocumentRouteHeaderValueContent getContent(String documentId);

    public boolean hasSearchableAttributeValue(String documentId, String searchableAttributeKey, String searchableAttributeValue);

    public String getDocumentStatus(String documentId);

    public String getAppDocId(String documentId);

    /**
     *
     * This method Returns the application document status for the given document id
     *
     * @param documentId
     * @return String
     */
    public String getAppDocStatus(String documentId);
    
    /**
     *
     * This method is a more direct way to get the searchable attribute values
     *
     * @param documentId
     * @param key
     * @return
     */
    public List<String> getSearchableAttributeStringValuesByKey(String documentId, String key);
    /**
     *
     * This method is a more direct way to get the searchable attribute values
     *
     * @param documentId
     * @param key
     * @return
     */
    public List<Timestamp> getSearchableAttributeDateTimeValuesByKey(String documentId, String key);
    /**
     *
     * This method is a more direct way to get the searchable attribute values
     *
     * @param documentId
     * @param key
     * @return
     */
    public List<BigDecimal> getSearchableAttributeFloatValuesByKey(String documentId, String key);
    /**
     *
     * This method is a more direct way to get the searchable attribute values
     *
     * @param documentId
     * @param key
     * @return
     */
    public List<Long> getSearchableAttributeLongValuesByKey(String documentId, String key);

}
