
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
 * <p>Java class for SupplyTermTimePointRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="SupplyTermTimePointRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:SupplyStartDate"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "SupplyTermTimePointRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum SupplyTermTimePointRelatorCode {


    /**
     * The start date for a supply service.
     */
    @XmlEnumValue("onixPL:SupplyStartDate")
    ONIX_PL_SUPPLY_START_DATE("onixPL:SupplyStartDate");
    private final String value;

    SupplyTermTimePointRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SupplyTermTimePointRelatorCode fromValue(String v) {
        for (SupplyTermTimePointRelatorCode c : SupplyTermTimePointRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
