package org.kuali.ole.deliver.controller.checkout;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.asr.service.ASRHelperServiceImpl;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.ASRItem;
import org.kuali.ole.deliver.bo.OleLoanFastAdd;
import org.kuali.ole.deliver.controller.CircBaseController;
import org.kuali.ole.deliver.controller.FastAddItemController;
import org.kuali.ole.deliver.drools.DroolsExchange;
import org.kuali.ole.deliver.form.CircForm;
import org.kuali.ole.deliver.service.CircDeskLocationResolver;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.util.ErrorMessage;
import org.kuali.ole.describe.bo.InstanceEditorFormDataHandler;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.client.impl.NewDocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.utility.Constants;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Palanivelrajanb on 6/10/2015.
 */
@Controller
@RequestMapping(value = "/circFastAddController")
public class CircFastAddItemController extends CircBaseController {

    private static final Logger LOG = Logger.getLogger(FastAddItemController.class);
    private DocstoreClientLocator docstoreClientLocator;
    private InstanceEditorFormDataHandler instanceEditorFormDataHandler = null;
    BusinessObjectService businessObjectService;
    ASRHelperServiceImpl asrHelperService;
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor;
    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor;
    private DateFormat dateFormat;

    private InstanceEditorFormDataHandler getInstanceEditorFormDataHandler() {
        if (null == instanceEditorFormDataHandler) {
            instanceEditorFormDataHandler = new InstanceEditorFormDataHandler();
        }
        return instanceEditorFormDataHandler;
    }
    @RequestMapping(params = "methodToCall=fastAddItemDialog")
    public ModelAndView fastAddItemDialog(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                                          HttpServletRequest request, HttpServletResponse response) {
        CircForm circForm = (CircForm) form;
        OleLoanFastAdd oleLoanFastAdd = new OleLoanFastAdd();
        oleLoanFastAdd.setCallNumber("X");
        circForm.setOleLoanFastAdd(oleLoanFastAdd);
        String overrideParameters = "{closeBtn:false,autoSize : false}";
        showDialogWithOverrideParameters("fastAddDialog", form, overrideParameters);
        return getUIFModelAndView(circForm);
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

        CircForm circForm = (CircForm) form;
        if(null != circForm.getErrorMessage()){
            circForm.getErrorMessage().setErrorMessage("");
        }else{
            circForm.setErrorMessage(new ErrorMessage());
        }
        OleLoanFastAdd oleLoanFastAdd = circForm.getOleLoanFastAdd();
        oleLoanFastAdd.setItemType(request.getParameter("itemType"));
        oleLoanFastAdd.setCallNumberType(request.getParameter("callNumberType"));
        oleLoanFastAdd.setCheckinNote(request.getParameter("checkinNote"));
        oleLoanFastAdd.setNote(request.getParameter("note"));
        if (validFields(oleLoanFastAdd)) {
            Bib bib = processBibMarcRecord(oleLoanFastAdd);
            org.kuali.ole.docstore.common.document.content.instance.Item item = generateItemRecord(oleLoanFastAdd);
            org.kuali.ole.docstore.common.document.Item itemXml = generateItemXML(item);
            OleHoldings oleHoldings = generateHoldingsRecord(oleLoanFastAdd);
            HoldingsTree holdingsTree = processHoldingsTree(itemXml, oleHoldings);
            BibTree bibTree = new BibTree();
            bibTree.setBib(bib);
            bibTree.getHoldingsTrees().add(holdingsTree);
            if(checkItemAlreadyExist(oleLoanFastAdd.getBarcode())){
                String script = "jq('#fastAddItemErrorMessage').attr('style','display:inline');jq('#fastAddItemErrorMessage').focus();";
                circForm.setLightboxScript(script);
                return getUIFModelAndView(circForm);
            }
            else{
                if(circForm.getViewId().equals("fastAddView")){
                    circForm.setLightboxScript("submitForm('returnToDeliverTab',null,null,null,null);");
                }else{
                    circForm.setLightboxScript("jq.fancybox.close();submitForm('refresh',null,null,null,null);");
                }
            }
            NewDocstoreClientLocator.getInstance().getDocstoreClient(true).createBibTree(bibTree);
            if (item.getLocation() != null) {
                String location = getInstanceEditorFormDataHandler().getLocationCode(item.getLocation().getLocationLevel());
                if (getAsrHelperService().isAnASRItem(location)) {
                    processASRItem(oleLoanFastAdd, item, oleHoldings);
                }
            } else if (oleHoldings.getLocation() != null) {
                String location = getInstanceEditorFormDataHandler().getLocationCode(oleHoldings.getLocation().getLocationLevel());
                if (getAsrHelperService().isAnASRItem(location)) {
                    processASRForHoldings(oleLoanFastAdd, item, oleHoldings);
                }
            }

        } else {
            circForm.getErrorMessage().setErrorMessage(OLEConstants.REQ_FIELD);
            showDialog("generalMessageAndResetUIDialog", circForm, request, response);
        }


        circForm.setItemBarcode(oleLoanFastAdd.getBarcode());
        if(circForm.getDroolsExchange()==null) {
            DroolsExchange droolsExchange = new DroolsExchange();
            droolsExchange.addToContext("fastAddSuccess","success");
            circForm.setDroolsExchange(droolsExchange);
        }else{
            circForm.getDroolsExchange().addToContext("fastAddSuccess","success");
        }
        return getUIFModelAndView(circForm);
    }

