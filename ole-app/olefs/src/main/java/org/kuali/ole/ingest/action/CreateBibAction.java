package org.kuali.ole.ingest.action;

import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OleHoldingsRecordHandler;
import org.kuali.ole.OleItemRecordHandler;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.model.xmlpojo.ingest.ResponseDocument;
import org.kuali.ole.describe.service.DiscoveryHelperService;
import org.kuali.ole.ingest.pojo.ProfileAttributeBo;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.ProfileAttribute;
import org.kuali.ole.pojo.edi.EDIOrder;
import org.kuali.ole.pojo.edi.LineItemOrder;
import org.kuali.ole.service.OleOrderRecordService;
import org.kuali.ole.service.OleOverlayActionService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * CreateBibAction is the action class for BatchIngest(Staff upload screen) which creates a bibliographic
 * record in Docstore.
 */
public class CreateBibAction implements Action {
    private DiscoveryHelperService discoveryHelperService;
    private OleOrderRecordService oleOrderRecordService;
    private OleOverlayActionService oleOverlayActionService;
    private OLEBatchProcessProfileBo oleBatchProcessProfileBo;

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    private List<ProfileAttribute> attributes = new ArrayList<ProfileAttribute>();

    public OLEBatchProcessProfileBo getOleBatchProcessProfileBo() {
        return oleBatchProcessProfileBo;
    }

    public void setOleBatchProcessProfileBo(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        this.oleBatchProcessProfileBo = oleBatchProcessProfileBo;
    }

    /**
     * This method takes the initial request when creating the BibAction.
     *
     * @param executionEnvironment
     */
    @Override
    public void execute(ExecutionEnvironment executionEnvironment) {
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        String ediXMLContent = (String) dataCarrierService.getData("ediXMLContent");
        if (ediXMLContent != null) {
            executeForMarcEdi(executionEnvironment);
        } else {
            executeForMarc(executionEnvironment);
        }
    }


    private List getBibInfo(String instanceUUID) {
        return getDiscoveryHelperService().getBibInformationFromInsatnceId(instanceUUID);
    }

