package org.kuali.ole.batch.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.*;
import org.kuali.ole.batch.helper.OLEBatchProcessDataHelper;
import org.kuali.ole.batch.service.BatchProcessBibImportService;
import org.kuali.ole.batch.util.BatchBibImportUtil;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;

import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.service.OLEEResourceHelperService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 8/8/13
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchProcessBibImportServiceImpl implements BatchProcessBibImportService {

    private static final Logger LOG = LoggerFactory.getLogger(BatchProcessBibImportServiceImpl.class);
    private OLEBatchProcessDataHelper oleBatchProcessDataHelper;
    private String bibMatchRecordRegex = "[0-9]+";
    private BusinessObjectService businessObjectService;
    private DocstoreClientLocator docstoreClientLocator;
    private OLEEResourceHelperService oleeResourceHelperService;


    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public OLEEResourceHelperService getOleeResourceHelperService() {
        if(oleeResourceHelperService == null) {
            oleeResourceHelperService = new OLEEResourceHelperService();
        }
        return oleeResourceHelperService;
    }
    /**
     * Process bib according to the selected profile by deleting fields, renaming fields, setting default values, sets Bib status and staff only flag and creates request document.
     * returns request document for bib.
     *
     * @param bibRecord
     * @param oleBatchProcessProfileBo
     * @return RequestDocument
     * @throws Exception
     */
    @Override
    public Bib performProcessBib(BibMarcRecord bibRecord, OLEBatchProcessProfileBo oleBatchProcessProfileBo, String staffOnly) throws Exception {
        Bib requestBib = null;
        deleteFields(bibRecord, oleBatchProcessProfileBo);
        renameFields(bibRecord, oleBatchProcessProfileBo);
        setDefaultOrConstants(bibRecord, oleBatchProcessProfileBo);
        String uuid = getUuid(bibRecord);
        requestBib = buildBibRequest(bibRecord, uuid);
        setBibStatus(requestBib, oleBatchProcessProfileBo, uuid);
        if (StringUtils.isEmpty(uuid)) {
            requestBib.setStaffOnly(oleBatchProcessProfileBo.isBibStaffOnly());
        } else {
            if (OLEConstants.OLEBatchProcess.CHANGE.equals(oleBatchProcessProfileBo.getOverlayNoChangeOrSet())) {
                requestBib.setStaffOnly(oleBatchProcessProfileBo.isOverlayBibStaffOnly());
            }else if (oleBatchProcessProfileBo.getOverlayNoChangeOrSet().equalsIgnoreCase(OLEConstants.OLEBatchProcess.DONOT_CHANGE)) {
                if (staffOnly != null) {
                    requestBib.setStaffOnly(Boolean.valueOf(staffOnly));
                }
            }
        }
        return requestBib;
    }

    /**
     * Finds matching bib record in the database based on the match points given in the profile.
     *
     * @param bibRecord
     * @param oleBatchProcessProfileBo
     * @return Bib
     * @throws Exception
     */
    @Override
    public Bib findMatchingBibRecord(BibMarcRecord bibRecord, OLEBatchProcessProfileBo oleBatchProcessProfileBo, List<BibMarcRecord> failureRecordsList) throws Exception {

        Bib bibDocument = null;
        List<OLEBatchProcessProfileMatchPoint> bibMatchingRecordList = BatchBibImportUtil.buildMatchPointListByDataType(oleBatchProcessProfileBo.getOleBatchProcessProfileMatchPointList(), DocType.BIB.getCode());
        for (OLEBatchProcessProfileMatchPoint oleBatchProcessProfileBibMatchPoint : bibMatchingRecordList) {
            String profileBibMatchRecord = oleBatchProcessProfileBibMatchPoint.getMatchPoint();
            List<String> profileBibMatchRecordValues = getMatchingrecordValue(bibRecord, profileBibMatchRecord);
            String matchRecord = getMatchRecord(profileBibMatchRecord);
            List<String> matchPointUuids = new ArrayList<String>();
            for (String profileBibMatchRecordValue : profileBibMatchRecordValues) {
                if (matchRecord != null && profileBibMatchRecordValue != null && StringUtils.isNotEmpty(profileBibMatchRecordValue)) {
                    SearchParams searchParams = new SearchParams();
                    searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(DocType.BIB.getCode(), matchRecord, profileBibMatchRecordValue), "AND"));
                    searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "bibIdentifier"));
                    try {
                        SearchResponse searchResponse = getDocstoreClientLocator().getDocstoreClient().search(searchParams);
                        List<SearchResult> searchResults = searchResponse.getSearchResults();
                        if (searchResults.size() > 0) {
                            for (SearchResult searchResult : searchResults) {
                                for (SearchResultField searchResultfield : searchResult.getSearchResultFields()) {
                                    if (searchResultfield.getFieldName().equalsIgnoreCase("bibIdentifier") && searchResultfield.getFieldValue() != null && !searchResultfield.getFieldValue().isEmpty()) {
                                        matchPointUuids.add(searchResultfield.getFieldValue());
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    if (matchPointUuids.size() > 0) {
                        break;
                    }
                }
            }
            if (matchPointUuids.size() == 1) {
                for (String uuid : matchPointUuids) {
                    Bib bib = docstoreClientLocator.getDocstoreClient().retrieveBib(uuid);
                    if (bib != null) {
                        bibDocument = bib;
                    }
                }
            } else if (matchPointUuids.size() > 1) {
                failureRecordsList.add(bibRecord);
                break;
            }
        }
        return bibDocument;
    }

    /**
     * Creates XML content based on the Incoming Marc Records.
     *
     * @param marcFileContent
     * @return XML String
     */
    @Override
    public String preProcessMarc(String marcFileContent) throws Exception {
        String marcXMLContent = null;
        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        marcXMLContent = marcXMLConverter.convert(marcFileContent);

        String modifiedXMLContent =
                marcXMLContent.
                        replace("collection xmlns=\"http://www.loc.gov/MARC21/slim\" xmlns=\"http://www.loc.gov/MARC21/slim",
                                "collection xmlns=\"http://www.loc.gov/MARC21/slim");
        return modifiedXMLContent;
    }

    /**
     * Deleted the bib record fields based on  oleBatchProcessProfileBo profile
     *
     * @param bibRecord
     * @param oleBatchProcessProfileBo
     */
    public void deleteFields(BibMarcRecord bibRecord, OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        List<OLEBatchProcessProfileDeleteField> deleteFields = oleBatchProcessProfileBo.getOleBatchProcessProfileDeleteFieldsList();
        for (OLEBatchProcessProfileDeleteField oleBatchProcessProfileDeleteField : deleteFields) {
            if (oleBatchProcessProfileDeleteField.getFirstIndicator() == null) {
                oleBatchProcessProfileDeleteField.setFirstIndicator("");
            }
            if (oleBatchProcessProfileDeleteField.getFirstIndicator() != null) {
                if (oleBatchProcessProfileDeleteField.getFirstIndicator().contains("#")) {
                    oleBatchProcessProfileDeleteField.setFirstIndicator("");
                }
            }
            if (oleBatchProcessProfileDeleteField.getSecondIndicator() == null) {
                oleBatchProcessProfileDeleteField.setSecondIndicator("");
            }
            if (oleBatchProcessProfileDeleteField.getSecondIndicator() != null) {
                if (oleBatchProcessProfileDeleteField.getSecondIndicator().contains("#")) {
                    oleBatchProcessProfileDeleteField.setSecondIndicator("");
                }
            }
            if (StringUtils.isEmpty(oleBatchProcessProfileDeleteField.getSubField()) || (oleBatchProcessProfileDeleteField.getSubField() == "" || oleBatchProcessProfileDeleteField.getSubField() == null)) {
                if (!isProtectedDataField(oleBatchProcessProfileBo, oleBatchProcessProfileDeleteField)) {
                    if (StringUtils.isNotEmpty(oleBatchProcessProfileDeleteField.getTag())) {
                        getOleBatchProcessDataHelper().deleteMarcFields(bibRecord, oleBatchProcessProfileDeleteField);
                    }
                }
            }
            if (StringUtils.isNotEmpty(oleBatchProcessProfileDeleteField.getSubField()) || oleBatchProcessProfileDeleteField.getSubField() != "" || oleBatchProcessProfileDeleteField.getSubField() != null) {
                if (StringUtils.isNotEmpty(oleBatchProcessProfileDeleteField.getSubField())) {
                    getOleBatchProcessDataHelper().deleteMarcSubFields(bibRecord, oleBatchProcessProfileDeleteField);
                }
            }
        }
    }

    private boolean isProtectedSubField(OLEBatchProcessProfileBo oleBatchProcessProfileBo, OLEBatchProcessProfileDeleteField deleteField) {
        List<OLEBatchGloballyProtectedField> batchGloballyProtectedFieldList = oleBatchProcessProfileBo.getOleBatchGloballyProtectedFieldList();
        List<OLEBatchProcessProfileProtectedField> batchProcessProfileProtectedFieldList = oleBatchProcessProfileBo.getOleBatchProcessProfileProtectedFieldList();
        for (OLEBatchGloballyProtectedField oleBatchGloballyProtectedField : batchGloballyProtectedFieldList) {
            if (oleBatchGloballyProtectedField.isIgnoreValue()) {
                if (oleBatchGloballyProtectedField.getFirstIndicator() == null) {
                    oleBatchGloballyProtectedField.setFirstIndicator("");
                }
                if (oleBatchGloballyProtectedField.getSecondIndicator() == null) {
                    oleBatchGloballyProtectedField.setSecondIndicator("");
                }
                if (StringUtils.isNotEmpty(oleBatchGloballyProtectedField.getTag()) && StringUtils.isNotEmpty(deleteField.getTag()) && oleBatchGloballyProtectedField.getTag().equalsIgnoreCase(deleteField.getTag())) {
                    if (oleBatchGloballyProtectedField.getFirstIndicator().equalsIgnoreCase(deleteField.getFirstIndicator())) {
                        if (oleBatchGloballyProtectedField.getSecondIndicator().equalsIgnoreCase(deleteField.getSecondIndicator())) {
                            if (StringUtils.isNotEmpty(oleBatchGloballyProtectedField.getSubField()) && StringUtils.isNotEmpty(deleteField.getSubField()) && oleBatchGloballyProtectedField.getSubField().equalsIgnoreCase(deleteField.getSubField())) {
                                return true;
                            } else if (StringUtils.isEmpty(oleBatchGloballyProtectedField.getSubField()) || oleBatchGloballyProtectedField.getSubField() == "" || oleBatchGloballyProtectedField.getSubField() == null) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        for (OLEBatchProcessProfileProtectedField oleBatchProcessProfileProtectedField : batchProcessProfileProtectedFieldList) {
            if (oleBatchProcessProfileProtectedField.getFirstIndicator() == null) {
                oleBatchProcessProfileProtectedField.setFirstIndicator("");
            }
            if (oleBatchProcessProfileProtectedField.getSecondIndicator() == null) {
                oleBatchProcessProfileProtectedField.setSecondIndicator("");
            }
            if (StringUtils.isNotEmpty(oleBatchProcessProfileProtectedField.getTag()) && StringUtils.isNotEmpty(deleteField.getTag()) && oleBatchProcessProfileProtectedField.getTag().equalsIgnoreCase(deleteField.getTag())) {
                if (oleBatchProcessProfileProtectedField.getFirstIndicator().equalsIgnoreCase(deleteField.getFirstIndicator())) {
                    if (oleBatchProcessProfileProtectedField.getSecondIndicator().equalsIgnoreCase(deleteField.getSecondIndicator())) {
                        if (StringUtils.isNotEmpty(oleBatchProcessProfileProtectedField.getSubField()) && StringUtils.isNotEmpty(deleteField.getSubField()) && oleBatchProcessProfileProtectedField.getSubField().equalsIgnoreCase(deleteField.getSubField())) {
                            return true;
                        } else if (StringUtils.isEmpty(oleBatchProcessProfileProtectedField.getSubField()) || oleBatchProcessProfileProtectedField.getSubField() == "" || oleBatchProcessProfileProtectedField.getSubField() == null) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isProtectedDataField(OLEBatchProcessProfileBo oleBatchProcessProfileBo, OLEBatchProcessProfileDeleteField deleteField) {
        List<OLEBatchGloballyProtectedField> batchGloballyProtectedFieldList = oleBatchProcessProfileBo.getOleBatchGloballyProtectedFieldList();
        List<OLEBatchProcessProfileProtectedField> batchProcessProfileProtectedFieldList = oleBatchProcessProfileBo.getOleBatchProcessProfileProtectedFieldList();
        boolean isExist = false;
        for (OLEBatchGloballyProtectedField oleBatchGloballyProtectedField : batchGloballyProtectedFieldList) {
            if (StringUtils.isNotEmpty(oleBatchGloballyProtectedField.getTag()) && StringUtils.isNotEmpty(deleteField.getTag()) && oleBatchGloballyProtectedField.getTag().equalsIgnoreCase(deleteField.getTag())) {
                isExist = true;
            }
        }
        if (isExist) {
            for (OLEBatchGloballyProtectedField oleBatchGloballyProtectedField : batchGloballyProtectedFieldList) {
                if (oleBatchGloballyProtectedField.isIgnoreValue()) {
                    if (StringUtils.isNotEmpty(oleBatchGloballyProtectedField.getTag()) && StringUtils.isNotEmpty(deleteField.getTag()) && oleBatchGloballyProtectedField.getTag().equalsIgnoreCase(deleteField.getTag())) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        for (OLEBatchProcessProfileProtectedField oleBatchProcessProfileProtectedField : batchProcessProfileProtectedFieldList) {
            if (StringUtils.isNotEmpty(oleBatchProcessProfileProtectedField.getTag()) && StringUtils.isNotEmpty(deleteField.getTag()) && oleBatchProcessProfileProtectedField.getTag().equalsIgnoreCase(deleteField.getTag())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check the bib record data fields are protected fields or not
     *
     * @param oleBatchProcessProfileBo
     * @param deleteField
     * @param subFieldContains
     * @return
     */
    private boolean isProtectedField(OLEBatchProcessProfileBo oleBatchProcessProfileBo, String deleteField, String subFieldContains) {
        List<OLEBatchGloballyProtectedField> batchGloballyProtectedFieldList = oleBatchProcessProfileBo.getOleBatchGloballyProtectedFieldList();
        List<OLEBatchProcessProfileProtectedField> batchProcessProfileProtectedFieldList = oleBatchProcessProfileBo.getOleBatchProcessProfileProtectedFieldList();
        String batchGlblyPrctdFld = "";
        String prflPrctFld = "";
        String deleteFldWithValue = deleteField + subFieldContains;
        for (OLEBatchGloballyProtectedField oleBatchGloballyProtectedField : batchGloballyProtectedFieldList) {
            if (oleBatchGloballyProtectedField.isIgnoreValue()) {
                batchGlblyPrctdFld = getBatchDataFldFullString(oleBatchGloballyProtectedField.getTag(), oleBatchGloballyProtectedField.getFirstIndicator(), oleBatchGloballyProtectedField.getSecondIndicator(), oleBatchGloballyProtectedField.getSubField());
                if (deleteField != null && batchGlblyPrctdFld.equalsIgnoreCase(deleteField)) {
                    return true;
                } else if (deleteField != null && batchGlblyPrctdFld.contains(deleteField)) {
                    return true;
                }
            }
        }
        for (OLEBatchProcessProfileProtectedField oleBatchProcessProfileProtectedField : batchProcessProfileProtectedFieldList) {
            prflPrctFld = getBatchDataFldFullValueString(oleBatchProcessProfileProtectedField.getTag(), oleBatchProcessProfileProtectedField.getFirstIndicator(), oleBatchProcessProfileProtectedField.getSecondIndicator(), oleBatchProcessProfileProtectedField.getSubField(), oleBatchProcessProfileProtectedField.getSubFieldContains());
            if (deleteFldWithValue != null && prflPrctFld.equalsIgnoreCase(deleteFldWithValue)) {
                return true;
            } else if (deleteFldWithValue != null && prflPrctFld.contains(deleteFldWithValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * performs the caluclate bib data fields according to specified in the profile
     *
     * @param tag
     * @param ind1
     * @param ind2
     * @param subField
     * @return
     */
    private String getBatchDataFldFullString(String tag, String ind1, String ind2, String subField) {
        String fullRecord = null;
        if (tag != null) {
            if (ind1 == null || ind1.equalsIgnoreCase(" ") || StringUtils.isEmpty(ind1)) ind1 = "#";
            if (ind2 == null || ind2.equalsIgnoreCase(" ") || StringUtils.isEmpty(ind2)) ind2 = "#";
            if (!subField.contains("$")) {
                fullRecord = tag + " " + ind1 + ind2 + " $" + subField;
            } else {
                fullRecord = tag + " " + ind1 + ind2 + " " + subField;
            }
        }
        return fullRecord;
    }

    /**
     * performs the caluclate bib data fields according to specified in the profile
     *
     * @param tag
     * @param ind1
     * @param ind2
     * @param subField
     * @param profileBibMatchRecord
     * @return
     */
    private String getBatchDataFldFullString(String tag, String ind1, String ind2, String subField, String profileBibMatchRecord) {
        String fullRecord = null;
        if (StringUtils.isNotEmpty(profileBibMatchRecord) && profileBibMatchRecord.contains(tag)) {
            String[] profileBibInds = profileBibMatchRecord.split(" ");
            if (profileBibInds.length == 3 && profileBibInds[1].contains("#")) {
                String inds = profileBibInds[1];
                if (inds.equals("##")) {
                    ind1 = "";
                    ind2 = "";
                } else if (inds.startsWith("#")) {
                    ind1 = "";
                } else if (inds.endsWith("#")) {
                    ind2 = "";
                }

            } else if (profileBibInds.length == 2 && profileBibInds[1].contains("$")) {
                fullRecord = tag + " $" + subField;
                return fullRecord;
            }
        }


        if (tag != null) {
            if (ind1 == null || ind1.equalsIgnoreCase(" ") || StringUtils.isEmpty(ind1)) ind1 = "#";
            if (ind2 == null || ind2.equalsIgnoreCase(" ") || StringUtils.isEmpty(ind2)) ind2 = "#";
            fullRecord = tag + " " + ind1 + ind2 + " $" + subField;
        }
        return fullRecord;
    }

    /**
     * performs the caluclate bib data fields according to specified in the profile
     *
     * @param tag
     * @param ind1
     * @param ind2
     * @param subField
     * @param subFieldValue
     * @return
     */
    private String getBatchDataFldFullValueString(String tag, String ind1, String ind2, String subField, String subFieldValue) {
        String fullRecord = null;
        if (tag != null) {
            if (ind1 == null || ind1.equalsIgnoreCase(" ") || StringUtils.isEmpty(ind1)) ind1 = "#";
            if (ind2 == null || ind2.equalsIgnoreCase(" ") || StringUtils.isEmpty(ind2)) ind2 = "#";
            if (subField != null && !subField.contains("$")) {
                fullRecord = tag + " " + ind1 + ind2 + " $" + subField + subFieldValue;
            } else {
                subField="";
                subFieldValue="";
                fullRecord = tag + " " + ind1 + ind2 + " " + subField + subFieldValue;
            }
            //fullRecord = tag + " " + ind1 + ind2 + " $" + subField + subFieldValue;
        }
        return fullRecord;
    }

    /**
     * Performs the rename the bib data fields according to rename data fileds in profile
     *
     * @param targetRecord
     * @param oleBatchProcessProfileBo
     */
    public void renameFields(BibMarcRecord targetRecord, OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        List<OLEBatchProcessProfileRenameField> renameList = oleBatchProcessProfileBo.getOleBatchProcessProfileRenameFieldsList();
        for (OLEBatchProcessProfileRenameField renameField : renameList) {
            if (renameField.getOriginalFirstIndicator() == null) {
                renameField.setOriginalFirstIndicator("");
            }
            if (renameField.getOriginalFirstIndicator() != null) {
                if (renameField.getOriginalFirstIndicator().contains("#")) {
                    renameField.setOriginalFirstIndicator(" ");
                }
            }
            if (renameField.getOriginalSecondIndicator() == null) {
                renameField.setOriginalSecondIndicator("");
            }
            if (renameField.getOriginalSecondIndicator() != null) {
                if (renameField.getOriginalSecondIndicator().contains("#")) {
                    renameField.setOriginalSecondIndicator(" ");
                }
            }
            OLEBatchProcessProfileDeleteField oleBatchProcessProfileDeleteField = new OLEBatchProcessProfileDeleteField();
            oleBatchProcessProfileDeleteField.setTag(renameField.getOriginalTag());
            oleBatchProcessProfileDeleteField.setFirstIndicator(renameField.getOriginalFirstIndicator());
            oleBatchProcessProfileDeleteField.setSecondIndicator(renameField.getOriginalSecondIndicator());
            oleBatchProcessProfileDeleteField.setSubField(renameField.getOriginalSubField());
            if (StringUtils.isNotEmpty(oleBatchProcessProfileDeleteField.getSubField()) || oleBatchProcessProfileDeleteField.getSubField() != "" || oleBatchProcessProfileDeleteField.getSubField() != null) {
                if (StringUtils.isNotEmpty(oleBatchProcessProfileDeleteField.getSubField())) {
                    getOleBatchProcessDataHelper().addMarcFields(targetRecord, renameField);
                    getOleBatchProcessDataHelper().deleteMarcSubFields(targetRecord, oleBatchProcessProfileDeleteField);
                }
            }
            if (StringUtils.isEmpty(oleBatchProcessProfileDeleteField.getSubField()) || (oleBatchProcessProfileDeleteField.getSubField() == "" || oleBatchProcessProfileDeleteField.getSubField() == null)) {
                if (StringUtils.isNotEmpty(oleBatchProcessProfileDeleteField.getTag())) {
                    getOleBatchProcessDataHelper().renameMarcFields(targetRecord, renameField);
                }
            }
        }
    }

    /**
     * Performs to add the 003 value to 035
     *
     * @param valueOf003
     * @return
     */
    private DataField addControlField003To035a(String valueOf003) {
        DataField dataField = new DataField();
        dataField.setTag(OLEConstants.OLEBatchProcess.DATA_FIELD_035);
        SubField subField = new SubField();
        subField.setCode("a");
        subField.setValue(valueOf003);
        List<SubField> subFields = new ArrayList<>();
        subFields.add(subField);
        dataField.setSubFields(subFields);
        return dataField;
    }

    /**
     * get the control filed based on tag value
     *
     * @param controlFields
     * @param tag
     * @return
     */
    private ControlField getControlField(List<ControlField> controlFields, String tag) {
        for (ControlField controlField : controlFields) {
            if (tag.equalsIgnoreCase(controlField.getTag())) {
                return controlField;
            }
        }
        return null;
    }

    /**
     * This method performs to build the request document by using bib record and uuid information
     *
     * @param bibRecord
     * @param uuid
     * @return
     */
    public Bib buildBibRequest(BibMarcRecord bibRecord, String uuid) {
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
        String bibXML = bibMarcRecordProcessor.generateXML(bibRecord);
        return buildBibRequest(bibXML, uuid);
    }

    public Bib buildBibRequest(String bibXML, String uuid) {
        Bib requestBib = new Bib();
        if (StringUtils.isNotEmpty(uuid)) {
            requestBib.setId(uuid);
        }
        requestBib.setContent(bibXML);
        return requestBib;
    }

    /**
     * This method peforms to set the bib status based on overlay or new add record  according to OLEBatchProcessProfileBo profile
     *
     * @param requestBib
     * @param oleBatchProcessProfileBo
     * @param uuid
     */
    public void setBibStatus(Bib requestBib, OLEBatchProcessProfileBo oleBatchProcessProfileBo, String uuid) {
        if (null != requestBib) {
            if (uuid == null) {
                requestBib.setStatus(oleBatchProcessProfileBo.getNewBibStaus());
            } else if (uuid != null && OLEConstants.OLEBatchProcess.CHANGE.equals(oleBatchProcessProfileBo.getNoChangeOrSet())) {
                requestBib.setStatus(oleBatchProcessProfileBo.getExistedBibStatus());
            }
        }
    }

    /**
     * this method performs the fetch the uuid value from  BibliographicRecord
     *
     * @param bibRecord
     * @return
     */
    public String getUuid(BibMarcRecord bibRecord) {
        List<ControlField> controlFields = bibRecord.getControlFields();
        String uuid = null;
        for (ControlField controlField : controlFields) {
            if (OLEConstants.OLEBatchProcess.CONTROL_FIELD_001.equals(controlField.getTag())) {
                uuid = "wbm-" + controlField.getValue();
                break;
            }
        }
        return uuid;
    }

    /**
     * performs to set default or constant values to bib record
     *
     * @param targetRecord
     * @param oleBatchProcessProfileBo
     */
    public void setDefaultOrConstants(BibMarcRecord targetRecord, OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        String fullDataField = null;
        List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsBoList = oleBatchProcessProfileBo.getOleBatchProcessProfileConstantsList();
        for (OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo : oleBatchProcessProfileConstantsBoList) {
            if (!"Bibmarc".equalsIgnoreCase(oleBatchProcessProfileConstantsBo.getDataType()))
                continue;

            boolean isDataFieldExist = false;
            for (DataField dataField : targetRecord.getDataFields()) {
                if (dataField.getTag().equalsIgnoreCase(oleBatchProcessProfileConstantsBo.getAttributeName().substring(0, 3))) {
                    for (SubField subField : dataField.getSubFields()) {
                        fullDataField = getBatchDataFldFullString(dataField.getTag(), dataField.getInd1(), dataField.getInd2(), subField.getCode());
                        buildDataFiledfullForComparision(oleBatchProcessProfileConstantsBo);
                        if (fullDataField.equalsIgnoreCase(oleBatchProcessProfileConstantsBo.getAttributeName())) {
                            if (oleBatchProcessProfileConstantsBo.getDefaultValue().equalsIgnoreCase(OLEConstants.OLEBatchProcess.PROFILE_CONSTANT_DEFAULT)) {
                                isDataFieldExist = true;
                                if (StringUtils.isEmpty(subField.getValue())) {
                                    subField.setValue(oleBatchProcessProfileConstantsBo.getAttributeValue());
                                }
                            }
                        }
                    }
                }
            }
            if (!isDataFieldExist) {
                DataField dataField = BatchBibImportUtil.buildDataField(oleBatchProcessProfileConstantsBo.getAttributeName(), oleBatchProcessProfileConstantsBo.getAttributeValue());
                targetRecord.getDataFields().add(dataField);
            }
        }

    }

    private void buildDataFiledfullForComparision(OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo) {
        if (oleBatchProcessProfileConstantsBo.getAttributeName().length() < 7) {
            String[] attributeList = oleBatchProcessProfileConstantsBo.getAttributeName().split(" ");
            if (attributeList.length == 2) {
                oleBatchProcessProfileConstantsBo.setAttributeName(getBatchDataFldFullString(attributeList[0], "#", "#", attributeList[1]));
            }
        }
    }

    /**
     * Performs the get match record value for search in doc store
     *
     * @param profileBibMatchRecord
     * @return
     */
    private String getMatchRecord(String profileBibMatchRecord) {
        String matchRecord = null;
        if (OLEConstants.OLEBatchProcess.CONTROL_FIELD_001.equals(profileBibMatchRecord)) {
            matchRecord = profileBibMatchRecord;
        } else {

            String[] matchRecordSplit = profileBibMatchRecord.split(" ");
            String fullSubField = matchRecordSplit[matchRecordSplit.length - 1];
            matchRecord = matchRecordSplit[0] + fullSubField.substring(fullSubField.length() - 1);
        }
        return OLEConstants.PREFIX_FOR_DATA_FIELD + matchRecord;
    }

    /**
     * Performs the to get match record value from the bib record based on match point specified in the profile
     *
     * @param bibRecord
     * @param profileBibMatchRecord
     * @return
     */
    private List<String> getMatchingrecordValue(BibMarcRecord bibRecord, String profileBibMatchRecord) {
        List<String> profileBibMatchRecordValue = new ArrayList<>();
        if (OLEConstants.OLEBatchProcess.CONTROL_FIELD_001.equals(profileBibMatchRecord)) {
            List<ControlField> controlFields = bibRecord.getControlFields();
            for (ControlField controlField : controlFields) {
                if (profileBibMatchRecord.equals(controlField.getTag())) {
                    profileBibMatchRecordValue.add(controlField.getValue());
                    controlFields.remove(controlField);
                    break;
                }
            }
        } else {

            List<DataField> dataFieldsList = bibRecord.getDataFields();
            String dataFieldString = null;
            for (DataField dataField : dataFieldsList) {
                for (SubField subField : dataField.getSubFields()) {
                    if (StringUtils.isNotEmpty(profileBibMatchRecord) && profileBibMatchRecord.contains(dataField.getTag())) {
                        dataFieldString = getBatchDataFldFullString(dataField.getTag(), dataField.getInd1(), dataField.getInd2(), subField.getCode(), profileBibMatchRecord);
                        if (dataFieldString.equalsIgnoreCase(profileBibMatchRecord)) {
                            profileBibMatchRecordValue.add(subField.getValue());
                        }
                    }
                }
            }
        }
        return profileBibMatchRecordValue;
    }


    /**
     * Treats 001 Datafield in the bib record based on the profile.
     *
     * @param bibMarcRecord
     * @param oleBatchProcessProfileBo
     */
    @Override
    public void process001(BibMarcRecord bibMarcRecord, OLEBatchProcessProfileBo oleBatchProcessProfileBo) {
        ControlField controlField001 = getMatchedControlField(bibMarcRecord, OLEConstants.OLEBatchProcess.CONTROL_FIELD_001);
        ControlField controlField003 = getMatchedControlField(bibMarcRecord, OLEConstants.OLEBatchProcess.CONTROL_FIELD_003);

        if (controlField001 != null) {
            String controlField001Value = controlField001.getValue();
            if (OLEConstants.OLEBatchProcess.DELETE_001.equalsIgnoreCase(oleBatchProcessProfileBo.getDontChange001())) {
                bibMarcRecord.getControlFields().remove(controlField001);
            } else if (OLEConstants.OLEBatchProcess.CHANGE_TAG_035.equalsIgnoreCase(oleBatchProcessProfileBo.getDontChange001())) {
                boolean removePrefixIndicator = oleBatchProcessProfileBo.getRemoveValueFrom001();
                if (oleBatchProcessProfileBo.getValueToRemove() != null && !oleBatchProcessProfileBo.getValueToRemove().isEmpty()) {
                    String[] valuesToRemove = oleBatchProcessProfileBo.getValueToRemove().split(",");
                    for (String valueToRemove : valuesToRemove) {
                        controlField001Value = controlField001Value.replaceAll(valueToRemove.trim(), "");
                    }
                }
                String prefix = "";
                if (OLEConstants.OLEBatchProcess.PREPEND_001_TO_035.equalsIgnoreCase(oleBatchProcessProfileBo.getPrepend003To035())) {
                    if (controlField003 != null && controlField003.getValue() != null && controlField003.getValue().length() > 0) {
                        prefix = "(" + controlField003.getValue() + ")";
                    }
                } else if (OLEConstants.OLEBatchProcess.PREPEND_VALUE_TO_035.equalsIgnoreCase(oleBatchProcessProfileBo.getPrepend003To035())) {
                    prefix = StringUtils.isEmpty(oleBatchProcessProfileBo.getValueToPrepend()) ? "" : oleBatchProcessProfileBo.getValueToPrepend();
                }
                String dataField035aValue = prefix + controlField001Value;
                DataField dataField035a = addControlField003To035a(dataField035aValue);
                bibMarcRecord.getDataFields().add(dataField035a);
                bibMarcRecord.getControlFields().remove(controlField001);
            }
        }
    }

    /**
     * return the match control field from bib record based on tag value
     *
     * @param bibMarcRecord
     * @param tag
     * @return
     */
    public ControlField getMatchedControlField(BibMarcRecord bibMarcRecord, String tag) {

        for (ControlField controlField : bibMarcRecord.getControlFields()) {
            if (controlField.getTag().equalsIgnoreCase(tag)) {
                return controlField;
            }
        }
        return null;
    }

    /**
     * Overlays in the existing bib record with the incoming bib record except protected fields.
     *
     * @param matchedRecord
     * @param inComingRecord
     * @param processProfile
     * @return
     */
    @Override
    public BibMarcRecord overlayFields(BibMarcRecord inComingRecord, BibMarcRecord matchedRecord, OLEBatchProcessProfileBo processProfile) {

        List<String> protectedFields = getProtectedFieldsTags(processProfile);
        List<DataField> dataFields = matchedRecord.getDataFields();
        List<DataField> dataFieldList = new ArrayList<>();

        //Add Protected fields into list from matched record
        for (DataField dataField : dataFields) {
            if (protectedFields.contains(dataField.getTag())) {
                dataFieldList.add(dataField);
            }
        }

        // Add incoming data fields from list
        dataFieldList.addAll(inComingRecord.getDataFields());
        Collections.sort(dataFieldList);
        matchedRecord.setDataFields(dataFieldList);
        List<ControlField> controlFields = inComingRecord.getControlFields();
        ControlField controlField001 = getMatchedControlField(inComingRecord, OLEConstants.OLEBatchProcess.CONTROL_FIELD_001);
        if (controlField001 != null) {
            controlFields.remove(controlField001);
        }
        controlFields.add(getControlField(matchedRecord.getControlFields(), OLEConstants.OLEBatchProcess.CONTROL_FIELD_001));
        matchedRecord.setControlFields(controlFields);
        matchedRecord.setLeader(inComingRecord.getLeader());
        return matchedRecord;

    }


    @Override
    public List<BibMarcRecord> saveBatch(List<BibMarcRecord> bibMarcRecords, OLEBatchBibImportDataObjects oleBatchBibImportDataObjects, OLEBatchBibImportStatistics oleBatchbibImportStatistics) {

        docstoreClientLocator = getDocstoreClientLocator();
        try {
            oleBatchBibImportDataObjects.setBibTreesObj(docstoreClientLocator.getDocstoreClient().processBibTrees(oleBatchBibImportDataObjects.getBibTrees()));
        } catch (Exception e) {
            LOG.error("Batch Process", e);
            oleBatchbibImportStatistics.getErrorBuilder().append(OLEConstants.OLEBatchProcess.PROCESS_FAILURE).append(System.lineSeparator());
        }

        for (int i = 0; i < oleBatchBibImportDataObjects.getBibTrees().getBibTrees().size(); i++) {
            BibTree bibTree = oleBatchBibImportDataObjects.getBibTrees().getBibTrees().get(i);
            if (null != bibTree) {
                if (null != bibTree.getBib() && DocstoreDocument.ResultType.FAILURE.equals(bibTree.getBib().getResult())) {
                    setErrorMessage(bibMarcRecords, oleBatchbibImportStatistics, i, bibTree.getBib().getMessage());
                    continue;
                }
                if (CollectionUtils.isNotEmpty(bibTree.getHoldingsTrees())) {
                    for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                        if (null != holdingsTree.getHoldings() && DocstoreDocument.ResultType.FAILURE.equals(holdingsTree.getHoldings().getResult())) {
                            setErrorMessage(bibMarcRecords, oleBatchbibImportStatistics, i, holdingsTree.getHoldings().getMessage());
                            continue;
                        }
                        else {
                            if (holdingsTree.getHoldings() != null && holdingsTree.getHoldings().getHoldingsType() != null && holdingsTree.getHoldings().getHoldingsType().equalsIgnoreCase("electronic") && holdingsTree.getHoldings().getOperation().equals(DocstoreDocument.OperationType.CREATE)) {
                                getOleeResourceHelperService().updateEHoldingsInEResource(holdingsTree.getHoldings());
                            }
                        }
                        if (CollectionUtils.isNotEmpty(holdingsTree.getItems())) {
                            for (Item item : holdingsTree.getItems()) {
                                if (null != item) {
                                    if (DocstoreDocument.ResultType.FAILURE.equals(item.getResult())) {
                                        setErrorMessage(bibMarcRecords, oleBatchbibImportStatistics, i, item.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return oleBatchbibImportStatistics.getMismatchRecordList();
    }

    private void setErrorMessage(List<BibMarcRecord> bibMarcRecords, OLEBatchBibImportStatistics oleBatchbibImportStatistics, int recordNumber, String failureMessage) {
        oleBatchbibImportStatistics.getMismatchRecordList().add(bibMarcRecords.get(recordNumber));
        oleBatchbibImportStatistics.getErrorBuilder().append("Record #" + ++recordNumber).append(" : ");
        oleBatchbibImportStatistics.getErrorBuilder().append(failureMessage).append(System.lineSeparator());
    }

    @Override
    public List<OrderBibMarcRecord> saveOderBatch(List<OrderBibMarcRecord> orderBibMarcRecords, OLEBatchBibImportDataObjects oleBatchBibImportDataObjects, OLEBatchBibImportStatistics bibImportStatistics) {
        docstoreClientLocator = getDocstoreClientLocator();
        List<BibMarcRecord> bibMarcRecords = new ArrayList<>();
        for (OrderBibMarcRecord orderBibMarcRecord : orderBibMarcRecords) {
            bibMarcRecords.add(orderBibMarcRecord.getBibMarcRecord());
        }
        saveBatch(bibMarcRecords, oleBatchBibImportDataObjects, bibImportStatistics);
        return oleBatchBibImportDataObjects.getResponseOrderRecord(orderBibMarcRecords);
    }



    /**
     * performs to fetch the protected field from the OLEBatchProcessProfileBo profile
     *
     * @param processProfile
     * @return
     */
    private List<String> getProtectedFieldsTags(OLEBatchProcessProfileBo processProfile) {
        List<String> protectedFields = new ArrayList<>();

        for (OLEBatchGloballyProtectedField oleBatchGloballyProtectedField : processProfile.getOleBatchGloballyProtectedFieldList()) {
            protectedFields.add(oleBatchGloballyProtectedField.getTag());
        }
        for (OLEBatchProcessProfileProtectedField oleBatchProcessProfileProtectedField : processProfile.getOleBatchProcessProfileProtectedFieldList()) {
            protectedFields.add(oleBatchProcessProfileProtectedField.getTag());
        }
        return protectedFields;
    }

    public OLEBatchProcessDataHelper getOleBatchProcessDataHelper() {
        if (oleBatchProcessDataHelper == null) oleBatchProcessDataHelper = OLEBatchProcessDataHelper.getInstance();
        return oleBatchProcessDataHelper;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) businessObjectService = KRADServiceLocator.getBusinessObjectService();
        return businessObjectService;
    }


}