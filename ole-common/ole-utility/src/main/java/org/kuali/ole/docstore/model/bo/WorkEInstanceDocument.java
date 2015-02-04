package org.kuali.ole.docstore.model.bo;

import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.CallNumber;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.Location;
import org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml.Note;
import sun.awt.geom.AreaOp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Srinivasane
 * Date: 3/30/12
 * Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkEInstanceDocument extends OleDocument {

    private String eInstanceIdentifier;
    private String instanceIdentifier;
    private List<WorkEInstanceCoverage> instanceCoverages;
    private List<WorkEInstancePerpetualAccess> instancePerpetualAccesses;
    private CallNumber callNumber;
    protected List<Note> note;
    protected Location location;
    private String oleERSIdentifier;
    private String relatedEInstances;
    private String oleERSTitle;

    private String publisher;
    private String imprint;
    private String platform;
    private String accessStatus;
    private String StatusDate;
    private boolean staffOnly;
    private String statisticalCode;
    private String publicDisplayNote;
    private WorkEHoldingsDocument workEHoldingsDocument;

    private String bibIdentifier;

    public WorkEInstanceDocument() {
        instanceCoverages = new ArrayList<WorkEInstanceCoverage>();
        instancePerpetualAccesses = new ArrayList<WorkEInstancePerpetualAccess>();
        note = new ArrayList<Note>();
    }

    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    public void setInstanceIdentifier(String instanceIdentifier) {
        this.instanceIdentifier = instanceIdentifier;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    public String geteInstanceIdentifier() {
        return eInstanceIdentifier;
    }

    public void seteInstanceIdentifier(String eInstanceIdentifier) {
        this.eInstanceIdentifier = eInstanceIdentifier;
    }

    public List<WorkEInstanceCoverage> getInstanceCoverages() {
        return instanceCoverages;
    }

    public void setInstanceCoverages(List<WorkEInstanceCoverage> instanceCoverages) {
        this.instanceCoverages = instanceCoverages;
    }

    public List<WorkEInstancePerpetualAccess> getInstancePerpetualAccesses() {
        return instancePerpetualAccesses;
    }

    public void setInstancePerpetualAccesses(List<WorkEInstancePerpetualAccess> instancePerpetualAccesses) {
        this.instancePerpetualAccesses = instancePerpetualAccesses;
    }

    public CallNumber getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(CallNumber callNumber) {
        this.callNumber = callNumber;
    }

    public List<Note> getNote() {
        return note;
    }

    public void setNote(List<Note> note) {
        this.note = note;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getRelatedEInstances() {
        return relatedEInstances;
    }

    public void setRelatedEInstances(String relatedEInstances) {
        this.relatedEInstances = relatedEInstances;
    }

    public String getOleERSTitle() {
        return oleERSTitle;
    }

    public void setOleERSTitle(String oleERSTitle) {
        this.oleERSTitle = oleERSTitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImprint() {
        return imprint;
    }

    public void setImprint(String imprint) {
        this.imprint = imprint;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String accessStatus) {
        this.accessStatus = accessStatus;
    }

    public String getStatusDate() {
        return StatusDate;
    }

    public void setStatusDate(String statusDate) {
        StatusDate = statusDate;
    }

    public boolean isStaffOnly() {
        return staffOnly;
    }

    public void setStaffOnly(boolean staffOnly) {
        this.staffOnly = staffOnly;
    }

    public String getStatisticalCode() {
        return statisticalCode;
    }

    public void setStatisticalCode(String statisticalCode) {
        this.statisticalCode = statisticalCode;
    }

    public WorkEHoldingsDocument getWorkEHoldingsDocument() {
        return workEHoldingsDocument;
    }

    public void setWorkEHoldingsDocument(WorkEHoldingsDocument workEHoldingsDocument) {
        this.workEHoldingsDocument = workEHoldingsDocument;
    }

    public String getPublicDisplayNote() {
        return publicDisplayNote;
    }

    public void setPublicDisplayNote(String publicDisplayNote) {
        this.publicDisplayNote = publicDisplayNote;
    }

    @Override
    public String toString() {
        return "WorkEInstanceDocument{" +
                "eInstanceIdentifier='" + eInstanceIdentifier + '\'' +
                ", instanceIdentifier='" + instanceIdentifier + '\'' +
                ", instanceCoverages=" + instanceCoverages +
                ", instancePerpetualAccesses=" + instancePerpetualAccesses +
                ", callNumber=" + callNumber +
                ", note=" + note +
                ", location=" + location +
                ", bibIdentifier='" + bibIdentifier + '\'' +
                '}';
    }
}
