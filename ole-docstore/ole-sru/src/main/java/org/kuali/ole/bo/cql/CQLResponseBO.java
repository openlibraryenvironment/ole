package org.kuali.ole.bo.cql;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CQLResponseBO {

    private CQLBooleanTag booleanTagValue;
    private CQLResponseBO leftOperand;
    private CQLResponseBO rightOperand;
    private CQLResponseBO triple;
    private CQLSearchClauseTag searchClauseTag;

    public CQLBooleanTag getBooleanTagValue() {
        return booleanTagValue;
    }

    public void setBooleanTagValue(CQLBooleanTag booleanTagValue) {
        this.booleanTagValue = booleanTagValue;
    }

/*

    public String getBooleanTagValue() {
        return booleanTagValue;
    }

    public void setBooleanTagValue(String booleanTagValue) {
        this.booleanTagValue = booleanTagValue;
    }
*/

    public CQLResponseBO getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(CQLResponseBO leftOperand) {
        this.leftOperand = leftOperand;
    }

    public CQLResponseBO getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(CQLResponseBO rightOperand) {
        this.rightOperand = rightOperand;
    }

    public CQLResponseBO getTriple() {
        return triple;
    }

    public void setTriple(CQLResponseBO triple) {
        this.triple = triple;
    }

    public CQLSearchClauseTag getSearchClauseTag() {
        return searchClauseTag;
    }

    public void setSearchClauseTag(CQLSearchClauseTag searchClauseTag) {
        this.searchClauseTag = searchClauseTag;
    }
}
