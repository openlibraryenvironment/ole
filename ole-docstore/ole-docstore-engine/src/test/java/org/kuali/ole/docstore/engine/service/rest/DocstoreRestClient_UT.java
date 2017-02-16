package org.kuali.ole.docstore.engine.service.rest;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.client.DocstoreRestClient;
import org.kuali.ole.docstore.common.client.RestResponse;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingScheme;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.BrowseParams;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 1/20/14
 * Time: 5:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreRestClient_UT extends BaseTestCase implements DocstoreConstants {

    private static final Logger LOG = LoggerFactory.getLogger(DocstoreRestClient_UT.class);
    private DocstoreRestClient restClient = new DocstoreRestClient();


    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        String url = "http://tst.docstore.oleproject.org/documentrest/";
        DocstoreRestClient.setDocstoreUrl(url);
    }

    @Ignore
    @Test
    public void createBib() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(input);
        bibMarc.setId("");
        restClient.createBib(bibMarc);
        Assert.assertNotNull(bibMarc.getId());
        Bib bib = restClient.retrieveBib(bibMarc.getId());
        bib = (Bib) bib.deserializeContent(bib);
        Assert.assertEquals(bib.getId(), bibMarc.getId());
        SearchParams searchParams = new SearchParams();
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title"));
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField("bibliographic", "id", bibMarc.getId()), "OR"));
        SearchResponse searchResponse = restClient.search(searchParams);
        Assert.assertEquals("Thankfulness to Almighty God", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue());
    }

    @Ignore
    @Test
    public void createBibUnicode() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(input);
        bibMarc.setId("");
        restClient.createBib(bibMarc);
        Assert.assertNotNull(bibMarc.getId());
        Bib bib = restClient.retrieveBib(bibMarc.getId());
        System.out.println(bib.getContent());
      //  Bib bib = bibtree.getBib();
    }

    @Ignore
    @Test
    public void updateBib() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);

        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(input);
        restClient.createBib(bibMarc);
        bibMarc = restClient.retrieveBib(bibMarc.getId());
        setDate(bibMarc);
        updateContent(bibMarc);
        restClient.updateBib(bibMarc);
        bibMarc = restClient.retrieveBib(bibMarc.getId());
        Assert.assertNotNull(bibMarc.getId());
        Assert.assertEquals("The Alchemist", bibMarc.getTitle());

        SearchParams searchParams = new SearchParams();
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title"));
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField("bibliographic", "id", bibMarc.getId()), "OR"));
        SearchResponse searchResponse = restClient.search(searchParams);
        Assert.assertEquals("The Alchemist", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue());
    }

    @Ignore
    @Test
    public void updateBibs() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);

        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(input);
        restClient.createBib(bibMarc);
        bibMarc = restClient.retrieveBib(bibMarc.getId());
        setDate(bibMarc);
        updateContent(bibMarc);
        List<Bib> bibs = new ArrayList<>();
        bibs.add(bibMarc);
       // restClient.updateBibs(bibs);
        bibMarc = restClient.retrieveBib(bibMarc.getId());
        Assert.assertNotNull(bibMarc.getId());
        Assert.assertEquals("The Alchemist", bibMarc.getTitle());

        SearchParams searchParams = new SearchParams();
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic", "Title"));
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND", searchParams.buildSearchField("bibliographic", "id", bibMarc.getId()), "OR"));
        SearchResponse searchResponse = restClient.search(searchParams);
        Assert.assertEquals("The Alchemist", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue());
    }

    @Ignore
    @Test
    public void retrieveBib() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);

        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(input);
        restClient.createBib(bibMarc);

        Bib bib = restClient.retrieveBib(bibMarc.getId());
        bib = (Bib) bib.deserializeContent(bib);
        Assert.assertEquals(bibMarc.getId(), bib.getId());
    }

    @Ignore
    @Test
    public void retrieveBibs() {
        List<String> bibIds = new ArrayList<String>();
        bibIds.add("wbm-10000001");
        bibIds.add("wbm-10000002");
        List<Bib> bibs = restClient.retrieveBibs(bibIds);
        Assert.assertNotNull(bibs.get(0));
        Assert.assertNotNull(bibs.get(1));

    }

    @Ignore
    @Test
    public void createBibTree() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibTree1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        BibTree bibTree = new BibTree();
        bibTree = (BibTree) bibTree.deserialize(input);
        restClient.createBibTree(bibTree);
        Assert.assertNotNull(bibTree.getBib().getId());
        Map map = new HashMap();
        map.put(DOC_TYPE, "bibliographic");
        map.put("ID", bibTree.getBib().getId());
        BibTree retrievedBibTree = restClient.findBibTree(map);
        Map holdingsMap = new HashMap();
        holdingsMap.put("ID", retrievedBibTree.getHoldingsTrees().get(0).getHoldings().getId());
        holdingsMap.put(DOC_TYPE, "holdings");
        HoldingsTree holdingsTree = restClient.findHoldingsTree(holdingsMap);
        Assert.assertNotNull(holdingsTree.getHoldings().getId());
        Assert.assertNotNull(holdingsTree.getItems().get(0).getId());
        Assert.assertEquals(bibTree.getBib().getId(), retrievedBibTree.getBib().getId());
        Assert.assertEquals(retrievedBibTree.getHoldingsTrees().get(0).getHoldings().getId(), holdingsTree.getHoldings().getId());
    }

    @Ignore
    @Test
    public void retrieveBibTree() {
        BibTree bibTree = restClient.retrieveBibTree("wbm-10000003");
        Assert.assertEquals(bibTree.getBib().getId(), "wbm-10000003");
    }

    @Ignore
    @Test
    public void findBib() {
        createBib();
        Map map = new HashMap();
        map.put(DOC_TYPE, "bibliographic");
        map.put("ID", "wbm-10000001");
        Bib bib = restClient.findBib(map);
        Assert.assertEquals(bib.getId(), "wbm-10000001");
    }

    @Ignore
    @Test
    public void findBibTree() {
        createBib();
        Map map = new HashMap();
        map.put(DOC_TYPE, "bibliographic");
        map.put("ID", "wbm-10000001");
        BibTree bibTree = restClient.findBibTree(map);
        Assert.assertEquals(bibTree.getBib().getId(), "wbm-10000001");
    }

    @Ignore
    @Test
    public void deleteBib() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);

        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(input);
        restClient.createBib(bibMarc);

        restClient.deleteBib(bibMarc.getId());
    }

    @Ignore
    @Test
    public void deleteBibs() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);

        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(input);
        restClient.createBib(bibMarc);

        List bibIds = new ArrayList<>();
        bibIds.add(bibMarc.getId());
        restClient.deleteBibs(bibIds);

    }

    @Ignore
    @Test
    public void testCreateEHoldings() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);

        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(input);
        restClient.createBib(bibMarc);

        try {
            file = new File(getClass().getResource("/documents/EHoldings1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Holdings holdings = new EHoldings();
        holdings = (EHoldings) holdings.deserialize(input);
        holdings.setBib(bibMarc);
        restClient.createHoldings(holdings);

        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        CallNumber callNumber = oleHoldings.getCallNumber();

        Assert.assertNotNull(holdings.getId());

        Holdings holdings1 = restClient.retrieveHoldings(holdings.getId());
        OleHoldings oleHoldings1 = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        CallNumber callNumber1 = oleHoldings1.getCallNumber();
        Assert.assertEquals(callNumber.getNumber(),callNumber1.getNumber());

    }

    @Ignore
    @Test
    public void testUpdateEHoldings() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/EHoldings1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Holdings holdings = new EHoldings();
        holdings = (EHoldings) holdings.deserialize(input);
        restClient.createHoldings(holdings);
        Assert.assertNotNull(holdings.getId());
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        oleHoldings.setCopyNumber("1234567");
        holdings.setContent(holdingOlemlRecordProcessor.toXML(oleHoldings));
        restClient.updateHoldings(holdings);
        Holdings holdings1 = restClient.retrieveHoldings(holdings.getId());
        Assert.assertEquals(holdings1.getId(), holdings.getId());
        Assert.assertNotNull(holdings.getId());
    }

    @Ignore
    @Test
    public void testCreatePHoldings() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/PHoldings1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Holdings holdings = new PHoldings();
        holdings = (PHoldings) holdings.deserialize(input);
        restClient.createHoldings(holdings);
        Assert.assertNotNull(holdings.getId());
    }

    @Ignore
    @Test
    public void testUpdatePHoldings() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/PHoldings1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Holdings holdings = new PHoldings();
        holdings = (PHoldings) holdings.deserialize(input);
        restClient.createHoldings(holdings);
        Assert.assertNotNull(holdings.getId());
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        oleHoldings.setCopyNumber("1234567");
        holdings.setContent(holdingOlemlRecordProcessor.toXML(oleHoldings));
        restClient.updateHoldings(holdings);
        Holdings holdings1 = restClient.retrieveHoldings(holdings.getId());
        Assert.assertEquals(holdings1.getId(), holdings.getId());
        Assert.assertNotNull(holdings.getId());
    }

    @Ignore
    @Test
    public void retrievePHoldings() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/PHoldings1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Holdings holdings = new PHoldings();
        holdings = (PHoldings) holdings.deserialize(input);
        restClient.createHoldings(holdings);
        Assert.assertNotNull(holdings.getId());


        Holdings holdings1 = restClient.retrieveHoldings(holdings.getId());
        Assert.assertEquals(holdings1.getId(), holdings.getId());
    }

    @Ignore
    @Test
    public void retrieveEHoldings() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/EHoldings1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Holdings holdings = new EHoldings();
        holdings = (EHoldings) holdings.deserialize(input);
        restClient.createHoldings(holdings);
        Assert.assertNotNull(holdings.getId());
        Holdings holdings1 = restClient.retrieveHoldings(holdings.getId());
        Assert.assertEquals(holdings1.getId(), holdings.getId());
    }

    @Ignore
    @Test
    public void createHoldingsTree() {
        String bibInput = "";
        File bibFile = null;
        String holdingsInput = "";
        File holdingsFile = null;
        try {
            bibFile = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            bibInput = FileUtils.readFileToString(bibFile);
            holdingsFile = new File(getClass().getResource("/documents/HoldingsTree2.xml").toURI());
            holdingsInput = FileUtils.readFileToString(holdingsFile);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        HoldingsTree holdingsTree = new HoldingsTree();
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(bibInput);
        bibMarc.setId("");
        restClient.createBib(bibMarc);
        holdingsTree = (HoldingsTree) holdingsTree.deserialize(holdingsInput);
        holdingsTree.getHoldings().setBib(bibMarc);
        restClient.createHoldingsTree(holdingsTree);
        HoldingsTree retrievedHoldingsTree = restClient.retrieveHoldingsTree(holdingsTree.getHoldings().getId());
        Assert.assertNotNull(retrievedHoldingsTree);
        Assert.assertEquals(holdingsTree.getHoldings().getId(), retrievedHoldingsTree.getHoldings().getId());
    }

    @Ignore
    @Test
    public void retrieveHoldingsTree() {
        HoldingsTree holdingsTree = new HoldingsTree();
        holdingsTree = restClient.retrieveHoldingsTree("who-1");
        Assert.assertEquals(holdingsTree.getHoldings().getId(), "who-1");
    }

    @Ignore
    @Test
    public void findHoldings() {
        testCreatePHoldings();
        Map map = new HashMap();
        map.put("ID", "who-1");
        map.put(DOC_TYPE, "holdings");
        Holdings holdings = restClient.findHoldings(map);
        Assert.assertEquals(holdings.getId(), "who-1");

    }

    @Ignore
    @Test
    public void findHoldingsTree() {
        testCreatePHoldings();
        Map map = new HashMap();
        map.put("ID", "who-1");
        map.put(DOC_TYPE, "holdings");
        HoldingsTree holdingsTree = restClient.findHoldingsTree(map);
        Assert.assertEquals(holdingsTree.getHoldings().getId(), "who-1");

    }

    private HoldingsTree getHoldingsTreeRecord() {
        HoldingsTree holdingsTree = new HoldingsTree();
        holdingsTree.setHoldings(getHoldingsRecord());
        holdingsTree.getItems().add(getItemRecord());
        return holdingsTree;
    }

    private Bib getBibRecord() {
        Bib bib = new Bib();
        bib.setCategory(DocCategory.WORK.getCode());
        bib.setType(DocType.BIB.getCode());
        bib.setFormat(DocFormat.MARC.getCode());
        bib.setId("wbm-10000001");
        return bib;
    }

    private Item getItemRecord() {
        Item item = new ItemOleml();
        item.setCategory("work");
        item.setType("item");
        item.setFormat("oleml");
        org.kuali.ole.docstore.common.document.content.instance.Item item1 = new org.kuali.ole.docstore.common.document.content.instance.Item();
        item1.setVolumeNumber("123");
        item1.setChronology("12344");
        item1.setEnumeration("en");
        item1.setBarcodeARSL("bararsl");
        item.setContent(new ItemOlemlRecordProcessor().toXML(item1));
        return item;
    }

    private Holdings getHoldingsRecord() {
        Holdings holdings = new PHoldings();
        holdings.setCategory(DocCategory.WORK.getCode());
        holdings.setType(DocType.HOLDINGS.getCode());
        holdings.setFormat(DocFormat.OLEML.getCode());

        OleHoldings oleHoldings = new OleHoldings();
        ShelvingScheme shelvingScheme = new ShelvingScheme();
        shelvingScheme.setCodeValue("LCC");
        CallNumber callNumber = new CallNumber();
        callNumber.setNumber("1234");
        callNumber.setShelvingScheme(shelvingScheme);
        oleHoldings.setCallNumber(callNumber);
        holdings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
        holdings.setBib(getBibRecord());
        return holdings;
    }

    @Ignore
    @Test
    public void deletePHoldings() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/PHoldings1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Holdings holdings = new PHoldings();
        holdings = (PHoldings) holdings.deserialize(input);
        restClient.createHoldings(holdings);
        Assert.assertNotNull(holdings.getId());
        restClient.deleteHoldings(holdings.getId());

    }

    @Ignore
    @Test
    public void deleteEHoldings() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/EHoldings1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Holdings holdings = new EHoldings();
        holdings = (EHoldings) holdings.deserialize(input);
        restClient.createHoldings(holdings);
        Assert.assertNotNull(holdings.getId());
        restClient.deleteHoldings(holdings.getId());

    }

    @Ignore
    @Test
    public void testTransferHoldings() {
        /*List<String> holdingsIds =new ArrayList<String>();
        String bibId="wbm-10000005";
        holdingsIds.add("who-5".toString());*/
        String bibId = "wbm-10000005";
        String holdingsId = "who-5";
        List holdingIds = new ArrayList<String>();
        holdingIds.add(holdingsId);
        restClient.transferHoldings(holdingIds, bibId);

    }

    @Ignore
    @Test
    public void testBrowseHoldings() {
        BrowseParams browseParams = getBrowseParams1();
        SearchResponse searchResponse = restClient.browseHoldings(browseParams);
        Assert.assertNotNull(searchResponse);
    }

    public BrowseParams getBrowseParams1() {
        BrowseParams browseParams = new BrowseParams();
        String xml = getXmlAsString("/org/kuali/ole/browse/BrowseParams1.xml");
        return (BrowseParams) browseParams.deserialize(xml);
    }

    public String getXmlAsString(String filePath) {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource(filePath).toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        return input;
    }

    @Ignore
    @Test
    public void createItem() {
        Item item = getItemRecord();
        String serializeXml = item.serialize(item);
        RestResponse response = restClient.postRequest(serializeXml, "items/doc/");
        Assert.assertEquals(200, response.getResponse().getStatusLine().getStatusCode());
        Assert.assertNotNull(response.getResponseBody());
    }

    @Ignore
    @Test
    public void retrieveBibContent() {
        Bib bib1 = getBibRecord1();
        restClient.createBib(bib1);
        Bib bib2 = getBibRecord2();
        restClient.createBib(bib2);
        List<String> bibIds = new ArrayList<>();
        bibIds.add(bib1.getId());
        bibIds.add(bib2.getId());
        BibMarcRecords bibMarcRecords = restClient.retrieveBibContent(bibIds);
        Assert.assertNotNull(bibMarcRecords);
        Assert.assertEquals(DocumentUniqueIDPrefix.getDocumentId(bib1.getId()), bibMarcRecords.getRecords().get(0).getControlFields().get(0).getValue());
        Assert.assertEquals(DocumentUniqueIDPrefix.getDocumentId(bib2.getId()), bibMarcRecords.getRecords().get(1).getControlFields().get(0).getValue());
    }

    @Ignore
    @Test
    public void retrieveItemByBarcode() {
        BibTree bibTree = getBibTree1();
        restClient.createBibTree(bibTree);
        String barcode = bibTree.getHoldingsTrees().get(0).getItems().get(0).getBarcode();
        Item item1 = restClient.retrieveItemByBarcode(barcode);
        Assert.assertNotNull(item1);
    }

    @Ignore
    @Test
    public void patchItem() {
        Item item = getItemDoc();
        restClient.createItem(item);
        String itemXML = item.serialize(item);
        String itemDoc = restClient.patchItem(itemXML);
        item = (Item) item.deserialize(itemDoc);
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item itemContent = itemOlemlRecordProcessor.fromXML(item.getContent());
        Assert.assertEquals("testcn3", itemContent.getCopyNumber());
        Assert.assertEquals("testenum3", itemContent.getEnumeration());
        Assert.assertEquals("testchn3", itemContent.getChronology());
    }

    @Ignore
    @Test
    public void updateItemByBarcode() {
        Item item = getItemDoc();
        restClient.createItem(item);
        String itemXML = item.serialize(item);
        String itemDoc = restClient.updateItemByBarcode(item.getBarcode(), itemXML);
        item = (Item) item.deserialize(itemDoc);
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item itemContent = itemOlemlRecordProcessor.fromXML(item.getContent());
        Assert.assertEquals("testcn3", itemContent.getCopyNumber());
        Assert.assertEquals("testenum3", itemContent.getEnumeration());
        Assert.assertEquals("testchn3", itemContent.getChronology());
    }

    private Item getItemDoc() {
        Item item = new Item();
        String xml = getXmlAsString("/org/kuali/ole/patch/ItemDoc1.xml");
        return (Item) item.deserialize(xml);
    }

    private Bib getBibRecord1() {
        Bib bib = new BibMarc();
        String xml = getXmlAsString("/org/kuali/ole/documents/BibMarcRecord1.xml");
        return (Bib) bib.deserialize(xml);
    }

    private Bib getBibRecord2() {
        Bib bib = new BibMarc();
        String xml = getXmlAsString("/org/kuali/ole/documents/BibMarcRecord2.xml");
        return (Bib) bib.deserialize(xml);
    }

    private BibTree getBibTree1() {
        BibTree bibTree = new BibTree();
        String xml = getXmlAsString("/org/kuali/ole/documents/BibTree1.xml");
        return (BibTree) bibTree.deserialize(xml);
    }

    @Ignore
    @Test
    public void updateItem() {
        Item item = getItemRecord();
        item.setId("wio-5");
        String serializeXml = item.serialize(item);
        RestResponse response = restClient.putRequest(serializeXml, "items/doc/");
        Assert.assertEquals(200, response.getResponse().getStatusLine().getStatusCode());
        Assert.assertNotNull(response.getResponseBody());
    }

    @Ignore
    @Test
    public void retrieveItem() {
        Item item = restClient.retrieveItem("wio-20");
        Assert.assertEquals(item.getId(), "wio-20");
    }

    @Ignore
    @Test
    public void retrieveItems() {
        List<String> itemIds = new ArrayList<>();
        itemIds.add("wio-1");
        itemIds.add("wio-2");
        List<Item> items = restClient.retrieveItems(itemIds);
        Assert.assertNotNull(items.get(0));
        Assert.assertNotNull(items.get(1));
    }

    @Ignore
    @Test
    public void findItem() {
        createItem();
        Map map = new HashMap();
        map.put("ID", "wio-1");
        map.put(DOC_TYPE, "item");
        Item item = restClient.findItem(map);
        Assert.assertEquals(item.getId(), "wio-1");
    }

    @Ignore
    @Test
    public void deleteItem() {
        createItem();
        restClient.deleteItem("wio-4");

    }

    @Ignore
    @Test
    public void testBrowseItems() {
        BrowseParams browseParams = getBrowseParams2();
        SearchResponse searchResponse = restClient.browseItems(browseParams);
        Assert.assertNotNull(searchResponse);
    }

    public BrowseParams getBrowseParams2() {
        BrowseParams browseParams = new BrowseParams();
        String xml = getXmlAsString("/org/kuali/ole/browse/BrowseParams2.xml");
        return (BrowseParams) browseParams.deserialize(xml);
    }

    @Ignore
    @Test
    public void testSearch1() {
        SearchParams searchParams = getSearchParams1();
        SearchResponse searchResponse = restClient.search(searchParams);
        Assert.assertNotNull(searchResponse);
    }

    public SearchParams getSearchParams1() {
        SearchParams searchParams = new SearchParams();
        String xml = getXmlAsString("/org/kuali/ole/search/SearchParams1.xml");
        return (SearchParams) searchParams.deserialize(xml);
    }

    @Ignore
    @Test
    public void testSearch2() {
        SearchParams searchParams = getSearchParams2();
        SearchResponse searchResponse = restClient.search(searchParams);
        Assert.assertNotNull(searchResponse);
    }

    public SearchParams getSearchParams2() {
        SearchParams searchParams = new SearchParams();
        String xml = getXmlAsString("/org/kuali/ole/search/SearchParams2.xml");
        return (SearchParams) searchParams.deserialize(xml);
    }

    @Ignore
    @Test
    public void testBoundHoldingsWithBibs() {
        String bibId1 = "wbm-10000001";
        BibTree bibTree1 = restClient.retrieveBibTree(bibId1);
        System.out.println("Before Boundwith Bib id: " + bibId1 + " Holdings count: " + bibTree1.getHoldingsTrees().size());
        String bibId2 = "wbm-10000002";
        BibTree bibTree2 = restClient.retrieveBibTree(bibId2);
        System.out.println("Before Boundwith Bib id: " + bibId2 + " Holdings count: " + bibTree2.getHoldingsTrees().size());
        String holdingsId = "who-1";
        List bibIds = new ArrayList<String>();
        bibIds.add(bibId2);
        restClient.boundWithBibs(holdingsId, bibIds);
        bibTree1 = restClient.retrieveBibTree(bibId1);
        System.out.println("After Boundwith Bib id: " + bibId1 + " Holdings count: " + bibTree1.getHoldingsTrees().size());
        bibTree2 = restClient.retrieveBibTree(bibId2);
        System.out.println("After Boundwith Bib id: " + bibId2 + " Holdings count: " + bibTree2.getHoldingsTrees().size());
    }

    @Ignore
    @Test
    public void testTransferItem() {
        String holdingsId = "who-2";
        String itemId = "wio-6";
        List itemIds = new ArrayList<String>();
        itemIds.add(itemId);
        restClient.transferItems(itemIds,holdingsId);


    }

    private void updateContent(Bib bib){
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(bib.getContent());
        BibMarcRecord bibMarcRecord = bibMarcRecords.getRecords().get(0);
        List<DataField> dataFields = bibMarcRecord.getDataFields();
        for(DataField dataField : dataFields){
            if(dataField.getTag().equalsIgnoreCase("245")){
                List<SubField> subFields = dataField.getSubFields();
                subFields.get(0).setValue("The Alchemist");
            }
        }
        bib.setContent(bibMarcRecordProcessor.generateXML(bibMarcRecord));
    }

    @Ignore
    @Test
    public void createBibTrees() {
        List bibTrees = new ArrayList();
        BibTree bibTree = new BibTree();
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibTree1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        bibTree = (BibTree) bibTree.deserialize(input);
        bibTrees.add(bibTree);
        try {
            file = new File(getClass().getResource("/documents/BibTree2.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        bibTree = (BibTree) bibTree.deserialize(input);
        bibTrees.add(bibTree);
        try {
            file = new File(getClass().getResource("/documents/BibTree3.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        bibTree = (BibTree) bibTree.deserialize(input);
        bibTrees.add(bibTree);
        BibTree bibTree1=new BibTree();


        BibTrees bibTreesobj = new BibTrees();
        Bib bibMarc = new Bib();
        bibTreesobj.getBibTrees().addAll(bibTrees);
        Assert.assertNotNull(bibTreesobj);
    }


    private void setDate(Bib bib) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        bib.setCreatedOn(dateStr);
    }

   /* @Ignore
    @Test
    public void testCreateLicenseAttachment() {
        LicenseAttachment license = new LicenseAttachment();
        license.setCategory("work");
        license.setType("license");
        license.setFormat("pdf");
        license.setFileName("document.pdf");
        license.setFilePath("/home/likewise-open/HTCINDIA/sambasivam/Downloads");
        Licenses licenses = new Licenses();
        licenses.getLicenses().add(license);
        licenses.getLicenses().add(license);
        restClient.createLicenses(licenses);
    }*/

    @Ignore
    @Test
    public void testUpdate() {
        LicenseAttachment license = new LicenseAttachment();
        license.setCategory("work");
        license.setType("license");
        license.setFormat("pdf");
        license.setId("wl-1");
        license.setDocumentNote("doc note");
        license.setDocumentTitle("doc title");
        restClient.updateLicense(license);
    }

    @Ignore
    @Test
    public void testLicenseDelete() {
        restClient.deleteLicense("wl-1");
    }

    @Test
    public void testUpdateAndSearch1() throws Exception {

        TestUpdateAndSearch testUpdateAndSearch = new TestUpdateAndSearch();
        testUpdateAndSearch.testUpdateAndSearch();
    }



    private void searchBibWithTitleNId(String bibId, String title) {
        SearchParams searchParams = new SearchParams();
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(), "id", bibId), "AND"));
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("phrase", searchParams.buildSearchField(DocType.BIB.getCode(), "Title_search", title), "AND"));
        SearchResponse response = restClient.search(searchParams);
        System.out.println("no. of bib with id " + bibId + " and with title " + title + " is " + response.getSearchResults().size());
        System.out.println("time taken to search is " + response.getTime());
    }
    private void searchBibWithTitle(String title) {
        SearchParams searchParams = new SearchParams();
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(), "Title_search", title), "AND"));
        SearchResponse response = restClient.search(searchParams);
        System.out.println("Search result count " + response.getTotalRecordCount() + " and  time taken to search is " + response.getTime());
    }
    private void searchBib() {
        SearchParams searchParams = new SearchParams();
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("", searchParams.buildSearchField(DocType.BIB.getCode(), "DocType", DocType.BIB.getCode()), "AND"));
        SearchResponse response = restClient.search(searchParams);
        System.out.println("Search result count " + response.getTotalRecordCount() + " and  time taken to search is " + response.getTime());

    }

    public void searchBibWithFacets(String facetSort) throws Exception {
        String[] facets = {"Author_facet", "Format_facet", "Genre_facet", "Language_facet", "PublicationDate_facet", "PublicationDate_sort"};
        String[] fieldList = {"LocalId_display", "Title_sort", "Title_display", "JournalTitle_display", "Author_display", "Publisher_display", "ISBN_display", "ISSN_display", "Subject_display", "Publisher_display", "PublicationDate_display", "Edition_display", "Format_display", "Language_display", "Description_display", "FormGenre_display", "DocFormat", "staffOnlyFlag", "bibIdentifier", "holdingsIdentifier"};
        String args = "(DocType:bibliographic)AND((*:*))";
        SolrServer solr = SolrServerManager.getInstance().getSolrServer();
        SolrQuery query = new SolrQuery();
        query.setQuery(args);
        query.setFacet(true);
        query.addFacetField(facets);
        query.setFacetMinCount(1);
        query.setFacetSort(facetSort);
        query.setFacetLimit(5);
        query.setFields(fieldList);
        query.set("facet.offset", "0");
        QueryResponse response = solr.query(query);
        System.out.println("Search result count " + response.getResults().getNumFound() + " and  time taken to search is " + response.getQTime());
    }


    @Test
    public void testUpdateAndSearchBib() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Create bib");
        Bib bib = createBibRecord();
        stopWatch.stop();

        String bibId = bib.getId();

        stopWatch.start("Search before create bib");
        searchBibWithTitleNId(bibId, "Thankfulness to Almighty God");
        stopWatch.stop();

        stopWatch.start("Update bib");
        updateTitle(bibId);
        stopWatch.stop();

        stopWatch.start("Search after update bib");
        searchBibWithTitleNId(bibId, "wings of fire");
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());

    }

    private void updateTitle(String bibId) {

        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);

        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(input);
        bibMarc.setId(bibId);
        restClient.updateBib(bibMarc);

    }

    @Test
    public void testSearchBibBlankSearch() {
        searchBib();
        Bib bib = createBibRecord();
        String bibId = bib.getId();
        updateTitle(bibId);
        System.out.println(" after update");
        searchBib();
    }

    @Test
    public void testSearchBibWithTitle() {
        searchBibWithTitle("wings");
        Bib bib = createBibRecord();
        String bibId = bib.getId();
        updateTitle(bibId);
        searchBibWithTitle("wings");
    }


    @Test
    public void testSearchBibWithFacets() throws Exception {
        searchBibWithTitle("wings");
        Bib bib = createBibRecord();
        String bibId = bib.getId();
        updateTitle(bibId);
        searchBibWithTitle("wings");
    }

    private Bib createBibRecord() {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.info("Exception :", e);
        }
        Bib bibMarc = new Bib();
        bibMarc = (Bib) bibMarc.deserialize(input);
        bibMarc.setId("");
        restClient.createBib(bibMarc);
        return bibMarc;
    }

}
