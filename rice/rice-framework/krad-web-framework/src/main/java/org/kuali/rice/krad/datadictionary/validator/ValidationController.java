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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.kuali.rice.krad.datadictionary.DefaultListableBeanFactory;
import org.kuali.rice.krad.uif.component.Component;
import org.springframework.core.io.ResourceLoader;

/**
 * A combination view controller for the Rice Dictionary Validator that handles both the setup/execution of the
 * validation and the output of the results.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ValidationController {
    protected static final String endl = System.getProperty("line.separator");

    protected boolean displayWarnings;
    protected boolean displayErrors;
    protected boolean displayXmlPages;
    protected boolean displayErrorMessages;
    protected boolean displayWarningMessages;

    /**
     * Constructor creating a new Rice Dictionary Validator with limited information during output
     *
     * @param displayErrors - True if the Validator should show the number of error during output
     * @param displayWarnings - True if the Validator should show the number of warnings during output
     * @param displayErrorMessages - True if the Validator should show the messages for the error reports
     * @param displayWarningMessages - True if the Validator should show messages involving warnings
     * @param displayXmlPages - True if the Validator should show the list of xml pages for the error reports
     */
    public ValidationController(boolean displayErrors, boolean displayWarnings, boolean displayErrorMessages,
            boolean displayWarningMessages, boolean displayXmlPages) {
        //LOG.debug("Creating new Rice Dictionary Validator with limited output");
        this.displayErrors = displayErrors;
        this.displayWarnings = displayWarnings;
        this.displayErrorMessages = displayErrorMessages;
        this.displayWarningMessages = displayWarningMessages;
        this.displayXmlPages = displayXmlPages;
    }

    /**
     * Constructor creating a new Rice Dictionary Validator
     */
    public ValidationController() {
        //LOG.debug("Creating new Rice Dictionary Validator");
        displayErrors = true;
        displayWarnings = true;
        displayErrorMessages = true;
        displayWarningMessages = true;
        displayXmlPages = true;
    }

    /**
     * Validates a collection of Spring Beans with no output
     *
     * @param xmlFiles - The collection of xml files used to load the provided beans
     * @param beans - Collection of preloaded beans
     * @param failOnWarning - Whether detecting a warning should cause the validation to fail
     * @return Returns true if the beans past validation
     */
    public boolean validate(String[] xmlFiles, ResourceLoader loader, DefaultListableBeanFactory beans,
            boolean failOnWarning) {
        // LOG.debug("Validating without output");
        Validator validator = new Validator();

        boolean passed = validator.validate(xmlFiles, loader, beans, failOnWarning);

        return passed;
    }

    /**
     * Validates a collection of Spring Beans with output going to a file
     *
     * @param xmlFiles - The collection of xml files used to load the provided beans
     * @param loader - The source that was used to load the beans
     * @param beans - Collection of preloaded beans
     * @param outputFile - The file location to save the output to
     * @param failOnWarning - Whether detecting a warning should cause the validation to fail
     * @return Returns true if the beans past validation
     */
    public boolean validate(String[] xmlFiles, ResourceLoader loader, DefaultListableBeanFactory beans,
            String outputFile, boolean failOnWarning) {
        Validator validator = new Validator();
        // LOG.debug("Validating with file output to "+outputFile);

        boolean passed = validator.validate(xmlFiles, loader, beans, failOnWarning);

        writeToFile(outputFile, validator, passed);

        return passed;
    }

    /**
     * Validates a collection of Spring Beans with output going to a print stream
     *
     * @param xmlFiles - The collection of xml files used to load the provided beans
     * @param loader - The source that was used to load the beans
     * @param beans - Collection of preloaded beans
     * @param stream - The PrintStream the output is sent to
     * @param failOnWarning - Whether detecting a warning should cause the validation to fail
     * @return Returns true if the beans past validation
     */
    public boolean validate(String[] xmlFiles, ResourceLoader loader, DefaultListableBeanFactory beans,
            PrintStream stream, boolean failOnWarning) {
        Validator validator = new Validator();
        // LOG.debug("Validating with Print Stream output");

        boolean passed = validator.validate(xmlFiles, loader, beans, failOnWarning);

        writeToStream(stream, validator, passed);

        return passed;
    }

    /**
     * Validates a collection of Spring Beans with output going to Log4j
     *
     * @param xmlFiles - The collection of xml files used to load the provided beans
     * @param loader - The source that was used to load the beans
     * @param beans - Collection of preloaded beans
     * @param log - The Log4j logger the output is sent to
     * @param failOnWarning - Whether detecting a warning should cause the validation to fail
     * @return Returns true if the beans past validation
     */
    public boolean validate(String[] xmlFiles, ResourceLoader loader, DefaultListableBeanFactory beans, Log log,
            boolean failOnWarning) {
        Validator validator = new Validator();
        //LOG.debug("Validating with Log4j output");

        boolean passed = validator.validate(xmlFiles, loader, beans, failOnWarning);

        writeToLog(log, validator, passed);

        return passed;
    }

    /**
     * Validates a collection of Spring Beans with no output
     *
     * @param xmlFiles - The collection of xml files used to load the beans
     * @param failOnWarning - Whether detecting a warning should cause the validation to fail
     * @return Returns true if the beans past validation
     */
    public boolean validate(String[] xmlFiles, boolean failOnWarning) {
        // LOG.debug("Validating without output");
        Validator validator = new Validator();

        boolean passed = validator.validate(xmlFiles, failOnWarning);

        return passed;
    }

    /**
     * Validates a collection of Spring Beans with output going to a file
     *
     * @param xmlFiles - The collection of xml files used to load the beans
     * @param outputFile - The file location to save the output to
     * @param failOnWarning - Whether detecting a warning should cause the validation to fail
     * @return Returns true if the beans past validation
     */
    public boolean validate(String[] xmlFiles, String outputFile, boolean failOnWarning) {
        Validator validator = new Validator();
        //LOG.debug("Validating with file output to "+outputFile);

        boolean passed = validator.validate(xmlFiles, failOnWarning);

        writeToFile(outputFile, validator, passed);

        return passed;
    }

    /**
     * Validates a collection of Spring Beans with output going to a print stream
     *
     * @param xmlFiles - The collection of xml files used to load the beans
     * @param stream - The PrintStream the output is sent to
     * @param failOnWarning - Whether detecting a warning should cause the validation to fail
     * @return Returns true if the beans past validation
     */
    public boolean validate(String[] xmlFiles, PrintStream stream, boolean failOnWarning) {
        Validator validator = new Validator();
        //LOG.debug("Validating with Print Stream output");

        boolean passed = validator.validate(xmlFiles, failOnWarning);

        writeToStream(stream, validator, passed);

        return passed;
    }

    /**
     * Validates a collection of Spring Beans with output going to Log4j
     *
     * @param xmlFiles - The collection of xml files used to load the provided beans
     * @param log - The Log4j logger the output is sent to
     * @param failOnWarning - Whether detecting a warning should cause the validation to fail
     * @return Returns true if the beans past validation
     */
    public boolean validate(String[] xmlFiles, Log log, boolean failOnWarning) {
        Validator validator = new Validator();
        //LOG.debug("Validating with Log4j output");

        boolean passed = validator.validate(xmlFiles, failOnWarning);

        writeToLog(log, validator, passed);

        return passed;
    }

    /**
     * Validates a Component with output going to Log4j
     *
     * @param object - The component to be validated
     * @param log - The Log4j logger the output is sent to
     * @param failOnWarning - Whether detecting a warning should cause the validation to fail
     * @return Returns true if the beans past validation
     */
    public boolean validate(Component object, Log log, boolean failOnWarning) {
        Validator validator = new Validator();
        //LOG.debug("Validating with Log4j output");

        boolean passed = validator.validate(object, failOnWarning);

        writeToLog(log, validator, passed);

        return passed;
    }

    /**
     * Writes the results of the validation to an output file
     *
     * @param path - The path to the file to write results to
     * @param validator - The filled validator
     * @param passed - Whether the validation passed or not
     */
    protected void writeToFile(String path, Validator validator, boolean passed) {
        try {
            BufferedWriter fout = new BufferedWriter(new FileWriter(path));

            fout.write("Validation Results" + endl);
            fout.write("Passed: " + passed + endl);
            if (displayErrors) {
                fout.write("Number of Errors: " + validator.getNumberOfErrors() + endl);
            }
            if (displayWarnings) {
                fout.write("Number of Warnings: " + validator.getNumberOfWarnings() + endl);
            }

            if (displayErrorMessages) {
                for (int i = 0; i < validator.getErrorReportSize(); i++) {
                    if (displayWarningMessages) {
                        fout.write(endl);
                        fout.write(validator.getErrorReport(i).errorMessage());
                    } else if (validator.getErrorReport(i).getErrorStatus() == ErrorReport.ERROR) {
                        fout.write(endl);
                        fout.write(validator.getErrorReport(i).errorMessage());
                    }

                    if (displayXmlPages) {
                        fout.write(validator.getErrorReport(i).errorPageList());
                    }
                }
            }

            fout.close();
        } catch (IOException e) {
            //LOG.warn("Exception when writing file", e);
        }
    }

    /**
     * Writes the results of the validation to an output file
     *
     * @param stream - The PrintStream the output is sent to
     * @param validator - The filled validator
     * @param passed - Whether the validation passed or not
     */
    protected void writeToStream(PrintStream stream, Validator validator, boolean passed) {
        stream.println("Validation Results");
        stream.println("Passed: " + passed);
        if (displayErrors) {
            stream.println("Number of Errors: " + validator.getNumberOfErrors());
        }
        if (displayWarnings) {
            stream.println("Number of Warnings: " + validator.getNumberOfWarnings());
        }

        if (displayErrorMessages) {
            for (int i = 0; i < validator.getErrorReportSize(); i++) {
                stream.println();
                if (displayWarningMessages) {
                    stream.println(validator.getErrorReport(i).errorMessage());
                } else if (validator.getErrorReport(i).getErrorStatus() == ErrorReport.ERROR) {
                    stream.println(validator.getErrorReport(i).errorMessage());
                }

                if (displayXmlPages) {
                    stream.println(validator.getErrorReport(i).errorPageList());
                }
            }
        }
    }

    /**
     * Writes the results of the validation to an output file
     *
     * @param log - The Log4j logger the output is sent to
     * @param validator - The filled validator
     * @param passed - Whether the validation passed or not
     */
    protected void writeToLog(Log log, Validator validator, boolean passed) {
        log.info("Passed: " + passed);
        if (displayErrors) {
            log.info("Number of Errors: " + validator.getNumberOfErrors());
        }
        if (displayWarnings) {
            log.info("Number of Warnings: " + validator.getNumberOfWarnings());
        }

        if (displayErrorMessages) {
            for (int i = 0; i < validator.getErrorReportSize(); i++) {
                if (validator.getErrorReport(i).getErrorStatus() == ErrorReport.ERROR) {
                    if (displayXmlPages) {
                        log.error(validator.getErrorReport(i).errorMessage() + validator.getErrorReport(i)
                                .errorPageList());
                    } else {
                        log.error(validator.getErrorReport(i).errorMessage());
                    }

                } else {
                    if (displayWarningMessages) {
                        if (displayXmlPages) {
                            log.warn(validator.getErrorReport(i).errorMessage() + validator.getErrorReport(i)
                                    .errorPageList());
                        } else {
                            log.warn(validator.getErrorReport(i).errorMessage());
                        }
                    }
                }

            }
        }
    }

    /**
     * Sets the displayWarnings
     *
     * @param display - Display or not
     */
    public void setDisplayWarnings(boolean display) {
        displayWarnings = display;
    }

    /**
     * Sets the displayErrors
     *
     * @param display - Display or not
     */
    public void setDisplayErrors(boolean display) {
        displayErrors = display;
    }

    /**
     * Sets the displayXmlPages
     *
     * @param display - Display or not
     */
    public void setDisplayXmlPages(boolean display) {
        displayXmlPages = display;
    }

    /**
     * Sets the displayErrorMessages
     *
     * @param display - Display or not
     */
    public void setDisplayErrorMessages(boolean display) {
        displayErrorMessages = display;
    }

    /**
     * Sets the displayWarningMessages
     *
     * @param display - Display or not
     */
    public void setDisplayWarningMessages(boolean display) {
        displayWarningMessages = display;
    }

    /**
     * Gets the displayWarnings, whether the number of warnings should be displayed
     *
     * @return displayWarnings
     */
    public boolean isDisplayWarnings() {
        return displayWarnings;
    }

    /**
     * Gets the displayErrors, whether the number of errors should be displayed
     *
     * @return displayErros
     */
    public boolean isDisplayErrors() {
        return displayErrors;
    }

    /**
     * Gets the displayXmlPages, whether the xml pages involved should be displayed
     *
     * @return displayXmlPages
     */
    public boolean isDisplayXmlPages() {
        return displayXmlPages;
    }

    /**
     * Gets the displayErrorMessages, whether the error messages should be displayed
     *
     * @return displayErrorMessages
     */
    public boolean isDisplayErrorMessages() {
        return displayErrorMessages;
    }

    /**
     * Gets the displayWarningMessages, whether the warning messages should be displayed
     *
     * @return displayWarningMessages
     */
    public boolean isDisplayWarningMessages() {
        return displayWarningMessages;
    }
}
