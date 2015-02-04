package org.kuali.ole.deliver.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/26/12
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleRenewalLoanDocument extends PersistableBusinessObjectBase implements OlePatronLoanDocumentContract, OlePatronLoanDocumentsContract {

    private String itemBarcode;
    private String title;
    private String author;
    private String callNumber;
    private String location;
    private Date dueDate;
    private String messageInfo;
    private boolean itemCheckFlag;


    private List<? extends OlePatronLoanDocumentContract> olePatronLoanDocuments;

    public boolean isItemCheckFlag() {
        return itemCheckFlag;
    }

    public void setItemCheckFlag(boolean itemCheckFlag) {
        this.itemCheckFlag = itemCheckFlag;
    }

    public OleRenewalLoanDocument() {
        olePatronLoanDocuments = new ArrayList<OlePatronLoanDocumentContract>();
    }


    @Override
    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    @Override
    public String getId() {
        return "";
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String getItemBarcode() {
        return itemBarcode;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getCallNumber() {
        return callNumber;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public Date getDueDate() {
        return dueDate;
    }

    public void setOlePatronLoanDocuments(List<? extends OlePatronLoanDocumentContract> olePatronLoanDocuments) {
        this.olePatronLoanDocuments = olePatronLoanDocuments;
    }

    @Override
    public List<? extends OlePatronLoanDocumentContract> getOlePatronLoanDocuments() {
        return olePatronLoanDocuments;
    }
}
