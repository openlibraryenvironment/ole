
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
 * <p>Java class for SupplyTermQuantityTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="SupplyTermQuantityTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:GuaranteedUptime"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "SupplyTermQuantityTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum SupplyTermQuantityTypeCode {


    /**
     * The time during which access to licensed content on a host system is guaranteed to be available.
     */
    @XmlEnumValue("onixPL:GuaranteedUptime")
    ONIX_PL_GUARANTEED_UPTIME("onixPL:GuaranteedUptime");
    private final String value;

    SupplyTermQuantityTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SupplyTermQuantityTypeCode fromValue(String v) {
        for (SupplyTermQuantityTypeCode c : SupplyTermQuantityTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
