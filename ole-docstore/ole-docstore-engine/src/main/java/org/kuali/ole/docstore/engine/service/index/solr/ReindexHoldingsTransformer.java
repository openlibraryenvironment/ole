package org.kuali.ole.docstore.engine.service.index.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.kuali.ole.docstore.common.exception.DocstoreIndexException;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.query.Row;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 4/15/14
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReindexHoldingsTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(ReindexHoldingsTransformer.class);

    public Object transformRow(Map<String, Object> row) throws Exception    {
        String uniqueBibId = "wbm-" +  row.get("BIB_ID");
        String uniqueHoldingsId = "who-" + row.get("HOLDINGS_ID");
        String uniqueItemId = "wio-" + row.get("ITEM_ID");

        String staffOnly = (String) row.get("STAFFONLYFLAG");

        if("N".equals(staffOnly)){
            staffOnly = "false";
        }
        else {
            staffOnly = "true";
        }



        dateConvertField("DATEENTERED", row);
        dateConvertField("DATEUPDATED" , row);

        row.put("staffOnlyFlag",staffOnly);
        row.put("uniqueBibId",uniqueBibId);
        row.put("uniqueHoldingsId",uniqueHoldingsId);
        row.put("uniqueItemId",uniqueItemId);



        //To index bib fields to holdings

        List<SolrDocument> solrBibDocs = getSolrDocumentBySolrId(uniqueBibId);
        if(solrBibDocs != null && solrBibDocs.size() > 0) {
            SolrDocument bibSolrDoc = solrBibDocs.get(0);
            addBibInfo(row, bibSolrDoc);

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

    protected void addBibInfo(Map<String, Object> row, SolrDocument bibSolrDoc) {
        row.put("Title_search", bibSolrDoc.getFieldValue("Title_search"));
        row.put("Author_search", bibSolrDoc.getFieldValue("Author_search"));
        row.put("ISSN_search", bibSolrDoc.getFieldValue("ISSN_search"));
        row.put("Publisher_search",bibSolrDoc.getFieldValue("Publisher_search"));
        row.put("ISBN_search", bibSolrDoc.getFieldValue("ISBN_search"));
        row.put("Format_search", bibSolrDoc.getFieldValue("Format_search"));
        row.put("Language_search", bibSolrDoc.getFieldValue("Language_search"));
        row.put("PublicationDate_search", bibSolrDoc.getFieldValue("PublicationDate_search"));

        row.put("Title_display", bibSolrDoc.getFieldValue("Title_display"));
        row.put("Author_display", bibSolrDoc.getFieldValue("Author_display"));
        row.put("ISSN_display", bibSolrDoc.getFieldValue("ISSN_display"));
        row.put("Publisher_display",bibSolrDoc.getFieldValue("Publisher_display"));
        row.put("ISBN_display", bibSolrDoc.getFieldValue("ISBN_display"));
        row.put("Format_display", bibSolrDoc.getFieldValue("Format_display"));
        row.put("Language_display", bibSolrDoc.getFieldValue("Language_display"));
        row.put("PublicationDate_display", bibSolrDoc.getFieldValue("PublicationDate_display"));
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
