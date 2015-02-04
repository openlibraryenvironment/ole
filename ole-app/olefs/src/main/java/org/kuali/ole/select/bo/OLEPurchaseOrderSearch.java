package org.kuali.ole.select.bo;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 11/6/13
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPurchaseOrderSearch extends PersistableBusinessObjectBase {
    private String vendorName;
    private String vendorAliasName;

    private Integer poId;
    private String vendorId;
    private String instanceId;
    private String callNumber;
    private String copyNumber;
    private String publisher;
    private String localId;

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPoIdLink() {
        return poIdLink;
    }

    public void setPoIdLink(String poDocNumber) {
       /* String documentTypeName = PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_DOCUMENT;
        DocumentType docType = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(documentTypeName);
        String docHandlerUrl = docType.getResolvedDocumentHandlerUrl();
        int endSubString = docHandlerUrl.lastIndexOf("/");
        String serverName = docHandlerUrl.substring(0, endSubString);
        String handler = docHandlerUrl.substring(endSubString + 1, docHandlerUrl.lastIndexOf("?"));
        this.poIdLink = serverName + "/" + KRADConstants.PORTAL_ACTION + "?channelTitle=" + docType.getName() + "&channelUrl=" + handler + "?" + KRADConstants.DISPATCH_REQUEST_PARAMETER + "=" + KRADConstants.DOC_HANDLER_METHOD + "&" + KRADConstants.PARAMETER_DOC_ID + "=" + poDocNumber + "&" + KRADConstants.PARAMETER_COMMAND + "=" + KewApiConstants.DOCSEARCH_COMMAND;*/
        this.poIdLink=poDocNumber;
    }


    private String title;
    private  String bibId;
    private String poIdLink;

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getVendorAliasName() {
        return vendorAliasName;
    }

    public void setVendorAliasName(String vendorAliasName) {
        this.vendorAliasName = vendorAliasName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }



    private String author;
    private String issn;

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public void setVendorName(String vendorName) {

        this.vendorName = vendorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }




}

