package org.kuali.ole.ingest.krms.pojo;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleCompoundProposition {
    private String description;
    private String operator;
    private List<OleSimpleProposition> simplePropositions;
    private List<OleCompoundProposition> compoundPropositions;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<OleSimpleProposition> getSimplePropositions() {
        return simplePropositions;
    }

    public void setSimplePropositions(List<OleSimpleProposition> simplePropositions) {
        this.simplePropositions = simplePropositions;
    }

    public List<OleCompoundProposition> getCompoundPropositions() {
        return compoundPropositions;
    }

    public void setCompoundPropositions(List<OleCompoundProposition> compoundPropositions) {
        this.compoundPropositions = compoundPropositions;
    }
}
