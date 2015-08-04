package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/26/12
 * Time: 7:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleTemporaryCirculationHistory extends PersistableBusinessObjectBase {

    private String temporaryCirculationHistoryId;
    private String olePatronId;
    private String itemId;
    private String circulationLocationId;
    private String circulationLocationCode;
    private Timestamp checkInDate;
    private String callNumber;
    private String copyNumber;
    private String shelvingLocation;
    private String volumeNumber;
    private String itemStatus;
    private String title;
    private String author;
    private String itemType;
    private String itemUuid;
    private Timestamp dueDate;
    private Date checkOutDate;
    private String proxyPatronBarcode;
    private String proxyPatronBarcodeUrl;
    private String proxyPatronName;
    private String oleProxyPatronId;
    private String enumeration;
    private String chronology;

    public String getCirculationLocationCode() {
        return circulationLocationCode;
    }

    public void setCirculationLocationCode(String circulationLocationCode) {
        this.circulationLocationCode = circulationLocationCode;
    }

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    public String getTemporaryCirculationHistoryId() {
        return temporaryCirculationHistoryId;
    }

    public void setTemporaryCirculationHistoryId(String temporaryCirculationHistoryId) {
        this.temporaryCirculationHistoryId = temporaryCirculationHistoryId;
    }

    public String getOlePatronId() {
        return olePatronId;
    }

    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCirculationLocationId() {
        return circulationLocationId;
    }

    public void setCirculationLocationId(String circulationLocationId) {
        this.circulationLocationId = circulationLocationId;
    }

    public Timestamp getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Timestamp checkInDate) {
        this.checkInDate = checkInDate;
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

    public String getShelvingLocation() {
        return shelvingLocation;
    }

    public void setShelvingLocation(String shelvingLocation) {
        this.shelvingLocation = shelvingLocation;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getProxyPatronBarcode() {
        return proxyPatronBarcode;
    }

    public void setProxyPatronBarcode(String proxyPatronBarcode) {
        this.proxyPatronBarcode = proxyPatronBarcode;
    }

    public String getProxyPatronBarcodeUrl() {
        return proxyPatronBarcodeUrl;
    }

    public void setProxyPatronBarcodeUrl(String proxyPatronBarcodeUrl) {
        this.proxyPatronBarcodeUrl = proxyPatronBarcodeUrl;
    }

    public String getProxyPatronName() {
        return proxyPatronName;
    }

    public void setProxyPatronName(String proxyPatronName) {
        this.proxyPatronName = proxyPatronName;
    }

    public String getOleProxyPatronId() {
        return oleProxyPatronId;
    }

    public void setOleProxyPatronId(String oleProxyPatronId) {
        this.oleProxyPatronId = oleProxyPatronId;
    }

    public String getEnumeration() {
        return enumeration;
    }

    public void setEnumeration(String enumeration) {
        this.enumeration = enumeration;
    }

    public String getChronology() {
        return chronology;
    }

    public void setChronology(String chronology) {
        this.chronology = chronology;
    }
}
