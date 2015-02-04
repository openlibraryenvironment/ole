
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
 * <p>Java class for UsageAgentRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="UsageAgentRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:ReceivingAgent"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "UsageAgentRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum UsageAgentRelatorCode {


    /**
     * The recipient of a resource that is delivered from a usage.
     */
    @XmlEnumValue("onixPL:ReceivingAgent")
    ONIX_PL_RECEIVING_AGENT("onixPL:ReceivingAgent");
    private final String value;

    UsageAgentRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UsageAgentRelatorCode fromValue(String v) {
        for (UsageAgentRelatorCode c : UsageAgentRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
