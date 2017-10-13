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
// begin Kuali Foundation modification
package org.kuali.rice.core.web.format;
// end Kuali Foundation modification

// begin Kuali Foundation modification

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * begin Kuali Foundation modification
 * This class is used to format Currency objects.
 * end Kuali Foundation modification
 */
public class CurrencyFormatter extends Formatter {
	
    private static final long serialVersionUID = -7986953503122821012L;
    
	// begin Kuali Foundation modification
    // original code
    /*
    /** The default scale for currency values * /
    public final static int SCALE = 2;

    /** The key used to look up the currency error string * /
    public final static String CURRENCY_ERROR_KEY = "error.currency";

    public static final String SHOW_SYMBOL = "showCurrencySymbol";

    static final String PARSE_MSG = "Unable to parse a currency value from ";

    static final String FORMAT_MSG = "Unable to format a currency value from ";
    */
    private static Logger LOG = Logger.getLogger(CurrencyFormatter.class);
    public static final String SHOW_SYMBOL = "showCurrencySymbol";
    private static final Pattern CURRENCY_PATTERN = Pattern.compile("[-\\(\\)\\$\\.,0-9]*");
    private static final Pattern TRAILING_DECIMAL_PATTERN = Pattern.compile("^(\\.[0-9]{0,2}){0,1}\\)?$");
    // end Kuali Foundation modification

    /**
     * begin Kuali Foundation modification
     * Unformats its argument and returns a KualiDecimal instance initialized with the resulting string value
     * 
     * @see org.kuali.rice.core.web.format.Formatter#convertToObject(java.lang.String)
     * end Kuali Foundation modification
     */
    @Override
    protected Object convertToObject(String target) {
        // begin Kuali Foundation modification
        KualiDecimal value = null;

        LOG.debug("convertToObject '" + target + "'");

        if (target != null) {
            target = target.trim();

            String rawString = target;

            // parseable values are $1.23 and ($1.23), not (1.23)
            if (target.startsWith("(")) {
                if (!target.startsWith("($")) {
                    target = "($" + StringUtils.substringAfter(target, "(");
                }
            }

            // Insert currency symbol if absent
            if (!(target.startsWith("(") || target.startsWith(getSymbol()))) {
                target = interpolateSymbol(target);
            }

            // preemptively detect non-numeric-related symbols, since NumberFormat.parse seems to be silently deleting them
            // (i.e. 9aaaaaaaaaaaaaaa is silently converted into 9)
            if (!CURRENCY_PATTERN.matcher(target).matches()) {
                throw new FormatException("parsing", RiceKeyConstants.ERROR_CURRENCY, rawString);
            }

            // preemptively detect String with excessive digits after the decimal, to prevent them from being silently rounded
            if (rawString.contains(".") && !TRAILING_DECIMAL_PATTERN.matcher(target.substring(target.indexOf("."), target.length())).matches()) {
                throw new FormatException("parsing", RiceKeyConstants.ERROR_CURRENCY_DECIMAL, rawString);
            }

            // actually reformat the numeric value
            NumberFormat formatter = getCurrencyInstanceUsingParseBigDecimal();
            try {
                Number parsedNumber = formatter.parse(target);
                value = new KualiDecimal(parsedNumber.toString());
            }
            catch (NumberFormatException e) {
                throw new FormatException("parsing", RiceKeyConstants.ERROR_CURRENCY, rawString, e);
            }
            catch (ParseException e) {
                throw new FormatException("parsing", RiceKeyConstants.ERROR_CURRENCY, rawString, e);
            }
        }

        return value;
        // end Kuali Foundation modification
    }

    protected String interpolateSymbol(String target) {
        if (target.startsWith("-")) {
            int dashPos = target.indexOf('-');
            int symbolPos = target.indexOf(getSymbol());
            int index = (dashPos > symbolPos ? dashPos : symbolPos);
            return "($" + target.substring(index + 1) + ")";
        }
        return target.startsWith("(") ? "($" + target.indexOf("(" + 1) : "$" + target;
    }

