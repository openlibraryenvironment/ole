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
package org.kuali.rice.krad.datadictionary.validation.processor;

import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.krad.datadictionary.DataDictionaryEntry;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.DictionaryObjectAttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ValidationUtils;
import org.kuali.rice.krad.datadictionary.validation.capability.Constrainable;
import org.kuali.rice.krad.datadictionary.validation.capability.HierarchicallyConstrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.CaseConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.DataTypeConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.WhenConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.ProcessorResult;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.ArrayList;
import java.util.List;

/**
 * CaseConstraintProcessor processes 'case constraints', which are constraints that are imposed only in specific cases
 *
 * <p>For example, when a value is equal to some constant, or greater than some limit.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CaseConstraintProcessor extends MandatoryElementConstraintProcessor<CaseConstraint> {

    private static final String CONSTRAINT_NAME = "case constraint";

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#process(org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult,
     *      Object, org.kuali.rice.krad.datadictionary.validation.constraint.Constraint,
     *      org.kuali.rice.krad.datadictionary.validation.AttributeValueReader)
     */
    @Override
    public ProcessorResult process(DictionaryValidationResult result, Object value, CaseConstraint caseConstraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {

        // Don't process this constraint if it's null
        if (null == caseConstraint) {
            return new ProcessorResult(result.addNoConstraint(attributeValueReader, CONSTRAINT_NAME));
        }
        AttributeValueReader constraintAttributeReader = attributeValueReader.clone();

        String operator = (ValidationUtils.hasText(caseConstraint.getOperator())) ? caseConstraint.getOperator() :
                "EQUALS";
        AttributeValueReader fieldPathReader = (ValidationUtils.hasText(caseConstraint.getPropertyName())) ?
                getChildAttributeValueReader(caseConstraint.getPropertyName(), attributeValueReader) :
                attributeValueReader;

        Constrainable caseField = (null != fieldPathReader) ? fieldPathReader.getDefinition(
                fieldPathReader.getAttributeName()) : null;
        Object fieldValue = (null != fieldPathReader) ? fieldPathReader.getValue(fieldPathReader.getAttributeName()) :
                value;
        DataType fieldDataType = (null != caseField && caseField instanceof DataTypeConstraint) ?
                ((DataTypeConstraint) caseField).getDataType() : null;

        // Default to a string comparison
        if (fieldDataType == null) {
            fieldDataType = DataType.STRING;
        }

        // If fieldValue is null then skip Case check
        if (null == fieldValue) {
            // FIXME: not sure if the definition and attribute value reader should change under this case
            return new ProcessorResult(result.addSkipped(attributeValueReader, CONSTRAINT_NAME), caseField,
                    fieldPathReader);
        }

        List<Constraint> constraints = new ArrayList<Constraint>();
        // Extract value for field Key
        for (WhenConstraint wc : caseConstraint.getWhenConstraint()) {
            evaluateWhenConstraint(fieldValue, fieldDataType, operator, caseConstraint, wc, attributeValueReader,
                    constraints);
        }
        if (!constraints.isEmpty()) {
            return new ProcessorResult(result.addSuccess(attributeValueReader, CONSTRAINT_NAME), null,
                    constraintAttributeReader, constraints);
        }

        // Assuming that not finding any case constraints is equivalent to 'skipping' the constraint
        return new ProcessorResult(result.addSkipped(attributeValueReader, CONSTRAINT_NAME));
    }

    /**
     * evaluates the provided {@link WhenConstraint}
     *
     * @param fieldValue - the value of the field
     * @param fieldDataType - the data type of the field which caseConstraint's propertyName refers to
     * @param operator - the relationship to check between the fieldValue and the value provided in the whenConstraint
     * @param caseConstraint - the case constraint containing the provided whenConstraint
     * @param wc - the whenConstraint to evaluate
     * @param attributeValueReader - provides access to the attribute being validated
     * @param constraints - the constraints to populate as discovered in the provided whenConstraint
     */
    private void evaluateWhenConstraint(Object fieldValue, DataType fieldDataType, String operator,
            CaseConstraint caseConstraint, WhenConstraint wc, AttributeValueReader attributeValueReader,
            List<Constraint> constraints) {
        if (ValidationUtils.hasText(wc.getValuePath())) {
            Object whenValue = null;

            //String originalName = attributeValueReader.getAttributeName();
            AttributeValueReader whenValueReader = getChildAttributeValueReader(wc.getValuePath(),
                    attributeValueReader);
            whenValue = whenValueReader.getValue(whenValueReader.getAttributeName());

            if (ValidationUtils.compareValues(fieldValue, whenValue, fieldDataType, operator,
                    caseConstraint.isCaseSensitive(), dateTimeService) && null != wc.getConstraint()) {
                constraints.add(wc.getConstraint());
            }
            //whenValueReader.setAttributeName(originalName);
        } else {
            List<Object> whenValueList = wc.getValues();

            for (Object whenValue : whenValueList) {
                if (ValidationUtils.compareValues(fieldValue, whenValue, fieldDataType, operator,
                        caseConstraint.isCaseSensitive(), dateTimeService) && null != wc.getConstraint()) {
                    constraints.add(wc.getConstraint());
                    break;
                }
            }
        }
    }

    @Override
    public String getName() {
        return CONSTRAINT_NAME;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#getConstraintType()
     */
    @Override
    public Class<? extends Constraint> getConstraintType() {
        return CaseConstraint.class;
    }

    /**
     * provides access to the attribute specified in a whenConstraint
     *
     * @param key - a string representation of specifically which attribute (at some depth) is being accessed
     * @param attributeValueReader - provides access to the attribute being validated
     * @return an attribute value reader for the path represented by the key param
     * @throws AttributeValidationException
     */
    private AttributeValueReader getChildAttributeValueReader(String key,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {
        String[] lookupPathTokens = ValidationUtils.getPathTokens(key);

        AttributeValueReader localAttributeValueReader = attributeValueReader.clone();
        for (int i = 0; i < lookupPathTokens.length; i++) {
            for (Constrainable definition : localAttributeValueReader.getDefinitions()) {
                String attributeName = definition.getName();
                if (attributeName.equals(lookupPathTokens[i])) {
                    if (i == lookupPathTokens.length - 1) {
                        localAttributeValueReader.setAttributeName(attributeName);
                        return localAttributeValueReader;
                    }
                    if (definition instanceof HierarchicallyConstrainable) {
                        String childEntryName = ((HierarchicallyConstrainable) definition).getChildEntryName();
                        DataDictionaryEntry entry = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary()
                                .getDictionaryObjectEntry(childEntryName);
                        Object value = attributeValueReader.getValue(attributeName);
                        attributeValueReader.setAttributeName(attributeName);
                        String attributePath = attributeValueReader.getPath();
                        localAttributeValueReader = new DictionaryObjectAttributeValueReader(value, childEntryName,
                                entry, attributePath);
                    }
                    break;
                }
            }
        }
        return null;
    }

}
