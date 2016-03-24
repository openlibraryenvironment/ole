package org.kuali.ole.select.document.service.impl;

import org.apache.commons.lang.time.StopWatch;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.document.service.DonorEncumberReportDAOService;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.framework.persistence.jdbc.dao.PlatformAwareDaoBaseJdbc;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by premkb on 3/15/16.
 */
public class DonorEncumberReportDAOServiceImpl extends PlatformAwareDaoBaseJdbc implements DonorEncumberReportDAOService {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DonorEncumberReportDAOServiceImpl.class);
    private String dbVendor = ConfigContext.getCurrentContextConfig().getProperty(OLEConstants.DB_VENDOR);

    public List<OLEDonor> getDonorEncumberList(Map<String, Object> criteria){
        List<OLEDonor> oleDonorList=null;
        List<Map<String, Object>> resultSets=null;
        StopWatch executeQueryTimeWatch = new StopWatch();
        executeQueryTimeWatch.start();
        resultSets=getDonorEncumberResultset(criteria);
        oleDonorList=buildPODocumentWithPOItem(resultSets);
        return oleDonorList;
    }

    public List<Map<String, Object>> getDonorEncumberResultset(Map<String,Object> criteria){
        String query ="SELECT DONORID,DONOR_CODE,PONUM,DOCID,DONOR_NOTE,DONOR_AMT,(ENCUM/DONOR_COUNT) AS ENCUMBERAMT,(EXPENSE/DONOR_COUNT) AS EXPENSEDAMT \n" +
                "FROM \n" +
                "(SELECT \n" +
                "DNR.DONOR_ID AS DONORID,PUR_DNR.DONOR_CODE AS DONOR_CODE, PO.PO_ID AS PONUM,PO.FDOC_NBR AS DOCID, DNR.DONOR_NOTE AS DONOR_NOTE, DNR.DONOR_AMT AS DONOR_AMT\n" +
                ",(SELECT COUNT(*) FROM OLE_LINK_PURAP_DONOR_T PUR_DNR_TEMP WHERE POITM.PO_ITM_ID=PUR_DNR_TEMP.PO_ITM_ID GROUP BY PUR_DNR_TEMP.PO_ITM_ID) AS DONOR_COUNT\n" +
                ", POITM.ITM_OSTND_ENC_AMT AS ENCUM,POITM.ITM_INV_TOT_AMT AS EXPENSE\n" +
                "FROM OLE_DONOR_T DNR, OLE_LINK_PURAP_DONOR_T PUR_DNR, PUR_PO_ITM_T POITM, PUR_PO_T PO, KREW_DOC_HDR_T DOCHDRT\n" +
                "WHERE \n" +
                "DNR.DONOR_ID=PUR_DNR.DONOR_ID AND \n" +
                "PUR_DNR.PO_ITM_ID=POITM.PO_ITM_ID AND\n" +
                " PO.FDOC_NBR=POITM.FDOC_NBR AND " +
                " DOCHDRT.DOC_HDR_ID=PO.FDOC_NBR AND\n" +
                " DOCHDRT.APP_DOC_STAT NOT IN ('"+PurapConstants.PurchaseOrderStatuses.APPDOC_RETIRED_VERSION+"') " +
                getQueryCriteriaString(criteria,OleSelectConstant.EncumberReportConstant.DONORCODE)+
                getQueryCriteriaString(criteria,OleSelectConstant.EncumberReportConstant.FROM_DATE)+
                getQueryCriteriaString(criteria,OleSelectConstant.EncumberReportConstant.TO_DATE)+
                getResultSetLimit()+
                ")  DONOR_DATA ORDER BY DONOR_CODE ASC";
        if(LOG.isInfoEnabled()){
            LOG.info("Donor encumber query----->" + query);
        }
        return getSimpleJdbcTemplate().queryForList(query);
    }

    private List<OLEDonor> buildPODocumentWithPOItem(List<Map<String, Object>> resultSets){
        List<OLEDonor> oleDonorList=new ArrayList<>();
        for (Map<String, Object> resultSet:resultSets) {
            OLEDonor oleDonor=new OLEDonor();
            if (resultSet.get(OleSelectConstant.EncumberReportConstant.DONORID)!=null){
                oleDonor.setDonorId(resultSet.get(OleSelectConstant.EncumberReportConstant.DONORID).toString());
            }
            if (resultSet.get(OleSelectConstant.EncumberReportConstant.DONOR_CODE)!=null){
                oleDonor.setDonorCode(resultSet.get(OleSelectConstant.EncumberReportConstant.DONOR_CODE).toString());
            }
            if (resultSet.get(OleSelectConstant.EncumberReportConstant.PONUM)!=null){
                oleDonor.setPoNumber(resultSet.get(OleSelectConstant.EncumberReportConstant.PONUM).toString());
            }
            if (resultSet.get(OleSelectConstant.EncumberReportConstant.DONOR_NOTE)!=null){
                oleDonor.setDonorNote(resultSet.get(OleSelectConstant.EncumberReportConstant.DONOR_NOTE).toString());
            }
            if (resultSet.get(OleSelectConstant.EncumberReportConstant.DONOR_AMT)!=null){
                oleDonor.setDonorAmount(new KualiDecimal(resultSet.get(OleSelectConstant.EncumberReportConstant.DONOR_AMT).toString()));
            }
            if (resultSet.get(OleSelectConstant.EncumberReportConstant.ENCUMBERAMT)!=null){
                oleDonor.setEncumberedAmount(new KualiDecimal(resultSet.get(OleSelectConstant.EncumberReportConstant.ENCUMBERAMT).toString()));
            }
            if (resultSet.get(OleSelectConstant.EncumberReportConstant.EXPENSEDAMT)!=null){
                oleDonor.setExpensedAmount(new KualiDecimal(resultSet.get(OleSelectConstant.EncumberReportConstant.EXPENSEDAMT).toString()));
            }
            if (resultSet.get(OleSelectConstant.EncumberReportConstant.DOCID)!=null){
                oleDonor.setPoLinkUrl(ConfigContext.getCurrentContextConfig().getProperty("application.url") + "/kew/DocHandler.do?command=displayDocSearchView&docId=" + resultSet.get(OleSelectConstant.EncumberReportConstant.DOCID).toString());
            }
            oleDonorList.add(oleDonor);
        }
        return oleDonorList;
    }

    private String getQueryCriteriaString(Map<String,Object> criteria,String criteriaString){
        String queryCriteriaString = "";
        if((criteriaString.equals(OleSelectConstant.EncumberReportConstant.DONORCODE)&&!criteria.get(OleSelectConstant.EncumberReportConstant.DONORCODE).toString().isEmpty())){
            queryCriteriaString=" AND PUR_DNR.DONOR_CODE= '"+criteria.get(OleSelectConstant.EncumberReportConstant.DONORCODE).toString()+"' ";
        }
        if((criteriaString.equals(OleSelectConstant.EncumberReportConstant.FROM_DATE)&&criteria.get(OleSelectConstant.EncumberReportConstant.FROM_DATE)!=null)){
            queryCriteriaString=" AND PO.PO_CRTE_DT >=(DATE '"+criteria.get(OleSelectConstant.EncumberReportConstant.FROM_DATE).toString()+"') ";
        }
        if((criteriaString.equals(OleSelectConstant.EncumberReportConstant.TO_DATE)&&criteria.get(OleSelectConstant.EncumberReportConstant.TO_DATE)!=null)){
            queryCriteriaString=" AND PO.PO_CRTE_DT <=(DATE '"+criteria.get(OleSelectConstant.EncumberReportConstant.TO_DATE).toString()+"') ";
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
}
