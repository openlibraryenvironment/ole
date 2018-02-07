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
package org.kuali.rice.kew.impl.document.attribute;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContent;
import org.kuali.rice.kew.api.document.DocumentWithContent;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeDateTime;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeDecimal;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeIndexingQueue;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeInteger;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeString;
import org.kuali.rice.kew.docsearch.SearchableAttributeDateTimeValue;
import org.kuali.rice.kew.docsearch.SearchableAttributeFloatValue;
import org.kuali.rice.kew.docsearch.SearchableAttributeLongValue;
import org.kuali.rice.kew.docsearch.SearchableAttributeStringValue;
import org.kuali.rice.kew.docsearch.SearchableAttributeValue;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.framework.document.attribute.SearchableAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Reference implementation of the DocumentAttributeIndexingQueue.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentAttributeIndexingQueueImpl implements DocumentAttributeIndexingQueue {

	private static Logger LOG = Logger.getLogger(DocumentAttributeIndexingQueueImpl.class);

    @Override
    public void indexDocument(String documentId) {
        if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was null or blank");
        }
        MDC.put("docId", documentId);
        try {
            long t1 = System.currentTimeMillis();
            LOG.info("Indexing document attributes for document " + documentId);
            Document document = getWorkflowDocumentService().getDocument(documentId);
            if (document == null) {
                throw new RiceIllegalArgumentException("Failed to locate document with the given id: " + documentId);
            }
            DocumentContent documentContent =
                    KewApiServiceLocator.getWorkflowDocumentService().getDocumentContent(documentId);
            List<SearchableAttributeValue> attributes = buildSearchableAttributeValues(document, documentContent);
            KEWServiceLocator.getRouteHeaderService().updateRouteHeaderSearchValues(documentId, attributes);
            long t2 = System.currentTimeMillis();
            LOG.info("...finished indexing document " + documentId + " for document search, total time = " + (t2 - t1) +
                    " ms.");
        } finally {
            MDC.remove("docId");
        }
    }

    /**
     * Determines the {@link DocumentAttribute}s for the given document and returns a List of SearchableAttributeValue
     * which will be saved.
     */
	private List<SearchableAttributeValue> buildSearchableAttributeValues(Document document, DocumentContent documentContent) {
		List<SearchableAttributeValue> searchableAttributeValues = new ArrayList<SearchableAttributeValue>();
        DocumentType documentTypeBo = KEWServiceLocator.getDocumentTypeService().findByName(document.getDocumentTypeName());
		for (DocumentType.ExtensionHolder<SearchableAttribute> searchableAttributeHolder : documentTypeBo.loadSearchableAttributes()) {
            DocumentWithContent documentWithContent = DocumentWithContent.create(document, documentContent);
            SearchableAttribute searchableAttribute = searchableAttributeHolder.getExtension();
            List<DocumentAttribute> documentAttributes = searchableAttribute.extractDocumentAttributes(
                    searchableAttributeHolder.getExtensionDefinition(), documentWithContent);
			if (documentAttributes != null) {
                for (DocumentAttribute documentAttribute : documentAttributes) {
                    if (documentAttribute == null) {
                        LOG.warn("Encountered a 'null' DocumentAttribute from searchable attribute: " + searchableAttribute);
                        continue;
                    }
                    SearchableAttributeValue searchableAttributeValue = null;
                    if (documentAttribute instanceof DocumentAttributeString) {
                        searchableAttributeValue = new SearchableAttributeStringValue();
                        ((SearchableAttributeStringValue)searchableAttributeValue).setSearchableAttributeValue(((DocumentAttributeString)documentAttribute).getValue());
                    } else if (documentAttribute instanceof DocumentAttributeDateTime) {
                        searchableAttributeValue = new SearchableAttributeDateTimeValue();
                        DateTime dateTimeValue = ((DocumentAttributeDateTime)documentAttribute).getValue();
                        Timestamp timestamp = (dateTimeValue == null ? null : new Timestamp(dateTimeValue.getMillis()));
                        ((SearchableAttributeDateTimeValue)searchableAttributeValue).setSearchableAttributeValue(timestamp);
                    } else if (documentAttribute instanceof DocumentAttributeInteger) {
                        searchableAttributeValue = new SearchableAttributeLongValue();
                        BigInteger bigIntegerValue = ((DocumentAttributeInteger)documentAttribute).getValue();
                        Long longValue = (bigIntegerValue == null ? null : bigIntegerValue.longValue());
                        ((SearchableAttributeLongValue)searchableAttributeValue).setSearchableAttributeValue(longValue);
                    } else if (documentAttribute instanceof DocumentAttributeDecimal) {
                        searchableAttributeValue = new SearchableAttributeFloatValue();
                        ((SearchableAttributeFloatValue)searchableAttributeValue).setSearchableAttributeValue(((DocumentAttributeDecimal)documentAttribute).getValue());
                    } else {
                        throw new WorkflowRuntimeException("Encountered an invalid instance of DocumentAttribute, was: " + documentAttribute.getClass());
                    }
                    searchableAttributeValue.setSearchableAttributeKey(documentAttribute.getName());
                    searchableAttributeValue.setDocumentId(document.getDocumentId());
                    searchableAttributeValue.setRouteHeader(null); // let the documentId we set represent this reference
                    searchableAttributeValues.add(searchableAttributeValue);
                }
			}
		}
		return searchableAttributeValues;
	}

    protected WorkflowDocumentService getWorkflowDocumentService() {
        return KewApiServiceLocator.getWorkflowDocumentService();
    }
}
