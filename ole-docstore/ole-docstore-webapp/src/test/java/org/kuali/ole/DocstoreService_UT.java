package org.kuali.ole;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;
import org.kuali.ole.docstore.common.exception.DocstoreValidationException;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.discovery.service.SolrServerManager;
import org.kuali.ole.docstore.engine.service.search.DocstoreSolrSearchService;
import org.kuali.ole.docstore.engine.service.storage.rdbms.RdbmsItemDocumentManager;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.service.BeanLocator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/16/13
 * Time: 6:17 PM
 * To change this template use File | Settings | File Templates.
 */



public class DocstoreService_UT extends SpringBaseTestCase {

    DocstoreService docstoreService = BeanLocator.getDocstoreService();

    @Test
    public void testCreateBib() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibMarc1.xml");
        Bib bib = new BibMarc();
        bib = (Bib) bib.deserialize(xml);
        bib.setId(null);
        docstoreService.createBib(bib);
        Assert.assertNotNull(bib.getId());
    }

    @Test
    public void testCreateBibFails() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibMarc1.xml");
        Bib bib = new BibMarc();
        bib = (Bib) bib.deserialize(xml);
        bib.setId(null);
        docstoreService.createBib(bib);

        String xml2 = getXmlAsString("/org/kuali/ole/documents/BibMarc1.xml");
        Bib bib2 = new BibMarc();
        bib2 = (Bib) bib2.deserialize(xml2);
        bib2.setId(bib.getId());
        try {
            docstoreService.createBib(bib2);
        }
        catch (Exception e) {
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                Assert.assertNotNull(docstoreException.getErrorCode());
            } else {
                Assert.assertNotNull(e.getMessage());
            }
        }
    }

    @Test
    public void testCreatePHoldings() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibMarc1.xml");
        Bib bib = new BibMarc();
        bib = (Bib) bib.deserialize(xml);
        bib.setId(null);
        docstoreService.createBib(bib);
        Assert.assertNotNull(bib.getId());

        xml = getXmlAsString("/org/kuali/ole/documents/PHoldings1.xml");
        Holdings holdings = new PHoldings();
        holdings = (PHoldings) holdings.deserialize(xml);
        holdings.setBib(bib);
        docstoreService.createHoldings(holdings);

        Assert.assertNotNull(holdings.getId());
    }

    @Test
    public void testCreatePHoldingsFails() {
        Bib bib = getBibRecord();
        docstoreService.createBib(bib);
        Holdings holdings = getHoldingsRecord();
        holdings.setBib(null);
        try {
            docstoreService.createHoldings(holdings);
        }
        catch (Exception e) {
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                Assert.assertNotNull(docstoreException.getErrorCode());
            } else {
                Assert.assertNotNull(e.getMessage());
            }
        }
    }


    @Test
    public void testCreateEHoldings() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibMarc1.xml");
        Bib bib = new BibMarc();
        bib = (Bib) bib.deserialize(xml);
        bib.setId(null);
        docstoreService.createBib(bib);
        Assert.assertNotNull(bib.getId());

        xml = getXmlAsString("/org/kuali/ole/documents/EHoldings1.xml");
        Holdings holdings = new EHoldings();
        holdings = (PHoldings) holdings.deserialize(xml);
        holdings.setBib(bib);
        docstoreService.createHoldings(holdings);

        Assert.assertNotNull(holdings.getId());
    }

    @Test
    public void testCreateEHoldingsFails() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibMarc1.xml");
        Bib bib = new BibMarc();
        bib = (Bib) bib.deserialize(xml);
        bib.setId(null);
        docstoreService.createBib(bib);
        Assert.assertNotNull(bib.getId());

        xml = getXmlAsString("/org/kuali/ole/documents/EHoldings1.xml");
        Holdings holdings = new EHoldings();
        holdings = (PHoldings) holdings.deserialize(xml);
        holdings.setBib(null);
        try {
            docstoreService.createHoldings(holdings);
        }
        catch (Exception e) {
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                Assert.assertNotNull(docstoreException.getErrorCode());
            } else {
                Assert.assertNotNull(e.getMessage());
            }
        }
    }
    @Test
    public void testCreateItem() {
        Bib bib = getBibRecord();
        docstoreService.createBib(bib);

        Holdings holdings = getHoldingsRecord();
        holdings.setBib(bib);
        docstoreService.createHoldings(holdings);
        Item item = getItemRecord();
        item.setHolding(holdings);
        docstoreService.createItem(item);
        Assert.assertNotNull(item.getId());
    }

    @Test
    public void testCreateItemFails() {
        Bib bib = getBibRecord();
        docstoreService.createBib(bib);

        Holdings holdings = getHoldingsRecord();
        holdings.setBib(bib);
        docstoreService.createHoldings(holdings);
        Item item = getItemRecord();
        item.setHolding(null);
         try {
            docstoreService.createItem(item);
         }
         catch (Exception e) {
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                 Assert.assertNotNull(docstoreException.getErrorCode());
            }else {
                 Assert.assertNotNull(e.getMessage());
             }
         }

    }

    @Test
    public void testCreateHoldingsTree() {
        HoldingsTree holdingsTree = getHoldingsTreeRecord();
        Bib bib = getBibRecord();
        docstoreService.createBib(bib);
        holdingsTree.getHoldings().setBib(bib);
        docstoreService.createHoldingsTree(holdingsTree);
        Assert.assertNotNull(holdingsTree.getHoldings().getId());
        Assert.assertNotNull(holdingsTree.getItems().get(0).getId());
        Holdings holdings = docstoreService.retrieveHoldings(holdingsTree.getHoldings().getId());
        Item item = docstoreService.retrieveItem(holdingsTree.getItems().get(0).getId());
        Assert.assertEquals(holdingsTree.getHoldings().getId(), holdings.getId());
        Assert.assertEquals(holdingsTree.getItems().get(0).getId(), item.getId());
    }


    @Test
    public void testCreateHoldingsTreeFails() {
        HoldingsTree holdingsTree = getHoldingsTreeRecord();
        holdingsTree.getHoldings().setBib(null);


        try {
            docstoreService.createHoldingsTree(holdingsTree);
        }
        catch (Exception e) {
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                Assert.assertNotNull(docstoreException.getErrorCode());
            }else {
                Assert.assertNotNull(e.getMessage());
            }
        }

    }

    @Test
    public void testCreateBibTree() {
        BibTree bibTree = getBibTree();
        docstoreService.createBibTree(bibTree);
        Assert.assertNotNull(bibTree.getBib().getId());
        HoldingsTree holdingsTree = bibTree.getHoldingsTrees().get(0);
        Assert.assertNotNull(holdingsTree.getHoldings().getId());
        Assert.assertNotNull(holdingsTree.getItems().get(0).getId());
        Map map = new HashMap();
        map.put(DOC_TYPE, "bibliographic");
        map.put("ID", bibTree.getBib().getId());
        BibTree retrievedBibTree = docstoreService.findBibTree(map);
        Map holdingsMap = new HashMap();
        holdingsMap.put("ID", holdingsTree.getHoldings().getId());
        holdingsMap.put(DOC_TYPE, "holdings");
        HoldingsTree retrievedHoldingsTree = docstoreService.findHoldingsTree(holdingsMap);
        Assert.assertEquals(bibTree.getBib().getId(), retrievedBibTree.getBib().getId());
        Assert.assertEquals(holdingsTree.getHoldings().getId(), retrievedHoldingsTree.getHoldings().getId());
    }

    @Test
    public void testCreateBibTreeFails() {
        BibTree bibTree = getBibTree();
        docstoreService.createBibTree(bibTree);

        BibTree bibTree2 = getBibTree();
        bibTree2.getBib().setId(bibTree.getBib().getId());


        try {
            docstoreService.createBibTree(bibTree2);
        }
        catch (Exception e) {
            DocstoreException docstoreException = (DocstoreException) e;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                Assert.assertNotNull(docstoreException.getErrorCode());
            }else {
                Assert.assertNotNull(e.getMessage());
            }
        }

    }

    private HoldingsTree getHoldingsTreeRecord() {
        HoldingsTree holdingsTree = new HoldingsTree();
        holdingsTree.setHoldings(getHoldingsRecord());
        List<Item> items = new ArrayList<Item>(0);
        Item item = getItemRecord();
        item.setHolding(holdingsTree.getHoldings());
        items.add(item);
        holdingsTree.getItems().addAll(items);
        return holdingsTree;
    }

    private Item getItemRecord() {
        Item item = new ItemOleml();
        item.setCategory("work");
        item.setType("item");
        item.setFormat("oleml");
        org.kuali.ole.docstore.common.document.content.instance.Item item1 = new org.kuali.ole.docstore.common.document.content.instance.Item();
        item1.setVolumeNumber("12345");
        ShelvingScheme shelvingScheme = new ShelvingScheme();
        shelvingScheme.setCodeValue("LCC");

        CallNumber callNumber = new CallNumber();
        callNumber.setNumber("1234");
        callNumber.setShelvingScheme(shelvingScheme);
        item1.setCallNumber(callNumber);
        item1.setChronology("1234");
        item1.setEnumeration("en");
        item1.setBarcodeARSL("bararsl");
        Location location = new Location();
        LocationLevel locationLevel1 = new LocationLevel();
        locationLevel1.setName("ARCH");
        locationLevel1.setLevel("1");
        LocationLevel locationLevel2 = new LocationLevel();
        locationLevel2.setName("PA");
        locationLevel2.setLevel("2");
        location.setLocationLevel(locationLevel1);
        item1.setLocation(location);
        item.setContent(new ItemOlemlRecordProcessor().toXML(item1));
        return item;
    }


    @Test
    public void testRetrieveBib() {
//        Bib bib = getBibRecord();
//        docstoreService.createBib(bib);
        Bib retrievedBib = docstoreService.retrieveBib("wbm-10000001");
        Assert.assertNotNull(retrievedBib);
//        Assert.assertEquals(bib.getId(), retrievedBib.getId());
    }

    @Test
    public void testRetrieveBibFails() {

       Bib retrievedBib = null;
       String bibId = "";
        try {
            retrievedBib = docstoreService.retrieveBib(bibId);
        }
        catch (Exception e) {
            DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.BIB_ID_NOT_FOUND, DocstoreResources.BIB_ID_NOT_FOUND);
            docstoreException.addErrorParams("bibId", bibId );
            throw docstoreException;
        }
    }

    @Test
    public void testRetrieveBibs() {
        Bib bib = getBibRecord();
        docstoreService.createBib(bib);
        List<String> bibIds = new ArrayList<String>();
        bibIds.add(bib.getId());
        List<Bib> retrievedBibs = docstoreService.retrieveBibs(bibIds);
        Assert.assertNotNull(retrievedBibs);
        Assert.assertEquals(bib.getId(), retrievedBibs.get(0).getId());

    }

    @Test
    public void testUpdateBib() {
        Bib bib = getBibRecord();
        docstoreService.createBib(bib);
        Bib retrievedBib = docstoreService.retrieveBib(bib.getId());
        Assert.assertNotNull(retrievedBib);
        updateContent(retrievedBib);
        setDate(retrievedBib);
        docstoreService.updateBib(retrievedBib);
        retrievedBib = docstoreService.retrieveBib(retrievedBib.getId());
        if (bib.getId().equalsIgnoreCase(retrievedBib.getId()))
            Assert.assertEquals("The Alchemist", retrievedBib.getTitle());
        else
            Assert.fail();
    }

    @Test
    public void testUpdateBibs() {
        Bib bib = getBibRecord();
        docstoreService.createBib(bib);
        List<String> bibIds = new ArrayList<String>();
        bibIds.add(bib.getId());
        Bib retrievedBib = docstoreService.retrieveBib(bib.getId());
        Assert.assertNotNull(retrievedBib);
        updateContent(retrievedBib);
        setDate(retrievedBib);
        docstoreService.updateBib(retrievedBib);
        List<Bib> bibs = new ArrayList<Bib>();
        bibs.add(retrievedBib);
        docstoreService.updateBibs(bibs);
        retrievedBib = docstoreService.retrieveBib(retrievedBib.getId());
        if (bib.getId().equalsIgnoreCase(retrievedBib.getId()))
            Assert.assertEquals("The Alchemist", retrievedBib.getTitle());
        else
            Assert.fail();
    }

    @Test
    public void testRetrieveHoldings() {
        String holdingsId = "";
        Holdings holdings = docstoreService.retrieveHoldings(holdingsId);
        OleHoldings oleHoldings = (OleHoldings) holdings.getContentObject();
        Assert.assertNotNull(holdings);
    }
 @Test
    public void testRetrieveHoldingsFails() {
        String holdingsId = "";
     Holdings holdings = null;
     try {
        holdings = docstoreService.retrieveHoldings(holdingsId);
     }
     catch (Exception e) {
         DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.HOLDING_ID_NOT_FOUND, DocstoreResources.HOLDING_ID_NOT_FOUND);
         docstoreException.addErrorParams("holdingsId", holdingsId );
         throw docstoreException;
     }

    }

    @Test
    public void testRetrieveItem() {
        String itemId = "";
        Item item = docstoreService.retrieveItem(itemId);
        item.getCallNumber();
        item.getHolding().getBib().getAuthor();
        Assert.assertNotNull(item);
    }
  @Test
    public void testRetrieveItemFails() {
        String itemId = null;
      Item item = null;
      try {
        item= docstoreService.retrieveItem(itemId);
      }
      catch (Exception e) {
          DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.ITEM_ID_NOT_FOUND, DocstoreResources.ITEM_ID_NOT_FOUND);
          docstoreException.addErrorParams("itemId", itemId );
          throw docstoreException;
      }
    }

    @Test
    public void testRetrieveItems() {
        List<String> itemIds = new ArrayList<String>();
        itemIds.add("1");
        itemIds.add("2");
        List<Item> items = docstoreService.retrieveItems(itemIds);
        for (Item item : items) {
            item.getCallNumber();
            item.getHolding().getBib().getAuthor();
            Assert.assertNotNull(item);
        }
    }

    @Test
    public void testRetrieveHoldingsTree() {
        String holdingsId = "";
        HoldingsTree holdingsTree = docstoreService.retrieveHoldingsTree(holdingsId);
        Assert.assertNotNull(holdingsTree);
    }

    @Test
    public void testRetrieveBibTree() {
        String bibId = "wbm-10000001";
        BibTree bibTree = docstoreService.retrieveBibTree(bibId);
        Assert.assertNotNull(bibTree);
    }

    @Test
    public void testSortHoldings() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibTree1.xml");
        BibTree bibTree = new BibTree();
        String strHoldings = new String();
        bibTree = (BibTree) bibTree.deserialize(xml);

        docstoreService.createBibTree(bibTree);
        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        System.out.println("BEFORE SORTING HOLDINGS:");
        System.out.println("HOLDINGS : Loc | ShlOrd");
        for(HoldingsTree holdingsTree :bibTree.getHoldingsTrees()){
            OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdingsTree.getHoldings().getContent());
