package org.kuali.ole;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.solr.core.SolrTemplate;

import java.io.File;

@SpringBootApplication
public class OleSolrClientApplication extends SpringBootServletInitializer {

	@Value("${solr.server.protocol}")
	String solrServerProtocol;

	@Value("${solr.url}")
	String solrUrl;

	@Value("${solr.parent.core}")
	String solrParentCore;

	public static void main(String[] args) {
		SpringApplication.run(OleSolrClientApplication.class, args);
	}

	@Bean
	public SolrClient solrAdminClient() {
		return new HttpSolrClient(solrServerProtocol + solrUrl);
	}

	@Bean
	public SolrClient solrClient() {
		String baseURLForParentCore = solrServerProtocol + solrUrl + File.separator + solrParentCore;
		return new HttpSolrClient(baseURLForParentCore);
	}

	@Bean
	public SolrTemplate solrTemplate(SolrClient solrClient) throws Exception {
		SolrTemplate solrTemplate = new SolrTemplate(solrClient);
		return solrTemplate;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(OleSolrClientApplication.class);
	}
}
