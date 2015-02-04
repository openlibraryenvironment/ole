package org.kuali.ole.bo.serachRetrieve;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/17/12
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUBibDocument {

    public String title;
    public String author;
    public String subject;
    public String issn;
    public String isbn;
    public String publicationPlace;
    public String publisher;
    public String description;
    public String publicationDate;
    public String edition;
    public String formGenres;
    public String language;
    public String id;
    public String format;
    public String instanceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublicationPlace() {
        return publicationPlace;
    }

    public void setPublicationPlace(String publicationPlace) {
        this.publicationPlace = publicationPlace;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFormGenres() {
        return formGenres;
    }

    public void setFormGenres(String formGenres) {
        this.formGenres = formGenres;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }


    @Override
    public String toString() {
        return "Bibliographic Record {" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", subject='" + subject + '\'' +
                ", issn='" + issn + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publicationPlace='" + publicationPlace + '\'' +
                ", publisher='" + publisher + '\'' +
                ", description='" + description + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                ", edition='" + edition + '\'' +
                ", formGenres='" + formGenres + '\'' +
                ", language='" + language + '\'' +
                ", id='" + id + '\'' +
                ", format='" + format + '\'' +
                ", instanceId='" + instanceId + '\'' +
                "{";
    }
}
