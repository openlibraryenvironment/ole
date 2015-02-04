package org.kuali.ole.bo.cql;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/10/12
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class CQLSearchClauseTag {

    private List<CQLPrefixes> prefixes;
    private String index;
    private CQLRelationTag relationTag;
    //private String relationTag;
    private String term;

    public List<CQLPrefixes> getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(List<CQLPrefixes> prefixes) {
        this.prefixes = prefixes;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public CQLRelationTag getRelationTag() {
        return relationTag;
    }

    public void setRelationTag(CQLRelationTag relationTag) {
        this.relationTag = relationTag;
    }

    /*public String getRelationTag() {
        return relationTag;
    }

    public void setRelationTag(String relationTag) {
        this.relationTag = relationTag;
    }*/

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
