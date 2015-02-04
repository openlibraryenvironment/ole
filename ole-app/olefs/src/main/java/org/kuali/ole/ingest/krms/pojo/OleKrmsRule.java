package org.kuali.ole.ingest.krms.pojo;


/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleKrmsRule {

    private String name;
    private OleKrmsProposition oleProposition;
    private OleKrmsRuleAction trueActions;
    private OleKrmsRuleAction falseActions;
    /**
     * Gets the trueActions attribute.
     * @return  Returns the trueActions.
     */
    public OleKrmsRuleAction getTrueActions() {
        return trueActions;
    }
    /**
     * Sets the trueActions attribute value.
     * @param trueActions  The trueActions to set.
     */
    public void setTrueActions(OleKrmsRuleAction trueActions) {
        this.trueActions = trueActions;
    }
    /**
     * Gets the falseActions attribute.
     * @return  Returns the falseActions.
     */
    public OleKrmsRuleAction getFalseActions() {
        return falseActions;
    }
    /**
     * Sets the falseActions attribute value.
     * @param falseActions  The falseActions to set.
     */
    public void setFalseActions(OleKrmsRuleAction falseActions) {
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

    public OleKrmsProposition getOleProposition() {
        return oleProposition;
    }

    public void setOleProposition(OleKrmsProposition oleProposition) {
        this.oleProposition = oleProposition;
    }
}
