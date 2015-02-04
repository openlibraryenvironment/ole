package org.kuali.ole.ingest.pojo;

import java.util.List;

/**
 * KrmsProposition is a business object class for Krms Proposition
 */
public class KrmsProposition {
    private String term;
    private String type;
    private String operator;

    private String constant;
    private String function;
    private String operatorCode;

    /**
     * Gets the term attribute.
     * @return  Returns the term.
     */
    public String getTerm() {
        return term;
    }
    /**
     * Sets the term attribute value.
     * @param term  The term to set.
     */
    public void setTerm(String term) {
        this.term = term;
    }
    /**
     * Gets the type attribute.
     * @return  Returns the type.
     */
    public String getType() {
        return type;
    }
    /**
     * Sets the type attribute value.
     * @param type  The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Gets the operator attribute.
     * @return  Returns the operator.
     */
    public String getOperator() {
        return operator;
    }
    /**
     * Sets the operator attribute value.
     * @param operator  The operator to set.
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }
    /**
     * Gets the constant attribute.
     * @return  Returns the constant.
     */
    public String getConstant() {
        return constant;
    }
    /**
     * Sets the constant attribute value.
     * @param constant  The constant to set.
     */
    public void setConstant(String constant) {
        this.constant = constant;
    }
    /**
     * Gets the function attribute.
     * @return  Returns the function.
     */
    public String getFunction() {
        return function;
    }
    /**
     * Sets the function attribute value.
     * @param function  The function to set.
     */
    public void setFunction(String function) {
        this.function = function;
    }
    /**
     * Gets the operatorCode attribute.
     * @return  Returns the operatorCode.
     */
    public String getOperatorCode() {
        return operatorCode;
    }
    /**
     * Sets the operatorCode attribute value.
     * @param operatorCode  The operatorCode to set.
     */
    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }


}
