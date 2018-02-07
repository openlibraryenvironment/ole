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
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.DataBinding;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Linear collection of identifiers for individual Spring Beans starting with the base bean and ending with the most
 * recent.  Has the ability to located xml files related to the trace.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ValidationTrace {
    private static final Log LOG = LogFactory.getLog(ValidationTrace.class);

    // Constant identifer for a trace entry where the bean has no identifier itself
    public static final String NO_BEAN_ID = "NOBEANID";

    // Constant identifier for a trace during startup
    public static final int START_UP = 0;

    // Constant identifier for a trace during render
    public static final int BUILD = 1;

    private ArrayList<String> beanIds;
    private ArrayList<String> beanTypes;
    private Map<String, Document> beanMap;
    private int validationStage;

    /**
     * Constructor for an empty token to start a trace
     */
    public ValidationTrace() {
        beanIds = new ArrayList<String>();
        beanTypes = new ArrayList<String>();
        beanMap = new HashMap<String, Document>();
    }

    /**
     * Constructor for an empty token to start a trace
     */
    public ValidationTrace(String[] files, ResourceLoader loader) {
        beanIds = new ArrayList<String>();
        beanTypes = new ArrayList<String>();
        beanMap = new HashMap<String, Document>();
        loadFiles(files, loader);
    }

    /**
     * Adds a single entry into the trace
     *
     * @param beanId - An identifier for the bean
     * @param beanType - The type of bean
     */
    public void addBean(String beanType, String beanId) {
        beanIds.add(beanId);
        beanTypes.add(beanType);
    }

    /**
     * Adds a UIF Component to the trace
     *
     * @param component - The object to be added
     */
    public void addBean(Component component) {
        String beanId = NO_BEAN_ID;
        String beanType = component.getClass().getSimpleName();
        if (component.getId() != null) {
            if (component.getId().compareTo("null") != 0) {
                beanId = component.getId();
            } else {
                try {
                    beanId = ((DataBinding) component).getPropertyName();

                } catch (Exception e) {
                    beanId = NO_BEAN_ID;
                }
            }
        } else {
            try {
                beanId = ((DataBinding) component).getPropertyName();
            } catch (Exception e) {
                beanId = NO_BEAN_ID;
            }
        }
        addBean(beanType, beanId);
    }

    /**
     * Adds a UIF Configurable to the trace
     *
     * @param configurable - The object to be added
     */
    public void addBean(UifDictionaryBean configurable) {
        String beanId = "configurable";
        String beanType = configurable.getClass().getSimpleName();
        addBean(beanType, beanId);
    }

    /**
     * Removes an entry from the trace
     *
     * @param index
     */
    public void removeBean(int index) {
        beanIds.remove(index);
        beanTypes.remove(index);
    }

    /**
     * Replaces a trace entry's information
     *
     * @param index - The location of the bean
     * @param beanId - An identifier for the bean
     * @param beanType - The type of bean
     */
    public void modifyBean(int index, String beanId, String beanType) {
        beanIds.set(index, beanId);
        beanTypes.set(index, beanType);
    }

    /**
     * Creates a copy of the ValidationTrace
     *
     * @return A complete copy of the current token
     */
    public ValidationTrace getCopy() {
        ValidationTrace copy = new ValidationTrace();

        for (int i = 0; i < getTraceSize(); i++) {
            copy.addBean(getBeanType(i), getBeanId(i));
        }
        copy.setValidationStage(getValidationStage());
        copy.setBeanMap(beanMap);
        return copy;
    }

    /**
     * Loads the xmlFiles of the data objects being validated into a list of Documents that can be parsed to find the
     * xmls related to the error.
     *
     * @param beanFiles - The list of file paths used in the creation of the beans
     * @param loader - The source that was used to load the beans
     */
    private void loadFiles(String[] beanFiles, ResourceLoader loader) {
        LOG.debug("Started Loading Parser Files");

        for (int i = 0; i < beanFiles.length; i++) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document;
                String file = beanFiles[i];//.substring(0,10)+"/"+beanFiles[i].substring(10);
                LOG.debug("Loading file: " + file);
                document = builder.parse(loader.getResource(file).getInputStream());
                beanMap.put(file, document);
            } catch (Exception e) {
                LOG.error("Not Found: " + beanFiles[i]);
            }
        }
        LOG.debug("Finished Loading Parser Files");
    }

    /**
     * Parse the the Documents contained in the Map an finding the map entries whos documents contain the passed in Id
     * in a bean element's attributes. All attributes are checked because the id used in the trace can be from
     * different
     * properties on different beans and not just the id attribute.
     *
     * @param id - The attribute value to be found
     * @param beans - A Map containing the Documents to be looked through
     * @return - A sub set of maps from the past in list that contains the value being looked for
     */
    private Map<String, Document> findBeanById(String id, Map<String, Document> beans) {
        Map<String, Document> result = new HashMap<String, Document>();
        LOG.debug("Searching for bean of Id: " + id);

        Iterator iter = beans.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Document document = (Document) entry.getValue();
            NodeList nodes = document.getElementsByTagName("bean");

            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).hasAttributes()) {
                    for (int j = 0; j < nodes.item(i).getAttributes().getLength(); j++) {
                        if (nodes.item(i).getAttributes().item(j).getNodeValue().toLowerCase().compareTo(
                                id.toLowerCase()) == 0) {
                            LOG.debug("Found bean of Id = " + id);

                            result.put((String) entry.getKey(), (Document) entry.getValue());

                            break;
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Finds related xml files to an error by searching for files that contain beans that have been encountered in the
     * validation.  The file path and Document version of the xmls are paired and stored in a Map.  This allows for
     * returning the file paths easy when searching through the Documents.
     *
     * @return A list of file paths to the xmls in which the beans were found
     */
    public ArrayList<String> findXmlFiles() {
        Map<String, Document> result = new HashMap<String, Document>();
        LOG.debug("Looking for Xml files");

        for (int i = 0; i < getTraceSize(); i++) {
            if (getBeanId(i) != null) {
                if (getBeanId(i).compareTo(NO_BEAN_ID) != 0) {
                    result.putAll(findBeanById(getBeanId(i), beanMap));
                }
            }
        }

        ArrayList<String> files = new ArrayList<String>();
        Iterator iter = result.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            files.add((String) entry.getKey());
        }

        return files;
    }

    /**
     * Sets the stage of the validation where the trace is taking place
     *
     * @param stage - The stage of the validation
     */
    public void setValidationStage(int stage) {
        validationStage = stage;
    }

    /**
     * Sets the beanMap for when copying the tracer
     *
     * @param newMap - The map to be stored
     */
    private void setBeanMap(Map<String, Document> newMap) {
        beanMap = newMap;
    }

    /**
     * Creates a new error report as an Error and adds it to the global list.
     *
     * @param validation - The validation that fails.
     * @param values - The values involved.
     */
    public void createError(String validation, String values[]) {
        ErrorReport report = new ErrorReport(ErrorReport.ERROR, validation, this, values);
        Validator.addErrorReport(report);

    }

    /**
     * Creates a new error report as a Warning and adds it to the global list.
     *
     * @param validation - The validation that fails.
     * @param values - The values involved.
     */
    public void createWarning(String validation, String values[]) {
        ErrorReport report = new ErrorReport(ErrorReport.WARNING, validation, this, values);
        Validator.addErrorReport(report);

    }

    /**
     * Retrieves a single entry in the BeanId trace list, a collection identifiers for the traced beans
     *
     * @param index - The location of the bean
     * @return String Identifier for the bean at the provided index of the trace
     */
    public String getBeanId(int index) {
        return beanIds.get(index);
    }

    /**
     * Retrieves a single entry in the BeanType trace list, a collection of types for the traced beansa collection
     * identifiers for the traced beans
     *
     * @param index - The location of the bean type
     * @return String Type for the bean at the provided index of the trace
     */
    public String getBeanType(int index) {
        return beanTypes.get(index);
    }

    /**
     * Retrieves the stage when the trace is taking place
     * The stage is the time frame when the validation is taking place in the application
     *
     * @return Returns the stage of the validation.
     */
    public int getValidationStage() {
        return validationStage;
    }

    /**
     * Retrieves the number of beans in the trace list
     *
     * @return Number of beans stored in the trace
     */
    public int getTraceSize() {
        return beanIds.size();
    }

    /**
     * Retrieves the complete trace path with each bean shown in the form beanId(BeanType)
     *
     * @return The String path of the trace
     */
    public String getBeanLocation() {
        String path = "";

        for (int i = 0; i < beanTypes.size() - 1; i++) {
            path = path + beanTypes.get(i) + "(" + beanIds.get(i) + ")" + ".";
        }

        if (getTraceSize() > 0) {
            path = path + beanTypes.get(beanTypes.size() - 1) + "(" + beanIds.get(beanTypes.size() - 1) + ")";
        }

        return path;
    }

    /**
     * Retrieves the list of xmls file paths found to be related to error
     *
     * @return A list of file paths to the related xmls
     */
    public ArrayList<String> getRelatedXmls() {
        return findXmlFiles();
    }
}
