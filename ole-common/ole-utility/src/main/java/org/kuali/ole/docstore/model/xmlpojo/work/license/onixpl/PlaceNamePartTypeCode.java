
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
 * <p>Java class for PlaceNamePartTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="PlaceNamePartTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AddressLine"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "PlaceNamePartTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum PlaceNamePartTypeCode {


    /**
     * A line of an address entered as a series of separate lines.
     */
    @XmlEnumValue("onixPL:AddressLine")
    ONIX_PL_ADDRESS_LINE("onixPL:AddressLine");
    private final String value;

    PlaceNamePartTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PlaceNamePartTypeCode fromValue(String v) {
        for (PlaceNamePartTypeCode c : PlaceNamePartTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
