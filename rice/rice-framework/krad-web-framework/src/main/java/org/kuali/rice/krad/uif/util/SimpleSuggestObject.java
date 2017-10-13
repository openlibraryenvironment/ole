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

/**
 * An object that represents a simple suggestion with a label and a value.  For use when returning a suggestion without
 * specifying valuePropertyName or labelPropertyName on the Suggest widget.  This class is also available for
 * extension purposes.
 */
public class SimpleSuggestObject {
    private String label;
    private String value;

    /**
     * Create a SimpleSuggestObject
     *
     * @param label the label to show for the suggestion
     * @param value the value to insert when the label is selected
     */
    public SimpleSuggestObject(String label, String value){
        this.label = label;
        this.value = value;
    }

    /**
     * The label of the suggestion
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label
     *
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * The value of the suggestion (inserted when the suggestion is picked)
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the value
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
