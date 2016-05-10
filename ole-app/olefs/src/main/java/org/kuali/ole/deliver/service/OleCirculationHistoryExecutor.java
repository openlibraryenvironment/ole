package org.kuali.ole.deliver.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.deliver.bo.OleCirculationHistory;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 3/8/16.
 */
public class OleCirculationHistoryExecutor implements Runnable{
    private static final Logger LOG = Logger.getLogger(OleCirculationHistoryExecutor.class);
    private BusinessObjectService businessObjectService;
    protected List<OleLoanDocument> oleLoanDocumentList;
    protected List<OleCirculationHistory> oleCirculationHistoryList;
    private DocstoreUtil docstoreUtil;
    private OleLoanDocumentDaoOjb oleLoanDocumentDaoOjb;
    private OleDeliverRequestDocumentHelperServiceImpl oleDeliverRequestDocumentHelperService;
    private EntityAffiliationBo entityAffiliationBo;
    private EntityEmploymentBo entityEmploymentBo;
    private OlePatronDocument olePatronDocument;
    private PlatformTransactionManager transactionManager;


    public OleLoanDocumentDaoOjb getOleLoanDocumentDaoOjb() {
        if (oleLoanDocumentDaoOjb == null) {
            oleLoanDocumentDaoOjb = (OleLoanDocumentDaoOjb) SpringContext.getBean("oleLoanDao");
        }
        return oleLoanDocumentDaoOjb;
    }


    public OleCirculationHistoryExecutor(List<OleLoanDocument> oleLoanDocumentList,EntityAffiliationBo entityAffiliationBo,
                                         EntityEmploymentBo entityEmploymentBo,OlePatronDocument olePatronDocument){
        this.oleLoanDocumentList = oleLoanDocumentList;
        this.entityAffiliationBo = entityAffiliationBo;
        this.entityEmploymentBo = entityEmploymentBo;
    }
    public BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public DocstoreUtil getDocstoreUtil(){
        if(docstoreUtil==null){
            docstoreUtil = new DocstoreUtil();
        }
        return docstoreUtil;
    }

    public OleDeliverRequestDocumentHelperServiceImpl getOleDeliverRequestDocumentHelperService() {
        if (oleDeliverRequestDocumentHelperService == null) {
            oleDeliverRequestDocumentHelperService = new OleDeliverRequestDocumentHelperServiceImpl();
        }
        return oleDeliverRequestDocumentHelperService;
    }


    @Override
    public void run() {
        final TransactionTemplate template = new TransactionTemplate(getTransactionManager());
        try{
            template.execute(new TransactionCallback<Object>() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    try {
                        getOleDeliverRequestDocumentHelperService().getLoanDocumentWithItemInfo(oleLoanDocumentList,false);
                        oleCirculationHistoryList = new ArrayList<OleCirculationHistory>();
                        for(OleLoanDocument oleLoanDocument : oleLoanDocumentList){
                            OleCirculationHistory oleCirculationHistory = oleCirculationHistory = createCirculationHistoryRecords(oleLoanDocument);
                            if(oleCirculationHistory!=null){
                                oleCirculationHistoryList.add(oleCirculationHistory);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(CollectionUtils.isNotEmpty(oleCirculationHistoryList)){
                        getBusinessObjectService().save(oleCirculationHistoryList);
                    }
                    return null;
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public OleCirculationHistory createCirculationHistoryRecords(OleLoanDocument oleLoanDocument) {
        LOG.info("Processing Loan Document with Loan Id: " + oleLoanDocument.getLoanId());
        OleCirculationHistory oleCirculationHistory = new OleCirculationHistory();
        try{
            oleCirculationHistory = setCirculationDetails(oleCirculationHistory, oleLoanDocument);
            oleCirculationHistory = setPatronDetails(oleCirculationHistory,olePatronDocument);
            oleCirculationHistory = setItemDetails(oleCirculationHistory,oleLoanDocument);
        }catch(Exception e){
            oleCirculationHistory = null;
            LOG.error(e,e);
            LOG.info("Exception occured while processing the loan with Loan Id " + oleLoanDocument.getLoanId());
        }
        return oleCirculationHistory;
    }

    public OleCirculationHistory setPatronDetails(OleCirculationHistory oleCirculationHistory,OlePatronDocument olePatronDocument){
        if(olePatronDocument!=null){
            oleCirculationHistory.setStatisticalCategory(olePatronDocument.getStatisticalCategory());
            oleCirculationHistory.setProxyPatronId(olePatronDocument.getProxyPatronId());
            if (olePatronDocument.getOleBorrowerType() != null) {
                oleCirculationHistory.setPatronTypeId(olePatronDocument.getOleBorrowerType().getBorrowerTypeId());
            }
          setAffiliationDetails(oleCirculationHistory);
        }
   return oleCirculationHistory;
    }


    public OleCirculationHistory setCirculationDetails(OleCirculationHistory oleCirculationHistory,OleLoanDocument oleLoanDocument){
        oleCirculationHistory.setLoanId(oleLoanDocument.getLoanId());
        oleCirculationHistory.setCirculationPolicyId(oleLoanDocument.getCirculationPolicyId());
        oleCirculationHistory.setCreateDate(oleLoanDocument.getCreateDate());
        oleCirculationHistory.setCirculationLocationId(oleLoanDocument.getCirculationLocationId());
        oleCirculationHistory.setDueDate(oleLoanDocument.getLoanDueDate());
        oleCirculationHistory.setPatronId(oleLoanDocument.getPatronId());
        oleCirculationHistory.setOleRequestId(oleLoanDocument.getOleRequestId());
        oleCirculationHistory.setProxyPatronId(oleLoanDocument.getProxyPatronId());
        oleCirculationHistory.setOperatorCreateId(oleLoanDocument.getLoanOperatorId());
        return oleCirculationHistory;
    }

    public OleCirculationHistory setItemDetails(OleCirculationHistory oleCirculationHistory,OleLoanDocument oleLoanDocument){
    oleCirculationHistory.setBibAuthor(oleLoanDocument.getAuthor());
    oleCirculationHistory.setBibTitle(oleLoanDocument.getTitle());
    oleCirculationHistory.setItemId(oleLoanDocument.getItemId());
    oleCirculationHistory.setItemUuid(oleLoanDocument.getItemUuid());
    oleCirculationHistory.setItemLocation(oleLoanDocument.getItemsLocation());
    oleCirculationHistory.setHoldingsLocation(oleLoanDocument.getHoldingsLocation());
    oleCirculationHistory.setItemTypeId(oleLoanDocument.getItemTypeId());
    oleCirculationHistory.setTemporaryItemTypeId(oleLoanDocument.getTempItemTypeId());
    return oleCirculationHistory;

}

    protected void setAffiliationDetails(OleCirculationHistory oleCirculationHistory) {
        if ( entityAffiliationBo!= null) {
            oleCirculationHistory.setAffiliationId(entityAffiliationBo.getAffiliationTypeCode());
        }
        if (entityEmploymentBo != null && entityEmploymentBo.getPrimaryDepartmentCode() != null) {
            oleCirculationHistory.setDeptId(entityEmploymentBo.getPrimaryDepartmentCode());
        }
    }

    public PlatformTransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = GlobalResourceLoader.getService(OleNGConstants.TRANSACTION_MANAGER);
        }
        return this.transactionManager;
    }
}
