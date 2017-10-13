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
import org.kuali.rice.krad.datadictionary.validator.ErrorReport;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements the required accessor for label keys. This provides a convenient base class
 * from which other constraints can be derived.
 *
 * Only BaseConstraints can have state validation.
 *
 * This class is a direct copy of one that was in Kuali Student.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 1.1
 */
@BeanTag(name = "constraint-bean")
public class BaseConstraint implements Constraint {
    protected String messageNamespaceCode;
    protected String messageComponentCode;
    protected String messageKey;

    protected Boolean applyClientSide;

    protected List<String> validationMessageParams;
    protected List<String> states;
    protected List<? extends BaseConstraint> constraintStateOverrides;

    public BaseConstraint() {
        applyClientSide = Boolean.valueOf(true);
    }

    /**
     * Namespace code (often an application or module code) the constraint failure message is associated with
     *
     * <p>
     * Used with the component code and error key for retrieving the constraint. If null,
     * the default namespace code will be used
     * </p>
     *
     * @return String constraint message namespace code
     */
    @BeanTagAttribute(name = "messageNamespaceCode")
    public String getMessageNamespaceCode() {
        return messageNamespaceCode;
    }

    /**
     * Setter for the constraint message associated namespace code
     *
     * @param messageNamespaceCode
     */
    public void setMessageNamespaceCode(String messageNamespaceCode) {
        this.messageNamespaceCode = messageNamespaceCode;
    }

    /**
     * A code within the namespace that identifies a component or group the constraint message is associated with
     *
     * <p>
     * Used with the namespace and error key for retrieving the constraint text. If null,
     * the default component code will be used
     * </p>
     *
     * @return String message component code
     */
    @BeanTagAttribute(name = "messageComponentCode")
    public String getMessageComponentCode() {
        return messageComponentCode;
    }

    /**
     * Setter for the constraint message associated component code
     *
     * @param messageComponentCode
     */
    public void setMessageComponentCode(String messageComponentCode) {
        this.messageComponentCode = messageComponentCode;
    }

    /**
     * A key that is used to retrieve the constraint message text (used with the namespace and component
     * code if specified)
     *
     * @return String message key
     */
    @BeanTagAttribute(name = "messageKey")
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Setter for the constraint message key
     *
     * @param messageKey
     */
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    /**
     * If this is true, the constraint should be applied on the client side when the user interacts with
     * a field - if this constraint can be interpreted for client side use. Default is true.
     *
     * @return the applyClientSide
     */
    @BeanTagAttribute(name = "applyClientSide")
    public Boolean getApplyClientSide() {
        return this.applyClientSide;
    }

    /**
     * @param applyClientSide the applyClientSide to set
     */
    public void setApplyClientSide(Boolean applyClientSide) {
        this.applyClientSide = applyClientSide;
    }

