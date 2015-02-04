package org.kuali.ole.bo.serachRetrieve;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 7/5/13
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUDublin {

    private String title;
    private String creator;
    private String subject;
    private String description;
    private String publisher;
    private String date;
    private String identifier;
    private String source;
    private String language;
    private String type;
    private String rights;
    private String format;
    private String contributor;
    private String coverage;
    private String relation;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "OleSRUDublinRecord{" +
                "title='" + title + '\'' +
                ", creator='" + creator + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", publisher='" + publisher + '\'' +
                ", date='" + date + '\'' +
                ", identifier='" + identifier + '\'' +
                ", source='" + source + '\'' +
                ", language='" + language + '\'' +
                ", type='" + type + '\'' +
                ", rights='" + rights + '\'' +
                ", format='" + format + '\'' +
                ", contributor='" + contributor + '\'' +
                ", coverage='" + coverage + '\'' +
                ", relation='" + relation + '\'' +
                '}';
    }
}