    private boolean checkItemAlreadyExist(String barcode) {
        boolean exists = false;
        Map<String,String> itemRecordMap = new HashMap<String,String>();
        itemRecordMap.put("barCode",barcode);
        List<ItemRecord> itemRecords = (List<ItemRecord>)getBusinessObjectService().findMatching(ItemRecord.class,itemRecordMap);
        if(itemRecords.size()>0){
            exists = true;
        }
        return exists;

    }

    private boolean validFields(OleLoanFastAdd oleLoanFastAdd) {
        return !oleLoanFastAdd.getBarcode().isEmpty() &&
                !StringUtils.isBlank(oleLoanFastAdd.getCallNumberType()) &&
                !oleLoanFastAdd.getCallNumberType().equalsIgnoreCase("#")
                && !oleLoanFastAdd.getCallNumber().isEmpty() &&
                !oleLoanFastAdd.getCheckinNote().isEmpty() &&
                !oleLoanFastAdd.getLocationName().isEmpty() &&
                !oleLoanFastAdd.getItemType().isEmpty() &&
                !oleLoanFastAdd.getTitle().isEmpty();
    }

    private void processASRForHoldings(OleLoanFastAdd oleLoanFastAdd, org.kuali.ole.docstore.common.document.content.instance.Item item, OleHoldings oleHoldings) {
        String callNumber;
        String prefix;
        Map<String, String> asrItemMap = new HashMap<String, String>();
        asrItemMap.put("itemBarcode", item.getAccessInformation().getBarcode());
        List<ASRItem> asrItems = (List<ASRItem>) businessObjectService.findMatching(ASRItem.class, asrItemMap);
        if (asrItems.size() == 0) {
            ASRItem asrItem = new ASRItem();
            if (item.getAccessInformation() != null && item.getAccessInformation().getBarcode() != null) {
                asrItem.setItemBarcode(item.getAccessInformation().getBarcode());
            }
            if (oleLoanFastAdd.getTitle() != null) {
                asrItem.setTitle((oleLoanFastAdd.getTitle().length() > 37) ? oleLoanFastAdd.getTitle().substring(0, 36) : oleLoanFastAdd.getTitle());
            }
            if (oleLoanFastAdd.getAuthor() != null) {
                asrItem.setAuthor((oleLoanFastAdd.getAuthor().length() > 37) ? oleLoanFastAdd.getAuthor().substring(0, 36) : oleLoanFastAdd.getAuthor());
            }
            if (item.getCallNumber() != null && item.getCallNumber().getNumber() != null) {
                if (item.getCallNumber().getNumber() != null && !item.getCallNumber().getNumber().isEmpty()) {
                    callNumber = (item.getCallNumber().getNumber().length() > 37) ? item.getCallNumber().getNumber().substring(0, 36) : item.getCallNumber().getNumber();
                    prefix = item.getCallNumber().getPrefix() != null && !item.getCallNumber().getPrefix().isEmpty() ? item.getCallNumber().getPrefix() : "";
                    asrItem.setCallNumber(prefix + " " + callNumber);
                } else {
                    callNumber = (oleHoldings.getCallNumber().getNumber().length() > 37) ? oleHoldings.getCallNumber().getNumber().substring(0, 36) : oleHoldings.getCallNumber().getNumber();
                    prefix = oleHoldings.getCallNumber().getPrefix() != null && !oleHoldings.getCallNumber().getPrefix().isEmpty() ? oleHoldings.getCallNumber().getPrefix() : "";
                    asrItem.setCallNumber(prefix + " " + callNumber);
                }
            }
            getBusinessObjectService().save(asrItem);
        }
    }

