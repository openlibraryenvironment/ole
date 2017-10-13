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

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.core.web.format.FormatException;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Class is used to format
 * <code>org.kuali.rice.core.api.util.type.KualiDecimal</code> in the local
 * currency
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifCurrencyEditor extends PropertyEditorSupport implements Serializable {
    private static final long serialVersionUID = 6692868638156609014L;
    private static Logger LOG = Logger.getLogger(UifCurrencyEditor.class);

    /**
     * This overridden method ...
     *
     * @see java.beans.PropertyEditorSupport#getAsText()
     */
    @Override
    public String getAsText() {
        Object obj = this.getValue();

        LOG.debug("format '" + obj + "'");
        if (obj == null)
            return null;

        NumberFormat formatter = getCurrencyInstanceUsingParseBigDecimal();
        String string = null;

        try {
            Number number = (Number) obj;
            if (obj instanceof KualiInteger) {
                formatter.setMaximumFractionDigits(0);
            }
            string = formatter.format(number.doubleValue());
        } catch (IllegalArgumentException e) {
            throw new FormatException("formatting", RiceKeyConstants.ERROR_CURRENCY, obj.toString(), e);
        } catch (ClassCastException e) {
            throw new FormatException("formatting", RiceKeyConstants.ERROR_CURRENCY, obj.toString(), e);
        }

        return string;
    }

    /**
     * retrieves a currency formatter instance and sets ParseBigDecimal to true
     * to fix [KULEDOCS-742]
     *
     * @return CurrencyInstance
     */
    private NumberFormat getCurrencyInstanceUsingParseBigDecimal() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        if (formatter instanceof DecimalFormat) {
            ((DecimalFormat) formatter).setParseBigDecimal(true);
        }
        return formatter;
    }

    /**
     * This overridden method sets the property value by parsing a given String.
     * It uses the <code>convertToObject</code> method to make the code
     * available to sub classes.
     *
     * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
     */
    @Override
    public void setAsText(String text) {
        this.setValue(convertToObject(text));
    }

    /**
     * Converts the string to a
     * <code>org.kuali.rice.core.api.util.type.KualiDecimal</code> object using the
     * local currency format.
     *
     * @param text
     *            the text from the UI to convert
     * @return the <code>org.kuali.rice.core.api.util.type.KualiDecimal</code>
     *         object to be set on the bean
     */
    protected Object convertToObject(String text) {
        KualiDecimal value = null;

        LOG.debug("convertToObject '" + text + "'");

        if (text != null) {
            text = text.trim();
            NumberFormat formatter = getCurrencyInstanceUsingParseBigDecimal();
            // Add the currency symbol suffix/prefix to the text to change to
            // correct format
            if (formatter instanceof DecimalFormat) {
                String prefix = ((DecimalFormat) formatter).getPositivePrefix();
                String suffix = ((DecimalFormat) formatter).getPositiveSuffix();
                if (!prefix.equals("") && !text.startsWith(prefix)) {
                    text = prefix.concat(text);
                }
                if (!suffix.equals("") && !text.endsWith(suffix)) {
                    text = text.concat(suffix);
                }
            }
            try {
                Number parsedNumber = formatter.parse(text);
                value = new KualiDecimal(parsedNumber.toString());
            } catch (NumberFormatException e) {
                throw new FormatException("parsing", RiceKeyConstants.ERROR_CURRENCY, text, e);
            } catch (ParseException e) {
                throw new FormatException("parsing", RiceKeyConstants.ERROR_CURRENCY, text, e);
            }
        }
        return value;
    }

}
