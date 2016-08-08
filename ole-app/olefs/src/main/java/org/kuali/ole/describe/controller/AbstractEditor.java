package org.kuali.ole.describe.controller;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.form.EditorForm;
import org.kuali.ole.describe.form.WorkBibMarcForm;
import org.kuali.ole.describe.form.WorkEInstanceOlemlForm;
import org.kuali.ole.describe.form.WorkInstanceOlemlForm;
import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.DonorInfo;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingScheme;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;
import org.kuali.ole.docstore.common.exception.DocstoreValidationException;
import org.kuali.ole.docstore.engine.client.DocstoreLocalClient;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.document.OLEEResourceInstance;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractEditor implements DocumentEditor {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractEditor.class);
    private DocstoreClientLocator docstoreClientLocator;
    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
    private DocstoreClient docstoreClient = null;

    public DocstoreClient getDocstoreLocalClient() {
        if (null == docstoreClient) {
            return new DocstoreLocalClient();
        }
        return docstoreClient;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return  SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    public AbstractEditor() {
    }

    /**
     * This method deletes the record from docstore based on the doc type.
     * @param editorForm
     * @param docId
     * @param operation
     * @throws Exception
     */
    protected void getResponseFromDocStore(EditorForm editorForm, String docId, String operation) throws Exception {
        try {
            if(DocType.BIB.getCode().equalsIgnoreCase(editorForm.getDocType())){
                Map<String, String> map = new HashMap<>();
                map.put(OLEConstants.BIB_ID, docId);
                List<OleCopy> oleCopy = (List<OleCopy>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCopy.class,map);
                if (oleCopy.size()==0) {
                    getDocstoreClientLocator().getDocstoreClient().deleteBib(docId);
                }else{
                    DocstoreException docstoreException = new DocstoreValidationException(DocstoreResources.COPY_DELETE_MESSAGE, DocstoreResources.COPY_DELETE_MESSAGE);
                    docstoreException.addErrorParams("DocId", docId);
                    throw docstoreException;
                }
            }  else if(DocType.HOLDINGS.getCode().equalsIgnoreCase(editorForm.getDocType()) || DocType.EHOLDINGS.getCode().equalsIgnoreCase(editorForm.getDocType())){
                if (!DocumentUniqueIDPrefix.hasPrefix(docId)) {
                    docId = DocumentUniqueIDPrefix.getPrefixedId(DocumentUniqueIDPrefix.PREFIX_WORK_HOLDINGS_OLEML, docId);
                    editorForm.setDocId(docId);
                }
                getDocstoreClientLocator().getDocstoreClient().deleteHoldings(docId);
                if (StringUtils.isNotBlank(docId)) {
                    Map<String, String> map = new HashMap<>();
                    map.put(OLEConstants.INSTANCE_ID, docId);
                    KRADServiceLocator.getBusinessObjectService().deleteMatching(OLEEResourceInstance.class, map);
                }
            } else if(DocType.ITEM.getCode().equalsIgnoreCase(editorForm.getDocType())){
                getDocstoreClientLocator().getDocstoreClient().deleteItem(docId);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * This method returns error message if it has exception else deletes record.
     * @param editorForm
     * @return
     */
    protected EditorForm deleteFromDocStore(EditorForm editorForm) {
        String docId = editorForm.getDocId();
        try {
            getResponseFromDocStore(editorForm, docId, OLEConstants.DELETE);
        } catch (Exception e) {
            DocstoreException docstoreException = (DocstoreException) e;
            String errorCode = docstoreException.getErrorCode();
            if (StringUtils.isNotEmpty(errorCode)) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, errorCode);
            } else {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
            }
            setDocTypeOfViewPage(editorForm);
            editorForm.getDocumentForm().setCanDeleteEHoldings(Boolean.FALSE);
            editorForm.setAddSpaceField(true);
            return editorForm.getDocumentForm();
        }
        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, OLEConstants.RECORD_DELETE_MESSAGE);
        removeDocumentFromTree(editorForm);
        editorForm.setAddSpaceField(true);
        editorForm.setShowDeleteTree(false);
        editorForm.setHasLink(true);
        editorForm.getDocumentForm().setViewId(OLEConstants.DELETE_VIEW_PAGE);
        return editorForm.getDocumentForm();
    }

    /**
     * This methods sets doc type to return to view page
     * @param editorForm
     */
    private void setDocTypeOfViewPage(EditorForm editorForm) {
        if (null != editorForm.getDocumentForm()) {
            if (editorForm.getDocumentForm() instanceof WorkBibMarcForm) {
                editorForm.setDocType(DocType.BIB.getCode());
            } else if (editorForm.getDocumentForm() instanceof WorkInstanceOlemlForm) {
                editorForm.setDocType(DocType.HOLDINGS.getCode());
            }  else if (editorForm.getDocumentForm() instanceof WorkEInstanceOlemlForm) {
                editorForm.setDocType(DocType.EHOLDINGS.getCode());
            }  else {
                editorForm.setDocType(DocType.ITEM.getCode());
            }
        }
    }

    /**
     * This method removes deleted record from documentForm bib tree to build left pane tree.
     * @param editorForm
     */
    protected void removeDocumentFromTree(EditorForm editorForm) {
        if (DocType.BIB.getCode().equalsIgnoreCase(editorForm.getDocType())) {
            List<BibTree> bibTreeList = editorForm.getDocumentForm().getBibTreeList();
            if (null != bibTreeList.get(0)) {
                BibTree bibTree = bibTreeList.get(0);
                if (null != bibTree.getBib() && null != bibTree.getBib().getId()) {
                    if (editorForm.getDocId().equalsIgnoreCase(bibTree.getBib().getId())) {
                        bibTreeList.remove(bibTree);
                    }
                }
            }
        } else if (DocType.HOLDINGS.getCode().equalsIgnoreCase(editorForm.getDocType()) || DocType.EHOLDINGS.getCode().equalsIgnoreCase(editorForm.getDocType())) {
            List<BibTree> bibTreeList = editorForm.getDocumentForm().getBibTreeList();
            boolean deleteHoldingsTree = false;
            int bibTreeIndex = 0;
            int holdingsTreeIndex = 0;
            for (bibTreeIndex = 0; bibTreeIndex < bibTreeList.size(); bibTreeIndex++) {
                if (!deleteHoldingsTree) {
                    BibTree bibTree = bibTreeList.get(bibTreeIndex);
                    if (bibTree != null) {
                        for (holdingsTreeIndex = 0; holdingsTreeIndex < bibTree.getHoldingsTrees().size(); holdingsTreeIndex++) {
                            HoldingsTree holdingsTree = bibTree.getHoldingsTrees().get(holdingsTreeIndex);
                            if (holdingsTree != null) {
                                if (null != holdingsTree.getHoldings() && editorForm.getDocId().equalsIgnoreCase(holdingsTree.getHoldings().getId())) {
                                    deleteHoldingsTree = true;
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            if (deleteHoldingsTree) {
                editorForm.getDocumentForm().getBibTreeList().get(--bibTreeIndex).getHoldingsTrees().remove(holdingsTreeIndex);
            }
        } else if (DocType.ITEM.getCode().equalsIgnoreCase(editorForm.getDocType())) {
            List<BibTree> bibTreeList = editorForm.getDocumentForm().getBibTreeList();
            boolean deleteItem = false;
            int bibTreeIndex = 0;
            int holdingsTreeIndex = 0;
            int itemIndex = 0;
            for (bibTreeIndex = 0; bibTreeIndex < bibTreeList.size(); bibTreeIndex++) {
                BibTree bibTree = bibTreeList.get(bibTreeIndex);
                if (bibTree != null) {
                    for (holdingsTreeIndex = 0; holdingsTreeIndex < bibTree.getHoldingsTrees().size(); holdingsTreeIndex++) {
                        if (!deleteItem) {
                            HoldingsTree holdingsTree = bibTree.getHoldingsTrees().get(holdingsTreeIndex);
                            if (holdingsTree != null) {
                                for (itemIndex = 0; itemIndex < holdingsTree.getItems().size(); itemIndex++) {
                                    Item item = holdingsTree.getItems().get(itemIndex);
                                    if (null != item) {
                                        if (editorForm.getDocId().equalsIgnoreCase(item.getId())) {
                                            deleteItem = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
            if (deleteItem) {
                editorForm.getDocumentForm().getBibTreeList().get(--bibTreeIndex).getHoldingsTrees().get(--holdingsTreeIndex).getItems().remove(itemIndex);
            }
        }
    }

    @Override
    public EditorForm loadDocument(EditorForm editorForm) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm saveDocument(EditorForm editorForm) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm deleteDocument(EditorForm editorForm) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm createNewRecord(EditorForm editorForm, BibTree bibTree) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm editNewRecord(EditorForm editorForm, BibTree bibTree) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String saveDocument(BibTree bibTree, EditorForm editorForm) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm addORDeleteFields(EditorForm editorForm, HttpServletRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm deleteVerify(EditorForm editorForm) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm delete(EditorForm editorForm) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm addORRemoveExtentOfOwnership(EditorForm editorForm, HttpServletRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm addORRemoveAccessInformationAndHoldingsNotes(EditorForm editorForm, HttpServletRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm addORRemoveItemNote(EditorForm editorForm, HttpServletRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm addORRemoveItemDonor(EditorForm editorForm, HttpServletRequest request) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EditorForm showBibs(EditorForm editorForm) {
        return null;
    }

    @Override
    public EditorForm copy(EditorForm editorForm) {
        return null;
    }

    @Override
    public Boolean isValidUpdate(EditorForm editorForm) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected HoldingsTree getHoldingsTree(String eResourceID) {
        HoldingsTree holdingsTree = new HoldingsTree();
        holdingsTree.setHoldings(getHoldingsRecord(eResourceID));
       /* if (!((StringUtils.isNotBlank(linkToOrderOption) && linkToOrderOption.equals(OLEConstants.NB_ELECTRONIC)) || StringUtils.isNotEmpty(eResourceID))) {
            holdingsTree.getItems().add(getItemRecord());
        }*/
        return holdingsTree;
    }

    protected Item getItemRecord() {
        Item item = new ItemOleml();
        item.setType(DocType.ITEM.getCode());
        item.setCategory("work");
        item.setFormat("olml");
        if(item.getDisplayLabel() == null) {
            item.setDisplayLabel("Item");
        }
        org.kuali.ole.docstore.common.document.content.instance.Item oleItem = new org.kuali.ole.docstore.common.document.content.instance.Item();
        CallNumber callNumber = new CallNumber();
        oleItem.setCallNumber(callNumber);
        List donorInfos = new ArrayList<DonorInfo>();
        DonorInfo donorInfo = new DonorInfo();
        donorInfos.add(donorInfo);
        oleItem.setDonorInfo(donorInfos);
        item.setContent(itemOlemlRecordProcessor.toXML(oleItem));
        return item;
    }

    protected Holdings getHoldingsRecord(String eResourceID) {
        Holdings holdings = null;
        OleHoldings oleHoldings = null;
        CallNumber callNumber;
        if (StringUtils.isNotEmpty(eResourceID)) {
            holdings = new EHoldings();
            oleHoldings = new OleHoldings();
            callNumber = new CallNumber();
            ShelvingScheme shelvingScheme = new ShelvingScheme();
            shelvingScheme.setCodeValue("none");
            callNumber.setShelvingScheme(shelvingScheme);
            oleHoldings.setCallNumber(callNumber);
            oleHoldings.setEResourceId(eResourceID);
        } /*else {
            holdings = new PHoldings();
            oleHoldings = new OleHoldings();
            callNumber = new CallNumber();
            oleHoldings.setCallNumber(callNumber);
        }*/
        if (holdings != null && oleHoldings != null)  {
            holdings.setContent(holdingOlemlRecordProcessor.toXML(oleHoldings));
        }
        return holdings;
    }

    protected  Holdings retrieveHoldings(String holdingsId) throws Exception{
        Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(holdingsId);
        return holdings;
    }

    protected  org.kuali.ole.docstore.common.document.Item retrieveItem(String itemId) throws Exception{
        org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemId);
        return item;
    }

    @Override
    public EditorForm bulkUpdate(EditorForm editorForm, List<String> ids) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getParameter(String applicationId, String namespace, String componentId, String parameterName) {
        ParameterKey parameterKey = ParameterKey.create(applicationId, namespace, componentId, parameterName);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);

        return parameter!=null?parameter.getValue():null;
    }

    public void setStaffOnly(EditorForm editorForm) {
        List<BibTree> bibTreeList = editorForm.getBibTreeList();
        if (bibTreeList != null) {
            for (BibTree bibTree : bibTreeList) {
                Bib bib = bibTree.getBib();
                if (bib != null) {
                    if (bib.isStaffOnly()) {
                        editorForm.setStaffOnlyFlagForBib(bib.isStaffOnly());
                        editorForm.setStaffOnlyFlagForHoldings(bib.isStaffOnly());
                        editorForm.setStaffOnlyFlagForItem(bib.isStaffOnly());
                    }
                }
            }
        }
    }
}