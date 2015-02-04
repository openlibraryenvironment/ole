package org.kuali.ole.select.bo;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/26/13
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESearchCondition {

    public String searchBy;
    public String searchCriteria;
    public String operator;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
