package org.kuali.ole.repo.solr;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
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

    public BibCrudRepositoryMultiCoreSupport(String coreName, String solrUrl) {

        SolrTemplate solrTemplate = new SolrTemplate( new HttpSolrClient(solrUrl+ File.separator+coreName));
        solrTemplate.setSolrConverter(new MappingSolrConverter(new SimpleSolrMappingContext()) {
        });
        solrTemplate.setSolrCore(coreName);
        setSolrOperations(solrTemplate);
    }
}
