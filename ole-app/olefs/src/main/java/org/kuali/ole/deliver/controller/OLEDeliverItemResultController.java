package org.kuali.ole.deliver.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OLEItemNoteResultDisplayRow;
import org.kuali.ole.deliver.bo.OLESingleItemResultDisplayRow;
import org.kuali.ole.deliver.form.OLEDeliverItemResultForm;
import org.kuali.ole.deliver.service.OLEDeliverItemSearchService;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.Note;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 1/28/15.
 */
@Controller
@RequestMapping(value = "/deliverItemResultController")
public class OLEDeliverItemResultController extends UifControllerBase {

    private OLEDeliverItemSearchService oleDeliverItemSearchService;
    private DocstoreClientLocator docstoreClientLocator;

    public OLEDeliverItemSearchService getOleDeliverItemSearchService() {
        if (oleDeliverItemSearchService == null) {
            oleDeliverItemSearchService = GlobalResourceLoader.getService(OLEConstants.DELIVER_ITEM__SEARCH_SERVICE);
        }
        return oleDeliverItemSearchService;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new OLEDeliverItemResultForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        OLEDeliverItemResultForm deliverItemResultForm = (OLEDeliverItemResultForm) form;
        deliverItemResultForm.setSingleItemFlag(true);
        OLESingleItemResultDisplayRow oleSingleItemResultDisplayRow = new OLESingleItemResultDisplayRow();
        String itemid = request.getParameter(OLEConstants.OleDeliverRequest.ITEM_ID);
        Map itemIdMap = new HashMap();
        itemIdMap.put(OLEConstants.ITEM_UUID, itemid);
        org.kuali.ole.docstore.common.document.Item item = null;
        try {
            item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(itemid);
            oleSingleItemResultDisplayRow.setLocation(item.getLocation());
            oleSingleItemResultDisplayRow.setId(itemid);
            oleSingleItemResultDisplayRow.setCreatedDate(item.getCreatedOn());
            oleSingleItemResultDisplayRow.setCreatedBy(item.getCreatedBy());
            oleSingleItemResultDisplayRow.setUpdatedBy(item.getUpdatedBy());
            oleSingleItemResultDisplayRow.setUpdatedDate(item.getUpdatedOn());
            if (item.getHolding() != null) {
                oleSingleItemResultDisplayRow.setHoldingsLocation(item.getHolding().getLocationName());
                oleSingleItemResultDisplayRow.setHoldingsIdentifier(item.getHolding().getId());
                if (item.getHolding().getBib() != null) {
                    Bib bib = item.getHolding().getBib();
                    oleSingleItemResultDisplayRow.setBibIdentifier(bib.getId());
                    oleSingleItemResultDisplayRow.setTitle(bib.getTitle());
                    oleSingleItemResultDisplayRow.setAuthor(bib.getAuthor());
                    oleSingleItemResultDisplayRow.setPublication(bib.getPublisher() +" "+ bib.getPublicationDate());
                    oleSingleItemResultDisplayRow.setIsbn(bib.getIsbn());

                    BibMarcRecordProcessor recordProcessor = new BibMarcRecordProcessor();
                    if (bib.getContent() != null) {
                        BibMarcRecords bibMarcRecords = recordProcessor.fromXML(bib.getContent());
                        if (bibMarcRecords.getRecords() != null && bibMarcRecords.getRecords().size() > 0) {
                            DataField dataField = bibMarcRecords.getRecords().get(0).getDataFieldForTag("035");
                            if (dataField != null) {
                                List<SubField> subFields = dataField.getSubFields();
                                if (subFields != null && subFields.size() > 0) {
                                    for (SubField subField : subFields) {
                                        if (subField.getCode().equals("a")) {
                                            oleSingleItemResultDisplayRow.setOclcNumber(subField.getValue());
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Object itemContent = item.getContentObject();
            if (itemContent != null) {
                org.kuali.ole.docstore.common.document.content.instance.Item itemPojo = (org.kuali.ole.docstore.common.document.content.instance.Item) itemContent;
                if (itemPojo.getItemType() != null)
                    oleSingleItemResultDisplayRow.setItemType(itemPojo.getItemType().getCodeValue());
                if (itemPojo.getItemStatus() != null)
                    oleSingleItemResultDisplayRow.setItemStatus(itemPojo.getItemStatus().getCodeValue());
                if (itemPojo.getAccessInformation() != null)
                    oleSingleItemResultDisplayRow.setItemBarCode(itemPojo.getAccessInformation().getBarcode());
                if (itemPojo.getCallNumber() != null && StringUtils.isNotBlank(itemPojo.getCallNumber().getNumber())) {
                    oleSingleItemResultDisplayRow.setCallNumber(itemPojo.getCallNumber().getNumber());
                } else if (item.getHolding() != null) {
                    OleHoldings holdingsContent = item.getHolding().getContentObject();
                    if (holdingsContent != null && holdingsContent.getCallNumber() != null && StringUtils.isNotBlank(holdingsContent.getCallNumber().getNumber())) {
                        oleSingleItemResultDisplayRow.setCallNumber(holdingsContent.getCallNumber().getNumber());
                    }
                }
                oleSingleItemResultDisplayRow.setClaimsReturnedFlag(String.valueOf(itemPojo.isClaimsReturnedFlag()));
                if (itemPojo.isClaimsReturnedFlag()) {
                    oleSingleItemResultDisplayRow.setClaimsReturnedNote(itemPojo.getClaimsReturnedNote());
                    oleSingleItemResultDisplayRow.setClaimsReturnedDate(itemPojo.getClaimsReturnedFlagCreateDate());
                }
                oleSingleItemResultDisplayRow.setMissingPieceFlag(String.valueOf(itemPojo.isMissingPieceFlag()));
                if (itemPojo.isMissingPieceFlag()) {
                    oleSingleItemResultDisplayRow.setMissingPieceCount(itemPojo.getMissingPiecesCount());
                    oleSingleItemResultDisplayRow.setMissingPieceNote(itemPojo.getMissingPieceFlagNote());
                }
                oleSingleItemResultDisplayRow.setItemDamagedStatusFlag(String.valueOf(itemPojo.isItemDamagedStatus()));
                if (itemPojo.isItemDamagedStatus()) {
                    oleSingleItemResultDisplayRow.setItemDamagedNote(itemPojo.getDamagedItemNote());
                }
                if (itemPojo.getItemStatusEffectiveDate() != null) {
                    oleSingleItemResultDisplayRow.setItemStatusDate(itemPojo.getItemStatusEffectiveDate());
                }
                oleSingleItemResultDisplayRow.setCurrentBorrowerId(itemPojo.getCurrentBorrower());
                oleSingleItemResultDisplayRow.setProxyBorrowerId(itemPojo.getProxyBorrower());
                oleSingleItemResultDisplayRow.setDueDate(itemPojo.getDueDateTime());
                oleSingleItemResultDisplayRow.setOriginalDueDate(itemPojo.getOriginalDueDate());
                oleSingleItemResultDisplayRow.setEnumeration(itemPojo.getEnumeration());
                oleSingleItemResultDisplayRow.setChronology(itemPojo.getChronology());
                oleSingleItemResultDisplayRow.setNoOfPieces(itemPojo.getNumberOfPieces());
                if (CollectionUtils.isNotEmpty(itemPojo.getNote())) {
                    List<OLEItemNoteResultDisplayRow> itemNoteResultDisplayRowList = new ArrayList<>();
                    for (Note note : itemPojo.getNote()) {
                        OLEItemNoteResultDisplayRow oleItemNoteResultDisplayRow = new OLEItemNoteResultDisplayRow();
                        oleItemNoteResultDisplayRow.setNoteType(note.getType());
                        oleItemNoteResultDisplayRow.setNoteValue(note.getValue());
                        itemNoteResultDisplayRowList.add(oleItemNoteResultDisplayRow);
                    }
                    oleSingleItemResultDisplayRow.setOleItemNoteResultDisplayRowList(itemNoteResultDisplayRowList);
                }
            }
            oleSingleItemResultDisplayRow.setPlaceRequest(true);
            getOleDeliverItemSearchService().setBorrowerInfo(oleSingleItemResultDisplayRow);
            getOleDeliverItemSearchService().setAdditionalCopiesInfo(oleSingleItemResultDisplayRow);
            getOleDeliverItemSearchService().setDeliverRequestInfo(itemIdMap, oleSingleItemResultDisplayRow);
            getOleDeliverItemSearchService().setOutstandingFineInfo(itemIdMap, oleSingleItemResultDisplayRow);
            getOleDeliverItemSearchService().setRequestHistoryInfo(oleSingleItemResultDisplayRow);
            getOleDeliverItemSearchService().setInTransitHistoryInfo(oleSingleItemResultDisplayRow);
            getOleDeliverItemSearchService().setMissingPieceItemInfo(oleSingleItemResultDisplayRow);
            getOleDeliverItemSearchService().setClaimsReturnedInfo(oleSingleItemResultDisplayRow);
            getOleDeliverItemSearchService().setDamagedInfo(oleSingleItemResultDisplayRow);
        } catch (Exception e) {
            e.printStackTrace();
        }
        deliverItemResultForm.setOleSingleItemResultDisplayRow(oleSingleItemResultDisplayRow);
        return super.navigate(deliverItemResultForm, result, request, response);
    }
}
