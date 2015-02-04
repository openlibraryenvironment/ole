package org.kuali.ole.ingest.action;

import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.service.MockDiscoveryHelperService;
import org.kuali.ole.describe.service.MockDocstoreHelperService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Action;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/18/12
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockUpdateItemAction implements Action {

    private MockDocstoreHelperService docstoreHelperService;
    private MockDiscoveryHelperService discoveryHelperService;

    private static final Logger LOG = Logger.getLogger(MockUpdateItemAction.class);

    /**
     *     This method takes the initial request when updating the itemAction.
     *      @param executionEnvironment
     */
    @Override
    public void execute(ExecutionEnvironment executionEnvironment) {

        LOG.info(" Called UpdateItemAction ---------------> ");

        //TODO: Use strings from OleConstants.java; if not present add the string below to the class.
        /*DataCarrierService dataCarrierService = getDataCarrierService();
        BibliographicRecord bibliographicRecord = (BibliographicRecord) dataCarrierService.getData(OLEConstants.REQUEST_BIB_RECORD);
        List bibInfoList = (List) dataCarrierService.getData(OLEConstants.BIB_INFO_LIST_FROM_SOLR_RESPONSE);
        String bibUUID = null;
        for (Iterator iterator = bibInfoList.iterator(); iterator.hasNext(); ) {
            Map map = (Map) iterator.next();
            if (map.containsKey(OLEConstants.BIB_UNIQUE_ID)) {
                bibUUID = (String) map.get(OLEConstants.BIB_UNIQUE_ID);
                break;
            }
        }

        List responseFromSOLR =  getDiscoveryHelperService().getResponseFromSOLR("DocType=bibliographic and id", bibUUID);
        String instanceUUID = null;
        for (Iterator iterator = responseFromSOLR.iterator(); iterator.hasNext(); ) {
            Map map = (Map) iterator.next();
            if (map.containsKey(OLEConstants.BIB_INSTANCE_ID)) {
                List list =(List)map.get(OLEConstants.BIB_INSTANCE_ID);
                instanceUUID =list !=null & list.size()>0 ?(String) list.get(0):null;
                break;
            }
        }

        List responseFromSOLR1 =  getDiscoveryHelperService().getResponseFromSOLR("DocType=instance and id", instanceUUID);
        String itemUUID = null;
        for (Iterator iterator = responseFromSOLR1.iterator(); iterator.hasNext(); ) {
            Map map = (Map) iterator.next();
            if (map.containsKey(OLEConstants.BIB_ITEM_ID)) {
                List list = (List)map.get(OLEConstants.BIB_ITEM_ID);
                itemUUID = list !=null & list.size()>0 ?(String) list.get(0):null;
                break;
            }
        }

        getDocstoreHelperService().updateItem(bibliographicRecord, itemUUID);
        executionEnvironment.getEngineResults().setAttribute(OLEConstants.UPDATE_ITEM_FLAG, true);*/
    }

    /**
     *  Gets the dataCarrierService attribute.
     * @return  Returns dataCarrierService.
     */
    protected DataCarrierService getDataCarrierService() {
        return GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
    }

    /**
     *   This method simulate the executionEnvironment.
     * @param executionEnvironment
     */
    @Override
    public void executeSimulation(ExecutionEnvironment executionEnvironment) {
        execute(executionEnvironment);
    }

    public MockDocstoreHelperService getDocstoreHelperService() {
        return docstoreHelperService;
    }

    public void setDocstoreHelperService(MockDocstoreHelperService docstoreHelperService) {
        this.docstoreHelperService = docstoreHelperService;
    }

    public MockDiscoveryHelperService getDiscoveryHelperService() {
        return discoveryHelperService;
    }

    public void setDiscoveryHelperService(MockDiscoveryHelperService discoveryHelperService) {
        this.discoveryHelperService = discoveryHelperService;
    }
}
