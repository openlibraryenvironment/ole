package org.kuali.ole.select.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 7/4/13
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEInstanceSearch extends PersistableBusinessObjectBase {

    private String title;
    private String author;
    private String publisher;
    private List<String> instanceIdList;
    private String bibId;
    private String instanceId;
    private String location;
    private String callNumber;
    private String localId;
    private String copyNumber;
    private  String issn;
    private String instanceLocalId;

    public String getInstanceLocalId() {
        return instanceLocalId;
    }

    public void setInstanceLocalId(String instanceLocalId) {
        this.instanceLocalId = instanceLocalId;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(String copyNumber) {
        this.copyNumber = copyNumber;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public OLEInstanceSearch() {
        instanceIdList = new ArrayList<String>();
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

    public String getPublisher() {
        return publisher;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getInstanceIdList() {
        return instanceIdList;
    }

    public void setInstanceIdList(List<String> instanceIdList) {
        this.instanceIdList = instanceIdList;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }
}

