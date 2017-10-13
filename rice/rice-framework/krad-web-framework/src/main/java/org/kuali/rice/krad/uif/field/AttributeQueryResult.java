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
package org.kuali.rice.krad.uif.field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object that is returned for Ajax attribute queries and exposed
 * as JSON
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AttributeQueryResult implements Serializable {
    private static final long serialVersionUID = -6688384365943881516L;

    private String resultMessage;
    private String resultMessageStyleClasses;

    private Map<String, String> resultFieldData;
    private List<Object> resultData;

    public AttributeQueryResult() {
        resultFieldData = new HashMap<String, String>();
        resultData = new ArrayList<Object>();
    }

    /**
     * Message text that should display (if non empty) with the results.
     * Can be used to given messages such as data not found
     *
     * @return text to display with results
     */
    public String getResultMessage() {
        return resultMessage;
    }

    /**
     * Setter for the result message text
     *
     * @param resultMessage
     */
    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    /**
     * CSS Style classes that should be applied to the result message text
     *
     * @return CSS style classes
     */
    public String getResultMessageStyleClasses() {
        return resultMessageStyleClasses;
    }

    /**
     * Setter for the CSS style classes to use for the return message
     *
     * @param resultMessageStyleClasses
     */
    public void setResultMessageStyleClasses(String resultMessageStyleClasses) {
        this.resultMessageStyleClasses = resultMessageStyleClasses;
    }

    /**
     * Returns data for multiple fields as a Map where key is the field
     * name and map value is the field value
     *
     * @return result field data
     */
    public Map<String, String> getResultFieldData() {
        return resultFieldData;
    }

    /**
     * Setter for the map field data
     *
     * @param resultFieldData
     */
    public void setResultFieldData(Map<String, String> resultFieldData) {
        this.resultFieldData = resultFieldData;
    }

    /**
     * Result of an attribute query that will be sent back to the client
     *
     * @return result data
     */
    public List<Object> getResultData() {
        return resultData;
    }

    /**
     * Setter for the attribute query result data
     *
     * @param resultData
     */
    public void setResultData(List<Object> resultData) {
        this.resultData = resultData;
    }

    /**
     * Returns a copy of the attribute query result.
     *
     * @return AttributeQueryResult copy of the component
     */
    public <T> T copy() {
        T copiedClass = null;
        try {
            copiedClass = (T)this.getClass().newInstance();
        }
        catch(Exception exception) {
            throw new RuntimeException();
        }

        copyProperties(copiedClass);

        return copiedClass;
    }

    /**
     * Copies the properties over for the copy method.
     *
     * @param attributeQuery The AttributeQuery to copy
     */
    protected <T> void copyProperties(T attributeQueryResult) {
        AttributeQueryResult attributeQueryResultCopy = (AttributeQueryResult) attributeQueryResult;

        if (this.resultFieldData != null) {
            attributeQueryResultCopy.setResultFieldData(new HashMap<String, String>(this.resultFieldData));
        }

        attributeQueryResultCopy.setResultMessage(this.resultMessage);
        attributeQueryResultCopy.setResultMessageStyleClasses(this.resultMessageStyleClasses);
    }
}
