package org.kuali.ole.spring.batch.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.IOUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.docstore.common.pojo.RecordDetails;
import org.kuali.ole.docstore.common.response.BatchProcessFailureResponse;
import org.kuali.ole.docstore.common.response.OleNgBatchResponse;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.spring.batch.BatchUtil;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 4/4/2016.
 */
public class MarcStreamingProcesssor implements Processor {
    private static Logger logger = LoggerFactory.getLogger(MarcStreamingProcesssor.class);
    private int batchCount = 1;
    private int recordIndex = 1;
    private MarcXMLConverter marcXMLConverter;
    private BatchProcessTxObject batchProcessTxObject;
    private UserSession userSession;
    private BatchUtil batchUtil;

    public MarcStreamingProcesssor(BatchProcessTxObject batchProcessTxObject, UserSession userSession) {
        this.batchProcessTxObject = batchProcessTxObject;
        this.userSession = userSession;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        BatchProcessProfile batchProcessProfile = batchProcessTxObject.getBatchProcessProfile();
        try {
            System.out.println("Batch : " + batchCount);
            GlobalVariables.setUserSession(userSession);
            batchCount++;
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
            Date date = new Date();

            StringBuilder stringBuilder = new StringBuilder();

            System.out.println("Bulk ingest: Batch Start time : \t" + dateFormat.format(date));
            if (exchange.getIn().getBody() instanceof List) {
                for (String content : (List<String>) exchange.getIn().getBody()) {
                    stringBuilder.append(content);
                    stringBuilder.append(OleNGConstants.MARC_SPLIT);
                }
            }
            date = new Date();
            Map<Integer, RecordDetails> recordDetailsMap = getRecordDetailsMap(stringBuilder.toString());
            OleNgBatchResponse oleNgBatchResponse = batchProcessTxObject.getBatchFileProcessor().processRecords(recordDetailsMap, batchProcessTxObject,
                    batchProcessProfile);
            batchProcessTxObject.setTotalNumberOfRecords(batchProcessTxObject.getTotalNumberOfRecords() + recordDetailsMap.size());
            batchProcessTxObject.setNumberOfFailurRecords(batchProcessTxObject.getNumberOfFailurRecords() + oleNgBatchResponse.getNoOfFailureRecord());
            System.out.println(recordDetailsMap.size());
            System.out.println("Bulk ingest: Batch End time : \t" + dateFormat.format(date));
        } catch (Exception e) {
            e.printStackTrace();
            exchange.setException(e);
            batchProcessTxObject.setExceptionCaught(true);
            BatchProcessFailureResponse batchProcessFailureResponse = new BatchProcessFailureResponse();
            batchProcessFailureResponse.setBatchProcessProfileName(batchProcessProfile.getBatchProcessProfileName());
            batchProcessFailureResponse.setFailureReason(e.toString());
            batchProcessFailureResponse.setDetailedMessage(getBatchUtil().getDetailedMessage(e));
            batchProcessTxObject.getBatchProcessFailureResponses().add(batchProcessFailureResponse);
            throw e;
        }
    }

    private Map<Integer, RecordDetails> getRecordDetailsMap(String rawContent) {
        Map<Integer, RecordDetails> recordDetailsMap = new HashMap<>();
        MarcReader reader = new MarcStreamReader(IOUtils.toInputStream(rawContent));
        Record nextRecord = null;
        do {
            RecordDetails recordDetails = new RecordDetails();
            try {
                nextRecord = getMarcXMLConverter().getNextRecord(reader);
                recordDetails.setRecord(nextRecord);
            } catch (Exception e) {
                e.printStackTrace();
                recordDetails.setMessage("Unable to parse the marc file. Allowed format is UTF-8. " + e.toString());
            }
            recordDetails.setIndex(recordIndex);
            recordDetailsMap.put(recordIndex, recordDetails);
            recordIndex++;
        } while(reader.hasNext());
        return recordDetailsMap;
    }

    public MarcXMLConverter getMarcXMLConverter() {
        if(null == marcXMLConverter) {
            marcXMLConverter = new MarcXMLConverter();
        }
        return marcXMLConverter;
    }

    public BatchUtil getBatchUtil() {
        if(null == batchUtil) {
            batchUtil = new BatchUtil();
        }
        return batchUtil;
    }
}
