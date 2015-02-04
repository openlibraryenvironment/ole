package org.kuali.ole.ingest.action;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OleItemRecordHandler;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.pojo.ProfileAttribute;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Action;

import java.util.*;

/**
 * UpdateItemAction is a action class for Update Item Action
 */
public class UpdateItemAction implements Action {

    private DocstoreClientLocator docstoreClientLocator;

    /**
     *     This method takes the initial request when updating the itemAction.
     *      @param executionEnvironment
     */
    @Override
    public void execute(ExecutionEnvironment executionEnvironment) {
        //TODO: Use strings from OleConstants.java; if not present add the string below to the class.
        DataCarrierService dataCarrierService = getDataCarrierService();
        BibMarcRecord bibMarcRecord = (BibMarcRecord) dataCarrierService.getData(OLEConstants.REQUEST_BIB_RECORD);
        List bibInfoList = (List) dataCarrierService.getData(OLEConstants.BIB_INFO_LIST_FROM_SOLR_RESPONSE);
        String bibUUID = null;
        for (Iterator iterator = bibInfoList.iterator(); iterator.hasNext(); ) {
            Map map = (Map) iterator.next();
            if (map.containsKey(OLEConstants.BIB_UNIQUE_ID)) {
                bibUUID = (String) map.get(OLEConstants.BIB_UNIQUE_ID);
                break;
            }
        }
        /*List responseFromSOLR =  getDiscoveryHelperService().getResponseFromSOLR("DocType=bibliographic and id", bibUUID);
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
        }*/

        try {
            BibTree bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibUUID);
            List<HoldingsTree> holdingsTree=bibTree.getHoldingsTrees();
            Holdings holdings = holdingsTree.get(0).getHoldings();
            String  holdingsId = holdings.getId();
            List<Item> items=holdingsTree.get(0).getItems();
            String  itemId=items.get(0).getId();
            BibMarcRecordProcessor oleItemRecordHandler = new BibMarcRecordProcessor();
            org.kuali.ole.docstore.common.document.content.instance.Item oleItem = getOleItem(bibMarcRecord);
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            String itemXML = itemOlemlRecordProcessor.toXML(oleItem);
            // items.get(0).setId(itemId);
            items.get(0).setContent(itemXML);
            getDocstoreClientLocator().getDocstoreClient().updateItem(items.get(0));
        }
        catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //  getDocstoreHelperService().updateItem(bibliographicRecord, itemUUID);
        executionEnvironment.getEngineResults().setAttribute(OLEConstants.UPDATE_ITEM_FLAG, true);
    }

    public org.kuali.ole.docstore.common.document.content.instance.Item getOleItem(BibMarcRecord bibMarcRecord) {
        List<ProfileAttribute>   profileAttributes=new ArrayList<>();
        for (DataField dataField : bibMarcRecord.getDataFields()) {
            if (dataField.getTag().equalsIgnoreCase(OLEConstants.DATA_FIELD_985)) {
                List<SubField> subFieldList = dataField.getSubFields();
                SubField subField = new SubField();
                subField.setCode(OLEConstants.SUB_FIELD_A);
                subField.setValue(OLEConstants.DEFAULT_LOCATION_LEVEL_INSTITUTION);
                subFieldList.add(subField);
                dataField.setSubFields(subFieldList);
            }
        }
        return new OleItemRecordHandler().getOleItem(bibMarcRecord, profileAttributes);
    }


    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return  SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public void setDocstoreClientLocator(DocstoreClientLocator docstoreClientLocator) {
        this.docstoreClientLocator = docstoreClientLocator;
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



}
