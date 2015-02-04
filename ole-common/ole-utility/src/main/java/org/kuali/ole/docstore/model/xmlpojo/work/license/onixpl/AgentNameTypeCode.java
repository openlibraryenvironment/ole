
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
 * <p>Java class for AgentNameTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="AgentNameTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:CommonName"/>
 *     &lt;enumeration value="onixPL:PositionInOrganization"/>
 *     &lt;enumeration value="onixPL:RegisteredName"/>
 *     &lt;enumeration value="onixPL:TradingName"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "AgentNameTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum AgentNameTypeCode {


    /**
     * A name by which an agent is commonly known.
     */
    @XmlEnumValue("onixPL:CommonName")
    ONIX_PL_COMMON_NAME("onixPL:CommonName"),

    /**
     * A name of a position which an agent holds within an organization.
     */
    @XmlEnumValue("onixPL:PositionInOrganization")
    ONIX_PL_POSITION_IN_ORGANIZATION("onixPL:PositionInOrganization"),

    /**
     * A name under which an agent is officially registered.
     */
    @XmlEnumValue("onixPL:RegisteredName")
    ONIX_PL_REGISTERED_NAME("onixPL:RegisteredName"),

    /**
     * A name under which an agent carries out business.
     */
    @XmlEnumValue("onixPL:TradingName")
    ONIX_PL_TRADING_NAME("onixPL:TradingName");
    private final String value;

    AgentNameTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AgentNameTypeCode fromValue(String v) {
        for (AgentNameTypeCode c : AgentNameTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
