package org.kuali.ole.select.document.service.impl;

import org.apache.commons.lang.time.StopWatch;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.DateFormatHelper;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.kuali.rice.krad.util.GlobalVariables;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by premkb on 9/7/15.
 */
public class ReceivingQueueDAOServiceimpl extends PlatformAwareDaoBaseJdbc implements org.kuali.ole.select.document.service.ReceivingQueueDAOService{

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ReceivingQueueDAOServiceimpl.class);
    private String dbVendor = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.DB_VENDOR);


    @Override
    public List<Map<String, Object>> getPODetails(Map<String,Object> criteria) {
        String query = "SELECT PO.FDOC_NBR AS FDOC_NBR,PO.PO_ID AS PO_ID,PO.VNDR_NM AS VNDR_NM ,DHR.DOC_HDR_STAT_CD AS DOC_HDR_STAT_CD,POITM.OLE_DOCUMENT_UUID as BIBID " +
                ",PO.PO_CRTE_DT AS PO_CRTE_DT,POITM.PO_ITM_ID AS PO_ITM_ID,POITM.ITM_UNIT_PRC AS ITM_UNIT_PRC,POITM.ITM_ORD_QTY AS ITM_ORD_QTY,POITM.OLE_DNT_CLM AS OLE_DNT_CLM" +
                ",POITM.ITM_DESC AS ITM_DESC,POITM.OLE_NUM_PRTS AS OLE_NUM_PRTS,POITM.OLE_CLM_DT AS OLE_CLM_DT " +
                ",POITM.OLE_REQ_RCPT_STATUS_ID AS OLE_REQ_RCPT_STATUS_ID,POITM.OLE_FOR_UNT_CST AS OLE_FOR_UNT_CST" +
                ",(SELECT OLE_REQ_RCPT_STATUS_CD FROM OLE_PUR_REQ_RCPT_STATUS_T WHERE OLE_REQ_RCPT_STATUS_ID=POITM.OLE_REQ_RCPT_STATUS_ID AND OLE_REQ_RCPT_STATUS_DOC_TYP='PO')AS REPSTATCD " +
                ",POITM.OLE_NO_COPIES_RCVD AS OLE_NO_COPIES_RCVD,POITM.OLE_NO_PARTS_RCVD AS OLE_NO_PARTS_RCVD" +
                ",(SELECT TITLE FROM  OLE_E_RES_REC_T WHERE E_RES_REC_ID=COPY.E_RES_REC_ID)AS TITLE " +
                ",COPY.SER_RCV_ID AS RECEIVINGID, POTYP.OLE_PO_TYPE AS POTYPE "+
                "FROM PUR_PO_T PO, KREW_DOC_HDR_T DHR,OLE_PUR_PO_TYP_T POTYP,PUR_PO_ITM_T POITM" +
                ",(SELECT DISTINCT PO_ITM_ID,E_RES_REC_ID,SER_RCV_ID FROM OLE_COPY_T) COPY " +
                "WHERE PO.FDOC_NBR=DHR.DOC_HDR_ID AND PO.OLE_PO_TYPE_ID=POTYP.OLE_PO_TYPE_ID AND PO.FDOC_NBR=POITM.FDOC_NBR AND COPY.PO_ITM_ID=POITM.PO_ITM_ID " +
                "AND PO.FDOC_NBR NOT IN (SELECT NOTE.FDOC_NBR FROM OLE_PUR_PO_ITM_NTE_T NOTE,OLE_NTE_TYP_T NOTETYPE WHERE NOTETYPE.OLE_NTE_TYP_ID=NOTE.OLE_NTE_TYP_ID " +
                "AND NOTETYPE.OLE_NTE_TYPE='Special Processing Instruction Note') " +
                "AND PO.PO_ID NOT IN (SELECT PO_ID FROM PUR_RCVNG_LN_T RCV,KREW_DOC_HDR_T RDHR WHERE RCV.PO_ID=PO.PO_ID AND RCV.FDOC_NBR=RDHR.DOC_HDR_ID AND RDHR.DOC_HDR_STAT_CD NOT IN ('E','X','F')) " +
                "AND POITM.ITM_TYP_CD='ITEM' " +
                "AND PO.PO_CUR_IND='Y' " +
                "AND POITM.ITM_ACTV_IND='Y' " +
                getQueryCriteriaString(criteria,"bibIds")+
                getQueryCriteriaString(criteria,"purchaseOrderNumber")+
                getQueryCriteriaString(criteria,"purchaseOrderStatus")+
                getQueryCriteriaString(criteria,"vendorName")+
                getQueryCriteriaString(criteria,"purchaseOrderType")+
                getQueryCriteriaString(criteria,"poCreateFromDate")+
                getQueryCriteriaString(criteria,"poCreateToDate")+
                getQueryCriteriaString(criteria,"claimFilter")+
                getQueryCriteriaString(criteria,"title")+
                getResultSetLimit();
        if (LOG.isInfoEnabled()) {
            LOG.info("receiving claiming query ----->"+query);
        }
        return getSimpleJdbcTemplate().queryForList(query);
    }

    private String getQueryCriteriaString(Map<String,Object> criteria,String criteriaString){
        String queryCriteriaString = "";
        if((criteriaString.equals("bibIds")&&criteria.get("bibIds")!=null)){
            queryCriteriaString="AND POITM.OLE_DOCUMENT_UUID IN ('"+criteria.get("bibIds").toString()+"') ";
        }
        if(criteriaString.equals("purchaseOrderNumber")&&criteria.get("purchaseOrderNumber")!=null){
            queryCriteriaString="AND PO.PO_ID IN ('"+criteria.get("purchaseOrderNumber").toString().replaceAll(",","','")+"') ";
        }
        if(criteriaString.equals("purchaseOrderStatus")&&criteria.get("purchaseOrderStatus")!=null){
            queryCriteriaString="AND DHR.APP_DOC_STAT='"+criteria.get("purchaseOrderStatus").toString()+"' ";
        }else if(criteriaString.equals("purchaseOrderStatus")){//To add app doc status only once to the query
            queryCriteriaString="AND DHR.APP_DOC_STAT='"+ PurapConstants.PurchaseOrderStatuses.APPDOC_OPEN+"' ";
        }
        if(criteriaString.equals("vendorName")&&criteria.get("vendorName")!=null){
            queryCriteriaString="AND PO.VNDR_NM='"+criteria.get("vendorName").toString()+"' ";
        }
        if(criteriaString.equals("purchaseOrderType")&&criteria.get("purchaseOrderType")!=null){
            queryCriteriaString="AND PO.OLE_PO_TYPE_ID=(SELECT OLE_PO_TYPE_ID FROM OLE_PUR_PO_TYP_T WHERE OLE_PO_TYPE='"+criteria.get("purchaseOrderType").toString()+"') ";
        }
        if(criteriaString.equals("poCreateFromDate")&&criteria.get("poCreateFromDate")!=null){
            queryCriteriaString="AND PO.PO_CRTE_DT >=(DATE '"+criteria.get("poCreateFromDate").toString()+"') ";
        }
        if(criteriaString.equals("poCreateToDate")&&criteria.get("poCreateToDate")!=null){
            queryCriteriaString="AND PO.PO_CRTE_DT <=(DATE '"+criteria.get("poCreateToDate").toString()+"') ";
        }
        if(criteriaString.equals("claimFilter")&&criteria.get("claimFilter")!=null&&(boolean)criteria.get("claimFilter")){
            queryCriteriaString="AND POITM.OLE_CLM_DT <=(DATE '"+getFormattedCurrentDate()+"') AND POITM.OLE_DNT_CLM='N' ";
        }
        if(criteriaString.equals("title")&&criteria.get("title")!=null){
            queryCriteriaString=" AND COPY.E_RES_REC_ID IN (SELECT E_RES_REC_ID FROM OLE_E_RES_REC_T WHERE TITLE='"+criteria.get("title").toString()+"' ) ";
        }
        return queryCriteriaString;
    }

    private String getResultSetLimit(){

        String queryCriteriaString="";
        if(dbVendor.equals(OLEConstants.MYSQL)){
            queryCriteriaString=" LIMIT 1000";
        }else{
            queryCriteriaString=" AND ROWNUM <=1000";
        }
        return queryCriteriaString;
    }
    @Override
    public List<OlePurchaseOrderDocument> getPODocumentList(Map<String, Object> criteria) {
        List<Map<String, Object>> resultSets=null;
        StopWatch executeQueryTimeWatch = new StopWatch();
        executeQueryTimeWatch.start();
        resultSets=getPODetails(criteria);
        executeQueryTimeWatch.stop();
        List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList= new ArrayList<>();
        olePurchaseOrderDocumentList=buildPODocumentWithPOItem(resultSets);
        return olePurchaseOrderDocumentList;
    }


    private List<OlePurchaseOrderDocument> buildPODocumentWithPOItem(List<Map<String, Object>> resultSets){
        List<OlePurchaseOrderDocument> olePurchaseOrderDocumentList=new ArrayList<>();
        boolean isContinuing=false;
        for (Map<String, Object> resultSet:resultSets) {
            OlePurchaseOrderDocument olePurchaseOrderDocument=new OlePurchaseOrderDocument();
            List<OlePurchaseOrderItem> olePurchaseOrderItemList=new ArrayList<>();
            olePurchaseOrderDocument.setDocumentNumber(resultSet.get("FDOC_NBR").toString());
            olePurchaseOrderDocument.setVendorName(resultSet.get("VNDR_NM").toString());
            olePurchaseOrderDocument.setPurapDocumentIdentifier(Integer.parseInt(resultSet.get("PO_ID").toString()));
            if (resultSet.get("POTYPE")!=null){
                isContinuing=resultSet.get("POTYPE").toString().equalsIgnoreCase("Continuing")?true:false;
            }
            OlePurchaseOrderItem olePurchaseOrderItem = new OlePurchaseOrderItem();
            olePurchaseOrderItem.setItemIdentifier(Integer.parseInt(resultSet.get("PO_ITM_ID").toString()));
            if (resultSet.get("BIBID")!=null) {//For EResoruce bibid will be null
                olePurchaseOrderItem.setItemTitleId(resultSet.get("BIBID").toString());
                olePurchaseOrderItem.setBibUUID(resultSet.get("BIBID").toString());
            }
            if (resultSet.get("TITLE")!=null) {
                DocData docData=new DocData();
                docData.setTitle(resultSet.get("TITLE").toString());
                olePurchaseOrderItem.setDocData(docData);
            }
            if (resultSet.get("ITM_DESC")!=null) {
                olePurchaseOrderItem.setItemDescription(resultSet.get("ITM_DESC").toString());
            }
            if (resultSet.get("OLE_DNT_CLM")!=null) {
                olePurchaseOrderItem.setDoNotClaim(Boolean.parseBoolean(resultSet.get("OLE_DNT_CLM").toString()));
            }
            if (resultSet.get("ITM_UNIT_PRC")!=null) {
                olePurchaseOrderItem.setItemUnitPrice(new BigDecimal(resultSet.get("ITM_UNIT_PRC").toString()));
            }
            if (resultSet.get("OLE_NUM_PRTS")!=null) {
                olePurchaseOrderItem.setItemNoOfParts(new KualiInteger(new Double(resultSet.get("OLE_NUM_PRTS").toString()).intValue()));
            }
            if (resultSet.get("OLE_CLM_DT")!=null){
                olePurchaseOrderItem.setClaimDate(getSqlDate(resultSet.get("OLE_CLM_DT").toString()));
            }
            if (resultSet.get("OLE_NO_COPIES_RCVD")!=null) {
                olePurchaseOrderItem.setNoOfCopiesReceived(resultSet.get("OLE_NO_COPIES_RCVD").toString());
            }
            if (resultSet.get("OLE_NO_PARTS_RCVD")!=null) {
                olePurchaseOrderItem.setNoOfPartsReceived(resultSet.get("OLE_NO_PARTS_RCVD").toString());
            }
            if(resultSet.get("OLE_REQ_RCPT_STATUS_ID")!=null){
                olePurchaseOrderItem.setReceiptStatusId(Integer.parseInt(resultSet.get("OLE_REQ_RCPT_STATUS_ID").toString()));
            }
            if (resultSet.get("OLE_FOR_UNT_CST")!=null){
                olePurchaseOrderItem.setItemForeignUnitCost(new KualiDecimal(new Double(resultSet.get("OLE_FOR_UNT_CST").toString()).intValue()));
            }
            if (resultSet.get("RECEIVINGID")!=null){
                List<OleCopy> copyList=new ArrayList<>();
                OleCopy oleCopy=new OleCopy();
                oleCopy.setSerialReceivingIdentifier(resultSet.get("RECEIVINGID").toString());
                copyList.add(oleCopy);
                olePurchaseOrderItem.setCopyList(copyList);
            }
            if(resultSet.get("REPSTATCD")!=null){
                if(resultSet.get("REPSTATCD").toString().equals(org.kuali.ole.sys.OLEConstants.PO_RECEIPT_STATUS_FULLY_RECEIVED)){
                    GlobalVariables.clear();
                    GlobalVariables.getMessageMap().putInfo(OleSelectConstant.RECEIVING_QUEUE_SEARCH,
                            OLEKeyConstants.ERROR_NO_PURCHASEORDERS_FOUND_FOR_FULLY_RECEIVED);
                }
            }
            setClaimFilter(olePurchaseOrderItem,isContinuing);
            olePurchaseOrderItem.setItemPoQty(new KualiInteger(new Double(resultSet.get("ITM_ORD_QTY").toString()).intValue()));
            olePurchaseOrderItemList.add(olePurchaseOrderItem);
            olePurchaseOrderDocument.setOlePurchaseOrderItemList(olePurchaseOrderItemList);
            olePurchaseOrderDocument.setItems(olePurchaseOrderItemList);
            olePurchaseOrderItem.setPurchaseOrder(olePurchaseOrderDocument);
            olePurchaseOrderItem.setOlePurchaseOrderDocument(olePurchaseOrderDocument);
            olePurchaseOrderDocumentList.add(olePurchaseOrderDocument);
        }
        return olePurchaseOrderDocumentList;
    }

    private String getFormattedCurrentDate(){
        String currentDate=null;
        SimpleDateFormat simpleDateFormat = null;
        String outputDateFormat = "yyyy-MM-dd";
        simpleDateFormat = new SimpleDateFormat(outputDateFormat);
        currentDate = simpleDateFormat.format(new Date());
        return currentDate;
    }

    private java.sql.Date getSqlDate(String stringDate){
        java.sql.Date sqlDate=null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
            Date parsedDate = simpleDateFormat.parse(stringDate);
            sqlDate = new java.sql.Date(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sqlDate;
    }

    private void setClaimFilter(OlePurchaseOrderItem olePurchaseOrderItem,boolean isContinuing){
        boolean serialPOLink = olePurchaseOrderItem.getCopyList() != null && olePurchaseOrderItem.getCopyList().size() > 0 ? olePurchaseOrderItem.getCopyList().get(0).getSerialReceivingIdentifier() != null : false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
        String dateString = dateFormat.format(new Date());
        String actionDateString = olePurchaseOrderItem.getClaimDate() != null ? dateFormat.format(olePurchaseOrderItem.getClaimDate()) : "";
        if (!olePurchaseOrderItem.isDoNotClaim() && olePurchaseOrderItem.getClaimDate() != null && (actionDateString.equalsIgnoreCase(dateString) || olePurchaseOrderItem.getClaimDate().before(new Date()))
                && !serialPOLink && !isContinuing) {
            olePurchaseOrderItem.setClaimFilter(true);
        }
    }
}

