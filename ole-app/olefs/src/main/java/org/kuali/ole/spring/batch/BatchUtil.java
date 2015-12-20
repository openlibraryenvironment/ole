package org.kuali.ole.spring.batch;

import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.oleng.util.OleNgUtil;
import org.kuali.ole.utility.MarcRecordUtil;
import org.kuali.ole.utility.OleDsNgRestClient;

/**
 * Created by SheikS on 12/8/2015.
 */
public class BatchUtil extends OleNgUtil{
    private OleDsNgRestClient oleDsNgRestClient;
    SolrRequestReponseHandler solrRequestReponseHandler;
    private MarcRecordUtil marcRecordUtil;

    public OleDsNgRestClient getOleDsNgRestClient() {
        if(null == oleDsNgRestClient) {
            oleDsNgRestClient = new OleDsNgRestClient();
        }
        return oleDsNgRestClient;
    }

    public void setOleDsNgRestClient(OleDsNgRestClient oleDsNgRestClient) {
        this.oleDsNgRestClient = oleDsNgRestClient;
    }

    public SolrRequestReponseHandler getSolrRequestReponseHandler() {
        if(null == solrRequestReponseHandler) {
            solrRequestReponseHandler  = new SolrRequestReponseHandler();
        }
        return solrRequestReponseHandler;
    }

    public void setSolrRequestReponseHandler(SolrRequestReponseHandler solrRequestReponseHandler) {
        this.solrRequestReponseHandler = solrRequestReponseHandler;
    }

    public MarcRecordUtil getMarcRecordUtil() {
        if(null == marcRecordUtil) {
            marcRecordUtil = new MarcRecordUtil();
        }
        return marcRecordUtil;
    }

    public void setMarcRecordUtil(MarcRecordUtil marcRecordUtil) {
        this.marcRecordUtil = marcRecordUtil;
    }
}
