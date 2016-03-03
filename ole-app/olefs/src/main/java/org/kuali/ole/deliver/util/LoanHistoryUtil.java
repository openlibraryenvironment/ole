package org.kuali.ole.deliver.util;

import org.apache.log4j.Logger;
import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.deliver.service.OleDeliverRequestDocumentHelperServiceImpl;
import org.kuali.ole.deliver.service.OleLoanDocumentDaoOjb;
import org.kuali.ole.docstore.common.document.content.instance.ItemType;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.docstore.model.rdbms.bo.ItemTypeRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 3/1/16.
 */
public class LoanHistoryUtil {
    private static final Logger LOG = Logger.getLogger(LoanHistoryUtil.class);
    private BusinessObjectService businessObjectService;
    private OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;

    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public OleLoanDocumentDaoOjb getOleLoanDocumentDaoOjb() {
        if (oleLoanDocumentDaoOjb == null) {
            oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return oleLoanDocumentDaoOjb;
    }


    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (oleDeliverRequestDocumentHelperService == null) {
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }


    public void populateCirculationHistoryTable() {

     /*   List<OleLoanDocument> oleLoanDocumentList = getOleLoanDocumentDaoOjb().getAllLoans();
        List<OleCirculationHistory> oleCirculationHistoryList = new ArrayList<OleCirculationHistory>();
        for (OleLoanDocument oleLoanDocument : oleLoanDocumentList) {
            oleCirculationHistoryList.add(createCirculationHistoryRecords(oleLoanDocument));
        }
        getBusinessObjectService().save(oleCirculationHistoryList);*/
    }


    public OleCirculationHistory createCirculationHistoryRecords(OleLoanDocument oleLoanDocument) {
        LOG.info("Processing Loan Document with Loan Id: " + oleLoanDocument.getLoanId());
        OleCirculationHistory oleCirculationHistory = new OleCirculationHistory();
       try{
        ItemRecord itemRecord = ItemInfoUtil.getInstance().getItemRecordByBarcode(oleLoanDocument.getItemId());
        OleItemSearch oleItemSearch = new DocstoreUtil().getOleItemSearchList(oleLoanDocument.getItemUuid());
        OlePatronDocument olePatronDocument = oleLoanDocument.getOlePatron();
        oleCirculationHistory.setLoanId(oleLoanDocument.getLoanId());
        oleCirculationHistory.setCirculationPolicyId(oleLoanDocument.getCirculationPolicyId());
        oleCirculationHistory.setBibAuthor(oleItemSearch.getAuthor());
        oleCirculationHistory.setBibTitle(oleItemSearch.getTitle());
        oleCirculationHistory.setCreateDate(oleLoanDocument.getCreateDate());
        oleCirculationHistory.setCirculationLocationId(oleLoanDocument.getCirculationLocationId());
        oleCirculationHistory.setDueDate(oleLoanDocument.getLoanDueDate());
        oleCirculationHistory.setItemId(oleLoanDocument.getItemId());
        oleCirculationHistory.setStatisticalCategory(olePatronDocument.getStatisticalCategory());
        oleCirculationHistory.setProxyPatronId(olePatronDocument.getProxyPatronId());
        if (olePatronDocument.getOleBorrowerType() != null) {
            oleCirculationHistory.setPatronTypeId(olePatronDocument.getOleBorrowerType().getBorrowerTypeId());
        }
        oleCirculationHistory.setPatronId(oleLoanDocument.getPatronId());
        oleCirculationHistory.setOleRequestId(oleLoanDocument.getOleRequestId());
        oleCirculationHistory.setItemUuid(oleLoanDocument.getItemUuid());
        oleCirculationHistory.setItemLocation(itemRecord.getLocation());
        oleCirculationHistory.setHoldingsLocation(getHoldingsLocation(itemRecord.getHoldingsId()));
        setAffiliationDetails(oleCirculationHistory, oleLoanDocument);
        oleCirculationHistory.setItemTypeId(itemRecord.getItemTypeId());
        oleCirculationHistory.setTemporaryItemTypeId(itemRecord.getTempItemTypeId());
        oleCirculationHistory.setProxyPatronId(oleLoanDocument.getProxyPatronId());
        oleCirculationHistory.setOperatorCreateId(oleLoanDocument.getLoanOperatorId());
       }catch(Exception e){
           LOG.error(e,e);
           LOG.info("Exception occured while processing the loan with Loan Id " + oleLoanDocument.getLoanId());
       }
           return oleCirculationHistory;
    }

    protected void setAffiliationDetails(OleCirculationHistory oleCirculationHistory, OleLoanDocument oleLoanDocument) {
        if (oleLoanDocument.getOlePatron().getEntity().getDefaultAffiliation() != null) {
            oleCirculationHistory.setAffiliationId(oleLoanDocument.getEntity().getDefaultAffiliation().getAffiliationTypeCode());
            if (oleLoanDocument.getOlePatron().getEntity().getPrimaryEmployment() != null && oleLoanDocument.getOlePatron().getEntity().getPrimaryEmployment().getPrimaryDepartmentCode() != null) {
                oleCirculationHistory.setDeptId(oleLoanDocument.getOlePatron().getEntity().getPrimaryEmployment().getPrimaryDepartmentCode());
            }
        }

    }

    private String getHoldingsLocation(String holdignsId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("holdingsId", holdignsId);
        HoldingsRecord byPrimaryKey = getBusinessObjectService().findByPrimaryKey(HoldingsRecord.class, map);
        if (null != byPrimaryKey) {
            return byPrimaryKey.getLocation();
        }
        return null;
    }


}
