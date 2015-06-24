package org.kuali.ole.deliver.controller;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.StringUtils;
import org.kuali.asr.service.ASRHelperServiceImpl;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.ASRItem;
import org.kuali.ole.deliver.bo.OleLoanFastAdd;
import org.kuali.ole.deliver.form.OleLoanForm;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.describe.bo.InstanceEditorFormDataHandler;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.client.DocstoreClientLocatorService;
import org.kuali.ole.docstore.common.client.impl.DocstoreClientLocatorServiceImpl;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.exception.DocstoreException;
import org.kuali.ole.docstore.model.xmlpojo.ingest.Response;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.model.xstream.ingest.ResponseHandler;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.Constants;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 3/15/13
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/fastAddController")
public class FastAddItemController extends UifControllerBase {
    private static final Logger LOG = Logger.getLogger(FastAddItemController.class);
    private DocstoreClientLocator docstoreClientLocator;
    private InstanceEditorFormDataHandler instanceEditorFormDataHandler = null;
    BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
    ASRHelperServiceImpl asrHelperService = new ASRHelperServiceImpl();

    private InstanceEditorFormDataHandler getInstanceEditorFormDataHandler() {
        if (null == instanceEditorFormDataHandler) {
            instanceEditorFormDataHandler = new InstanceEditorFormDataHandler();
        }
        return instanceEditorFormDataHandler;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    @Override
    protected OleLoanForm createInitialForm(HttpServletRequest request) {
        return new OleLoanForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        OleLoanFastAdd oleLoanFastAdd = new OleLoanFastAdd();
        oleLoanFastAdd.setCallNumberType(OLEConstants.DEFAULT_CALL_NUMBER_TYPE);
        oleLoanFastAdd.setCallNumber(OLEConstants.DEFAULT_CALL_NUMBER);
        oleLoanForm.setOleLoanFastAdd(oleLoanFastAdd);
        return getUIFModelAndView(oleLoanForm, "FastAddItemViewPage");
    }

    /**
     * Create a Bib and Instance record in docstore.
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(params = "methodToCall=createFastAddItem")
    public ModelAndView createFastAddItem(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {

        LOG.debug("Inside the create fast add item method");
        OleLoanForm oleLoanForm = (OleLoanForm) form;
        oleLoanForm.setItemFlag("false");
        String callNumber;
        String prefix;
        String maxSessionTime = oleLoanForm.getMaxTimeForCheckOutConstant();
        if (LOG.isInfoEnabled()){
            LOG.info("session timeout:" + maxSessionTime);
        }
        if (maxSessionTime != null && !maxSessionTime.equalsIgnoreCase(""))
            oleLoanForm.setMaxSessionTime(Integer.parseInt(maxSessionTime));
        oleLoanForm.setInformation("");
        oleLoanForm.setReturnInformation("");
        LoanProcessor loanProcessor = new LoanProcessor();
        if (!oleLoanForm.getOleLoanFastAdd().getBarcode().isEmpty() && !oleLoanForm.getOleLoanFastAdd().getCallNumberType().isEmpty() && !oleLoanForm.getOleLoanFastAdd().getCallNumberType().equalsIgnoreCase("#") && !oleLoanForm.getOleLoanFastAdd().getCallNumber().isEmpty() && !oleLoanForm.getOleLoanFastAdd().getCheckinNote().isEmpty() && !oleLoanForm.getOleLoanFastAdd().getLocationName().isEmpty() && !oleLoanForm.getOleLoanFastAdd().getItemType().isEmpty()&&!oleLoanForm.getOleLoanFastAdd().getTitle().isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
            Item item = loanProcessor.getItemRecord(oleLoanForm.getOleLoanFastAdd());
            OleHoldings oleHoldings = loanProcessor.getHoldingRecord(oleLoanForm.getOleLoanFastAdd());
            oleHoldings.setHoldingsType("print");
            HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
            BibMarcRecord bibMarcRecord = loanProcessor.getBibMarcRecord(oleLoanForm.getOleLoanFastAdd().getTitle(), oleLoanForm.getOleLoanFastAdd().getAuthor());
            List<BibMarcRecord> bibMarcRecordList = new ArrayList<>();
            bibMarcRecordList.add(bibMarcRecord);
            BibMarcRecords bibMarcRecords = new BibMarcRecords();
            bibMarcRecords.setRecords(bibMarcRecordList);
            BibMarcRecordProcessor bibMarcRecordProcessor = new BibMarcRecordProcessor();
            Bib bib = new BibMarc();
            bib.setCategory(DocCategory.WORK.getCode());
            bib.setType(DocType.BIB.getCode());
            bib.setFormat(DocFormat.MARC.getCode());
            bib.setContent(bibMarcRecordProcessor.toXml(bibMarcRecords));
            ItemOlemlRecordProcessor itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
            org.kuali.ole.docstore.common.document.Item itemXml = new ItemOleml();
            itemXml.setContent(itemOlemlRecordProcessor.toXML(item));
            itemXml.setCreatedOn(String.valueOf(dateFormat.format(new Date())));
            itemXml.setLastUpdated(String.valueOf(dateFormat.format(new Date())));
            itemXml.setPublic(false);
            itemXml.setFastAdd(true);
            Holdings holdings = new PHoldings();
            holdings.setContent(holdingOlemlRecordProcessor.toXML(oleHoldings));
            holdings.setCreatedOn(String.valueOf(dateFormat.format(new Date())));
            holdings.setLastUpdated(String.valueOf(dateFormat.format(new Date())));
            holdings.setPublic(false);
            holdings.setFastAdd(true);
            holdings.setStatus("n"); // new Record
            HoldingsTree holdingsTree = new HoldingsTree();
            holdingsTree.setHoldings(holdings);
            holdingsTree.getItems().add(itemXml);
            BibTree bibTree = new BibTree();
            bibTree.setBib(bib);
            bibTree.getHoldingsTrees().add(holdingsTree);
            try {
                getDocstoreClientLocator().getDocstoreClient().createBibTree(bibTree);
                if (item.getLocation() != null) {
                    String location = instanceEditorFormDataHandler.getLocationCode(item.getLocation().getLocationLevel());
                    if (asrHelperService.isAnASRItem(location)) {

                        Map<String, String> asrItemMap = new HashMap<String, String>();
                        asrItemMap.put("itemBarcode", item.getAccessInformation().getBarcode());
                        List<ASRItem> asrItems = (List<ASRItem>) businessObjectService.findMatching(ASRItem.class, asrItemMap);
                        if (asrItems.size() == 0) {
                            ASRItem asrItem = new ASRItem();
                            if (item.getAccessInformation() != null && item.getAccessInformation().getBarcode() != null) {
                                asrItem.setItemBarcode(item.getAccessInformation().getBarcode());
                            }
                            if (oleLoanForm.getOleLoanFastAdd().getTitle() != null) {
                                asrItem.setTitle((oleLoanForm.getOleLoanFastAdd().getTitle().length() > 37) ? oleLoanForm.getOleLoanFastAdd().getTitle().substring(0, 36) : oleLoanForm.getOleLoanFastAdd().getTitle());
                            }
                            if (oleLoanForm.getOleLoanFastAdd().getAuthor() != null) {
                                asrItem.setAuthor((oleLoanForm.getOleLoanFastAdd().getAuthor().length() > 37) ? oleLoanForm.getOleLoanFastAdd().getAuthor().substring(0, 36) : oleLoanForm.getOleLoanFastAdd().getAuthor());
                            }
                            if (item.getCallNumber() != null && item.getCallNumber().getNumber() != null) {
                                if (item.getCallNumber().getNumber() != null && !item.getCallNumber().getNumber().isEmpty()) {
                                    callNumber = (item.getCallNumber().getNumber().length() > 37) ? item.getCallNumber().getNumber().substring(0, 36) : item.getCallNumber().getNumber();
                                    prefix=item.getCallNumber().getPrefix() !=null&&!item.getCallNumber().getPrefix() .isEmpty()?item.getCallNumber().getPrefix() :"";
                                    asrItem.setCallNumber(prefix+ " " + callNumber);
                                } else {
                                    callNumber = (oleHoldings.getCallNumber().getNumber().length() > 37) ? oleHoldings.getCallNumber().getNumber().substring(0, 36) : oleHoldings.getCallNumber().getNumber();
                                    prefix=oleHoldings.getCallNumber().getPrefix()!=null&&!oleHoldings.getCallNumber().getPrefix().isEmpty()?oleHoldings.getCallNumber().getPrefix():"";
                                    asrItem.setCallNumber(prefix+" " + callNumber);

                                }
                            }
                            businessObjectService.save(asrItem);
                        }
                    }
                } else if (oleHoldings.getLocation() != null) {
                    String location = getInstanceEditorFormDataHandler().getLocationCode(oleHoldings.getLocation().getLocationLevel());
                    if (asrHelperService.isAnASRItem(location)) {
                        Map<String, String> asrItemMap = new HashMap<String, String>();
                        asrItemMap.put("itemBarcode", item.getAccessInformation().getBarcode());
                        List<ASRItem> asrItems = (List<ASRItem>) businessObjectService.findMatching(ASRItem.class, asrItemMap);
                        if (asrItems.size() == 0) {
                            ASRItem asrItem = new ASRItem();
                            if (item.getAccessInformation() != null && item.getAccessInformation().getBarcode() != null) {
                                asrItem.setItemBarcode(item.getAccessInformation().getBarcode());
                            }
                            if (oleLoanForm.getOleLoanFastAdd().getTitle() != null) {
                                asrItem.setTitle((oleLoanForm.getOleLoanFastAdd().getTitle().length() > 37) ? oleLoanForm.getOleLoanFastAdd().getTitle().substring(0, 36) : oleLoanForm.getOleLoanFastAdd().getTitle());
                            }
                            if (oleLoanForm.getOleLoanFastAdd().getAuthor() != null) {
                                asrItem.setAuthor((oleLoanForm.getOleLoanFastAdd().getAuthor().length() > 37) ? oleLoanForm.getOleLoanFastAdd().getAuthor().substring(0, 36) : oleLoanForm.getOleLoanFastAdd().getAuthor());
                            }
                            if (item.getCallNumber() != null && item.getCallNumber().getNumber() != null) {
                                if (item.getCallNumber().getNumber() != null && !item.getCallNumber().getNumber().isEmpty()) {
                                    callNumber = (item.getCallNumber().getNumber().length() > 37) ? item.getCallNumber().getNumber().substring(0, 36) : item.getCallNumber().getNumber();
                                    prefix=item.getCallNumber().getPrefix()!=null&&!item.getCallNumber().getPrefix().isEmpty()?item.getCallNumber().getPrefix():"";
                                    asrItem.setCallNumber(prefix + " " + callNumber);
                                } else {
                                    callNumber = (oleHoldings.getCallNumber().getNumber().length() > 37) ? oleHoldings.getCallNumber().getNumber().substring(0, 36) : oleHoldings.getCallNumber().getNumber();
                                    prefix=oleHoldings.getCallNumber().getPrefix()!=null&&!oleHoldings.getCallNumber().getPrefix().isEmpty()?oleHoldings.getCallNumber().getPrefix():"";
                                    asrItem.setCallNumber(prefix+ " " + callNumber);
                                }
                            }
                            businessObjectService.save(asrItem);
                        }
                    }
                }
            } catch (DocstoreException e) {
                LOG.error("Exception while creating fast add item",e);
                DocstoreException docstoreException = (DocstoreException) e;
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(docstoreException.getErrorCode())) {
                    Map<String, String> paramsMap = docstoreException.getErrorParams();
                    if (paramsMap != null && paramsMap.size() > 0 && paramsMap.containsKey("barcode")) {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode(), paramsMap.get("barcode"));
                    } else {
                        GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, docstoreException.getErrorCode());
                    }
                } else {
                    GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, e.getMessage());
                }
                oleLoanForm.setItemFlag("true");
            } catch (Exception e) {
                LOG.error("Exception ", e);
                GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, "docstore.response", e.getMessage());
                oleLoanForm.setItemFlag("true");
            }
        } else {
            oleLoanForm.setInformation(OLEConstants.REQ_FIELD);
        }
        oleLoanForm.setItem(oleLoanForm.getOleLoanFastAdd().getBarcode());
        oleLoanForm.setFastAddItemIndicator(true);
        LoanController.fastAddBarcode = oleLoanForm.getItem();
        return getUIFModelAndView(oleLoanForm, "FastAddItemViewPage");
    }


}
