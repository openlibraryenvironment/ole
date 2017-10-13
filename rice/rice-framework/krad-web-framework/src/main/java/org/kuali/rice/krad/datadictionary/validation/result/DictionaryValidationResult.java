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

import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.ValidationUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DictionaryValidationResult holds dictionary validation results
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DictionaryValidationResult implements Iterable<ConstraintValidationResult> {

    private Map<String, EntryValidationResult> entryValidationResultMap;
    private ErrorLevel errorLevel;

    private int numberOfErrors;
    private int numberOfWarnings;

    private Iterator<ConstraintValidationResult> iterator;

    /**
     * default constructor
     */
    public DictionaryValidationResult() {
        this.entryValidationResultMap = new LinkedHashMap<String, EntryValidationResult>();
        this.errorLevel = ErrorLevel.ERROR;
        this.numberOfErrors = 0;
        this.numberOfWarnings = 0;
    }

    /**
     * adds the result of a constraint validation performed on an attribute
     *
     * @param attributeValueReader - provides access to the attribute being validated
     * @param constraintValidationResult - the result of processing a constraint
     */
    public void addConstraintValidationResult(AttributeValueReader attributeValueReader,
            ConstraintValidationResult constraintValidationResult) {

        // Don't bother to store this if the error level of the constraint validation result is lower than the level this dictionary validation result is tracking
        if (constraintValidationResult.getStatus().getLevel() < errorLevel.getLevel()) {
            return;
        }

        switch (constraintValidationResult.getStatus()) {
            case ERROR:
                numberOfErrors++;
                break;
            case WARN:
                numberOfWarnings++;
                break;
            default:
                // Do nothing
        }

        // Give the constraint a chance to override the entry and attribute name - important if the attribute name is not the same as the one in the attribute value reader!
        String entryName = constraintValidationResult.getEntryName();
        String attributeName = constraintValidationResult.getAttributeName();
        String attributePath = constraintValidationResult.getAttributePath();

        if (entryName == null) {
            entryName = attributeValueReader.getEntryName();
        }

        if (attributeName == null) {
            attributeName = attributeValueReader.getAttributeName();
        }

        if (attributePath == null) {
            attributePath = attributeValueReader.getPath();
        }

        constraintValidationResult.setEntryName(entryName);
        constraintValidationResult.setAttributeName(attributeName);
        constraintValidationResult.setAttributePath(attributePath);

        String entryKey = getEntryValidationResultKey(entryName, attributePath);
        getEntryValidationResult(entryKey).getAttributeValidationResult(attributeName).addConstraintValidationResult(
                constraintValidationResult);
    }

    /**
     * provides information used to display error messages to the user concerning a constraint validation
     *
     * @param attributeValueReader - provides access to the attribute being validated
     * @param constraintName - a descriptive name of the current constraint processor
     * @param errorKey - a key used to fetch an informational message to show the user
     * @param errorParameters - parameters to substitute into the informational message
     * @return a constraint validation result encompassing the information provided
     */
    public ConstraintValidationResult addError(AttributeValueReader attributeValueReader, String constraintName,
            String errorKey, String... errorParameters) {
        ConstraintValidationResult constraintValidationResult = getConstraintValidationResult(
                attributeValueReader.getEntryName(), attributeValueReader.getAttributeName(),
                attributeValueReader.getPath(), constraintName);
        constraintValidationResult.setError(errorKey, errorParameters);
        numberOfErrors++;
        return constraintValidationResult;
    }

    /**
     * provides information used to display error messages to the user concerning a constraint validation
     *
     * @param constraintLabelKey - a key used to fetch an information message to show the user
     * @param attributeValueReader - provides access to the attribute being validated
     * @param constraintName - a descriptive name of the current constraint processor
     * @param errorKey - a key used to fetch an error message to show the user
     * @param errorParameters - parameters to substitute into the error message
     * @return a constraint validation result encompassing the information provided
     */
    public ConstraintValidationResult addError(String constraintLabelKey, AttributeValueReader attributeValueReader,
            String constraintName, String errorKey, String... errorParameters) {
        ConstraintValidationResult constraintValidationResult = getConstraintValidationResult(
                attributeValueReader.getEntryName(), attributeValueReader.getAttributeName(),
                attributeValueReader.getPath(), constraintName);
        constraintValidationResult.setError(errorKey, errorParameters);
        constraintValidationResult.setConstraintLabelKey(constraintLabelKey);
        numberOfErrors++;
        return constraintValidationResult;
    }

    /**
     * provides information used to display warning messages to the user concerning a constraint validation
     *
     * @param attributeValueReader - provides access to the attribute being validated
     * @param constraintName - a descriptive name of the current constraint processor
     * @param errorKey - a key used to fetch a warning message to show the user
     * @param errorParameters - parameters to substitute into the warning message
     * @return a constraint validation result encompassing the information provided
     */
    public ConstraintValidationResult addWarning(AttributeValueReader attributeValueReader, String constraintName,
            String errorKey, String... errorParameters) {
        if (errorLevel.getLevel() > ErrorLevel.WARN.getLevel()) {
            return new ConstraintValidationResult(constraintName, ErrorLevel.WARN);
        }

        ConstraintValidationResult constraintValidationResult = getConstraintValidationResult(
                attributeValueReader.getEntryName(), attributeValueReader.getAttributeName(),
                attributeValueReader.getPath(), constraintName);
        constraintValidationResult.setWarning(errorKey, errorParameters);
        numberOfWarnings++;
        return constraintValidationResult;
    }

    /**
     * indicates that a constraint validation has succeeded
     *
     * @param attributeValueReader - provides access to the attribute being validated
     * @param constraintName - a descriptive name of the current constraint processor
     * @return a constraint validation result encompassing the information provided
     */
    public ConstraintValidationResult addSuccess(AttributeValueReader attributeValueReader, String constraintName) {
        if (errorLevel.getLevel() > ErrorLevel.OK.getLevel()) {
            return new ConstraintValidationResult(constraintName, ErrorLevel.OK);
        }

        return getConstraintValidationResult(attributeValueReader.getEntryName(),
                attributeValueReader.getAttributeName(), attributeValueReader.getPath(), constraintName);
    }

    /**
     * indicates that a constraint validation has been skipped
     *
     * @param attributeValueReader - provides access to the attribute being validated
     * @param constraintName - a descriptive name of the current constraint processor
     * @return a constraint validation result encompassing the information provided
     */
    public ConstraintValidationResult addSkipped(AttributeValueReader attributeValueReader, String constraintName) {
        if (errorLevel.getLevel() > ErrorLevel.OK.getLevel()) {
            return new ConstraintValidationResult(constraintName, ErrorLevel.INAPPLICABLE);
        }

        ConstraintValidationResult constraintValidationResult = getConstraintValidationResult(
                attributeValueReader.getEntryName(), attributeValueReader.getAttributeName(),
                attributeValueReader.getPath(), constraintName);
        constraintValidationResult.setStatus(ErrorLevel.INAPPLICABLE);
        return constraintValidationResult;
    }

    /**
     * indicates that a constraint validation processing has been skipped
     *
     * @param attributeValueReader - provides access to the attribute being validated
     * @param constraintName - a descriptive name of the current constraint processor
     * @return a constraint validation result encompassing the information provided
     */
    public ConstraintValidationResult addNoConstraint(AttributeValueReader attributeValueReader,
            String constraintName) {
        if (errorLevel.getLevel() > ErrorLevel.OK.getLevel()) {
            return new ConstraintValidationResult(constraintName, ErrorLevel.NOCONSTRAINT);
        }

        ConstraintValidationResult constraintValidationResult = getConstraintValidationResult(
                attributeValueReader.getEntryName(), attributeValueReader.getAttributeName(),
                attributeValueReader.getPath(), constraintName);
        constraintValidationResult.setStatus(ErrorLevel.NOCONSTRAINT);
        return constraintValidationResult;
    }

    /**
     * gets an iterator over the various {@code ConstraintValidationResult}'s contained in this class
     *
     * @return an iterator
     */
    public Iterator<ConstraintValidationResult> iterator() {

        iterator = new Iterator<ConstraintValidationResult>() {

            private Iterator<EntryValidationResult> entryIterator;
            private Iterator<AttributeValidationResult> attributeIterator;
            private Iterator<ConstraintValidationResult> constraintIterator;

            @Override
            public boolean hasNext() {
                Iterator<ConstraintValidationResult> currentConstraintIterator = getCurrentConstraintIterator();
                return currentConstraintIterator != null && currentConstraintIterator.hasNext();
            }

            @Override
            public ConstraintValidationResult next() {
                Iterator<ConstraintValidationResult> currentConstraintIterator = getCurrentConstraintIterator();
                return currentConstraintIterator != null ? currentConstraintIterator.next() : null;
            }

            @Override
            public void remove() {
                throw new RuntimeException("Can't remove from this iterator!");
            }

            private Iterator<ConstraintValidationResult> getCurrentConstraintIterator() {
                if (constraintIterator == null || constraintIterator.hasNext() == false) {
                    Iterator<AttributeValidationResult> currentAttributeIterator = getCurrentAttributeIterator();
                    if (currentAttributeIterator != null && currentAttributeIterator.hasNext()) {
                        AttributeValidationResult currentAttributeValidationResult = currentAttributeIterator.next();
                        constraintIterator = currentAttributeValidationResult.iterator();
                    }
                }
                return constraintIterator;
            }

            private Iterator<AttributeValidationResult> getCurrentAttributeIterator() {
                if (attributeIterator == null || attributeIterator.hasNext() == false) {
                    Iterator<EntryValidationResult> currentEntryIterator = getCurrentEntryIterator();
                    if (currentEntryIterator != null && currentEntryIterator.hasNext()) {
                        EntryValidationResult currentEntryValidationResult = currentEntryIterator.next();
                        attributeIterator = currentEntryValidationResult.iterator();
                    }
                }
                return attributeIterator;
            }

            private Iterator<EntryValidationResult> getCurrentEntryIterator() {
                if (entryIterator == null) // || entryIterator.hasNext() == false)
                {
                    entryIterator = entryValidationResultMap.values().iterator();
                }
                return entryIterator;
            }

        };

        return iterator;
    }

    /**
     * gets an entry validation result for the given {@code entryName}
     *
     * @param entryName - the name that the data dictionary uses to store metadata about the attribute
     * @return the existing {@code EntryValidationResult} for the given {@code entryName} or creates a new one if
     *         absent
     */
    protected EntryValidationResult getEntryValidationResult(String entryName) {
        EntryValidationResult entryValidationResult = entryValidationResultMap.get(entryName);
        if (entryValidationResult == null) {
            entryValidationResult = new EntryValidationResult(entryName);
            entryValidationResultMap.put(entryName, entryValidationResult);
        }
        return entryValidationResult;
    }

    /**
     * composes a {@code ConstraintValidationResult} from the parameters given
     *
     * @param entryName - the name that the data dictionary uses to store metadata about the attribute
     * @param attributeName - the attribute name
     * @param attributePath - a string representation of specifically which attribute (at some depth) is being accessed
     * @param constraintName - a descriptive name of the current constraint processor
     * @return
     */
    private ConstraintValidationResult getConstraintValidationResult(String entryName, String attributeName,
            String attributePath, String constraintName) {
        String entryKey = getEntryValidationResultKey(entryName, attributePath);
        ConstraintValidationResult constraintValidationResult = getEntryValidationResult(entryKey)
                .getAttributeValidationResult(attributeName).getConstraintValidationResult(constraintName);
        constraintValidationResult.setEntryName(entryName);
        constraintValidationResult.setAttributeName(attributeName);
        constraintValidationResult.setAttributePath(attributePath);
        return constraintValidationResult;
    }

    /**
     * gets the key to the {@link EntryValidationResult} entry in the EntryValidationResultMap
     *
     * <p>Most cases entry key will be the entryName, unless the attribute is part of a collection,
     * in which case entry key will be suffixed with index of attribute's parent item.</p>
     *
     * @param entryName - the name that the data dictionary uses to store metadata about the attribute
     * @param attributePath - a string representation of specifically which attribute (at some depth) is being accessed
     * @return a key used to fetch an associated {@code EntryValidationResult}
     */
    private String getEntryValidationResultKey(String entryName, String attributePath) {
        if (attributePath.contains("[")) {
            return entryName + "[" + ValidationUtils.getLastPathIndex(attributePath) + "]";
        }
        return entryName;
    }

    /**
     * @return the errorLevel
     */
    public ErrorLevel getErrorLevel() {
        return this.errorLevel;
    }

    /**
     * @param errorLevel the errorLevel to set
     */
    public void setErrorLevel(ErrorLevel errorLevel) {
        this.errorLevel = errorLevel;
    }

    /**
     * @return the numberOfErrors
     */
    public int getNumberOfErrors() {
        return this.numberOfErrors;
    }

    /**
     * @return the numberOfWarnings
     */
    public int getNumberOfWarnings() {
        return this.numberOfWarnings;
    }

}
