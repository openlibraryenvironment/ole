package org.kuali.ole.docstore.model.xmlpojo.ingest;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tirumalesh.b
 * Date: 9/2/12 Time: 7:50 PM
 */
public class RequestDocument implements Cloneable {
    private String id;
    private String category;
    private String type;
    private String format;
    private String operation;
    private Content content = new Content();

    //New fields for License

    private String documentName;
    private String documentTitle;
    private String documentMimeType;
    private String user;
    private String uuid;
    private List<RequestDocument> linkedRequestDocuments = new ArrayList<RequestDocument>();
    private AdditionalAttributes additionalAttributes;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<RequestDocument> getLinkedRequestDocuments() {
        return linkedRequestDocuments;
    }

    public void setLinkedRequestDocuments(List<RequestDocument> linkedRequestDocuments) {
        this.linkedRequestDocuments = linkedRequestDocuments;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
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

    public AdditionalAttributes getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(AdditionalAttributes additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public void addLinkedRequestDocument(RequestDocument requestDocument) {
        if (!linkedRequestDocuments.contains(requestDocument)) {
            this.linkedRequestDocuments.add(requestDocument);
        }
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
