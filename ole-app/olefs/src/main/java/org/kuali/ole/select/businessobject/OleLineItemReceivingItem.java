/*
 * Copyright 2011 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.businessobject;

import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.bo.WorkInstanceDocument;
import org.kuali.ole.docstore.model.bo.WorkItemDocument;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.LineItemReceivingItem;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderItem;
import org.kuali.ole.module.purap.document.LineItemReceivingDocument;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.exception.PurError;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.document.OleLineItemReceivingDocument;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.*;

/**
 * This class holds line item information pertaining to OLE Receiving.
 */

public class OleLineItemReceivingItem extends LineItemReceivingItem implements OleReceivingItem {
    private KualiDecimal itemOrderedParts;
    private KualiDecimal itemReceivedTotalParts;
    private KualiDecimal itemReturnedTotalParts;
    private KualiDecimal itemDamagedTotalParts;
    // not stored in db
    private KualiDecimal itemReceivedPriorParts;
    private KualiDecimal itemReceivedToBeParts;
    private Boolean availableToPublic = true;
    private Integer oleFormatId;
    protected KualiDecimal itemOriginalReceivedTotalParts;
    protected KualiDecimal itemOriginalReturnedTotalParts;
    protected KualiDecimal itemOriginalDamagedTotalParts;
    protected String localTitleId;
    // Changes for OLE-2061 Starts
    private List<OleReceivingLineExceptionNotes> exceptionNoteList;
    private Integer exceptionTypeId;
    private String exceptionNotes;
    // Changes for OLE-2061 Ends

    // Changes for OLE-2062 Starts
    private List<OleLineItemReceivingReceiptNotes> noteList;
    private List<OleLineItemReceivingReceiptNotes> receiptNoteList;
    private Integer noteTypeId;
    private String receiptNotes;
    private String notes;
    private Integer receiptNoteListSize;
    // Changes for OLE-2062 Ends

    private List<OleLineItemReceivingReceiptNotes> specialHandlingNoteList;
    private boolean notesAck = false;

    private String itemTitleId;
    private BibInfoBean bibInfoBean;
    private String bibUUID;
    private DocData docData;
    //private OleLineItemReceivingDoc oleLineItemReceivingItemDoc;
    private List<OleLineItemReceivingDoc> oleLineItemReceivingItemDocList;
    private BusinessObjectService businessObjectService;
    private OleOrderRecord oleOrderRecord;
    protected Integer receiptStatusId;
    private OleReceiptStatus oleReceiptStatus;
    private String donorCode;
    private List<OLELinkPurapDonor> oleDonors=new ArrayList<>();
    /**
     * For List of copies
     */
    private Integer itemCopiesId;
    private KualiInteger parts;
    private KualiDecimal itemCopies;
    private String partEnumeration;
    private String locationCopies;
    private KualiInteger startingCopyNumber;
    private List<OleCopies> copies = new ArrayList<OleCopies>();

    /**
     * For Quantity and Parts fields As Integer
     */
    private KualiInteger oleItemOrderedQuantity;
    private KualiInteger oleItemReceivedPriorQuantity;
    private KualiInteger oleItemReceivedToBeQuantity;
    private KualiInteger oleItemReceivedTotalQuantity;
    private KualiInteger oleItemReturnedTotalQuantity;
    private KualiInteger oleItemDamagedTotalQuantity;
    private KualiInteger oleItemOrderedParts;
    private KualiInteger oleItemReceivedPriorParts;
    private KualiInteger oleItemReceivedToBeParts;
    private KualiInteger oleItemReceivedTotalParts;
    private KualiInteger oleItemReturnedTotalParts;
    private KualiInteger oleItemDamagedTotalParts;

    private String enumeration;
    private String location;
    private String copyNumber;
    private String receiptStatus;
    private List<OleCopy> copyList = new ArrayList<>();
    private boolean poSelected;
    private String docFormat;


    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public boolean isPoSelected() {
        return poSelected;
    }

    public void setPoSelected(boolean poSelected) {
        this.poSelected = poSelected;
    }

    public String getLocalTitleId() {
        return localTitleId;
    }

    public void setLocalTitleId(String localTitleId) {
        this.localTitleId = localTitleId;
    }

    /**
     * Gets the value of the receiptStatusId property
     *
     * @return receiptStatusId
     */
    public Integer getReceiptStatusId() {
        return receiptStatusId;
    }

    /**
     * Sets the receiptStatusId value
     *
     * @param receiptStatusId
     */
    public void setReceiptStatusId(Integer receiptStatusId) {
        this.receiptStatusId = receiptStatusId;
    }

    /**
     * Gets the instance of OleReceiptStatus
     *
     * @return oleReceiptStatus
     */
    public OleReceiptStatus getOleReceiptStatus() {
        return oleReceiptStatus;
    }

    /**
     * Sets the instance value for OleReceiptStatus
     *
     * @param oleReceiptStatus
     */
    public void setOleReceiptStatus(OleReceiptStatus oleReceiptStatus) {
        this.oleReceiptStatus = oleReceiptStatus;
    }

    public OleOrderRecord getOleOrderRecord() {
        return oleOrderRecord;
    }

