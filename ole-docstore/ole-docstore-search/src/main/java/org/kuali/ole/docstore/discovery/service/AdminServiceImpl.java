package org.kuali.ole.docstore.discovery.service;

import org.apache.solr.client.solrj.SolrServer;

/**
 * User: tirumalesh.b
 * Date: 23/12/11 Time: 12:09 PM
 */
public class AdminServiceImpl implements AdminService {
    private static AdminServiceImpl adminService = null;

    private AdminServiceImpl() {
    }

    public static AdminService getInstance() {
        if (null == adminService) {
            adminService = new AdminServiceImpl();
        }
        return adminService;
    }

    @Override
    public void optimize(String indexName) throws Exception {
        SolrServer solr = SolrServerManager.getInstance().getSolrServer(indexName, false, false);
        solr.optimize();
    }

    @Override
    public void optimize() throws Exception {
        SolrServer solr = SolrServerManager.getInstance().getSolrServer();
        solr.optimize();
        // TODO: Handle other cores also, if any.
    }

    @Override
    public void optimize(Boolean waitFlush, Boolean waitSearcher) throws Exception {
        SolrServer solr = SolrServerManager.getInstance().getSolrServer();
        solr.optimize(false, false);
    }
}
