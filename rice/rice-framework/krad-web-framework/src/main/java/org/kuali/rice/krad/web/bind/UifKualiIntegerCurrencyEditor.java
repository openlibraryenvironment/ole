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
package org.kuali.rice.krad.web.bind;

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;

/**
 * Class used to format
 * <code>org.kuali.rice.core.api.util.type.KualiInteger</code> in the local currency
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifKualiIntegerCurrencyEditor extends UifCurrencyEditor {

    /**
     * This overridden method converts the display string to a
     * <code>org.kuali.rice.core.api.util.type.KualiInteger</code> object using
     * local currency format.
     *
     * @see UifCurrencyEditor#setAsText(java.lang.String)
     */
    @Override
    public void setAsText(String text) {
        KualiDecimal value = (KualiDecimal) (super.convertToObject(text));
        this.setValue(new KualiInteger(value.longValue()));
    }

}
