package org.kuali.ole.batch.helper;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.*;
import org.kuali.ole.batch.impl.BatchProcessBibImportServiceImpl;
import org.kuali.ole.batch.util.BatchBibImportUtil;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreSearchException;
import org.kuali.ole.docstore.common.search.SearchCondition;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.document.OLEPlatformRecordDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 7/1/14
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class BatchBibImportHelper {

    private static final Logger LOG = LoggerFactory.getLogger(BatchBibImportHelper.class);
    private BatchProcessBibImportServiceImpl batchProcessBibImportService = new BatchProcessBibImportServiceImpl();
    private OLEBatchBibImportStatistics bibImportStatistics = null;
    private List<DataField> holdingsDataFields = new ArrayList<>();
    private List<DataField> eHoldingsDataFields = new ArrayList<>();
    private List<DataField> overlayeHoldingsDataFields = new ArrayList<>();
    private List<DataField> itemDataFields = new ArrayList<>();
    private List<OLEBatchProcessProfileDataMappingOptionsBo> dataMapping = new ArrayList<>();
    private DocstoreClientLocator docstoreClientLocator;
    private List<OLEBatchProcessProfileConstantsBo> constantsMapping = new ArrayList<>();
    String holdingsDataFieldNumber = null;
    String itemDataFieldNumber = null;
    private String userName;

    /**
     * @return
     */
    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    /**
     * @param bibMarcRecordList
     * @param profile
     * @param batchBibImportStatistics
     * @param user
     */
    public OLEBatchBibImportDataObjects processBatch(List<BibMarcRecord> bibMarcRecordList, OLEBatchProcessProfileBo profile, OLEBatchBibImportStatistics batchBibImportStatistics, String user) {

        OLEBatchBibImportDataObjects oleBatchBibImportDataObjects = new OLEBatchBibImportDataObjects();
        bibImportStatistics = batchBibImportStatistics;
        userName = user;

        MatchingProfile matchingProfile = profile.getMatchingProfileObj();

        for (BibMarcRecord bibRecord : bibMarcRecordList) {
            processBibMarcRecord(profile, oleBatchBibImportDataObjects, matchingProfile, bibRecord);

        }

        return oleBatchBibImportDataObjects;
    }

    private void processBibMarcRecord(OLEBatchProcessProfileBo profile, OLEBatchBibImportDataObjects oleBatchBibImportDataObjects, MatchingProfile matchingProfile, BibMarcRecord bibRecord) {

        BibTree bibTree = new BibTree();

        String leader = bibRecord.getLeader();
        char unicode = leader.charAt(9);
        if (unicode == 'a') {

            if (BatchBibImportUtil.has245aDataField(bibRecord)) {

                dataMapping = processPriority(bibRecord, profile);
                constantsMapping = profile.getOleBatchProcessProfileConstantsList();
                // Getting Holdings and item Data Fields  for every Bib Record
                getHoldingsItemDataFields(bibRecord, dataMapping, holdingsDataFields, itemDataFields, eHoldingsDataFields);


            if (!matchingProfile.isMatchBibs()) {
                // Do not perform match
                applyBibDataMapping(bibRecord, profile);
                bibTree = buildBibTree(bibRecord, profile);
                setOperationAddToBibTreeList(DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE, bibTree);
            } else {
                try {
                    // Finding Matching for Bib` from Bib match point
                    Bib matchedBib = findMatchingBib(profile, bibRecord);
                    if (matchedBib == null) {
                        // No match  Found
                        if (matchingProfile.isBibNotMatched_addBib()) {
                            applyBibDataMapping(bibRecord, profile);

                            // Add Bib Tree
                            bibTree = buildBibTree(bibRecord, profile);
                            setOperationAddToBibTreeList(DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE, bibTree);
                        } else if (matchingProfile.isBibNotMatched_discardBib()) {
                            // Discard bib if no match found. New bib is set to bib tree with no operation and proceed.
                            Bib bib = new Bib();
                            bib.setMessage(OLEConstants.OLEBatchProcess.NO_MATCH_DISCARD_BIB);
                            bibTree.setBib(bib);
                        }
                        bibImportStatistics.getNonMatchedBibMarc().add(bibRecord);
                    } else {
                        // Match found
                        if (matchingProfile.isBibMatched_addBib()) {
                            applyBibDataMapping(bibRecord, profile);
                            // Add Bib
                            bibTree = buildBibTree(bibRecord, profile);
                            setOperationAddToBibTreeList(DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE, bibTree);
                        } else {
                            // Update Bib
                            if (matchingProfile.isBibMatched_updateBib()) {
                                boolean isBibOverlay = true;
                                // Overlay bib when status is not equal to profile status

                                for (OLEBatchProcessProfileBibStatus oleBatchProcessProfileBibStatus : profile.getOleBatchProcessProfileBibStatusList()) {
                                    if (oleBatchProcessProfileBibStatus.getBatchProcessBibStatus() != null && oleBatchProcessProfileBibStatus.getBatchProcessBibStatus().equals(matchedBib.getStatus())) {
                                        isBibOverlay = false;
                                        break;
                                    }
                                }
                                applyBibDataMappingOverlay(bibRecord, profile);
                                bibTree.setBib(matchedBib);
                                processHoldingsAndItemsForBib(bibRecord, profile, bibTree, oleBatchBibImportDataObjects);
                                if (isBibOverlay) {
                                    overlayBib(bibRecord, matchedBib, profile);
                                }
                                setOperationAddToBibTreeList(DocstoreDocument.OperationType.UPDATE, null, null, bibTree);
                            }

                            // Discarding Bib if match found and processing  holdings and items
                            if (matchingProfile.isBibMatched_discardBib()) {
                                bibTree.setBib(matchedBib);
                                processHoldingsAndItemsForBib(bibRecord, profile, bibTree, oleBatchBibImportDataObjects);

                            }
                        }
                        bibImportStatistics.getMatchedBibMarc().add(bibRecord);
                        bibImportStatistics.getMatchedBibIds().add(matchedBib.getId());
                    }
                } catch (DocstoreException e) {
                    Bib bib = new Bib();
                    bib.setMessage(e.getErrorMessage());
                    bib.setResult(DocstoreDocument.ResultType.FAILURE);
                    bibTree.setBib(bib);
                    LOG.info(e.getErrorMessage());
                }

                }
            } else {
                Bib bib = new Bib();
                bib.setMessage("Bib Record with id " + bibRecord.getRecordId() + " does not have a 245a data field");
                bib.setResult(DocstoreDocument.ResultType.FAILURE);
                bibTree.setBib(bib);
            }
        } else {
            Bib bib = new Bib();
            bib.setMessage("Invalid Leader Field - " + leader);
            bib.setResult(DocstoreDocument.ResultType.FAILURE);
            bibTree.setBib(bib);
            bibImportStatistics.getInvalidLeaderField().add("Invalid Leader Field - " + leader);
        }

        oleBatchBibImportDataObjects.getBibTrees().getBibTrees().add(bibTree);

    }

    protected void applyBibDataMappingOverlay(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile) {

    }

    protected void applyBibDataMapping(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile) {

    }

    private void setOperationAddToBibTreeList(DocstoreDocument.OperationType bibOperation, DocstoreDocument.OperationType holdingOperation, DocstoreDocument.OperationType itemOperation, BibTree bibTree) {
        bibTree.getBib().setOperation(bibOperation);
        setHoldingsTreeOperations(bibTree.getHoldingsTrees(), holdingOperation, itemOperation);
    }


    protected void setHoldingsTreeOperations(List<HoldingsTree> holdingsTrees, DocstoreDocument.OperationType holdingOperation, DocstoreDocument.OperationType itemOperation) {
        for (HoldingsTree holdingsTree : holdingsTrees) {
            Holdings holdings = holdingsTree.getHoldings();
            if (holdingOperation != null && holdings != null) {
                if(holdings.getOperation() == null) {
                    holdings.setOperation(holdingOperation);
                }
                else if (DocstoreDocument.OperationType.NONE.equals(holdings.getOperation())) {
                    holdings.setContentObject(null);
                    holdings.setContent(null);
                }
                setItemOperation(itemOperation, holdingsTree);
            }

        }
    }


    private void setItemOperation(DocstoreDocument.OperationType itemOperation, HoldingsTree holdingsTree) {
        if (holdingsTree.getHoldings().getHoldingsType().equalsIgnoreCase(PHoldings.PRINT)) {
            for (Item item : holdingsTree.getItems()) {
                if (item != null && itemOperation != null) {
                    item.setOperation(itemOperation);
                }
            }
        }
    }


    /**
     * Process Holdings and Items for Bib
     *
     * @param bibRecord
     * @param profile
     * @param bibTree
     */
    private void processHoldingsAndItemsForBib(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile, BibTree bibTree, OLEBatchBibImportDataObjects oleBatchBibImportDataObjects) {

        // Getting hHoldings Tree from bibTree
        List<HoldingsTree> holdingsTrees = bibTree.getHoldingsTrees();

        MatchingProfile matchingProfile = profile.getMatchingProfileObj();

        if (!matchingProfile.isMatchHoldings()) {
            // Do not perform matching
            if (matchingProfile.isNoMatchHoldings_deleteAddHoldingsItems()) {
                //Getting holdings Tree to  delete from bib
                HoldingsTrees holdingsTreesDelete = BatchBibImportUtil.getHoldingsTrees(bibTree.getBib().getId());
                // Setting  Holding Trees With Operation Delete
                for (HoldingsTree holdingsTree : holdingsTreesDelete.getHoldingsTrees()) {
                    Holdings holdings = holdingsTree.getHoldings();
                    holdings.setOperation(DocstoreDocument.OperationType.DELETE);
                    holdingsTrees.add(holdingsTree);
                }
                // Building  Holdings  Trees  with Create Operation
                List<HoldingsTree> holdingsTreesCreate = buildHoldingsTreesForBib(bibRecord, profile, true);
                setHoldingsTreeOperations(holdingsTreesCreate, DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE);
                holdingsTrees.addAll(holdingsTreesCreate);
            } else if (matchingProfile.isNoMatchHoldings_retainAddHoldingsItems()) {
                // Building  Holdings  Trees  with Create Operation
                List<HoldingsTree> holdingsTreesCreate = buildHoldingsTreesForBib(bibRecord, profile, true);
                setHoldingsTreeOperations(holdingsTreesCreate, DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE);
                holdingsTrees.addAll(holdingsTreesCreate);
            }
        } else {
            // Building  Match Point List for PHoldings
            List<OLEBatchProcessProfileMatchPoint> pHoldingsMatchPointList = BatchBibImportUtil.buildMatchPointListByDataType(profile.getOleBatchProcessProfileMatchPointList(), DocType.HOLDINGS.getCode());
            String docType = DocType.HOLDINGS.getCode();

            // When input file contains only one Holdings
            if (holdingsDataFields.size() == 1) {
                Holdings matchedHoldings = getMatchedHoldings(bibTree.getBib(), holdingsDataFields, docType);
                if (matchedHoldings != null) {
                    // Matched Holdings found
                    processMatchedPHoldings(bibRecord, profile, oleBatchBibImportDataObjects, holdingsTrees, matchingProfile, docType, matchedHoldings, holdingsDataFields.get(0));
                }
            } else {
                // It there is match points and we Check both Map[ping and Constants
                if (pHoldingsMatchPointList != null && pHoldingsMatchPointList.size() > 0) {
                    // Process pHoldings   to find matching record
                    processMatchedPHoldings(bibRecord, profile, bibTree.getBib(), oleBatchBibImportDataObjects, holdingsTrees, matchingProfile, pHoldingsMatchPointList, docType);
                }

            }


            // Building  Match Point List for EHoldings
            List<OLEBatchProcessProfileMatchPoint> eHoldingsMatchPointList = BatchBibImportUtil.buildMatchPointListByDataType(profile.getOleBatchProcessProfileMatchPointList(), DocType.EHOLDINGS.getCode());

                if (eHoldingsMatchPointList != null && eHoldingsMatchPointList.size() > 0) {
                    docType = DocType.EHOLDINGS.getCode();
                    // Process EHoldings   to find matching record
                    processMatchedEHoldings(bibRecord, profile, bibTree.getBib(), oleBatchBibImportDataObjects, holdingsTrees, matchingProfile, eHoldingsMatchPointList, docType);
               }
        }

    }


    /**
     * @param bibRecord
     * @param profile
     * @param bib
     * @param oleBatchBibImportDataObjects
     * @param holdingsTrees
     * @param matchingProfile
     * @param holdingsMatchPointList
     * @param docType
     */
    private void processMatchedPHoldings(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile, Bib bib, OLEBatchBibImportDataObjects oleBatchBibImportDataObjects, List<HoldingsTree> holdingsTrees, MatchingProfile matchingProfile, List<OLEBatchProcessProfileMatchPoint> holdingsMatchPointList, String docType) {
        Holdings matchedHoldings = null;
        for (DataField holdingsDataField : holdingsDataFields) {
            try {
                if (matchedHoldings == null) {
                    matchedHoldings = findMatchingForPHoldingsAndEholdings(bib.getId(), holdingsDataField, holdingsMatchPointList, docType);
                }
                processMatchedPHoldings(bibRecord, profile, oleBatchBibImportDataObjects, holdingsTrees, matchingProfile, docType, matchedHoldings, holdingsDataField);
            } catch (DocstoreException e) {
                bibImportStatistics.getMoreThanOneHoldingsMatched().add(bibRecord);
                LOG.info(e.getErrorMessage());
                continue;
            }
        }

        if (holdingsDataFields.size() == 0) {
            try {
                if (matchedHoldings == null) {
                    matchedHoldings = findMatchingForPHoldingsAndEholdings(bib.getId(), null, holdingsMatchPointList, docType);
                }
                processMatchedPHoldings(bibRecord, profile, oleBatchBibImportDataObjects, holdingsTrees, matchingProfile, docType, matchedHoldings, null);
            } catch (DocstoreException e) {
                bibImportStatistics.getMoreThanOneHoldingsMatched().add(bibRecord);
                LOG.info(e.getErrorMessage());
            }
        }
    }

    private void processMatchedPHoldings(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile, OLEBatchBibImportDataObjects oleBatchBibImportDataObjects, List<HoldingsTree> holdingsTrees, MatchingProfile matchingProfile, String docType, Holdings matchedHoldings, DataField holdingsDataField) {

        List<HoldingsTree> holdingsTreesList = new ArrayList<>();
        if (matchedHoldings == null) {

            if (matchingProfile.isHoldingsNotMatched_addHoldings()) {
                // Add Holdings
                if (matchingProfile.isHoldingsNotMatched_addItems()) {
                    //add Items with holdings
                    holdingsTreesList.addAll(buildPHoldingsTreesForBib(profile, true, holdingsDataField));
                } else {
                    holdingsTreesList.addAll(buildPHoldingsTreesForBib(profile, false, holdingsDataField));
                }
                // Setting Operation type as create
                setHoldingsTreeOperations(holdingsTreesList, DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE);
                holdingsTrees.addAll(holdingsTreesList);
            }
            bibImportStatistics.getNonMatchedHoldingsMarc().add(bibRecord);
        } else {
            HoldingsTree holdingsTree = new HoldingsTree();
            if (matchingProfile.isHoldingsMatched_addHoldings()) {
                // Add Holdings

                if (matchingProfile.isHoldingsMatched_addItems()) {
                    //add Items with holdings
                    holdingsTreesList.addAll(buildPHoldingsTreesForBib(profile, true, holdingsDataField));
                } else {
                    holdingsTreesList.addAll(buildPHoldingsTreesForBib(profile, false, holdingsDataField));
                }
                // Setting Operation type as create
                setHoldingsTreeOperations(holdingsTreesList, DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE);
                holdingsTrees.addAll(holdingsTreesList);

            } else if (matchingProfile.isHoldingsMatched_updateHoldings()) {
                //Update Holdings
                overlayHoldings(holdingsDataField, matchedHoldings, profile);
                // Setting Operation
                matchedHoldings.setOperation(DocstoreDocument.OperationType.UPDATE);

                holdingsTree.setHoldings(matchedHoldings);
                holdingsTrees.add(holdingsTree);

                // Processing Items
                if (docType.equals(DocType.HOLDINGS.getCode())) {
                    processItem(bibRecord, matchedHoldings.getId(), oleBatchBibImportDataObjects, profile, matchingProfile, matchedHoldings, holdingsTree);
                }
                // Discard Holdings and process Items
            } else if (matchingProfile.isHoldingsMatched_discardHoldings()) {
                if (docType.equals(DocType.HOLDINGS.getCode())) {
                    holdingsTree.setHoldings(matchedHoldings);
                    holdingsTrees.add(holdingsTree);
                    processItem(bibRecord, matchedHoldings.getId(), oleBatchBibImportDataObjects, profile, matchingProfile, matchedHoldings, holdingsTree);
                }
            }
            bibImportStatistics.getMatchedHoldingsMarc().add(bibRecord);

        }

    }


    protected List<HoldingsTree> processMatchedEHoldings(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile, OLEBatchBibImportDataObjects oleBatchBibImportDataObjects, List<HoldingsTree> holdingsTrees, MatchingProfile matchingProfile, Holdings matchedHoldings, DataField eHoldingsDataField) {
        if (matchedHoldings == null) {
            if (matchingProfile.isHoldingsNotMatched_addHoldings()) {
                // Add Holdings
                holdingsTrees.addAll(buildEHoldingsTreesForBib(bibRecord, profile, false));
                // Setting Operation type as create
                setHoldingsTreeOperations(holdingsTrees, DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE);
            }
            bibImportStatistics.getNonMatchedHoldingsMarc().add(bibRecord);
        } else {
            if (matchingProfile.isHoldingsMatched_addHoldings()) {
                // Add Holdings
                holdingsTrees.addAll(buildEHoldingsTreesForBib(bibRecord, profile, false));
                // Setting Operation type as create
                setHoldingsTreeOperations(holdingsTrees, DocstoreDocument.OperationType.CREATE, DocstoreDocument.OperationType.CREATE);
            } else if (matchingProfile.isHoldingsMatched_updateHoldings()) {
                //Update Holdings
                overlayHoldings(eHoldingsDataField, matchedHoldings, profile);
                // Setting Operation
                if(matchedHoldings.getOperation() == null) {
                    matchedHoldings.setOperation(DocstoreDocument.OperationType.UPDATE);
                }
                HoldingsTree holdingsTree = new HoldingsTree();
                holdingsTree.setHoldings(matchedHoldings);
                holdingsTrees.add(holdingsTree);
            }
            bibImportStatistics.getMatchedHoldingsMarc().add(bibRecord);
        }
        return holdingsTrees;
    }


    /**
     * @param bibRecord
     * @param profile
     * @param bib
     * @param oleBatchBibImportDataObjects
     * @param holdingsTrees
     * @param matchingProfile
     * @param holdingsMatchPointList
     * @param docType
     */
    private void processMatchedEHoldings(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile, Bib bib, OLEBatchBibImportDataObjects oleBatchBibImportDataObjects, List<HoldingsTree> holdingsTrees, MatchingProfile matchingProfile, List<OLEBatchProcessProfileMatchPoint> holdingsMatchPointList, String docType) {
        // multiple Eholdings Field in bib record
        for (DataField dataField : bibRecord.getDataFields()) {

            if (eHoldingsDataFields.size() > 0 && dataField.getTag().equalsIgnoreCase(eHoldingsDataFields.get(0).getTag())) {
                try {
                    Holdings matchedHoldings = findMatchingForPHoldingsAndEholdings(bib.getId(), dataField, holdingsMatchPointList, docType);
                    overlayeHoldingsDataFields.clear();
                    eHoldingsDataFields.clear();
                    eHoldingsDataFields.add(dataField);
                    if (matchedHoldings == null) {
                        // To avoid duplicates for multiple eholdings while overlaying
                        overlayeHoldingsDataFields.add(dataField);
                    }
                    processMatchedEHoldings(bibRecord, profile, oleBatchBibImportDataObjects, holdingsTrees, matchingProfile, matchedHoldings, eHoldingsDataFields.get(0));
                } catch (DocstoreException e) {
                    bibImportStatistics.getMoreThanOneHoldingsMatched().add(bibRecord);
                    LOG.info(e.getErrorMessage());
                }
            }
        }
    }

    /**
     * @param bib
     * @param holdingsDataFields
     * @param docType
     * @return
     */
    private Holdings getMatchedHoldings(Bib bib, List<DataField> holdingsDataFields, String docType) {
        Holdings matchedHoldings = null;


        boolean itemConstant = false;
        boolean holdingsConstant = false;
        boolean eHoldingsConstant = false;
        // Checks in constants and defaults and if there is no data mapping making flag as true.
        for (OLEBatchProcessProfileConstantsBo oLEBatchProcessProfileConstantsBo : constantsMapping) {
            String docTypeConstant = oLEBatchProcessProfileConstantsBo.getDataType();
            if (docTypeConstant.equalsIgnoreCase(DocType.ITEM.getCode())) {
                if (itemDataFields.size() == 0) {
                    itemConstant = true;
                }
            }

            if (docTypeConstant.equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
                if (holdingsDataFields.size() == 0) {
                    holdingsConstant = true;
                }
            }

            if (docTypeConstant.equalsIgnoreCase(DocType.EHOLDINGS.getCode())) {
                if (holdingsDataFields.size() == 0) {
                    eHoldingsConstant = true;
                }
            }
        }

        // If incoming file has single holdings
        if (holdingsDataFields.size() == 1 || holdingsConstant || eHoldingsConstant) {
            HoldingsTrees holdingsTrees = null;
            if (itemDataFields.size() == 1 || itemConstant) {
                holdingsTrees = BatchBibImportUtil.getHoldingsTrees(bib.getId());
            }

            if (holdingsTrees != null && holdingsTrees.getHoldingsTrees().size() == 1) {
                try {
                    List<String> itemIds = BatchBibImportUtil.getItemIds(holdingsTrees.getHoldingsTrees().get(0).getHoldings().getId());
                    if (itemIds != null && itemIds.size() == 1) {
                        matchedHoldings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsTrees.getHoldingsTrees().get(0).getHoldings().getId());
                    }

                } catch (Exception e) {
                    LOG.error("Error :", e);
                }
            }
        }
        return matchedHoldings;
    }


    /**
     * @param bibRecord
     * @param profile
     * @param withItem
     * @return
     */
    private List<HoldingsTree> buildHoldingsTreesForBib(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile, boolean withItem) {
        List<HoldingsTree> holdingsTreeList = new ArrayList<>();
        holdingsTreeList.addAll(buildPHoldingsTreesForBib(profile, withItem, null));
        holdingsTreeList.addAll(buildEHoldingsTreesForBib(bibRecord, profile, withItem));
        return holdingsTreeList;

    }


    /**
     * @param holdingsTreeList
     * @param OleBatchProcessProfileDataMappingOptionsBoList
     */
    private void applyDataMappingForPHoldings(List<HoldingsTree> holdingsTreeList, List<OLEBatchProcessProfileDataMappingOptionsBo> OleBatchProcessProfileDataMappingOptionsBoList) {
        String docType = null;
        String docField = null;
        String dataField = null;

        String bibFieldValue = null;
        for (OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo : OleBatchProcessProfileDataMappingOptionsBoList) {
            docType = oleBatchProcessProfileDataMappingOptionsBo.getDataTypeDestinationField();
            docField = oleBatchProcessProfileDataMappingOptionsBo.getDestinationField();
            dataField = oleBatchProcessProfileDataMappingOptionsBo.getSourceField().substring(0, 3);

            bibFieldValue = oleBatchProcessProfileDataMappingOptionsBo.getSourceFieldValue();
            if (docType.equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
                for (HoldingsTree holdingsTree : holdingsTreeList) {
                    PHoldings pHoldings = (PHoldings) BatchBibImportUtil.getHoldings(holdingsTree, DocType.HOLDINGS.getCode());

                    if (dataField.equalsIgnoreCase(holdingsDataFieldNumber))
                        continue;

                    if (pHoldings != null) {
                        pHoldings.setField(docField, bibFieldValue);
                    }
                }
            } else if (docType.equalsIgnoreCase(DocType.ITEM.getCode()))
                for (HoldingsTree holdingsTree : holdingsTreeList) {
                    List<Item> items = BatchBibImportUtil.getItem(holdingsTreeList);

                    if (dataField.equalsIgnoreCase(itemDataFieldNumber))
                        continue;
                    for (Item item : items) {
                        if (item != null) {
                            item.setField(docField, bibFieldValue);
                        }
                    }

                }
        }
    }


    /**
     * @param profile
     * @param withItem
     * @return
     */
    private List<HoldingsTree> buildPHoldingsTreesForBib(OLEBatchProcessProfileBo profile, boolean withItem, DataField dataField) {
        List<HoldingsTree> holdingsTreeList = new ArrayList<>();

        if ((profile.getDataToImport().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DATA_TO_IMPORT_BIB_INSTANCE))
                || (profile.getDataToImport().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DATA_TO_IMPORT_BIB_INSTANCE_EINSTANCE))) {
            if (dataField == null) {
                buildPHoldings(holdingsTreeList, profile, withItem);
            } else {
                buildPHoldings(holdingsTreeList, profile, withItem, dataField);
            }

        }
        applyDataMappingForPHoldings(holdingsTreeList, dataMapping);
        applyDefaultsAndConstants(profile, holdingsTreeList);
        return holdingsTreeList;

    }

    /**
     * @param bibRecord
     * @param profile
     * @param withItem
     * @return
     */
    protected List<HoldingsTree> buildEHoldingsTreesForBib(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile, boolean withItem) {
        List<HoldingsTree> holdingsTreeList = new ArrayList<>();
        StringBuilder urlMappedDataTag = new StringBuilder();
        if ((profile.getDataToImport().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DATA_TO_IMPORT_BIB_EINSTANCE))
                || (profile.getDataToImport().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DATA_TO_IMPORT_BIB_INSTANCE_EINSTANCE))) {
            buildEHoldings(bibRecord, dataMapping, holdingsTreeList, profile, urlMappedDataTag);
            applyDataMapping(holdingsTreeList, dataMapping, urlMappedDataTag);
            applyDefaultsAndConstants(profile, holdingsTreeList);
        }
        return holdingsTreeList;
    }

    /**
     * @param bibRecord
     * @param holdingId
     * @param profile
     * @param matchingProfile
     * @param matchedHoldings
     * @param holdingsTree
     */
    private void processItem(BibMarcRecord bibRecord, String holdingId, OLEBatchBibImportDataObjects oleBatchBibImportDataObjects, OLEBatchProcessProfileBo profile, MatchingProfile matchingProfile, Holdings matchedHoldings, HoldingsTree holdingsTree) {

        if (!matchingProfile.isMatchItems()) {
            List<Item> items = new ArrayList<>();


            if (matchingProfile.isNoMatchItem_deleteAddItems()) {
                List<String> itemIds = BatchBibImportUtil.getItemIds(holdingId);

                for (String itemId : itemIds) {
                    Item item = new Item();
                    item.setOperation(DocstoreDocument.OperationType.DELETE);
                    item.setId((itemId));
                    items.add(item);
                }

                items.addAll(buildItems(profile, matchedHoldings));

            } else if (matchingProfile.isNoMatchItem_retainAddItems()) {
                items.addAll(buildItems(profile, matchedHoldings));
            }

            holdingsTree.getItems().addAll(items);
        } else {
            Item matchedItem = null;
            if (itemDataFields.size() == 0) {
                // If no data mapping, check in constants
                matchedItem = findMatchingItem(profile, holdingId, bibRecord, null);
                performMatchedItem(profile, matchingProfile, matchedHoldings, matchedItem, null, holdingsTree);
            } else {
                for (DataField dataField : itemDataFields) {
                    try {
                        matchedItem = findMatchingItem(profile, holdingId, bibRecord, dataField);
                        performMatchedItem(profile, matchingProfile, matchedHoldings, matchedItem, dataField, holdingsTree);
                    } catch (DocstoreException e) {
                        LOG.info(e.getErrorMessage());
                    }
                }

            }
             if(matchedItem == null){
                 bibImportStatistics.getNonMatchedItemMarc().add(bibRecord);
             } else {
                 bibImportStatistics.getMatchedItemMarc().add(bibRecord);
                 bibImportStatistics.getMatchedItemIds().add(matchedItem.getId());
             }
        }
    }

    private void performMatchedItem(OLEBatchProcessProfileBo profile, MatchingProfile matchingProfile, Holdings matchedHoldings, Item matchedItem, DataField dataField, HoldingsTree holdingsTree) {
        List<Item> items = new ArrayList<>();
        if (matchedItem == null) {
            if (matchingProfile.isItemNotMatched_addItem()) {
                items.addAll(buildItems(profile, matchedHoldings));
            }
        } else {
            if (matchingProfile.isItemMatched_addItem()) {
                items.addAll(buildItems(profile, matchedHoldings));
            } else if (matchingProfile.isItemMatched_updateItem()) {
                overlayItem(matchedItem, profile, dataField);
                matchedItem.setOperation(DocstoreDocument.OperationType.UPDATE);
                items.add(matchedItem);
            }
        }
        holdingsTree.getItems().addAll(items);
    }

    /**
     * Build  Bib Tree by Using Bib record and Mapping from profile
     *
     * @param bibRecord
     * @param profile
     * @return
     */
    private BibTree buildBibTree(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile) {
        BibTree bibTree = new BibTree();
        if ((profile.getDataToImport().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DATA_TO_IMPORT_BIB_INSTANCE))
                || (profile.getDataToImport().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DATA_TO_IMPORT_BIB_EINSTANCE))
                || (profile.getDataToImport().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DATA_TO_IMPORT_BIB_INSTANCE_EINSTANCE))) {
            // Build bib tree with  Holdings and Items
            List<HoldingsTree> holdingsTrees = buildHoldingsTreesForBib(bibRecord, profile, true);
            bibTree.getHoldingsTrees().addAll(holdingsTrees);
        }

        Bib bib = buildBib(bibRecord, profile);
        bibTree.setBib(bib);
        return bibTree;
    }

    /**
     * Processing New Bib
     *
     * @param bibRecord
     * @param profile
     * @return
     */
    private Bib buildBib(BibMarcRecord bibRecord, OLEBatchProcessProfileBo profile) {
        Bib requestBib = null;
        batchProcessBibImportService.deleteFields(bibRecord, profile);
        batchProcessBibImportService.renameFields(bibRecord, profile);
        batchProcessBibImportService.setDefaultOrConstants(bibRecord, profile);
        batchProcessBibImportService.process001(bibRecord, profile);

        String uuid = batchProcessBibImportService.getUuid(bibRecord);
        requestBib = batchProcessBibImportService.buildBibRequest(bibRecord, uuid);
        batchProcessBibImportService.setBibStatus(requestBib, profile, uuid);
        if (StringUtils.isEmpty(uuid)) {
            requestBib.setStaffOnly(profile.isBibStaffOnly());
        }
        requestBib.setCreatedBy(userName);
        return requestBib;
    }

    /**
     * @param holdingsTreeList
     * @param profile
     * @param withItem
     */
    private void buildPHoldings(List<HoldingsTree> holdingsTreeList, OLEBatchProcessProfileBo profile, boolean withItem) {

        for (DataField dataField : holdingsDataFields) {
            buildHoldingsFromDataField(holdingsTreeList, profile, dataField);
        }

        if (holdingsDataFields.size() == 0) {
            buildHoldingsFromDataField(holdingsTreeList, profile, null);
        }
        //Build Items for Holdings
        if (withItem) {
            buildItemForHoldings(dataMapping, holdingsTreeList, profile);
        }
    }


    /**
     * @param holdingsTreeList
     * @param profile
     * @param withItem
     * @param dataField
     */
    private void buildPHoldings(List<HoldingsTree> holdingsTreeList, OLEBatchProcessProfileBo profile, boolean withItem, DataField dataField) {

        buildHoldingsFromDataField(holdingsTreeList, profile, dataField);
        //Build Items for Holdings
        if (withItem) {
            buildItemForHoldings(dataMapping, holdingsTreeList, profile);
        }
    }

    /**
     * @param holdings
     * @param profile
     * @param dataField
     */

    private void buildHoldingsFromDataField(Holdings holdings, OLEBatchProcessProfileBo profile, DataField dataField) {
        String docField;
        String bibField;

        if (holdings != null && dataField != null) {
            holdings.setStaffOnly(profile.isInstanceStaffOnly());
            for (OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo : dataMapping) {
                docField = oleBatchProcessProfileDataMappingOptionsBo.getDestinationField();
                bibField = oleBatchProcessProfileDataMappingOptionsBo.getSourceField();
                String dataFieldValue = BatchBibImportUtil.getSubFieldValue(bibField, dataField);
                if (StringUtils.isNotEmpty(dataFieldValue)) {
                    holdings.setField(docField, dataFieldValue);
                }
            }
        }

    }

    /**
     * @param holdingsTreeList
     * @param profile
     * @param dataField
     */

    private void buildHoldingsFromDataField(List<HoldingsTree> holdingsTreeList, OLEBatchProcessProfileBo profile, DataField dataField) {
        HoldingsTree holdingTree = BatchBibImportUtil.buildHoldingsTree(DocType.HOLDINGS.getCode());
        Holdings holdings = holdingTree.getHoldings();
        buildHoldingsFromDataField(holdings, profile, dataField);
        holdingsTreeList.add(holdingTree);
    }

    /**
     * @param bibRecord
     * @param oleBatchProcessProfileBo
     */
    public List<OLEBatchProcessProfileDataMappingOptionsBo> processPriority(BibMarcRecord bibRecord, OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        String docField = null;
        String bibField = null;
        String bibFieldValue = null;
        List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileMappingOptionsBos = new ArrayList<>();
        HashMap<String, OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileMappingOptionsBoMap = new HashMap<>();
        List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsList = oleBatchProcessProfileBo.getOleBatchProcessProfileMappingOptionsList();
        for (OLEBatchProcessProfileMappingOptionsBo oleBatchProcessProfileMappingOptionsBo : oleBatchProcessProfileMappingOptionsList) {
            List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = oleBatchProcessProfileMappingOptionsBo.getOleBatchProcessProfileDataMappingOptionsBoList();
            Collections.sort(oleBatchProcessProfileDataMappingOptionsBoList);
            for (OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo : oleBatchProcessProfileDataMappingOptionsBoList) {
                docField = oleBatchProcessProfileDataMappingOptionsBo.getDestinationField();
                bibField = oleBatchProcessProfileDataMappingOptionsBo.getSourceField();
                String key = oleBatchProcessProfileDataMappingOptionsBo.getDataTypeDestinationField() + docField;
                if (!oleBatchProcessProfileMappingOptionsBoMap.containsKey(key)) {
                    bibFieldValue = BatchBibImportUtil.getBibDataFieldValue(bibRecord, bibField);
                    if (!StringUtils.isEmpty(bibFieldValue)) {
                        oleBatchProcessProfileDataMappingOptionsBo.setSourceFieldValue(bibFieldValue);
                        oleBatchProcessProfileMappingOptionsBoMap.put(key, oleBatchProcessProfileDataMappingOptionsBo);
                    }
                }
            }
        }
        for (Map.Entry<String, OLEBatchProcessProfileDataMappingOptionsBo> entry : oleBatchProcessProfileMappingOptionsBoMap.entrySet()) {
            oleBatchProcessProfileMappingOptionsBos.add(entry.getValue());
        }
        return oleBatchProcessProfileMappingOptionsBos;
    }

    /**
     * Getting  Holdings and Item Data fields
     *
     * @param bibRecord
     * @param oleBatchProcessProfileDataMappingOptionsBos
     * @param holdingsDataFields
     * @param itemDataFields
     * @param eHoldingsDataFields
     */
    private void getHoldingsItemDataFields(BibMarcRecord bibRecord, List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBos, List<DataField> holdingsDataFields, List<DataField> itemDataFields, List<DataField> eHoldingsDataFields) {
        holdingsDataFieldNumber = null;
        itemDataFieldNumber = null;
        String eHoldingsDataField = null;
        holdingsDataFields.clear();
        itemDataFields.clear();
        eHoldingsDataFields.clear();

        Map<String, String> itemHoldingsMapping = new TreeMap<>();
        for (OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo : oleBatchProcessProfileDataMappingOptionsBos) {
            if (holdingsDataFieldNumber == null && oleBatchProcessProfileDataMappingOptionsBo.getDataTypeDestinationField().equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
                holdingsDataFieldNumber = oleBatchProcessProfileDataMappingOptionsBo.getSourceField().substring(0, 3);
            }

            if (itemDataFieldNumber == null && oleBatchProcessProfileDataMappingOptionsBo.getDataTypeDestinationField().equalsIgnoreCase(DocType.ITEM.getCode())) {
                itemDataFieldNumber = oleBatchProcessProfileDataMappingOptionsBo.getSourceField().substring(0, 3);
            }

            if (eHoldingsDataField == null && oleBatchProcessProfileDataMappingOptionsBo.getDataTypeDestinationField().equalsIgnoreCase(DocType.EHOLDINGS.getCode())
                    && oleBatchProcessProfileDataMappingOptionsBo.getDestinationField().equalsIgnoreCase(EHoldings.DESTINATION_FIELD_LINK_URL)) {
                eHoldingsDataField = oleBatchProcessProfileDataMappingOptionsBo.getSourceField().substring(0, 3);
            }

            if (oleBatchProcessProfileDataMappingOptionsBo.getDataTypeDestinationField().equalsIgnoreCase(DocType.ITEM.getCode())) {
                itemHoldingsMapping.put(oleBatchProcessProfileDataMappingOptionsBo.getDestinationField(), oleBatchProcessProfileDataMappingOptionsBo.getDestinationField());
            }
        }
        for (DataField dataField : bibRecord.getDataFields()) {

            if (dataField.getTag().equalsIgnoreCase(holdingsDataFieldNumber)) {
                holdingsDataFields.add(dataField);
            }

            if (dataField.getTag().equalsIgnoreCase(itemDataFieldNumber)) {
                itemDataFields.add(dataField);
            }


            if (dataField.getTag().equalsIgnoreCase(eHoldingsDataField)) {
                eHoldingsDataFields.add(dataField);
            }
        }


        // This is to check  if Item does not contain Holding mapping details .To create Single Holdings and Item.
        if (itemHoldingsMapping.size() > 0 && !BatchBibImportUtil.isItemHoldingMapping(itemHoldingsMapping)) {
            if (holdingsDataFields.size() > 0) {
                DataField dataField = holdingsDataFields.get(0);
                holdingsDataFields.clear();
                holdingsDataFields.add(dataField);
            }

            if (itemDataFields.size() > 0) {
                DataField dataField = itemDataFields.get(0);
                itemDataFields.clear();
                itemDataFields.add(dataField);
            }

        }
    }

    /**
     * Build Items for Holdings
     *
     * @param oleBatchProcessProfileDataMappingOptionsBos
     * @param holdingsTreeList
     * @param profile
     */
    private void buildItemForHoldings(List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBos, List<HoldingsTree> holdingsTreeList, OLEBatchProcessProfileBo profile) {
        boolean isItemDefaultorConstants = false;

        if (itemDataFields.size() == 0) {
            String docType = null;
            List<OLEBatchProcessProfileConstantsBo> constantsMapping = profile.getOleBatchProcessProfileConstantsList();
            for (OLEBatchProcessProfileConstantsBo oLEBatchProcessProfileConstantsBo : constantsMapping) {
                docType = oLEBatchProcessProfileConstantsBo.getDataType();

                if (!docType.equalsIgnoreCase((DocType.ITEM.getCode())))
                    continue;

                if (docType.equalsIgnoreCase(DocType.ITEM.getCode())) {
                    isItemDefaultorConstants = true;
                    break;
                }
            }
        }

        for (HoldingsTree holdingsTree : holdingsTreeList) {
            //Comparing Holdings Data Value and Item Data value. If Matched Create Item
            String docField = null;
            String bibField = null;
            String dataFieldValue = null;
            Map<String, String> itemDocFields = new TreeMap<>();

            Holdings holdings = holdingsTree.getHoldings();
            BatchBibImportUtil.buildLocationLevels(holdings);

            for (DataField dataField : itemDataFields) {
                Item item = new Item();
                item.setField(Item.DESTINATION_FIELD_ITEM_ITEM_BARCODE, "");
                dataFieldValue = null;
                for (OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo : oleBatchProcessProfileDataMappingOptionsBos) {
                    if (oleBatchProcessProfileDataMappingOptionsBo.getDataTypeDestinationField().equalsIgnoreCase(DocType.ITEM.getCode())) {
                        docField = oleBatchProcessProfileDataMappingOptionsBo.getDestinationField();
                        bibField = oleBatchProcessProfileDataMappingOptionsBo.getSourceField();
                        dataFieldValue = BatchBibImportUtil.getSubFieldValue(bibField, dataField);
                        if (StringUtils.isNotEmpty(dataFieldValue)) {
                            item.setField(docField, dataFieldValue);
                        }
                        itemDocFields.put(docField, docField);
                    }
                }
                if (item.getItemHoldingsDataMappingValue(itemDocFields).equalsIgnoreCase(holdings.getHoldingsDataMappingValue(itemDocFields))) {
                    setItem(profile, holdingsTree, item);
                }
            }
            if (isItemDefaultorConstants) {
                Item item = new Item();
                item.setField(Item.DESTINATION_FIELD_ITEM_ITEM_BARCODE, "");
                setItem(profile, holdingsTree, item);
            }
        }

    }


    /**
     * @param profile
     * @param holdingsTree
     * @param item
     */
    private void setItem(OLEBatchProcessProfileBo profile, HoldingsTree holdingsTree, Item item) {
        item.setStaffOnly(profile.isItemStaffOnly());
        holdingsTree.getItems().add(item);
        BatchBibImportUtil.buildLocationLevels(item);
        applyDefaultConstantsandSerilazeContent(item, profile);
    }

    /**
     * @param holdingsTreeList
     * @param oleBatchProcessProfileConstantsBoList
     * @param defaultOrConstant
     */
    private void applyDefaultsAndConstants(List<HoldingsTree> holdingsTreeList, List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsBoList, String defaultOrConstant) {
        for (OLEBatchProcessProfileConstantsBo oLEBatchProcessProfileConstantsBo : oleBatchProcessProfileConstantsBoList) {
            String defaultValue = oLEBatchProcessProfileConstantsBo.getDefaultValue();

            if (!defaultValue.equalsIgnoreCase(defaultOrConstant))
                continue;

            String docType = oLEBatchProcessProfileConstantsBo.getDataType();
            String docField = oLEBatchProcessProfileConstantsBo.getAttributeName();
            String fieldValue = oLEBatchProcessProfileConstantsBo.getAttributeValue();


            if (docType.equalsIgnoreCase(DocType.HOLDINGS.getCode())) {
                for (HoldingsTree holdingsTree : holdingsTreeList) {
                    PHoldings pHoldings = (PHoldings) BatchBibImportUtil.getHoldings(holdingsTree, DocType.HOLDINGS.getCode());
                    if (pHoldings != null) {
                        if (defaultValue.equalsIgnoreCase(OLEConstants.OLEBatchProcess.CONSTANT)) {
                            pHoldings.setField(docField, fieldValue);
                        } else {
                            pHoldings.setDefaultField(docField, fieldValue);
                        }

                    }
                }

            } else if (docType.equalsIgnoreCase(DocType.EHOLDINGS.getCode())) {
                for (HoldingsTree holdingsTree : holdingsTreeList) {
                    EHoldings eHoldings = (EHoldings) BatchBibImportUtil.getHoldings(holdingsTree, DocType.EHOLDINGS.getCode());

                    if (EHoldings.DESTINATION_FIELD_LINK_URL.equalsIgnoreCase(docField) && defaultOrConstant.equalsIgnoreCase(OLEConstants.OLEBatchProcess.DEFAULT))
                        continue;

                    if (EHoldings.DESTINATION_FIELD_LINK_TEXT.equalsIgnoreCase(docField) && defaultOrConstant.equalsIgnoreCase(OLEConstants.OLEBatchProcess.DEFAULT))
                        continue;


                    if (eHoldings != null) {
                        if (defaultValue.equalsIgnoreCase(OLEConstants.OLEBatchProcess.CONSTANT)) {
                            if (docField.equalsIgnoreCase(EHoldings.DESTINATION_FIELD_ERESOURCE_NAME) || docField.equalsIgnoreCase(EHoldings.DESTINATION_FIELD_ERESOURCE_ID)) {
                                validateAndSetEResourceId(fieldValue, eHoldings);
                            } else {
                                eHoldings.setField(docField, fieldValue);
                            }
                        } else {
                            eHoldings.setDefaultField(docField, fieldValue);
                        }

                    }
                }

            }
        }
    }


    /**
     * @param holdingsTreeList
     */
    public void prepareDocContent(List<HoldingsTree> holdingsTreeList) {
        List<Integer> holdingsCount = new ArrayList<>();
        Integer count = 0;
        for (HoldingsTree holdingsTree : holdingsTreeList) {
            holdingsTree.getHoldings().serializeContent();
            if (holdingsTree.getHoldings().getCreatedBy() == null) {
                holdingsTree.getHoldings().setCreatedBy(userName);
            } else {
                holdingsTree.getHoldings().setUpdatedBy(userName);
            }

            for (org.kuali.ole.docstore.common.document.Item item : holdingsTree.getItems()) {

                if (item.getCreatedBy() == null) {
                    item.setCreatedBy(userName);
                } else {
                    item.setUpdatedBy(userName);
                }
                // To Do set Item Content
                item.serializeContent();
            }
            if (holdingsTree.getHoldings().getContent() == null) {
                holdingsCount.add(count);
            }
            count++;
        }
        Collections.reverse(holdingsCount);
        for (Integer emptyHoldings : holdingsCount) {
            holdingsTreeList.remove(holdingsTreeList.get(emptyHoldings));
        }

    }

    /**
     * @param bibRecord
     * @param oleBatchProcessProfileDataMappingOptionsBos
     * @param holdingsTreeList
     * @param profile
     */
    private void buildEHoldings(BibMarcRecord bibRecord, List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBos, List<HoldingsTree> holdingsTreeList, OLEBatchProcessProfileBo profile, StringBuilder urlMappedDataTag) {
        String urlDocField = null;
        String urlTagField = null;
        List<DataField> dataFields = new ArrayList<>();
        for (OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo : oleBatchProcessProfileDataMappingOptionsBos) {

            urlDocField = oleBatchProcessProfileDataMappingOptionsBo.getDestinationField();
            if (!EHoldings.DESTINATION_FIELD_LINK_URL.equalsIgnoreCase(urlDocField))
                continue;

            urlTagField = oleBatchProcessProfileDataMappingOptionsBo.getSourceField().substring(0, 3);
            urlMappedDataTag.append(urlTagField);
            if (overlayeHoldingsDataFields.size() > 0) {
                dataFields = overlayeHoldingsDataFields;
            } else {
                dataFields = BatchBibImportUtil.getMatchedUrlDataFields(urlTagField, bibRecord);
            }
            for (DataField dataField : dataFields) {
                String urlSourceFieldValue = BatchBibImportUtil.getDataFieldValue(dataField, oleBatchProcessProfileDataMappingOptionsBo.getSourceField());
                if (StringUtils.isNotBlank(urlSourceFieldValue)) {
                    HoldingsTree eHoldingTree = BatchBibImportUtil.buildHoldingsTree(DocType.EHOLDINGS.getCode());
                    EHoldings eHoldings = (EHoldings) eHoldingTree.getHoldings();
                    bibImportStatistics.getTotalRecordsCreated().add(eHoldings);
                    if (eHoldings != null) {
                        eHoldingTree.getHoldings().setStaffOnly(profile.isInstanceStaffOnly());
                        eHoldings.setField(urlDocField, urlSourceFieldValue);
                        for (OLEBatchProcessProfileDataMappingOptionsBo batchProcessProfileDataMappingOptionsBo : oleBatchProcessProfileDataMappingOptionsBos) {
                            if (batchProcessProfileDataMappingOptionsBo.getSourceField().substring(0, 3).equals(dataField.getTag()) && !batchProcessProfileDataMappingOptionsBo.getDestinationField().equalsIgnoreCase(urlDocField)) {
                                String docField = batchProcessProfileDataMappingOptionsBo.getDestinationField();
                                String sourceFieldValue = BatchBibImportUtil.getDataFieldValue(dataField, batchProcessProfileDataMappingOptionsBo.getSourceField());
                                if (docField.equalsIgnoreCase(EHoldings.DESTINATION_FIELD_ERESOURCE_NAME)) {
                                    validateAndSetEResource(sourceFieldValue, eHoldings);
                                } else if (docField.equalsIgnoreCase(EHoldings.DESTINATION_FIELD_ERESOURCE_ID)) {
                                    validateAndSetEResourceId(sourceFieldValue, eHoldings);
                                } else if (docField.equalsIgnoreCase(EHoldings.DESTINATION_FIELD_PLATFORM)) {
                                    validateAndSetPlatform(sourceFieldValue, eHoldings);
                                } else {
                                    eHoldings.setField(docField, sourceFieldValue);
                                }
                            }
                        }
                    }
                    holdingsTreeList.add(eHoldingTree);
                }
            }

            if (holdingsTreeList.size() > 1) {
                bibImportStatistics.getRecordsCreatedWithMoreThanOneLink().add(bibRecord);
            }
        }

    }

    /**
     * @param holdingsTreeList
     * @param OleBatchProcessProfileDataMappingOptionsBoList
     */
    private void applyDataMapping(List<HoldingsTree> holdingsTreeList, List<OLEBatchProcessProfileDataMappingOptionsBo> OleBatchProcessProfileDataMappingOptionsBoList, StringBuilder urlMappedDataTag) {
        String docType = null;
        String docField = null;
        String sourceTag = null;

        String bibFieldValue = null;
        for (OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo : OleBatchProcessProfileDataMappingOptionsBoList) {
            docType = oleBatchProcessProfileDataMappingOptionsBo.getDataTypeDestinationField();
            docField = oleBatchProcessProfileDataMappingOptionsBo.getDestinationField();
            sourceTag = oleBatchProcessProfileDataMappingOptionsBo.getSourceField().substring(0, 3);

            bibFieldValue = oleBatchProcessProfileDataMappingOptionsBo.getSourceFieldValue();
            if (docType.equalsIgnoreCase(DocType.EHOLDINGS.getCode())) {
                for (HoldingsTree holdingsTree : holdingsTreeList) {
                    EHoldings eHoldings = (EHoldings) BatchBibImportUtil.getHoldings(holdingsTree, DocType.EHOLDINGS.getCode());

                    if (StringUtils.isNotBlank(sourceTag) && urlMappedDataTag != null && sourceTag.equalsIgnoreCase(urlMappedDataTag.toString()))
                        continue;

                    if (eHoldings != null) {
                        if (docField.equalsIgnoreCase(EHoldings.DESTINATION_FIELD_ERESOURCE_NAME)) {
                            validateAndSetEResource(bibFieldValue, eHoldings);
                        } else if (docField.equalsIgnoreCase(EHoldings.DESTINATION_FIELD_ERESOURCE_ID)) {
                            validateAndSetEResourceId(bibFieldValue, eHoldings);
                        } else if (docField.equalsIgnoreCase(EHoldings.DESTINATION_FIELD_PLATFORM)) {
                            validateAndSetPlatform(bibFieldValue, eHoldings);
                        } else {
                            eHoldings.setField(docField, bibFieldValue);
                        }
                    }
                }
            }
        }
    }

    /**
     * Apply data mapping for eHoldings records for overlay
     *
     * @param holdingsTreeList
     * @param oleBatchProcessProfileDataMappingOptionsBoList
     */

    private void applyDataMappingOverlay(List<HoldingsTree> holdingsTreeList, List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList) {
        String docType = null;
        String docField = null;

        String bibFieldValue = null;
        if (!oleBatchProcessProfileDataMappingOptionsBoList.isEmpty()) {
            for (OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo : oleBatchProcessProfileDataMappingOptionsBoList) {
                docType = oleBatchProcessProfileDataMappingOptionsBo.getDataTypeDestinationField();
                docField = oleBatchProcessProfileDataMappingOptionsBo.getDestinationField();

                bibFieldValue = oleBatchProcessProfileDataMappingOptionsBo.getSourceFieldValue();
                if (docType.equalsIgnoreCase(DocType.EHOLDINGS.getCode())) {
                    if (!holdingsTreeList.isEmpty()) {
                        for (HoldingsTree holdingsTree : holdingsTreeList) {
                            EHoldings eHoldings = (EHoldings) BatchBibImportUtil.getHoldings(holdingsTree, DocType.EHOLDINGS.getCode());

//                    if (EHoldings.DESTINATION_FIELD_LINK_URL.equalsIgnoreCase(docField)) {
//                        continue;
//                    }
                            if (eHoldings != null) {
                                if (EHoldings.DESTINATION_FIELD_ERESOURCE_NAME.equals(docField)) {

                                    //set the eResource name if exists in OLE
                                    validateAndSetEResource(bibFieldValue, eHoldings);

                                }
                                if (EHoldings.DESTINATION_FIELD_ERESOURCE_ID.equals(docField)) {

                                    //set the eResource name if exists in OLE
                                    validateAndSetEResourceId(bibFieldValue, eHoldings);

                                } else if (EHoldings.DESTINATION_FIELD_PLATFORM.equals(docField)) {

                                    //set the platform name if exists in OLE
                                    validateAndSetPlatform(bibFieldValue, eHoldings);
                                } else {
                                    eHoldings.setField(docField, bibFieldValue);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void validateAndSetPlatform(String bibFieldValue, EHoldings eHoldings) {
        List<OLEPlatformRecordDocument> olePlatformRecordDocuments = getPlatformRecordByName(bibFieldValue);
        if(olePlatformRecordDocuments != null && olePlatformRecordDocuments.size() == 1) {
            eHoldings.setField(EHoldings.DESTINATION_FIELD_PLATFORM, olePlatformRecordDocuments.get(0).getOlePlatformId());
        }
        else {
            bibImportStatistics.setNoOfEHoldingsCreatedWithOutPlatfom(1 + bibImportStatistics.getNoOfEHoldingsCreatedWithOutPlatfom());
            //if platform name  doesnt exists in OLE, set operation to NONE
            if(olePlatformRecordDocuments != null && olePlatformRecordDocuments.size() > 1) {
                setErrorMessage(eHoldings, OLEConstants.ERROR_MESSAGE_MORE_THAN_ONE_PLATFORM);
            }
            else {
                setErrorMessage(eHoldings, OLEConstants.ERROR_MESSAGE_PLATFORM + bibFieldValue);
            }
        }
    }



    private void validateAndSetEResource(String bibFieldValue, EHoldings eHoldings) {
        List<OLEEResourceRecordDocument> oleeResourceRecordDocuments = getEResourceDocumentByName(bibFieldValue);
        if(oleeResourceRecordDocuments != null && oleeResourceRecordDocuments.size() == 1) {
            eHoldings.setField(EHoldings.DESTINATION_FIELD_ERESOURCE_ID, oleeResourceRecordDocuments.get(0).getOleERSIdentifier());
        }
        else {
            bibImportStatistics.setNoOfEHoldingsCreatedWithOutEResource(1 + bibImportStatistics.getNoOfEHoldingsCreatedWithOutEResource());
            //if eResource name  doesnt exists in OLE, set operation to NONE
            if(oleeResourceRecordDocuments != null && oleeResourceRecordDocuments.size() > 1) {
                setErrorMessage(eHoldings, OLEConstants.ERROR_MESSAGE_MORE_THAN_ONE_ERESOURCE);
            }
            else {
                setErrorMessage(eHoldings, OLEConstants.ERROR_MESSAGE_ERESOURCE + bibFieldValue);
            }
        }
    }

    /**
     * This method will append the error message to eholdings
     * @param eHoldings
     * @param failureMessage
     */
    private void setErrorMessage(EHoldings eHoldings, String failureMessage) {
        String errorMessage = eHoldings.getMessage();
        if(StringUtils.isNotEmpty(errorMessage)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(errorMessage);
            stringBuilder.append(" and ");
            stringBuilder.append(failureMessage);
            eHoldings.setMessage(stringBuilder.toString());
        }
        else {
            eHoldings.setMessage(failureMessage);
        }
    }

    /**
     * Find Matching Bib by using Bib Match  from profile and returns only if only one Bib is Matched
     *
     * @param profile
     * @param bibRecord
     * @return
     */
    private Bib findMatchingBib(OLEBatchProcessProfileBo profile, BibMarcRecord bibRecord) {
        List<OLEBatchProcessProfileMatchPoint> bibMatchPointList = BatchBibImportUtil.buildMatchPointListByDataType(profile.getOleBatchProcessProfileMatchPointList(), DocType.BIB.getCode());
        String fieldValue = null;
        SearchParams searchParams = new SearchParams();
        StringBuilder matchBuiderForReport = new StringBuilder();
        matchBuiderForReport.append("For Title - " + BatchBibImportUtil.getTitle(bibRecord) +"  -  ");
        for (OLEBatchProcessProfileMatchPoint matchPoint : bibMatchPointList) {
            String cascadingMatchPoint = matchPoint.getCascadingMatchPoint();
            List<String>  dataValues =  BatchBibImportUtil.getMatchedDataField(bibRecord, matchPoint.getMatchPoint());
            for(String dataValue:dataValues){
                fieldValue = dataValue;
                if (StringUtils.isNotBlank(fieldValue)) {

                    if (cascadingMatchPoint != null &&  cascadingMatchPoint.contains("*")) {
                        fieldValue = cascadingMatchPoint.replace("*", fieldValue);
                    }
                    addSearchCondition(fieldValue, searchParams, matchPoint);
                    matchBuiderForReport.append(matchPoint.getMatchPoint()+" : "+fieldValue +" - ");
                }
            }
        }
        if (fieldValue == null) {
          return null;
        }
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.BIB.getCode(), Bib.BIBIDENTIFIER));

        List<SearchResult> searchResults = new ArrayList<>();
        try {
            SearchResponse response = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            searchResults = response.getSearchResults();
        } catch (Exception e) {
            LOG.error("Error :", e);
        }

        if (searchResults.size() > 1) {
            matchBuiderForReport.append("-"+OLEConstants.OLEBatchProcess.MORE_THAN_ONE_MATCHING_FOUND_FROM_EXISTING_RECORD);
            bibImportStatistics.getNoMatchFoundBibs().add(matchBuiderForReport.toString());
            throw new DocstoreSearchException(OLEConstants.OLEBatchProcess.MORE_THAN_ONE_MATCHING_FOUND_FROM_EXISTING_RECORD);
        }

        if (searchResults.size() == 0) {
            matchBuiderForReport.append("-"+OLEConstants.OLEBatchProcess.NO_MATCHING_RECORD);
            bibImportStatistics.getNoMatchFoundBibs().add(matchBuiderForReport.toString());
        }

        if (searchResults.size() == 1) {
            Bib bib = new Bib();
            String bibId = searchResults.get(0).getSearchResultFields().get(0).getFieldValue();
            try {
                bib = getDocstoreClientLocator().getDocstoreClient().retrieveBib(bibId);
            } catch (Exception e) {
                LOG.error("Error :", e);
            }
            return bib;
        }
        return null;
    }

    protected void addSearchCondition(String fieldValue, SearchParams searchParams, OLEBatchProcessProfileMatchPoint matchPoint) {
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(DocType.BIB.getCode(), BatchBibImportUtil.getDataFieldWithout$(matchPoint.getMatchPoint()), fieldValue), "OR"));
    }

    /**
     * Update Matched Bib
     *
     * @param bibRecord
     * @param matchedBib
     * @param profile
     * @return
     */
    private void overlayBib(BibMarcRecord bibRecord, Bib matchedBib, OLEBatchProcessProfileBo profile) {


        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();

        batchProcessBibImportService.deleteFields(bibRecord, profile);
        batchProcessBibImportService.renameFields(bibRecord, profile);
        batchProcessBibImportService.setDefaultOrConstants(bibRecord, profile);
        batchProcessBibImportService.process001(bibRecord, profile);

        BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(matchedBib.getContent());
        BibMarcRecord matchedBibMarcRecord = bibMarcRecords.getRecords().get(0);


        batchProcessBibImportService.overlayFields(bibRecord, matchedBibMarcRecord, profile);

        String bibXML = bibMarcRecordProcessor.generateXML(matchedBibMarcRecord);
        matchedBib.setContent(bibXML);

        batchProcessBibImportService.setBibStatus(matchedBib, profile, matchedBib.getId());

        if (StringUtils.isEmpty(matchedBib.getId())) {
            matchedBib.setStaffOnly(profile.isBibStaffOnly());
        } else {
            if (OLEConstants.OLEBatchProcess.CHANGE.equals(profile.getOverlayNoChangeOrSet())) {
                matchedBib.setStaffOnly(profile.isOverlayBibStaffOnly());
                matchedBib.setStatusUpdatedBy(userName);
            }
        }
        matchedBib.setUpdatedBy(userName);
    }

    /**
     * @param bibId
     * @param dataField
     * @param holdingsMatchPointList
     * @param docType
     * @return
     */
    private Holdings findMatchingForPHoldingsAndEholdings(String bibId, DataField dataField, List<OLEBatchProcessProfileMatchPoint> holdingsMatchPointList, String docType) {
        SearchParams searchParams = new SearchParams();
        StringBuilder matchPointString = new StringBuilder("For Bib Id :"+bibId+" and  Holdings match points -");
        for (OLEBatchProcessProfileMatchPoint matchPoint : holdingsMatchPointList) {
            matchPointString.append(matchPoint.getMatchPoint()+" - ");
            String fieldValue = null;
            //Checking  for field value in Holdings constants
            fieldValue = getFieldValueFromConstantsOrDefaults(matchPoint, OLEConstants.OLEBatchProcess.CONSTANT, docType);

            // Checking value from  data Mapping
            if (dataField != null && fieldValue == null) {
                for (OLEBatchProcessProfileDataMappingOptionsBo mappingOptionsBo : dataMapping) {
                    if (mappingOptionsBo.getDataTypeDestinationField().equalsIgnoreCase(docType)) {
                        if (mappingOptionsBo.getDestinationField().equalsIgnoreCase(matchPoint.getMatchPoint())) {
                            String bibField = mappingOptionsBo.getSourceField();
                            fieldValue = BatchBibImportUtil.getSubFieldValue(bibField, dataField);
                        }
                    }
                }
            }

            //Checking  for field value in Holdings Default
            if (fieldValue == null) {
                fieldValue = getFieldValueFromConstantsOrDefaults(matchPoint, OLEConstants.OLEBatchProcess.DEFAULT, DocType.HOLDINGS.getCode());
            }

            if (fieldValue != null) {
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(docType, matchPoint.getMatchPoint().replaceAll(" ", "").toUpperCase(), fieldValue), "OR"));
            }
        }
        if (searchParams.getSearchConditions().size() > 0) {
            int lastIndex = searchParams.getSearchConditions().size() - 1;
            SearchCondition searchCondition = searchParams.getSearchConditions().get(lastIndex);
            searchCondition.setOperator("AND");
        }

        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField(docType, Bib.BIBIDENTIFIER, bibId), "AND"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, Holdings.HOLDINGSIDENTIFIER));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(docType, "DocType"));

        List<SearchResult> searchResults = new ArrayList<>();
        try {
            SearchResponse response = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            searchResults = response.getSearchResults();
        } catch (Exception e) {
            LOG.error("Error :", e);
        }

        if (searchResults.size() > 1) {
            bibImportStatistics.getNoMatchFoundHoldings().add(matchPointString.toString()+" - "+OLEConstants.OLEBatchProcess.MORE_THAN_ONE_MATCHING_FOUND_FROM_EXISTING_RECORD);
            throw new DocstoreSearchException(OLEConstants.OLEBatchProcess.MORE_THAN_ONE_MATCHING_FOUND_FROM_EXISTING_RECORD);
        }
        if(searchResults.size() == 0){
            bibImportStatistics.getNoMatchFoundHoldings().add(matchPointString.toString()+" - "+OLEConstants.OLEBatchProcess.NO_MATCHING_RECORD);
        }

        if (searchResults.size() == 1) {
            if (searchResults.get(0).getSearchResultFields() != null && searchResults.get(0).getSearchResultFields().size() > 0) {
                Holdings holdings = new Holdings();

                String holdingsId = searchResults.get(0).getSearchResultFields().get(0).getFieldValue();
                bibImportStatistics.getMatchedBibIds().add(holdingsId);
                try {
                    holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsId);
                } catch (Exception e) {
                    LOG.error("Error :", e);
                }
                return holdings;
            }
        }
        return null;
    }

    /**
     * @param matchPoint
     * @param constantOrDefault
     * @param docType
     * @return
     */
    private String getFieldValueFromConstantsOrDefaults(OLEBatchProcessProfileMatchPoint matchPoint, String constantOrDefault, String docType) {

        String fieldValue = null;
        for (OLEBatchProcessProfileConstantsBo oLEBatchProcessProfileConstantsBo : constantsMapping) {

            String defaultValue = oLEBatchProcessProfileConstantsBo.getDefaultValue();

            if (!defaultValue.equalsIgnoreCase(constantOrDefault))
                continue;

            String docTypeConstant = oLEBatchProcessProfileConstantsBo.getDataType();

            if (!docTypeConstant.equalsIgnoreCase(docType))
                continue;

            String docField = oLEBatchProcessProfileConstantsBo.getAttributeName();
            if (docField.equalsIgnoreCase(matchPoint.getMatchPoint())) {
                fieldValue = oLEBatchProcessProfileConstantsBo.getAttributeValue();
            }
        }
        return fieldValue;
    }

    /**
     * @param dataField
     * @param matchedHoldings
     */
    private void overlayHoldings(DataField dataField, Holdings matchedHoldings, OLEBatchProcessProfileBo profile) {
        List<HoldingsTree> holdingsTreeList = BatchBibImportUtil.getHoldingsTrees(matchedHoldings);
        buildHoldingsFromDataField(matchedHoldings, profile, dataField);
        applyDataMappingForPHoldings(holdingsTreeList, dataMapping);
        applyDataMappingOverlay(holdingsTreeList, dataMapping);
        applyDefaultsAndConstants(profile, holdingsTreeList);
    }

    /**
     * @param profile
     * @param holdingsTreeList
     */
    private void applyDefaultsAndConstants(OLEBatchProcessProfileBo profile, List<HoldingsTree> holdingsTreeList) {
        List<OLEBatchProcessProfileConstantsBo> constantsMapping = profile.getOleBatchProcessProfileConstantsList();
        BatchBibImportUtil.buildLocationLevels(holdingsTreeList);
        applyDefaultsAndConstants(holdingsTreeList, constantsMapping, OLEConstants.OLEBatchProcess.DEFAULT);
        applyDefaultsAndConstants(holdingsTreeList, constantsMapping, OLEConstants.OLEBatchProcess.CONSTANT);
        prepareDocContent(holdingsTreeList);
    }

    /**
     * @param profile
     * @param holdingId
     * @param dataField
     * @return
     */
    private Item findMatchingItem(OLEBatchProcessProfileBo profile, String holdingId, BibMarcRecord bibRecord, DataField dataField) {
        List<OLEBatchProcessProfileMatchPoint> itemMatchPointList = BatchBibImportUtil.buildMatchPointListByDataType(profile.getOleBatchProcessProfileMatchPointList(), DocType.ITEM.getCode());

        SearchParams searchParams = new SearchParams();
        StringBuilder matchPointString = new StringBuilder("For Holdings Id :"+holdingId+" and  Item match points -");

        for (OLEBatchProcessProfileMatchPoint matchPoint : itemMatchPointList) {
            matchPointString.append(matchPoint.getMatchPoint());
            String fieldValue = null;
            //Checking  for field value in Item constants
            fieldValue = getFieldValueFromConstantsOrDefaults(matchPoint, OLEConstants.OLEBatchProcess.CONSTANT, DocType.ITEM.getCode());

            // Checking value from  data Mapping
            if (dataField != null && fieldValue == null) {
                for (OLEBatchProcessProfileDataMappingOptionsBo mappingOptionsBo : dataMapping) {
                    if (mappingOptionsBo.getDataTypeDestinationField().equalsIgnoreCase(DocType.ITEM.getCode())) {
                        if (mappingOptionsBo.getDestinationField().equalsIgnoreCase(matchPoint.getMatchPoint())) {
                            String bibField = mappingOptionsBo.getSourceField();
                            fieldValue = BatchBibImportUtil.getSubFieldValue(bibField, dataField);
                        }
                    }
                }
            }

            if (fieldValue == null) {
                //Checking  for field value in Item Defaults
                fieldValue = getFieldValueFromConstantsOrDefaults(matchPoint, OLEConstants.OLEBatchProcess.DEFAULT, DocType.ITEM.getCode());
            }

            if (fieldValue != null) {
                searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(DocType.ITEM.getCode(), matchPoint.getMatchPoint().replaceAll(" ", "").toUpperCase(), fieldValue), "OR"));
            }

        }
        if (searchParams.getSearchConditions().size() > 0) {
            int lastIndex = searchParams.getSearchConditions().size() - 1;
            SearchCondition searchCondition = searchParams.getSearchConditions().get(lastIndex);
            searchCondition.setOperator("AND");
        }

        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField(DocType.ITEM.getCode(), Holdings.HOLDINGSIDENTIFIER, holdingId), "AND"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField(DocType.ITEM.getCode(), Item.ITEMIDENTIFIER));

        List<SearchResult> searchResults = new ArrayList<>();
        try {
            SearchResponse response = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
            searchResults = response.getSearchResults();
        } catch (Exception e) {
            LOG.error("Error :", e);
        }

        if (searchResults.size() > 1) {
            bibImportStatistics.getNoMatchFoundItem().add(matchPointString.toString() + " - " + OLEConstants.OLEBatchProcess.MORE_THAN_ONE_MATCHING_FOUND_FROM_EXISTING_RECORD);
            throw new DocstoreSearchException(OLEConstants.OLEBatchProcess.MORE_THAN_ONE_MATCHING_FOUND_FROM_EXISTING_RECORD);
        }

        if (searchResults.size() == 0) {
            bibImportStatistics.getNoMatchFoundItem().add(matchPointString.toString() + " - " + OLEConstants.OLEBatchProcess.NO_MATCHING_RECORD);
        }

        if (searchResults.size() == 1) {
            Item item = new Item();
            String itemId = searchResults.get(0).getSearchResultFields().get(0).getFieldValue();
            try {
                item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemId);
            } catch (Exception e) {
                bibImportStatistics.getMoreThanOneItemMatched().add(bibRecord);
                LOG.error("Error :", e);
            }
            return item;
        }
        return null;
    }

    /**
     * Buiild list of Items
     *
     * @param profile
     * @param holdings
     * @return
     */
    private List<Item> buildItems(OLEBatchProcessProfileBo profile, Holdings holdings) {
        List<HoldingsTree> holdingsTreeList = BatchBibImportUtil.getHoldingsTrees(holdings);
        buildItemForHoldings(dataMapping, holdingsTreeList, profile);
        for (HoldingsTree holdingsTree : holdingsTreeList) {
            setItemOperation(DocstoreDocument.OperationType.CREATE, holdingsTree);
        }

        return BatchBibImportUtil.getItem(holdingsTreeList);
    }

    /**
     * @param matchedItem
     * @param profile
     * @param dataField
     */
    private void overlayItem(Item matchedItem, OLEBatchProcessProfileBo profile, DataField dataField) {
        applyItemDataMapping(matchedItem, dataField);
        applyDefaultConstantsandSerilazeContent(matchedItem, profile);
    }

    private void applyDefaultConstantsandSerilazeContent(Item matchedItem, OLEBatchProcessProfileBo profile) {
        List<OLEBatchProcessProfileConstantsBo> constantsMapping = profile.getOleBatchProcessProfileConstantsList();
        applyItemDefaultAndConstants(matchedItem, constantsMapping, OLEConstants.OLEBatchProcess.DEFAULT);
        applyItemDefaultAndConstants(matchedItem, constantsMapping, OLEConstants.OLEBatchProcess.CONSTANT);
        matchedItem.serializeContent();
    }

    /**
     * @param item
     * @param dataField
     */

    private void applyItemDataMapping(Item item, DataField dataField) {
        String docType = null;
        String docField = null;

        String bibFieldValue = null;
        if (dataField != null) {
            for (OLEBatchProcessProfileDataMappingOptionsBo oleBatchProcessProfileDataMappingOptionsBo : dataMapping) {
                docType = oleBatchProcessProfileDataMappingOptionsBo.getDataTypeDestinationField();

                if (!docType.equalsIgnoreCase(DocType.ITEM.getCode()))
                    continue;


                docField = oleBatchProcessProfileDataMappingOptionsBo.getDestinationField();
                bibFieldValue =  oleBatchProcessProfileDataMappingOptionsBo.getSourceFieldValue();
                if (docType.equalsIgnoreCase(DocType.ITEM.getCode())) {
                    if (item != null) {
                        item.setField(docField, bibFieldValue);
                    }
                }
            }
        }
    }

    /**
     * @param item
     * @param oleBatchProcessProfileConstantsBoLis
     * @param defaultOrConstant
     */
    private void applyItemDefaultAndConstants(Item item, List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsBoLis, String defaultOrConstant) {

        for (OLEBatchProcessProfileConstantsBo oLEBatchProcessProfileConstantsBo : oleBatchProcessProfileConstantsBoLis) {
            String defaultValue = oLEBatchProcessProfileConstantsBo.getDefaultValue();

            if (!defaultValue.equalsIgnoreCase(defaultOrConstant))
                continue;

            String docType = oLEBatchProcessProfileConstantsBo.getDataType();

            if (!docType.equalsIgnoreCase(DocType.ITEM.getCode()))
                continue;

            String docField = oLEBatchProcessProfileConstantsBo.getAttributeName();
            String fieldValue = oLEBatchProcessProfileConstantsBo.getAttributeValue();
            if (docType.equalsIgnoreCase(DocType.ITEM.getCode())) {
                if (item != null) {
                    if (defaultValue.equalsIgnoreCase(OLEConstants.OLEBatchProcess.CONSTANT)) {
                        item.setField(docField, fieldValue);
                    } else {
                        item.setDefaultField(docField, fieldValue);
                    }

                }
            }
        }
    }


    public OLEBatchBibImportDataObjects processOrderBatch(List<OrderBibMarcRecord> orderBibMarcRecords, OLEBatchProcessProfileBo profile, OLEBatchBibImportStatistics batchBibImportStatistics, String user) {
        OLEBatchBibImportDataObjects oleBatchBibImportDataObjects = new OLEBatchBibImportDataObjects();
        this.userName = user;
        bibImportStatistics = batchBibImportStatistics;
        MatchingProfile matchingProfile = profile.getMatchingProfileObj();
        for (OrderBibMarcRecord orderBibMarcRecord : orderBibMarcRecords) {
            BibMarcRecord bibRecord = orderBibMarcRecord.getBibMarcRecord();
            processBibMarcRecord(profile, oleBatchBibImportDataObjects, matchingProfile, bibRecord);
        }
        return oleBatchBibImportDataObjects;
    }


    private List<OLEEResourceRecordDocument> getEResourceDocumentByName(String title) {
        List<OLEEResourceRecordDocument> oleeResourceRecordDocuments = null;
        Map eResMap = new HashMap();
        eResMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_NAME, title);
        oleeResourceRecordDocuments = (List<OLEEResourceRecordDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEEResourceRecordDocument.class, eResMap);
        return oleeResourceRecordDocuments;
    }

    private List<OLEEResourceRecordDocument> getEResourceDocumentById(String id) {
        List<OLEEResourceRecordDocument> oleeResourceRecordDocuments = null;
        Map eResMap = new HashMap();
        eResMap.put(OLEConstants.OLEEResourceRecord.ERESOURCE_IDENTIFIER, id);
        oleeResourceRecordDocuments = (List<OLEEResourceRecordDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEEResourceRecordDocument.class, eResMap);
        return oleeResourceRecordDocuments;
    }


    private List<OLEPlatformRecordDocument> getPlatformRecordByName(String platformName) {
        List<OLEPlatformRecordDocument> olePlatformRecordDocuments = null;
        Map platformMap = new HashMap();
        platformMap.put(OLEConstants.PLATFORM_NAME, platformName);
        olePlatformRecordDocuments = (List<OLEPlatformRecordDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEPlatformRecordDocument.class, platformMap);
        return olePlatformRecordDocuments;
    }

    private void validateAndSetEResourceId(String eResourceId, EHoldings eHoldings) {
        List<OLEEResourceRecordDocument> oleeResourceRecordDocuments = getEResourceDocumentById(eResourceId);
        if (oleeResourceRecordDocuments != null && oleeResourceRecordDocuments.size() == 1) {
            eHoldings.setField(EHoldings.DESTINATION_FIELD_ERESOURCE_ID, oleeResourceRecordDocuments.get(0).getOleERSIdentifier());
        } else {
            //if eResource name  doesnt exists in OLE, set operation to NONE
            if (oleeResourceRecordDocuments != null && oleeResourceRecordDocuments.size() > 1) {
                setErrorMessage(eHoldings, OLEConstants.ERROR_MESSAGE_MORE_THAN_ONE_ERESOURCE);
            } else {
                setErrorMessage(eHoldings, OLEConstants.ERROR_MESSAGE_ERESOURCE + eResourceId);
            }
        }
    }
}
