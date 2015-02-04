
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
 * <p>Java class for GeneralTermTimePointRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="GeneralTermTimePointRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:DeadlineForNotice"/>
 *     &lt;enumeration value="onixPL:EffectiveDateForVariation"/>
 *     &lt;enumeration value="onixPL:ExpiryDateForNotice"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "GeneralTermTimePointRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum GeneralTermTimePointRelatorCode {


    /**
     * A date on or before which a notice must be served.
     */
    @XmlEnumValue("onixPL:DeadlineForNotice")
    ONIX_PL_DEADLINE_FOR_NOTICE("onixPL:DeadlineForNotice"),

    /**
     * A date on which a variation to a license or to the licensed content becomes effective.
     */
    @XmlEnumValue("onixPL:EffectiveDateForVariation")
    ONIX_PL_EFFECTIVE_DATE_FOR_VARIATION("onixPL:EffectiveDateForVariation"),

    /**
     * A date on which a notice expires.
     */
    @XmlEnumValue("onixPL:ExpiryDateForNotice")
    ONIX_PL_EXPIRY_DATE_FOR_NOTICE("onixPL:ExpiryDateForNotice");
    private final String value;

    GeneralTermTimePointRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GeneralTermTimePointRelatorCode fromValue(String v) {
        for (GeneralTermTimePointRelatorCode c : GeneralTermTimePointRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
