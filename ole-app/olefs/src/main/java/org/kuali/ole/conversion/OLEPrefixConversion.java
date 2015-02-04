package org.kuali.ole.conversion;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 4/11/14
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPrefixConversion implements FieldConversion {
    @Override
    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof String && ((String) source).contains("who")) {
           return ((String) source).substring(4);
        }
        return source;
    }

    @Override
    public Object sqlToJava(Object source) throws ConversionException {
        return source;
    }
}
