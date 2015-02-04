package org.kuali.ole.docstore.engine.service.index.solr;

import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.docstore.common.document.BibTrees;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 4:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentIndexer {

    public void create(Object object);

    public void update(Object object);

    public void delete(String id);

    public void transfer(List<String> sourceIds, String destinationId);

    public void bind(String holdingsId, List<String> bibIds) throws SolrServerException, IOException;

    public void bindAnalytics(String holdingsId, List<String> bibIds, String createOrBreak) throws SolrServerException, IOException;

    public void createTree(Object object);

    public void createTrees(Object object);

    public void processBibTrees(BibTrees bibTrees);

}