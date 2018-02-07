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
package org.kuali.rice.krad.labs.kitchensink;

import org.apache.commons.lang.StringUtils;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UITestPropertyEditor extends PropertyEditorSupport implements Serializable {
    private static final long serialVersionUID = -4113846709722954737L;

    /**
     * @see java.beans.PropertyEditorSupport#getAsText()
     */
    @Override
    public String getAsText() {
        Object obj = this.getValue();

        if (obj == null) {
            return null;
        }

        String displayValue = obj.toString();
        if (displayValue.length() > 3) {
            displayValue = StringUtils.substring(displayValue, 0, 3) + "-" + StringUtils.substring(displayValue, 3);
        }

        return displayValue;
    }

    /**
     * @see java.beans.PropertyEditorSupport#setAsText(String)
     */
    @Override
    public void setAsText(String text) {
        String value = text;
        if (StringUtils.contains(value, "-")) {
            value = StringUtils.replaceOnce(value, "-", "");
        }

        this.setValue(value);
    }

}
