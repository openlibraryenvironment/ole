package org.kuali.ole.docstore.engine.service.storage.rdbms.dao;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.util.ReindexBatchStatistics;
import org.kuali.ole.docstore.engine.service.index.solr.BibMarcIndexer;
import org.kuali.ole.docstore.engine.service.index.solr.DocumentIndexer;
import org.kuali.ole.docstore.util.RebuildIndexUtil;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StopWatch;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by jayabharathreddy on 2/18/16.
 */
public class RebuildIndexBibDao implements Callable {

    public static Map<String, String> callNumberType = new HashMap<>();
    public static Map<String, String> receiptStatus = new HashMap<>();
    public static Map<String, String> authenticationType = new HashMap<>();
    public static Map<String, String> itemTypeMap = new HashMap<>();
    public static Map<String, String> itemStatusMap = new HashMap<>();
    public static Map<String, String> statisticalSearchCodeMap = new HashMap<>();
    public static Map<String, String> extentOfOwnershipTypeMap = new HashMap<>();


    private PlatformTransactionManager transactionManager;
    String bibQuery = "SELECT * FROM OLE_DS_BIB_T WHERE BIB_ID ";
    JdbcTemplate jdbcTemplate;
    String bibIds;
    ReindexBatchStatistics reindexBatchStatistics;

    public RebuildIndexBibDao(Map<String, Map<String, String>> commomFields, JdbcTemplate jdbcTemplate, List<String> tempBibIdList, ReindexBatchStatistics reindexBatchStatistics) {
        this.jdbcTemplate = jdbcTemplate;
        this.bibIds = StringUtils.join(tempBibIdList, ',');
        this.callNumberType = commomFields.get("callNumberType");
        this.receiptStatus = commomFields.get("receiptStatus");
        this.authenticationType = commomFields.get("authenticationType");
        this.itemTypeMap = commomFields.get("itemTypeMap");
        this.itemStatusMap = commomFields.get("itemStatusMap");
        this.statisticalSearchCodeMap = commomFields.get("statisticalSearchCodeMap");
        this.extentOfOwnershipTypeMap = commomFields.get("extentOfOwnershipTypeMap");
        this.reindexBatchStatistics = reindexBatchStatistics;
    }

    @Override
    public Object call() throws Exception {
        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());
        final SqlRowSet bibResultSet = this.jdbcTemplate.queryForRowSet(bibQuery + " IN ( " + bibIds + " )");
        try {
            template.execute(new TransactionCallback<Object>() {
                BibTrees bibTrees = new BibTrees();

                @Override
                public Object doInTransaction(TransactionStatus status) {
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    while (bibResultSet.next()) {
                        BibTree bibTree = new BibTree();
                        Bib bib = null;
                        try {
                            bib = fetchBibRecord(bibResultSet);
                            bibTree.setBib(bib);
                            List<HoldingsTree> holdingsList = fetchHoldingsTreeForBib(Integer.parseInt(bib.getLocalId()));
                            bibTree.getHoldingsTrees().addAll(holdingsList);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        bibTrees.getBibTrees().add(bibTree);
                    }
                    DocumentIndexer documentIndexer = BibMarcIndexer.getInstance();
                    documentIndexer.createTrees(bibTrees);

                    for(BibTree bibTree :bibTrees.getBibTrees()){
                        reindexBatchStatistics.addHoldingsCount(bibTree.getHoldingsTrees().size());
                        for(HoldingsTree holdingsTree:bibTree.getHoldingsTrees()){
                            reindexBatchStatistics.addItemCount(holdingsTree.getItems().size());
                        }
                    }
                    bibTrees.getBibTrees().clear();
                    return reindexBatchStatistics;
                }
            });
        } catch (Exception ex) {
            throw ex;
        } finally {
            this.transactionManager = null;

        }
        return null;
    }


