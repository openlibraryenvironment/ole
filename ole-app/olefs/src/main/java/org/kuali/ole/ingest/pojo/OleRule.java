package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * OleRule is a business object class for Ole Rule
 */
public class OleRule {
    private String name;
    private String term;
    private String matchType;
    private String docType;
    private MatchPoint incomingField;
    private MatchPoint existingField;
    private List<OleAction> trueActions;
    private List<OleAction> falseActions;
    /**
     * Gets the name attribute.
     * @return  Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name attribute value.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Gets the term attribute.
     * @return  Returns the term.
     */
    public String getTerm() {
        return term;
    }
    /**
     * Sets the term attribute value.
     * @param term The term to set.
     */
    public void setTerm(String term) {
        this.term = term;
    }
    /**
     * Gets the matchType attribute.
     * @return  Returns the matchType.
     */
    public String getMatchType() {
        return matchType;
    }
    /**
     * Gets the docType attribute.
     * @return  Returns the docType.
     */
    public String getDocType() {
        return docType;
    }
    /**
     * Sets the docType attribute value.
     * @param docType The docType to set.
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }
    /**
     * Gets the incomingField attribute.
     * @return  Returns the incomingField.
     */
    public MatchPoint getIncomingField() {
        return incomingField;
    }
    /**
     * Sets the incomingField attribute value.
     * @param incomingField The incomingField to set.
     */
    public void setIncomingField(MatchPoint incomingField) {
        this.incomingField = incomingField;
    }
    /**
     * Gets the existingField attribute.
     * @return  Returns the existingField.
     */
    public MatchPoint getExistingField() {
        return existingField;
    }
    /**
     * Sets the existingField attribute value.
     * @param existingField The existingField to set.
     */
    public void setExistingField(MatchPoint existingField) {
        this.existingField = existingField;
    }
    /**
     * Sets the matchType attribute value.
     * @param matchType The matchType to set.
     */
    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }
    /**
     * Gets the trueActions attribute.
     * @return  Returns the trueActions.
     */
    public List<OleAction> getTrueActions() {
        return trueActions;
    }
    /**
     * Sets the trueActions attribute value.
     * @param trueActions The trueActions to set.
     */
    public void setTrueActions(List<OleAction> trueActions) {
        this.trueActions = trueActions;
    }
    /**
     * Gets the falseActions attribute.
     * @return  Returns the falseActions.
     */
    public List<OleAction> getFalseActions() {
        return falseActions;
    }
    /**
     * Sets the falseActions attribute value.
     * @param falseActions The falseActions to set.
     */
    public void setFalseActions(List<OleAction> falseActions) {
        this.falseActions = falseActions;
    }
}
