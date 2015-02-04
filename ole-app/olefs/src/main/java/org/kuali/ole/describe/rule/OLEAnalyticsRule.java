package org.kuali.ole.describe.rule;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.DocumentSelectionTree;
import org.kuali.ole.describe.bo.DocumentTreeNode;
import org.kuali.ole.describe.form.AnalyticsForm;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 3/19/14
 * Time: 7:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEAnalyticsRule {
    private static final Logger LOG = Logger.getLogger(OLEAnalyticsRule.class);

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public void selectCheckedNodesForSeriesTree(AnalyticsForm analyticsForm, Node<DocumentTreeNode, String> seriesTreeRootElement) {
        DocumentTreeNode documentTreeNode;
        BibTree bibTree = new BibTree();
        HoldingsTree holdingsTree = new HoldingsTree();
        Holdings holdings = new Holdings();
        Set<String> selectedBibsList = new HashSet<String>();
        List<String> selectedBibs = new ArrayList<String>();
        List<String> selectedBibsFromTree1 = new ArrayList<String>();
        List<String> selectedInstancesList = new ArrayList<String>();
        List<String> selectedHoldings = new ArrayList<String>();
        List<String> selectedHoldingsFromTree1 = new ArrayList<String>();
        String tree1BibId = null;
        Bib bib = new Bib();
        List<Item> items = new ArrayList<Item>();
        if (seriesTreeRootElement != null) {
            List<Node<DocumentTreeNode, String>> seriesTreeList = seriesTreeRootElement.getChildren();
            for (Node<DocumentTreeNode, String> bibNode : seriesTreeList) {
                documentTreeNode = bibNode.getData();
                LOG.info("Series-bibId-->" + bibNode.getNodeType());
                LOG.info("Series-bibNodeTitle-->" + bibNode.getData().getTitle());
                LOG.info("Series-bibNodeSelected-->" + documentTreeNode.isSelect());
                List<Node<DocumentTreeNode, String>> holdingsList = bibNode.getChildren();
                for (Node<DocumentTreeNode, String> holdingsNode : holdingsList) {
                    documentTreeNode = holdingsNode.getData();
                    LOG.info("Series-holdingsId-->" + holdingsNode.getNodeType());
                    LOG.info("Series-holdingsNodeTitle-->" + holdingsNode.getData().getTitle());
                    LOG.info("Series-holdingsNodeSelected-->" + documentTreeNode.isSelect());
                    if (documentTreeNode.isSelect()) {
                        bib.setId(bibNode.getNodeType());
                        selectedBibsList.add(bibNode.getNodeType());
                        selectedBibs.add(bibNode.getNodeType());
                        selectedBibsFromTree1.add(bibNode.getNodeType());
                        bib.setTitle(bibNode.getNodeLabel());
                        tree1BibId = bibNode.getNodeType();
                        analyticsForm.setTree1BibId(tree1BibId);
                        holdings.setId(holdingsNode.getNodeType());
                        selectedInstancesList.add(holdingsNode.getNodeType());
                        selectedHoldings.add(holdingsNode.getNodeType());
                        selectedHoldingsFromTree1.add(holdingsNode.getNodeType());
                        holdings.setLocationName(holdingsNode.getNodeLabel());
                        documentTreeNode.setSelect(true);
                        List<Node<DocumentTreeNode, String>> itemList = holdingsNode.getChildren();
                        for (Node<DocumentTreeNode, String> itemNode : itemList) {
                            Item itemDoc = new Item();
                            documentTreeNode = itemNode.getData();
                            LOG.info("Series-itemId-->" + holdingsNode.getNodeType());
                            LOG.info("Series-itemNodeTitle-->" + holdingsNode.getData().getTitle());
                            LOG.info("Series-itemNodeSelected-->" + documentTreeNode.isSelect());
                            itemDoc.setId(itemNode.getNodeType());
                            itemDoc.setCallNumber(itemNode.getNodeLabel());
                            items.add(itemDoc);
                        }
                        holdingsTree.getItems().addAll(items);
                        holdingsTree.setHoldings(holdings);
                        bibTree.getHoldingsTrees().add(holdingsTree);
                        bibTree.setBib(bib);
                        analyticsForm.setBibTree(bibTree);
                        analyticsForm.setSelectedBibsList(selectedBibsList);
                        analyticsForm.setSelectedInstancesList(selectedInstancesList);
                        analyticsForm.setSelectedBibs(selectedBibs);
                        analyticsForm.setSelectedHoldings(selectedHoldings);
                        analyticsForm.setSelectedBibsFromTree1(selectedBibs);
                        analyticsForm.setSelectedHoldingsFromTree1(selectedHoldings);
                    }
                }
            }
        }
    }

    public void selectCheckedNodesForAnalyticTree(AnalyticsForm analyticsForm, Node<DocumentTreeNode, String> analyticsTreeRootElement) {
        DocumentTreeNode documentTreeNode;
        BibTree bibTree = new BibTree();
        HoldingsTree holdingsTree = new HoldingsTree();
        Holdings holdings = new Holdings();
        List<Item> items = new ArrayList<Item>();
        Set<String> selectedItemsList = new HashSet<String>();
        ArrayList<String> setSelectedBibsFromTree2 = new ArrayList<String>();
        ArrayList<String> setSelectedItemsFromTree2 = new ArrayList<String>();
        ArrayList<String> selectedItems = new ArrayList<String>();
        if (analyticsTreeRootElement != null) {
            List<Node<DocumentTreeNode, String>> analyticTreeList = analyticsTreeRootElement.getChildren();
            for (Node<DocumentTreeNode, String> bibNode : analyticTreeList) {
                documentTreeNode = bibNode.getData();
                LOG.info("Analytic-bibId-->" + bibNode.getNodeType());
                LOG.info("Analytic-bibNodeTitle-->" + bibNode.getData().getTitle());
                LOG.info("Analytic-bibNodeSelected-->" + documentTreeNode.isSelect());
                List<Node<DocumentTreeNode, String>> holdingsList = bibNode.getChildren();
                for (Node<DocumentTreeNode, String> holdingsNode : holdingsList) {
                    documentTreeNode = holdingsNode.getData();
                    LOG.info("Analytic-holdingsId-->" + holdingsNode.getNodeType());
                    LOG.info("Analytic-holdingsNodeTitle-->" + holdingsNode.getData().getTitle());
                    LOG.info("Analytic-holdingsNodeSelected-->" + documentTreeNode.isSelect());
                    holdings.setId(holdingsNode.getNodeType());
                    holdings.setLocationName(holdingsNode.getNodeLabel());
                    List<Node<DocumentTreeNode, String>> itemList = holdingsNode.getChildren();
                    for (Node<DocumentTreeNode, String> itemNode : itemList) {
                        Item item = new Item();
                        documentTreeNode = itemNode.getData();
                        LOG.info("Analytic-itemId-->" + holdingsNode.getNodeType());
                        LOG.info("Analytic-itemNodeTitle-->" + holdingsNode.getData().getTitle());
                        LOG.info("Analytic-itemNodeSelected-->" + documentTreeNode.isSelect());
                        if (documentTreeNode.isSelect()) {
                            item.setId(itemNode.getNodeType());
                            item.setCallNumber(itemNode.getNodeLabel());
                            items.add(item);
                            setSelectedItemsFromTree2.add(itemNode.getNodeType());
                            selectedItems.add(itemNode.getNodeType());
                            selectedItemsList.add(itemNode.getNodeType());
                            documentTreeNode.setSelect(true);
                        }
                    }
                    analyticsForm.setSelectedItems(selectedItems);
                    analyticsForm.setSelectedItemsFromTree2(setSelectedItemsFromTree2);
                    analyticsForm.setSelectedItemsList(selectedItemsList);
                }
            }
        }
    }

    public void getAnalyticsSummaryByItemId(AnalyticsForm analyticsForm, String itemId) throws Exception {
        Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemId);
        if (item != null) {
            if (!CollectionUtils.isEmpty(item.getHoldings())) {
                for (Holdings holdings : item.getHoldings()) {
                    if (holdings.isSeries()) {
                        getAnalyticsSummaryByHoldingsId(analyticsForm, holdings.getId());
                    }
                }
            }
        }
    }

    public void getAnalyticsSummaryByHoldingsId(AnalyticsForm analyticsForm, String holdingsId) throws Exception {
        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
        Node<DocumentTreeNode, String> seriesRootNode = null;
        Node<DocumentTreeNode, String> analyticRootNode = null;
        List<String> bibUuidList = new ArrayList<String>();
        List<String> holdingsUuidList = new ArrayList<String>();
        HoldingsTree holdingsTree = getDocstoreClientLocator().getDocstoreClient().retrieveHoldingsTree(holdingsId);
        if (holdingsTree.getHoldings().isSeries()) {
            bibUuidList.add(holdingsTree.getHoldings().getBib().getId());
            seriesRootNode = documentSelectionTree.add(bibUuidList, org.kuali.ole.docstore.model.enums.DocType.BIB.getDescription(), true);
            analyticsForm.getLeftTree().setRootElement(seriesRootNode);
            analyticsForm.setShowSeriesTree(true);
            bibUuidList.clear();
            for (Item item : holdingsTree.getItems()) {
                if (!holdingsId.equalsIgnoreCase(item.getHolding().getId())) {
                    if (!holdingsUuidList.contains(item.getHolding().getId())) {
                        if (!bibUuidList.contains(item.getHolding().getBib().getId())) {
                            bibUuidList.add(item.getHolding().getBib().getId());
                        }
                        holdingsUuidList.add(item.getHolding().getId());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(bibUuidList)) {
                DocumentSelectionTree documentSelectionTree1 = new DocumentSelectionTree();
                analyticRootNode = documentSelectionTree1.add(bibUuidList, org.kuali.ole.docstore.model.enums.DocType.BIB.getDescription(), true);
                if (analyticRootNode != null) {
                    analyticsForm.getRightTree().setRootElement(analyticRootNode);
                    analyticsForm.setShowAnalyticsTree(true);
                }
            }
        } else {
            analyticsForm.setShowSeriesTree(false);
            analyticsForm.setShowAnalyticsTree(false);
        }
    }

    public Boolean validateInputForAnalytics(AnalyticsForm analyticsForm, String createOrBreak) throws Exception {
        DocumentTreeNode documentTreeNode;
        boolean isSeriesBibSelected = false, isSeriesHoldingsSelected = false, isSeriesItemSelected = false;
        boolean isAnalyticBibSelected = false, isAnalyticHoldingsSelected = false, isAnalyticItemSelected = false;
        Tree<DocumentTreeNode, String> leftTree = analyticsForm.getLeftTree();
        Tree<DocumentTreeNode, String> rightTree = analyticsForm.getRightTree();
        Node<DocumentTreeNode, String> leftTreeRootElement = leftTree.getRootElement();
        Node<DocumentTreeNode, String> rightTreeRootElement = rightTree.getRootElement();

        if (leftTreeRootElement == null && rightTreeRootElement == null) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_SERIES_AND_ANALYTICS);
        } else if (leftTreeRootElement != null && rightTreeRootElement == null) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_ANALYTICS);
        } else if (leftTreeRootElement == null && rightTreeRootElement != null) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_SERIES);
        } else if (leftTreeRootElement != null && rightTreeRootElement != null) {
            List<Node<DocumentTreeNode, String>> leftList = leftTree.getRootElement().getChildren();
            List<Node<DocumentTreeNode, String>> rightList = rightTree.getRootElement().getChildren();
            if (CollectionUtils.isEmpty(leftList) && CollectionUtils.isEmpty(rightList)) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_SERIES_AND_ANALYTICS);
            } else if (!CollectionUtils.isEmpty(leftList) && CollectionUtils.isEmpty(rightList)) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_ANALYTICS);
            } else if (CollectionUtils.isEmpty(leftList) && !CollectionUtils.isEmpty(rightList)) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_SERIES);
            } else if (!CollectionUtils.isEmpty(leftList) && !CollectionUtils.isEmpty(rightList)) {
                if (createOrBreak.equalsIgnoreCase(OLEConstants.CREATE_ANALYTICS)) {
                    for (Node<DocumentTreeNode, String> bibNode : leftList) {
                        documentTreeNode = bibNode.getData();
                        LOG.info("bibNode-->" + documentTreeNode.isSelect());
                        if (documentTreeNode.isSelect()) {
                            isSeriesBibSelected = true;
                        }
                        List<Node<DocumentTreeNode, String>> holdingsList = bibNode.getChildren();
                        for (Node<DocumentTreeNode, String> holdingsNode : holdingsList) {
                            documentTreeNode = holdingsNode.getData();
                            LOG.info("holdingsNode-->" + documentTreeNode.isSelect());
                            if (documentTreeNode.isSelect()) {
                                isSeriesHoldingsSelected = true;
                            }
                            List<Node<DocumentTreeNode, String>> itemList = holdingsNode.getChildren();
                            for (Node<DocumentTreeNode, String> itemNode : itemList) {
                                documentTreeNode = itemNode.getData();
                                LOG.info("itemNode-->" + documentTreeNode.isSelect());
                                if (documentTreeNode.isSelect()) {
                                    isSeriesItemSelected = true;
                                }
                            }
                        }
                    }
                    if (isSeriesBibSelected || isSeriesItemSelected) {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_ONLY_HOLDINGS);
                    } else if (!isSeriesHoldingsSelected) {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_ONE_HOLDINGS);
                    }
                }

                BibTree bibTree = null;
                List<String> analyticItemIds = new ArrayList<>();
                if (createOrBreak.equalsIgnoreCase(OLEConstants.BREAK_ANALYTICS)) {
                    String bibId = null;
                    bibId = leftList.get(0).getNodeType();
                    bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibId);
                    analyticItemIds = getAnalyticItemIdsByBibTree(bibTree);
                }

                StringBuffer itemLabelsBuffer = new StringBuffer();
                for (Node<DocumentTreeNode, String> bibNode : rightList) {
                    documentTreeNode = bibNode.getData();
                    LOG.info("bibNode-->" + documentTreeNode.isSelect());
                    if (documentTreeNode.isSelect()) {
                        isAnalyticBibSelected = true;
                    }
                    List<Node<DocumentTreeNode, String>> holdingsList = bibNode.getChildren();
                    for (Node<DocumentTreeNode, String> holdingsNode : holdingsList) {
                        documentTreeNode = holdingsNode.getData();
                        LOG.info("holdingsNode-->" + documentTreeNode.isSelect());
                        if (documentTreeNode.isSelect()) {
                            isAnalyticHoldingsSelected = true;
                        }
                        List<Node<DocumentTreeNode, String>> itemList = holdingsNode.getChildren();
                        for (Node<DocumentTreeNode, String> itemNode : itemList) {
                            documentTreeNode = itemNode.getData();
                            LOG.info("itemNode-->" + documentTreeNode.isSelect());
                            if (documentTreeNode.isSelect()) {
                                isAnalyticItemSelected = true;
                                if (createOrBreak.equalsIgnoreCase(OLEConstants.BREAK_ANALYTICS)) {
                                    String itemLabel = validateAnalyticRelatedItem(analyticItemIds, itemNode);
                                    if (StringUtils.isNotBlank(itemLabel)) {
                                        if (StringUtils.isBlank(itemLabelsBuffer.toString())) {
                                            itemLabelsBuffer.append("\n");
                                        }
                                        itemLabelsBuffer.append(itemLabel).append("\n");
                                    }
                                } else {
                                    if (isAnalytic(itemNode.getNodeType())) {
                                        itemLabelsBuffer.append(itemNode.getNodeLabel());
                                    }
                                }
                            }
                        }
                    }
                }
                if (isAnalyticBibSelected || isAnalyticHoldingsSelected) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_ONLY_ITEMS);
                } else if (!isAnalyticItemSelected) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_ONE_ITEM);
                } else if (StringUtils.isNotBlank(itemLabelsBuffer.toString()) && !isAnalyticBibSelected && !isAnalyticHoldingsSelected) {
                    if (createOrBreak.equalsIgnoreCase(OLEConstants.BREAK_ANALYTICS)) {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_ITEM_NOT_ANALYTIC);
                    } else {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.ANALYTICS_SELECTION_SECTION, OLEConstants.ERROR_SELECT_ITEM_IN_ANALYTIC_RELATION);
                    }
                }
            }
        }

        int errorCount = GlobalVariables.getMessageMap().getErrorCount();
        if (errorCount > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isAnalytic(String itemId) throws Exception {
        boolean isAnalytic = false;
        if (StringUtils.isNotBlank(itemId)) {
            Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemId);
            if (item.isAnalytic()) {
                isAnalytic = true;
            }
        }
        return isAnalytic;
    }

    public List<String> getAnalyticItemIdsByBibTree(BibTree bibTree) {
        List<String> analyticItemIdList = new ArrayList<>();
        if (bibTree != null) {
            if (!CollectionUtils.isEmpty(bibTree.getHoldingsTrees())) {
                for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
                    if (!CollectionUtils.isEmpty(holdingsTree.getItems())) {
                        for (Item item : holdingsTree.getItems()) {
                            if (item.isAnalytic()) {
                                if (!analyticItemIdList.contains(item.getId())) {
                                    analyticItemIdList.add(item.getId());
                                }
                            }
                        }
                    }
                }
            }
        }
        return analyticItemIdList;
    }

    public String validateAnalyticRelatedItem(List<String> analyticItemIds, Node<DocumentTreeNode, String> item) {
        String itemLabel = null;
        if (!CollectionUtils.isEmpty(analyticItemIds)) {
            if (!analyticItemIds.contains(item.getNodeType())) {
                itemLabel = item.getNodeLabel();
            }
        }
        return itemLabel;
    }


}