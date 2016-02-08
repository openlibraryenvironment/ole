package org.kuali.ole.oleng.batch.utils;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 11/25/2015.
 */
public class DocstoreAPIClient_IT extends OLETestCaseBase{

    /*This test case for creating bib*/
    @Test
    public void testCreateBib() throws Exception {
        BibTrees bibTrees = new BibTrees();

        BibTree bibTree = new BibTree();
        Bib bib = new Bib();
        bib.setTitle("Test Bib title");
        bib.setAuthor("Test Bib Author");
        bib.setOperation(DocstoreDocument.OperationType.CREATE);
        bib.setContent(BIB_CONTENT);
        bibTree.setBib(bib);

        bibTrees.getBibTrees().add(bibTree);

        DocstoreAPIClient docstoreAPIClient = new DocstoreAPIClient();
        BibTrees savedBibTrees = docstoreAPIClient.createOrUpdateBibTrees(bibTrees);
        assertNotNull(savedBibTrees);
        List<BibTree> savedBibTreeList = savedBibTrees.getBibTrees();
        for (Iterator<BibTree> iterator = savedBibTreeList.iterator(); iterator.hasNext(); ) {
            BibTree tree = iterator.next();
            Bib savedBib = tree.getBib();
            System.out.println("Bib Id : " + savedBib.getId() + "    Status : " + savedBib.getResult());
        }
    }

    /*This test case for creating bib and holding*/
    @Test
    public void testCreateBibAndHoldings() throws Exception {
        BibTrees bibTrees = new BibTrees();

        BibTree bibTree = new BibTree();
        Bib bib = new Bib();
        bib.setTitle("Test Bib title");
        bib.setAuthor("Test Bib Author");
        bib.setOperation(DocstoreDocument.OperationType.CREATE);
        bib.setContent(BIB_CONTENT);
        bibTree.setBib(bib);

        HoldingsTree holdingsTree = new HoldingsTree();
        Holdings holdings = new Holdings();
        holdings.setBib(bib);
        holdings.setCallNumber("CallNumber");
        holdings.setOperation(DocstoreDocument.OperationType.CREATE);
        holdings.setHoldingsType(PHoldings.PRINT);

        holdings.setContent(HOLDINGS_CONTENT);
        holdingsTree.setHoldings(holdings);


        bibTree.getHoldingsTrees().add(holdingsTree);

        bibTrees.getBibTrees().add(bibTree);

        DocstoreAPIClient docstoreAPIClient = new DocstoreAPIClient();
        BibTrees savedBibTrees = docstoreAPIClient.createOrUpdateBibTrees(bibTrees);
        assertNotNull(savedBibTrees);
        List<BibTree> savedBibTreeList = savedBibTrees.getBibTrees();
        for (Iterator<BibTree> iterator = savedBibTreeList.iterator(); iterator.hasNext(); ) {
            BibTree tree = iterator.next();
            Bib savedBib = tree.getBib();
            System.out.println("Bib Id : " + savedBib.getId() + "    Status : " + savedBib.getResult());
        }
    }

    /*This test case for creating bib, holding and items.*/
    @Test
    public void testCreateBibAndHoldingsAndItem() throws Exception {
        BibTrees bibTrees = new BibTrees();

        BibTree bibTree = new BibTree();
        Bib bib = new Bib();
        bib.setTitle("Test Bib title");
        bib.setAuthor("Test Bib Author");
        bib.setOperation(DocstoreDocument.OperationType.CREATE);
        bib.setContent(BIB_CONTENT);
        bibTree.setBib(bib);

        HoldingsTree holdingsTree = new HoldingsTree();
        Holdings holdings = new Holdings();
        holdings.setBib(bib);
        holdings.setCallNumber("CallNumber");
        holdings.setOperation(DocstoreDocument.OperationType.CREATE);
        holdings.setHoldingsType(PHoldings.PRINT);

        holdings.setContent(HOLDINGS_CONTENT);
        holdingsTree.setHoldings(holdings);

        Item documentItemObject = new Item();
        documentItemObject.setField(Item.DUE_DATE_TIME, "08/20/2015 02:36:00");
        documentItemObject.serializeContent();

        Item documentItemObject2 = new Item();
        documentItemObject2.setField(Item.DUE_DATE_TIME, "08/20/2015 02:36:00");

        Item documentItemObject3 = new Item();
        documentItemObject3.setField(Item.DUE_DATE_TIME, "08/20/2015 02:36:00");

        List<Item> itemList = new ArrayList<>();
        itemList.add(documentItemObject);
        itemList.add(documentItemObject2);
        itemList.add(documentItemObject3);

        for (Iterator<Item> iterator = itemList.iterator(); iterator.hasNext(); ) {
            Item item = iterator.next();
            item.setOperation(DocstoreDocument.OperationType.CREATE);
            item.serializeContent();
            holdingsTree.getItems().add(item);
        }


        bibTree.getHoldingsTrees().add(holdingsTree);

        bibTrees.getBibTrees().add(bibTree);

        DocstoreAPIClient docstoreAPIClient = new DocstoreAPIClient();
        BibTrees savedBibTrees = docstoreAPIClient.createOrUpdateBibTrees(bibTrees);
        assertNotNull(savedBibTrees);
        List<BibTree> savedBibTreeList = savedBibTrees.getBibTrees();
        for (Iterator<BibTree> iterator = savedBibTreeList.iterator(); iterator.hasNext(); ) {
            BibTree tree = iterator.next();
            Bib savedBib = tree.getBib();
            System.out.println("Bib Id : " + savedBib.getId() + "    Result : " + savedBib.getResult());
        }
    }


