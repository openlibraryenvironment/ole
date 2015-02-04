package org.kuali.ole.docstore.discovery.service;

import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.indexer.solr.WorkBibDocumentIndexer;

/**
 * Class for getting services without depending on the implementation classes.
 * User: tirumalesh.b
 * Date: 22/12/11 Time: 8:05 PM
 */
public class ServiceLocator {
    private static IndexerService indexerService = null;
    private static DiscoveryService discoveryService = null;
    private static QueryService queryService = null;

    public static IndexerService getIndexerService() {
        if (null == indexerService) {
            indexerService = WorkBibDocumentIndexer.getInstance();
        }
        return indexerService;
    }

    public static DiscoveryService getDiscoveryService() {
        if (null == discoveryService) {
            discoveryService = DiscoveryServiceImpl.getInstance();
        }
        return discoveryService;
    }

    public static QueryService getQueryService() {
        if (null == queryService) {
            queryService = QueryServiceImpl.getInstance();
        }
        return queryService;
    }

}
