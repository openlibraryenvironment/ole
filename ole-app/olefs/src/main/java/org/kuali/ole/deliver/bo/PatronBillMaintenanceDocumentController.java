package org.kuali.ole.deliver.bo;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.search.SearchResponse;
import org.kuali.ole.docstore.common.search.SearchResult;
import org.kuali.ole.docstore.common.search.SearchResultField;

import org.kuali.ole.service.impl.OlePatronBillMaintenanceDocumentServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.bo.PersistableAttachment;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceUtils;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.MaintenanceDocumentService;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/5/12
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/patronBillMaintenance")
public class PatronBillMaintenanceDocumentController extends MaintenanceDocumentController {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(org.kuali.ole.deliver.bo.PatronBillMaintenanceDocumentController.class);
    private BusinessObjectService businessObjectService;
    private DocstoreClientLocator docstoreClientLocator;
    private PatronBillHelperService patronBillHelperService;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    /**
     * Gets the businessObjectService attribute.
     *
     * @return Returns the businessObjectService
     */
    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public PatronBillHelperService getPatronBillHelperService() {
        if(patronBillHelperService==null){
            patronBillHelperService=new PatronBillHelperService();
        }
        return patronBillHelperService;
    }

    /**
     * Default method for controller that setups a new
     * <code>MaintenanceView</code> with the default new action
     */
    @RequestMapping(params = "methodToCall=" + KRADConstants.Maintenance.METHOD_TO_CALL_NEW)
    @Override
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        Date billDate = new Date();
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        setupMaintenance(maintenanceForm, request, KRADConstants.MAINTENANCE_NEW_ACTION);
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        if (document.getDocumentHeader() != null) {
            document.getDocumentHeader().setDocumentDescription("New Patron Bill");
        }
        PatronBillPayment patronBillPayment = (PatronBillPayment) document.getDocumentDataObject();
        Map patronMap = new HashMap();
        patronMap.put(OLEConstants.OleDeliverRequest.PATRON_ID, request.getParameter(OLEConstants.PTRN_ID));
        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class, patronMap);
        if (olePatronDocumentList != null && olePatronDocumentList.size() > 0) {
            OlePatronDocument olePatronDocument = olePatronDocumentList.get(0);
            if (CollectionUtils.isNotEmpty(olePatronDocument.getPatronBillPayments())) {
                olePatronDocument.setPatronBillFileName(OLEConstants.OlePatron.PATRON_VIEW_BILLS);
                olePatronDocument.setViewBillUrl(OLEConstants.OlePatron.PATRON_VIEW_BILL_URL + olePatronDocument.getOlePatronId());
            }
            patronBillPayment.setOlePatron(olePatronDocumentList.get(0));
        }
        patronBillPayment.setPatronId(request.getParameter(OLEConstants.PTRN_ID));
        patronBillPayment.setFirstName(request.getParameter(OLEConstants.PTRN_FN));
        patronBillPayment.setLastName(request.getParameter(OLEConstants.PTRN_LN));
        /*Map proxyPatronMap = new HashMap();
        String firstName = "";
        String lastName = "";
        proxyPatronMap.put("olePatronId",request.getParameter(OLEConstants.PTRN_ID));
        List<OleProxyPatronDocument> oleProxyPatronDocumentList = (List<OleProxyPatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OleProxyPatronDocument.class,proxyPatronMap);
        String proxyPatronId = "";
        if(oleProxyPatronDocumentList.size() > 0){
            proxyPatronId = oleProxyPatronDocumentList.get(0).getProxyPatronId();
            Map patronMap = new HashMap();
            patronMap.put("olePatronId",proxyPatronId);
            List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,patronMap);
            if(olePatronDocumentList.size() > 0){
                 firstName = olePatronDocumentList.get(0).getEntity().getNames().get(0).getFirstName();
                 lastName = olePatronDocumentList.get(0).getEntity().getNames().get(0).getLastName();
                 patronBillPayment.setProxyFirstName(firstName);
                 patronBillPayment.setProxyLastName(lastName);
            }
        }*/
        patronBillPayment.setBillDate(new java.sql.Date(billDate.getTime()));
        return getUIFModelAndView(maintenanceForm);
    }


    /**
     * This method returns the instance of olePatronBillMaintenanceDocumentService
     *
     * @return olePatronMaintenanceDocumentService(MaintenanceDocumentService)
     */
    @Override
    protected MaintenanceDocumentService getMaintenanceDocumentService() {
        return GlobalResourceLoader.getService(OLEConstants.OlePatron.OLE_PTRN_BILL_MAIN_DOC_SER);
    }


    /**
     * This method will add the patron bill  document to the existing list and also stores the attachment
     * to the specified path.
     *
     * @param uifForm - MaintenanceDocumentForm
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=addToTotalAmount")
    public ModelAndView addToTotalAmount(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOG.debug("Initialized addToTotalAmount method");
        ModelAndView modelAndView = super.addLine(uifForm, result, request, response);
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        PatronBillPayment patronBillPayment = (PatronBillPayment) document.getNewMaintainableObject().getDataObject();
        BigDecimal amount = new BigDecimal(0.0);
        if (patronBillPayment.getFeeType() != null) {
            List<FeeType> feeTypes = patronBillPayment.getFeeType();
            for (int i = 0; i < feeTypes.size(); i++) {
                amount = amount.add(feeTypes.get(i).getFeeAmount().bigDecimalValue());
            }
            patronBillPayment.setTotalAmount(new KualiDecimal(amount));

        }
        return modelAndView;
    }


    /**
     * Called by the add line action for a new collection line. Method
     * determines which collection the add action was selected for and invokes
     * the view helper service to add the line
     */

    @RequestMapping(params = "methodToCall=addFeeType")
    public ModelAndView addFeeType(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Initialized addFeeType method");
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        PatronBillPayment patronBillPayment = (PatronBillPayment) document.getNewMaintainableObject().getDataObject();
        patronBillPayment.setZeroFeeAmount(false);
        patronBillPayment.setRequiredFeeAmount(false);
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        CollectionGroup collectionGroup = form.getPostedView().getViewIndex().getCollectionGroupByPath(
                selectedCollectionPath);
        String addLinePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
        Object eventObject = ObjectPropertyUtils.getPropertyValue(form, addLinePath);
        FeeType feeType = (FeeType) eventObject;
        if(form.getMaintenanceAction().equalsIgnoreCase("New")){
            Map<String,String> map=new HashMap<String,String>();
            map.put("paymentStatusCode","PAY_OUTSTN");
            OlePaymentStatus olePaymentStatus=(OlePaymentStatus)KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OlePaymentStatus.class,map);
            if(olePaymentStatus!=null){
                feeType.setOlePaymentStatus(olePaymentStatus);
                feeType.setPaymentStatus(olePaymentStatus.getPaymentStatusId());
            }
        }
        String itemBarcode = feeType.getItemBarcode();
        String itemUUID = feeType.getItemUuid();
        String itemType = feeType.getItemType();
        String itemTitle = feeType.getItemTitle();
        String feeAmount = feeType.getFeeAmount() != null ? feeType.getFeeAmount().toString() : "";
        if (feeAmount.isEmpty()) {
            patronBillPayment.setRequiredFeeAmount(true);
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_PATRON_BILL_FEE_TYPE_SECTION_ID, OLEConstants.FEE_AMOUNT_REQUIRED, "FeeAmount Field");
            return getUIFModelAndView(form);
        }
        if(feeType.getFeeAmount()!=null && feeType.getFeeAmount().bigDecimalValue().compareTo(OLEConstants.BIGDECIMAL_DEF_VALUE)<=0){
            patronBillPayment.setZeroFeeAmount(true);
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_PATRON_BILL_FEE_TYPE_SECTION_ID, OLEConstants.FEE_AMOUNT_ZERO_NOT_ALLOWED);
            return getUIFModelAndView(form);
        }
        feeType.setBalFeeAmount(feeType.getFeeAmount());
        String operatorId = GlobalVariables.getUserSession().getPrincipalId();
        feeType.setFeeSource(operatorId);
        if(feeType.getFeeTypes()!=null){
            feeType.getFeeTypes().add(feeType);
        }
        View view = form.getPostedView();
        patronBillPayment.setErrorMessage(null);

        if ((itemBarcode != null && !itemBarcode.isEmpty()) && (itemUUID == null || itemUUID.isEmpty())) {

            try {

                org.kuali.ole.docstore.common.document.Item item = new ItemOleml();
                org.kuali.ole.docstore.common.search.SearchParams search_Params = new org.kuali.ole.docstore.common.search.SearchParams();
                SearchResponse searchResponse = null;
                search_Params.getSearchConditions().add(search_Params.buildSearchCondition("phrase", search_Params.buildSearchField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), item.ITEM_BARCODE, itemBarcode), ""));


                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "id"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "title"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.BIB.getCode(), "author"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "itemType"));
              //  search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(),"TemporaryItemTypeCodeValue_search"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "Enumeration"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "Chronology"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "copyNumber"));
                search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(org.kuali.ole.docstore.common.document.content.enums.DocType.ITEM.getCode(), "callNumber"));
              //  search_Params.getSearchResultFields().add(search_Params.buildSearchResultField(DocType.HOLDINGS.getCode(), "callNumber"));


                searchResponse = getDocstoreClientLocator().getDocstoreClient().search(search_Params);


                for (SearchResult searchResult : searchResponse.getSearchResults()) {
                    for (SearchResultField searchResultField : searchResult.getSearchResultFields()) {
                        String fieldName = searchResultField.getFieldName();
                        String fieldValue = searchResultField.getFieldValue() != null ? searchResultField.getFieldValue() : "";

                        if (fieldName.equalsIgnoreCase("id") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {


                            feeType.setItemUuid(fieldValue);


                        } else if (fieldName.equalsIgnoreCase("title") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("bibliographic")) {
                            feeType.setItemTitle(fieldValue);
                        } else if (fieldName.equalsIgnoreCase("author") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("bibliographic")) {
                            feeType.setItemAuthor(fieldValue);
                        } else if (fieldName.equalsIgnoreCase("itemChronology") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                            feeType.setItemChronology(fieldValue);
                        } else if (fieldName.equalsIgnoreCase("itemEnumration") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                            feeType.setItemEnumeration(fieldValue);
                        } else if (fieldName.equalsIgnoreCase("callnumber") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                            feeType.setItemCallNumber(fieldValue);
                        }  else if (StringUtils.isEmpty(feeType.getItemCallNumber()) && fieldName.equalsIgnoreCase("callnumber") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("holdings")) {
                            feeType.setItemCallNumber(fieldValue);
                        } else if (fieldName.equalsIgnoreCase("copynumber") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                            feeType.setItemCopyNumber(fieldValue);
                        } else if (fieldName.equalsIgnoreCase("TemporaryItemTypeCodeValue_search") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                            feeType.setItemType(fieldValue);
                        } else if (StringUtils.isEmpty(feeType.getItemType()) && fieldName.equalsIgnoreCase("itemType") && !fieldValue.isEmpty() && searchResultField.getDocType().equalsIgnoreCase("item")) {
                            feeType.setItemType(fieldValue);
                        }

                    }
                }


            } catch (Exception ex) {
                LOG.error("Exception while adding fee type", ex);
                patronBillPayment.setErrorMessage(OLEConstants.OlePatron.INV_ITEM_BAR);
                return getUIFModelAndView(form);
            }
        }
        if(feeType.getItemUuid()!=null){
            getPatronBillHelperService().setFeeTypeInfo(feeType,feeType.getItemUuid());
        }
        patronBillPayment.setErrorMessage(null);
        view.getViewHelperService().processCollectionAddLine(view, form, selectedCollectionPath);
        return getUIFModelAndView(form);
    }

    @Override
    @RequestMapping(params = "methodToCall=maintenanceEdit")
    public ModelAndView maintenanceEdit(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {

        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        setupMaintenance(form, request, KRADConstants.MAINTENANCE_EDIT_ACTION);

        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        PatronBillPayment patronBillPayment = (PatronBillPayment) document.getOldMaintainableObject().getDataObject();
        List<FeeType> feeTypes=patronBillPayment.getFeeType();
        for(FeeType feeType:feeTypes){
            if(feeType.getFeeTypes()!=null){
                feeType.getFeeTypes().add(feeType);
            }
        }
        patronBillPayment.setFeeType(feeTypes);
        return super.maintenanceEdit(form, result, request, response);
    }

    @Override
    @RequestMapping(params = "methodToCall=maintenanceCopy")
    public ModelAndView maintenanceCopy(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        setupMaintenance(form, request, KRADConstants.MAINTENANCE_COPY_ACTION);
        super.maintenanceCopy(form,result,request, response);
        MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();
        PatronBillPayment patronBillPayment = (PatronBillPayment) document.getOldMaintainableObject().getDataObject();
        PatronBillPayment newpatronBillPayment = (PatronBillPayment) document.getNewMaintainableObject().getDataObject();
        List<FeeType> feeTypes=patronBillPayment.getFeeType();
        for(FeeType feeType:feeTypes){
            if (feeType.getItemUuid() != null) {
                org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(feeType.getItemUuid());
                ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
                Item itemContent = itemOlemlRecordProcessor.fromXML(item.getContent());
                OleHoldings oleHoldings = new HoldingOlemlRecordProcessor().fromXML(item.getHolding().getContent());
                if (feeType.getItemUuid().equals(item.getId())) {
                    feeType.setItemTitle(item.getHolding().getBib().getTitle());
                    feeType.setItemAuthor(item.getHolding().getBib().getAuthor());
                    /*if(itemContent.getCallNumber()!=null && !StringUtils.isEmpty(itemContent.getCallNumber().getNumber())){
                        feeType.setItemCallNumber((new LoanProcessor()).getItemCallNumber(itemContent.getCallNumber()));
                    }else {
                        feeType.setItemCallNumber((new LoanProcessor()).getItemCallNumber(oleHoldings.getCallNumber()));
                    }*/
                    feeType.setItemCallNumber((new LoanProcessor()).getItemCallNumber(itemContent.getCallNumber(),oleHoldings.getCallNumber()));
                    feeType.setItemCopyNumber(itemContent.getCopyNumber());
                    feeType.setItemChronology(itemContent.getChronology());
                    feeType.setItemEnumeration(itemContent.getEnumeration());
                    if(itemContent.getTemporaryItemType()!=null && itemContent.getTemporaryItemType().getCodeValue()!=null){
                        feeType.setItemType(itemContent.getTemporaryItemType().getCodeValue());
                    }else{
                        feeType.setItemType(itemContent.getItemType().getCodeValue());
                    }
                }
            }
            if(feeType.getFeeTypes()!=null){
                feeType.getFeeTypes().add(feeType);
            }
        }
        newpatronBillPayment.setFeeType(feeTypes);
        newpatronBillPayment.setBillNumber(null);
        return getUIFModelAndView(form);
    }




    /**
     * This method will add the agreement document to the existing list and also stores the attachment
     * to the specified path.
     *
     * @param uifForm - MaintenanceDocumentForm
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=deleteFromTotalAmount")
    public ModelAndView deleteFromTotalAmount(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                              HttpServletRequest request, HttpServletResponse response) {

        ModelAndView modelAndView = super.deleteLine(uifForm, result, request, response);
        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        PatronBillPayment patronBillPayment = (PatronBillPayment) document.getNewMaintainableObject().getDataObject();
        BigDecimal amount = new BigDecimal(0.0);
        String billNumber = "";
        Map feeTypeId = new HashMap();
        if (patronBillPayment.getFeeType() != null) {
            List<FeeType> feeTypes = patronBillPayment.getFeeType();
            for (int i = 0; i < feeTypes.size(); i++) {
                amount = amount.add(feeTypes.get(i).getFeeAmount().bigDecimalValue());
                billNumber = feeTypes.get(i).getBillNumber();
                feeTypeId.put(OLEConstants.OlePatron.BILL_PAYMENT_ID, billNumber);
            }
            patronBillPayment.setTotalAmount(new KualiDecimal(amount));
            //getBusinessObjectService().deleteMatching(FeeType.class, feeTypeId);

        }
        return modelAndView;
    }
    /**
     * This method will add the all fee amount and calculate total
     *
     * @param uifForm - MaintenanceDocumentForm
     * @return ModelAndView
     */
    @RequestMapping(params = "methodToCall=calculateTotal")
    public ModelAndView calculateTotal(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {

        MaintenanceDocumentForm form = (MaintenanceDocumentForm) uifForm;
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        PatronBillPayment patronBillPayment = (PatronBillPayment) document.getNewMaintainableObject().getDataObject();
        BigDecimal amount = new BigDecimal(0.0);
        BigDecimal balAmount = new BigDecimal(0.0);
        if (patronBillPayment.getFeeType() != null) {
            List<FeeType> feeTypes = patronBillPayment.getFeeType();
            for (FeeType feeType : feeTypes) {
                amount = amount.add(feeType.getFeeAmount().bigDecimalValue());
                balAmount = balAmount.add(feeType.getBalFeeAmount().bigDecimalValue());
            }
            patronBillPayment.setTotalAmount(new KualiDecimal(amount));
            patronBillPayment.setUnPaidBalance(new KualiDecimal(balAmount));
        }
        if (patronBillPayment.isRequiredFeeAmount()) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_PATRON_BILL_FEE_TYPE_SECTION_ID, OLEConstants.FEE_AMOUNT_REQUIRED, "FeeAmount Field");
            return getUIFModelAndView(form);
        }
        if(patronBillPayment.isZeroFeeAmount()){
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.OLE_PATRON_BILL_FEE_TYPE_SECTION_ID, OLEConstants.FEE_AMOUNT_ZERO_NOT_ALLOWED);
            return getUIFModelAndView(form);
        }
        patronBillPayment.setZeroFeeAmount(false);
        patronBillPayment.setRequiredFeeAmount(false);
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=" + "maintenanceDelete")
    public ModelAndView maintenanceDelete(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {
        setupMaintenanceForDelete(form, request, OLEConstants.OlePatron.OLE_PATRON_BILL_DELETE);
        return getUIFModelAndView(form);
    }


    @RequestMapping(params = "methodToCall=" + "deleteDocument")
    public ModelAndView deleteDocument(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws Exception {
        MaintenanceDocument document = form.getDocument();
        PatronBillPayment patronBillPayment = new PatronBillPayment();
        //OleProxyPatronDocument proxyPatronDocument = new OleProxyPatronDocument();

        if (document.getDocumentDataObject() != null) {
            patronBillPayment = (PatronBillPayment) document.getDocumentDataObject();
            if (patronBillPayment != null && patronBillPayment.getPatronId() != null) {
                KRADServiceLocator.getBusinessObjectService().delete(patronBillPayment);
                GlobalVariables.getMessageMap().putInfoWithoutFullErrorPath(KRADConstants.GLOBAL_ERRORS,
                        OLEConstants.OleLicenseRequest.MSG_DELETE_DOC);
            } else {
                GlobalVariables.getMessageMap().putErrorWithoutFullErrorPath(KRADConstants.GLOBAL_MESSAGES, OLEConstants.OleLicenseRequest.ERROR_CHECKLIST_NOT_FOUND);
                return getUIFModelAndView(form);
            }
        }
        return back(form, result, request, response);
    }


    /**
     * This method populates confirmation to delete the document.
     *
     * @param form
     * @param request
     * @param maintenanceAction
     */
    protected void setupMaintenanceForDelete(MaintenanceDocumentForm form, HttpServletRequest request, String maintenanceAction) {
        MaintenanceDocument document = form.getDocument();
        if (document == null) {
            document = getMaintenanceDocumentService()
                    .setupNewMaintenanceDocument(form.getDataObjectClassName(), form.getDocTypeName(),
                            maintenanceAction);

            form.setDocument(document);
            form.setDocTypeName(document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
        }
       // MaintenanceDocument document = (MaintenanceDocument) maintenanceForm.getDocument();

        form.setMaintenanceAction(maintenanceAction);
        OlePatronBillMaintenanceDocumentServiceImpl olePatronBillMaintenanceDocumentService = (OlePatronBillMaintenanceDocumentServiceImpl) getMaintenanceDocumentService();
        olePatronBillMaintenanceDocumentService.setupMaintenanceObjectForDelete(document, maintenanceAction, request.getParameterMap());
        MaintenanceUtils.checkForLockingDocument(document, false);
    }

    /**
     * Override route to retrieve the calculated total amount
     * <p/>
     * (DocumentFormBase, HttpServletRequest, HttpServletResponse)
     */
    @Override
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView;
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;

        try {
            calculateTotal(form, result, request, response);
        } catch (Exception e) {
            LOG.error("Exception while calculating total", e);
        }
        MaintenanceDocument document = (MaintenanceDocument) form.getDocument();
        PatronBillPayment patronBillPayment = (PatronBillPayment) document.getNewMaintainableObject().getDataObject();
        // patronBillPayment.setUnPaidBalance(patronBillPayment.getTotalAmount());
        if (patronBillPayment.getFeeType() != null) {
            List<FeeType> feeTypes = patronBillPayment.getFeeType();
            if (feeTypes.size() == 0) {
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, OLEConstants.ADD_FEE_TYPE);
                return getUIFModelAndView(maintenanceForm);
            }
        }
        if (document.getNewMaintainableObject().getDataObject() instanceof PersistableAttachment) {
            PersistableAttachment bo = (PersistableAttachment) getBusinessObjectService()
                    .retrieve((PersistableBusinessObject) document.getNewMaintainableObject().getDataObject());
            request.setAttribute(OLEConstants.OlePatron.FILE_NAME, bo.getFileName());
        }
        //modelAndView = getUIFModelAndView(form);
        if (LOG.isDebugEnabled()){
            LOG.debug("****************************** Patron Id" + patronBillPayment.getBillNumber());
        }
        if (patronBillPayment.getBillNumber() != null) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("billNumber", patronBillPayment.getBillNumber());
            List<PatronBillPayment> patronBillPaymentdblist = (List<PatronBillPayment>) KRADServiceLocator.getBusinessObjectService().findMatching(PatronBillPayment.class, map);
            if (patronBillPaymentdblist.size() > 0) {
                PatronBillPayment patronBillPaymentdb = patronBillPaymentdblist.get(0);
                List<FeeType> feeTypesdb = patronBillPaymentdb.getFeeType();
                for (FeeType feeTypedb : feeTypesdb) {
                    boolean isFound = false;
                    List<FeeType> feeTypesuser = patronBillPayment.getFeeType();
                    for (FeeType userfeeType : feeTypesuser) {
                        if (feeTypedb.getId().equals(userfeeType.getId())) {
                            isFound = true;
                        }
                    }
                    if (!isFound) {
                        Map<String, String> deleteItemHistoryMap = new HashMap<String, String>();
                        deleteItemHistoryMap.put("lineItemId", feeTypedb.getId());
                        List<OleItemLevelBillPayment> oleItemLevelBillPaymentsDB = (List<OleItemLevelBillPayment>) KRADServiceLocator.getBusinessObjectService().findMatching(OleItemLevelBillPayment.class, deleteItemHistoryMap);
                        if (oleItemLevelBillPaymentsDB.size() > 0) {
                            KRADServiceLocator.getBusinessObjectService().deleteMatching(OleItemLevelBillPayment.class, deleteItemHistoryMap);
                        }
                        Map<String, String> deleteFeeTypeMap = new HashMap<String, String>();
                        deleteFeeTypeMap.put("id", feeTypedb.getId());
                        KRADServiceLocator.getBusinessObjectService().deleteMatching(FeeType.class, deleteFeeTypeMap);
                    }
                }
            }
        }
        patronBillPayment.setErrorMessage(null);
        modelAndView = super.route(maintenanceForm, result, request, response);
        setViewBillUrlToDocument(maintenanceForm);
        return modelAndView;
    }

    /**
     * Override save method to set view bills url.
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView;
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        modelAndView = super.save(maintenanceForm, result, request, response);
        setViewBillUrlToDocument(maintenanceForm);
        return modelAndView;
    }

    /**
     * Override reload method to set view bills url.
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=reload")
    public ModelAndView reload(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView;
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) form;
        modelAndView = super.reload(maintenanceForm, result, request, response);
        setViewBillUrlToDocument(maintenanceForm);
        return modelAndView;
    }

    private void setViewBillUrlToDocument(MaintenanceDocumentForm maintenanceForm) {
        PatronBillPayment patronBillPayment = (PatronBillPayment) maintenanceForm.getDocument().getNewMaintainableObject().getDataObject();
        OlePatronDocument olePatronDocument = patronBillPayment.getOlePatron();
        if (CollectionUtils.isNotEmpty(olePatronDocument.getPatronBillPayments())) {
            olePatronDocument.setPatronBillFileName(OLEConstants.OlePatron.PATRON_VIEW_BILLS);
            olePatronDocument.setViewBillUrl(OLEConstants.OlePatron.PATRON_VIEW_BILL_URL + olePatronDocument.getOlePatronId());
        }
    }
}