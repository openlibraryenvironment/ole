package org.kuali.ole.select.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.describe.bo.DocumentSelectionTree;
import org.kuali.ole.describe.bo.DocumentTreeNode;
import org.kuali.ole.describe.form.BoundwithForm;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nd6967
 * Date: 16/7/13
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransferUtil {
    private static final Logger LOG = Logger.getLogger(TransferUtil.class);
    private static final TransferUtil transferUtil = new TransferUtil();

    public static TransferUtil getInstance() {
        return transferUtil;
    }

    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public void deleteVerify(BoundwithForm transferForm, List<String> ids) throws Exception {
        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
        if (transferForm.getTransferLeftTree()) {
            transferForm.setInDeleteLeftTree("true");
            transferForm.setInDeleteRightTree("false");
        } else if (transferForm.getTransferRighttree()) {
            transferForm.setInDeleteLeftTree("false");
            transferForm.setInDeleteRightTree("true");
        }
        if (transferForm.getDeleteVerifyResponse().equalsIgnoreCase("success")) {
            Node<DocumentTreeNode, String> docTree = documentSelectionTree.add(ids, transferForm.getDocType());
            transferForm.getDeleteConfirmationTree().setRootElement(docTree);
        } else {
            transferForm.setInDeleteLeftTree("false");
            transferForm.setInDeleteRightTree("false");
        }
    }

    public void transferInstances(List<String> selectedSourceInstances, String selectedDestBib) {
        try {
            getDocstoreClientLocator().getDocstoreClient().transferHoldings(selectedSourceInstances, selectedDestBib);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transferItems(List<String> selectedSourceItems, String selectedDestInstance) {
        try {
            getDocstoreClientLocator().getDocstoreClient().transferItems(selectedSourceItems, selectedDestInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void copyToTree(BoundwithForm transferForm, List<String> bibIdentifierListForTree, String treeId) {
        DocumentSelectionTree documentSelectionTree = new DocumentSelectionTree();
        Node<DocumentTreeNode, String> rootNode = documentSelectionTree.addForTransfer(bibIdentifierListForTree, transferForm.getDocType());
        if (LOG.isDebugEnabled()){
            LOG.debug("Tree id-->" + treeId);
        }
        if (treeId != null) {
            if (treeId.equalsIgnoreCase("leftTree")) {
                transferForm.getLeftTree().setRootElement(rootNode);
                transferForm.setLabelText("select");
            }
            if (treeId.equalsIgnoreCase("rightTree")) {
                transferForm.getRightTree().setRootElement(rootNode);
                transferForm.setTree2LabelText("select");
            }

        }

    }

    @Deprecated
    public String getDeleteResponseFromDocStore(String operation, String uuid, BoundwithForm transferForm)
            throws IOException {
        String restfulUrl = ConfigContext.getCurrentContextConfig().getProperty("docstore.restful.url");
        restfulUrl = restfulUrl.concat("/") + uuid;
        HttpClient httpClient = new HttpClient();
        DeleteMethod deleteMethod = new DeleteMethod(restfulUrl);
        NameValuePair nvp1 = new NameValuePair("identifierType", "UUID");
        NameValuePair nvp2 = new NameValuePair("operation", operation);

        NameValuePair category = new NameValuePair("docCategory", transferForm.getDocCategory());
        NameValuePair type = new NameValuePair("docType", transferForm.getDocType());
        NameValuePair format = new NameValuePair("docFormat", transferForm.getDocFormat());
        deleteMethod.setQueryString(new NameValuePair[]{nvp1, nvp2, category, type, format});
        int statusCode = httpClient.executeMethod(deleteMethod);
        if (LOG.isDebugEnabled()){
            LOG.debug("statusCode-->" + statusCode);
        }
        InputStream inputStream = deleteMethod.getResponseBodyAsStream();
        return IOUtils.toString(inputStream);
    }

    public String checkItemExistsInOleForBibs(List<String> bibIds) throws Exception {
        boolean itemCheck = false;
        BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
        for (String bibId : bibIds) {
            Map<String, String> map = new HashMap<>();
            map.put(OLEConstants.BIB_ID, bibId);
            List<OleCopy> listOfValues = (List<OleCopy>) boService.findMatching(OleCopy.class, map);
            if (listOfValues.size() != 0) {
                itemCheck = true;
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DELETE_PURCHASE_ORDER_FAIL_MESSAGE, "Item");
            } else {
                List<String> itemList = getItemsForBib(bibId);
                for (String itemUuid : itemList) {
                    Map<String, String> uuidMap = new HashMap<>();
                    uuidMap.put("itemUuid", itemUuid);
                    List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) boService.findMatching(OleLoanDocument.class, uuidMap);
                    if (oleLoanDocuments.size() != 0) {
                        itemCheck = true;
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DELETE_LOANED_FAIL_MESSAGE);
                        break;
                    } else {
                        List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>) boService.findMatching(OleDeliverRequestBo.class, uuidMap);
                        if (oleDeliverRequestBos.size() != 0) {
                            itemCheck = true;
                            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DELETE_REQUEST_FAIL_MESSAGE);
                            break;
                        }
                    }
                }
            }
        }
        if (!itemCheck) {
            return "success";
        }
        return "Failed";
    }

    private List<String> getItemsForBib(String bibId) throws Exception {
        BibTree bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(bibId);
        List<String> itemsId = new ArrayList<>();
        for (HoldingsTree holdingsTree : bibTree.getHoldingsTrees()) {
            for (Item item : holdingsTree.getItems()) {
                itemsId.add(item.getId());
            }
        }
        return itemsId;
    }


    private List<String> getItemsForHoldings(String holdingsId) throws Exception {
        HoldingsTree holdingsTree = getDocstoreClientLocator().getDocstoreClient().retrieveHoldingsTree(holdingsId);
        List<String> itemsIds = new ArrayList<>();
        for (Item item : holdingsTree.getItems()) {
            itemsIds.add(item.getId());
        }
        return itemsIds;
    }

    public String checkItemExistsInOleForHoldings(List<String> holdingsIds) throws Exception {
        BusinessObjectService boService = KRADServiceLocator.getBusinessObjectService();
        boolean itemCheck = false;
        List<String> itemIdList = new ArrayList<>();
        for (String holdingsId : holdingsIds) {
            itemIdList.addAll(getItemsForHoldings(holdingsId));
        }
        for (String itemUuid : itemIdList) {
            Map<String, String> map = new HashMap<>();
            Map<String, String> uuidMap = new HashMap<>();
            map.put("itemUUID", itemUuid);
            uuidMap.put("itemUuid", itemUuid);
            List<OleCopy> listOfValues = (List<OleCopy>) boService.findMatching(OleCopy.class, map);
            if (listOfValues.size() != 0) {
                itemCheck = true;
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DELETE_PURCHASE_ORDER_FAIL_MESSAGE, "Item");
                break;
            } else {
                List<OleLoanDocument> oleLoanDocuments = (List<OleLoanDocument>) boService.findMatching(OleLoanDocument.class, uuidMap);
                if (oleLoanDocuments.size() != 0) {
                    itemCheck = true;
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DELETE_LOANED_FAIL_MESSAGE);
                    break;
                } else {
                    List<OleDeliverRequestBo> oleDeliverRequestBos = (List<OleDeliverRequestBo>) boService.findMatching(OleDeliverRequestBo.class, uuidMap);
                    if (oleDeliverRequestBos.size() != 0) {
                        itemCheck = true;
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.DELETE_REQUEST_FAIL_MESSAGE);
                        break;
                    }
                }
            }
        }
        if (!itemCheck) {
            return "success";
        }
        return "Failed";
    }

    public boolean checkItemIsBoundWith(List<String> holdingsIds) throws Exception {
        boolean isBoundwith = false;
        for (String id : holdingsIds) {
            Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(id);
            if (holdings.isBoundWithBib()) {
                isBoundwith = true;
                break;
            }
        }
        return isBoundwith;
    }

}
