package org.kuali.ole.select.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/26/13
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESearchParams {

    private List<OLESearchCondition> searchFieldsList = new ArrayList<OLESearchCondition>();

    public List<OLESearchCondition> getSearchFieldsList() {
        return searchFieldsList;
    }

    public void setSearchFieldsList(List<OLESearchCondition> searchFieldsList) {
        this.searchFieldsList = searchFieldsList;
    }
}
