/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.docstore.service;


import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for Indexing Documents.
 *
 * @author Rajesh Chowdary K
 * @created Feb 16, 2012
 */
public class DocumentIndexer {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentIndexer.class);

    /**
     * Method to index Bib And Linked Instance Documents for a given list.
     *
     * @param reqDocs
     * @throws Exception
     */
    public void indexDocuments(List<RequestDocument> reqDocs) throws Exception {
        String result = ServiceLocator.getIndexerService().indexDocuments(reqDocs);
        if (!result.startsWith("success")) {
            throw new Exception(result);
        }
    }

    /**
     * Method to index Documents For Bulk Process.
     *
     * @param reqDocs
     * @throws Exception
     */
    public void indexDocumentsForBulk(List<RequestDocument> reqDocs, boolean isCommit) throws Exception {
        String result = ServiceLocator.getIndexerService().bulkIndexDocuments(reqDocs, isCommit);
        if (!result.startsWith("success")) {
            throw new Exception(result);
        }
    }

    /**
     * Method to index Bib And Linked Instance Documents
     *
     * @param reqDoc
     * @throws Exception
     */
    public void indexDocument(RequestDocument reqDoc) throws Exception {
        String result = ServiceLocator.getIndexerService().indexDocument(reqDoc);
        if (result.startsWith(IndexerService.FAILURE)) {
            throw new Exception("Indexing failed. Message=" + result);
        }
    }

    /**
     * Method to roll back Indexed Data for a given Request Docuemnts.
     *
     * @param requestDocuments
     */
    public void rollbackIndexedData(List<RequestDocument> requestDocuments) {
        try {
            Map<String, List<String>> uuids = new HashMap<String, List<String>>();
            for (RequestDocument document : requestDocuments) {
                for (RequestDocument linkedDoc : document.getLinkedRequestDocuments()) {
                    if (uuids.get(linkedDoc.getCategory()) == null) {
                        uuids.put(linkedDoc.getCategory(), new ArrayList<String>());
                    }
                    uuids.get(linkedDoc.getCategory()).add(linkedDoc.getUuid());
                }
                if (uuids.get(document.getCategory()) == null) {
                    uuids.put(document.getCategory(), new ArrayList<String>());
                }
                uuids.get(document.getCategory()).add(document.getUuid());
            }
            for (String category : uuids.keySet()) {
                ServiceLocator.getIndexerService().deleteDocuments(category, uuids.get(category));
            }
        } catch (Exception e) {
            LOG.info(e.getMessage(),e);
        }
    }

    /**
     * Method to optimizeSolr
     *
     * @throws Exception
     */
    public void optimizeSolr() throws Exception {
        ServiceLocator.getDiscoveryAdminService().optimize();
    }

    /**
     * Method to optimizeSolr
     *
     * @throws Exception
     */
    public void optimizeSolr(Boolean waitFlush, Boolean waitSearcher) throws Exception {
        ServiceLocator.getDiscoveryAdminService().optimize(waitFlush, waitSearcher);
    }

}
