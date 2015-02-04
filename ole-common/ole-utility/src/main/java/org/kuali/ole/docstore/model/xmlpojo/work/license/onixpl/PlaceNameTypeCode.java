
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
 * <p>Java class for PlaceNameTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="PlaceNameTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AddressAsFreeText"/>
 *     &lt;enumeration value="onixPL:AddressAsSeparateLines"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "PlaceNameTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum PlaceNameTypeCode {


    /**
     * An address entered as a single string of free text.
     */
    @XmlEnumValue("onixPL:AddressAsFreeText")
    ONIX_PL_ADDRESS_AS_FREE_TEXT("onixPL:AddressAsFreeText"),

    /**
     * An address entered with each line as a separate element.
     */
    @XmlEnumValue("onixPL:AddressAsSeparateLines")
    ONIX_PL_ADDRESS_AS_SEPARATE_LINES("onixPL:AddressAsSeparateLines");
    private final String value;

    PlaceNameTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PlaceNameTypeCode fromValue(String v) {
        for (PlaceNameTypeCode c : PlaceNameTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
