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
package org.kuali.rice.krad.uif.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.state.StateMapping;
import org.kuali.rice.krad.datadictionary.validation.constraint.BaseConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.CaseConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.MustOccurConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.PrerequisiteConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.SimpleConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.WhenConstraint;
import org.kuali.rice.krad.messages.MessageService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.control.TextControl;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.view.FormView;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.DatePicker;
import org.kuali.rice.krad.util.KRADUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains all the methods necessary for generating the js required to perform validation client
 * side. The processAndApplyConstraints(InputField field, View view) is the key method of this class
 * used by InputField to setup its client side validation mechanisms.
 * 
 * Methods now take into account state based validation and states on constraints.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ClientValidationUtils {
    // used to give validation methods unique signatures
    private static int methodKey = 0;

    // list used to temporarily store mustOccurs field names for the error
    // message
    private static List<List<String>> mustOccursPathNames;

    public static final String LABEL_KEY_SPLIT_PATTERN = ",";

    public static final String PREREQ_MSG_KEY = "prerequisite";
    public static final String POSTREQ_MSG_KEY = "postrequisite";
    public static final String MUSTOCCURS_MSG_KEY = "mustOccurs";
    public static final String MUSTOCCURS_MSG_EQUAL_KEY = "mustOccursEqualMinMax";
    public static final String GENERIC_FIELD_MSG_KEY = "general.genericFieldName";

    public static final String ALL_MSG_KEY = "general.all";
    public static final String ATMOST_MSG_KEY = "general.atMost";
    public static final String AND_MSG_KEY = "general.and";
    public static final String OR_MSG_KEY = "general.or";

    // enum representing names of rules provided by the jQuery plugin
    public static enum ValidationMessageKeys {
        REQUIRED("required"),
        MIN_EXCLUSIVE("minExclusive"),
        MAX_INCLUSIVE("maxInclusive"),
        MIN_LENGTH("minLengthConditional"),
        MAX_LENGTH("maxLengthConditional");

        private ValidationMessageKeys(String name) {
            this.name = name;
        }

        private final String name;

        @Override
        public String toString() {
            return name;
        }

        public static boolean contains(String name) {
            for (ValidationMessageKeys element : EnumSet.allOf(ValidationMessageKeys.class)) {
                if (element.toString().equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Returns formatted message text for the given message namespace, component, and key
     * 
     * @param namespace namespace code the message is associated with, if null the default namespace
     *        will be used
     * @param componentCode component code the message is associated with, if null default component
     *        code is used
     * @param messageKey key for the message to retrieve
     * @param params list of parameters for the message text
     * @return formatted message text
     */
    public static String generateMessageText(String namespace, String componentCode, String messageKey,
            List<String> params) {
        String message = "NO MESSAGE";
        if (StringUtils.isNotEmpty(messageKey)) {
            message = KRADServiceLocatorWeb.getMessageService().getMessageText(namespace, componentCode, messageKey);
            if (params != null && !params.isEmpty() && StringUtils.isNotEmpty(message)) {
                message = MessageFormat.format(message, params.toArray());
                message = MessageStructureUtils.translateStringMessage(message);
            }
        }

        if (StringUtils.isEmpty(message)) {
            message = messageKey;
        }

        //replace characters that might cause issues with their equivalent html codes
        message = KRADUtils.convertToHTMLAttributeSafeString(message);

        return message;
    }

    /**
     * Generates the js object used to override all default messages for validator jquery plugin
     * with custom messages retrieved from the message service
     * 
     * @return script for message override
     */
    public static String generateValidatorMessagesOption() {
        MessageService messageService = KRADServiceLocatorWeb.getMessageService();

        String mOption = "";
        String keyValuePairs = "";
        for (ValidationMessageKeys element : EnumSet.allOf(ValidationMessageKeys.class)) {
            String key = element.toString();
            String message = messageService.getMessageText(UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + key);

            if (StringUtils.isNotEmpty(message)) {
                message = MessageStructureUtils.translateStringMessage(message);
                keyValuePairs = keyValuePairs + "\n" + key + ": '" + message + "',";
            }
        }

        keyValuePairs = StringUtils.removeEnd(keyValuePairs, ",");
        if (StringUtils.isNotEmpty(keyValuePairs)) {
            mOption = "{" + keyValuePairs + "}";
        }

        return mOption;
    }

    /**
     * Returns the add method jquery validator call for the regular expression stored in
     * validCharactersConstraint.
     * 
     * @param validCharactersConstraint
     * @return js validator.addMethod script
     */
    public static String getRegexMethod(InputField field, ValidCharactersConstraint validCharactersConstraint) {
        String message = generateMessageText(validCharactersConstraint.getMessageNamespaceCode(),
                validCharactersConstraint.getMessageComponentCode(), validCharactersConstraint.getMessageKey(),
                validCharactersConstraint.getValidationMessageParams());
        String key = "validChar-" + field.getBindingInfo().getBindingPath() + methodKey;

        // replace characters known to cause issues if not escaped
        String regex = validCharactersConstraint.getValue();
        if (regex.contains("\\\\")) {
            regex = regex.replaceAll("\\\\", "\\\\\\\\");
        }
        if (regex.contains("/")) {
            regex = regex.replace("/", "\\/");
        }

        return "\njQuery.validator.addMethod(\""
                + ScriptUtils.escapeName(key)
                + "\", function(value, element) {\n "
                + "return this.optional(element) || /"
                + regex
                + "/.test(value);"
                + "}, \""
                + message
                + "\");";
    }

    /**
     * Returns the add method jquery validator call for the regular expression stored in
     * validCharactersConstraint that explicitly checks a boolean. Needed because one method accepts
     * params and the other doesn't.
     * 
     * @param validCharactersConstraint
     * @return js validator.addMethod script
     */
    public static String getRegexMethodWithBooleanCheck(InputField field,
            ValidCharactersConstraint validCharactersConstraint) {
        String message = generateMessageText(validCharactersConstraint.getMessageNamespaceCode(),
                validCharactersConstraint.getMessageComponentCode(), validCharactersConstraint.getMessageKey(),
                validCharactersConstraint.getValidationMessageParams());
        String key = "validChar-" + field.getBindingInfo().getBindingPath() + methodKey;

        // replace characters known to cause issues if not escaped
        String regex = validCharactersConstraint.getValue();
        if (regex.contains("\\\\")) {
            regex = regex.replaceAll("\\\\", "\\\\\\\\");
        }
        if (regex.contains("/")) {
            regex = regex.replace("/", "\\/");
        }

        return "\njQuery.validator.addMethod(\""
                + ScriptUtils.escapeName(key)
                + "\", function(value, element, doCheck) {\n if(doCheck === false){return true;}else{"
                + "return this.optional(element) || /"
                + regex
                + "/.test(value);}"
                + "}, \""
                + message
                + "\");";
    }

    /**
     * This method processes a single CaseConstraint. Internally it makes calls to
     * processWhenConstraint for each WhenConstraint that exists in this constraint. It adds a
     * "dependsOn" css class to this field for the field which the CaseConstraint references.
     * 
     * @param view
     * @param andedCase the boolean logic to be anded when determining if this case is satisfied
     *        (used for nested CaseConstraints)
     */
    public static void processCaseConstraint(InputField field, View view, CaseConstraint constraint, String andedCase,
            String validationState, StateMapping stateMapping) {
        if (constraint.getOperator() == null) {
            constraint.setOperator("equals");
        }

        String operator = "==";
        if (constraint.getOperator().equalsIgnoreCase("not_equals") || constraint.getOperator().equalsIgnoreCase(
                "not_equal")) {
            operator = "!=";
        } else if (constraint.getOperator().equalsIgnoreCase("greater_than_equal")) {
            operator = ">=";
        } else if (constraint.getOperator().equalsIgnoreCase("less_than_equal")) {
            operator = "<=";
        } else if (constraint.getOperator().equalsIgnoreCase("greater_than")) {
            operator = ">";
        } else if (constraint.getOperator().equalsIgnoreCase("less_than")) {
            operator = "<";
        } else if (constraint.getOperator().equalsIgnoreCase("has_value")) {
            operator = "";
        }
        // add more operator types here if more are supported later

        field.getControl().addStyleClass("dependsOn-" + ScriptUtils.escapeName(constraint.getPropertyName()));

        if (constraint.getWhenConstraint() != null && !constraint.getWhenConstraint().isEmpty()) {
            //String fieldPath = field.getBindingInfo().getBindingObjectPath() + "." + constraint.getPropertyName();
            String fieldPath = constraint.getPropertyName();
            for (WhenConstraint wc : constraint.getWhenConstraint()) {
                wc = ConstraintStateUtils.getApplicableConstraint(wc, validationState, stateMapping);
                if (wc != null) {
                    processWhenConstraint(field, view, constraint, wc, ScriptUtils.escapeName(fieldPath), operator,
                            andedCase, validationState, stateMapping);
                }
            }
        }
    }

    /**
     * This method processes the WhenConstraint passed in. The when constraint is used to create a
     * boolean statement to determine if the constraint will be applied. The necessary rules/methods
     * for applying this constraint are created in the createRule call. Note the use of the use of
     * coerceValue js function call.
     * 
     * @param view
     * @param wc
     * @param fieldPath
     * @param operator
     * @param andedCase
     */
    private static void processWhenConstraint(InputField field, View view, CaseConstraint caseConstraint,
            WhenConstraint wc, String fieldPath, String operator, String andedCase, String validationState,
            StateMapping stateMapping) {
        String ruleString = "";
        // prerequisite constraint

        String booleanStatement = "";
        if (wc.getValues() != null) {

            String caseStr = "";
            if (!caseConstraint.isCaseSensitive()) {
                caseStr = ".toUpperCase()";
            }
            for (int i = 0; i < wc.getValues().size(); i++) {
                if (operator.isEmpty()) {
                    // has_value case
                    if (wc.getValues().get(i) instanceof String && ((String) wc.getValues().get(i)).equalsIgnoreCase(
                            "false")) {
                        booleanStatement = booleanStatement + "(coerceValue('" + fieldPath + "') == '')";
                    } else {
                        booleanStatement = booleanStatement + "(coerceValue('" + fieldPath + "') != '')";
                    }
                } else {
                    // everything else
                    booleanStatement = booleanStatement
                            + "(coerceValue('"
                            + fieldPath
                            + "')"
                            + caseStr
                            + " "
                            + operator
                            + " \""
                            + wc.getValues().get(i)
                            + "\""
                            + caseStr
                            + ")";
                }
                if ((i + 1) != wc.getValues().size()) {
                    booleanStatement = booleanStatement + " || ";
                }
            }

        }

        if (andedCase != null) {
            booleanStatement = "(" + booleanStatement + ") && (" + andedCase + ")";
        }

        if (wc.getConstraint() != null && StringUtils.isNotEmpty(booleanStatement)) {
            Constraint constraint = ConstraintStateUtils.getApplicableConstraint(wc.getConstraint(), validationState,
                    stateMapping);
            if (constraint != null) {
                ruleString = createRule(field, constraint, booleanStatement, view, validationState, stateMapping);
            }
        }

        if (StringUtils.isNotEmpty(ruleString)) {
            addScriptToPage(view, field, ruleString);
        }
    }

    /**
     * Adds the script to the view to execute on a jQuery document ready event.
     * 
     * @param view
     * @param script
     */
    public static void addScriptToPage(View view, InputField field, String script) {
        String prefixScript = "";

        if (field.getOnDocumentReadyScript() != null) {
            prefixScript = field.getOnDocumentReadyScript();
        }
        field.setOnDocumentReadyScript(prefixScript + "\n" + "runValidationScript(function(){" + script + "});");
    }

    /**
     * Determines which fields are being evaluated in a boolean statement, so handlers can be
     * attached to them if needed, returns these names in a list.
     * 
     * @param statement
     * @return
     */
    private static List<String> parseOutFields(String statement) {
        List<String> fieldNames = new ArrayList<String>();
        String[] splits = StringUtils.splitByWholeSeparator(statement, "coerceValue(");
        for (String s : splits) {
            //must be a coerceValue param and not preceding content from the split, always starts with "'"
            if (!s.startsWith("'")) {
                continue;
            }

            s = s.substring(1);
            String fieldName = StringUtils.substringBefore(s, "'");
            //Only add field name once for this condition check
            if (fieldNames.contains(fieldName)) {
                fieldNames.add(fieldName);
            }

        }
        return fieldNames;
    }

    /**
     * This method takes in a constraint to apply only when the passed in booleanStatement is valid.
     * The method will create the necessary addMethod and addRule jquery validator calls for the
     * rule to be applied to the field when the statement passed in evaluates to true during runtime
     * and this field is being validated. Note the use of custom methods for min/max length/value.
     * 
     * @param field the field to apply the generated methods and rules to
     * @param constraint the constraint to be applied when the booleanStatement evaluates to true
     *        during validation
     * @param booleanStatement the booleanstatement in js - should return true when the validation
     *        rule should be applied
     * @param view
     * @return
     */
    @SuppressWarnings("boxing")
    private static String createRule(InputField field, Constraint constraint, String booleanStatement, View view,
            String validationState, StateMapping stateMapping) {
        String rule = "";
        int constraintCount = 0;
        if (constraint instanceof BaseConstraint && ((BaseConstraint) constraint).getApplyClientSide()) {
            if (constraint instanceof SimpleConstraint) {
                if (((SimpleConstraint) constraint).getRequired() != null && ((SimpleConstraint) constraint)
                        .getRequired()) {
                    rule = rule + "required: function(element){\nreturn (" + booleanStatement + ");}";
                    //special requiredness indicator handling
                    String showIndicatorScript = "";
                    for (String checkedField : parseOutFields(booleanStatement)) {
                        showIndicatorScript = showIndicatorScript +
                                "setupShowReqIndicatorCheck('" + checkedField + "', '" + field.getBindingInfo()
                                        .getBindingPath() + "', " + "function(){\nreturn (" + booleanStatement
                                + ");});\n";
                    }
                    addScriptToPage(view, field, showIndicatorScript);

                    constraintCount++;
                }

                if (((SimpleConstraint) constraint).getMinLength() != null) {
                    if (constraintCount > 0) {
                        rule = rule + ",\n";
                    }
                    rule = rule
                            + "minLengthConditional: ["
                            + ((SimpleConstraint) constraint).getMinLength()
                            + ", function(){return "
                            + booleanStatement
                            + ";}]";
                    constraintCount++;
                }

                if (((SimpleConstraint) constraint).getMaxLength() != null) {
                    if (constraintCount > 0) {
                        rule = rule + ",\n";
                    }
                    rule = rule
                            + "maxLengthConditional: ["
                            + ((SimpleConstraint) constraint).getMaxLength()
                            + ", function(){return "
                            + booleanStatement
                            + ";}]";
                    constraintCount++;
                }

                if (((SimpleConstraint) constraint).getExclusiveMin() != null) {
                    if (constraintCount > 0) {
                        rule = rule + ",\n";
                    }
                    rule = rule
                            + "minExclusive: ["
                            + ((SimpleConstraint) constraint).getExclusiveMin()
                            + ", function(){return "
                            + booleanStatement
                            + ";}]";
                    constraintCount++;
                }

                if (((SimpleConstraint) constraint).getInclusiveMax() != null) {
                    if (constraintCount > 0) {
                        rule = rule + ",\n";
                    }
                    rule = rule
                            + "maxInclusive: ["
                            + ((SimpleConstraint) constraint).getInclusiveMax()
                            + ", function(){return "
                            + booleanStatement
                            + ";}]";
                    constraintCount++;
                }

                rule = "jQuery('[name=\""
                        + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                        + "\"]').rules(\"add\", {"
                        + rule
                        + "\n});";
            } else if (constraint instanceof ValidCharactersConstraint) {
                String regexMethod = "";
                String methodName = "";
                if (StringUtils.isNotEmpty(((ValidCharactersConstraint) constraint).getValue())) {
                    regexMethod = ClientValidationUtils.getRegexMethodWithBooleanCheck(field,
                            (ValidCharactersConstraint) constraint) + "\n";
                    methodName = "validChar-" + field.getBindingInfo().getBindingPath() + methodKey;
                    methodKey++;
                } else {
                    if (StringUtils.isNotEmpty(((ValidCharactersConstraint) constraint).getMessageKey())) {
                        methodName = ((ValidCharactersConstraint) constraint).getMessageKey();
                    }
                }

                if (StringUtils.isNotEmpty(methodName)) {
                    rule = regexMethod
                            + "jQuery('[name=\""
                            + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                            + "\"]').rules(\"add\", {\n\""
                            + methodName
                            + "\" : function(element){return ("
                            + booleanStatement
                            + ");}\n});";
                }
            } else if (constraint instanceof PrerequisiteConstraint) {
                processPrerequisiteConstraint(field, (PrerequisiteConstraint) constraint, view, booleanStatement);
            } else if (constraint instanceof CaseConstraint) {
                processCaseConstraint(field, view, (CaseConstraint) constraint, booleanStatement, validationState,
                        stateMapping);
            } else if (constraint instanceof MustOccurConstraint) {
                processMustOccurConstraint(field, view, (MustOccurConstraint) constraint, booleanStatement);
            }
        }

        return rule;
    }

    /**
     * Simpler version of processPrerequisiteConstraint
     * 
     * @param constraint
     * @param view
     * @see ClientValidationUtils#processPrerequisiteConstraint(org.kuali.rice.krad.uif.field.InputField,
     *      PrerequisiteConstraint, View, String)
     */
    public static void processPrerequisiteConstraint(InputField field, PrerequisiteConstraint constraint, View view) {
        processPrerequisiteConstraint(field, constraint, view, "true");
    }

    /**
     * Processes a Prerequisite constraint that should be applied when the booleanStatement passed
     * in evaluates to true.
     * 
     * @param constraint prerequisiteConstraint
     * @param view
     * @param booleanStatement the booleanstatement in js - should return true when the validation
     *        rule should be applied
     */
    public static void processPrerequisiteConstraint(InputField field, PrerequisiteConstraint constraint, View view,
            String booleanStatement) {
        if (constraint != null && constraint.getApplyClientSide().booleanValue()) {
            String dependsClass = "dependsOn-" + ScriptUtils.escapeName(constraint.getPropertyName());
            String addClass = "jQuery('[name=\""
                    + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                    + "\"]').addClass('"
                    + dependsClass
                    + "');"
                    +
                    "jQuery('[name=\""
                    + ScriptUtils.escapeName(constraint.getPropertyName())
                    + "\"]').addClass('"
                    + "dependsOn-"
                    + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                    + "');";

            addScriptToPage(view, field, addClass
                    + getPrerequisiteStatement(field, view, constraint, booleanStatement)
                    + getPostrequisiteStatement(field, constraint, booleanStatement));

            //special requiredness indicator handling
            String showIndicatorScript = "setupShowReqIndicatorCheck('" + ScriptUtils.escapeName(
                    field.getBindingInfo().getBindingPath()) + "', '" + ScriptUtils.escapeName(
                    constraint.getPropertyName()) + "', " + "function(){\nreturn (coerceValue('" + ScriptUtils
                    .escapeName(field.getBindingInfo().getBindingPath()) + "') && " + booleanStatement + ");});\n";

            addScriptToPage(view, field, showIndicatorScript);
        }
    }

    /**
     * Creates the script necessary for executing a prerequisite rule in which this field occurs
     * after the field specified in the prerequisite rule - since it requires a specific set of UI
     * logic. Builds an if statement containing an addMethod jquery validator call. Adds a
     * "dependsOn" css class to this field for the field specified.
     * 
     * @param constraint prerequisiteConstraint
     * @param booleanStatement the booleanstatement in js - should return true when the validation
     *        rule should be applied
     * @return
     */
    private static String getPrerequisiteStatement(InputField field, View view, PrerequisiteConstraint constraint,
            String booleanStatement) {
        methodKey++;

        MessageService messageService = KRADServiceLocatorWeb.getMessageService();

        String message = "";
        if (StringUtils.isEmpty(constraint.getMessageKey())) {
            message = messageService.getMessageText(UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "prerequisite");
            message = MessageStructureUtils.translateStringMessage(message);
        } else {
            message = generateMessageText(constraint.getMessageNamespaceCode(),
                    constraint.getMessageComponentCode(), constraint.getMessageKey(),
                    constraint.getValidationMessageParams());
        }

        if (StringUtils.isEmpty(message)) {
            message = "prerequisite - No message";
        } else {
            InputField requiredField = (InputField) view.getViewIndex().getDataFieldByPath(
                    constraint.getPropertyName());
            if (requiredField != null && StringUtils.isNotEmpty(requiredField.getLabel())) {
                message = MessageFormat.format(message, requiredField.getLabel());
            } else {
                String genericFieldLabel = messageService.getMessageText(GENERIC_FIELD_MSG_KEY);
                message = MessageFormat.format(message, genericFieldLabel);
            }
        }

        // field occurs before case
        String methodName = "prConstraint-"
                + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                + methodKey;

        String addClass = "jQuery('[name=\""
                + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                + "\"]').addClass('"
                + methodName
                + "');\n";

        String method = "\njQuery.validator.addMethod(\"" + methodName + "\", function(value, element) {\n" +
                " if(" + booleanStatement + "){ return (this.optional(element) || (coerceValue('" + ScriptUtils
                        .escapeName(constraint.getPropertyName()) + "')));}else{return true;} " +
                "}, \"" + message + "\");";

        String ifStatement = "if(occursBefore('"
                + ScriptUtils.escapeName(constraint.getPropertyName())
                + "','"
                + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                +
                "')){"
                + addClass
                + method
                + "}";

        return ifStatement;
    }

    /**
     * This method creates the script necessary for executing a prerequisite rule in which this
     * field occurs before the field specified in the prerequisite rule - since it requires a
     * specific set of UI logic. Builds an if statement containing an addMethod jquery validator
     * call.
     * 
     * @param constraint prerequisiteConstraint
     * @param booleanStatement the booleanstatement in js - should return true when the validation
     *        rule should be applied
     * @return
     */
    private static String getPostrequisiteStatement(InputField field, PrerequisiteConstraint constraint,
            String booleanStatement) {
        MessageService messageService = KRADServiceLocatorWeb.getMessageService();

        // field occurs after case
        String message = "";
        if (StringUtils.isEmpty(constraint.getMessageKey())) {
            message = messageService.getMessageText(UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "postrequisite");
            message = MessageStructureUtils.translateStringMessage(message);
        } else {
            message = generateMessageText(constraint.getMessageNamespaceCode(), constraint.getMessageComponentCode(),
                    constraint.getMessageKey(), constraint.getValidationMessageParams());
        }

        if (StringUtils.isEmpty(constraint.getMessageKey())) {
            if (StringUtils.isNotEmpty(field.getLabel())) {
                message = MessageFormat.format(message, field.getLabel());
            } else {
                String genericFieldLabel = messageService.getMessageText(GENERIC_FIELD_MSG_KEY);
                message = MessageFormat.format(message, genericFieldLabel);
            }
        }

        String function = "function(element){\n" +
                "return (coerceValue('"
                + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                + "') && "
                + booleanStatement
                + ");}";
        String postStatement = "\nelse if(occursBefore('"
                + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                + "','"
                + ScriptUtils.escapeName(constraint.getPropertyName())
                +
                "')){\njQuery('[name=\""
                + ScriptUtils.escapeName(constraint.getPropertyName())
                +
                "\"]').rules(\"add\", { required: \n"
                + function
                + ", \nmessages: {\nrequired: \""
                + message
                + "\"}});}\n";

        return postStatement;

    }

    /**
     * This method processes the MustOccurConstraint. The constraint is only applied when the
     * booleanStatement evaluates to true during validation. This method creates the addMethod and
     * add rule calls for the jquery validation plugin necessary for applying this constraint to
     * this field.
     * 
     * @param view
     * @param mc
     * @param booleanStatement the booleanstatement in js - should return true when the validation
     *        rule should be applied
     */
    public static void processMustOccurConstraint(InputField field, View view, MustOccurConstraint mc,
            String booleanStatement) {
        methodKey++;
        mustOccursPathNames = new ArrayList<List<String>>();
        // TODO make this show the fields its requiring
        String methodName = "moConstraint-"
                + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                + methodKey;
        String method = "\njQuery.validator.addMethod(\"" + methodName + "\", function(value, element) {\n" +
                " if("
                + booleanStatement
                + "){return (this.optional(element) || ("
                + getMustOccurStatement(field, mc)
                + "));}else{return true;}"
                +
                "}, \""
                + getMustOccursMessage(view, mc)
                + "\");";
        String rule = method
                + "jQuery('[name=\""
                + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                + "\"]').rules(\"add\", {\n\""
                + methodName
                + "\": function(element){return ("
                + booleanStatement
                + ");}\n});";
        addScriptToPage(view, field, rule);
    }

    /**
     * This method takes in a MustOccurConstraint and returns the statement used in determining if
     * the must occurs constraint has been satisfied when this field is validated. Note the use of
     * the mustOccurCheck method. Nested mustOccurConstraints are ored against the result of the
     * mustOccurCheck by calling this method recursively.
     * 
     * @param constraint
     * @return
     */
    @SuppressWarnings("boxing")
    private static String getMustOccurStatement(InputField field, MustOccurConstraint constraint) {
        String statement = "";
        List<String> attributePaths = new ArrayList<String>();
        if (constraint != null && constraint.getApplyClientSide()) {
            String array = "[";
            if (constraint.getPrerequisiteConstraints() != null) {
                for (int i = 0; i < constraint.getPrerequisiteConstraints().size(); i++) {
                    field.getControl().addStyleClass("dependsOn-" + constraint.getPrerequisiteConstraints().get(i)
                            .getPropertyName());
                    array = array + "'" + ScriptUtils.escapeName(constraint.getPrerequisiteConstraints().get(i)
                            .getPropertyName()) + "'";
                    attributePaths.add(constraint.getPrerequisiteConstraints().get(i).getPropertyName());
                    if (i + 1 != constraint.getPrerequisiteConstraints().size()) {
                        array = array + ",";
                    }

                }
            }
            array = array + "]";
            statement = "mustOccurTotal(" + array + ", " + constraint.getMin() + ", " + constraint.getMax() + ")";
            //add min to string list
            if (constraint.getMin() != null) {
                attributePaths.add(constraint.getMin().toString());
            } else {
                attributePaths.add(null);
            }
            //add max to string list
            if (constraint.getMax() != null) {
                attributePaths.add(constraint.getMax().toString());
            } else {
                attributePaths.add(null);
            }

            mustOccursPathNames.add(attributePaths);
            if (StringUtils.isEmpty(statement)) {
                statement = "0";
            }
            if (constraint.getMustOccurConstraints() != null) {
                for (MustOccurConstraint mc : constraint.getMustOccurConstraints()) {
                    statement = "mustOccurCheck(" + statement + " + " + getMustOccurStatement(field, mc) +
                            ", " + constraint.getMin() + ", " + constraint.getMax() + ")";
                }
            } else {
                statement = "mustOccurCheck(" + statement +
                        ", " + constraint.getMin() + ", " + constraint.getMax() + ")";
            }
        }
        return statement;
    }

    /**
     * Generates a message for the must occur constraint (if no label key is specified). This
     * message is most accurate when must occurs is a single or double level constraint. Beyond
     * that, the message will still be accurate but may be confusing for the user - this
     * auto-generated message however will work in MOST use cases.
     * 
     * @param view
     * @return
     */
    private static String getMustOccursMessage(View view, MustOccurConstraint constraint) {
        MessageService messageService = KRADServiceLocatorWeb.getMessageService();

        String message = "";
        if (StringUtils.isNotEmpty(constraint.getMessageKey())) {
            message = generateMessageText(constraint.getMessageNamespaceCode(), constraint.getMessageComponentCode(),
                    constraint.getMessageKey(), constraint.getValidationMessageParams());
        } else {
            String and = messageService.getMessageText(AND_MSG_KEY);
            String or = messageService.getMessageText(OR_MSG_KEY);
            String mustOccursMsgEqualMinMax = messageService.getMessageText(
                    UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + MUSTOCCURS_MSG_EQUAL_KEY);
            String atMost = messageService.getMessageText(ATMOST_MSG_KEY);
            String genericLabel = messageService.getMessageText(GENERIC_FIELD_MSG_KEY);
            String mustOccursMsg = messageService.getMessageText(
                    UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + MUSTOCCURS_MSG_KEY);

            String statement = "";
            for (int i = 0; i < mustOccursPathNames.size(); i++) {
                String andedString = "";

                List<String> paths = mustOccursPathNames.get(i);
                if (!paths.isEmpty()) {
                    //note that the last 2 strings are min and max and rest are attribute paths
                    String min = paths.get(paths.size() - 2);
                    String max = paths.get(paths.size() - 1);
                    for (int j = 0; j < paths.size() - 2; j++) {
                        InputField field = (InputField) view.getViewIndex().getDataFieldByPath(paths.get(j).trim());
                        String label = genericLabel;
                        if (field != null && StringUtils.isNotEmpty(field.getLabel())) {
                            label = field.getLabel();
                        }
                        if (min.equals(max)) {
                            if (j == 0) {
                                andedString = label;
                            } else if (j == paths.size() - 3) {
                                andedString = andedString + " " + and + " " + label;
                            } else {
                                andedString = andedString + ", " + label;
                            }
                        } else {
                            andedString = andedString + "(" + label + ")";
                        }
                    }
                    if (min.equals(max)) {
                        andedString = "(" + andedString + ")";
                    }

                    if (StringUtils.isNotBlank(andedString) && !andedString.equals("()")) {
                        if (StringUtils.isNotEmpty(min) && StringUtils.isNotEmpty(max) && !min.equals(max)) {
                            andedString = MessageFormat.format(mustOccursMsg, min + "-" + max) + " " + andedString;
                        } else if (StringUtils.isNotEmpty(min)
                                && StringUtils.isNotEmpty(max)
                                && min.equals(max)
                                && i == 0) {
                            andedString = mustOccursMsgEqualMinMax + " " + andedString;
                        } else if (StringUtils.isNotEmpty(min)
                                && StringUtils.isNotEmpty(max)
                                && min.equals(max)
                                && i != 0) {
                            //leave andedString as is
                        } else if (StringUtils.isNotEmpty(min)) {
                            andedString = MessageFormat.format(mustOccursMsg, min) + " " + andedString;
                        } else if (StringUtils.isNotEmpty(max)) {
                            andedString = MessageFormat.format(mustOccursMsg, atMost + " " + max) + " " + andedString;
                        }
                    }
                }
                if (StringUtils.isNotEmpty(andedString)) {
                    if (StringUtils.isNotBlank(statement)) {
                        statement = statement + " " + or.toUpperCase() + " " + andedString;
                    } else {
                        statement = andedString;
                    }
                }
            }
            if (StringUtils.isNotEmpty(statement)) {
                message = statement;
                message = message.replace(")(", " " + or + " ");
            }
        }

        return message;
    }

    /**
     * This method processes all the constraints on the InputField passed in and adds all the
     * necessary jQuery and js required (validator's rules, methods, and messages) to the View's
     * onDocumentReady call. The result is js that will validate all the constraints contained on an
     * InputField during user interaction with the field using the jQuery validation plugin and
     * custom code.
     * 
     * @param field
     */
    @SuppressWarnings("boxing")
    public static void processAndApplyConstraints(InputField field, View view, Object model) {
        methodKey = 0;
        String validationState = ConstraintStateUtils.getClientViewValidationState(model, view);
        StateMapping stateMapping = view.getStateMapping();

        if (view instanceof FormView && ((FormView) view).isValidateClientSide()) {
            SimpleConstraint simpleConstraint = ConstraintStateUtils.getApplicableConstraint(
                    field.getSimpleConstraint(), validationState, stateMapping);
            if (simpleConstraint != null && simpleConstraint.getApplyClientSide()) {

                if ((simpleConstraint.getRequired() != null) && (simpleConstraint.getRequired().booleanValue())) {
                    field.getControl().addStyleClass("required");
                }

                if (simpleConstraint.getExclusiveMin() != null) {
                    if (field.getControl() instanceof TextControl
                            && ((TextControl) field.getControl()).getDatePicker() != null) {
                        DatePicker datePicker = ((TextControl) field.getControl()).getDatePicker();
                        Map<String, String> dpTemplateOptions = datePicker.getTemplateOptions();

                        if (dpTemplateOptions == null) {
                            datePicker.setTemplateOptions(dpTemplateOptions = new HashMap<String, String>());
                        }

                        dpTemplateOptions.put("minDate",
                                simpleConstraint.getExclusiveMin());
                    } else {
                        String rule = "jQuery('[name=\""
                                + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                                + "\"]').rules(\"add\", {\n minExclusive: ["
                                + simpleConstraint.getExclusiveMin()
                                + "]});";
                        addScriptToPage(view, field, rule);
                    }
                }

                if (simpleConstraint.getInclusiveMax() != null) {
                    if (field.getControl() instanceof TextControl
                            && ((TextControl) field.getControl()).getDatePicker() != null) {
                        ((TextControl) field.getControl()).getDatePicker().getTemplateOptions().put("maxDate",
                                simpleConstraint.getInclusiveMax());
                    } else {
                        String rule = "jQuery('[name=\""
                                + ScriptUtils.escapeName(field.getBindingInfo().getBindingPath())
                                + "\"]').rules(\"add\", {\n maxInclusive: ["
                                + simpleConstraint.getInclusiveMax()
                                + "]});";
                        addScriptToPage(view, field, rule);
                    }
                }
            }

            ValidCharactersConstraint validCharactersConstraint = ConstraintStateUtils.getApplicableConstraint(
                    field.getValidCharactersConstraint(), validationState, stateMapping);

            if (validCharactersConstraint != null && validCharactersConstraint.getApplyClientSide()) {
                if (StringUtils.isNotEmpty(validCharactersConstraint.getValue())) {
                    // set regex value takes precedence
                    addScriptToPage(view, field, ClientValidationUtils.getRegexMethod(field,
                            validCharactersConstraint));
                    field.getControl().addStyleClass(
                            "validChar-" + field.getBindingInfo().getBindingPath() + methodKey);
                    methodKey++;
                } else {
                    //blindly assume that if there is no regex value defined that there must be a method by this name
                    if (StringUtils.isNotEmpty(validCharactersConstraint.getMessageKey())) {
                        field.getControl().addStyleClass(validCharactersConstraint.getMessageKey());
                    }
                }
            }

            CaseConstraint caseConstraint = ConstraintStateUtils.getApplicableConstraint(field.getCaseConstraint(),
                    validationState, stateMapping);
            if (caseConstraint != null && caseConstraint.getApplyClientSide()) {
                processCaseConstraint(field, view, caseConstraint, null, validationState, stateMapping);
            }

            if (field.getDependencyConstraints() != null) {
                for (PrerequisiteConstraint prc : field.getDependencyConstraints()) {
                    prc = ConstraintStateUtils.getApplicableConstraint(prc, validationState, stateMapping);
                    if (prc != null) {
                        processPrerequisiteConstraint(field, prc, view);
                    }
                }
            }

            if (field.getMustOccurConstraints() != null) {
                for (MustOccurConstraint mc : field.getMustOccurConstraints()) {
                    mc = ConstraintStateUtils.getApplicableConstraint(mc, validationState, stateMapping);
                    if (mc != null) {
                        processMustOccurConstraint(field, view, mc, "true");
                    }
                }
            }

        }
    }

}
