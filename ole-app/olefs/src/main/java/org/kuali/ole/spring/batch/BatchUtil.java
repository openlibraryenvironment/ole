package org.kuali.ole.spring.batch;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.incubator.SolrRequestReponseHandler;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.CallNumberTypeRecord;
import org.kuali.ole.select.bo.OleCallNumber;
import org.kuali.ole.service.OLEEResourceSearchService;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.kuali.ole.utility.OleHttpRestClient;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;

/**
 * Created by SheikS on 12/8/2015.
 */
public class BatchUtil {
    private OleDsNgRestClient oleDsNgRestClient;
    SolrRequestReponseHandler solrRequestReponseHandler;
    private BusinessObjectService businessObjectService;

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

    public BusinessObjectService getBusinessObjectService() {
        if(null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }
}
