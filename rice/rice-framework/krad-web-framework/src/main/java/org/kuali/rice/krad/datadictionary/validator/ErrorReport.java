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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

/**
 * Collection of information regarding a single error detected within a dictionary bean
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ErrorReport {
    private static final Log LOG = LogFactory.getLog(ErrorReport.class);

    // Constant identifiers of the type of error
    public static final int ERROR = 1;
    public static final int WARNING = 2;

    private static final String endl = System.getProperty("line.separator");

    // Is type of error detailed in the report
    private int errorStatus;
    private String validationFailed;
    private String beanLocation;
    private ArrayList<String> currentValues;
    private ArrayList<String> xmlPages;

    /**
     * Constructor creating a new report for an error
     *
     * @param ErrorStatus - The type of error being reported
     */
    public ErrorReport(int ErrorStatus) {
        this.errorStatus = ErrorStatus;
        this.validationFailed = "";
        this.beanLocation = "";
        this.currentValues = new ArrayList<String>();
        this.xmlPages = new ArrayList<String>();
    }

    /**
     * Constructor creating a new report for an error with set values
     *
     * @param errorStatus - The type of error being reported
     * @param validationFailed - The validation that was failed
     * @param beanLocation - The location of the bean in which error occured
     * @param values - An array of the values effected
     */
    public ErrorReport(int errorStatus, String validationFailed, String beanLocation, String values[]) {
        this.errorStatus = errorStatus;
        this.validationFailed = validationFailed;
        this.beanLocation = beanLocation;
        this.currentValues = new ArrayList<String>();
        this.xmlPages = new ArrayList<String>();

        for (int i = 0; i < values.length; i++) {
            addCurrentValue(values[i]);
        }
    }

    /**
     * Constructor creating a new report for an error with set values
     *
     * @param errorStatus - The type of error being reported
     * @param validationFailed - The validation that was failed
     * @param trace - ValidationTrace containing information on xml files and location
     */
    public ErrorReport(int errorStatus, String validationFailed, ValidationTrace trace) {
        this.errorStatus = errorStatus;
        this.validationFailed = validationFailed;
        this.xmlPages = trace.getRelatedXmls();
        this.beanLocation = trace.getBeanLocation();
        this.currentValues = new ArrayList<String>();
    }

    /**
     * Constructor creating a new report for an error with set values
     *
     * @param errorStatus - The type of error being reported
     * @param validationFailed - The validation that was failed
     * @param trace - ValidationTrace containing information on xml files and location
     * @param values - An array of the values effected
     */
    public ErrorReport(int errorStatus, String validationFailed, ValidationTrace trace, String values[]) {
        this.errorStatus = errorStatus;
        this.validationFailed = validationFailed;
        this.beanLocation = trace.getBeanLocation();
        this.xmlPages = trace.getRelatedXmls();
        this.currentValues = new ArrayList<String>();

        for (int i = 0; i < values.length; i++) {
            addCurrentValue(values[i]);
        }
    }

    /**
     * Constructor creating a new report for an error with set values
     *
     * @param errorStatus - The type of error being reported
     * @param validationFailed - The validation that was failed
     * @param beanLocation - The location of the bean in which error occured
     */
    public ErrorReport(int errorStatus, String validationFailed, String beanLocation) {
        this.errorStatus = errorStatus;
        this.validationFailed = validationFailed;
        this.beanLocation = beanLocation;
        this.currentValues = new ArrayList<String>();
        this.xmlPages = new ArrayList<String>();
    }

    /**
     * Creates a new ErrorReport of ERROR status
     *
     * @param validationFailed - The validation that was failed
     * @param trace - ValidationTrace containing information on xml files and location
     * @return Returns a new ErrorReport of ERROR status
     */
    public static ErrorReport createError(String validationFailed, ValidationTrace trace) {
        return new ErrorReport(ERROR, validationFailed, trace);
    }

    /**
     * Creates a new ErrorReport of WARNING status
     *
     * @param validationFailed - The validation that was failed
     * @param trace - ValidationTrace containing information on xml files and location
     * @return Returns a new ErrorReport of WARNING status
     */
    public static ErrorReport createWarning(String validationFailed, ValidationTrace trace) {
        return new ErrorReport(WARNING, validationFailed, trace);
    }

    /**
     * Adds a value involved in the error
     *
     * @param value - Value involved ("Name of Value = Its Value")
     */
    public void addCurrentValue(String value) {
        currentValues.add(value);
    }

    /**
     * Adds a xml page involved in the error
     *
     * @param page - The file path of the xml page involved
     */
    public void addXmlPage(String page) {
        xmlPages.add(page);
    }

    /**
     * Add a list of xml page involved in the error
     *
     * @param pages - The file path of the xml page involved
     */
    public void addXmlPages(ArrayList<String> pages) {
        xmlPages.addAll(pages);
    }

    /**
     * Removes a value from the list of those involved
     *
     * @param index - The index of the value
     */
    public void removeCurrentValue(int index) {
        currentValues.remove(index);
    }

    /**
     * Removes a xml page from the list of those involved
     *
     * @param index - The index of the xml page
     */
    public void removeXmlPage(int index) {
        xmlPages.remove(index);
    }

    /**
     * Replaces a value in the list of those involved
     *
     * @param index - The index of the value
     * @param value - The value to replace the value with
     */
    public void modifyCurrentValue(int index, String value) {
        currentValues.set(index, value);
    }

    /**
     * Replaces a xml page in the list of those involved
     *
     * @param index - The index of the page
     * @param page - The page to replace the xml page with
     */
    public void modifyXmlPage(int index, String page) {
        xmlPages.set(index, page);
    }

    /**
     * Creates a message for the error being reported
     *
     * @return A compiled message about the error in the report
     */
    public String errorMessage() {
        String message = "";

        if (errorStatus == ERROR) {
            message = message + ("Dictionary Error Detected: " + getValidationFailed() + endl);
        } else if (errorStatus == WARNING) {
            message = message + ("Dictionary Warning Detected: " + getValidationFailed() + endl);
        }

        message = message + ("Bean: " + getBeanLocation() + endl);
        message = message + ("Values involved:" + endl);
        for (int i = 0; i < getCurrentValueSize(); i++) {
            message = message + (getCurrentValue(i) + endl);
        }

        return message;
    }

    /**
     * Creates a message for the xml pages involved
     *
     * @return A compiled list of the xml pages involved
     */
    public String errorPageList() {
        String pages = "Xml Pages Involved" + endl;

        for (int i = 0; i < getXmlPageSize(); i++) {
            pages = pages + getXmlPage(i) + endl;
        }

        return pages;
    }

    /**
     * Sets the validation that was failed
     *
     * @param validation - The validation that failed
     */
    public void setValidationFailed(String validation) {
        validationFailed = validation;
    }

    /**
     * Sets the location of the bean in the trace
     *
     * @param location - The Bean location
     */
    public void setBeanLocation(String location) {
        beanLocation = location;
    }

    /**
     * Retrieves the type of error
     *
     * @return Integer value of the type of error
     */
    public int getErrorStatus() {
        return errorStatus;
    }

    /**
     * Retrieves the validation that was failed
     *
     * @return The failed validation
     */
    public String getValidationFailed() {
        return validationFailed;
    }

    /**
     * Retrieves the location of the bean in the trace
     *
     * @return The location of the bean
     */
    public String getBeanLocation() {
        return beanLocation;
    }

    /**
     * Retrieves a value involved in the error
     *
     * @param index - The index of the value
     * @return The value involved at the provided index
     */
    public String getCurrentValue(int index) {
        return currentValues.get(index);
    }

    /**
     * Retrieves a xml page file location involved in the error
     *
     * @param index - The index of the page
     * @return The xml file involved at the provided index
     */
    public String getXmlPage(int index) {
        return xmlPages.get(index);
    }

    /**
     * Retrieves the number of values involved in the error.
     *
     * @return The number of values involved
     */
    public int getCurrentValueSize() {
        return currentValues.size();
    }

    /**
     * Retireves the number of xml pages involved in the error
     *
     * @return The number of xml pages involved
     */
    public int getXmlPageSize() {
        return xmlPages.size();
    }
}
