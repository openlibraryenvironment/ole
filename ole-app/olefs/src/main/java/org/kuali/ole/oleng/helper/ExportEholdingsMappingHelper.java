package org.kuali.ole.oleng.helper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.helper.InstanceMappingHelper;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.response.OleNGBatchExportResponse;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataMapping;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.impl.SubfieldImpl;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.kuali.ole.OLEConstants.OLEBatchProcess.*;
import static org.kuali.ole.OLEConstants.OLEBatchProcess.TIME_STAMP;

/**
 * Created by rajeshbabuk on 4/22/16.
 */
public class ExportEholdingsMappingHelper extends ExportHoldingsMappingHelper {
    private List<DataField> dataFieldList = new ArrayList<DataField>();
    private StringBuilder errBuilder;

    private static final Logger LOG = Logger.getLogger(InstanceMappingHelper.class);
    private HoldingOlemlRecordProcessor workEHoldingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();

    public List<DataField> generateDataFieldForEHolding(HoldingsTree holdingsTree, BatchProcessProfile profile) throws Exception {
        dataFieldList.clear();
        if (holdingsTree != null) {
            OleHoldings oleHoldings = null;
            if (holdingsTree.getHoldings().getContentObject() != null) {
                oleHoldings= holdingsTree.getHoldings().getContentObject();
            } else {
                oleHoldings = workEHoldingOlemlRecordProcessor.fromXML(holdingsTree.getHoldings().getContent());
                oleHoldings.setHoldingsIdentifier(holdingsTree.getHoldings().getId());
            }
            Map<String, String> dataFieldEHoldingMap = new LinkedHashMap<>();
            Map<String, String> dataFieldCoverageMap = new HashMap<>();
            Map<String, String> dataFieldsDonorMap = new HashMap<>();
            List<BatchProfileDataMapping> mappingOptionsBoList = profile.getBatchProfileDataMappingList();
            for (BatchProfileDataMapping mappingOptionsBo : mappingOptionsBoList) {
                String key=buildkey(mappingOptionsBo);
                if (mappingOptionsBo.getDestination().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_PROCESS_PROFILE_DATATYPE_EHOLDINGS)) {
                    if (mappingOptionsBo.getField().equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_START_DATE)
                            || mappingOptionsBo.getField().equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_END_DATE)
                            || mappingOptionsBo.getField().equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_START_ISSUE)
                            || mappingOptionsBo.getField().equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_END_ISSUE)
                            || mappingOptionsBo.getField().equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_START_VOLUME)
                            || mappingOptionsBo.getField().equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_END_VOLUME)) {
                        dataFieldCoverageMap.put(key, mappingOptionsBo.getField());
                    } else if (mappingOptionsBo.getField().equalsIgnoreCase(DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY)
                            || mappingOptionsBo.getField().equalsIgnoreCase(DESTINATION_FIELD_DONOR_NOTE)
                            || mappingOptionsBo.getField().equalsIgnoreCase(DESTINATION_FIELD_DONOR_CODE)) {
                        dataFieldsDonorMap.put(key, mappingOptionsBo.getField());
                    } else {
                        dataFieldEHoldingMap.put(key, mappingOptionsBo.getField());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(dataFieldEHoldingMap) || !CollectionUtils.isEmpty(dataFieldCoverageMap) || !CollectionUtils.isEmpty(dataFieldsDonorMap)) {
                boolean isStaffOnly = false;
                if(profile.getExportScope().equalsIgnoreCase(OleNGConstants.INCREMENTAL_EXCEPT_STAFF_ONLY) ||
                        profile.getExportScope().equalsIgnoreCase(OleNGConstants.FULL_EXCEPT_STAFF_ONLY)) {
                    if(holdingsTree.getHoldings()!=null && holdingsTree.getHoldings().isStaffOnly()) {
                        isStaffOnly=true;
                    }
                }
                if(!isStaffOnly) {
                    generateSubFieldsForEHolding(oleHoldings, dataFieldEHoldingMap, dataFieldCoverageMap, dataFieldsDonorMap);
                }
            } else {
                return Collections.EMPTY_LIST;
            }
        }
        return dataFieldList;
    }

    protected void generateSubFieldsForEHolding(OleHoldings oleHoldings, Map<String, String> dataFieldEHoldingMap, Map<String, String> dataFieldCoverageMap, Map<String, String> dataFieldsDonorMap) throws Exception {
        List<DataField> linkList = new ArrayList<>();
        try {
            DataField dataField;
            for (Map.Entry<String, String> entry : dataFieldEHoldingMap.entrySet()) {
                if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LINK_URL)) {
                    for (Link link : oleHoldings.getLink()) {
                        if (StringUtils.isNotEmpty(link.getUrl())) {
                            dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                            if (dataField == null) {
                                dataField = getDataField(entry);
                                generateLink(oleHoldings, link, getCode(entry.getKey()), dataField);
                                if (dataFieldEHoldingMap.containsValue(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LINK_TEXT)) {
                                    for (Map.Entry<String, String> dataMapEntry : dataFieldEHoldingMap.entrySet()) {
                                        if (dataMapEntry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LINK_TEXT)) {
                                            if (StringUtils.isNotEmpty(link.getText())) {
                                                generateLinkText(oleHoldings, link, getCode(dataMapEntry.getKey()), dataField);
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                            } else {
                                generateLink(oleHoldings, link, getCode(entry.getKey()), dataField);
                                if (dataFieldEHoldingMap.containsValue(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LINK_TEXT)) {
                                    for (Map.Entry<String, String> dataMapEntry : dataFieldEHoldingMap.entrySet()) {
                                        if (dataMapEntry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LINK_TEXT)) {
                                            if (StringUtils.isNotEmpty(link.getText())) {
                                                generateLinkText(oleHoldings, link, getCode(dataMapEntry.getKey()), dataField);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }
            for (Map.Entry<String, String> entry : dataFieldEHoldingMap.entrySet()) {

                if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.LOCAL_IDENTIFIER)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateHoldingLocalIdentifier(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateHoldingLocalIdentifier(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumber(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateCallNumber(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_HOLDING_CALL_NUMBER_TYPE)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumberType(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateCallNumberType(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_1)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel1(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel1(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_2)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel2(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel2(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_3)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel3(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel3(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_4)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel4(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel4(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_5)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel5(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel5(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumberPrefix(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateCallNumberPrefix(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                }  else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_PERSISTENTLINK)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generatePersistentLink(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generatePersistentLink(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_STATISTICAL_CODE)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateStatisticalCode(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateStatisticalCode(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ACCESS_STATUS_CODE)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateAccessStatus(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateAccessStatus(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                }else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_MATIRIAL_SPECIFIED)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateHoldingsAccesssInformation(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateHoldingsAccesssInformation(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_FIRST_INDICATOR)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateHoldingsAccesssInformationFI(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateHoldingsAccesssInformationFI(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_SECOND_INICATOR)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateHoldingsAccesssInformationSI(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateHoldingsAccesssInformationSI(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_PLATFORM)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generatePlatform(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generatePlatform(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_PUBLIC_DISPLAY_NOTE)) {
                    for (Note note : oleHoldings.getNote()) {
                        if (StringUtils.isNotEmpty(note.getType()) && note.getType().equalsIgnoreCase(OLEConstants.NOTE_TYPE)) {
                            dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                            if (dataField == null) {
                                dataField = getDataField(entry);
                                generatePublicDisplayNote(oleHoldings, note, getCode(entry.getKey()), dataField);
                                if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                            } else {
                                generatePublicDisplayNote(oleHoldings, note, getCode(entry.getKey()), dataField);
                            }
                        }
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_PUBLIC_NOTE)) {
                    for (Note note : oleHoldings.getNote()) {
                        if (StringUtils.isNotEmpty(note.getType()) && note.getType().equalsIgnoreCase(OLEConstants.NOTE_TYPE)) {
                            dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                            if (dataField == null) {
                                dataField = getDataField(entry);
                                generatePublicDisplayNote(oleHoldings, note, getCode(entry.getKey()), dataField);
                                if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                            } else {
                                generatePublicDisplayNote(oleHoldings, note, getCode(entry.getKey()), dataField);
                            }
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(dataFieldCoverageMap)) {
                generateCoverageFields(oleHoldings, dataFieldCoverageMap);
            }
            if (!CollectionUtils.isEmpty(linkList)) {
                dataFieldList.addAll(linkList);
            }
            if (!CollectionUtils.isEmpty(dataFieldsDonorMap)) {
                generateDonorFields(oleHoldings, dataFieldsDonorMap);
            }
        } catch (Exception ex) {
            LOG.error("Error while mapping oleHoldings data ::" + oleHoldings.getHoldingsIdentifier(), ex);
            buildError(ERR_INSTANCE, oleHoldings.getHoldingsIdentifier(), ERR_CAUSE, ex.getMessage(), TIME_STAMP, new Date().toString());
            throw ex;
        }
    }

    private void generateCoverageFields(OleHoldings oleHoldings, Map<String, String> dataFieldCoverageMap) throws Exception {
        List<DataField> coverageFieldList = new ArrayList<>();
        try {
            if (!CollectionUtils.isEmpty(oleHoldings.getExtentOfOwnership())) {
                for (ExtentOfOwnership extentOfOwnership : oleHoldings.getExtentOfOwnership()) {
                    if (null != extentOfOwnership.getCoverages() && !CollectionUtils.isEmpty(extentOfOwnership.getCoverages().getCoverage())) {
                        for (Coverage coverage : extentOfOwnership.getCoverages().getCoverage()) {
                            for (Map.Entry<String, String> entry : dataFieldCoverageMap.entrySet()) {
                                DataField dataField;
                                if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COVERAGE_START_DATE)) {
                                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                                    if (dataField == null) {
                                        dataField = getDataField(entry);
                                        generateCoverageStartDate(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                                    } else {
                                        generateCoverageStartDate(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                    }
                                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COVERAGE_END_DATE)) {
                                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                                    if (dataField == null) {
                                        dataField = getDataField(entry);
                                        generateCoverageEndDate(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                                    } else {
                                        generateCoverageEndDate(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                    }
                                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COVERAGE_START_ISSUE)) {
                                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                                    if (dataField == null) {
                                        dataField = getDataField(entry);
                                        generateCoverageStartIssue(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                                    } else {
                                        generateCoverageStartIssue(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                    }
                                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COVERAGE_END_ISSUE)) {
                                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                                    if (dataField == null) {
                                        dataField = getDataField(entry);
                                        generateCoverageEndIssue(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                                    } else {
                                        generateCoverageEndIssue(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                    }
                                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COVERAGE_START_VOLUME)) {
                                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                                    if (dataField == null) {
                                        dataField = getDataField(entry);
                                        generateCoverageStartVolume(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                                    } else {
                                        generateCoverageStartVolume(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                    }
                                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COVERAGE_END_VOLUME)) {
                                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                                    if (dataField == null) {
                                        dataField = getDataField(entry);
                                        generateCoverageEndVolume(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                        if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                                    } else {
                                        generateCoverageEndVolume(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                    }
                                }
                            }
                            dataFieldList.addAll(coverageFieldList);
                            coverageFieldList.clear();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Error while mapping oleHoldings data ::" + oleHoldings.getHoldingsIdentifier(), ex);
            buildError(ERR_INSTANCE, oleHoldings.getHoldingsIdentifier(), ERR_CAUSE, ex.getMessage(), TIME_STAMP, new Date().toString());
            throw ex;
        }

    }

    private void generateDonorFields(OleHoldings oleHoldings, Map<String, String> dataFieldsDonorMap) throws Exception {
        List<DataField> donorFieldList = new ArrayList<>();
        try {
            if (!CollectionUtils.isEmpty(oleHoldings.getDonorInfo())) {
                for (DonorInfo donorInfo : oleHoldings.getDonorInfo()) {
                    for (Map.Entry<String, String> entry : dataFieldsDonorMap.entrySet()) {
                        DataField dataField;
                        if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY)) {
                            dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                            if (dataField == null) {
                                dataField = getDataField(entry);
                                generateDonorPublicDisplay(oleHoldings, donorInfo, getCode(entry.getKey()), dataField);
                                if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                            } else {
                                generateDonorPublicDisplay(oleHoldings, donorInfo, getCode(entry.getKey()), dataField);
                            }
                        } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_NOTE)) {
                            dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                            if (dataField == null) {
                                dataField = getDataField(entry);
                                generateDonorNote(oleHoldings, donorInfo, getCode(entry.getKey()), dataField);
                                if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                            } else {
                                generateDonorNote(oleHoldings, donorInfo, getCode(entry.getKey()), dataField);
                            }
                        } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_CODE)) {
                            dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                            if (dataField == null) {
                                dataField = getDataField(entry);
                                generateDonorCode(oleHoldings, donorInfo, getCode(entry.getKey()), dataField);
                                if (!dataField.getSubfields().isEmpty()) dataFieldList.add(dataField);
                            } else {
                                generateDonorCode(oleHoldings, donorInfo, getCode(entry.getKey()), dataField);
                            }
                        }
                    }
                    dataFieldList.addAll(donorFieldList);
                    donorFieldList.clear();
                }
            }
        } catch (Exception ex) {
            LOG.error("Error while mapping oleHoldings data ::" + oleHoldings.getHoldingsIdentifier(), ex);
            buildError(ERR_INSTANCE, oleHoldings.getHoldingsIdentifier(), ERR_CAUSE, ex.getMessage(), TIME_STAMP, new Date().toString());
            throw ex;
        }
    }

    /**
     * generates the Link for the given oleHoldings
     * @param oleHoldings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateLink(OleHoldings oleHoldings, Link link, char code, DataField dataField) throws Exception {
        Subfield subField = new SubfieldImpl();
        subField.setCode(code);
        try {
            if (link != null && null != link.getUrl()) {
                subField.setData(link.getUrl());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateLink()");
        }

    }

    /**
     * generates the persistent link for the given oleHoldings
     * @param oleHoldings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generatePersistentLink(OleHoldings oleHoldings, char code, DataField dataField) throws Exception {
        Subfield subField = new SubfieldImpl();
        subField.setCode(code);
        try {
            if (oleHoldings != null && oleHoldings.getLocalPersistentLink() != null && StringUtils.isNotEmpty(oleHoldings.getLocalPersistentLink())) {
                subField.setData(oleHoldings.getLocalPersistentLink());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generatePersistentLink()");
        }

    }

    /**
     * generates the Link Text for the given oleHoldings
     * @param oleHoldings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateLinkText(OleHoldings oleHoldings,Link link, char code, DataField dataField) throws Exception {
        Subfield subField = new SubfieldImpl();
        subField.setCode(code);
        try {
            if (link != null && null != link.getText()) {
                subField.setData(link.getText());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateLinkText()");
        }

    }

    /**
     * generates the subfields for the Statistical Code for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateStatisticalCode(OleHoldings oleHoldings, char code, DataField dataField) throws Exception {
        try {
            if (oleHoldings != null && oleHoldings.getStatisticalSearchingCode() != null && oleHoldings.getStatisticalSearchingCode().getCodeValue() != null) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(oleHoldings.getStatisticalSearchingCode().getCodeValue());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateStatisticalCode()");
        }
    }

    /**
     * generates the subfields for the Access Status for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateAccessStatus(OleHoldings oleHoldings, char code, DataField dataField) throws Exception {
        try {
            if (oleHoldings != null && oleHoldings.getAccessStatus() != null) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(oleHoldings.getAccessStatus());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateAccessStatus()");
        }
    }

    /**
     * generates the subfields for the Platform for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generatePlatform(OleHoldings oleHoldings, char code, DataField dataField) throws Exception {
        try {
            if (oleHoldings != null && oleHoldings.getPlatform() != null && null != oleHoldings.getPlatform().getPlatformName()) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(oleHoldings.getPlatform().getPlatformName());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generatePlatform()");
        }
    }

    /**
     * generates the subfields for the Coverage Start Date for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateCoverageStartDate(OleHoldings oleHoldings, Coverage coverage, char code, DataField dataField) throws Exception {
        try {
            if (null != coverage) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(coverage.getCoverageStartDate());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateCoverageStartDate()");
        }
    }

    /**
     * generates the subfields for the Coverage End Date for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateCoverageEndDate(OleHoldings oleHoldings, Coverage coverage, char code, DataField dataField) throws Exception {
        try {
            if (null != coverage) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(coverage.getCoverageEndDate());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateCoverageEndDate()");
        }
    }

    /**
     * generates the subfields for the Coverage Start Volume for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateCoverageStartVolume(OleHoldings oleHoldings, Coverage coverage, char code, DataField dataField) throws Exception {
        try {
            if (null != coverage) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(coverage.getCoverageStartVolume());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateCoverageStartVolume()");
        }
    }

    /**
     * generates the subfields for the Coverage End Volume for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateCoverageEndVolume(OleHoldings oleHoldings, Coverage coverage, char code, DataField dataField) throws Exception {
        try {
            if (null != coverage) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(coverage.getCoverageEndVolume());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateCoverageEndVolume()");
        }
    }

    /**
     * generates the subfields for the Coverage Start Issue for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateCoverageStartIssue(OleHoldings oleHoldings, Coverage coverage, char code, DataField dataField) throws Exception {
        try {
            if (null != coverage) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(coverage.getCoverageStartIssue());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateCoverageStartIssue()");
        }
    }

    /**
     * generates the subfields for the Coverage End Issue for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateCoverageEndIssue(OleHoldings oleHoldings, Coverage coverage, char code, DataField dataField) throws Exception {
        try {
            if (null != coverage) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(coverage.getCoverageEndIssue());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateCoverageEndIssue()");
        }
    }

    /**
     * generates the subfields for the Coverage End Issue for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generatePublicDisplayNote(OleHoldings oleHoldings, Note note, char code, DataField dataField) throws Exception {
        try {
            if (null != oleHoldings && null != note && StringUtils.isNotBlank(note.getValue())) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(note.getValue());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generatePublicDisplayNote()");
        }
    }

    /**
     * generates the subfield for the donor public display for the given EHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateDonorPublicDisplay(OleHoldings oleHoldings, DonorInfo donorInfo, char code, DataField dataField) throws Exception {
        try {
            if (null != donorInfo && StringUtils.isNotBlank(donorInfo.getDonorPublicDisplay())) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(donorInfo.getDonorPublicDisplay());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateDonorPublicDisplay()");
        }
    }

    /**
     * generates the subfield for the donor note for the given EHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateDonorNote(OleHoldings oleHoldings, DonorInfo donorInfo, char code, DataField dataField) throws Exception {
        try {
            if (null != donorInfo && StringUtils.isNotBlank(donorInfo.getDonorNote())) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(donorInfo.getDonorNote());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateDonorNote()");
        }
    }

    /**
     * generates the subfield for the donor code for the given EHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateDonorCode(OleHoldings oleHoldings, DonorInfo donorInfo, char code, DataField dataField) throws Exception {
        try {
            if (null != donorInfo && StringUtils.isNotBlank(donorInfo.getDonorCode())) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(donorInfo.getDonorCode());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateDonorCode()");
        }
    }

    /**
     * @param errorString
     */
    private void buildError(String... errorString) {
        for (String str : errorString) {
            errBuilder.append(str).append(COMMA);
        }
        errBuilder.append(lineSeparator);
    }

    /**
     * Logs error for exception happening for oleHoldings mapping
     *
     * @param oleHoldings
     * @param ex
     */
    private void logError(OleHoldings oleHoldings, Exception ex, String method) throws Exception {
        if (oleHoldings != null) {
            LOG.error("Error while " + method + " for eholding::" + oleHoldings.getHoldingsIdentifier(), ex);
            buildError(ERR_HOLDING, oleHoldings.getHoldingsIdentifier(), ERR_CAUSE, ex.getMessage(), " ::At:: ", method, TIME_STAMP, new Date().toString());
            throw ex;
        } else {
            LOG.error("Error while " + method + " for OleHoldings::" + oleHoldings.getHoldingsIdentifier(), ex);
            throw ex;
        }
    }

    private void generateHoldingsAccesssInformationFI(OleHoldings oleHoldings, char code, DataField dataField) throws Exception {
        try {
            if (oleHoldings != null && oleHoldings.getHoldingsAccessInformation() != null) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(oleHoldings.getHoldingsAccessInformation().getFirstIndicator());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateHoldingsAccesssInformationFI()");
        }
    }

    private void generateHoldingsAccesssInformationSI(OleHoldings oleHoldings, char code, DataField dataField) throws Exception {
        try {
            if (oleHoldings != null && oleHoldings.getHoldingsAccessInformation() != null) {
                Subfield subField =  new SubfieldImpl();
                subField.setCode(code);
                subField.setData(oleHoldings.getHoldingsAccessInformation().getSecondIndicator());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateHoldingsAccesssInformationSI()");
        }
    }


    private void generateHoldingsAccesssInformation(OleHoldings oleHoldings, char code, DataField dataField) throws Exception {
        try {
            if (oleHoldings != null && oleHoldings.getHoldingsAccessInformation() != null && oleHoldings.getHoldingsAccessInformation().getMaterialsSpecified() != null) {
                Subfield subField = new SubfieldImpl();
                subField.setCode(code);
                subField.setData(oleHoldings.getHoldingsAccessInformation().getMaterialsSpecified());
                dataField.addSubfield(subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateHoldingsAccesssInformation()");
        }
    }

}
