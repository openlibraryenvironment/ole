package org.kuali.ole.docstore.engine.service.storage.rdbms;


import java.util.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.audit.Audit;
import org.kuali.ole.audit.HoldingsAudit;
import org.kuali.ole.audit.OleAuditManager;
import org.kuali.ole.docstore.DocStoreConstants;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;
import org.kuali.ole.docstore.common.exception.DocstoreValidationException;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.*;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.CallNumberTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.EInstanceCoverageRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.EInstancePerpetualAccessRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ExtentNoteRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ExtentOfOwnerShipRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ExtentOfOwnerShipTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsNoteRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.InstanceRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ReceiptStatusRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.StatisticalSearchRecord;
import org.kuali.ole.utility.callnumber.CallNumberFactory;
import org.kuali.ole.utility.callnumber.CallNumberType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/17/13
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class RdbmsHoldingsDocumentManager extends RdbmsAbstarctDocumentManager {

    private static final Logger LOG = LoggerFactory.getLogger(RdbmsHoldingsDocumentManager.class);
    private static RdbmsHoldingsDocumentManager rdbmsHoldingsDocumentManager = null;

    private HoldingOlemlRecordProcessor workHoldingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();

    public static RdbmsHoldingsDocumentManager getInstance() {
        if (rdbmsHoldingsDocumentManager == null) {
            rdbmsHoldingsDocumentManager = new RdbmsHoldingsDocumentManager();
        }
        return rdbmsHoldingsDocumentManager;
    }


    @Override
    public void create(Object object) {
        Holdings holdings = (Holdings) object;
        HoldingsRecord holdingsRecord = new HoldingsRecord();
        String bibId = "";
        if (holdings != null && holdings.getBib() != null && StringUtils.isNotEmpty(holdings.getBib().getId())) {
            bibId = holdings.getBib().getId();
        } else {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.BIB_ID_NOT_FOUND, DocstoreResources.BIB_ID_NOT_FOUND);
            throw docstoreException;
        }
        holdingsRecord.setCreatedBy(holdings.getCreatedBy());
        holdingsRecord.setCreatedDate(createdDate());
        holdingsRecord.setUpdatedBy(holdings.getCreatedBy());
        holdingsRecord.setUpdatedDate(createdDate());
        String content = holdings.getContent();
        OleHoldings oleHoldings = workHoldingOlemlRecordProcessor.fromXML(content);
        holdingsRecord.setStaffOnlyFlag(holdings.isStaffOnly());
        holdingsRecord.setBibId(DocumentUniqueIDPrefix.getDocumentId(bibId));
        holdingsRecord.setUniqueIdPrefix(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML);

        processCallNumber(oleHoldings);

        holdingsRecord.setHoldingsType(holdings.getHoldingsType());
        setHoldingsCommonInformation(oleHoldings, holdingsRecord);

        if (holdings instanceof PHoldings || PHoldings.PRINT.equalsIgnoreCase(holdings.getHoldingsType())) {
            setPHoldingsInformation(holdingsRecord, oleHoldings);
        } else {
            setEHoldingsInformation(oleHoldings, holdingsRecord);
        }
        getBusinessObjectService().save(holdingsRecord);
        holdings.setId(DocumentUniqueIDPrefix.getPrefixedId(holdingsRecord.getUniqueIdPrefix(), holdingsRecord.getHoldingsId()));
        content = workHoldingOlemlRecordProcessor.toXML(oleHoldings);
        holdings.setContent(content);
        buildLabelForHoldings(holdingsRecord, holdings);
    }

    private void setEHoldingsInformation(OleHoldings oleHoldings, HoldingsRecord holdingsRecord) {

        if (oleHoldings.getHoldingsAccessInformation() != null) {
            AuthenticationTypeRecord authenticationTypeRecord = saveAuthenticationType(oleHoldings.getHoldingsAccessInformation().getAuthenticationType());
            holdingsRecord.setAuthenticationTypeId(authenticationTypeRecord.getAuthenticationTypeId());
            holdingsRecord.setAccessPassword(oleHoldings.getHoldingsAccessInformation().getAccessPassword());
            holdingsRecord.setAccessUserName(oleHoldings.getHoldingsAccessInformation().getAccessUsername());
            holdingsRecord.setMaterialsSpecified(oleHoldings.getHoldingsAccessInformation().getMaterialsSpecified());
            holdingsRecord.setFirstIndicator(oleHoldings.getHoldingsAccessInformation().getFirstIndicator());
            holdingsRecord.setSecondIndicator(oleHoldings.getHoldingsAccessInformation().getSecondIndicator());

            if (oleHoldings.getHoldingsAccessInformation() != null && StringUtils.isNotEmpty(oleHoldings.getHoldingsAccessInformation().getNumberOfSimultaneousUser())) {
                holdingsRecord.setNumberSimultaneousUsers(oleHoldings.getHoldingsAccessInformation().getNumberOfSimultaneousUser());
            }
            holdingsRecord.setProxiedResource(oleHoldings.getHoldingsAccessInformation().getProxiedResource());
        }
        holdingsRecord.setAccessStatus(oleHoldings.getAccessStatus());
        if (oleHoldings.getLink() != null) {
//            holdingsRecord.setLink(oleHoldings.getLink().getUrl() != null ? oleHoldings.getLink().getUrl() : "");
//            holdingsRecord.setLinkText(oleHoldings.getLink().getText() != null ? oleHoldings.getLink().getText() : "");
            saveLink(oleHoldings.getLink(), holdingsRecord.getHoldingsId());
        }
        holdingsRecord.setImprint(oleHoldings.getImprint() != null ? oleHoldings.getImprint() : "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Timestamp statusDate = null;
        try {
            if (oleHoldings.getStatusDate() != null) {
                statusDate = new Timestamp(sdf.parse(oleHoldings.getStatusDate()).getTime());

            }
        } catch (Exception e) {
            LOG.error("Exception : ", e);
        }
        holdingsRecord.setStatusDate(statusDate);

        holdingsRecord.seteResourceId(oleHoldings.getEResourceId());
        holdingsRecord.setPublisherId(oleHoldings.getPublisher());
        holdingsRecord.setLocalPersistentUri(oleHoldings.getLocalPersistentLink());

        holdingsRecord.setAllowIll(oleHoldings.isInterLibraryLoanAllowed());
        holdingsRecord.setSubscriptionStatus(oleHoldings.getSubscriptionStatus());
        if (oleHoldings.getPlatform() != null) {
            holdingsRecord.setPlatform(oleHoldings.getPlatform().getPlatformName());
            holdingsRecord.setAdminUrl(oleHoldings.getPlatform().getAdminUrl());
            holdingsRecord.setAdminPassword(oleHoldings.getPlatform().getAdminPassword());
            holdingsRecord.setAdminUserName(oleHoldings.getPlatform().getAdminUserName());
        }
        if (oleHoldings.geteResourceSubscriptionStatus() == null || (oleHoldings.geteResourceSubscriptionStatus() != null && oleHoldings.getSubscriptionStatus() != oleHoldings.geteResourceSubscriptionStatus())) {
            holdingsRecord.setSubscriptionStatus(oleHoldings.getSubscriptionStatus());
        }
        holdingsRecord.setCancellationCandidate(oleHoldings.isCancellationCandidate());
        if (oleHoldings.geteResourceCancellationReason() == null || (oleHoldings.geteResourceCancellationReason() != null && oleHoldings.getCancellationReason() != oleHoldings.geteResourceCancellationReason())) {
            holdingsRecord.setCancellationReason(oleHoldings.getCancellationReason());
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            if (StringUtils.isNotBlank(oleHoldings.getCancellationDecisionDate()) && (oleHoldings.geteResourceCancellationDecisionDate() == null || (oleHoldings.geteResourceCancellationDecisionDate() != null &&
                    (new Timestamp(format.parse(oleHoldings.getCancellationDecisionDate()).getTime())) != oleHoldings.geteResourceCancellationDecisionDate()))) {
                holdingsRecord.setCancellationDecisionDate(new Timestamp(format.parse(oleHoldings.getCancellationDecisionDate()).getTime()));
            }
            if (StringUtils.isNotBlank(oleHoldings.getCancellationEffectiveDate()) && (oleHoldings.geteResourceCancellationEffectiveDate() == null || (oleHoldings.geteResourceCancellationEffectiveDate() != null &&
                    (new Timestamp(format.parse(oleHoldings.getCancellationEffectiveDate()).getTime())) != oleHoldings.geteResourceCancellationEffectiveDate()))) {
                holdingsRecord.setCancellationEffectiveDate(new Timestamp(format.parse(oleHoldings.getCancellationEffectiveDate()).getTime()));
            }
            if (StringUtils.isNotBlank(oleHoldings.getInitialSubscriptionStartDate()) && (oleHoldings.geteResourceInitialSubscriptionStartDate() == null || (oleHoldings.geteResourceInitialSubscriptionStartDate() != null &&
                    (new Timestamp(format.parse(oleHoldings.getInitialSubscriptionStartDate()).getTime())) != oleHoldings.geteResourceInitialSubscriptionStartDate()))) {
                holdingsRecord.setInitialSubscriptionStartDate(new Timestamp(format.parse(oleHoldings.getInitialSubscriptionStartDate()).getTime()));
            }
            if (StringUtils.isNotBlank(oleHoldings.getCurrentSubscriptionStartDate()) && (oleHoldings.geteResourceCurrentSubscriptionStartDate() == null || (oleHoldings.geteResourceCurrentSubscriptionStartDate() != null &&
                    (new Timestamp(format.parse(oleHoldings.getCurrentSubscriptionStartDate()).getTime())) != oleHoldings.geteResourceCurrentSubscriptionStartDate()))) {
                holdingsRecord.setCurrentSubscriptionStartDate(new Timestamp(format.parse(oleHoldings.getCurrentSubscriptionStartDate()).getTime()));
            }
            if (StringUtils.isNotBlank(oleHoldings.getCurrentSubscriptionEndDate()) && (oleHoldings.geteResourceCurrentSubscriptionEndDate() == null || (oleHoldings.geteResourceCurrentSubscriptionEndDate() != null &&
                    (new Timestamp(format.parse(oleHoldings.getCurrentSubscriptionEndDate()).getTime())) != oleHoldings.geteResourceCurrentSubscriptionEndDate()))) {
                holdingsRecord.setCurrentSubscriptionEndDate(new Timestamp(format.parse(oleHoldings.getCurrentSubscriptionEndDate()).getTime()));
            }
        } catch (Exception e) {
            LOG.error("Exception : ", e);
        }

        getBusinessObjectService().save(holdingsRecord);
        if (oleHoldings.getStatisticalSearchingCode() != null) {
            List<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecord = saveHoldingsStatisticalSearchCode(oleHoldings.getStatisticalSearchingCode(), holdingsRecord.getHoldingsId());
            if(CollectionUtils.isNotEmpty(holdingsStatisticalSearchRecord)){
                holdingsRecord.setHoldingsStatisticalSearchRecords(holdingsStatisticalSearchRecord);
            }
        }
        if (oleHoldings.getExtentOfOwnership() != null) {
            saveEHoldingsExtentOfOwnerShip(oleHoldings.getExtentOfOwnership(), holdingsRecord.getHoldingsId());
        }
        if (oleHoldings.getHoldingsAccessInformation() != null && oleHoldings.getHoldingsAccessInformation().getAccessLocation() != null) {
            HoldingsAccessLocation holdingsAccessLocation = saveHoldingsAccessLocation(oleHoldings.getHoldingsAccessInformation().getAccessLocation(), holdingsRecord.getHoldingsId());
//            if(holdingsAccessLocation != null) {
//                holdingsRecord.setHoldingsAccessLocationId(holdingsAccessLocation.getHoldingsAccessLocationId());
//            }
        }
        if (oleHoldings.getDonorInfo() != null && oleHoldings.getDonorInfo().size() >= 0) {
            holdingsRecord = saveDonorList(oleHoldings.getDonorInfo(), holdingsRecord);
        }
        getBusinessObjectService().save(holdingsRecord);
    }

    private void saveLink(List<Link> links, String holdingsId) {
        getBusinessObjectService().deleteMatching(HoldingsUriRecord.class, getHoldingsMap(holdingsId));
        List<HoldingsUriRecord> holdingsUriRecords = new ArrayList<>();

        String linkInfo = "";
        for (Link link : links) {
            if (StringUtils.isNotBlank(link.getText()) || StringUtils.isNotBlank(link.getUrl())) {
                HoldingsUriRecord holdingsUriRecord = new HoldingsUriRecord();
                holdingsUriRecord.setText(link.getText());
                holdingsUriRecord.setUri(link.getUrl());
                holdingsUriRecord.setHoldingsId(holdingsId);
                linkInfo = "holdingsId:" + holdingsId + " uri:" + link.getUrl() + " text:" + link.getText() + " ";
                holdingsUriRecords.add(holdingsUriRecord);
            }

        }
        try {
        	getBusinessObjectService().save(holdingsUriRecords);
        } catch (Exception e) {
        	String errStr = "Exception saving Link for " + linkInfo;
            LOG.error(errStr + e.getMessage());
        }
    }

    private HoldingsAccessLocation saveHoldingsAccessLocation(String accessLocation, String holdingsId) {
        getBusinessObjectService().deleteMatching(HoldingsAccessLocation.class, getHoldingsMap(holdingsId));
        HoldingsAccessLocation holdingsAccessLocation = new HoldingsAccessLocation();
        holdingsAccessLocation.setHoldingsId(holdingsId);
        AccessLocation accessLocation1 = saveAccessLocation(accessLocation);
        holdingsAccessLocation.setAccessLocationId(accessLocation1.getAccessLocationId());
        getBusinessObjectService().save(holdingsAccessLocation);
        return holdingsAccessLocation;
    }

    private AccessLocation saveAccessLocation(String accessLocation) {
        Map map = new HashMap();
        map.put("code", accessLocation);
        AccessLocation accessLocation1 = getBusinessObjectService().findByPrimaryKey(AccessLocation.class, map);

        if (accessLocation1 == null) {
            accessLocation1 = new AccessLocation();
            accessLocation1.setCode(accessLocation);
            getBusinessObjectService().save(accessLocation1);
        }

        return accessLocation1;
    }

    private AuthenticationTypeRecord saveAuthenticationType(String authenticationType) {
        Map map = new HashMap();
        map.put("code", authenticationType);
        AuthenticationTypeRecord authenticationTypeRecord = getBusinessObjectService().findByPrimaryKey(AuthenticationTypeRecord.class, map);

        if (authenticationTypeRecord == null) {
            authenticationTypeRecord = new AuthenticationTypeRecord();
            authenticationTypeRecord.setCode(authenticationType);
            authenticationTypeRecord.setName(authenticationType);
            getBusinessObjectService().save(authenticationTypeRecord);
        }
        return authenticationTypeRecord;
    }

    private List<HoldingsStatisticalSearchRecord> saveHoldingsStatisticalSearchCode(StatisticalSearchingCode statisticalSearchingCode, String holdingsId) {
        if (StringUtils.isNotEmpty(statisticalSearchingCode.getCodeValue())) {
            StatisticalSearchRecord statisticalSearchRecord = saveStatisticalSearchRecord(statisticalSearchingCode);
            HoldingsStatisticalSearchRecord holdingsStatisticalSearchRecord = null;
            List<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecordList = new ArrayList<>();
            List<HoldingsStatisticalSearchRecord> holdingsStatisticalSearchRecords = (List<HoldingsStatisticalSearchRecord>) getBusinessObjectService().findMatching(HoldingsStatisticalSearchRecord.class, getHoldingsMap(holdingsId));
            if(holdingsStatisticalSearchRecords != null && holdingsStatisticalSearchRecords.size() > 0) {
                holdingsStatisticalSearchRecord = holdingsStatisticalSearchRecords.get(0);
            }
            else {
                holdingsStatisticalSearchRecord = new HoldingsStatisticalSearchRecord();
                holdingsStatisticalSearchRecord.setHoldingsId(holdingsId);
            }

            holdingsStatisticalSearchRecord.setHoldingsId(holdingsId);
            if (statisticalSearchRecord != null) {
                holdingsStatisticalSearchRecord.setStatisticalSearchId(statisticalSearchRecord.getStatisticalSearchId());
            }
            getBusinessObjectService().save(holdingsStatisticalSearchRecord);
            holdingsStatisticalSearchRecordList.add(holdingsStatisticalSearchRecord);
            return holdingsStatisticalSearchRecordList;
        }
        return null;
    }

    private void saveEHoldingsExtentOfOwnerShip(List<ExtentOfOwnership> extentOfOwnerships, String holdingsId) {
        if (extentOfOwnerships != null && extentOfOwnerships.size() > 0) {
            ExtentOfOwnership extentOfOwnership = extentOfOwnerships.get(0);
            if (extentOfOwnership.getCoverages() != null) {
                saveCoverageRecord(extentOfOwnership.getCoverages(), holdingsId);
            }
            if (extentOfOwnership.getPerpetualAccesses() != null) {
                savePerpetualAccessRecord(extentOfOwnership.getPerpetualAccesses(), holdingsId);
            }
        }

    }

    protected HoldingsRecord saveDonorList(List<DonorInfo> donorslist, HoldingsRecord holdingsRecord) {
        Map map = new HashMap();
        map.put("holdingsId", holdingsRecord.getHoldingsId());
        List<OLEHoldingsDonorRecord> holdingsDonorRecordList = (List<OLEHoldingsDonorRecord>) getBusinessObjectService().findMatching(OLEHoldingsDonorRecord.class, map);
        List<OLEHoldingsDonorRecord> holdingsDonorRecordListRemoved = (List<OLEHoldingsDonorRecord>) getBusinessObjectService().findMatching(OLEHoldingsDonorRecord.class, map);
        if (holdingsDonorRecordList != null && holdingsDonorRecordList.size() >= 0) {
            for(OLEHoldingsDonorRecord oleHoldingDonorRecord : holdingsDonorRecordList) {
                holdingsDonorRecordListRemoved.remove(oleHoldingDonorRecord);
            }
            holdingsRecord.setDonorList(holdingsDonorRecordListRemoved);
            getBusinessObjectService().save(holdingsRecord);
        }
        if (donorslist.size() > 0) {
            List<OLEHoldingsDonorRecord> oleHoldingsDonorRecords = new ArrayList<>();
            for (int i = 0; i < donorslist.size(); i++) {
                DonorInfo donorinfo = donorslist.get(i);
                if (StringUtils.isNotBlank(donorinfo.getDonorCode()) || StringUtils.isNotBlank(donorinfo.getDonorNote()) || StringUtils.isNotBlank(donorinfo.getDonorPublicDisplay())) {
                    OLEHoldingsDonorRecord oleHoldingsDonorRecord = new OLEHoldingsDonorRecord();
                    oleHoldingsDonorRecord.setDonorPublicDisplay(donorinfo.getDonorPublicDisplay());
                    oleHoldingsDonorRecord.setDonorCode(donorinfo.getDonorCode());
                    oleHoldingsDonorRecord.setDonorNote(donorinfo.getDonorNote());
                    oleHoldingsDonorRecord.setHoldingsId(holdingsRecord.getHoldingsId());
                    oleHoldingsDonorRecords.add(oleHoldingsDonorRecord);
                }
                holdingsRecord.setDonorList(oleHoldingsDonorRecords);
            }
            if (oleHoldingsDonorRecords.size() > 0) {
                getBusinessObjectService().save(oleHoldingsDonorRecords);
            }
        }
        return holdingsRecord;
    }

    private void saveCoverageRecord(Coverages coverages, String eHoldingsIdentifier) {

        if (eHoldingsIdentifier != null) {
            Map parentCriteria1 = new HashMap();
            parentCriteria1.put("holdingsId", eHoldingsIdentifier);
            getBusinessObjectService().deleteMatching(EInstanceCoverageRecord.class, parentCriteria1);
        }

        List<EInstanceCoverageRecord> coverageList = new ArrayList<>();
        for (Coverage coverage : coverages.getCoverage()) {
            if (StringUtils.isNotBlank(coverage.getCoverageStartDateFormat()) || StringUtils.isNotBlank(coverage.getCoverageStartDateString()) || StringUtils.isNotBlank(coverage.getCoverageStartVolume())
                    || StringUtils.isNotBlank(coverage.getCoverageStartIssue()) || StringUtils.isNotBlank(coverage.getCoverageEndDateFormat()) || StringUtils.isNotBlank(coverage.getCoverageEndDateString())
                    || StringUtils.isNotBlank(coverage.getCoverageEndVolume()) || StringUtils.isNotBlank(coverage.getCoverageEndIssue())) {
                EInstanceCoverageRecord eInstanceCoverageRecord = new EInstanceCoverageRecord();
                eInstanceCoverageRecord.setHoldingsId(eHoldingsIdentifier);
                if (StringUtils.isNotBlank(coverage.getCoverageStartDateFormat()) || StringUtils.isNotBlank(coverage.getCoverageStartDateString()) || StringUtils.isNotBlank(coverage.getCoverageStartVolume())
                        || StringUtils.isNotBlank(coverage.getCoverageStartIssue())) {
                    if (StringUtils.isNotEmpty(coverage.getCoverageStartDate())) {
                        eInstanceCoverageRecord.setCoverageStartDate(coverage.getCoverageStartDate());
                    }
                    eInstanceCoverageRecord.setCoverageStartVolume(coverage.getCoverageStartVolume());
                    eInstanceCoverageRecord.setCoverageStartIssue(coverage.getCoverageStartIssue());
                }
                if (StringUtils.isNotBlank(coverage.getCoverageEndDateFormat()) || StringUtils.isNotBlank(coverage.getCoverageEndDateString())
                        || StringUtils.isNotBlank(coverage.getCoverageEndVolume()) || StringUtils.isNotBlank(coverage.getCoverageEndIssue())) {

                    if (StringUtils.isNotEmpty(coverage.getCoverageEndDate())) {
                        eInstanceCoverageRecord.setCoverageEndDate(coverage.getCoverageEndDate());
                    }
                    eInstanceCoverageRecord.setCoverageEndVolume(coverage.getCoverageEndVolume());
                    eInstanceCoverageRecord.setCoverageEndIssue(coverage.getCoverageEndIssue());
                }
                coverageList.add(eInstanceCoverageRecord);
            }
        }
        getBusinessObjectService().save(coverageList);
    }

    protected StatisticalSearchRecord saveStatisticalSearchRecord(StatisticalSearchingCode statisticalSearchingCode) {
        Map map = new HashMap();
        map.put("code", statisticalSearchingCode.getCodeValue());
        List<StatisticalSearchRecord> statisticalSearchRecords = (List<StatisticalSearchRecord>) getBusinessObjectService().findMatching(StatisticalSearchRecord.class, map);
        if (statisticalSearchRecords.size() == 0) {
            if (statisticalSearchingCode.getCodeValue() != null && !"".equals(statisticalSearchingCode.getCodeValue())) {
                StatisticalSearchRecord statisticalSearchRecord = new StatisticalSearchRecord();
                statisticalSearchRecord.setCode(statisticalSearchingCode.getCodeValue());
                statisticalSearchRecord.setName(statisticalSearchingCode.getFullValue());
                try {
                    getBusinessObjectService().save(statisticalSearchRecord);
                } catch (Exception e) {
                    throw new DocstoreException("Exception while processing statistical Search :: " + statisticalSearchingCode.getCodeValue());
                }
                return statisticalSearchRecord;
            } else {
                return null;
            }
        } else {
            return statisticalSearchRecords.get(0);
        }
    }


    //Perpetual Access
    private void savePerpetualAccessRecord(PerpetualAccesses perpetualAccesses, String eHoldingsIdentifier) {

        if (eHoldingsIdentifier != null) {
            Map parentCriteria1 = new HashMap();
            parentCriteria1.put("holdingsId", eHoldingsIdentifier);
            getBusinessObjectService().deleteMatching(EInstancePerpetualAccessRecord.class, parentCriteria1);
        }

        List<EInstancePerpetualAccessRecord> perpetualAccessList = new ArrayList<>();
        for (PerpetualAccess perpetualAccess : perpetualAccesses.getPerpetualAccess()) {
            if (StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartDateFormat()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartDateString()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartVolume())
                    || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartIssue()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndDateFormat()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndDateString())
                    || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndVolume()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndIssue())) {
                EInstancePerpetualAccessRecord eInstancePerpetualAccess = new EInstancePerpetualAccessRecord();
                eInstancePerpetualAccess.setHoldingsId(eHoldingsIdentifier);
                if (StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartDateFormat()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartDateString()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartVolume())
                        || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessStartIssue())) {
                    if (StringUtils.isNotEmpty(perpetualAccess.getPerpetualAccessStartDate())) {
                        eInstancePerpetualAccess.setPerpetualAccessStartDate(perpetualAccess.getPerpetualAccessStartDate());
                    }
                    eInstancePerpetualAccess.setPerpetualAccessStartVolume(perpetualAccess.getPerpetualAccessStartVolume());
                    eInstancePerpetualAccess.setPerpetualAccessStartIssue(perpetualAccess.getPerpetualAccessStartIssue());
                }
                if (StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndDateFormat()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndDateString())
                        || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndVolume()) || StringUtils.isNotBlank(perpetualAccess.getPerpetualAccessEndIssue())) {
                    if (StringUtils.isNotEmpty(perpetualAccess.getPerpetualAccessEndDate())) {
                        eInstancePerpetualAccess.setPerpetualAccessEndDate(perpetualAccess.getPerpetualAccessEndDate());
                    }
                    eInstancePerpetualAccess.setPerpetualAccessEndVolume(perpetualAccess.getPerpetualAccessEndVolume());
                    eInstancePerpetualAccess.setPerpetualAccessEndIssue(perpetualAccess.getPerpetualAccessEndIssue());
                }
                perpetualAccessList.add(eInstancePerpetualAccess);
            }
        }
        getBusinessObjectService().save(perpetualAccessList);
    }


    @Override
    public void update(Object object) {
        Holdings holdings = (Holdings) object;
        HoldingsRecord holdingsRecord = getExistingHoldings(holdings.getId());
        HoldingsRecord oldHoldingsRecord = (HoldingsRecord)SerializationUtils.clone(holdingsRecord);
        if (holdingsRecord == null) {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.HOLDING_ID_NOT_FOUND, DocstoreResources.HOLDING_ID_NOT_FOUND);
            docstoreException.addErrorParams("holdingsId", holdings.getId());
            throw docstoreException;
        }
        setStaffOnly(holdings, holdingsRecord);
        holdingsRecord.setUpdatedBy(holdings.getUpdatedBy());
        holdingsRecord.setUpdatedDate(createdDate());
        String content = holdings.getContent();
        OleHoldings oleHoldings = workHoldingOlemlRecordProcessor.fromXML(content);
        if (oleHoldings.getCallNumber() != null) {
            CallNumber callNumber = oleHoldings.getCallNumber();
            holdingsRecord.setCallNumberPrefix(callNumber.getPrefix());
            holdingsRecord.setCallNumber(callNumber.getNumber());

            if(StringUtils.isNotEmpty(callNumber.getNumber())){
                if (callNumber.getShelvingOrder() != null) {
                    holdingsRecord.setShelvingOrder(callNumber.getShelvingOrder().getFullValue());
                }
                processCallNumber(oleHoldings);
                CallNumberTypeRecord callNumberTypeRecord = saveCallNumberTypeRecord(callNumber.getShelvingScheme());
                holdingsRecord.setCallNumberTypeId(callNumberTypeRecord == null ? null : callNumberTypeRecord.getCallNumberTypeId());
            } else if(StringUtils.isEmpty(callNumber.getNumber())){
                if (callNumber.getShelvingOrder() != null) {
                    holdingsRecord.setShelvingOrder(callNumber.getShelvingOrder().getFullValue());
                } else {
                    callNumber.setShelvingScheme(new ShelvingScheme());
                }
                callNumber.getShelvingScheme().setFullValue("");
                CallNumberTypeRecord callNumberTypeRecord = saveCallNumberTypeRecord(callNumber.getShelvingScheme());
                holdingsRecord.setCallNumberTypeId(callNumberTypeRecord == null ? null : callNumberTypeRecord.getCallNumberTypeId());
            }else{
                callNumber.setShelvingOrder(new ShelvingOrder());
                callNumber.setShelvingScheme(new ShelvingScheme());
            }
        }
        holdingsRecord.setStaffOnlyFlag(holdings.isStaffOnly());
        setHoldingsCommonInformation(oleHoldings, holdingsRecord);
        if (holdings instanceof PHoldings) {
            setPHoldingsInformation(holdingsRecord, oleHoldings);

        } else {
            setEHoldingsInformation(oleHoldings, holdingsRecord);
        }
        getBusinessObjectService().save(holdingsRecord);
        holdings.setId(DocumentUniqueIDPrefix.getPrefixedId(holdingsRecord.getUniqueIdPrefix(), holdingsRecord.getHoldingsId()));
        content = workHoldingOlemlRecordProcessor.toXML(oleHoldings);
        holdings.setContent(content);
        buildLabelForHoldings(holdingsRecord, holdings);
        if (Boolean.TRUE == isAuditRequired()) {
            try {
                oldHoldingsRecord = processHoldingsForAudit(oldHoldingsRecord);
                holdingsRecord = processHoldingsForAudit(holdingsRecord);
                List<Audit> auditList= OleAuditManager.getInstance().audit(HoldingsAudit.class, oldHoldingsRecord, holdingsRecord, holdingsRecord.getHoldingsId(), "ole");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void setStaffOnly(Holdings holdings, HoldingsRecord holdingsRecord) {
        if(!holdingsRecord.getStaffOnlyFlag().toString().equalsIgnoreCase(String.valueOf(holdings.isStaffOnly()))){
            Map parentCriteria = new HashMap();
            parentCriteria.put("bibId", holdingsRecord.getBibId());
            boolean allStaffOnly = true;
            List<HoldingsRecord> holdingsRecords = (List<HoldingsRecord>) getBusinessObjectService().findMatching(HoldingsRecord.class, parentCriteria);
            if(holdingsRecords.size() == 1){
                BibRecord bibRecord = getBusinessObjectService().findByPrimaryKey(BibRecord.class, parentCriteria);
                bibRecord.setStaffOnlyFlag(holdings.isStaffOnly());
                getBusinessObjectService().save(bibRecord);
            }else if(holdingsRecords.size() > 1){
                for(HoldingsRecord holdingsRecord1:holdingsRecords ){
                    if (holdingsRecord1.getHoldingsId().equalsIgnoreCase(DocumentUniqueIDPrefix.getDocumentId(holdings.getId()))){
                         if(!holdings.isStaffOnly()){
                             allStaffOnly=holdingsRecord1.getStaffOnlyFlag();
                         }
                    }else{
                        if(!holdingsRecord1.getStaffOnlyFlag()){
                            allStaffOnly=holdingsRecord1.getStaffOnlyFlag();
                        }
                    }

                }
                if(allStaffOnly){
                    BibRecord bibRecord = getBusinessObjectService().findByPrimaryKey(BibRecord.class, parentCriteria);
                    bibRecord.setStaffOnlyFlag(holdings.isStaffOnly());
                    getBusinessObjectService().save(bibRecord);
                }
            }
            Map parentCriteriaForItem = new HashMap();
            parentCriteriaForItem.put("holdingsId", holdingsRecord.getHoldingsId());
            holdingsRecord.setStaffOnlyFlag(holdings.isStaffOnly());
            List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, parentCriteriaForItem);
            for(ItemRecord itemRecord:itemRecords){
                itemRecord.setStaffOnlyFlag(holdings.isStaffOnly());
            }
            getBusinessObjectService().save(itemRecords);
        }
    }

    private void setPHoldingsInformation(HoldingsRecord holdingsRecord, OleHoldings oleHoldings) {

        if (oleHoldings.getReceiptStatus() != null) {
            ReceiptStatusRecord receiptStatusRecord = saveReceiptStatusRecord(oleHoldings.getReceiptStatus());
            holdingsRecord.setReceiptStatusId(receiptStatusRecord == null ? null : receiptStatusRecord.getReceiptStatusId());
            holdingsRecord.setReceiptStatusRecord(receiptStatusRecord);
        }

        savePHoldingsExtentOfOwnerShip(oleHoldings.getExtentOfOwnership(), holdingsRecord.getHoldingsId());
        holdingsRecord.setHoldingsUriRecords(saveAccessUriRecord(oleHoldings.getUri(), holdingsRecord.getHoldingsId()));
    }

    @Override
    public Object retrieve(String id) {
//        HoldingsRecord holdingsRec = getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, getHoldingsMap(id));
//        if(holdingsRec == null) {
//            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.HOLDING_ID_NOT_FOUND, DocstoreResources.HOLDING_ID_NOT_FOUND);
//            docstoreException.addErrorParams("holdingsId", id);
//            throw docstoreException;
//        }
//        Holdings holdings = buildHoldingsFromHoldingsRecord(holdingsRec);
        Holdings holdings = retrieveHoldings(id, null, null);
        return holdings;
    }

    @Override
    public void delete(String holdingsId) {
        HoldingsRecord holdingsRecord = getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, getHoldingsMap(holdingsId));
        List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords = (List<ExtentOfOwnerShipRecord>) getBusinessObjectService().findMatching(ExtentOfOwnerShipRecord.class, getHoldingsMap(holdingsId));
        if (extentOfOwnerShipRecords != null && extentOfOwnerShipRecords.size() > 0) {

            for (int i = 0; i < extentOfOwnerShipRecords.size(); i++) {
                List<ExtentNoteRecord> extentNoteRecords = extentOfOwnerShipRecords.get(i).getExtentNoteRecords();
                if (extentNoteRecords != null && extentNoteRecords.size() > 0) {
                    getBusinessObjectService().delete(extentNoteRecords);
                }
            }
            getBusinessObjectService().delete(extentOfOwnerShipRecords);
        }


        if (holdingsRecord.getHoldingsNoteRecords() != null && holdingsRecord.getHoldingsNoteRecords().size() > 0) {
            List<HoldingsNoteRecord> holdingsNoteRecords = holdingsRecord.getHoldingsNoteRecords();
            getBusinessObjectService().delete(holdingsNoteRecords);
        }

        if (holdingsRecord.getHoldingsUriRecords() != null && holdingsRecord.getHoldingsUriRecords().size() > 0) {
            List<HoldingsUriRecord> accessUriRecords = holdingsRecord.getHoldingsUriRecords();
            getBusinessObjectService().delete(accessUriRecords);
        }
        if (holdingsRecord.getDonorList() != null && holdingsRecord.getDonorList().size() > 0) {
            List<OLEHoldingsDonorRecord> oleHoldingsDonorRecordList = holdingsRecord.getDonorList();
            getBusinessObjectService().delete(oleHoldingsDonorRecordList);
        }

        if (holdingsRecord.geteInstanceCoverageRecordList() != null && holdingsRecord.geteInstanceCoverageRecordList().size() > 0) {
            List<EInstanceCoverageRecord> eInstanceCoverageRecordList = holdingsRecord.geteInstanceCoverageRecordList();
            getBusinessObjectService().delete(eInstanceCoverageRecordList);
        }
        if (holdingsRecord.geteInstancePerpetualAccessRecordList() != null && holdingsRecord.geteInstancePerpetualAccessRecordList().size() > 0) {
            List<EInstancePerpetualAccessRecord> eInstancePerpetualAccessRecords = holdingsRecord.geteInstancePerpetualAccessRecordList();
            getBusinessObjectService().delete(eInstancePerpetualAccessRecords);
        }
        holdingsRecord.setCallNumberTypeId(null);
        holdingsRecord.setReceiptStatusId(null);

        List<ItemRecord> itemRecordList = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, getHoldingsMap(holdingsId));
        if (itemRecordList != null) {
            for (ItemRecord itemRecord : itemRecordList) {

                if (itemRecord.getFormerIdentifierRecords() != null && itemRecord.getFormerIdentifierRecords().size() > 0) {
                    List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.FormerIdentifierRecord> formerIdentifierRecords = itemRecord.getFormerIdentifierRecords();
                    getBusinessObjectService().delete(formerIdentifierRecords);
                }


                if (itemRecord.getItemNoteRecords() != null && itemRecord.getItemNoteRecords().size() > 0) {
                    List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemNoteRecord> itemNoteRecords = itemRecord.getItemNoteRecords();
                    getBusinessObjectService().delete(itemNoteRecords);
                }

                if (itemRecord.getLocationsCheckinCountRecords() != null && itemRecord.getLocationsCheckinCountRecords().size() > 0) {
                    List<org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.LocationsCheckinCountRecord> locationsCheckinCountRecords = itemRecord.getLocationsCheckinCountRecords();
                    getBusinessObjectService().delete(locationsCheckinCountRecords);
                }
                if (itemRecord.getDonorList() != null && itemRecord.getDonorList().size() > 0) {
                    List<OLEItemDonorRecord> donorList = itemRecord.getDonorList();
                    getBusinessObjectService().delete(donorList);
                }
                itemRecord.setItemStatusId(null);
                itemRecord.setItemTypeId(null);
                itemRecord.setTempItemTypeId(null);
                itemRecord.setStatisticalSearchId(null);
                itemRecord.setHighDensityStorageId(null);
                Map map = new HashMap();
                map.put("itemId", itemRecord.getItemId());
                getBusinessObjectService().deleteMatching(ItemStatisticalSearchRecord.class, map);
                getBusinessObjectService().delete(itemRecord);

            }
        }

        getBusinessObjectService().deleteMatching(HoldingsAccessLocation.class, getHoldingsMap(holdingsId));
        getBusinessObjectService().deleteMatching(HoldingsStatisticalSearchRecord.class, getHoldingsMap(holdingsId));

        getBusinessObjectService().deleteMatching(HoldingsRecord.class, getHoldingsMap(holdingsId));
    }

    @Override
    public Object retrieveTree(String id) {


//        HoldingsRecord holdingsRecord = getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, getHoldingsMap(id));
//        HoldingsTree holdingsTree = buildHoldingsTree(id, holdingsRecord);
        HoldingsTree holdingsTree = retrieveHoldingsTree(id, null, null);
        Collections.sort(holdingsTree.getItems());
        return holdingsTree;
    }

    protected HoldingsTree buildHoldingsTree(String id, HoldingsRecord holdingsRecord) {

        HoldingsTree holdingsTree = new HoldingsTree();
        holdingsTree.setHoldings(buildHoldingsFromHoldingsRecord(holdingsRecord));

        List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, getHoldingsMap(id));
        RdbmsItemDocumentManager rdbmsItemDocumentManager = RdbmsItemDocumentManager.getInstance();
        List<org.kuali.ole.docstore.common.document.Item> itemList = new ArrayList<>();
        for (ItemRecord itemRecord : itemRecords) {
            holdingsTree.getItems().add(rdbmsItemDocumentManager.buildItemContent(itemRecord));
        }
        List<HoldingsItemRecord> holdingsItemRecords = (List<HoldingsItemRecord>) getBusinessObjectService().findMatching(HoldingsItemRecord.class, getHoldingsMap(id));
        for (HoldingsItemRecord holdingsItemRecord : holdingsItemRecords) {
            holdingsTree.getItems().add((Item) rdbmsItemDocumentManager.retrieve(holdingsItemRecord.getItemId()));
        }
        return holdingsTree;
    }

    @Override
    public void validate(Object object) {
        Holdings holdings = (Holdings) object;
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        validateHoldings(oleHoldings);
    }

    private HoldingsRecord getExistingHoldings(String holdingsId) {
        Map map = new HashMap();
        map.put("holdingsId", DocumentUniqueIDPrefix.getDocumentId(holdingsId));
        HoldingsRecord holdingsRecord = (HoldingsRecord) getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, map);
        return holdingsRecord;
    }

    protected String getLocation(Location location, StringBuffer locationLevel) {
        StringBuffer locationName = new StringBuffer("");
        if (location != null && location.getLocationLevel() != null) {
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
        return locationName.toString();
    }

    protected CallNumberTypeRecord saveCallNumberTypeRecord(ShelvingScheme scheme) {

        Map callMap = new HashMap();
        if (scheme.getCodeValue() != null && scheme.getCodeValue().equalsIgnoreCase("none")) {
            scheme.setCodeValue("NOINFO");
        }
        callMap.put("code", scheme.getCodeValue());
        List<CallNumberTypeRecord> callNumberTypeRecords = (List<CallNumberTypeRecord>) getBusinessObjectService().findMatching(CallNumberTypeRecord.class, callMap);
        if (callNumberTypeRecords.size() == 0) {
            if (scheme.getCodeValue() != null && !"".equals(scheme.getCodeValue())) {
                CallNumberTypeRecord callNumberTypeRecord = new CallNumberTypeRecord();
                callNumberTypeRecord.setCode(scheme.getCodeValue());
                callNumberTypeRecord.setName(scheme.getFullValue());
                try {
                    getBusinessObjectService().save(callNumberTypeRecord);
                } catch (Exception e) {
                    throw new DocstoreException("Exception while processing Call Number :: " + scheme.getCodeValue());
                }
                return callNumberTypeRecord;
            } else
                return null;
        }
        return callNumberTypeRecords.get(0);
    }

    /**
     * @param extentOfOwnershipList
     * @param holdingsId
     */
    protected void savePHoldingsExtentOfOwnerShip(List<ExtentOfOwnership> extentOfOwnershipList, String holdingsId) {

        Map map = new HashMap();
        map.put("holdingsId", holdingsId);
        List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords = (List<ExtentOfOwnerShipRecord>) getBusinessObjectService().findMatching(ExtentOfOwnerShipRecord.class, map);

        // Getting child table record  and Deleting.
        for (ExtentOfOwnerShipRecord extentOfOwnerShipRecord : extentOfOwnerShipRecords) {
            deleteExtentNoteRecords(extentOfOwnerShipRecord.getExtOfOwnerShipId());
        }
        // Delete Parent table
        getBusinessObjectService().delete(extentOfOwnerShipRecords);

        // Saving list coming from view
        if (extentOfOwnershipList != null && extentOfOwnershipList.size() > 0) {
            for (int i = 0; i < extentOfOwnershipList.size(); i++) {
                ExtentOfOwnerShipRecord extentOfOwnerShipRecord = new ExtentOfOwnerShipRecord();
                ExtentOfOwnership extentOfOwnership = extentOfOwnershipList.get(i);
                ExtentOfOwnerShipTypeRecord extentOfOwnerShipTypeRecord = saveExtentOfOwnerShipType(extentOfOwnership.getType());
                extentOfOwnerShipRecord.setExtOfOwnerShipTypeId(extentOfOwnerShipTypeRecord != null ? extentOfOwnerShipTypeRecord.getExtOfOwnerShipTypeId() : null);
                extentOfOwnerShipRecord.setText(extentOfOwnership.getTextualHoldings());
                extentOfOwnerShipRecord.setHoldingsId(holdingsId);
                extentOfOwnerShipRecord.setExtentOfOwnerShipTypeRecord(extentOfOwnerShipTypeRecord);
                extentOfOwnerShipRecord.setOrd(i + 1);
                getBusinessObjectService().save(extentOfOwnerShipRecord);
                List<Note> notes = extentOfOwnership.getNote();
                if (notes.size() > 0) {
                    extentOfOwnerShipRecord.setExtentNoteRecords(saveExtentNoteRecord(extentOfOwnerShipRecord.getExtOfOwnerShipId(), notes));
                }
            }
        }

    }

    /**
     * Delete Extent  Note Record from dataBase
     *
     * @param extentOfOwnerShipId
     */
    private void deleteExtentNoteRecords(String extentOfOwnerShipId) {
        Map extOwnerShipRecordMap = new HashMap();
        extOwnerShipRecordMap.put(ExtentNoteRecord.EXTENT_OF_OWNERSHIP_ID, extentOfOwnerShipId);
        getBusinessObjectService().delete((List<ExtentNoteRecord>) getBusinessObjectService().findMatching(ExtentNoteRecord.class, extOwnerShipRecordMap));
    }

    /**
     * Delete the existing note records and save the new record values coming from view
     *
     * @param extOfOwnerShipID
     * @param notes
     */
    protected List<ExtentNoteRecord> saveExtentNoteRecord(String extOfOwnerShipID, List<Note> notes) {
        deleteExtentNoteRecords(extOfOwnerShipID);
        List<ExtentNoteRecord> extentNoteRecords = new ArrayList<>();
        if (notes != null && notes.size() > 0) {
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                if (note.getType() != null && ("public".equalsIgnoreCase(note.getType()) || "nonPublic".equalsIgnoreCase(note.getType()))) {
                    ExtentNoteRecord noteRecord = new ExtentNoteRecord();
                    noteRecord.setExtOfOwnerShipId(extOfOwnerShipID);
                    noteRecord.setType(note.getType());
                    noteRecord.setNote(note.getValue());
                    getBusinessObjectService().save(noteRecord);
                    extentNoteRecords.add(noteRecord);
                }
            }

        }
        return extentNoteRecords;
    }

    protected ReceiptStatusRecord saveReceiptStatusRecord(String receiptStatus) {
        Map map = new HashMap();
        map.put("code", receiptStatus);
        List<ReceiptStatusRecord> receiptStatusRecords = (List<ReceiptStatusRecord>) getBusinessObjectService().findMatching(ReceiptStatusRecord.class, map);
        if (receiptStatusRecords.size() == 0) {
            map = new HashMap();
            map.put("name", receiptStatus);
            receiptStatusRecords = (List<ReceiptStatusRecord>) getBusinessObjectService().findMatching(ReceiptStatusRecord.class, map);
        }
        if (receiptStatusRecords.size() == 0) {
            if (receiptStatus != null && !"".equals(receiptStatus)) {
                ReceiptStatusRecord receiptStatusRecord = new ReceiptStatusRecord();
                receiptStatusRecord.setCode(receiptStatus);
                receiptStatusRecord.setName(receiptStatus);
                try {
                    getBusinessObjectService().save(receiptStatusRecord);
                } catch (Exception e) {
                    throw new DocstoreException("Exception while processing receipt Status :: " + receiptStatus);
                }
                return receiptStatusRecord;
            } else {
                return null;
            }
        }
        return receiptStatusRecords.get(0);
    }

    protected ExtentOfOwnerShipTypeRecord saveExtentOfOwnerShipType(String type) {
        Map map = new HashMap();
        map.put("code", type);
        List<ExtentOfOwnerShipTypeRecord> extentOfOwnerShipTypeRecords = (List<ExtentOfOwnerShipTypeRecord>) getBusinessObjectService().findMatching(ExtentOfOwnerShipTypeRecord.class, map);
        if (extentOfOwnerShipTypeRecords.size() == 0) {
            if (type != null && !"".equals(type)) {
                ExtentOfOwnerShipTypeRecord extentOfOwnerShipTypeRecord = new ExtentOfOwnerShipTypeRecord();
                extentOfOwnerShipTypeRecord.setCode(type);
                extentOfOwnerShipTypeRecord.setName(type);
                try {
                    getBusinessObjectService().save(extentOfOwnerShipTypeRecord);
                } catch (Exception e) {
                    throw new DocstoreException("Exception while processing Extent Of OwnerShip  :: " + type);
                }
                return extentOfOwnerShipTypeRecord;
            } else {
                return null;
            }
        }
        return extentOfOwnerShipTypeRecords.get(0);
    }

    protected List<HoldingsNoteRecord> saveHoldingNoteRecords(List<Note> noteList, String holdingsId) {
        Map map = new HashMap();
        map.put("holdingsId", holdingsId);
        List<HoldingsNoteRecord> holdingsNoteRecordList = (List<HoldingsNoteRecord>) getBusinessObjectService().findMatching(HoldingsNoteRecord.class, map);
        getBusinessObjectService().delete(holdingsNoteRecordList);
        holdingsNoteRecordList.clear();
        if (noteList.size() > 0) {
            for (int i = 0; i < noteList.size(); i++) {
                Note note = noteList.get(i);
                if (StringUtils.isNotBlank(note.getValue())) {
                    if (note.getType() != null && ("public".equalsIgnoreCase(note.getType()) || "nonPublic".equalsIgnoreCase(note.getType()))) {
                        HoldingsNoteRecord holdingsNoteRecord = new HoldingsNoteRecord();
                        holdingsNoteRecord.setType(note.getType());
                        holdingsNoteRecord.setNote(note.getValue());
                        holdingsNoteRecord.setHoldingsId(holdingsId);
                        holdingsNoteRecordList.add(holdingsNoteRecord);
                    }
                }
                if (holdingsNoteRecordList.size() > 0) {
                    getBusinessObjectService().save(holdingsNoteRecordList);
                }
            }
        }
        return holdingsNoteRecordList;
    }

    protected List<HoldingsUriRecord> saveAccessUriRecord(List<Uri> uriList, String holdingsId) {

        Map map = new HashMap();
        map.put("holdingsId", holdingsId);
        List<HoldingsUriRecord> holdingsUriRecordList = (List<HoldingsUriRecord>) getBusinessObjectService().findMatching(HoldingsUriRecord.class, map);
        getBusinessObjectService().delete(holdingsUriRecordList);
        holdingsUriRecordList.clear();
        if (uriList.size() > 0) {
            for (int i = 0; i < uriList.size(); i++) {
                Uri accessUri = uriList.get(i);
                if (StringUtils.isNotBlank(accessUri.getValue())) {
                    HoldingsUriRecord holdingsUriRecord = new HoldingsUriRecord();
                    Uri uri = uriList.get(i);
                    holdingsUriRecord.setText(uri.getValue());
                    holdingsUriRecord.setHoldingsId(holdingsId);
                    holdingsUriRecordList.add(holdingsUriRecord);
                }
            }

            if (holdingsUriRecordList.size() > 0) {
                getBusinessObjectService().save(holdingsUriRecordList);
            }
        }
        return holdingsUriRecordList;
    }


