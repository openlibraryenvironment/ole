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
package org.kuali.rice.kew.routeheader.dao;

import java.util.Collection;
import java.util.Set;

import org.kuali.rice.kew.docsearch.SearchableAttributeValue;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValueContent;



/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentRouteHeaderDAO {

  public void saveRouteHeader(DocumentRouteHeaderValue routeHeader);
  /**
   * "Locks" the route header at the datasource level.
   */
  public void lockRouteHeader(String documentId, boolean wait);
  public DocumentRouteHeaderValue findRouteHeader(String documentId);
  public DocumentRouteHeaderValue findRouteHeader(String documentId, boolean clearCache);
  public Collection<DocumentRouteHeaderValue> findRouteHeaders(Collection<String> documentIds);
  public Collection<DocumentRouteHeaderValue> findRouteHeaders(Collection<String> documentIds, boolean clearCache);
  public void deleteRouteHeader(DocumentRouteHeaderValue routeHeader);
  public String getNextDocumentId();
  public Collection<String> findPendingByResponsibilityIds(Set<String> responsibilityIds);
  public void clearRouteHeaderSearchValues(String documentId);
  public Collection<SearchableAttributeValue> findSearchableAttributeValues(String documentId);
  public String getApplicationIdByDocumentId(String documentId);
  public DocumentRouteHeaderValueContent getContent(String documentId);
  public boolean hasSearchableAttributeValue(String documentId, String searchableAttributeKey, String searchableAttributeValue);
  public String getDocumentStatus(String documentId);
  public void save(SearchableAttributeValue searchableAttribute);
  public String getAppDocId(String documentId);
  public String getAppDocStatus(String documentId);

  public Collection findByDocTypeAndAppId(String documentTypeName, String appId);

}
