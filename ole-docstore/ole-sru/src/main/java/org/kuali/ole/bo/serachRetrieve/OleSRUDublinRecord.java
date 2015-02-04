package org.kuali.ole.bo.serachRetrieve;


import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 7/6/13
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUDublinRecord {

    public static final String TITLE = "title";
    public static final String CREATOR = "creator";
    public static final String SUBJECT = "subject";
    public static final String DESCRIPTION = "description";
    public static final String PUBLISHER = "publisher";
    public static final String DATE = "date";
    public static final String IDENTIFIER = "identifier";
    public static final String SOURCE = "source";
    public static final String LANGUAGE = "language";
    public static final String TYPE = "type";
    public static final String RIGHTS = "rights";
    public static final String FORMAT = "format";
    public static final String CONTRIBUTOR = "contributor";
    public static final String COVERAGE = "coverage";
    public static final String RELATION = "relation";

    private List<OleSRUDublin> records = new ArrayList<OleSRUDublin>();

    /**
     * Method to add, set or update tag value.
     *
     * @param tagName
     * @param value
     */
    public void put(String tagName, String value) {
        OleSRUDublin OleSRUDublin = new org.kuali.ole.bo.serachRetrieve.OleSRUDublin();
        if (tagName.equalsIgnoreCase(TITLE)) {
            OleSRUDublin.setTitle(value);
        } else if (tagName.equalsIgnoreCase(DATE)) {
            OleSRUDublin.setDate(value);
        } else if (tagName.equalsIgnoreCase(CREATOR)) {
            OleSRUDublin.setCreator(value);
        } else if (tagName.equalsIgnoreCase(SUBJECT)) {
            OleSRUDublin.setSubject(value);
        } else if (tagName.equalsIgnoreCase(DESCRIPTION)) {
            OleSRUDublin.setDescription(value);
        } else if (tagName.equalsIgnoreCase(PUBLISHER)) {
            OleSRUDublin.setPublisher(value);
        } else if (tagName.equalsIgnoreCase(IDENTIFIER)) {
            OleSRUDublin.setIdentifier(value);
        } else if (tagName.equalsIgnoreCase(SOURCE)) {
            OleSRUDublin.setSource(value);
        } else if (tagName.equalsIgnoreCase(LANGUAGE)) {
            OleSRUDublin.setLanguage(value);
        } else if (tagName.equalsIgnoreCase(TYPE)) {
            OleSRUDublin.setType(value);
        } else if (tagName.equalsIgnoreCase(RIGHTS)) {
            OleSRUDublin.setRights(value);
        } else if (tagName.equalsIgnoreCase(FORMAT)) {
            OleSRUDublin.setFormat(value);
        } else if (tagName.equalsIgnoreCase(CONTRIBUTOR)) {
            OleSRUDublin.setContributor(value);
        } else if (tagName.equalsIgnoreCase(COVERAGE)) {
            OleSRUDublin.setCoverage(value);
        } else if (tagName.equalsIgnoreCase(RELATION)) {
            OleSRUDublin.setRelation(value);
        }
        records.add(OleSRUDublin);
    }

    /**
     * Method to return all tags
     *
     * @return
     */
    public List<OleSRUDublin> getAllTags() {
        return records;
    }

    @Override
    public String toString() {
        return "[OaiDcDoc: " + records + "]";
    }

}