    private void processASRItem(OleLoanFastAdd oleLoanFastAdd, org.kuali.ole.docstore.common.document.content.instance.Item item, OleHoldings oleHoldings) {
        String callNumber;
        String prefix;
        Map<String, String> asrItemMap = new HashMap<String, String>();
        asrItemMap.put("itemBarcode", item.getAccessInformation().getBarcode());
        List<ASRItem> asrItems = (List<ASRItem>) getBusinessObjectService().findMatching(ASRItem.class, asrItemMap);
        if (asrItems.size() == 0) {
            ASRItem asrItem = new ASRItem();
            if (item.getAccessInformation() != null && item.getAccessInformation().getBarcode() != null) {
                asrItem.setItemBarcode(item.getAccessInformation().getBarcode());
            }
            if (oleLoanFastAdd.getTitle() != null) {
                asrItem.setTitle((oleLoanFastAdd.getTitle().length() > 37) ? oleLoanFastAdd.getTitle().substring(0, 36) : oleLoanFastAdd.getTitle());
            }
            if (oleLoanFastAdd.getAuthor() != null) {
                asrItem.setAuthor((oleLoanFastAdd.getAuthor().length() > 37) ? oleLoanFastAdd.getAuthor().substring(0, 36) : oleLoanFastAdd.getAuthor());
            }
            if (item.getCallNumber() != null && item.getCallNumber().getNumber() != null) {
                if (item.getCallNumber().getNumber() != null && !item.getCallNumber().getNumber().isEmpty()) {
                    callNumber = (item.getCallNumber().getNumber().length() > 37) ? item.getCallNumber().getNumber().substring(0, 36) : item.getCallNumber().getNumber();
                    prefix = item.getCallNumber().getPrefix() != null && !item.getCallNumber().getPrefix().isEmpty() ? item.getCallNumber().getPrefix() : "";
                    asrItem.setCallNumber(prefix + " " + callNumber);
                } else {
                    callNumber = (oleHoldings.getCallNumber().getNumber().length() > 37) ? oleHoldings.getCallNumber().getNumber().substring(0, 36) : oleHoldings.getCallNumber().getNumber();
                    prefix = oleHoldings.getCallNumber().getPrefix() != null && !oleHoldings.getCallNumber().getPrefix().isEmpty() ? oleHoldings.getCallNumber().getPrefix() : "";
                    asrItem.setCallNumber(prefix + " " + callNumber);

                }
            }
            getBusinessObjectService().save(asrItem);
        }
    }

    private HoldingsTree processHoldingsTree(Item itemXml, OleHoldings oleHoldings) {
        oleHoldings.setHoldingsType("print");
        Holdings holdings = new PHoldings();
        holdings.setContent(getHoldingOlemlRecordProcessor().toXML(oleHoldings));
        holdings.setCreatedOn(String.valueOf(getDateFormat().format(new Date())));
        holdings.setLastUpdated(String.valueOf(getDateFormat().format(new Date())));
        holdings.setPublic(false);
        holdings.setFastAdd(true);
        holdings.setStatus("n"); // new Record
        HoldingsTree holdingsTree = new HoldingsTree();
        holdingsTree.setHoldings(holdings);
        holdingsTree.getItems().add(itemXml);
        return holdingsTree;
    }

