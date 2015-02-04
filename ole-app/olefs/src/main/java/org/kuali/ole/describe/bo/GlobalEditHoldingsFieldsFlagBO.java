package org.kuali.ole.describe.bo;

/**
 * Created with IntelliJ IDEA.
 * User: mjagan
 * Date: 11/3/14
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class GlobalEditHoldingsFieldsFlagBO {

    private boolean locationEditFlag = false;
    private boolean callNumberEditFlag = false;
    private boolean shelvingOrderEditFlag = false;
    private boolean callNumberTypeEditFlag = false;
    private boolean callNumberPrefixEditFlag = false;
    private boolean copyNumberEditFlag = false;
    private boolean extentOwnerShipEditFlag = false;
    private boolean extendedInfoEditFlag = false;
    private boolean holdingsNoteEditFlag = false;
    private boolean receiptStatusEditFlag = false;
    private boolean accessInformationEditFlag = false;


    public boolean isAccessInformationEditFlag() {
        return accessInformationEditFlag;
    }

    public void setAccessInformationEditFlag(boolean accessInformationEditFlag) {
        this.accessInformationEditFlag = accessInformationEditFlag;
    }

    public boolean isReceiptStatusEditFlag() {
        return receiptStatusEditFlag;
    }

    public void setReceiptStatusEditFlag(boolean receiptStatusEditFlag) {
        this.receiptStatusEditFlag = receiptStatusEditFlag;
    }

    public boolean isLocationEditFlag() {
        return locationEditFlag;
    }

    public void setLocationEditFlag(boolean locationEditFlag) {
        this.locationEditFlag = locationEditFlag;
    }

    public boolean isCallNumberEditFlag() {
        return callNumberEditFlag;
    }

    public void setCallNumberEditFlag(boolean callNumberEditFlag) {
        this.callNumberEditFlag = callNumberEditFlag;
    }

    public boolean isShelvingOrderEditFlag() {
        return shelvingOrderEditFlag;
    }

    public void setShelvingOrderEditFlag(boolean shelvingOrderEditFlag) {
        this.shelvingOrderEditFlag = shelvingOrderEditFlag;
    }

    public boolean isCallNumberTypeEditFlag() {
        return callNumberTypeEditFlag;
    }

    public void setCallNumberTypeEditFlag(boolean callNumberTypeEditFlag) {
        this.callNumberTypeEditFlag = callNumberTypeEditFlag;
    }

    public boolean isCallNumberPrefixEditFlag() {
        return callNumberPrefixEditFlag;
    }

    public void setCallNumberPrefixEditFlag(boolean callNumberPrefixEditFlag) {
        this.callNumberPrefixEditFlag = callNumberPrefixEditFlag;
    }

    public boolean isCopyNumberEditFlag() {
        return copyNumberEditFlag;
    }

    public void setCopyNumberEditFlag(boolean copyNumberEditFlag) {
        this.copyNumberEditFlag = copyNumberEditFlag;
    }

    public boolean isExtentOwnerShipEditFlag() {
        return extentOwnerShipEditFlag;
    }

    public void setExtentOwnerShipEditFlag(boolean extentOwnerShipEditFlag) {
        this.extentOwnerShipEditFlag = extentOwnerShipEditFlag;
    }

    public boolean isExtendedInfoEditFlag() {
        return extendedInfoEditFlag;
    }

    public void setExtendedInfoEditFlag(boolean extendedInfoEditFlag) {
        this.extendedInfoEditFlag = extendedInfoEditFlag;
    }

    public boolean isHoldingsNoteEditFlag() {
        return holdingsNoteEditFlag;
    }

    public void setHoldingsNoteEditFlag(boolean holdingsNoteEditFlag) {
        this.holdingsNoteEditFlag = holdingsNoteEditFlag;
    }
}