    public void executeForMarcEdi(ExecutionEnvironment executionEnvironment) {
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        List<ProfileAttributeBo> profileAttributes = (List<ProfileAttributeBo>) dataCarrierService.getData(OLEConstants.PROFILE_ATTRIBUTE_LIST);
        BibMarcRecord bibMarcRecord = (BibMarcRecord) dataCarrierService.getData(OLEConstants.REQUEST_BIB_RECORD);
        LineItemOrder lineItemOrder = (LineItemOrder) dataCarrierService.getData(OLEConstants.REQUEST_LINE_ITEM_ORDER_RECORD);
        EDIOrder ediOrder = (EDIOrder) dataCarrierService.getData(OLEConstants.EDI_ORDER);
        String profileName = (String) dataCarrierService.getData(OLEConstants.PROFILE_NM);
        oleBatchProcessProfileBo = (OLEBatchProcessProfileBo) dataCarrierService.getData(OLEConstants.BATCH_PROFILE_BO);
        try {
            // String Ndocst = docstoreHelperService.persistNewToDocstoreForIngest(lineItemOrder,bibliographicRecord, profileAttributes);


            // BibMarcRecord bibMarcRecord = loanProcessor.getBibMarcRecord(oleLoanForm.getOleLoanFastAdd().getTitle(), oleLoanForm.getOleLoanFastAdd().getAuthor());

            List<BibMarcRecord> bibMarcRecordList = new ArrayList<>();
            bibMarcRecordList.add(bibMarcRecord);
            BibMarcRecords bibMarcRecords = new BibMarcRecords();
            bibMarcRecords.setRecords(bibMarcRecordList);
            BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
            Bib bib = new BibMarc();
            bib.setCategory(DocCategory.WORK.getCode());
            bib.setType(DocType.BIB.getCode());
            bib.setFormat(DocFormat.MARC.getCode());
            bib.setContent(bibMarcRecordProcessor.toXml(bibMarcRecords));
            List<Item> oleItemList = new ArrayList<Item>();
            oleItemList.add(getOleItem(lineItemOrder, bibMarcRecord));
            List<org.kuali.ole.docstore.common.document.Item> itemXml = new ArrayList<org.kuali.ole.docstore.common.document.Item>();
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            for (Item item : oleItemList) {
                org.kuali.ole.docstore.common.document.Item itemXmlContent = new ItemOleml();
                itemXmlContent.setContent(itemOlemlRecordProcessor.toXML(item));
                itemXml.add(itemXmlContent);

            }
            this.attributes = buildListOfProfileAttributes(profileAttributes);
            OleHoldings oleHoldings = new OleHoldingsRecordHandler().getOleHoldings(bibMarcRecord, attributes);
            Holdings holdings = new PHoldings();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            holdings.setCreatedOn(String.valueOf(dateFormat.format(new Date())));
            holdings.setLastUpdated(String.valueOf(dateFormat.format(new Date())));
            holdings.setPublic(false);
            holdings.setFastAdd(false);
            //holdings.setHarvestable("true");
            holdings.setStatus("n");


            Uri uri = new Uri();
            uri.setValue("");
            uri.setResolvable(null);
            oleHoldings.getUri().add(uri);
            Note note = new Note();
            note.setType(OLEConstants.NOTE_TYPE);
            oleHoldings.getNote().add(note);
            CallNumber callNumber = new CallNumber();
            ShelvingScheme shelvingScheme = new ShelvingScheme();
            ShelvingOrder shelvingOrder = new ShelvingOrder();
            callNumber.setType("");
            callNumber.setPrefix("");
            callNumber.setNumber("");
            shelvingScheme.setCodeValue("");
            shelvingOrder.setCodeValue("");
            callNumber.setShelvingScheme(shelvingScheme);
            callNumber.setShelvingOrder(shelvingOrder);
            oleHoldings.setCallNumber(callNumber);
            if (oleHoldings.getLocation() == null) {
                LocationLevel locationLevel = new LocationLevel();
                Location location = new Location();
                locationLevel.setLevel("");
                locationLevel.setName("");
                location.setPrimary(OLEConstants.TRUE);
                location.setStatus(OLEConstants.PERMANENT);
                location.setLocationLevel(locationLevel);
                oleHoldings.setLocation(location);
            }

            HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
            holdings.setContent(holdingOlemlRecordProcessor.toXML(oleHoldings));
            HoldingsTree holdingsTree = new HoldingsTree();
            holdingsTree.setHoldings(holdings);
            holdingsTree.getItems().addAll(itemXml);
            BibTree bibTree = new BibTree();
            bibTree.setBib(bib);
            bibTree.getHoldingsTrees().add(holdingsTree);
            try {
                getDocstoreClientLocator().getDocstoreClient().createBibTree(bibTree);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            HashMap<String, Object> objects = new HashMap<String, Object>();
            objects.put(OLEConstants.MRC, bibMarcRecord);
            objects.put(OLEConstants.EDI, lineItemOrder);
            /*ResponseHandler responseHander = new ResponseHandler();
            Response response = responseHander.toObject(responseFromDocstore);*/
            BibTree bib_Tree=getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibTree.getBib().getId());
            if (bib_Tree==null) {
                Thread.sleep(2000);
                bib_Tree=getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibTree.getBib().getId());
            }
            String holdingsUUID = bib_Tree.getHoldingsTrees().get(0).getHoldings().getId();
            //String instanceUUID = getUUID(response, OLEConstants.INSTANCE_DOC_TYPE);
            if (null == holdingsUUID) {
                throw new Exception("instance id returned from docstore is null");
            }
            /*List bibInfo = getBibInfo(instanceUUID);
            if (bibInfo.isEmpty()) {
                Thread.sleep(2000);
                bibInfo = getBibInfo(instanceUUID);
            }*/
            OleBibRecord oleBibRecord = new OleBibRecord();
            //Map<String, List> bibFieldValues = (Map<String, List>) bibInfo.get(0);
            //oleBibRecord.setBibAssociatedFieldsValueMap(bibFieldValues);
            oleBibRecord.setBib(bib_Tree.getBib());
            oleBibRecord.setLinkedInstanceId(holdingsUUID);
            // oleBibRecord.setBibUUID(getUUID(response, OLEConstants.BIB_DOC_TYPE));
            oleBibRecord.setBibUUID(bibTree.getBib().getId());
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.BIB_CREATION_FLAG, true);
            OleOrderRecord oleOrderRecord = oleOrderRecordService.fetchOleOrderRecordForMarcEdi(null, ediOrder, bibMarcRecord,0, null);
            oleOverlayActionService.performOverlayLookupAction(profileName, objects, holdingsUUID, oleOrderRecord);
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.OLE_ORDER_RECORD, oleOrderRecord);
        } catch (Exception e) {
            e.printStackTrace();
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.OLE_ORDER_RECORD, null);
        }
    }


    public void executeForMarc(ExecutionEnvironment executionEnvironment) {
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
        List<ProfileAttributeBo> profileAttributes = (List<ProfileAttributeBo>) dataCarrierService.getData(OLEConstants.PROFILE_ATTRIBUTE_LIST);
        BibMarcRecord bibMarcRecord = (BibMarcRecord) dataCarrierService.getData(OLEConstants.REQUEST_BIB_RECORD);
        //LineItemOrder lineItemOrder = (LineItemOrder) dataCarrierService.getData(OLEConstants.REQUEST_LINE_ITEM_ORDER_RECORD);
        // EDIOrder ediOrder = (EDIOrder) dataCarrierService.getData(OLEConstants.EDI_ORDER);
        String profileName = (String) dataCarrierService.getData(OLEConstants.PROFILE_NM);
        oleBatchProcessProfileBo = (OLEBatchProcessProfileBo) dataCarrierService.getData(OLEConstants.BATCH_PROFILE_BO);

        try {
            // String responseFromDocstore = docstoreHelperService.persistNewToDocstoreForIngest(bibliographicRecord, profileAttributes);
            List<BibMarcRecord> bibMarcRecordList = new ArrayList<>();
            bibMarcRecordList.add(bibMarcRecord);
            BibMarcRecords bibMarcRecords = new BibMarcRecords();
            bibMarcRecords.setRecords(bibMarcRecordList);
            BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();


            Bib bib = new BibMarc();
            bib.setCategory(DocCategory.WORK.getCode());
            bib.setType(DocType.BIB.getCode());
            bib.setFormat(DocFormat.MARC.getCode());
            bib.setContent(bibMarcRecordProcessor.toXml(bibMarcRecords));

            List<Item> oleItemList = new ArrayList<Item>();
            oleItemList.add(getOleItem(bibMarcRecord));
            List<org.kuali.ole.docstore.common.document.Item> itemXml = new ArrayList<org.kuali.ole.docstore.common.document.Item>();
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            for (Item item : oleItemList) {
                org.kuali.ole.docstore.common.document.Item itemXmlContent = new ItemOleml();
                itemXmlContent.setContent(itemOlemlRecordProcessor.toXML(item));
                itemXml.add(itemXmlContent);

            }

            this.attributes = buildListOfProfileAttributes(profileAttributes);

            OleHoldings oleHoldings = new OleHoldingsRecordHandler().getOleHoldings(bibMarcRecord, attributes);
            Holdings holdings = new PHoldings();

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            holdings.setCreatedOn(String.valueOf(dateFormat.format(new Date())));
            holdings.setLastUpdated(String.valueOf(dateFormat.format(new Date())));
            holdings.setPublic(false);
            holdings.setFastAdd(false);
            // holdings.setHarvestable("true");
            holdings.setStatus("n"); // new Record

            //List<Extension>   extensionList = new ArrayList<Extension>();
            //extensionList.add(extension);
            // oleHoldings.setExtension(extension);
            oleHoldings.setPrimary(OLEConstants.TRUE);
            //oleInstance.getOleHoldings().setReceiptStatus("");
            Uri uri = new Uri();
            uri.setValue("");
            uri.setResolvable(null);
            oleHoldings.getUri().add(uri);
            Note note = new Note();
            note.setType(OLEConstants.NOTE_TYPE);
            oleHoldings.getNote().add(note);
            CallNumber callNumber = new CallNumber();
            ShelvingScheme shelvingScheme = new ShelvingScheme();
            ShelvingOrder shelvingOrder = new ShelvingOrder();
            callNumber.setType("");
            callNumber.setPrefix("");
            callNumber.setNumber("");
            shelvingScheme.setCodeValue("");
            shelvingOrder.setCodeValue("");
            callNumber.setShelvingScheme(shelvingScheme);
            callNumber.setShelvingOrder(shelvingOrder);
            oleHoldings.setCallNumber(callNumber);
            // List<Instance> oleInstanceList = new ArrayList<Instance>();
            //OleHoldings oleHoldings = oleInstance.getOleHoldings();
            if (oleHoldings.getLocation() == null) {
                LocationLevel locationLevel = new LocationLevel();
                Location location = new Location();
                locationLevel.setLevel("");
                locationLevel.setName("");
                location.setPrimary(OLEConstants.TRUE);
                location.setStatus(OLEConstants.PERMANENT);
                location.setLocationLevel(locationLevel);
                oleHoldings.setLocation(location);
            }


            HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
            holdings.setContent(holdingOlemlRecordProcessor.toXML(oleHoldings));

            HoldingsTree holdingsTree = new HoldingsTree();

            holdingsTree.setHoldings(holdings);
            holdingsTree.getItems().addAll(itemXml);


            BibTree bibTree = new BibTree();
            bibTree.setBib(bib);
            bibTree.getHoldingsTrees().add(holdingsTree);


            try {
                getDocstoreClientLocator().getDocstoreClient().createBibTree(bibTree);
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            HashMap<String, Object> objects = new HashMap<String, Object>();
            objects.put(OLEConstants.MRC, bibMarcRecord);
            // objects.put(OLEConstants.EDI,lineItemOrder);
           /* ResponseHandler responseHander = new ResponseHandler();
            Response response = responseHander.toObject(responseFromDocstore);*/

            BibTree bib_Tree=getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibTree.getBib().getId());
            if (bib_Tree == null) {
                Thread.sleep(2000);
                bib_Tree=getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibTree.getBib().getId());
            }
            String holdingsUUID = bib_Tree.getHoldingsTrees().get(0).getHoldings().getId();
            // String instanceUUID=getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibTree.getBib().getId()).getHoldingsTrees().get(0).getHoldings().getId();
            if (null == holdingsUUID) {
                throw new Exception("instance id returned from docstore is null");
            }
/*            List bibInfo = getBibInfo(instanceUUID);
            if (bibInfo.isEmpty()) {
                Thread.sleep(2000);
                bibInfo = getBibInfo(instanceUUID);
            }*/
            OleBibRecord oleBibRecord = new OleBibRecord();
            //Map<String, List> bibFieldValues = (Map<String, List>) bibInfo.get(0);
            //oleBibRecord.setBibAssociatedFieldsValueMap(bibFieldValues);
            oleBibRecord.setBib(bib_Tree.getBib());
            oleBibRecord.setLinkedInstanceId(holdingsUUID);
            oleBibRecord.setBibUUID(bibTree.getBib().getId());
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.BIB_CREATION_FLAG, true);
            OleOrderRecord oleOrderRecord = oleOrderRecordService.fetchOleOrderRecordForMarc(null,bibMarcRecord,0, null);
            boolean validBFNFlag = (Boolean) (oleOrderRecord.getMessageMap().get("isValidBFN") == null ? true : oleOrderRecord.getMessageMap().get("isValidBFN"));
            if (validBFNFlag) {
                oleOverlayActionService.performOverlayLookupAction(profileName, objects, holdingsUUID, oleOrderRecord);
            }
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.OLE_ORDER_RECORD, oleOrderRecord);
        } catch (Exception e) {
            e.printStackTrace();
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.OLE_ORDER_RECORD, null);
        }
    }

    /**
     * This method simulate the executionEnvironment.
     *
     * @param executionEnvironment
     */

    @Override
    public void executeSimulation(ExecutionEnvironment executionEnvironment) {
        execute(executionEnvironment);
    }

    /**
     * This method gets the uuid based on docType
     *
     * @param response
     * @param docType
     * @return uuid
     */
    private String getUUID(Response response, String docType) {
        List<ResponseDocument> documents = response.getDocuments();
        return getUUID(documents, docType);
    }

    /**
     * This method gets the uuid based on List of documents and docType.
     *
     * @param documents
     * @param docType
     * @return uuid
     */
    private String getUUID(List<ResponseDocument> documents, String docType) {
        for (Iterator<ResponseDocument> iterator = documents.iterator(); iterator.hasNext(); ) {
            ResponseDocument responseDocument = iterator.next();
            if (responseDocument.getType().equals(docType)) {
                return responseDocument.getUuid();
            } else {
                return getUUID(responseDocument.getLinkedDocuments(), docType);
            }
        }
        return null;
    }


    /**
     * Sets the discoveryHelperService attribute value.
     *
     * @param discoveryHelperService .The discoveryHelperService to set.
     */
    public void setDiscoveryHelperService(DiscoveryHelperService discoveryHelperService) {
        this.discoveryHelperService = discoveryHelperService;
    }

    /**
     * Gets the discoveryHelperService attribute.
     *
     * @return Returns discoveryHelperService.
     */
    public DiscoveryHelperService getDiscoveryHelperService() {
        return discoveryHelperService;
    }

    public OleOrderRecordService getOleOrderRecordService() {
        return oleOrderRecordService;
    }

    public void setOleOrderRecordService(OleOrderRecordService oleOrderRecordService) {
        this.oleOrderRecordService = oleOrderRecordService;
    }

    public OleOverlayActionService getOleOverlayActionService() {
        return oleOverlayActionService;
    }

    public void setOleOverlayActionService(OleOverlayActionService oleOverlayActionService) {
        this.oleOverlayActionService = oleOverlayActionService;
    }

    private List<ProfileAttribute> buildListOfProfileAttributes(List<ProfileAttributeBo> profileAttributes) {
        for (Iterator iterator = profileAttributes.iterator(); iterator.hasNext(); ) {
            ProfileAttributeBo profileAttributeBo = (ProfileAttributeBo) iterator.next();
            ProfileAttribute profileAttribute = new ProfileAttribute();
            profileAttribute.setAgendaName(profileAttributeBo.getAgendaName());
            profileAttribute.setAttributeName(profileAttributeBo.getAttributeName());
            profileAttribute.setAttributeValue(profileAttributeBo.getAttributeValue());
            profileAttribute.setSystemValue(profileAttributeBo.getSystemValue());
            attributes.add(profileAttribute);
        }
        return attributes;
    }

    public Item getOleItem(LineItemOrder lineItemOrder, BibMarcRecord bibliographicRecord) {
        for (DataField dataField : bibliographicRecord.getDataFields()) {
            if (dataField.getTag().equalsIgnoreCase(OLEConstants.DATA_FIELD_985)) {
                List<SubField> subFieldList = dataField.getSubFields();
                SubField subField = new SubField();
                subField.setCode(OLEConstants.SUB_FIELD_A);
                subField.setValue(OLEConstants.DEFAULT_LOCATION_LEVEL_INSTITUTION);
                subFieldList.add(subField);
                dataField.setSubFields(subFieldList);
            }
        }
        return new OleItemRecordHandler().getOleItem(lineItemOrder, bibliographicRecord, attributes);
    }

    public Item getOleItem(BibMarcRecord bibliographicRecord) {
        for (DataField dataField : bibliographicRecord.getDataFields()) {
            if (dataField.getTag().equalsIgnoreCase(OLEConstants.DATA_FIELD_985)) {
                List<SubField> subFieldList = dataField.getSubFields();
                SubField subField = new SubField();
                subField.setCode(OLEConstants.SUB_FIELD_A);
                subField.setValue(OLEConstants.DEFAULT_LOCATION_LEVEL_INSTITUTION);
                subFieldList.add(subField);
                dataField.setSubFields(subFieldList);
            }
        }
        return new OleItemRecordHandler().getOleItem(bibliographicRecord, attributes);
    }

}
