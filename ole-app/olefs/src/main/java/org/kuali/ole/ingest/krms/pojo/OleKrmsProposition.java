package org.kuali.ole.ingest.krms.pojo;


/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleKrmsProposition {
    private String type;
    private OleSimpleProposition simpleProposition;
    private OleCompoundProposition compoundProposition;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OleSimpleProposition getSimpleProposition() {
        return simpleProposition;
    }

    public void setSimpleProposition(OleSimpleProposition simpleProposition) {
        this.simpleProposition = simpleProposition;
    }

    public OleCompoundProposition getCompoundProposition() {
        return compoundProposition;
    }

    public void setCompoundProposition(OleCompoundProposition compoundProposition) {
        this.compoundProposition = compoundProposition;
    }
}
