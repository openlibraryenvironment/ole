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
package org.kuali.rice.krad.datadictionary.validation.constraint;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;

import java.util.List;

/**
 * CaseConstraint is imposed only when a certain condition is met
 *
 * <p>For example, if the country attribute value is "USA",
 * then a prerequisite constraint may be imposed that the 'State' attribute is non-null.</p>
 *
 * <p>
 * This class is a direct copy of one that was in Kuali Student.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 1.1
 */
@BeanTag(name = "caseConstriant-bean", parent = "CaseConstraint")
public class CaseConstraint extends BaseConstraint {

    protected String propertyName;
    protected String operator;
    protected boolean caseSensitive;

    protected List<WhenConstraint> whenConstraint;

    /**
     * get the {@code WhenConstraint}'s defined by this case constraint
     *
     * @return a list of constraints, null if not initialized
     */
    @BeanTagAttribute(name = "whenConstraint", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<WhenConstraint> getWhenConstraint() {
        return whenConstraint;
    }

    /**
     * sets the {@code WhenConstraint}'s defined by this case constraint
     *
     * @param whenConstraint - the list of constraints
     */
    public void setWhenConstraint(List<WhenConstraint> whenConstraint) {
        this.whenConstraint = whenConstraint;
    }

    /**
     * gets the property name for the attribute to which the case constraint is applied to
     *
     * @return the property name
     */
    @BeanTagAttribute(name = "propertyName")
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * setter for property name
     *
     * @param propertyName a valid property name
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * specifies the kind of relationship to be checked between the actual value and the ones defined in the {@link
     * #getWhenConstraint()}
     *
     * @return an operator name
     * @see org.kuali.rice.krad.uif.UifConstants.CaseConstraintOperators
     */
    @BeanTagAttribute(name = "operator")
    public String getOperator() {
        return operator;
    }

    /**
     * setter for the operator
     *
     * @param operator
     * @see org.kuali.rice.krad.uif.UifConstants.CaseConstraintOperators
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * checks whether string comparison will be carried out in a case sensitive fashion
     *
     * @return true if string comparison is case sensitive, false if not
     */
    @BeanTagAttribute(name = "caseSensitive")
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * setter for case sensitive
     *
     * @param caseSensitive - the case sensitive value to set
     */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    /**
     * Validates different requirements of component compiling a series of reports detailing information on errors
     * found in the component.  Used by the RiceDictionaryValidator.
     *
     * @param tracer Record of component's location
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean("CaseConstraint", getMessageKey());

        if (getWhenConstraint() == null) {
            String currentValues[] = {"whenCaseConstraint = " + getWhenConstraint()};
            tracer.createWarning("WhenCaseConstraints should at least have 1 item", currentValues);
        } else {
            if (getWhenConstraint().size() == 0) {
                String currentValues[] = {"whenCaseConstraint.size() = " + getWhenConstraint().size()};
                tracer.createError("WhenCaseConstraints should at least have 1 item", currentValues);
            } else {
                for (int i = 0; i < getWhenConstraint().size(); i++) {
                    getWhenConstraint().get(i).completeValidation(tracer.getCopy());
                }
            }
        }
    }
}