    private Item generateItemXML(org.kuali.ole.docstore.common.document.content.instance.Item item) {
        Item itemXml = new ItemOleml();
        itemXml.setContent(getItemOlemlRecordProcessor().toXML(item));
        itemXml.setCreatedOn(String.valueOf(getDateFormat().format(new Date())));
        itemXml.setLastUpdated(String.valueOf(getDateFormat().format(new Date())));
        itemXml.setPublic(false);
        itemXml.setFastAdd(true);
        return itemXml;
    }

    private Bib processBibMarcRecord(OleLoanFastAdd oleLoanFastAdd) {
        BibMarcRecord bibMarcRecord = genereateBibRecord(oleLoanFastAdd.getTitle(), oleLoanFastAdd.getAuthor());
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
        return bib;
    }

    private BibMarcRecord genereateBibRecord(String title, String author) {

        BibMarcRecord bibMarcRecord = new BibMarcRecord();
        bibMarcRecord.setLeader("     na  a22     uu 4500");
        List<org.kuali.ole.docstore.common.document.content.bib.marc.DataField> dataFieldList = new ArrayList<org.kuali.ole.docstore.common.document.content.bib.marc.DataField>();
        org.kuali.ole.docstore.common.document.content.bib.marc.DataField titleDataField = new org.kuali.ole.docstore.common.document.content.bib.marc.DataField();
        titleDataField.setTag(OLEConstants.MARC_EDITOR_TITLE_245);
        List<org.kuali.ole.docstore.common.document.content.bib.marc.SubField> subFields = new ArrayList<org.kuali.ole.docstore.common.document.content.bib.marc.SubField>();
        org.kuali.ole.docstore.common.document.content.bib.marc.SubField subField = new org.kuali.ole.docstore.common.document.content.bib.marc.SubField();
        subField.setCode(OLEConstants.A);
        subField.setValue(title);
        subFields.add(subField);
        titleDataField.setSubFields(subFields);
        dataFieldList.add(titleDataField);

        if (author != null && !author.trim().isEmpty()) {
            org.kuali.ole.docstore.common.document.content.bib.marc.DataField authorDataField = new org.kuali.ole.docstore.common.document.content.bib.marc.DataField();
            authorDataField.setTag(OLEConstants.MARC_EDITOR_TITLE_100);
            subFields = new ArrayList<org.kuali.ole.docstore.common.document.content.bib.marc.SubField>();
            subField = new org.kuali.ole.docstore.common.document.content.bib.marc.SubField();
            subField.setCode(OLEConstants.A);
            subField.setValue(author);
            subFields.add(subField);
            authorDataField.setSubFields(subFields);
            dataFieldList.add(authorDataField);
        }

        bibMarcRecord.setDataFields(dataFieldList);
        return bibMarcRecord;

    }

    private HoldingOlemlRecordProcessor getHoldingOlemlRecordProcessor() {
        if (null == holdingOlemlRecordProcessor) {
            holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        }
        return holdingOlemlRecordProcessor;
    }

    private OleHoldings generateHoldingsRecord(OleLoanFastAdd oleLoanFastAdd) {
        OleHoldings oleHolding = new OleHoldings();
        LocationLevel locationLevel = new LocationLevel();
        locationLevel = getCircDeskLocationResolver().createLocationLevel(oleLoanFastAdd.getLocationName(), locationLevel);
        Location location = new Location();
        location.setPrimary(OLEConstants.TRUE);
        location.setStatus(OLEConstants.PERMANENT);
        location.setLocationLevel(locationLevel);

        oleHolding.setLocation(location);
        oleHolding.setCallNumber(getCallNumber(oleLoanFastAdd));
        oleHolding.setPrimary(OLEConstants.TRUE);
        return oleHolding;

    }

