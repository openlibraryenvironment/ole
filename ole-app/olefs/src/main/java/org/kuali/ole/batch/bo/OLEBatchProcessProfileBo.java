package org.kuali.ole.batch.bo;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/6/13
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessProfileBo extends PersistableBusinessObjectBase implements Comparable<OLEBatchProcessProfileBo> {

    private String batchProcessProfileId;
    private String batchProcessProfileName;
    private String batchProcessProfileDesc;
    private String batchProcessProfileType;
    private String krmsProfileName;
    private String bibImportProfileForOrderRecord;
    private OLEBatchProcessTypeBo oleBatchProcessTypeBo;
    private List<OLEBatchProcessProfileFilterCriteriaBo> oleBatchProcessProfileFilterCriteriaList = new ArrayList<OLEBatchProcessProfileFilterCriteriaBo>();
    private List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsList = new ArrayList<OLEBatchProcessProfileMappingOptionsBo>();
    private List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList = new ArrayList<OLEBatchProcessProfileDataMappingOptionsBo>();
    private List<OLEBatchGloballyProtectedField> oleBatchGloballyProtectedFieldList = new ArrayList<OLEBatchGloballyProtectedField>();
    private List<OLEBatchProcessProfileProtectedField> oleBatchProcessProfileProtectedFieldList = new ArrayList<OLEBatchProcessProfileProtectedField>();
    private List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsList = new ArrayList<OLEBatchProcessProfileConstantsBo>();
    private List<OLEBatchProcessProfileInstanceMatchPoint> oleBatchProcessProfileInstanceMatchPointList = new ArrayList<OLEBatchProcessProfileInstanceMatchPoint>();
    private List<OLEBatchProcessProfileBibMatchPoint> oleBatchProcessProfileBibMatchPointList = new ArrayList<OLEBatchProcessProfileBibMatchPoint>();
    private List<OLEBatchProcessProfileMatchPoint> oleBatchProcessProfileMatchPointList = new ArrayList<OLEBatchProcessProfileMatchPoint>();
    private List<OLEBatchProcessProfileMatchPoint> oleBatchProcessProfileBibliographicMatchPointList = new ArrayList<OLEBatchProcessProfileMatchPoint>();
    private List<OLEBatchProcessProfileMatchPoint> oleBatchProcessProfileHoldingMatchPointList = new ArrayList<OLEBatchProcessProfileMatchPoint>();
    private List<OLEBatchProcessProfileMatchPoint> oleBatchProcessProfileItemMatchPointList = new ArrayList<OLEBatchProcessProfileMatchPoint>();
    private List<OLEBatchProcessProfileMatchPoint> oleBatchProcessProfileEholdingMatchPointList = new ArrayList<OLEBatchProcessProfileMatchPoint>();

    private List<OLEBatchProcessProfileBibMatchPoint> deletedBatchProcessProfileBibMatchPointList = new ArrayList<OLEBatchProcessProfileBibMatchPoint>();
    private List<OLEBatchProcessProfileBibStatus> deleteBatchProcessProfileBibStatusList = new ArrayList<OLEBatchProcessProfileBibStatus>();
    private List<OLEBatchProcessProfileDeleteField> deletedBatchProcessProfileDeleteFieldsList = new ArrayList<OLEBatchProcessProfileDeleteField>();
    private List<OLEBatchProcessProfileRenameField> deletedBatchProcessProfileRenameFieldsList = new ArrayList<OLEBatchProcessProfileRenameField>();
    private List<OLEBatchProcessProfileFilterCriteriaBo> deleteBatchProcessProfileFilterCriteriaList = new ArrayList<OLEBatchProcessProfileFilterCriteriaBo>();
    private List<OLEBatchProcessProfileMappingOptionsBo> deletedBatchProcessProfileMappingOptionsList = new ArrayList<OLEBatchProcessProfileMappingOptionsBo>();
    private List<OLEBatchProcessProfileDataMappingOptionsBo>  deletedBatchProcessProfileDataMappingOptionsList= new ArrayList<OLEBatchProcessProfileDataMappingOptionsBo>();
    private List<OLEBatchProcessProfileConstantsBo> deletedBatchProcessProfileConstantsList = new ArrayList<OLEBatchProcessProfileConstantsBo>();
    private List<OLEBatchProcessProfileProtectedField> deletedBatchProcessProfileProtectedFieldList = new ArrayList<OLEBatchProcessProfileProtectedField>();
    private List<OLEBatchProcessProfileMatchPoint> deletedBatchProcessProfileMatchPointList = new ArrayList<OLEBatchProcessProfileMatchPoint>();
    private  MatchingProfile matchingProfileObj;

    private String dataToImport;
    private String dataToExport;
    private String exportScope;
    private String requisitionsforTitle;
    private String bibOverlayOrAddOrNone;
    private List<OLEBatchProcessProfileBibStatus> oleBatchProcessProfileBibStatusList = new ArrayList<OLEBatchProcessProfileBibStatus>();
    private List<OLEBatchProcessProfileBibWorkUnit> oleBatchProcessProfileBibWorkUnitList = new ArrayList<OLEBatchProcessProfileBibWorkUnit>();
    private List<OLEBatchProcessProfileInstanceWorkUnit> oleBatchProcessProfileInstanceWorkUnitList = new ArrayList<OLEBatchProcessProfileInstanceWorkUnit>();
    private String bibMatch;
    private String bibNoMatch;
    private String bibNoMatchAction;
    private String instanceOverlayOrAddOrNone;
    private String instanceMatch;
    private String instanceNoMatch;
    private String instanceNoMatchAction;

    private String newBibStaus;
    private String existedBibStatus;
    private String noChangeOrSet;
    private boolean bibStaffOnly;
    private boolean instanceStaffOnly;
    private boolean itemStaffOnly;
    private String dontChange001;
    private String prepend003To035;
    private String prependvalueto035;
    private String valueToPrepend;
    private boolean removeValueFrom001;
    private String valueToRemove;
    private List<OLEBatchProcessProfileDeleteField> oleBatchProcessProfileDeleteFieldsList = new ArrayList<OLEBatchProcessProfileDeleteField>();
    private List<OLEBatchProcessProfileRenameField> oleBatchProcessProfileRenameFieldsList = new ArrayList<OLEBatchProcessProfileRenameField>();
    private boolean overlay=false;
    private String bibMultipleMatch;
    private String instanceMultipleMatch;
    private Boolean marcOnly;
    private String overlayNoChangeOrSet;
    private boolean overlayBibStaffOnly;
    private String bibImportProfileType = "Bib Import";
    private String dummyAttributeName;

    private List<OLEBatchProcessBibDataMappingNew> oleBatchProcessBibDataMappingNewList = new ArrayList<>();
    private List<OLEBatchProcessBibDataMappingNew> deleteBatchProcessBibDataMappingNewList = new ArrayList<>();

    private List<OLEBatchProcessBibDataMappingOverlay> oleBatchProcessBibDataMappingOverlayList = new ArrayList<>();
    private List<OLEBatchProcessBibDataMappingOverlay> deleteBatchProcessBibDataMappingOverlayList = new ArrayList<>();

    private String matchingProfile;

    public String getDummyAttributeName() {
        return dummyAttributeName;
    }

    public void setDummyAttributeName(String dummyAttributeName) {
        this.dummyAttributeName = dummyAttributeName;
    }

    public Boolean getMarcOnly() {
        return marcOnly;
    }

    public void setMarcOnly(Boolean marcOnly) {
        this.marcOnly = marcOnly;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    private String fileType;

    public boolean isOverlay() {
        return overlay;
    }

    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
    }

    public String getBatchProcessProfileId() {
        return batchProcessProfileId;
    }

    public void setBatchProcessProfileId(String batchProcessProfileId) {
        this.batchProcessProfileId = batchProcessProfileId;
    }

    public String getBatchProcessProfileName() {
        return batchProcessProfileName;
    }

    public void setBatchProcessProfileName(String batchProcessProfileName) {
        this.batchProcessProfileName = batchProcessProfileName;
    }

    public String getBatchProcessProfileDesc() {
        return batchProcessProfileDesc;
    }

    public void setBatchProcessProfileDesc(String batchProcessProfileDesc) {
        this.batchProcessProfileDesc = batchProcessProfileDesc;
    }

    public String getBatchProcessProfileType() {
        return batchProcessProfileType;
    }

    public void setBatchProcessProfileType(String batchProcessProfileType) {
        this.batchProcessProfileType = batchProcessProfileType;
    }

    public String getKrmsProfileName() {
        return krmsProfileName;
    }

    public void setKrmsProfileName(String krmsProfileName) {
        this.krmsProfileName = krmsProfileName;
    }

    public OLEBatchProcessTypeBo getOleBatchProcessTypeBo() {
        return oleBatchProcessTypeBo;
    }

    public void setOleBatchProcessTypeBo(OLEBatchProcessTypeBo oleBatchProcessTypeBo) {
        this.oleBatchProcessTypeBo = oleBatchProcessTypeBo;
    }

    public List<OLEBatchProcessProfileFilterCriteriaBo> getOleBatchProcessProfileFilterCriteriaList() {
        return oleBatchProcessProfileFilterCriteriaList;
    }

    public void setOleBatchProcessProfileFilterCriteriaList(List<OLEBatchProcessProfileFilterCriteriaBo> oleBatchProcessProfileFilterCriteriaList) {
        this.oleBatchProcessProfileFilterCriteriaList = oleBatchProcessProfileFilterCriteriaList;
    }

    public List<OLEBatchProcessProfileMappingOptionsBo> getOleBatchProcessProfileMappingOptionsList() {
        return oleBatchProcessProfileMappingOptionsList;
    }

    public void setOleBatchProcessProfileMappingOptionsList(List<OLEBatchProcessProfileMappingOptionsBo> oleBatchProcessProfileMappingOptionsList) {
        this.oleBatchProcessProfileMappingOptionsList = oleBatchProcessProfileMappingOptionsList;
    }

    public List<OLEBatchProcessProfileDataMappingOptionsBo> getOleBatchProcessProfileDataMappingOptionsBoList() {
        return oleBatchProcessProfileDataMappingOptionsBoList;
    }

    public void setOleBatchProcessProfileDataMappingOptionsBoList(List<OLEBatchProcessProfileDataMappingOptionsBo> oleBatchProcessProfileDataMappingOptionsBoList) {
        this.oleBatchProcessProfileDataMappingOptionsBoList = oleBatchProcessProfileDataMappingOptionsBoList;
    }

    public List<OLEBatchGloballyProtectedField> getOleBatchGloballyProtectedFieldList() {
        return oleBatchGloballyProtectedFieldList;
    }

    public void setOleBatchGloballyProtectedFieldList(List<OLEBatchGloballyProtectedField> oleBatchGloballyProtectedFieldList) {
        this.oleBatchGloballyProtectedFieldList = oleBatchGloballyProtectedFieldList;
    }

    public List<OLEBatchProcessProfileProtectedField> getOleBatchProcessProfileProtectedFieldList() {
        return oleBatchProcessProfileProtectedFieldList;
    }

    public void setOleBatchProcessProfileProtectedFieldList(List<OLEBatchProcessProfileProtectedField> oleBatchProcessProfileProtectedFieldList) {
        this.oleBatchProcessProfileProtectedFieldList = oleBatchProcessProfileProtectedFieldList;
    }

    public List<OLEBatchProcessProfileConstantsBo> getOleBatchProcessProfileConstantsList() {
        return oleBatchProcessProfileConstantsList;
    }

    public void setOleBatchProcessProfileConstantsList(List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsList) {
        this.oleBatchProcessProfileConstantsList = oleBatchProcessProfileConstantsList;
    }

    public List<OLEBatchProcessProfileInstanceMatchPoint> getOleBatchProcessProfileInstanceMatchPointList() {
        return oleBatchProcessProfileInstanceMatchPointList;
    }

    public void setOleBatchProcessProfileInstanceMatchPointList(List<OLEBatchProcessProfileInstanceMatchPoint> oleBatchProcessProfileInstanceMatchPointList) {
        this.oleBatchProcessProfileInstanceMatchPointList = oleBatchProcessProfileInstanceMatchPointList;
    }

    public List<OLEBatchProcessProfileBibMatchPoint> getOleBatchProcessProfileBibMatchPointList() {
        return oleBatchProcessProfileBibMatchPointList;
    }

    public void setOleBatchProcessProfileBibMatchPointList(List<OLEBatchProcessProfileBibMatchPoint> oleBatchProcessProfileBibMatchPointList) {
        this.oleBatchProcessProfileBibMatchPointList = oleBatchProcessProfileBibMatchPointList;
    }

    public String getDataToImport() {
        return dataToImport;
    }

    public void setDataToImport(String dataToImport) {
        this.dataToImport = dataToImport;
    }

    public String getRequisitionsforTitle() {
        return requisitionsforTitle;
    }

    public void setRequisitionsforTitle(String requisitionsforTitle) {
        this.requisitionsforTitle = requisitionsforTitle;
    }

    public String getBibOverlayOrAddOrNone() {
        return bibOverlayOrAddOrNone;
    }

    public void setBibOverlayOrAddOrNone(String bibOverlayOrAddOrNone) {
        this.bibOverlayOrAddOrNone = bibOverlayOrAddOrNone;
    }

    public List<OLEBatchProcessProfileBibStatus> getOleBatchProcessProfileBibStatusList() {
        return oleBatchProcessProfileBibStatusList;
    }

    public void setOleBatchProcessProfileBibStatusList(List<OLEBatchProcessProfileBibStatus> oleBatchProcessProfileBibStatusList) {
        this.oleBatchProcessProfileBibStatusList = oleBatchProcessProfileBibStatusList;
    }

    public List<OLEBatchProcessProfileBibWorkUnit> getOleBatchProcessProfileBibWorkUnitList() {
        return oleBatchProcessProfileBibWorkUnitList;
    }

    public void setOleBatchProcessProfileBibWorkUnitList(List<OLEBatchProcessProfileBibWorkUnit> oleBatchProcessProfileBibWorkUnitList) {
        this.oleBatchProcessProfileBibWorkUnitList = oleBatchProcessProfileBibWorkUnitList;
    }

    public List<OLEBatchProcessProfileInstanceWorkUnit> getOleBatchProcessProfileInstanceWorkUnitList() {
        return oleBatchProcessProfileInstanceWorkUnitList;
    }

    public void setOleBatchProcessProfileInstanceWorkUnitList(List<OLEBatchProcessProfileInstanceWorkUnit> oleBatchProcessProfileInstanceWorkUnitList) {
        this.oleBatchProcessProfileInstanceWorkUnitList = oleBatchProcessProfileInstanceWorkUnitList;
    }

    public String getBibMatch() {
        return bibMatch;
    }

    public void setBibMatch(String bibMatch) {
        this.bibMatch = bibMatch;
    }

    public String getBibNoMatch() {
        return bibNoMatch;
    }

    public void setBibNoMatch(String bibNoMatch) {
        this.bibNoMatch = bibNoMatch;
    }

    public String getBibNoMatchAction() {
        return bibNoMatchAction;
    }

    public void setBibNoMatchAction(String bibNoMatchAction) {
        this.bibNoMatchAction = bibNoMatchAction;
    }

    public String getInstanceOverlayOrAddOrNone() {
        return instanceOverlayOrAddOrNone;
    }

    public void setInstanceOverlayOrAddOrNone(String instanceOverlayOrAddOrNone) {
        this.instanceOverlayOrAddOrNone = instanceOverlayOrAddOrNone;
    }

    public String getInstanceMatch() {
        return instanceMatch;
    }

    public void setInstanceMatch(String instanceMatch) {
        this.instanceMatch = instanceMatch;
    }

    public String getInstanceNoMatch() {
        return instanceNoMatch;
    }

    public void setInstanceNoMatch(String instanceNoMatch) {
        this.instanceNoMatch = instanceNoMatch;
    }

    public String getInstanceNoMatchAction() {
        return instanceNoMatchAction;
    }

    public void setInstanceNoMatchAction(String instanceNoMatchAction) {
        this.instanceNoMatchAction = instanceNoMatchAction;
    }

    public String getNewBibStaus() {
        return newBibStaus;
    }

    public void setNewBibStaus(String newBibStaus) {
        this.newBibStaus = newBibStaus;
    }

    public String getExistedBibStatus() {
        return existedBibStatus;
    }

    public void setExistedBibStatus(String existedBibStatus) {
        this.existedBibStatus = existedBibStatus;
    }

    public String getNoChangeOrSet() {
        return noChangeOrSet;
    }

    public void setNoChangeOrSet(String noChangeOrSet) {
        this.noChangeOrSet = noChangeOrSet;
    }

    public boolean isBibStaffOnly() {
        return bibStaffOnly;
    }

    public void setBibStaffOnly(boolean bibStaffOnly) {
        this.bibStaffOnly = bibStaffOnly;
    }

    public boolean isInstanceStaffOnly() {
        return instanceStaffOnly;
    }

    public void setInstanceStaffOnly(boolean instanceStaffOnly) {
        this.instanceStaffOnly = instanceStaffOnly;
    }

    public boolean isItemStaffOnly() {
        return itemStaffOnly;
    }

    public void setItemStaffOnly(boolean itemStaffOnly) {
        this.itemStaffOnly = itemStaffOnly;
    }

    public String getDontChange001() {
        return dontChange001;
    }

    public void setDontChange001(String dontChange001) {
        this.dontChange001 = dontChange001;
    }

    public String getValueToPrepend() {
        return valueToPrepend;
    }

    public void setValueToPrepend(String valueToPrepend) {
        this.valueToPrepend = valueToPrepend;
    }

    public List<OLEBatchProcessProfileDeleteField> getOleBatchProcessProfileDeleteFieldsList() {
        return oleBatchProcessProfileDeleteFieldsList;
    }

    public void setOleBatchProcessProfileDeleteFieldsList(List<OLEBatchProcessProfileDeleteField> oleBatchProcessProfileDeleteFieldsList) {
        this.oleBatchProcessProfileDeleteFieldsList = oleBatchProcessProfileDeleteFieldsList;
    }

    public List<OLEBatchProcessProfileRenameField> getOleBatchProcessProfileRenameFieldsList() {
        return oleBatchProcessProfileRenameFieldsList;
    }

    public void setOleBatchProcessProfileRenameFieldsList(List<OLEBatchProcessProfileRenameField> oleBatchProcessProfileRenameFieldsList) {
        this.oleBatchProcessProfileRenameFieldsList = oleBatchProcessProfileRenameFieldsList;
    }

    public String getDataToExport() {
        return dataToExport;
    }

    public void setDataToExport(String dataToExport) {
        this.dataToExport = dataToExport;
    }

    public String getExportScope() {
        return exportScope;
    }

    public void setExportScope(String exportScope) {
        this.exportScope = exportScope;
    }

    public String getPrepend003To035() {
        return prepend003To035;
    }

    public void setPrepend003To035(String prepend003To035) {
        this.prepend003To035 = prepend003To035;
    }

    public String getPrependvalueto035() {
        return prependvalueto035;
    }

    public void setPrependvalueto035(String prependvalueto035) {
        this.prependvalueto035 = prependvalueto035;
    }

    public String getBibMultipleMatch() {
        return bibMultipleMatch;
    }

    public void setBibMultipleMatch(String bibMultipleMatch) {
        this.bibMultipleMatch = bibMultipleMatch;
    }

    public String getInstanceMultipleMatch() {
        return instanceMultipleMatch;
    }

    public void setInstanceMultipleMatch(String instanceMultipleMatch) {
        this.instanceMultipleMatch = instanceMultipleMatch;
    }

    public List<OLEBatchProcessProfileBibMatchPoint> getDeletedBatchProcessProfileBibMatchPointList() {
        return deletedBatchProcessProfileBibMatchPointList;
    }

    public void setDeletedBatchProcessProfileBibMatchPointList(List<OLEBatchProcessProfileBibMatchPoint> deletedBatchProcessProfileBibMatchPointList) {
        this.deletedBatchProcessProfileBibMatchPointList = deletedBatchProcessProfileBibMatchPointList;
    }

    public List<OLEBatchProcessProfileBibStatus> getDeleteBatchProcessProfileBibStatusList() {
        return deleteBatchProcessProfileBibStatusList;
    }

    public void setDeleteBatchProcessProfileBibStatusList(List<OLEBatchProcessProfileBibStatus> deleteBatchProcessProfileBibStatusList) {
        this.deleteBatchProcessProfileBibStatusList = deleteBatchProcessProfileBibStatusList;
    }

    public List<OLEBatchProcessProfileDeleteField> getDeletedBatchProcessProfileDeleteFieldsList() {
        return deletedBatchProcessProfileDeleteFieldsList;
    }

    public void setDeletedBatchProcessProfileDeleteFieldsList(List<OLEBatchProcessProfileDeleteField> deletedBatchProcessProfileDeleteFieldsList) {
        this.deletedBatchProcessProfileDeleteFieldsList = deletedBatchProcessProfileDeleteFieldsList;
    }

    public List<OLEBatchProcessProfileRenameField> getDeletedBatchProcessProfileRenameFieldsList() {
        return deletedBatchProcessProfileRenameFieldsList;
    }

    public void setDeletedBatchProcessProfileRenameFieldsList(List<OLEBatchProcessProfileRenameField> deletedBatchProcessProfileRenameFieldsList) {
        this.deletedBatchProcessProfileRenameFieldsList = deletedBatchProcessProfileRenameFieldsList;
    }

    public List<OLEBatchProcessProfileFilterCriteriaBo> getDeleteBatchProcessProfileFilterCriteriaList() {
        return deleteBatchProcessProfileFilterCriteriaList;
    }

    public void setDeleteBatchProcessProfileFilterCriteriaList(List<OLEBatchProcessProfileFilterCriteriaBo> deleteBatchProcessProfileFilterCriteriaList) {
        this.deleteBatchProcessProfileFilterCriteriaList = deleteBatchProcessProfileFilterCriteriaList;
    }

    public List<OLEBatchProcessProfileMappingOptionsBo> getDeletedBatchProcessProfileMappingOptionsList() {
        return deletedBatchProcessProfileMappingOptionsList;
    }

    public void setDeletedBatchProcessProfileMappingOptionsList(List<OLEBatchProcessProfileMappingOptionsBo> deletedBatchProcessProfileMappingOptionsList) {
        this.deletedBatchProcessProfileMappingOptionsList = deletedBatchProcessProfileMappingOptionsList;
    }

    public List<OLEBatchProcessProfileDataMappingOptionsBo> getDeletedBatchProcessProfileDataMappingOptionsList() {
        return deletedBatchProcessProfileDataMappingOptionsList;
    }

    public void setDeletedBatchProcessProfileDataMappingOptionsList(List<OLEBatchProcessProfileDataMappingOptionsBo> deletedBatchProcessProfileDataMappingOptionsList) {
        this.deletedBatchProcessProfileDataMappingOptionsList = deletedBatchProcessProfileDataMappingOptionsList;
    }

    public List<OLEBatchProcessProfileConstantsBo> getDeletedBatchProcessProfileConstantsList() {
        return deletedBatchProcessProfileConstantsList;
    }

    public void setDeletedBatchProcessProfileConstantsList(List<OLEBatchProcessProfileConstantsBo> deletedBatchProcessProfileConstantsList) {
        this.deletedBatchProcessProfileConstantsList = deletedBatchProcessProfileConstantsList;
    }

    public List<OLEBatchProcessProfileProtectedField> getDeletedBatchProcessProfileProtectedFieldList() {
        return deletedBatchProcessProfileProtectedFieldList;
    }

    public void setDeletedBatchProcessProfileProtectedFieldList(List<OLEBatchProcessProfileProtectedField> deletedBatchProcessProfileProtectedFieldList) {
        this.deletedBatchProcessProfileProtectedFieldList = deletedBatchProcessProfileProtectedFieldList;
    }

    public List<OLEBatchProcessProfileMatchPoint> getDeletedBatchProcessProfileMatchPointList() {
        return deletedBatchProcessProfileMatchPointList;
    }

    public void setDeletedBatchProcessProfileMatchPointList(List<OLEBatchProcessProfileMatchPoint> deletedBatchProcessProfileMatchPointList) {
        this.deletedBatchProcessProfileMatchPointList = deletedBatchProcessProfileMatchPointList;
    }

    public boolean getRemoveValueFrom001() {
        return removeValueFrom001;
    }

    public void setRemoveValueFrom001(boolean removeValueFrom001) {
        this.removeValueFrom001 = removeValueFrom001;
    }

    public String getValueToRemove() {
        return valueToRemove;
    }

    public void setValueToRemove(String valueToRemove) {
        this.valueToRemove = valueToRemove;
    }

    public String getOverlayNoChangeOrSet() {
        return overlayNoChangeOrSet;
    }

    public void setOverlayNoChangeOrSet(String overlayNoChangeOrSet) {
        this.overlayNoChangeOrSet = overlayNoChangeOrSet;
    }

    public boolean isOverlayBibStaffOnly() {
        return overlayBibStaffOnly;
    }

    public void setOverlayBibStaffOnly(boolean overlayBibStaffOnly) {
        this.overlayBibStaffOnly = overlayBibStaffOnly;
    }

    public String getBibImportProfileForOrderRecord() {
        return bibImportProfileForOrderRecord;
    }

    public void setBibImportProfileForOrderRecord(String bibImportProfileForOrderRecord) {
        this.bibImportProfileForOrderRecord = bibImportProfileForOrderRecord;
    }

    public String getBibImportProfileType() {
        return bibImportProfileType;
    }

    public void setBibImportProfileType(String bibImportProfileType) {
        this.bibImportProfileType = bibImportProfileType;
    }

    @Override
    public int compareTo(OLEBatchProcessProfileBo o) {
        return batchProcessProfileId.compareTo(o.batchProcessProfileId);
    }

    public List<OLEBatchProcessProfileMatchPoint> getOleBatchProcessProfileMatchPointList() {
        return oleBatchProcessProfileMatchPointList;
    }

    public void setOleBatchProcessProfileMatchPointList(List<OLEBatchProcessProfileMatchPoint> oleBatchProcessProfileMatchPointList) {
        this.oleBatchProcessProfileMatchPointList = oleBatchProcessProfileMatchPointList;
    }

    public List<OLEBatchProcessProfileMatchPoint> getOleBatchProcessProfileBibliographicMatchPointList() {
        return oleBatchProcessProfileBibliographicMatchPointList;
    }

    public void setOleBatchProcessProfileBibliographicMatchPointList(List<OLEBatchProcessProfileMatchPoint> oleBatchProcessProfileBibliographicMatchPointList) {
        this.oleBatchProcessProfileBibliographicMatchPointList = oleBatchProcessProfileBibliographicMatchPointList;
    }

    public List<OLEBatchProcessProfileMatchPoint> getOleBatchProcessProfileHoldingMatchPointList() {
        return oleBatchProcessProfileHoldingMatchPointList;
    }

    public void setOleBatchProcessProfileHoldingMatchPointList(List<OLEBatchProcessProfileMatchPoint> oleBatchProcessProfileHoldingMatchPointList) {
        this.oleBatchProcessProfileHoldingMatchPointList = oleBatchProcessProfileHoldingMatchPointList;
    }

    public List<OLEBatchProcessProfileMatchPoint> getOleBatchProcessProfileItemMatchPointList() {
        return oleBatchProcessProfileItemMatchPointList;
    }

    public void setOleBatchProcessProfileItemMatchPointList(List<OLEBatchProcessProfileMatchPoint> oleBatchProcessProfileItemMatchPointList) {
        this.oleBatchProcessProfileItemMatchPointList = oleBatchProcessProfileItemMatchPointList;
    }

    public List<OLEBatchProcessProfileMatchPoint> getOleBatchProcessProfileEholdingMatchPointList() {
        return oleBatchProcessProfileEholdingMatchPointList;
    }

    public void setOleBatchProcessProfileEholdingMatchPointList(List<OLEBatchProcessProfileMatchPoint> oleBatchProcessProfileEholdingMatchPointList) {
        this.oleBatchProcessProfileEholdingMatchPointList = oleBatchProcessProfileEholdingMatchPointList;
    }


    public String getMatchingProfile() {
        return matchingProfile;
    }

    public void setMatchingProfile(String matchingProfile) {
        this.matchingProfile = matchingProfile;
    }

    public MatchingProfile getMatchingProfileObj() {
        if(matchingProfileObj==null && StringUtils.isNotBlank(this.matchingProfile)) {
             try {
                matchingProfileObj = MatchingProfile.buildMatchProfileObj(this.matchingProfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return matchingProfileObj;
    }

    public void setMatchingProfileObj(MatchingProfile matchingProfileObj) {
        this.matchingProfileObj = matchingProfileObj;
    }

    public List<OLEBatchProcessBibDataMappingNew> getOleBatchProcessBibDataMappingNewList() {
        return oleBatchProcessBibDataMappingNewList;
    }

    public void setOleBatchProcessBibDataMappingNewList(List<OLEBatchProcessBibDataMappingNew> oleBatchProcessBibDataMappingNewList) {
        this.oleBatchProcessBibDataMappingNewList = oleBatchProcessBibDataMappingNewList;
    }

    public List<OLEBatchProcessBibDataMappingNew> getDeleteBatchProcessBibDataMappingNewList() {
        return deleteBatchProcessBibDataMappingNewList;
    }

    public void setDeleteBatchProcessBibDataMappingNewList(List<OLEBatchProcessBibDataMappingNew> deleteBatchProcessBibDataMappingNewList) {
        this.deleteBatchProcessBibDataMappingNewList = deleteBatchProcessBibDataMappingNewList;
    }

    public List<OLEBatchProcessBibDataMappingOverlay> getDeleteBatchProcessBibDataMappingOverlayList() {
        return deleteBatchProcessBibDataMappingOverlayList;
    }

    public void setDeleteBatchProcessBibDataMappingOverlayList(List<OLEBatchProcessBibDataMappingOverlay> deleteBatchProcessBibDataMappingOverlayList) {
        this.deleteBatchProcessBibDataMappingOverlayList = deleteBatchProcessBibDataMappingOverlayList;
    }

    public List<OLEBatchProcessBibDataMappingOverlay> getOleBatchProcessBibDataMappingOverlayList() {
        return oleBatchProcessBibDataMappingOverlayList;
    }

    public void setOleBatchProcessBibDataMappingOverlayList(List<OLEBatchProcessBibDataMappingOverlay> oleBatchProcessBibDataMappingOverlayList) {
        this.oleBatchProcessBibDataMappingOverlayList = oleBatchProcessBibDataMappingOverlayList;
    }
}
