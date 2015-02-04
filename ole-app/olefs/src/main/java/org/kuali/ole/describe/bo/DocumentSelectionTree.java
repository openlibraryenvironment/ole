package org.kuali.ole.describe.bo;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Created with IntelliJ IDEA.
 * User: Sreekanth
 * Date: 12/20/12
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentSelectionTree {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentSelectionTree.class);
    private Node<DocumentTreeNode, String> rootNode;

    public DocumentSelectionTree() {
        initTree();
    }

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }
    protected void initTree() {
        rootNode = new Node<DocumentTreeNode, String>(new DocumentTreeNode());
    }

    public Node<DocumentTreeNode, String> add(Collection<String> uuidList, String docType) throws SolrServerException {
        Node<DocumentTreeNode, String> rootNode = add(uuidList, docType, false);
        return rootNode;
    }

    /**
     * This method adds the holdings trees to the root node in left pane tree.
     * @param holdingsTrees
     * @param docType
     * @return
     * @throws SolrServerException
     */
    public Node<DocumentTreeNode, String> addHoldingsTree(List<HoldingsTree> holdingsTrees, String docType) throws SolrServerException {
        if (docType.equalsIgnoreCase((DocType.HOLDINGS.getCode()))) {
            for (HoldingsTree holdingsTree : holdingsTrees) {
                Node<DocumentTreeNode, String> node = buildHoldingsNode(holdingsTree);
                rootNode.addChild(node);
            }
        } else if (docType.equalsIgnoreCase(DocType.EHOLDINGS.getCode())) {
            for (HoldingsTree holdingsTree : holdingsTrees) {
                Node<DocumentTreeNode, String> node = buildEHoldingsNode(holdingsTree);
                rootNode.addChild(node);
            }
        }
        return rootNode;
    }

    /**
     * This method builds the EHoldings node to left pane tree.
     * @param holdingsTree
     */
    private Node<DocumentTreeNode, String>  buildEHoldingsNode(HoldingsTree holdingsTree) {
        Node<DocumentTreeNode, String> node = buildHoldingsNode(holdingsTree);
            HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
            if (holdingsTree.getHoldings() != null) {
                OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdingsTree.getHoldings().getContent());
                if (oleHoldings.getEResourceId() != null && !oleHoldings.getEResourceId().isEmpty()) {
                    OLEEResourceRecordDocument oleeResourceRecordDocument = KRADServiceLocator.getBusinessObjectService().findBySinglePrimaryKey(OLEEResourceRecordDocument.class, oleHoldings.getEResourceId());
                    node.setNodeLabel(node.getNodeLabel() + OLEConstants.DOCSTORE_NODE + oleeResourceRecordDocument.getTitle());
                }
            }
        return node;
    }

    public Node<DocumentTreeNode, String> add(Collection<String> uuidList, String docType, boolean excludeEHoldings) throws SolrServerException {

        List<Node<DocumentTreeNode, String>> nodeList = null;
        Node<DocumentTreeNode, String> node = null;
        if (!StringUtils.isBlank(docType)) {
            if (docType.equalsIgnoreCase(DocType.BIB.getDescription())) {
                List<BibTree> bibTreeList = null;
                bibTreeList = buildDocTreeList(DocCategory.WORK.getCode(), docType, uuidList, excludeEHoldings);
                if(excludeEHoldings){
                    for(BibTree bibTree : bibTreeList){
                        List<HoldingsTree> holdingsTrees = new ArrayList<>();
                        holdingsTrees = bibTree.getHoldingsTrees();
                        ListIterator<HoldingsTree> holdingsTreeListIterator = holdingsTrees.listIterator();
                        while (holdingsTreeListIterator.hasNext()){
                            if(holdingsTreeListIterator.next().getHoldings().getHoldingsType().equalsIgnoreCase("electronic")){
                                holdingsTreeListIterator.remove();
                            }
                        }
                    }
                }
                nodeList = buildNodeList(bibTreeList);
                for (Node<DocumentTreeNode, String> bibNode : nodeList) {
                    rootNode.addChild(bibNode);
                }
            }

            if (docType.equalsIgnoreCase((DocType.HOLDINGS.getCode()))) {
                List<HoldingsTree> holdingsTreeList = buildHoldingsTreeList(DocCategory.WORK.getCode(), docType, uuidList);
                for (HoldingsTree holdingsTree : holdingsTreeList) {
                    node = buildHoldingsNode(holdingsTree);
                    rootNode.addChild(node);
                }
            }

            if (docType.equalsIgnoreCase(DocType.ITEM.getCode())) {
                List<Item> itemList = buildItemTreeList(DocCategory.WORK.getCode(), docType, uuidList);
                for (Item item : itemList) {
                    node = buildItemNode(item);
                    rootNode.addChild(node);

                }
            }

            if (docType.equalsIgnoreCase(DocType.EHOLDINGS.getCode())) {
                List<HoldingsTree> holdingsTreeList = buildHoldingsTreeList(DocCategory.WORK.getCode(), docType, uuidList);
                for (HoldingsTree holdingsTree : holdingsTreeList) {
                    node = buildEHoldingsNode(holdingsTree);
                    rootNode.addChild(node);
                }
            }
        }
        return rootNode;
    }

    /**
     * @param category
     * @param docType
     * @param uuidList
     * @return
     */
    private List<BibTree> buildDocTreeList(String category, String docType, Collection<String> uuidList) {
        List<BibTree> bibTrees = buildDocTreeList(category, docType, uuidList, false);
        return bibTrees;
    }

    private List<BibTree> buildDocTreeList(String category, String docType, Collection<String> uuidList,boolean excludeEHoldings) {

        List<BibTree> bibTreeList = new ArrayList<>();

        if (uuidList != null && uuidList.size() > 0) {
            for (String uuid : uuidList) {
                BibTree bibTree = new BibTree();
                try {
                    bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(uuid);
                } catch (Exception e) {
                    LOG.error("Exception occurred in buildDocTreeList() :", e);
                }
                bibTreeList.add(bibTree);
            }
        }
        return bibTreeList;
    }

    private List<HoldingsTree> buildHoldingsTreeList(String category, String docType, Collection<String> uuidList) throws SolrServerException {

        List<HoldingsTree> holdingsTreeList = new ArrayList<HoldingsTree>();

        for (String uuid : uuidList) {
            HoldingsTree holdingsTree = new HoldingsTree();
            try {
                holdingsTree = getDocstoreClientLocator().getDocstoreClient().retrieveHoldingsTree(uuid);
            } catch (Exception e) {
                LOG.error("Exception occurred in buildHoldingsTreeList() :", e);
            }
            holdingsTreeList.add(holdingsTree);
        }
        return holdingsTreeList;
    }

    private List<Item> buildItemTreeList(String category, String docType, Collection<String> uuidList) throws SolrServerException {

        List<Item> itemList = new ArrayList<Item>();

        for (String uuid : uuidList) {
            Item item = new Item();
            try {
                item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(uuid);
            } catch (Exception e) {
                LOG.error("Exception occurred in buildItemTreeList() :", e);
            }
            itemList.add(item);
        }
        return itemList;
    }


    public void remove() {

    }

    /**
     * @param bibTreeList
     * @return
     */
    public List<Node<DocumentTreeNode, String>> buildNodeList(List<BibTree> bibTreeList) {
        List<Node<DocumentTreeNode, String>> nodeList = new ArrayList<Node<DocumentTreeNode, String>>();
        for (BibTree bibTree : bibTreeList) {
            DocumentTreeNode bibDocumentNode = new DocumentTreeNode();
            if (bibTree.getBib() != null) {
                bibDocumentNode.setBib(bibTree.getBib());
                Node<DocumentTreeNode, String> bibNode = new Node<DocumentTreeNode, String>(
                        new DocumentTreeNode(), bibDocumentNode.getTitle());
                bibNode.setNodeType(bibDocumentNode.getUuid());
                bibNode.setNodeLabel(bibDocumentNode.getTitle());
                List<HoldingsTree> holdingsTreeList = bibTree.getHoldingsTrees();
                Node<DocumentTreeNode, String> holdingsNode;
                Node<DocumentTreeNode, String> itemNode = null;
                if (holdingsTreeList != null && holdingsTreeList.size() > 0) {
                    for (HoldingsTree holdingsTree : holdingsTreeList) {
                        holdingsNode = buildHoldingsNode(holdingsTree);
                        bibNode.addChild(holdingsNode);
                    }
                }
                nodeList.add(bibNode);
            }
        }
        return nodeList;
    }

    private Node<DocumentTreeNode, String> buildHoldingsNode(HoldingsTree holdingsTree) {
        Node<DocumentTreeNode, String> instanceNode;
        List<Item> itemList = new ArrayList<>();
        DocumentTreeNode instanceDocumentNode = new DocumentTreeNode();
        instanceDocumentNode.setHoldings(holdingsTree);
        instanceNode = new Node<DocumentTreeNode, String>(
                new DocumentTreeNode(), instanceDocumentNode.getTitle());
        instanceNode.setNodeType(instanceDocumentNode.getUuid());
        instanceNode.setNodeLabel(instanceDocumentNode.getTitle());
        itemList = holdingsTree.getItems();
        if (itemList != null) {
            Node<DocumentTreeNode, String> itemNode;
            for (Item item : itemList) {
                itemNode = buildHoldingsNode(item, holdingsTree.getHoldings());
                instanceNode.addChild(itemNode);

            }
        }
        return instanceNode;
    }


    private Node<DocumentTreeNode, String> buildItemNode(Item item) {
        Node<DocumentTreeNode, String> itemNode;
        DocumentTreeNode itemDocumentNode = new DocumentTreeNode();
        itemDocumentNode.setWorkItemDocument(item);
        itemNode = new Node<DocumentTreeNode, String>(new DocumentTreeNode(), itemDocumentNode.getTitle());
        itemNode.setNodeType(itemDocumentNode.getUuid());
        itemNode.setNodeLabel(itemDocumentNode.getTitle());
        return itemNode;
    }

    private Node<DocumentTreeNode, String> buildHoldingsNode(Item item, Holdings holdings) {
        Node<DocumentTreeNode, String> itemNode;
        DocumentTreeNode itemDocumentNode = new DocumentTreeNode();
        itemDocumentNode.setWorkHoldingsDocument(item, holdings);
        itemNode = new Node<DocumentTreeNode, String>(new DocumentTreeNode(), itemDocumentNode.getTitle());
        itemNode.setNodeType(itemDocumentNode.getUuid());
        itemNode.setNodeLabel(itemDocumentNode.getTitle());
        return itemNode;
    }


    public Node<DocumentTreeNode, String> addForTransfer(List<String> uuidList, String docType) {
        if (docType.equalsIgnoreCase((DocType.BIB.getDescription()))) {
            List<BibTree> bibTreeList = buildDocTreeList(DocCategory.WORK.getCode(), DocType.BIB.getDescription(), uuidList);
            List<Node<DocumentTreeNode, String>> nodeList = buildNodeListForTransfer(bibTreeList);
            for (Node<DocumentTreeNode, String> node : nodeList) {
                rootNode.addChild(node);
            }
        } else if (docType.equalsIgnoreCase((DocType.HOLDINGS.getCode()))) {
            try {
                List<HoldingsTree> holdingsTreeList = buildHoldingsTreeList(DocCategory.WORK.getCode(), docType, uuidList);
                for (HoldingsTree holdingsTree : holdingsTreeList) {
                    Node<DocumentTreeNode,String> node = buildHoldingsNode(holdingsTree);
                    rootNode.addChild(node);
                }
            } catch (SolrServerException ex) {
                LOG.error(ex.getMessage());
            }
        }


        return rootNode;
    }

    /**
     * @param bibTreeList
     * @return
     */
    public List<Node<DocumentTreeNode, String>> buildNodeListForTransfer(List<BibTree> bibTreeList) {
        List<Node<DocumentTreeNode, String>> nodeList = new ArrayList<Node<DocumentTreeNode, String>>();
        for (BibTree bibTree : bibTreeList) {
            DocumentTreeNode bibDocumentNode = new DocumentTreeNode();
            bibDocumentNode.setBibTree(bibTree);
            bibDocumentNode.setBib(bibTree.getBib());
//            String title = bibDocumentNode.getBibTree().getBib().getTitle();
            Node<DocumentTreeNode, String> bibNode = new Node<DocumentTreeNode, String>(new DocumentTreeNode(), bibDocumentNode.getTitle());
            bibNode.setNodeType(bibDocumentNode.getUuid());
            bibNode.setNodeLabel(bibDocumentNode.getTitle());

            List<HoldingsTree> holdingsTreeList = bibTree.getHoldingsTrees();
            List<Item> itemList = new ArrayList<>();
            Node<DocumentTreeNode, String> holdingsNode;
            Node<DocumentTreeNode, String> itemNode = null;
            if (holdingsTreeList != null) {
                for (HoldingsTree holdingsTree : holdingsTreeList) {
                    DocumentTreeNode holdingsDocumentNode = new DocumentTreeNode();
//                    holdingsDocumentNode.setHoldings(holdingsTree.getHoldings());
                    holdingsDocumentNode.setHoldings(holdingsTree);
                    holdingsNode = new Node<DocumentTreeNode, String>(new DocumentTreeNode(), holdingsDocumentNode.getTitle());
                    holdingsNode.setNodeType(holdingsDocumentNode.getUuid());
                    holdingsNode.setNodeLabel(holdingsDocumentNode.getTitle());
                    itemList = holdingsTree.getItems();
                    if (itemList != null) {
                        for (Item item : itemList) {
                            DocumentTreeNode itemDocumentNode = new DocumentTreeNode();
                            itemDocumentNode.setItem(item);
                             itemNode = new Node<DocumentTreeNode, String>(new DocumentTreeNode(),
                                    itemDocumentNode.getTitle());
                            itemNode.setNodeType(itemDocumentNode.getUuid());
                            itemNode.setNodeLabel(itemDocumentNode.getTitle());
                            holdingsNode.addChild(itemNode);
                        }
                    }
                    bibNode.addChild(holdingsNode);
                }
            }
            nodeList.add(bibNode);
        }
        return nodeList;
    }
}