    private Bib fetchBibRecord(SqlRowSet bibResultSet) throws SQLException {
        Bib bib = new BibMarc();
        bib.setCreatedBy(bibResultSet.getString("CREATED_BY"));
        bib.setCreatedOn(bibResultSet.getString("DATE_CREATED"));
        if (bibResultSet.getString("STAFF_ONLY") != null) {
            bib.setStaffOnly((bibResultSet.getString("STAFF_ONLY").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE));
        }
        bib.setContent(bibResultSet.getString("CONTENT"));
        bib.setUpdatedBy(bibResultSet.getString("UPDATED_BY"));
        bib.setUpdatedOn(bibResultSet.getString("DATE_UPDATED"));
        bib.setStatus(bibResultSet.getString("STATUS"));
        bib.setStatusUpdatedBy(bibResultSet.getString("STATUS_UPDATED_BY"));
        bib.setStatusUpdatedOn(bibResultSet.getString("STATUS_UPDATED_DATE"));
        bib.setLastUpdated(bibResultSet.getString("DATE_UPDATED"));
        String uuid = bibResultSet.getString("UNIQUE_ID_PREFIX") + "-" + bibResultSet.getString(1);
        bib.setId(uuid);
        bib.setLocalId(bibResultSet.getString(1));
        return bib;
    }


