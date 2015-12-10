package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.dao.BibDAO;
import org.kuali.ole.dsng.dao.HoldingDAO;
import org.kuali.ole.dsng.dao.ItemDAO;
import org.kuali.ole.dsng.util.OleDsHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/8/2015.
 */
public class OleDsNgOverlayProcessor extends OleDsHelperUtil implements DocstoreConstants {


    @Autowired
    BibDAO bibDAO;

    @Autowired
    HoldingDAO holdingDAO;

    @Autowired
    ItemDAO itemDAO;

    public String processOverlayForBib(String jsonBody) {
        JSONArray responseJsonArray = null;
        try {
            JSONArray requestJsonArray = new JSONArray(jsonBody);
            responseJsonArray = new JSONArray();
            for(int index = 0 ; index < requestJsonArray.length() ; index++) {
                JSONObject jsonObject = requestJsonArray.getJSONObject(index);

                String bibId = jsonObject.getString("id");

                String updatedContent = jsonObject.getString("content");
                String updatedBy = jsonObject.getString("updatedBy");
                String updatedDateString = (String) jsonObject.get("updatedDate");

                BibRecord bibRecord = bibDAO.retrieveBibById(bibId);
                if(null != bibRecord) {
                    //TODO : process bib record with overlay
                    bibRecord.setContent(updatedContent);
                    bibRecord.setUpdatedBy(updatedBy);
                    if(StringUtils.isNotBlank(updatedDateString)) {
                        bibRecord.setDateEntered(getDateTimeStamp(updatedDateString));
                    }
                    BibRecord updatedBibRecord = bibDAO.save(bibRecord);

                    getBibIndexer().updateDocument(updatedBibRecord);

                    JSONObject responseObject = new JSONObject();
                    responseObject.put("bibId",updatedBibRecord.getBibId());
                    responseJsonArray.put(responseObject);
                } else {
                    // TODO : need to handle if bib record is not found
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJsonArray.toString();
    }


    public String processOverlayForHolding(String jsonBody) {
        JSONArray responseJsonArray = null;
        try {
            JSONArray requestJsonArray = new JSONArray(jsonBody);
            responseJsonArray = new JSONArray();
            for(int index = 0 ; index < requestJsonArray.length() ; index++) {
                JSONObject jsonObject = requestJsonArray.getJSONObject(index);

                String holdingId = jsonObject.getString("id");

                //String updatedContent = jsonObject.getString("content");
                String updatedBy = jsonObject.getString("updatedBy");
                String updatedDateString = (String) jsonObject.get("updatedDate");

                HoldingsRecord holdingsRecord = holdingDAO.retrieveHoldingById(holdingId);
                if(null != holdingsRecord) {
                    //TODO : process holding record with overlay
                    holdingsRecord.setUpdatedBy(updatedBy);
                    if(StringUtils.isNotBlank(updatedDateString)) {
                        holdingsRecord.setUpdatedDate(getDateTimeStamp(updatedDateString));
                    }
                    HoldingsRecord updatedHoldingRecord = holdingDAO.save(holdingsRecord);

                    getHoldingIndexer().updateDocument(updatedHoldingRecord);

                    JSONObject responseObject = new JSONObject();
                    responseObject.put("holdingId",updatedHoldingRecord.getHoldingsId());
                    responseJsonArray.put(responseObject);
                } else {
                    // TODO : need to handle if bib record is not found
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJsonArray.toString();
    }



    public String processOverlayForBibAndHoldings(String jsonBody) {
        List<SolrInputDocument> solrInputDocuments = new ArrayList<SolrInputDocument>();
        JSONArray responseJsonArray = null;
        try {
            JSONArray requestJsonArray = new JSONArray(jsonBody);
            responseJsonArray = new JSONArray();
            for(int index = 0 ; index < requestJsonArray.length() ; index++) {
                JSONObject jsonObject = requestJsonArray.getJSONObject(index);

                String bibId = jsonObject.getString("id");

                String updatedContent = jsonObject.getString("content");
                String updatedBy = jsonObject.getString("updatedBy");
                String updatedDateString = (String) jsonObject.get("updatedDate");

                BibRecord bibRecord = bibDAO.retrieveBibById(bibId);
                if(null != bibRecord) {
                    //TODO : process bib record with overlay
                    bibRecord.setContent(updatedContent);
                    bibRecord.setUpdatedBy(updatedBy);
                    if(StringUtils.isNotBlank(updatedDateString)) {
                        bibRecord.setDateEntered(getDateTimeStamp(updatedDateString));
                    }
                    BibRecord updatedBibRecord = bibDAO.save(bibRecord);

                    List<SolrInputDocument> inputDocumentForBib = getBibIndexer().getInputDocumentForBib(bibRecord);
                    solrInputDocuments.addAll(inputDocumentForBib);

                    JSONObject responseObject = new JSONObject();
                    responseObject.put("bibId",updatedBibRecord.getBibId());

                    List<HoldingsRecord> holdingsRecords = bibRecord.getHoldingsRecords();
                    if(CollectionUtils.isNotEmpty(holdingsRecords) && jsonObject.has("holdingIds")) {
                        JSONArray holdingIdsJsonArray = jsonObject.getJSONArray("holdingIds");
                        for(int holdingIndex = 0 ; holdingIndex < holdingIdsJsonArray.length() ; holdingIndex++) {
                            JSONObject holdingJsonObject = holdingIdsJsonArray.getJSONObject(holdingIndex);

                            String holdingId = holdingJsonObject.getString("id");

                            String holdingUpdateBy = holdingJsonObject.getString("updatedBy");
                            String holdingUpdatedDateString = (String) holdingJsonObject.get("updatedDate");

                            JSONArray holdingsResponseJsonArray = new JSONArray();
                            for (Iterator<HoldingsRecord> iterator = holdingsRecords.iterator(); iterator.hasNext(); ) {
                                HoldingsRecord holdingsRecord = iterator.next();
                                String holdingsIdentifierWithPrefix = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, holdingsRecord.getHoldingsId());
                                if(holdingId.equalsIgnoreCase(holdingsIdentifierWithPrefix)) {
                                    holdingsRecord.setUpdatedBy(holdingUpdateBy);
                                    if(StringUtils.isNotBlank(holdingUpdatedDateString)) {
                                        holdingsRecord.setUpdatedDate(getDateTimeStamp(holdingUpdatedDateString));
                                    }
                                    HoldingsRecord updatedHoldingRecord = holdingDAO.save(holdingsRecord);
                                    List<SolrInputDocument> inputDocumentForHoldings = getHoldingIndexer().getInputDocumentForHoldings(holdingsRecord);
                                    solrInputDocuments.addAll(inputDocumentForHoldings);

                                    JSONObject holdingResonseObject = new JSONObject();
                                    holdingResonseObject.put("holdingId",updatedHoldingRecord.getHoldingsId());
                                    holdingsResponseJsonArray.put(holdingResonseObject);
                                }
                            }
                            responseObject.put("holdingsIds" , holdingsResponseJsonArray);
                        }
                    }
                    getBibIndexer().commitDocumentToSolr(solrInputDocuments);
                    responseJsonArray.put(responseObject);
                } else {
                    // TODO : need to handle if bib record is not found
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseJsonArray.toString();
    }

    private Timestamp getDateTimeStamp(String updatedDateString){
        Timestamp timeStamp = null;
        try {
            Date parse = DocstoreConstants.DOCSTORE_DATE_FORMAT.parse(updatedDateString);
            if(null != parse) {
                timeStamp = new Timestamp(parse.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }
}
