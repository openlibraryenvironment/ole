package org.kuali.ole.docstore.model.xmlpojo.ingest;


import java.util.ArrayList;
import java.util.List;

/**
 * User: tirumalesh.b
 * Date: 9/2/12 Time: 7:50 PM
 */
public class ResponseDocument {
    private List<LinkInformation> linkInformation;
    private List<ResponseDocument> linkedDocuments = new ArrayList<ResponseDocument>();
    private List<ResponseDocument> linkedInstanceDocuments = new ArrayList<ResponseDocument>();
    private String id;
    private String category;
    private String type;
    private String format;
    private String uuid;
    private Content content;

    //New fields for License

    private String documentName;
    private String documentTitle;
    private String documentMimeType;
    private String status;
    private String statusMessage;
    private String version;
    private AdditionalAttributes additionalAttributes;

    public AdditionalAttributes getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(AdditionalAttributes additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<ResponseDocument> getLinkedInstanceDocuments() {
        return linkedInstanceDocuments;
    }

    public void setLinkedInstanceDocuments(List<ResponseDocument> linkedInstanceDocuments) {
        this.linkedInstanceDocuments = linkedInstanceDocuments;
    }

    public List<ResponseDocument> getLinkedDocuments() {
        return linkedDocuments;
    }

    public void setLinkedDocuments(List<ResponseDocument> linkedDocuments) {
        this.linkedDocuments = linkedDocuments;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public List<LinkInformation> getLinkInformation() {
        return linkInformation;
    }

    public void setLinkInformation(List<LinkInformation> linkInformation) {
        this.linkInformation = linkInformation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDocumentMimeType() {
        return documentMimeType;
    }

    public void setDocumentMimeType(String documentMimeType) {
        this.documentMimeType = documentMimeType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void addLinkedDocument(ResponseDocument responseDocument) {
        if (!linkedDocuments.contains(responseDocument)) {
            this.linkedDocuments.add(responseDocument);
        }
    }

    public void addLinkedInstanseDocument(ResponseDocument responseDocument) {
        if (!linkedInstanceDocuments.contains(responseDocument)) {
            this.linkedInstanceDocuments.add(responseDocument);
        }
    }
}