    public void setOleOrderRecord(OleOrderRecord oleOrderRecord) {
        this.oleOrderRecord = oleOrderRecord;
    }

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

/*    public OleLineItemReceivingDoc getOleLineItemReceivingItemDoc() {
        return oleLineItemReceivingItemDoc;
    }

    public void setOleLineItemReceivingItemDoc(OleLineItemReceivingDoc oleLineItemReceivingItemDoc) {
        this.oleLineItemReceivingItemDoc = oleLineItemReceivingItemDoc;
    }*/

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleLineItemReceivingItem.class);

    public List<OleLineItemReceivingDoc> getOleLineItemReceivingItemDocList() {
        return oleLineItemReceivingItemDocList;
    }

    public void setOleLineItemReceivingItemDocList(List<OleLineItemReceivingDoc> oleLineItemReceivingItemDocList) {
        this.oleLineItemReceivingItemDocList = oleLineItemReceivingItemDocList;
    }

    /**
     * Default constructor.
     */
    public OleLineItemReceivingItem() {
        this.setItemUnitOfMeasureCode(PurapConstants.LineItemReceivingDocumentStrings.CUSTOMER_INVOICE_DETAIL_UOM_DEFAULT);
        this.setOleItemReceivedTotalQuantity(KualiInteger.ZERO);
        this.setOleItemReturnedTotalQuantity(KualiInteger.ZERO);
        this.setOleItemDamagedTotalQuantity(KualiInteger.ZERO);
        this.setItemOriginalReceivedTotalQuantity(KualiDecimal.ZERO);
        this.setItemOriginalReturnedTotalQuantity(KualiDecimal.ZERO);
        this.setItemOriginalDamagedTotalQuantity(KualiDecimal.ZERO);
        this.setItemReceivedTotalParts(KualiDecimal.ZERO);
        this.setItemReturnedTotalParts(KualiDecimal.ZERO);
        this.setItemDamagedTotalParts(KualiDecimal.ZERO);
        this.setItemOriginalReceivedTotalParts(KualiDecimal.ZERO);
        this.setItemOriginalReturnedTotalParts(KualiDecimal.ZERO);
        this.setItemOriginalDamagedTotalParts(KualiDecimal.ZERO);
        exceptionNoteList = new ArrayList();
        receiptNoteList = new ArrayList();
        specialHandlingNoteList = new ArrayList();
        noteList = new ArrayList();
        oleLineItemReceivingItemDocList = new ArrayList();
    }

    /**
     * Constructs a OleLineItemReceivingItem with default values for parts and quantity related attributes.
     *
     * @param rld
     */
    public OleLineItemReceivingItem(LineItemReceivingDocument rld) {
        super(rld);
        this.setItemReceivedTotalParts(KualiDecimal.ZERO);
        this.setItemReturnedTotalParts(KualiDecimal.ZERO);
        this.setItemDamagedTotalParts(KualiDecimal.ZERO);
        this.setItemOriginalReceivedTotalParts(KualiDecimal.ZERO);
        this.setItemOriginalReturnedTotalParts(KualiDecimal.ZERO);
        this.setItemOriginalDamagedTotalParts(KualiDecimal.ZERO);

        exceptionNoteList = new ArrayList();
        receiptNoteList = new ArrayList();
        specialHandlingNoteList = new ArrayList();
        noteList = new ArrayList();
        oleLineItemReceivingItemDocList = new ArrayList();
    }

    /**
     * Constructs a OleLineItemReceivingItem with default values for parts and quantity related attributes.
     * Also sets values for parts/quantity that were received and to be received.
     *
     * @param poi
     * @param rld
     */
    public OleLineItemReceivingItem(PurchaseOrderItem poi, LineItemReceivingDocument rld) {
        super(poi, rld);
        OlePurchaseOrderItem olePoi = (OlePurchaseOrderItem) poi;
        this.setReceiptStatusId(olePoi.getReceiptStatusId());
        OleLineItemReceivingDocument oleLineItemRecvDoc = (OleLineItemReceivingDocument) rld;
        if (this.getItemTitleId() == null) {
            this.setItemTitleId(olePoi.getItemTitleId());
        }
        OleLineItemReceivingDoc oleLineItemReceivingDoc = new OleLineItemReceivingDoc();
        oleLineItemReceivingDoc.setReceivingLineItemIdentifier(this.getReceivingItemIdentifier());
        oleLineItemReceivingDoc.setItemTitleId(olePoi.getItemTitleId());
        oleLineItemReceivingItemDocList = oleLineItemReceivingDoc.getItemTitleId() != null ? Collections.singletonList(oleLineItemReceivingDoc) : new ArrayList();
        if (ObjectUtils.isNotNull(olePoi.getItemNoOfParts())) {
            this.setItemOrderedParts((olePoi.getItemNoOfParts().kualiDecimalValue()));
        } else {
            this.setItemOrderedParts(new KualiDecimal(0));
        }

        if (ObjectUtils.isNull(olePoi.getItemReceivedTotalParts())) {
            this.setItemReceivedPriorParts(KualiDecimal.ZERO);
        } else {
            this.setItemReceivedPriorParts(olePoi.getItemReceivedTotalParts());
        }

        this.setItemReceivedToBeParts(this.getItemOrderedParts().subtract(this.getItemReceivedPriorParts()));

        this.setItemReceivedTotalParts(KualiDecimal.ZERO);
        this.setItemReturnedTotalParts(KualiDecimal.ZERO);
        this.setItemDamagedTotalParts(KualiDecimal.ZERO);
        this.setItemOriginalReceivedTotalParts(KualiDecimal.ZERO);
        this.setItemOriginalReturnedTotalParts(KualiDecimal.ZERO);
        this.setItemOriginalDamagedTotalParts(KualiDecimal.ZERO);
        exceptionNoteList = new ArrayList();
        receiptNoteList = new ArrayList();
        specialHandlingNoteList = new ArrayList();
        noteList = new ArrayList();
        if (olePoi.getNotes() != null && olePoi.getNotes().size() > 0) {
            List receiptNoteList = new ArrayList();
            List specialHandlingNoteList = new ArrayList();
            List noteList = new ArrayList();
            for (OleNotes poNote : olePoi.getNotes()) {
                OleLineItemReceivingReceiptNotes receiptNote = new OleLineItemReceivingReceiptNotes(poNote);
                receiptNote.setReceivingLineItem(this);
                receiptNote.setNoteType(poNote.getNoteType());
                String note = receiptNote.getNoteType().getNoteType();
                if (note.equalsIgnoreCase(OLEConstants.SPECIAL_PROCESSING_INSTRUCTION_NOTE)) {
                    specialHandlingNoteList.add(receiptNote);
                } else {
                    receiptNoteList.add(receiptNote);
                }
                noteList.add(receiptNote);
            }
            this.setSpecialHandlingNoteList(specialHandlingNoteList);
            this.setReceiptNoteList(receiptNoteList);
            this.setReceiptNoteListSize(receiptNoteList.size());
            this.setNoteList(noteList);
        }
        this.setPurchaseOrderIdentifier(poi.getItemIdentifier());
        this.setOleFormatId(olePoi.getFormatTypeId());
        this.setOleDonors(olePoi.getOleDonors());

        if(olePoi.getItemTitleId()!=null){
            this.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(olePoi.getItemTitleId()));
        }
        /*List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
        if (null != this.itemTitleId) {
            List<String> itemTitleIdsList = new ArrayList<String>();

            itemTitleIdsList.add(this.itemTitleId);
            workBibDocuments = getWorkBibDocuments(itemTitleIdsList);
        }
        if (null != this.itemTitleId && workBibDocuments.size() > 0) {
            if (this.getItemOrderedQuantity().isGreaterThan(new KualiDecimal(1))
                    || this.getItemOrderedParts().isGreaterThan(new KualiDecimal(1))) {
                for (WorkBibDocument workBibDocument : workBibDocuments) {
                    List<OleCopies> copies = setCopiesToLineItem(workBibDocument);
                    this.setCopies(copies);
                }
            }
          //  this.setItemReceivedTotalQuantity(this.getItemReceivedToBeQuantity());
         //   this.setItemReceivedTotalParts(new KualiDecimal(this.getItemReceivedToBeParts().intValue()));
        }*/
        this.setCopies(olePoi.getCopies());
        this.setCopyList(olePoi.getCopyList());
        List<OleCopy> copyList = this.getCopyList() != null ? this.getCopyList() : new ArrayList<OleCopy>();
        OleCopyHelperService oleCopyHelperService = SpringContext.getBean(OleCopyHelperService.class);

        HashMap<String, List<OleCopy>> copyListBasedOnCopyNumber = oleCopyHelperService.getCopyListBasedOnCopyNumber(copyList,olePoi.getItemNoOfParts().intValue());
        Iterator<Map.Entry<String, List<OleCopy>>> entries = copyListBasedOnCopyNumber.entrySet().iterator();
        Integer receivedCopyCount = 0;
        while (entries.hasNext()) {
            Map.Entry<String, List<OleCopy>> entry = entries.next();
            List<OleCopy> copyMap = entry.getValue();
            Integer copyCount = 0;
            for (OleCopy copy : copyMap) {
                if (copy.getReceiptStatus()!=null && copy.getReceiptStatus().equalsIgnoreCase(OLEConstants.OleLineItemReceiving.RECEIVED_STATUS)) {
                    copyCount++;
                }
            }
            if (copyCount == this.getItemOrderedParts().intValue()) {
                receivedCopyCount++;
            }
        }
        if (receivedCopyCount > 0) {
            this.setItemReceivedTotalQuantity(new KualiDecimal(receivedCopyCount));
            this.setItemReceivedTotalParts(new KualiDecimal(this.getItemOrderedParts().intValue()));
        }
    }

    /**
     * Gets the parts ordered attribute value.
     *
     * @return Returns the itemOrderedParts.
     */
    public KualiDecimal getItemOrderedParts() {
        return itemOrderedParts;
    }

    /**
     * Sets the parts ordered attribute value.
     *
     * @param itemOrderedParts to set.
     */
    public void setItemOrderedParts(KualiDecimal itemOrderedParts) {
        this.itemOrderedParts = itemOrderedParts;
    }

    /**
     * Gets the parts received attribute value.
     *
     * @return Returns the itemReceivedTotalParts.
     */
    @Override
    public KualiDecimal getItemReceivedTotalParts() {
        return itemReceivedTotalParts;
    }

    /**
     * Sets the parts received attribute value.
     *
     * @param itemReceivedTotalParts to set.
     */
    @Override
    public void setItemReceivedTotalParts(KualiDecimal itemReceivedTotalParts) {
        this.itemReceivedTotalParts = itemReceivedTotalParts;
    }

    /**
     * Gets the parts returned attribute value.
     *
     * @return Returns the itemReturnedTotalParts.
     */
    @Override
    public KualiDecimal getItemReturnedTotalParts() {
        return itemReturnedTotalParts;
    }

    /**
     * Sets the parts returned attribute value.
     *
     * @param itemReturnedTotalParts to set.
     */
    @Override
    public void setItemReturnedTotalParts(KualiDecimal itemReturnedTotalParts) {
        this.itemReturnedTotalParts = itemReturnedTotalParts;
    }

    /**
     * Gets the parts damaged attribute value.
     *
     * @return Returns the itemDamagedTotalParts.
     */
    @Override
    public KualiDecimal getItemDamagedTotalParts() {
        return itemDamagedTotalParts;
    }

    /**
     * Sets the parts damaged attribute value.
     *
     * @param itemDamagedTotalParts to set.
     */
    @Override
    public void setItemDamagedTotalParts(KualiDecimal itemDamagedTotalParts) {
        this.itemDamagedTotalParts = itemDamagedTotalParts;
    }

    /**
     * Gets the prior parts received attribute value.
     *
     * @return Returns the itemReceivedPriorParts.
     */
    public KualiDecimal getItemReceivedPriorParts() {
        if (ObjectUtils.isNull(itemReceivedPriorParts)) {
            OlePurchaseOrderItem olePoi = (OlePurchaseOrderItem) getPurchaseOrderItem();
            KualiDecimal priorParts = olePoi.getItemReceivedTotalParts();
            if (priorParts == null) {
                priorParts = new KualiDecimal(0.00);
            }
            setItemReceivedPriorParts(priorParts);
        }
        return itemReceivedPriorParts;
    }

    /**
     * Sets the prior parts received attribute value.
     *
     * @param itemReceivedPriorParts to set.
     */
    public void setItemReceivedPriorParts(KualiDecimal itemReceivedPriorParts) {
        this.itemReceivedPriorParts = itemReceivedPriorParts;
    }

    /**
     * Gets the parts to be received received attribute value.
     *
     * @return Returns the itemReceivedToBeParts.
     */
    public KualiDecimal getItemReceivedToBeParts() {
        // lazy loaded
        if (null != this.getItemOrderedParts()) {
            KualiDecimal priorParts = getItemReceivedPriorParts();
            if (priorParts == null) {
                priorParts = new KualiDecimal(0.00);
            }
            KualiDecimal toBeParts = this.getItemOrderedParts().subtract(priorParts);
            if (toBeParts.isNegative()) {
                toBeParts = KualiDecimal.ZERO;
            }
            setItemReceivedToBeParts(toBeParts);
            // setItemReceivedTotalParts(toBeParts);
        }
        return itemReceivedToBeParts;
    }

    /**
     * Sets the prior parts to be received attribute value.
     *
     * @param itemReceivedToBeParts to set.
     */
    public void setItemReceivedToBeParts(KualiDecimal itemReceivedToBeParts) {
        this.itemReceivedToBeParts = itemReceivedToBeParts;
    }

    /**
     * Gets the availableToPublic attribute value.
     *
     * @return Returns the availableToPublic.
     */
    public Boolean getAvailableToPublic() {
        return availableToPublic;
    }

    /**
     * Sets the availableToPublic attribute value.
     *
     * @param availableToPublic to set.
     */
    public void setAvailableToPublic(Boolean availableToPublic) {
        this.availableToPublic = availableToPublic;
    }

    /**
     * Gets the oleFormatId attribute value.
     *
     * @return Returns the oleFormatId.
     */
    public Integer getOleFormatId() {
        return oleFormatId;
    }

    /**
     * Sets the oleFormatId attribute value.
     *
     * @param oleFormatId to set.
     */
    public void setOleFormatId(Integer oleFormatId) {
        this.oleFormatId = oleFormatId;
    }

    /**
     * Gets the itemOriginalReceivedTotalParts attribute value.
     *
     * @return Returns the itemOriginalReceivedTotalParts.
     */
    @Override
    public KualiDecimal getItemOriginalReceivedTotalParts() {
        return itemOriginalReceivedTotalParts;
    }

    /**
     * Sets the itemOriginalReceivedTotalParts attribute value.
     *
     * @param itemOriginalReceivedTotalParts to set.
     */
    @Override
    public void setItemOriginalReceivedTotalParts(KualiDecimal itemOriginalReceivedTotalParts) {
        this.itemOriginalReceivedTotalParts = itemOriginalReceivedTotalParts;
    }

    /**
     * Gets the itemOriginalReturnedTotalParts attribute value.
     *
     * @return Returns the itemOriginalReturnedTotalParts.
     */
    @Override
    public KualiDecimal getItemOriginalReturnedTotalParts() {
        return itemOriginalReturnedTotalParts;
    }

    /**
     * Sets the itemOriginalReturnedTotalParts attribute value.
     *
     * @param itemOriginalReturnedTotalParts to set.
     */
    @Override
    public void setItemOriginalReturnedTotalParts(KualiDecimal itemOriginalReturnedTotalParts) {
        this.itemOriginalReturnedTotalParts = itemOriginalReturnedTotalParts;
    }

    /**
     * Gets the itemOriginalDamagedTotalParts attribute value.
     *
     * @return Returns the itemOriginalDamagedTotalParts.
     */
    @Override
    public KualiDecimal getItemOriginalDamagedTotalParts() {
        return itemOriginalDamagedTotalParts;
    }

    /**
     * Sets the itemOriginalDamagedTotalParts attribute value.
     *
     * @param itemOriginalDamagedTotalParts to set.
     */
    @Override
    public void setItemOriginalDamagedTotalParts(KualiDecimal itemOriginalDamagedTotalParts) {
        this.itemOriginalDamagedTotalParts = itemOriginalDamagedTotalParts;
    }

    /**
     * Overridden method to check if atleast one parts and quantity related item is entered.
     *
     * @see org.kuali.ole.module.purap.businessobject.ReceivingItemBase#isConsideredEntered()
     */
    @Override
    public boolean isConsideredEntered() {
        LOG.debug("Inside OleLineItemReceivingItem isConsideredEntered");
        boolean isConsideredEntered = super.isConsideredEntered();
        if (LOG.isDebugEnabled()) {
            LOG.debug("isConsideredEntered from LineItemReceivingItem :" + isConsideredEntered);
        }
        isConsideredEntered &= !((ObjectUtils.isNotNull(this.getItemOrderedParts()) && this.getItemOrderedParts().isGreaterThan(KualiDecimal.ZERO)) && ((ObjectUtils.isNull(this.getItemReceivedTotalParts()) || this.getItemReceivedTotalParts().isZero()) &&
                (ObjectUtils.isNull(this.getItemDamagedTotalParts()) || this.getItemDamagedTotalParts().isZero()) &&
                (ObjectUtils.isNull(this.getItemReturnedTotalParts()) || this.getItemReturnedTotalParts().isZero())));

        if (LOG.isDebugEnabled()) {
            LOG.debug("isConsideredEntered from OleLineItemReceivingItem :" + isConsideredEntered);
        }
        return isConsideredEntered;
    }

    // Changes for OLE-2061 Starts

    /**
     * Gets list of OleReceivingLineExceptionNotes.
     *
     * @return exceptionNoteList.
     */
    public List<OleReceivingLineExceptionNotes> getExceptionNoteList() {
        return exceptionNoteList;
    }

    /**
     * Sets list of OleReceivingLineExceptionNotes.
     *
     * @param exceptionNoteList to set.
     */
    public void setExceptionNoteList(List<OleReceivingLineExceptionNotes> exceptionNoteList) {
        this.exceptionNoteList = exceptionNoteList;
    }

    /**
     * Gets exception type id.
     *
     * @return exceptionTypeId.
     */
    public Integer getExceptionTypeId() {
        return exceptionTypeId;
    }

    /**
     * Sets exception type id.
     *
     * @param exceptionTypeId to set.
     */
    public void setExceptionTypeId(Integer exceptionTypeId) {
        this.exceptionTypeId = exceptionTypeId;
    }

    /**
     * Gets exception notes.
     *
     * @return exceptionNotes.
     */
    public String getExceptionNotes() {
        return exceptionNotes;
    }

    /**
     * Sets exception notes.
     *
     * @param exceptionNotes to set.
     */
    public void setExceptionNotes(String exceptionNotes) {
        this.exceptionNotes = exceptionNotes;
    }

    /**
     * This method returns OleReceivingLineExceptionNotes class
     *
     * @return Class
     */
    public Class getExceptionNotesClass() {
        return OleReceivingLineExceptionNotes.class;
    }

    /**
     * This method adds OleReceivingLineExceptionNotes to the exceptioNoteList.
     *
     * @param exceptionNotes
     */
    public void addExceptionNote(OleReceivingLineExceptionNotes exceptionNotes) {
        exceptionNoteList.add(exceptionNotes);

    }

    /**
     * This method deletes exception note from exceptionNoteList at specified index.
     *
     * @param lineNum
     */
    public void deleteExceptionNote(int lineNum) {
        exceptionNoteList.remove(lineNum);

    }
    // Changes for OLE-2061 Ends

    // Changes for OLE-2062 Starts

    /**
     * Gets list of OleLineItemReceivingReceiptNotes.
     *
     * @return noteList.
     */
    public List<OleLineItemReceivingReceiptNotes> getNoteList() {
        return noteList;
    }

    /**
     * Sets list of OleLineItemReceivingReceiptNotes.
     *
     * @param noteList to set.
     */
    public void setNoteList(List<OleLineItemReceivingReceiptNotes> noteList) {
        this.noteList = noteList;
    }

    /**
     * Gets list of OleLineItemReceivingReceiptNotes.
     *
     * @return receiptNoteList.
     */
    public List<OleLineItemReceivingReceiptNotes> getReceiptNoteList() {
        return receiptNoteList;
    }

    /**
     * Sets list of OleLineItemReceivingReceiptNotes.
     *
     * @param receiptNoteList to set.
     */
    public void setReceiptNoteList(List<OleLineItemReceivingReceiptNotes> receiptNoteList) {
        this.receiptNoteList = receiptNoteList;
    }

    /**
     * Gets note type id.
     *
     * @return noteTypeId.
     */
    public Integer getNoteTypeId() {
        return noteTypeId;
    }

    /**
     * Sets note type id.
     *
     * @param noteTypeId to set.
     */
    public void setNoteTypeId(Integer noteTypeId) {
        this.noteTypeId = noteTypeId;
    }

    /**
     * Gets receipt notes.
     *
     * @return receiptNotes.
     */
    public String getReceiptNotes() {
        return receiptNotes;
    }

    /**
     * Sets receipt notes.
     *
     * @param receiptNotes to set.
     */
    public void setReceiptNotes(String receiptNotes) {
        this.receiptNotes = receiptNotes;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * This method returns OleLineItemReceivingReceiptNotes class
     *
     * @return Class
     */
    public Class getOleLineItemReceivingDocClass() {
        return OleLineItemReceivingDoc.class;
    }

    /**
     * This method returns OleLineItemReceivingReceiptNotes class
     *
     * @return Class
     */
    public Class getReceiptNotesClass() {
        return OleLineItemReceivingReceiptNotes.class;
    }

    /**
     * This method adds OleLineItemReceivingReceiptNotes to the receiptNoteList.
     *
     * @param receiptNotes
     */
    public void addReceiptNote(OleLineItemReceivingReceiptNotes receiptNotes) {
        receiptNoteList.add(receiptNotes);

    }

    /**
     * This method deletes receipt note from receiptNoteList at specified index.
     *
     * @param lineNum
     */
    public void deleteReceiptNote(int lineNum) {
        receiptNoteList.remove(lineNum);

    }

    /**
     * This method adds OleLineItemReceivingReceiptNotes to the noteList.
     *
     * @param receiptNotes
     */
    public void addNote(OleLineItemReceivingReceiptNotes receiptNotes) {
        noteList.add(receiptNotes);
    }

    /**
     * This method deletes receipt note from noteList at specified index.
     *
     * @param lineNum
     */
    public void deleteNote(int lineNum) {
        noteList.remove(lineNum);

    }
    // Changes for OLE-2062 Ends

    /**
     * Gets list of Special Handling Notes.
     *
     * @return specialHandlingNoteList.
     */
    public List<OleLineItemReceivingReceiptNotes> getSpecialHandlingNoteList() {
        return specialHandlingNoteList;
    }

    /**
     * Sets list of Special Handling Notes.
     *
     * @param specialHandlingNoteList.
     */
    public void setSpecialHandlingNoteList(List<OleLineItemReceivingReceiptNotes> specialHandlingNoteList) {
        this.specialHandlingNoteList = specialHandlingNoteList;
    }

    /**
     * Gets acknowledgement flag for special handling notes
     *
     * @return notesAck
     */
    public boolean isNotesAck() {
        return notesAck;
    }

    /**
     * Sets acknowledgement flag for special handling notes
     *
     * @param notesAck
     */
    public void setNotesAck(boolean notesAck) {
        this.notesAck = notesAck;
    }

    /**
     * Gets size of receipt notes list retrived from database
     *
     * @return receiptNoteListSize
     */
    public Integer getReceiptNoteListSize() {
        return receiptNoteListSize;
    }

    /**
     * Sets size of receipt notes list retrived from database
     *
     * @param receiptNoteListSize
     */
    public void setReceiptNoteListSize(Integer receiptNoteListSize) {
        this.receiptNoteListSize = receiptNoteListSize;
    }

    public String getItemTitleId() {
        return itemTitleId;
    }

    public void setItemTitleId(String itemTitleId) {
        this.itemTitleId = itemTitleId;
    }

    /**
     * Retreives a purchase order item by inspecting the item type to see if its above the line or below the line and returns the
     * appropriate type.
     *
     * @return purchase order item
     */
    @Override
    public PurchaseOrderItem getPurchaseOrderItem() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Inside OleLineItemReceivingItem getPurchaseOrderItem");
            LOG.debug("Ole Line Item Receiving Document before refresh :" + getLineItemReceivingDocument());
        }
        //if (ObjectUtils.isNotNull(this.getLineItemReceivingDocument())) {
        if (ObjectUtils.isNull(this.getLineItemReceivingDocument())) {
            this.refreshReferenceObject("lineItemReceivingDocument");
        }
        //}
        // ideally we should do this a different way - maybe move it all into the service or save this info somehow (make sure and
        // update though)
        if (getLineItemReceivingDocument() != null) {
            PurchaseOrderDocument po = getLineItemReceivingDocument().getPurchaseOrderDocument();
            PurchaseOrderItem poi = null;
            if (this.getItemType().isLineItemIndicator()) {
                poi = (PurchaseOrderItem) po.getItem(this.getItemLineNumber().intValue() - 1);
                // throw error if line numbers don't match
            }
            if (poi != null) {
                return poi;
            } else {
                // LOG.debug("getPurchaseOrderItem() Returning null because PurchaseOrderItem object for line number" +
                // getItemLineNumber() + "or itemType " + getItemTypeCode() + " is null");
                return null;
            }
        } else {
            LOG.error("getLineItemReceivingDocument() Returning null in getPurchaseOrderItem()");
            throw new PurError("Receiving Line Object in Purchase Order item line number " + getItemLineNumber() + "or itemType " + getItemTypeCode() + " is null");
        }
    }

    public BibInfoBean getBibInfoBean() {
        return bibInfoBean;
    }

    public void setBibInfoBean(BibInfoBean bibInfoBean) {
        this.bibInfoBean = bibInfoBean;
    }

    public String getBibUUID() {
        return bibUUID;
    }

    public void setBibUUID(String bibUUID) {
        this.bibUUID = bibUUID;
    }

    public DocData getDocData() {
        return docData;
    }

    public void setDocData(DocData docData) {
        this.docData = docData;
    }

    /**
     * Gets the itemCopiesId attribute.
     *
     * @return Returns the itemCopiesId.
     */
    public Integer getItemCopiesId() {
        return itemCopiesId;
    }

    /**
     * Sets the itemCopiesId attribute value.
     *
     * @param itemCopiesId The itemCopiesId to set.
     */
    public void setItemCopiesId(Integer itemCopiesId) {
        this.itemCopiesId = itemCopiesId;
    }

    /**
     * Gets the parts attribute.
     *
     * @return Returns the parts.
     */
    public KualiInteger getParts() {
        return parts;
    }

    /**
     * Sets the parts attribute value.
     *
     * @param parts The parts to set.
     */
    public void setParts(KualiInteger parts) {
        this.parts = parts;
    }

    /**
     * Gets the itemCopies attribute.
     *
     * @return Returns the itemCopies.
     */
    public KualiDecimal getItemCopies() {
        return itemCopies;
    }

    /**
     * Sets the itemCopies attribute value.
     *
     * @param itemCopies The itemCopies to set.
     */
    public void setItemCopies(KualiDecimal itemCopies) {
        this.itemCopies = itemCopies;
    }

    /**
     * Gets the partEnumeration attribute.
     *
     * @return Returns the partEnumeration.
     */
    public String getPartEnumeration() {
        return partEnumeration;
    }

    /**
     * Sets the partEnumeration attribute value.
     *
     * @param partEnumeration The partEnumeration to set.
     */
    public void setPartEnumeration(String partEnumeration) {
        this.partEnumeration = partEnumeration;
    }

    /**
     * Gets the locationCopies attribute.
     *
     * @return Returns the locationCopies.
     */
    public String getLocationCopies() {
        return locationCopies;
    }

    /**
     * Sets the locationCopies attribute value.
     *
     * @param locationCopies The locationCopies to set.
     */
    public void setLocationCopies(String locationCopies) {
        this.locationCopies = locationCopies;
    }

    /**
     * Gets the startingCopyNumber attribute.
     *
     * @return Returns the startingCopyNumber.
     */
    public KualiInteger getStartingCopyNumber() {
        return startingCopyNumber;
    }

    /**
     * Sets the startingCopyNumber attribute value.
     *
     * @param startingCopyNumber The startingCopyNumber to set.
     */
    public void setStartingCopyNumber(KualiInteger startingCopyNumber) {
        this.startingCopyNumber = startingCopyNumber;
    }

    /**
     * Gets the copies attribute.
     *
     * @return Returns the copies.
     */
    public List<OleCopies> getCopies() {
        return copies;
    }

    /**
     * Sets the copies attribute value.
     *
     * @param copies The copies to set.
     */
    public void setCopies(List<OleCopies> copies) {
        this.copies = copies;
    }

    // Added for Jira OLE-1900 Starts

    /**
     * This method will set copies into list of copies for LineItem.
     *
     * @param singleItem
     * @param workBibDocument
     * @return
     */
    public List<OleCopies> setCopiesToLineItem(WorkBibDocument workBibDocument) {
        List<WorkInstanceDocument> instanceDocuments = workBibDocument.getWorkInstanceDocumentList();
        List<OleCopies> copies = new ArrayList<OleCopies>();
        for (WorkInstanceDocument workInstanceDocument : instanceDocuments) {
            List<WorkItemDocument> itemDocuments = workInstanceDocument.getItemDocumentList();
            StringBuffer enumeration = new StringBuffer();
            for (int itemDocs = 0; itemDocs < itemDocuments.size(); itemDocs++) {
                if (itemDocs + 1 == itemDocuments.size()) {
                    enumeration = enumeration.append(itemDocuments.get(itemDocs).getEnumeration());
                } else {
                    enumeration = enumeration.append(itemDocuments.get(itemDocs).getEnumeration() + ",");
                }

            }
            int startingCopy = 0;
            if (this.getItemReturnedTotalParts().intValue() != 0 && null != enumeration) {
                String enumerationSplit = enumeration.substring(1, 2);
                boolean isint = checkIsEnumerationSplitIsIntegerOrNot(enumerationSplit);
                if (isint) {
                    startingCopy = Integer.parseInt(enumerationSplit);
                }
            }
            int noOfCopies = workInstanceDocument.getItemDocumentList().size() / this.getItemOrderedParts().intValue();
            OleRequisitionCopies copy = new OleRequisitionCopies();
            copy.setParts(new KualiInteger(this.getItemOrderedParts().intValue()));
            copy.setLocationCopies(workInstanceDocument.getHoldingsDocument().getLocationName());
            copy.setItemCopies(new KualiDecimal(noOfCopies));
            copy.setPartEnumeration(enumeration.toString());
            copy.setStartingCopyNumber(new KualiInteger(startingCopy));
            copies.add(copy);
        }
        return copies;
    }

    public boolean checkIsEnumerationSplitIsIntegerOrNot(String enumerationSplit) {
        try {
            int startingCopy = Integer.parseInt(enumerationSplit);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * This method takes List of UUids as parameter and creates a LinkedHashMap with instance as key and id as value. and calls
     * Docstore's QueryServiceImpl class getWorkBibRecords method and return workBibDocument for passed instance Id.
     *
     * @param instanceIdsList
     * @return List<WorkBibDocument>
     */
//    private List<WorkBibDocument> getWorkBibDocuments(List<String> instanceIdsList) {
//        List<LinkedHashMap<String, String>> instanceIdMapList = new ArrayList<LinkedHashMap<String, String>>();
//        for (String instanceId : instanceIdsList) {
//            LinkedHashMap<String, String> instanceIdMap = new LinkedHashMap<String, String>();
//            instanceIdMap.put(DocType.INSTANCE.getDescription(), instanceId);
//            instanceIdMapList.add(instanceIdMap);
//        }
//
//        QueryService queryService = QueryServiceImpl.getInstance();
//        List<WorkBibDocument> workBibDocuments = new ArrayList<WorkBibDocument>();
//        try {
//            workBibDocuments = queryService.getWorkBibRecords(instanceIdMapList);
//        } catch (Exception ex) {
//            // TODO Auto-generated catch block
//            ex.printStackTrace();
//        }
//        return workBibDocuments;
//    }

    /**
     * Gets the oleItemOrderedQuantity attribute.
     *
     * @return Returns the oleItemOrderedQuantity.
     */
    public KualiInteger getOleItemOrderedQuantity() {
        return new KualiInteger(super.getItemOrderedQuantity().intValue());
    }

    /**
     * Sets the oleItemOrderedQuantity attribute value.
     *
     * @param oleItemOrderedQuantity The oleItemOrderedQuantity to set.
     */
    public void setOleItemOrderedQuantity(KualiInteger oleItemOrderedQuantity) {
        super.setItemOrderedQuantity(new KualiDecimal(oleItemOrderedQuantity.intValue()));
    }

    /**
     * Gets the oleItemReceivedPriorQuantity attribute.
     *
     * @return Returns the oleItemReceivedPriorQuantity.
     */
    public KualiInteger getOleItemReceivedPriorQuantity() {
        return new KualiInteger(super.getItemReceivedPriorQuantity().intValue());
    }

    /**
     * Sets the oleItemReceivedPriorQuantity attribute value.
     *
     * @param oleItemReceivedPriorQuantity The oleItemReceivedPriorQuantity to set.
     */
    public void setOleItemReceivedPriorQuantity(KualiInteger oleItemReceivedPriorQuantity) {
        super.setItemReceivedPriorQuantity(new KualiDecimal(oleItemReceivedPriorQuantity.intValue()));
    }

    /**
     * Gets the oleItemReceivedToBeQuantity attribute.
     *
     * @return Returns the oleItemReceivedToBeQuantity.
     */
    public KualiInteger getOleItemReceivedToBeQuantity() {
        return new KualiInteger(super.getItemReceivedToBeQuantity().intValue());
    }

    /**
     * Sets the oleItemReceivedToBeQuantity attribute value.
     *
     * @param oleItemReceivedToBeQuantity The oleItemReceivedToBeQuantity to set.
     */
    public void setOleItemReceivedToBeQuantity(KualiInteger oleItemReceivedToBeQuantity) {
        super.setItemReceivedToBeQuantity(new KualiDecimal(oleItemReceivedToBeQuantity.intValue()));
    }

    /**
     * Gets the oleItemReceivedTotalQuantity attribute.
     *
     * @return Returns the oleItemReceivedTotalQuantity.
     */
    public KualiInteger getOleItemReceivedTotalQuantity() {
        return new KualiInteger(super.getItemReceivedTotalQuantity().intValue());
    }

    /**
     * Sets the oleItemReceivedTotalQuantity attribute value.
     *
     * @param oleItemReceivedTotalQuantity The oleItemReceivedTotalQuantity to set.
     */
    public void setOleItemReceivedTotalQuantity(KualiInteger oleItemReceivedTotalQuantity) {
        super.setItemReceivedTotalQuantity(new KualiDecimal(oleItemReceivedTotalQuantity.intValue()));
    }

    /**
     * Gets the oleItemReturnedTotalQuantity attribute.
     *
     * @return Returns the oleItemReturnedTotalQuantity.
     */
    public KualiInteger getOleItemReturnedTotalQuantity() {
        return new KualiInteger(super.getItemReturnedTotalQuantity().intValue());
    }

    /**
     * Sets the oleItemReturnedTotalQuantity attribute value.
     *
     * @param oleItemReturnedTotalQuantity The oleItemReturnedTotalQuantity to set.
     */
    public void setOleItemReturnedTotalQuantity(KualiInteger oleItemReturnedTotalQuantity) {
        super.setItemReturnedTotalQuantity(new KualiDecimal(oleItemReturnedTotalQuantity.intValue()));
    }

    /**
     * Gets the oleItemDamagedTotalQuantity attribute.
     *
     * @return Returns the oleItemDamagedTotalQuantity.
     */
    public KualiInteger getOleItemDamagedTotalQuantity() {
        return new KualiInteger(super.getItemDamagedTotalQuantity().intValue());
    }

    /**
     * Sets the oleItemDamagedTotalQuantity attribute value.
     *
     * @param oleItemDamagedTotalQuantity The oleItemDamagedTotalQuantity to set.
     */
    public void setOleItemDamagedTotalQuantity(KualiInteger oleItemDamagedTotalQuantity) {
        super.setItemDamagedTotalQuantity(new KualiDecimal(oleItemDamagedTotalQuantity.intValue()));
    }

    /**
     * Gets the oleItemOrderedParts attribute.
     *
     * @return Returns the oleItemOrderedParts.
     */
    public KualiInteger getOleItemOrderedParts() {
        return new KualiInteger(getItemOrderedParts().intValue());
    }

    /**
     * Sets the oleItemOrderedParts attribute value.
     *
     * @param oleItemOrderedParts The oleItemOrderedParts to set.
     */
    public void setOleItemOrderedParts(KualiInteger oleItemOrderedParts) {
        setItemOrderedParts(new KualiDecimal(oleItemOrderedParts.intValue()));
    }

    /**
     * Gets the oleItemReceivedPriorParts attribute.
     *
     * @return Returns the oleItemReceivedPriorParts.
     */
    public KualiInteger getOleItemReceivedPriorParts() {
        return new KualiInteger(getItemReceivedPriorParts().intValue());
    }

    /**
     * Sets the oleItemReceivedPriorParts attribute value.
     *
     * @param oleItemReceivedPriorParts The oleItemReceivedPriorParts to set.
     */
    public void setOleItemReceivedPriorParts(KualiInteger oleItemReceivedPriorParts) {
        setItemReceivedPriorParts(new KualiDecimal(oleItemReceivedPriorParts.intValue()));
    }

    /**
     * Gets the oleItemReceivedToBeParts attribute.
     *
     * @return Returns the oleItemReceivedToBeParts.
     */
    public KualiInteger getOleItemReceivedToBeParts() {
        return new KualiInteger(getItemReceivedToBeParts().intValue());
    }

    /**
     * Sets the oleItemReceivedToBeParts attribute value.
     *
     * @param oleItemReceivedToBeParts The oleItemReceivedToBeParts to set.
     */
    public void setOleItemReceivedToBeParts(KualiInteger oleItemReceivedToBeParts) {
        setItemReceivedToBeParts(new KualiDecimal(oleItemReceivedToBeParts.intValue()));
    }

    /**
     * Gets the oleItemReceivedTotalParts attribute.
     *
     * @return Returns the oleItemReceivedTotalParts.
     */
    public KualiInteger getOleItemReceivedTotalParts() {
        return new KualiInteger(getItemReceivedTotalParts().intValue());
    }

    /**
     * Sets the oleItemReceivedTotalParts attribute value.
     *
     * @param oleItemReceivedTotalParts The oleItemReceivedTotalParts to set.
     */
    public void setOleItemReceivedTotalParts(KualiInteger oleItemReceivedTotalParts) {
        setItemReceivedTotalParts(new KualiDecimal(oleItemReceivedTotalParts.intValue()));
    }

    /**
     * Gets the oleItemReturnedTotalParts attribute.
     *
     * @return Returns the oleItemReturnedTotalParts.
     */
    public KualiInteger getOleItemReturnedTotalParts() {
        return new KualiInteger(getItemReturnedTotalParts().intValue());
    }

    /**
     * Sets the oleItemReturnedTotalParts attribute value.
     *
     * @param oleItemReturnedTotalParts The oleItemReturnedTotalParts to set.
     */
    public void setOleItemReturnedTotalParts(KualiInteger oleItemReturnedTotalParts) {
        setItemReturnedTotalParts(new KualiDecimal(oleItemReturnedTotalParts.intValue()));
    }

    /**
     * Gets the oleItemDamagedTotalParts attribute.
     *
     * @return Returns the oleItemDamagedTotalParts.
     */
    public KualiInteger getOleItemDamagedTotalParts() {
        return new KualiInteger(getItemDamagedTotalParts().intValue());
    }

    /**
     * Sets the oleItemDamagedTotalParts attribute value.
     *
     * @param oleItemDamagedTotalParts The oleItemDamagedTotalParts to set.
     */
    public void setOleItemDamagedTotalParts(KualiInteger oleItemDamagedTotalParts) {
        setItemDamagedTotalParts(new KualiDecimal(oleItemDamagedTotalParts.intValue()));
    }


    // Added for Jira OLE-1900 Ends


    public String getDonorCode() {
        return donorCode;
    }

    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }

    public List<OLELinkPurapDonor> getOleDonors() {
        return oleDonors;
    }

    public void setOleDonors(List<OLELinkPurapDonor> oleDonors) {
        this.oleDonors = oleDonors;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public List<OleCopy> getCopyList() {
        return copyList;
    }

    public void setCopyList(List<OleCopy> copyList) {
        this.copyList = copyList;
    }
}

