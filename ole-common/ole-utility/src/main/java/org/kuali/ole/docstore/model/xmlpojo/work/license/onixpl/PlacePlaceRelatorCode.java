
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
 * <p>Java class for PlacePlaceRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="PlacePlaceRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:IsAnyOf"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "PlacePlaceRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum PlacePlaceRelatorCode {


    /**
     * The place is any of the listed related places, or belongs to at least one of the related place classes.
     */
    @XmlEnumValue("onixPL:IsAnyOf")
    ONIX_PL_IS_ANY_OF("onixPL:IsAnyOf");
    private final String value;

    PlacePlaceRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PlacePlaceRelatorCode fromValue(String v) {
        for (PlacePlaceRelatorCode c : PlacePlaceRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
