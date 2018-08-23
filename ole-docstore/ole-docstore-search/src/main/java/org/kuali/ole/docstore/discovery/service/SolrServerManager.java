package org.kuali.ole.docstore.discovery.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.core.CoreContainer;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * This class handles the creation of solr server object, used for connecting to solr server (core).
 * User: tirumalesh.b
 * Date: 23/12/11 Time: 12:32 PM
 */
public class SolrServerManager {

    private static final Logger LOG = LoggerFactory.getLogger(SolrServerManager.class);

    public static final String SOLR_HOME = "solr.solr.home";
    public static final String SOLR_CORE_MAIN = "bib";
    public static final String SOLR_SERVER_EMBEDDED = "solr.server.embedded";
    public static final String SOLR_SERVER_STREAMING = "solr.server.streaming";

    private static SolrServerManager solrServerMgr = null;
    private static String docSearchUrl = null;
    public static Map<String, SolrServer> serverMap = null;

    public static SolrServerManager getInstance() {
        if (null == solrServerMgr) {
            solrServerMgr = new SolrServerManager();
        }
        return solrServerMgr;
    }

    private SolrServerManager() {
        init();
    }

    protected void init() {
        LOG.debug("SolrServerManager.init()");
        if(ConfigContext.getCurrentContextConfig()!=null){
            docSearchUrl = ConfigContext.getCurrentContextConfig().getProperty("docSearchURL");
        }
        if ((null != docSearchUrl) && !docSearchUrl.endsWith("/")) {
            docSearchUrl = docSearchUrl + "/";
        }
        LOG.info("Actual Document Search URL being used docSearchURL=" + docSearchUrl);
        serverMap = new HashMap<String, SolrServer>();
    }

    /**
     * Simplest method to get a solr server for the default core.
     *
     * @return Returns solr server object.
     * @throws Exception Throws an exception on error.
     */
    public SolrServer getSolrServer() throws SolrServerException {
        return getSolrServer(SOLR_CORE_MAIN);
    }

    /**
     * Get a solr server for the given core.
     *
     * @param solrCore Solr core for which server is required.
     * @return Returns solr server object.
     * @throws Exception Throws an exception on error.
     */
    public SolrServer getSolrServer(String solrCore) throws SolrServerException {
        boolean isStreaming = false;
        boolean isEmbedded = false;
        String embedPropValue = System.getProperty(SOLR_SERVER_EMBEDDED);
        if (StringUtils.isNotEmpty(embedPropValue)) {
            isEmbedded = Boolean.parseBoolean(embedPropValue);
        }
        String streamingPropValue = System.getProperty(SOLR_SERVER_STREAMING);
        if (StringUtils.isNotEmpty(streamingPropValue)) {
            isStreaming = Boolean.parseBoolean(streamingPropValue);
        }
        return getSolrServer(solrCore, isStreaming, isEmbedded);
    }

    /**
     * Get a solr server for the given core, with the specified options for streaming or embedded.
     * The streaming and embedded options are considered only for the first time, for the sake of simplicity.
     * The server objects are saved in a map and reused.
     *
     * @param solrCore    Solr core for which server is required.
     * @param isStreaming Indicates whether a streaming server is needed.
     * @param isEmbedded  Indicates whether an embedded server is needed.
     * @return Returns solr server object.
     * @throws Exception Throws an exception on error.
     */
    public SolrServer getSolrServer(String solrCore, boolean isStreaming, boolean isEmbedded)
            throws SolrServerException {
        SolrServer solr = null;
        while(docSearchUrl.endsWith("//")) {
            docSearchUrl  = docSearchUrl.substring(0,docSearchUrl.lastIndexOf("/"));
        }
        if(solrCore.contains("/")) {
            solrCore = solrCore.replaceAll("/","");
        }
        try {
            if (null == serverMap) {
                serverMap = new HashMap<String, SolrServer>();
            }
            solr = serverMap.get(solrCore);
            if (null != solr) {
                return solr;
            }
            if (isEmbedded) {
                solr = getEmbeddedSolrServer(solrCore);
            } else if (isStreaming) {
                solr = new ConcurrentUpdateSolrServer(docSearchUrl + solrCore, 100, 5);
            } else {
                solr = getCommonsHttpSolrServer(docSearchUrl + solrCore);
            }
            serverMap.put(solrCore, solr);
        } catch (SolrServerException sse) {
            throw sse;
        } catch (Exception e) {
            throw new SolrServerException(e);
        }
        return solr;
    }

    public String getSolrCoreURL() {
        return docSearchUrl + SOLR_CORE_MAIN;
    }

    protected static HttpSolrServer getCommonsHttpSolrServer(String solrUrl) throws MalformedURLException {
        HttpSolrServer server = new HttpSolrServer(solrUrl);
        server.setConnectionTimeout(100);
        server.setDefaultMaxConnectionsPerHost(100);
        server.setMaxTotalConnections(100);
        server.setFollowRedirects(false);
        server.setAllowCompression(true);
        server.setMaxRetries(1);
        server.setParser(new XMLResponseParser());
        return server;
    }

    protected SolrServer getEmbeddedSolrServer(String solrCore) throws IOException, ParserConfigurationException, SAXException, SolrServerException {
        String solrHomeDir = System.getProperty(SOLR_HOME);
        File home = new File(solrHomeDir);
        File f = new File(home, "solr.xml");
        CoreContainer container = new CoreContainer();
        container.load();
        return new EmbeddedSolrServer(container, solrCore);
    }

}
