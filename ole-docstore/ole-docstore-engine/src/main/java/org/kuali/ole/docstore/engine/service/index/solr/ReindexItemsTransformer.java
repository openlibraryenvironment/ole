package org.kuali.ole.docstore.engine.service.index.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.kuali.ole.docstore.common.exception.DocstoreIndexException;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 4/16/14
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReindexItemsTransformer extends ReindexHoldingsTransformer {


    private static final Logger LOG = LoggerFactory.getLogger(ReindexItemsTransformer.class);

    public Object transformRow(Map<String, Object> row)  throws Exception   {
        String uniqueBibId = "wbm-" +  row.get("BIB_ID");
        String uniqueHoldingsId = "who-" + row.get("HOLDINGS_ID");
        String uniqueItemId = "wio-" + row.get("ITEM_ID");

        String staffOnly = (String) row.get("staffOnlyFlag");
        if("N".equals(staffOnly)){
            staffOnly = "false";
        }
        else {
            staffOnly = "true";
        }

        dateConvertField("DATEENTERED", row);
        dateConvertField("DATEUPDATED", row);
        dateConvertField("DUE_DATE_TIME", row);
        dateConvertField("CLAIMS_RETURNED_DATE_CREATED", row);
        dateConvertField("ITEM_STATUS_DATE_UPDATED", row);
                 
        row.put("staffOnlyFlag", staffOnly);
        row.put("uniqueBibId",uniqueBibId);
        row.put("uniqueHoldingsId",uniqueHoldingsId);
        row.put("uniqueItemId",uniqueItemId);



        //To index bib fields to holdings

        List<SolrDocument> solrHoldingsDocs = getSolrDocumentBySolrId(uniqueHoldingsId);
        if(solrHoldingsDocs != null && solrHoldingsDocs.size() > 0) {
            SolrDocument holdingsSolrDoc = solrHoldingsDocs.get(0);
            addBibInfo(row, holdingsSolrDoc);

            row.put("HoldingsCallNumber_search", holdingsSolrDoc.getFieldValue("CallNumber_search"));
            row.put("HoldingsLocation_search", holdingsSolrDoc.getFieldValue("Location_search"));

            row.put("HoldingsCallNumber_display", holdingsSolrDoc.getFieldValue("CallNumber_display"));
            row.put("HoldingsLocation_display", holdingsSolrDoc.getFieldValue("Location_display"));

            row.put("bibIdentifier", holdingsSolrDoc.getFieldValue("bibIdentifier"));
        }

        return row;
    }

    private void dateConvertField(String field , Map<String, Object> row) throws Exception {
        if(row.get(field) !=  null) {

            String dateField = (String) row.get(field).toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date oldDate = (Date)sdf.parse(dateField);
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            dateField = sdf.format(oldDate);

            if(dateField != null) {
                dateField = dateField.replace(" ", "T")+"Z";
            }
            row.put(field,dateField);
        }
    }
    public List<SolrDocument> getSolrDocumentBySolrId(String uniqueId) {
        QueryResponse response = null;
        String result = null;
        try {
            String args = "(" + BibConstants.UNIQUE_ID + ":" + uniqueId + ")";
            SolrServer solr = SolrServerManager.getInstance().getSolrServer();
            SolrQuery query = new SolrQuery();
            query.setQuery(args);
            response = solr.query(query);
        } catch (Exception e) {
            LOG.info("Exception :", e);
            throw new DocstoreIndexException(e.getMessage());
        }
        return response.getResults();
    }

}
