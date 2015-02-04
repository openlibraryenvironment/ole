package org.kuali.ole.describe.service;

import org.kuali.incubator.SolrRequestReponseHandler;

import java.util.List;

/**
 * DiscoveryHelperService is the service class to access Discovery services
 */
public class DiscoveryHelperService {
    private final String queryString = "DocType:bibliographic AND instanceIdentifier:";
    private final String bibQueryString = "DocType:bibliographic AND uniqueId:";
    SolrRequestReponseHandler solrRequestReponseHandler;

    /**
     * Returns the Response List based on the queryField and Value from the SolrRequestReponseHandler class.
     *
     * @param queryField
     * @param value
     * @return Returns the Response.
     */
    public List getResponseFromSOLR(String queryField, String value) {
        String queryString = queryField + ":" + value;
        return getSolrRequestReponseHandler().retriveResults(queryString);
    }

    /**
     * Returns the List of BibInformation records based on the instanceUUID.
     *
     * @param instanceUUID
     * @return Returns BibInformation
     */
    public List getBibInformationFromInsatnceId(String instanceUUID) {
        return getSolrRequestReponseHandler().retriveResults(queryString + instanceUUID);
    }

    /**
     * Returns the List of BibInformation records based on the BibUUID.
     *
     * @param bibUUID
     * @return Returns BibInformation
     */
    public List getBibInformationFromBibId(String bibUUID) {
        return getSolrRequestReponseHandler().retriveResults(bibQueryString + bibUUID);
    }


    /**
     * Returns the new instance of SolrRequestReponseHandler provided there should not be any existing instance,Otherwise returns existing instance.
     *
     * @return Returns the SolrRequestReponseHandler
     */
    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if (null == solrRequestReponseHandler) {
            solrRequestReponseHandler = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }

    /**
     * Sets the SolrRequestReponseHandler attribute value.
     *
     * @param solrRequestReponseHandler
     */
    public void setSolrRequestReponseHandler(SolrRequestReponseHandler solrRequestReponseHandler) {
        this.solrRequestReponseHandler = solrRequestReponseHandler;
    }
}