    protected String removeSymbol(String target) {
        int index = target.indexOf(getSymbol());
        String prefix = (index > 0 ? target.substring(0, index) : "");
        return prefix + target.substring(index + 1);
    }

    protected String getSymbol() {
        return "$";
    }

    protected boolean showSymbol() {
        String showSymbol = (settings == null ? null : (String) settings.get(SHOW_SYMBOL));
        return Boolean.valueOf(showSymbol).booleanValue();
    }

    /**
     * Returns a string representation of its argument formatted as a currency value.
     * 
     * begin Kuali Foundation modification
     * @see org.kuali.rice.core.web.format.Formatter#format(java.lang.Object)
     * end Kuali Foundation modification
     */
    // begin Kuali Foundation modification
    @Override
    // end Kuali Foundation modification
    public Object format(Object obj) {
        // begin Kuali Foundation modification
        // major code rewrite, original code commented
        /*
        if (obj == null)
            return null;

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String string = null;

        try {
            BigDecimal number = (BigDecimal) obj;
            number = number.setScale(SCALE, BigDecimal.ROUND_HALF_UP);
            string = formatter.format(number.doubleValue());
        } catch (IllegalArgumentException e) {
            throw new FormatException(FORMAT_MSG + obj, e);
        } catch (ClassCastException e) {
            throw new FormatException(FORMAT_MSG + obj, e);
        }

        return showSymbol() ? string : removeSymbol(string);
        */
        LOG.debug("format '" + obj + "'");
        if (obj == null) {
            return null;
        }
        if (obj instanceof String
                && StringUtils.isEmpty((String)obj)) {
            return null;
        }
        

        NumberFormat formatter = getCurrencyInstanceUsingParseBigDecimal();
        String string = null;

        try {
            Number number;

            if (obj instanceof KualiInteger) {
                formatter.setMaximumFractionDigits(0);
                number = (KualiInteger) obj;

                // Note that treating the number as a KualiDecimal below is obsolete. But it doesn't do any harm either since
                // we already set maximumFractionDigits above.
            }
            else {
                number = (KualiDecimal) obj;
            }

            // run the incoming KualiDecimal's string representation through convertToObject, so that KualiDecimal objects
            // containing ill-formatted incoming values will cause the same errors here that ill-formatted Strings do in
            // convertToObject
            KualiDecimal convertedNumber = (KualiDecimal) convertToObject(number.toString());

            string = formatter.format(convertedNumber.bigDecimalValue());
        }
        catch (IllegalArgumentException e) {
            throw new FormatException("formatting", RiceKeyConstants.ERROR_CURRENCY, obj.toString(), e);
        }
        catch (ClassCastException e) {
            throw new FormatException("formatting", RiceKeyConstants.ERROR_CURRENCY, obj.toString(), e);
        }

        return showSymbol() ? string : removeSymbol(string);
        // end Kuali Foundation modification
    }

    // begin Kuali Foundation modification
    /**
     * retrieves a currency formatter instance and sets ParseBigDecimal to true to fix [KULEDOCS-742]
     * 
     * @return CurrencyInstance
     */
    static final NumberFormat getCurrencyInstanceUsingParseBigDecimal() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        if (formatter instanceof DecimalFormat) {
            ((DecimalFormat) formatter).setParseBigDecimal(true);
        }
        return formatter;
    }
    // end Kuali Foundation modification
    
    // begin Kuali Foundation modification
    /**
     * Validates a currency string by passing it into the convertToObject method and determining if conversion succeeded.
     * 
     * @param currencyString The string to attempt to format.
     * @return True if no exceptions occurred when parsing and the conversion returned a non-null value; false otherwise.
     * @see org.kuali.rice.core.web.format.CurrencyFormatter#convertToObject(java.lang.String)
     */
    public boolean validate(String currencyString) {
    	Object currencyObject = null;
    	try {
    		currencyObject = convertToObject(currencyString);
    	} catch (Exception e) {
    		currencyObject = null;
    	}
    	return (currencyObject != null);
    }
    // end Kuali Foundation modification
}
