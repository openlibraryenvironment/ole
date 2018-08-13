package org.kuali.ole.docstore.common.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 5/30/14
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class BatchBibTreeDBUtil {

    private static final Logger LOG = LoggerFactory.getLogger(BatchBibTreeDBUtil.class);
    public static Map<String, String> callNumberType = new HashMap<>();
    public static Map<String, String> receiptStatus = new HashMap<>();
    public static Map<String, String> authenticationType = new HashMap<>();
    public static Map<String, String> itemTypeMap = new HashMap<>();
    public static Map<String, String> itemStatusMap = new HashMap<>();
    public static Map<String, String> statisticalSearchCodeMap = new HashMap<>();
    public static Map<String, String> extentOfOwnershipTypeMap = new HashMap<>();

    private Connection connection = null;
    private Connection bibConnection = null;
    private Connection holdingsConnection = null;
    private Connection itemConnection = null;

    private Statement bibStatement = null;
    private PreparedStatement holdingsPreparedStatement = null;
    private PreparedStatement itemPreparedStatement = null;
    private ResultSet bibResultSet = null;
    private ResultSet bibHoldingsResultSet = null;
    private ResultSet holdingItemResultSet = null;
    private final static String dbVendor = ConfigContext.getCurrentContextConfig().getProperty("db.vendor");
    static BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
    private boolean isStaffOnly = true;


    PreparedStatement bibInsertPreparedStatement = null;
    PreparedStatement bibUpdatePreparedStatement = null;


    private String bibQuery = "SELECT * FROM OLE_DS_BIB_T ORDER BY BIB_ID";

    private String holdingsQuery = "SELECT * FROM OLE_DS_HOLDINGS_T " +
            " LEFT JOIN ole_ds_holdings_uri_t ON OLE_DS_HOLDINGS_T.HOLDINGS_ID=ole_ds_holdings_uri_t.HOLDINGS_ID " +
            " LEFT JOIN ole_ds_holdings_note_t ON OLE_DS_HOLDINGS_T.HOLDINGS_ID=ole_ds_holdings_note_t.HOLDINGS_ID " +
            " LEFT JOIN OLE_DS_HOLDINGS_DONOR_T ON OLE_DS_HOLDINGS_T.HOLDINGS_ID=OLE_DS_HOLDINGS_DONOR_T.HOLDINGS_ID" +
            " LEFT JOIN OLE_DS_HOLDINGS_COVERAGE_T ON OLE_DS_HOLDINGS_T.HOLDINGS_ID=OLE_DS_HOLDINGS_COVERAGE_T.HOLDINGS_ID " +
            " LEFT JOIN OLE_DS_PERPETUAL_ACCESS_T ON OLE_DS_HOLDINGS_T.HOLDINGS_ID=OLE_DS_PERPETUAL_ACCESS_T.HOLDINGS_ID " +
            " LEFT JOIN OLE_DS_HOLDINGS_STAT_SEARCH_T ON OLE_DS_HOLDINGS_T.HOLDINGS_ID=OLE_DS_HOLDINGS_STAT_SEARCH_T.HOLDINGS_ID " +
            " LEFT JOIN OLE_DS_ACCESS_LOCATION_T ON OLE_DS_HOLDINGS_T.HOLDINGS_ID=OLE_DS_ACCESS_LOCATION_T.HOLDINGS_ID " +
            " LEFT JOIN OLE_DS_ACCESS_LOCATION_CODE_T ON OLE_DS_ACCESS_LOCATION_T.ACCESS_LOCATION_CODE_ID=OLE_DS_ACCESS_LOCATION_CODE_T.ACCESS_LOCATION_CODE_ID " +
            " LEFT JOIN ole_ds_ext_ownership_t ON OLE_DS_HOLDINGS_T.HOLDINGS_ID=ole_ds_ext_ownership_t.holdings_id " +
            " LEFT JOIN ole_ds_ext_ownership_note_t ON ole_ds_ext_ownership_t.EXT_OWNERSHIP_ID=ole_ds_ext_ownership_note_t.EXT_OWNERSHIP_ID " +
            " WHERE OLE_DS_HOLDINGS_T.BIB_ID=?";

    private String itemQuery = "SELECT * FROM ole_ds_item_t " +
            " LEFT JOIN ole_ds_item_donor_t ON ole_ds_item_t.item_id=ole_ds_item_donor_t.item_id " +
            " LEFT JOIN ole_ds_item_note_t ON ole_ds_item_t.item_id = ole_ds_item_note_t.item_id " +
            " LEFT JOIN ole_ds_item_stat_search_t ON ole_ds_item_t.item_id=ole_ds_item_stat_search_t.item_id " +
            " WHERE OLE_DS_ITEM_T.HOLDINGS_ID=?";

    private String bibStaffOnly = " SELECT * FROM OLE_DS_BIB_T  WHERE STAFF_ONLY= 'N' ";
    private String staffOnly = " WHERE STAFF_ONLY= 'N' ";
    private String staffOnlyHoldings = " AND OLE_DS_HOLDINGS_T.STAFF_ONLY= 'N' ";
    private String staffOnlyItem = " AND OLE_DS_ITEM_T.STAFF_ONLY= 'N' ";

    private String bibCountQuery = "SELECT count(*) as totalRecords FROM ole_ds_bib_t";


    public  BatchBibTreeDBUtil(){

    }

   public  BatchBibTreeDBUtil(boolean isStaffOnly){
           this.isStaffOnly=isStaffOnly;
   }

    public void init(int startIndex, int endIndex, String updateDate) throws SQLException {

        if (connection == null || connection.isClosed()) {
            connection = getConnection();
        }
        if (startIndex != 0 && endIndex != 0) {
            bibQuery = "SELECT * FROM OLE_DS_BIB_T WHERE BIB_ID BETWEEN " + startIndex + " AND " + endIndex + " ORDER BY BIB_ID";
        } else if (StringUtils.isNotEmpty(updateDate)) {
            updateDate = getDateStringForOracle(updateDate);
            bibQuery = "SELECT * FROM OLE_DS_BIB_T where DATE_UPDATED > '"+updateDate+"'";
        }
        else{
            bibQuery = "SELECT * FROM OLE_DS_BIB_T ORDER BY BIB_ID";
        }
        if(!isStaffOnly){
            bibQuery = bibStaffOnly;
            holdingsQuery =  holdingsQuery + staffOnlyHoldings;
            itemQuery =  itemQuery + staffOnlyItem;
        }

        fetchCallNumberType();
        fetchReceiptStatus();
        fetchAuthenticationType();
        fetchItemType();
        fetchItemStatus();
        fetchStatisticalSearchCode();
        fetchExtentOfOwnershipType();

        bibConnection = getConnection();
        holdingsConnection = getConnection();
        itemConnection = getConnection();
        bibConnection.setAutoCommit(false);

        bibStatement = bibConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        if (dbVendor.equalsIgnoreCase("oracle")) {
            bibStatement.setFetchSize(1);
        } else if (dbVendor.equalsIgnoreCase("mysql")) {
            bibStatement.setFetchSize(Integer.MIN_VALUE);
        }

        bibResultSet = bibStatement.executeQuery(bibQuery);

        holdingsPreparedStatement = holdingsConnection.prepareStatement(holdingsQuery);

        itemPreparedStatement = itemConnection.prepareStatement(itemQuery);

        String insertQuery = "INSERT INTO OLE_DS_BIB_INFO_T(BIB_ID, BIB_ID_STR, TITLE, AUTHOR, PUBLISHER, ISXN) VALUES (?,?,?,?,?,?)";
        bibInsertPreparedStatement = connection.prepareStatement(insertQuery);

        String updateQuery = "UPDATE OLE_DS_BIB_INFO_T SET TITLE=?, AUTHOR=?, PUBLISHER=?, ISXN=?, BIB_ID_STR=? WHERE BIB_ID=?";
        bibUpdatePreparedStatement = connection.prepareStatement(updateQuery);
    }

    private String getDateStringForOracle(String updateDate) {
        try {
            if (dbVendor.equalsIgnoreCase("oracle")) {
                if(updateDate.length()<11){
                    updateDate=updateDate+ " 00:00:00";
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yy-mm-dd hh:mm:ss");
                Date date = formatter.parse(updateDate);

                String dateFormat="dd-MMM-yy hh:mm:ss";

                SimpleDateFormat oracleFormat = new SimpleDateFormat(dateFormat);
                updateDate=oracleFormat.format(date);
            }

        } catch (ParseException e) {
            LOG.error("ParseException : " + e);
            throw new DocstoreException(e);
        }
        return updateDate;
    }


    private Connection getConnection() throws SQLException {
        DataSource dataSource = null;
        try {
            dataSource = DataSource.getInstance();
        } catch (IOException e) {
            LOG.error("IOException : " + e);
        } catch (SQLException e) {
            LOG.error("SQLException : " + e);
        } catch (PropertyVetoException e) {
            LOG.error("PropertyVetoException : " + e);
        }
        return dataSource.getConnection();
    }


    public void closeConnections() throws SQLException {
        if (itemPreparedStatement != null) {
            itemPreparedStatement.close();
        }

        if (itemConnection != null) {
            itemConnection.close();
        }

        if (holdingsPreparedStatement != null) {
            holdingsPreparedStatement.close();
        }

        if (holdingsConnection != null) {
            holdingsConnection.close();
        }

        if (bibResultSet != null) {
            bibResultSet.close();
        }
        if (bibStatement != null) {
            bibStatement.close();
        }

        if (bibConnection != null) {
            bibConnection.close();
        }
        if (connection != null) {
            connection.close();
        }

    }

    private void fetchCallNumberType() throws SQLException {

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT SHVLG_SCHM_ID,SHVLG_SCHM_CD,SHVLG_SCHM_NM from OLE_CAT_SHVLG_SCHM_T");
        while (resultSet.next()) {
            callNumberType.put(resultSet.getString("SHVLG_SCHM_ID"), resultSet.getString("SHVLG_SCHM_CD") + "|" + resultSet.getString("SHVLG_SCHM_NM"));
        }
        resultSet.close();
    }

    private void fetchReceiptStatus() throws SQLException {

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT RCPT_STAT_CD,RCPT_STAT_NM from OLE_CAT_RCPT_STAT_T");
        while (resultSet.next()) {
            receiptStatus.put(resultSet.getString("RCPT_STAT_CD"), resultSet.getString("RCPT_STAT_NM"));
        }
        resultSet.close();

    }

    private void fetchAuthenticationType() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT CODE,NAME from OLE_DS_AUTHENTICATION_TYPE_T");
        while (resultSet.next()) {
            authenticationType.put(resultSet.getString("CODE"), resultSet.getString("NAME"));
        }
        resultSet.close();

    }

    private void fetchItemType() throws SQLException {

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT ITM_TYP_CD_ID,ITM_TYP_CD,ITM_TYP_NM from OLE_CAT_ITM_TYP_T");
        while (resultSet.next()) {
            itemTypeMap.put(resultSet.getString("ITM_TYP_CD_ID"), resultSet.getString("ITM_TYP_CD") + "|" + resultSet.getString("ITM_TYP_NM"));
        }
        resultSet.close();

    }

    private void fetchItemStatus() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT ITEM_AVAIL_STAT_ID,ITEM_AVAIL_STAT_CD,ITEM_AVAIL_STAT_NM from OLE_DLVR_ITEM_AVAIL_STAT_T");
        while (resultSet.next()) {
            itemStatusMap.put(resultSet.getString("ITEM_AVAIL_STAT_ID"), resultSet.getString("ITEM_AVAIL_STAT_CD") + "|" + resultSet.getString("ITEM_AVAIL_STAT_NM"));
        }
        resultSet.close();

    }

    private void fetchStatisticalSearchCode() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT STAT_SRCH_CD_ID, STAT_SRCH_CD, STAT_SRCH_NM from OLE_CAT_STAT_SRCH_CD_T");
        while (resultSet.next()) {
            statisticalSearchCodeMap.put(resultSet.getString("STAT_SRCH_CD_ID"), resultSet.getString("STAT_SRCH_CD") + "|" + resultSet.getString("STAT_SRCH_NM"));
        }
        resultSet.close();
    }

    private void fetchExtentOfOwnershipType() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT TYPE_OWNERSHIP_ID, TYPE_OWNERSHIP_CD,TYPE_OWNERSHIP_NM  from OLE_CAT_TYPE_OWNERSHIP_T");
        while (resultSet.next()) {
            extentOfOwnershipTypeMap.put(resultSet.getString("TYPE_OWNERSHIP_ID"), resultSet.getString("TYPE_OWNERSHIP_CD") + "|" + resultSet.getString("TYPE_OWNERSHIP_NM"));
        }
        resultSet.close();
    }
    public void fetchHoldingItems() throws SQLException {
        Statement statement = connection.createStatement();
        holdingItemResultSet = statement.executeQuery("SELECT * FROM OLE_DS_ITEM_HOLDINGS_T");

    }

    public Map<String, List> fetchHoldingItem(boolean cursor) throws SQLException {

        String holdingId = "";
        String tempHoldingId = "";
        List itemIds = new ArrayList();

        Map<String, List> map = null;
        if (cursor) {
            while (holdingItemResultSet.next()) {
                holdingId = "who-" + holdingItemResultSet.getString("HOLDINGS_ID");
                if (StringUtils.isNotEmpty(tempHoldingId) && !tempHoldingId.equals(holdingId)) {
                    if(itemIds.size() > 0) {
                        map = new HashMap<>();
                        map.put(tempHoldingId, itemIds);
                        return map;
                    }
                }
                itemIds.add("wio-" + holdingItemResultSet.getString("ITEM_ID"));
                tempHoldingId = holdingId;
            }

        } else {
            holdingItemResultSet.previous();
//           while(holdingItemResultSet.previous()) {
//               break;
//           }
            while (holdingItemResultSet.next()) {
                holdingId = "who-" + holdingItemResultSet.getString("HOLDINGS_ID");
                if (StringUtils.isNotEmpty(tempHoldingId) && !tempHoldingId.equals(holdingId)) {
                    if(itemIds.size() > 0) {
                        map = new HashMap<>();
                        map.put(tempHoldingId, itemIds);
                        return map;
                    }

                }
                itemIds.add("wio-" + holdingItemResultSet.getString("ITEM_ID"));
                tempHoldingId = holdingId;
            }

        }

        if (itemIds.size() > 0) {
            map = new HashMap<>();
            map.put(tempHoldingId, itemIds);
            return map;
        } else {
            map = null;
        }
        holdingItemResultSet.close();
        return map;

    }


    public void fetchBibHoldings() throws SQLException {
        Statement statement = connection.createStatement();
        bibHoldingsResultSet = statement.executeQuery("SELECT * FROM OLE_DS_BIB_HOLDINGS_T");

    }


    public Map<String, List> fetchBibHolding(boolean cursor) throws SQLException {
        String holdingId = "";
        String tempHoldingId = "";
        List bibIds = new ArrayList();
        Map<String, List> map = null;

        if(cursor) {
            while (bibHoldingsResultSet.next()) {
                holdingId = "who-"+bibHoldingsResultSet.getString("HOLDINGS_ID");
                if(StringUtils.isNotEmpty(tempHoldingId) && !tempHoldingId.equals(holdingId)) {

                    if(bibIds.size() > 0) {
                        map = new HashMap<>();
                        bibIds.remove(0);
                        map.put(tempHoldingId, bibIds);
                        return map;
                    }
                }

                bibIds.add("wbm-"+bibHoldingsResultSet.getString("BIB_ID"));
                tempHoldingId = holdingId;
            }

        } else {
            while (bibHoldingsResultSet.next()) {
                holdingId = "who-"+bibHoldingsResultSet.getString("HOLDINGS_ID");
                if(StringUtils.isNotEmpty(tempHoldingId) && !tempHoldingId.equals(holdingId)) {

                    map = new HashMap<>();
                    map.put(tempHoldingId, bibIds);
                    return map;
                }
                bibIds.add("wbm-"+bibHoldingsResultSet.getString("BIB_ID"));
                tempHoldingId = holdingId;

            }


        }

        if(bibIds.size() > 0) {
            map = new HashMap<>();
            bibIds.remove(0);
            map.put(tempHoldingId, bibIds);
            return map;
        }
        else {
            map = null;
        }
        bibHoldingsResultSet.close();
        return map;
    }

    public synchronized BibTrees fetchNextBatch(int batchSize, BatchStatistics batchStatistics, Boolean isBibOnly) throws Exception {
        return fetchResultSet(batchSize, batchStatistics, isBibOnly);
    }

    public synchronized BibTrees fetchNextBatch(int batchSize, BatchStatistics batchStatistics) throws Exception {
        BibTrees bibTrees = null;

        bibTrees = fetchResultSet(batchSize, batchStatistics, false);
        return bibTrees;
    }

    private BibTrees fetchResultSet(int batchSize, BatchStatistics batchStatistics, Boolean isBibOnly) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        BibTrees bibTrees = new BibTrees();
        int count = 0;
        if (batchSize <= 0) {
            return bibTrees;
        }
        while (bibResultSet.next()) {
            count++;
            BibTree bibTree = new BibTree();
            Bib bib = fetchBibRecord();
            bibTree.setBib(bib);
            if (!isBibOnly) {
                List<HoldingsTree> holdingsList = fetchHoldingsTreeForBib(Integer.parseInt(bib.getLocalId()));
                bibTree.getHoldingsTrees().addAll(holdingsList);
                batchStatistics.addHoldingsCount(holdingsList.size());

                for (HoldingsTree holdingsTree : holdingsList) {
                    batchStatistics.addItemCount(holdingsTree.getItems().size());
                }
            }

            bibTrees.getBibTrees().add(bibTree);
            if (count == batchSize) {
                break;
            }
        }
        stopWatch.stop();
        batchStatistics.addTimeTaken(stopWatch.getTotalTimeMillis());
        batchStatistics.addBibCount(bibTrees.getBibTrees().size());
        return bibTrees;
    }

    private Bib fetchBibRecord() throws SQLException {
        Bib bib = new BibMarc();
        bib.setCreatedBy(bibResultSet.getString("CREATED_BY"));
        bib.setCreatedOn(bibResultSet.getString("DATE_CREATED"));
        if(bibResultSet.getString("STAFF_ONLY") !=null){
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


    public List<HoldingsTree> fetchHoldingsTreeForBib(int bibId) throws Exception {
        List<HoldingsTree> holdingsTrees = new ArrayList<HoldingsTree>();

        holdingsPreparedStatement.setInt(1, bibId);
        ResultSet resultSet = holdingsPreparedStatement.executeQuery();
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
            OleHoldings oleHoldings = null;
            if (map.containsKey(id)) {
                oleHoldings = map.get(id).getHoldings().getContentObject();
            } else {
                oleHoldings = new OleHoldings();
                Location location = getLocationDetails(resultSet.getString("LOCATION"), resultSet.getString("LOCATION_LEVEL"));
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
                    if(StringUtils.isNotEmpty(statisticalSearchId)) {
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
                if(resultSet.getString("STAFF_ONLY") !=null){
                    holdings.setStaffOnly((resultSet.getString("STAFF_ONLY").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE));
                }
                holdings.setUpdatedBy(resultSet.getString("UPDATED_BY"));
                holdings.setUpdatedOn(resultSet.getString("DATE_UPDATED"));
                holdings.setLastUpdated(resultSet.getString("DATE_UPDATED"));

                uriSet = new HashSet<>();
                noteSet = new HashSet<>();
                linkSet = new HashSet<>();
                List<Item> itemList = fetchItemForHoldings(Integer.parseInt(id));
                holdingsTree.setHoldings(holdings);
                holdingsTree.getItems().addAll(itemList);

                map.put(id, holdingsTree);

                holdingsTrees.add(holdingsTree);
            }

            if(StringUtils.isNotEmpty(holdingsType) && holdingsType.equalsIgnoreCase(PHoldings.PRINT)) {
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
                    if(StringUtils.isNotEmpty(resultSet.getString("EXT_OWNERSHIP_TYPE_ID"))) {
                        String[] strings = extentOfOwnershipTypeMap.get(resultSet.getString("EXT_OWNERSHIP_TYPE_ID")).split("[|]");
                        extentOfOwnership.setType(strings[1]);
                    }
                    extentOfOwnershipMap.put(resultSet.getString("EXT_OWNERSHIP_ID"), extentOfOwnership);
                    oleHoldings.getExtentOfOwnership().add(extentOfOwnership);
                }
                String extOwnershipNoteId = resultSet.getString("EXT_OWNERSHIP_NOTE_ID");
                if (extentOfOwnershipNoteSet != null && StringUtils.isNotEmpty(extOwnershipNoteId) && extentOfOwnershipNoteSet.add(resultSet.getString("EXT_OWNERSHIP_NOTE_ID"))) {
                    Note note = new Note();
                    note.setValue(resultSet.getString(83));
                    note.setType(resultSet.getString(82));

                    extentOfOwnership.getNote().add(note);
                }
            }
            else {
                if (linkSet.add(resultSet.getString("HOLDINGS_URI_ID"))) {
                    Link link = new Link();
                    link.setUrl(resultSet.getString("URI"));
                    link.setText(resultSet.getString("TEXT"));
                    oleHoldings.getLink().add(link);
                }
                if(oleHoldings.getExtentOfOwnership() != null && oleHoldings.getExtentOfOwnership().size() > 0) {
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
        resultSet.close();
        return holdingsTrees;
    }

    public Location getLocationDetails(String locationName, String locationLevelName) {
        Location location = new Location();
        LocationLevel locationLevel = createLocationLevel(locationName, locationLevelName);
        location.setLocationLevel(locationLevel);
        return location;
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


    public List<Item> fetchItemForHoldings(int holdingsId) throws Exception {
        List<Item> itemList = new ArrayList<Item>();
        Map<String, Item> itemHashMap = new HashMap<>();
        itemPreparedStatement.setInt(1, holdingsId);
        ResultSet resultSet = itemPreparedStatement.executeQuery();
        Set<String> donorNoteSet = null;
        Set<String> itemNoteSet = null;
        Set<String> statisticalSearchSet = null;
        org.kuali.ole.docstore.common.document.content.instance.Item itemObj = null;

        while (resultSet.next()) {
            String id = resultSet.getString("ITEM_ID");
            if (itemHashMap.containsKey(id)) {
                itemObj = (org.kuali.ole.docstore.common.document.content.instance.Item) itemHashMap.get(id).getContentObject();
            } else {
                itemObj = new org.kuali.ole.docstore.common.document.content.instance.Item();
                Item item = new Item();
                itemList.add(item);
                item.setId("wio-" + resultSet.getString("ITEM_ID"));
                item.setContentObject(itemObj);
                Location location = getLocationDetails(resultSet.getString("LOCATION"), resultSet.getString("LOCATION_LEVEL"));
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
                itemObj.setItemStatusEffectiveDate(convertDateFormat(resultSet.getString("ITEM_STATUS_DATE_UPDATED")));
                if(resultSet.getString("FAST_ADD") != null){
                    itemObj.setFastAddFlag(resultSet.getString("FAST_ADD").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE);
                }
                itemObj.setCheckinNote(resultSet.getString("CHECK_IN_NOTE"));
                if(resultSet.getString("CLAIMS_RETURNED") != null) {
                    itemObj.setClaimsReturnedFlag(resultSet.getString("CLAIMS_RETURNED").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE);
                }
                String claimsReturnFlagCreatedDate = resultSet.getString("CLAIMS_RETURNED_DATE_CREATED");
                itemObj.setClaimsReturnedFlagCreateDate(convertDateFormat(claimsReturnFlagCreatedDate));
                itemObj.setClaimsReturnedNote(resultSet.getString("CLAIMS_RETURNED_NOTE"));
                itemObj.setCurrentBorrower(resultSet.getString("CURRENT_BORROWER"));
                itemObj.setProxyBorrower(resultSet.getString("PROXY_BORROWER"));
                String dueDateTime = resultSet.getString("DUE_DATE_TIME");
                itemObj.setDueDateTime(convertDateFormat(dueDateTime));
                String originalDueDate = resultSet.getString("ORG_DUE_DATE_TIME");
                itemObj.setOriginalDueDate(convertDateFormat(originalDueDate));
                String checkOutDateTime = resultSet.getString("CHECK_OUT_DATE_TIME");
                itemObj.setCheckOutDateTime(convertDateFormat(checkOutDateTime));
                itemObj.setDamagedItemNote(resultSet.getString("ITEM_DAMAGED_NOTE"));
                if (resultSet.getString("ITEM_DAMAGED_STATUS") != null) {
                    itemObj.setItemDamagedStatus(resultSet.getString("ITEM_DAMAGED_STATUS").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE);
                }
                if(resultSet.getString("MISSING_PIECES") !=null) {
                    itemObj.setMissingPieceFlag(resultSet.getString("MISSING_PIECES").equalsIgnoreCase("Y") ? Boolean.TRUE : Boolean.FALSE);
                }
                itemObj.setMissingPiecesCount(resultSet.getString("MISSING_PIECES_COUNT"));
                itemObj.setMissingPieceFlagNote(resultSet.getString("MISSING_PIECES_NOTE"));
                itemObj.setMissingPieceEffectiveDate(resultSet.getString("MISSING_PIECES_EFFECTIVE_DATE"));
                itemObj.setNumberOfPieces(resultSet.getString("NUM_PIECES"));
                itemObj.setDescriptionOfPieces(resultSet.getString("DESC_OF_PIECES"));
                itemObj.setNumberOfRenew(resultSet.getInt("NUM_OF_RENEW"));
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
        }
        resultSet.close();
        return itemList;
    }

    public String convertDateFormat(String date) {
        String convertedDate = "";
        if (date != null && !date.isEmpty()) {
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date itemDate = null;
            try {
                itemDate = format2.parse(date);
            } catch (ParseException e) {
                LOG.error("format string to Date " + e);
            }
            convertedDate = format1.format(itemDate).toString();
        }
        return convertedDate;
    }


    public String getTotalNoOfRecords() throws SQLException {
        String totalRecords = "0";
        Connection connection = getConnection();
        PreparedStatement preparedStatement =null;
        if(isStaffOnly){
             preparedStatement = connection.prepareStatement(bibCountQuery);
        }else{
            preparedStatement = connection.prepareStatement(bibCountQuery+staffOnly);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            totalRecords = resultSet.getString("totalRecords");
        }
        preparedStatement.close();
        resultSet.close();
        connection.close();
        return totalRecords;
    }

    public static void writeStatusToFile(String directoryPath, String fileName, String content) {
        try {
            String fileSeparator = File.separator;
            Date date = new Date();
            FileWriter fw = new FileWriter(directoryPath + fileSeparator + fileName, true);
            fw.write("\n");
            fw.write("******************************************************************");
            fw.write("\n");
            fw.write(date.toString());
            fw.write("\n");
            fw.write(content);
            fw.write("\n");
            fw.write("******************************************************************");
            fw.write("\n");
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    public static void writeStatusToFile(String directoryPath, String fileName, String content, String first, String last) {
        try {
            String fileSeparator = File.separator;
            FileWriter fw = new FileWriter(directoryPath + fileSeparator + fileName, true);
            fw.write("\n");
            fw.write("******************************************************************");
            fw.write("\n");
            fw.write(content);
            fw.write("\n");
            fw.write("Batch start id :" + first);
            fw.write("\n");
            fw.write("Batch end id :" + last);
            fw.write("\n");
            fw.write("******************************************************************");
            fw.write("\n");
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }


    public int storeBibInfo(int batchSize, String filePath, String fileName, BibInfoStatistics bibInfoStatistics, int batchNo) throws SQLException {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int count = 0;
        BibMarcUtil bibMarcUtil = new BibMarcUtil();

        while (bibResultSet.next()) {
            count++;

            int bibId = 0;
            int bibId2 = 0;
            String bibIdStr = "";


            try {

                bibId = bibResultSet.getInt("BIB_ID");
                bibId2 = bibResultSet.getInt("BIB_ID");
                bibIdStr = bibResultSet.getString("UNIQUE_ID_PREFIX") + "-" + bibId;

                if (bibId != bibId2) {
                    LOG.error("bibId is not equal to bibId2: bibId = " + bibId + "; bibId2 = " + bibId2);
                }


                BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(bibResultSet.getString("CONTENT"));

                if (bibMarcRecords != null && bibMarcRecords.getRecords() != null && bibMarcRecords.getRecords().size() > 0) {

                    Map<String, String> dataFields = bibMarcUtil.buildDataValuesForBibInfo(bibMarcRecords.getRecords().get(0));
                    String title = dataFields.get(BibMarcUtil.TITLE_DISPLAY);
                    String author = dataFields.get(BibMarcUtil.AUTHOR_DISPLAY);
                    String publisher = dataFields.get(BibMarcUtil.PUBLISHER_DISPLAY);
                    String isbn = dataFields.get(BibMarcUtil.ISBN_DISPLAY);
                    String issn = dataFields.get(BibMarcUtil.ISSN_DISPLAY);

                    String commonIdentifier = "";
                    if (StringUtils.isNotEmpty(isbn)) {
                        commonIdentifier = isbn;
                    } else {
                        commonIdentifier = issn;
                    }

                    bibInsertPreparedStatement.setInt(1, bibId);
                    bibInsertPreparedStatement.setString(2, bibIdStr);
                    bibInsertPreparedStatement.setString(3, truncateData(title, 4000));
                    bibInsertPreparedStatement.setString(4, truncateData(author, 4000));
                    bibInsertPreparedStatement.setString(5, truncateData(publisher, 4000));
                    bibInsertPreparedStatement.setString(6, truncateData(commonIdentifier, 100));


                    try {
                        bibInsertPreparedStatement.executeUpdate();
                    } catch (Exception e) {
                        if (e.getMessage().startsWith("Duplicate entry")) {

                            bibUpdatePreparedStatement.setString(1, truncateData(title, 4000));
                            bibUpdatePreparedStatement.setString(2, truncateData(author, 4000));
                            bibUpdatePreparedStatement.setString(3, truncateData(publisher, 4000));
                            bibUpdatePreparedStatement.setString(4, truncateData(commonIdentifier, 100));
                            bibUpdatePreparedStatement.setString(5, bibIdStr);
                            bibUpdatePreparedStatement.setInt(6, bibId);
                            try {
                                bibUpdatePreparedStatement.executeUpdate();
                            } catch (Exception e1) {
                                LOG.error("Exception while updating into BIB_INFO_T, BibId = " + bibId + " BibIdStr = " + bibIdStr + " : ", e1);
                                writeStatusToFile(filePath, fileName, "Exception while updating into BIB_INFO_T, BibId = " + bibId + " BibIdStr = " + bibIdStr + " : " + e1.getMessage());
                            }
                        } else {
                            LOG.error("Exception while inserting into BIB_INFO_T, BibId = " + bibId + " BibIdStr = " + bibIdStr + " : ", e);
                            writeStatusToFile(filePath, fileName, "Exception while inserting into BIB_INFO_T, BibId = " + bibId + " BibIdStr = " + bibIdStr + " : " + e.getMessage());
                        }
                    }
                }

            } catch (Exception e) {
                LOG.error("Exception inserting/updating bibId " + bibId + "; bibId2 = " + bibId2 + " BibIdStr = " + bibIdStr, e);
                writeStatusToFile(filePath, fileName, "Exception inserting/updating bibId " + bibId + "; bibId2 = " + bibId2 + " BibIdStr = " + bibIdStr + "\t" + e.getMessage());
            }
            bibInfoStatistics.setBibCount((batchSize * batchNo) + count);
            if (count == batchSize) {
                break;
            }
        }
        stopWatch.stop();
        connection.commit();
        return count;
    }

    public static String truncateData(String data, int idLength) {
        //TODO: Handle the case of unicode chars where string.length() <> byte length
        String truncateData = data;
        if (data != null && data.length() > idLength) {
            truncateData = data.substring(0, (idLength-1));
        }
        return truncateData;
    }

}