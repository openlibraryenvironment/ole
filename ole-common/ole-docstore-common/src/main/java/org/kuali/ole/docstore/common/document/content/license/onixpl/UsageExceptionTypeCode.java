
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
 * <p>Java class for UsageExceptionTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="UsageExceptionTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:ExceptAsPermitted"/>
 *     &lt;enumeration value="onixPL:ExceptAsPermittedByStatute"/>
 *     &lt;enumeration value="onixPL:ExceptOnSecureNetwork"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "UsageExceptionTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum UsageExceptionTypeCode {


    /**
     * Except as explicitly permitted under the terms of a license.
     */
    @XmlEnumValue("onixPL:ExceptAsPermitted")
    ONIX_PL_EXCEPT_AS_PERMITTED("onixPL:ExceptAsPermitted"),

    /**
     * Except as permitted under applicable laws, eg fair use or fair dealing under copyright law.
     */
    @XmlEnumValue("onixPL:ExceptAsPermittedByStatute")
    ONIX_PL_EXCEPT_AS_PERMITTED_BY_STATUTE("onixPL:ExceptAsPermittedByStatute"),

    /**
     * Except on a secure network as defined in a license.
     */
    @XmlEnumValue("onixPL:ExceptOnSecureNetwork")
    ONIX_PL_EXCEPT_ON_SECURE_NETWORK("onixPL:ExceptOnSecureNetwork");
    private final String value;

    UsageExceptionTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UsageExceptionTypeCode fromValue(String v) {
        for (UsageExceptionTypeCode c : UsageExceptionTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
