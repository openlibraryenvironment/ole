
package org.kuali.ole.docstore.common.document.content.license.onixpl;

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
 * <p>Java class for UsageMethodCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="UsageMethodCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:ElectronicTransmission"/>
 *     &lt;enumeration value="onixPL:Fax"/>
 *     &lt;enumeration value="onixPL:Post"/>
 *     &lt;enumeration value="onixPL:PublicNetwork"/>
 *     &lt;enumeration value="onixPL:SecureAuthentication"/>
 *     &lt;enumeration value="onixPL:SecureElectronicTransmission"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "UsageMethodCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum UsageMethodCode {


    /**
     * Delivery of a digital copy by unrestricted electronic transmission.
     */
    @XmlEnumValue("onixPL:ElectronicTransmission")
    ONIX_PL_ELECTRONIC_TRANSMISSION("onixPL:ElectronicTransmission"),

    /**
     * Delivery of a paper copy by fax.
     */
    @XmlEnumValue("onixPL:Fax")
    ONIX_PL_FAX("onixPL:Fax"),

    /**
     * Delivery of a paper copy by postal service.
     */
    @XmlEnumValue("onixPL:Post")
    ONIX_PL_POST("onixPL:Post"),

    /**
     * Posting on a public network or networks.
     */
    @XmlEnumValue("onixPL:PublicNetwork")
    ONIX_PL_PUBLIC_NETWORK("onixPL:PublicNetwork"),

    /**
     * Secure authentication of Authorized Users as specified in a license.
     */
    @XmlEnumValue("onixPL:SecureAuthentication")
    ONIX_PL_SECURE_AUTHENTICATION("onixPL:SecureAuthentication"),

    /**
     * Delivery of a paper copy by a secure electronic transmission method such as Ariel.
     */
    @XmlEnumValue("onixPL:SecureElectronicTransmission")
    ONIX_PL_SECURE_ELECTRONIC_TRANSMISSION("onixPL:SecureElectronicTransmission");
    private final String value;

    UsageMethodCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UsageMethodCode fromValue(String v) {
        for (UsageMethodCode c : UsageMethodCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
