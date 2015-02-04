
package org.kuali.ole;

/*import org.apache.jackrabbit.core.persistence.util.BLOBStore;*/
import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.search.SearchCondition;
import org.kuali.ole.docstore.common.search.SearchParams;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
/*import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.ole.docstore.engine.client.DocstoreLocalClient;*/

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 12/16/13
 * Time: 6:17 PM
 * To change this template use File | Settings | File Templates.
 */

public class DocstoreLocalClient_UT {
    @Test
    public void test(){

    }

   /* DocstoreLocalClient docstoreLC = new   DocstoreLocalClient();

    @Test
    public void testCreateBib() {
        Bib bib = getBibRecord();
        docstoreLC.createBib(bib);
        Assert.assertNotSame(bib.getId(), "123");

        //Stimulate an exception while storing.
        //Assert.assertNull(bib.getId());

        //Stimulate an exception while indexing
        //Assert.assertNotSame(bib.getId(), "123");

        //To clean up record
        //docstoreService.deleteBib(bib.getId());
    }

    @Test
    public void testCreateHoldings() {
        Bib bib = getBibRecord();
        docstoreLC.createBib(bib);
        Holdings holdings = getHoldingsRecord();
        holdings.setBib(bib);
        docstoreLC.createHoldings(holdings);

        Assert.assertEquals(holdings.getId(), "who-1");
    }

    @Test
    public void testCreateItem() {
        Bib bib = getBibRecord();
        docstoreLC.createBib(bib);

        Holdings holdings = getHoldingsRecord();
        docstoreLC.createHoldings(holdings);
        Item item = getItemRecord();
        docstoreLC.createItem(item);
        Assert.assertEquals(item.getId(), "wio-1");
    }

    @Test
    public void testCreateHoldingsTree() {
        HoldingsTree holdingsTree = getHoldingsTreeRecord();

        Bib bib = getBibRecord();
        docstoreLC.createBib(bib);
        holdingsTree.getHoldings().setBib(bib);
        docstoreLC.createHoldingsTree(holdingsTree);
        Assert.assertEquals(holdingsTree.getHoldings().getId(), "who-1");
        Assert.assertEquals(holdingsTree.getItems().get(0).getId(), "wio-1");

    }

    @Test
    public void testCreateBibTree() {
        BibTree bibTree = new BibTree();
        bibTree.setBib(getBibRecord());
        List<HoldingsTree> holdingsTrees = new ArrayList<>(0);
        holdingsTrees.add(getHoldingsTreeRecord());
        //bibTree.setHoldingsTrees(holdingsTrees);
        docstoreLC.createBibTree(bibTree);
        Assert.assertEquals(bibTree.getBib().getId(), "123");
        HoldingsTree holdingsTree = bibTree.getHoldingsTrees().get(0);
        Assert.assertEquals(holdingsTree.getHoldings().getId(), "who-1");
        Assert.assertEquals(holdingsTree.getItems().get(0).getId(), "wio-1");

    }

    @Test
    public void testDeleteBib() {
        Bib bib = getBibRecord();
        docstoreLC.deleteBib(bib.getId());
    }

    @Test
    public void testDeleteHoldings() {
        Holdings holdings = getHoldingsRecord();
        docstoreLC.deleteHoldings(holdings.getId());
    }

    @Test
    public void testDeleteItem() {
        Item item = getItemRecord();
        docstoreLC.deleteItem(item.getId());
    }

    private HoldingsTree getHoldingsTreeRecord() {
        HoldingsTree holdingsTree = new HoldingsTree();
        holdingsTree.setHoldings(getHoldingsRecord());
        List<Item> items = new ArrayList<>(0);
        items.add(getItemRecord());
        //holdingsTree.setItems(items);
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

    @Test
    public void testRetrieveBib() {
        String bibId = "123";
        Bib bib = docstoreLC.retrieveBib(bibId);
        Assert.assertNotNull(bib);
    }

    @Test
    public void testRetrieveHoldings() {
        String holdingsId = "who-1";
        Holdings holdings = docstoreLC.retrieveHoldings(holdingsId);
        Assert.assertNotNull(holdings);
    }

    @Test
    public void testRetrieveItem() {
        String itemId = "wio-1";
        Item item = docstoreLC.retrieveItem(itemId);
        Assert.assertNotNull(item);
    }

    @Test
    public void testRetrieveHoldingsTree() {
        String holdingsId = "who-1";
        HoldingsTree holdingsTree = docstoreLC.retrieveHoldingsTree(holdingsId);
        Assert.assertNotNull(holdingsTree);
    }

    @Test
    public void testRetrieveBibTree() {
        String bibId = "123";
        BibTree bibTree = docstoreLC.retrieveBibTree(bibId);
        Assert.assertNotNull(bibTree);
    }

//    @Test
//    public void testSearchBibs() {
//        SearchParams searchParams = getSearchParams();
//        List<Bib> bibs = docstoreLC.searchBibs(searchParams);
//        Assert.assertNotNull(bibs);
//    }
//
//    @Test
//    public void testSearchBibWithHoldings() {
//        SearchParams searchParams = getSearchParams();
//        List<BibTree> bibTrees = docstoreLC.searchBibWithHoldings(searchParams);
//        Assert.assertNotNull(bibTrees);
//    }
//
//    @Test
//    public void testSearchBibWithHoldingsAndItems() {
//        SearchParams searchParams = getSearchParams();
//        List<BibTree> bibTrees = docstoreLC.searchBibWithHoldingsAndItems(searchParams);
//        Assert.assertNotNull(bibTrees);
//    }
//
//    @Test
//    public void testSearchHoldings() {
//        SearchParams searchParams = getSearchParams();
//        List<Holdings> holdingsList = docstoreLC.searchHoldings(searchParams);
//        Assert.assertNotNull(holdingsList);
//    }
//
//    @Test
//    public void testSearchHoldingsWithItems() {
//        SearchParams searchParams = getSearchParams();
//        List<HoldingsTree> holdingsTrees = docstoreLC.searchHoldingsWithItems(searchParams);
//        Assert.assertNotNull(holdingsTrees);
//    }

//    @Test
//    public void testSearchItems() {
//        SearchParams searchParams = getSearchParams();
//        List<Item> items = docstoreLC.searchItems(searchParams);
//        Assert.assertNotNull(items);
//    }

 public SearchParams getSearchParams() {
       SearchParams searchParams = new SearchParams();
    */
/*        searchParams.setDocCategory("work");
        searchParams.setDocType("bibliographic");
        searchParams.setDocFormat("marc");
        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchText("Carl San");
        searchCondition.setOperator("NOT");
        searchCondition.setDocField("Author_search");
        searchCondition.setSearchScope("OR");
        searchConditionList.add(searchCondition);
        searchCondition = new SearchCondition();
        searchCondition.setSearchText("Sandburg");
        searchCondition.setOperator("NOT");
        searchCondition.setDocField("Author_search");
        searchCondition.setSearchScope("OR");
        searchConditionList.add(searchCondition);
        searchParams.setSearchFieldsList(searchConditionList);
        //searchParams.setSearchType("moreFacets"); *//*

        return searchParams;
    }


    @Test
    public void testUpdateBib() {
        Bib bib = getBibRecord();
        bib.setContent("mock update content");
        docstoreLC.updateBib(bib);
    }

    @Test
    public void testUpdateHoldings() {
        Holdings holdings = getHoldingsRecord();
        holdings.setContent("mock update content");
        docstoreLC.updateHoldings(holdings);
    }

    @Test
    public void testUpdateItem() {
        Item item = getItemRecord();
        item.setContent("mock update content");
        docstoreLC.updateItem(item);
    }

    @Test
    public void testSearchBibs() {
        SearchParams searchParams = getSearchParams();
        List<Bib> bibs = (List<Bib>)docstoreLC.search(searchParams);
        Assert.assertNotNull(bibs);
    }

    @Test
    public void testSearchBibWithHoldings() {
        SearchParams searchParams = getSearchParams();
        List<BibTree> bibTrees = (List<BibTree>)docstoreLC.search(searchParams);
        Assert.assertNotNull(bibTrees);
    }

    @Test
    public void testSearchBibWithHoldingsAndItems() {
        SearchParams searchParams = getSearchParams();
        List<BibTree> bibTrees = (List<BibTree>)docstoreLC.search(searchParams);
        Assert.assertNotNull(bibTrees);
    }

    @Test
    public void testSearchHoldings() {
        SearchParams searchParams = getSearchParams();
        List<Holdings> holdingsList = (List<Holdings>)docstoreLC.search(searchParams);
        Assert.assertNotNull(holdingsList);
    }

    @Test
    public void testSearchHoldingsWithItems() {
        SearchParams searchParams = getSearchParams();
        List<HoldingsTree> holdingsTrees = (List<HoldingsTree>)docstoreLC.search(searchParams);
        Assert.assertNotNull(holdingsTrees);
    }

    @Test
    public void testSearchItems() {
        SearchParams searchParams = getSearchParams();
        List<Item> items = (List<Item>)docstoreLC.search(searchParams);
        Assert.assertNotNull(items);
    }
   */
}

