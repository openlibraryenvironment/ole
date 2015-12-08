package org.kuali.ole.dsng.rest.processor;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.kuali.ole.DocstoreTestCaseBase;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 12/8/2015.
 */
public class OleDsNgOverlayProcessorTest implements DocstoreConstants {

    @Autowired
    OleDsNgRestAPIProcessor oleDsNgRestAPIProcessor;

    @Test
    public void testProcessOverlayForBib() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(LOCALID_DISPLAY,"10000034");

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);

        String savedJSON = oleDsNgRestAPIProcessor.processOverlayForBib(jsonArray.toString());
        assertTrue(StringUtils.isNotBlank(savedJSON));
    }

    @Test
    public void testSerializeAndDeserializeSolrInputDocumentAsJSON() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.addField("id",10101);
        solrInputDocument.addField("Title","Bib Title");
        solrInputDocument.addField("Author","Bib Author");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 10101);
        jsonObject.put("solrInputDocument", solrInputDocument);

        String serializedContent = jsonObject.toString();
        assertTrue(StringUtils.isNotBlank(serializedContent));
        System.out.println(serializedContent);

        JSONObject jsonObject1 = new JSONObject(serializedContent);
        assertNotNull(jsonObject1);

        Integer id = (Integer) jsonObject1.get("id");
        System.out.println(id);

        Object solrInputDocument1 = jsonObject1.get("solrInputDocument");
        assertNotNull(solrInputDocument1);


    }

    @Test
    public void testSerializeAndDeserializeSolrDocumentAsJSON() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        SolrDocument solrDocument = new SolrDocument();
        solrDocument.addField("id",10101);
        solrDocument.addField("Title","Bib Title");
        solrDocument.addField("Author","Bib Author");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 10101);
        jsonObject.put("solrDocument", objectMapper.writeValueAsString(solrDocument));

        String serializedContent = jsonObject.toString();
        assertTrue(StringUtils.isNotBlank(serializedContent));
        System.out.println(serializedContent);


        JSONObject jsonObject1 = new JSONObject(serializedContent);
        assertNotNull(jsonObject1);

        String solrDocuemntContent = (String) jsonObject1.getString("solrDocument");
        SolrDocument deserializedDocument = objectMapper.readValue(solrDocuemntContent, SolrDocument.class);
        assertNotNull(deserializedDocument);
        System.out.println(deserializedDocument);

        SolrInputDocument solrInputDocument = ClientUtils.toSolrInputDocument(solrDocument);
        assertNotNull(solrInputDocument);
        System.out.println(solrInputDocument);


    }
}