//    private void updatedDateForHoldings(Holdings holdings, HoldingsRecord holdingsRecord) {
//        DateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//        Timestamp updatedDate = null;
//        try {
//            updatedDate = new Timestamp(df.parse(holdings.getUpdatedOn()).getTime());
//            holdingsRecord.setUpdatedDate(updatedDate);
//
//        } catch (Exception e) {
//        }
//    }

    protected void setHoldingsCommonInformation(OleHoldings oleHoldings, HoldingsRecord holdingsRecord) {
        holdingsRecord.setGokbIdentifier(oleHoldings.getGokbIdentifier());
        StringBuffer locationLevel = new StringBuffer("");
        holdingsRecord.setLocation(getLocation(oleHoldings.getLocation(), locationLevel));
        if (holdingsRecord.getStaffOnlyFlag() != null) {
            oleHoldings.setStaffOnlyFlag(holdingsRecord.getStaffOnlyFlag());
        }
        holdingsRecord.setLocationLevel(locationLevel.toString());
        if (oleHoldings.getCallNumber() != null) {
            CallNumber callNumber = oleHoldings.getCallNumber();
            holdingsRecord.setCallNumberPrefix(callNumber.getPrefix());
            holdingsRecord.setCallNumber(callNumber.getNumber());

            if (callNumber.getShelvingOrder() != null) {
                holdingsRecord.setShelvingOrder(callNumber.getShelvingOrder().getFullValue());
            }
            if (callNumber.getShelvingScheme() != null) {
                CallNumberTypeRecord callNumberTypeRecord = saveCallNumberTypeRecord(callNumber.getShelvingScheme());
                holdingsRecord.setCallNumberTypeId(callNumberTypeRecord == null ? null : callNumberTypeRecord.getCallNumberTypeId());
                holdingsRecord.setCallNumberTypeRecord(callNumberTypeRecord);
            }

        }
//        if (oleHoldings.getReceiptStatus() != null) {
//            ReceiptStatusRecord receiptStatusRecord = saveReceiptStatusRecord(oleHoldings.getReceiptStatus());
//            holdingsRecord.setReceiptStatusId(receiptStatusRecord == null ? null : receiptStatusRecord.getReceiptStatusId());
//        }
        holdingsRecord.setContent("mock content");
        holdingsRecord.setExtentOfOwnerShipRecords(null);
        holdingsRecord.setHoldingsUriRecords(null);
        holdingsRecord.setHoldingsNoteRecords(null);
        if (oleHoldings.getCopyNumber() != null) {
            holdingsRecord.setCopyNumber(oleHoldings.getCopyNumber());
        }
        getBusinessObjectService().save(holdingsRecord);
//        savePHoldingsExtentOfOwnerShip(oleHoldings.getExtentOfOwnership(), holdingsRecord.getHoldingsId());
        holdingsRecord.setHoldingsNoteRecords(saveHoldingNoteRecords(oleHoldings.getNote(), holdingsRecord.getHoldingsId()));
//        saveAccessUriRecord(oleHoldings.getUri(), holdingsRecord.getHoldingsId());
    }

    public Holdings buildHoldingsFromHoldingsRecord(HoldingsRecord holdingsRecord) {
        OleHoldings oleHoldings = new OleHoldings();
        oleHoldings.setBibIdentifier(DocumentUniqueIDPrefix.PREFIX_WORK_BIB_MARC + "-" + holdingsRecord.getBibId());
        oleHoldings.setHoldingsIdentifier(DocumentUniqueIDPrefix.getPrefixedId(holdingsRecord.getUniqueIdPrefix(), holdingsRecord.getHoldingsId()));
        Holdings holdings = null;

        if ("print".equals(holdingsRecord.getHoldingsType())) {
            holdings = new PHoldingsOleml();
            retrievePrintHoldings(holdingsRecord, oleHoldings);
            retrievePHoldingsExtentOfownership(holdingsRecord.getHoldingsId(), oleHoldings);

        } else {
            holdings = new EHoldingsOleml();
            retrieveElectronicHoldings(holdingsRecord, oleHoldings);

        }
        if (holdingsRecord.getLocation() != null) {
            Location location = getLocationDetails(holdingsRecord.getLocation(), holdingsRecord.getLocationLevel());
            oleHoldings.setLocation(location);
        }
        CallNumber callNumber = new CallNumber();
        callNumber.setPrefix(holdingsRecord.getCallNumberPrefix());
        if (holdingsRecord.getCallNumberTypeRecord() != null) {
            callNumber.setNumber(holdingsRecord.getCallNumber());
            ShelvingScheme shelvingScheme = new ShelvingScheme();
            shelvingScheme.setCodeValue(holdingsRecord.getCallNumberTypeRecord().getCode());
            shelvingScheme.setFullValue(holdingsRecord.getCallNumberTypeRecord().getName());
            callNumber.setShelvingScheme(shelvingScheme);
            ShelvingOrder shelvingOrder = new ShelvingOrder();
            shelvingOrder.setCodeValue(holdingsRecord.getShelvingOrder());
            shelvingOrder.setFullValue(holdingsRecord.getShelvingOrder());
            callNumber.setShelvingOrder(shelvingOrder);
        }
        oleHoldings.setCallNumber(callNumber);
        if (holdingsRecord.getGokbIdentifier() != null) {
            oleHoldings.setGokbIdentifier(holdingsRecord.getGokbIdentifier());
        }

        if (holdingsRecord.getHoldingsNoteRecords() != null && holdingsRecord.getHoldingsNoteRecords().size() > 0) {
            List<HoldingsNoteRecord> holdingsNoteRecords = holdingsRecord.getHoldingsNoteRecords();
            if (holdingsNoteRecords != null && holdingsNoteRecords.size() > 0) {
                List<Note> notes = new ArrayList<Note>();
                for (HoldingsNoteRecord holdingsNoteRecord : holdingsNoteRecords) {
                    Note note = new Note();
                    note.setType(holdingsNoteRecord.getType());
                    note.setValue(holdingsNoteRecord.getNote());
                    notes.add(note);
                }
                oleHoldings.setNote(notes);
            }
        }
        holdings.setCreatedBy(holdingsRecord.getCreatedBy());
        holdings.setUpdatedBy(holdingsRecord.getUpdatedBy());
        if (holdingsRecord.getCreatedDate() != null) {
            holdings.setCreatedOn(holdingsRecord.getCreatedDate().toString());
        }
        if (holdingsRecord.getUpdatedDate() != null) {
            holdings.setUpdatedOn(holdingsRecord.getUpdatedDate().toString());
        }

        if (holdingsRecord.getStaffOnlyFlag() != null) {
            holdings.setStaffOnly(holdingsRecord.getStaffOnlyFlag());
            oleHoldings.setStaffOnlyFlag(holdingsRecord.getStaffOnlyFlag());
        }

        String content = workHoldingOlemlRecordProcessor.toXML(oleHoldings);
        holdings.setContent(content);
        holdings.setId(DocumentUniqueIDPrefix.getPrefixedId(holdingsRecord.getUniqueIdPrefix(), holdingsRecord.getHoldingsId()));
//        DocumentManager documentManager = RdbmsBibDocumentManager.getInstance();
//        holdings.setBib((Bib) documentManager.retrieve(holdingsRecord.getBibId()));
//        List<BibHoldingsRecord> bibHoldingsRecords = (List<BibHoldingsRecord>) getBusinessObjectService().findMatching(BibHoldingsRecord.class, getHoldingsMap(holdingsRecord.getHoldingsId()));
//        if(bibHoldingsRecords != null && bibHoldingsRecords.size() > 0) {
//            Bibs bibs = new Bibs();
//            for(BibHoldingsRecord bibHoldingsRecord : bibHoldingsRecords) {
//                bibs.getBibs().add((Bib) documentManager.retrieve(bibHoldingsRecord.getBibId()));
//            }
//            holdings.setBoundWithBib(true);
//            holdings.setBibs(bibs);
//        }
        buildLabelForHoldings(holdingsRecord, holdings);
//        List<HoldingsItemRecord> holdingsItemRecords = (List<HoldingsItemRecord>) getBusinessObjectService().findMatching(HoldingsItemRecord.class, getHoldingsMap(holdingsRecord.getHoldingsId()));
//        if (!CollectionUtils.isEmpty(holdingsItemRecords)) {
//            holdings.setSeries(true);
//        }

        holdings.setLocationName(holdingsRecord.getLocation());

        return holdings;
    }

    private void buildLabelForHoldings(HoldingsRecord holdingsRecord, Holdings holdings) {
        StringBuilder sortedValue = new StringBuilder();
        if (StringUtils.isNotEmpty(holdingsRecord.getLocation())) {
            addDataToLabel(sortedValue, holdingsRecord.getLocation());
        }
//        if (StringUtils.isNotEmpty(holdingsRecord.getCallNumberPrefix())) {
//            sortedValue.append(holdingsRecord.getCallNumberPrefix());
//        }
        if (StringUtils.isNotEmpty(holdingsRecord.getShelvingOrder())) {
            addDataToLabel(sortedValue, holdingsRecord.getShelvingOrder());
        }
        holdings.setSortedValue(sortedValue.toString());

        StringBuilder labelName = new StringBuilder();
        if (StringUtils.isNotEmpty(holdingsRecord.getLocation())) {
            addDataToLabel(labelName, holdingsRecord.getLocation());
        }
        if (StringUtils.isNotEmpty(holdingsRecord.getCallNumberPrefix())) {
            addDataToLabel(labelName, holdingsRecord.getCallNumberPrefix());
        }
        if (StringUtils.isNotEmpty(holdingsRecord.getCallNumber())) {
            addDataToLabel(labelName, holdingsRecord.getCallNumber());
        }
        if (StringUtils.isNotEmpty(holdingsRecord.getCopyNumber())) {
            addDataToLabel(labelName, holdingsRecord.getCopyNumber());
        }
        if (labelName.length() == 0) {
            labelName.append("Holdings");
        }
        //holdings.setDisplayLabel(labelName.toString());
        holdings.setDisplayLabel(encodeString(labelName.toString()));
    }

    public String encodeString(String label) {
        StringBuilder result = new StringBuilder();
        StringCharacterIterator iterator = new StringCharacterIterator(label);
        char character = iterator.current();
        while (character != CharacterIterator.DONE) {
            if (character == '<') {
                result.append("&lt;");
            } else if (character == '>') {
                result.append("&gt;");
            } else if (character == '&') {
                result.append("&amp;");
            } else if (character == '\"') {
                result.append("&quot;");
            } else if (character == '\t') {
                addCharEntity(9, result);
            } else if (character == '!') {
                addCharEntity(33, result);
            } else if (character == '#') {
                addCharEntity(35, result);
            } else if (character == '$') {
                addCharEntity(36, result);
            } else if (character == '%') {
                addCharEntity(37, result);
            } else if (character == '\'') {
                addCharEntity(39, result);
            } else if (character == '(') {
                addCharEntity(40, result);
            } else if (character == ')') {
                addCharEntity(41, result);
            } else if (character == '*') {
                addCharEntity(42, result);
            } else if (character == '+') {
                addCharEntity(43, result);
            } else if (character == ',') {
                addCharEntity(44, result);
            } else if (character == '-') {
                addCharEntity(45, result);
            } else if (character == '.') {
                addCharEntity(46, result);
            } else if (character == '/') {
                addCharEntity(47, result);
            } else if (character == ':') {
                addCharEntity(58, result);
            } else if (character == ';') {
                addCharEntity(59, result);
            } else if (character == '=') {
                addCharEntity(61, result);
            } else if (character == '?') {
                addCharEntity(63, result);
            } else if (character == '@') {
                addCharEntity(64, result);
            } else if (character == '[') {
                addCharEntity(91, result);
            } else if (character == '\\') {
                addCharEntity(92, result);
            } else if (character == ']') {
                addCharEntity(93, result);
            } else if (character == '^') {
                addCharEntity(94, result);
            } else if (character == '_') {
                addCharEntity(95, result);
            } else if (character == '`') {
                addCharEntity(96, result);
            } else if (character == '{') {
                addCharEntity(123, result);
            } else if (character == '|') {
                addCharEntity(124, result);
            } else if (character == '}') {
                addCharEntity(125, result);
            } else if (character == '~') {
                addCharEntity(126, result);
            } else {
                //the char is not a special one
                //add it to the result as is
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }

    private void addCharEntity(Integer escapeId, StringBuilder labelBuilder) {
        String padding = "";
        if (escapeId <= 9) {
            padding = "00";
        } else if (escapeId <= 99) {
            padding = "0";
        } else {
            //no prefix
        }
        String number = padding + escapeId.toString();
        labelBuilder.append("&#" + number + ";");
    }

    private void retrieveElectronicHoldings(HoldingsRecord holdingsRecord, OleHoldings oleHoldings) {
        HoldingsAccessInformation accessInformation = new HoldingsAccessInformation();
        accessInformation.setAccessPassword(holdingsRecord.getAccessPassword());
        accessInformation.setAccessUsername(holdingsRecord.getAccessUserName());
        accessInformation.setMaterialsSpecified(holdingsRecord.getMaterialsSpecified());
        accessInformation.setFirstIndicator(holdingsRecord.getFirstIndicator());
        accessInformation.setSecondIndicator(holdingsRecord.getSecondIndicator());

        if (holdingsRecord.getHoldingsAccessLocations() != null && holdingsRecord.getHoldingsAccessLocations().size() > 0) {
            accessInformation.setAccessLocation(holdingsRecord.getHoldingsAccessLocations().get(0).getAccessLocation().getCode());
        }
        if (holdingsRecord.getAuthenticationType() != null) {
            accessInformation.setAuthenticationType(holdingsRecord.getAuthenticationType().getName());
        }
        accessInformation.setNumberOfSimultaneousUser(holdingsRecord.getNumberSimultaneousUsers());
        accessInformation.setProxiedResource(holdingsRecord.getProxiedResource());
        oleHoldings.setHoldingsAccessInformation(accessInformation);

        List<Link> links = new ArrayList<>();
        if (holdingsRecord.getHoldingsUriRecords() != null && holdingsRecord.getHoldingsUriRecords().size() > 0) {
            for (HoldingsUriRecord holdingsUriRecord : holdingsRecord.getHoldingsUriRecords()) {
                Link link = new Link();
                link.setUrl(holdingsUriRecord.getUri());
                link.setText(holdingsUriRecord.getText());
                links.add(link);
            }
        }
        oleHoldings.setLink(links);

        if (holdingsRecord.getStatusDate() != null) {
            String statusDate = new SimpleDateFormat("dd-MM-yyyy").format(holdingsRecord.getStatusDate());
            oleHoldings.setStatusDate(statusDate);
        }

        if (holdingsRecord.getHoldingsStatisticalSearchRecords() != null && holdingsRecord.getHoldingsStatisticalSearchRecords().size() > 0) {
            StatisticalSearchingCode statisticalSearchingCode = new StatisticalSearchingCode();
            if(holdingsRecord.getHoldingsStatisticalSearchRecords().get(0).getStatisticalSearchRecord() != null) {
                statisticalSearchingCode.setCodeValue(holdingsRecord.getHoldingsStatisticalSearchRecords().get(0).getStatisticalSearchRecord().getCode());
            } else {
                if (StringUtils.isNotBlank(holdingsRecord.getHoldingsStatisticalSearchRecords().get(0).getStatisticalSearchId())) {
                    Map<String, String> statisticalIdMap = new HashMap<>();
                    statisticalIdMap.put("statisticalSearchId", holdingsRecord.getHoldingsStatisticalSearchRecords().get(0).getStatisticalSearchId());
                    StatisticalSearchRecord statisticalSearchRecord = getBusinessObjectService().findByPrimaryKey(StatisticalSearchRecord.class, statisticalIdMap);
                    if (statisticalSearchRecord != null) {
                        statisticalSearchingCode.setCodeValue(statisticalSearchRecord.getCode());
                    }
                }
            }
            oleHoldings.setStatisticalSearchingCode(statisticalSearchingCode);

        }
        oleHoldings.setSubscriptionStatus(holdingsRecord.getSubscriptionStatus());
        oleHoldings.setPublisher(holdingsRecord.getPublisherId());

        Platform platform = new Platform();
        platform.setAdminPassword(holdingsRecord.getAdminPassword());
        platform.setAdminUrl(holdingsRecord.getAdminUrl());
        platform.setAdminUserName(holdingsRecord.getAdminUserName());
        platform.setPlatformName(holdingsRecord.getPlatform());
        oleHoldings.setPlatform(platform);

        oleHoldings.setImprint(holdingsRecord.getImprint());
        oleHoldings.setInterLibraryLoanAllowed(holdingsRecord.getAllowIll());
        oleHoldings.setLocalPersistentLink(holdingsRecord.getLocalPersistentUri());
        oleHoldings.setEResourceId(holdingsRecord.geteResourceId());
        oleHoldings.setAccessStatus(holdingsRecord.getAccessStatus());

        ExtentOfOwnership extentOfOwnership = new ExtentOfOwnership();
        String coverageStartDate = "";
        String coverageEndDate = "";
        String perAccStartDate = "";
        String perAccEndDate = "";
        if (holdingsRecord.geteInstanceCoverageRecordList() != null && holdingsRecord.geteInstanceCoverageRecordList().size() > 0) {
            Coverages coverages = new Coverages();
            for (EInstanceCoverageRecord holdingCoverage : holdingsRecord.geteInstanceCoverageRecordList()) {
                Coverage coverage = new Coverage();
                if (holdingCoverage != null) {
                    if (holdingCoverage.getCoverageStartDate() != null) {
                        coverageStartDate = holdingCoverage.getCoverageStartDate();
                        if (coverageStartDate.contains("/")) {
                            coverage.setCoverageStartDateFormat(coverageStartDate);
                            coverage.setCoverageStartDateString("");
                        } else {
                            coverage.setCoverageStartDateString(coverageStartDate);
                            coverage.setCoverageStartDateFormat("");
                        }
                        coverage.setCoverageStartDate(holdingCoverage.getCoverageStartDate());
                    }
                    if (holdingCoverage.getCoverageEndDate() != null) {
                        coverageEndDate = holdingCoverage.getCoverageEndDate();
                        if (coverageEndDate.contains("/")) {
                            coverage.setCoverageEndDateFormat(coverageEndDate);
                            coverage.setCoverageEndDateString("");
                        } else {
                            coverage.setCoverageEndDateString(coverageEndDate);
                            coverage.setCoverageEndDateFormat("");
                        }
                        coverage.setCoverageEndDate(holdingCoverage.getCoverageEndDate());
                    }
                    coverage.setCoverageStartVolume(holdingCoverage.getCoverageStartVolume());
                    coverage.setCoverageStartIssue(holdingCoverage.getCoverageStartIssue());
                    coverage.setCoverageEndVolume(holdingCoverage.getCoverageEndVolume());
                    coverage.setCoverageEndIssue(holdingCoverage.getCoverageEndIssue());
                    coverages.getCoverage().add(coverage);
                }
            }
            extentOfOwnership.setCoverages(coverages);
        }
        if (holdingsRecord.geteInstancePerpetualAccessRecordList() != null && holdingsRecord.geteInstancePerpetualAccessRecordList().size() > 0) {
            PerpetualAccesses perpetualAccesses = new PerpetualAccesses();
            for (EInstancePerpetualAccessRecord holdingPerpetualAccess : holdingsRecord.geteInstancePerpetualAccessRecordList()) {
                PerpetualAccess perpetualAccess = new PerpetualAccess();
                if (holdingPerpetualAccess != null) {
                    if (holdingPerpetualAccess.getPerpetualAccessStartDate() != null) {
                        perAccStartDate = holdingPerpetualAccess.getPerpetualAccessStartDate();
                        if (perAccStartDate.contains("/")) {
                            perpetualAccess.setPerpetualAccessStartDateFormat(perAccStartDate);
                            perpetualAccess.setPerpetualAccessStartDateString("");
                        } else {
                            perpetualAccess.setPerpetualAccessStartDateString(perAccStartDate);
                            perpetualAccess.setPerpetualAccessStartDateFormat("");
                        }
                        perpetualAccess.setPerpetualAccessStartDate(perAccStartDate);
                    }
                    if (holdingPerpetualAccess.getPerpetualAccessEndDate() != null) {
                        perAccEndDate = holdingPerpetualAccess.getPerpetualAccessEndDate();
                        if (perAccEndDate.contains("/")) {
                            perpetualAccess.setPerpetualAccessEndDateFormat(perAccEndDate);
                            perpetualAccess.setPerpetualAccessEndDateString("");
                        } else {
                            perpetualAccess.setPerpetualAccessEndDateString(perAccEndDate);
                            perpetualAccess.setPerpetualAccessEndDateFormat("");
                        }
                        perpetualAccess.setPerpetualAccessEndDate(holdingPerpetualAccess.getPerpetualAccessEndDate());
                    }
                    perpetualAccess.setPerpetualAccessStartVolume(holdingPerpetualAccess.getPerpetualAccessStartVolume());
                    perpetualAccess.setPerpetualAccessStartIssue(holdingPerpetualAccess.getPerpetualAccessStartIssue());
                    perpetualAccess.setPerpetualAccessEndVolume(holdingPerpetualAccess.getPerpetualAccessEndVolume());
                    perpetualAccess.setPerpetualAccessEndIssue(holdingPerpetualAccess.getPerpetualAccessEndIssue());
                    perpetualAccesses.getPerpetualAccess().add(perpetualAccess);
                }
            }
            extentOfOwnership.setPerpetualAccesses(perpetualAccesses);
        }
        oleHoldings.getExtentOfOwnership().add(extentOfOwnership);

        List<DonorInfo> donorInfoList = new ArrayList<DonorInfo>();
        if (holdingsRecord.getDonorList() != null) {
            List<OLEHoldingsDonorRecord> oleHoldingsDonorRecordList = holdingsRecord.getDonorList();
            for (OLEHoldingsDonorRecord oleHoldingsDonorRecord : oleHoldingsDonorRecordList) {
                DonorInfo donorInfo = new DonorInfo();
                donorInfo.setDonorCode(oleHoldingsDonorRecord.getDonorCode());
                donorInfo.setDonorPublicDisplay(oleHoldingsDonorRecord.getDonorPublicDisplay());
                donorInfo.setDonorNote(oleHoldingsDonorRecord.getDonorNote());
                donorInfoList.add(donorInfo);
            }
            oleHoldings.setDonorInfo(donorInfoList);
        }

        oleHoldings.setCancellationCandidate(holdingsRecord.isCancellationCandidate());
        if (holdingsRecord.getInitialSubscriptionStartDate() != null)
            oleHoldings.setInitialSubscriptionStartDate(new SimpleDateFormat("MM/dd/yyyy").format(holdingsRecord.getInitialSubscriptionStartDate()));
        if (holdingsRecord.getCurrentSubscriptionEndDate() != null)
            oleHoldings.setCurrentSubscriptionEndDate(new SimpleDateFormat("MM/dd/yyyy").format(holdingsRecord.getCurrentSubscriptionEndDate()));
        if (holdingsRecord.getCurrentSubscriptionStartDate() != null)
            oleHoldings.setCurrentSubscriptionStartDate(new SimpleDateFormat("MM/dd/yyyy").format(holdingsRecord.getCurrentSubscriptionStartDate()));
        if (holdingsRecord.getCancellationDecisionDate() != null)
            oleHoldings.setCancellationDecisionDate(new SimpleDateFormat("MM/dd/yyyy").format(holdingsRecord.getCancellationDecisionDate()));
        if (holdingsRecord.getCancellationEffectiveDate() != null)
            oleHoldings.setCancellationEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").format(holdingsRecord.getCancellationEffectiveDate()));
        if (holdingsRecord.getCancellationReason() != null)
            oleHoldings.setCancellationReason(holdingsRecord.getCancellationReason());
    }

    private void retrievePrintHoldings(HoldingsRecord holdingsRecord, OleHoldings oleHoldings) {
        if (holdingsRecord.getReceiptStatusRecord() != null) {
            ReceiptStatusRecord receiptStatusRecord = holdingsRecord.getReceiptStatusRecord();
            if (receiptStatusRecord != null) {
                oleHoldings.setReceiptStatus(receiptStatusRecord.getCode());
            }
        }
        if (holdingsRecord.getHoldingsUriRecords() != null && holdingsRecord.getHoldingsUriRecords().size() > 0) {
            List<Uri> uris = new ArrayList<Uri>();
            List<HoldingsUriRecord> holdingsUriRecords = holdingsRecord.getHoldingsUriRecords();
            for (HoldingsUriRecord holdingsUriRecord : holdingsUriRecords) {
                Uri uri = new Uri();
                uri.setValue(holdingsUriRecord.getText());
                uris.add(uri);
            }
            oleHoldings.setUri(uris);
        }
        if (holdingsRecord.getCopyNumber() != null) {
            oleHoldings.setCopyNumber(holdingsRecord.getCopyNumber());

        }

    }

    private void retrievePHoldingsExtentOfownership(String holdingsId, OleHoldings oleHoldings) {
        List<ExtentOfOwnerShipRecord> extentOfOwnerShipRecords = (List<ExtentOfOwnerShipRecord>) getBusinessObjectService().findMatchingOrderBy(ExtentOfOwnerShipRecord.class, getHoldingsMap(holdingsId), "ORD", true);
        if (extentOfOwnerShipRecords != null && extentOfOwnerShipRecords.size() > 0) {
            List<ExtentOfOwnership> extentOfOwnerships = new ArrayList<ExtentOfOwnership>();
            for (ExtentOfOwnerShipRecord extentOfOwnerShipRecord : extentOfOwnerShipRecords) {
                ExtentOfOwnership extentOfOwnership = new ExtentOfOwnership();
                if (extentOfOwnerShipRecord.getExtentOfOwnerShipTypeRecord() != null) {
                    extentOfOwnership.setTextualHoldings(extentOfOwnerShipRecord.getText());
                    extentOfOwnership.setType(extentOfOwnerShipRecord.getExtentOfOwnerShipTypeRecord().getCode());
                }

                if (extentOfOwnerShipRecord.getExtentNoteRecords() != null && extentOfOwnerShipRecord.getExtentNoteRecords().size() > 0) {
                    for (ExtentNoteRecord extentNoteRecord : extentOfOwnerShipRecord.getExtentNoteRecords()) {
                        Note note = new Note();
                        note.setType(extentNoteRecord.getType());
                        note.setValue(extentNoteRecord.getNote());
                        extentOfOwnership.getNote().add(note);
                    }
                } else {
                    Note note = new Note();
                    extentOfOwnership.getNote().add(note);
                }

                extentOfOwnerships.add(extentOfOwnership);
            }
            oleHoldings.setExtentOfOwnership(extentOfOwnerships);
        }
    }

    protected void processCallNumber(OleHoldings oleHolding) {
        if (oleHolding != null && oleHolding.getCallNumber() != null) {
            //validateCallNumber(oleHolding.getCallNumber());
            CallNumber cNum = oleHolding.getCallNumber();
            computeCallNumberType(cNum);
            if (cNum.getNumber() != null && cNum.getNumber().trim().length() > 0) {
                //Build sortable key if a valid call number exists
//                boolean isValid = validateCallNumber(cNum.getNumber(), cNum.getShelvingScheme().getCodeValue());
                String value = "";
//                if (isValid) {
//                    value = buildSortableCallNumber(cNum.getNumber(), cNum.getShelvingScheme().getCodeValue());
//                } else {
//                    value = cNum.getNumber();
//                }
                value = buildSortableCallNumber(cNum.getNumber(), cNum.getShelvingScheme().getCodeValue());
                if (cNum.getShelvingOrder() == null) {
                    cNum.setShelvingOrder(new ShelvingOrder());
                }
                cNum.getShelvingOrder().setFullValue(value);
            }
        }
    }

    public void computeCallNumberType(CallNumber callNumber) {
        Set<String> validCallNumberTypeSet = CallNumberType.validCallNumberTypeCodeSet;
        if (callNumber != null) {
            if (callNumber.getShelvingScheme() != null) {
                String callNumberTypeCode = callNumber.getShelvingScheme().getCodeValue();
                String callNumberTypeName = "";
                //If call number type code is valid
                if ((StringUtils.isNotEmpty(callNumberTypeCode)) && (validCallNumberTypeSet
                        .contains(callNumberTypeCode))) {
                    callNumberTypeName = CallNumberType.valueOf(callNumberTypeCode).getDescription();
                    callNumber.getShelvingScheme().setFullValue(callNumberTypeName);
                }
            }
        }
    }

    protected boolean validateCallNumber(String callNumber, String codeValue) {
        boolean isValid = false;
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.solrmarc.callnum.CallNumber callNumberObj = CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                callNumberObj.parse(callNumber);
                isValid = callNumberObj.isValid();
            }
        }
        return isValid;
    }

    protected String buildSortableCallNumber(String callNumber, String codeValue) {
        String shelvingOrder = "";
        if (StringUtils.isNotEmpty(callNumber) && StringUtils.isNotEmpty(codeValue)) {
            org.solrmarc.callnum.CallNumber callNumberObj= CallNumberFactory.getInstance().getCallNumber(codeValue);
            if (callNumberObj != null) {
                callNumberObj.parse(callNumber);
                shelvingOrder = callNumberObj.getShelfKey();
            }
        }
        return shelvingOrder;
    }

    public LocationLevel createLocationLevel(String locationName, String locationLevelName) {
        LocationLevel locationLevel = null;
        if (StringUtils.isNotEmpty(locationName) && StringUtils.isNotEmpty(locationLevelName)) {
            String[] locations = locationName.split("/");
            String[] locationLevels = locationLevelName.split("/");
            String locName = "";
            String levelName = "";
            if (locations.length > 0) {
                locName = locations[0];
                levelName = locationLevels[0];
                if (locationName.contains("/")) {
                    locationName = locationName.replaceFirst(locations[0] + "/", "");
                } else {
                    locationName = locationName.replace(locations[0], "");
                }

                if (locationLevelName.contains("/")) {
                    locationLevelName = locationLevelName.replaceFirst(locationLevels[0] + "/", "");
                } else {
                    locationLevelName = locationLevelName.replace(locationLevels[0], "");
                }
                if (locName != null && locations.length != 0) {
                    locationLevel = new LocationLevel();
                    locationLevel.setLevel(levelName);
                    locationLevel.setName(locName);
                    locationLevel.setLocationLevel(createLocationLevel(locationName, locationLevelName));
                }
            }
        }
        return locationLevel;
    }

    private Location getLocationDetails(String locationName, String locationLevelName) {
        Location location = new Location();
        LocationLevel locationLevel = createLocationLevel(locationName, locationLevelName);
        location.setLocationLevel(locationLevel);
        return location;
    }


    public void boundHoldingsWithBibs(String holdingsId, List<String> bibIds) {
        HoldingsRecord holdingsRecord = getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, getHoldingsMap(holdingsId));
        try {
            if (holdingsRecord != null) {
                BibHoldingsRecord bibHoldingsRecord = new BibHoldingsRecord();
                bibHoldingsRecord.setBibId(holdingsRecord.getBibId());
                bibHoldingsRecord.setHoldingsId(holdingsRecord.getHoldingsId());
                getBusinessObjectService().save(bibHoldingsRecord);
            }
            for (String bibId : bibIds) {
                BibHoldingsRecord bibHoldingsRecord = new BibHoldingsRecord();
                bibHoldingsRecord.setHoldingsId(DocumentUniqueIDPrefix.getDocumentId(holdingsId));
                bibHoldingsRecord.setBibId(DocumentUniqueIDPrefix.getDocumentId(bibId));
                getBusinessObjectService().save(bibHoldingsRecord);
            }
        } catch (Exception e) {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.BOUNDWITH_FAILED, e.getMessage());

            throw docstoreException;
        }
    }

    public void createAnalyticsRelation(String seriesHoldingsId, List<String> ItemIds) {
        HoldingsRecord holdingsRecord = getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, getHoldingsMap(seriesHoldingsId));
        try {
            for (String itemId : ItemIds) {
                HoldingsItemRecord itemHoldingsRecord = new HoldingsItemRecord();
                if (holdingsRecord != null) {
                    itemHoldingsRecord.setHoldingsId(holdingsRecord.getHoldingsId());
                    itemHoldingsRecord.setItemId(DocumentUniqueIDPrefix.getDocumentId(itemId));
                    getBusinessObjectService().save(itemHoldingsRecord);
                }
            }
        } catch (Exception e) {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.ANALYTICS_FAILED, e.getMessage());

            throw docstoreException;
        }
    }

    public void breakAnalyticsRelation(String seriesHoldingsId, List<String> itemIds) {
        List<HoldingsItemRecord> holdingsItemRecords = (List<HoldingsItemRecord>) getBusinessObjectService().findMatching(HoldingsItemRecord.class, getHoldingsMap(seriesHoldingsId));
        try {
            for (HoldingsItemRecord holdingsItemRecord : holdingsItemRecords) {
                if (itemIds.contains(DocumentUniqueIDPrefix.PREFIX_WORK_ITEM_OLEML + "-" + holdingsItemRecord.getItemId())) {
                    getBusinessObjectService().delete(holdingsItemRecord);
                }
            }
        } catch (Exception e) {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.ANALYTICS_FAILED, e.getMessage());
            throw docstoreException;
        }
    }

    public void transferInstances(List<String> holdingsIds, String bibId) {
        Collection<InstanceRecord> instanceRecords = null;
        String desBibIdentifier = bibId;
        Map holdingsMap = new HashMap();
        Map bibHoldingsMap = new HashMap();
        for (String holdingsId : holdingsIds) {
            holdingsMap.put("holdingsId", DocumentUniqueIDPrefix.getDocumentId(holdingsId));
            bibHoldingsMap.put("holdingsId", DocumentUniqueIDPrefix.getDocumentId(holdingsId));
            List<BibHoldingsRecord> bibHoldingsRecords = (List<BibHoldingsRecord>) getBusinessObjectService()
                    .findMatching(BibHoldingsRecord.class, bibHoldingsMap);
            if (bibHoldingsRecords.size() > 1) {
                //Instances are associated with multiple bibs means it has bound with with other bib. So we cant transfer. So throw exception
                DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.HOLDINGS_BIND_WITH_MULTIPLE_BIB, DocstoreResources.HOLDINGS_BIND_WITH_MULTIPLE_BIB);
                docstoreException.addErrorParams("holdingsId", holdingsId);
                throw docstoreException;
            }
            //else {
            //    BibInstanceRecord bibInstanceRecord = bibInstanceRecordList.get(0);
            //    bibInstanceRecord.setBibId(DocumentUniqueIDPrefix.getDocumentId(desBibIdentifier));
            //    businessObjectService.save(bibInstanceRecord);
            //}
            HoldingsRecord holdingsRecord = getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, holdingsMap);
            holdingsRecord.setBibId(DocumentUniqueIDPrefix.getDocumentId(desBibIdentifier));
            getBusinessObjectService().save(holdingsRecord);
        }
    }

    public void validateHoldings(OleHoldings oleHoldings) {
        if (oleHoldings.getCallNumber() != null) {
            CallNumber callNumber = oleHoldings.getCallNumber();
            validateCallNumber(callNumber);
        }
    }

    protected void validateCallNumber(CallNumber cNum) {
        validateCNumNCNumType(cNum);
        validateShelvingOrderNCNum(cNum);
    }

    private void validateShelvingOrderNCNum(CallNumber cNum) {
        if (cNum.getShelvingOrder() != null && cNum.getShelvingOrder().getFullValue() != null &&
                cNum.getShelvingOrder().getFullValue().trim().length() > 0) {
            if (!(cNum.getNumber() != null && cNum.getNumber().length() > 0)) {
                throw new DocstoreValidationException(DocstoreResources.CALL_NUMBER_INFO, DocstoreResources.CALL_NUMBER_INFO);
            }
        }
    }

    private void validateCNumNCNumType(CallNumber cNum) {
        String callNumber = "";
        String callNumberType = "";
        // Get callNumber and callNumberType
        if (cNum != null) {
            callNumber = cNum.getNumber();
            if (cNum.getShelvingScheme() != null) {
                callNumberType = cNum.getShelvingScheme().getCodeValue();
            }
            if (cNum.getShelvingOrder()!=null){
                cNum.getShelvingOrder().setFullValue("");
            }
        }
        // Check if CallNumber is present
        if (StringUtils.isNotBlank(callNumber)) {
            // Check if callNumberType is empty or #
            if ((callNumberType == null) || (callNumberType.length() == 0) || callNumber.equalsIgnoreCase("none")) {
                cNum.getShelvingScheme().setCodeValue("NOINFO");
            }
        }
    }

    protected Holdings retrieveHoldings(String id, Bib bib, HoldingsRecord holdingsRecord) {
        if (holdingsRecord == null) {
            holdingsRecord = getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, getHoldingsMap(id));
            if (holdingsRecord == null) {
                DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.HOLDING_ID_NOT_FOUND, DocstoreResources.HOLDING_ID_NOT_FOUND);
                docstoreException.addErrorParams("holdingsId", id);
                throw docstoreException;
            }
        }

        Holdings holdings = buildHoldingsFromHoldingsRecord(holdingsRecord);

        if (bib == null) {
            bib = RdbmsBibDocumentManager.getInstance().retrieveBib(holdingsRecord.getBibId());
        }

        holdings.setBib(bib);

        Bibs bibs = retrieveBibRecordsFromBoundwith(id);
        if (bibs != null && bibs.getBibs() != null && bibs.getBibs().size() > 0) {
            holdings.setBoundWithBib(true);
            holdings.setBibs(bibs);
        }

        List<HoldingsItemRecord> holdingsItemRecords = (List<HoldingsItemRecord>) getBusinessObjectService().findMatching(HoldingsItemRecord.class, getHoldingsMap(id));

        if (holdingsItemRecords != null && holdingsItemRecords.size() > 0) {
            holdings.setSeries(true);
        }

        return holdings;
    }


    protected Bibs retrieveBibRecordsFromBoundwith(String id) {
        Bibs bibs = new Bibs();
        RdbmsBibDocumentManager rdbmsBibDocumentManager = RdbmsBibDocumentManager.getInstance();
        List<BibHoldingsRecord> bibHoldingsRecords = (List<BibHoldingsRecord>) getBusinessObjectService().findMatching(BibHoldingsRecord.class, getHoldingsMap(id));
        for (BibHoldingsRecord bibHoldingsRecord : bibHoldingsRecords) {
            bibs.getBibs().add(rdbmsBibDocumentManager.retrieveBib(bibHoldingsRecord.getBibId()));
        }
        return bibs;
    }


    protected HoldingsTree retrieveHoldingsTree(String id, Bib bib, HoldingsRecord holdingsRecord) {
        HoldingsTree holdingsTree = new HoldingsTree();
        holdingsTree.setHoldings(retrieveHoldings(id, bib, holdingsRecord));

        RdbmsItemDocumentManager rdbmsItemDocumentManager = RdbmsItemDocumentManager.getInstance();
        List<ItemRecord> itemRecords = (List<ItemRecord>) getBusinessObjectService().findMatching(ItemRecord.class, getHoldingsMap(id));
        for (ItemRecord itemRecord : itemRecords) {
            Item item = rdbmsItemDocumentManager.buildItemContent(itemRecord);
            item.setHolding(holdingsTree.getHoldings());
            holdingsTree.getItems().add(item);
        }

        List<HoldingsItemRecord> holdingsItemRecords = (List<HoldingsItemRecord>) getBusinessObjectService().findMatching(HoldingsItemRecord.class, getHoldingsMap(id));
        for (HoldingsItemRecord holdingsItemRecord : holdingsItemRecords) {
            holdingsTree.getItems().add(rdbmsItemDocumentManager.retrieveItem(holdingsItemRecord.getItemId(), null, null));
        }

        Collections.sort(holdingsTree.getItems());
        return holdingsTree;
    }

    /**
     * This method verifies the existence of linked documents to the holdings record. If exists throws exception with appropriate error message.
     *
     * @param holdingsId
     */
    @Override
    public void deleteVerify(String holdingsId) {
        String uuids = null;
        int uuidCount = 0;
        HoldingsTree deletingHoldingsTree = retrieveHoldingsTree(holdingsId, null, null);
        if (null != deletingHoldingsTree && null != deletingHoldingsTree.getHoldings()) {
            if (deletingHoldingsTree.getHoldings().isBoundWithBib()) {
                DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.BOUND_WITH_DELETE_MESSAGE, DocstoreResources.BOUND_WITH_DELETE_MESSAGE);
                throw docstoreException;
            } else if (deletingHoldingsTree.getHoldings().isSeries()) {
                DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.ANALYTIC_DELETE_MESSAGE_HOLDINGS, DocstoreResources.ANALYTIC_DELETE_MESSAGE_HOLDINGS);
                throw docstoreException;
            }
            OleHoldings oleHoldings = workHoldingOlemlRecordProcessor.fromXML(deletingHoldingsTree.getHoldings().getContent());
            if (oleHoldings.getGokbIdentifier() != null) {
                DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.HOLDINGS_GOKB_ID, "Holdings has imported from GOKB. So it cannot be deleted.");
                throw docstoreException;
            }
            StringBuilder uuidsSB = new StringBuilder();
            uuidsSB.append(holdingsId).append(DocStoreConstants.COMMA);
            uuidCount++;
            if (null != deletingHoldingsTree.getItems()) {
                for (Item item : deletingHoldingsTree.getItems()) {
                    if (null != item && null != item.getId()) {
                        if (item.isAnalytic()) {
                            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.ANALYTIC_DELETE_MESSAGE_ITEM, DocstoreResources.ANALYTIC_DELETE_MESSAGE_ITEM);
                            throw docstoreException;
                        }
                        uuidsSB.append(item.getId()).append(DocStoreConstants.COMMA);
                        uuidCount++;
                    }
                }
            }
            uuids = uuidsSB.substring(0, uuidsSB.length() - 1);
            checkUuidsToDelete(uuids, uuidCount);
        }
    }

    public void unbindWithOneBib(List<String> holdingsIds, String bibId) {

        Map<String, Object> criteria = new HashMap<String, Object>();
        Set<String> holdingsIdList = new HashSet<>();
        List<BibHoldingsRecord> bibHoldingsRecordList = new ArrayList<>();
        criteria.put("bibId", DocumentUniqueIDPrefix.getDocumentId(bibId));
        for (String holdingsId : holdingsIds) {
            criteria.put("holdingsId", DocumentUniqueIDPrefix.getDocumentId(holdingsId));
            bibHoldingsRecordList.addAll(getBusinessObjectService().findMatching(BibHoldingsRecord.class, criteria));
        }
        try {
            if (bibHoldingsRecordList.size() > 0) {
                getBusinessObjectService().delete(bibHoldingsRecordList);
            }
            holdingsIds.clear();
            for (BibHoldingsRecord bibHoldingsRecord : bibHoldingsRecordList) {
                holdingsIdList.add(DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, bibHoldingsRecord.getHoldingsId()));
            }
            holdingsIds.addAll(holdingsIdList);
        } catch (Exception e) {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.UN_BOUNDWITH_FAILED, e.getMessage());
            throw docstoreException;
        }

    }

    public void unbindWithAllBibs(List<String> holdingsIds, String bibId) {

        Map<String, Object> criteria = new HashMap<String, Object>();
        Set<String> holdingsIdList = new HashSet<>();
        List<BibHoldingsRecord> bibHoldingsRecordList = new ArrayList<>();
        List<BibHoldingsRecord> bibHoldingsRecordsToDelete = new ArrayList<>();
        for (String holdingsId : holdingsIds) {
            criteria.put("holdingsId", DocumentUniqueIDPrefix.getDocumentId(holdingsId));
            bibHoldingsRecordList.addAll(getBusinessObjectService().findMatching(BibHoldingsRecord.class, criteria));
        }
        if (bibHoldingsRecordList.size() > 0) {
            for (BibHoldingsRecord bibHoldingsRecord : bibHoldingsRecordList) {
                if (!bibHoldingsRecord.getBibId().equals(DocumentUniqueIDPrefix.getDocumentId(bibId))) {
                    criteria.put("bibId", bibHoldingsRecord.getBibId());
                    criteria.put("holdingsId", bibHoldingsRecord.getHoldingsId());
                    bibHoldingsRecordsToDelete.addAll(getBusinessObjectService().findMatching(BibHoldingsRecord.class, criteria));
                }
            }
            try {
                if (bibHoldingsRecordsToDelete.size() > 0) {
                    getBusinessObjectService().delete(bibHoldingsRecordsToDelete);
                    holdingsIds.clear();
                    for (BibHoldingsRecord bibHoldingsRecord : bibHoldingsRecordsToDelete) {
                        holdingsIdList.add(DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, bibHoldingsRecord.getHoldingsId()));
                    }
                    holdingsIds.addAll(holdingsIdList);
                }
            } catch (Exception e) {
                DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.UN_BOUNDWITH_FAILED, e.getMessage());
                throw docstoreException;
            }
        }

    }

    public HoldingsRecord processHoldingsForAudit(HoldingsRecord holdingsRecord){
        holdingsRecord.setAccessUriRecords(null);
        holdingsRecord.setDonorList(null);
        holdingsRecord.setBibRecords(null);
        holdingsRecord.setItemRecords(null);
        holdingsRecord.setHoldingsUriRecords(null);
        holdingsRecord.setHoldingsNoteRecords(null);
        holdingsRecord.setHoldingsStatisticalSearchRecords(null);
        holdingsRecord.seteInstancePerpetualAccessRecordList(null);
        holdingsRecord.setExtentOfOwnerShipRecords(null);
        holdingsRecord.seteInstanceCoverageRecordList(null);
        holdingsRecord.setHoldingsAccessLocations(null);
        return holdingsRecord;
    }
}