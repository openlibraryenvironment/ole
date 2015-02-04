/*
 * Copyright 2012 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.document.service;

import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.select.businessobject.OleAcquisitionSearchResult;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;

import java.util.List;
import java.util.Map;

public interface OleAcquisitionSearchService {

    public List<String> getTitleIds(List<SearchResult> searchResults);

    public List<OleAcquisitionSearchResult> performDocumentSearch(DocumentSearchCriteria docSearchCriteria, Map<String, List<String>> fixedParameters);

}
