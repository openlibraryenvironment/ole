package org.kuali.ole.repo.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.kuali.ole.model.solr.Bib;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.convert.MappingSolrConverter;
import org.springframework.data.solr.core.mapping.SimpleSolrMappingContext;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import java.io.File;

/**
 * Created by sheiks on 21/11/16.
 */
public class BibCrudRepositoryMultiCoreSupport extends SimpleSolrRepository<Bib, String> {

    SolrTemplate solrTemplate;

    public BibCrudRepositoryMultiCoreSupport(String coreName, String solrUrl) {

        solrTemplate = new SolrTemplate( new HttpSolrClient(solrUrl+ File.separator+coreName));
        solrTemplate.setSolrConverter(new MappingSolrConverter(new SimpleSolrMappingContext()) {
        });
        solrTemplate.setSolrCore(coreName);
        setSolrOperations(solrTemplate);
    }

    public SolrTemplate getSolrTemplate() {
        return solrTemplate;
    }

    public void setSolrTemplate(SolrTemplate solrTemplate) {
        this.solrTemplate = solrTemplate;
    }

    public String getBibIdOrderByBidId(String orderBy){
        SolrQuery solrQuery = new SolrQuery();
        String localId ="";
        String query = "DocType:bibliographic";
        if(orderBy.equalsIgnoreCase("asc")) {
            solrQuery.addSort("LocalId_search", SolrQuery.ORDER.asc);
        } else{
            solrQuery.addSort("LocalId_search", SolrQuery.ORDER.desc);
        }
        solrQuery.setQuery(query);
        solrQuery.setRows(1);
        solrQuery.setFields("LocalId_search");
        try {
            QueryResponse queryResonse= getSolrTemplate().getSolrClient().query(solrQuery);
            for(SolrDocument solrDocument:queryResonse.getResults()){
                localId = ""+solrDocument.get("LocalId_search");
            }
        } catch (Exception e){

        }
        return localId;
    }
}
