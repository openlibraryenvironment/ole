package org.kuali.ole.select.bo;

import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.businessobject.OleRequisitionItem;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Date;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 1/15/14
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPOClaimHistory extends PersistableBusinessObjectBase {
    private Integer id;
    private Date claimDate;
    private String operator;
    private Integer claimCount;
    private String claimResponseInformation;
    private Integer reqItemId;
    private String poDocNum;
    private Integer poItemId;
    private String vendorName;
    private String title;
    private OlePurchaseOrderItem olePurchaseOrderItem;
    private OleRequisitionItem oleRequisitionItem;

    public OleRequisitionItem getOleRequisitionItem() {
        return oleRequisitionItem;
    }

    public void setOleRequisitionItem(OleRequisitionItem oleRequisitionItem) {
        this.oleRequisitionItem = oleRequisitionItem;
    }

    public OlePurchaseOrderItem getOlePurchaseOrderItem() {
        return olePurchaseOrderItem;
    }

    public void setOlePurchaseOrderItem(OlePurchaseOrderItem olePurchaseOrderItem) {
        this.olePurchaseOrderItem = olePurchaseOrderItem;
    }

    public String getVendorName() {
        return vendorName;
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

    public Integer getReqItemId() {
        return reqItemId;
    }

    public void setReqItemId(Integer reqItemId) {
        this.reqItemId = reqItemId;
    }

    public String getPoDocNum() {
        return poDocNum;
    }

    public void setPoDocNum(String poDocNum) {
        this.poDocNum = poDocNum;
    }

    public Integer getPoItemId() {
        return poItemId;
    }

    public void setPoItemId(Integer poItemId) {
        this.poItemId = poItemId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getClaimCount() {
        return claimCount;
    }

    public void setClaimCount(Integer claimCount) {
        this.claimCount = claimCount;
    }

    public String getClaimResponseInformation() {
        return claimResponseInformation;
    }

    public void setClaimResponseInformation(String claimResponseInformation) {
        this.claimResponseInformation = claimResponseInformation;
    }
}
