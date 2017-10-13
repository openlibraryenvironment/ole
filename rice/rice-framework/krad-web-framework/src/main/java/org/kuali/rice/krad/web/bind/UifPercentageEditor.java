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

import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiPercent;
import org.kuali.rice.core.web.format.FormatException;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * PropertyEditor converts between percentage strings and
 * <code>org.kuali.rice.core.api.util.type.KualiPercent</code> objects
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifPercentageEditor extends PropertyEditorSupport implements Serializable {
    private static final long serialVersionUID = -3562156375311932094L;

    /** The default scale for percentage values. */
    public final static int PERCENTAGE_SCALE = 2;

    /** The default format for percentage values. */
    public final static String PERCENTAGE_FORMAT = "#,##0.00";

    /**
     * This overridden method converts
     * <code>org.kuali.rice.core.api.util.type.KualiPercent</code> objects to the
     * display string.
     *
     * @see java.beans.PropertyEditorSupport#getAsText()
     */
    @Override
    public String getAsText() {
        Object value = this.getValue();
        // Previously returned N/A
        if (value == null)
            return "";

        String stringValue = "";
        try {
            if (value instanceof KualiDecimal) {
                value = ((KualiDecimal) this.getValue()).bigDecimalValue();
            }
            BigDecimal bigDecValue = (BigDecimal) value;
            bigDecValue = bigDecValue.setScale(PERCENTAGE_SCALE, BigDecimal.ROUND_HALF_UP);
            stringValue = NumberFormat.getInstance().format(bigDecValue.doubleValue());
        } catch (IllegalArgumentException iae) {
            throw new FormatException("formatting", RiceKeyConstants.ERROR_PERCENTAGE, this.getValue().toString(), iae);
        }

        return stringValue + " percent";
    }

    /**
     * This overridden method converts the display string to a
     * <code>org.kuali.rice.core.api.util.type.KualiPercent</code> object.
     *
     * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            DecimalFormat formatter = new DecimalFormat(PERCENTAGE_FORMAT);
            Number parsedNumber = formatter.parse(text.trim());
            this.setValue(new KualiPercent(parsedNumber.doubleValue()));
        } catch (NumberFormatException e) {
            throw new FormatException("parsing", RiceKeyConstants.ERROR_PERCENTAGE, text, e);
        } catch (ParseException e) {
            throw new FormatException("parsing", RiceKeyConstants.ERROR_PERCENTAGE, text, e);
        }
    }

}