    private List<HoldingsTree> fetchHoldingsTreeForBib(int bibId) throws Exception {
        List<HoldingsTree> holdingsTrees = new ArrayList<HoldingsTree>();
        String holdingsQuery = getHoldingsQuery(bibId);
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(holdingsQuery);
        Map<String, HoldingsTree> map = new HashMap<>();
        Map<String, ExtentOfOwnership> extentOfOwnershipMap = new HashMap<>();
        Set<String> uriSet = null;
        Set<String> noteSet = null;
        Set<String> donorSet = null;
        Set<String> coverageSet = null;
        Set<String> perpetualSet = null;
        Set<String> extentOfOwnershipNoteSet = null;
        Set<String> linkSet = null;

        while (resultSet.next()) {
            String holdingsType = resultSet.getString("HOLDINGS_TYPE");
            String id = resultSet.getString("HOLDINGS_ID");
            if (id == null) {
                continue;
            }
            OleHoldings oleHoldings = null;
            if (map.containsKey(id)) {
                oleHoldings = map.get(id).getHoldings().getContentObject();
            } else {
                oleHoldings = new OleHoldings();
                Location location = RebuildIndexUtil.getLocationDetails(resultSet.getString("LOCATION"), resultSet.getString("LOCATION_LEVEL"));
                oleHoldings.setLocation(location);
                oleHoldings.setHoldingsType(holdingsType);
                CallNumber callNumber = new CallNumber();
                callNumber.setNumber(resultSet.getString("CALL_NUMBER"));
                callNumber.setPrefix(resultSet.getString("CALL_NUMBER_PREFIX"));
                ShelvingOrder shelvingOrder = new ShelvingOrder();
                if (resultSet.getString("SHELVING_ORDER") != null && !"null".equals(resultSet.getString("SHELVING_ORDER"))) {
                    shelvingOrder.setCodeValue(resultSet.getString("SHELVING_ORDER"));
                    shelvingOrder.setFullValue(resultSet.getString("SHELVING_ORDER"));
                }
                callNumber.setShelvingOrder(shelvingOrder);
                ShelvingScheme shelvingScheme = new ShelvingScheme();
                if (resultSet.getString("CALL_NUMBER_TYPE_ID") != null) {
                    String[] strings = callNumberType.get(resultSet.getString("CALL_NUMBER_TYPE_ID")).split("[|]");
                    shelvingScheme.setCodeValue(strings[0]);
                    shelvingScheme.setFullValue(strings[1]);
                }
                callNumber.setShelvingScheme(shelvingScheme);
                oleHoldings.setCallNumber(callNumber);
                oleHoldings.setCopyNumber(resultSet.getString("COPY_NUMBER"));
                HoldingsTree holdingsTree = new HoldingsTree();

                Holdings holdings = null;

                if (holdingsType.equalsIgnoreCase(PHoldings.PRINT)) {
                    holdings = new PHoldings();

                    if (resultSet.getString("RECEIPT_STATUS_ID") != null) {
                        oleHoldings.setReceiptStatus(resultSet.getString("RECEIPT_STATUS_ID"));
                    }
                    extentOfOwnershipNoteSet = new HashSet<>();
                    donorSet = coverageSet = perpetualSet = null;
                } else {
                    holdings = new EHoldings();
                    oleHoldings.setAccessStatus(resultSet.getString("ACCESS_STATUS"));
                    oleHoldings.setImprint(resultSet.getString("IMPRINT"));
                    Platform platform = new Platform();
                    platform.setPlatformName(resultSet.getString("PLATFORM"));
                    platform.setAdminUrl(resultSet.getString("ADMIN_URL"));
                    platform.setAdminUserName(resultSet.getString("ADMIN_USERNAME"));
                    platform.setAdminPassword(resultSet.getString("ADMIN_PASSWORD"));
                    oleHoldings.setPlatform(platform);

                    oleHoldings.setPublisher(resultSet.getString("PUBLISHER"));
                    HoldingsAccessInformation holdingsAccessInformation = new HoldingsAccessInformation();

                    holdingsAccessInformation.setProxiedResource(resultSet.getString("PROXIED_RESOURCE"));
                    holdingsAccessInformation.setAccessUsername(resultSet.getString("ACCESS_USERNAME"));
                    holdingsAccessInformation.setAccessPassword(resultSet.getString("ACCESS_PASSWORD"));
                    holdingsAccessInformation.setNumberOfSimultaneousUser(resultSet.getString("NUMBER_SIMULT_USERS"));
                    holdingsAccessInformation.setAccessLocation(resultSet.getString("CODE"));
                    holdingsAccessInformation.setAuthenticationType(resultSet.getString("AUTHENTICATION_TYPE_ID"));
                    oleHoldings.setHoldingsAccessInformation(holdingsAccessInformation);
                    String statisticalSearchId = resultSet.getString("STAT_SEARCH_CODE_ID");
                    if (StringUtils.isNotEmpty(statisticalSearchId)) {
                        String[] strings = statisticalSearchCodeMap.get(statisticalSearchId).split("[|]");
                        StatisticalSearchingCode statisticalSearchingCode = new StatisticalSearchingCode();
                        statisticalSearchingCode.setCodeValue(strings[0]);
                        statisticalSearchingCode.setFullValue(strings[1]);
                        oleHoldings.setStatisticalSearchingCode(statisticalSearchingCode);
                    }
                    oleHoldings.setLocalPersistentLink(resultSet.getString("LOCAL_PERSISTENT_URI"));
                    oleHoldings.setSubscriptionStatus(resultSet.getString("SUBSCRIPTION_STATUS"));
                    oleHoldings.setInterLibraryLoanAllowed(Boolean.valueOf(resultSet.getString("ALLOW_ILL")));
                    coverageSet = new HashSet<>();
                    perpetualSet = new HashSet<>();
                    donorSet = new HashSet<>();
                    ExtentOfOwnership extentOfOwnership = new ExtentOfOwnership();
                    Coverages coverages = new Coverages();
                    PerpetualAccesses perpetualAccesses = new PerpetualAccesses();
                    extentOfOwnership.setCoverages(coverages);
                    extentOfOwnership.setPerpetualAccesses(perpetualAccesses);
                    oleHoldings.getExtentOfOwnership().add(extentOfOwnership);

                }
                holdings.setHoldingsType(holdingsType);
                holdings.setId("who-" + id);
                holdings.setContentObject(oleHoldings);
                holdings.setCreatedBy(resultSet.getString("CREATED_BY"));
                holdings.setCreatedOn(resultSet.getString("DATE_CREATED"));
                if (resultSet.getString("STAFF_ONLY") != null) {
                    holdings.setStaffOnly((resultSet.getString("STAFF_ONLY").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE));
                }
                holdings.setUpdatedBy(resultSet.getString("UPDATED_BY"));
                holdings.setUpdatedOn(resultSet.getString("DATE_UPDATED"));
                holdings.setLastUpdated(resultSet.getString("DATE_UPDATED"));

                uriSet = new HashSet<>();
                noteSet = new HashSet<>();
                linkSet = new HashSet<>();

                holdingsTree.setHoldings(holdings);
                if (StringUtils.isNotBlank(id)) {
                    List<Item> itemList = fetchItemForHoldings(Integer.parseInt(id));
                    holdingsTree.getItems().addAll(itemList);
                }
                map.put(id, holdingsTree);

                holdingsTrees.add(holdingsTree);
            }

            if (StringUtils.isNotEmpty(holdingsType) && holdingsType.equalsIgnoreCase(PHoldings.PRINT)) {
                if (uriSet.add(resultSet.getString("HOLDINGS_URI_ID"))) {
                    Uri uri = new Uri();
                    uri.setValue(resultSet.getString("TEXT"));
                    oleHoldings.getUri().add(uri);
                }
                ExtentOfOwnership extentOfOwnership = null;
                if (extentOfOwnershipMap.containsKey(resultSet.getString("EXT_OWNERSHIP_ID"))) {
                    extentOfOwnership = extentOfOwnershipMap.get(resultSet.getString("EXT_OWNERSHIP_ID"));
                } else {
                    extentOfOwnership = new ExtentOfOwnership();
                    if (StringUtils.isNotEmpty(resultSet.getString("EXT_OWNERSHIP_TYPE_ID"))) {
                        String[] strings = extentOfOwnershipTypeMap.get(resultSet.getString("EXT_OWNERSHIP_TYPE_ID")).split("[|]");
                        extentOfOwnership.setType(strings[1]);
                    }
                    extentOfOwnershipMap.put(resultSet.getString("EXT_OWNERSHIP_ID"), extentOfOwnership);
                    oleHoldings.getExtentOfOwnership().add(extentOfOwnership);
                }
                String extOwnershipNoteId = resultSet.getString("EXT_OWNERSHIP_NOTE_ID");
                if (extentOfOwnershipNoteSet != null && StringUtils.isNotEmpty(extOwnershipNoteId) && extentOfOwnershipNoteSet.add(resultSet.getString("EXT_OWNERSHIP_NOTE_ID"))) {
                    Note note = new Note();
                    note.setValue((String) resultSet.getString("EXNOTE"));
                    note.setType((String) resultSet.getString("EXTYPE"));

                    extentOfOwnership.getNote().add(note);
                }
            } else {
                if (linkSet.add(resultSet.getString("HOLDINGS_URI_ID"))) {
                    Link link = new Link();
                    link.setUrl(resultSet.getString("URI"));
                    link.setText(resultSet.getString("TEXT"));
                    oleHoldings.getLink().add(link);
                }
                if (oleHoldings.getExtentOfOwnership() != null && oleHoldings.getExtentOfOwnership().size() > 0) {
                    if (coverageSet != null && coverageSet.add(resultSet.getString("HOLDINGS_COVERAGE_ID"))) {
                        Coverage coverage = new Coverage();
                        coverage.setCoverageStartIssue(resultSet.getString("COVERAGE_START_ISSUE"));
                        coverage.setCoverageStartDate(resultSet.getString("COVERAGE_START_DATE"));
                        coverage.setCoverageStartVolume(resultSet.getString("COVERAGE_START_VOLUME"));
                        coverage.setCoverageEndIssue(resultSet.getString("HOLDINGS_COVERAGE_ID"));
                        coverage.setCoverageEndDate(resultSet.getString("COVERAGE_END_DATE"));
                        coverage.setCoverageEndVolume(resultSet.getString("COVERAGE_END_VOLUME"));
                        oleHoldings.getExtentOfOwnership().get(0).getCoverages().getCoverage().add(coverage);
                    }

                    if (perpetualSet != null && perpetualSet.add(resultSet.getString("HOLDINGS_PERPETUAL_ACCESS_ID"))) {
                        PerpetualAccess perpetualAccess = new PerpetualAccess();
                        perpetualAccess.setPerpetualAccessStartDate(resultSet.getString("PERPETUAL_ACCESS_START_DATE"));
                        perpetualAccess.setPerpetualAccessStartIssue(resultSet.getString("PERPETUAL_ACCESS_START_ISSUE"));
                        perpetualAccess.setPerpetualAccessStartVolume(resultSet.getString("PERPETUAL_ACCESS_START_VOLUME"));
                        perpetualAccess.setPerpetualAccessEndDate(resultSet.getString("PERPETUAL_ACCESS_END_DATE"));
                        perpetualAccess.setPerpetualAccessEndVolume(resultSet.getString("PERPETUAL_ACCESS_END_VOLUME"));
                        perpetualAccess.setPerpetualAccessEndIssue(resultSet.getString("PERPETUAL_ACCESS_END_ISSUE"));
                        oleHoldings.getExtentOfOwnership().get(0).getPerpetualAccesses().getPerpetualAccess().add(perpetualAccess);
                    }
                }
                if (donorSet != null && donorSet.add(resultSet.getString("HOLDINGS_DONOR_ID"))) {
                    DonorInfo donorInfo = new DonorInfo();
                    donorInfo.setDonorCode(resultSet.getString("DONOR_CODE"));
                    donorInfo.setDonorNote(resultSet.getString("DONOR_NOTE"));
                    donorInfo.setDonorPublicDisplay(resultSet.getString("DONOR_DISPLAY_NOTE"));
                    oleHoldings.getDonorInfo().add(donorInfo);
                }
            }

            if (noteSet.add(resultSet.getString("HOLDINGS_NOTE_ID"))) {
                Note note = new Note();
                note.setValue(resultSet.getString("NOTE"));
                note.setType(resultSet.getString("TYPE"));
                oleHoldings.getNote().add(note);
            }
        }

        return holdingsTrees;
    }

