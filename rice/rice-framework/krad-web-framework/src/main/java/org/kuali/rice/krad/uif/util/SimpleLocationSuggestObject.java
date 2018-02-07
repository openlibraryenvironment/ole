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
 * An object that represents a simple suggestion with a label, value, and href.  For use with simple LocationSuggest
 * widget result methods.  This class is also available for extension.
 */
public class SimpleLocationSuggestObject extends SimpleSuggestObject{
    private String href;

    /**
     * Create SimpleLocationSuggestObject
     *
     * @param label the label to show for the suggestion
     * @param value the value to insert when the label is selected
     * @param href the href(url) to navigate to
     */
    public SimpleLocationSuggestObject(String label, String value, String href) {
        super(label, value);
        this.href = href;
    }

    /**
     * The href to navigate to for this suggestion
     *
     * @return the href(url)
     */
    public String getHref() {
        return href;
    }

    /**
     * Set the href
     *
     * @param href
     */
    public void setHref(String href) {
        this.href = href;
    }
}
