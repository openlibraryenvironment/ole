package org.kuali.ole.describe.bo;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.describe.bo.DocumentSelectionTree;
import org.kuali.ole.describe.bo.DocumentTreeNode;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.bo.WorkHoldingsDocument;
import org.kuali.ole.docstore.model.bo.WorkInstanceDocument;
import org.kuali.ole.docstore.model.bo.WorkItemDocument;
import org.kuali.rice.core.api.util.tree.Node;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 12/22/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentSelectionTree_UT {
    private static final Logger LOG = Logger.getLogger(DocumentSelectionTree_UT.class);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testBuildNodeList() throws Exception {
        DocumentSelectionTree dst = new DocumentSelectionTree();
        List<Node<DocumentTreeNode, String>> nodeList = new ArrayList<Node<DocumentTreeNode, String>>();
        List<BibTree> bibTreeList = new ArrayList<BibTree>();
        BibTree bibTree = prepareTestData(bibTreeList);
        nodeList = dst.buildNodeList(bibTreeList);
        assertNotNull(nodeList);
        assertEquals(bibTreeList.size(), nodeList.size());
        for (Node<DocumentTreeNode, String> node : nodeList) {
            assertNotNull(node.getNumberOfChildren());
            assertEquals("History of Computers", bibTree.getBib().getTitle());
        }
    }
    @Test
    public void testBuildNodeListForTransfer() throws Exception {
        DocumentSelectionTree dst = new DocumentSelectionTree();
        List<Node<DocumentTreeNode, String>> nodeList = new ArrayList<Node<DocumentTreeNode, String>>();
        List<BibTree> bibTreeList = new ArrayList<BibTree>();
        BibTree  bibTree = prepareTestData(bibTreeList);
        nodeList = dst.buildNodeListForTransfer(bibTreeList);
        assertNotNull(nodeList);
        assertEquals(bibTreeList.size(), nodeList.size());
        for (Node<DocumentTreeNode, String> node : nodeList) {
            assertNotNull(node.getNumberOfChildren());
            assertEquals("History of Computers", bibTree.getBib().getTitle());
        }

    }


    private BibTree prepareTestData(List<BibTree> bibTreeList) {
        BibTree bibTree = new BibTree();
        List<HoldingsTree> holdingsTreeList = new ArrayList<HoldingsTree>();
        HoldingsTree holdingsTree = new HoldingsTree();
        Holdings holdings = new Holdings();
        List<Item> itemList = new ArrayList<Item>();
        Item item = new Item();
        WorkHoldingsDocument workHoldingsDocument = new WorkHoldingsDocument();
        org.kuali.ole.docstore.common.document.content.instance.Item olemlItem= new org.kuali.ole.docstore.common.document.content.instance.Item();
        olemlItem.setItemIdentifier("Item-1");

        item.setId("Item-1");
        item.setCallNumber("Item callNumber");
        itemList.add(item);

        item.setContent(new ItemOlemlRecordProcessor().toXML(olemlItem));
        OleHoldings oleHoldings=new OleHoldings();
        oleHoldings.setHoldingsIdentifier("Holding-1");

        holdings.setId("Holding-1");
        holdings.setLocationName("Holding location");
        holdings.setCallNumber("Holding callNumber");

        holdings.setHoldingsType("print");
        holdings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
        holdingsTree.setHoldings(holdings);
        item.setHolding(holdings);
        holdingsTree.getItems().add(item);
        holdingsTree.getHoldings().setId("Instance-1");
        holdingsTreeList.add(holdingsTree);
        bibTree.getHoldingsTrees().add(holdingsTree);
        Bib bib=new Bib();
        bib.setId("Bib-1");
        bib.setTitle("History of Computers");
        bibTree.setBib(bib);
        //bibTree.getBib().setTitle("History of Computers");
        bibTree.getBib().setId("Bib-1");
        //bibTree.getBib().setId("Bib-1");
        bibTreeList.add(bibTree);
        return bibTree;
    }
}
