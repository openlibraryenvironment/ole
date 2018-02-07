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
package org.kuali.rice.krad.datadictionary.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.krad.datadictionary.DataDictionaryEntry;
import org.kuali.rice.krad.datadictionary.DataDictionaryException;
import org.kuali.rice.krad.datadictionary.DefaultListableBeanFactory;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.util.ExpressionUtils;
import org.kuali.rice.krad.uif.util.UifBeanFactoryPostProcessor;
import org.kuali.rice.krad.uif.view.View;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * A validator for Rice Dictionaries that stores the information found during its validation.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class Validator {
    private static final Log LOG = LogFactory.getLog(Validator.class);

    private static ArrayList<ErrorReport> errorReports = new ArrayList<ErrorReport>();

    private ValidationTrace tracerTemp;
    private int numberOfErrors;
    private int numberOfWarnings;

    /**
     * Constructor creating an empty validation report
     */
    public Validator() {
        tracerTemp = new ValidationTrace();
        numberOfErrors = 0;
        numberOfWarnings = 0;
    }

    public static void addErrorReport(ErrorReport report) {
        errorReports.add(report);
    }

    public static void resetErrorReport() {
        errorReports = new ArrayList<ErrorReport>();
    }

    /**
     * Runs the validations on a collection of beans
     *
     * @param beans - Collection of beans being validated
     * @param failOnWarning - Whether detecting a warning should cause the validation to fail
     * @return Returns true if the beans past validation
     */
    private boolean runValidations(DefaultListableBeanFactory beans, boolean failOnWarning) {
        LOG.info("Starting Dictionary Validation");
        resetErrorReport();
        Map<String, View> uifBeans;

        try {
            uifBeans = beans.getBeansOfType(View.class);
            for (View views : uifBeans.values()) {
                try {
                    ValidationTrace tracer = tracerTemp.getCopy();
                    if (doValidationOnUIFBean(views)) {
                        tracer.setValidationStage(ValidationTrace.START_UP);
                        runValidationsOnComponents(views, tracer);
                    }
                } catch (Exception e) {
                    String value[] = {views.getId(), "Exception = " + e.getMessage()};
                    tracerTemp.createError("Error Validating Bean View", value);
                }
            }
        } catch (Exception e) {
            String value[] = {"Validation set = views", "Exception = " + e.getMessage()};
            tracerTemp.createError("Error in Loading Spring Beans", value);
        }

        Map<String, DataDictionaryEntry> ddBeans;

        try {
            ddBeans = beans.getBeansOfType(DataDictionaryEntry.class);
            for (DataDictionaryEntry entry : ddBeans.values()) {
                try {

                    ValidationTrace tracer = tracerTemp.getCopy();
                    tracer.setValidationStage(ValidationTrace.BUILD);
                    entry.completeValidation(tracer);

                } catch (Exception e) {
                    String value[] = {"Validation set = Data Dictionary Entries", "Exception = " + e.getMessage()};
                    tracerTemp.createError("Error in Loading Spring Beans", value);
                }
            }
        } catch (Exception e) {
            String value[] = {"Validation set = Data Dictionary Entries", "Exception = " + e.getMessage()};
            tracerTemp.createError("Error in Loading Spring Beans", value);
        }

        compileFinalReport();

        LOG.info("Completed Dictionary Validation");

        if (numberOfErrors > 0) {
            return false;
        }
        if (failOnWarning) {
            if (numberOfWarnings > 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validates a UIF Component
     *
     * @param object - The UIF Component to be validated
     * @param failOnWarning - Whether the validation should fail if warnings are found
     * @return Returns true if the validation passes
     */
    public boolean validate(Component object, boolean failOnWarning) {
        LOG.info("Starting Dictionary Validation");

        if (doValidationOnUIFBean(object)) {
            ValidationTrace tracer = tracerTemp.getCopy();
            resetErrorReport();

            tracer.setValidationStage(ValidationTrace.BUILD);

            LOG.debug("Validating Component: " + object.getId());
            object.completeValidation(tracer.getCopy());

            runValidationsOnLifecycle(object, tracer.getCopy());

            runValidationsOnPrototype(object, tracer.getCopy());
        }

        compileFinalReport();

        LOG.info("Completed Dictionary Validation");

        if (numberOfErrors > 0) {
            return false;
        }
        if (failOnWarning) {
            if (numberOfWarnings > 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validates the beans in a collection of xml files
     *
     * @param failOnWarning - Whether detecting a warning should cause the validation to fail
     * @return Returns true if the beans past validation
     */
    public boolean validate(String[] xmlFiles, boolean failOnWarning) {
        DefaultListableBeanFactory beans = loadBeans(xmlFiles);

        return runValidations(beans, failOnWarning);
    }

    /**
     * Validates a collection of beans
     *
     * @param xmlFiles - The collection of xml files used to load the provided beans
     * @param loader - The source that was used to load the beans
     * @param beans - Collection of preloaded beans
     * @param failOnWarning - Whether detecting a warning should cause the validation to fail
     * @return Returns true if the beans past validation
     */
    public boolean validate(String xmlFiles[], ResourceLoader loader, DefaultListableBeanFactory beans,
            boolean failOnWarning) {
        tracerTemp = new ValidationTrace(xmlFiles, loader);
        return runValidations(beans, failOnWarning);
    }

    /**
     * Runs the validations on a component
     *
     * @param component - The component being checked
     * @param tracer - The current bean trace for the validation line
     */
    private void runValidationsOnComponents(Component component, ValidationTrace tracer) {

        try {
            ExpressionUtils.populatePropertyExpressionsFromGraph(component, false);
        } catch (Exception e) {
            String value[] = {"view = " + component.getId()};
            tracerTemp.createError("Error Validating Bean View while loading expressions", value);
        }

        LOG.debug("Validating View: " + component.getId());

        try {
            component.completeValidation(tracer.getCopy());
        } catch (Exception e) {
            String value[] = {component.getId()};
            tracerTemp.createError("Error Validating Bean View", value);
        }

        try {
            runValidationsOnLifecycle(component, tracer.getCopy());
        } catch (Exception e) {
            String value[] = {component.getId(), component.getComponentsForLifecycle().size() + "",
                    "Exception " + e.getMessage()};
            tracerTemp.createError("Error Validating Bean Lifecycle", value);
        }

        try {
            runValidationsOnPrototype(component, tracer.getCopy());
        } catch (Exception e) {
            String value[] = {component.getId(), component.getComponentPrototypes().size() + "",
                    "Exceptions : " + e.getLocalizedMessage()};
            tracerTemp.createError("Error Validating Bean Prototypes", value);
        }
    }

    /**
     * Runs the validations on a components lifecycle items
     *
     * @param component - The component whose lifecycle items are being checked
     * @param tracer - The current bean trace for the validation line
     */
    private void runValidationsOnLifecycle(Component component, ValidationTrace tracer) {
        List<Component> nestedComponents = component.getComponentsForLifecycle();
        if (nestedComponents == null) {
            return;
        }
        if (!doValidationOnUIFBean(component)) {
            return;
        }
        tracer.addBean(component);
        for (Component temp : nestedComponents) {
            if (temp == null) {
                continue;
            }
            if (tracer.getValidationStage() == ValidationTrace.START_UP) {
                ExpressionUtils.populatePropertyExpressionsFromGraph(temp, false);
            }
            if (temp.isRender()) {
                temp.completeValidation(tracer.getCopy());
                runValidationsOnLifecycle(temp, tracer.getCopy());
            }
        }
    }

    /**
     * Runs the validations on a components prototypes
     *
     * @param component - The component whose prototypes are being checked
     * @param tracer - The current bean trace for the validation line
     */
    private void runValidationsOnPrototype(Component component, ValidationTrace tracer) {
        List<Component> componentPrototypes = component.getComponentPrototypes();
        if (componentPrototypes == null) {
            return;
        }
        if (!doValidationOnUIFBean(component)) {
            return;
        }
        tracer.addBean(component);
        for (Component temp : componentPrototypes) {
            if (temp == null) {
                continue;
            }
            if (tracer.getValidationStage() == ValidationTrace.START_UP) {
                ExpressionUtils.populatePropertyExpressionsFromGraph(temp, false);
            }
            if (temp.isRender()) {
                temp.completeValidation(tracer.getCopy());
                runValidationsOnPrototype(temp, tracer.getCopy());
            }
        }
    }

    /**
     * Checks if the component being checked is a default or template component by seeing if its id starts with "uif"
     *
     * @param component - The component being checked
     * @return Returns true if the component is not a default or template
     */
    private boolean doValidationOnUIFBean(Component component) {
        if (component.getId() == null) {
            return true;
        }
        if (component.getId().length() < 3) {
            return true;
        }
        String temp = component.getId().substring(0, 3).toLowerCase();
        if (temp.contains("uif")) {
            return false;
        }
        return true;
    }

    /**
     * Validates an expression string for correct Spring Expression language syntax
     *
     * @param expression - The expression being validated
     * @return Returns true if the expression is of correct SpringEL syntax
     */
    public static boolean validateSpringEL(String expression) {
        if (expression == null) {
            return true;
        }
        if (expression.compareTo("") == 0) {
            return true;
        }
        if (expression.length() <= 3) {
            return false;
        }

        if (!expression.substring(0, 1).contains("@") || !expression.substring(1, 2).contains("{") ||
                !expression.substring(expression.length() - 1, expression.length()).contains("}")) {
            return false;
        }

        expression = expression.substring(2, expression.length() - 2);

        ArrayList<String> values = getExpressionValues(expression);

        for (int i = 0; i < values.size(); i++) {
            checkPropertyName(values.get(i));
        }

        return true;
    }

    /**
     * Gets the list of properties from an expression
     *
     * @param expression - The expression being validated.
     * @return A list of properties from the expression.
     */
    private static ArrayList<String> getExpressionValues(String expression) {
        expression = StringUtils.replace(expression, "!=", " != ");
        expression = StringUtils.replace(expression, "==", " == ");
        expression = StringUtils.replace(expression, ">", " > ");
        expression = StringUtils.replace(expression, "<", " < ");
        expression = StringUtils.replace(expression, "<=", " <= ");
        expression = StringUtils.replace(expression, ">=", " >= ");

        String stack = "";
        ArrayList<String> controlNames = new ArrayList<String>();

        boolean expectingSingleQuote = false;
        boolean ignoreNext = false;
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (!expectingSingleQuote && !ignoreNext && (c == '(' || c == ' ' || c == ')')) {
                ExpressionUtils.evaluateCurrentStack(stack.trim(), controlNames);
                //reset stack
                stack = "";
                continue;
            } else if (!ignoreNext && c == '\'') {
                stack = stack + c;
                expectingSingleQuote = !expectingSingleQuote;
            } else if (c == '\\') {
                stack = stack + c;
                ignoreNext = !ignoreNext;
            } else {
                stack = stack + c;
                ignoreNext = false;
            }
        }

        if (StringUtils.isNotEmpty(stack)) {
            ExpressionUtils.evaluateCurrentStack(stack.trim(), controlNames);
        }

        return controlNames;
    }

    /**
     * Checks the property for a valid name.
     *
     * @param name - The property name.
     * @return True if the validation passes, false if not
     */
    private static boolean checkPropertyName(String name) {
        if (!Character.isLetter(name.charAt(0))) {
            return false;
        }

        return true;
    }

    /**
     * Checks if a property of a Component is being set by expressions
     *
     * @param object - The Component being checked
     * @param property - The property being set
     * @return Returns true if the property is contained in the Components property expressions
     */
    public static boolean checkExpressions(Component object, String property) {
        if (object.getPropertyExpressions().containsKey(property)) {
            return true;
        }
        return false;
    }

    /**
     * Compiles general information on the validation from the list of generated error reports
     */
    private void compileFinalReport() {
        ArrayList<ErrorReport> reports = Validator.errorReports;
        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i).getErrorStatus() == ErrorReport.ERROR) {
                numberOfErrors++;
            } else if (reports.get(i).getErrorStatus() == ErrorReport.WARNING) {
                numberOfWarnings++;
            }
        }
    }

    /**
     * Loads the Spring Beans from a list of xml files
     *
     * @param xmlFiles
     * @return The Spring Bean Factory for the provided list of xml files
     */
    public DefaultListableBeanFactory loadBeans(String[] xmlFiles) {

        LOG.info("Starting XML File Load");
        DefaultListableBeanFactory beans = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(beans);

        DataDictionary.setupProcessor(beans);

        ArrayList<String> coreFiles = new ArrayList<String>();
        ArrayList<String> testFiles = new ArrayList<String>();

        for (int i = 0; i < xmlFiles.length; i++) {
            if (xmlFiles[i].contains("classpath")) {
                coreFiles.add(xmlFiles[i]);
            } else {
                testFiles.add(xmlFiles[i]);
            }
        }
        String core[] = new String[coreFiles.size()];
        coreFiles.toArray(core);

        String test[] = new String[testFiles.size()];
        testFiles.toArray(test);

        try {
            xmlReader.loadBeanDefinitions(core);
        } catch (Exception e) {
            LOG.error("Error loading bean definitions", e);
            throw new DataDictionaryException("Error loading bean definitions: " + e.getLocalizedMessage());
        }

        try {
            xmlReader.loadBeanDefinitions(getResources(test));
        } catch (Exception e) {
            LOG.error("Error loading bean definitions", e);
            throw new DataDictionaryException("Error loading bean definitions: " + e.getLocalizedMessage());
        }

        UifBeanFactoryPostProcessor factoryPostProcessor = new UifBeanFactoryPostProcessor();
        factoryPostProcessor.postProcessBeanFactory(beans);

        tracerTemp = new ValidationTrace(xmlFiles, xmlReader.getResourceLoader());

        LOG.info("Completed XML File Load");

        return beans;
    }

    /**
     * Converts the list of file paths into a list of resources
     *
     * @param files The list of file paths for conversion
     * @return A list of resources created from the file paths
     */
    private Resource[] getResources(String files[]) {
        Resource resources[] = new Resource[files.length];
        for (int i = 0; i < files.length; i++) {
            resources[0] = new FileSystemResource(files[i]);
        }

        return resources;
    }

    /**
     * Retrieves the number of errors found in the validation
     *
     * @return The number of errors found in the validation
     */
    public int getNumberOfErrors() {
        return numberOfErrors;
    }

    /**
     * Retrieves the number of warnings found in the validation
     *
     * @return The number of warnings found in the validation
     */
    public int getNumberOfWarnings() {
        return numberOfWarnings;
    }

    /**
     * Retrieves an individual error report for errors found during the validation
     *
     * @param index
     * @return The error report at the provided index
     */
    public ErrorReport getErrorReport(int index) {
        return errorReports.get(index);
    }

    /**
     * Retrieves the number of error reports generated during the validation
     *
     * @return The number of ErrorReports
     */
    public int getErrorReportSize() {
        return errorReports.size();
    }
}