    /**
     * Parameters to be used in the string retrieved by this constraint's messageKey, ordered by number of
     * the param
     *
     * @return the validationMessageParams
     */
    @BeanTagAttribute(name = "validationMessageParams", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getValidationMessageParams() {
        return this.validationMessageParams;
    }

    /**
     * Parameters to be used in the string retrieved by this constraint's messageKey, ordered by number of
     * the param
     *
     * @return the validationMessageParams
     */
    public String[] getValidationMessageParamsArray() {
        if (this.getValidationMessageParams() != null) {
            return this.getValidationMessageParams().toArray(new String[this.getValidationMessageParams().size()]);
        } else {
            return null;
        }

    }

    /**
     * @param validationMessageParams the validationMessageParams to set
     */
    public void setValidationMessageParams(List<String> validationMessageParams) {
        this.validationMessageParams = validationMessageParams;
    }

    /**
     * A list of states to apply this constraint for, this will effect when a constraint
     * is applied.
     *
     * <p>Each state this constraint is applied for needs to be declared with few additional options:
     * <ul>
     * <li>if NO states are defined for this constraint, this constraint is applied for ALL states</li>
     * <li>if a state is defined with a + symbol, example "state+", then this constraint will be applied for that state
     * and ALL following states</li>
     * <li>if a state is defined as a range with ">", example "state1>state6", then this constraint will be applied for
     * all
     * states from state1 to state6 </li>
     * </ul>
     * These can be mixed and matched, as appropriate, though states using a + symbol should always be the last
     * item of a list (as they imply this state and everything else after).</p>
     *
     * <p>Example state list may be: ["state1", "state3>state5", "state6+"].  In this example, note that this
     * constraint
     * is never applied to "state2" (assuming these example states represent a state order by number)</p>
     *
     * @return the states to apply the constraint on, an empty list if the constraint is applied for all states
     */
    @BeanTagAttribute(name = "states", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getStates() {
        if (states == null) {
            states = new ArrayList<String>();
        }
        return states;
    }

    /**
     * Set the states for this contraint to be applied on
     *
     * @param states
     */
    public void setStates(List<String> states) {
        this.states = states;
    }

    /**
     * Get the list of constraintStateOverrides which represent constraints that will replace THIS constraint
     * when their state is matched during validation.
     * Because of this, constraints added to this list MUST have their states defined.
     *
     * <p>ConstraintStateOverrides always take precedence over this
     * constraint if they apply to the state being evaluated during validation.  These settings have no effect if
     * there is no stateMapping represented on the entry/view being evaluated.
     * </p>
     *
     * @return List of constraint overrides for this constraint
     */
    @BeanTagAttribute(name = "constraintStateOverrides", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<? extends BaseConstraint> getConstraintStateOverrides() {
        return constraintStateOverrides;
    }

    /**
     * Set the constraintStateOverrides to be used when a state is matched during validation
     *
     * @param constraintStateOverrides
     */
    public void setConstraintStateOverrides(List<? extends BaseConstraint> constraintStateOverrides) {
        if (constraintStateOverrides != null) {
            for (BaseConstraint bc : constraintStateOverrides) {
                if (!bc.getClass().equals(this.getClass())) {
                    List<Class<?>> superClasses = new ArrayList<Class<?>>();
                    Class<?> o = bc.getClass();
                    while (o != null && !o.equals(BaseConstraint.class)) {
                        superClasses.add(o);
                        o = o.getSuperclass();
                    }

                    List<Class<?>> thisSuperClasses = new ArrayList<Class<?>>();
                    o = this.getClass();
                    while (o != null && !o.equals(BaseConstraint.class)) {
                        thisSuperClasses.add(o);
                        o = o.getSuperclass();
                    }
                    superClasses.retainAll(thisSuperClasses);

                    if (superClasses.isEmpty()) {
                        throw new RuntimeException("Constraint State Override is not a correct type, type should be " +
                                this.getClass().toString() + " (or child/parent of that constraint type)");
                    }
                }
                if (bc.getStates().isEmpty()) {
                    throw new RuntimeException(
                            "Constraint State Overrides MUST declare the states they apply to.  No states"
                                    + "were declared.");
                }
            }
        }
        this.constraintStateOverrides = constraintStateOverrides;
    }

    /**
     * Validates different requirements of component compiling a series of reports detailing information on errors
     * found in the component.  Used by the RiceDictionaryValidator.
     *
     * @param tracer Record of component's location
     */
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean("BaseConstraint", getMessageKey());

        if (getConstraintStateOverrides() != null) {
            for (int i = 0; i < constraintStateOverrides.size(); i++) {
                if (constraintStateOverrides.get(i).getStates() == null) {
                    String currentValues[] =
                            {"constraintStateOverrides(" + i + ").messageKey =" + constraintStateOverrides.get(i)
                                    .getMessageKey()};
                    tracer.createError("Constraints set in State Overrides must have there states property set",
                            currentValues);
                }
                constraintStateOverrides.get(i).completeValidation(tracer.getCopy());
            }
        }

        if (getMessageKey() == null) {
            String currentValues[] = {"messageKey =" + getMessageKey()};
            tracer.createWarning("Message key is not set", currentValues);
            ErrorReport error = new ErrorReport(ErrorReport.WARNING);
        }
    }
}