    private org.kuali.ole.docstore.common.document.content.instance.Item generateItemRecord(OleLoanFastAdd
                                                                                                    oleLoanFastAdd) {
        LOG.debug("Inside the getItemRecord method");
        org.kuali.ole.docstore.common.document.content.instance.Item item = new org.kuali.ole.docstore.common.document.content.instance.Item();
        ItemType itemType = new ItemType();
        itemType.setCodeValue(oleLoanFastAdd.getItemType());
        item.setItemType(itemType);
        item.setCallNumber(getCallNumber(oleLoanFastAdd));
        item.setCopyNumberLabel(oleLoanFastAdd.getCopyNumber());
        item.setEnumeration(oleLoanFastAdd.getEnumeration());
        item.setCheckinNote(oleLoanFastAdd.getCheckinNote());
        item.setFastAddFlag(true);
        item.setNumberOfPieces(oleLoanFastAdd.getNumberOfPieces());
        List<Note> notes = new ArrayList<Note>();
        Note note = new Note();
        note.setValue(oleLoanFastAdd.getNote());
        notes.add(note);
        item.setNote(notes);
        AccessInformation accessInformation = new AccessInformation();
        accessInformation.setBarcode(oleLoanFastAdd.getBarcode());
        item.setAccessInformation(accessInformation);
        OleItemAvailableStatus itemAvailableStatus = validateAndGetItemStatus(getParameter(OLEParameterConstants.FAST_ADD_ITEM_DEFAULT_STATUS));
        ItemStatus itemStatus = new ItemStatus();
        itemStatus.setCodeValue(itemAvailableStatus != null ? itemAvailableStatus.getItemAvailableStatusCode() : null);
        itemStatus.setFullValue(itemAvailableStatus != null ? itemAvailableStatus.getItemAvailableStatusCode() : null);
        item.setItemStatus(itemStatus);
        item.setCopyNumber(oleLoanFastAdd.getCopyNumber());
        return item;

    }

    private String getParameter(String fastAddItemDefaultStatus) {
        String parameter = ParameterValueResolver.getInstance().getParameter(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants
                .DLVR_CMPNT, fastAddItemDefaultStatus);
        return parameter;
    }

    public CallNumber getCallNumber(OleLoanFastAdd oleLoanFastAdd) {
        CallNumber callNumber = new CallNumber();
        callNumber.setNumber(oleLoanFastAdd.getCallNumber());
        callNumber.setPrefix(oleLoanFastAdd.getCallNumberPrefix());
        ShelvingScheme shelvingScheme = new ShelvingScheme();
        shelvingScheme.setCodeValue(oleLoanFastAdd.getCallNumberType());
        callNumber.setShelvingScheme(shelvingScheme);
        return callNumber;
    }

    public OleItemAvailableStatus validateAndGetItemStatus(String itemStatusCode) {
        Map criteriaMap = new HashMap();
        criteriaMap.put(OLEConstants.ITEM_STATUS_CODE, itemStatusCode);
        OleItemAvailableStatus itemAvailableStatus = getBusinessObjectService().findByPrimaryKey(OleItemAvailableStatus.class, criteriaMap);
        return itemAvailableStatus;
    }

    public BusinessObjectService getBusinessObjectService() {
        if (null == businessObjectService) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public CircDeskLocationResolver getCircDeskLocationResolver() {
        return new CircDeskLocationResolver();
    }

    public ItemOlemlRecordProcessor getItemOlemlRecordProcessor() {
        if (itemOlemlRecordProcessor == null) {
            itemOlemlRecordProcessor = SpringContext.getBean(ItemOlemlRecordProcessor.class);
        }
        return itemOlemlRecordProcessor;
    }

    public DateFormat getDateFormat() {
        if (null == dateFormat) {
            dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        }
        return dateFormat;
    }

    public ASRHelperServiceImpl getAsrHelperService() {
        if (null == asrHelperService) {
            return new ASRHelperServiceImpl();
        }
        return asrHelperService;
    }
}
