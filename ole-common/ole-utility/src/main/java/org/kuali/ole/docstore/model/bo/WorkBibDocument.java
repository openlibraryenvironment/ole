package org.kuali.ole.docstore.model.bo;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for logical representation of OLE documents of category:Work and type:Bibliographic.
 * User: tirumalesh.b
 * Date: 1/2/12 Time: 2:59 PM
 */
public class WorkBibDocument
        extends OleDocument {

    protected List<String> localId = new ArrayList<String>();
    protected List<String> titles = new ArrayList<String>();
    protected List<String> authors = new ArrayList<String>();
    protected List<String> subjects;
    protected List<String> issns;
    protected List<String> isbns;
    protected List<String> publicationPlaces;
    protected List<String> publishers = new ArrayList<String>();
    protected List<String> descriptions;
    protected List<String> publicationDates = new ArrayList<String>();
    protected List<String> editions = new ArrayList<String>();
    protected List<String> formGenres;
    protected List<String> languages;
    protected List<String> formats;
    protected WorkInstanceDocument instanceDocument;
    protected List<WorkInstanceDocument> workInstanceDocumentList;
    protected String instanceIdentifier;
    protected String docFormat;
    protected String staffOnlyFlag;
    protected WorkEInstanceDocument eInstanceDocument;
    protected List<WorkEInstanceDocument> workEInstanceDocumentList;

    public List<String> getLocalId() {
        return localId;
    }

    public void setLocalId(List<String> localId) {
        this.localId = localId;
    }

    public String getStaffOnlyFlag() {
        return staffOnlyFlag;
    }

    public void setStaffOnlyFlag(String staffOnlyFlag) {
        this.staffOnlyFlag = staffOnlyFlag;
    }

    public WorkInstanceDocument getInstanceDocument() {
        return instanceDocument;
    }

    public void setInstanceDocument(WorkInstanceDocument instanceDocument) {
        this.instanceDocument = instanceDocument;
    }

    public WorkBibDocument() {
        docCategory = DocCategory.WORK;
        docType = DocType.BIB;
    }

    /* Getters and Setters */
    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getIssns() {
        return issns;
    }

    public void setIssns(List<String> issns) {
        this.issns = issns;
    }

    public List<String> getIsbns() {
        return isbns;
    }

    public void setIsbns(List<String> isbns) {
        this.isbns = isbns;
    }

    public List<String> getPublicationPlaces() {
        return publicationPlaces;
    }

    public void setPublicationPlaces(List<String> publicationPlaces) {
        this.publicationPlaces = publicationPlaces;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
    }

    public List<String> getPublicationDates() {
        return publicationDates;
    }

    public void setPublicationDates(List<String> publicationDates) {
        this.publicationDates = publicationDates;
    }

    public List<String> getEditions() {
        return editions;
    }

    public void setEditions(List<String> editions) {
        this.editions = editions;
    }

    public List<String> getFormGenres() {
        return formGenres;
    }

    public void setFormGenres(List<String> formGenres) {
        this.formGenres = formGenres;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getFormats() {
        return formats;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
    }


    /* Convenience methods */
    public String getTitle() {
        if (!CollectionUtils.isEmpty(titles)) {
            return titles.get(0);
        } else {
            return null;
        }
    }

    public void setTitle(String title) {
        if (null == titles) {
            titles = new ArrayList<String>();
            titles.add(title);
        } else {
            titles.add(title);
        }
    }

    public String getEdition() {
        if (!CollectionUtils.isEmpty(editions)) {
            return editions.get(0);
        } else {
            return null;
        }
    }

    public void setEdition(String edition) {
        if (null == editions) {
            editions = new ArrayList<String>();
            editions.add(edition);
        } else {
            editions.add(edition);
        }
    }

    public String getAuthor() {
        if (!CollectionUtils.isEmpty(authors)) {
            return authors.get(0);
        } else {
            return null;
        }
    }

    public void setAuthor(String author) {
        if (null == authors) {
            authors = new ArrayList<String>();
            authors.add(author);
        } else {
            authors.add(author);
        }
    }

    public String getPublicationDate() {
        if (!CollectionUtils.isEmpty(publicationDates)) {
            return publicationDates.get(0);
        } else {
            return null;
        }
    }

    public void setPublicationDate(String publicationDate) {
        if (null == publicationDates) {
            publicationDates = new ArrayList<String>();
            publicationDates.add(publicationDate);
        } else {
            publicationDates.add(publicationDate);
        }
    }

    public String getIsbn() {
        if (!CollectionUtils.isEmpty(isbns)) {
            return isbns.get(0);
        } else {
            return null;
        }
    }

    public void setIsbn(String isbn) {
        if (null == isbns) {
            isbns = new ArrayList<String>();
            isbns.add(isbn);
        } else {
            isbns.add(isbn);
        }
    }

    public String getIssn() {
        if (!CollectionUtils.isEmpty(issns)) {
            return issns.get(0);
        } else {
            return null;
        }
    }

    public void setIssn(String issn) {
        if (null == issns) {
            issns = new ArrayList<String>();
            issns.add(issn);
        } else {
            issns.add(issn);
        }
    }


    public String getPublisher() {
        if (!CollectionUtils.isEmpty(publishers)) {
            return publishers.get(0);
        } else {
            return null;
        }
    }

    public List<WorkInstanceDocument> getWorkInstanceDocumentList() {
        return workInstanceDocumentList;
    }

    public void setWorkInstanceDocumentList(List<WorkInstanceDocument> workInstanceDocumentList) {
        this.workInstanceDocumentList = workInstanceDocumentList;
    }

    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    public void setInstanceIdentifier(String instanceIdentifier) {
        this.instanceIdentifier = instanceIdentifier;
    }

    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public WorkEInstanceDocument geteInstanceDocument() {
        return eInstanceDocument;
    }

    public void seteInstanceDocument(WorkEInstanceDocument eInstanceDocument) {
        this.eInstanceDocument = eInstanceDocument;
    }

    public List<WorkEInstanceDocument> getWorkEInstanceDocumentList() {
        return workEInstanceDocumentList;
    }

    public void setWorkEInstanceDocumentList(List<WorkEInstanceDocument> workEInstanceDocumentList) {
        this.workEInstanceDocumentList = workEInstanceDocumentList;
    }


    public String getLocalIds() {
        if (!CollectionUtils.isEmpty(localId)) {
            return localId.get(0);
        } else {
            return null;
        }
    }

    public void setLocalIds(String localIds) {
        if (null == localId) {
            localId = new ArrayList<String>();
            localId.add(localIds);
        } else {
            localId.add(localIds);
        }
    }


    @Override
    public String toString() {
        return "WorkBibDocument{" +
                "localId=" + localId +
                ", titles=" + titles +
                ", authors=" + authors +
                ", subjects=" + subjects +
                ", issns=" + issns +
                ", isbns=" + isbns +
                ", publicationPlaces=" + publicationPlaces +
                ", publishers=" + publishers +
                ", descriptions=" + descriptions +
                ", publicationDates=" + publicationDates +
                ", editions=" + editions +
                ", formGenres=" + formGenres +
                ", languages=" + languages +
                ", formats=" + formats +
                ", instanceDocument=" + instanceDocument +
                ", workInstanceDocumentList=" + workInstanceDocumentList +
                ", instanceIdentifier='" + instanceIdentifier + '\'' +
                ", docFormat='" + docFormat + '\'' +
                ", staffOnlyFlag='" + staffOnlyFlag + '\'' +
                ", eInstanceDocument=" + eInstanceDocument +
                ", workEInstanceDocumentList=" + workEInstanceDocumentList +
                '}';
    }

    public void setPublisher(String publisher) {
        if (null == publishers) {
            publishers = new ArrayList<String>();
            publishers.add(publisher);
        } else {
            publishers.add(publisher);
        }


    }

    /*   public String getItemLink() {
        if (!CollectionUtils.isEmpty(itemLinks)) {
            return itemLinks.get(0);
        }
        else {
            return null;
        }
    }

    public void setItemLink(String itemLink) {
        if (null == itemLinks) {
            itemLinks = new ArrayList<String>();
            itemLinks.add(itemLink);
        }
        else {
            itemLinks.add(itemLink);
        }
    }*/

}