    public List<Item> fetchItemForHoldings(int holdingsId) throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        Map<String, Item> itemHashMap = new HashMap<>();
        String itemQuery = getItemQuery(holdingsId);
        SqlRowSet resultSet = jdbcTemplate.queryForRowSet(itemQuery);
        Set<String> highDensityStorageSet = null;
        Set<String> donorNoteSet = null;
        Set<String> itemNoteSet = null;
        Set<String> statisticalSearchSet = null;
        org.kuali.ole.docstore.common.document.content.instance.Item itemObj = null;

        while (resultSet.next()) {
            String id = resultSet.getString("ITEM_ID");
            if (id == null) {
                continue;
            }
            if (itemHashMap.containsKey(id)) {
                itemObj = (org.kuali.ole.docstore.common.document.content.instance.Item) itemHashMap.get(id).getContentObject();
            } else {
                itemObj = new org.kuali.ole.docstore.common.document.content.instance.Item();
                Item item = new Item();
                itemList.add(item);
                item.setId("wio-" + resultSet.getString("ITEM_ID"));
                item.setContentObject(itemObj);
                Location location = RebuildIndexUtil.getLocationDetails(resultSet.getString("LOCATION"), resultSet.getString("LOCATION_LEVEL"));
                itemObj.setLocation(location);
                CallNumber callNumber = new CallNumber();
                callNumber.setNumber(resultSet.getString("CALL_NUMBER"));
                callNumber.setPrefix(resultSet.getString("CALL_NUMBER_PREFIX"));
                ShelvingOrder shelvingOrder = new ShelvingOrder();
                if (resultSet.getString("SHELVING_ORDER") != null && !"null".equals(resultSet.getString("SHELVING_ORDER"))) {
                    shelvingOrder.setCodeValue(resultSet.getString("SHELVING_ORDER"));
                    shelvingOrder.setFullValue(resultSet.getString("SHELVING_ORDER"));
                }
                callNumber.setShelvingOrder(shelvingOrder);
                ShelvingScheme shelvingScheme = new ShelvingScheme();
                if (callNumberType.get(resultSet.getString("CALL_NUMBER_TYPE_ID")) != null) {
                    String[] strings = callNumberType.get(resultSet.getString("CALL_NUMBER_TYPE_ID")).split("[|]");
                    shelvingScheme.setCodeValue(strings[0]);
                    shelvingScheme.setFullValue(strings[1]);
                }
                callNumber.setShelvingScheme(shelvingScheme);
                itemObj.setCallNumber(callNumber);
                itemObj.setBarcodeARSL(resultSet.getString("BARCODE_ARSL"));
                itemObj.setEnumeration(resultSet.getString("ENUMERATION"));
                itemObj.setChronology(resultSet.getString("CHRONOLOGY"));
                itemObj.setCopyNumber(resultSet.getString("COPY_NUMBER"));
                AccessInformation accessInformation = new AccessInformation();
                accessInformation.setBarcode(resultSet.getString("BARCODE"));
                Uri uri = new Uri();
                uri.setValue(resultSet.getString("URI"));
                accessInformation.setUri(uri);
                itemObj.setAccessInformation(accessInformation);
                itemObj.setPurchaseOrderLineItemIdentifier(resultSet.getString("PURCHASE_ORDER_LINE_ITEM_ID"));
                itemObj.setVendorLineItemIdentifier(resultSet.getString("VENDOR_LINE_ITEM_ID"));
                itemObj.setFund(resultSet.getString("FUND"));
                itemObj.setPrice(resultSet.getString("PRICE"));
                itemObj.setItemStatusEffectiveDate(RebuildIndexUtil.convertDateFormat(resultSet.getString("ITEM_STATUS_DATE_UPDATED")));
                if (resultSet.getString("FAST_ADD") != null) {
                    itemObj.setFastAddFlag(resultSet.getString("FAST_ADD").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE);
                }
                itemObj.setCheckinNote(resultSet.getString("CHECK_IN_NOTE"));
                if (resultSet.getString("CLAIMS_RETURNED") != null) {
                    itemObj.setClaimsReturnedFlag(resultSet.getString("CLAIMS_RETURNED").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE);
                }
                String claimsReturnFlagCreatedDate = resultSet.getString("CLAIMS_RETURNED_DATE_CREATED");
                itemObj.setClaimsReturnedFlagCreateDate(RebuildIndexUtil.convertDateFormat(claimsReturnFlagCreatedDate));
                itemObj.setClaimsReturnedNote(resultSet.getString("CLAIMS_RETURNED_NOTE"));
                itemObj.setCurrentBorrower(resultSet.getString("CURRENT_BORROWER"));
                itemObj.setProxyBorrower(resultSet.getString("PROXY_BORROWER"));
                String dueDateTime = resultSet.getString("DUE_DATE_TIME");
                itemObj.setDueDateTime(RebuildIndexUtil.convertDateFormat(dueDateTime));
                String originalDueDate = resultSet.getString("ORG_DUE_DATE_TIME");
                itemObj.setOriginalDueDate(RebuildIndexUtil.convertDateFormat(originalDueDate));
                String checkOutDateTime = resultSet.getString("CHECK_OUT_DATE_TIME");
                itemObj.setCheckOutDateTime(RebuildIndexUtil.convertDateFormat(checkOutDateTime));
                itemObj.setDamagedItemNote(resultSet.getString("ITEM_DAMAGED_NOTE"));
                if (resultSet.getString("ITEM_DAMAGED_STATUS") != null) {
                    itemObj.setItemDamagedStatus(resultSet.getString("ITEM_DAMAGED_STATUS").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE);
                }
                if (resultSet.getString("MISSING_PIECES") != null) {
                    itemObj.setMissingPieceFlag(resultSet.getString("MISSING_PIECES").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE);
                }
                itemObj.setMissingPiecesCount(resultSet.getString("MISSING_PIECES_COUNT"));
                itemObj.setMissingPieceFlagNote(resultSet.getString("MISSING_PIECES_NOTE"));
                itemObj.setMissingPieceEffectiveDate(resultSet.getString("MISSING_PIECES_EFFECTIVE_DATE"));
                itemObj.setNumberOfPieces(resultSet.getString("NUM_PIECES"));
                itemObj.setDescriptionOfPieces(resultSet.getString("DESC_OF_PIECES"));
                itemObj.setNumberOfRenew(resultSet.getInt("NUM_OF_RENEW"));
                highDensityStorageSet = new HashSet<>();
                itemNoteSet = new HashSet<>();
                statisticalSearchSet = new HashSet<>();
                donorNoteSet = new HashSet<>();

                ItemStatus itemStatus = new ItemStatus();
                if (itemStatusMap.containsKey(resultSet.getString("ITEM_STATUS_ID"))) {
                    String[] strings = itemStatusMap.get(resultSet.getString("ITEM_STATUS_ID")).split("[|]");
                    itemStatus.setCodeValue(strings[0]);
                    itemStatus.setFullValue(strings[1]);
                }

                itemObj.setItemStatus(itemStatus);
                ItemType itemType = new ItemType();
                if (itemTypeMap.containsKey(resultSet.getString("ITEM_TYPE_ID"))) {
                    String[] strings = itemTypeMap.get(resultSet.getString("ITEM_TYPE_ID")).split("[|]");
                    itemType.setCodeValue(strings[0]);
                    itemType.setFullValue(strings[1]);
                }
                itemObj.setItemType(itemType);
                ItemType tempItemType = new ItemType();
                if (itemTypeMap.containsKey(resultSet.getString("TEMP_ITEM_TYPE_ID"))) {
                    String[] strings = itemTypeMap.get(resultSet.getString("TEMP_ITEM_TYPE_ID")).split("[|]");
                    tempItemType.setCodeValue(strings[0]);
                    tempItemType.setFullValue(strings[1]);
                }
                itemObj.setTemporaryItemType(tempItemType);
                item.setContentObject(itemObj);
                item.setCreatedBy(resultSet.getString("CREATED_BY"));
                item.setCreatedOn(resultSet.getString("DATE_CREATED"));
                if (resultSet.getString("STAFF_ONLY") != null) {
                    item.setStaffOnly((resultSet.getString("STAFF_ONLY").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE));
                }
                item.setUpdatedBy(resultSet.getString("UPDATED_BY"));
                item.setUpdatedOn(resultSet.getString("DATE_UPDATED"));
                item.setLastUpdated(resultSet.getString("DATE_UPDATED"));

            }
            if (itemNoteSet.add(resultSet.getString("ITEM_NOTE_ID"))) {
                Note note = new Note();
                note.setValue(resultSet.getString("NOTE"));
                note.setType(resultSet.getString("TYPE"));
                itemObj.getNote().add(note);
            }
            if (resultSet.getString("STAT_SEARCH_CODE_ID") != null && statisticalSearchSet.add(resultSet.getString("STAT_SEARCH_CODE_ID"))) {
                String[] strings = statisticalSearchCodeMap.get(resultSet.getString("STAT_SEARCH_CODE_ID")).split("[|]");
                StatisticalSearchingCode statisticalSearchingCode = new StatisticalSearchingCode();
                statisticalSearchingCode.setCodeValue(strings[0]);
                statisticalSearchingCode.setFullValue(strings[1]);
                itemObj.getStatisticalSearchingCode().add(statisticalSearchingCode);
            }
            if (donorNoteSet.add(resultSet.getString("ITEM_DONOR_ID"))) {
                DonorInfo donorInfo = new DonorInfo();
                donorInfo.setDonorCode(resultSet.getString("DONOR_CODE"));
                donorInfo.setDonorNote(resultSet.getString("DONOR_NOTE"));
                donorInfo.setDonorPublicDisplay(resultSet.getString("DONOR_DISPLAY_NOTE"));
                itemObj.getDonorInfo().add(donorInfo);
            }
            if (highDensityStorageSet.add(resultSet.getString("ITEM_DONOR_ID"))) {
                HighDensityStorage highDensityStorage = new HighDensityStorage();
                highDensityStorage.setRow(resultSet.getString("HIGH_DENSITY_ROW"));
                itemObj.setHighDensityStorage(highDensityStorage);
            }
        }
        return itemList;
    }

    private String getItemQuery(int holdingsId) {
        return "SELECT I.*,N.ITEM_NOTE_ID,N.NOTE,N.TYPE, S.STAT_SEARCH_CODE_ID," +
                "D.ITEM_DONOR_ID,D.DONOR_CODE,D.DONOR_DISPLAY_NOTE,D.DONOR_NOTE,HD.HIGH_DENSITY_ROW " +
                "FROM ole_ds_item_t I " +
                "LEFT JOIN ole_ds_item_donor_t D ON I.item_id=D.item_id " +
                "LEFT JOIN ole_ds_item_note_t N ON I.item_id = N.item_id " +
                "LEFT JOIN ole_ds_item_stat_search_t S ON I.item_id=S.item_id " +
                "LEFT JOIN OLE_DS_HIGH_DENSITY_STORAGE_T HD ON I.HIGH_DENSITY_STORAGE_ID =HD.HIGH_DENSITY_STORAGE_ID " +
                "WHERE I.HOLDINGS_ID= " + holdingsId;
    }

    private String getHoldingsQuery(int bibId) {
        return "SELECT H.* ,LOC.CODE,STAT.STAT_SEARCH_CODE_ID,URI.HOLDINGS_URI_ID,URI.TEXT,URI.URI,EXT.EXT_OWNERSHIP_ID,EXT.EXT_OWNERSHIP_TYPE_ID," +
                "EXTN.EXT_OWNERSHIP_NOTE_ID,EXTN.NOTE AS EXNOTE,EXTN.TYPE AS EXTYPE,COV.HOLDINGS_COVERAGE_ID,COV.COVERAGE_START_ISSUE, COV.COVERAGE_START_DATE," +
                "COV.COVERAGE_START_VOLUME,COV.COVERAGE_END_DATE,COV.COVERAGE_END_VOLUME,PA.HOLDINGS_PERPETUAL_ACCESS_ID,PA.PERPETUAL_ACCESS_START_DATE,PA.PERPETUAL_ACCESS_START_ISSUE," +
                "PA.PERPETUAL_ACCESS_START_VOLUME,PA.PERPETUAL_ACCESS_END_DATE,PA.PERPETUAL_ACCESS_END_VOLUME,PA.PERPETUAL_ACCESS_END_ISSUE,DONOR.HOLDINGS_DONOR_ID, " +
                "DONOR.DONOR_CODE,DONOR.DONOR_NOTE,DONOR.DONOR_DISPLAY_NOTE,NOTE.HOLDINGS_NOTE_ID,NOTE.NOTE,NOTE.TYPE " +
                "FROM OLE_DS_HOLDINGS_T  H " +
                "LEFT JOIN ole_ds_holdings_uri_t  URI ON H.HOLDINGS_ID=URI.HOLDINGS_ID " +
                "LEFT JOIN ole_ds_holdings_note_t  NOTE ON H.HOLDINGS_ID=NOTE.HOLDINGS_ID " +
                "LEFT JOIN OLE_DS_HOLDINGS_DONOR_T DONOR ON H.HOLDINGS_ID=DONOR.HOLDINGS_ID " +
                "LEFT JOIN OLE_DS_HOLDINGS_COVERAGE_T COV ON H.HOLDINGS_ID=COV.HOLDINGS_ID " +
                "LEFT JOIN OLE_DS_PERPETUAL_ACCESS_T PA ON H.HOLDINGS_ID=PA.HOLDINGS_ID " +
                "LEFT JOIN OLE_DS_HOLDINGS_STAT_SEARCH_T  STAT ON H.HOLDINGS_ID=STAT.HOLDINGS_ID " +
                "LEFT JOIN OLE_DS_ACCESS_LOCATION_T ACC ON H.HOLDINGS_ID=ACC.HOLDINGS_ID " +
                "LEFT JOIN OLE_DS_ACCESS_LOCATION_CODE_T LOC ON ACC.ACCESS_LOCATION_CODE_ID=LOC.ACCESS_LOCATION_CODE_ID " +
                "LEFT JOIN ole_ds_ext_ownership_t  ext ON H.HOLDINGS_ID=ext.holdings_id " +
                "LEFT JOIN ole_ds_ext_ownership_note_t  EXTN ON ext.EXT_OWNERSHIP_ID=EXTN.EXT_OWNERSHIP_ID  WHERE H.BIB_ID= " + bibId;
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService("transactionManager");
        }
        return this.transactionManager;
    }
}