    private String BIB_CONTENT = "<collection xmlns=\"http://www.loc.gov/MARC21/slim\">\n" +
            "    <record>\n" +
            "    <leader>#####nam#a22######a#4500</leader>\n" +
            "    <controlfield tag=\"001\"></controlfield>\n" +
            "    <controlfield tag=\"003\">OCoLC</controlfield>\n" +
            "    <controlfield tag=\"005\">20090213152530.7</controlfield>\n" +
            "    <controlfield tag=\"008\">131031s########xxu###########000#0#eng#d</controlfield>\n" +
            "    <datafield tag=\"035\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">(OCoLC)ocm62378465</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"040\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">DLC</subfield>\n" +
            "    <subfield code=\"c\">DLC</subfield>\n" +
            "    <subfield code=\"d\">DLC</subfield>\n" +
            "    <subfield code=\"d\">HLS</subfield>\n" +
            "    <subfield code=\"d\">IUL</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"022\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">1729-1070|20</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"029\" ind1=\"1\" ind2=\" \">\n" +
            "    <subfield code=\"a\">AU@|b000040176476</subfield>\n" +
            "    <subfield code=\"b\">000040176476</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"037\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"b\">The Managing Editor, BIAC Journal, P.O. Box 10026, Gaborone, Botswana</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"042\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">lc</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"043\" ind1=\"1\" ind2=\"0\">\n" +
            "    <subfield code=\"a\">f-bs---</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"050\" ind1=\"0\" ind2=\"0\">\n" +
            "    <subfield code=\"a\">HD70.B55|bB53</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"049\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">IULA</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"210\" ind1=\"1\" ind2=\" \">\n" +
            "    <subfield code=\"a\">BIAC j.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"222\" ind1=\" \" ind2=\"0\">\n" +
            "    <subfield code=\"a\">BIAC journal</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"245\" ind1=\"0\" ind2=\"0\">\n" +
            "    <subfield code=\"a\">BIAC journal</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"246\" ind1=\"1\" ind2=\"3\">\n" +
            "    <subfield code=\"a\">Botswana Institute of Administration and Commerce journal</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"260\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">Gaborone, Botswana :|bBotswana Institute of Administration and Commerce</subfield>\n" +
            "    <subfield code=\"b\">Botswana Institute of Administration and Commerce</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"300\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">v. ;</subfield>\n" +
            "    <subfield code=\"c\">24 cm.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"300\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">v. ;</subfield>\n" +
            "    <subfield code=\"c\">24 cm.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"300\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">v. ;</subfield>\n" +
            "    <subfield code=\"c\">24 cm.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"310\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">Semiannual</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"362\" ind1=\"1\" ind2=\" \">\n" +
            "    <subfield code=\"a\">Began in 2004.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"500\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">Description based on: Vol. 1, no. 1 (May. 2004); title from cover.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"500\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">Latest issue consulted: Vol. 3, no. 1 (May 2006).</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"650\" ind1=\"0\" ind2=\" \">\n" +
            "    <subfield code=\"a\">Industrial management</subfield>\n" +
            "    <subfield code=\"z\">Botswana</subfield>\n" +
            "    <subfield code=\"v\">Periodicals.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"650\" ind1=\"0\" ind2=\" \">\n" +
            "    <subfield code=\"a\">Occupational training</subfield>\n" +
            "    <subfield code=\"z\">Botswana</subfield>\n" +
            "    <subfield code=\"v\">Periodicals.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"710\" ind1=\"2\" ind2=\" \">\n" +
            "    <subfield code=\"a\">Botswana Institute of Administration and Commerce.</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"850\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">DLC</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"891\" ind1=\"2\" ind2=\"0\">\n" +
            "    <subfield code=\"a\">9853|81.1</subfield>\n" +
            "    <subfield code=\"a\">v.</subfield>\n" +
            "    <subfield code=\"b\">no</subfield>\n" +
            "    <subfield code=\"u\">2</subfield>\n" +
            "    <subfield code=\"v\">r</subfield>\n" +
            "    <subfield code=\"i\">(year)</subfield>\n" +
            "    <subfield code=\"j\">(month)</subfield>\n" +
            "    <subfield code=\"w\">f</subfield>\n" +
            "    <subfield code=\"x\">05</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"891\" ind1=\"4\" ind2=\"1\">\n" +
            "    <subfield code=\"a\">9863|81.1</subfield>\n" +
            "    <subfield code=\"a\">1</subfield>\n" +
            "    <subfield code=\"b\">1</subfield>\n" +
            "    <subfield code=\"i\">2004</subfield>\n" +
            "    <subfield code=\"j\">05</subfield>\n" +
            "    </datafield>\n" +
            "    <datafield tag=\"596\" ind1=\" \" ind2=\" \">\n" +
            "    <subfield code=\"a\">1</subfield>\n" +
            "    </datafield>\n" +
            "    </record>\n" +
            "    </collection>";


    private String HOLDINGS_CONTENT = "<oleHoldings>\n" +
            "  <callNumber>\n" +
            "    <number>1234</number>\n" +
            "    <shelvingScheme>\n" +
            "      <codeValue>LCC</codeValue>\n" +
            "    </shelvingScheme>\n" +
            "  </callNumber>\n" +
            "  <interLibraryLoanAllowed>false</interLibraryLoanAllowed>\n" +
            "  <staffOnlyFlag>false</staffOnlyFlag>\n" +
            "</oleHoldings>";
}