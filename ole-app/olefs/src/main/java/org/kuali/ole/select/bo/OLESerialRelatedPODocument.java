package org.kuali.ole.select.bo;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.krad.util.KRADConstants;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 9/25/13
 * Time: 3:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESerialRelatedPODocument implements Serializable {

    private boolean selectPO;
    private String poId;
    private String poIdLink;
    private String vendorId;
    private String vendorAliasName;
    private String vendorName;
    private String actionInterval;

    public String getActionInterval() {
        return actionInterval;
    }

    public void setActionInterval(String actionInterval) {
        this.actionInterval = actionInterval;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorAliasName() {
        return vendorAliasName;
    }

    public void setVendorAliasName(String vendorAliasName) {
        this.vendorAliasName = vendorAliasName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public boolean isSelectPO() {
        return selectPO;
    }

    public void setSelectPO(boolean selectPO) {
        this.selectPO = selectPO;
    }

    public String getPoIdLink() {
        return poIdLink;
    }

    public void setPoIdLink(String poDocNumber) {
        String documentTypeName = PurapConstants.PurchaseOrderDocTypes.PURCHASE_ORDER_DOCUMENT;
        DocumentType docType = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(documentTypeName);
        String docHandlerUrl = docType.getResolvedDocumentHandlerUrl();
        int endSubString = docHandlerUrl.lastIndexOf("/");
        String serverName = docHandlerUrl.substring(0, endSubString);
        String handler = docHandlerUrl.substring(endSubString + 1, docHandlerUrl.lastIndexOf("?"));
        this.poIdLink = serverName + "/" + KRADConstants.PORTAL_ACTION + "?channelTitle=" + docType.getName() + "&channelUrl=" + handler + "?" + KRADConstants.DISPATCH_REQUEST_PARAMETER + "=" + KRADConstants.DOC_HANDLER_METHOD + "&" + KRADConstants.PARAMETER_DOC_ID + "=" + poDocNumber + "&" + KRADConstants.PARAMETER_COMMAND + "=" + KewApiConstants.DOCSEARCH_COMMAND;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }
}
