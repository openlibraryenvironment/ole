
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
 * <p>Java class for DocumentIDTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="DocumentIDTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:DOI"/>
 *     &lt;enumeration value="onixPL:LicenseeDocumentReference"/>
 *     &lt;enumeration value="onixPL:LicensorDocumentReference"/>
 *     &lt;enumeration value="onixPL:Proprietary"/>
 *     &lt;enumeration value="onixPL:URI"/>
 *     &lt;enumeration value="onixPL:URL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "DocumentIDTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum DocumentIDTypeCode {


    /**
     * Digital Object Identifier.
     */
    @XmlEnumValue("onixPL:DOI")
    ONIX_PL_DOI("onixPL:DOI"),

    /**
     * A reference number assigned to a document by a licensee.
     */
    @XmlEnumValue("onixPL:LicenseeDocumentReference")
    ONIX_PL_LICENSEE_DOCUMENT_REFERENCE("onixPL:LicenseeDocumentReference"),

    /**
     * A reference number assigned to a document by a licensor.
     */
    @XmlEnumValue("onixPL:LicensorDocumentReference")
    ONIX_PL_LICENSOR_DOCUMENT_REFERENCE("onixPL:LicensorDocumentReference"),

    /**
     * An identifier assigned under a proprietary scheme.
     */
    @XmlEnumValue("onixPL:Proprietary")
    ONIX_PL_PROPRIETARY("onixPL:Proprietary"),

    /**
     * Universal Resource Identifier.
     */
    @XmlEnumValue("onixPL:URI")
    ONIX_PL_URI("onixPL:URI"),

    /**
     * Universal Resource Locator.
     */
    @XmlEnumValue("onixPL:URL")
    ONIX_PL_URL("onixPL:URL");
    private final String value;

    DocumentIDTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DocumentIDTypeCode fromValue(String v) {
        for (DocumentIDTypeCode c : DocumentIDTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
