package org.kuali.ole.docstore.discovery.solr.work.instance.oleml;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.InstanceOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.discovery.solr.work.bib.DocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.bib.WorkBibCommonFields;
import org.kuali.ole.docstore.discovery.solr.work.bib.marc.WorkBibMarcDocBuilder;
import org.kuali.ole.docstore.discovery.solr.work.instance.WorkInstanceCommonFields;
import org.kuali.ole.docstore.indexer.solr.DocumentLocalId;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentCategory;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentConfig;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentFormat;
import org.kuali.ole.docstore.model.xmlpojo.config.DocumentType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.docstore.model.xmlpojo.metadata.DocumentMetaData;
import org.kuali.ole.docstore.model.xmlpojo.metadata.Field;
import org.kuali.ole.docstore.utility.XMLUtility;
import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ND6967
 * Date: 2/14/12
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkInstanceOlemlDocBuilder extends DocBuilder implements WorkBibCommonFields, WorkInstanceCommonFields {

    private static final DocumentMetaData oleMLInstanceMetaData = new DocumentMetaData();
    private static final DocumentMetaData oleMLHoldingMetaData = new DocumentMetaData();
    private static final DocumentMetaData oleMLItemMetaData = new DocumentMetaData();
    private static final Logger LOG = LoggerFactory.getLogger(WorkInstanceOlemlDocBuilder.class);
    private ItemOlemlRecordProcessor workItemOlemlRecordProcessor = new ItemOlemlRecordProcessor();

    static {

        List<DocumentCategory> docCategories = DocumentConfig.getInstance().getDocumentCategories();

        for (DocumentCategory cat : docCategories) {

            for (DocumentType type : cat.getDocumentTypes()) {

                for (DocumentFormat format : type.getDocumentFormats()) {

                    if (DocCategory.WORK.isEqualTo(cat.getId()) && DocType.INSTANCE.isEqualTo(type.getId())
                            && DocFormat.OLEML.isEqualTo(format.getId())) {

                        for (org.kuali.ole.docstore.model.xmlpojo.config.Field field : format.getFields()) {

                            Field olemlField = new Field();
                            olemlField.setName(field.getId());
                            if (field.getMapping().getInclude() != null)
                                olemlField.set("xpath", field.getMapping().getInclude());
                            oleMLInstanceMetaData.getFields().add(olemlField);

                        }

                    } else if (DocCategory.WORK.isEqualTo(cat.getId()) && DocType.HOLDINGS.isEqualTo(type.getId())
                            && DocFormat.OLEML.isEqualTo(format.getId())) {

                        for (org.kuali.ole.docstore.model.xmlpojo.config.Field field : format.getFields()) {

                            Field olemlField = new Field();
                            olemlField.setName(field.getId());
                            if (field.getMapping().getInclude() != null)
                                olemlField.set("xpath", field.getMapping().getInclude());
                            oleMLHoldingMetaData.getFields().add(olemlField);

                        }

                    } else if (DocCategory.WORK.isEqualTo(cat.getId()) && DocType.ITEM.isEqualTo(type.getId()) && DocFormat.OLEML.isEqualTo(format.getId())) {

                        for (org.kuali.ole.docstore.model.xmlpojo.config.Field field : format.getFields()) {

                            Field olemlField = new Field();
                            olemlField.setName(field.getId());
                            if (field.getMapping().getInclude() != null)
                                olemlField.set("xpath", field.getMapping().getInclude());
                            oleMLItemMetaData.getFields().add(olemlField);

                        }

                    }

                }

            }

        }

    }

    public void buildSolrInputDocumentsForHolding(Instance instance, List<SolrInputDocument> solrInputDocuments, RequestDocument requestDocument) {
//        CallNumber callNumber = null;
        String shelvingOrderFullValue = "";
        SolrInputDocument solrDocForHolding = new SolrInputDocument();
        solrDocForHolding.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrDocForHolding.addField(DOC_TYPE, DocType.HOLDINGS.getCode());
        solrDocForHolding.addField(DOC_FORMAT, DocFormat.OLEML.getCode());
        solrDocForHolding.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(instance.getOleHoldings().getHoldingsIdentifier()));
        solrDocForHolding.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(instance.getOleHoldings().getHoldingsIdentifier()));
        solrDocForHolding.addField(ID, instance.getOleHoldings().getHoldingsIdentifier());
        solrDocForHolding.addField(UNIQUE_ID, instance.getOleHoldings().getHoldingsIdentifier());
        InstanceOlemlRecordProcessor workInstaceOlemlRecordProcessor = new InstanceOlemlRecordProcessor();
        XMLUtility xmlUtility = new XMLUtility();
        solrDocForHolding.addField(ALL_TEXT, xmlUtility.getAllContentText(workInstaceOlemlRecordProcessor.toXML(instance.getOleHoldings())));
        if (instance.getOleHoldings() != null && instance.getOleHoldings().getExtension() != null && instance.getOleHoldings().getExtension().getContent().size() > 0 && instance.getOleHoldings().getExtension().getContent().get(0) != null) {
            AdditionalAttributes additionalAttributes = (AdditionalAttributes) instance.getOleHoldings().getExtension().getContent().get(0);
            String staffOnlyFlagForHoldings = additionalAttributes.getAttributeMap().get("staffOnlyFlag");
            if (staffOnlyFlagForHoldings != null) {
                solrDocForHolding.addField(STAFF_ONLY_FLAG, staffOnlyFlagForHoldings.equalsIgnoreCase(Boolean.TRUE.toString()) ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
            } else if (requestDocument.getAdditionalAttributes() != null && requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STAFFONLYFLAG) != null &&
                    requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STAFFONLYFLAG).equalsIgnoreCase(Boolean.TRUE.toString())) {
                solrDocForHolding.addField(STAFF_ONLY_FLAG, Boolean.TRUE.toString());
            } else {
                solrDocForHolding.addField(STAFF_ONLY_FLAG, Boolean.FALSE.toString());
            }

        } else {
            if (requestDocument.getAdditionalAttributes() != null && requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STAFFONLYFLAG) != null && requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STAFFONLYFLAG).equalsIgnoreCase(Boolean.TRUE.toString())) {
                solrDocForHolding.addField(STAFF_ONLY_FLAG, Boolean.TRUE.toString());
            } else {
                solrDocForHolding.addField(STAFF_ONLY_FLAG, Boolean.FALSE.toString());
            }
        }

        /*    if(requestDocument.getAdditionalAttributes()!=null){
                    solrDocForHolding.setField(STAFF_ONLY_FLAG, requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STAFFONLYFLAG));
        }*/
        Date date = new Date();
        if ("checkIn".equalsIgnoreCase(requestDocument.getOperation())) {
            IndexerService indexerService = getIndexerService(requestDocument);
            List<SolrDocument> solrDocumentList = indexerService.getSolrDocumentBySolrId(requestDocument.getId());
            SolrDocument solrDocument = solrDocumentList.get(0);
            //String user = requestDocument.getAdditionalAttributes() == null ? null : requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.UPDATED_BY);
            //Extension extention=instance.getOleHoldings().getExtension();
            String user = null;
            if (instance.getOleHoldings() != null && instance.getOleHoldings().getExtension() != null && instance.getOleHoldings().getExtension().getContent() != null) {
                AdditionalAttributes additionalAttributes = (AdditionalAttributes) instance.getOleHoldings().getExtension().getContent().get(0);
                user = additionalAttributes.getAttributeMap().get("createdBy");
            } else {
                if (requestDocument.getAdditionalAttributes() != null && requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.UPDATED_BY) != null) {
                    user = requestDocument.getAdditionalAttributes() == null ? null : requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.UPDATED_BY);
                }
            }
            user = user == null ? requestDocument.getUser() : user;
            solrDocForHolding.addField(UPDATED_BY, user);
            solrDocForHolding.addField(DATE_ENTERED, solrDocument.getFieldValue(DATE_ENTERED));
            solrDocForHolding.addField(CREATED_BY, solrDocument.getFieldValue(CREATED_BY));
            solrDocForHolding.addField(DATE_UPDATED, date);
        } else {
            //String user = requestDocument.getAdditionalAttributes() == null ? null : requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.CREATED_BY);
            String user = null;
            if (instance.getOleHoldings() != null && instance.getOleHoldings().getExtension() != null && instance.getOleHoldings().getExtension().getContent()!=null && instance.getOleHoldings().getExtension().getContent().size()>0) {
                AdditionalAttributes additionalAttributes = (AdditionalAttributes) instance.getOleHoldings().getExtension().getContent().get(0);
                user = additionalAttributes.getAttributeMap().get("createdBy");
                if(user == null){
                    if(requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY) != null){
                        user = requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY);
                    }
                }
            } else {
                if (requestDocument != null && requestDocument.getAdditionalAttributes() != null) {
                    if(requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.CREATED_BY) != null){
                        user = requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.CREATED_BY);
                    }
                    if(user == null){
                        if(requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY) != null){
                            user = requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.HOLDINGS_CREATED_BY);
                        }
                    }
                }
            }
            user = user == null ? requestDocument.getUser() : user;
            solrDocForHolding.addField(DATE_ENTERED, date);
            solrDocForHolding.addField(CREATED_BY, user);
            //solrDocForHolding.addField(UPDATED_BY, user);
        }
        // solrDocForHolding.addField(DATE_UPDATED, date);

        solrDocForHolding.addField(RECEIPT_STATUS_SEARCH, instance.getOleHoldings().getReceiptStatus());
        solrDocForHolding.addField(RECEIPT_STATUS_DISPLAY, instance.getOleHoldings().getReceiptStatus());

        if (instance.getOleHoldings().getCopyNumber() != null) {
            solrDocForHolding.addField(COPY_NUMBER_SEARCH, instance.getOleHoldings().getCopyNumber());
            solrDocForHolding.addField(COPY_NUMBER_LABEL_SEARCH, instance.getOleHoldings().getCopyNumber());
            solrDocForHolding.addField(COPY_NUMBER_DISPLAY, instance.getOleHoldings().getCopyNumber());
            solrDocForHolding.addField(COPY_NUMBER_LABEL_DISPLAY, instance.getOleHoldings().getCopyNumber());
        }
        if (instance.getOleHoldings().getCallNumber() != null) {
            solrDocForHolding.addField(CALL_NUMBER_TYPE_SEARCH, instance.getOleHoldings().getCallNumber().getType());
            solrDocForHolding.addField(CALL_NUMBER_SEARCH, instance.getOleHoldings().getCallNumber().getNumber());
            solrDocForHolding.addField(ITEM_PART_SEARCH, instance.getOleHoldings().getCallNumber().getItemPart());
            solrDocForHolding.addField(CALL_NUMBER_PREFIX_SEARCH, instance.getOleHoldings().getCallNumber().getPrefix());
            solrDocForHolding.addField(CLASSIFICATION_PART_SEARCH, instance.getOleHoldings().getCallNumber().getClassificationPart());

            solrDocForHolding.addField(ITEM_PART_DISPLAY, instance.getOleHoldings().getCallNumber().getItemPart());
            solrDocForHolding.addField(CALL_NUMBER_TYPE_DISPLAY, instance.getOleHoldings().getCallNumber().getType());
            solrDocForHolding.addField(CALL_NUMBER_DISPLAY, instance.getOleHoldings().getCallNumber().getNumber());
            solrDocForHolding.addField(CALL_NUMBER_PREFIX_DISPLAY, instance.getOleHoldings().getCallNumber().getPrefix());
            solrDocForHolding.addField(CLASSIFICATION_PART_DISPLAY, instance.getOleHoldings().getCallNumber().getClassificationPart());

            if (instance.getOleHoldings().getCallNumber().getShelvingScheme() != null) {
                solrDocForHolding.addField(SHELVING_SCHEME_VALUE_SEARCH, instance.getOleHoldings().getCallNumber().getShelvingScheme().getFullValue());
                solrDocForHolding.addField(SHELVING_SCHEME_CODE_SEARCH, instance.getOleHoldings().getCallNumber().getShelvingScheme().getCodeValue());
                solrDocForHolding.addField(SHELVING_SCHEME_VALUE_DISPLAY, instance.getOleHoldings().getCallNumber().getShelvingScheme().getFullValue());
                solrDocForHolding.addField(SHELVING_SCHEME_CODE_DISPLAY, instance.getOleHoldings().getCallNumber().getShelvingScheme().getCodeValue());
            }

            /* String shelvingOrder = instance.getOleHoldings().getCallNumber().getShelvingOrder().getFullValue();
            // Compute shelvingOrder if empty. - Temporary
            if (StringUtils.isEmpty(shelvingOrder)) {
                String callNumber = null;
                String shelvingScheme = null;
                if (instance.getOleHoldings().getCallNumber() != null) {
                    callNumber = instance.getOleHoldings().getCallNumber().getNumber();
                    if (instance.getOleHoldings().getCallNumber().getShelvingScheme() != null) {
                        shelvingScheme = instance.getOleHoldings().getCallNumber().getShelvingScheme().getCodeValue();
                    }

                }
                if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(shelvingScheme)) {

                    CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(shelvingScheme);
                    shelvingOrder = callNumberObj.getSortableKey(callNumber);
                    shelvingOrder = shelvingOrder.replaceAll(" ", "_");
                    instance.getOleHoldings().getCallNumber().getShelvingOrder().setFullValue(shelvingOrder);
                }
            }*/
//            if (instance.getOleHoldings().getCallNumber().getShelvingOrder() != null) {
//                solrDocForHolding.addField(SHELVING_ORDER_SORT, instance.getOleHoldings().getCallNumber().getShelvingOrder().getFullValue());
//                solrDocForHolding.addField(SHELVING_ORDER_SEARCH, instance.getOleHoldings().getCallNumber().getShelvingOrder().getFullValue());
//                solrDocForHolding.addField(SHELVING_ORDER_DISPLAY, instance.getOleHoldings().getCallNumber().getShelvingOrder().getFullValue());
//            }
            String shelvingOrder = null;
            if (instance.getOleHoldings().getCallNumber().getShelvingOrder() != null) {
                shelvingOrder = instance.getOleHoldings().getCallNumber().getShelvingOrder().getFullValue();
                //shelvingOrder = shelvingOrder.replaceAll(" ", "_");
            }
            if (StringUtils.isNotEmpty(shelvingOrder)) {
                shelvingOrder = shelvingOrder.replaceAll(" ", "-");
                solrDocForHolding.addField(SHELVING_ORDER_SORT, shelvingOrder);
                solrDocForHolding.addField(SHELVING_ORDER_SEARCH, shelvingOrder);
                solrDocForHolding.addField(SHELVING_ORDER_DISPLAY, shelvingOrder);
            }
        }
        if (instance.getOleHoldings() != null && instance.getOleHoldings().getLocation() != null &&
                instance.getOleHoldings().getLocation().getLocationLevel() != null) {
            StringBuffer locationName = new StringBuffer();
            StringBuffer locationLevel = new StringBuffer();
            Location location = instance.getOleHoldings().getLocation();
            buildLocationNameAndLocationLevel(location, locationName, locationLevel);
            solrDocForHolding.addField(LOCATION_LEVEL_SEARCH, locationName.toString());
            solrDocForHolding.addField(LOCATION_LEVEL_NAME_SEARCH, locationLevel.toString());
            solrDocForHolding.addField(LOCATION_LEVEL_DISPLAY, locationName.toString());
            solrDocForHolding.addField(LOCATION_LEVEL_NAME_DISPLAY, locationLevel.toString());
            solrDocForHolding.addField(LOCATION_LEVEL_SORT, locationName.toString());
        }
        for (Note holdingNote : instance.getOleHoldings().getNote()) {
            solrDocForHolding.addField(HOLDING_NOTE_SEARCH, holdingNote.getValue());
            solrDocForHolding.addField(HOLDING_NOTE_DISPLAY, holdingNote.getValue());
        }
        for (Uri uri : instance.getOleHoldings().getUri()) {
            solrDocForHolding.addField(URI_SEARCH, uri.getValue());
            solrDocForHolding.addField(URI_DISPLAY, uri.getValue());
        }
        for (String rid : instance.getResourceIdentifier()) {
            solrDocForHolding.addField(BIB_IDENTIFIER, rid);
        }
        solrDocForHolding.addField(INSTANCE_IDENTIFIER, instance.getInstanceIdentifier());
        for (Item oleItem : instance.getItems().getItem()) {
            solrDocForHolding.addField(ITEM_IDENTIFIER, oleItem.getItemIdentifier());
        }
        solrInputDocuments.add(solrDocForHolding);
    }

    public void buildSolrInputDocumentsForItems(Instance oleInstance, List<SolrInputDocument> solrInputDocuments, RequestDocument requestDocument) {
//        CallNumber callNumber = null;
        String shelvingOrderFullValue = "";
        XMLUtility xmlUtility = new XMLUtility();
        for (Item item : oleInstance.getItems().getItem()) {
            SolrInputDocument solrDocForItem = new SolrInputDocument();
            solrDocForItem.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
            solrDocForItem.addField(DOC_TYPE, DOC_TYPE_ITEM_VALUE);
            solrDocForItem.addField(DOC_FORMAT, DOC_FORMAT_INSTANCE_VALUE);
            solrDocForItem.addField(ID, item.getItemIdentifier());
            solrDocForItem.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(item.getItemIdentifier()));
            solrDocForItem.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(item.getItemIdentifier()));
            solrDocForItem.addField(ALL_TEXT, xmlUtility.getAllContentText(workItemOlemlRecordProcessor.toXML(item)));
            solrDocForItem.addField(CLMS_RET_FLAG, item.isClaimsReturnedFlag());
            solrDocForItem.addField(CLMS_RET_FLAG_CRE_DATE, item.getClaimsReturnedFlagCreateDate());
            solrDocForItem.addField(CLMS_RET_NOTE, item.getClaimsReturnedNote());
            solrDocForItem.addField(CURRENT_BORROWER, item.getCurrentBorrower());
            solrDocForItem.addField(PROXY_BORROWER, item.getProxyBorrower());
            solrDocForItem.addField(DUE_DATE_TIME, item.getDueDateTime());

            if (item.getExtension() != null && item.getExtension().getContent() != null && item.getExtension().getContent().size() > 0 && item.getExtension().getContent().get(0) != null) {
                AdditionalAttributes additionalAttributes = (AdditionalAttributes) item.getExtension().getContent().get(0);
                String staffOnlyFlagForItem = additionalAttributes.getAttributeMap().get("staffOnlyFlag");
                if (staffOnlyFlagForItem != null) {
                    solrDocForItem.addField(STAFF_ONLY_FLAG, staffOnlyFlagForItem.equalsIgnoreCase(Boolean.TRUE.toString()) ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
                } else if (requestDocument.getAdditionalAttributes() != null && requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STAFFONLYFLAG) != null &&
                        requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.STAFFONLYFLAG).equalsIgnoreCase(Boolean.TRUE.toString())) {
                    solrDocForItem.addField(STAFF_ONLY_FLAG, Boolean.TRUE.toString());
                } else {
                    solrDocForItem.addField(STAFF_ONLY_FLAG, Boolean.FALSE.toString());
                }
            } else {
                solrDocForItem.addField(STAFF_ONLY_FLAG, Boolean.FALSE.toString());
            }

            Date date = new Date();
            if ("checkIn".equalsIgnoreCase(requestDocument.getOperation())) {
                IndexerService indexerService = getIndexerService(requestDocument);
                List<SolrDocument> solrDocumentList = indexerService.getSolrDocumentBySolrId(requestDocument.getUuid());
                SolrDocument solrDocument = solrDocumentList.get(0);
                String user = requestDocument.getAdditionalAttributes() == null ? null : requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.UPDATED_BY);
                user = user == null ? requestDocument.getUser() : user;
                solrDocForItem.addField(UPDATED_BY, user);
                solrDocForItem.addField(DATE_ENTERED, solrDocument.getFieldValue(DATE_ENTERED));
                solrDocForItem.addField(CREATED_BY, solrDocument.getFieldValue(CREATED_BY));
                solrDocForItem.addField(DATE_UPDATED, date);
            } else {
                //String user = requestDocument.getAdditionalAttributes() == null ? null : requestDocument.getAdditionalAttributes().getAttribute(AdditionalAttributes.CREATED_BY);
                String user = null;
                if (item.getExtension() != null && item.getExtension().getContent() != null  && item.getExtension().getContent().size()>0) {
                    AdditionalAttributes additionalAttributes = (AdditionalAttributes) item.getExtension().getContent().get(0);
                    user = additionalAttributes.getAttributeMap().get("createdBy");
                }
                user = user == null ? requestDocument.getUser() : user;
                solrDocForItem.addField(DATE_ENTERED, date);
                solrDocForItem.addField(CREATED_BY, user);
                //solrDocForItem.addField(UPDATED_BY, user);
            }
            //solrDocForItem.addField(DATE_UPDATED, date);

            solrDocForItem.addField(ITEM_IDENTIFIER_SEARCH, item.getItemIdentifier());
            solrDocForItem.addField(BARCODE_ARSL_SEARCH, item.getBarcodeARSL());
            solrDocForItem.addField(COPY_NUMBER_SEARCH, item.getCopyNumber());
            solrDocForItem.addField(COPY_NUMBER_LABEL_SEARCH, item.getCopyNumberLabel());
            solrDocForItem.addField(PURCHASE_ORDER_LINE_ITEM_IDENTIFIER_SEARCH, item.getPurchaseOrderLineItemIdentifier());
            solrDocForItem.addField(VENDOR_LINE_ITEM_IDENTIFIER_SEARCH, item.getVendorLineItemIdentifier());
            solrDocForItem.addField(COPY_NUMBER_LABEL_SEARCH, item.getCopyNumberLabel());
            solrDocForItem.addField(VOLUME_NUMBER_LABEL_SEARCH, item.getVolumeNumberLabel());
            solrDocForItem.addField(VOLUME_NUMBER_SEARCH, item.getVolumeNumberLabel());
            solrDocForItem.addField(ENUMERATION_SEARCH, item.getEnumeration());
            solrDocForItem.addField(CHRONOLOGY_SEARCH, item.getChronology());

            // Item call number should be indexed if it is available at item level or holdings level.
            String itemCallNumber = null;

            // Not available at item level
            if ((item.getCallNumber() == null) || StringUtils.isEmpty(StringUtils.trimToEmpty(item.getCallNumber().getNumber()))) {
                if (oleInstance.getOleHoldings().getCallNumber() != null) {
                    itemCallNumber = StringUtils.trimToEmpty(oleInstance.getOleHoldings().getCallNumber().getNumber());
                }
            }
            // Available at item level
            else {
                itemCallNumber = item.getCallNumber().getNumber();
            }

            if (StringUtils.isNotEmpty(itemCallNumber)) {
                solrDocForItem.addField(CALL_NUMBER_SEARCH, itemCallNumber);
                solrDocForItem.addField(CALL_NUMBER_DISPLAY, itemCallNumber);
            }

            if (item.getCallNumber() != null) {
                solrDocForItem.addField(CALL_NUMBER_TYPE_SEARCH, item.getCallNumber().getType());
                solrDocForItem.addField(CALL_NUMBER_PREFIX_SEARCH, item.getCallNumber().getPrefix());
                solrDocForItem.addField(CLASSIFICATION_PART_SEARCH, item.getCallNumber().getClassificationPart());

                solrDocForItem.addField(CALL_NUMBER_TYPE_DISPLAY, item.getCallNumber().getType());
                solrDocForItem.addField(CALL_NUMBER_PREFIX_DISPLAY, item.getCallNumber().getPrefix());
                solrDocForItem.addField(CLASSIFICATION_PART_DISPLAY, item.getCallNumber().getClassificationPart());

                //Shelving scheme code should be indexed if it is available at holdings level
                String shelvingSchemeCode = "";
                String shelvingSchemeValue = "";

                //Not available at item level
                if ((item.getCallNumber().getShelvingScheme() == null) || StringUtils
                        .isEmpty(StringUtils.trimToEmpty(item.getCallNumber().getShelvingScheme().getCodeValue()))) {
                    if (oleInstance.getOleHoldings().getCallNumber() != null) {
                        if (oleInstance.getOleHoldings().getCallNumber().getShelvingScheme() != null) {
                            shelvingSchemeCode = StringUtils.trimToEmpty(
                                    oleInstance.getOleHoldings().getCallNumber().getShelvingScheme().getCodeValue());
                            shelvingSchemeValue = StringUtils.trimToEmpty(
                                    oleInstance.getOleHoldings().getCallNumber().getShelvingScheme().getFullValue());
                        }
                    }
                }
                //Available at Item level
                else {
                    shelvingSchemeCode = item.getCallNumber().getShelvingScheme().getCodeValue();
                    shelvingSchemeValue = item.getCallNumber().getShelvingScheme().getFullValue();
                }

                if (StringUtils.isNotEmpty(shelvingSchemeCode)) {
                    solrDocForItem.addField(SHELVING_SCHEME_CODE_SEARCH, shelvingSchemeCode);
                    solrDocForItem.addField(SHELVING_SCHEME_CODE_DISPLAY, shelvingSchemeCode);
                }
                if (StringUtils.isNotEmpty(shelvingSchemeValue)) {
                    solrDocForItem.addField(SHELVING_SCHEME_VALUE_SEARCH, shelvingSchemeValue);
//                    solrDocForItem.addField(SHELVING_SCHEME_CODE_SEARCH, item.getCallNumber().getShelvingScheme().getCodeValue());

                    solrDocForItem.addField(SHELVING_SCHEME_VALUE_DISPLAY, shelvingSchemeValue);
//                    solrDocForItem.addField(SHELVING_SCHEME_CODE_DISPLAY, item.getCallNumber().getShelvingScheme().getCodeValue());
                }

                String shelvingOrder = null;
                if (item.getCallNumber().getShelvingOrder() != null) {
                    shelvingOrder = item.getCallNumber().getShelvingOrder().getFullValue();
                }
                if (StringUtils.isEmpty(shelvingOrder) && oleInstance.getOleHoldings().getCallNumber() != null) {
                    try {
                        //Build sortable key for a valid call number
                        if (oleInstance.getOleHoldings().getCallNumber().getShelvingScheme() != null) {
                            boolean isValid = validateCallNumber(itemCallNumber, oleInstance.getOleHoldings().getCallNumber().getShelvingScheme().getCodeValue());
                            if (isValid) {
                                shelvingOrder = buildSortableCallNumber(itemCallNumber, oleInstance.getOleHoldings().getCallNumber().getShelvingScheme().getCodeValue());
                            } else {
                                shelvingOrder = itemCallNumber;
                            }
                        }
                    } catch (Exception e) {
                        LOG.info("Exception due to :" + e.getMessage(), e);
                        LOG.error(e.getMessage(), e);  //To change body of catch statement use File | Settings | File Templates.
                    }
                    // shelvingOrder = oleInstance.getOleHoldings().getCallNumber().getShelvingOrder().getFullValue();
                }
                if (StringUtils.isNotEmpty(shelvingOrder)) {
                    shelvingOrder = shelvingOrder.replaceAll(" ", "-");
                    solrDocForItem.addField(SHELVING_ORDER_SORT, shelvingOrder);
                    solrDocForItem.addField(SHELVING_ORDER_SEARCH, shelvingOrder);
                    solrDocForItem.addField(SHELVING_ORDER_DISPLAY, shelvingOrder);
                }
                if (item.getCallNumber() != null && item.getCallNumber().getPrefix() != null) {
                    solrDocForItem.addField(CALLNUMBER_PREFIX_SORT, item.getCallNumber().getPrefix());
                }
                if (item.getCallNumber() != null && item.getCallNumber().getNumber() != null) {
                    solrDocForItem.addField(CALLNUMBER_SORT, item.getCallNumber().getNumber());
                }
                if (item.getEnumeration() != null) {
                    String enumerationSort = getNormalizedEnumeration(item.getEnumeration());
                    solrDocForItem.addField(ENUMERATION_SORT, enumerationSort);
                }
                if (item.getChronology() != null) {
                    solrDocForItem.addField(CHRONOLOGY_SORT, item.getChronology());
                }
                if (item.getCopyNumber() != null) {
                    String copyNumberSort = getNormalizedEnumeration(item.getCopyNumber());
                    solrDocForItem.addField(COPYNUMBER_SORT, copyNumberSort);
                }
                if (item.getAccessInformation() != null && item.getAccessInformation().getBarcode() != null) {
                    solrDocForItem.addField(ITEM_BARCODE_SORT, item.getAccessInformation().getBarcode());
                }
            }

            if (item.getItemStatus() != null) {
                solrDocForItem.addField(ITEM_STATUS_DISPLAY, item.getItemStatus().getCodeValue());
                solrDocForItem.addField(ITEM_STATUS_SEARCH, item.getItemStatus().getCodeValue());
            }
            if (item.getLocation() != null &&
                    item.getLocation().getLocationLevel() != null) {
                StringBuffer locationName = new StringBuffer();
                StringBuffer locationLevel = new StringBuffer();
                Location location = item.getLocation();
                buildLocationNameAndLocationLevel(location, locationName, locationLevel);
                solrDocForItem.addField(LOCATION_LEVEL_SEARCH, locationName.toString());
                solrDocForItem.addField(LOCATION_LEVEL_NAME_SEARCH, locationLevel.toString());
                solrDocForItem.addField(LOCATION_LEVEL_DISPLAY, locationName.toString());
                solrDocForItem.addField(LOCATION_LEVEL_NAME_DISPLAY, locationLevel.toString());
                solrDocForItem.addField(LOCATION_LEVEL_SORT, locationName.toString());
            }


            if (item.getItemType() != null) {
                solrDocForItem.addField(ITEM_TYPE_FULL_VALUE_SEARCH, item.getItemType().getFullValue());
                solrDocForItem.addField(ITEM_TYPE_CODE_VALUE_SEARCH, item.getItemType().getCodeValue());
                solrDocForItem.addField(ITEM_TYPE_FULL_VALUE_DISPLAY, item.getItemType().getFullValue());
                solrDocForItem.addField(ITEM_TYPE_CODE_VALUE_DISPLAY, item.getItemType().getCodeValue());
            }

            if (item.getAccessInformation() != null) {
                if (item.getAccessInformation().getBarcode() != null) {
                    solrDocForItem.addField(ITEM_BARCODE_SEARCH, item.getAccessInformation().getBarcode());
                    solrDocForItem.addField(ITEM_BARCODE_DISPLAY, item.getAccessInformation().getBarcode());
                }
                if (item.getAccessInformation().getUri() != null) {
                    solrDocForItem.addField(ITEM_URI_SEARCH, item.getAccessInformation().getUri().getValue());
                    solrDocForItem.addField(ITEM_URI_DISPLAY, item.getAccessInformation().getUri().getValue());
                }
            }

            for (StatisticalSearchingCode searchingCode : item.getStatisticalSearchingCode()) {
                if (searchingCode != null) {
                    solrDocForItem.addField(STATISTICAL_SEARCHING_CODE_VALUE_SEARCH, searchingCode.getCodeValue());
                    solrDocForItem.addField(STATISTICAL_SEARCHING_CODE_VALUE_DISPLAY, searchingCode.getCodeValue());
                    solrDocForItem.addField(STATISTICAL_SEARCHING_FULL_VALUE_SEARCH, searchingCode.getFullValue());
                    solrDocForItem.addField(STATISTICAL_SEARCHING_FULL_VALUE_DISPLAY, searchingCode.getFullValue());
                }
            }
            solrDocForItem.addField(ITEM_IDENTIFIER_DISPLAY, item.getItemIdentifier());
            solrDocForItem.addField(BARCODE_ARSL_DISPLAY, item.getBarcodeARSL());
            solrDocForItem.addField(COPY_NUMBER_DISPLAY, item.getCopyNumber());
            solrDocForItem.addField(COPY_NUMBER_LABEL_DISPLAY, item.getCopyNumberLabel());
            solrDocForItem.addField(PURCHASE_ORDER_LINE_ITEM_IDENTIFIER_DISPLAY, item.getPurchaseOrderLineItemIdentifier());
            solrDocForItem.addField(VENDOR_LINE_ITEM_IDENTIFIER_DISPLAY, item.getVendorLineItemIdentifier());
            solrDocForItem.addField(COPY_NUMBER_LABEL_DISPLAY, item.getCopyNumberLabel());
            solrDocForItem.addField(VOLUME_NUMBER_LABEL_DISPLAY, item.getVolumeNumberLabel());
            solrDocForItem.addField(VOLUME_NUMBER_DISPLAY, item.getVolumeNumber());
            solrDocForItem.addField(ENUMERATION_DISPLAY, item.getEnumeration());
            solrDocForItem.addField(CHRONOLOGY_DISPLAY, item.getChronology());


            solrDocForItem.addField(UNIQUE_ID, item.getItemIdentifier());
            solrDocForItem.addField(INSTANCE_IDENTIFIER, oleInstance.getInstanceIdentifier());
            if (oleInstance.getResourceIdentifier() != null && oleInstance.getResourceIdentifier().size() > 0) {
                for (String rid : oleInstance.getResourceIdentifier()) {
                    solrDocForItem.addField(BIB_IDENTIFIER, rid);
                }
            }
            if (oleInstance.getOleHoldings() != null) {
                if (oleInstance.getOleHoldings().getHoldingsIdentifier() != null && oleInstance.getOleHoldings().getHoldingsIdentifier().length() > 0) {
                    solrDocForItem.addField(HOLDINGS_IDENTIFIER, oleInstance.getOleHoldings().getHoldingsIdentifier());
                }
            }
            solrInputDocuments.add(solrDocForItem);
        }
    }

    private void buildLocationNameAndLocationLevel(Location location, StringBuffer locationName, StringBuffer locationLevel) {
        locationName = locationName.append(location.getLocationLevel().getName());
        locationLevel = locationLevel.append(location.getLocationLevel().getLevel());

        if (location.getLocationLevel().getLocationLevel() != null) {
            locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getName());
            locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLevel());

            if (location.getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getName());
                locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLevel());

                if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                    locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName());
                    locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLevel());

                    if (location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel() != null) {
                        locationName = locationName.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getName());
                        locationLevel = locationLevel.append("/").append(location.getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLocationLevel().getLevel());
                    }
                }
            }
        }
    }

    public void buildSolrInputDocumentsForinstance(Instance oleInstance, List<SolrInputDocument> solrInputDocuments) {
        SolrInputDocument solrDocForInstance = new SolrInputDocument();
        solrDocForInstance.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrDocForInstance.addField(DOC_TYPE, DocType.INSTANCE.getCode());
        solrDocForInstance.addField(DOC_FORMAT, DocFormat.OLEML.getCode());

        for (FormerIdentifier rid : oleInstance.getFormerResourceIdentifier()) {
            Identifier identifier = rid.getIdentifier();
            solrDocForInstance.addField(FORMER_RESOURCE_IDENTIFIER_SOURCE_SEARCH, identifier.getSource());
            solrDocForInstance.addField(FORMER_RESOURCE_IDENTIFIER_SOURCE_DISPLAY, identifier.getSource());
        }
        for (String rid : oleInstance.getResourceIdentifier()) {
            WorkBibMarcDocBuilder marcDocBuilder = new WorkBibMarcDocBuilder();
            marcDocBuilder.addInstIdToBib(rid, oleInstance.getInstanceIdentifier(), solrInputDocuments);
            solrDocForInstance.addField(BIB_IDENTIFIER, rid);
        }
        solrDocForInstance.addField(HOLDINGS_IDENTIFIER, oleInstance.getOleHoldings().getHoldingsIdentifier());
        solrDocForInstance.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(oleInstance.getInstanceIdentifier()));
        solrDocForInstance.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(oleInstance.getInstanceIdentifier()));
        if (oleInstance.getOleHoldings().getCopyNumber() != null) {
            solrDocForInstance.addField(COPY_NUMBER_SEARCH, oleInstance.getOleHoldings().getCopyNumber());
            solrDocForInstance.addField(COPY_NUMBER_LABEL_SEARCH, oleInstance.getOleHoldings().getCopyNumber());
            solrDocForInstance.addField(COPY_NUMBER_DISPLAY, oleInstance.getOleHoldings().getCopyNumber());
            solrDocForInstance.addField(COPY_NUMBER_LABEL_DISPLAY, oleInstance.getOleHoldings().getCopyNumber());
        }
        solrDocForInstance.addField(ID, oleInstance.getInstanceIdentifier());
        solrDocForInstance.addField(UNIQUE_ID, oleInstance.getInstanceIdentifier());
        for (Item oleItem : oleInstance.getItems().getItem()) {
            solrDocForInstance.addField(ITEM_IDENTIFIER, oleItem.getItemIdentifier());
        }
        solrInputDocuments.add(solrDocForInstance);
    }


    protected void buildSolrInputDocumentsForInstanceByJXPath(Instance oleInstance, List<SolrInputDocument> solrDocs) {
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrDoc.addField(DOC_TYPE, DocType.INSTANCE.getCode());
        solrDoc.addField(DOC_FORMAT, DocFormat.OLEML.getCode());
        solrDoc.addField(ID, oleInstance.getInstanceIdentifier());
        solrDoc.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(oleInstance.getInstanceIdentifier()));
        solrDoc.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(oleInstance.getInstanceIdentifier()));
        solrDoc.addField(UNIQUE_ID, oleInstance.getInstanceIdentifier());
        JXPathContext instance = JXPathContext.newContext(oleInstance);
        for (Field field : oleMLInstanceMetaData.getFields())
            if (field.get("xpath") != null && field.get("xpath").trim().length() != 0) {
                Iterator values = instance.iterate(field.get("xpath"));
                boolean hasValues = false;
                while (values.hasNext()) {
                    hasValues = true;
                    solrDoc.addField(field.getName(), values.next());
                }
                if (!hasValues)
                    solrDoc.addField(field.getName(), null);
            }
        for (String rid : oleInstance.getResourceIdentifier()) {
            solrDoc.addField(BIB_IDENTIFIER, rid);
        }
        solrDoc.addField(HOLDINGS_IDENTIFIER, oleInstance.getOleHoldings().getHoldingsIdentifier());
        List<Item> oleItemsList = oleInstance.getItems().getItem();
        for (Item oleItem : oleItemsList)
            solrDoc.addField(ITEM_IDENTIFIER, oleItem.getItemIdentifier());
        solrDocs.add(solrDoc);
    }


    protected void buildSolrInputDocumentsForHoldingByJXPath(Instance instance, List<SolrInputDocument> solrDocs) {
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrDoc.addField(DOC_TYPE, DocType.HOLDINGS.getCode());
        solrDoc.addField(DOC_FORMAT, DocFormat.OLEML.getCode());
        solrDoc.addField(ID, instance.getOleHoldings().getHoldingsIdentifier());
        solrDoc.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(instance.getOleHoldings().getHoldingsIdentifier()));
        solrDoc.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(instance.getOleHoldings().getHoldingsIdentifier()));
        solrDoc.addField(UNIQUE_ID, instance.getOleHoldings().getHoldingsIdentifier());

        JXPathContext holding = JXPathContext.newContext(instance.getOleHoldings());
        for (Field field : oleMLHoldingMetaData.getFields())
            if (field.get("xpath") != null && field.get("xpath").trim().length() != 0) {
                Iterator values = holding.iterate(field.get("xpath"));
                boolean hasValues = false;
                while (values.hasNext()) {
                    hasValues = true;
                    solrDoc.addField(field.getName(), values.next());
                }
                if (!hasValues)
                    solrDoc.addField(field.getName(), null);
            }

        for (String rid : instance.getResourceIdentifier())
            solrDoc.addField(BIB_IDENTIFIER, rid);
        solrDoc.addField(INSTANCE_IDENTIFIER, instance.getInstanceIdentifier());
        List<Item> oleItemsList = instance.getItems().getItem();
        for (Item oleItem : oleItemsList) {
            solrDoc.addField(ITEM_IDENTIFIER, oleItem.getItemIdentifier());
        }
        solrDocs.add(solrDoc);
    }


    protected void buildSolrInputDocumentsForItemsByJXPath(Instance instance, List<SolrInputDocument> solrDocs) {
        for (Item item : instance.getItems().getItem()) {
            SolrInputDocument solrDoc = new SolrInputDocument();
            solrDoc.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
            solrDoc.addField(DOC_TYPE, DocType.ITEM.getCode());
            solrDoc.addField(DOC_FORMAT, DocFormat.OLEML.getCode());
            solrDoc.addField(ID, item.getItemIdentifier());
            solrDoc.addField(LOCALID_SEARCH, DocumentLocalId.getDocumentId(item.getItemIdentifier()));
            solrDoc.addField(LOCALID_DISPLAY, DocumentLocalId.getDocumentIdDisplay(item.getItemIdentifier()));
            solrDoc.addField(UNIQUE_ID, item.getItemIdentifier());
            JXPathContext itemRec = JXPathContext.newContext(item);
            for (Field field : oleMLItemMetaData.getFields())
                if (field.get("xpath") != null && field.get("xpath").trim().length() != 0) {
                    Iterator values = itemRec.iterate(field.get("xpath"));
                    boolean hasValues = false;
                    while (values.hasNext()) {
                        hasValues = true;
                        solrDoc.addField(field.getName(), values.next());
                    }
                    if (!hasValues)
                        solrDoc.addField(field.getName(), null);
                }
            solrDoc.addField(ITEM_TYPE_SEARCH, item.getItemType());
            solrDoc.addField(INSTANCE_IDENTIFIER, instance.getInstanceIdentifier());
            for (String rid : instance.getResourceIdentifier()) {
                solrDoc.addField(BIB_IDENTIFIER, rid);
            }
            solrDoc.addField(HOLDINGS_IDENTIFIER, instance.getOleHoldings().getHoldingsIdentifier());
            solrDocs.add(solrDoc);
        }
    }

    public void buildSolrInputDocuments(RequestDocument linkedRequestDocument,
                                        List<SolrInputDocument> solrInputDocuments) {
        InstanceCollection instanceCollection = (InstanceCollection) linkedRequestDocument.getContent()
                .getContentObject();
        if (instanceCollection != null) {
            for (Instance instance : instanceCollection.getInstance()) {
                buildSolrInputDocumentsForinstance(instance, solrInputDocuments);
                buildSolrInputDocumentsForHolding(instance, solrInputDocuments, linkedRequestDocument);
                buildSolrInputDocumentsForItems(instance, solrInputDocuments, linkedRequestDocument);
            }
        }
    }

    public void buildSolrInputDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocuments) {
        if (requestDocument != null && requestDocument.getOperation() != null && "checkIn"
                .equalsIgnoreCase(requestDocument.getOperation())) {
            updateRecordContentInSolr(requestDocument, solrInputDocuments);
        } else if (requestDocument != null) {
            buildSolrInputDocuments(requestDocument, solrInputDocuments);
        }
    }

    public void updateRecordContentInSolr(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocuments) {
        IndexerService indexerService = getIndexerService(requestDocument);
        SolrDocument solrDocument = new SolrDocument();
        List<SolrDocument> solrDocumentList = new ArrayList<SolrDocument>();
        if (requestDocument.getId() != null && requestDocument.getId().length() > 0) {
            solrDocumentList = indexerService.getSolrDocumentBySolrId(requestDocument.getUuid());
            if (solrDocumentList != null && solrDocumentList.size() > 0) {
                solrDocument = solrDocumentList.get(0);
            }
            if (requestDocument.getType().equalsIgnoreCase(DocType.INSTANCE.getCode())) {
                updateSolrDocumentsWithLinkedDocs(requestDocument, solrInputDocuments, solrDocument);
            } else if (requestDocument.getType().equalsIgnoreCase(DocType.ITEM.getCode())
                    || requestDocument.getType().equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
                updateSolrDocument(requestDocument, solrInputDocuments, solrDocument);
            }
        }
    }

    public void updateSolrDocument(RequestDocument requestDocument, List<SolrInputDocument> solrInputDocuments,
                                   SolrDocument solrDocument) {
        if (requestDocument.getType().equalsIgnoreCase(DocType.ITEM.getCode())) {
            String itemXml = requestDocument.getContent().getContent();
            Item itemContent = workItemOlemlRecordProcessor.fromXML(itemXml);
            List<Item> oleItemList = new ArrayList<Item>();
            oleItemList.add(itemContent);
            Instance oleInstance = new Instance();
            Items items = new Items();
            items.setItem(oleItemList);
            oleInstance.setItems(items);
            if (solrDocument != null && solrDocument.size() > 0) {
                if (solrDocument.getFieldValue(INSTANCE_IDENTIFIER) == null && solrDocument.getFieldValue(DOC_TYPE)
                        .equals(DocType.INSTANCE
                                .getCode())) {
                    if (solrDocument.getFieldValue(ID) != null && solrDocument.getFieldValue(ID) instanceof List) {
                        List<String> idList = (List<String>) solrDocument.getFieldValue(ID);
                        for (String id : idList) {
                            oleInstance.setInstanceIdentifier(id);
                        }
                    } else if (solrDocument.getFieldValue(ID) != null && solrDocument
                            .getFieldValue(ID) instanceof String) {
                        oleInstance.setInstanceIdentifier((String) solrDocument.getFieldValue(ID));
                    }
                } else {
                    if (solrDocument.getFieldValue(INSTANCE_IDENTIFIER) != null && solrDocument
                            .getFieldValue(INSTANCE_IDENTIFIER) instanceof List) {
                        List<String> instanceIdList = (List<String>) solrDocument.getFieldValue(INSTANCE_IDENTIFIER);
                        for (String instanceId : instanceIdList) {
                            oleInstance.setInstanceIdentifier(instanceId);
                        }
                    } else if (solrDocument.getFieldValue(INSTANCE_IDENTIFIER) != null && solrDocument
                            .getFieldValue(INSTANCE_IDENTIFIER) instanceof String) {
                        oleInstance.setInstanceIdentifier((String) solrDocument.getFieldValue(INSTANCE_IDENTIFIER));
                    }
                }
                itemContent.setItemIdentifier(requestDocument.getId());
                OleHoldings oleHolding = new OleHoldings();
                if (solrDocument.getFieldValue(HOLDINGS_IDENTIFIER) != null && solrDocument
                        .getFieldValue(HOLDINGS_IDENTIFIER) instanceof List) {
                    List<String> holdingIdList = (List<String>) solrDocument.getFieldValue(HOLDINGS_IDENTIFIER);
                    for (String holdingId : holdingIdList) {
                        oleHolding.setHoldingsIdentifier(holdingId);
                    }
                } else if (solrDocument.getFieldValue(HOLDINGS_IDENTIFIER) != null && solrDocument
                        .getFieldValue(HOLDINGS_IDENTIFIER) instanceof String) {
                    oleHolding.setHoldingsIdentifier((String) solrDocument.getFieldValue(HOLDINGS_IDENTIFIER));
                }
                oleInstance.setOleHoldings(oleHolding);
                List<String> reId = new ArrayList<String>();
                if (solrDocument.getFieldValue(BIB_IDENTIFIER) != null && solrDocument
                        .getFieldValue(BIB_IDENTIFIER) instanceof List) {
                    List<String> idList = (List<String>) solrDocument.getFieldValue(BIB_IDENTIFIER);
                    for (String bibId : idList) {
                        reId.add(bibId);
                    }
                } else if (solrDocument.getFieldValue(BIB_IDENTIFIER) != null && solrDocument
                        .getFieldValue(BIB_IDENTIFIER) instanceof String) {
                    reId.add((String) solrDocument.getFieldValue(BIB_IDENTIFIER));
                }
                oleInstance.setResourceIdentifier(reId);
            }
            buildSolrInputDocumentsForItems(oleInstance, solrInputDocuments, requestDocument);
        } else if (requestDocument.getType().equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
            String holdingsXml = requestDocument.getContent().getContent();
            HoldingOlemlRecordProcessor recordProcessor = new HoldingOlemlRecordProcessor();
            OleHoldings holdingContent = recordProcessor.fromXML(holdingsXml);
            Instance oleInstance = new Instance();
            oleInstance.setOleHoldings(holdingContent);
            if (solrDocument != null) {
                if (solrDocument.getFieldValue(INSTANCE_IDENTIFIER) != null && solrDocument
                        .getFieldValue(INSTANCE_IDENTIFIER) instanceof List) {
                    List<String> instanceIdList = (List<String>) solrDocument.getFieldValue(INSTANCE_IDENTIFIER);
                    for (String instanceId : instanceIdList) {
                        oleInstance.setInstanceIdentifier(instanceId);
                    }
                } else if (solrDocument.getFieldValue(INSTANCE_IDENTIFIER) != null && solrDocument
                        .getFieldValue(INSTANCE_IDENTIFIER) instanceof String) {
                    oleInstance.setInstanceIdentifier((String) solrDocument.getFieldValue(INSTANCE_IDENTIFIER));
                }
                holdingContent.setHoldingsIdentifier(requestDocument.getId());
                List<Item> oleItemList = new ArrayList<Item>();
                Object itemIdentifier = solrDocument.getFieldValue(ITEM_IDENTIFIER);
                if (itemIdentifier instanceof List) {
                    for (String itemId : (List<String>) itemIdentifier) {
                        Item oleItem = new Item();
                        oleItem.setItemIdentifier(itemId);
                        oleItemList.add(oleItem);
                    }
                } else if (itemIdentifier instanceof String) {
                    String itemId = (String) itemIdentifier;
                    Item oleItem = new Item();
                    oleItem.setItemIdentifier(itemId);
                    oleItemList.add(oleItem);
                }
                Items items = new Items();
                items.setItem(oleItemList);
                oleInstance.setItems(items);
                List<String> reId = new ArrayList<String>();
                if (solrDocument.getFieldValue(BIB_IDENTIFIER) != null && solrDocument
                        .getFieldValue(BIB_IDENTIFIER) instanceof List) {
                    List<String> idList = (List<String>) solrDocument.getFieldValue(BIB_IDENTIFIER);
                    for (String bibId : idList) {
                        reId.add(bibId);
                    }
                } else if (solrDocument.getFieldValue(BIB_IDENTIFIER) != null && solrDocument
                        .getFieldValue(BIB_IDENTIFIER) instanceof String) {
                    reId.add((String) solrDocument.getFieldValue(BIB_IDENTIFIER));
                }
                oleInstance.setResourceIdentifier(reId);
            }
            buildSolrInputDocumentsForHolding(oleInstance, solrInputDocuments, requestDocument);
            for (RequestDocument linkReqDoc : requestDocument.getLinkedRequestDocuments()) {
                if (linkReqDoc.getType().equalsIgnoreCase(DocType.ITEM.getDescription())) {
                    updateRecordContentInSolr(linkReqDoc, solrInputDocuments);
                }
            }
        }
    }

    public void updateSolrDocumentsWithLinkedDocs(RequestDocument requestDocument,
                                                  List<SolrInputDocument> solrInputDocuments,
                                                  SolrDocument solrDocument) {

        SolrInputDocument solrInputDocument = new SolrInputDocument();
        if (requestDocument.getType().equalsIgnoreCase(DocType.INSTANCE.getCode())
                && requestDocument.getContent().getContent() == null) {
            String holdingId = null;
            WorkBibMarcDocBuilder workBibMarcDocBuilder = new WorkBibMarcDocBuilder();
            for (RequestDocument linkedItemRequestDoc : requestDocument.getLinkedRequestDocuments()) {
                if (linkedItemRequestDoc.getType().equalsIgnoreCase(DocType.ITEM.getCode())
                        && linkedItemRequestDoc.getContent().getContent() != null) {
                    workBibMarcDocBuilder.buildSolrInputDocFromSolrDoc(solrDocument, solrInputDocument);
                    solrInputDocument.addField(ITEM_IDENTIFIER, linkedItemRequestDoc.getId());
                    if (solrDocument.getFieldValue(HOLDINGS_IDENTIFIER) != null && solrDocument
                            .getFieldValue(HOLDINGS_IDENTIFIER) instanceof List) {
                        List<String> holdingIdList = (List<String>) solrDocument.getFieldValue(HOLDINGS_IDENTIFIER);
                        for (String holId : holdingIdList) {
                            holdingId = holId;
                        }
                    } else if (solrDocument.getFieldValue(HOLDINGS_IDENTIFIER) != null && solrDocument
                            .getFieldValue(HOLDINGS_IDENTIFIER) instanceof String) {
                        holdingId = (String) solrDocument.getFieldValue(HOLDINGS_IDENTIFIER);
                    }
                    solrInputDocuments.add(solrInputDocument);
                    IndexerService indexerService = getIndexerService(requestDocument);
                    List<SolrDocument> solrHoldingDocList = indexerService.getSolrDocumentBySolrId(holdingId);
                    SolrDocument solrHoldingDocument = solrHoldingDocList.get(0);
                    solrInputDocument = new SolrInputDocument();
                    workBibMarcDocBuilder.buildSolrInputDocFromSolrDoc(solrHoldingDocument, solrInputDocument);
                    solrInputDocument.addField(ITEM_IDENTIFIER, linkedItemRequestDoc.getId());
                    solrInputDocuments.add(solrInputDocument);
                    updateSolrDocument(linkedItemRequestDoc, solrInputDocuments, solrDocument);
                    requestDocument = linkedItemRequestDoc;
                }
            }
        }
        solrInputDocuments.add(solrInputDocument);
    }


    protected void buildSolrInputDocumentsForInstanceByJXPathTest(JXPathContext instance, Instance oleInstance,
                                                                  List<SolrInputDocument> solrDocs) {
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrDoc.addField(DOC_TYPE, DocType.INSTANCE.getCode());
        solrDoc.addField(DOC_FORMAT, DocFormat.OLEML.getCode());
        solrDoc.addField(ID, oleInstance.getInstanceIdentifier());
        solrDoc.addField(UNIQUE_ID, oleInstance.getInstanceIdentifier());
        //JXPathContext instance = JXPathContext.newContext(oleInstance);
        for (Field field : oleMLInstanceMetaData.getFields()) {
            if (field.get("xpath") != null && field.get("xpath").trim().length() != 0) {
                Iterator values = instance.iterate(field.get("xpath"));
                boolean hasValues = false;
                while (values.hasNext()) {
                    hasValues = true;
                    System.out.println("Instance field.getName() " + field.getName());
                    solrDoc.addField(field.getName(), values.next());
                    System.out.println("Instance getField " + solrDoc.getField(field.getName()));
                }
                if (!hasValues) {
                    System.out.println("Instance hasValues field.getName() " + field.getName());
                    solrDoc.addField(field.getName(), null);
                    System.out.println("Instance hasValues getField " + solrDoc.getField(field.getName()));
                }
            }
        }
        for (String rid : oleInstance.getResourceIdentifier()) {
            solrDoc.addField(BIB_IDENTIFIER, rid);
        }
        solrDoc.addField(HOLDINGS_IDENTIFIER, oleInstance.getOleHoldings().getHoldingsIdentifier());
        List<Item> oleItemsList = oleInstance.getItems().getItem();
        for (Item oleItem : oleItemsList) {
            solrDoc.addField(ITEM_IDENTIFIER, oleItem.getItemIdentifier());
        }
        solrDocs.add(solrDoc);
    }

    protected void buildSolrInputDocumentsForHoldingByJXPathTest(JXPathContext holding, Instance instance,
                                                                 List<SolrInputDocument> solrDocs) {
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
        solrDoc.addField(DOC_TYPE, DocType.HOLDINGS.getCode());
        solrDoc.addField(DOC_FORMAT, DocFormat.OLEML.getCode());
        solrDoc.addField(ID, instance.getOleHoldings().getHoldingsIdentifier());
        solrDoc.addField(UNIQUE_ID, instance.getOleHoldings().getHoldingsIdentifier());

        //  JXPathContext holding = JXPathContext.newContext(instance.getOleHoldings());
        for (Field field : oleMLHoldingMetaData.getFields()) {
            if (field.get("xpath") != null && field.get("xpath").trim().length() != 0) {
                Iterator values = holding.iterate(field.get("xpath"));
                boolean hasValues = false;
                while (values.hasNext()) {
                    System.out.println("Holding field.getName() " + field.getName());
                    hasValues = true;
                    solrDoc.addField(field.getName(), values.next());
                    System.out.println("Holding getField " + solrDoc.getField(field.getName()));
                }
                if (!hasValues) {
                    System.out.println("Holding hasValues field.getName() " + field.getName());
                    solrDoc.addField(field.getName(), null);
                    System.out.println("Holding hasValues getField " + solrDoc.getField(field.getName()));
                }
            }
        }

        for (String rid : instance.getResourceIdentifier()) {
            solrDoc.addField(BIB_IDENTIFIER, rid);
        }
        solrDoc.addField(INSTANCE_IDENTIFIER, instance.getInstanceIdentifier());
        List<Item> oleItemsList = instance.getItems().getItem();

        for (Item oleItem : oleItemsList) {
            solrDoc.addField(ITEM_IDENTIFIER, oleItem.getItemIdentifier());
        }
        solrDocs.add(solrDoc);
    }

    protected void buildSolrInputDocumentsForItemsByJXPathTest(JXPathContext itemRec, Instance instance,
                                                               List<SolrInputDocument> solrDocs) {
        for (Item item : instance.getItems().getItem()) {
            SolrInputDocument solrDoc = new SolrInputDocument();
            solrDoc.addField(DOC_CATEGORY, DocCategory.WORK.getCode());
            solrDoc.addField(DOC_TYPE, DocType.ITEM.getCode());
            solrDoc.addField(DOC_FORMAT, DocFormat.OLEML.getCode());
            solrDoc.addField(ID, item.getItemIdentifier());
            solrDoc.addField(UNIQUE_ID, item.getItemIdentifier());
            // JXPathContext itemRec = JXPathContext.newContext(item);
            for (Field field : oleMLItemMetaData.getFields()) {
                if (field.get("xpath") != null && field.get("xpath").trim().length() != 0) {
                    Iterator values = itemRec.iterate(field.get("xpath"));
                    boolean hasValues = false;
                    while (values.hasNext()) {
                        System.out.println("Items field.getName() " + field.getName());
                        hasValues = true;
                        solrDoc.addField(field.getName(), values.next());
                        System.out.println("Items getField " + solrDoc.getField(field.getName()));
                    }
                    if (!hasValues) {
                        System.out.println("Items hasValues field.getName() " + field.getName());
                        solrDoc.addField(field.getName(), null);
                        System.out.println("Items hasValues getField " + solrDoc.getField(field.getName()));
                    }
                }
            }
            solrDoc.addField(ITEM_TYPE_SEARCH, item.getItemType());
            solrDoc.addField(INSTANCE_IDENTIFIER, instance.getInstanceIdentifier());
            for (String rid : instance.getResourceIdentifier()) {
                solrDoc.addField(BIB_IDENTIFIER, rid);
            }
            solrDoc.addField(HOLDINGS_IDENTIFIER, instance.getOleHoldings().getHoldingsIdentifier());
            solrDocs.add(solrDoc);
        }
    }

    protected String buildSortableCallNumber(String callNumber, String codeValue) throws Exception {
        String shelvingOrder = "";
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.kuali.ole.utility.callnumber.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                shelvingOrder = callNumberObj.getSortableKey(callNumber);
                //shelvingOrder = shelvingOrder.replaceAll(" ", "_");
            }
        }
        return shelvingOrder;
    }

    protected boolean validateCallNumber(String callNumber, String codeValue) throws Exception {
        boolean isValid = false;
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.kuali.ole.utility.callnumber.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                isValid = callNumberObj.isValid(callNumber);
            }
        }
        return isValid;
    }

    public String getNormalizedEnumeration(String enumation) {
        if (enumation.contains(".")) {
            StringBuffer resultBuf = new StringBuffer();
            String[] splitEnum = enumation.split("\\.");
            if (splitEnum.length > 1) {
                String enumerationNo = splitEnum[1];
                String enumBufAfterDot = null;
                String enumBufAfterSpecial = null;
                String normalizedEnum = null;
                if (enumerationNo != null && (enumerationNo.trim().length() > 0)) {
                    int pos = 0;
                    boolean numCheck = false;
                    for (int i = 0; i < enumerationNo.length(); i++) {
                        char c = enumerationNo.charAt(i);
                        String convertedEnum = String.valueOf(c);
                        if (convertedEnum.matches("[0-9]")) {
                            if (Character.isDigit(c)) {
                                pos = i;
                                numCheck = true;
                            } else {
                                break;
                            }
                        } else {
                            if (pos == 0 && numCheck == false) {
                                return enumation;
                            }
                            break;
                        }
                    }
                    enumBufAfterDot = enumerationNo.substring(0, pos + 1);
                    normalizedEnum = normalizeFloatForEnumeration(enumBufAfterDot, 5);
                    enumBufAfterSpecial = enumerationNo.substring(pos + 1);
                    splitEnum[1] = normalizedEnum + enumBufAfterSpecial;
                }
                for (int j = 0; j < splitEnum.length; j++) {
                    resultBuf.append(splitEnum[j]);
                    resultBuf.append(".");
                }

                return resultBuf.substring(0, resultBuf.length() - 1).toString();
            } else {
                return enumation;
            }
        } else {
            return enumation;
        }
    }


    public String normalizeFloatForEnumeration(String floatStr, int digitsB4) {
        String replacString = floatStr.replaceAll("[^a-zA-Z0-9]+", "");
        double value = Double.valueOf(replacString).doubleValue();
        String formatStr = getFormatString(digitsB4);
        DecimalFormat normFormat = new DecimalFormat(formatStr);
        String norm = normFormat.format(value);
        if (norm.endsWith("."))
            norm = norm.substring(0, norm.length() - 1);
        return norm;
    }

    private String getFormatString(int numDigits) {
        StringBuilder b4 = new StringBuilder();
        if (numDigits < 0)
            b4.append("############");
        else if (numDigits > 0) {
            for (int i = 0; i < numDigits; i++) {
                b4.append('0');
            }
        }
        return b4.toString();
    }
}