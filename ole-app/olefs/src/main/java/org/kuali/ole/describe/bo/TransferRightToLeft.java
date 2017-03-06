package org.kuali.ole.describe.bo;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.form.BoundwithForm;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.select.service.DocumentUUIDUpdateService;
import org.kuali.ole.select.util.TransferUtil;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nd6967
 * Date: 1/18/13
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransferRightToLeft {
    private static final Logger LOG = Logger.getLogger(TransferRightToLeft.class);
    private DocstoreClientLocator docstoreClientLocator;
    private DocumentUUIDUpdateService documentUUIDUpdateService;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public DocumentUUIDUpdateService getDocumentUUIDUpdateService() {
        if (null == documentUUIDUpdateService) {
            documentUUIDUpdateService = SpringContext.getBean(DocumentUUIDUpdateService.class);
        }
        return documentUUIDUpdateService;
    }

    public void transferRightToLeft(BoundwithForm transferForm, BindingResult result, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        List<String> bibIdentifierListForRightTree = new ArrayList<String>();
        List<String> bibInstanceListForRightTree = new ArrayList<String>();
        List<String> bibInstanceListForLeftTree = new ArrayList<String>();
        List<String> bibInstanceListAllForRightTree = new ArrayList<String>();
        List<String> bibItemListForRightTree = new ArrayList<String>();
        List<String> bibIdentifierListForLeftTree = new ArrayList<String>();
        List<String> destBibIdentifierListForLeftTree = new ArrayList<String>();
        List<String> destInstanceIdentifierListForLeftTree = new ArrayList<String>();


        StringBuffer stringBufferLeftTree = new StringBuffer();
        StringBuffer stringBufferRightTree = new StringBuffer();
        String isRightBibsSelected = "false";
        String isLeftItemsSelected = "false";
        String status1 = "";
        String status2 = "";
        List<String> bibIdentifiersListWhenInstanceSelectedForRightTree = new ArrayList<String>();
        List<String> instanceIdentifiersListWhenItemSelectedForRightTree = new ArrayList<String>();
        boolean transferInstance = true;
        boolean transferItem = true;
        boolean success = Boolean.FALSE;
        String itemFailed = null;
        List<String> bibIdentifiersToDelete = new ArrayList<String>();
        List<String> instanceIdentifiersToDelete = new ArrayList<String>();
        Tree<DocumentTreeNode, String> leftTree = transferForm.getLeftTree();
        Node<DocumentTreeNode, String> leftTreeRootElement = leftTree.getRootElement();

        Tree<DocumentTreeNode, String> rightTree = transferForm.getRightTree();
        Node<DocumentTreeNode, String> rightTreeRootElement = rightTree.getRootElement();
        if(transferForm.getDocType().equalsIgnoreCase(OLEConstants.BIB_DOC_TYPE)) {
            status1 = selectCheckedNodesRightTree(rightTreeRootElement, bibIdentifierListForRightTree,
                    bibInstanceListForRightTree, bibItemListForRightTree,
                    stringBufferRightTree, bibIdentifiersListWhenInstanceSelectedForRightTree,
                    instanceIdentifiersListWhenItemSelectedForRightTree,
                    bibIdentifiersToDelete, instanceIdentifiersToDelete); //source
            selectCheckedNodesLeftTree(leftTreeRootElement, bibIdentifierListForLeftTree, stringBufferLeftTree,
                    destBibIdentifierListForLeftTree, destInstanceIdentifierListForLeftTree);//dest
        } else if(transferForm.getDocType().equalsIgnoreCase(OLEConstants.HOLDING_DOC_TYPE)) {
            selectCheckedNodesRightTreeForItemTransfer(rightTreeRootElement, bibInstanceListForRightTree, bibInstanceListAllForRightTree,
                                                       stringBufferRightTree, bibItemListForRightTree, instanceIdentifiersListWhenItemSelectedForRightTree, instanceIdentifiersToDelete); //source

            selectCheckedNodesLeftTreeForItemTransfer(leftTreeRootElement, bibInstanceListForLeftTree, stringBufferLeftTree, destInstanceIdentifierListForLeftTree);//dest
        }

        isRightBibsSelected = stringBufferRightTree.toString();
        isLeftItemsSelected = stringBufferLeftTree.toString();
        if (leftTreeRootElement == null) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "error.transfer.empty.records", "left");
        } else if (rightTreeRootElement == null) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "error.transfer.empty.records", "right");
        }else if (bibInstanceListForRightTree.size() > 0 && transferForm.getDocType().equalsIgnoreCase(OLEConstants.HOLDING_DOC_TYPE)) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "error.transfer.invalid", "Holding", "right");
        }else if (isRightBibsSelected.equalsIgnoreCase("true")) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "error.transfer.invalid", "Bib", "right");
        } else if (bibInstanceListForRightTree.size() == 0 && bibItemListForRightTree.size() == 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "error.transfer.selected.none", "right");
        } else if (isLeftItemsSelected.equalsIgnoreCase("true")) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "error.transfer.invalid", "Item", "right");
        } else if (destBibIdentifierListForLeftTree.size() == 0 && destInstanceIdentifierListForLeftTree.size() == 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_LEFT_TREE_SECTION, "error.transfer.selected.none", "left");
        } else if (bibInstanceListForRightTree.size() > 0 && bibItemListForRightTree.size() > 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "error.transfer.selected.multiple", "Instances", "items", "right");
        } else if (destBibIdentifierListForLeftTree.size() > 0 && destInstanceIdentifierListForLeftTree.size() > 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_LEFT_TREE_SECTION, "error.transfer.selected.multiple", "Bibs", "instances", "left");
        } else if (destBibIdentifierListForLeftTree.size() > 1) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_LEFT_TREE_SECTION, "error.transfer.selected.invalid", "bib", "left");
        } else if (destInstanceIdentifierListForLeftTree.size() > 1) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_LEFT_TREE_SECTION, "error.transfer.selected.invalid", "instance", "left");
        } else if (bibInstanceListForRightTree.size() > 0 && destInstanceIdentifierListForLeftTree.size() > 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "error.transfer.right", "Instances", "Instances");
        } else if (bibItemListForRightTree.size() > 0 && destBibIdentifierListForLeftTree.size() > 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_LEFT_TREE_SECTION, "error.transfer.right", "Items", "Bibs");
        }
        else if (bibInstanceListForRightTree.size() > 0) {

            for (String bibUuid : bibIdentifiersListWhenInstanceSelectedForRightTree) {
                if (destBibIdentifierListForLeftTree.contains(bibUuid)) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "error.transfer.bib");
                    transferInstance = false;
                    break;
                }
            }
            if (transferInstance) {
                String destBibIdentifier = destBibIdentifierListForLeftTree.get(0);
                if (bibIdentifiersToDelete.size() > 0) {
                    transferForm.setDocFormat(OLEConstants.MARC_FORMAT);
                    transferForm.setDocCategory(OLEConstants.WORK_CATEGORY);
                    transferForm.setDocType(OLEConstants.BIB_DOC_TYPE);
                    transferForm.setBibInstanceListForTree1(bibInstanceListForRightTree);
                    transferForm.setDestBibIdentifier(destBibIdentifier);

                    // According to OLE-6912 no need to check the item which is exist in Requisition or  PO or Loan
                    transferForm.setDeleteVerifyResponse("success");
//                    transferForm.setDeleteVerifyResponse(TransferUtil.getInstance().checkItemExistsInOleForBibs(bibIdentifiersToDelete));
                    transferForm.setDeleteIds(bibIdentifiersToDelete);
                    TransferUtil.getInstance().deleteVerify(transferForm, bibIdentifiersToDelete);
                    // Holding wouldn't transfer, if item is attached with Loan, PO, etc.
                    if (transferForm.getDeleteVerifyResponse().equalsIgnoreCase(OLEConstants.OLEBatchProcess.RESPONSE_STATUS_FAILED)) {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, OLEConstants.TRANSFER_FAIL_MESSAGE_ITEM_ATTACHED_OLE);
                    }
                    return;
                }
                for(String holding: bibInstanceListForRightTree) {
                       success = getDocumentUUIDUpdateService().updateCopyRecord(holding, destBibIdentifier);
                    if(!success) {
                        itemFailed = holding;
                        break;
                    }
                }
                if(success) {
                    TransferUtil.getInstance().transferInstances(bibInstanceListForRightTree, destBibIdentifier);
                    TransferUtil.getInstance().copyToTree(transferForm, bibIdentifierListForRightTree, OLEConstants.RIGHT_TREE);
                    TransferUtil.getInstance().copyToTree(transferForm, bibIdentifierListForLeftTree, OLEConstants.LEFT_TREE);
                    GlobalVariables.getMessageMap().putInfoForSectionId(OLEConstants.TRANSFER_LEFT_TREE_SECTION, "info.transfer", "Instances");
                }
                else {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "Update of PO Tables Failed for Holding Id >>>  " + itemFailed);
                }
            }
        } else if (bibItemListForRightTree.size() > 0) {
            //If Transfer of items happen between same left holding and right holding.
            for (String instanceUuid : instanceIdentifiersListWhenItemSelectedForRightTree) {
                if (destInstanceIdentifierListForLeftTree.contains(instanceUuid)) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "error.transfer.item");
                    transferItem = false;
                    break;
                }
            }
            if (transferItem) {
                String destInstanceIdentifier = destInstanceIdentifierListForLeftTree.get(0);
                if (instanceIdentifiersToDelete.size() > 0) {
                    transferForm.setDocFormat(OLEConstants.OLEML_FORMAT);
                    transferForm.setDocCategory(OLEConstants.WORK_CATEGORY);
                    transferForm.setDocType(OLEConstants.HOLDING_DOC_TYPE);
                    transferForm.setBibItemListForTree1(bibItemListForRightTree);
                    transferForm.setDestInstanceIdentifier(destInstanceIdentifier);
                    // According to OLE-6912 no need to check the item which is exist in Requisition or  PO or Loan
                    transferForm.setDeleteVerifyResponse("success");
//                    transferForm.setDeleteVerifyResponse(TransferUtil.getInstance().checkItemExistsInOleForHoldings(instanceIdentifiersToDelete));
                    transferForm.setDeleteIds(instanceIdentifiersToDelete);
                    TransferUtil.getInstance().deleteVerify(transferForm, instanceIdentifiersToDelete);
                    // Item wouldn't transfer, if item is attached with Loan, PO, etc.
                    if (transferForm.getDeleteVerifyResponse().equalsIgnoreCase(OLEConstants.OLEBatchProcess.RESPONSE_STATUS_FAILED)) {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, OLEConstants.TRANSFER_FAIL_MESSAGE_ITEM_ATTACHED_OLE);
                    }
                    return;
                }
                for (String item : bibItemListForRightTree) {
                    success = getDocumentUUIDUpdateService().updateCopyRecord(item, destInstanceIdentifier);
                    if (!success) {
                        itemFailed = item;
                        break;
                    }
                }
                if (success) {
                    TransferUtil.getInstance().transferItems(bibItemListForRightTree, destInstanceIdentifier);

                    if (transferForm.getDocType().equalsIgnoreCase((DocType.BIB.getDescription()))) {
                        TransferUtil.getInstance().copyToTree(transferForm, bibIdentifierListForRightTree, OLEConstants.RIGHT_TREE);
                        TransferUtil.getInstance().copyToTree(transferForm, bibIdentifierListForLeftTree, OLEConstants.LEFT_TREE);
                    } else if (transferForm.getDocType().equalsIgnoreCase((DocType.HOLDINGS.getCode()))) {
                        TransferUtil.getInstance().copyToTree(transferForm, bibInstanceListAllForRightTree, OLEConstants.RIGHT_TREE);
                        TransferUtil.getInstance().copyToTree(transferForm, bibInstanceListForLeftTree, OLEConstants.LEFT_TREE);
                    }
                    GlobalVariables.getMessageMap().putInfoForSectionId(OLEConstants.TRANSFER_LEFT_TREE_SECTION, "info.transfer", "Items");
                }
                else {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "Update of PO Tables Failed for Item Id >>>  " + itemFailed);
                }
            }
            else{
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.TRANSFER_RIGHT_TREE_SECTION, "error.transfer.bib");
            }

        }

        return;
    }

    private void selectCheckedNodesLeftTreeForItemTransfer(Node<DocumentTreeNode, String> rootElement, List<String> bibInstanceListForLeftTree,
                                                           StringBuffer stringBufferLeftTree,
                                                           List<String> destInstanceIdentifierListForLeftTree) {

        DocumentTreeNode documentTreeNode;
        if (rootElement != null) {
            List<Node<DocumentTreeNode, String>> instanceList = rootElement.getChildren();

                for (Node<DocumentTreeNode, String> instanceNode : instanceList) {
                    documentTreeNode = instanceNode.getData();
                    bibInstanceListForLeftTree.add(instanceNode.getNodeType());
                    if (documentTreeNode.isSelect()) {
                        String instanceUUID = instanceNode.getNodeType();
                        destInstanceIdentifierListForLeftTree.add(instanceUUID);
                    }
                    List<Node<DocumentTreeNode, String>> itemList = instanceNode.getChildren();
                    for (Node<DocumentTreeNode, String> itemNode : itemList) {
                        documentTreeNode = itemNode.getData();
                        if (documentTreeNode.isSelect()) {
                            stringBufferLeftTree.append("true");
                            break;
                        }
                    }

                }
        }

    }

    private void selectCheckedNodesRightTreeForItemTransfer(Node<DocumentTreeNode, String> rootElement, List<String> bibInstanceListForRightTree,
                                                            List<String> bibInstanceListAllForRightTree,
                                                            StringBuffer stringBufferRightTree,
                                                            List<String> bibItemListForRightTree,
                                                            List<String> instanceIdentifiersListWhenItemSelectedForRightTree,
                                                            List<String> instanceIdentifiersToDelete) {
        DocumentTreeNode documentTreeNode;
        int itemCount = 0;
        List<String> instanceIdentifiersListTemp = new ArrayList<String>();
        if (rootElement != null) {
            List<Node<DocumentTreeNode, String>> instanceList = rootElement.getChildren();
            for (Node<DocumentTreeNode, String> instanceNode : instanceList) {
                String instanceUUID = "";
                documentTreeNode = instanceNode.getData();
                instanceUUID = instanceNode.getNodeType();
                bibInstanceListAllForRightTree.add(instanceUUID);
                if (documentTreeNode.isSelect()) {
                    LOG.info("documentTreeNode.isSelectTree1()-->" + documentTreeNode.isSelect());
//                    stringBufferRightTree.append("true");
                    bibInstanceListForRightTree.add(instanceUUID);
                    break;
                }
                List<Node<DocumentTreeNode, String>> itemList = null;
                instanceIdentifiersListTemp.clear();
                itemCount = 0;
                itemList = instanceNode.getChildren();
                for (Node<DocumentTreeNode, String> itemNode : itemList) {
                        //itemCount=0;
                        documentTreeNode = itemNode.getData();
                        if (documentTreeNode.isSelect()) {
                            itemCount++;
                            String itemUUID = itemNode.getNodeType();
                            bibItemListForRightTree.add(itemUUID);
                            instanceIdentifiersListWhenItemSelectedForRightTree.add(instanceUUID);
                        }
                    } //item loop end
                    if (itemCount == itemList.size()) {
                        instanceIdentifiersListTemp.add(instanceUUID);
                        LOG.debug("in if of no items instanceidenifiersListTemp " + instanceIdentifiersListTemp);
                        //status="Instance will remain with no items if this transfer takes place. Instance must have atleast one item. Transfer failed";
                }
                //instance loop end

                instanceIdentifiersToDelete.addAll(instanceIdentifiersListTemp);
                LOG.debug("instanceIdentifiersToDelete " + instanceIdentifiersToDelete);

            }//bib loop end
        }


    }

    private String selectCheckedNodesRightTree(Node<DocumentTreeNode, String> rootElement,
                                               List<String> bibIdentifierListForTree1,
                                               List<String> bibInstanceListForTree1, List<String> bibItemListForTree1,
                                               StringBuffer stringBufferRightTree,
                                               List<String> bibIdentifiersListWhenInstanceSelectedForRightTree,
                                               List<String> instanceIdentifiersListWhenItemSelectedForRightTree,
                                               List<String> bibIdentifiersToDelete,
                                               List<String> instanceIdentifiersToDelete) {

        DocumentTreeNode documentTreeNode;
        String status = "success";
        int instanceCount = 0;
        int itemCount = 0;
        List<String> instanceidenifiersListTemp = new ArrayList<String>();
        if (rootElement != null) {
            List<Node<DocumentTreeNode, String>> list = rootElement.getChildren();
            for (Node<DocumentTreeNode, String> marcNode : list) {
                instanceCount = 0;
                String instanceUUID = "";
                documentTreeNode = marcNode.getData();
                String bibUUID = marcNode.getNodeType();
                LOG.debug(" bibUUID " + bibUUID);
                bibIdentifierListForTree1.add(bibUUID);
                LOG.info("documentTreeNode.isSelectTree1()-->" + documentTreeNode.isSelect());
                if (documentTreeNode.isSelect()) {
                    stringBufferRightTree.append("true");
                    break;
                }
                List<Node<DocumentTreeNode, String>> instanceList = marcNode.getChildren();
                List<Node<DocumentTreeNode, String>> itemList = null;
                instanceidenifiersListTemp.clear();
                for (Node<DocumentTreeNode, String> instanceNode : instanceList) {
                    itemCount = 0;
                    documentTreeNode = instanceNode.getData();
                    instanceUUID = instanceNode.getNodeType();
                    if (documentTreeNode.isSelect()) {
                        instanceCount++;
                        bibInstanceListForTree1.add(instanceUUID);
                        bibIdentifiersListWhenInstanceSelectedForRightTree.add(bibUUID);
                    }
                    itemList = instanceNode.getChildren();
                    for (Node<DocumentTreeNode, String> itemNode : itemList) {
                        //itemCount=0;
                        documentTreeNode = itemNode.getData();
                        if (documentTreeNode.isSelect()) {
                            itemCount++;
                            String itemUUID = itemNode.getNodeType();
                            bibItemListForTree1.add(itemUUID);
                            instanceIdentifiersListWhenItemSelectedForRightTree.add(instanceUUID);
                        }
                    } //item loop end
                    if (itemCount == itemList.size()) {
                        instanceidenifiersListTemp.add(instanceUUID);
                        LOG.debug("in if of no items instanceidenifiersListTemp " + instanceidenifiersListTemp);
                        //status="Instance will remain with no items if this transfer takes place. Instance must have atleast one item. Transfer failed";
                    }
                } //instance loop end
                if (instanceCount == instanceList.size()) {
                    bibIdentifiersToDelete.add(bibUUID);
                    LOG.debug("in if of no instances bibIdentifierToDelete " + bibIdentifiersToDelete);
                    status
                            = "Bib will remain with no instances if this transfer takes place. Bib must have atleast one instance. Transfer failed";
                }
                instanceIdentifiersToDelete.addAll(instanceidenifiersListTemp);
                LOG.debug("instanceIdentifiersToDelete " + instanceIdentifiersToDelete);
                //                else if(itemCount==itemList.size()){
                //                    status="Instance will remain with no items if this transfer takes place. Instance must have atleast one item. Transfer failed";
                //                }
            }//bib loop end
        }
        return status;
    }

    private void selectCheckedNodesLeftTree(Node<DocumentTreeNode, String> rootElement,
                                            List<String> bibIdentifierListForTree2, StringBuffer stringBufferLeftTree,
                                            List<String> destBibIdentifierListForTree2,
                                            List<String> destInstanceIdentifierListForTree2) {

        DocumentTreeNode documentTreeNode;
        if (rootElement != null) {
            List<Node<DocumentTreeNode, String>> list = rootElement.getChildren();
            for (Node<DocumentTreeNode, String> marcNode : list) {
                documentTreeNode = marcNode.getData();
                String bibUUID = marcNode.getNodeType();
                bibIdentifierListForTree2.add(bibUUID);
                LOG.info("documentTreeNode.isSelectTree1()-->" + documentTreeNode.isSelect());
                if (documentTreeNode.isSelect()) {
                    destBibIdentifierListForTree2.add(bibUUID);
                }
                List<Node<DocumentTreeNode, String>> instanceList = marcNode.getChildren();
                for (Node<DocumentTreeNode, String> instanceNode : instanceList) {
                    documentTreeNode = instanceNode.getData();
                    if (documentTreeNode.isSelect()) {
                        String instanceUUID = instanceNode.getNodeType();
                        destInstanceIdentifierListForTree2.add(instanceUUID);
                    }
                    List<Node<DocumentTreeNode, String>> itemList = instanceNode.getChildren();
                    for (Node<DocumentTreeNode, String> itemNode : itemList) {
                        documentTreeNode = itemNode.getData();
                        if (documentTreeNode.isSelect()) {
                            stringBufferLeftTree.append("true");
                            break;
                        }
                    }

                }
            }
        }
    }
}
