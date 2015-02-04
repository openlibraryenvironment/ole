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

import org.kuali.ole.describe.bo.SearchResultDisplayRow;
import org.kuali.ole.docstore.common.client.DocstoreClient;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.BibTree;
import org.kuali.ole.docstore.common.document.HoldingsTree;
import org.kuali.ole.docstore.common.document.Item;
import  org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.content.instance.Items;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.DocumentUniqueIDPrefix;
import org.kuali.ole.docstore.model.bo.WorkBibDocument;
import org.kuali.ole.docstore.model.bo.WorkInstanceDocument;
import org.kuali.ole.docstore.model.bo.WorkItemDocument;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.module.purap.businessobject.CorrectionReceivingItem;
import org.kuali.ole.module.purap.businessobject.LineItemReceivingItem;
import org.kuali.ole.module.purap.document.CorrectionReceivingDocument;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.document.service.OleLineItemReceivingService;
import org.kuali.ole.select.document.service.impl.OleLineItemReceivingServiceImpl;
import org.kuali.ole.select.lookup.DocData;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class OleCorrectionReceivingItem extends CorrectionReceivingItem implements OleReceivingItem {

    private Integer itemFormatId;
    private KualiDecimal itemOriginalReceivedTotalParts;
    private KualiDecimal itemOriginalReturnedTotalParts;
    private KualiDecimal itemOriginalDamagedTotalParts;
    private KualiDecimal itemReceivedTotalParts;
    private KualiDecimal itemReturnedTotalParts;
    private KualiDecimal itemDamagedTotalParts;

    private List<OleCorrectionReceivingItemExceptionNotes> correctionExceptionNoteList;
    private Integer exceptionTypeId;
    private String exceptionNotes;
    private List<OleCorrectionReceivingItemReceiptNotes> correctionReceiptNoteList;
    private List<OleCorrectionReceivingItemReceiptNotes> correctionNoteList;
    private List<OleCorrectionReceivingItemReceiptNotes> correctionSpecialHandlingNoteList;
    private Integer noteTypeId;
    private String receiptNotes;
    private boolean notesAck = false;
    private Integer correctionReceiptNoteListSize;
    private String itemTitleId;
    private BibInfoBean bibInfoBean;
    private String bibUUID;
    private DocData docData;
    private OleLineItemCorrectionReceivingDoc oleLineItemReceivingCorrection;
    private OleOrderRecord oleOrderRecord;
    private List<OleLineItemCorrectionReceivingDoc> oleCorrectionReceivingItemDocList;
    private String notes;
    /**
     * For List of copies
     */
    private String donorCode;
    private List<OLELinkPurapDonor> oleDonors=new ArrayList<>();
    protected String localTitleId;
    private Integer itemCopiesId;
    private KualiInteger parts;
    private KualiDecimal itemCopies;
    private String partEnumeration;
    private String locationCopies;
    private KualiInteger startingCopyNumber;
    private List<OleCopies> copies = new ArrayList<OleCopies>();
    private String itemLocation;

    /**
     * For Quantity and Parts fields As Integer
     */
    private KualiInteger oleItemOriginalReceivedTotalQuantity;
    private KualiInteger oleItemOriginalReturnedTotalQuantity;
    private KualiInteger oleItemOriginalDamagedTotalQuantity;
    private KualiInteger oleItemReceivedTotalQuantity;
    private KualiInteger oleItemReturnedTotalQuantity;
    private KualiInteger oleItemDamagedTotalQuantity;
    private KualiInteger oleItemOriginalReceivedTotalParts;
    private KualiInteger oleItemOriginalReturnedTotalParts;
    private KualiInteger oleItemOriginalDamagedTotalParts;
    private KualiInteger oleItemReceivedTotalParts;
    private KualiInteger oleItemReturnedTotalParts;
    private KualiInteger oleItemDamagedTotalParts;
    private String docFormat;
    private String enumeration;
    private String location;
    private String copyNumber;
    private String receiptStatus;
    private List<OleCopy> copyList = new ArrayList<>();

    private DocstoreClientLocator docstoreClientLocator;
    public String getDocFormat() {
        return docFormat;
    }

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }
    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public Class getOleLineItemCorrectionDocClass() {
        return OleLineItemCorrectionReceivingDoc.class;
    }

    /**
     * Default constructor.
     */
    public OleCorrectionReceivingItem() {
        correctionExceptionNoteList = new ArrayList();
        correctionReceiptNoteList = new ArrayList();
        correctionSpecialHandlingNoteList = new ArrayList();
        correctionNoteList = new ArrayList();
        oleCorrectionReceivingItemDocList = new ArrayList();
    }

    public OleCorrectionReceivingItem(CorrectionReceivingDocument rld) {
        oleCorrectionReceivingItemDocList = new ArrayList();
    }

    public String getLocalTitleId() {
        return localTitleId;
    }

    public void setLocalTitleId(String localTitleId) {
        this.localTitleId = localTitleId;
    }

    /**
     * Constructs a OleCorrectionReceivingItem with default values for parts attributes.
     *
     * @param rli
     * @param rcd
     */
    public OleCorrectionReceivingItem(LineItemReceivingItem rli, CorrectionReceivingDocument rcd) {
        super(rli, rcd);

        OleLineItemReceivingItem oleRli = (OleLineItemReceivingItem) rli;
        this.setItemFormatId(oleRli.getOleFormatId());
        this.setItemOriginalReceivedTotalParts(oleRli.getItemReceivedTotalParts());
        this.setItemOriginalReturnedTotalParts(oleRli.getItemReturnedTotalParts());
        this.setItemOriginalDamagedTotalParts(oleRli.getItemDamagedTotalParts());
        this.setOleDonors(oleRli.getOleDonors());
        this.setItemReceivedTotalParts(oleRli.getItemReceivedTotalParts());
        this.setItemReturnedTotalParts(oleRli.getItemReturnedTotalParts());
        this.setItemDamagedTotalParts(oleRli.getItemDamagedTotalParts());
        this.setItemTitleId(oleRli.getItemTitleId());
        if (oleRli.getItemTitleId() != null) {
            this.setDocFormat(DocumentUniqueIDPrefix.getBibFormatType(oleRli.getItemTitleId()));
        }
        String titleId = oleRli.getItemTitleId();
        OleLineItemReceivingService oleLineItemReceivingService = SpringContext.getBean(OleLineItemReceivingServiceImpl.class);
        if (titleId == null) {
            OlePurchaseOrderItem olePurchaseOrderItem = oleLineItemReceivingService.getOlePurchaseOrderItem(oleRli.getPurchaseOrderIdentifier());
            this.setItemTitleId(olePurchaseOrderItem.getItemTitleId());
        }
        OleLineItemCorrectionReceivingDoc oleLineItemCorrectionReceivingDoc = new OleLineItemCorrectionReceivingDoc();
        oleLineItemCorrectionReceivingDoc.setReceivingLineItemIdentifier(this.getReceivingItemIdentifier());
        oleLineItemCorrectionReceivingDoc.setItemTitleId(oleRli.getItemTitleId());
        oleCorrectionReceivingItemDocList = oleLineItemCorrectionReceivingDoc.getItemTitleId() != null ? Collections.singletonList(oleLineItemCorrectionReceivingDoc) : new ArrayList();


        correctionExceptionNoteList = new ArrayList();
        correctionReceiptNoteList = new ArrayList();
        correctionSpecialHandlingNoteList = new ArrayList();
        correctionNoteList = new ArrayList();

        if (oleRli.getNoteList() != null && oleRli.getNoteList().size() > 0) {
            List receiptNoteList = new ArrayList();
            List specialHandlingNoteList = new ArrayList();
            List noteList = new ArrayList();
            for (OleLineItemReceivingReceiptNotes receiveNotes : oleRli.getNoteList()) {
                OleCorrectionReceivingItemReceiptNotes receiptNote = new OleCorrectionReceivingItemReceiptNotes(receiveNotes);
                receiptNote.setCorrectionReceivingItem(this);
                receiptNote.setNoteType(receiveNotes.getNoteType());
                String note = receiptNote.getNoteType().getNoteType();
                if (note.equalsIgnoreCase(OLEConstants.SPECIAL_PROCESSING_INSTRUCTION_NOTE)) {
                    specialHandlingNoteList.add(receiptNote);
                } else {
                    receiptNoteList.add(receiptNote);
                }
                noteList.add(receiptNote);
            }
            this.setCorrectionSpecialHandlingNoteList(specialHandlingNoteList);
            this.setCorrectionReceiptNoteList(receiptNoteList);
            this.setCorrectionReceiptNoteListSize(receiptNoteList.size());
            this.setCorrectionNoteList(noteList);
        }

        if (oleRli.getExceptionNoteList() != null && oleRli.getExceptionNoteList().size() > 0) {
            List exceptionNoteList = new ArrayList();
            for (OleReceivingLineExceptionNotes receiveExceptionNotes : oleRli.getExceptionNoteList()) {
                OleCorrectionReceivingItemExceptionNotes exceptionNotes = new OleCorrectionReceivingItemExceptionNotes(receiveExceptionNotes);
                exceptionNotes.setReceivingLineItem(oleRli);
                exceptionNotes.setExceptionType(receiveExceptionNotes.getExceptionType());
                exceptionNoteList.add(exceptionNotes);
            }
            this.setCorrectionExceptionNoteList(exceptionNoteList);
        }
        this.setPurchaseOrderIdentifier(rli.getPurchaseOrderIdentifier());
        BibTree bibTree=new BibTree();
        if (null != this.itemTitleId) {
            //List<String> itemTitleIdsList = new ArrayList<String>();

            //itemTitleIdsList.add(this.itemTitleId);
            try{
            bibTree = getDocstoreClientLocator().getDocstoreClient().retrieveBibTree(this.itemTitleId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (null != this.itemTitleId ) {
            if (this.getItemReceivedTotalQuantity().isGreaterThan(new KualiDecimal(1))
                    || this.getItemReceivedTotalParts().isGreaterThan(new KualiDecimal(1))) {
                    List<OleCopies> copies = setCopiesToLineItem(bibTree);
                    this.setCopies(copies);
                    this.setItemReceivedTotalQuantity(oleRli.getItemReceivedTotalQuantity());
                    this.setItemReceivedTotalParts(oleRli.getItemReceivedTotalParts());

            }
        }
        this.setCopyList(oleRli.getCopyList());
        // this.setCopies(oleRli.getCopies());
    }

    /**
     * Overridden method to check if atleast one parts or quantity related item is entered.
     *
     * @see org.kuali.ole.module.purap.businessobject.ReceivingItemBase#isConsideredEntered()
     */
    @Override
    public boolean isConsideredEntered() {
        boolean isConsideredEntered = super.isConsideredEntered();
        isConsideredEntered |= !((ObjectUtils.isNull(this.getItemReceivedTotalParts()) || this.getItemReceivedTotalParts().isZero()) &&
                (ObjectUtils.isNull(this.getItemDamagedTotalParts()) || this.getItemDamagedTotalParts().isZero()) &&
                (ObjectUtils.isNull(this.getItemReturnedTotalParts()) || this.getItemReturnedTotalParts().isZero()));
        return isConsideredEntered;
    }

    public Integer getItemFormatId() {
        return itemFormatId;
    }

    public void setItemFormatId(Integer itemFormatId) {
        this.itemFormatId = itemFormatId;
    }


    public List<OleLineItemCorrectionReceivingDoc> getOleCorrectionReceivingItemDocList() {
        return oleCorrectionReceivingItemDocList;
    }

    public void setOleCorrectionReceivingItemDocList(List<OleLineItemCorrectionReceivingDoc> oleCorrectionReceivingItemDocList) {
        this.oleCorrectionReceivingItemDocList = oleCorrectionReceivingItemDocList;
    }

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
     * Gets list of OleCorrectionReceivingItemExceptionNotes.
     *
     * @return correctionExceptionNoteList.
     */
    public List<OleCorrectionReceivingItemExceptionNotes> getCorrectionExceptionNoteList() {
        return correctionExceptionNoteList;
    }

    /**
     * Sets list of OleCorrectionReceivingItemExceptionNotes.
     *
     * @param exceptionNoteList to set.
     */
    public void setCorrectionExceptionNoteList(List<OleCorrectionReceivingItemExceptionNotes> correctionExceptionNoteList) {
        this.correctionExceptionNoteList = correctionExceptionNoteList;
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
        return OleCorrectionReceivingItemExceptionNotes.class;
    }

    /**
     * This method adds OleCorrectionReceivingItemExceptionNotes to the exceptioNoteList.
     *
     * @param exceptionNotes
     */
    public void addExceptionNote(OleCorrectionReceivingItemExceptionNotes exceptionNotes) {
        correctionExceptionNoteList.add(exceptionNotes);

    }

    /**
     * This method deletes exception note from exceptionNoteList at specified index.
     *
     * @param lineNum
     */
    public void deleteExceptionNote(int lineNum) {
        correctionExceptionNoteList.remove(lineNum);

    }

    /**
     * Gets list of OleCorrectionReceivingItemReceiptNotes.
     *
     * @return correctionReceiptNoteList.
     */
    public List<OleCorrectionReceivingItemReceiptNotes> getCorrectionReceiptNoteList() {
        return correctionReceiptNoteList;
    }

    /**
     * Sets list of OleCorrectionReceivingItemReceiptNotes.
     *
     * @param correctionReceiptNoteList to set.
     */
    public void setCorrectionReceiptNoteList(List<OleCorrectionReceivingItemReceiptNotes> correctionReceiptNoteList) {
        this.correctionReceiptNoteList = correctionReceiptNoteList;
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

    /**
     * This method returns OleLineItemReceivingReceiptNotes class
     *
     * @return Class
     */
    public Class getReceiptNotesClass() {
        return OleCorrectionReceivingItemReceiptNotes.class;
    }

    /**
     * This method adds OleLineItemReceivingReceiptNotes to the receiptNoteList.
     *
     * @param receiptNotes
     */
    public void addReceiptNote(OleCorrectionReceivingItemReceiptNotes receiptNotes) {
        correctionReceiptNoteList.add(receiptNotes);

    }

    /**
     * This method deletes receipt note from receiptNoteList at specified index.
     *
     * @param lineNum
     */
    public void deleteReceiptNote(int lineNum) {
        correctionReceiptNoteList.remove(lineNum);

    }

    /**
     * Gets list of Special Handling Notes.
     *
     * @return correctionSpecialHandlingNoteList.
     */
    public List<OleCorrectionReceivingItemReceiptNotes> getCorrectionSpecialHandlingNoteList() {
        return correctionSpecialHandlingNoteList;
    }

    /**
     * Sets list of Special Handling Notes.
     *
     * @param correctionSpecialHandlingNoteList.
     *
     */
    public void setCorrectionSpecialHandlingNoteList(List<OleCorrectionReceivingItemReceiptNotes> specialHandlingNoteList) {
        this.correctionSpecialHandlingNoteList = specialHandlingNoteList;
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
     * Gets list of OleLineItemReceivingReceiptNotes.
     *
     * @return noteList.
     */
    public List<OleCorrectionReceivingItemReceiptNotes> getCorrectionNoteList() {
        return correctionNoteList;
    }

    /**
     * Sets list of OleLineItemReceivingReceiptNotes.
     *
     * @param noteList to set.
     */
    public void setCorrectionNoteList(List<OleCorrectionReceivingItemReceiptNotes> correctionNoteList) {
        this.correctionNoteList = correctionNoteList;
    }

    /**
     * This method adds OleLineItemReceivingReceiptNotes to the noteList.
     *
     * @param receiptNotes
     */
    public void addNote(OleCorrectionReceivingItemReceiptNotes receiptNotes) {
        correctionNoteList.add(receiptNotes);
    }

    /**
     * This method deletes receipt note from noteList at specified index.
     *
     * @param lineNum
     */
    public void deleteNote(int lineNum) {
        correctionNoteList.remove(lineNum);

    }

    /**
     * Gets size of receipt notes list retrived from database
     *
     * @return correctionReceiptNoteListSize
     */
    public Integer getCorrectionReceiptNoteListSize() {
        return correctionReceiptNoteListSize;
    }

    /**
     * Sets size of receipt notes list retrived from database
     *
     * @param correctionReceiptNoteListSize
     */
    public void setCorrectionReceiptNoteListSize(Integer correctionReceiptNoteListSize) {
        this.correctionReceiptNoteListSize = correctionReceiptNoteListSize;
    }

    public String getItemTitleId() {
        return itemTitleId;
    }

    public void setItemTitleId(String itemTitleId) {
        this.itemTitleId = itemTitleId;
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

    public OleLineItemCorrectionReceivingDoc getOleLineItemReceivingCorrection() {
        return oleLineItemReceivingCorrection;
    }

    public void setOleLineItemReceivingCorrection(OleLineItemCorrectionReceivingDoc oleLineItemReceivingCorrection) {
        this.oleLineItemReceivingCorrection = oleLineItemReceivingCorrection;
    }

    public OleOrderRecord getOleOrderRecord() {
        return oleOrderRecord;
    }

    public void setOleOrderRecord(OleOrderRecord oleOrderRecord) {
        this.oleOrderRecord = oleOrderRecord;
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

    /**
     * Gets the notes attribute.
     *
     * @return Returns the notes.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes attribute value.
     *
     * @param notes The notes to set.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the itemLocation attribute.
     *
     * @return Returns the itemLocation.
     */
    public String getItemLocation() {
        return itemLocation;
    }

    /**
     * Sets the itemLocation attribute value.
     *
     * @param itemLocation The itemLocation to set.
     */
    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    /**
     * This method takes List of UUids as parameter and creates a LinkedHashMap with instance as key and id as value. and calls
     * Docstore's QueryServiceImpl class getWorkBibRecords method and return workBibDocument for passed instance Id.
     *
     * @param instanceIdsList
     * @return List<WorkBibDocument>
     */


    // Modified for Jira OLE-1900 Starts

    /**
     * This method will set copies into list of copies for LineItem.
     *
     * @param singleItem
     * @param workBibDocument
     * @return
     */
    /*public List<OleCopies> setCopiesToLineItem(WorkBibDocument workBibDocument) {
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
            int noOfCopies = workInstanceDocument.getItemDocumentList().size()
                    / this.getItemReceivedTotalParts().intValue();
            OleRequisitionCopies copy = new OleRequisitionCopies();
            copy.setParts(new KualiInteger(this.getItemReceivedTotalParts().intValue()));
            copy.setLocationCopies(workInstanceDocument.getHoldingsDocument().getLocationName());
            copy.setItemCopies(new KualiDecimal(noOfCopies));
            copy.setPartEnumeration(enumeration.toString());
            copy.setStartingCopyNumber(new KualiInteger(startingCopy));
            copies.add(copy);
            // }
        }
        return copies;
    }
*/
    public List<OleCopies> setCopiesToLineItem(BibTree bibTree) {
       List<HoldingsTree> holdingsTreeList= bibTree.getHoldingsTrees();
        List<OleCopies> copies = new ArrayList<OleCopies>();
        for (HoldingsTree holdingsTree : holdingsTreeList) {
            OleHoldings oleHoldings=new OleHoldings();
            HoldingOlemlRecordProcessor holdingOlemlRecordProcessor=new HoldingOlemlRecordProcessor();
            oleHoldings=(OleHoldings)holdingOlemlRecordProcessor.fromXML(holdingsTree.getHoldings().getContent());
            List<Item> items = holdingsTree.getItems();
            StringBuffer enumeration = new StringBuffer();
            for (int itemDocs = 0; itemDocs < items.size(); itemDocs++) {
                if (itemDocs + 1 == items.size()) {
                    enumeration = enumeration.append(items.get(itemDocs).getEnumeration());
                } else {
                    enumeration = enumeration.append(items.get(itemDocs).getEnumeration() + ",");
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
            int noOfCopies = holdingsTree.getItems().size()
                    / this.getItemReceivedTotalParts().intValue();
            OleRequisitionCopies copy = new OleRequisitionCopies();
            copy.setParts(new KualiInteger(this.getItemReceivedTotalParts().intValue()));
            copy.setLocationCopies(oleHoldings.getLocation().getLocationLevel().getName());
            copy.setItemCopies(new KualiDecimal(noOfCopies));
            copy.setPartEnumeration(enumeration.toString());
            copy.setStartingCopyNumber(new KualiInteger(startingCopy));
            copies.add(copy);
            // }
        }
        return copies;
    }

    /**
     * Gets the oleItemOriginalReceivedTotalQuantity attribute.
     *
     * @return Returns the oleItemOriginalReceivedTotalQuantity.
     */
    public KualiInteger getOleItemOriginalReceivedTotalQuantity() {
        return new KualiInteger(super.getItemOriginalReceivedTotalQuantity().intValue());
    }

    /**
     * Sets the oleItemOriginalReceivedTotalQuantity attribute value.
     *
     * @param oleItemOriginalReceivedTotalQuantity
     *         The oleItemOriginalReceivedTotalQuantity to set.
     */
    public void setOleItemOriginalReceivedTotalQuantity(KualiInteger oleItemOriginalReceivedTotalQuantity) {
        super.setItemOriginalReceivedTotalQuantity(new KualiDecimal(oleItemOriginalReceivedTotalQuantity.intValue()));
    }

    /**
     * Gets the oleItemOriginalReturnedTotalQuantity attribute.
     *
     * @return Returns the oleItemOriginalReturnedTotalQuantity.
     */
    public KualiInteger getOleItemOriginalReturnedTotalQuantity() {
        return new KualiInteger(super.getItemOriginalReturnedTotalQuantity().intValue());
    }

    /**
     * Sets the oleItemOriginalReturnedTotalQuantity attribute value.
     *
     * @param oleItemOriginalReturnedTotalQuantity
     *         The oleItemOriginalReturnedTotalQuantity to set.
     */
    public void setOleItemOriginalReturnedTotalQuantity(KualiInteger oleItemOriginalReturnedTotalQuantity) {
        super.setItemOriginalReturnedTotalQuantity(new KualiDecimal(oleItemOriginalReturnedTotalQuantity.intValue()));
    }

    /**
     * Gets the oleItemOriginalDamagedTotalQuantity attribute.
     *
     * @return Returns the oleItemOriginalDamagedTotalQuantity.
     */
    public KualiInteger getOleItemOriginalDamagedTotalQuantity() {
        return new KualiInteger(super.getItemOriginalDamagedTotalQuantity().intValue());
    }

    /**
     * Sets the oleItemOriginalDamagedTotalQuantity attribute value.
     *
     * @param oleItemOriginalDamagedTotalQuantity
     *         The oleItemOriginalDamagedTotalQuantity to set.
     */
    public void setOleItemOriginalDamagedTotalQuantity(KualiInteger oleItemOriginalDamagedTotalQuantity) {
        super.setItemOriginalDamagedTotalQuantity(new KualiDecimal(oleItemOriginalDamagedTotalQuantity.intValue()));
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
     * Gets the oleItemOriginalReceivedTotalParts attribute.
     *
     * @return Returns the oleItemOriginalReceivedTotalParts.
     */
    public KualiInteger getOleItemOriginalReceivedTotalParts() {
        return new KualiInteger(getItemOriginalReceivedTotalParts().intValue());
    }

    /**
     * Sets the oleItemOriginalReceivedTotalParts attribute value.
     *
     * @param oleItemOriginalReceivedTotalParts
     *         The oleItemOriginalReceivedTotalParts to set.
     */
    public void setOleItemOriginalReceivedTotalParts(KualiInteger oleItemOriginalReceivedTotalParts) {
        setItemOriginalReceivedTotalParts(new KualiDecimal(oleItemOriginalReceivedTotalParts.intValue()));
    }

    /**
     * Gets the oleItemOriginalReturnedTotalParts attribute.
     *
     * @return Returns the oleItemOriginalReturnedTotalParts.
     */
    public KualiInteger getOleItemOriginalReturnedTotalParts() {
        return new KualiInteger(getItemOriginalReturnedTotalParts().intValue());
    }

    /**
     * Sets the oleItemOriginalReturnedTotalParts attribute value.
     *
     * @param oleItemOriginalReturnedTotalParts
     *         The oleItemOriginalReturnedTotalParts to set.
     */
    public void setOleItemOriginalReturnedTotalParts(KualiInteger oleItemOriginalReturnedTotalParts) {
        setItemOriginalReturnedTotalParts(new KualiDecimal(oleItemOriginalReturnedTotalParts.intValue()));
    }

    /**
     * Gets the oleItemOriginalDamagedTotalParts attribute.
     *
     * @return Returns the oleItemOriginalDamagedTotalParts.
     */
    public KualiInteger getOleItemOriginalDamagedTotalParts() {
        return new KualiInteger(getItemOriginalDamagedTotalParts().intValue());
    }

    /**
     * Sets the oleItemOriginalDamagedTotalParts attribute value.
     *
     * @param oleItemOriginalDamagedTotalParts
     *         The oleItemOriginalDamagedTotalParts to set.
     */
    public void setOleItemOriginalDamagedTotalParts(KualiInteger oleItemOriginalDamagedTotalParts) {
        setItemOriginalDamagedTotalParts(new KualiDecimal(oleItemOriginalDamagedTotalParts.intValue()));
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

    public boolean checkIsEnumerationSplitIsIntegerOrNot(String enumerationSplit) {
        try {
            int startingCopy = Integer.parseInt(enumerationSplit);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // Modified for Jira OLE-1900 Starts


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