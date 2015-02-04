package org.kuali.ole.ingest.krms.pojo;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 7/20/12
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleKrmsRuleAction {
    private List<OleKrmsAction> krmsActions;
    private List<OleKrmsRule> krmsRules;

    public List<OleKrmsAction> getKrmsActions() {
        return krmsActions;
    }

    public void setKrmsActions(List<OleKrmsAction> krmsActions) {
        this.krmsActions = krmsActions;
    }

    public List<OleKrmsRule> getKrmsRules() {
        return krmsRules;
    }

    public void setKrmsRules(List<OleKrmsRule> krmsRules) {
        this.krmsRules = krmsRules;
    }
}
