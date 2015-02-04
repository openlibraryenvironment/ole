package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * KrmsRule is a business object class for Krms Rule
 */
public class KrmsRule {

    private String name;
    private OleProposition compoundProposition;
    private KrmsProposition proposition;
    private List<KrmsAction> trueActions;
    private List<KrmsAction> falseActions;
    /**
     * Gets the trueActions attribute.
     * @return  Returns the trueActions.
     */
    public List<KrmsAction> getTrueActions() {
        return trueActions;
    }
    /**
     * Sets the trueActions attribute value.
     * @param trueActions  The trueActions to set.
     */
    public void setTrueActions(List<KrmsAction> trueActions) {
        this.trueActions = trueActions;
    }
    /**
     * Gets the falseActions attribute.
     * @return  Returns the falseActions.
     */
    public List<KrmsAction> getFalseActions() {
        return falseActions;
    }
    /**
     * Sets the falseActions attribute value.
     * @param falseActions  The falseActions to set.
     */
    public void setFalseActions(List<KrmsAction> falseActions) {
        this.falseActions = falseActions;
    }
    /**
     * Gets the name attribute.
     * @return  Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name attribute value.
     * @param name  The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Gets the compoundProposition attribute.
     * @return  Returns the compoundProposition.
     */
    public OleProposition getCompoundProposition() {
        return compoundProposition;
    }
    /**
     * Sets the compoundProposition attribute value.
     * @param compoundProposition  The compoundProposition to set.
     */
    public void setCompoundProposition(OleProposition compoundProposition) {
        this.compoundProposition = compoundProposition;
    }
    /**
     * Gets the proposition attribute.
     * @return  Returns the proposition.
     */
    public KrmsProposition getProposition() {
        return proposition;
    }
    /**
     * Sets the proposition attribute value.
     * @param proposition  The proposition to set.
     */
    public void setProposition(KrmsProposition proposition) {
        this.proposition = proposition;
    }
}
