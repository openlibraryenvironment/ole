package org.kuali.ole.docstore.service;

import org.kuali.ole.docstore.discovery.service.AdminService;
import org.kuali.ole.docstore.discovery.service.AdminServiceImpl;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.indexer.solr.WorkBibDocumentIndexer;
import org.kuali.ole.solr.DummyAdminService;
import org.kuali.ole.solr.DummyIndexerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for getting services without depending on the implementation classes.
 * User: tirumalesh.b
 * Date: 22/12/11 Time: 8:05 PM
 */
public class ServiceLocator {
    private static Boolean OLE_DOCSTORE_USE_DISCOVERY = null;
    private static IndexerService indexerService = WorkBibDocumentIndexer.getInstance();
    private static IndexerService dummyIndexerService = DummyIndexerServiceImpl.getInstance();
    private static final Logger LOG = LoggerFactory.getLogger(ServiceLocator.class);
    private static AdminService adminService = AdminServiceImpl.getInstance();
    private static AdminService adminServiceDummy = new DummyAdminService();

    public static IndexerService getIndexerService() {
        initUseDiscovery();
        if (OLE_DOCSTORE_USE_DISCOVERY) {
            return indexerService;
        } else {
            return dummyIndexerService;
        }
    }

    private static void initUseDiscovery() {
        if (OLE_DOCSTORE_USE_DISCOVERY != null) {
            return;
        }

        String strUseDiscovery = System.getProperty("OLE_DOCSTORE_USE_DISCOVERY");
        if ((strUseDiscovery != null) && strUseDiscovery.equalsIgnoreCase("false")) {
            OLE_DOCSTORE_USE_DISCOVERY = Boolean.FALSE;
        } else {
            OLE_DOCSTORE_USE_DISCOVERY = Boolean.TRUE;
        }
        LOG.info("OLE_DOCSTORE_USE_DISCOVERY = " + OLE_DOCSTORE_USE_DISCOVERY);
    }

    public static AdminService getDiscoveryAdminService() {
        initUseDiscovery();
        if (OLE_DOCSTORE_USE_DISCOVERY) {
            return adminService;
        } else {
            return adminServiceDummy;
        }
    }
}
