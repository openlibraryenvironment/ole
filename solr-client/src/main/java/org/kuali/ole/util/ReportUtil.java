package org.kuali.ole.util;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.Constants;
import org.kuali.ole.model.jpa.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sheiks on 14/02/17.
 */
public class ReportUtil {

    private ProducerTemplate producerTemplate;

    public void saveExceptionReportForBib(BibRecord bibRecord, Exception exception) {
        List<ReportDataEntity> reportDataEntities = new ArrayList<>();

        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setCreatedDate(new Date());
        reportEntity.setType(Constants.SOLR_INDEX_EXCEPTION);

        ReportDataEntity docTypeDataEntity = new ReportDataEntity();
        docTypeDataEntity.setHeaderName(Constants.DOC_TYPE);
        docTypeDataEntity.setHeaderValue(Constants.BIB);
        reportDataEntities.add(docTypeDataEntity);

        ReportDataEntity exceptionMsgDataEntity = new ReportDataEntity();
        exceptionMsgDataEntity.setHeaderName(Constants.EXCEPTION_MSG);
        exceptionMsgDataEntity.setHeaderValue(getDetailedMessage(exception));
        reportDataEntities.add(exceptionMsgDataEntity);

        if(bibRecord.getBibId() != null) {
            ReportDataEntity bibIdDataEntity = new ReportDataEntity();
            bibIdDataEntity.setHeaderName(Constants.BIBID);
            bibIdDataEntity.setHeaderValue(String.valueOf(bibRecord.getBibId()));
            reportDataEntities.add(bibIdDataEntity);
        }

        reportEntity.addAll(reportDataEntities);
        producerTemplate.sendBody(Constants.REPORT_Q, reportEntity);
    }
    
    public void saveExceptionReportForHoldings(HoldingsRecord holdingsRecord, Exception exception) {
        List<ReportDataEntity> reportDataEntities = new ArrayList<>();

        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setCreatedDate(new Date());
        reportEntity.setType(Constants.SOLR_INDEX_EXCEPTION);

        ReportDataEntity docTypeDataEntity = new ReportDataEntity();
        docTypeDataEntity.setHeaderName(Constants.DOC_TYPE);
        docTypeDataEntity.setHeaderValue(Constants.HOLDINGS);
        reportDataEntities.add(docTypeDataEntity);

        ReportDataEntity exceptionMsgDataEntity = new ReportDataEntity();
        exceptionMsgDataEntity.setHeaderName(Constants.EXCEPTION_MSG);
        exceptionMsgDataEntity.setHeaderValue(getDetailedMessage(exception));
        reportDataEntities.add(exceptionMsgDataEntity);

        Integer holdingsId = holdingsRecord.getHoldingsId();
        if(holdingsId != null) {
            ReportDataEntity holdingsIdDataEntity = new ReportDataEntity();
            holdingsIdDataEntity.setHeaderName(Constants.HOLDING_ID);
            holdingsIdDataEntity.setHeaderValue(String.valueOf(holdingsId));
            reportDataEntities.add(holdingsIdDataEntity);

            BibRecord bibRecord = holdingsRecord.getBibRecord();
            if(null != bibRecord) {
                ReportDataEntity bibIdDataEntity = new ReportDataEntity();
                bibIdDataEntity.setHeaderName(Constants.BIBID);
                bibIdDataEntity.setHeaderValue(String.valueOf(bibRecord.getBibId()));
                reportDataEntities.add(bibIdDataEntity);
            }
        }

        reportEntity.addAll(reportDataEntities);
        producerTemplate.sendBody(Constants.REPORT_Q, reportEntity);
    }
    
    

    public void saveExceptionReportForItem(ItemRecord itemRecord, Exception exception) {
        List<ReportDataEntity> reportDataEntities = new ArrayList<>();

        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setCreatedDate(new Date());
        reportEntity.setType(Constants.SOLR_INDEX_EXCEPTION);

        ReportDataEntity docTypeDataEntity = new ReportDataEntity();
        docTypeDataEntity.setHeaderName(Constants.DOC_TYPE);
        docTypeDataEntity.setHeaderValue(Constants.ITEM);
        reportDataEntities.add(docTypeDataEntity);

        ReportDataEntity exceptionMsgDataEntity = new ReportDataEntity();
        exceptionMsgDataEntity.setHeaderName(Constants.EXCEPTION_MSG);
        exceptionMsgDataEntity.setHeaderValue(getDetailedMessage(exception));
        reportDataEntities.add(exceptionMsgDataEntity);

        if(itemRecord != null && itemRecord.getItemId() != null) {
            ReportDataEntity itemIdDataEntity = new ReportDataEntity();
            itemIdDataEntity.setHeaderName(Constants.ITEM_ID);
            itemIdDataEntity.setHeaderValue(String.valueOf(itemRecord.getItemId()));
            reportDataEntities.add(itemIdDataEntity);

            HoldingsRecord holdingsRecord = itemRecord.getHoldingsRecord();
            if(null != holdingsRecord) {
                ReportDataEntity holdingIdDataEntity = new ReportDataEntity();
                holdingIdDataEntity.setHeaderName(Constants.HOLDING_ID);
                holdingIdDataEntity.setHeaderValue(String.valueOf(holdingsRecord.getHoldingsId()));
                reportDataEntities.add(holdingIdDataEntity);
                
                BibRecord bibRecord = holdingsRecord.getBibRecord();
                if(null != bibRecord) {
                    ReportDataEntity bibIdDataEntity = new ReportDataEntity();
                    bibIdDataEntity.setHeaderName(Constants.BIBID);
                    bibIdDataEntity.setHeaderValue(String.valueOf(bibRecord.getBibId()));
                    reportDataEntities.add(bibIdDataEntity);
                }                
            }           
            
        }

        reportEntity.addAll(reportDataEntities);
        producerTemplate.sendBody(Constants.REPORT_Q, reportEntity);
    }



    public String getDetailedMessage(Exception exception) {
        StackTraceElement[] stackTrace = exception.getStackTrace();
        StringBuilder detailedMessage = new StringBuilder();
        String className = null;
        String methodName = null;
        int lineNumber = 0;
        if (null != stackTrace && stackTrace.length >= 2) {
            for(int index = 0; index < 2; index++) {
                StackTraceElement stackTraceElement = stackTrace[index];
                String fullClassName = stackTraceElement.getClassName();
                className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
                methodName = stackTraceElement.getMethodName();
                lineNumber = stackTraceElement.getLineNumber();
                if(StringUtils.isNotBlank(detailedMessage.toString())){
                    detailedMessage.append("---> ");
                }
                detailedMessage.append(className + "." + methodName + "():line#" + lineNumber);
            }
        }
        return detailedMessage.toString();
    }

    public ProducerTemplate getProducerTemplate() {
        return producerTemplate;
    }

    public void setProducerTemplate(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }
}
