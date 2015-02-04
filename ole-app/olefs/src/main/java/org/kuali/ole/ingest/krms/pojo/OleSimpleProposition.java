package org.kuali.ole.ingest.krms.pojo;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 8/22/12
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleSimpleProposition {
    private List<OleTerm> terms;
    private String operator;
    private String description;
    private List<OleValue> values;
    private String function;


    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public List<OleTerm> getTerms() {
        return terms;
    }

    public void setTerms(List<OleTerm> terms) {
        this.terms = terms;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<OleValue> getValues() {
        return values;
    }

    public void setValues(List<OleValue> values) {
        this.values = values;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
