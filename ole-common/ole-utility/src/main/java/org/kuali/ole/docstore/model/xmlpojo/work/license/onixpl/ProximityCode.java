
package org.kuali.ole.docstore.model.xmlpojo.work.license.onixpl;

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
 * <p>Java class for ProximityCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="ProximityCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:Exactly"/>
 *     &lt;enumeration value="onixPL:LessThan"/>
 *     &lt;enumeration value="onixPL:MoreThan"/>
 *     &lt;enumeration value="onixPL:NotLessThan"/>
 *     &lt;enumeration value="onixPL:NotMoreThan"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "ProximityCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum ProximityCode {


    /**
     * The quantity is exactly the stated value.
     */
    @XmlEnumValue("onixPL:Exactly")
    ONIX_PL_EXACTLY("onixPL:Exactly"),

    /**
     * The quantity may be less than but not equal to the stated value.
     */
    @XmlEnumValue("onixPL:LessThan")
    ONIX_PL_LESS_THAN("onixPL:LessThan"),

    /**
     * The quantity may be greater than but not equal to the stated value.
     */
    @XmlEnumValue("onixPL:MoreThan")
    ONIX_PL_MORE_THAN("onixPL:MoreThan"),

    /**
     * The quantity may be greater than or equal to the stated value.
     */
    @XmlEnumValue("onixPL:NotLessThan")
    ONIX_PL_NOT_LESS_THAN("onixPL:NotLessThan"),

    /**
     * The quantity may be less than or equal to the stated value.
     */
    @XmlEnumValue("onixPL:NotMoreThan")
    ONIX_PL_NOT_MORE_THAN("onixPL:NotMoreThan");
    private final String value;

    ProximityCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ProximityCode fromValue(String v) {
        for (ProximityCode c : ProximityCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
