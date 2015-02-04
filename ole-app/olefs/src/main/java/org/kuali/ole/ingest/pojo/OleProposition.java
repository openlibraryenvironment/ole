package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * OleProposition is a business object class for Ole Proposition
 */
public class OleProposition {
    private String propositionType;
    private List<KrmsProposition> propositions;
    private List<OleProposition> olePropositions;
    /**
     * Gets the olePropositions attribute.
     * @return  Returns the olePropositions.
     */
    public List<OleProposition> getOlePropositions() {
        return olePropositions;
    }
    /**
     * Sets the olePropositions attribute value.
     * @param olePropositions The olePropositions to set.
     */
    public void setOlePropositions(List<OleProposition> olePropositions) {
        this.olePropositions = olePropositions;
    }
    /**
     * Gets the propositions attribute.
     * @return  Returns the propositions.
     */
    public List<KrmsProposition> getPropositions() {
        return propositions;
    }
    /**
     * Sets the olePropositions attribute value.
     * @param propositions The propositions to set.
     */
    public void setPropositions(List<KrmsProposition> propositions) {
        this.propositions = propositions;
    }
    /**
     * Gets the propositionType attribute.
     * @return  Returns the propositionType.
     */
    public String getPropositionType() {
        return propositionType;
    }
    /**
     * Sets the propositionType attribute value.
     * @param propositionType The propositionType to set.
     */
    public void setPropositionType(String propositionType) {
        this.propositionType = propositionType;
    }
}
