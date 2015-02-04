
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
 * <p>Java class for QuantityUnitCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="QuantityUnitCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:Copies"/>
 *     &lt;enumeration value="onixPL:Days"/>
 *     &lt;enumeration value="onixPL:Months"/>
 *     &lt;enumeration value="onixPL:Pages"/>
 *     &lt;enumeration value="onixPL:Percent"/>
 *     &lt;enumeration value="onixPL:Users"/>
 *     &lt;enumeration value="onixPL:Weeks"/>
 *     &lt;enumeration value="onixPL:WorkingDays"/>
 *     &lt;enumeration value="onixPL:Years"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "QuantityUnitCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum QuantityUnitCode {


    /**
     * Copies of a resource.
     */
    @XmlEnumValue("onixPL:Copies")
    ONIX_PL_COPIES("onixPL:Copies"),

    /**
     * Calendar days.
     */
    @XmlEnumValue("onixPL:Days")
    ONIX_PL_DAYS("onixPL:Days"),

    /**
     * Calendar months.
     */
    @XmlEnumValue("onixPL:Months")
    ONIX_PL_MONTHS("onixPL:Months"),

    /**
     * Pages of printed material or digital content presented in page form.
     */
    @XmlEnumValue("onixPL:Pages")
    ONIX_PL_PAGES("onixPL:Pages"),

    /**
     * Percentage.
     */
    @XmlEnumValue("onixPL:Percent")
    ONIX_PL_PERCENT("onixPL:Percent"),

    /**
     * Persons accessing or using licensed content.
     */
    @XmlEnumValue("onixPL:Users")
    ONIX_PL_USERS("onixPL:Users"),

    /**
     * Calendar weeks.
     */
    @XmlEnumValue("onixPL:Weeks")
    ONIX_PL_WEEKS("onixPL:Weeks"),

    /**
     * Working days as defined in a license, or by custom and practice in the place where the license is exercised.
     */
    @XmlEnumValue("onixPL:WorkingDays")
    ONIX_PL_WORKING_DAYS("onixPL:WorkingDays"),

    /**
     * Years as twelve-month periods from any start date.
     */
    @XmlEnumValue("onixPL:Years")
    ONIX_PL_YEARS("onixPL:Years");
    private final String value;

    QuantityUnitCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static QuantityUnitCode fromValue(String v) {
        for (QuantityUnitCode c : QuantityUnitCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
