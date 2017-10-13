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
package org.kuali.rice.core.web.format;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.RiceKeyConstants;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * This class is used to format BigDecimal objects.
 */
public class BigDecimalFormatter extends Formatter {
	
    private static final long serialVersionUID = 4628393689860734306L;
    
	private static Logger LOG = Logger.getLogger(BigDecimalFormatter.class);
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("\\-?[0-9,]*\\.?[0-9]*");

    /**
     * Unformats its argument and returns a KualiDecimal instance initialized with the resulting string value
     * 
     * @see org.kuali.rice.core.web.format.Formatter#convertToObject(java.lang.String)
     */
    protected Object convertToObject(String target) {
        BigDecimal value = null;

        LOG.debug("convertToObject '" + target + "'");

        if (target != null) {
 
            // preemptively detect non-numeric-related symbols, since NumberFormat.parse seems to be silently deleting them
            // (i.e. 9aaaaaaaaaaaaaaa is silently converted into 9)
            if (!DECIMAL_PATTERN.matcher(target).matches()) {
                throw new FormatException("parsing", RiceKeyConstants.ERROR_NUMERIC, target);
            }


            // actually reformat the numeric value
            DecimalFormat formatter = new DecimalFormat();
            formatter.setParseBigDecimal(true);
            try {
                Number parsedNumber = formatter.parse(target);
                value = new BigDecimal(parsedNumber.toString());
            }
            catch (NumberFormatException e) {
                throw new FormatException("parsing", RiceKeyConstants.ERROR_BIG_DECIMAL, target, e);
            }
            catch (ParseException e) {
                throw new FormatException("parsing", RiceKeyConstants.ERROR_BIG_DECIMAL, target, e);
            }
        }

        return value;
    }



    /**
     * Returns a string representation of its argument formatted as a decimal value.
     * 
     * @see org.kuali.rice.core.web.format.Formatter#format(java.lang.Object)
     */
    public Object format(Object obj) {
        LOG.debug("format '" + obj + "'");
        if (obj == null)
            return null;

        DecimalFormat formatter = new DecimalFormat();
        String string = null;

        try {
            BigDecimal number = (BigDecimal) obj;


            if(number!=null && number.scale()>0) {
                //remember to force a scale (with whatever rounding) in your java object to enforce this
                formatter.setMinimumFractionDigits(number.scale());
            } else {//arbitrary scale
                //according to the api this line shouldn't be needed for BigDecimal and it should be
                //able to do arbitrary precision, however it didn't work in my tests it appears there 
                //is an open java bug that relates to this sun bug (sun BUG:5060859) and that's why
                //we may need this workaround for now
                formatter.setMaximumFractionDigits(340);
            }
            string = formatter.format(number);
        }
        catch (IllegalArgumentException e) {
            throw new FormatException("formatting", RiceKeyConstants.ERROR_BIG_DECIMAL, obj.toString(), e);
        }
        catch (ClassCastException e) {
            throw new FormatException("formatting", RiceKeyConstants.ERROR_BIG_DECIMAL, obj.toString(), e);
        }

        return string;
    }
}
