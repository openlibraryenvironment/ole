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
package org.kuali.ole.select.document.web;

import org.apache.struts.action.ActionMapping;
import org.kuali.ole.module.purap.businessobject.PurchaseOrderType;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.OleReceivingQueueSearchDocument;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.kns.service.DataDictionaryService;
import org.kuali.rice.kns.web.struts.form.KualiTransactionalDocumentFormBase;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class OleReceivingQueueSearchForm extends KualiTransactionalDocumentFormBase {

    protected Integer purchaseOrderNumber;

    protected String standardNumber;

    protected String title;

    //protected String journal;

    protected String vendorName;

    protected String beginDate;

    protected String endDate;

    //protected boolean serials;

    //protected boolean standingOrders;

    //protected boolean vendor;

    protected boolean monograph;

    //protected boolean purchaseOrderDate;

    //protected boolean status;

    protected VendorDetail vendorDetail;

    private String purchaseOrderStatusDescription;

    protected PurchaseOrderType orderType;

    private String purchaseOrderType;

    protected boolean receive;

    protected String author;

    protected String publisher;

    protected String edition;

    protected String quatityOrdered;

    protected String points;

    protected String instructions;

    protected String url;

    public String getUrl() {
        return OLEConstants.DOC_HANDLER_URL;
    }

    public List<OlePurchaseOrderItem> purchaseOrders = new ArrayList<OlePurchaseOrderItem>(0);

    /**
     * Gets the purchaseOrderStatusDescription attribute.
     *
     * @return Returns the purchaseOrderStatusDescription.
     */
    public String getPurchaseOrderStatusDescription() {
        return purchaseOrderStatusDescription;
    }

    /**
     * Sets the purchaseOrderStatusDescription attribute value.
     *
     * @param purchaseOrderStatusDescription The purchaseOrderStatusDescription to set.
     */
    public void setPurchaseOrderStatusDescription(String purchaseOrderStatusDescription) {
        this.purchaseOrderStatusDescription = purchaseOrderStatusDescription;
    }

    /**
     * Gets the purchaseOrderType attribute.
     *
     * @return Returns the purchaseOrderType.
     */
    public String getPurchaseOrderType() {
        return purchaseOrderType;
    }

    /**
     * Sets the purchaseOrderType attribute value.
     *
     * @param purchaseOrderType The purchaseOrderType to set.
     */
    public void setPurchaseOrderType(String purchaseOrderType) {
        this.purchaseOrderType = purchaseOrderType;
    }

    /**
     * Gets the purchaseOrders attribute.
     *
     * @return Returns the purchaseOrders.
     */
    public List<OlePurchaseOrderItem> getPurchaseOrders() {
        return purchaseOrders;
    }

    /**
     * Sets the purchaseOrders attribute value.
     *
     * @param purchaseOrders The purchaseOrders to set.
     */
    public void setPurchaseOrders(List<OlePurchaseOrderItem> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
    }

    /**
     * Gets the purchaseOrderNumber attribute.
     *
     * @return Returns the purchaseOrderNumber.
     */
    public Integer getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    /**
     * Sets the purchaseOrderNumber attribute value.
     *
     * @param purchaseOrderNumber The purchaseOrderNumber to set.
     */
    public void setPurchaseOrderNumber(Integer purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    /**
     * Gets the standardNumber attribute.
     *
     * @return Returns the standardNumber.
     */
    public String getStandardNumber() {
        return standardNumber;
    }

    /**
     * Sets the standardNumber attribute value.
     *
     * @param standardNumber The standardNumber to set.
     */
    public void setStandardNumber(String standardNumber) {
        this.standardNumber = standardNumber;
    }

    /**
     * Gets the title attribute.
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title attribute value.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the journal attribute.
     * @return Returns the journal.
     *//*
    public String getJournal() {
        return journal;
    }

    *//**
     * Sets the journal attribute value.
     * @param journal The journal to set.
     *//*
    public void setJournal(String journal) {
        this.journal = journal;
    }*/

    /**
     * Gets the vendorName attribute.
     *
     * @return Returns the vendorName.
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * Sets the vendorName attribute value.
     *
     * @param vendorName The vendorName to set.
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * Gets the serials attribute.
     * @return Returns the serials.
     *//*
    public boolean isSerials() {
        return serials;
    }

    *//**
     * Sets the serials attribute value.
     * @param serials The serials to set.
     *//*
    public void setSerials(boolean serials) {
        this.serials = serials;
    }*/

    /**
     * Gets the standingOrders attribute.
     * @return Returns the standingOrders.
     *//*
    public boolean isStandingOrders() {
        return standingOrders;
    }

    *//**
     * Sets the standingOrders attribute value.
     * @param standingOrders The standingOrders to set.
     *//*
    public void setStandingOrders(boolean standingOrders) {
        this.standingOrders = standingOrders;
    }*/

    /**
     * Gets the vendor attribute.
     * @return Returns the vendor.
     *//*
    public boolean isVendor() {
        return vendor;
    }

    *//**
     * Sets the vendor attribute value.
     * @param vendor The vendor to set.
     *//*
    public void setVendor(boolean vendor) {
        this.vendor = vendor;
    }*/

    /**
     * Gets the monograph attribute.
     *
     * @return Returns the monograph.
     */
    public boolean isMonograph() {
        return monograph;
    }

    /**
     * Sets the monograph attribute value.
     *
     * @param monograph The monograph to set.
     */
    public void setMonograph(boolean monograph) {
        this.monograph = monograph;
    }

    /**
     * Gets the purchaseOrderDate attribute.
     * @return Returns the purchaseOrderDate.
     *//*
    public boolean isPurchaseOrderDate() {
        return purchaseOrderDate;
    }

    *//**
     * Sets the purchaseOrderDate attribute value.
     * @param purchaseOrderDate The purchaseOrderDate to set.
     *//*
    public void setPurchaseOrderDate(boolean purchaseOrderDate) {
        this.purchaseOrderDate = purchaseOrderDate;
    }*/

    /**
     * Gets the status attribute.
     * @return Returns the status.
     *//*
    public boolean isStatus() {
        return status;
    }

    *//**
     * Sets the status attribute value.
     * @param status The status to set.
     *//*
    public void setStatus(boolean status) {
        this.status = status;
    }*/

    /**
     * Gets the vendorDetail attribute.
     *
     * @return Returns the vendorDetail.
     */
    public VendorDetail getVendorDetail() {
        return vendorDetail;
    }

    /**
     * Sets the vendorDetail attribute value.
     *
     * @param vendorDetail The vendorDetail to set.
     */
    public void setVendorDetail(VendorDetail vendorDetail) {
        this.vendorDetail = vendorDetail;
    }

    /**
     * Gets the receive attribute.
     *
     * @return Returns the receive.
     */
    public boolean isReceive() {
        return receive;
    }

    /**
     * Sets the receive attribute value.
     *
     * @param receive The receive to set.
     */
    public void setReceive(boolean receive) {
        this.receive = receive;
    }

    /**
     * Gets the author attribute.
     *
     * @return Returns the author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author attribute value.
     *
     * @param author The author to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the publisher attribute.
     *
     * @return Returns the publisher.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the publisher attribute value.
     *
     * @param publisher The publisher to set.
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Gets the edition attribute.
     *
     * @return Returns the edition.
     */
    public String getEdition() {
        return edition;
    }

    /**
     * Sets the edition attribute value.
     *
     * @param edition The edition to set.
     */
    public void setEdition(String edition) {
        this.edition = edition;
    }

    /**
     * Gets the quatityOrdered attribute.
     *
     * @return Returns the quatityOrdered.
     */
    public String getQuatityOrdered() {
        return quatityOrdered;
    }

    /**
     * Sets the quatityOrdered attribute value.
     *
     * @param quatityOrdered The quatityOrdered to set.
     */
    public void setQuatityOrdered(String quatityOrdered) {
        this.quatityOrdered = quatityOrdered;
    }

    /**
     * Gets the points attribute.
     *
     * @return Returns the points.
     */
    public String getPoints() {
        return points;
    }

    /**
     * Sets the points attribute value.
     *
     * @param points The points to set.
     */
    public void setPoints(String points) {
        this.points = points;
    }

    /**
     * Gets the instructions attribute.
     *
     * @return Returns the instructions.
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the instructions attribute value.
     *
     * @param instructions The instructions to set.
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * Constructs a OleReceivingQueueSearchForm.java.
     */
    public OleReceivingQueueSearchForm() {
        super();
        OleReceivingQueueSearchDocument document = new OleReceivingQueueSearchDocument();
        setDocument(document);
        setDocTypeName(SpringContext.getBean(DataDictionaryService.class).getDocumentTypeNameByClass(OleReceivingQueueSearchDocument.class));
    }

    /**
     * @see org.kuali.rice.kns.web.struts.form.KualiTransactionalDocumentFormBase#populate(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void populate(HttpServletRequest req) {
        super.populate(req);
    }

    /**
     * @see org.kuali.rice.kns.web.struts.form.KualiForm#getRefreshCaller()
     */
    @Override
    public String getRefreshCaller() {
        return "refreshCaller";
    }

    /**
     * @see org.kuali.rice.kns.web.struts.form.KualiDocumentFormBase#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    /**
     * Gets the beginDate attribute.
     *
     * @return Returns the beginDate.
     */
    public String getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the beginDate attribute value.
     *
     * @param beginDate The beginDate to set.
     */
    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * Gets the endDate attribute.
     *
     * @return Returns the endDate.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the endDate attribute value.
     *
     * @param endDate The endDate to set.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
