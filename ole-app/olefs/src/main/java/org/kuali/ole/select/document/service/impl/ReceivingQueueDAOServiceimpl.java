package org.kuali.ole.select.document.service.impl;

import org.apache.commons.lang.time.StopWatch;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.service.DateFormatHelper;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.kuali.rice.krad.util.GlobalVariables;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by premkb on 9/7/15.
 */
public class ReceivingQueueDAOServiceimpl extends PlatformAwareDaoBaseJdbc implements org.kuali.ole.select.document.service.ReceivingQueueDAOService{

    private String dbVendor = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.DB_VENDOR);


    @Override
    public List<Map<String, Object>> getPODetails(Map<String,Object> criteria) {
        String query = "SELECT PO.FDOC_NBR AS FDOC_NBR,PO.PO_ID AS PO_ID,PO.VNDR_NM AS VNDR_NM ,DHR.DOC_HDR_STAT_CD AS DOC_HDR_STAT_CD,POITM.OLE_DOCUMENT_UUID as BIBID " +
                ",PO.PO_CRTE_DT AS PO_CRTE_DT,POITM.PO_ITM_ID AS PO_ITM_ID,POITM.ITM_UNIT_PRC AS ITM_UNIT_PRC,POITM.ITM_ORD_QTY AS ITM_ORD_QTY,POITM.OLE_DNT_CLM AS OLE_DNT_CLM" +
                ",POITM.ITM_DESC AS ITM_DESC,POITM.OLE_NUM_PRTS AS OLE_NUM_PRTS,POITM.OLE_CLM_DT AS OLE_CLM_DT " +
                ",POITM.OLE_REQ_RCPT_STATUS_ID AS OLE_REQ_RCPT_STATUS_ID,(SELECT OLE_REQ_RCPT_STATUS_CD FROM OLE_PUR_REQ_RCPT_STATUS_T WHERE OLE_REQ_RCPT_STATUS_ID=POITM.OLE_REQ_RCPT_STATUS_ID AND OLE_REQ_RCPT_STATUS_DOC_TYP='PO')AS REPSTATCD " +
                ",POITM.OLE_NO_COPIES_RCVD AS OLE_NO_COPIES_RCVD,POITM.OLE_NO_PARTS_RCVD AS OLE_NO_PARTS_RCVD "+
                "FROM PUR_PO_T PO, KREW_DOC_HDR_T DHR,OLE_PUR_PO_TYP_T POTYP,PUR_PO_ITM_T POITM " +
                "WHERE PO.FDOC_NBR=DHR.DOC_HDR_ID AND PO.OLE_PO_TYPE_ID=POTYP.OLE_PO_TYPE_ID AND PO.FDOC_NBR=POITM.FDOC_NBR  " +
                "AND PO.FDOC_NBR NOT IN (SELECT NOTE.FDOC_NBR FROM OLE_PUR_PO_ITM_NTE_T NOTE,OLE_NTE_TYP_T NOTETYPE WHERE NOTETYPE.OLE_NTE_TYP_ID=NOTE.OLE_NTE_TYP_ID " +
                "AND NOTETYPE.OLE_NTE_TYPE='Special Processing Instruction Note') " +
                "AND POITM.ITM_TYP_CD='ITEM' " +
                "AND PO.PO_CUR_IND='Y' " +
                getQueryCriteriaString(criteria,"bibIds")+
                getQueryCriteriaString(criteria,"purchaseOrderNumber")+
                getQueryCriteriaString(criteria,"purchaseOrderStatus")+
                getQueryCriteriaString(criteria,"vendorName")+
                getQueryCriteriaString(criteria,"purchaseOrderType")+
                getQueryCriteriaString(criteria,"poCreateFromDate")+
                getQueryCriteriaString(criteria,"poCreateToDate")+
                getQueryCriteriaString(criteria,"claimFilter")+
                getResultSetLimit();
                //" LIMIT 1000";
                //+"AND PO.VNDR_NM='"+vendorName+"'";
        System.out.println("query ----->"+query);
        return getSimpleJdbcTemplate().queryForList(query);
    }

    private String getQueryCriteriaString(Map<String,Object> criteria,String criteriaString){
        String queryCriteriaString = "";
        if((criteriaString.equals("bibIds")&&criteria.get("bibIds")!=null)){
            queryCriteriaString="AND POITM.OLE_DOCUMENT_UUID IN ('"+criteria.get("bibIds").toString()+"') ";
        }
        if(criteriaString.equals("purchaseOrderNumber")&&criteria.get("purchaseOrderNumber")!=null){
            queryCriteriaString="AND PO.PO_ID='"+criteria.get("purchaseOrderNumber").toString()+"' ";
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
        //OlePurchaseOrderDocument olePurchaseOrderDocument=null;
        StopWatch poBuildWatch = new StopWatch();
        poBuildWatch.start();
        for (Map<String, Object> resultSet:resultSets) {
            OlePurchaseOrderDocument olePurchaseOrderDocument=new OlePurchaseOrderDocument();
            List<OlePurchaseOrderItem> olePurchaseOrderItemList=new ArrayList<>();
            olePurchaseOrderDocument.setDocumentNumber(resultSet.get("FDOC_NBR").toString());
            olePurchaseOrderDocument.setVendorName(resultSet.get("VNDR_NM").toString());
            olePurchaseOrderDocument.setPurapDocumentIdentifier(Integer.parseInt(resultSet.get("PO_ID").toString()));
            OlePurchaseOrderItem olePurchaseOrderItem = new OlePurchaseOrderItem();
            olePurchaseOrderItem.setItemIdentifier(Integer.parseInt(resultSet.get("PO_ITM_ID").toString()));
            olePurchaseOrderItem.setItemTitleId(resultSet.get("BIBID").toString());
            olePurchaseOrderItem.setItemDescription(resultSet.get("ITM_DESC").toString());
            olePurchaseOrderItem.setDoNotClaim(Boolean.parseBoolean(resultSet.get("OLE_DNT_CLM").toString()));
            //olePurchaseOrderItem.setClaimDate();
            if (resultSet.get("ITM_UNIT_PRC")!=null) {
                olePurchaseOrderItem.setItemUnitPrice(new BigDecimal(resultSet.get("ITM_UNIT_PRC").toString()));
            }
            if (resultSet.get("OLE_NUM_PRTS")!=null) {
                olePurchaseOrderItem.setItemNoOfParts(new KualiInteger(new Double(resultSet.get("OLE_NUM_PRTS").toString()).intValue()));
            }
            if (resultSet.get("OLE_NO_COPIES_RCVD")!=null) {
                olePurchaseOrderItem.setNoOfCopiesReceived(resultSet.get("OLE_NO_COPIES_RCVD").toString());
            }
            if (resultSet.get("OLE_NO_PARTS_RCVD")!=null) {
                olePurchaseOrderItem.setNoOfPartsReceived(resultSet.get("OLE_NO_PARTS_RCVD").toString());
            }
            if(resultSet.get("OLE_REQ_RCPT_STATUS_ID")!=null){
                olePurchaseOrderItem.setReceiptStatusId(Integer.parseInt(resultSet.get("OLE_REQ_RCPT_STATUS_ID").toString()));
                if(resultSet.get("REPSTATCD").toString().equals(org.kuali.ole.sys.OLEConstants.PO_RECEIPT_STATUS_FULLY_RECEIVED)){
                    GlobalVariables.clear();
                    GlobalVariables.getMessageMap().putInfo(OleSelectConstant.RECEIVING_QUEUE_SEARCH,
                            OLEKeyConstants.ERROR_NO_PURCHASEORDERS_FOUND_FOR_FULLY_RECEIVED);
                }
            }
            olePurchaseOrderItem.setItemPoQty(new KualiInteger(new Double(resultSet.get("ITM_ORD_QTY").toString()).intValue()));
            olePurchaseOrderItem.setPoAdded(true);
            //olePurchaseOrderItem.setItemQuantity(new KualiDecimal(resultSet.get("ITM_ORD_QTY").toString()));
            olePurchaseOrderItemList.add(olePurchaseOrderItem);
            olePurchaseOrderDocument.setOlePurchaseOrderItemList(olePurchaseOrderItemList);
            olePurchaseOrderDocument.setItems(olePurchaseOrderItemList);
            olePurchaseOrderItem.setPurchaseOrder(olePurchaseOrderDocument);
            olePurchaseOrderItem.setOlePurchaseOrderDocument(olePurchaseOrderDocument);
            olePurchaseOrderDocumentList.add(olePurchaseOrderDocument);
        }
        poBuildWatch.stop();
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
}
