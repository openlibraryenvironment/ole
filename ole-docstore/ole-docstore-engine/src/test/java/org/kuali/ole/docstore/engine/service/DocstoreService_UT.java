package org.kuali.ole.docstore.engine.service;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.BaseTestCase;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.search.*;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/16/13
 * Time: 6:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocstoreService_UT extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(DocstoreService_UT.class);
    DocstoreService docstoreService = BeanLocator.getDocstoreService();

    @Mock
    private BusinessObjectService businessObjectService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @Ignore
    @Test
    public void testCreateBib() {
        Bib bib = getBibRecord();
        docstoreService.createBib(bib);
        Assert.assertSame(bib.getId(), "123");

        //Stimulate an exception while storing.
        //Assert.assertNull(bib.getId());

        //Stimulate an exception while indexing
        //Assert.assertNotSame(bib.getId(), "123");

        //To clean up record
        //docstoreService.deleteBib(bib.getId());

    }

    @Ignore
    @Test
    public void testCreateHoldings() {
        Bib bib = getBibRecord();
        docstoreService.createBib(bib);

        Holdings holdings = getHoldingsRecord();
        docstoreService.createHoldings(holdings);

        Assert.assertEquals(holdings.getId(), "who-1");
    }

    @Ignore
    @Test
    public void testCreateItem() {
        Bib bib = getBibRecord();
        docstoreService.createBib(bib);

        Holdings holdings = getHoldingsRecord();
        docstoreService.createHoldings(holdings);
        Item item = getItemRecord();
        docstoreService.createItem(item);
        Assert.assertEquals(item.getId(), "wio-1");
    }

    @Ignore
    @Test
    public void testCreateHoldingsTree() {
        HoldingsTree holdingsTree = getHoldingsTreeRecord();

        Bib bib = getBibRecord();
        docstoreService.createBib(bib);

        docstoreService.createHoldingsTree(holdingsTree);
        Assert.assertEquals(holdingsTree.getHoldings().getId(), "who-1");
        Assert.assertEquals(holdingsTree.getItems().get(0).getId(), "wio-1");

    }

    @Ignore
    @Test
    public void testCreateBibTree() {
        BibTree bibTree = new BibTree();
        bibTree.setBib(getBibRecord());
        bibTree.getHoldingsTrees().add(getHoldingsTreeRecord());
        docstoreService.createBibTree(bibTree);
        Assert.assertEquals(bibTree.getBib().getId(), "123");
        HoldingsTree holdingsTree = bibTree.getHoldingsTrees().get(0);
        Assert.assertEquals(holdingsTree.getHoldings().getId(), "who-1");
        Assert.assertEquals(holdingsTree.getItems().get(0).getId(), "wio-1");

    }

    @Ignore
    @Test
    public void testDeleteBib() {
        Bib bib = getBibRecord();
        docstoreService.deleteBib(bib.getId());
    }

    @Ignore
    @Test
    public void testDeleteHoldings() {
        Holdings holdings = getHoldingsRecord();
        docstoreService.deleteHoldings(holdings.getId());
    }

    @Ignore
    @Test
    public void testDeleteItem() {
        Item item = getItemRecord();
        docstoreService.deleteItem(item.getId());
    }


    private HoldingsTree getHoldingsTreeRecord() {
        HoldingsTree holdingsTree = new HoldingsTree();
        holdingsTree.setHoldings(getHoldingsRecord());
        holdingsTree.getItems().add(getItemRecord());
        return holdingsTree;
    }

    private Item getItemRecord() {
        Item item = new ItemOleml();
        item.setCategory(DocCategory.WORK.getCode());
        item.setType(DocType.ITEM.getCode());
        item.setFormat(DocFormat.OLEML.getCode());
        return item;
    }

    private Holdings getHoldingsRecord() {
        Holdings holdings = new PHoldingsOleml();
        holdings.setCategory(DocCategory.WORK.getCode());
        holdings.setType(DocType.HOLDINGS.getCode());
        holdings.setFormat(DocFormat.OLEML.getCode());
        return holdings;
    }


    private Bib getBibRecord() {
        Bib bib = new BibMarc();
        bib.setCategory(DocCategory.WORK.getCode());
        bib.setType(DocType.BIB.getCode());
        bib.setFormat(DocFormat.MARC.getCode());
        bib.setContent("<collection xmlns=\"http://www.loc.gov/MARC21/slim\">\n" +
                "    <record>\n" +
                "        <leader>#####nam#a22######a#4500</leader>\n" +
                "\t<controlfield tag=\"001\">3</controlfield>\n" +
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

    @Ignore
    @Test
    public void testRetrieveBib() {
        String bibId = "123";
        Bib bib = docstoreService.retrieveBib(bibId);
        Assert.assertNotNull(bibId);
    }

    @Ignore
    @Test
    public void testRetrieveHoldings() {
        String holdingsId = "who-1";
        Holdings holdings = docstoreService.retrieveHoldings(holdingsId);
        Assert.assertNotNull(holdingsId);
    }

    @Ignore
    @Test
    public void testRetrieveItem() {
        String itemId = "wio-1";
        Item item = docstoreService.retrieveItem(itemId);
        Assert.assertNotNull(itemId);
    }

    @Ignore
    @Test
    public void testRetrieveHoldingsTree() {
        String holdingsId = "who-1";
        HoldingsTree holdingsTree = docstoreService.retrieveHoldingsTree(holdingsId);
        Assert.assertNotNull(holdingsId);
    }

    @Ignore
    @Test
    public void testRetrieveBibTree() {
        String bibId = "123";
        BibTree bibTree = docstoreService.retrieveBibTree(bibId);
        Assert.assertNotNull(bibId);
    }

    @Ignore
    @Test
    public void testSearch1() {
        SearchParams searchParams = getSearchParams1();
        SearchResponse searchResponse = docstoreService.search(searchParams);
        Assert.assertNotNull(searchResponse);
    }

    @Ignore
    @Test
    public void testSearch2() {
        SearchParams searchParams = getSearchParams2();
        SearchResponse searchResponse = docstoreService.search(searchParams);
        Assert.assertNotNull(searchResponse);
    }

    @Ignore
    @Test
    public void testSearch3() {
        SearchParams searchParams = getSearchParams3();
        SearchResponse searchResponse = docstoreService.search(searchParams);
        Assert.assertNotNull(searchResponse);
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

    public String getXmlAsString(String filePath) {
        String input ="";
        File file = null;
        try {
            file = new File(getClass().getResource(filePath).toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception : ", e);
        }
        return input;
    }

    /*@Test
    public void testSearch4() {
        SearchParams searchParams = getSearchParams4();
        SearchResponse searchResponse = docstoreService.search(searchParams);
        Assert.assertNotNull(searchResponse);
    }

    public SearchParams getSearchParams4() {
        SearchParams searchParams = new SearchParams();
        SearchField searchField1 = searchParams.buildSearchField("item","CallNumber","123");
        SearchField searchField2 = searchParams.buildSearchField("item","CopyNumber","111CN");
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("OR",searchField1,"AND"));
        searchParams.getSearchConditions().add(searchParams.buildSearchCondition("OR",searchField2,"AND"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic","Title"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic","Author"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic","CallNumber"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic","PublicationDate"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic","Publisher"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic","ISBN"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("bibliographic","ISSN"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("holdings","CallNumber"));
        searchParams.getSearchResultFields().add(searchParams.buildSearchResultField("item","Enumeration"));
        searchParams.setPageSize(25);
        searchParams.setStartIndex(0);
        return searchParams;
    }   */

    @Ignore
    @Test
    public void testUpdateBib() {
        Bib bib = getBibRecord();
        bib.setContent("mock update content");
        docstoreService.updateBib(bib);
    }

    @Ignore
    @Test
    public void testUpdateHoldings() {
        Holdings holdings = getHoldingsRecord();
        holdings.setContent("mock update content");
        docstoreService.updateHoldings(holdings);
    }

    @Ignore
    @Test
    public void testUpdateItem() {
        Item item = getItemRecord();
        item.setContent("mock update content");
        docstoreService.updateItem(item);
    }

}
