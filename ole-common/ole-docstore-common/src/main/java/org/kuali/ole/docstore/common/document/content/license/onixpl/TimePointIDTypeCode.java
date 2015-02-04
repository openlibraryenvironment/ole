
package org.kuali.ole.docstore.common.document.content.license.onixpl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 5/30/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for TimePointIDTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="TimePointIDTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:YYYYMMDD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "TimePointIDTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum TimePointIDTypeCode {


    /**
     * A date according to the Gregorian Calendar expressed as year month day.
     */
    @XmlEnumValue("onixPL:YYYYMMDD")
    ONIX_PL_YYYYMMDD("onixPL:YYYYMMDD");
    private final String value;

    TimePointIDTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TimePointIDTypeCode fromValue(String v) {
        for (TimePointIDTypeCode c : TimePointIDTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
