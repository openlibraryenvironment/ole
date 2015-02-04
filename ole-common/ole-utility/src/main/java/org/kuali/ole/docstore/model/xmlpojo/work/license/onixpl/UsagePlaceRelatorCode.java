
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
 * <p>Java class for UsagePlaceRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="UsagePlaceRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:PlaceOfDeposit"/>
 *     &lt;enumeration value="onixPL:PlaceOfReceivingAgent"/>
 *     &lt;enumeration value="onixPL:PlaceOfUsage"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "UsagePlaceRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum UsagePlaceRelatorCode {


    /**
     * A place where a resource is deposited.
     */
    @XmlEnumValue("onixPL:PlaceOfDeposit")
    ONIX_PL_PLACE_OF_DEPOSIT("onixPL:PlaceOfDeposit"),

    /**
     * A place where a recipient of a resource that is delivered from a usage is located.
     */
    @XmlEnumValue("onixPL:PlaceOfReceivingAgent")
    ONIX_PL_PLACE_OF_RECEIVING_AGENT("onixPL:PlaceOfReceivingAgent"),

    /**
     * A place where a user is located when a usage occurs.
     */
    @XmlEnumValue("onixPL:PlaceOfUsage")
    ONIX_PL_PLACE_OF_USAGE("onixPL:PlaceOfUsage");
    private final String value;

    UsagePlaceRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UsagePlaceRelatorCode fromValue(String v) {
        for (UsagePlaceRelatorCode c : UsagePlaceRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
