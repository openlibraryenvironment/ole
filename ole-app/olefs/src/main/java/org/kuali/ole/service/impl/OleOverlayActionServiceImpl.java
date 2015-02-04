package org.kuali.ole.service.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.BibliographicRecordHandler;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.factory.OverlayFileReaderFactory;
import org.kuali.ole.factory.OverlayOutputServiceFactory;
import org.kuali.ole.ingest.pojo.*;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.edi.LineItemOrder;
import org.kuali.ole.service.OleOverlayActionService;
import org.kuali.ole.service.OverlayFileReaderService;
import org.kuali.ole.service.OverlayHelperService;
import org.kuali.ole.service.OverlayOutputService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 3/2/13
 * Time: 4:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleOverlayActionServiceImpl implements OleOverlayActionService {

    private static final Logger LOG = Logger.getLogger(OleOverlayActionServiceImpl.class);

    private OverlayOutputService overlayDocstoreOutputService;
    private OverlayOutputService overlayTransactionOutputService;
    private OverlayOutputServiceFactory overlayOutputServiceFactory;
    private   OverlayFileReaderFactory overlayFileReaderFactory;
    private BibliographicRecordHandler bibliographicRecordHandler;
    private OverlayHelperService overlayHelperService;

    private DocstoreClientLocator docstoreClientLocator;
    private   BibMarcRecordProcessor recordProcessor;
    @Override
    public String performOverlayLookupAction(String profileName, HashMap<String, Object> objects, String holdingId, OleOrderRecord oleOrderRecord) throws Exception {
      String updatedResponse = null;
        List<Item>  itemList=new ArrayList<>();
        HoldingsTree holdingsTree = getDocstoreClientLocator().getDocstoreClient().retrieveHoldingsTree(holdingId);
        Holdings holdings=holdingsTree.getHoldings();
        OleHoldings oleHoldings =  new HoldingOlemlRecordProcessor().fromXML(holdingsTree.getHoldings().getContent());
        List<org.kuali.ole.docstore.common.document.Item> items = holdingsTree.getItems();
        for(org.kuali.ole.docstore.common.document.Item itemRecord:items){
            itemList.add(new ItemOlemlRecordProcessor().fromXML(itemRecord.getContent()));
        }
        HashMap<String,Boolean> persistMap =  new HashMap<String, Boolean>();
        HashMap<String,String> actionMap = new HashMap<String, String>();
        OverlayOutputService overlayOutputService = null;
        overlayDocstoreOutputService = getOverlayDocstoreOutputService();
        overlayTransactionOutputService = getOverlayTransactionOutputService();
        actionMap.put("profileName",profileName);
        List<OleOverlayAction> oleOverlayActions = (List<OleOverlayAction>) KRADServiceLocator.getBusinessObjectService().findMatching(OleOverlayAction.class,actionMap);
        for(OleOverlayAction oleOverlayAction : oleOverlayActions){
            OverlayFileReaderService overlayFileReaderService = null;
            boolean isValid=true;
            for(OleMappingField oleMappingField : oleOverlayAction.getOleMappingFields()){
                overlayFileReaderService = getOverlayFileReaderFactory().getOverlayFileReaderService(oleMappingField.getFileFormat(),objects);
                String incomingFieldValue=null;
                    if(oleMappingField.getIncomingField()!=null){
                        String incomingFieldFromProfile =oleMappingField.getIncomingField();
                        incomingFieldValue = overlayFileReaderService.getInputFieldValue(incomingFieldFromProfile);
                        if (LOG.isDebugEnabled()){
                            LOG.debug("###incomingFieldValue####"+incomingFieldValue);
                        }
                        isValid &= incomingFieldValue.equalsIgnoreCase(oleMappingField.getIncomingFieldValue());
                    }
            }
            if(isValid){
                for(OleOutputFieldMapping oleOutputFieldMapping : oleOverlayAction.getOleOutputFieldMappings()){
                    String targetField = getTargetField(oleOutputFieldMapping);
                    String targetFieldValue = oleOutputFieldMapping.getFieldValue();
                    if(oleOutputFieldMapping.isLookUp() && overlayFileReaderService!=null){
                        StringBuffer finalValueSB = new StringBuffer();
                        List<String> dataFieldSubFieldKeyList = getDataFieldSubFieldKeyList(oleOutputFieldMapping.getFieldValue());
                        for(String dataFieldSubField : dataFieldSubFieldKeyList){
                            finalValueSB.append(overlayFileReaderService.getInputFieldValue(dataFieldSubField));
                        }
                        targetFieldValue = finalValueSB.toString();
                    }
                    performOutputMappingField(persistMap,oleOutputFieldMapping,holdings,oleHoldings,itemList,oleOrderRecord,targetField,targetFieldValue)  ;
                }
            }
        }
        overlayOutputService = getOverlayOutputServiceFactory().getOverlayOutputServiceFactory(OLEConstants.OVERLAY_DOCSTORE_OUTPUT_TARGET_OBJECT);
        if(persistMap.get(OLEConstants.OVERLAY_OLE_HOLDINGS)!=null && persistMap.get(OLEConstants.OVERLAY_OLE_HOLDINGS)){
            overlayOutputService.persist(oleHoldings);
        }
        if(persistMap.get(OLEConstants.OVERLAY_ITEM)!=null && persistMap.get(OLEConstants.OVERLAY_ITEM)){
            overlayOutputService.persist(items);
        }
        return updatedResponse;
    }
    @Override
    public void updateRecordExcludingGPF(HashMap<String, String> uuids, BibMarcRecord oldMarcRecord, BibMarcRecord newBibMarcRecord,LineItemOrder lineItemOrder, List<String> gpfFieldList, List<OverlayOption> overlayOptionList, String profileName)throws Exception{
        String bibUUID = uuids.get(OLEConstants.BIBlIOGRAPHICUUID);
       String holdingId = uuids.get(OLEConstants.OVERLAY_HOLDINGUUID);
        String updatedResponse = null;
        BibMarcRecord updateBibMarcRecord = null;
        HashMap<String,Object> objects = new HashMap<String, Object>();
        objects.put(OLEConstants.MRC,newBibMarcRecord);
        objects.put(OLEConstants.EDI,lineItemOrder);
        Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibUUID);
        String bibContent=bib.getContent();
        BibMarcRecordProcessor recordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords bibMarcRecords = recordProcessor.fromXML(bibContent);
        BibMarcRecord bibMarcRecord=bibMarcRecords.getRecords().get(0);
        updateBibMarcRecord = getOverlayHelperService().updateBibMarcRecordExcludingGPF(oldMarcRecord, newBibMarcRecord,gpfFieldList,overlayOptionList);
        String updatedRecordContent = getBibMarcRecordHandler().generateXML(updateBibMarcRecord);
        updatedResponse =performOverlayLookupAction(profileName, objects, holdingId, null);
        bib.setContent(updatedRecordContent);
        /*  bib.setId(bibUUID);*/
        getDocstoreClientLocator().getDocstoreClient().updateBib(bib);

    }
    @Override
    public void updateRecordIncludingGPF(HashMap<String,String> uuids,BibMarcRecord oldBibMarcRecord,BibMarcRecord newBibMarcRecord,LineItemOrder lineItemOrder,List<String> gpfFieldList,List<OverlayOption> overlayOptionList,String profileName)throws Exception{
        String bibUUID = uuids.get(OLEConstants.BIBlIOGRAPHICUUID);
         String holdingId = uuids.get(OLEConstants.OVERLAY_HOLDINGUUID);
        String updatedResponse=null;
        HashMap<String,Object> objects = new HashMap<String, Object>();
        objects.put(OLEConstants.MRC,newBibMarcRecord);
        objects.put(OLEConstants.EDI,lineItemOrder);
        BibMarcRecord updateBibMarcRecord = null;
        Bib bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibUUID);
        String bibContent= bib.getContent();
        BibMarcRecordProcessor recordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords bibMarcRecords = recordProcessor.fromXML(bibContent);
        BibMarcRecord bibMarcRecord = bibMarcRecords.getRecords().get(0);

        updateBibMarcRecord = getOverlayHelperService().updateBibMarcRecordIncludingGPF(bibMarcRecord, newBibMarcRecord, gpfFieldList, overlayOptionList);
        String updatedRecordContent = getBibMarcRecordHandler().generateXML(updateBibMarcRecord);
        updatedResponse = performOverlayLookupAction(profileName,objects,holdingId,null);
        bib.setContent(updatedRecordContent);

        getDocstoreClientLocator().getDocstoreClient().updateBib(bib);


    }

    private List<String> getDataFieldSubFieldKeyList(String dataFieldSubfield){
        String[] dataFieldSubfieldSplitArray = dataFieldSubfield.split("\\$");
        List<String> dataFieldSubfieldKeyList = new ArrayList<String>();
        StringBuffer dataFieldSubfieldKeySB = new StringBuffer();
        String tagName = dataFieldSubfieldSplitArray[0];
        for(int splitCount = 1;splitCount <dataFieldSubfieldSplitArray.length;splitCount++){
            dataFieldSubfieldKeySB.append(tagName).append(OLEConstants.DELIMITER_DOLLAR).append(dataFieldSubfieldSplitArray[splitCount]);
            dataFieldSubfieldKeyList.add(dataFieldSubfieldKeySB.toString());
            dataFieldSubfieldKeySB = new StringBuffer();
        }
        return dataFieldSubfieldKeyList;
    }
    private String getTargetField(OleOutputFieldMapping outputFieldMapping){
        String outputMappingTargeField = outputFieldMapping.getTargetField();
        String[] splittedOutputMappingTargeFieldArray = outputMappingTargeField.split("\\.");
        int splittedOutputMappingTargeFieldArrayLength = splittedOutputMappingTargeFieldArray.length;
        outputFieldMapping.setTargetObject(splittedOutputMappingTargeFieldArrayLength>1?splittedOutputMappingTargeFieldArray[0]:null);
        outputFieldMapping.setDetailedTargetObject(splittedOutputMappingTargeFieldArrayLength>2?splittedOutputMappingTargeFieldArray[splittedOutputMappingTargeFieldArrayLength-2]:null);
        String targetField = splittedOutputMappingTargeFieldArrayLength>0?splittedOutputMappingTargeFieldArray[splittedOutputMappingTargeFieldArrayLength-1]:null;
        return targetField;
    }
    private void performOutputMappingField(HashMap<String,Boolean> persistMap,OleOutputFieldMapping outputFieldMapping,Holdings holdings ,OleHoldings oleHoldings,List<Item> itemList, OleOrderRecord oleOrderRecord,String targetField,String targetFieldValue){
        if(outputFieldMapping.getTargetObject().equalsIgnoreCase(OLEConstants.OVERLAY_DOCSTORE_OUTPUT_TARGET_OBJECT)){
            if(outputFieldMapping.getDetailedTargetObject().equals(OLEConstants.OVERLAY_INSTANCE)){
                overlayDocstoreOutputService.setOutPutValue(targetField,targetFieldValue,holdings);
                persistMap.put(OLEConstants.OVERLAY_INSTANCE,true);
            }else if(outputFieldMapping.getDetailedTargetObject().equals(OLEConstants.OVERLAY_OLE_HOLDINGS)){
                overlayDocstoreOutputService.setOutPutValue(targetField,targetFieldValue,oleHoldings);
                persistMap.put(OLEConstants.OVERLAY_OLE_HOLDINGS,true);
            }else if(outputFieldMapping.getDetailedTargetObject().equals(OLEConstants.OVERLAY_ITEM)){
                for(Item item : itemList){
                    overlayDocstoreOutputService.setOutPutValue(targetField,targetFieldValue,item);
                }
                persistMap.put(OLEConstants.OVERLAY_ITEM,true);
            }
        }else if(outputFieldMapping.getTargetObject().equalsIgnoreCase(OLEConstants.OVERLAY_ORDERRECORD) && oleOrderRecord!=null){
            if(outputFieldMapping.getDetailedTargetObject().equalsIgnoreCase(OLEConstants.OVERLAY_TX_RECORD))     {
                overlayTransactionOutputService.setOutPutValue(targetField,targetFieldValue,oleOrderRecord.getOleTxRecord());
            }
        }
    }


    public OverlayOutputService getOverlayDocstoreOutputService() {
        if(overlayDocstoreOutputService==null){
           overlayDocstoreOutputService = getOverlayOutputServiceFactory().getOverlayOutputServiceFactory(OLEConstants.OVERLAY_DOCSTORE_OUTPUT_TARGET_OBJECT);
        }
        return overlayDocstoreOutputService;
    }

    public OverlayOutputService getOverlayTransactionOutputService() {
        if(overlayTransactionOutputService == null){
           overlayTransactionOutputService = getOverlayOutputServiceFactory().getOverlayOutputServiceFactory(OLEConstants.OVERLAY_ORDERRECORD);
        }
        return overlayTransactionOutputService;
    }

    public OverlayOutputServiceFactory getOverlayOutputServiceFactory() {
        if(overlayOutputServiceFactory == null){
            overlayOutputServiceFactory =GlobalResourceLoader.getService(OLEConstants.OVERLAY_OUTPUTSERVICE_FACTORY);
        }
        return overlayOutputServiceFactory;
    }

    public OverlayFileReaderFactory getOverlayFileReaderFactory() {
        if(overlayFileReaderFactory == null){
            overlayFileReaderFactory =GlobalResourceLoader.getService(OLEConstants.OVERLAY_FILE_READER_FACTORY);
        }
        return overlayFileReaderFactory;
    }
    private BibliographicRecordHandler getBibliographicRecordHandler(){
        if(bibliographicRecordHandler == null){
            bibliographicRecordHandler = new BibliographicRecordHandler();
        }
        return bibliographicRecordHandler;
    }
    public OverlayHelperService getOverlayHelperService() {
        if(overlayHelperService == null){
            overlayHelperService = GlobalResourceLoader.getService(OLEConstants.OVERLAY_HELPER_SERVICE);
        }
        return overlayHelperService;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return  SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    private BibMarcRecordProcessor getBibMarcRecordHandler(){
        if(recordProcessor == null){
            recordProcessor = new BibMarcRecordProcessor();
        }
        return recordProcessor;
    }



}
