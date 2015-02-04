package org.kuali.ole.docstore.metrics.reindex;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 6/27/12
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReIndexingStatus {
    private static final Logger log = LoggerFactory.getLogger(ReIndexingStatus.class);
    private List<ReIndexingDocTypeStatus> reIndTypStatusList = new ArrayList<ReIndexingDocTypeStatus>();

    private ReIndexingStatus() {

    }

    private static ReIndexingStatus reIndexingStatus = null;

    public static ReIndexingStatus getInstance() {
        if (reIndexingStatus == null) {
            reIndexingStatus = new ReIndexingStatus();
        }
        return reIndexingStatus;
    }

    public void reset() {
        reIndTypStatusList = new ArrayList<ReIndexingDocTypeStatus>();
    }

    public void startDocType(String docCategory, String docType, String docFormat) {
        ReIndexingDocTypeStatus reIndexingDocTypeStatus = new ReIndexingDocTypeStatus();
        reIndexingDocTypeStatus.setDocCategory(docCategory);
        reIndexingDocTypeStatus.setDocType(docType);
        reIndexingDocTypeStatus.setDocFormat(docFormat);
        reIndexingDocTypeStatus.setStatus("Started");
        reIndTypStatusList.add(reIndexingDocTypeStatus);
        //        reIndexingStatus.setReIndTypStatusList(reIndTypStatusList);
    }

    public ReIndexingDocTypeStatus getDocTypeList() {
        ReIndexingDocTypeStatus reIndexingDocTypeStatus = null;
        if (reIndTypStatusList != null && reIndTypStatusList.size() > 0) {
            reIndexingDocTypeStatus = reIndTypStatusList.get(reIndTypStatusList.size() - 1);
        } else {
            reIndexingDocTypeStatus = new ReIndexingDocTypeStatus();
        }
        return reIndexingDocTypeStatus;
    }

    public List<ReIndexingDocTypeStatus> getReIndTypStatusList() {
        return reIndTypStatusList;
    }

    public void setReIndTypStatusList(List<ReIndexingDocTypeStatus> reIndTypStatusList) {
        this.reIndTypStatusList = reIndTypStatusList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"Doc Category\" \t" + "\"Doc Type \" \t" + "\"Doc Format\" \t" + "\"Type Status\" \t"
                + "\"Batch Load Time\" \t" + "\"Batch Start Time\" \t" + "\"Batch End Time\" \t"
                + "\"Batch Indexing Time\"\t" + "\"Records processed\"\t" + "\"Batch Total Time\" \t" +
                  /* "\"Remaining Records\"\t" +*/ "\"BatchStatus\"\t");
        for (ReIndexingDocTypeStatus bTS : reIndTypStatusList) {
            sb.append("\n" + bTS.getDocCategory() + "\t");
            sb.append(bTS.getDocType() + "\t");
            sb.append(bTS.getDocFormat() + "\t");
            sb.append(bTS.getStatus() + "\t");

            for (ReIndexingBatchStatus bS : bTS.getReIndBatStatusList()) {
                sb.append("\n" + "\t" + "\t" + "\t" + "\t" + bS.getBatchLoadTime() + "\t");
                sb.append(bS.getBatchStartTime() + "\t");
                sb.append(bS.getBatchEndTime() + "\t");
                sb.append(bS.getBatchIndexingTime() + "\t");
                sb.append(bS.getRecordsProcessed() + "\t");
                sb.append(bS.getBatchTotalTime() + "\t");
                //                sb.append(bS.getRecordsRemaining() + "\t");
                sb.append(bS.getStatus() + "\t");

            }
        }
        return sb.toString();
    }

    public String getJsonString() {
        StringWriter out = new StringWriter();
        JSONObject obj = new JSONObject();
        LinkedHashMap reindexMap = null;
        LinkedList reindexList = new LinkedList();
        for (ReIndexingDocTypeStatus bTS : reIndTypStatusList) {
            reindexMap = getRebuildIndexMap(bTS.getDocCategory(), bTS.getDocType(), bTS.getDocFormat(), bTS.getStatus(),
                    "", "", "", "", null, "", "");
            reindexList.add(reindexMap);
            for (ReIndexingBatchStatus bS : bTS.getReIndBatStatusList()) {
                reindexMap = getRebuildIndexMap("", "", "", "", bS.getBatchLoadTime(), bS.getBatchStartTime(),
                        bS.getBatchEndTime(), bS.getBatchIndexingTime(),
                        bS.getRecordsProcessed(), bS.getBatchTotalTime(), bS.getStatus());
                reindexList.add(reindexMap);
            }
        }
        obj.put("rows", reindexList);
        try {
            obj.writeJSONString(out);
        } catch (IOException e) {
            log.error("Error occurred due to :", e);
        }
        return out.toString();
    }

    public LinkedHashMap getRebuildIndexMap(String category, String type, String format, String typeStatus,
                                            String batchLoadTime, String batchStartTime, String batchEndTime,
                                            String batchIndexTime, Long recordsProcessed, String batchTotalTime,
                                            String status) {
        LinkedHashMap reindexMap = new LinkedHashMap();
        reindexMap.put("category", category);
        reindexMap.put("type", type);
        reindexMap.put("format", format);
        reindexMap.put("typeStatus", typeStatus);
        reindexMap.put("batchLoadTime", batchLoadTime);
        reindexMap.put("batchStartTime", batchStartTime);
        reindexMap.put("batchEndTime", batchEndTime);
        reindexMap.put("batchIndexTime", batchIndexTime);
        reindexMap.put("recordsProcessed", recordsProcessed);
        reindexMap.put("batchTotalTime", batchTotalTime);
        reindexMap.put("status", status);

        return reindexMap;
    }


}
