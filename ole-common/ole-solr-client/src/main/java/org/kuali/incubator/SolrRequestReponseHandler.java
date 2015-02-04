package org.kuali.incubator;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 10/21/11
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class SolrRequestReponseHandler {

    public List retriveResults(String queryString) {
        HttpSolrServer server = null;
        ArrayList<HashMap<String, Object>> hitsOnPage = new ArrayList<HashMap<String, Object>>();

        try {
            String serverUrl = ConfigContext.getCurrentContextConfig().getProperty("discovery.url");
            server = new HttpSolrServer(serverUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SolrQuery query = new SolrQuery();
        query.setQuery(queryString);
        query.setIncludeScore(true);

        try {
            QueryResponse qr = server.query(query);

            SolrDocumentList sdl = qr.getResults();


            for (SolrDocument d : sdl) {
                HashMap<String, Object> values = new HashMap<String, Object>();

                for (Iterator<Map.Entry<String, Object>> i = d.iterator(); i.hasNext(); ) {
                    Map.Entry<String, Object> e2 = i.next();

                    values.put(e2.getKey(), e2.getValue());
                }

                hitsOnPage.add(values);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return hitsOnPage;
    }
}
