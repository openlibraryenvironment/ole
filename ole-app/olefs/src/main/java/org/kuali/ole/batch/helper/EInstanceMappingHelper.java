package org.kuali.ole.batch.helper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileDataMappingOptionsBo;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.kuali.ole.OLEConstants.OLEBatchProcess.*;
import static org.kuali.ole.OLEConstants.OLEBatchProcess.lineSeparator;

/**
 * Created with IntelliJ IDEA.
 * User: meenrajd
 * Date: 9/2/13
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class EInstanceMappingHelper {
    private List<DataField> dataFieldList = new ArrayList<DataField>();
    private StringBuilder errBuilder;

    private static final Logger LOG = Logger.getLogger(InstanceMappingHelper.class);
    private HoldingOlemlRecordProcessor workEHoldingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
    
    /*public List<DataField> generateDataField(InstanceCollection instanceCollection, OLEBatchProcessProfileBo profile, StringBuilder errBuilder) throws Exception {
        dataFieldList.clear();
        this.errBuilder = errBuilder;
        if (instanceCollection != null && instanceCollection.getInstance() != null) {
            OleHoldings oleHoldings;
            String dataFieldInstance = null;
            List<OLEBatchProcessProfileDataMappingOptionsBo> mappingOptionsBoList = profile.getOleBatchProcessProfileMappingOptionsList().get(0).getOleBatchProcessProfileDataMappingOptionsBoList();

            for (OLEBatchProcessProfileDataMappingOptionsBo mappingOptionsBo : mappingOptionsBoList) {
                if (mappingOptionsBo.getDataType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_PROCESS_PROFILE_DATATYPE_EHOLDINGS)) {
                    dataFieldInstance = getTag(mappingOptionsBo.getDestinationField());
                }
            }
            if (dataFieldInstance == null) return Collections.EMPTY_LIST; //no subfields would be created for the bib

            for (int i = 0; i < instanceCollection.getInstance().size(); i++) {
                if ((oleHoldings = instanceCollection.getInstance().get(i)) == null) continue;
                if (oleHoldings == null) continue;
                DataField dataField = new DataField();
                dataField.setTag(dataFieldInstance);
                dataField.setInd1(" ");
                dataField.setInd2(" ");
                generateSubFields(mappingOptionsBoList, oleHoldings, dataField);
                if (!dataField.getSubFields().isEmpty())
                    dataFieldList.add(dataField);

            }
        }
        return dataFieldList;
    }*/

    public List<DataField> generateDataFieldForEHolding(HoldingsTree holdingsTree, OLEBatchProcessProfileBo profile, StringBuilder errBuilder) throws Exception {
        dataFieldList.clear();
        this.errBuilder = errBuilder;        
        if (holdingsTree != null) {
            OleHoldings oleHoldings = null;
            if (holdingsTree.getHoldings().getContentObject() != null) {
                oleHoldings= holdingsTree.getHoldings().getContentObject();
            } else {
                oleHoldings = workEHoldingOlemlRecordProcessor.fromXML(holdingsTree.getHoldings().getContent());
            }
            Map<String, String> dataFieldEHoldingMap = new HashMap<>();
            Map<String, String> dataFieldCoverageMap = new HashMap<>();
            Map<String, String> dataFieldsDonorMap = new HashMap<>();
            List<OLEBatchProcessProfileDataMappingOptionsBo> mappingOptionsBoList = profile.getOleBatchProcessProfileMappingOptionsList().get(0).getOleBatchProcessProfileDataMappingOptionsBoList();
            for (OLEBatchProcessProfileDataMappingOptionsBo mappingOptionsBo : mappingOptionsBoList) {
                if (mappingOptionsBo.getDataType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_PROCESS_PROFILE_DATATYPE_EHOLDINGS)) {
                    if (mappingOptionsBo.getSourceField().equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_START_DATE)
                            || mappingOptionsBo.getSourceField().equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_END_DATE)
                            || mappingOptionsBo.getSourceField().equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_START_ISSUE)
                            || mappingOptionsBo.getSourceField().equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_END_ISSUE)
                            || mappingOptionsBo.getSourceField().equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_START_VOLUME)
                            || mappingOptionsBo.getSourceField().equalsIgnoreCase(DESTINATION_FIELD_COVERAGE_END_VOLUME)) {
                        dataFieldCoverageMap.put(mappingOptionsBo.getDestinationField(), mappingOptionsBo.getSourceField());
                    } else if (mappingOptionsBo.getSourceField().equalsIgnoreCase(DESTINATION_FIELD_DONOR_PUBLIC_DISPLAY)
                            || mappingOptionsBo.getSourceField().equalsIgnoreCase(DESTINATION_FIELD_DONOR_NOTE)
                            || mappingOptionsBo.getSourceField().equalsIgnoreCase(DESTINATION_FIELD_DONOR_CODE)) {
                        dataFieldsDonorMap.put(mappingOptionsBo.getDestinationField(), mappingOptionsBo.getSourceField());
                    } else {
                        dataFieldEHoldingMap.put(mappingOptionsBo.getDestinationField(), mappingOptionsBo.getSourceField());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(dataFieldEHoldingMap) || !CollectionUtils.isEmpty(dataFieldCoverageMap) || !CollectionUtils.isEmpty(dataFieldsDonorMap)) {
                generateSubFieldsForEHolding(oleHoldings, dataFieldEHoldingMap, dataFieldCoverageMap, dataFieldsDonorMap);
            } else {
                return Collections.EMPTY_LIST;
            }
        }
        return dataFieldList;
    }

    protected void generateSubFieldsForEHolding(OleHoldings oleHoldings, Map<String, String> dataFieldEHoldingMap, Map<String, String> dataFieldCoverageMap, Map<String, String> dataFieldsDonorMap) throws Exception {
        try {
            for (Map.Entry<String, String> entry : dataFieldEHoldingMap.entrySet()) {
                DataField dataField;
                if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumber(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateCallNumber(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_HOLDING_CALL_NUMBER_TYPE)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumberType(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateCallNumberType(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_1)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel1(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel1(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_2)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel2(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel2(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_3)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel3(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel3(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_4)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel4(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel4(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_5)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateLocationLevel5(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateLocationLevel5(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateCallNumberPrefix(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateCallNumberPrefix(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LINK_URL)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        for (Link link : oleHoldings.getLink()) {
                            generateLink(oleHoldings, link, getCode(entry.getKey()), dataField);
                            if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                        }
                    }else {
                        for (Link link : oleHoldings.getLink()) {
                            generateLink(oleHoldings, link, getCode(entry.getKey()), dataField);
                        }
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_PERSISTENTLINK)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generatePersistentLink(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generatePersistentLink(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LINK_TEXT)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        for (Link link : oleHoldings.getLink()) {
                            generateLinkText(oleHoldings, link, getCode(entry.getKey()), dataField);
                            if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                        }
                    }else{
                        for (Link link : oleHoldings.getLink()) {
                            generateLinkText(oleHoldings, link, getCode(entry.getKey()), dataField);
                        }
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_STATISTICAL_CODE)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateStatisticalCode(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateStatisticalCode(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_ACCESS_STATUS_CODE)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateAccessStatus(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateAccessStatus(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                }
                else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_MATIRIAL_SPECIFIED)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateHoldingsAccesssInformation(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateHoldingsAccesssInformation(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_FIRST_INDICATOR)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateHoldingsAccesssInformationFI(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateHoldingsAccesssInformationFI(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_SECOND_INICATOR)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generateHoldingsAccesssInformationSI(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
                    } else {
                        generateHoldingsAccesssInformationSI(oleHoldings, getCode(entry.getKey()), dataField);
                    }
                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_PLATFORM)) {
                    dataField = checkDataField(dataFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                    if (dataField == null) {
                        dataField = getDataField(entry);
                        generatePlatform(oleHoldings, getCode(entry.getKey()), dataField);
                        if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
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
                                if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
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
                                if (!dataField.getSubFields().isEmpty()) dataFieldList.add(dataField);
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
            if (!CollectionUtils.isEmpty(dataFieldsDonorMap)) {
                generateDonorFields(oleHoldings, dataFieldsDonorMap);
            }
        } catch (Exception ex) {
            LOG.error("Error while mapping oleHoldings data ::" + oleHoldings.getHoldingsIdentifier(), ex);
            buildError(ERR_INSTANCE, oleHoldings.getHoldingsIdentifier(), ERR_CAUSE, ex.getMessage(), TIME_STAMP, new Date().toString());
            throw ex;
        }
    }

    private void generateHoldingsAccesssInformationFI(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        try {
            if (oleHoldings != null && oleHoldings.getHoldingsAccessInformation() != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(oleHoldings.getHoldingsAccessInformation().getFirstIndicator());
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateHoldingsAccesssInformation()");
        }
    }

    private void generateHoldingsAccesssInformationSI(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        try {
            if (oleHoldings != null && oleHoldings.getHoldingsAccessInformation() != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(oleHoldings.getHoldingsAccessInformation().getSecondIndicator());
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateHoldingsAccesssInformation()");
        }
    }


    private void generateHoldingsAccesssInformation(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        try {
            if (oleHoldings != null && oleHoldings.getHoldingsAccessInformation() != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(oleHoldings.getHoldingsAccessInformation().getMaterialsSpecified());
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateHoldingsAccesssInformation()");
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
                                    dataField = checkDataField(coverageFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                                    if (dataField == null) {
                                        dataField = getDataField(entry);
                                        generateCoverageStartDate(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                        if (!dataField.getSubFields().isEmpty()) coverageFieldList.add(dataField);
                                    } else {
                                        generateCoverageStartDate(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                    }
                                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COVERAGE_END_DATE)) {
                                    dataField = checkDataField(coverageFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                                    if (dataField == null) {
                                        dataField = getDataField(entry);
                                        generateCoverageEndDate(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                        if (!dataField.getSubFields().isEmpty()) coverageFieldList.add(dataField);
                                    } else {
                                        generateCoverageEndDate(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                    }
                                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COVERAGE_START_ISSUE)) {
                                    dataField = checkDataField(coverageFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                                    if (dataField == null) {
                                        dataField = getDataField(entry);
                                        generateCoverageStartIssue(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                        if (!dataField.getSubFields().isEmpty()) coverageFieldList.add(dataField);
                                    } else {
                                        generateCoverageStartIssue(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                    }
                                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COVERAGE_END_ISSUE)) {
                                    dataField = checkDataField(coverageFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                                    if (dataField == null) {
                                        dataField = getDataField(entry);
                                        generateCoverageEndIssue(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                        if (!dataField.getSubFields().isEmpty()) coverageFieldList.add(dataField);
                                    } else {
                                        generateCoverageEndIssue(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                    }
                                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COVERAGE_START_VOLUME)) {
                                    dataField = checkDataField(coverageFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                                    if (dataField == null) {
                                        dataField = getDataField(entry);
                                        generateCoverageStartVolume(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                        if (!dataField.getSubFields().isEmpty()) coverageFieldList.add(dataField);
                                    } else {
                                        generateCoverageStartVolume(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                    }
                                } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_COVERAGE_END_VOLUME)) {
                                    dataField = checkDataField(coverageFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                                    if (dataField == null) {
                                        dataField = getDataField(entry);
                                        generateCoverageEndVolume(oleHoldings, coverage, getCode(entry.getKey()), dataField);
                                        if (!dataField.getSubFields().isEmpty()) coverageFieldList.add(dataField);
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
                            dataField = checkDataField(donorFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                            if (dataField == null) {
                                dataField = getDataField(entry);
                                generateDonorPublicDisplay(oleHoldings, donorInfo, getCode(entry.getKey()), dataField);
                                if (!dataField.getSubFields().isEmpty()) donorFieldList.add(dataField);
                            } else {
                                generateDonorPublicDisplay(oleHoldings, donorInfo, getCode(entry.getKey()), dataField);
                            }
                        } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_NOTE)) {
                            dataField = checkDataField(donorFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                            if (dataField == null) {
                                dataField = getDataField(entry);
                                generateDonorNote(oleHoldings, donorInfo, getCode(entry.getKey()), dataField);
                                if (!dataField.getSubFields().isEmpty()) donorFieldList.add(dataField);
                            } else {
                                generateDonorNote(oleHoldings, donorInfo, getCode(entry.getKey()), dataField);
                            }
                        } else if (entry.getValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_DONOR_CODE)) {
                            dataField = checkDataField(donorFieldList, StringUtils.trim(entry.getKey()).substring(0, 3));
                            if (dataField == null) {
                                dataField = getDataField(entry);
                                generateDonorCode(oleHoldings, donorInfo, getCode(entry.getKey()), dataField);
                                if (!dataField.getSubFields().isEmpty()) donorFieldList.add(dataField);
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

    protected DataField checkDataField(List<DataField> dataFieldList, String dataField) {
        for (DataField field : dataFieldList) {
            if (dataField.equals(field.getTag())) {
                return field;
            }
        }
        return null;
    }

    protected DataField getDataField(Map.Entry<String, String> entry) {
        DataField dataField = new DataField();
        dataField.setTag(StringUtils.trim(entry.getKey()).substring(0, 3));
        dataField.setInd1(" ");
        dataField.setInd2(" ");
        return dataField;
    }

    /**
     * generates the subfields for the given oleHoldings and mapping options
     *
     * @param mappingOptionsBoList
     * @param oleHoldings
     * @param dataField
     */
    protected void generateSubFields(List<OLEBatchProcessProfileDataMappingOptionsBo> mappingOptionsBoList, OleHoldings oleHoldings, DataField dataField) throws Exception {
        try {
            for (OLEBatchProcessProfileDataMappingOptionsBo mappingField : mappingOptionsBoList) {
                if (!mappingField.getDataType().equalsIgnoreCase(OLEConstants.OLEBatchProcess.BATCH_PROCESS_PROFILE_DATATYPE_EHOLDINGS)) continue;
                if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER)) {
                    generateCallNumber(oleHoldings, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_HOLDING_CALL_NUMBER_TYPE)) {
                    generateCallNumberType(oleHoldings, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_1)) {
                    generateLocationLevel1(oleHoldings, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_2)) {
                    generateLocationLevel2(oleHoldings, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_3)) {
                    generateLocationLevel3(oleHoldings, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_4)) {
                    generateLocationLevel4(oleHoldings, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LOCATION_LEVEL_5)) {
                    generateLocationLevel5(oleHoldings, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_CALL_NUMBER_TYPE_PREFIX)) {
                    generateCallNumberPrefix(oleHoldings, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LINK_URL)) {
                    //generateLink(oleHoldings, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_PERSISTENTLINK)) {
                    generatePersistentLink(oleHoldings, getCode(mappingField.getDestinationField()), dataField);
                } else if (mappingField.getSourceField().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DESTINATION_FIELD_LINK_TEXT)) {
                    //generateLinkText(oleHoldings, getCode(mappingField.getDestinationField()), dataField);
                }
            }
        } catch (Exception ex) {
            LOG.error("Error while mapping oleHoldings data ::" + oleHoldings.getHoldingsIdentifier(), ex);
            buildError(ERR_INSTANCE, oleHoldings.getHoldingsIdentifier(), ERR_CAUSE, ex.getMessage(), TIME_STAMP, new Date().toString());
            throw ex;
        }
    }

    private void addDataField(DataField dataField, SubField subField) {
        if (StringUtils.isEmpty(subField.getValue())) return;
        dataField.getSubFields().add(subField);
    }

    protected String getTag(String mappingField) {
        return StringUtils.trim(StringUtils.substringBefore(mappingField, "$"));
    }

    private String getCode(String mappingField) {
        return StringUtils.trim(StringUtils.substringAfter(mappingField, "$"));
    }

    /**
     * generates the subfields for call number for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateCallNumber(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        SubField subField = new SubField();
        subField.setCode(code);
        try {
            if (oleHoldings != null && oleHoldings.getCallNumber() != null && StringUtils.isNotEmpty(oleHoldings.getCallNumber().getNumber())) {
                subField.setValue(oleHoldings.getCallNumber().getNumber());
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateCallNumber()");
        }

    }
    /**
     * generates the call number prefix for the given oleHoldings
     * @param oleHoldings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateCallNumberPrefix(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        SubField subField = new SubField();
        subField.setCode(code);
        try {
            if (oleHoldings != null && oleHoldings.getCallNumber() != null && StringUtils.isNotEmpty(oleHoldings.getCallNumber().getPrefix())) {
                subField.setValue(oleHoldings.getCallNumber().getPrefix());
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateCallNumberPrefix()");
        }

    }

    /**
     * generates the Link for the given oleHoldings
     * @param oleHoldings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateLink(OleHoldings oleHoldings, Link link, String code, DataField dataField) throws Exception {
        SubField subField = new SubField();
        subField.setCode(code);
        try {
            if (link != null && null != link.getUrl()) {
                subField.setValue(link.getUrl());
                addDataField(dataField, subField);
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
    private void generatePersistentLink(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        SubField subField = new SubField();
        subField.setCode(code);
        try {
            if (oleHoldings != null && oleHoldings.getLocalPersistentLink() != null && StringUtils.isNotEmpty(oleHoldings.getLocalPersistentLink())) {
                subField.setValue(oleHoldings.getLocalPersistentLink());
                addDataField(dataField, subField);
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
    private void generateLinkText(OleHoldings oleHoldings,Link link, String code, DataField dataField) throws Exception {
        SubField subField = new SubField();
        subField.setCode(code);
        try {
            if (link != null && null != link.getText()) {
                subField.setValue(link.getText());
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateLinkText()");
        }

    }

    /**
     * generates the subfield for the call number type for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateCallNumberType(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        SubField subField = new SubField();
        subField.setCode(code);
        try {
            if (oleHoldings != null && oleHoldings.getCallNumber() != null && oleHoldings.getCallNumber().getShelvingScheme() != null && oleHoldings.getCallNumber().getShelvingScheme().getCodeValue() != null) {
                subField.setValue(oleHoldings.getCallNumber().getShelvingScheme().getCodeValue());
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateCallNumberType()");
        }
    }

    /**
     * gets the locationlevel name for the given level and locationlevel
     *
     * @param locationLevel
     * @param level
     * @return
     */
    private String getLocationLevelName(LocationLevel locationLevel, String level) {
        if (locationLevel == null || StringUtils.isEmpty(locationLevel.getLevel())) return null;
        if (locationLevel.getLevel().toUpperCase().startsWith(level)) return locationLevel.getName();
        return getLocationLevelName(locationLevel.getLocationLevel(), level);
    }

    /**
     * generates subfield for location level 3 - LIBRARY for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateLocationLevel3(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        String locationLevelName;
        try {
            if (oleHoldings != null && oleHoldings.getLocation() != null
                    && (locationLevelName = getLocationLevelName(oleHoldings.getLocation().getLocationLevel(), "LIBRARY")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateLocationLevel3()");
        }
    }

    /**
     * generates subfield for location level 2 - CAMPUS for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateLocationLevel2(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        String locationLevelName;
        try {
            if (oleHoldings != null && oleHoldings.getLocation() != null
                    && (locationLevelName = getLocationLevelName(oleHoldings.getLocation().getLocationLevel(), "CAMPUS")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateLocationLevel2()");
        }
    }

    /**
     * generates subfield for location level 1 - INSTITUTION for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateLocationLevel1(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        String locationLevelName;
        try {
            if (oleHoldings != null && oleHoldings.getLocation() != null
                    && (locationLevelName = getLocationLevelName(oleHoldings.getLocation().getLocationLevel(), "INSTITUTION")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateLocationLevel1()");
        }
    }

    /**
     * generates subfield for location level 5 - SHELVING for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     * @throws Exception
     */
    private void generateLocationLevel5(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        String locationLevelName;
        try {
            if (oleHoldings != null && oleHoldings.getLocation() != null
                    && (locationLevelName = getLocationLevelName(oleHoldings.getLocation().getLocationLevel(), "SHELVING")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateLocationLevel5()");
        }
    }

    /**
     * generates the subfields for the location level -4 COLLECTION for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateLocationLevel4(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        String locationLevelName;
        try {
            if (oleHoldings != null && oleHoldings.getLocation() != null
                    && (locationLevelName = getLocationLevelName(oleHoldings.getLocation().getLocationLevel(), "COLLECTION")) != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(locationLevelName);
                addDataField(dataField, subField);
            }
        } catch (Exception ex) {
            logError(oleHoldings, ex, "generateLocationLevel4()");
        }

    }

    /**
     * generates the subfields for the Statistical Code for the given oleHoldings
     *
     * @param oleHoldings
     * @param code
     * @param dataField
     */
    private void generateStatisticalCode(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        try {
            if (oleHoldings != null && oleHoldings.getStatisticalSearchingCode() != null && oleHoldings.getStatisticalSearchingCode().getCodeValue() != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(oleHoldings.getStatisticalSearchingCode().getCodeValue());
                addDataField(dataField, subField);
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
    private void generateAccessStatus(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        try {
            if (oleHoldings != null && oleHoldings.getAccessStatus() != null) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(oleHoldings.getAccessStatus());
                addDataField(dataField, subField);
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
    private void generatePlatform(OleHoldings oleHoldings, String code, DataField dataField) throws Exception {
        try {
            if (oleHoldings != null && oleHoldings.getPlatform() != null && null != oleHoldings.getPlatform().getPlatformName()) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(oleHoldings.getPlatform().getPlatformName());
                addDataField(dataField, subField);
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
    private void generateCoverageStartDate(OleHoldings oleHoldings, Coverage coverage, String code, DataField dataField) throws Exception {
        try {
            if (null != coverage) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(coverage.getCoverageStartDate());
                addDataField(dataField, subField);
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
    private void generateCoverageEndDate(OleHoldings oleHoldings, Coverage coverage, String code, DataField dataField) throws Exception {
        try {
            if (null != coverage) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(coverage.getCoverageEndDate());
                addDataField(dataField, subField);
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
    private void generateCoverageStartVolume(OleHoldings oleHoldings, Coverage coverage, String code, DataField dataField) throws Exception {
        try {
            if (null != coverage) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(coverage.getCoverageStartVolume());
                addDataField(dataField, subField);
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
    private void generateCoverageEndVolume(OleHoldings oleHoldings, Coverage coverage, String code, DataField dataField) throws Exception {
        try {
            if (null != coverage) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(coverage.getCoverageEndVolume());
                addDataField(dataField, subField);
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
    private void generateCoverageStartIssue(OleHoldings oleHoldings, Coverage coverage, String code, DataField dataField) throws Exception {
        try {
            if (null != coverage) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(coverage.getCoverageStartIssue());
                addDataField(dataField, subField);
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
    private void generateCoverageEndIssue(OleHoldings oleHoldings, Coverage coverage, String code, DataField dataField) throws Exception {
        try {
            if (null != coverage) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(coverage.getCoverageEndIssue());
                addDataField(dataField, subField);
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
    private void generatePublicDisplayNote(OleHoldings oleHoldings, Note note, String code, DataField dataField) throws Exception {
        try {
            if (null != oleHoldings && null != note) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(note.getValue());
                addDataField(dataField, subField);
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
    private void generateDonorPublicDisplay(OleHoldings oleHoldings, DonorInfo donorInfo, String code, DataField dataField) throws Exception {
        try {
            if (null != donorInfo) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(donorInfo.getDonorPublicDisplay());
                addDataField(dataField, subField);
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
    private void generateDonorNote(OleHoldings oleHoldings, DonorInfo donorInfo, String code, DataField dataField) throws Exception {
        try {
            if (null != donorInfo) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(donorInfo.getDonorNote());
                addDataField(dataField, subField);
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
    private void generateDonorCode(OleHoldings oleHoldings, DonorInfo donorInfo, String code, DataField dataField) throws Exception {
        try {
            if (null != donorInfo) {
                SubField subField = new SubField();
                subField.setCode(code);
                subField.setValue(donorInfo.getDonorCode());
                addDataField(dataField, subField);
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
}