//            System.out.println(strHoldings.format("%s | %s", Holdings.buildLocationString(oleHoldings.getLocation()), oleHoldings.getCallNumber().getShelvingOrder().getCodeValue() + "\n"));
        }
        bibTree = docstoreService.retrieveBibTree(bibTree.getBib().getId());
        System.out.println("AFTER SORTING HOLDINGS:");
        System.out.println("HOLDINGS : Loc | ShlOrd ");
        for(HoldingsTree holdingsTree :bibTree.getHoldingsTrees()){
            OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdingsTree.getHoldings().getContent());
//            System.out.println(strHoldings.format("%s | %s", Holdings.buildLocationString(oleHoldings.getLocation()), oleHoldings.getCallNumber().getShelvingOrder().getCodeValue() + "\n"));

        }
    }


    @Test
    public void testSortItems() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibTree1.xml");
        BibTree bibTree = new BibTree();
        String strItems = new String();
        bibTree = (BibTree) bibTree.deserialize(xml);
        docstoreService.createBibTree(bibTree);
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();

        System.out.println("BEFORE SORTING ITEMS:");
        for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
            System.out.println("***HOLDINGS***");
            System.out.println("ITEMS : Loc | Prefix | ShlOrd | Enum | Chro | Copy | bar");
            for (Item item : holdingsTree.getItems()) {
                org.kuali.ole.docstore.common.document.content.instance.Item item1 = itemOlemlRecordProcessor.fromXML(item.getContent());
//                System.out.println(strItems.format("%s | %s | %s | %s | %s | %s | %s", Holdings.buildLocationString(item1.getLocation()), item1.getCallNumber().getPrefix(),
//                        item1.getCallNumber().getShelvingOrder().getCodeValue(), item1.getEnumeration(), item1.getChronology(), item1.getCopyNumber(), item1.getAccessInformation().getBarcode() + "\n"));
            }
        }
        bibTree = docstoreService.retrieveBibTree(bibTree.getBib().getId());
        System.out.println("AFTER SORTING ITEMS:");
        for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
            System.out.println("***HOLDINGS***");
            System.out.println("ITEMS : Loc | Prefix | ShlOrd | Enum | Chro | Copy | bar");
            for (Item item : holdingsTree.getItems()) {
                org.kuali.ole.docstore.common.document.content.instance.Item item1 = itemOlemlRecordProcessor.fromXML(item.getContent());
//                System.out.println(strItems.format("%s | %s | %s | %s | %s | %s | %s", Holdings.buildLocationString(item1.getLocation()), item1.getCallNumber().getPrefix(),
//                        item1.getCallNumber().getShelvingOrder().getCodeValue(), item1.getEnumeration(), item1.getChronology(), item1.getCopyNumber(), item1.getAccessInformation().getBarcode() + "\n"));
            }
        }
    }



    /*@Test
    public void testSortItems() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibTree1.xml");
        BibTree bibTree = new BibTree();
        bibTree = (BibTree) bibTree.deserialize(xml);
        docstoreService.createBibTree(bibTree);
        bibTree = docstoreService.retrieveBibTree(bibTree.getBib().getId());
        String serialize = bibTree.serialize(bibTree);
        serialize = ParseXml.formatXml(serialize);
        System.out.println(serialize);

    }*/


    @Test
    public void createBibTrees2() {
        List bibTrees = new ArrayList();
        BibTree bibTree = new BibTree();
        String xml = getXmlAsString("/org/kuali/ole/search/BibTree1.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree2.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree3.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree4.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree5.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree6.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree7.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree8.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree9.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree10.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree11.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree12.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree13.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree14.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/search/BibTree15.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        BibTrees bibTreesobj = new BibTrees();
        bibTreesobj.getBibTrees().add(bibTree);
        bibTreesobj.getBibTrees().addAll(bibTrees);
        docstoreService.createBibTrees(bibTreesobj);
    }

    @Test
    public void testSearch1() {
        Bib bib = getBibMarc1();
        docstoreService.createBib(bib);
        Assert.assertNotNull(bib.getId());
        SearchParams searchParams = getSearchParams1();
        SearchField searchField = searchParams.buildSearchField("bibliographic", "bibIdentifier", bib.getId());
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND", searchField, "AND"));

        Assert.assertEquals("bibliographic", searchParams.getSearchConditions().get(0).getSearchField().getDocType());
        Assert.assertEquals("Title", searchParams.getSearchConditions().get(0).getSearchField().getFieldName());
        Assert.assertEquals("This Title is for Test1", searchParams.getSearchConditions().get(0).getSearchField().getFieldValue());
        Assert.assertEquals("bibliographic", searchParams.getSearchConditions().get(1).getSearchField().getDocType());
        Assert.assertEquals("Author", searchParams.getSearchConditions().get(1).getSearchField().getFieldName());
        Assert.assertEquals("This Author is for Test1", searchParams.getSearchConditions().get(1).getSearchField().getFieldValue());
        Assert.assertEquals("bibliographic", searchParams.getSearchConditions().get(2).getSearchField().getDocType());
        Assert.assertEquals("bibIdentifier", searchParams.getSearchConditions().get(2).getSearchField().getFieldName());
        Assert.assertEquals(bib.getId(), searchParams.getSearchConditions().get(2).getSearchField().getFieldValue());

        Assert.assertEquals("bibliographic", searchParams.getSearchResultFields().get(0).getDocType());
        Assert.assertEquals("Title", searchParams.getSearchResultFields().get(0).getFieldName());
        Assert.assertEquals("bibliographic", searchParams.getSearchResultFields().get(1).getDocType());
        Assert.assertEquals("Author", searchParams.getSearchResultFields().get(1).getFieldName());

        SearchResponse searchResponse = docstoreService.search(searchParams);

        Assert.assertNotNull(searchResponse);
        Assert.assertNotNull(searchResponse.getSearchResults());
        Assert.assertNotNull(searchResponse.getSearchResults().get(0).getSearchResultFields());

        Assert.assertEquals("bibliographic", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getDocType());
        Assert.assertEquals("Title", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldName());
        Assert.assertEquals("This Title is for Test1", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue());
        Assert.assertEquals("bibliographic", searchResponse.getSearchResults().get(0).getSearchResultFields().get(1).getDocType());
        Assert.assertEquals("Author", searchResponse.getSearchResults().get(0).getSearchResultFields().get(1).getFieldName());
        Assert.assertEquals("This Author is for Test1", searchResponse.getSearchResults().get(0).getSearchResultFields().get(1).getFieldValue());
    }

    public Bib getBibMarc1() {
        Bib bib = new BibMarc();
        return (Bib) bib.deserialize(getXmlAsString("/org/kuali/ole/search/BibMarc1.xml"));
    }

    @Test
    public void testSearch2() {
        BibTree bibTree = getBibTree1();
        docstoreService.createBibTree(bibTree);
        Assert.assertNotNull(bibTree.getBib().getId());
        SearchParams searchParams = getSearchParams2();
        SearchField searchField = searchParams.buildSearchField("bibliographic", "bibIdentifier", bibTree.getBib().getId());
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND", searchField, "AND"));

        Assert.assertEquals("bibliographic", searchParams.getSearchConditions().get(0).getSearchField().getDocType());
        Assert.assertEquals("Title", searchParams.getSearchConditions().get(0).getSearchField().getFieldName());
        Assert.assertEquals("This Title is for Test2", searchParams.getSearchConditions().get(0).getSearchField().getFieldValue());
        Assert.assertEquals("bibliographic", searchParams.getSearchConditions().get(1).getSearchField().getDocType());
        Assert.assertEquals("bibIdentifier", searchParams.getSearchConditions().get(1).getSearchField().getFieldName());
        Assert.assertEquals(bibTree.getBib().getId(), searchParams.getSearchConditions().get(1).getSearchField().getFieldValue());

        Assert.assertEquals("bibliographic", searchParams.getSearchResultFields().get(0).getDocType());
        Assert.assertEquals("Title", searchParams.getSearchResultFields().get(0).getFieldName());
        Assert.assertEquals("holdings", searchParams.getSearchResultFields().get(1).getDocType());
        Assert.assertEquals("CallNumber", searchParams.getSearchResultFields().get(1).getFieldName());
        Assert.assertEquals("item", searchParams.getSearchResultFields().get(2).getDocType());
        Assert.assertEquals("CallNumber", searchParams.getSearchResultFields().get(2).getFieldName());
        Assert.assertEquals("item", searchParams.getSearchResultFields().get(3).getDocType());
        Assert.assertEquals("chronology", searchParams.getSearchResultFields().get(3).getFieldName());
        Assert.assertEquals("item", searchParams.getSearchResultFields().get(4).getDocType());
        Assert.assertEquals("enumeration", searchParams.getSearchResultFields().get(4).getFieldName());

        SearchResponse searchResponse = docstoreService.search(searchParams);

        Assert.assertNotNull(searchResponse);
        Assert.assertNotNull(searchResponse.getSearchResults());
        Assert.assertNotNull(searchResponse.getSearchResults().get(0).getSearchResultFields());

        Assert.assertEquals("bibliographic", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getDocType());
        Assert.assertEquals("Title", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldName());
        Assert.assertEquals("This Title is for Test2", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue());
        Assert.assertEquals("holdings", searchResponse.getSearchResults().get(0).getSearchResultFields().get(1).getDocType());
        Assert.assertEquals("CallNumber", searchResponse.getSearchResults().get(0).getSearchResultFields().get(1).getFieldName());
        Assert.assertEquals("CN Holdings Test2", searchResponse.getSearchResults().get(0).getSearchResultFields().get(1).getFieldValue());
        Assert.assertEquals("item", searchResponse.getSearchResults().get(0).getSearchResultFields().get(2).getDocType());
        Assert.assertEquals("CallNumber", searchResponse.getSearchResults().get(0).getSearchResultFields().get(2).getFieldName());
        Assert.assertEquals("CN Item Test2", searchResponse.getSearchResults().get(0).getSearchResultFields().get(2).getFieldValue());
        Assert.assertEquals("item", searchResponse.getSearchResults().get(0).getSearchResultFields().get(3).getDocType());
        Assert.assertEquals("chronology", searchResponse.getSearchResults().get(0).getSearchResultFields().get(3).getFieldName());
        Assert.assertEquals("CHN Item Test2", searchResponse.getSearchResults().get(0).getSearchResultFields().get(3).getFieldValue());
        Assert.assertEquals("item", searchResponse.getSearchResults().get(0).getSearchResultFields().get(4).getDocType());
        Assert.assertEquals("enumeration", searchResponse.getSearchResults().get(0).getSearchResultFields().get(4).getFieldName());
        Assert.assertEquals("ENUM Item Test2", searchResponse.getSearchResults().get(0).getSearchResultFields().get(4).getFieldValue());
    }

    public BibTree getBibTree1() {
        BibTree bibTree = new BibTree();
        return (BibTree) bibTree.deserialize(getXmlAsString("/org/kuali/ole/search/BibTree1.xml"));
    }

    @Test
    public void testSearch3() {
        BibTree bibTree = getBibTree2();
        docstoreService.createBibTree(bibTree);
        Assert.assertNotNull(bibTree.getHoldingsTrees().get(0).getHoldings().getId());
        SearchParams searchParams = getSearchParams3();
        SearchField searchField = searchParams.buildSearchField("holdings", "holdingsIdentifier", bibTree.getHoldingsTrees().get(0).getHoldings().getId());
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND", searchField, "AND"));

        Assert.assertEquals("holdings", searchParams.getSearchConditions().get(0).getSearchField().getDocType());
        Assert.assertEquals("CallNumber", searchParams.getSearchConditions().get(0).getSearchField().getFieldName());
        Assert.assertEquals("CN Holdings Test3", searchParams.getSearchConditions().get(0).getSearchField().getFieldValue());
        Assert.assertEquals("holdings", searchParams.getSearchConditions().get(1).getSearchField().getDocType());
        Assert.assertEquals("ShelvingOrder_search", searchParams.getSearchConditions().get(1).getSearchField().getFieldName());
        Assert.assertEquals("CN-Holdings-Test3", searchParams.getSearchConditions().get(1).getSearchField().getFieldValue());
        Assert.assertEquals("holdings", searchParams.getSearchConditions().get(2).getSearchField().getDocType());
        Assert.assertEquals("holdingsIdentifier", searchParams.getSearchConditions().get(2).getSearchField().getFieldName());
        Assert.assertEquals(bibTree.getHoldingsTrees().get(0).getHoldings().getId(), searchParams.getSearchConditions().get(2).getSearchField().getFieldValue());

        Assert.assertEquals("holdings", searchParams.getSearchResultFields().get(0).getDocType());
        Assert.assertEquals("CallNumber", searchParams.getSearchResultFields().get(0).getFieldName());
        Assert.assertEquals("item", searchParams.getSearchResultFields().get(1).getDocType());
        Assert.assertEquals("CallNumber", searchParams.getSearchResultFields().get(1).getFieldName());
        Assert.assertEquals("item", searchParams.getSearchResultFields().get(2).getDocType());
        Assert.assertEquals("chronology", searchParams.getSearchResultFields().get(2).getFieldName());
        Assert.assertEquals("item", searchParams.getSearchResultFields().get(3).getDocType());
        Assert.assertEquals("enumeration", searchParams.getSearchResultFields().get(3).getFieldName());
        Assert.assertEquals("bibliographic", searchParams.getSearchResultFields().get(4).getDocType());
        Assert.assertEquals("Title", searchParams.getSearchResultFields().get(4).getFieldName());

        SearchResponse searchResponse = docstoreService.search(searchParams);

        Assert.assertNotNull(searchResponse);
        Assert.assertNotNull(searchResponse.getSearchResults());
        Assert.assertNotNull(searchResponse.getSearchResults().get(0).getSearchResultFields());

        Assert.assertEquals("holdings", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getDocType());
        Assert.assertEquals("CallNumber", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldName());
        Assert.assertEquals("CN Holdings Test3", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue());
        Assert.assertEquals("item", searchResponse.getSearchResults().get(0).getSearchResultFields().get(1).getDocType());
        Assert.assertEquals("CallNumber", searchResponse.getSearchResults().get(0).getSearchResultFields().get(1).getFieldName());
        Assert.assertEquals("CN Item Test3", searchResponse.getSearchResults().get(0).getSearchResultFields().get(1).getFieldValue());
        Assert.assertEquals("item", searchResponse.getSearchResults().get(0).getSearchResultFields().get(2).getDocType());
        Assert.assertEquals("chronology", searchResponse.getSearchResults().get(0).getSearchResultFields().get(2).getFieldName());
        Assert.assertEquals("CHN Item Test3", searchResponse.getSearchResults().get(0).getSearchResultFields().get(2).getFieldValue());
        Assert.assertEquals("item", searchResponse.getSearchResults().get(0).getSearchResultFields().get(3).getDocType());
        Assert.assertEquals("enumeration", searchResponse.getSearchResults().get(0).getSearchResultFields().get(3).getFieldName());
        Assert.assertEquals("ENUM Item Test3", searchResponse.getSearchResults().get(0).getSearchResultFields().get(3).getFieldValue());
        Assert.assertEquals("bibliographic", searchResponse.getSearchResults().get(0).getSearchResultFields().get(4).getDocType());
        Assert.assertEquals("Title", searchResponse.getSearchResults().get(0).getSearchResultFields().get(4).getFieldName());
        Assert.assertEquals("This Title is for Test3", searchResponse.getSearchResults().get(0).getSearchResultFields().get(4).getFieldValue());
    }

    public BibTree getBibTree2() {
        BibTree bibTree = new BibTree();
        return (BibTree) bibTree.deserialize(getXmlAsString("/org/kuali/ole/search/BibTree2.xml"));
    }

    @Test
    public void testSearch4() {
        BibTree bibTree = getBibTree3();
        docstoreService.createBibTree(bibTree);
        Assert.assertNotNull(bibTree.getHoldingsTrees().get(0).getItems().get(0).getId());
        SearchParams searchParams = getSearchParams4();
        SearchField searchField = searchParams.buildSearchField("item", "itemIdentifier", bibTree.getHoldingsTrees().get(0).getItems().get(0).getId());
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("AND", searchField, "AND"));

        Assert.assertEquals("item", searchParams.getSearchConditions().get(0).getSearchField().getDocType());
        Assert.assertEquals("enumeration", searchParams.getSearchConditions().get(0).getSearchField().getFieldName());
        Assert.assertEquals("ENUM Item Test4", searchParams.getSearchConditions().get(0).getSearchField().getFieldValue());
        Assert.assertEquals("item", searchParams.getSearchConditions().get(1).getSearchField().getDocType());
        Assert.assertEquals("itemIdentifier", searchParams.getSearchConditions().get(1).getSearchField().getFieldName());
        Assert.assertEquals(bibTree.getHoldingsTrees().get(0).getItems().get(0).getId(), searchParams.getSearchConditions().get(1).getSearchField().getFieldValue());

        Assert.assertEquals("holdings", searchParams.getSearchResultFields().get(0).getDocType());
        Assert.assertEquals("CallNumber", searchParams.getSearchResultFields().get(0).getFieldName());
        Assert.assertEquals("item", searchParams.getSearchResultFields().get(1).getDocType());
        Assert.assertEquals("CallNumber", searchParams.getSearchResultFields().get(1).getFieldName());
        Assert.assertEquals("item", searchParams.getSearchResultFields().get(2).getDocType());
        Assert.assertEquals("chronology", searchParams.getSearchResultFields().get(2).getFieldName());
        Assert.assertEquals("item", searchParams.getSearchResultFields().get(3).getDocType());
        Assert.assertEquals("enumeration", searchParams.getSearchResultFields().get(3).getFieldName());
        Assert.assertEquals("bibliographic", searchParams.getSearchResultFields().get(4).getDocType());
        Assert.assertEquals("Title", searchParams.getSearchResultFields().get(4).getFieldName());

        SearchResponse searchResponse = docstoreService.search(searchParams);

        Assert.assertNotNull(searchResponse);
        Assert.assertNotNull(searchResponse.getSearchResults());
        Assert.assertNotNull(searchResponse.getSearchResults().get(0).getSearchResultFields());

        Assert.assertEquals("holdings", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getDocType());
        Assert.assertEquals("CallNumber", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldName());
        Assert.assertEquals("CN Holdings Test4", searchResponse.getSearchResults().get(0).getSearchResultFields().get(0).getFieldValue());
        Assert.assertEquals("item", searchResponse.getSearchResults().get(0).getSearchResultFields().get(1).getDocType());
        Assert.assertEquals("CallNumber", searchResponse.getSearchResults().get(0).getSearchResultFields().get(1).getFieldName());
        Assert.assertEquals("CN Item Test4", searchResponse.getSearchResults().get(0).getSearchResultFields().get(1).getFieldValue());
        Assert.assertEquals("item", searchResponse.getSearchResults().get(0).getSearchResultFields().get(2).getDocType());
        Assert.assertEquals("chronology", searchResponse.getSearchResults().get(0).getSearchResultFields().get(2).getFieldName());
        Assert.assertEquals("CHN Item Test4", searchResponse.getSearchResults().get(0).getSearchResultFields().get(2).getFieldValue());
        Assert.assertEquals("item", searchResponse.getSearchResults().get(0).getSearchResultFields().get(3).getDocType());
        Assert.assertEquals("enumeration", searchResponse.getSearchResults().get(0).getSearchResultFields().get(3).getFieldName());
        Assert.assertEquals("ENUM Item Test4", searchResponse.getSearchResults().get(0).getSearchResultFields().get(3).getFieldValue());
        Assert.assertEquals("bibliographic", searchResponse.getSearchResults().get(0).getSearchResultFields().get(4).getDocType());
        Assert.assertEquals("Title", searchResponse.getSearchResults().get(0).getSearchResultFields().get(4).getFieldName());
        Assert.assertEquals("This Title is for Test4", searchResponse.getSearchResults().get(0).getSearchResultFields().get(4).getFieldValue());
    }

    public BibTree getBibTree3() {
        BibTree bibTree = new BibTree();
        return (BibTree) bibTree.deserialize(getXmlAsString("/org/kuali/ole/search/BibTree3.xml"));
    }

    @Test
    public void testSearch5() {
        createBibTrees2();
        SearchParams searchParams = getSearchParams5();

        Assert.assertEquals("bibliographic", searchParams.getSearchConditions().get(0).getSearchField().getDocType());

        Assert.assertEquals("bibliographic", searchParams.getSearchResultFields().get(0).getDocType());
        Assert.assertEquals("bibIdentifier", searchParams.getSearchResultFields().get(0).getFieldName());
        Assert.assertEquals("bibliographic", searchParams.getSearchResultFields().get(1).getDocType());
        Assert.assertEquals("Title", searchParams.getSearchResultFields().get(1).getFieldName());
        Assert.assertEquals("0", String.valueOf(searchParams.getStartIndex()));
        Assert.assertEquals("10", String.valueOf(searchParams.getPageSize()));

        SearchResponse searchResponse1 = docstoreService.search(searchParams);

        Assert.assertNotNull(searchResponse1);
        Assert.assertNotNull(searchResponse1.getSearchResults());
        Assert.assertNotNull(searchResponse1.getSearchResults().get(0).getSearchResultFields());

        Assert.assertTrue(searchResponse1.getTotalRecordCount() > 10);
        Assert.assertNotNull(searchResponse1.getTotalRecordCount());
        Assert.assertEquals("0", String.valueOf(searchResponse1.getStartIndex()));
        Assert.assertEquals("10", String.valueOf(searchResponse1.getPageSize()));
        Assert.assertEquals(searchResponse1.getStartIndex() + searchResponse1.getPageSize(), searchResponse1.getEndIndex());

        searchParams.setStartIndex(10);
        searchParams.setPageSize(10);

        SearchResponse searchResponse2 = docstoreService.search(searchParams);

        Assert.assertNotNull(searchResponse2);
        Assert.assertNotNull(searchResponse2.getSearchResults());
        Assert.assertNotNull(searchResponse2.getSearchResults().get(0).getSearchResultFields());

        Assert.assertTrue(searchResponse2.getTotalRecordCount() > 10);
        Assert.assertNotNull(searchResponse2.getTotalRecordCount());
        Assert.assertEquals("10", String.valueOf(searchResponse2.getStartIndex()));
        Assert.assertEquals("10", String.valueOf(searchResponse2.getPageSize()));
        Assert.assertEquals(searchResponse1.getStartIndex() + searchResponse1.getPageSize(), searchResponse1.getEndIndex());
    }

    public SearchParams getSearchParams1() {
        SearchParams searchParams = new SearchParams();
        String xml = getXmlAsString("/org/kuali/ole/search/SearchParams1.xml");
        return (SearchParams) searchParams.deserialize(xml);
    }

    public SearchParams getSearchParams2() {
        SearchParams searchParams = new SearchParams();
        String xml = getXmlAsString("/org/kuali/ole/search/SearchParams2.xml");
        return (SearchParams) searchParams.deserialize(xml);
    }

    public SearchParams getSearchParams3() {
        SearchParams searchParams = new SearchParams();
        String xml = getXmlAsString("/org/kuali/ole/search/SearchParams3.xml");
        return (SearchParams) searchParams.deserialize(xml);
    }

    public SearchParams getSearchParams4() {
        SearchParams searchParams = new SearchParams();
        String xml = getXmlAsString("/org/kuali/ole/search/SearchParams4.xml");
        return (SearchParams) searchParams.deserialize(xml);
    }

    public SearchParams getSearchParams5() {
        SearchParams searchParams = new SearchParams();
        String xml = getXmlAsString("/org/kuali/ole/search/SearchParams5.xml");
        return (SearchParams) searchParams.deserialize(xml);
    }

    @Test
    public void createBibTrees1() {
        List bibTrees = new ArrayList();
        BibTree bibTree = new BibTree();
        String xml = getXmlAsString("/org/kuali/ole/documents/BibTree1.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/documents/BibTree2.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/documents/BibTree3.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/documents/BibTree4.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        bibTree = new BibTree();
        xml = getXmlAsString("/org/kuali/ole/documents/BibTree5.xml");
        bibTree = (BibTree) bibTree.deserialize(xml);
        bibTrees.add(bibTree);
        BibTrees bibTreesobj = new BibTrees();
        //bibTreesobj.getBibTrees().add(bibTree);
        bibTreesobj.getBibTrees().addAll(bibTrees);
        docstoreService.createBibTrees(bibTreesobj);
    }

    @Test
    public void testBoundHoldingsWithBibs() {
        String bibId1 = "wbm-10000001";
        BibTree bibTree1 = docstoreService.retrieveBibTree(bibId1);
        System.out.println("Before Boundwith Bib id: " + bibId1 + " Holdings count: " + bibTree1.getHoldingsTrees().size());
        String bibId2 = "wbm-10000002";
        BibTree bibTree2 = docstoreService.retrieveBibTree(bibId2);
        System.out.println("Before Boundwith Bib id: " + bibId2 + " Holdings count: " + bibTree2.getHoldingsTrees().size());
        String holdingsId = "who-1";
        List bibIds = new ArrayList<String>();
        bibIds.add(bibId2);
        docstoreService.boundHoldingsWithBibs(holdingsId, bibIds);
        bibTree1 = docstoreService.retrieveBibTree(bibId1);
        System.out.println("After Boundwith Bib id: " + bibId1 + " Holdings count: " + bibTree1.getHoldingsTrees().size());
        bibTree2 = docstoreService.retrieveBibTree(bibId2);
        System.out.println("After Boundwith Bib id: " + bibId2 + " Holdings count: " + bibTree2.getHoldingsTrees().size());
    }

    @Test
    public void transferHoldings() {
        String bibId = "wbm-10000001";
        String holdingsId = "who-2";
        List holdingIds = new ArrayList<String>();
        holdingIds.add(holdingsId);
        docstoreService.transferHoldings(holdingIds, bibId);
    }

    @Test
    public void transferItems() {
        String holdingId = "wbm-10000001";
        String itemId = "who-2";
        List itemIds = new ArrayList<String>();
        itemIds.add(itemId);
        docstoreService.transferItems(itemIds, holdingId);
    }

    @Test
    public void findBib() {
        testCreateBib();
        Map map = new HashMap();
        map.put(DOC_TYPE, "bibliographic");
        map.put("ID", "wbm-10000001");
        Bib bib = docstoreService.findBib(map);
        Assert.assertEquals(bib.getId(), "wbm-10000001");
    }

    @Test
    public void findBibTree() {
        testCreateBib();
        Map map = new HashMap();
        map.put(DOC_TYPE, "bibliographic");
        map.put("ID", "wbm-10000001");
        BibTree bibTree = docstoreService.findBibTree(map);
        Assert.assertEquals(bibTree.getBib().getId(), "wbm-10000001");
    }

    @Test
    public void findHoldings() {
        testCreatePHoldings();
        Map map = new HashMap();
        map.put("ID", "who-1");
        map.put(DOC_TYPE, "holdings");
        Holdings holdings = docstoreService.findHoldings(map);
        Assert.assertEquals(holdings.getId(), "who-1");

    }

    @Test
    public void findHoldingsTree() {
        testCreatePHoldings();
        Map map = new HashMap();
        map.put("ID", "who-1");
        map.put(DOC_TYPE, "holdings");
        HoldingsTree holdingsTree = docstoreService.findHoldingsTree(map);
        Assert.assertEquals(holdingsTree.getHoldings().getId(), "who-1");

    }

    @Test
    public void findItem() {
        testCreateItem();
        Map map = new HashMap();
        map.put("ID", "wio-1");
        map.put(DOC_TYPE, "item");
        Item item = docstoreService.findItem(map);
        Assert.assertEquals(item.getId(), "wio-1");
    }

    @Test
    public void deleteBibs() {
        testCreateBib();
        List bibIds = new ArrayList();
        bibIds.add("wbm-10000001");
        docstoreService.deleteBibs(bibIds);
    }

    public String getXmlAsString(String filePath) {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource(filePath).toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    public Bib getBibRecord() {
        Bib bib = new BibMarc();
        bib.setCategory(DocCategory.WORK.getCode());
        bib.setType(DocType.BIB.getCode());
        bib.setFormat(DocFormat.MARC.getCode());
        bib.setContent("<collection xmlns=\"http://www.loc.gov/MARC21/slim\">\n" +
                "    <record>\n" +
                "        <leader>#####nam#a22######a#4500</leader>\n" +
                "\t<controlfield tag=\"001\"></controlfield>\n" +
                "        <controlfield tag=\"003\">OCoLC</controlfield>\n" +
                "        <controlfield tag=\"005\">20090213152530.7</controlfield>\n" +
                "\t\t<controlfield tag=\"008\">131031s########xxu###########000#0#eng#d</controlfield>\n" +
                "        <datafield tag=\"035\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">(OCoLC)ocm62378465</subfield>\n" +
                "        </datafield>\n" +
                "        <datafield tag=\"040\" ind1=\" \" ind2=\" \">\n" +
                "             <subfield code=\"a\">DLC</subfield>\n" +
                "\t\t\t <subfield code=\"c\">DLC</subfield>\n" +
                "\t\t\t <subfield code=\"d\">DLC</subfield>\n" +
                "\t\t\t <subfield code=\"d\">HLS</subfield>\n" +
                "\t\t\t <subfield code=\"d\">IUL</subfield>\n" +
                "        </datafield>\n" +
                "         <datafield tag=\"022\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">1729-1070|20</subfield>\n" +
                "        </datafield>\n" +
                "        <datafield tag=\"029\" ind1=\"1\" ind2=\" \">\n" +
                "            <subfield code=\"a\">AU@|b000040176476</subfield>\n" +
                "\t\t\t<subfield code=\"b\">000040176476</subfield>\n" +
                "        </datafield>\n" +
                "        <datafield tag=\"037\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"b\">The Managing Editor, BIAC Journal, P.O. Box 10026, Gaborone, Botswana</subfield>\n" +
                "        </datafield>\n" +
                "        <datafield tag=\"042\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">lc</subfield>\n" +
                "        </datafield>\n" +
                "        <datafield tag=\"043\" ind1=\"1\" ind2=\"0\">\n" +
                "            <subfield code=\"a\">f-bs---</subfield>\n" +
                "        </datafield>\n" +
                "        <datafield tag=\"050\" ind1=\"0\" ind2=\"0\">\n" +
                "            <subfield code=\"a\">HD70.B55|bB53</subfield>\n" +
                "        </datafield>\n" +
                "        <datafield tag=\"049\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">IULA</subfield>\n" +
                "        </datafield>\n" +
                "         <datafield tag=\"210\" ind1=\"1\" ind2=\" \">\n" +
                "            <subfield code=\"a\">BIAC j.</subfield>\n" +
                "        </datafield>\n" +
                "         <datafield tag=\"222\" ind1=\" \" ind2=\"0\">\n" +
                "            <subfield code=\"a\">BIAC journal</subfield>\n" +
                "        </datafield>\n" +
                "         <datafield tag=\"245\" ind1=\"0\" ind2=\"0\">\n" +
                "            <subfield code=\"a\">BIAC journal</subfield>\n" +
                "        </datafield>\n" +
                "         <datafield tag=\"246\" ind1=\"1\" ind2=\"3\">\n" +
                "            <subfield code=\"a\">Botswana Institute of Administration and Commerce journal</subfield>\n" +
                "        </datafield>\n" +
                "        <datafield tag=\"260\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">Gaborone, Botswana :|bBotswana Institute of Administration and Commerce</subfield>\n" +
                "\t\t\t<subfield code=\"b\">Botswana Institute of Administration and Commerce</subfield>\n" +
                "        </datafield>\n" +
                "        <datafield tag=\"300\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">v. ;</subfield>\n" +
                "\t\t\t<subfield code=\"c\">24 cm.</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"300\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">v. ;</subfield>\n" +
                "\t\t\t<subfield code=\"c\">24 cm.</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"300\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">v. ;</subfield>\n" +
                "\t\t\t<subfield code=\"c\">24 cm.</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"310\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">Semiannual</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"362\" ind1=\"1\" ind2=\" \">\n" +
                "            <subfield code=\"a\"> Began in 2004.</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"500\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">Description based on: Vol. 1, no. 1 (May. 2004); title from cover.</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"500\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">Latest issue consulted: Vol. 3, no. 1 (May 2006).</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"650\" ind1=\"0\" ind2=\" \">\n" +
                "            <subfield code=\"a\">Industrial management</subfield>\n" +
                "\t\t\t<subfield code=\"z\">Botswana</subfield>\n" +
                "\t\t\t<subfield code=\"v\">Periodicals.</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"650\" ind1=\"0\" ind2=\" \">\n" +
                "            <subfield code=\"a\">Occupational training</subfield>\n" +
                "\t\t\t<subfield code=\"z\">Botswana</subfield>\n" +
                "\t\t\t<subfield code=\"v\">Periodicals.</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"710\" ind1=\"2\" ind2=\" \">\n" +
                "            <subfield code=\"a\">Botswana Institute of Administration and Commerce.</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"850\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">DLC</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"891\" ind1=\"2\" ind2=\"0\">\n" +
                "            <subfield code=\"a\">9853|81.1</subfield>\n" +
                "\t\t\t<subfield code=\"a\">v.</subfield>\n" +
                "\t\t\t<subfield code=\"b\">no</subfield>\n" +
                "\t\t\t<subfield code=\"u\">2</subfield>\n" +
                "\t\t\t<subfield code=\"v\">r</subfield>\n" +
                "\t\t\t<subfield code=\"i\">(year)</subfield>\n" +
                "\t\t\t<subfield code=\"j\">(month)</subfield>\n" +
                "\t\t\t<subfield code=\"w\">f</subfield>\n" +
                "\t\t\t<subfield code=\"x\">05</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"891\" ind1=\"4\" ind2=\"1\">\n" +
                "            <subfield code=\"a\">9863|81.1</subfield>\n" +
                "\t\t\t<subfield code=\"a\">1</subfield>\n" +
                "\t\t\t<subfield code=\"b\">1</subfield>\n" +
                "\t\t\t<subfield code=\"i\">2004</subfield>\n" +
                "\t\t\t<subfield code=\"j\">05</subfield>\n" +
                "        </datafield>\n" +
                "\t\t<datafield tag=\"596\" ind1=\" \" ind2=\" \">\n" +
                "            <subfield code=\"a\">1</subfield>\n" +
                "        </datafield>\n" +
                "    </record>\n" +
                "</collection>");
        return bib;
    }

    private Holdings getHoldingsRecord() {
        Holdings holdings = new PHoldings();
//        Holdings holdings = new EHoldingsOleml();
        holdings.setCategory(DocCategory.WORK.getCode());
        holdings.setType(DocType.HOLDINGS.getCode());
        holdings.setFormat(DocFormat.OLEML.getCode());

        OleHoldings oleHoldings = new OleHoldings();
        ShelvingScheme shelvingScheme = new ShelvingScheme();
        shelvingScheme.setCodeValue("LCC");

        CallNumber callNumber = new CallNumber();
        callNumber.setNumber("123");
        callNumber.setShelvingScheme(shelvingScheme);
        oleHoldings.setCallNumber(callNumber);

        ExtentOfOwnership pHoldingsExtentOfOwnership = new ExtentOfOwnership();
        Note note = new Note();
        note.setType("public");
        note.setValue("123");
        pHoldingsExtentOfOwnership.setTextualHoldings("textual holdings");
        pHoldingsExtentOfOwnership.setType("public");
        pHoldingsExtentOfOwnership.getNote().add(note);

        ExtentOfOwnership pHoldingsExtentOfOwnership1 = new ExtentOfOwnership();
        pHoldingsExtentOfOwnership1.setTextualHoldings("textual holdings");
        pHoldingsExtentOfOwnership1.setType("public");
        pHoldingsExtentOfOwnership1.getNote().add(note);

        oleHoldings.getExtentOfOwnership().add(pHoldingsExtentOfOwnership);
        oleHoldings.getExtentOfOwnership().add(pHoldingsExtentOfOwnership1);

        oleHoldings.setReceiptStatus("1");
        oleHoldings.getNote().add(note);
        oleHoldings.getNote().add(note);

        oleHoldings.setCopyNumber("copy1");

        Uri uri = new Uri();
        uri.setValue("1");

        Uri uri1 = new Uri();
        uri1.setValue("2");

        oleHoldings.getUri().add(uri);
        oleHoldings.getUri().add(uri1);


        ExtentOfOwnership eofCoverage = new ExtentOfOwnership();
        Coverages coverages = new Coverages();
        Coverage coverage = new Coverage();
        coverage.setCoverageEndIssue("endIssue");
        coverage.setCoverageStartIssue("startIssue");
        coverages.getCoverage().add(coverage);
        eofCoverage.setCoverages(coverages);

        PerpetualAccesses perpetualAccesses = new PerpetualAccesses();
        PerpetualAccess perpetualAccess = new PerpetualAccess();
        perpetualAccess.setPerpetualAccessEndIssue("end issue");
        perpetualAccess.setPerpetualAccessStartIssue("end issue");
        perpetualAccesses.getPerpetualAccess().add(perpetualAccess);
        eofCoverage.setPerpetualAccesses(perpetualAccesses);
        if (holdings instanceof EHoldings) {
            oleHoldings.getExtentOfOwnership().clear();
            oleHoldings.getExtentOfOwnership().add(eofCoverage);
        }

        oleHoldings.setDonorNote("donor note");
        oleHoldings.setAccessStatus("active");
        StatisticalSearchingCode statisticalSearchingCode = new StatisticalSearchingCode();
        statisticalSearchingCode.setCodeValue("BETA");
        oleHoldings.setStatisticalSearchingCode(statisticalSearchingCode);

        oleHoldings.setDonorPublicDisplay("donor public display");
//        oleHoldings.setStatusDate("");
        oleHoldings.setEResourceId("1001");
        oleHoldings.setStaffOnlyFlag(true);
        oleHoldings.setImprint("imprint");
        oleHoldings.setInterLibraryLoanAllowed(true);
        Link link = new Link();
        link.setText("text");
        link.setUrl("url");
        //oleHoldings.setLink(link);
        oleHoldings.setLocalPersistentLink("local persist link");
        oleHoldings.setPublisher("pub");
        Platform platform = new Platform();
        platform.setAdminPassword("apwd");
        platform.setAdminUrl("aurl");
        platform.setAdminUserName("ausr");
        platform.setPlatformName("platform");
        oleHoldings.setPlatform(platform);

        oleHoldings.setSubscriptionStatus("status");

        HoldingsAccessInformation holdingsAccessInformation = new HoldingsAccessInformation();
        holdingsAccessInformation.setAccessLocation("access location");
        holdingsAccessInformation.setAccessPassword("pwd");
        holdingsAccessInformation.setAccessUsername("usr");
        holdingsAccessInformation.setAuthenticationType("1");
        holdingsAccessInformation.setNumberOfSimultaneousUser("2");
        holdingsAccessInformation.setProxiedResource("proxied");
        oleHoldings.setHoldingsAccessInformation(holdingsAccessInformation);

        holdings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));

        return holdings;
    }

    private BibTree getBibTree() {
        BibTree bibTree = new BibTree();
        bibTree.setBib(getBibRecord());
        HoldingsTree holdingsTree = getHoldingsTree();
        bibTree.getHoldingsTrees().add(holdingsTree);
        return bibTree;
    }

    private HoldingsTree getHoldingsTree() {
        HoldingsTree holdingsTree = new HoldingsTree();
        holdingsTree.setHoldings(getHoldingsRecord());
        holdingsTree.getItems().add(getItemRecord());
        return holdingsTree;
    }

    @Test
    public void testDeleteBib() {
        Bib bib = getBibRecord();
        docstoreService.deleteBib(bib.getId());
    }

    @Test
    public void testDeleteHoldings() {
        Holdings holdings = getHoldingsRecord();
        docstoreService.deleteHoldings(holdings.getId());
    }

    @Test
    public void testDeleteItem() {
        Item item = getItemRecord();
        docstoreService.deleteItem(item.getId());
    }

    @Test
    public void testUpdatePHoldings() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibMarc1.xml");
        Bib bib = new BibMarc();
        bib = (Bib) bib.deserialize(xml);
        bib.setId(null);
        docstoreService.createBib(bib);
        Assert.assertNotNull(bib.getId());

        xml = getXmlAsString("/org/kuali/ole/documents/PHoldings1.xml");
        Holdings holdings = new PHoldings();
        holdings = (PHoldings) holdings.deserialize(xml);
        holdings.setBib(bib);
        docstoreService.createHoldings(holdings);

        Assert.assertNotNull(holdings.getId());


        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        oleHoldings.setCopyNumber("1234567");
        holdings.setContent(holdingOlemlRecordProcessor.toXML(oleHoldings));
        docstoreService.updateHoldings(holdings);

        Holdings holdings1 = docstoreService.retrieveHoldings(holdings.getId());
        Assert.assertEquals(holdings1.getId(), holdings.getId());
        Assert.assertNotNull(holdings.getId());
    }

    @Test
    public void testUpdateEHoldings() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibMarc1.xml");
        Bib bib = new BibMarc();
        bib = (Bib) bib.deserialize(xml);
        bib.setId(null);
        docstoreService.createBib(bib);
        Assert.assertNotNull(bib.getId());

        xml = getXmlAsString("/org/kuali/ole/documents/EHoldings1.xml");
        Holdings holdings = new EHoldings();
        holdings = (EHoldings) holdings.deserialize(xml);
        holdings.setBib(bib);
        docstoreService.createHoldings(holdings);

        Assert.assertNotNull(holdings.getId());


        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        oleHoldings.setCopyNumber("1234567");
        holdings.setContent(holdingOlemlRecordProcessor.toXML(oleHoldings));
        docstoreService.updateHoldings(holdings);

        Holdings holdings1 = docstoreService.retrieveHoldings(holdings.getId());
        Assert.assertEquals(holdings1.getId(), holdings.getId());
        Assert.assertNotNull(holdings.getId());
    }

    @Test
    public void testUpdateItem() {
        Item item = getItemRecord();
        item.setContent("mock update content");
        docstoreService.updateItem(item);
    }

    @Test
    public void testBrowseHoldings1() {
        BrowseParams browseParams = getBrowseParams1();
        SearchResponse searchResponse = docstoreService.browseHoldings(browseParams);
        Assert.assertNotNull(searchResponse);
    }

    @Test
    public void testBrowseHoldings2() {
        BrowseParams browseParams = getBrowseParams2();
        SearchResponse searchResponse = docstoreService.browseHoldings(browseParams);
        Assert.assertNotNull(searchResponse);
    }

    @Test
    public void testBrowseItems1() {
        BrowseParams browseParams = getBrowseParams3();
        SearchResponse searchResponse = docstoreService.browseItems(browseParams);
        Assert.assertNotNull(searchResponse);
    }

    @Test
    public void testBrowseItems2() {
        BrowseParams browseParams = getBrowseParams4();
        SearchResponse searchResponse = docstoreService.browseItems(browseParams);
        Assert.assertNotNull(searchResponse);
    }


    @Test
    public void testCreateDc() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibDUnqualified1.xml");
        Bib bibDcUnqualified = new BibDcUnqualified();
        bibDcUnqualified.setCategory("work");
        bibDcUnqualified.setType(DocType.BIB.getCode());
        bibDcUnqualified.setFormat(DocFormat.DUBLIN_UNQUALIFIED.getCode());
        bibDcUnqualified = (BibDcUnqualified) bibDcUnqualified.deserialize(xml);
        docstoreService.createBib(bibDcUnqualified);
        Assert.assertNotNull(bibDcUnqualified.getId());
    }

    public BrowseParams getBrowseParams1() {
        BrowseParams browseParams = new BrowseParams();
        String xml = getXmlAsString("/org/kuali/ole/browse/BrowseParams1.xml");
        return (BrowseParams) browseParams.deserialize(xml);
    }

    public BrowseParams getBrowseParams2() {
        BrowseParams browseParams = new BrowseParams();
        String xml = getXmlAsString("/org/kuali/ole/browse/BrowseParams2.xml");
        return (BrowseParams) browseParams.deserialize(xml);
    }

    public BrowseParams getBrowseParams3() {
        BrowseParams browseParams = new BrowseParams();
        String xml = getXmlAsString("/org/kuali/ole/browse/BrowseParams3.xml");
        return (BrowseParams) browseParams.deserialize(xml);
    }

    public BrowseParams getBrowseParams4() {
        BrowseParams browseParams = new BrowseParams();
        String xml = getXmlAsString("/org/kuali/ole/browse/BrowseParams4.xml");
        return (BrowseParams) browseParams.deserialize(xml);
    }

    private void updateContent(Bib bib) {
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
        BibMarcRecords bibMarcRecords = bibMarcRecordProcessor.fromXML(bib.getContent());
        BibMarcRecord bibMarcRecord = bibMarcRecords.getRecords().get(0);
        List<DataField> dataFields = bibMarcRecord.getDataFields();
        for (DataField dataField : dataFields) {
            if (dataField.getTag().equalsIgnoreCase("245")) {
                List<SubField> subFields = dataField.getSubFields();
                subFields.get(0).setValue("The Alchemist");
            }
        }
        bib.setContent(bibMarcRecordProcessor.generateXML(bibMarcRecord));
    }

    private void setDate(Bib bib) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        bib.setCreatedOn(dateStr);
    }

    @Test
    public void testCreateLicenseAttachment() {
        LicenseAttachment license = new LicenseAttachment();
        license.setCategory("work");
        license.setType("license");
        license.setFormat("pdf");
        license.setFileName("123.pdf");
        license.setFilePath("file path");
        license.setId("wl-1");
        Licenses licenses = new Licenses();
        licenses.getLicenses().add(license);
        docstoreService.createLicenses(licenses);
    }
    @Test
    public void testCreateLicense() {
        Licenses licenses = new Licenses();
        License license = new LicenseOnixpl();
        license = (License) license.deserialize(getXmlAsString("/org/kuali/ole/documents/license-onixpl.xml"));
        licenses.getLicenses().add(license);
        docstoreService.createLicenses(licenses);
    }

    @Test
    public void testBulkUpdatePHoldings() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibMarc1.xml");
        Bib bib = new BibMarc();
        bib = (Bib) bib.deserialize(xml);
        bib.setId(null);
        docstoreService.createBib(bib);
        Assert.assertNotNull(bib.getId());

        xml = getXmlAsString("/org/kuali/ole/documents/PHoldings1.xml");
        Holdings holdings = new PHoldings();
        holdings = (PHoldings) holdings.deserialize(xml);
        holdings.setBib(bib);
        docstoreService.createHoldings(holdings);
        Assert.assertNotNull(holdings.getId());
        Holdings holdings1 = holdings;
        holdings.setId(null);
        docstoreService.createHoldings(holdings);
        Assert.assertNotNull(holdings.getId());
        Holdings holdings2 = holdings;
        Assert.assertNotNull(holdings.getId());


        List<String> holdingIds = new ArrayList<String>();
        holdingIds.add(holdings1.getId());
        holdingIds.add(holdings2.getId());

        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        oleHoldings.setCopyNumber("1234567");
        holdings.setContent(holdingOlemlRecordProcessor.toXML(oleHoldings));
        docstoreService.bulkUpdateHoldings(holdings, holdingIds,"false");

        Holdings updatedHoldings1 = docstoreService.retrieveHoldings(holdings1.getId());
        Assert.assertEquals(updatedHoldings1.getId(), holdings1.getId());
        OleHoldings updateOleHoldings1 = holdingOlemlRecordProcessor.fromXML(updatedHoldings1.getContent());
        Assert.assertNotNull(updateOleHoldings1);
        Assert.assertEquals(updateOleHoldings1.getCopyNumber(),"1234567");

        Holdings updatedHoldings2 = docstoreService.retrieveHoldings(holdings2.getId());
        Assert.assertEquals(updatedHoldings2.getId(), holdings2.getId());
        OleHoldings updateOleHoldings2 = holdingOlemlRecordProcessor.fromXML(updatedHoldings2.getContent());
        Assert.assertNotNull(updateOleHoldings2);
        Assert.assertEquals(updateOleHoldings2.getCopyNumber(),"1234567");

    }

    @Test
    public void testBulkUpdateEHoldings() {
        String xml = getXmlAsString("/org/kuali/ole/documents/BibMarc1.xml");
        Bib bib = new BibMarc();
        bib = (Bib) bib.deserialize(xml);
        bib.setId(null);
        docstoreService.createBib(bib);
        Assert.assertNotNull(bib.getId());

        xml = getXmlAsString("/org/kuali/ole/documents/EHoldings1.xml");
        Holdings holdings = new EHoldings();
        holdings = (EHoldings) holdings.deserialize(xml);
        holdings.setBib(bib);
        docstoreService.createHoldings(holdings);
        Assert.assertNotNull(holdings.getId());
        Holdings holdings1 = holdings;
        holdings.setId(null);
        docstoreService.createHoldings(holdings);
        Assert.assertNotNull(holdings.getId());
        Holdings holdings2 = holdings;
        Assert.assertNotNull(holdings.getId());


        List<String> holdingIds = new ArrayList<String>();
        holdingIds.add(holdings1.getId());
        holdingIds.add(holdings2.getId());

        HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
        oleHoldings.setImprint("Mock Imprint");
        holdings.setContent(holdingOlemlRecordProcessor.toXML(oleHoldings));
        docstoreService.bulkUpdateHoldings(holdings,holdingIds,"false");

        Holdings updatedHoldings1 = docstoreService.retrieveHoldings(holdings1.getId());
        Assert.assertEquals(updatedHoldings1.getId(), holdings1.getId());
        OleHoldings updateOleHoldings1 = holdingOlemlRecordProcessor.fromXML(updatedHoldings1.getContent());
        Assert.assertNotNull(updateOleHoldings1);
        Assert.assertEquals(updateOleHoldings1.getImprint(),"Mock Imprint");

        Holdings updatedHoldings2 = docstoreService.retrieveHoldings(holdings2.getId());
        Assert.assertEquals(updatedHoldings2.getId(), holdings2.getId());
        OleHoldings updateOleHoldings2 = holdingOlemlRecordProcessor.fromXML(updatedHoldings2.getContent());
        Assert.assertNotNull(updateOleHoldings2);
        Assert.assertEquals(updateOleHoldings2.getImprint(),"Mock Imprint");

    }

    @Test
    public void testBulkUpdateItem() {

        String xml = getXmlAsString("/org/kuali/ole/documents/BibMarc1.xml");
        Bib bib = new BibMarc();
        bib = (Bib) bib.deserialize(xml);
        bib.setId(null);
        docstoreService.createBib(bib);
        Assert.assertNotNull(bib.getId());

        xml = getXmlAsString("/org/kuali/ole/documents/EHoldings1.xml");
        Holdings holdings = new EHoldings();
        holdings = (EHoldings) holdings.deserialize(xml);
        holdings.setBib(bib);
        docstoreService.createHoldings(holdings);
        Assert.assertNotNull(holdings.getId());

        xml = getXmlAsString("/org/kuali/ole/documents/ItemOleml1.xml");
        Item item = new Item();
        item = (Item)item.deserialize(xml);
        item.setHolding(holdings);
        docstoreService.createItem(item);
        Assert.assertNotNull(item);

        Item item1 = new Item();
        item1 = (Item)item1.deserialize(xml);
        item1.setHolding(holdings);
        docstoreService.createItem(item1);
        Assert.assertNotNull(item1.getId());

        Item item2 = new Item();
        item2 = (Item)item2.deserialize(xml);
        item2.setHolding(holdings);
        docstoreService.createItem(item2);
        Assert.assertNotNull(item2.getId());

        List<String> itemIds = new ArrayList<String>();
        itemIds.add(item1.getId());
        itemIds.add(item2.getId());

        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        org.kuali.ole.docstore.common.document.content.instance.Item oleItem = itemOlemlRecordProcessor.fromXML(item.getContent());

        oleItem.setBarcodeARSL("mock barcodeARSL");
        item.setContent(itemOlemlRecordProcessor.toXML(oleItem));
        docstoreService.bulkUpdateItem(item,itemIds,"false");

        Item updateItem1 = docstoreService.retrieveItem(item1.getId());
        Assert.assertNotNull(updateItem1);
        org.kuali.ole.docstore.common.document.content.instance.Item updateOleItem1 = itemOlemlRecordProcessor.fromXML(updateItem1.getContent());
        Assert.assertNotNull(updateOleItem1);
        Assert.assertEquals(updateOleItem1.getBarcodeARSL(),"mock barcodeARSL");


        Item updateItem2 = docstoreService.retrieveItem(item2.getId());
        Assert.assertNotNull(updateItem2);
        org.kuali.ole.docstore.common.document.content.instance.Item updateOleItem2 = itemOlemlRecordProcessor.fromXML(updateItem2.getContent());
        Assert.assertNotNull(updateOleItem2);
        Assert.assertEquals(updateOleItem2.getBarcodeARSL(),"mock barcodeARSL");
    }
    
    @Test
    public void testGetNormalised() {
        RdbmsItemDocumentManager rdbmsItemDocumentManager = new RdbmsItemDocumentManager();
        String value = "v. 1";
        System.out.println("Before Normalization: " +value);
        System.out.println("After Normalization: " + rdbmsItemDocumentManager.getNormalized(value));
        value = "v.2";
        System.out.println("Before Normalization: " +value);
        System.out.println("After Normalization: " +rdbmsItemDocumentManager.getNormalized(value));
        value = "v. 3";
        System.out.println("Before Normalization: " +value);
        System.out.println("After Normalization: " +rdbmsItemDocumentManager.getNormalized(value));
        BibTree bibTree = new BibTree();
        Bib bib = new Bib();
        bib.setId("wbm-10000001");
        bibTree.setBib(bib);
        ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        bibTree = docstoreService.retrieveBibTree(bibTree.getBib().getId());
        System.out.println("AFTER SORTING ITEMS:");
        for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
            System.out.println("***HOLDINGS***");
            System.out.println("ITEMS : Loc | Prefix | ShlOrd | Enum | Chro | Copy | bar");
            for (Item item : holdingsTree.getItems()) {
                org.kuali.ole.docstore.common.document.content.instance.Item item1 = itemOlemlRecordProcessor.fromXML(item.getContent());
//                System.out.println(strItems.format("%s | %s | %s | %s | %s | %s | %s", Holdings.buildLocationString(item1.getLocation()), item1.getCallNumber().getPrefix(),
//                        item1.getCallNumber().getShelvingOrder().getCodeValue(), item1.getEnumeration(), item1.getChronology(), item1.getCopyNumber(), item1.getAccessInformation().getBarcode() + "\n"));
            }
        }
    }

    @Test
    public void processBibTrees() {
        BibTrees bibTrees = getBibTrees();
        docstoreService.processBibTrees(bibTrees);
        List<String> bibIds = new ArrayList<String>();
        for (BibTree bibTree : bibTrees.getBibTrees()) {
            if (null != bibTree.getBib()) {
                bibIds.add(bibTree.getBib().getId());
            }
        }
        docstoreService.retrieveBibTrees(bibIds);
        SearchParams searchParams = buildSearchParams();
        docstoreService.search(searchParams);

        updateBibTree(bibTrees.getBibTrees().get(0));
        bibTrees.getBibTrees().get(0).getBib().setOperation(DocstoreDocument.OperationType.DELETE);

        updateBibTree(bibTrees.getBibTrees().get(1));
        docstoreService.processBibTrees(bibTrees);
        docstoreService.retrieveBibTrees(bibIds);

        docstoreService.search(searchParams);


    }

    private void updateBibTree(BibTree bibTree) {
        Bib bib=bibTree.getBib();

        bib.setOperation(DocstoreDocument.OperationType.UPDATE);
        bib.setResult(null);
        BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
         BibMarcRecords bibMarcRecords= bibMarcRecordProcessor.fromXML(bib.getContent());
        for(BibMarcRecord bibMarcRecord: bibMarcRecords.getRecords()){
            for(DataField dataField:bibMarcRecord.getDataFields()){
                if("245".equals(dataField.getTag())){
                   for(SubField subField:dataField.getSubFields()){
                       if(subField.getCode().equals("a")){
                           subField.setValue("Author-1 Updated");
                       }
                   }
                }
            }
        }
        bib.setContent(bibMarcRecordProcessor.toXml(bibMarcRecords));
        List<HoldingsTree> holdingsTrees = bibTree.getHoldingsTrees();

        HoldingsTree holdingsTree=holdingsTrees.get(0);
        //creating another holding
        Holdings holdings=holdingsTree.getHoldings();
        holdings.setMessage(null);
        holdings.setResult(null);
        holdings.setOperation(DocstoreDocument.OperationType.CREATE);
        holdings.setId(null);

        for(Item item:holdingsTree.getItems()){
            item.setMessage(null);
            item.setResult(null);
            item.setId(null);
            item.setOperation(DocstoreDocument.OperationType.CREATE);
        }

         holdingsTree=holdingsTrees.get(1);
        //creating another holding
        holdings=holdingsTree.getHoldings();
        holdings.setOperation(Holdings.OperationType.UPDATE);
        holdings.setMessage(null);
        holdings.setResult(null);

        Item item =holdingsTree.getItems().get(0);
        item.setMessage(null);
        item.setResult(null);
        item.setOperation(DocstoreDocument.OperationType.DELETE);

        item =holdingsTree.getItems().get(1);
        item.setMessage(null);
        item.setResult(null);
        item.setOperation(DocstoreDocument.OperationType.UPDATE);


        holdingsTree=holdingsTrees.get(2);
        //creating another holding
        holdings=holdingsTree.getHoldings();
        holdings.setOperation(Holdings.OperationType.DELETE);
        holdings.setMessage(null);
        holdings.setResult(null);



    }

    private SearchParams buildSearchParams() {
        SearchParams searchParams = new SearchParams();
        String xml = getXmlAsString("/org/kuali/ole/search/BibTreesSearchParams1.xml");
        return (SearchParams) searchParams.deserialize(xml);
    }

    public BibTrees getBibTrees() {
        BibTrees bibTrees = new BibTrees();
        bibTrees= (BibTrees) bibTrees.deserialize(getXmlAsString("/org/kuali/ole/documents/BibTrees1.xml"));
        List<BibTree> removeBibtrees = new  ArrayList<BibTree>();
        for (BibTree bibTree : bibTrees.getBibTrees()) {
            if (null == bibTree.getBib()) {
                removeBibtrees.add(bibTree);
            }
        }
        bibTrees.getBibTrees().removeAll(removeBibtrees);
        return bibTrees;
    }

    @Test
    public void updateBibsFailure() {
        List<String> bibIds = new ArrayList<String>();
        bibIds.add("10000016");
        bibIds.add("10000017");
        bibIds.add("10000018");
        bibIds.add("10000019");
        bibIds.add("10000020");
        List<Bib> bibs = docstoreService.retrieveBibs(bibIds);
        bibs.get(0).setStatus("None");
        try {
            docstoreService.updateBibs(bibs);
        } catch (Exception e) {
            bibs = docstoreService.retrieveBibs(bibIds);
            Assert.assertEquals("", bibs.get(0).getStatus());
        }

    }


    @Test
    public  void searchQuery() throws SolrServerException {
        SearchParams searchParams = new SearchParams();
        SolrQuery solrQuery = new SolrQuery();
        DocstoreSolrSearchService docstoreSolrSearchService = new DocstoreSolrSearchService();
        for(int bibCount = 0;bibCount<250;bibCount++){
            SearchCondition searchCondition = searchParams.buildSearchCondition("",searchParams.buildSearchField("bibliographic","LocalId_search", "1000000"+String.valueOf(bibCount)),"OR");
            searchParams.getSearchConditions().add(searchCondition);
        }
        SolrServer server = SolrServerManager.getInstance().getSolrServer();
        String query = docstoreSolrSearchService.buildQueryWithSearchParams(searchParams);
        solrQuery.setQuery(query);
        System.out.println("Executing solr query :" + solrQuery.toString().length());
        QueryResponse response = server.query(solrQuery, SolrRequest.METHOD.POST);


    }

    @Test
    public void testCreateAnalytics() {

        BibTree bibTree1 = getBibTree1();
        Assert.assertNotNull(bibTree1);
        docstoreService.createBibTree(bibTree1);

        BibTree bibTree2 = getBibTree2();
        Assert.assertNotNull(bibTree2);
        docstoreService.createBibTree(bibTree2);

        String seriesHoldingsId = bibTree1.getHoldingsTrees().get(0).getHoldings().getId();
        Assert.assertNotNull(seriesHoldingsId);

        List<String> itemIds = new ArrayList<String>();
        List<Item> items = bibTree2.getHoldingsTrees().get(0).getItems();
        Assert.assertNotNull(items);

        for (Item item : items) {
            itemIds.add(item.getId());
        }
        Assert.assertNotNull(itemIds);
        docstoreService.createAnalyticsRelation(seriesHoldingsId, itemIds);
        Assert.assertEquals(docstoreService.retrieveHoldings(seriesHoldingsId).isSeries(), Boolean.TRUE);
        items = docstoreService.retrieveItems(itemIds);
        for (Item item : items) {
            Assert.assertEquals(item.isAnalytic(), Boolean.TRUE);
        }
    }

    @Test
    public void testBreakAnalytics() {

        BibTree bibTree1 = getBibTree3();
        Assert.assertNotNull(bibTree1);
        docstoreService.createBibTree(bibTree1);

        BibTree bibTree2 = getBibTree4();
        Assert.assertNotNull(bibTree2);
        docstoreService.createBibTree(bibTree2);

        String seriesHoldingsId = bibTree1.getHoldingsTrees().get(0).getHoldings().getId();
        Assert.assertNotNull(seriesHoldingsId);

        List<String> itemIds = new ArrayList<String>();
        List<Item> items = bibTree2.getHoldingsTrees().get(0).getItems();
        Assert.assertNotNull(items);

        for (Item item : items) {
            itemIds.add(item.getId());
        }
        Assert.assertNotNull(itemIds);

        docstoreService.createAnalyticsRelation(seriesHoldingsId, itemIds);
        Assert.assertEquals(docstoreService.retrieveHoldings(seriesHoldingsId).isSeries(), Boolean.TRUE);
        items = docstoreService.retrieveItems(itemIds);
        for (Item item : items) {
            Assert.assertEquals(item.isAnalytic(), Boolean.TRUE);
        }
        docstoreService.breakAnalyticsRelation(seriesHoldingsId, itemIds);
        Assert.assertEquals(docstoreService.retrieveHoldings(seriesHoldingsId).isSeries(), Boolean.FALSE);
        items = docstoreService.retrieveItems(itemIds);
        for (Item item : items) {
            Assert.assertEquals(item.isAnalytic(), Boolean.FALSE);
        }
    }

    public BibTree getBibTree4() {
        BibTree bibTree = new BibTree();
        return (BibTree) bibTree.deserialize(getXmlAsString("/org/kuali/ole/search/BibTree4.xml"));
    }

}