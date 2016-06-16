package org.kuali.ole.batch.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.helper.EInstanceMappingHelper;
import org.kuali.ole.batch.helper.InstanceMappingHelper;
import org.kuali.ole.batch.helper.OLEBatchProcessDataHelper;
import org.kuali.ole.batch.service.ExportDataService;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.docstore.engine.service.DocstoreServiceImpl;

import java.util.*;

import static org.kuali.ole.OLEConstants.OLEBatchProcess.*;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 7/5/13
 * Time: 2:38 PM
 */
public class ExportDataServiceImpl implements ExportDataService {

    private static final Logger LOG = Logger.getLogger(ExportDataServiceImpl.class);
    private List<String> bibIdList = new ArrayList<>();
    private DocstoreServiceImpl docstoreService ;

    public DocstoreServiceImpl getDocstoreService() {
        if (null == docstoreService) {
            docstoreService= new DocstoreServiceImpl();
        }
        return docstoreService;
    }


    public ExportDataServiceImpl() {
        bibIdList.clear();
    }

    /**
     * public method to retrieves the list of bib / instance records for the given profile
     * <p/>
     * Returns object array - array size is  4
     * Following positions of the array has the details of the data it contains
     * 0 - has the count of bib records
     * 1 - List with xml data of the bib records , if the export type is marc xml the list will have only one item will
     * all the bib records in a single String object, if the export type is marc the list will have each record as an item
     * 2 - Errors generated
     * 3 -  Error count
     *
     * @param searchResultList
     * @param profile
     * @return
     * @throws Exception
     */
    public Object[] getExportDataBySolr(List<SearchResult> searchResultList, OLEBatchProcessProfileBo profile) throws Exception {
        List<String> bibMarcRecordList = new ArrayList<String>();
        List<BibMarcRecord> bibRecords = new ArrayList<BibMarcRecord>();
        StringBuilder errBuilder = new StringBuilder();
        int errCnt = 0;
        for (SearchResult searchResult : searchResultList) {
            try {
                for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                    if ( searchResultField.getFieldName().equalsIgnoreCase("bibIdentifier")) {
                        String bibId = searchResultField.getFieldValue();
                        if (bibIdList.contains(bibId)) continue;
                        bibIdList.add(bibId);
                        try {
                            if (StringUtils.isNotEmpty(bibId)) {
                                //BibliographicRecord bibliographicRecord = helperService.getBibliographicRecord(bibId);
                                BibMarcRecord bibMarcRecord = getBibMarcRecord(bibId);
                                //Intance mapping

                                if (!profile.getOleBatchProcessProfileMappingOptionsList().isEmpty()
                                        && StringUtils.isNotEmpty(profile.getDataToExport()) && (profile.getDataToExport().equalsIgnoreCase(OLEBatchProcess.EXPORT_BIB_AND_INSTANCE) || profile.getDataToExport().equalsIgnoreCase(OLEBatchProcess.EXPORT_BIB_INSTANCE_AND_EINSTANCE))) {
                                    try {
                                        getInstanceDetails(bibMarcRecord, profile, errBuilder);
                                        LOG.debug("Instance data mapping completed");
                                    } catch (Exception ex) {
                                        LOG.error("Instance data mapping Error for Bib record id::" + bibMarcRecord.getRecordId(), ex);
                                        buildError(errBuilder, ERR_BIB, bibMarcRecord.getRecordId(), "", "",
                                                ERR_CAUSE, ex.getMessage(), TIME_STAMP, new Date().toString());
                                    }
                                }
                                //Marc record rename
                                if (!profile.getOleBatchProcessProfileRenameFieldsList().isEmpty()) {
                                    try {
                                        OLEBatchProcessDataHelper.getInstance().renameMarcFieldsSubFields(profile, bibMarcRecord);
                                        LOG.debug("Rename of bib marc records completed");
                                    } catch (Exception ex) {
                                        LOG.error("Marc Record Rename error for Bib record id::" + bibMarcRecord.getRecordId(), ex);
                                        buildError(errBuilder, ERR_BIB, bibMarcRecord.getRecordId(), ERR_CAUSE, ex.getMessage(), " ::At:: ", "renameMarcFieldsSubFields", TIME_STAMP, new Date().toString());
                                    }
                                }
                                //Marc record delete
                                if (!profile.getOleBatchProcessProfileDeleteFieldsList().isEmpty()) {
                                    try {
                                        OLEBatchProcessDataHelper.getInstance().deleteFieldsSubfields(profile, bibMarcRecord);
                                        LOG.debug("Deletion of bib marc records completed");
                                    } catch (Exception ex) {
                                        LOG.error("Marc record delete Error for Bib record id::" + bibMarcRecord.getRecordId(), ex);
                                        buildError(errBuilder, ERR_BIB, bibMarcRecord.getRecordId(), ERR_CAUSE, ex.getMessage(), " ::At:: ", "deleteFieldsSubfields", TIME_STAMP, new Date().toString());
                                    }
                                }
                                bibRecords.add(bibMarcRecord);
                            }
                        } catch (Exception ex) {
                            LOG.error("Error while getting bib information for record id::" + bibId, ex);
                            buildError(errBuilder, ERR_BIB, bibId, ERR_CAUSE, ex.getMessage(), " ::At:: ", "getBibliographicRecord", TIME_STAMP, new Date().toString());
                            errCnt++;
                        }
                    }
                }
            } catch (Exception ex) {
                LOG.error("Error while Exporting bibs :: No of bibs processed while error occured :: " + bibIdList.size(), ex);

                if (!bibIdList.isEmpty()) {
                    LOG.error("Bib record where error occured: " + bibIdList.get(bibIdList.size() - 1), ex);
                    buildError(errBuilder, ERR_BIB, bibIdList.get(bibIdList.size() - 1), ERR_CAUSE, ex.getMessage(), " ::At:: ", "getBibliographicRecord-P", TIME_STAMP, new Date().toString());
                    errCnt++;
                }
            }
        }//End loop of bib ids
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor(errBuilder);
        try {
            getResult(bibMarcRecordProcessor, bibRecords, bibMarcRecordList);
        } catch (Exception ex) {
            LOG.error("Error while Exporting bibs :: No of bibs processed while error occured :: " + bibIdList.size(), ex);
            buildError(errBuilder, ERR_CAUSE, "Error while getting bib data::" + ex.getMessage(), TIME_STAMP, new Date().toString());
        }
        return new Object[]{String.valueOf(bibMarcRecordProcessor.getSuccessCnt()), bibMarcRecordList, errBuilder.toString(), String.valueOf((errCnt + bibMarcRecordProcessor.getErrCnt()))};

    }


    /**
     * Method retrives the list of instances record for the bib record with the given instanceIdentifier list
     *
     * @param bibMarcRecord
     * @param profile
     */
    private void getInstanceDetails(BibMarcRecord bibMarcRecord, OLEBatchProcessProfileBo profile, StringBuilder errBuilder) throws Exception {
        List<DataField> dataFields = bibMarcRecord.getDataFields();
        //for (String bibIdentifier : holdingsIdentifierList) {
        try {
            // if (StringUtils.isBlank(bibIdentifier)) continue;
            List<DataField> holdingsItemDataField = Collections.emptyList();
            BibTree bibTree = getDocstoreService().retrieveBibTree(bibMarcRecord.getRecordId());
            if (bibTree != null && bibTree.getHoldingsTrees() != null && bibTree.getHoldingsTrees().size() > 0) {
                for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                    boolean isStaffOnly = false;
                    if (profile.getExportScope().equalsIgnoreCase(OLEBatchProcess.INCREMENTAL_EXPORT_EX_STAFF)) {
                        if (holdingsTree.getHoldings().isStaffOnly()) {
                            isStaffOnly = true;
                        }
                    }

                    if (!isStaffOnly) {
                        if (holdingsTree.getHoldings().getHoldingsType().equalsIgnoreCase("print")) {
                            holdingsItemDataField = new InstanceMappingHelper().generateDataFieldForHolding(holdingsTree, profile, errBuilder);
                        } else {
                            holdingsItemDataField = new EInstanceMappingHelper().generateDataFieldForEHolding(holdingsTree, profile, errBuilder);
                        }
                    }
                    dataFields.addAll(holdingsItemDataField);
                    Collections.sort(dataFields);
                }
            }
        } catch (Exception ex) {
            LOG.error("Error while getting instance details for instanceID :: " + bibMarcRecord.getRecordId(), ex);
            errBuilder.append("-----");
            buildError(errBuilder, ERR_INSTANCE, bibMarcRecord.getRecordId(), ERR_CAUSE, ex.getMessage(), " ::At:: ", "getInstanceDetails", TIME_STAMP, new Date().toString());

        }
    }


    private void getResult(BibMarcRecordProcessor bibMarcRecordProcessor, List<BibMarcRecord> bibRecords, List<String> bibMarcRecordList) {
        String bibMarcRecord = bibMarcRecordProcessor.generateXML(bibRecords);
        bibMarcRecordList.add(bibMarcRecord);
    }


    private void buildError(StringBuilder errBuilder, String... errorString) {
        for (String str : errorString) {
            errBuilder.append(str).append(COMMA);
        }
        errBuilder.append(lineSeparator);
    }

    private BibMarcRecord getBibMarcRecord(String bibUUID) throws Exception {
        BibMarcRecord bibMarcRecord = null;
        Bib bib = getDocstoreService().retrieveBib(bibUUID);
        String responseDocStoreData = bib.getContent();
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords marcRecords = bibMarcRecordProcessor.fromXML(responseDocStoreData);
        List<BibMarcRecord> bibMarcRecordList = marcRecords.getRecords();
        Iterator<BibMarcRecord> bibMarcRecordListIterator = bibMarcRecordList.iterator();
        if (bibMarcRecordListIterator.hasNext()) {
            bibMarcRecord = bibMarcRecordListIterator.next();
        }
        return bibMarcRecord;
    }
}
