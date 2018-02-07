/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.datadictionary.validation.result;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class AttributeValidationResult implements Serializable {

    private String attributeName;
    private Map<String, ConstraintValidationResult> constraintValidationResultMap;

    /**
     * creates a new instance with the given attribute name
     *
     * @param attributeName - the attribute name
     */
    public AttributeValidationResult(String attributeName) {
        this.attributeName = attributeName;
        this.constraintValidationResultMap = new LinkedHashMap<String, ConstraintValidationResult>();
    }

    /**
     * adds a constraint validation result
     *
     * @param constraintValidationResult - the constraint validation result to set
     */
    public void addConstraintValidationResult(ConstraintValidationResult constraintValidationResult) {
        constraintValidationResultMap.put(constraintValidationResult.getConstraintName(), constraintValidationResult);
    }

    /**
     * gets an iterator over the constraint validation results added to this class
     *
     * @return an iterator to stored constraint validation results
     */
    public Iterator<ConstraintValidationResult> iterator() {
        return constraintValidationResultMap.values().iterator();
    }

    /**
     * gets a constraint validation result with the given constraintName
     *
     * @param constraintName - a descriptive name of the current constraint processor
     * @return
     */
    protected ConstraintValidationResult getConstraintValidationResult(String constraintName) {
        ConstraintValidationResult constraintValidationResult = constraintValidationResultMap.get(constraintName);
        if (constraintValidationResult == null) {
            constraintValidationResult = new ConstraintValidationResult(constraintName);
            constraintValidationResultMap.put(constraintName, constraintValidationResult);
        }
        return constraintValidationResult;
    }

    /**
     * @return the attributeName
     */
    public String getAttributeName() {
        return this.attributeName;
    }

    /**
     * @param attributeName the attributeName to set
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /*
     private static final long serialVersionUID = 1L;

     protected String element;

     protected ErrorLevel level = ErrorLevel.OK;

     private String entryName;
     private String attributeName;
     private String errorKey;
     private String[] errorParameters;

     public AttributeValidationResult(String attributeName) {
         this.level = ErrorLevel.OK;
         this.attributeName = attributeName;
     }

     public AttributeValidationResult(String entryName, String attributeName) {
         this.level = ErrorLevel.OK;
         this.entryName = entryName;
         this.attributeName = attributeName;
     }

     public ErrorLevel getLevel() {
         return level;
     }

     public void setLevel(ErrorLevel level) {
         this.level = level;
     }

     public String getElement() {
         return element;
     }

     public void setElement(String element) {
         this.element = element;
     }


     public ErrorLevel getErrorLevel() {
         return level;
     }

     public void setError(String errorKey, String... errorParameters) {
         this.level = ErrorLevel.ERROR;
         this.errorKey = errorKey;
         this.errorParameters = errorParameters;
     }

     public boolean isOk() {
         return getErrorLevel() == ErrorLevel.OK;
     }


     public boolean isWarn() {
         return getErrorLevel() == ErrorLevel.WARN;
     }

     public boolean isError() {
         return getErrorLevel() == ErrorLevel.ERROR;
     }

     public String toString(){
         return "Entry: [" + entryName + "] Attribute: [" + attributeName + "] - " + errorKey + " data=[" + errorParameters + "]";
     }

     public String getEntryName() {
         return this.entryName;
     }

     public void setEntryName(String entryName) {
         this.entryName = entryName;
     }

     public String getAttributeName() {
         return this.attributeName;
     }

     public void setAttributeName(String attributeName) {
         this.attributeName = attributeName;
     }

     public String getErrorKey() {
         return this.errorKey;
     }

     public void setErrorKey(String errorKey) {
         this.errorKey = errorKey;
     }

     public String[] getErrorParameters() {
         return this.errorParameters;
     }
     public void setErrorParameters(String[] errorParameters) {
         this.errorParameters = errorParameters;
     }
     */

}
